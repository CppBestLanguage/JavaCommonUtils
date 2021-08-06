package com.github.pangju666.utils.office;

import com.github.pangju666.utils.sys.FileUtils;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.action.PDAction;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;

/**
 * PDF工具类
 *
 * @author pangju666
 * @version 1.0 2021-7-21
 * @since 1.0
 */
public class PdfUtils {
    private static final Set<String> PDF_FILE_EXTENSION_SET = new HashSet<>(Arrays.asList("pdf", "PDF"));
    private static final String PDF_FILE_EXTENSION = ".pdf";

    public static boolean isPdfFile(File file) {
        return isPdfFile(file.getName());
    }

    public static boolean isPdfFile(String filePath) {
        String fileExtension = FilenameUtils.getExtension(filePath);
        return PDF_FILE_EXTENSION_SET.contains(fileExtension);
    }

    public static PDDocument createFromDocument(PDDocument sourceDocument) {
        PDDocument document = new PDDocument();
        // 复制文档属性
        document.getDocument().setVersion(sourceDocument.getVersion());
        document.setDocumentInformation(sourceDocument.getDocumentInformation());
        document.getDocumentCatalog().setViewerPreferences(sourceDocument.getDocumentCatalog().getViewerPreferences());
        return document;
    }

    /**
     * 获取文档对象
     *
     * @param inputStream 文档输入流
     */
    public static PDDocument getDocument(InputStream inputStream) throws IOException {
        return PDDocument.load(inputStream);
    }

    /**
     * 获取文档对象
     *
     * @param documentPath 文档路径
     */
    public static PDDocument getDocument(String documentPath) throws IOException {
        return getDocument(FileUtils.getFile(documentPath));
    }

    /**
     * 获取文档对象
     *
     * @param documentFile 文档文件
     */
    public static PDDocument getDocument(File documentFile) throws IOException {
        String filePath = documentFile.getAbsolutePath();
        // 获取文件后缀
        String fileExtension = FilenameUtils.getExtension(filePath);
        // 判断是否为pdf文件
        if (!PDF_FILE_EXTENSION_SET.contains(fileExtension)) {
            throw new InvalidPathException(filePath, "不是合法的pdf文件路径");
        }

        if (!documentFile.exists() || !documentFile.isFile()) {
            throw new FileNotFoundException("文件不存在， 请检查路径是否正确");
        }
        return PDDocument.load(documentFile);
    }

    public static List<BufferedImage> getDocumentPagesAsImage(PDDocument document) throws IOException {
        List<BufferedImage> pageImages = new ArrayList<>();
        PDFRenderer renderer = new PDFRenderer(document);
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            pageImages.add(getDocumentPageAsImage(renderer, i));
        }
        return pageImages;
    }

    public static BufferedImage getDocumentPageAsImage(PDFRenderer renderer, Integer pageIndex) throws IOException {
        return renderer.renderImage(pageIndex);
    }

    public static BufferedImage getDocumentPageAsImage(PDDocument document, Integer pageIndex) throws IOException {
        PDFRenderer renderer = new PDFRenderer(document);
        return renderer.renderImage(pageIndex);
    }

    /**
     * 根据页码切分文档
     *
     * @param sourceDocument 源文档
     * @param splitPage      切分页数
     * @return 切割结果
     */
    public static List<PDDocument> splitDocumentByPages(PDDocument sourceDocument, int splitPage) throws IOException {
        List<PDDocument> outputFileList = new ArrayList<>();
        int totalPages = sourceDocument.getPages().getCount();
        for (int pageNumber = 1; pageNumber <= totalPages; pageNumber += splitPage) {
            PDDocument document = createFromDocument(sourceDocument);
            copyDocumentByPages(sourceDocument, document, pageNumber, pageNumber + splitPage - 1);
            outputFileList.add(document);
        }
        return outputFileList;
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocumentPath 源文档
     * @param targetDocumentPath 目标文档
     * @param startPage          起始页码
     * @param endPage            结束页码
     */
    public static void copyDocumentByPages(String sourceDocumentPath, String targetDocumentPath, int startPage, int endPage) throws IOException {
        copyDocumentByPages(sourceDocumentPath, targetDocumentPath, getPagesByRange(startPage, endPage));
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocumentPath 源文档
     * @param targetDocumentPath 目标文档
     * @param pageList           页码列表
     */
    public static void copyDocumentByPages(String sourceDocumentPath, String targetDocumentPath, List<Integer> pageList) throws IOException {
        copyDocumentByPages(FileUtils.getFile(sourceDocumentPath), FileUtils.getFile(targetDocumentPath), pageList);
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocumentFile 源文档
     * @param targetDocumentFile 目标文档
     * @param startPage          起始页码
     * @param endPage            结束页码
     */
    public static void copyDocumentByPages(File sourceDocumentFile, File targetDocumentFile, int startPage, int endPage) throws IOException {
        copyDocumentByPages(sourceDocumentFile, targetDocumentFile, getPagesByRange(startPage, endPage));
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocumentFile 源文档
     * @param targetDocumentFile 目标文档
     * @param pageList           页码列表
     */
    public static void copyDocumentByPages(File sourceDocumentFile, File targetDocumentFile, List<Integer> pageList) throws IOException {
        try (PDDocument sourceDocument = getDocument(sourceDocumentFile);
             PDDocument targetDocument = createFromDocument(sourceDocument)) {
            copyDocumentByPages(sourceDocument, targetDocument, pageList);
            targetDocument.save(targetDocumentFile);
        }
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocument 源文档
     * @param targetDocument 目标文档
     * @param startPage      起始页码
     * @param endPage        结束页码
     */
    public static void copyDocumentByPages(PDDocument sourceDocument, PDDocument targetDocument, int startPage, int endPage) throws IOException {
        copyDocumentByPages(sourceDocument, targetDocument, getPagesByRange(startPage, endPage));
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocument 源文档
     * @param targetDocument 目标文档
     * @param pageList       页码列表
     */
    public static void copyDocumentByPages(PDDocument sourceDocument, PDDocument targetDocument, List<Integer> pageList) throws IOException {
        int maxPageNumber = sourceDocument.getNumberOfPages();

        // 页码排序并过滤
        List<Integer> pageNumberList = pageList.stream().distinct()
                .filter(pageNumber -> pageNumber >= 1 && pageNumber <= maxPageNumber)
                .sorted(Integer::compareTo).collect(Collectors.toList());

        for (Integer pageNumber : pageNumberList) {
            PDPage sourcePage = sourceDocument.getPage(pageNumber - 1);
            PDPage targetPage = targetDocument.importPage(sourcePage);
            targetPage.setResources(sourcePage.getResources());
            copyAnnotations(targetPage);
        }
    }

    protected static void copyAnnotations(PDPage sourcePage) throws IOException {
        List<PDAnnotation> annotations = sourcePage.getAnnotations();
        for (PDAnnotation annotation : annotations) {
            if (annotation instanceof PDAnnotationLink) {
                PDAnnotationLink link = (PDAnnotationLink) annotation;
                PDDestination destination = link.getDestination();
                PDAction action = link.getAction();
                if (destination == null && action instanceof PDActionGoTo) {
                    destination = ((PDActionGoTo) action).getDestination();
                }
                if (destination instanceof PDPageDestination) {
                    ((PDPageDestination) destination).setPage(null);
                }
            }
            annotation.setPage(null);
        }
    }

    /*protected static void copyBookMarks(PDDocument sourceDocument, PDDocument targetDocument, List<Integer> pageList) {
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
    }*/

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