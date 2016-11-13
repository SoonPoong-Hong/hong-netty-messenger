package rocklike.netty.messenger.common;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class RequestDecoder extends ByteToMessageDecoder {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private List<String> headers = new ArrayList<>();
	private int contentLength = 0; 
//	private ByteBuf contentsBuf;
	private Stage stage = Stage.Header;
	private StrMsgEntity requestEntity;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		switch (stage) {
		case Header:
			String line = ByteBufConsumer.line(in);
			if (line == null) {
				return;
			}
			headers.add(line);

			// 두번째 "="인 row에서 header는 끝난다.
			if (headers.size() > 1 && line.equals("=")) {
				this.requestEntity = transformToHeader();
				int length = requestEntity.getLength();
				if (length == 0) {
					out.add(requestEntity);
					initLocalVars();
					return;
				} else {
					this.stage = Stage.Contents;
				}
				break;

			}

			break;

		case Contents:
			int length = requestEntity.getLength();
			if(in.readableBytes()<length){
				break;
			}
			ByteBuf buf = Unpooled.buffer(length);
			in.readBytes(buf);
			requestEntity.setContents(buf.toString(Charset.forName("UTF8")));
			buf.release();
			out.add(requestEntity);
			initLocalVars();
			break;

		default:
			throw new IllegalAccessException("여기는 오면 안돼는데..");
		}
	}

	StrMsgEntity transformToHeader() {
		StrMsgEntity entity = new StrMsgEntity();
		int size = headers.size();
		for (int i = 0; i < size; i++) {
			String thisLine = headers.get(i);

			if (i == 0) {
				// 첫번째 라인
				if (!thisLine.equals("=")) {
					throw new IllegalArgumentException("첫번째 줄은 '='이어야 함(" + thisLine + ")");
				}

			} else if (i == 1) {
				// 두번째 라인 (action)
				entity.setAction(MsgAction.decode(thisLine));

			} else if (i == size - 1) {
				// 마지막 라인
				if (!thisLine.equals("=")) {
					throw new IllegalArgumentException("마지막 줄은 '='이어야 함(" + thisLine + ")");
				}

			} else {
				// 나머지 헤더
				int pos = thisLine.indexOf(":");
				if (pos > 0) {
					String key = thisLine.substring(0, pos).trim();
					String val = thisLine.substring(pos + 1).trim();
					if (key.equals("length")) {
						entity.setLength(Integer.parseInt(val));
					} else {
						entity.getHeaders().put(key, val);
					}
				}

			}
		}
		return entity;
	}

	enum Stage {
		Header, Contents
	}

	private void initLocalVars() {
		this.headers.clear();
		this.contentLength = 0;
		this.stage = Stage.Header;
		this.requestEntity = null;
	}

}