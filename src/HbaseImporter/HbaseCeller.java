package HbaseImporter;

import json.JSONObject;

/**
 * Created by root on 1/12/17.
 *
 * weibo��������Hbaseǰ�Č���
 *
 * ��Ҫ�Ę��췽ʽ��ݔ��һ��JsonObject���row key�Լ����������е�����
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
