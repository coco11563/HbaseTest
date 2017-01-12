package chapter12;

import java.util.HashMap;
import java.util.Iterator;
import json.JSONException;
import json.JSONObject;

public class SinaJsonRead {
	/**
	 * д��json�ļ������Ӧ��ӳ���ļ�
	 * @param json
	 * @return json mapper
	 * @throws JSONException
	 */
public static HashMap<String, String> getJsonData(JSONObject json) throws JSONException
{
	
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
