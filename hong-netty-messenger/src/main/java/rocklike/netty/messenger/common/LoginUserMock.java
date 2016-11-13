package rocklike.netty.messenger.common;

import java.util.UUID;

public class LoginUserMock {

	private LoginUserMock(){ }

	public static LoginUserInfo get(){
		String seq = UUID.randomUUID().toString().substring(0,6);
		LoginUserInfo m = new LoginUserInfo(""+seq, "이름 " + seq, "닉 "+seq, null );
		return m;
	}
	
}
