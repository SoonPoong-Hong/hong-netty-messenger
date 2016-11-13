package rocklike.netty.messenger.common;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonHelper {
	private JacksonHelper() { }
	
	public static String toJsonStr(Object obj){
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr=null;
		try {
			jsonStr = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		return jsonStr;
	}
	
	
	public static <T> T toObj(String str, Class<T> clz){
		ObjectMapper m = new ObjectMapper();
		try {
			return m.readValue(str, clz);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
