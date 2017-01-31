package com.test.rmi.chat.client;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javafx.scene.control.TextArea;

import com.test.rmi.chat.ChatClientInf;


public class ChatClient extends UnicastRemoteObject implements ChatClientInf {

	//ä���� ǥ���� �ؽ�Ʈ����� - �����δ� Chat.fxml�� �ִ� �ؽ�Ʈ ������� �ּҰ�
	private TextArea textArea_chat;
	
	public ChatClient() throws RemoteException{
		
	}
	
	//ä�� ȭ�� ��Ʈ�ѷ����� �ؽ�Ʈ ����� �ּҰ��� �޾ƿ� ����
	public void setTextArea_chat(TextArea textArea_chat){
		this.textArea_chat = textArea_chat;
	}

	//RMI�� ��ϵ� ����Ʈ ������Ʈ�� �� �� ä�� ����Ʈ ������Ʈ�� �����Ѵ�
	//�̷��� �ؾ� ���α׷� ���� �� RMI�� ����Ǿ� ���α׷��� ��� �������� ��
	public void unexport(){
		try {
			UnicastRemoteObject.unexportObject(this, true);
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}
	}
	
	//�ؽ�Ʈ ���� ä�� �޽����� �߰�
	@Override
	public void setMessage(String msg) throws RemoteException {
		if(this.textArea_chat!=null){
			this.textArea_chat.setText(this.textArea_chat.getText()+"\n"+msg);
		}
	}

	
}
