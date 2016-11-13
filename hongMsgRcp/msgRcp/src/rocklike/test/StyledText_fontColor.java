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

public class StyledText_fontColor {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(shell);

		StyledText styledText = new StyledText(shell, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL ); // | SWT.READ_ONLY
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(styledText);

		styledText.setText("01234\n"
				+ "01234\n"
				+ "01234\n"
				+ "01234\n"
				+ "01234\n"
				+ "01234\n"
				+ "01234\n"
				+ "01234\n"
				+ "01234\n"
				+ "01234 01234 01234 01234 01234 01234 0123401234 01234 01234 01234 01234 01234 0123401234\n"
				+ "");
		// make 0123456789 appear bold
		StyleRange style1 = new StyleRange();
		style1.start = 0;
		style1.length = 5;
		style1.fontStyle = SWT.BOLD;
		styledText.setStyleRange(style1);
		// make ABCDEFGHIJKLM have a red font
		StyleRange style2 = new StyleRange();
		style2.start = 5;
		style2.length = 5;
		style2.foreground = display.getSystemColor(SWT.COLOR_RED);
		styledText.setStyleRange(style2);
		// make NOPQRSTUVWXYZ have a blue background
		StyleRange style3 = new StyleRange();
		style3.start = 10;
		style3.length = 5;
		style3.background = display.getSystemColor(SWT.COLOR_BLUE);
		styledText.setStyleRange(style3);

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
				str = str + "  " + str;

				StyleRange styleRange = new StyleRange();
				styleRange.start = length+1;
				styleRange.length = str.length();
				styledText.append("\n" + str);
				if(h.get()%3==0){
					styleRange.foreground = display.getSystemColor(SWT.COLOR_RED);

				}else if(h.get()%3==1){
					styleRange.foreground = display.getSystemColor(SWT.COLOR_DARK_BLUE);

				}else{
//					styleRange.foreground = display.getSystemColor(SWT.COLOR_DARK_CYAN);

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