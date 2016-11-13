package rocklike.netty.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Widget;

public class SwtHelper {
	public static void setReadOnly(Widget w){
		w.addListener(SWT.Verify, e->e.doit=false);
	}
}
