package rocklike.netty.messenger.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import rocklike.netty.messenger.common.LoginUserInfo;
import rocklike.netty.messenger.common.LoginUserMock;
import rocklike.netty.messenger.common.MsgAction;
import rocklike.netty.messenger.common.StrMsgEntity;
import rocklike.netty.messenger.common.StrMsgEntity.StrMsgEntityBuilder;

public class MsgClientHeavyTester {

	public static void main(String[] args) throws Exception {
		MsgClientHeavyTester main = new MsgClientHeavyTester();
		String[] param = new String[]{"127.0.0.1", "80", "300", "10"};
		param = args;
		main.create(param);
	}

	void create(String[] args) throws InterruptedException{
		String ip ;
		int port;
		int cnt;
		int roomCnt;

		if(args.length<3){
			System.err.println("파라미터 순서 : IP, port, 만들 client갯수, 방 갯수");
			System.err.println("예) 127.0.0.1 80 300 20");
			return;
		}

		ip = args[0];
		port = Integer.parseInt(args[1]);
		cnt = Integer.parseInt(args[2]);
		roomCnt = Integer.parseInt(args[3]);

		MsgClient main = new MsgClient();
		for(int i=0; i<cnt; i++){
			String roomName = "Room " + i % roomCnt;
			System.out.printf("== client 띄움 (%s) \n", i);
			LoginUserInfo loginInfo = LoginUserMock.get();
			LoginRequest req = new LoginRequest();
			req.setId(loginInfo.getId());
			req.setName(loginInfo.getName());
			req.setNick(loginInfo.getNickname());
			req.setPasswd("1111");
			req.setServerIp(ip);
			req.setServerPort(port);
			Channel ch = main.connect(req);
			StrMsgEntity msgEntity = new StrMsgEntityBuilder(ch).setAction(MsgAction.EnterToRoom).setContents(roomName).build();
			ChannelFuture entered = ch.writeAndFlush(msgEntity);
			entered.addListener(f->{
				new Thread(){
					@Override
					public void run() {
						try {
							while(true){
								double r = Math.random();
								StrMsgEntity msg = new StrMsgEntityBuilder(ch).setAction(MsgAction.TalkMsg).setContents("메시지 " + r).build();
								ch.writeAndFlush(msg);
								try {
									Thread.sleep(1000 * (int)(r*100));
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
			});


		}

	}

}
