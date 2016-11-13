package rocklike.netty.messenger.client;

import io.netty.channel.Channel;

public class MyLoginInfo {
	private MyLoginInfo() { }
	public static final MyLoginInfo INSTANCE = new MyLoginInfo();

	private String id;
	private String name;
	private String nickname;
	private Channel channel;
	private String roomName;


	public String getId() {
		return id;
	}
	public MyLoginInfo setId(String id) {
		this.id = id;
		return this;
	}
	public String getName() {
		return name;
	}
	public MyLoginInfo setName(String name) {
		this.name = name;
		return this;
	}
	public String getNickname() {
		return nickname;
	}
	public MyLoginInfo setNickname(String nickname) {
		this.nickname = nickname;
		return this;
	}
	public Channel getChannel() {
		return channel;
	}
	public MyLoginInfo setChannel(Channel channel) {
		this.channel = channel;
		return this;
	}
	public String getRoomName() {
		return roomName;
	}
	public MyLoginInfo setRoomName(String roomName) {
		this.roomName = roomName;
		return this;
	}

	public boolean isActive(){
		return channel!=null && channel.isActive();
	}

	public boolean isNotActive(){
		return !isActive();
	}



}
