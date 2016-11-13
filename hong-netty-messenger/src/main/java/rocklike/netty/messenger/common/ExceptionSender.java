package rocklike.netty.messenger.common;

import io.netty.channel.ChannelHandlerContext;
import rocklike.netty.messenger.common.StrMsgEntity.StrMsgEntityBuilder;

public class ExceptionSender {
	private ChannelHandlerContext ctx;

	public ExceptionSender(ChannelHandlerContext ctx) {
		super();
		this.ctx = ctx;
	}

	public void send(String msg, boolean closeChannel){
		StrMsgEntity msgEntity = new StrMsgEntityBuilder(ctx).setAction(MsgAction.Response_Fail).setContents(msg) .build();
		ctx.writeAndFlush(msgEntity).addListener(f -> {
			if (closeChannel){ ctx.close(); }
		});
	}
	
	
	public void send(Throwable th, boolean closeChannel) {
		if (th instanceof MsgException) {
			MsgException ex = (MsgException) th;
			String msg = ex.getMessage() + "[" + ex.getCode() + "]";
			StrMsgEntity msgEntity = new StrMsgEntityBuilder(ctx).setAction(MsgAction.Response_Fail).setContents(msg) .build();
			ctx.writeAndFlush(msgEntity).addListener(f -> {
				if (closeChannel){ ctx.close(); }
			});
		} else {
			String msg = "[ERROR] " + th.getMessage();
			StrMsgEntity msgEntity = new StrMsgEntityBuilder(ctx).setAction(MsgAction.Response_Fail).setContents(msg).build();
			ctx.writeAndFlush(msgEntity).addListener(f -> {
				if (closeChannel){ ctx.close(); }
			});
		}

	}

}
