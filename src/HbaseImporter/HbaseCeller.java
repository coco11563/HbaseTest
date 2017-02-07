package HbaseImporter;

import HbaseImporter.ConfigurePart.Inial;
import HbaseImporter.HolidayPart.CellerInnerClass.rowKey;
import net.sf.json.JSONObject;
import org.apache.hadoop.hbase.client.HTable;

import java.text.ParseException;

/**
 * Created by root on 1/12/17.
 *
 *
 */
public class HbaseCeller {
    private rowKey rowKey;
    private OtherInform otherInform;
    private static Inial inial;

    static {
        inial = new Inial();
    }
    public HbaseCeller(JSONObject jsonObject) throws ParseException {
        this.rowKey = new rowKey(jsonObject, inial);
        this.otherInform = new OtherInform(jsonObject.toString());
    }

    public HbaseCeller(String jsonObject) throws ParseException {
        this.rowKey = new rowKey(JSONObject.fromObject(jsonObject), inial);
        this.otherInform = new OtherInform(jsonObject);
    }

    public rowKey getRowKey() {
        return rowKey;
    }

    public void setRowKey(rowKey rowKey) {
        this.rowKey = rowKey;
    }

    public OtherInform getOtherInform() {
        return otherInform;
    }

    public void setOtherInform(OtherInform otherInform) {
        this.otherInform = otherInform;
    }


    public class OtherInform {
        private String content;
        private String gender;
        private String picURL;
        private String otherInform;
        private String username;
        OtherInform(String json) {
            setOtherInform(json);
            JSONObject jsonObject = JSONObject.fromObject(json);
            if(jsonObject.containsKey("bmiddle_pic")) {
                setPicURL(jsonObject.getString("bmiddle_pic"));
            } else {
                setPicURL("0");
            }
            setGender(jsonObject.getJSONObject("user").getString("gender"));
            setUsername(jsonObject.getJSONObject("user").getString("name"));
            setContent(jsonObject.getString("text"));
        }
        @Override
        public String toString(){
            return getOtherInform();
        }
        public String getOtherInform() {
            return otherInform;
        }

        public void setOtherInform(String otherInform) {
            this.otherInform = otherInform;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPicURL() {
            return picURL;
        }

        public void setPicURL(String picURL) {
            this.picURL = picURL;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

}