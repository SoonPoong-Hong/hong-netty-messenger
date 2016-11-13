package rocklike.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class FillLayoutSample {
	Display display = new Display();
	Shell shell = new Shell(display);

	public FillLayoutSample() {
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		fillLayout.marginHeight = 5;
		fillLayout.marginWidth = 5;
		fillLayout.spacing = 1;

		shell.setLayout(fillLayout);

		Button button1 = new Button(shell, SWT.PUSH);
		button1.setText("button1");

		Button button2 = new Button(shell, SWT.PUSH);
		button2.setText("button number 2");

		Button button3 = new Button(shell, SWT.PUSH);
		button3.setText("3");

		shell.pack();
		shell.open();
		// textUser.forceFocus();

		// Set up the event loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// If no more entries in event queue
				display.sleep();
			}
		}

		display.dispose();
	}

	private void init() {

	}

	public static void main(String[] args) {
		new FillLayoutSample();
	}
}
