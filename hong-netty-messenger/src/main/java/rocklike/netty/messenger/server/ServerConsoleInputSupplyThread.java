package rocklike.netty.messenger.server;

import java.util.Scanner;

import rocklike.netty.messenger.common.MsgAction;

/**
 * @deprecated 안씀. 나중에 지울것.
 */
public class ServerConsoleInputSupplyThread extends Thread{

	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			if(line.equals("bye")){
			
				break;
			}
			
			// Request_TalkMsg:잘 사냐
			int pos = line.indexOf(":");
			MsgAction action = MsgAction.TalkMsg;
			String sendContents = line;
			if(pos>-1){
				String actionStr = line.substring(0,pos);
				MsgAction parsed = MsgAction.decode(actionStr);
				if(parsed!=null){
					action = parsed;
					sendContents = line.substring(pos+1);
				}
			}
			
//			StrMsgEntity e = new StrMsgEntityBuilder(channel).setAction(action).setContents(sendContents).build();
//			channel.writeAndFlush(e);
		}
	}
	

}
