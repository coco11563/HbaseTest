package HbaseImporter;

import HbaseImporter.ConfigurePart.Inial;
import HbaseImporter.DatePart.dateFormat;
import HbaseImporter.DatePart.dateUtil;
import HbaseImporter.GeoHashPart.GeoHash;
import json.JSONException;
import json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;


import static HbaseImporter.HolidayPart.ChineseHoliday.getHoliday;

/**
 * Created by root on 1/12/17.
 *
 *
 */
public class HbaseCeller {


    static class rowKey {

        String country_id;
        String province_id;
        String city_id;
        String geo_Hash;
        String user_id;
        String weibo_id;
        String year;
        String month;
        String day;
        int isHoliday;
        rowKey(JSONObject weiboInform, Inial inial) throws JSONException, ParseException {
            this.country_id = inial.getCountry_id(weiboInform.getJSONArray("url_objects").
                    getJSONObject(0).getJSONObject("object").getJSONObject("object").getJSONObject("address").
                    getString("country"));
            this.city_id = inial.getCity_id(weiboInform.getJSONArray("url_objects").getJSONObject(0).
                    getJSONObject("object").getJSONObject("object").getJSONObject("address").getString("locality"));
            this.province_id = inial.getProvince_id(weiboInform.getJSONArray("url_objects").getJSONObject(0).
                    getJSONObject("object").getJSONObject("object").getJSONObject("address").getString("region"));
            double lat = weiboInform.getJSONObject("geo").getJSONArray("coordinates").getDouble(0);
            double lng = weiboInform.getJSONObject("geo").getJSONArray("coordinates").getDouble(1);
            this.geo_Hash = new GeoHash(lat, lng).getGeoHashBase32();
            this.user_id = weiboInform.getJSONObject("user").getString("id");
            this.weibo_id = weiboInform.getString("idstr");
            String[] date = weiboInform.getString("created_at").split(" ");
            dateFormat d = new dateFormat(date);
            this.year = d.getYear();
            this.month = d.getMonth();
            this.day = d.getDay();
            this.isHoliday = getHoliday(d);
        }
        @Override
        public String toString() {
            return this.country_id + "_" + this.province_id + "_" + this.city_id + "_" +
                    this.geo_Hash + "_" + this.year + "_" + this.month + "_" + this.day + "_" + this.isHoliday +
                    "_" + this.user_id + "_" + this.weibo_id;
        }



    }
    public static void main(String args[]) throws JSONException, IOException, ParseException {
        Inial inial = new Inial();
        rowKey rk = new rowKey(new JSONObject(json), inial);
        System.out.println(rk.toString());
    }
    class OtherInform {

    }
    static String json = "{\n" +
            "    \"idstr\": \"3905207295831269\",\n" +
            "    \"distance\": 4800,\n" +
            "    \"in_reply_to_status_id\": \"\",\n" +
            "    \"pic_ids\": [\n" +
            "        \"e6e6c646jw1exo3ye0s4fj20dc0m8759\"\n" +
            "    ],\n" +
            "    \"created_at\": \"Tue Nov 03 21:30:36 +0800 2015\",\n" +
            "    \"mid\": \"3905207295831269\",\n" +
            "    \"annotations\": [\n" +
            "        {\n" +
            "            \"place\": {\n" +
            "                \"lon\": 113.85344,\n" +
            "                \"poiid\": \"8008642011303000000\",\n" +
            "                \"title\": \"邓南街区\",\n" +
            "                \"type\": \"checkin\",\n" +
            "                \"lat\": 30.23647\n" +
            "            },\n" +
            "            \"client_mblogid\": \"7b3366ab-0035-42cf-807c-097bffc70727\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"mapi_request\": true\n" +
            "        }\n" +
            "    ],\n" +
            "    \"source\": \"<a href=\\\"http://app.weibo.com/t/feed/63af84\\\" rel=\\\"nofollow\\\">vivo智能手机</a>\",\n" +
            "    \"attitudes_count\": 0,\n" +
            "    \"rid\": \"1_0_100101_2450307592895753731\",\n" +
            "    \"bmiddle_pic\": \"http://ww2.sinaimg.cn/bmiddle/e6e6c646jw1exo3ye0s4fj20dc0m8759.jpg\",\n" +
            "    \"geo\": {\n" +
            "        \"coordinates\": [\n" +
            "            30.23647,\n" +
            "            113.85344\n" +
            "        ],\n" +
            "        \"type\": \"Point\"\n" +
            "    },\n" +
            "    \"filterID\": \"0:1\",\n" +
            "    \"darwin_tags\": [],\n" +
            "    \"in_reply_to_screen_name\": \"\",\n" +
            "    \"mlevel\": 0,\n" +
            "    \"in_reply_to_user_id\": \"\",\n" +
            "    \"id\": \"3905207295831269\",\n" +
            "    \"text\": \"快乐而美好的童年 http://t.cn/R2dU2wr\",\n" +
            "    \"reposts_count\": 0,\n" +
            "    \"favorited\": false,\n" +
            "    \"visible\": {\n" +
            "        \"list_id\": 0,\n" +
            "        \"type\": 0\n" +
            "    },\n" +
            "    \"thumbnail_pic\": \"http://ww2.sinaimg.cn/thumbnail/e6e6c646jw1exo3ye0s4fj20dc0m8759.jpg\",\n" +
            "    \"original_pic\": \"http://ww2.sinaimg.cn/large/e6e6c646jw1exo3ye0s4fj20dc0m8759.jpg\",\n" +
            "    \"truncated\": false,\n" +
            "    \"source_type\": 1,\n" +
            "    \"biz_feature\": 4294967300,\n" +
            "    \"source_allowclick\": 1,\n" +
            "    \"comments_count\": 0,\n" +
            "    \"url_objects\": [\n" +
            "        {\n" +
            "            \"asso_like_count\": 0,\n" +
            "            \"like_count\": 0,\n" +
            "            \"url_ori\": \"http://t.cn/R2dU2wr\",\n" +
            "            \"object_id\": \"1022:1001018008642011303000000\",\n" +
            "            \"follower_count\": 0,\n" +
            "            \"card_info_un_integrity\": false,\n" +
            "            \"info\": {\n" +
            "                \"result\": true,\n" +
            "                \"url_short\": \"http://t.cn/R2dU2wr\",\n" +
            "                \"url_long\": \"http://weibo.com/p/1001018008642011303000000\",\n" +
            "                \"transcode\": 0,\n" +
            "                \"description\": \"\",\n" +
            "                \"type\": 36,\n" +
            "                \"title\": \"\",\n" +
            "                \"last_modified\": 1446764017\n" +
            "            },\n" +
            "            \"object\": {\n" +
            "                \"object_type\": \"place\",\n" +
            "                \"object_domain_id\": \"1022\",\n" +
            "                \"activate_status\": \"0\",\n" +
            "                \"containerid\": \"1001018008642011303000000\",\n" +
            "                \"safe_status\": 1,\n" +
            "                \"show_status\": \"10\",\n" +
            "                \"object_id\": \"1022:1001018008642011303000000\",\n" +
            "                \"last_modified\": \"Fri Nov 06 06:53:37 CST 2015\",\n" +
            "                \"uuid\": 3856924725502555,\n" +
            "                \"act_status\": \"00\",\n" +
            "                \"timestamp\": 1446764017910,\n" +
            "                \"object\": {\n" +
            "                    \"summary\": \"2006年6月，湖北省民政厅以鄂民政发28号文件撤销汉南区邓南镇。设立邓南街道办事处，驻地为邓南正街特1号。 邓南街道人口16401人，面积80.50平方千米。辖1个居委会、15个村委会：振兴街、金城、窑头、捞子湖、鞋尖、水一、水二、水三、新沟、邓西、建新、解放、南庄、下南、塘江、郭家。\",\n" +
            "                    \"image\": {\n" +
            "                        \"width\": \"88\",\n" +
            "                        \"url\": \"http://ww4.sinaimg.cn/large/005BvWaMjw1euhf36lgmlj305005074g.jpg\",\n" +
            "                        \"height\": \"88\"\n" +
            "                    },\n" +
            "                    \"biz\": {\n" +
            "                        \"biz_id\": \"100101\",\n" +
            "                        \"containerid\": \"1001018008642011303000000\"\n" +
            "                    },\n" +
            "                    \"address\": {\n" +
            "                        \"country\": \"中国\",\n" +
            "                        \"street_address\": \"2006年6月，湖北省民政厅以鄂...\",\n" +
            "                        \"formatted\": \"2006年6月，湖北省民政...\",\n" +
            "                        \"locality\": \"武汉市\",\n" +
            "                        \"telephone\": \"\",\n" +
            "                        \"fax\": \"\",\n" +
            "                        \"postal_code\": \"\",\n" +
            "                        \"region\": \"湖北省\",\n" +
            "                        \"email\": \"\"\n" +
            "                    },\n" +
            "                    \"object_type\": \"place\",\n" +
            "                    \"target_url\": \"http://weibo.cn/pages/1001018008642011303000000\",\n" +
            "                    \"mobile\": {\n" +
            "                        \"page_id\": \"1001018008642011303000000\",\n" +
            "                        \"card\": {\n" +
            "                            \"scheme\": \"sinaweibo://pageinfo?pageid=1001018008642011303000000&title=商圈\",\n" +
            "                            \"contents\": [\n" +
            "                                \"武汉·邓南街区\",\n" +
            "                                \"2006年6月，湖北省民政厅以鄂民政发28号文件撤销汉南区邓南镇。设立邓南街道办事处，驻地为邓南正街特1号。 邓南街道人口16401人，面积80.50平方千米。辖1个居委会、15个村委会：振兴街、金城、窑头、捞子湖、鞋尖、水一、水二、水三、新沟、邓西、建新、解放、南庄、下南、塘江、郭家。\",\n" +
            "                                \"2535条微博\"\n" +
            "                            ],\n" +
            "                            \"is_asyn\": 0,\n" +
            "                            \"pic\": \"http://ww4.sinaimg.cn/large/005BvWaMjw1euhf36lgmlj305005074g.jpg\",\n" +
            "                            \"type\": 0,\n" +
            "                            \"status\": 1\n" +
            "                        },\n" +
            "                        \"url\": {\n" +
            "                            \"scheme\": \"sinaweibo://pageinfo?pageid=1001018008642011303000000&title=商圈\",\n" +
            "                            \"name\": \"武汉·邓南街区\",\n" +
            "                            \"status\": 1\n" +
            "                        }\n" +
            "                    },\n" +
            "                    \"id\": \"1022:1001018008642011303000000\",\n" +
            "                    \"position\": \"30.316 114.01337\",\n" +
            "                    \"display_name\": \"武汉·邓南街区\",\n" +
            "                    \"keyword\": \"武汉·邓南街区[地点]\",\n" +
            "                    \"url\": \"http://weibo.cn/pages/1001018008642011303000000\"\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    ],\n" +
            "    \"userType\": 0,\n" +
            "    \"user\": {\n" +
            "        \"allow_all_act_msg\": false,\n" +
            "        \"favourites_count\": 0,\n" +
            "        \"urank\": 4,\n" +
            "        \"verified_trade\": \"\",\n" +
            "        \"weihao\": \"\",\n" +
            "        \"verified_source_url\": \"\",\n" +
            "        \"type\": 1,\n" +
            "        \"province\": \"32\",\n" +
            "        \"screen_name\": \"用户3873883718\",\n" +
            "        \"id\": \"3873883718\",\n" +
            "        \"geo_enabled\": true,\n" +
            "        \"level\": 1,\n" +
            "        \"verified_type\": -1,\n" +
            "        \"extend\": {\n" +
            "            \"mbprivilege\": \"0000000000000000000000000000000000000000000000000000000000000000\",\n" +
            "            \"privacy\": {\n" +
            "                \"mobile\": 1\n" +
            "            }\n" +
            "        },\n" +
            "        \"badge\": {\n" +
            "            \"zongyiji\": 0,\n" +
            "            \"gongyi_level\": 0,\n" +
            "            \"gongyi\": 0,\n" +
            "            \"shuang11_2015\": 0,\n" +
            "            \"enterprise\": 0,\n" +
            "            \"bind_taobao\": 0,\n" +
            "            \"hongbao_2014\": 0,\n" +
            "            \"uc_domain\": 0,\n" +
            "            \"travel2013\": 0,\n" +
            "            \"taobao\": 0,\n" +
            "            \"suishoupai_2014\": 0,\n" +
            "            \"dailv\": 0,\n" +
            "            \"anniversary\": 0\n" +
            "        },\n" +
            "        \"pagefriends_count\": 0,\n" +
            "        \"domain\": \"\",\n" +
            "        \"following\": false,\n" +
            "        \"name\": \"用户3873883718\",\n" +
            "        \"idstr\": \"3873883718\",\n" +
            "        \"follow_me\": false,\n" +
            "        \"friends_count\": 39,\n" +
            "        \"credit_score\": 80,\n" +
            "        \"gender\": \"m\",\n" +
            "        \"city\": \"10\",\n" +
            "        \"profile_url\": \"u/3873883718\",\n" +
            "        \"description\": \"制造师\",\n" +
            "        \"created_at\": \"Sat Oct 26 12:47:04 +0800 2013\",\n" +
            "        \"remark\": \"\",\n" +
            "        \"ptype\": 0,\n" +
            "        \"badge_top\": \"\",\n" +
            "        \"verified_reason_url\": \"\",\n" +
            "        \"block_word\": 0,\n" +
            "        \"avatar_hd\": \"http://ww1.sinaimg.cn/crop.0.0.180.180.1024/e6e6c646jw8e9yhjz1zw9j2050050wei.jpg\",\n" +
            "        \"mbtype\": 0,\n" +
            "        \"bi_followers_count\": 0,\n" +
            "        \"user_ability\": 0,\n" +
            "        \"verified_reason\": \"\",\n" +
            "        \"mbrank\": 0,\n" +
            "        \"lang\": \"zh-cn\",\n" +
            "        \"class\": 1,\n" +
            "        \"has_ability_tag\": 0,\n" +
            "        \"star\": 0,\n" +
            "        \"allow_all_comment\": true,\n" +
            "        \"online_status\": 0,\n" +
            "        \"ulevel\": 0,\n" +
            "        \"verified\": false,\n" +
            "        \"profile_image_url\": \"http://tp3.sinaimg.cn/3873883718/50/5677730183/1\",\n" +
            "        \"block_app\": 0,\n" +
            "        \"url\": \"\",\n" +
            "        \"avatar_large\": \"http://tp3.sinaimg.cn/3873883718/180/5677730183/1\",\n" +
            "        \"statuses_count\": 4,\n" +
            "        \"followers_count\": 1,\n" +
            "        \"location\": \"江苏 扬州\",\n" +
            "        \"verified_source\": \"\"\n" +
            "    },\n" +
            "    \"biz_ids\": [\n" +
            "        100101\n" +
            "    ]\n" +
            "}";
}