package rocklike.netty.messenger.common;

public enum MsgAction {

	Login("Login", "로그인"), 
	LogOut("LogOut", "로그 아웃"), 
	EnterToRoom("EnterToRoom", "방 입장 요청"), 
	ExitFromRoom("ExitFromRoom", "방 퇴장 요청"), 
	RoomList("RoomList", "방 목록"), 
	TalkMsg("TalkMsg", "메시지 톡 전송"),
	InfoMsg("InfoMsg", "정보 메시지 전송"),
	UserList("UserList", "방 사람들 목록"),

	Response_Success("Response_Success", "성공"), 
	Response_Fail("Response_Fail", "실패"),
//	Response_RoomList("Response_RoomList", "방 목록"), 
//	Response_EnteredToRoom("Response_EnteredToRoom", "방에 입장"), 
//	Response_ExitFromRoom("Response_ExitFromRoom", "방에서 퇴장"), 
	;

	final private String code;
	final private String desc;

	private MsgAction(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String code() {
		return code;
	}

	static public MsgAction decode(String code) {
		for (MsgAction a : MsgAction.values()) {
			if (a.code.equals(code)) {
				return a;
			}
		}
		return null;
	}

}
