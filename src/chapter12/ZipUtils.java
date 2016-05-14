package chapter12;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class ZipUtils {

	/**
	 *���ļ������ļ��д��ѹ����zip��ʽ
	 * @author ysc
	 */

	    private static final Logger log = LoggerFactory.getLogger(ZipUtils.class);
	         
	    private ZipUtils(){};
	   /**
	     * ����ZIP�ļ�
	     * @param sourcePath �ļ����ļ���·��
	     * @param zipPath ���ɵ�zip�ļ�����·���������ļ�����
	     */
	    public static void createZip(String sourcePath, String zipPath) {
	        FileOutputStream fos = null;
	        ZipOutputStream zos = null;
	        try {
	            fos = new FileOutputStream(zipPath);
	            zos = new ZipOutputStream(fos);
	            writeZip(new File(sourcePath), "", zos);
	        } catch (FileNotFoundException e) {
	            log.error("����ZIP�ļ�ʧ��",e);
	        } finally {
	            try {
	                if (zos != null) {
	                    zos.close();
	                }
	            } catch (IOException e) {
	                log.error("����ZIP�ļ�ʧ��",e);
	            }
	 
	        }
	    }
	     
	    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
	        if(file.exists()){
	            if(file.isDirectory()){//�����ļ���
	                parentPath+=file.getName()+File.separator;
	                File [] files=file.listFiles();
	                for(File f:files){
	                    writeZip(f, parentPath, zos);
	                }
	            }else{
	                FileInputStream fis=null;
	                DataInputStream dis=null;
	                try {
	                    fis=new FileInputStream(file);
	                    dis=new DataInputStream(new BufferedInputStream(fis));
	                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
	                    zos.putNextEntry(ze);
	                    byte [] content=new byte[1024];
	                    int len;
	                    while((len=fis.read(content))!=-1){
	                        zos.write(content,0,len);
	                        zos.flush();
	                    }
	                     
	                } catch (FileNotFoundException e) {
	                    log.error("����ZIP�ļ�ʧ��",e);
	                } catch (IOException e) {
	                    log.error("����ZIP�ļ�ʧ��",e);
	                }finally{
	                    try {
	                        if(dis!=null){
	                            dis.close();
	                        }
	                    }catch(IOException e){
	                        log.error("����ZIP�ļ�ʧ��",e);
	                    }
	                }
	            }
	        }
	    }   
	    public static void main(String[] args) {
	        ZipUtils.createZip("D:\\workspaces\\netbeans\\APDPlat\\APDPlat_Web\\target\\APDPlat_Web-2.2\\platform\\temp\\backup", "D:\\workspaces\\netbeans\\APDPlat\\APDPlat_Web\\target\\APDPlat_Web-2.2\\platform\\temp\\backup.zip");
	        ZipUtils.createZip("D:\\workspaces\\netbeans\\APDPlat\\APDPlat_Web\\target\\APDPlat_Web-2.2\\platform\\index.jsp", "D:\\workspaces\\netbeans\\APDPlat\\APDPlat_Web\\target\\APDPlat_Web-2.2\\platform\\index.zip");
	         
	    }
	
	 

}
