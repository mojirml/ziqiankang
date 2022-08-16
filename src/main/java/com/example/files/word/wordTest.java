package com.example.files.word;

/**
 * @author yannbai
 * @group blade_db
 * @date 2022/8/17
 * @Description
 */

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class wordTest {

    public static void main(String[] args) throws Exception {
        wordToPdf();

        //try (
        //        InputStream docxInputStream = new FileInputStream("/Users/yannbai/Documents/tencent/test/introMe.doc");
        //        OutputStream outputStream = new FileOutputStream("/Users/yannbai/Documents/tencent/test/tpPdf3.pdf")
        //) {
        //    IConverter converter = LocalConverter.builder().build();
        //    converter.convert(docxInputStream).as(DocumentType.DOC).to(outputStream).as(DocumentType.PDF).execute();
        //    System.out.println("word to pdf success");
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
    }
    private static boolean license = false;

    public static InputStream getInputStream(String path,ClassLoader classLoader) throws IOException {
        InputStream  is = classLoader.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException(" cannot be opened because it does not exist");
        } else {
            return is;
        }
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;

        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable var3) {
        }

        if (cl == null) {
            cl = wordTest.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable var2) {
                }
            }
        }

        return cl;
    }

    public static void wordToPdf() throws Exception {
        FileOutputStream os = null;
        try {
            //凭证 不然切换后有水印
            File file1 = new File(
                    "/Users/yannbai/projects/yann/ziqiankang/src/main/resources/license.xml");
            InputStream is = new FileInputStream(file1);
            License aposeLic = new License();
            aposeLic.setLicense(is);
            license = true;
            if (!license) {
                System.out.println("License验证不通过...");
                return ;
            }
            //生成一个空的PDF文件
            File file = new File("/Users/yannbai/Documents/tencent/test/tpPdf3.pdf");
            os = new FileOutputStream(file);
            //要转换的word文件
            Document doc = new Document("/Users/yannbai/Documents/tencent/test/introMe.doc");
            doc.save(os, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}