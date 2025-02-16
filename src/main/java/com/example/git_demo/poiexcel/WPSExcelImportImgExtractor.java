package com.example.git_demo.poiexcel;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * copy 让AI帮我用java实现EasyExel读取图片—支持WPS嵌入图片 javastarboy@u012723183
 */
public class WPSExcelImportImgExtractor {

    /**
     * 提取 Excel 中的图片，并返回单元格与图片路径的映射
     * <p>
     * WPS 的内置函数无法提取图片，需要使用第三方库解析XML文件。
     * WPS 内嵌图片会把图片做DISPIMG函数处理，如=DISPIMG("ID_9D6E8C240C8945178DFF238232B217BF",1)
     * 我们可以将.xlsx 文件后缀改成.zip后解压，即可看到
     * 在xl路径下的cellimages.xml文件中，可以看到函数中的id值
     * 并且在cellimages.xml.rels中可以看到函数与图片之间的关系，而图片就位于xl/media路径下
     *
     * @param inputStream Excel 文件的输入流
     * @param outputDir   图片保存的目标目录
     * @return 单元格位置（如 A1）与图片路径的映射
     * @throws Exception 异常
     */
    public Map<String, String> extractImages(InputStream inputStream, String outputDir) throws Exception {
        Map<String, String> cellImageMap = new HashMap<>();
        Map<String, String> relsMap = new HashMap<>();
        Map<String, byte[]> imagesData = new HashMap<>();

        ZipInputStream zis = new ZipInputStream(inputStream);
        ZipEntry zipEntry;
        ByteArrayOutputStream baos = null;
        String cellImagesXml = null;
        String cellImagesRelsXml = null;

        // 首先遍历所有的Zip条目，找到需要的XML和图片文件
        while ((zipEntry = zis.getNextEntry()) != null) {
            String entryName = zipEntry.getName();
            if ("xl/cellimages.xml".equals(entryName)) {
                baos = new ByteArrayOutputStream();
                IOUtils.copy(zis, baos);
                cellImagesXml = baos.toString("UTF-8");
                baos.close();
            } else if ("xl/_rels/cellimages.xml.rels".equals(entryName)) {
                baos = new ByteArrayOutputStream();
                IOUtils.copy(zis, baos);
                cellImagesRelsXml = baos.toString("UTF-8");
                baos.close();
            } else if (entryName.startsWith("xl/media/")) {
                byte[] imageBytes = IOUtils.toByteArray(zis);
                String imageName = entryName.substring("xl/media/".length());
                imagesData.put(imageName, imageBytes);
            }
            zis.closeEntry();
        }
        zis.close();

        // 解析cellimages.xml.rels，建立rId到图片文件名的映射
        if (cellImagesRelsXml != null) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document relsDoc = builder.parse(new ByteArrayInputStream(cellImagesRelsXml.getBytes("UTF-8")));
            NodeList relNodes = relsDoc.getElementsByTagName("Relationship");
            for (int i = 0; i < relNodes.getLength(); i++) {
                Element relElement = (Element) relNodes.item(i);
                String rId = relElement.getAttribute("Id");
                // e.g., "media/image1.png"
                String target = relElement.getAttribute("Target");
                relsMap.put(rId, target.substring("media/".length()));
            }
        }

        // 解析cellimages.xml，提取图片与单元格的关系
        if (cellImagesXml != null) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document cellImagesDoc = builder.parse(new ByteArrayInputStream(cellImagesXml.getBytes("UTF-8")));
            NodeList cellImageNodes = cellImagesDoc.getElementsByTagName("etc:cellImage");
            for (int i = 0; i < cellImageNodes.getLength(); i++) {
                Element cellImageElement = (Element) cellImageNodes.item(i);
                // 获取图片 name 属性，如 "ID_6C483737A6AC427DAA4E4974252FB8A8"
                Element picElement = (Element) cellImageElement.getElementsByTagName("xdr:pic").item(0);
                Element cNvPr = (Element) picElement.getElementsByTagName("xdr:cNvPr").item(0);
                // e.g., "ID_6C483737A6AC427DAA4E4974252FB8A8"
                String imageName = cNvPr.getAttribute("name");
                // 获取 r:embed 属性，如 "rId1"
                Element blipFill = (Element) picElement.getElementsByTagName("xdr:blipFill").item(0);
                Element blip = (Element) blipFill.getElementsByTagName("a:blip").item(0);
                // e.g., "rId1"
                String rId = blip.getAttribute("r:embed");
                // e.g., "image1.png"
                String imageFileName = relsMap.get(rId);

                // 保存图片到本地
                byte[] imageBytes = imagesData.get(imageFileName);
                if (imageBytes != null) {
//                    String savedImagePath = outputDir + File.separator + imageFileName;
//                    FileOutputStream fos = new FileOutputStream(savedImagePath);
//                    fos.write(imageBytes);
//                    fos.close();

                    // 这里将图片名存储到一个列表中，后续与数据行进行关联
                    cellImageMap.put(imageName, "ossPath");
                }
            }
        }

        return cellImageMap;
    }
}

