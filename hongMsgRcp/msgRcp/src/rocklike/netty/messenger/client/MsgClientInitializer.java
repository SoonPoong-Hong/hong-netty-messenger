package rocklike.netty.messenger.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import rocklike.netty.messenger.common.MsgEntityEncoder;
import rocklike.netty.messenger.common.RequestDecoder;

public class MsgClientInitializer extends ChannelInitializer<SocketChannel> {

	private static MsgEntityEncoder msgEntityEncoder = new MsgEntityEncoder();
	private LoginRequest req ;


    public MsgClientInitializer(LoginRequest req) {
		this.req = req;
	}

	@Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(msgEntityEncoder);
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast(new MsgClientLoginHandler(req));
        pipeline.addLast("RequestEntityDecoder", new RequestDecoder());
        pipeline.addLast(new ClientMsgRcvHandler());
    }

}
