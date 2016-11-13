package rocklike.test;

import org.eclipse.jface.layout.RowDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class RowLayoutSample {
	Display display = new Display();
	Shell shell = new Shell(display);

	public RowLayoutSample() {
		RowLayout rowLayout = new RowLayout();
		rowLayout.wrap = false;  // 다음줄로 wrapping되지 않게
		rowLayout.pack = true;  // 오그라들지 않게
		rowLayout.justify = false;
		rowLayout.type = SWT.VERTICAL;
//		rowLayout.marginLeft = 1;
//		rowLayout.marginTop = 1;
//		rowLayout.marginRight = 1;
//		rowLayout.marginBottom = 1;
		rowLayout.spacing = 0;

		shell.setLayout(rowLayout);

		Button button1 = new Button(shell, SWT.PUSH);
		button1.setText("button1");
//		button1.setLayoutData(new RowData(100, 35));

		List list = new List(shell, SWT.BORDER);
		list.add("item 1");
		list.add("item 2");
		list.add("item 3");
		list.add("item 3");
		list.add("item 3");
		list.add("item 3");
		list.add("item 3");

		Button button2 = new Button(shell, SWT.PUSH);
		button2.setText("button #2");

		for(int i=0; i<3; i++){
			Button btn = new Button(shell, SWT.PUSH);
			btn.setText("버튼입니다.ㅎㅎㅎㅎㅎㅎㅎ" + i);
		}

		// shell.setSize(120, 120);
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
		new RowLayoutSample();
	}
}
