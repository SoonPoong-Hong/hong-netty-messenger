 
package rocklike.tycho.rcp.view;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class MyView {
	@Inject
	public MyView() {
		
	}
	
	Button btn;
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		btn = new Button(parent, SWT.PUSH );
		btn.setText("버튼입니다.");
	}
	
	
	
	
}