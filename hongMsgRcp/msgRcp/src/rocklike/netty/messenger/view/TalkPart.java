
package rocklike.netty.messenger.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import rocklike.netty.messenger.client.ClientMsgProcessor;
import rocklike.netty.messenger.client.MyLoginInfo;
import rocklike.netty.messenger.common.MsgAction;
import rocklike.netty.messenger.common.StrMsgEntity;
import rocklike.netty.messenger.common.StrMsgEntity.StrMsgEntityBuilder;
import rocklike.netty.messenger.handler.LoginHandler;
import rocklike.netty.util.SashFormHelper;
import rocklike.netty.util.SystemColor;

public class TalkPart implements ClientMsgProcessor {
	@Inject
	public TalkPart() {

	}

	Text inputText;
	Button btn;
	int cnt;
	// Composite displayContainer ;
	// ScrolledComposite sc ;
	StyledText stext;

	@PostConstruct
	public void postConstruct(Composite parent) throws Exception {

		parent.setLayout(new FillLayout());
		SashForm sashForm = SashFormHelper.prepareVerticalSashForm(parent, 5);

		stext = new StyledText(sashForm, SWT.BORDER | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY);

		Composite inputContainer = new Composite(sashForm, SWT.NULL);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(inputContainer);
		sashForm.setWeights(new int[] { 5, 1 });

		inputText = new Text(inputContainer, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		inputText.setText("");
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(inputText);

		btn = new Button(inputContainer, SWT.PUSH);
		btn.setText("전송");
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(false, false).applyTo(btn);
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sendMsg();
			}
		});

		attachContextMenutoButton(btn);
		attachContextMenutoButton(stext);

		inputText.addListener(SWT.Traverse, e -> {
			if (e.detail == SWT.TRAVERSE_RETURN) {
				sendMsg();
			}
		});

		inputText.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
					e.doit = false;
				}
			}
		});

		ClientMsgRcpProcessor.INSTANCE.addListener(this);

		new LoginHandler().execute();
	}

	void attachContextMenutoButton(Control c) {
		Menu popupMenu = new Menu(c);
		MenuItem clear = new MenuItem(popupMenu, SWT.NONE);
		clear.setText("대화 내용 지우기");
		clear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				stext.setText("");
			}
		});
		c.setMenu(popupMenu);
	}

	void sendMsg() {
		System.out.println("== 텍스트 건수 : " + cnt++);
		String text = inputText.getText();
		if (text.length() == 0) {
			return;
		}
		Channel channel = MyLoginInfo.INSTANCE.getChannel();
		StrMsgEntity msgEntity = new StrMsgEntityBuilder(channel).setAction(MsgAction.TalkMsg).setContents(text).build();
		inputText.setText("");
		channel.writeAndFlush(msgEntity);
	}

	@Inject
	private UISynchronize sync;

	void append(String id, String msg, boolean isTalkMsg) {
		sync.syncExec(() -> {
			// int oldLength = stext.getText().length();
			int pos = stext.getText().length();
			String header;
			String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
			if(!isTalkMsg){
				header = String.format("(%s)\n", time);
			}else if (id != null && id.equals(MyLoginInfo.INSTANCE.getId())) {
				header = String.format("%s (나) (%s) :\n", id, time);
			} else {
				header = String.format("%s (%s) :\n", id, time);
			}

			String contents = msg + "\n";
			if (isTalkMsg) {
				stext.append(header + contents);

				StyleRange headerStyle = new StyleRange();
				headerStyle.start = pos;
				headerStyle.length = header.length();
				headerStyle.fontStyle = SWT.BOLD;
				headerStyle.foreground = SystemColor.blue();
				stext.setStyleRange(headerStyle);

			} else {
				stext.append(header + contents);

				StyleRange headerStyle = new StyleRange();
				headerStyle.start = pos;
				headerStyle.length = header.length();
				headerStyle.fontStyle = SWT.BOLD;
				headerStyle.foreground = SystemColor.blue();
				stext.setStyleRange(headerStyle);

				StyleRange contentsStyle = new StyleRange();
				contentsStyle.start = pos + header.length();
				contentsStyle.length = contents.length();
				contentsStyle.foreground = SystemColor.darkGreen();
				stext.setStyleRange(contentsStyle);
			}
			stext.setTopIndex(stext.getLineCount() - 1);

		});
	}

	// void append_old( String id, String msg, boolean isTalkMsg){
	// sync.syncExec(()->{
	// StyledText text = new StyledText(displayContainer, SWT.BORDER | SWT.WRAP
	// | SWT.MULTI | SWT.V_SCROLL);
	// if(isTalkMsg){
	// text.setText(id + ":\n" + msg);
	// StyleRange nameStyle = new StyleRange();
	// nameStyle.start = 0;
	// nameStyle.length = (id+":").length();
	// nameStyle.fontStyle = SWT.BOLD;
	// nameStyle.foreground = SystemColor.blue();
	// text.setStyleRange(nameStyle);
	//
	// }else{
	// text.setText(msg);
	// StyleRange nameStyle = new StyleRange();
	// nameStyle.start = 0;
	// nameStyle.length = (msg).length();
	// nameStyle.foreground = SystemColor.darkGreen();
	// text.setStyleRange(nameStyle);
	// }
	//
	// if(isTalkMsg && id!=null && id.equals(MyLoginInfo.INSTANCE.getId())){
	// GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).grab(true,
	// true).applyTo(text);
	// }else{
	// GridDataFactory.fillDefaults().align(SWT.BEGINNING,
	// SWT.CENTER).grab(true, true).applyTo(text);
	// }
	//
	// text.addListener(SWT.Verify, e-> e.doit=false );
	//// displayContainer.setSize(displayContainer.computeSize(SWT.DEFAULT,
	// SWT.DEFAULT)); // => 꼭 호출해 줘야 함.
	//// displayContainer.requestLayout();
	//
	//// sc.setOrigin(0, displayContainer.getClientArea().height);
	// });
	// }

	@Override
	public void whenLogOut(StrMsgEntity msg) {
		sync.asyncExec(() -> {
			String refId = msg.getRefId();
			String s = String.format("'%s'님이 로그아웃했습니다.", refId);
			append(null, s, false);
		});
	}

	@Override
	public void whenFail(ChannelHandlerContext ctx, StrMsgEntity msg) {
		sync.asyncExec(() -> {
			append(null, "[에러]" + msg.getContents(), false);
		});
	}

	@Override
	public void whenLogin(StrMsgEntity entity) {
		sync.asyncExec(() -> {
			String refId = entity.getRefId();
			String s = String.format("'%s'님이 로그인했습니다.", refId);
			append(null, s, false);
		});
	}

	@Override
	public void whenExitFromRoom(StrMsgEntity entity) {
		sync.asyncExec(() -> {
			String refId = entity.getRefId();
			String roomName = entity.getRoomName();
			roomName = "".equals(roomName) ? "기본방" : roomName;
			String s = String.format("'%s'님이 '%s'방에서 나갔습니다.", refId, roomName);
			append(null, s, false);
		});
	}

	@Override
	public void whenEnteredToRoom(StrMsgEntity entity) {
		sync.asyncExec(() -> {
			String refId = entity.getRefId();
			String roomName = entity.getRoomName();
			String s = String.format("'%s'님이 '%s'방으로 들어왔습니다.", refId, roomName);
			append(null, s, false);
		});
	}

	@Override
	public void whenRoomlList(StrMsgEntity entity) {

	}

	@Override
	public void whenInfo(StrMsgEntity entity) {
		sync.asyncExec(() -> {
			append(null, entity.getContents(), false);
		});
	}

	@Override
	public void whenTalkMsg(StrMsgEntity entity) {
		sync.asyncExec(() -> {
			String str = entity.getContents();
			append(entity.getRefId(), (str == null) ? "" : str, true);
		});
	}

	@Override
	public void whenUserList(StrMsgEntity entity) {
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		sync.asyncExec(() -> {
			String s = "로그아웃 됐습니다.";
			append(null, s, false);
		});
	}


}