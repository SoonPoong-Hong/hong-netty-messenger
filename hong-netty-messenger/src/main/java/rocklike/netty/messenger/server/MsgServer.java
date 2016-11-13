package rocklike.netty.messenger.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public final class MsgServer {
	protected static Logger logger = LoggerFactory.getLogger(MsgServer.class);

	static final int PORT = 80;

	public static void main(String[] args) throws Exception {
		int port = PORT;
		if(args.length>0){
			try{
				port = Integer.parseInt(args[0]);
			}catch(Exception e){
			}
		}

		logger.warn("== 서버 시작 (port:{})", port);

		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(2);
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new MsgServerInitializer());

			new Thread(){

				@Override
				public void run() {
					try {
						while(true){
							logger.warn("총 client 건수 : {}" , ClientChannelHolder.INSTANCE.getTotalClientCount());
							Thread.sleep(10 * 1000);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}.start();

			b.bind(port).sync().channel().closeFuture().sync();


		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
