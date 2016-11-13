package rocklike.netty.messenger.common;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class InputStringPrintHandler extends SimpleChannelInboundHandler<String> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private List<String> list = new ArrayList<>();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		logger.info("== READ : {}", msg);
	}

}
