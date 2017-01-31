package com.test.rmi.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatServerInf extends Remote{
	public boolean makeNewChat(String chatName) throws RemoteException;
	public List<String> getChatList() throws RemoteException;
	public void joinChat(String chatName, ChatClientInf client) throws RemoteException;
	public void exitChat(String chatName, ChatClientInf client) throws RemoteException;
	public void setMessage(String chatName, String msg) throws RemoteException;
}
