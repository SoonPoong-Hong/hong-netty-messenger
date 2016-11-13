package rocklike.netty.messenger.common;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * <pre>
 * =
 * action
 * length:10
 * =
 * 1234567890
 * </pre>
 */
public class StrMsgEntity extends BaseRequestEntity<StrMsgEntity> {
	private String contents;
	private final Charset UTF_8 = Charset.forName("UTF-8");

	public String getContents() {
		return contents;
	}
	public StrMsgEntity setContents(String contentsStr) {
		this.contents = contentsStr;
		return this;
	}


	public StrMsgEntity setRefIdAndName(LoginUserInfo loginUserInfo) {
		this.setRefId(loginUserInfo.getId());
		this.setRefName(loginUserInfo.getName());
		return this;
	}

	@Override
	public ByteBuf formatToByteBuf() {
		if(this.getAction()==null){
			throw new IllegalArgumentException("action이 null임");
		}
		StringBuilder sbHeader = new StringBuilder();
		sbHeader.append("=").append(ByteBufHelper.STR_CRLF);

		// action
		sbHeader.append(this.getAction().code()).append(ByteBufHelper.STR_CRLF);

		// length
		int length = 0;
		ByteBuf contentsBuf = null;
		if(contents!=null && contents.length()>0){
			contentsBuf = ByteBufUtil.writeUtf8(PooledByteBufAllocator.DEFAULT, contents);
			length = contentsBuf.readableBytes();
		}
		sbHeader.append("length:" + length).append(ByteBufHelper.STR_CRLF);

		// 기타 헤더
		Map<String, String> headers = this.getHeaders();
		headers.entrySet().stream().filter(e->!"length".equals(e.getKey())).forEach(e->{
			sbHeader.append(e.getKey() + ":" + e.getValue()).append(ByteBufHelper.STR_CRLF);
		});
		// 헤더 끝
		sbHeader.append("=").append(ByteBufHelper.STR_CRLF);


		ByteBuf result = ByteBufUtil.writeUtf8(PooledByteBufAllocator.DEFAULT, sbHeader.toString());
		if(contentsBuf!=null){
			result.writeBytes(contentsBuf);
			contentsBuf.release();
		}

		return result;
	}

	@Override
	public String toString() {
		return "StrMsgEntity [action=" + getAction() + ", header=" + getHeaders() + ", contents=" + contents + "]";
	}


	public static class StrMsgEntityBuilder{
		private LoginUserInfo loginUserInfo;

		public StrMsgEntityBuilder(ChannelHandlerContext ctx) {
			loginUserInfo = LoginUserInfo.from(ctx);
		}
		public StrMsgEntityBuilder(Channel channel) {
			loginUserInfo = LoginUserInfo.from(channel);
		}
		public StrMsgEntityBuilder(LoginUserInfo loginUserInfo) {
			this.loginUserInfo = loginUserInfo;
		}
		public StrMsgEntityBuilder() {
			this.loginUserInfo = null;
		}

		private MsgAction action;
		private String contents;
		private Map<String, String> headers;

		public StrMsgEntityBuilder setAction(MsgAction action) {
			this.action = action;
			return this;
		}
		public StrMsgEntityBuilder setContents(String contents) {
			this.contents = contents;
			return this;
		}
		public StrMsgEntityBuilder setRefId(String refId) {
			setHeader("refId", refId);
			return this;
		}
		public StrMsgEntityBuilder setRefName(String refName) {
			setHeader("refName", refName);
			return this;
		}
		public StrMsgEntityBuilder setRoomName(String roomName) {
			setHeader("roomName", roomName);
			return this;
		}

		public StrMsgEntityBuilder setRefIdAndName(LoginUserInfo userInfo) {
			setRefId(userInfo.getId());
			setRefName(userInfo.getName());
			return this;
		}

		public StrMsgEntityBuilder setHeader(String key, String value) {
			if(headers==null){
				headers = new HashMap<>();
			}
			headers.put(key, value);
			return this;
		}

		public StrMsgEntity build(){
			StrMsgEntity e = new StrMsgEntity();
			e.setAction(action);
			e.setContents(contents);
			if(loginUserInfo!=null){
				e.setRefId(loginUserInfo.getId());
				e.setRefName(loginUserInfo.getName());
			}
			if(headers!=null){
				e.getHeaders().putAll(headers);
			}
			return e;
		}
	}

}
