package com.cooling.hydraulic.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

public class HttpClientUtils {
	private static Integer default_connTimeout = 3000;
	private static Integer default_readTimeout = 3000;

	public static String getMethod(String url) throws ClientProtocolException, IOException {
		return getMethod(url, null, null);
	}

	public static String postMethod(String url, List<NameValuePair> param) throws ClientProtocolException, IOException {
		return postMethod(url, param, null, null);
	}

	public static String postMethod(String url, String param) throws ClientProtocolException, IOException {
		return postMethod(url, param, null, null);
	}

	@SuppressWarnings("rawtypes")
	public static String postMethod(String url, Map<String, Object> param) throws ClientProtocolException, IOException {
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		if (param != null) {
			Set set = param.keySet();
			Iterator iterator = set.iterator();
			while (iterator.hasNext()) {
				Object key = iterator.next();
				Object value = param.get(key);
				formparams.add(new BasicNameValuePair(key.toString(), value.toString()));
			}
		}
		return postMethod(url, formparams, null, null);
	}

	public static String getMethod(String url, Integer connTimeout, Integer readTimeout)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String result = null;
		if (connTimeout != null) {
			default_connTimeout = connTimeout;
		}
		if (readTimeout != null) {
			default_readTimeout = readTimeout;
		}
		try {
			HttpGet httpGet = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(default_connTimeout)
					.setConnectTimeout(default_readTimeout).build();// 设置请求和传输超时时间
			httpGet.setConfig(requestConfig);
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				// http请求头状态码
				int status = response.getStatusLine().getStatusCode();
				// TODO 不成功报错
				if (status != HttpStatus.SC_OK) {

				}
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					result = EntityUtils.toString(entity, "UTF-8");// tostring方法读取实体内容，利用编码规则转换成String，默认规则是IS0-8859-1
				}
				EntityUtils.consume(entity);// 确保实体内容被消费，并且关闭内容流
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return result;
	}

	public static String postMethod(String url, List<NameValuePair> param, Integer connTimeout, Integer readTimeout)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String result = null;
		if (connTimeout != null) {
			default_connTimeout = connTimeout;
		}
		if (readTimeout != null) {
			default_readTimeout = readTimeout;
		}
		try {
			HttpPost httpPost = new HttpPost(url);
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(param);
			httpPost.setEntity(ent);
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(default_connTimeout)
					.setConnectTimeout(default_readTimeout).build();// 设置请求和传输超时时间
			httpPost.setConfig(requestConfig);

			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				// http请求头状态码
				int status = response.getStatusLine().getStatusCode();
				// TODO 不成功报错
				if (status != HttpStatus.SC_OK) {

				}
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					result = EntityUtils.toString(entity, "UTF-8");
				}
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return result;
	}

	public static String postMethod(String url, String param, Integer connTimeout, Integer readTimeout)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String result = null;
		if (connTimeout != null) {
			default_connTimeout = connTimeout;
		}
		if (readTimeout != null) {
			default_readTimeout = readTimeout;
		}
		try {
			HttpPost httpPost = new HttpPost(url);
			StringEntity ent = new StringEntity(param, "utf-8");
			ent.setContentType("application/json");
			// 设置类型
			ent.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(ent);
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(default_connTimeout)
					.setConnectTimeout(default_readTimeout).build();// 设置请求和传输超时时间
			httpPost.setConfig(requestConfig);

			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				// http请求头状态码
				int status = response.getStatusLine().getStatusCode();
				// TODO 不成功报错
				if (status != HttpStatus.SC_OK) {

				}
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					result = EntityUtils.toString(entity, "UTF-8");
				}
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return result;
	}

}
