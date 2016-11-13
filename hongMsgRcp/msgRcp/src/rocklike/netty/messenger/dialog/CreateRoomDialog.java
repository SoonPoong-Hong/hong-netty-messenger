package rocklike.netty.messenger.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import rocklike.netty.util.MessagePopupHelper;

public class CreateRoomDialog extends Dialog{

	public CreateRoomDialog() {
		super(Display.getDefault().getActiveShell());
	}

	String roomName;
	Text roomText;



//	@Override
//	protected void configureShell(Shell newShell) {
//		super.configureShell(newShell);
//		newShell.setText("방을 만들거나, 들어가기");
//	}



	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialogParent = (Composite) super.createDialogArea(parent);
		Composite thisContainer = new Composite(dialogParent, SWT.NULL);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(thisContainer);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(thisContainer);

		Label l = new Label(thisContainer, SWT.NULL);
		l.setText("방명");
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).grab(false, true).applyTo(l);

		roomText = new Text(thisContainer, SWT.BORDER);

		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, true).applyTo(roomText);
		this.getShell().setText("방을 만들거나 들어가거나");
		return thisContainer;
	}



	@Override
	protected void okPressed() {
		this.roomName = roomText.getText().trim();
		if(roomName==null || roomName.length()==0){
			MessagePopupHelper.showErrMsg("방명이 비었습니다.");
			return;
		}
		super.okPressed();
	}



	public String getRoomName() {
		return roomName;
	}

}
