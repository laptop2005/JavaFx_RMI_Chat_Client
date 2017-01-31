package application;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import com.test.rmi.chat.ChatServerInf;
import com.test.rmi.chat.client.ChatClient;

public class ChatController implements Initializable{

	//채팅방 이름
	@FXML private Label label_chat;
	//대화명
	@FXML private TextField textField_nickName;
	//대화내용을 입력할 곳
	@FXML private TextField textField_message;
	//대화내용이 표시될 곳
	@FXML private TextArea textArea_chat;
	
	//채팅방 이름
	private String chatName;
	//채팅 클라이언트
	private ChatClient client;
	//채팅 서버
	private ChatServerInf server;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	//메시지 전송 버튼을 클릭 시
	@FXML
	public void onclickSend(ActionEvent event){
		//서버가 null이 아니라면
		if(server!=null){
			try {
				//메시지에 닉네임을 붙여 채팅메시지를 만들고 
				String message = "["+textField_nickName.getText()+"]"+textField_message.getText();
				//채팅 서버로 해당 채팅방의 이름과 채팅메시지를 보낸다.
				server.setMessage(chatName, message);
				//메시지를 보낸 후 채팅입력창 내용을 지운다.
				textField_message.setText("");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	//채팅방 환경 정보를 설정
	public void setChatEnv(ChatServerInf server, ChatClient client, String chatName){
		this.server = server;
		this.client = client;
		this.chatName = chatName;
		this.client.setTextArea_chat(textArea_chat);
		this.label_chat.setText("채팅방이름:"+this.chatName);
	}
	
	//채팅을 종료
	public void closeChat(){
		try {
			server.exitChat(chatName, client);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		client.unexport();
	}
}
