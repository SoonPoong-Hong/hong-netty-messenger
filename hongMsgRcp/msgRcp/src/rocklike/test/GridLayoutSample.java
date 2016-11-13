package rocklike.test;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class GridLayoutSample {
	Display display = new Display();
	Shell shell = new Shell(display);
	int cnt = 0;
	public GridLayoutSample() {
		shell.setLayout(new FillLayout());
		Color red = display.getSystemColor(SWT.COLOR_RED);
		Color blue = display.getSystemColor(SWT.COLOR_BLUE);
		GridData gd = GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).create();

		ScrolledComposite sc = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setExpandVertical(false);
		sc.setExpandHorizontal(true);
		Composite comp = new Composite(sc, SWT.BORDER);
		sc.setContent(comp);
		GridLayoutFactory.fillDefaults().margins(0,0).spacing(0,0).numColumns(1).applyTo(comp);

		Button btn = null;
		btn = new Button(comp, SWT.PUSH);
		btn.setText("B1");
		GridDataFactory.createFrom(gd).applyTo(btn);
		btn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				for(int i=0; i<5; i++){
					cnt++;
					Button btn;
					btn = new Button(comp, SWT.PUSH);
					btn.setText("ㅎㅎㅎㅎㅎㅎㅎㅎ "+ cnt);
					GridDataFactory.createFrom(gd).applyTo(btn);
				}
				comp.setSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}

		});

		btn = new Button(comp, SWT.PUSH);
		btn.setText("Bsxxxxxx1");
		GridDataFactory.createFrom(gd).applyTo(btn);

		btn = new Button(comp, SWT.PUSH);
		btn.setText("Bㅎㅎㅎㅎㅎ1");
		GridDataFactory.createFrom(gd).applyTo(btn);

		btn = new Button(comp, SWT.PUSH);
		btn.setText("Bㅊㅊㅊㅊㅊㅊ1");
		GridDataFactory.createFrom(gd).applyTo(btn);

		btn = new Button(comp, SWT.PUSH);
		btn.setText("Bㅌㅌㅌㅌㅌㅌㅌㅌㅌ1");
		GridDataFactory.createFrom(gd).applyTo(btn);

		comp.setSize(comp.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		comp.requestLayout();


		shell.pack();
		shell.open();

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
		new GridLayoutSample();
	}
}
