package rocklike.netty.messenger.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import rocklike.netty.messenger.common.StrMsgEntity;

public interface ClientMsgProcessor {
	final Logger logger = LoggerFactory.getLogger(ClientMsgProcessor.class);

	public void whenLogOut(StrMsgEntity msg);
	public void whenFail(ChannelHandlerContext ctx, StrMsgEntity msg) ;
	public void whenLogin(StrMsgEntity entity) ;
	public void whenExitFromRoom(StrMsgEntity entity);
	public void whenEnteredToRoom(StrMsgEntity entity) ;
	public void whenRoomlList(StrMsgEntity entity);
	public void whenInfo(StrMsgEntity entity);
	public void whenTalkMsg(StrMsgEntity entity);
	public void whenUserList(StrMsgEntity entity);
	default public void channelInactive(ChannelHandlerContext ctx) throws Exception {
	}

	default public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	}
}
