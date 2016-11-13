package rocklike.netty.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class SystemColor {

	public static Color blue(){
		return Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
	}
	public static Color red(){
		return Display.getDefault().getSystemColor(SWT.COLOR_RED);
	}
	public static Color yellow(){
		return Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
	}

	public static Color darkGreen(){
		return Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);
	}
}
