package rocklike.netty.messenger.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DuplexChannel;
import rocklike.netty.messenger.common.AttachHelper;
import rocklike.netty.messenger.common.JacksonHelper;
import rocklike.netty.messenger.common.LoginUserInfo;
import rocklike.netty.messenger.common.MsgAction;
import rocklike.netty.messenger.common.StrMsgEntity;

public class ClientMsgRcvHandler extends SimpleChannelInboundHandler<StrMsgEntity>  {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	ClientMsgProcessor msgProcessor = ClientMsgProcessorResolver.INSTANCE.get();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, StrMsgEntity msg) throws Exception {
		MsgAction action = msg.getAction();

		switch (action) {
		case InfoMsg:
			msgProcessor.whenInfo(msg);
			break;
		case TalkMsg:
			msgProcessor.whenTalkMsg(msg);
			break;
		case RoomList:
			msgProcessor.whenRoomlList(msg);
			break;
		case ExitFromRoom:
			msgProcessor.whenExitFromRoom(msg);
			break;

		case EnterToRoom:
			msgProcessor.whenEnteredToRoom(msg);
			break;
		case Login:
			msgProcessor.whenLogin(msg);
			break;
		case LogOut:
			msgProcessor.whenLogOut(msg);
			break;
		case UserList:
			msgProcessor.whenUserList(msg);
			break;
		case Response_Fail:
			msgProcessor.whenFail(ctx, msg);
			break;
		default:
			break;
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.warn("끊어졌습니다.");
		msgProcessor.channelInactive(ctx);
//		ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("[에러]", cause);
		msgProcessor.exceptionCaught(ctx, cause);
	}




}
