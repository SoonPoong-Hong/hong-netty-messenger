package rocklike.netty.messenger.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import rocklike.netty.messenger.common.LoginUserInfo;
import rocklike.netty.messenger.common.LoginUserMock;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MsgClient {
	static final Logger logger = LoggerFactory.getLogger(MsgClient.class);

	static final int PORT = 9090;
	static final String HOST = "localhost";

	public static void main(String[] args) throws Exception {
		int port = PORT;
		String host = HOST;

		if(args.length>1){
			try {
				host = args[0];
				port = Integer.parseInt(args[1]);
			} catch (Exception e) {
			}
		}

		MsgClient main = new MsgClient();
		LoginUserInfo loginInfo = LoginUserMock.get();
		LoginRequest req = new LoginRequest();
		req.setId(loginInfo.getId());
		req.setName(loginInfo.getName());
		req.setNick(loginInfo.getNickname());
		req.setPasswd("1111");
		req.setServerIp(host);
		req.setServerPort(port);
		main.connect(req);
	}

	public Channel connect(LoginRequest req) throws InterruptedException {
		logger.info("== client 시작..");
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class).handler(new MsgClientInitializer(req));

		Channel ch = b.connect(req.getServerIp(), req.getServerPort()).sync().channel();
		ch.closeFuture().addListener(f -> {
			logger.info("close됨..");
		});

		return ch;

	}
}
