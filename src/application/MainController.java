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

	//������ �����ּ�
	@FXML private TextField textField_ip;
	//ä�ù� ����Ʈ
	@FXML private TableView<ChatVO> tableView_chatList;
	
	//ä�� ���� 
	private ChatServerInf server;
	
	//ä�ù� �˾��� ���� FXMLLoader
	private FXMLLoader loader;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//ä�ù� ����Ʈ �÷� "ä�ù���" �̶�� ǥ��
		TableColumn<ChatVO,String> col = new TableColumn<ChatVO,String>("ä�ù���");
		
		//�÷��� �����Ϳ� ǥ�õ� ������ ChatVO�� chatName���� ����
		col.setCellValueFactory(new PropertyValueFactory<ChatVO,String>("chatName"));
		
		//�÷��� �ּ� ũ�⸦ 300���� �����Ͽ� ���̺�信 ������ ǥ��
		col.setMinWidth(300);
		
		//������ ���̺� �÷��� ���̺�信 ����
		tableView_chatList.getColumns().add(col);
		
		//ä�ù� ����Ʈ�� ����Ŭ���ϸ� �߻��� �̺�Ʈ�� ����
		tableView_chatList.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//�̺�Ʈ�� ���콺 �ֹ�ư Ŭ���̰�, ���콺 Ŭ�� ���� �ι��̸� ����Ŭ���̶�� �Ǵ��ϵ��� ����
		        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
		        	//Ŭ���� ä�ù� ������ ��������
		        	ChatVO vo = tableView_chatList.getSelectionModel().getSelectedItem();
		        	
		        	//������ ä�ù� ������ ������ ä������ ��û�� ������.
		        	try {
		        		//ä�� Ŭ���̾�Ʈ�� �ű� �����Ͽ�
		        		ChatClient client = new ChatClient();
		        		
		        		//������ ä�� ���� ��û�� ����
						server.joinChat(vo.getChatName(), client);
						
						//ä�ù� �˾��� ���� ���� Chat.fxml�� �ε��Ͽ� ��
						loader = new FXMLLoader(getClass().getResource("Chat.fxml"));
						BorderPane pane = (BorderPane)loader.load();
						
						//ä��ȯ�� ���� ������ �ϱ� ���� loader�κ��� ��Ʈ�ѷ� ��ü�� �����´�.
						ChatController chatController = (ChatController)loader.getController();
						//��Ʈ�ѷ��� setChatEnv �޼��忡 �Ű������� ChatServer, ChatClient, ä�ù��̸��� ���� ä�ù� ȯ�漳��
						chatController.setChatEnv(server, client, vo.getChatName());
						
						//�� ����
						Scene scene = new Scene(pane,400,300);
						
						Stage stage = new Stage();
						//���������� ���� ���δ�
						stage.setScene(scene);
						
						//ä�ù� �˾��� ä�ù� ����Ʈ �����쿡 ���ӵǵ���(ä�ù� �˾��� ���� ������ ���ٵ��� �ʵ���)
						stage.initModality(Modality.APPLICATION_MODAL);
						
						//ä�ù��� ���� �� ä������ �޼��带 ȣ��ǵ��� �̺�Ʈ�� �����Ѵ�.
						stage.setOnCloseRequest((new javafx.event.EventHandler<WindowEvent>() {
							public void handle(WindowEvent we) {
								ChatController chatController = (ChatController)loader.getController();
								chatController.closeChat();
							}
						}));
						
						//���������� show �ϸ� ä�ù� �˾��� �����ȴ�.
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
	
	//���ӹ�ư Ŭ��
	@FXML
	public void onclickConnect(ActionEvent event){
		try {
			//�����ּ� �ؽ�Ʈ�ʵ忡 �Է��� IP�ּҷ� ����
			server = (ChatServerInf) Naming.lookup("rmi://" + textField_ip.getText() + "/chat");
			//���� �� ä�ù� ����Ʈ�� ����
			this.onclickRefresh(event);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	//���ΰ�ħ ��ư Ŭ��
	@FXML
	public void onclickRefresh(ActionEvent event){
		try {
			//�����κ��� ä�ø���Ʈ�� �����´�.
			List<String> chatList = server.getChatList();
			
			//�ܼ� String ����Ʈ�δ� ���̺���� ä�ù� ��Ͽ� ������ ǥ���� �� ����
			//ChatVO ��ü�� ���� �Ű� ��� ���ο� ����Ʈ�� �����.
			List<ChatVO> chatVOList = new ArrayList<>();
			for(String str:chatList){
				ChatVO chat = new ChatVO();
				chat.setChatName(str);
				chatVOList.add(chat);
			}
			
			//���̺�信 �����͸� �ֱ� ���ؼ��� ����Ʈ�� ����������Ʈ�� ��ȯ�Ͽ��� �Ѵ�.
			ObservableList<ChatVO> tableViewList = FXCollections.observableArrayList(chatVOList);
			
			//��ȯ�� ����������Ʈ�� ���̺�信 �����Ѵ�.
			tableView_chatList.setItems(tableViewList);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	//�� ä�ù� ��ư Ŭ��
	@FXML
	public void onclickMakeNewChat(ActionEvent event){
		//ä�ù� ���� �޽��� �˾��� ���� ���� �޽��� �˾��� ����� ���������� �����´�.
		Stage stage = (Stage)textField_ip.getScene().getWindow();
		
		//InputDialog�� ���� �Էµ� ä�ù� �̸��� �޴´�.
		String chatName = Dialogs.showInputDialog(stage, "���� ���� ä�ù� �̸��� �Է��ϼ���.");
		
		//���� ä�ù� �̸��� �Է����� �ʾҴٸ� �Է��� ������ �ٽ� �Է��϶�� �޽����� ����.
		while(chatName==null||chatName.trim().equals("")){
			chatName = Dialogs.showInputDialog(stage, "�߸� �Է��Ͽ����ϴ�. \n���� ���� ä�ù� �̸��� �Է��ϼ���.");
		}
		
		try {
			//������ ���ο� ä�ù� ���� ��û�� ������ ���ο� ä�ù��� �����Ǿ����� ���θ� �޴´�.
			boolean isMake = server.makeNewChat(chatName);
			
			//���� ä�ù��� �����Ǿ��ٸ� ���ο� ä�ù��� �����Ǿ��ٰ� �˸��� 
			//�������� �ʾҴٸ� �����Ͽ��ٴ� �޽����� ǥ���Ѵ�.
			if(isMake){
				Dialogs.showInformationDialog(stage, "���ο� ä�ù��� �����Ǿ����ϴ�.");
			}else{
				Dialogs.showWarningDialog(stage, "�̹� �����ϴ� ä�ù� �̸��Դϴ�.");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		//ä�ù��� ���� ���� �� ä�ù� ����Ʈ�� ���ΰ�ħ �Ѵ�.
		onclickRefresh(event);
	}

}
