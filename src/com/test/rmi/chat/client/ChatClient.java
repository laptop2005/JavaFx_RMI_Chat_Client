package com.test.rmi.chat.client;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javafx.scene.control.TextArea;

import com.test.rmi.chat.ChatClientInf;


public class ChatClient extends UnicastRemoteObject implements ChatClientInf {

	//채팅을 표시할 텍스트에어리어 - 실제로는 Chat.fxml에 있는 텍스트 에어리어의 주소값
	private TextArea textArea_chat;
	
	public ChatClient() throws RemoteException{
		
	}
	
	//채팅 화면 컨트롤러에서 텍스트 에어리어 주소값을 받아와 저장
	public void setTextArea_chat(TextArea textArea_chat){
		this.textArea_chat = textArea_chat;
	}

	//RMI에 등록된 리모트 오브젝트들 중 이 채팅 리모트 오브젝트를 제거한다
	//이렇게 해야 프로그램 종료 시 RMI도 종료되어 프로그램이 모두 정상종료 됨
	public void unexport(){
		try {
			UnicastRemoteObject.unexportObject(this, true);
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}
	}
	
	//텍스트 에어리어에 채팅 메시지를 추가
	@Override
	public void setMessage(String msg) throws RemoteException {
		if(this.textArea_chat!=null){
			this.textArea_chat.setText(this.textArea_chat.getText()+"\n"+msg);
		}
	}

	
}
