package rocklike.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import rocklike.netty.util.SashFormHelper;
import rocklike.netty.util.SystemColor;

public class LayoutTestForTalk {
	Display display = new Display();
	Shell shell = new Shell(display);
	int cnt = 0;
	List<Widget> texts = new ArrayList();
	Button btn ;

	public LayoutTestForTalk() {
		shell.setLayout(new FillLayout());

		GridData gd = GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).create();

		Composite outer = new Composite(shell, SWT.BORDER);
		outer.setLayout(new FillLayout());
		SashForm sashForm = SashFormHelper.prepareVerticalSashForm(outer, 5);

		ScrolledComposite sc = new ScrolledComposite(sashForm, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setExpandVertical(false);
		sc.setExpandHorizontal(true);

		Composite displayContainer = new Composite(sc, SWT.BORDER  );
		sc.setContent(displayContainer);
//		sc.addListener(SWT.Resize, new Listener(){
//
//			@Override
//			public void handleEvent(Event event) {
////				System.out.println("width : " + sc.getSize().x);
////				btn.setSize(100, 20);
//				int width = btn.getParent().getBounds().width;
//				System.out.printf("button : %s , %s, boundsWidth:%s \n", displayContainer.getSize().x, btn.getSize().x, width);
////				texts.forEach(t->{
////					int maxWidth = sc.getSize().x;
////					int width = t.getSize().x;
////					if(width>maxWidth){
////						btn.setSize(maxWidth, btn.getSize().y);
////						System.out.printf("button \n");
//////						System.out.printf("%s , %s => %s \n", displayContainer.getSize().x,  width, maxWidth);
////						t.setSize(maxWidth, displayContainer.getSize().y);
////					}
////				});
//			}
//
//		});

		sc.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {

			}

			@Override
			public void controlMoved(ControlEvent e) {
			}
		});

		GridLayoutFactory.fillDefaults().margins(0, 0).spacing(0, 0).numColumns(1).applyTo(displayContainer);

		btn = new Button(displayContainer, SWT.PUSH);
		btn.setText("B1");
		GridDataFactory.createFrom(gd).applyTo(btn);
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cnt++;
				if(cnt%2==0){
					append_original(displayContainer, "홍길동", "0xxxxxxxxx ㅔㅔㅔㅔㅔㅔㅔ ㅊㅊㅊㅊㅊㅊㅊㅊㅊㅊㅊㅊㅊㅊ \nㅌㅌㅎㅎㅎ "+ cnt);
				}else{
					append_original(displayContainer, "나도 몰라요, 내가 누군지", "야이야이야 "+ cnt);
				}
				displayContainer.setSize(displayContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				btn.setText(String.valueOf(cnt));
			}
		});






		new Text(sashForm, SWT.BORDER);
		sashForm.setWeights(new int[] { 5, 1 });

		displayContainer.setSize(displayContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));

//		shell.pack();
		shell.open();
		shell.setSize(500, 600);

		// Set up the event loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// If no more entries in event queue
				display.sleep();
			}
		}

		display.dispose();
	}

	void append(Composite inner, String name, String msg){
		Text text = new Text(inner, SWT.BORDER | SWT.WRAP);
		String s = name + ":\n" + msg;
		text.setText(s);
		if(cnt%2==0){
			GridDataFactory.fillDefaults().align(SWT.END, SWT.BEGINNING).applyTo(text);
		}
		texts.add(text);
	}

	void append_original(Composite inner, String name, String msg){
		StyledText text = new StyledText(inner, SWT.BORDER | SWT.WRAP);
		String s = name + ":\n" + msg;
		text.setText(s);
		StyleRange nameStyle = new StyleRange();
		nameStyle.start = 0;
		nameStyle.length = (name+":").length();
		nameStyle.fontStyle = SWT.BOLD;
		nameStyle.foreground = SystemColor.blue();
		text.setStyleRange(nameStyle);
		if(cnt%2==0){
			GridDataFactory.fillDefaults().align(SWT.END, SWT.BEGINNING).grab(true, false).applyTo(text);
		}else{
			GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING).grab(true, false).applyTo(text);
		}
		texts.add(text);
	}

	private void init() {

	}

	public static void main(String[] args) {
		new LayoutTestForTalk();
	}
}
