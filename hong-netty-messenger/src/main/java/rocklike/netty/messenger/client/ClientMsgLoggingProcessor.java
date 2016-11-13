package rocklike.netty.messenger.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import rocklike.netty.messenger.common.JacksonHelper;
import rocklike.netty.messenger.common.StrMsgEntity;

public class ClientMsgLoggingProcessor implements ClientMsgProcessor {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void whenLogOut(StrMsgEntity msg) {
		String refId = msg.getRefId();
		logger.info("'{}'님이 로그아웃했습니다.", refId);
	}

	@Override
	public void whenFail(ChannelHandlerContext ctx, StrMsgEntity msg) {
		logger.warn("[FAIL] " + msg.getContents() );

	}

	@Override
	public void whenLogin(StrMsgEntity entity) {
		String id = entity.getRefId();
		logger.info(String.format("'%s'님이 로그인 했습니다.", id));

	}

	@Override
	public void whenExitFromRoom(StrMsgEntity entity) {
		String id = entity.getRefId();
		logger.info("[{}] '{}'님이 방에서 퇴장..",entity.getRoomName(), id);
	}

	@Override
	public void whenEnteredToRoom(StrMsgEntity entity) {
		String id = entity.getRefId();
		logger.info("[{}] '{}'님이 방으로 들어왔습니다.",entity.getRoomName(), id);
	}



	@Override
	public void whenRoomlList(StrMsgEntity entity) {
		String[] rooms = JacksonHelper.toObj(entity.getContents(), String[].class);
		logger.info("방 목록입니다.");
		if(rooms!=null){
			for(String r:rooms){
				logger.info(r);
			}
		}
	}


	@Override
	public void whenInfo(StrMsgEntity entity){
		String contents = entity.getContents();
		logger.info("> {}", contents);
	}

	@Override
	public void whenTalkMsg(StrMsgEntity entity){
		String name = entity.getRefId();
		String contents = entity.getContents();

		logger.info("> [{}] : {}", name, contents);
	}

	@Override
	public void whenUserList(StrMsgEntity entity) {
		logger.info("> [{}] : {}", "사람들 목록", entity.getContents());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.warn("끊김.");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(cause.getMessage(), cause);
	}



}
