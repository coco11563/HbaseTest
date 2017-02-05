package HbaseImporter.DatePart;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static HbaseImporter.ConfigurePart.Inial.getJsonData;

/**
 * Created by root on 1/17/17.
 *
 * the format is yyyy-mm-dd
 */
public class dateFormat {
    private static final Map<String, String> monthDate = new HashMap<>();
    private static final String monthPath = "./conf/date.json";
    private String month;
    private String day;
    private String year;
    private String time;
    /**
     * Tue Nov 03 21:30:36 +0800 2015
     * week[0] month[1] day[2] HHMMSS[3] CST[4] YEAR[5]
     * @param segment
     */
    static {
        try {
            inialMonth();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    public dateFormat(String[] segment){
        setMonth(monthDate.get(segment[1]));
        setDay(segment[2]);
        setYear(segment[5]);
        setTime(segment[3]);
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public static void inialMonth() throws IOException, JSONException {
        File f  = new File(monthPath);
        InputStreamReader read = new InputStreamReader (
                new FileInputStream(f), "GBK" );// ¿¼ÂÇµ½±àÂë¸ñÊ½
        BufferedReader br = new BufferedReader(read);
        String temp = "";
        String data = "";
        while((temp = br.readLine()) != null){
            data += temp;
        }
        JSONObject json = JSONObject.fromObject(data);
        monthDate.putAll(getJsonData(json));
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    @Override
    public String toString() {
        return getYear() + "-" + getMonth() + "-" + getDay() + " " + getTime();
    }

    public Date toDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(toString());
    }


    public static void main(String args[]) {
        // week[0] month[1] day[2] HHMMSS[3] CST[4] YEAR[5]
        dateFormat df = new dateFormat(new String[]{"1", "12", "3", "12:34:00", "CST", "2008"});

    }
}
