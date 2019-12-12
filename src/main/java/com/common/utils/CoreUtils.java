/**
 *
 */
package com.common.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 常用工具类
 *
 * @author back_cloud_003
 */
public class CoreUtils {

	private static final Logger logger = getLogger(CoreUtils.class);
	public static final String REG_SPACE = "\\s*{0}\\s*";
	public static final String REG_TRANS = "\\{0}";
	public static final String NEED_TRANS = "[|\\\\?]";
	public static final String REG_CHINESE = "[\u4e00-\u9fa5（）《》——；，。“”]+";

	private CoreUtils() {
	}

	/**
	 * 把特殊字符转成正则表达式形式，常用在split
	 *
	 * @param s
	 * @return
	 */
	public static String s2reg(String s) {
		s = StringUtils.trimToEmpty(s);
		if (s.matches(NEED_TRANS)) {
			s = MessageFormat.format(REG_TRANS, s);
		}
		s = MessageFormat.format(REG_SPACE, s);
		return s;
	}

	/**
	 * 有没有中文判断
	 *
	 * @param input
	 * @return
	 */
	public static boolean hasChineseChar(String input) {
		return StringUtils.isNotBlank(input) && Pattern.compile(REG_CHINESE).matcher(input).find();
	}


	/**
	 * 添加item，不重复
	 *
	 * @param items
	 * @param item
	 */
	public static <T> void add(Collection<T> items, T item) {
		if (!items.contains(item)) {
			items.add(item);
		}
	}

	/**
	 * 如果item不为空则添加
	 *
	 * @param items
	 * @param item
	 */
	public static <T> void addIfNotNull(Collection<T> items, T item) {
		if (item != null) {
			items.add(item);
		}
	}

	/**
	 * 包装List
	 *
	 * @param items
	 * @return
	 */
	public static <T> List<T> wrapList(List<T> items) {
		if (items == null) {
			return newList();
		}
		return items;
	}

	/**
	 * 新<code>new ArrayList();</code>
	 *
	 * @return
	 */
	public static <T> List<T> newList() {
		return new ArrayList<>();
	}

	/**
	 * 新<code>new HashSet();</code>
	 *
	 * @return
	 */
	public static <T> Set<T> newSet() {
		return new HashSet<>();
	}

	/**
	 * 新<code>new ArrayList(items);</code>
	 *
	 * @return
	 */
	public static <T> List<T> newList(Collection<T> items) {
		return new ArrayList<>(items);
	}

	/**
	 * 新<code>new HashMap();</code>
	 *
	 * @return
	 */
	public static <K, V> Map<K, V> newMap() {
		return new HashMap<>();
	}

	/**
	 * Locale获取国家二字码
	 *
	 * @param locale
	 * @return
	 */
	public static String localeToCountry(Locale locale) {
		if (locale == null) {
			locale = DefaultConsts.DEFAULT_LOCALE;
		}
		return locale.getCountry();
	}

	/**
	 * @param s
	 * @return
	 */
	public static String trim(String s) {
		return StringUtils.trimToEmpty(s);
	}

	/**
	 * 解析2013-02-27T14:35:06.171875+08:00长日期，缩短成java能解析的格式
	 *
	 * @param date
	 * @return
	 */
	public static String parseCSDate(String date) {
		if (date != null && date.length() > 26) {
			Pattern pattern = Pattern.compile("(.*\\.)(\\d+)(\\+.*)");
			Matcher matcher = pattern.matcher(date);
			if (matcher.find() && matcher.groupCount() == 3) {
				String prefix = matcher.group(1);
				String ms = matcher.group(2);
				String subfix = matcher.group(3);
				if (ms.length() > 3) {
					ms = ms.substring(0, 3);
				}
				date = prefix + ms + subfix;
			}
		}
		return date;
	}

	/**
	 * 获取futures对象
	 *
	 * @param futures
	 * @return
	 */
	public static <T> List<T> transferFutures(List<Future<T>> futures) {
		List<T> result = newList();
		for (Future<T> future : futures) {
			try {
				result.add(future.get());
			} catch (InterruptedException e) {
				logger.error("future get失败", e);
				Thread.currentThread().interrupt();
			} catch (ExecutionException e) {
				logger.error("future get失败", e);
			}
		}
		return result;
	}

	/**
	 * 获取futures对象
	 *
	 * @param futures
	 * @return
	 */
	public static <T> List<T> transferFutures(Future<T>... futures) {
		return transferFutures(Arrays.asList(futures));
	}

	/**
	 * 获取一个uuid字符串
	 *
	 * @return
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 去掉-的uuid
	 *
	 * @return
	 */
	public static String uuid1() {
		return uuid().replace("-", "");
	}

	/**
	 * 判断是否是json格式
	 *
	 * @param str
	 * @return
	 */
	public static boolean isJson(String str) {
		return StringUtils.trimToEmpty(str).startsWith("{");
	}

	/**
	 * 计算日志payload是否是json
	 */
	public static boolean isPayloadJson(String str) {
		return StringUtils.isNotBlank(str) && (isJson(str) || str.contains("Payload: {"));
	}

	/**
	 * 判断是否是xml格式
	 *
	 * @param str
	 * @return
	 */
	public static boolean isXml(String str) {
		return StringUtils.trimToEmpty(str).startsWith("<");
	}

	public static String notBlank(String... args) {
		String result = StringUtils.EMPTY;
		for (String arg : args) {
			if (StringUtils.isNotBlank(arg)) {
				result = arg;
				break;
			}
		}
		return result;
	}

	public static boolean replaceAllChineseCharacter(String source) {
		if (StringUtils.isNotEmpty(source)) {
			String mre = "[\u4e00-\u9fa5]+";
			Pattern p = Pattern.compile(mre);
			Matcher m = p.matcher(source);
			return m.find();
		} else {
			return false;
		}
	}

	/**
	 * @param source
	 * @param len
	 * @param padStr
	 * @param left
	 * @return
	 */
	public static String fillOrRemove(String source, int len, String padStr,
									  boolean left) {
		source = source == null ? StringUtils.EMPTY : source;
		if (left) {
			return StringUtils.leftPad(StringUtils.left(source, len), len,
					padStr);
		} else {
			return StringUtils.rightPad(StringUtils.right(source, len), len,
					padStr);
		}
	}

	/**
	 * 校验字符串是否可转为long
	 * @param str
	 * @return
	 */
	public static boolean isValidLong(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		try {
			Long.parseLong(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Steam 去重复处理
	 *
	 * @param keyExtractor
	 * @param <T>
	 * @return
	 */
	public static <T> java.util.function.Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
	}

	public static String getParsedIndexName(String indexName) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String suffix = DateFormatUtils.format(c, DateFormatConsts.DATE_SHORT);
		return StringUtils.join(indexName, CoreConsts.UNDER_LINE, CoreConsts.UNDER_LINE, suffix);
	}

	/**
	 * 比较字符串是否相等 null转换为空字符串进行比较
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equalsIgnoreCaseAndNull(String str1, String str2) {
		str1 = str1 == null ? StringUtils.EMPTY : str1;
		str2 = str2 == null ? StringUtils.EMPTY : str2;
		return StringUtils.equalsIgnoreCase(str1, str2);
	}

	/**
	 * 比较字符串是否相等 null转换为空字符串进行比较
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equalsIgnoreNull(String str1, String str2) {
		str1 = str1 == null ? StringUtils.EMPTY : str1;
		str2 = str2 == null ? StringUtils.EMPTY : str2;
		return StringUtils.equals(str1, str2);
	}

	public static String getListText(Collection<String> items, String separator) {
		if (items != null && !items.isEmpty()) {
			List<String> collect = items.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
			return StringUtils.join(collect, separator);
		}
		return StringUtils.EMPTY;
	}

	public static String getListText(Collection<String> items) {
		return getListText(items, CoreConsts.SPACE);
	}
}
