package rocklike.netty.messenger.common;

public class MsgException extends RuntimeException {

	String code = "0000";

	public String getCode() {
		return code;
	}

	public MsgException setCode(String code) {
		this.code = code;
		return this;
	}

	public MsgException(String message) {
		super(message);
	}

	public MsgException(Throwable cause) {
		super(cause);
	}

	public MsgException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String toString() {
		return super.toString() + " [code=" + code + "] " ;
	}

	
	
}
