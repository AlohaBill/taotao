package com.toatao.httpclient;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpclientTest {
	@Test
	public void doGet() throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet get=new HttpGet("http://www.jd.com");
		CloseableHttpResponse httpResponse = httpClient.execute(get);
		int code = httpResponse.getStatusLine().getStatusCode();
		System.out.println(code);
		HttpEntity entity = httpResponse.getEntity();
		String str = EntityUtils.toString(entity);
		System.out.println(str);
		httpResponse.close();
		httpClient.close();
	}
	@Test
	public void doPost() throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post=new HttpPost("http://localhost:8080/httpclient/post");
		List<NameValuePair> kvList=new ArrayList<>();
		kvList.add(new BasicNameValuePair("username", "火狐"));
		kvList.add(new BasicNameValuePair("password", "123"));
		HttpEntity entity = new UrlEncodedFormEntity(kvList);
		post.setEntity(entity);
		CloseableHttpResponse response=httpClient.execute(post);
		String str=EntityUtils.toString(response.getEntity());
		System.out.println(str);
		response.close();
		httpClient.close();
	}
}
