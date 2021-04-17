package com.github.pangju666.utils.lang;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

/**
 * yaml工具类
 *
 * @author 胖橘
 * @version 1.0 2020-10-31
 * @since 1.1
 */
public class YamlUtils {
	private YamlUtils() { }

	/**
	 * 获取yaml映射
	 *
	 * @param yamlStr yaml字符串
	 * @return 键值
	 * @since 1.0
	 */
	private static Map<String, Object> parseYaml(final String yamlStr) {
		Yaml yaml = new Yaml();
		return yaml.load(yamlStr);
	}

	/**
	 * 获取yaml映射
	 *
	 * @param yamlFile yaml文件
	 * @return 键值
	 * @since 1.0
	 */
	private static Map<String, Object> parseYaml(final File yamlFile) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		return yaml.load(new FileInputStream(yamlFile));
	}

	/**
	 * 获取yaml映射
	 *
	 * @param stream yaml输入流
	 * @return 键值
	 * @since 1.0
	 */
	private static Map<String, Object> parseYaml(final InputStream stream) {
		Yaml yaml = new Yaml();
		return yaml.load(stream);
	}

	/**
	 * 获取键值
	 *
	 * @param key 键名，如：xxx.xxx.xxx
	 * @param yamlMap yaml映射
	 * @return 键值
	 * @since 1.0
	 */
	public static <T> T getValue(final String key, final Map<String, Object> yamlMap) {
		String[] keys = key.split("\\.");
		// 初始化配置映射对象
		Object value = yamlMap;
		// 遍历配置名称
		for (String s : keys) {
			// 初始化配置映射
			Map<String, Object> values = (Map<String, Object>) value;
			// 获取对应的值
			value = values.get(s);
			// 判断是否存在此项
			if (value == null) {
				return null;
			}
		}
		// 返回配置项对应的值
		return (T) value;
	}

	/**
	 * 获取键值
	 *
	 * @param key 键名，如：xxx.xxx.xxx
	 * @param yamlStr yaml字符串
	 * @return 键值
	 * @since 1.0
	 */
	public static <T> T getValue(final String key, final String yamlStr) {
		return getValue(key, parseYaml(yamlStr));
	}

	/**
	 * 获取键值
	 *
	 * @param key 键名，如：xxx.xxx.xxx
	 * @param yamlFile yaml文件
	 * @return 键值
	 * @since 1.0
	 */
	public static <T> T getValue(final String key, final File yamlFile) throws FileNotFoundException {
		return getValue(key, parseYaml(yamlFile));
	}

	/**
	 * 获取键值
	 *
	 * @param key 键名，如：xxx.xxx.xxx
	 * @param stream yaml输入流
	 * @return 键值
	 * @since 1.0
	 */
	public static <T> T getValue(final String key, final  InputStream stream) {
		return getValue(key, parseYaml(stream));
	}

	/**
	 * 判断键是否存在
	 *
	 * @param key 配置名称，如xxx.xxx.xxx
	 * @return 键值是否为空
	 * @since 1.0
	 */
	public static boolean existKey(final String key, final Map<String, Object> configMap) {
		return getValue(key, configMap) != null;
	}

	/**
	 * 判断键是否存在
	 *
	 * @param key 配置名称，如xxx.xxx.xxx
	 * @return 键值是否为空
	 * @since 1.0
	 */
	public static boolean existKey(final String key, final String yamlStr) {
		return getValue(key, yamlStr) != null;
	}

	/**
	 * 判断键是否存在
	 *
	 * @param key 配置名称，如xxx.xxx.xxx
	 * @return 键值是否为空
	 * @since 1.0
	 */
	public static boolean existKey(final String key, final File yamlFile) throws FileNotFoundException {
		return getValue(key, yamlFile) != null;
	}

	/**
	 * 判断键是否存在
	 *
	 * @param key 配置名称，如xxx.xxx.xxx
	 * @return 键值是否为空
	 * @since 1.0
	 */
	public static boolean existKey(final String key, final InputStream stream) {
		return getValue(key, stream) != null;
	}

	/**
	 * 将yaml映射转换为yaml字符串
	 *
	 * @param yamlMap yaml映射
	 * @return yaml字符串
	 * @since 1.0
	 */
	public static String toString(final Map<String, Object> yamlMap) {
		Yaml yaml = new Yaml();
		return yaml.dump(yamlMap);
	}

	/**
	 * 将yaml映射写入流中
	 *
	 * @param yamlMap yaml映射
	 * @param writer 流写入器
	 * @since 1.0
	 */
	public static void write(final Map<String, Object> yamlMap, final Writer writer) {
		Yaml yaml = new Yaml();
		yaml.dump(yamlMap, writer);
	}
}