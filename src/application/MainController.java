package application;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Dialogs;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import com.test.rmi.chat.ChatServerInf;
import com.test.rmi.chat.client.ChatClient;

public class MainController implements Initializable{

	//접속할 서버주소
	@FXML private TextField textField_ip;
	//채팅방 리스트
	@FXML private TableView<ChatVO> tableView_chatList;
	
	//채팅 서버 
	private ChatServerInf server;
	
	//채팅방 팝업을 위한 FXMLLoader
	private FXMLLoader loader;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//채팅방 리스트 컬럼 "채팅방목록" 이라고 표시
		TableColumn<ChatVO,String> col = new TableColumn<ChatVO,String>("채팅방목록");
		
		//컬럼의 데이터에 표시될 내용을 ChatVO의 chatName으로 설정
		col.setCellValueFactory(new PropertyValueFactory<ChatVO,String>("chatName"));
		
		//컬럼의 최소 크기를 300으로 설정하여 테이블뷰에 꽉차게 표시
		col.setMinWidth(300);
		
		//설정한 테이블 컬럼을 테이블뷰에 붙임
		tableView_chatList.getColumns().add(col);
		
		//채팅방 리스트를 더블클릭하면 발생할 이벤트를 설정
		tableView_chatList.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//이벤트가 마우스 주버튼 클릭이고, 마우스 클릭 수가 두번이면 더블클릭이라고 판단하도록 설정
		        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
		        	//클릭한 채팅방 정보를 가져오고
		        	ChatVO vo = tableView_chatList.getSelectionModel().getSelectedItem();
		        	
		        	//가져온 채팅방 정보로 서버에 채팅참여 요청을 보낸다.
		        	try {
		        		//채팅 클라이언트를 신규 생성하여
		        		ChatClient client = new ChatClient();
		        		
		        		//서버에 채팅 참여 요청을 보냄
						server.joinChat(vo.getChatName(), client);
						
						//채팅방 팝업을 띄우기 위해 Chat.fxml을 로드하여 옴
						loader = new FXMLLoader(getClass().getResource("Chat.fxml"));
						BorderPane pane = (BorderPane)loader.load();
						
						//채팅환경 정보 설정을 하기 위해 loader로부터 컨트롤러 객체를 가져온다.
						ChatController chatController = (ChatController)loader.getController();
						//컨트롤러의 setChatEnv 메서드에 매개변수를 ChatServer, ChatClient, 채팅방이름을 보내 채팅방 환경설정
						chatController.setChatEnv(server, client, vo.getChatName());
						
						//씬 설정
						Scene scene = new Scene(pane,400,300);
						
						Stage stage = new Stage();
						//스테이지에 씬을 붙인다
						stage.setScene(scene);
						
						//채팅방 팝업을 채팅방 리스트 윈도우에 종속되도록(채팅방 팝업을 끄기 전까지 접근되지 않도록)
						stage.initModality(Modality.APPLICATION_MODAL);
						
						//채팅방이 꺼질 때 채팅종료 메서드를 호출되도록 이벤트를 설정한다.
						stage.setOnCloseRequest((new javafx.event.EventHandler<WindowEvent>() {
							public void handle(WindowEvent we) {
								ChatController chatController = (ChatController)loader.getController();
								chatController.closeChat();
							}
						}));
						
						//스테이지를 show 하면 채팅방 팝업이 생성된다.
						stage.show();
						
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
		        	
		        }
			}
		});
	}
	
	//접속버튼 클릭
	@FXML
	public void onclickConnect(ActionEvent event){
		try {
			//서버주소 텍스트필드에 입력한 IP주소로 접속
			server = (ChatServerInf) Naming.lookup("rmi://" + textField_ip.getText() + "/chat");
			//접속 후 채팅방 리스트를 갱신
			this.onclickRefresh(event);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	//새로고침 버튼 클릭
	@FXML
	public void onclickRefresh(ActionEvent event){
		try {
			//서버로부터 채팅리스트를 가져온다.
			List<String> chatList = server.getChatList();
			
			//단순 String 리스트로는 테이블뷰인 채팅방 목록에 내용을 표시할 수 없어
			//ChatVO 객체에 각각 옮겨 담아 새로운 리스트를 만든다.
			List<ChatVO> chatVOList = new ArrayList<>();
			for(String str:chatList){
				ChatVO chat = new ChatVO();
				chat.setChatName(str);
				chatVOList.add(chat);
			}
			
			//테이블뷰에 데이터를 넣기 위해서는 리스트를 옵저버블리스트로 변환하여야 한다.
			ObservableList<ChatVO> tableViewList = FXCollections.observableArrayList(chatVOList);
			
			//변환한 옵저저블리스트를 테이블뷰에 셋팅한다.
			tableView_chatList.setItems(tableViewList);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	//새 채팅방 버튼 클릭
	@FXML
	public void onclickMakeNewChat(ActionEvent event){
		//채팅방 관련 메시지 팝업을 띄우기 위해 메시지 팝업이 띄워질 스테이지를 가져온다.
		Stage stage = (Stage)textField_ip.getScene().getWindow();
		
		//InputDialog를 통해 입력된 채팅방 이름을 받는다.
		String chatName = Dialogs.showInputDialog(stage, "새로 만들 채팅방 이름을 입력하세요.");
		
		//만약 채팅방 이름을 입력하지 않았다면 입력할 때까지 다시 입력하라는 메시지를 띄운다.
		while(chatName==null||chatName.trim().equals("")){
			chatName = Dialogs.showInputDialog(stage, "잘못 입력하였습니다. \n새로 만들 채팅방 이름을 입력하세요.");
		}
		
		try {
			//서버에 새로운 채팅방 생성 요청을 보내고 새로운 채팅방이 생성되었는지 여부를 받는다.
			boolean isMake = server.makeNewChat(chatName);
			
			//새로 채팅방이 생성되었다면 새로운 채팅방이 개설되었다고 알리고 
			//생성되지 않았다면 실패하였다는 메시지를 표시한다.
			if(isMake){
				Dialogs.showInformationDialog(stage, "새로운 채팅방이 개설되었습니다.");
			}else{
				Dialogs.showWarningDialog(stage, "이미 존재하는 채팅방 이름입니다.");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		//채팅방을 새로 생성 후 채팅방 리스트를 새로고침 한다.
		onclickRefresh(event);
	}

}
