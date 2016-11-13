package rocklike.netty.messenger.common;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class LoginUserInfo {
	private final String id;
	private final String name;
	private final String nickname;
	private final Channel channel;
//	private String roomName;

	public final String getId() {
		return id;
	}
	public final String getName() {
		return name;
	}
	public final String getNickname() {
		return nickname;
	}
	public final Channel getChannel() {
		return channel;
	}
	public LoginUserInfo(String id, String name, String nickname, Channel channel) {
		super();
		this.id = id;
		this.name = name;
		this.nickname = nickname;
		this.channel = channel;
	}

	public String desc(){
		return String.format("[%s] %s", id, name);
	}

	public static LoginUserInfo from(ChannelHandlerContext ctx){
		return AttachHelper.about(ctx).getLoginUserInfo();
	}
	
	public static LoginUserInfo from(Channel channel){
		return AttachHelper.about(channel).getLoginUserInfo();
	}
	
	public LoginUserInfo attachToChannel(Channel c){
		AttachHelper.about(c).attachLoginUserInfo(this);
		return this;
	}
	
//	public String getRoomName() {
//		return roomName;
//	}
//	
//	public LoginUserInfo setRoomName(String roomName) {
//		this.roomName = roomName;
//		return this;
//	}
	
	@Override
	public String toString() {
		return id;
	}

}