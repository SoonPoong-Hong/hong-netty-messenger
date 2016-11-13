package rocklike.netty.util;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;


/**
 *
 * @author 홍순풍 (rocklike@gmail.com)
 * @created 2009. 9. 4.
 */
public class MessagePopupHelper {

	static public void showErrMsg(final String msg) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openError(Display.getDefault().getActiveShell(), "에러", msg);
			}
		});
	}

	static public void showErrMsg(final Throwable th) {
		th.printStackTrace();
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openError(Display.getDefault().getActiveShell(), "에러", th.toString());
			}
		});
	}

	static public void showErrMsg(final Throwable th, final String msg) {
		th.printStackTrace();
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openError(Display.getDefault().getActiveShell(), "에러", msg + "\n\n"
				        + "에러내용 : " + th.toString());
			}
		});
	}

	static public void showInfomsg(final String msg) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "홍", msg);
			}
		});
	}
}
