package rocklike.netty.messenger.common;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Strings;

import io.netty.buffer.ByteBuf;

public abstract class BaseRequestEntity<T> {
	private MsgAction action;
	private Map<String, String> headers;

	public MsgAction getAction() {
		return action;
	}

	public T setAction(MsgAction action) {
		this.action = action;
		return (T)this;
	}

	public int getLength() {
		return Integer.parseInt( getHeaders().get("length") );
	}

	public T setLength(int length) {
		getHeaders().put("length", String.valueOf(length));
		return (T) this;
	}

	public Map<String, String> getHeaders() {
		if (this.headers == null) {
			this.headers = new HashMap<>(5);
		}
		return headers;
	}

	public T addToHeader(String key, String value) {
		if (Strings.isNullOrEmpty(key)) {
			throw new IllegalArgumentException("key값이 비었습니다. (" + key + ")");
		}
		if (headers == null) {
			headers = new HashMap<>();
		}
		headers.put(key, value);
		return (T) this;
	}

	public String getRefId() {
		return getHeaders().get("refId");
	}

	public T setRefId(String refId) {
		getHeaders().put("refId", refId);
		return (T) this;
	}

	
	public String getRoomName() {
		return getHeaders().get("roomName");
	}
	
	public T setRoomName(String roomName) {
		getHeaders().put("roomName", roomName);
		return (T) this;
	}
	
	
	public String getRefName() {
		return getHeaders().get("refName");
	}

	public T setRefName(String refName) {
		getHeaders().put("refName", refName);
		return (T) this;
	}
	
	// ==== abstract
	public abstract ByteBuf formatToByteBuf();
}
