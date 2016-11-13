
package rocklike.netty.messenger.view;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import rocklike.netty.messenger.client.ClientMsgProcessor;
import rocklike.netty.messenger.client.MyLoginInfo;
import rocklike.netty.messenger.common.JacksonHelper;
import rocklike.netty.messenger.common.MsgAction;
import rocklike.netty.messenger.common.StrMsgEntity;
import rocklike.netty.messenger.common.StrMsgEntity.StrMsgEntityBuilder;

public class RoomUsersPart implements ClientMsgProcessor{
	@Inject
	public RoomUsersPart() {
	}

	private ListViewer userList ;
	private Label thisRoomName;

	@PostConstruct
	public void postConstruct(Composite parent) {
		Composite g = new Composite(parent, SWT.BORDER);
		GridLayoutFactory.fillDefaults().numColumns(1).spacing(1,1).applyTo(g);

		thisRoomName = new Label(g, SWT.NULL);
		thisRoomName.setText("방 :");
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(thisRoomName);


		userList = new ListViewer(g);
		userList.setContentProvider(ArrayContentProvider.getInstance());
		userList.setLabelProvider(new LabelProvider());
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(userList.getControl());
		ClientMsgRcpProcessor.INSTANCE.addListener(this);

		attachRefreshPopupMenu();
	}

	void attachRefreshPopupMenu(){
		Menu popupMenu = new Menu(userList.getControl());
		MenuItem refresh = new MenuItem(popupMenu, SWT.NONE);
	    refresh.setText("새로고침");
	    refresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Channel channel = MyLoginInfo.INSTANCE.getChannel();
//				StrMsgEntity msgEntity = new StrMsgEntityBuilder(channel).setAction(MsgAction.UserList).setRoomName(MyLoginInfo.INSTANCE.getRoomName()).build();
				StrMsgEntity msgEntity = new StrMsgEntityBuilder(channel).setAction(MsgAction.UserList).build(); // room은 서버 정보를 따라감.
				channel.writeAndFlush(msgEntity);
			}
		});
	    userList.getControl().setMenu(popupMenu);
	}


	@Override
	public void whenLogOut(StrMsgEntity msg) {
		sync.asyncExec(()->{
			userList.setInput(null);
		});
	}

	@Override
	public void whenFail(ChannelHandlerContext ctx, StrMsgEntity msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void whenLogin(StrMsgEntity entity) {
		sync.asyncExec(()->{
			thisRoomName.setText("방 : " + "기본방");
		});

	}

	@Override
	public void whenExitFromRoom(StrMsgEntity entity) {
		MyLoginInfo iam = MyLoginInfo.INSTANCE;
		String refId = entity.getRefId();
		// 내가 퇴장했다면 기본방("")으로 전환
		if(refId.equals(iam.getId())){
			iam.setRoomName("");
			sync.asyncExec(()->{
				thisRoomName.setText("방 : " + "기본방");
			});
		}
		Channel channel = iam.getChannel();
		StrMsgEntity msgEntity = new StrMsgEntityBuilder().setAction(MsgAction.UserList).setRoomName(iam.getRoomName()).build();
		channel.writeAndFlush(msgEntity);
	}

	@Override
	public void whenEnteredToRoom(StrMsgEntity entity) {
		String roomName = entity.getRoomName();
		MyLoginInfo iam = MyLoginInfo.INSTANCE;
		if(iam.getId().equals(entity.getRefId())){
			iam.setRoomName(roomName);
			sync.asyncExec(()->{
				thisRoomName.setText( "방 : " +  ("".equals(roomName) ? "기본방" : roomName) );
			});
		}

		Channel channel = iam.getChannel();
		StrMsgEntity msgEntity = new StrMsgEntityBuilder().setAction(MsgAction.UserList).setRoomName(roomName).build();
		channel.writeAndFlush(msgEntity);
	}

	@Override
	public void whenRoomlList(StrMsgEntity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void whenInfo(StrMsgEntity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void whenTalkMsg(StrMsgEntity entity) {
		// TODO Auto-generated method stub

	}

	@Inject
	private UISynchronize sync;

	@Override
	public void whenUserList(StrMsgEntity entity) {

		List<String> users = Arrays.asList(JacksonHelper.toObj(entity.getContents(), String[].class)).stream()
				.map(id-> id.equals(MyLoginInfo.INSTANCE.getId()) ? id + " => 나" : id )
				.collect(Collectors.toList());
		sync.asyncExec(()->{
			if(users==null){
				userList.setInput(null);
			}else{
				userList.setInput(users);
			}
			userList.refresh();
		});

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		sync.asyncExec(()->{
			userList.setInput("");
			userList.refresh();
		});
	}



}