package com.example.files.zip;

/**
 * @author yannbai
 * @group blade_db
 * @date 2022/8/16
 * @Description
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * zip压缩工具类
 *  @className: UnzipUtils
 *  @author: gxs
 *  @date: 2021/6/5 9:13
 *  @version: 1.0
 */
public class UnzipUtil {

    private static final int BUFFER = 1024;
    private static final String CODING_UTF8 = "UTF-8";
    private static final String CODING_GBK = "GBK";

    /**
     * 解压入口方法
     *
     * @param zipPath zip文件物理地址
     * @param unzipPath 解压后存放路径
     * @throws Exception
     */
    public static void decompress(String zipPath, String unzipPath)  throws Exception {
        //解压缩执行方法
        decompressFile(new File(zipPath), new File(unzipPath));
    }

    /**
     * 解压缩执行方法
     *
     * @param srcFile 压缩文件File实体
     * @param destFile 解压路径File实体
     * @throws Exception
     */
    public static void decompressFile(File srcFile, File destFile) throws Exception {
        //创建数据输入流
        CheckedInputStream cis = new CheckedInputStream(new FileInputStream(srcFile), new CRC32());
        //创建压缩输入流
        ZipInputStream zis = new ZipInputStream(cis, Charset.forName(CODING_UTF8));
        //异常捕获的方式判断编码格式
        try {
            //判断代码，如果此代码未抛出异常，则表示编码为UTF-8
            zis.getNextEntry().getName();
        } catch (Exception e) {
            //如果乱码会抛异常，抛异常重新创建输入流，重新设置编码格式
            cis = new CheckedInputStream(new FileInputStream(srcFile), new CRC32());
            zis = new ZipInputStream(cis, Charset.forName(CODING_GBK));
        }
        //解压zip
        decompressZis(destFile, zis);
        //关闭流
        zis.close();

    }

    /**
     * 文件 解压缩执行方法
     *
     * @param destFile 目标文件
     * @param zis ZipInputStream
     * @throws Exception
     */
    private static void decompressZis(File destFile, ZipInputStream zis)   throws Exception {
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            //获取当前的ZIP条目路径
            String dir = destFile.getPath() + File.separator + entry.getName();
            System.out.println(dir);
            File dirFile = new File(dir);
            //递归检查文件路径，路径上没有文件夹则创建，保证整条路径在本地存在
            fileProber(dirFile);
            //判断是否是文件夹
            if (entry.isDirectory()) {
                //如果是，创建文件夹
                dirFile.mkdirs();
                System.out.println(dirFile.getName());
            } else {
                //如果不是文件夹，数据流输出，生成文件
                decompressFile(dirFile, zis);
            }
            //关闭当前的ZIP条目并定位流
            zis.closeEntry();
        }
    }

    /**
     * 文件探针，当父目录不存在时，创建目录
     *
     * @param dirFile ZIP条目路径
     */
    private static void fileProber(File dirFile) {
        //获取此路径的父目录
        File parentFile = dirFile.getParentFile();
        //判断是否存在
        if (!parentFile.exists()) {
            // 递归寻找上级目录
            fileProber(parentFile);
            //直至存在，递归执行创建文件夹
            parentFile.mkdir();
        }

    }

    /**
     * 生成文件
     *
     * @param destFile 目标文件
     * @param zis ZipInputStream
     * @throws Exception
     */
    private static void decompressFile(File destFile, ZipInputStream zis) throws Exception {
        //创建输出流
        BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream(destFile));
        //转成byte数组
        int count;
        byte[] data = new byte[BUFFER];
        //读取并写入文件
        while ((count = zis.read(data, 0, BUFFER)) != -1) {
            bos.write(data, 0, count);
        }
        //关闭数据流
        bos.close();
    }

    public static void main(String[] args){
        try {
            decompress("/Users/yannbai/Documents/tencent/test/360压缩文件.zip", "/Users/yannbai/Downloads");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}