package rocklike.test;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import rocklike.netty.messenger.common.IntHolder;

public class StyledText_fontColor2 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(shell);

		StyledText styledText = new StyledText(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL ); // | SWT.READ_ONLY
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(styledText);


		IntHolder h = new IntHolder(0);

		Button b = new Button(shell, SWT.PUSH);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(b);
		b.setText("추가.");
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String oldContents = styledText.getText();
				int length = oldContents.length();
				String str = String.valueOf(h.getAndIncrease() + 10000000) ;
				str = str + "\n";

				StyleRange styleRange = new StyleRange();
				styleRange.start = length;
				styleRange.length = str.length();
				styledText.append( str);
				if(h.get()%3==0){
					styleRange.foreground = display.getSystemColor(SWT.COLOR_RED);
				}else if(h.get()%3==1){
					styleRange.foreground = display.getSystemColor(SWT.COLOR_DARK_BLUE);
				}else{
					styleRange.foreground = display.getSystemColor(SWT.COLOR_DARK_CYAN);
				}
				styledText.setStyleRange(styleRange);
				styledText.setTopIndex(styledText.getLineCount() - 1);
			}
		});

		// read only로
		styledText.addVerifyKeyListener( (VerifyEvent e) -> {


//
//			if( (e.stateMask & SWT.CTRL) !=0){
//				System.out.println("stateMask");
//				return;
//			}
//			if(e.keyCode==SWT.CTRL){
////				System.out.println("control키가 눌렸다.");
//			}else{
//
//				e.doit = false;
//			}
		});

		styledText.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				int stateMask = e.stateMask;
				System.out.println("VerifyListener");

			}
		});


		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}