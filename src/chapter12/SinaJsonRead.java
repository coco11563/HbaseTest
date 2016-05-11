package chapter12;

import java.util.Iterator;
import java.util.LinkedHashMap;
import json.JSONException;
import json.JSONObject;

public class SinaJsonRead {
	/**
	 * 写入json文件输出相应的映射文件
	 * @param jsonobject
	 * @return json mapper
	 * @throws JSONException
	 */
public static LinkedHashMap<String, String> getJsonData(JSONObject json) throws JSONException
{
	
	@SuppressWarnings("rawtypes")
	Iterator iterator = json.keys();
	LinkedHashMap<String,String> mapper = new LinkedHashMap<String, String>();
	while(iterator.hasNext()){
	
    	String key = (String) iterator.next();
        String value = json.getString(key);
        
        mapper.put(key, value);
       
	}
	return mapper;
}
}
