package rocklike.netty.messenger.server;

import java.net.SocketAddress;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import rocklike.netty.messenger.common.ExceptionSender;
import rocklike.netty.messenger.common.JacksonHelper;
import rocklike.netty.messenger.common.LoginUserInfo;
import rocklike.netty.messenger.common.MsgAction;
import rocklike.netty.messenger.common.StrMsgEntity;
import rocklike.netty.messenger.common.StrMsgEntity.StrMsgEntityBuilder;

public class MsgServerRcvHandler extends SimpleChannelInboundHandler<StrMsgEntity> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, StrMsgEntity entity) throws Exception {
		MsgAction action = entity.getAction();
		LoginUserInfo user = LoginUserInfo.from(ctx);
		switch (action) {
		case RoomList:
			sendRoomList(ctx);
			break;

		case EnterToRoom:
			enterToRoom(ctx, entity);
			break;
		case TalkMsg:
			logger.info("[talk]"+entity.getContents());
			ClientChannelHolder.INSTANCE.talkInTheRoom(ctx.channel(), entity.getContents());
			break;

		case LogOut:
			ctx.close();
			// channelInactive()에서 처리함.
			break;

		case ExitFromRoom:
			exitFromRoom(ctx, entity);
			break;
		case UserList:
			userList(ctx, entity);
			break;
		default:
			break;
		}
	}

	private void userList(ChannelHandlerContext ctx, StrMsgEntity entity) {
//		// 방 사람들 목록 보내주기
		List<String> ids = ClientChannelHolder.INSTANCE.getIds(ctx.channel());
		String jsonStr = JacksonHelper.toJsonStr(ids);
		StrMsgEntity msgEntity = new StrMsgEntityBuilder().setAction(MsgAction.UserList).setContents(jsonStr).build();
		ctx.writeAndFlush(msgEntity);

	}

	private void exitFromRoom(ChannelHandlerContext ctx, StrMsgEntity entity) {
		ClientChannelHolder.INSTANCE.exitRoom(ctx.channel());
	}

	private void enterToRoom(ChannelHandlerContext ctx, StrMsgEntity entity) {
		String roomName = entity.getContents();
		if(Strings.isNullOrEmpty(roomName)){
			new ExceptionSender(ctx).send("방명을 입력하지 않았습니다.", false);
			return;
		}
		ClientChannelHolder.INSTANCE.enterToRoom(roomName, ctx.channel());
	}

	void sendRoomList(ChannelHandlerContext ctx) throws JsonProcessingException{
		List<String> roomList = ClientChannelHolder.INSTANCE.getRoomList();
//		String[] rooms = ClientChannelGroup.INSTANCE.getRoomList().toArray(new String[0]);
		String jsonStr = JacksonHelper.toJsonStr(roomList);
		StrMsgEntity output = new StrMsgEntityBuilder(ctx).setAction(MsgAction.RoomList).setContents(jsonStr).build();
		ctx.writeAndFlush(output);
	}


	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LoginUserInfo loginUserInfo = LoginUserInfo.from(ctx);
		logger.info("{} : 로그아웃", loginUserInfo);
	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LoginUserInfo loginUserInfo = LoginUserInfo.from(ctx);
		SocketAddress remoteAddress = ctx.channel().remoteAddress();
		logger.error("[에러] remoteAddress : {}", remoteAddress);
		logger.error("[에러] id:"+loginUserInfo, cause);
	}


}
