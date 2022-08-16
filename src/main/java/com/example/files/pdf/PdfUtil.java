package com.example.files.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import java.io.FileOutputStream;

public class PdfUtil {

    /**
     * 合并pdf
     * @param files 需要合并的pdf路径
     * @param newfile 合并成新的文件的路径
     */
    public static boolean mergePdfFiles(String[] files, String newfile) {
        boolean retValue = false;
        Document document = null;
        PdfCopy copy = null;
        PdfReader reader = null;
        try {
            document = new Document(new PdfReader(files[0]).getPageSize(1));
            copy = new PdfCopy(document, new FileOutputStream(newfile));
            document.open();
            for (int i = 0; i < files.length; i++) {
                reader = new PdfReader(files[i]);
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
                reader.close();
            }
            retValue = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (copy != null) {
                copy.close();
            }
            if (document != null) {
                document.close();
            }
        }
        return retValue;
    }

    public static void main(String[] args) {
        String[] files = {"/Users/yannbai/Documents/tencent/test/tpPdf3.pdf","/Users/yannbai/Documents/tencent/test/toPdf2.pdf" };
        String savepath = "/Users/yannbai/Documents/tencent/test/toPdfMerge.pdf";
        boolean b = mergePdfFiles(files, savepath);
        System.out.println(b);
    }
}