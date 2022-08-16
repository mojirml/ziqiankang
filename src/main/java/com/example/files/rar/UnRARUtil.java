package com.example.files.rar;

/**
 * @author yannbai
 * @group blade_db
 * @date 2022/8/17
 * @Description
 */

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class UnRARUtil {

    private static Log log = LogFactory.getLog(UnRARUtil.class);

    /**
     * @param  args
     * @throws  IOException
     * @throws  RarException
     */
    public static void main(String[] args) throws RarException, IOException {
        //压缩文件
        String rarPath = "E:\\项目文件\\中合担保标准回单模板\\rar4.2压缩包\\银行回单.rar";
        //解压到这个目录
        String dstDirectoryPath = "E:\\11\\"+System.currentTimeMillis();
        unrarFile(rarPath,dstDirectoryPath);
    }

    public static void unrarFile(String rarPath, String dstDirectoryPath) throws IOException, RarException {
        File dstDiretory = new File(dstDirectoryPath);
        if (!dstDiretory.exists()) {
            dstDiretory.mkdirs();
        }
        Archive a = new Archive(new File(rarPath));
        ArrayList<String> filepathList = new ArrayList<>();
        if (a != null) {
            a.getMainHeader().print(); //打印文件信息.
            FileHeader fh = a.nextFileHeader();
//            fileName=  fh.getFileNameW().trim();
//            if(!existZH(fileName)){
//                fileName = fh.getFileNameString().trim();
//            }
            while (fh != null) {
                // 判断编码,解决中文乱码的问题
                String localpath = fh.isUnicode() ? fh.getFileNameW() : fh.getFileNameString();
                //文件
                File out = new File(dstDirectoryPath + File.separator + localpath.trim());
                String outPath = out.getAbsolutePath().replaceAll("\\*", "/");;
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('\\')));
                if(!file.exists())
                {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if(new File(outPath).isDirectory())
                {
                    break;
                }
                //输出文件路径信息
                if(fh != null){
                    filepathList.add(out.getAbsolutePath());
                }
                FileOutputStream os = new FileOutputStream(out);
                a.extractFile(fh, os);
                os.close();
                fh = a.nextFileHeader();
            }
        }
        a.close();
        System.out.println(filepathList);
    }

}
