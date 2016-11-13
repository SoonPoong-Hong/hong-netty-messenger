package rocklike.netty.messenger.common;

import java.nio.charset.Charset;

import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.Unpooled;

public class ByteBufConsumer {
	public static String line(ByteBuf buf){
		int posLF = buf.forEachByte(ByteBufProcessor.FIND_LF);
		if(posLF<0){
			return null;
		}
		
		int strPos = posLF;
		
		if(posLF>0 && buf.getByte(posLF-1)==ByteBufHelper.CR){
			strPos--;
		}
		int readerIndex = buf.readerIndex();
		String msg = buf.toString(readerIndex, strPos-readerIndex, Charset.forName("UTF-8"));
		buf.readerIndex(posLF+1);
		return msg;
	}
	
	public static boolean byteSize(int length, ByteBuf src, ByteBuf target){
		Preconditions.checkNotNull(src, "src ByteBuf cannot be null");
		Preconditions.checkNotNull(target, "target ByteBuf cannot be null");
		
		if(src.readableBytes()<length){
			return false;
		}
		src.readBytes(target, length);
		return true;
	}
	
	public static void main(String[] args) {
		ByteBuf buf = Unpooled.buffer();
		for(int i=65; i<65+20; i++){
			buf.writeByte(i);
		}
		ByteBufHelper.printBytes(buf);
		System.out.println("=======");
		ByteBuf target = Unpooled.buffer();
		byteSize(3, buf, target);
		ByteBufHelper.printBytes(target);
		System.out.println("=======");
		byteSize(3, buf, target);
		ByteBufHelper.printBytes(target);
		target.release();
		buf.release();
		System.out.println("땡.");
	}
	
}
