package rocklike.netty.messenger.dialog;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;

import rocklike.netty.messenger.client.LoginRequest;
import rocklike.netty.util.MessagePopupHelper;
import rocklike.netty.util.SystemColor;

public class LoginPopupDialog extends Dialog{

	public LoginPopupDialog() {
		super(Display.getDefault().getActiveShell());
	}
	private Text serverIp;
	private Text serverPort;
	private Text id;
	private Text passwd;
	private Text name;
	private Text nick;

	private LoginRequest loginRequest ;
	private final String PREF_NODE = "rocklike.messenger.client";

	@Override
	protected Control createDialogArea(Composite parent) {
		IEclipsePreferences pref = ConfigurationScope.INSTANCE.getNode(PREF_NODE);

		Composite dialogParent = (Composite) super.createDialogArea(parent);
		Composite thisContainer = new Composite(dialogParent, SWT.NULL);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(thisContainer);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(thisContainer);
		GridData labelGd = GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).create();
		GridData textGd = GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).create();

		Label l;

		l = new Label(thisContainer, SWT.NULL);
		l.setText("서버 IP");
		GridDataFactory.createFrom(labelGd).applyTo(l);

		serverIp = new Text(thisContainer, SWT.BORDER);
		serverIp.setText(pref.get("serverIp", "127.0.0.1"));
		GridDataFactory.createFrom(textGd).applyTo(serverIp);

		l = new Label(thisContainer, SWT.NULL);
		l.setText("서버 Port");
		GridDataFactory.createFrom(labelGd).applyTo(l);

		serverPort = new Text(thisContainer, SWT.BORDER);
		serverPort.setText(pref.get("serverPort", "80"));
		GridDataFactory.createFrom(textGd).applyTo(serverPort);

		l = new Label(thisContainer, SWT.NULL);
		l.setText("ID");
		GridDataFactory.createFrom(labelGd).applyTo(l);

		id = new Text(thisContainer, SWT.BORDER);
		id.setText(pref.get("id", ""));
		GridDataFactory.createFrom(textGd).applyTo(id);

		l = new Label(thisContainer, SWT.NULL);
		l.setText("패스워드");
		GridDataFactory.createFrom(labelGd).applyTo(l);

		passwd = new Text(thisContainer, SWT.BORDER);
		passwd.setText(pref.get("passwd", "1111"));
		GridDataFactory.createFrom(textGd).applyTo(passwd);

		l = new Label(thisContainer, SWT.NULL);
		l.setText("이름");
		GridDataFactory.createFrom(labelGd).applyTo(l);

		name = new Text(thisContainer, SWT.BORDER);
		name.setText(pref.get("name", ""));
		GridDataFactory.createFrom(textGd).applyTo(name);

		l = new Label(thisContainer, SWT.NULL);
		l.setText("nick");
		GridDataFactory.createFrom(labelGd).applyTo(l);

		nick = new Text(thisContainer, SWT.BORDER);
		nick.setText(pref.get("nick", ""));
		GridDataFactory.createFrom(textGd).applyTo(nick);

		return thisContainer;
	}

	@Override
	protected void okPressed() {
		IEclipsePreferences pref = ConfigurationScope.INSTANCE.getNode(PREF_NODE);
		loginRequest = new LoginRequest();
		loginRequest.setServerIp(serverIp.getText());
		pref.put("serverIp", loginRequest.getServerIp() );
		loginRequest.setServerPort(Integer.parseInt(serverPort.getText()));
		pref.put("serverPort", serverPort.getText());
		loginRequest.setId(id.getText());
		pref.put("id", loginRequest.getId() );
		loginRequest.setPasswd(passwd.getText());
		pref.put("passwd", loginRequest.getPasswd() );
		loginRequest.setName(name.getText());
		pref.put("name", loginRequest.getName() );
		loginRequest.setNick(nick.getText());
		pref.put("nick", loginRequest.getNick() );
		try {
			pref.flush();
		} catch (BackingStoreException e) {
			throw new RuntimeException(e);
		}

		super.okPressed();
	}

	@Override
	protected Point getInitialSize() {
		return new Point(400, 400);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		super.configureShell(newShell);
	}


	public LoginRequest getLoginRequest() {
		return loginRequest;
	}

}
