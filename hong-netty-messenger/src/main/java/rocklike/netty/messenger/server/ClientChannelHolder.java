package rocklike.netty.messenger.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.ReferenceCountUtil;
import rocklike.netty.messenger.common.JacksonHelper;
import rocklike.netty.messenger.common.LoginUserInfo;
import rocklike.netty.messenger.common.MsgAction;
import rocklike.netty.messenger.common.StrMsgEntity;
import rocklike.netty.messenger.common.StrMsgEntity.StrMsgEntityBuilder;

public class ClientChannelHolder {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private ClientChannelHolder() { }
	public static final ClientChannelHolder INSTANCE = new ClientChannelHolder();

	private ConcurrentMap<String, CopyOnWriteArrayList<Channel>> roomToChannels = new ConcurrentHashMap();
	private Map<Channel,String> channelToRoom = new ConcurrentHashMap();

	public List<String> getRoomList(){
		List<String> list = roomToChannels.keySet().stream().collect(Collectors.toList());
		return list;
	}

	public int getTotalClientCount(){
		return channelToRoom.size();
	}

	synchronized private OldAndNewRoomChannels updateChannelRoom(Channel ch, String roomName){
		// roomName이 null이면, logout으로 간주함.
		OldAndNewRoomChannels result = new OldAndNewRoomChannels();
		result.setNewRoomName(roomName);
		String oldRoomName = channelToRoom.get(ch);
		if(oldRoomName!=null && !oldRoomName.equals("") && roomToChannels.get(oldRoomName)==null){
			result.setRoomListChanged(true);
		}
		if(roomName!=null && !roomName.equals("") && roomToChannels.get(roomName)==null){
			result.setRoomListChanged(true);
		}
		result.setOldRoomName(oldRoomName);
		CopyOnWriteArrayList<Channel> oldRoomChannels = null;
		if(oldRoomName!=null){
			oldRoomChannels = roomToChannels.get(oldRoomName);
			if(oldRoomChannels==null){
				refreshAllRooms();
				result.setRoomListChanged(true);
				return result.setNews(roomToChannels.get(roomName));
			}
			if(!oldRoomChannels.contains(ch)){
				refreshAllRooms();
				result.setRoomListChanged(true);
				return result.setNews(roomToChannels.get(roomName));
			}
		}

		if(oldRoomChannels!=null){
			// channel을 지우기 전에 먼저 셋팅
			result.setOlds(new ArrayList(oldRoomChannels));
			oldRoomChannels.remove(ch);
			if(!Strings.isNullOrEmpty(oldRoomName) && roomToChannels.get(oldRoomName).size()==0){
				roomToChannels.remove(oldRoomName);
				result.setRoomListChanged(true);
			}
		}

		if(roomName!=null){
			channelToRoom.put(ch, roomName);
		}
		if(roomName!=null){
			CopyOnWriteArrayList<Channel> newRoomChannels = roomToChannels.get(roomName);
			if(newRoomChannels==null){
				newRoomChannels = new CopyOnWriteArrayList();
				roomToChannels.putIfAbsent(roomName, newRoomChannels);
				if(!"".equals(roomName)){
					result.setRoomListChanged(true);
				}
			}
			newRoomChannels.add(ch);
			result.setNews(newRoomChannels);
		}
		return result;
	}


	synchronized private void refreshAllRooms(){
		logger.info("== 전체 방에 대해서 refresh함.");
		// CopyOnWriteArrayList 이기 때문에, 미리 다 만들어 놓았다가 한번에 add를 하자
		ListMultimap<String, Channel> multimap = ArrayListMultimap.create();
		channelToRoom.forEach((ch, r)->{
			multimap.put(r, ch);
		});

		roomToChannels.clear();
		multimap.keySet().forEach(k->{
			List<Channel> list = multimap.get(k);
			CopyOnWriteArrayList<Channel> newList = new CopyOnWriteArrayList();
			newList.addAll(list);
			roomToChannels.put(k, newList);
		});
	}

	public boolean isIdExists(String id){
		Optional<Channel> optional = channelToRoom.keySet().stream().filter(c->LoginUserInfo.from(c).equals(id)).findFirst();
		return optional.isPresent();
	}

	public Iterator<Channel> getChannels(String roomName){
		CopyOnWriteArrayList<Channel> list = roomToChannels.get(roomName);
		if(list==null){
			return Collections.emptyIterator();
		}
		return list.iterator();
	}

	public List<String> getIds(Channel ch){
		String roomName = channelToRoom.get(ch);
		if(roomName!=null){
			return getIds(roomName);
		}else{
			return (List<String>)((List)Collections.emptyList());
		}
	}

	public List<String> getIds(String roomName){
		CopyOnWriteArrayList<Channel> list = roomToChannels.get(roomName);
		if(list==null){
			return (List<String>)((List)Collections.emptyList());
		}
		return list.stream().map(c->LoginUserInfo.from(c).getId()).collect(Collectors.toList());
	}

	public Iterator<Channel> getSameRoomChannels(Channel ch){
		String roomName = channelToRoom.get(ch);
		if(roomName==null){
			return Collections.emptyIterator();
		}
		CopyOnWriteArrayList<Channel> list = roomToChannels.get(roomName);
		if(list==null){
			return Collections.emptyIterator();
		}
		return list.iterator();
	}


	public void login(Channel ch){
		LoginUserInfo userInfo = LoginUserInfo.from(ch);
		logger.info("login : {}", userInfo);
		// 종료할때 방에서 나오기
		ch.closeFuture().addListener(f->{
			OldAndNewRoomChannels oldAndNew = updateChannelRoom(ch, null);
			List<Channel> olds = oldAndNew.getOlds();
			// 방사람들한테 전송
			if(olds!=null){
				StrMsgEntity msgEntity = new StrMsgEntityBuilder(userInfo).setAction(MsgAction.LogOut).build();
				writeAndFlush(olds, msgEntity);
//				olds.iterator().forEachRemaining(eachChannel->{
//					eachChannel.writeAndFlush(msgEntity);
//				});
			}
			channelToRoom.remove(ch);
			roomToChannels.values().forEach(list->{
				list.stream().filter(c->c!=null && c.equals(ch)).forEach(c->list.remove(c));
			});
		});

		// 기본방에 들어가기
		OldAndNewRoomChannels oldAndNew = updateChannelRoom(ch, "");
		List<Channel> news = oldAndNew.getNews();
		// 방사람들한테 전송
		if(news!=null){
			StrMsgEntity msgEntity = new StrMsgEntityBuilder(ch).setAction(MsgAction.Login).build();
			writeAndFlush(news, msgEntity);
//			news.forEach(eachChannel->{
//				eachChannel.writeAndFlush(msgEntity);
//			});

		}
	}


	public void enterToRoom(String roomName, Channel ch){
		OldAndNewRoomChannels oldAndNew = updateChannelRoom(ch, roomName);
		// 이전방에서 나오기
		List<Channel> olds = oldAndNew.getOlds();
		if(olds!=null){
			StrMsgEntity msgEntity = new StrMsgEntityBuilder(ch).setAction(MsgAction.ExitFromRoom).setRoomName(oldAndNew.getOldRoomName()).build();
			writeAndFlush(olds, msgEntity);
//			olds.forEach(eachChannel->{
//				eachChannel.writeAndFlush(msgEntity);
//			});
		}

		// 들어가기
		List<Channel> news = oldAndNew.getNews();
		if(news!=null){
			StrMsgEntity msgEntity = new StrMsgEntityBuilder(ch).setAction(MsgAction.EnterToRoom).setRoomName(roomName).build();
			writeAndFlush(news, msgEntity);
//			news.forEach(eachChannel->{
//				eachChannel.writeAndFlush(msgEntity);
//			});
		}

		// 방목록이 바뀌었다면
		if(oldAndNew.isRoomListChanged()){
			sendRoomList();
		}

	}

	private void sendRoomList(){
		List<String> roomList = getRoomList();
		String jsonStr = JacksonHelper.toJsonStr(roomList);
		StrMsgEntity output = new StrMsgEntityBuilder().setAction(MsgAction.RoomList).setContents(jsonStr).build();
		List<Channel> channels = Arrays.asList(channelToRoom.keySet().toArray(new Channel[0]));
		writeAndFlush(channels, output );
//		channelToRoom.keySet().forEach(c->{
//			c.writeAndFlush(output);
//		});
	}


	public void exitRoom(Channel ch){
		OldAndNewRoomChannels oldAndNew = updateChannelRoom(ch, "");
		List<Channel> olds = oldAndNew.getOlds();
		if(olds!=null){
			StrMsgEntity msgEntity = new StrMsgEntityBuilder(ch).setAction(MsgAction.ExitFromRoom).setRoomName(oldAndNew.getOldRoomName()).build();
			writeAndFlush(olds, msgEntity);
//			olds.forEach(eachChannel->{
//				eachChannel.writeAndFlush(msgEntity);
//			});
		}

		// 방목록이 바뀌었다면
		if(oldAndNew.isRoomListChanged()){
			sendRoomList();
		}
	}


	public void talkInTheRoom(Channel ch, String msg){
		Iterator<Channel> ite = ClientChannelHolder.INSTANCE.getSameRoomChannels(ch);
		StrMsgEntity msgEntity = new StrMsgEntityBuilder(ch).setAction(MsgAction.TalkMsg).setContents(msg).build();
		ByteBuf byteBuf = msgEntity.formatToByteBuf();
		try {
			ite.forEachRemaining(eachChannel->{
				eachChannel.writeAndFlush(byteBuf.duplicate().retain());
			});
		} finally {
			ReferenceCountUtil.release(byteBuf);
		}
	}


	private void writeAndFlush(List<Channel> channels, StrMsgEntity strMsg){
		if(channels==null){
			return;
		}
		ByteBuf byteBuf = strMsg.formatToByteBuf();
		channels.forEach(ch->{
			if(ch.isActive()){
				ch.writeAndFlush(byteBuf.duplicate().retain());
			}
		});
		ReferenceCountUtil.release(byteBuf);
	}

	class OldAndNewRoomChannels{
		private List<Channel> olds;
		private List<Channel> news;
		private String oldRoomName;
		private String newRoomName;
		private boolean isRoomListChanged;

		public OldAndNewRoomChannels setOlds(List<Channel> olds) {
			this.olds = olds;
			return this;
		}
		public OldAndNewRoomChannels setNews(List<Channel> news) {
			this.news = news;
			return this;
		}
		public List<Channel> getOlds() {
			return olds;
		}
		public List<Channel> getNews() {
			return news;
		}
		public String getOldRoomName() {
			return oldRoomName;
		}
		public OldAndNewRoomChannels setOldRoomName(String oldRoomName) {
			this.oldRoomName = oldRoomName;
			return this;
		}
		public String getNewRoomName() {
			return newRoomName;
		}
		public OldAndNewRoomChannels setNewRoomName(String newRoomName) {
			this.newRoomName = newRoomName;
			return this;
		}
		public boolean isRoomListChanged() {
			return isRoomListChanged;
		}
		public OldAndNewRoomChannels setRoomListChanged(boolean isRoomListChanged) {
			this.isRoomListChanged = isRoomListChanged;
			return this;
		}

	}

}

