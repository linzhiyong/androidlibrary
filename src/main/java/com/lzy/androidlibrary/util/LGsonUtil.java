package com.lzy.androidlibrary.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Json管理工具类，提供生成实体与json数据相互转换的方法
 * 
 * @author linzhiyong
 * @time 2015年12月2日11:28:57
 * @email wflinzhiyong@163.com
 * 
 */
public class LGsonUtil {

	private static Gson gson;
	
	static {
		if(gson == null) {
			gson = new Gson();
		}
	}

	private LGsonUtil() {}
	
	/**
	 * 将实体转换成Json格式数据
	 * 
	 * @param object the Serializable entity
	 * @return jsonString
	 */
	public static String toJson(Object object) {
		if(object == null) {
			return null;
		}
		return gson.toJson(object);
	}
	
	/**
	 * 将实体集合转换成Json格式数据
	 * 
	 * @param list the List<?>
	 * @return jsonString
	 */
	public static String toJson(List<?> list) {
		if(list == null || list.isEmpty()) {
			return null;
		}
		return gson.toJson(list);
	}
	
	/**
	 * 将Json格式数据转成指定实体
	 * 
	 * @param jsonStr String jsonStr
	 * @param class1 Class<T> class1
	 * @return T
	 */
	public static <T> T fromJson(String jsonStr, Class<T> class1) throws Exception {
		if(TextUtils.isEmpty(jsonStr) || class1 == null) {
			return null;
		}
		return gson.fromJson(jsonStr, class1);
	}
	
	/**
	 * 将Json格式数据转成指定实体List集合
	 * 
	 * @param jsonStr String jsonStr
	 * @param type Type type = new TypeToken<List<T>>(){}.getType()
	 * @return list<T>
	 */
	public static <T> List<T> fromJson(String jsonStr, Type type) throws Exception {
		if(TextUtils.isEmpty(jsonStr) || type == null) {
			return null;
		}
		return gson.fromJson(jsonStr, type);
	}
	
	/**
	 * 将Json格式数据转成指定实体List集合（转换失败，类型丢失）
	 * 
	 * @param jsonStr String jsonStr
	 * @param class1
	 * @return list<T>
	 */
	@Deprecated
	public static <T> List<T> jsonToList(String jsonStr, Class<T> class1) throws Exception {
		if(TextUtils.isEmpty(jsonStr) || class1 == null) {
			return null;
		}
		return gson.fromJson(jsonStr, new TypeToken<List<T>>(){}.getType());
	}

	/**
	 * 将JSON转成list
	 *
	 * @param gsonStr
	 * @param type new TypeToken<List<Map<String, T>>>(){}.getType()
	 * @param <T>
	 * @return
	 */
    public static <T> List<Map<String, T>> jsonToMapList(String gsonStr, Type type) throws Exception {
    	if(gsonStr == null) {
			return null;
		}
        return gson.fromJson(gsonStr, type);
    }

	/**
	 * 将JSON转成Map
	 *
	 * @param gsonStr
	 * @param type new TypeToken<Map<String, T>>(){}.getType()
	 * @param <T>
	 * @return
	 */
    public static <T> Map<String, T> jsonToMap(String gsonStr, Type type) throws Exception {
    	if(gsonStr == null){
			return null;
		}
        return gson.fromJson(gsonStr, type);
    }

	/**
	 * 将JSON转成Map
	 *
	 * @param jsonR
	 * @param type new TypeToken<Map<String, T>>(){}.getType()
	 * @param <T>
	 * @return
	 */
    public static <T> Map<String, T> jsonToMap(Reader jsonR, Type type) throws Exception {
    	if(jsonR == null){
			return null;
		}
        return gson.fromJson(jsonR, type);
    }

}
