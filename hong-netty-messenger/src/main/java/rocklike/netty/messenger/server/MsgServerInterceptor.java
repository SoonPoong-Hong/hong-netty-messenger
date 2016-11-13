package rocklike.netty.messenger.server;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class MsgServerInterceptor extends ChannelDuplexHandler {

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
		// TODO Auto-generated method stub
		super.disconnect(ctx, future);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
	}

	
}
