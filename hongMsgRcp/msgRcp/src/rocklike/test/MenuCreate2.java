package rocklike.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import rocklike.netty.util.MessagePopupHelper;


public class MenuCreate2 {
	public static void main(String[] args) {
		MenuCreate2 main = new MenuCreate2();
		main.execute();
	}

	void execute() {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());

		final Button bn = new Button(shell, SWT.FLAT);
		bn.setText("Right Click to see the popup menu");

//		Menu popupMenu = new Menu(bn);
//		MenuItem newItem = new MenuItem(popupMenu, SWT.CASCADE);
//		newItem.setText("New");
//		MenuItem refreshItem = new MenuItem(popupMenu, SWT.NONE);
//		refreshItem.setText("Refresh");
//		MenuItem deleteItem = new MenuItem(popupMenu, SWT.NONE);
//		deleteItem.setText("Delete");
//
//		Menu newMenu = new Menu(popupMenu);
//		newItem.setMenu(newMenu);
//
//		MenuItem shortcutItem = new MenuItem(newMenu, SWT.NONE);
//		shortcutItem.setText("Shortcut");
//		MenuItem iconItem = new MenuItem(newMenu, SWT.NONE);
//		iconItem.setText("Icon");
//
//		bn.setMenu(popupMenu);

		bn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				showPopupMenu2(bn);
			}

		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

	}

	// 아래가 직관적이고 추천함.
	void showPopupMenu2(Button btn) {
		Menu popupMenu = new Menu(btn);
		MenuItem item1 = new MenuItem(popupMenu, SWT.CASCADE);
		item1.setText("메뉴 1");
		MenuItem item2 = new MenuItem(popupMenu, SWT.NONE);
		item2.setText("메뉴 2");
		MenuItem item3 = new MenuItem(popupMenu, SWT.NONE);
		item3.setText("메뉴 3");
		item3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessagePopupHelper.showInfomsg("뭬냐.....");
			}
		});

		Menu submenu = new Menu(popupMenu); // => sub menu도 popupMenu로 만든다.
		item1.setMenu(submenu);

		MenuItem menu1_1 = new MenuItem(submenu, SWT.NONE);
		menu1_1.setText("메뉴 1-1");
		MenuItem menu1_2 = new MenuItem(submenu, SWT.NONE);
		menu1_2.setText("메뉴 1-2");

		Rectangle bounds = btn.getBounds();
		Point topLeft = new Point(bounds.x, bounds.y + bounds.height);
		topLeft = btn.toDisplay(topLeft);
		popupMenu.setLocation(topLeft.x, topLeft.y);
		popupMenu.setVisible(true);
	}
}
