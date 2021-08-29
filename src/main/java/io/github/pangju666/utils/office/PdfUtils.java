package io.github.pangju666.utils.office;

import io.github.pangju666.utils.io.FileUtils;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFCloneUtility;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.PageExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.action.PDAction;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.rendering.PDFRenderer;

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
        return getDocumentPagesAsImage(document, 72);
    }

    public static List<BufferedImage> getDocumentPagesAsImage(PDDocument document, float dpi) throws IOException {
        List<BufferedImage> pageImages = new ArrayList<>();
        getDocumentPagesAsImage(document, dpi, ((bufferedImage, index) -> pageImages.add(bufferedImage)));
        return pageImages;
    }

    public static void getDocumentPagesAsImage(PDDocument document,
                                               PageAction<BufferedImage> action) throws IOException {
        getDocumentPagesAsImage(document, 72, action);
    }

    public static void getDocumentPagesAsImage(PDDocument document, float dpi,
                                               PageAction<BufferedImage> action) throws IOException {
        PDFRenderer renderer = new PDFRenderer(document);
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            BufferedImage image = renderer.renderImageWithDPI(i, dpi);
            action.apply(image, i);
        }
    }

    public static BufferedImage getDocumentPageAsImage(PDDocument document, Integer pageIndex) throws IOException {
        return getDocumentPageAsImage(document, pageIndex, 72);
    }

    public static BufferedImage getDocumentPageAsImage(PDDocument document, Integer pageIndex,
                                                       float dpi) throws IOException {
        PDFRenderer renderer = new PDFRenderer(document);
        return renderer.renderImageWithDPI(pageIndex, dpi);
    }

    public static void mergeDocumentByOptimizeResourcesMode(List<PDDocument> documents, File targetFile,
                                                            MemoryUsageSetting memoryUsageSetting) throws IOException {

        PDDocument result = mergeDocumentByOptimizeResourcesMode(documents, memoryUsageSetting);
        result.save(targetFile);
        result.close();

        for (PDDocument document : documents) {
            document.close();
        }
    }

    public static PDDocument mergeDocumentByOptimizeResourcesMode(List<PDDocument> documents,
                                                                  MemoryUsageSetting memoryUsageSetting) throws IOException {
        PDDocument result = new PDDocument(memoryUsageSetting);
        PDFCloneUtility cloner = new PDFCloneUtility(result);

        for (PDDocument document : documents) {
            for (PDPage page : document.getPages()) {
                PDPage newPage = new PDPage((COSDictionary) cloner.cloneForNewDocument(page.getCOSObject()));
                copyPage(page, newPage);
                result.addPage(newPage);
            }
        }
        return result;
    }

    public static void mergeDocumentByOptimizeResourcesMode(PDDocument source, PDDocument target) throws IOException {
        PDFCloneUtility cloner = new PDFCloneUtility(target);

        for (PDPage page : source.getPages()) {
            PDPage newPage = new PDPage((COSDictionary) cloner.cloneForNewDocument(page.getCOSObject()));
            copyPage(page, newPage);
            target.addPage(newPage);
        }
    }

    public static void mergeDocumentByLegacyMode(List<PDDocument> documents, File targetFile,
                                                 MemoryUsageSetting memoryUsageSetting) throws IOException {
        PDDocument result = mergeDocumentByLegacyMode(documents, memoryUsageSetting);
        result.save(targetFile);
        result.close();

        for (PDDocument document : documents) {
            document.close();
        }
    }

    public static PDDocument mergeDocumentByLegacyMode(List<PDDocument> documents,
                                                       MemoryUsageSetting memoryUsageSetting) throws IOException {
        PDFMergerUtility mergerUtility = new PDFMergerUtility();
        PDDocument result = new PDDocument(memoryUsageSetting);
        for (PDDocument document : documents) {
            mergerUtility.appendDocument(result, document);
        }
        return result;
    }

    public static void mergeDocumentByLegacyMode(PDDocument source, PDDocument target) throws IOException {
        PDFMergerUtility mergerUtility = new PDFMergerUtility();
        mergerUtility.appendDocument(target, source);
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
        int totalPages = sourceDocument.getNumberOfPages();
        for (int pageNumber = 1; pageNumber <= totalPages; pageNumber += splitPage) {
            outputFileList.add(copyDocumentByPages(sourceDocument, pageNumber, pageNumber + splitPage - 1));
        }
        return outputFileList;
    }

    public static PDDocument copyDocument(PDDocument document) throws IOException {
        return copyDocumentByPages(document, 1, document.getNumberOfPages());
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocumentPath 源文档
     * @param targetDocumentPath 目标文档
     * @param startPage          起始页码
     * @param endPage            结束页码
     */
    public static void copyDocumentByPages(String sourceDocumentPath, String targetDocumentPath,
                                           int startPage, int endPage) throws IOException {
        copyDocumentByPages(FileUtils.getFile(sourceDocumentPath),
                FileUtils.getFile(targetDocumentPath), startPage, endPage);
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocumentPath 源文档
     * @param targetDocumentPath 目标文档
     * @param pageList           页码列表
     */
    public static void copyDocumentByPages(String sourceDocumentPath, String targetDocumentPath,
                                           List<Integer> pageList) throws IOException {
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
    public static void copyDocumentByPages(File sourceDocumentFile, File targetDocumentFile,
                                           int startPage, int endPage) throws IOException {
        try (PDDocument sourceDocument = getDocument(sourceDocumentFile)) {
            PDDocument result = copyDocumentByPages(sourceDocument, startPage, endPage);
            result.save(targetDocumentFile);
        }
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocumentFile 源文档
     * @param targetDocumentFile 目标文档
     * @param pageList           页码列表
     */
    public static void copyDocumentByPages(File sourceDocumentFile, File targetDocumentFile,
                                           List<Integer> pageList) throws IOException {
        try (PDDocument sourceDocument = getDocument(sourceDocumentFile)) {
            PDDocument result = copyDocumentByPages(sourceDocument, pageList);
            result.save(targetDocumentFile);
        }
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocument 源文档
     * @param startPage      起始页码
     * @param endPage        结束页码
     */
    public static PDDocument copyDocumentByPages(PDDocument sourceDocument,
                                                 int startPage, int endPage) throws IOException {
        // 防止页码溢出
        int maxPage = Math.min(sourceDocument.getNumberOfPages(), endPage);
        PageExtractor pageExtractor = new PageExtractor(sourceDocument, startPage, maxPage);
        pageExtractor.setStartPage(startPage);
        pageExtractor.setEndPage(endPage);
        return pageExtractor.extract();
    }

    /**
     * 拷贝源文档的指定页面至目标文档
     *
     * @param sourceDocument 源文档
     * @param pageList       页码列表
     */
    public static PDDocument copyDocumentByPages(PDDocument sourceDocument,
                                                 List<Integer> pageList) throws IOException {
        int maxPageNumber = sourceDocument.getNumberOfPages();
        // 页码去重，过滤掉溢出的页码并排序
        List<Integer> pageNumberList = pageList.stream().distinct()
                .filter(pageNumber -> pageNumber >= 1 && pageNumber <= maxPageNumber)
                .sorted(Integer::compareTo).collect(Collectors.toList());

        PDDocument targetDocument = createFromDocument(sourceDocument);
        for (Integer pageNumber : pageNumberList) {
            PDPage sourcePage = sourceDocument.getPage(pageNumber - 1);
            PDPage targetPage = targetDocument.importPage(sourcePage);
            copyPage(sourcePage, targetPage);
        }
        return targetDocument;
    }

    protected static void copyPage(PDPage source, PDPage target) throws IOException {
        target.setCropBox(source.getCropBox());
        target.setMediaBox(source.getMediaBox());
        target.setResources(source.getResources());
        target.setRotation(source.getRotation());
        copyAnnotations(target);
    }

    protected static void copyAnnotations(PDPage page) throws IOException {
        List<PDAnnotation> annotations = page.getAnnotations();
        for (PDAnnotation annotation : annotations) {
            if (annotation instanceof PDAnnotationLink) {
                PDAnnotationLink link = (PDAnnotationLink) annotation;
                PDDestination destination = link.getDestination();
                PDAction action = link.getAction();
                if (destination == null && action instanceof PDActionGoTo) {
                    destination = ((PDActionGoTo) action).getDestination();
                }
                if (destination instanceof PDPageDestination) {
                    // TODO 在拆分结果中保留指向页面的链接
                    ((PDPageDestination) destination).setPage(null);
                }
            }
            // TODO 在拆分结果中保留指向页面的链接
            annotation.setPage(null);
        }
    }

    public interface PageAction<T> {
        void apply(T t, int index) throws IOException;
    }
}