package HbaseImporter.HolidayPart;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChineseHoliday{
    private final static Map<Date, Integer> holidayBuffer = new HashMap<>();
    private final static List<File> dataPath = new ArrayList<>();
    private final static String storDataPath = "/root/Documents/IdeaWork/HbaseTest/src/HbaseImporter/HolidayPart/data";

    static {
        getFilePath(new File(storDataPath));
        String temp = "";
        for (File f : dataPath) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(f));
                while ((temp = reader.readLine()) != null) {
                    if (!temp.equals("\n")) {
                        if (f.getName().matches("[2][0][1][0-7]_w.txt|[2][0][0][9]_w.txt"))
                        holidayBuffer.put(holidayToDate(f, temp), 0);
                        else holidayBuffer.put(holidayToDate(f, temp), 1);
                    }
                }
                reader.close();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param d 想要獲取是否爲節假日的日期
     * @return 0 爲工作日 1 爲節日 2 爲週末
     */
    public static Integer getHoliday(Date d) {
        if (holidayBuffer.containsKey(d)) {
            return holidayBuffer.get(d);
        } else {
            return isWeekend(d) ? 2 : 0;
        }
    }
    private static boolean isWeekend(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
    }
    private static void getFilePath (File file) {
        if(file.isDirectory())
        {
            File f[]= file.listFiles();
            if(f!=null)
            {
                for (File aF : f) {
                    getFilePath(aF);
                }
            }
        } else {
            dataPath.add(file);
        }
    }

    private static Date holidayToDate(File f, String s) throws ParseException {
        String fileName = f.getName();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        return sdf.parse(fileName.split("\\_|\\.")[0] + "-" + s.substring(0,2) + "-" + s.substring(2));

    }
    public static void main(String args[]) throws ParseException, ClassNotFoundException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        System.out.println(getHoliday((sdf.parse("2017-01-14"))));
    }
}