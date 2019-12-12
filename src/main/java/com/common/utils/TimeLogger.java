/**
 *
 */
package com.common.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;

/**
 * @author gary.fu
 */
public class TimeLogger {

	/**
	 * 定时工具
	 */
	private final StopWatch sw = new StopWatch();

	/**
	 * 上次时间
	 */
	private long lastTimeCache = 0L;

	/**
	 * 日志
	 */
	private final Logger logger;

	private TimeLogger(Logger logger) {
		this.logger = logger;
		sw.start();
	}

	private TimeLogger(Class<?> clazz) {
		this.logger = LoggerFactory.getLogger(clazz);
		sw.start();
	}

	public static TimeLogger create(Logger logger) {
		return new TimeLogger(logger);
	}

	public static TimeLogger create(Object target) {
		return new TimeLogger(target.getClass());
	}

	public static TimeLogger create(Class<?> clazz) {
		return new TimeLogger(clazz);
	}

	/**
	 * 计时日志
	 *
	 * @param pattern
	 * @param args
	 */
	public void record(String pattern, Object... args) {
		long time = sw.getTime();
		long timeInterval = time - lastTimeCache;
		lastTimeCache = time;
		args = ArrayUtils.add(args, timeInterval);
		String recordMsg = StringUtils.join(pattern, ",耗时{}ms");
		if (logger instanceof LocationAwareLogger) {
			if (logger.isInfoEnabled()) {
				((LocationAwareLogger) logger).log(null, TimeLogger.class.getName(),
						LocationAwareLogger.INFO_INT,
						MessageFormatter.arrayFormat(recordMsg, args).getMessage(), null, null);
			}
		} else {
			logger.info(recordMsg, args);
		}
	}

	/**
	 * 结束计时日志
	 *
	 * @param pattern
	 * @param args
	 */
	public void end(String pattern, Object... args) {
		sw.stop();
		long time = sw.getTime();
		args = ArrayUtils.add(args, time);
		String endMsg = StringUtils.join(pattern, ",整个耗时:{}ms");
		if (logger instanceof LocationAwareLogger) {
			if (logger.isInfoEnabled()) {
				((LocationAwareLogger) logger).log(null, TimeLogger.class.getName(),
						LocationAwareLogger.INFO_INT,
						MessageFormatter.arrayFormat(endMsg, args).getMessage(), null, null);
			}
		} else {
			logger.info(endMsg, args);
		}
	}

	public void end(long limit, String pattern, Object... args) {
		if (getTime() > limit) {
			this.end(pattern, args);
		} else {
			sw.stop();
		}
	}

	/**
	 * 获取耗时
	 *
	 * @return
	 */
	public long getTime() {
		return sw.getTime();
	}
}
