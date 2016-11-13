 
package rocklike.tycho.rcp.view;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class SendContentsView {
	@Inject
	public SendContentsView() {
		System.out.println("== View만들어졌냐..");
	}
	
	SourceViewer editor ;
	Button btn;
	@PostConstruct
	public void postConstruct(Composite parent) {
		GridLayoutFactory.fillDefaults().numColumns(1).margins(1,1).applyTo(parent);
		
		GridData gridData = GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).create();
		
		btn = new Button(parent, SWT.PUSH);
		btn.setText("Show the below text.");
		GridDataFactory.createFrom(gridData).grab(true, false).applyTo(btn);
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String text = editor.getTextWidget().getText();
				MessageDialog.openConfirm(parent.getShell(), "Hong", text);
			}
		});
		
		editor = new SourceViewer(parent, null, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		GridDataFactory.createFrom(gridData).applyTo(editor.getControl());
	}
	
}