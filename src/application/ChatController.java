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

	//ä�ù� �̸�
	@FXML private Label label_chat;
	//��ȭ��
	@FXML private TextField textField_nickName;
	//��ȭ������ �Է��� ��
	@FXML private TextField textField_message;
	//��ȭ������ ǥ�õ� ��
	@FXML private TextArea textArea_chat;
	
	//ä�ù� �̸�
	private String chatName;
	//ä�� Ŭ���̾�Ʈ
	private ChatClient client;
	//ä�� ����
	private ChatServerInf server;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

	//�޽��� ���� ��ư�� Ŭ�� ��
	@FXML
	public void onclickSend(ActionEvent event){
		//������ null�� �ƴ϶��
		if(server!=null){
			try {
				//�޽����� �г����� �ٿ� ä�ø޽����� ����� 
				String message = "["+textField_nickName.getText()+"]"+textField_message.getText();
				//ä�� ������ �ش� ä�ù��� �̸��� ä�ø޽����� ������.
				server.setMessage(chatName, message);
				//�޽����� ���� �� ä���Է�â ������ �����.
				textField_message.setText("");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	//ä�ù� ȯ�� ������ ����
	public void setChatEnv(ChatServerInf server, ChatClient client, String chatName){
		this.server = server;
		this.client = client;
		this.chatName = chatName;
		this.client.setTextArea_chat(textArea_chat);
		this.label_chat.setText("ä�ù��̸�:"+this.chatName);
	}
	
	//ä���� ����
	public void closeChat(){
		try {
			server.exitChat(chatName, client);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		client.unexport();
	}
}
