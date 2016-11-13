package rocklike.netty.messenger.client;

public class ClientMsgProcessorResolver {
	private ClientMsgProcessorResolver() { }
	public static final ClientMsgProcessorResolver INSTANCE = new ClientMsgProcessorResolver();
	private ClientMsgProcessor processor = new ClientMsgLoggingProcessor();

	public ClientMsgProcessorResolver set(ClientMsgProcessor p){
		this.processor  = p;
		return this;
	}


	public ClientMsgProcessor get(){
		return this.processor;
	}

}
