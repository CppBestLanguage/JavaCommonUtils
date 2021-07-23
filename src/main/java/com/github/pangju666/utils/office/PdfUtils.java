package com.github.pangju666.utils.office;

import com.github.pangju666.utils.sys.FileUtils;

import com.spire.pdf.*;
import com.spire.pdf.annotations.PdfAnnotationCollection;
import com.spire.pdf.bookmarks.PdfBookmark;
import com.spire.pdf.bookmarks.PdfBookmarkCollection;
import com.spire.pdf.general.PdfDestination;
import com.spire.pdf.general.find.PdfTextFind;
import com.spire.pdf.graphics.PdfMargins;
import com.spire.pdf.graphics.PdfTemplate;
import com.spire.pdf.widget.PdfPageCollection;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.InvalidPathException;
import java.util.*;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;

/**
 * PDF工具类
 *
 * @author pangju666
 * @version 1.0 2021-7-21
 * @since 1.0
 */
public class PdfUtils {
    private static final Set<String> FILE_EXTENSION_SET = new HashSet<>(Arrays.asList("pdf", "PDF"));
    private static final String SPLIT_SUFFIX_NAME = "split";

    public static boolean isPDF(String filePath) {
        String fileExtension = FilenameUtils.getExtension(filePath);
        return FILE_EXTENSION_SET.contains(fileExtension);
    }

    /**
     * 获取文档对象
     *
     * @param inputStream 文档输入流
     */
    public static PdfDocument getDocument(InputStream inputStream) {
        PdfDocument document = new PdfDocument();
        document.loadFromStream(inputStream);
        return document;
    }

    /**
     * 获取文档对象
     *
     * @param documentFile 文档文件
     */
    public static PdfDocument getDocument(File documentFile) {
        return getDocument(documentFile.getAbsolutePath());
    }

    /**
     * 获取文档对象
     *
     * @param documentPath 文档路径
     */
    public static PdfDocument getDocument(String documentPath) {
        // 获取文件后缀
        String fileExtension = FilenameUtils.getExtension(documentPath);
        // 判断是否为pdf文件
        if (!FILE_EXTENSION_SET.contains(fileExtension)) {
            throw new InvalidPathException(documentPath, "不是合法的pdf文件路径");
        }
        PdfDocument document = new PdfDocument();
        if (FileUtils.isExist(documentPath)) {
            // 加载源PDF文档
            document.loadFromFile(documentPath, FileFormat.PDF);
        }
        return document;
    }

    /**
     * 获取文档封面
     *
     * @param documentStream 文档输入流
     */
    public static BufferedImage getDocumentCover(InputStream documentStream) {
        return getDocumentCover(getDocument(documentStream));
    }

    /**
     * 获取文档封面
     *
     * @param documentFile 文档文件
     */
    public static BufferedImage getDocumentCover(File documentFile) {
        return getDocumentCover(getDocument(documentFile));
    }

    /**
     * 获取文档封面
     *
     * @param documentPath 文档路径
     */
    public static BufferedImage getDocumentCover(String documentPath) {
        return getDocumentCover(getDocument(documentPath));
    }

    /**
     * 获取文档封面
     *
     * @param pdfDocument 文档对象
     */
    public static BufferedImage getDocumentCover(PdfDocument pdfDocument) {
        BufferedImage image = pdfDocument.saveAsImage(0);
        pdfDocument.close();
        return image;
    }

    /**
     * 获取文档元数据
     *
     * @param documentStream 文档流
     * @return 文档元数据映射
     */
    public static Map<String, Object> getDocumentInformation(InputStream documentStream) {
        return getDocumentInformation(getDocument(documentStream));
    }

    /**
     * 获取文档元数据
     *
     * @param documentFile 文档文件
     * @return 文档元数据映射
     */
    public static Map<String, Object> getDocumentInformation(File documentFile) {
        return getDocumentInformation(getDocument(documentFile));
    }

    /**
     * 获取文档元数据
     *
     * @param documentPath 文档路径
     * @return 文档元数据映射
     */
    public static Map<String, Object> getDocumentInformation(String documentPath) {
        PdfDocument document = getDocument(documentPath);
        Map<String, Object> documentInfo = getDocumentInformation(document);
        document.close();
        return documentInfo;
    }

    /**
     * 获取文档元数据
     *
     * @param document pdf文档对象
     * @return 文档元数据映射
     */
    public static Map<String, Object> getDocumentInformation(PdfDocument document) {
        Map<String, Object> docInfoMap = new HashMap<>(9);
        PdfDocumentInformation docInfo = document.getDocumentInformation();

        docInfoMap.put("Author", docInfo.getAuthor());
        docInfoMap.put("CreationDate", docInfo.getCreationDate());
        docInfoMap.put("Creator", docInfo.getCreator());
        docInfoMap.put("Keywords", docInfo.getKeywords());
        docInfoMap.put("Producer", docInfo.getProducer());
        docInfoMap.put("Title", docInfo.getTitle());
        docInfoMap.put("Subject", docInfo.getSubject());
        docInfoMap.put("MetaData", docInfo.getMetaData());
        docInfoMap.put("pageCount", document.getPages().getCount());

        return docInfoMap;
    }

    /**
     * 在文档中查询文字，默认查询整个单词，大小写敏感
     *
     * @param document 文档
     * @param searchText 待查找文字（支持正则表达式）
     * @return 查找结果
     */
    public static List<PdfTextFind> findText(PdfDocument document, String searchText) {
        return findText(document, searchText, true, false);
    }

    /**
     * 在文档中查询文字
     *
     * @param document 文档
     * @param searchText 待查找文字（支持正则表达式）
     * @param isSearchWholeWord 是否查询整个单词（是否允许跨行）
     * @param ignoreCase 是否忽略大小写
     * @return 查找结果
     */
    public static List<PdfTextFind> findText(PdfDocument document, String searchText,
                                             boolean isSearchWholeWord, boolean ignoreCase) {
        List<PdfTextFind> textFinds = new ArrayList<>();
        PdfPageCollection pages = document.getPages();
        //遍历文档页面
        for (Object object : pages) {
            PdfPageBase page = (PdfPageBase) object;
            PdfTextFind[] result = page.findText(searchText, isSearchWholeWord, ignoreCase).getFinds();
            textFinds.addAll(Arrays.asList(result));
        }
        return textFinds;
    }

    /**
     * 根据页码切分文档
     *
     * @param sourceDocumentStream 源文档输入流
     * @param fileStorePath 文件保存路径
     * @param fileName 文件名称
     * @param splitPage 切分页数
     * @return 切割结果
     */
    public static List<File> splitDocumentByPages(InputStream sourceDocumentStream, File fileStorePath, String fileName, int splitPage) throws IOException {
        return splitDocumentByPages(getDocument(sourceDocumentStream), fileStorePath, fileName, splitPage);
    }

    /**
     * 根据页码切分文档
     *
     * @param sourceDocumentFile 源文档文件
     * @param splitStoreDirectory 文件保存路径
     * @param fileName 文件名称
     * @param splitPage 切分页数
     * @return 切割结果
     */
    public static List<File> splitDocumentByPages(File sourceDocumentFile, File splitStoreDirectory, String fileName, int splitPage) throws IOException {
        return splitDocumentByPages(getDocument(sourceDocumentFile), splitStoreDirectory, fileName, splitPage);
    }

    /**
     * 根据页码切分文档
     *
     * @param sourceDocumentPath 源文档路径
     * @param splitStoreDirectory 文件保存路径
     * @param fileName 文件名称
     * @param splitPage 切分页数
     * @return 切割结果
     */
    public static List<File> splitDocumentByPages(String sourceDocumentPath, File splitStoreDirectory, String fileName, int splitPage) throws IOException {
        return splitDocumentByPages(getDocument(sourceDocumentPath), splitStoreDirectory, fileName, splitPage);
    }

    /**
     * 根据页码切分文档
     *
     * @param sourceDocument 源文档
     * @param splitStoreDirectory 文件保存路径
     * @param fileName 文件名称
     * @param splitPage 切分页数
     * @return 切割结果
     */
    public static List<File> splitDocumentByPages(PdfDocument sourceDocument, File splitStoreDirectory, String fileName, int splitPage) throws IOException {
        FileUtils.forceMkdir(splitStoreDirectory);
        List<File> outputFileList = new ArrayList<>();
        int totalPages = sourceDocument.getPages().getCount();
        for (int i = 1; i <= totalPages; i += splitPage) {
            String splitFileName = FileUtils.getFileNameWithoutType(fileName) + "-" + SPLIT_SUFFIX_NAME + "-" + i + "." + FileFormat.PDF.getName();
            File splitFile = FileUtils.getFile(splitStoreDirectory, splitFileName);
            PdfDocument document = getDocument(splitFile);
            copyDocumentByPages(sourceDocument, document, i, i + splitPage - 1);
            document.saveToFile(splitFile.getAbsolutePath());
            document.close();
            outputFileList.add(splitFile);
        }
        sourceDocument.close();
        return outputFileList;
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param inputDocumentStream 源文档输入流
     * @param outputDocumentStream 目标文档输出流
     * @param startPage 起始页码
     * @param endPage 结束页码
     */
    public static void copyDocumentByPages(InputStream inputDocumentStream, OutputStream outputDocumentStream, int startPage, int endPage) {
        copyDocumentByPages(inputDocumentStream, outputDocumentStream, getPagesByRange(startPage, endPage));
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param inputDocumentStream 源文档输入流
     * @param outputDocumentStream 目标文档输出流
     * @param pageList 页码列表
     */
    public static void copyDocumentByPages(InputStream inputDocumentStream, OutputStream outputDocumentStream, List<Integer> pageList) {
        PdfDocument sourceDocument = new PdfDocument();
        PdfDocument targetDocument = new PdfDocument();
        // 加载源PDF文档
        sourceDocument.loadFromStream(inputDocumentStream);
        // 复制文档内容
        copyDocumentByPages(sourceDocument, targetDocument, pageList);
        //保存文档
        targetDocument.saveToStream(outputDocumentStream, FileFormat.PDF);
        sourceDocument.close();
        targetDocument.close();
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocumentFile 源文档
     * @param targetDocumentFile 目标文档
     * @param startPage 起始页码
     * @param endPage 结束页码
     */
    public static void copyDocumentByPages(File sourceDocumentFile, File targetDocumentFile, int startPage, int endPage) {
        copyDocumentByPages(sourceDocumentFile, targetDocumentFile, getPagesByRange(startPage, endPage));
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocumentFile 源文档
     * @param targetDocumentFile 目标文档
     * @param pageList 页码列表
     */
    public static void copyDocumentByPages(File sourceDocumentFile, File targetDocumentFile, List<Integer> pageList) {
        copyDocumentByPages(sourceDocumentFile.getAbsolutePath(), targetDocumentFile.getAbsolutePath(), pageList);
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocumentPath 源文档
     * @param targetDocumentPath 目标文档
     * @param startPage 起始页码
     * @param endPage 结束页码
     */
    public static void copyDocumentByPages(String sourceDocumentPath, String targetDocumentPath, int startPage, int endPage) {
        copyDocumentByPages(sourceDocumentPath, targetDocumentPath, getPagesByRange(startPage, endPage));
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocumentPath 源文档
     * @param targetDocumentPath 目标文档
     * @param pageList 页码列表
     */
    public static void copyDocumentByPages(String sourceDocumentPath, String targetDocumentPath, List<Integer> pageList) {
        PdfDocument sourceDocument = getDocument(sourceDocumentPath);
        PdfDocument targetDocument = new PdfDocument();
        // 复制文档内容
        copyDocumentByPages(sourceDocument, targetDocument, pageList);
        //保存文档
        targetDocument.saveToFile(targetDocumentPath, FileFormat.PDF);
        sourceDocument.close();
        targetDocument.close();
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocument 源文档
     * @param targetDocument 目标文档
     * @param startPage 起始页码
     * @param endPage 结束页码
     */
    public static void copyDocumentByPages(PdfDocument sourceDocument, PdfDocument targetDocument, int startPage, int endPage) {
        copyDocumentByPages(sourceDocument, targetDocument, getPagesByRange(startPage, endPage));
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocument 源文档
     * @param targetDocument 目标文档
     * @param pageList 页码列表
     */
    public static void copyDocumentByPages(PdfDocument sourceDocument, PdfDocument targetDocument, List<Integer> pageList) {
        // 页码排序
        if (pageList.size() > 1) {
            pageList.sort(Integer::compareTo);
        }

        // 获取文档页码集合
        PdfPageCollection sourcePages = sourceDocument.getPages();
        PdfPageCollection targetPages = targetDocument.getPages();

        // 遍历待复制页码列表
        for (Integer pageNumber : pageList) {
            // 判断是否大于源文档结束页码
            if (pageNumber <= sourcePages.getCount()) {
                PdfPageBase sourcePage = sourcePages.get(pageNumber - 1);
                PdfPageBase targetPage = targetPages.add(sourcePage.getSize(), new PdfMargins(0, 0));
                // 拷贝源页面标注信息至目标页面
                PdfAnnotationCollection annotations = sourcePage.getAnnotationsWidget();
                targetPage.setAnnotationsWidget(annotations);
                // 清理源页面标注信息，防止绘制页面时，视为图片处理
                annotations.clear();
                // 绘制目标页面
                sourcePage.createTemplate().draw(targetPage, new Point2D.Float(0, 0));
            }
        }
        // 复制文档元属性
        copyDocumentInformation(sourceDocument, targetDocument);
        // 复制文档书签
        copyBookMarks(sourceDocument, targetDocument, pageList);
    }

    /**
     * 复制文档书签至目标文档
     *
     * @param sourceDocument 源文档
     * @param targetDocument 目标文档
     * @param pageList 要复制的页码列表
     */
    protected static void copyBookMarks(PdfDocument sourceDocument, PdfDocument targetDocument, List<Integer> pageList) {
        // 获取文档标注
        PdfBookmarkCollection sourceBookmarks = sourceDocument.getBookmarks();
        PdfBookmarkCollection targetBookmarks = targetDocument.getBookmarks();

        // 遍历源文档标注
        for (Object object : sourceBookmarks) {
            PdfBookmark sourceBookmark = (PdfBookmark) object;
            // 获取文档标注所指向页面
            PdfDestination sourceDestination = sourceBookmark.getDestination();
            // 判断指向页面是否需要复制
            int pageIndex = pageList.indexOf(sourceDestination.getPageNumber() + 1);
            if (pageIndex != -1) {
                // 若需要复制，则拷贝标注信息至新文档
                PdfBookmark targetBookmark = targetBookmarks.add(sourceBookmark.getTitle());
                targetBookmark.setColor(sourceBookmark.getColor());
                targetBookmark.setDisplayStyle(sourceBookmark.getDisplayStyle());
                targetBookmark.setExpandBookmark(sourceBookmark.getExpandBookmark());
                targetBookmark.setAction(sourceBookmark.getAction());

                PdfDestination targetDestination = new PdfDestination(targetDocument.getPages().get(pageIndex));
                targetDestination.setRectangle(sourceDestination.getRectangle());
                targetDestination.setLocation(sourceDestination.getLocation());
                targetDestination.setMode(sourceDestination.getMode());
                targetDestination.setZoom(sourceDestination.getZoom());
                targetBookmark.setDestination(targetDestination);
            }
        }
    }

    /**
     * 复制文档元数据至目标文档
     *
     * @param sourceDocument 源文档
     * @param targetDocument 目标文档
     */
    protected static void copyDocumentInformation(PdfDocument sourceDocument, PdfDocument targetDocument) {
        // 复制文档属性
        Map<String, Object> documentInformationMap = getDocumentInformation(sourceDocument);
        PdfDocumentInformation targetDocumentInformation = targetDocument.getDocumentInformation();

        targetDocumentInformation.setAuthor((String) documentInformationMap.get("Author"));
        targetDocumentInformation.setCreationDate((Date) documentInformationMap.get("CreationDate"));
        targetDocumentInformation.setCreator((String) documentInformationMap.get("Creator"));
        targetDocumentInformation.setKeywords((String) documentInformationMap.get("Keywords"));
        targetDocumentInformation.setProducer((String) documentInformationMap.get("Producer"));
        targetDocumentInformation.setTitle((String) documentInformationMap.get("Title"));
        targetDocumentInformation.setSubject((String) documentInformationMap.get("Subject"));
    }

    private static List<Integer> getPagesByRange(int startPage, int endPage) {
        List<Integer> pageList = new ArrayList<>();
        if (startPage == endPage) {
            return Collections.singletonList(startPage);
        }
        int page = startPage;
        while (page <= endPage) {
            pageList.add(page);
            ++page;
        }
        return pageList;
    }
}