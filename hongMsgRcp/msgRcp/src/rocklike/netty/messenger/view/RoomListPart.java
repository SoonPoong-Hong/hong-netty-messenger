
package rocklike.netty.messenger.view;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
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
import rocklike.netty.messenger.dialog.CreateRoomDialog;

public class RoomListPart implements ClientMsgProcessor{
	@Inject
	public RoomListPart() {
	}

	Button createBtn ;
	private ListViewer roomList ;

	@PostConstruct
	public void postConstruct(Composite parent) {
		Group g = new Group(parent, SWT.BORDER);
		g.setText("방 목록");
		GridLayoutFactory.fillDefaults().numColumns(1).spacing(1,1).applyTo(g);

		createBtn = new Button(g, SWT.PUSH);
		createBtn.setText("방 만들기");
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).applyTo(createBtn);

		roomList = new ListViewer(g);
		roomList.setContentProvider(ArrayContentProvider.getInstance());
		roomList.setLabelProvider(new LabelProvider());
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(roomList.getControl());
		ClientMsgRcpProcessor.INSTANCE.addListener(this);

		createBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showPopupMenuToButton();
			}
		});


		// 방을 더블클릭하면 해당 방으로 들어가기
		roomList.addDoubleClickListener((DoubleClickEvent event)->{
//			ISelection sel = lv.getSelection();
			IStructuredSelection sel = (IStructuredSelection) roomList.getSelection();
			Object firstElem = sel.getFirstElement();
			if(firstElem!=null){
				String roomName = firstElem.toString();
				Channel channel = MyLoginInfo.INSTANCE.getChannel();
				StrMsgEntity msgEntity = new StrMsgEntityBuilder(channel).setAction(MsgAction.EnterToRoom).setContents(roomName).build();
				channel.writeAndFlush(msgEntity);
			}
		});

		attachContextMenu();

	}


	void attachContextMenu() {
		Menu popupMenu = new Menu(roomList.getControl());
		MenuItem clear = new MenuItem(popupMenu, SWT.NONE);
		clear.setText("새로 고침");
		clear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Channel channel = MyLoginInfo.INSTANCE.getChannel();
				StrMsgEntity msgEntity = new StrMsgEntityBuilder(channel).setAction(MsgAction.RoomList).build();
				channel.writeAndFlush(msgEntity);
			}
		});
		roomList.getControl().setMenu(popupMenu);
	}


	void processCreateRoomDialog(){
		CreateRoomDialog dialog = new CreateRoomDialog();
		int open = dialog.open();
		if(open==Dialog.OK){
			String roomName = dialog.getRoomName();
			Channel channel = MyLoginInfo.INSTANCE.getChannel();
			StrMsgEntity msgEntity = new StrMsgEntityBuilder(channel).setAction(MsgAction.EnterToRoom).setContents(roomName).build();
			channel.writeAndFlush(msgEntity);
		}
	}


	void showPopupMenuToButton(){
		Menu popupMenu = new Menu(createBtn);
		MenuItem create = new MenuItem(popupMenu, SWT.NONE);
		create.setText("방을 만들거나 들어가기");
		MenuItem exit = new MenuItem(popupMenu, SWT.NONE);
		exit.setText("방에서 나가기");
		MenuItem refresh = new MenuItem(popupMenu, SWT.NONE);
		refresh.setText("새로고침");

		create.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				processCreateRoomDialog();
			}
		});

		exit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Channel channel = MyLoginInfo.INSTANCE.getChannel();
				StrMsgEntity msgEntity = new StrMsgEntityBuilder(channel).setAction(MsgAction.ExitFromRoom).build();
				channel.writeAndFlush(msgEntity);
			}
		});

		refresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Channel channel = MyLoginInfo.INSTANCE.getChannel();
				StrMsgEntity msgEntity = new StrMsgEntityBuilder(channel).setAction(MsgAction.RoomList).build();
				channel.writeAndFlush(msgEntity);
			}
		});

		Rectangle bounds = createBtn.getBounds();
		Point topLeft = new Point(bounds.x, bounds.y + bounds.height); //  + bounds.height
		topLeft = createBtn.toDisplay(topLeft);
		popupMenu.setLocation(topLeft.x, topLeft.y);
		popupMenu.setVisible(true);
	}



	@Override
	public void whenLogOut(StrMsgEntity msg) {
		sync.asyncExec(()->{
			roomList.setInput(null);
		});
	}

	@Override
	public void whenFail(ChannelHandlerContext ctx, StrMsgEntity msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void whenLogin(StrMsgEntity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void whenExitFromRoom(StrMsgEntity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void whenEnteredToRoom(StrMsgEntity entity) {
		// TODO Auto-generated method stub

	}

	@Inject
	private UISynchronize sync;

	@Override
	public void whenRoomlList(StrMsgEntity entity) {
		List<String> rooms = Arrays.asList(JacksonHelper.toObj(entity.getContents(), String[].class)).stream().filter(r->r!=null && !r.equals(""))
				.collect(Collectors.toList());
		sync.asyncExec(()->{
			if(rooms==null){
				roomList.setInput(null);
			}else{
				roomList.setInput(rooms);
			}
			roomList.refresh();
		});
	}

	@Override
	public void whenInfo(StrMsgEntity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void whenTalkMsg(StrMsgEntity entity) {
		// TODO Auto-generated method stub

	}


	@Override
	public void whenUserList(StrMsgEntity entity) {
	}


	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		sync.asyncExec(()->{
			roomList.setInput(null);
			roomList.refresh();
		});
	}




}