package com.hirisun.check.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class HttpClientUtil {
/**
 * 传入需要的url地址，get方式
 * @param url
 */
public String get(String url){

    CloseableHttpClient httpClient = null;

    HttpGet httpGet = null;

    String statusCode = null;
    String message = null;
    try {

        httpClient = HttpClients.createDefault();
        //	设置超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000).build();      

        httpGet = new HttpGet(url);

        httpGet.setConfig(requestConfig);

        CloseableHttpResponse response = httpClient.execute(httpGet);
        //	获取响应状态码
		statusCode = String.valueOf(response.getStatusLine().getStatusCode());
		
        message = response.getStatusLine().toString();
        
    }catch (Exception e) {
    	message= e.toString();
    	return "异常," + message;

    }finally{

        try {
            if(httpGet!=null){
                httpGet.releaseConnection();
            }
            if(httpClient!=null){
                httpClient.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    if (statusCode.startsWith("2"))
    	return "SUCCESS," + statusCode;
    else
    	return statusCode + "," +message;
    
  }

/**
 * 带参的get方式
 * @param url
 * @param params
 */
public String get(String url, Map<String, String> params){

    CloseableHttpClient httpClient = null;
    HttpGet httpGet = null;
    String statusCode = null;
    String message = null;

    try {
        httpClient = HttpClients.createDefault();

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000).build(); 

        String ps = "";

        for (String pKey : params.keySet()) {
            if(!"".equals(ps)){
                ps = ps + "&";
            }
            ps = pKey + "=" + params.get(pKey);

        }

        if(!"".equals(ps)){
            url = url + "?" + ps;
        }

        httpGet = new HttpGet(url);

        httpGet.setConfig(requestConfig);

        CloseableHttpResponse response = httpClient.execute(httpGet);

//    	获取响应状态码
    		statusCode = String.valueOf(response.getStatusLine().getStatusCode());
    		
            message = response.getStatusLine().toString();
    } catch (Exception e) {
    	message= e.toString();
    	return message;

    }finally{
        try {
            if(httpGet!=null){
                httpGet.releaseConnection();
            }

            if(httpClient!=null){
                httpClient.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    if (statusCode.startsWith("2"))
    	return "SUCCESS";
    else
    	return message;
}

/**
 * 带参的 post方式
 * @param url
 * @param params
 */

public String post(String url, Map<String, String> params){

    CloseableHttpClient httpClient = null;

    HttpPost httpPost = null;
    String statusCode = null;
    String message = null;
    
    try {

        httpClient = HttpClients.createDefault();

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000).build(); 

        httpPost = new HttpPost(url);

        httpPost.setConfig(requestConfig);

        List<NameValuePair> ps = new ArrayList<NameValuePair>();

        for (String pKey : params.keySet()) {

            ps.add(new BasicNameValuePair(pKey, params.get(pKey)));

        }

        httpPost.setEntity(new UrlEncodedFormEntity(ps));

        CloseableHttpResponse response = httpClient.execute(httpPost);

//    	获取响应状态码
		statusCode = String.valueOf(response.getStatusLine().getStatusCode());
		
        message = response.getStatusLine().toString();
    } catch (Exception e) {
    	message= e.toString();
    	return message;
    	
    } finally{
        try {
            if(httpPost!=null){
                httpPost.releaseConnection();
            }

            if(httpClient!=null){
                httpClient.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    if (statusCode.startsWith("2"))
    	return "SUCCESS";
    else
    	return message;
}

}
