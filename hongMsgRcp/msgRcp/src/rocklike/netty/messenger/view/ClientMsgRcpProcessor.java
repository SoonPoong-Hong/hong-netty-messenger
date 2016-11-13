package rocklike.netty.messenger.view;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import rocklike.netty.messenger.client.ClientMsgProcessor;
import rocklike.netty.messenger.common.StrMsgEntity;
import rocklike.netty.util.MessagePopupHelper;

public class ClientMsgRcpProcessor implements ClientMsgProcessor {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private ClientMsgRcpProcessor() { }
	public static ClientMsgRcpProcessor INSTANCE = new ClientMsgRcpProcessor();

	private List<ClientMsgProcessor> listeners = new ArrayList(3);
	synchronized public ClientMsgRcpProcessor addListener(ClientMsgProcessor p){
		if(p!=null && !listeners.contains(p)){
			listeners.add(p);
		}
		return this;
	}

	synchronized public ClientMsgRcpProcessor removeListener(ClientMsgProcessor p){
		if(p!=null){
			listeners.remove(p);
		}
		return this;
	}

	@Override
	public void whenLogOut(StrMsgEntity msg) {
		listeners.forEach(p->p.whenLogOut(msg));
	}

	@Override
	public void whenFail(ChannelHandlerContext ctx, StrMsgEntity msg) {
		listeners.forEach(p->p.whenFail(ctx, msg));
	}

	@Override
	public void whenLogin(StrMsgEntity entity) {
		listeners.forEach(p->p.whenLogin(entity));
	}

	@Override
	public void whenExitFromRoom(StrMsgEntity entity) {
		listeners.forEach(p->p.whenExitFromRoom(entity));
	}

	@Override
	public void whenEnteredToRoom(StrMsgEntity entity) {
		listeners.forEach(p->p.whenEnteredToRoom(entity));
	}

	@Override
	public void whenRoomlList(StrMsgEntity entity) {
		listeners.forEach(p->p.whenRoomlList(entity));
	}

	@Override
	public void whenInfo(StrMsgEntity entity) {
		listeners.forEach(p->p.whenInfo(entity));
	}

	@Override
	public void whenTalkMsg(StrMsgEntity entity) {
		listeners.forEach(p->p.whenTalkMsg(entity));
	}

	@Override
	public void whenUserList(StrMsgEntity entity) {
		listeners.forEach(p->p.whenUserList(entity));
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.warn("연결이 끊김.");
		MessagePopupHelper.showInfomsg("연결이 끊겼습니다.");
		listeners.forEach(p->{
			try {
				p.channelInactive(ctx);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		});
		ClientMsgProcessor.super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(cause.getMessage(), cause);
		MessagePopupHelper.showErrMsg(cause, "에러 발생 : " + cause.getMessage());
		ctx.fireExceptionCaught(cause);
	}

}
