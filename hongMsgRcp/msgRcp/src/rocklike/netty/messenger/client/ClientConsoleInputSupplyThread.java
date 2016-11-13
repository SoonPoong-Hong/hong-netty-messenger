package rocklike.netty.messenger.client;

import java.util.Scanner;

import io.netty.channel.Channel;
import rocklike.netty.messenger.common.MsgAction;
import rocklike.netty.messenger.common.StrMsgEntity;
import rocklike.netty.messenger.common.StrMsgEntity.StrMsgEntityBuilder;

public class ClientConsoleInputSupplyThread extends Thread{
	final private Channel channel;
	
	public ClientConsoleInputSupplyThread(Channel channel) {
		this.channel = channel;
	}

	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			if(line.equals("bye")){
				channel.close();
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
			
			StrMsgEntity e = new StrMsgEntityBuilder(channel).setAction(action).setContents(sendContents).build();
			channel.writeAndFlush(e);
		}
	}
	

}
