// Stub class generated by rmic, do not edit.
// Contents subject to change without notice.

package com.test.rmi.chat.client;

public final class ChatClient_Stub
    extends java.rmi.server.RemoteStub
    implements com.test.rmi.chat.ChatClientInf, java.rmi.Remote
{
    private static final long serialVersionUID = 2;
    
    private static java.lang.reflect.Method $method_setMessage_0;
    
    static {
	try {
	    $method_setMessage_0 = com.test.rmi.chat.ChatClientInf.class.getMethod("setMessage", new java.lang.Class[] {java.lang.String.class});
	} catch (java.lang.NoSuchMethodException e) {
	    throw new java.lang.NoSuchMethodError(
		"stub class initialization failed");
	}
    }
    
    // constructors
    public ChatClient_Stub(java.rmi.server.RemoteRef ref) {
	super(ref);
    }
    
    // methods from remote interfaces
    
    // implementation of setMessage(String)
    public void setMessage(java.lang.String $param_String_1)
	throws java.rmi.RemoteException
    {
	try {
	    ref.invoke(this, $method_setMessage_0, new java.lang.Object[] {$param_String_1}, -6512708111504182638L);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
}
