package com.adinnet.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.xml.sax.SAXException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.net.ProtocolException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

	/**
	 * 创建HTTPS 客户端
	 * 
	 * @return 单例模式的客户端
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private static HttpClient httpClient = null;

	public static HttpClient getHttpsClient() throws KeyStoreException,
			UnrecoverableKeyException, NoSuchAlgorithmException,
			KeyManagementException {
		if (httpClient != null) {
			return httpClient;
		}
		X509TrustManager xtm = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}
		};
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, new TrustManager[] { xtm }, null);
		SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
				context, NoopHostnameVerifier.INSTANCE);
		Registry<ConnectionSocketFactory> sfr = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", scsf).build();
		PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager(
				sfr);
		httpClient = HttpClientBuilder.create().setConnectionManager(pcm)
				.build();
		return httpClient;
	}

	public static HttpClient createHttpsClient() throws KeyManagementException,
			NoSuchAlgorithmException {
		X509TrustManager xtm = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}
		};
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, new TrustManager[] { xtm }, null);
		SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
				context, NoopHostnameVerifier.INSTANCE);
		Registry<ConnectionSocketFactory> sfr = RegistryBuilder
				.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", scsf).build();
		PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager(
				sfr);
		return HttpClientBuilder.create().setConnectionManager(pcm).build();
	}

	/**
	 * 发起Https Get请求，并得到返回的JSON响应_测试环境
	 *
	 * @param url
	 *            接口Url
	 * @param params
	 *            参数u对
	 * @return
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static JSONObject httpsGetTest(String url, Map<String, String> params,String token,String channelCode,String lightAppCode){
		JSONObject jsonResult = null;
		CloseableHttpClient client = HttpClients.createDefault();
		String paramUrl = getUrlParamsByMap(params);
		HttpGet request = new HttpGet(url + "?" + paramUrl);
		request.setHeader("channelCode",channelCode);
		request.setHeader("lightAppCode",lightAppCode);
		request.setHeader("token",token);
		request.setHeader("timestamp", String.valueOf(System.currentTimeMillis()));
		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setSocketTimeout(60000).setConnectTimeout(60000)
				.setConnectionRequestTimeout(60000).build();
		request.setConfig(defaultRequestConfig);
		CloseableHttpResponse response = null;
		System.out.println("=========================请求时间："+System.currentTimeMillis());
		try {
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String strResult = EntityUtils.toString(response.getEntity());
				jsonResult = JSON.parseObject(strResult);
			}
		}catch (SocketTimeoutException e) {
			throw new RuntimeException("socket链接超时"+url, e);
		}
		catch (IOException e) {
			throw new RuntimeException("执行Get方法网络异常"+url, e);
		}finally {
			System.out.println("=========================返回时间："+System.currentTimeMillis());
			if(response != null){
				System.out.println("========================================关闭response");
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(client != null){
				System.out.println("========================================关闭client");
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return jsonResult;
	}


	/**
	 * 发起Https Get请求，并得到返回的JSON响应
	 * 
	 * @param url
	 *            接口Url
	 * @param params
	 *            参数u对
	 * @return
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static JSONObject httpsGet(String url, Map<String, String> params,String authKey){
		JSONObject jsonResult = null;
		CloseableHttpClient client = HttpClients.createDefault();
		String paramUrl = getUrlParamsByMap(params);
		HttpGet request = new HttpGet(url + "?" + paramUrl);
		//request.setHeader("channelCode",channelCode);
		//request.setHeader("lightAppCode",lightAppCode);
		//request.setHeader("token",token);
		request.setHeader("authKey", authKey);
		request.setHeader("timestamp", String.valueOf(System.currentTimeMillis()));
		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setSocketTimeout(60000).setConnectTimeout(60000)
				.setConnectionRequestTimeout(60000).build();
		request.setConfig(defaultRequestConfig);
		CloseableHttpResponse response = null;
		System.out.println("=========================请求时间："+System.currentTimeMillis());
		try {
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String strResult = EntityUtils.toString(response.getEntity());
				jsonResult = JSON.parseObject(strResult);
			}
		}catch (SocketTimeoutException e) {
			throw new RuntimeException("socket链接超时"+url, e);
		}
		catch (IOException e) {
			throw new RuntimeException("执行Get方法网络异常"+url, e);
		}finally {
			System.out.println("=========================返回时间："+System.currentTimeMillis());
			if(response != null){
				System.out.println("========================================关闭response");
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(client != null){
				System.out.println("========================================关闭client");
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return jsonResult;
	}

	/**
	 * 发起Https Get请求，并得到返回的JSON响应
	 *
	 * @param url
	 *            接口Url
	 * @param params
	 *            参数u对
	 * @return
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static JSONObject areaHttpsGet(String url, Map<String, String> params,String authKey)
			throws IOException, KeyStoreException, UnrecoverableKeyException,
			NoSuchAlgorithmException, KeyManagementException {
		HttpClient client = getHttpsClient();

		JSONObject jsonResult = null;
		String paramUrl = getUrlParamsByMap(params);
		System.out.println(url + "?" + paramUrl);
		HttpGet request = new HttpGet(url + "?" + paramUrl);
		request.setHeader("authKey",authKey);
		request.setHeader("timestamp",String.valueOf(System.currentTimeMillis()));
		RequestConfig defaultRequestConfig = RequestConfig.custom()
				.setSocketTimeout(60000).setConnectTimeout(60000)
				.setConnectionRequestTimeout(60000).build();
		request.setConfig(defaultRequestConfig);
		;
		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String strResult = EntityUtils.toString(response.getEntity());
			jsonResult = JSON.parseObject(strResult);
		}
		return jsonResult;
	}

	/**
	 * 工具方法，发送一个http post请求，并尝试将响应转换为JSON
	 *
	 * @param url
	 *            请求的方法名url
	 * @param params
	 *            参数表
	 * @return 如果请求成功则返回JSON, 否则抛异常或者返回空
	 * @throws IOException
	 */
	public static JSONObject httpsPost(String url, Map<String, String> params)
			throws IOException, UnrecoverableKeyException,
			NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		JSONObject jsonResult = null;
		// 发送get请求
		HttpClient client = getHttpsClient();
		HttpPost request = new HttpPost(url);
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			urlParameters.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
		request.setEntity(postParams);
		RequestConfig config = RequestConfig.custom().setSocketTimeout(5000)
				.setConnectTimeout(5000).setConnectionRequestTimeout(5000)
				.build();
		request.setConfig(config);
		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String strResult = EntityUtils.toString(response.getEntity());
			jsonResult = JSON.parseObject(strResult);
		}
		return jsonResult;
	}

	/**
	 * 工具方法，发送一个http post请求，并尝试将响应转换为JSON
	 *
	 * @param url
	 *            请求的方法名url
	 * @param paramsXML
	 *            申请xml
	 * @return 如果请求成功则返回JSON, 否则抛异常或者返回空
	 * @throws IOException
	 */
	public static JSONObject httpsPost(String url, String paramsXML)
			throws IOException, UnrecoverableKeyException,
			NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		JSONObject jsonResult = null;
		// 发送get请求
		HttpClient client = getHttpsClient();
		HttpPost post = new HttpPost(url);

		List<BasicNameValuePair> parameters = new ArrayList<>();
		parameters.add(new BasicNameValuePair("xml", paramsXML));
		post.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
		HttpResponse response = client.execute(post);
		System.out.println(response.toString());
		HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity, "UTF-8");
		return jsonResult;
	}

	/**
	 * 工具方法，发送一个http post请求，并尝试将响应转换为JSON
	 *
	 * @param url
	 *            请求的方法名url
	 * @param paramsXML
	 *            申请xml
	 * @return 如果请求成功则返回JSON, 否则抛异常或者返回空
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static String httpsPostCon(String url, String paramsXML)
			throws IOException, UnrecoverableKeyException,
			NoSuchAlgorithmException, KeyStoreException,
			KeyManagementException, ParserConfigurationException, SAXException {
		System.out.println(paramsXML);
		String result = "";
		HttpURLConnection httpURLConnection = null;
		ByteArrayOutputStream outputStream = null;
		InputStream in = null;
		try {
			URL urls = new URL(url);
			httpURLConnection = (HttpURLConnection) urls.openConnection();
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setInstanceFollowRedirects(true);
			httpURLConnection.setConnectTimeout(3 * 1000);
			httpURLConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			httpURLConnection.getOutputStream().write(
					paramsXML.getBytes("utf-8"));
			httpURLConnection.getOutputStream().flush();
			outputStream = new ByteArrayOutputStream();
			in = httpURLConnection.getInputStream();
			int len = 0;
			byte[] bt = new byte[1024];

			while ((len = in.read(bt)) != -1) {
				outputStream.write(bt, 0, len);
			}
			outputStream.flush();
			byte[] data = outputStream.toByteArray();
			result = new String(data, "utf-8");
			System.out.println(result);
		} catch (MalformedURLException e) {
			result = "error";
		} catch (ProtocolException e) {
			result = "error";
		} catch (IOException e) {
			e.printStackTrace();
			result = "error";
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					result = "error";
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					result = "error";
				}
			}
		}
		return result;
	}
	/**
	 * 将map转换成url
	 *
	 * @param map
	 * @return
	 */
	public static String getUrlParamsByMap(Map<String, String> map) {
		if (map == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue());
			sb.append("&");
		}
		String s = sb.toString();
		if (s.endsWith("&")) {
			s = StringUtils.substringBeforeLast(s, "&");
		}
		return s;
	}

	/**
	 * post请求传输json参数
	 *
	 * @param url       url地址
	 * @param jsonParam 参数
	 * @return
	 */
	public static JSONObject httpPost(String url, JSONObject jsonParam) {
		// post请求返回结果
		CloseableHttpClient httpClient = HttpClients.createDefault();
		JSONObject jsonResult = null;
		HttpPost httpPost = new HttpPost(url);
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpPost.setConfig(requestConfig);
		try {
			if (null != jsonParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				httpPost.setEntity(entity);
			}
			CloseableHttpResponse result = httpClient.execute(httpPost);
			//请求发送成功，并得到响应
			if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str;
				try {
					//读取服务器返回过来的json字符串数据
					str = EntityUtils.toString(result.getEntity(), "utf-8");
					//把json字符串转换成json对象
					jsonResult = JSONObject.parseObject(str);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpPost.releaseConnection();
		}
		return jsonResult;
	}
}
