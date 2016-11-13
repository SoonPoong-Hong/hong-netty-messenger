package rocklike.netty.messenger.client;

import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import rocklike.netty.messenger.common.AttachHelper;
import rocklike.netty.messenger.common.LoginUserInfo;
import rocklike.netty.messenger.common.LoginUserMock;
import rocklike.netty.messenger.common.MsgAction;
import rocklike.netty.messenger.common.StrMsgEntity;
import rocklike.netty.messenger.common.StrMsgEntity.StrMsgEntityBuilder;

public class MsgClientLoginHandler extends ChannelInboundHandlerAdapter{

	private LoginRequest loginRequest;

	public MsgClientLoginHandler(LoginRequest loginRequest) {
		this.loginRequest = loginRequest;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// login 처리
		StrMsgEntity entity = new StrMsgEntityBuilder(ctx).setAction(MsgAction.Login)
				.setRefId(loginRequest.getId())
				.setRefName(loginRequest.getName())
				.setHeader("nickname", loginRequest.getNick())
				.setHeader("passwd", loginRequest.getPasswd())
				.build() ;

		ctx.writeAndFlush(entity.formatToByteBuf()).addListener(f->{
			MyLoginInfo.INSTANCE.setChannel(ctx.channel())
				.setId(loginRequest.getId())
				.setRoomName("")
				.setName(loginRequest.getName())
				.setNickname(loginRequest.getNick())
				;

			// 기본방에 대한 user list 요청
			StrMsgEntity strMsgEntity = new StrMsgEntityBuilder(ctx).setAction(MsgAction.UserList).setRoomName(MyLoginInfo.INSTANCE.getRoomName()).build();
			ctx.writeAndFlush(strMsgEntity);
		});

		// 종료될때 처리
		ctx.channel().closeFuture().addListener(f->{
			MyLoginInfo.INSTANCE.setChannel(null);
		});
		ctx.pipeline().remove(this);

		new ClientConsoleInputSupplyThread(ctx.channel()).start();
	}

}
