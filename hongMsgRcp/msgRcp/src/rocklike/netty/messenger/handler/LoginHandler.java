
package rocklike.netty.messenger.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.SWT;

import rocklike.netty.messenger.client.MsgClient;
import rocklike.netty.messenger.dialog.LoginPopupDialog;
import rocklike.netty.util.MessagePopupHelper;

public class LoginHandler {
	@Execute
	public void execute() throws Exception {
		LoginPopupDialog dialog = new LoginPopupDialog();
		int open = dialog.open();
		if(open==dialog.OK){
			try {
				new MsgClient().connect(dialog.getLoginRequest());
			} catch (Throwable e) {
				MessagePopupHelper.showErrMsg(e, "접속하지 못함.");
			}
		}
	}

}