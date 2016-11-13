
package rocklike.netty.messenger.handler;

import org.eclipse.e4.core.di.annotations.Execute;

import io.netty.channel.Channel;
import rocklike.netty.messenger.client.MyLoginInfo;
import rocklike.netty.util.MessagePopupHelper;

public class LogoutHandler {
	@Execute
	public void execute() {
		Channel channel = MyLoginInfo.INSTANCE.getChannel();
		if(channel==null || !channel.isActive()){
			MessagePopupHelper.showErrMsg("로그인 상태가 아닙니다.");
			return;
		}
		channel.close();
	}

}