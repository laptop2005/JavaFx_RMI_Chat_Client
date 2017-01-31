package com.test.rmi.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClientInf extends Remote{
	public void setMessage(String msg) throws RemoteException;
}
