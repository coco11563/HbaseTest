package chapter12;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
 
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
	 * @throws MalformedURLException 
	 * @throws UnknownHostException 
	 * @throws SmbException 
	     */
	    public static void createSmbZip(String sourcePath, String zipPath) throws MalformedURLException, FileNotFoundException, SmbException, UnknownHostException {
	    	SmbFileOutputStream fos = null;
	        ZipOutputStream zos = null;
	        try {
	            fos = new SmbFileOutputStream(zipPath);
	            zos = new ZipOutputStream(fos);
	            writeSmbZip(new SmbFile(sourcePath), "", zos);
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
	     
	    private static void writeSmbZip(SmbFile smbFile, String parentPath, ZipOutputStream zos) {
	        try {
				if(smbFile.exists()){
				    if(smbFile.isDirectory()){//�����ļ���
				        parentPath+=smbFile.getName()+File.separator;
				        SmbFile[] smbfiles=smbFile.listFiles();
				        for(SmbFile f:smbfiles){
				            writeSmbZip(f, parentPath, zos);
				        }
				    }else{
				        SmbFileInputStream fis=null;
				        DataInputStream dis=null;
				        try {
				            fis=new jcifs.smb.SmbFileInputStream(smbFile);
				            dis=new DataInputStream(new BufferedInputStream(fis));
				            ZipEntry ze = new ZipEntry(parentPath + smbFile.getName());
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
			} catch (SmbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } 
	    public static void createZip(String sourcePath, String zipPath) throws MalformedURLException, FileNotFoundException, SmbException, UnknownHostException {
	        FileOutputStream fos = null;
	        ZipOutputStream zos = null;
	        try {
	            fos = new FileOutputStream(zipPath);
	            zos = new ZipOutputStream(fos);
	            writeZip(new File(sourcePath), "", zos);
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
	    public static void main(String[] args) throws MalformedURLException, FileNotFoundException, SmbException, UnknownHostException {
	        ZipUtils.createSmbZip("smb://biggrab:123456@192.168.1.111/biggrab/export/2016-03-06/�����ر�������/","smb://biggrab:123456@192.168.1.111/biggrab/export/2016-03-06/test.zip");
	        
	         
	    }
	
	 

}
