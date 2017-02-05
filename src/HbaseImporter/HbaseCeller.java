package HbaseImporter;

import HbaseImporter.ConfigurePart.Inial;
import HbaseImporter.HolidayPart.CellerInnerClass.rowKey;
import net.sf.json.JSONObject;

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
        this.rowKey = new rowKey(jsonObject, this.inial);
        this.otherInform = new OtherInform(jsonObject.toString());
    }

    public HbaseCeller(String jsonObject) throws ParseException {
        this.rowKey = new rowKey(JSONObject.fromObject(jsonObject), this.inial);
        this.otherInform = new OtherInform(jsonObject);
    }

    public HbaseImporter.HolidayPart.CellerInnerClass.rowKey getRowKey() {
        return rowKey;
    }

    public void setRowKey(HbaseImporter.HolidayPart.CellerInnerClass.rowKey rowKey) {
        this.rowKey = rowKey;
    }

    public OtherInform getOtherInform() {
        return otherInform;
    }

    public void setOtherInform(OtherInform otherInform) {
        this.otherInform = otherInform;
    }


    class OtherInform {
        private String otherInform;
        public OtherInform(String json) {
            this.otherInform = json;
        }
    }

}