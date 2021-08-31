package io.github.pangju666.utils.lang;

import org.dom4j.*;

import java.util.ArrayList;
import java.util.List;

/**
 * XML工具类
 *
 * @author 胖橘
 * @version 1.0
 * @see org.dom4j.Element
 * @since 1.0
 */
public class XmlUtils {
    protected XmlUtils() {
    }

    /**
     * 创建父元素根据给定的标签名
     *
     * @param tagName 标签名
     * @return 创建的元素 apache.common.io.com.胖橘.utils.file.FileUtils
     * @since 1.1
     */
    public static Element createRootElement(String tagName) {
        return DocumentHelper.createElement(tagName);
    }

    /**
     * 根据给定的标签名和Name属性来创建元素
     *
     * @param tagName 标签名
     * @param name    标签Name属性值
     * @return 创建的元素
     */
    public static Element createRootElement(String tagName, String name) {
        Element element = DocumentHelper.createElement(tagName);
        element.addAttribute("Name", name);
        return element;
    }

    /**
     * 创建子元素，根据给定的标签名来创建元素，并添加至父元素之中
     *
     * @param parent  父元素，不可为空
     * @param tagName 标签名
     * @return 创建的元素
     */
    public static Element createChildElement(Element parent, String tagName) {
        Element element = createRootElement(tagName);
        parent.add(element);
        return element;
    }

    /**
     * 创建子节点根据给定的标签名和Name属性来创建元素并添加至父元素之中
     *
     * @param parent  父元素，不可为空
     * @param tagName 标签名
     * @param name    标签Name属性值
     * @return 创建的元素
     */
    public static Element createChildElement(Element parent, String tagName, String name) {
        Element element = createRootElement(tagName, name);
        parent.add(element);
        return element;
    }

    /**
     * 向指定父元素添加若干相同的子元素
     *
     * @param parent  父元素
     * @param tagName 标签名
     * @param objects 对应文本
     */
    public static void addMultipleChildrenElement(Element parent, String tagName, List<Object> objects) {
        objects.forEach(object -> {
            Element element = createRootElement(tagName)
                    .addText(String.valueOf(object));
            parent.add(element);
        });
    }

    /**
     * 向指定父元素添加若干子元素
     *
     * @param parent   父元素
     * @param children 子元素列表
     */
    public static void addMultipleChildrenElement(Element parent, Element... children) {
        for (Element element : children) {
            parent.add(element);
        }
    }

    /**
     * 根据名称属性获取父元素中对应的子元素，不存在则创建
     *
     * @param parent  父元素，不可为空
     * @param tagName 标签名
     * @param name    元素名称
     * @return 获取到的元素，如果元素不存在则会创建此元素并返回
     */
    public static Element getElementByName(Element parent, String tagName, String name) {
        for (Element child : parent.elements()) {
            if (name.equals(child.attributeValue("Name"))) {
                return child;
            }
        }
        return createChildElement(parent, tagName, name);
    }

    /**
     * 查询符合条件的xml子节点的指定属性值
     *
     * @param element       当前元素
     * @param xpath         xpath表达式
     * @param attributeName 属性名称
     * @return 对应属性名称的属性值
     */
    public static String selectNode(Element element, String xpath, String attributeName) {
        Node node = element.selectSingleNode(xpath);
        return ((Element) node).attributeValue(attributeName);
    }

    /**
     * 查询符合条件的多个xml子节点的指定属性值
     *
     * @param element       当前元素
     * @param xpath         xpath表达式
     * @param attributeName 属性名称
     * @return 对应属性名称的属性值列表
     */
    public static List<String> selectNodes(Element element, String xpath, String attributeName) {
        List<String> attributeValues = new ArrayList<>();
        for (Node node : element.selectNodes(xpath)) {
            String attributeValue = ((Element) node).attributeValue(attributeName);
            attributeValues.add(attributeValue);
        }
        return attributeValues;
    }
}
