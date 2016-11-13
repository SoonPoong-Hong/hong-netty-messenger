 
package rocklike.tycho.rcp.view;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class RcvContentsView {
	@Inject
	public RcvContentsView() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		Button b = new Button(parent, SWT.PUSH);
		b.setText("버튼입니다.");
	}
	
	
	
	
}