package chapter12;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class SinaJsonRead {
	/**
	 * 写入json文件输出相应的映射文件
	 * @param json
	 * @return json mapper
	 * @throws JSONException
	 */
public static HashMap<String, String> getJsonData(JSONObject json) throws JSONException {
	
	@SuppressWarnings("rawtypes")
	Iterator iterator = json.keys();
	HashMap<String,String> mapper = new HashMap<String, String>();
	while(iterator.hasNext()){
	
    	String key = (String) iterator.next();
        String value = json.getString(key);
        
        mapper.put(key, value);
       
	}
	return mapper;
}


}
