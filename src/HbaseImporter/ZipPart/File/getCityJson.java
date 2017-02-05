package HbaseImporter.ZipPart.File;

/**
 * Created by coco1 on 2017/2/6.
 *
 */

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

public class getCityJson {
    private static File file = new File("/Users/xgxyi06/Documents/sina/data");
    private static List<File> filepath = new LinkedList<>();
    private static final int LIMIT = 10000;
    private static String KEYWORD = "天津市.json";
    private static String CITY = "天津市";
    private static String usb_path = "/Volumes/WININSTALL/";
    private static File STORPATH = new File(usb_path + CITY + "");
    private static String dateregex = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))";
    //	private static String dateregex = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[78]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[69]|11)-(0[1-9]|[12][0-9]|30)))";
    private static final int ZIPFILELIMIT = 240;
    private static ZipOutputStream zos = null;
    /**
     * 2016/10/11 17:36 测试通过
     *
     * 在输入的File类型地址下获取所有文件名是keywords关键字的文件
     *
     * @param keywords 关键字
     *
     * @param file 文件地址
     */
    public static void getFilePath (String keywords, File file) {
        if(file.isDirectory())
        {
            File f[]= file.listFiles();
            if(f!=null)
            {
                for(int i=0;i<f.length;i++)
                {
                    getFilePath(keywords,f[i]);
                }
            }
        } else {
            if (file.getName().equals(keywords)) {
                System.out.println(file.toString());
                filepath.add(file);
            }
        }
    }

    private static List<String> dataRead(File file, int part) throws IOException {
        BufferedReader reader = new BufferedReader ( new FileReader( file ) ) ;
        List<String> ret = new LinkedList<>();
        String temp;
        int counter = 0;
        while ((temp = reader.readLine()) != null) {
            if (counter >= part  * LIMIT && counter < (part + 1) * LIMIT)
                ret.add(temp);
            else if (counter >= (part + 1) * LIMIT)
                break;
            counter++;
        }
        reader.close();
        return ret;
    }


    public static void dataWrite(List<String> in, String date) throws IOException {
        File 					write_path				=		new File(STORPATH.toString() + "/"  + date + "-" + KEYWORD);
        FileWriter 				write_fw				=		new FileWriter(write_path, true); ;
        if (!write_path.isFile()) write_path.mkdirs();
        for(int k=0;k<in.size();k++){
            write_fw.write(in.get(k).toString() + "\r\n");
        }
        write_fw.close();
    }

    public static void main(String[] args) throws Exception {
        getFilePath(KEYWORD,file );
        System.out.println(filepath.size());
        if (!STORPATH.exists()) STORPATH.mkdirs();
        zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(STORPATH.toString() + File.separator + CITY + "_part_1.zip")));
        int times = 0;
        int flag = 0;
        for (File f : filepath) {
            if (times > ZIPFILELIMIT && flag == 0) {
                zos.close();
                zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(STORPATH.toString() + File.separator + CITY + "_part_2.zip")));
                flag ++;
            }
            System.out.println(f.toString());
            String[] years = f.toString().split("/");
            String date = "";
            for (int i = 0 ; i < years.length ; i ++) {
                if (years[i].matches(dateregex)) {
                    date = years[i];
                }
            }
            System.out.println("开始写入："+STORPATH.toString() + File.separator + CITY + ".zip" + File.separator + date + "-" + KEYWORD);
            writeZip(f, date);
            times ++;
        }
        zos.close();

    }


    private static void writeZip(File file, String date) {
        FileInputStream fis=null;
        DataInputStream dis=null;
        try {
            fis=new FileInputStream(file);
            dis=new DataInputStream(new BufferedInputStream(fis));
            ZipEntry ze = new ZipEntry(date + "-" + file.getName());
            zos.putNextEntry(ze);
            byte[] content=new byte[1024];
            while((fis.read(content))!=-1){
                zos.write(content);
            }
            zos.closeEntry();
        } catch (IOException e) {
            System.err.println("创建ZIP文件失败");
        } finally {
            try {
                if (dis!=null) {
                    dis.close();
                } if(fis!=null) {
                    fis.close();
                }
            } catch (IOException e) {
                System.err.println("删除流失败");
            }
        }
    }
    /**
     *
     * @param file 原地址
     * @param parentPath 原地址
     * @param zos zip输出流
     */

    @SuppressWarnings("unused")
    private static void writeZip(File file, String parentPath, ZipOutputStream zos , String date) {
        if(file.exists()) {
            if(file.isDirectory()) {//处理文件夹
                parentPath += file.getName() + File.separator;
                File[] files = file.listFiles();
                assert files != null;
                for(File f : files){
                    writeZip(f, parentPath, zos, date);
                }
            } else {
                FileInputStream fis=null;
                DataInputStream dis=null;
                try {
                    fis=new FileInputStream(file);
                    dis=new DataInputStream(new BufferedInputStream(fis));
                    zos.putNextEntry(new ZipEntry(date+ "-" + file.getName()));
                    byte[] content=new byte[1024];
                    int len;
                    while((len=fis.read(content))!=-1){
                        zos.write(content,0,len);
                        zos.flush();
                    }
                } catch (IOException e) {
                    System.err.println("创建ZIP文件失败");
                }finally{
                    try {
                        if(dis!=null){
                            dis.close();
                            zos.close();
                        }
                    }catch(IOException e){
                        System.err.println("创建ZIP文件失败");
                    }
                }
            }
        }
    }


}