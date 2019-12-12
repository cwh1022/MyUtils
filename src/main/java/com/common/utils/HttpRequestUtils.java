/**
 *
 */
package com.common.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author gary.fu
 */
public class HttpRequestUtils {

	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);


	public static final PoolingHttpClientConnectionManager CLIENT_CONNECTION_MANAGER = new PoolingHttpClientConnectionManager();

	public static final RequestConfig REQUEST_CONFIG;

	public static final int POOL_SIZE = 200; // HttpClient连接池大小

	public static final int MAX_PER_ROUTE = 100;

	public static final int CONNECT_TIMEOUT = 15000; // 连接超时

	public static final int SOCKET_TIMEOUT = 60000; // 数据获取超时

    public static final String GZIP_DEFLATE = "gzip,deflate";

    public static final String ACCEPT_ENCODING = "Accept-Encoding";

    public static final String CONNECTION = "Connection";

    public static final String KEEP_ALIVE = "keep-alive";

    public static final String CONTENT_ENCODING = "Content-Encoding";

    public static final String UTF_8 = "UTF-8";

    public static final String HTTP_CLIENT = "HttpClient发送完成";

	static {
		CLIENT_CONNECTION_MANAGER.setMaxTotal(POOL_SIZE);
		CLIENT_CONNECTION_MANAGER.setDefaultMaxPerRoute(MAX_PER_ROUTE);
		REQUEST_CONFIG = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setConnectionRequestTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
	}

	public static CloseableHttpClient getHttpClient() {
		return HttpClientBuilder.create().setDefaultRequestConfig(REQUEST_CONFIG).setConnectionManager(CLIENT_CONNECTION_MANAGER).build();
	}

	public static RequestConfig getRequestConfig(int connectTimeout, int soTimeout) {
		return RequestConfig.custom().setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectTimeout).setSocketTimeout(soTimeout).build();
	}

	private HttpRequestUtils() {
	}

	/**
	 * @param url
	 * @param queryParams
	 * @return
	 */
	public static String sendHttpClient(String url, Map<String, String> queryParams) {
		try {
			return sendHttpClient(buildURI(url, queryParams));
		} catch (Exception e) {
			logger.error("Http请求错误", e);
		}
		return null;
	}

	/**
	 * 使用HttpClient发送
	 *
	 * @param builder
	 * @return
	 * @throws Exception
	 */
	public static String sendHttpClient(URIBuilder builder) throws URISyntaxException, IOException {
		TimeLogger tl = TimeLogger.create(logger);
		URI uri = builder.build();
		HttpGet httpRequest = new HttpGet(uri);
		logger.debug("GET发送URL：{}", uri);
		httpRequest.addHeader(ACCEPT_ENCODING, GZIP_DEFLATE);
		httpRequest.addHeader(CONNECTION, KEEP_ALIVE);
		HttpClient client = getHttpClient();
		HttpResponse response = null;
		String result;
		try {
			response = client.execute(httpRequest);
			HttpEntity httpEntity = response.getEntity();
			Header ce = response.getFirstHeader(CONTENT_ENCODING);
			String enc = ce == null ? null : ce.getValue();
			if (isGzip(enc)) {
				httpEntity = new GzipDecompressingEntity(httpEntity);
			}
			result = EntityUtils.toString(httpEntity, UTF_8);
		} finally {
			httpRequest.releaseConnection();
			HttpClientUtils.closeQuietly(response);
		}
		logger.debug(result);
		tl.end(HTTP_CLIENT);
		return result;
	}

	/**
	 * 判断是否支持GZIP
	 *
	 * @param encoding
	 * @return
	 */
	public static boolean isGzip(String encoding) {
		if (StringUtils.isNotBlank(encoding)) {
			String[] encodings = encoding.split("\\s*,\\s*");
			return ArrayUtils.contains(encodings, "gzip");
		}
		return false;
	}

	/**
	 * @param url
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	public static URIBuilder buildURI(String url, Map<String, String> queryParams) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(url);
		if (queryParams != null) {
			for (Map.Entry<String, String> entry : queryParams.entrySet()) {
				uriBuilder.addParameter(entry.getKey(), entry.getValue());
			}
		}
		return uriBuilder;
	}


	/**
	 * 使用HttepClient发送Post请求
	 *
	 * @param url
	 * @param queryParams
	 * @return
	 */
	public static String sendHttpClientWithPost(String url, String queryParams) {
		try {
			return sendHttpClientPost(url, queryParams);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 使用Json格式的Post请求
	 *
	 * @param url
	 * @param queryParams
	 * @return
	 */
	public static String sendHttpClientWithPostJsonFormat(String url, String queryParams) {
		try {
			return sendHttpClientPost(url, queryParams, CoreConsts.JSON_FORMAT, null);
		} catch (Exception e) {
			logger.error("Http请求错误", e);
			return null;
		}
	}

	/**
	 * 使用Xml格式的Post请求
	 *
	 * @param url
	 * @param queryParams
	 * @return
	 */
	public static String sendHttpClientWithPostXmlFormat(String url, String queryParams) {
		try {
			return sendHttpClientPost(url, queryParams, CoreConsts.XML_FORMAT, null);
		} catch (Exception e) {
			logger.error("Http请求错误", e);
			return null;
		}
	}

	/**
	 * 使用HttpClient发送
	 *
	 * @param url 请求网址
	 * @param params 请求体参数
	 * @param contentType 请求体格式 ：可选JSON和XML；如果不写，会根据请求体内容自动来判断
     * @param customizeHeader 自定义的请求头
	 * @return
	 * @throws Exception
	 */
	public static String sendHttpClientPost(String url, String params, String contentType, Map<String, String> customizeHeader) throws Exception {
		TimeLogger tl = TimeLogger.create(logger);
		HttpPost httpPost = new HttpPost(url);
		logger.debug("POST发送URL：{}", url);
		httpPost.addHeader(ACCEPT_ENCODING, GZIP_DEFLATE);
		httpPost.addHeader(CONNECTION, KEEP_ALIVE);
		addCustomizeHeader(customizeHeader, httpPost);
		if (params != null) {
			if (null == contentType) {
				if (CoreUtils.isJson(params)) {
					httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
				} else if (CoreUtils.isXml(params)) {
					httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/xml;charset=UTF-8");
				}
			} else if (CoreConsts.JSON_FORMAT.equalsIgnoreCase(contentType)) {
				httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
			} else if (CoreConsts.XML_FORMAT.equalsIgnoreCase(contentType)) {
				httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/xml;charset=UTF-8");
			} else {
				throw new Exception("Invalid Content-Type: " + contentType);
			}
			StringEntity entity = new StringEntity(params, DefaultConsts.DEFAULT_CHARSET);
			httpPost.setEntity(entity);
		}
		HttpClient client = getHttpClient();
		HttpResponse response = null;
		String result;
		try {
			response = client.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			Header ce = response.getFirstHeader(CONTENT_ENCODING);
			String enc = ce == null ? null : ce.getValue();
			if (isGzip(enc)) {
				httpEntity = new GzipDecompressingEntity(httpEntity);
			}
			result = EntityUtils.toString(httpEntity, UTF_8);
		} finally {
			httpPost.releaseConnection();
			HttpClientUtils.closeQuietly(response);
		}
		logger.debug(result);
		tl.end(HTTP_CLIENT);
		return result;
	}

    /**
	 * 使用HttpClient发送(自动判断请求提格式)
	 *
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String sendHttpClientPost(String url, String params) throws Exception {
		return sendHttpClientPost(url, params, null, null);
	}

	/**
	 * 使用HttpClient发送(自动判断请求提格式),携带自定义的请求头
	 *
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String sendHttpClientPostwithCustomizeHeader(String url, String params, Map<String, String> customizeHeader) throws Exception {
		return sendHttpClientPost(url, params, null, customizeHeader);
	}

	public static String sendHttpClientWithGet(String url) {
		try {
			return sendHttpClientGet(url);
		} catch (Exception e) {
			return null;
		}
	}

	private static String sendHttpClientGet(String url) {
		TimeLogger tl = TimeLogger.create(logger);
		HttpGet httpRequest = new HttpGet(url);
		logger.debug("GET发送URL：{}", url);
		httpRequest.addHeader(ACCEPT_ENCODING, GZIP_DEFLATE);
		httpRequest.addHeader(CONNECTION, KEEP_ALIVE);
		HttpClient client = getHttpClient();
		HttpResponse response = null;
		String result = "";
		try {
			response = client.execute(httpRequest);
			HttpEntity httpEntity = response.getEntity();
			Header ce = response.getFirstHeader(CONTENT_ENCODING);
			String enc = ce == null ? null : ce.getValue();
			if (isGzip(enc)) {
				httpEntity = new GzipDecompressingEntity(httpEntity);
			}
			result = EntityUtils.toString(httpEntity, UTF_8);
		} catch (ParseException | IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			httpRequest.releaseConnection();
			HttpClientUtils.closeQuietly(response);
		}
		logger.debug(result);
		tl.end(HTTP_CLIENT);
		return result;
	}


	/**
	 * zip解压
	 * @param srcFile        zip源文件
	 * @param destDirPath     解压后的目标文件夹
	 * @throws RuntimeException 解压失败会抛出运行时异常
	 * 返回解压后的文件对象
	 */
	public static File unZip(File srcFile, String destDirPath) {
		File targetFile = null;
		TimeLogger tl = TimeLogger.create(logger);
		tl.record("开始解压：{}", srcFile.getName());
		// 判断源文件是否存在
		if (!srcFile.exists()) {
			logger.error(srcFile.getPath() + "所指文件不存在");
		}
		// 开始解压
		try (ZipFile zipFile = new ZipFile(srcFile)) {
			Enumeration<?> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				tl.record("解压文件：{}", entry.getName());
				// 如果是文件夹，就创建个文件夹
				if (entry.isDirectory()) {
					String dirPath = destDirPath + CoreConsts.XIE + entry.getName();
					File dir = new File(dirPath);
					dir.mkdirs();
				} else {
					// 如果是文件，就先创建一个文件，然后用io流把内容copy过去
					targetFile = new File(destDirPath + CoreConsts.XIE + entry.getName());
					// 保证这个文件的父文件夹必须要存在
					if (!targetFile.getParentFile().exists()) {
						targetFile.getParentFile().mkdirs();
					}
					if (!targetFile.createNewFile()) {
						logger.error(null +"unzip error from ZipUtils");
					}
					// 将压缩文件内容写入到这个文件中

					try (InputStream is = zipFile.getInputStream(entry); FileOutputStream fos = new FileOutputStream(targetFile)) {
						int len;
						byte[] buf = new byte[1024];
						while ((len = is.read(buf)) != -1) {
							fos.write(buf, 0, len);
						}
					}
				}
			}
			tl.end("{}  文件解压完成", srcFile.getName());
		} catch (Exception e) {
			logger.error(null + "unzip error from ZipUtils" + e.getMessage());
		}
		return targetFile;
	}

    private static void addCustomizeHeader(Map<String, String> customizeHeader, HttpPost httpPost) {
	    if (customizeHeader != null && !customizeHeader.isEmpty()){
	        customizeHeader.forEach((key, value) -> httpPost.addHeader(key, value));
        }
    }
}
