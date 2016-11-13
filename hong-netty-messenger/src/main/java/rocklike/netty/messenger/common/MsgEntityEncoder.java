package rocklike.netty.messenger.common;

import java.util.List;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

@Sharable
public class MsgEntityEncoder extends MessageToMessageEncoder<StrMsgEntity> {

	@Override
	protected void encode(ChannelHandlerContext ctx, StrMsgEntity msg, List<Object> out) throws Exception {
		if(msg!=null){
			out.add(msg.formatToByteBuf());
		}
	}


}
