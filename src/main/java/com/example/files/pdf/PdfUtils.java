package com.example.files.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * @author yannbai
 * @group blade_db
 * @date 2022/8/17
 * @Description
 */
public class PdfUtils {

    public static void main(String[] args) throws IOException {
        String filePath = "/Users/yannbai/Documents/tencent/test/md3.pdf";
        int numberOfPages = getNumberOfPages(filePath);
        System.out.println("该pdf总页数为：" + numberOfPages);
        String content = getContent(filePath);
        System.out.println(content);
    }
        /**
         * 通过PDFbox获取文章总页数
         *
         * @param filePath:文件路径
         * @return
         * @throws IOException
         */
        public static int getNumberOfPages(String filePath) throws IOException {
            PDDocument pdDocument = PDDocument.load(new File(filePath));
            int pages = pdDocument.getNumberOfPages();
            pdDocument.close();
            return pages;
        }
        /**
         * 通过PDFbox获取文章内容
         *
         * @param filePath
         * @return
         */
        public static String getContent(String filePath) throws IOException {
            PDFParser pdfParser = new PDFParser(new org.apache.pdfbox.io.RandomAccessFile(new File(filePath), "rw"));
            pdfParser.parse();
            PDDocument pdDocument = pdfParser.getPDDocument();
            String text = new PDFTextStripper().getText(pdDocument);
            pdDocument.close();

            return text;
        }

        /**
         * 通过PDFbox生成文件的缩略图
         *
         * @param filePath：文件路径
         * @param outPath：输出图片路径
         * @throws IOException
         */
        public static void getThumbnails(String filePath, String outPath) throws IOException {
            // 利用PdfBox生成图像
            PDDocument pdDocument = PDDocument.load(new File(filePath));
            PDFRenderer renderer = new PDFRenderer(pdDocument);

            // 构造图片
            BufferedImage imgTemp = renderer.renderImageWithDPI(0, 30, ImageType.RGB);
            // 设置图片格式
            Iterator<ImageWriter> it = ImageIO.getImageWritersBySuffix("png");
            // 将文件写出
            ImageWriter writer = it.next();
            ImageOutputStream imageout = ImageIO.createImageOutputStream(new FileOutputStream(outPath));
            writer.setOutput(imageout);
            writer.write(new IIOImage(imgTemp, null, null));
            imgTemp.flush();
            imageout.flush();
            imageout.close();
            pdDocument.close();
        }

        /**
         * PDF转图片 根据页码一页一页转
         *
         * @throws IOException imgType:转换后的图片类型 jpg,png
         */
        public static void PDFToImg(OutputStream sos, String fileUrl, int page, String imgType) throws IOException {
            PDDocument pdDocument = null;
            /* dpi越大转换后越清晰，相对转换速度越慢 */
            int dpi = 100;
            try {
                pdDocument = getPDDocument(fileUrl);
                PDFRenderer renderer = new PDFRenderer(pdDocument);
                int pages = pdDocument.getNumberOfPages();
                if (page <= pages && page >= 0) {
                    BufferedImage image = renderer.renderImageWithDPI(page, dpi);
                    ImageIO.write(image, imgType, sos);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (pdDocument != null) {
                    pdDocument.close();
                }
            }
        }

        private static PDDocument getPDDocument(String fileUrl) throws IOException {
            File file = new File(fileUrl);
            FileInputStream inputStream = new FileInputStream(file);
            return PDDocument.load(inputStream);
        }
    }