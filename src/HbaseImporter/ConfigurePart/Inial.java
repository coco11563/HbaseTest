package HbaseImporter.ConfigurePart;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by root on 1/13/17.
 *
 */
public class Inial {
    private static Map<String, String> cityNum = new HashMap<>();
    private static Map<String, String> provinceNum = new HashMap<>();
    private static Map<String, String> countryNum = new HashMap<>();

    private static final String cityNumPath = "./conf/cityNum.json";
    private static final String provincePath = "./conf/province_data.json";
    private static final String countryPath = "./conf/country.json";


    static{

        try {
            inialProvince();
            inialCity();
            inialCountry();
        } catch (IOException e) {
            System.out.println("出现异常，城市、省份、国家讯息初始化失败");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void inialCity() throws IOException, JSONException {
        File f  = new File(cityNumPath);
        InputStreamReader read = new InputStreamReader (
                new FileInputStream (f), "GBK" );// 考虑到编码格式
        BufferedReader br = new BufferedReader(read);
        String temp = "";
        String data = "";
        while((temp = br.readLine()) != null){
            data += temp;
        }
        JSONObject json = JSONObject.fromObject(data);
        cityNum.putAll(getJsonData(json));
    }

    public static void inialCountry() throws IOException, JSONException {
        File f  = new File(countryPath);
        InputStreamReader read = new InputStreamReader (
                new FileInputStream (f), "UTF-8" );// 考虑到编码格式
        BufferedReader br = new BufferedReader(read);
        String temp = "";
        String data = "";
        while((temp = br.readLine()) != null){
            data += temp;
        }
        JSONObject json = JSONObject.fromObject(data);
        countryNum.putAll(getJsonData(json));
    }
    public static void inialProvince() throws IOException, JSONException {
        File f  = new File(provincePath);
        InputStreamReader read = new InputStreamReader (
                new FileInputStream (f), "UTF-8" );// 考虑到编码格式
        BufferedReader br = new BufferedReader(read);
        String temp;
        String data = "";
        while((temp = br.readLine()) != null){
            data += temp;
        }
        JSONObject json = JSONObject.fromObject(data);
        provinceNum.putAll(getJsonData(json));
    }
    private static HashMap<String, String> getJsonData(JSONObject json) throws JSONException {
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
    public String getCountry_id(String s) {
        return countryNum.get(s);
    }

    public String getCity_id(String s) {
        return cityNum.get(s);
    }

    public String getProvince_id(String s) {
        return provinceNum.get(s);
    }
}
