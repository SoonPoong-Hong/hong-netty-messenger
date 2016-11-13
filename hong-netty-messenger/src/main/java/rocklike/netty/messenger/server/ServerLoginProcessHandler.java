package rocklike.netty.messenger.server;

import java.net.InetSocketAddress;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import rocklike.netty.messenger.common.AttachHelper;
import rocklike.netty.messenger.common.ExceptionSender;
import rocklike.netty.messenger.common.JacksonHelper;
import rocklike.netty.messenger.common.LoginUserInfo;
import rocklike.netty.messenger.common.MsgAction;
import rocklike.netty.messenger.common.StrMsgEntity;
import rocklike.netty.messenger.common.StrMsgEntity.StrMsgEntityBuilder;

public class ServerLoginProcessHandler extends SimpleChannelInboundHandler<StrMsgEntity> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, StrMsgEntity msg) throws Exception {
		if(msg.getAction()!=MsgAction.Login){
			new ExceptionSender(ctx).send("로그인 요청이 아닙니다.("+msg.getAction()+")", true);
			return;
		}

		String id = msg.getRefId();
		String name = msg.getRefName();
		String nickname = msg.getHeaders().get("nickname");
		String passwd = msg.getHeaders().get("passwd");

		if(Strings.isNullOrEmpty(id)){
			new ExceptionSender(ctx).send("id가 비었습니다.", true);
		}

		if(ClientChannelHolder.INSTANCE.isIdExists(id)){
			new ExceptionSender(ctx).send("해당하는 id가 이미 로그인한 사용자가 있습니다. ("+id+")", true);
		}

		if(!"1111".equals(passwd)){
			new ExceptionSender(ctx).send("패스워드가 틀립니다. (패스워드는 무조건 '1111'로)", true);
			return;
		}

		LoginUserInfo loginInfo = new LoginUserInfo(id, name, nickname, ctx.channel());

		String host = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
	    int port = ((InetSocketAddress)ctx.channel().remoteAddress()).getPort();

		logger.warn("로그인 함 {} [{}] [{}]",new Object[]{ loginInfo.desc(), host, port});
		AttachHelper.about(ctx).attachLoginUserInfo(loginInfo);

		ClientChannelHolder.INSTANCE.login(ctx.channel());
		ctx.pipeline().remove(this);

		// 방 목록을 보내주기
		List<String> roomList = ClientChannelHolder.INSTANCE.getRoomList();
		String jsonStr = JacksonHelper.toJsonStr(roomList);
		StrMsgEntity msgEntity = new StrMsgEntityBuilder().setAction(MsgAction.RoomList).setContents(jsonStr).build();
		ctx.writeAndFlush(msgEntity);
	}

}