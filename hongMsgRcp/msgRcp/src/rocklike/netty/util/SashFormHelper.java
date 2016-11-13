package rocklike.netty.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

public class SashFormHelper {

	public static SashForm prepareVerticalSashForm(Composite parent, int sashWidth){
		SashForm sform = new SashForm(parent, SWT.VERTICAL );
		sform.SASH_WIDTH = sashWidth;
		return sform;
	}

	public static SashForm prepareHorizontalSashForm(Composite parent, int sashWidth){
		SashForm sform = new SashForm(parent, SWT.HORIZONTAL );
		sform.SASH_WIDTH = sashWidth;
		return sform;
	}



}
