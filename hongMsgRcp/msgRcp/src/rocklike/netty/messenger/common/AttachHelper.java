package rocklike.netty.messenger.common;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

public class AttachHelper {
	private Channel channel;
	private AttachHelper(Channel channel){
		this.channel = channel;
	}

	public static AttachHelper about(Channel channel) {
		return new AttachHelper(channel);
	}

	public static AttachHelper about(ChannelHandlerContext ctx) {
		return new AttachHelper(ctx.channel());
	}

	public AttachHelper attach(String key, Object value) {
		AttributeKey attrKey = AttributeKey.valueOf(key);
		this.channel.attr(attrKey).set(value);
		return this;
	}
	private final String USER_INFO = "userInfo";
	public AttachHelper attachLoginUserInfo(LoginUserInfo userInfo){
		attach(USER_INFO, userInfo);
		return this;
	}
	
	public LoginUserInfo getLoginUserInfo(){
		return (LoginUserInfo)get(USER_INFO);
	}

	public Object get(String key){
		AttributeKey attrKey = AttributeKey.valueOf(key);
		return this.channel.attr(attrKey).get();
	}
}
