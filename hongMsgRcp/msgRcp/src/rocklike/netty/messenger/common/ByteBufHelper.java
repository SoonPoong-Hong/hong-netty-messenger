package rocklike.netty.messenger.common;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;

public class ByteBufHelper {
	private static Logger logger = LoggerFactory.getLogger(ByteBufHelper.class);

	public static final byte CR = (byte)13;
	public static final byte LF = (byte)10;
	public static final byte[] CRLF = {CR, LF};

	public static final String STR_CRLF = new String(CRLF);

	public static void printBytes(ByteBuf buf){
		IntHolder i = new IntHolder(0);
		buf.forEachByte(b->{
			logger.info("== print : {} :: {}", i.getAndIncrease(),  (int)b);
			return true;
		});
	}

	public static void printStr(Object msg){
		if(msg instanceof ByteBuf){
			ByteBuf buf = (ByteBuf) msg;
			logger.info("[ByteBuf] R:{}, W:{}, RB:{} {}", new Object[]{buf.readerIndex(), buf.writerIndex(), buf.readableBytes(), ((ByteBuf)msg).toString(Charset.forName("UTF8")) } );
		}
	}
}
