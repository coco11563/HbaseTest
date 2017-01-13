package HbaseImporter;

import json.JSONObject;

/**
 * Created by root on 1/12/17.
 *
 * weibo數據插入Hbase前的對象
 *
 * 主要的構造方式是輸入一個JsonObject完成row key以及其它數據行的生成
 *
 */
public class HbaseCeller {
    class rowKey {
        String country_id;
        String province_id;
        String city_id;
        String geo_Hash;
        String user_id;
        String weibo_id;

        rowKey(JSONObject weiboInform) {

        }
    }

    class OtherInform {

    }


}
