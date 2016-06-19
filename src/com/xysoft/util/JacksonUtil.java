package com.xysoft.util;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
@SuppressWarnings("unchecked")
public class JacksonUtil {

	private static  final ObjectMapper mapper = new ObjectMapper();
	
    private JacksonUtil() {
    	
    }  
  
    public static ObjectMapper getInstance() {  
        return mapper;  
    }  
    
    public static String toString(Object object) {
    	String json = null;
    	try {
    		json =  getInstance().writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return json;
    }
    
	public static String toRes(String title, String msg, Map<String, Object> ...res) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("success", true);
		resMap.put("title", title);
		resMap.put("msg", msg);
		if (res != null && res.length > 0) {
			resMap.putAll(res[0]);
		}
		return JacksonUtil.toString(resMap);
	}
	
	public static String toRes(String title, Map<String, Object> ...res) {
		return toRes(title, "", res);
	}
	
	public static String toResOfFail(String title, String msg, Map<String, Object> ...res) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("success", false);
		resMap.put("title", title);
		resMap.put("msg", msg);
		if (res != null && res.length > 0) {
			resMap.putAll(res[0]);
		}
		return JacksonUtil.toString(resMap);
	}
	
	public static String toResOfFail(String title, Map<String, Object> ...res) {
		return toResOfFail(title, "", res);
	}
	
	public static <T> T toObject (String json, Class<?> classz) {
		T object = null;
		try {
			object = (T) getInstance().readValue(json, classz);
		} catch (Exception e) {
			System.out.println("======>>鏃犳晥Json瀛楃涓�");
			e.printStackTrace();
		}
		return object;
	}
	
	
	
}
