package rocklike.tycho.rcp;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import rocklike.netty.messenger.client.ClientMsgProcessorResolver;
import rocklike.netty.messenger.view.ClientMsgRcpProcessor;
import rocklike.netty.util.MessagePopupHelper;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("=========== bundle start()");

		Activator.context = bundleContext;
		ClientMsgProcessorResolver.INSTANCE.set(ClientMsgRcpProcessor.INSTANCE);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
