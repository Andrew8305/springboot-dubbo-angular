package com.hqvoyage.platform.common.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpClientUtils {

    public static final String METHOD_GET = "get";
    public static final String METHOD_POST = "post";

    private static HttpClientBuilder httpBuilder = null;
    private static RequestConfig requestConfig = null;

    static {
        //设置http的状态参数
        requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(10);
        connectionManager.setDefaultMaxPerRoute(5);
        httpBuilder = HttpClients.custom();
        httpBuilder.setConnectionManager(connectionManager);
    }

    public static CloseableHttpClient getConnection() {
        CloseableHttpClient httpClient;
        httpClient = httpBuilder.build();
        return httpClient;
    }


    public static HttpUriRequest getRequestMethod(Map<String, String> map, String url, String method) {
        List<BasicNameValuePair> params = new ArrayList<>();
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> e : entrySet) {
            String name = e.getKey();
            String value = e.getValue();
            BasicNameValuePair pair = new BasicNameValuePair(name, value);
            params.add(pair);
        }
        HttpUriRequest reqMethod = null;
        if (METHOD_POST.equals(method)) {
            reqMethod = RequestBuilder.post().setUri(url)
                    .addParameters(params.toArray(new NameValuePair[params.size()]))
                    .setConfig(requestConfig).build();
        } else if (METHOD_GET.equals(method)) {
            reqMethod = RequestBuilder.get().setUri(url)
                    .addParameters(params.toArray(new NameValuePair[params.size()]))
                    .setConfig(requestConfig).build();
        }
        return reqMethod;
    }

    public static HttpUriRequest getRequestMethod(String data, String url, String method) {
        StringEntity myEntity = new StringEntity(data, "UTF-8");
        HttpUriRequest reqMethod = null;
        if (METHOD_POST.equals(method)) {
            reqMethod = RequestBuilder.post().setUri(url)
                    .setEntity(myEntity)
                    .setConfig(requestConfig).build();
        } else if (METHOD_GET.equals(method)) {
            reqMethod = RequestBuilder.get().setUri(url)
                    .setEntity(myEntity)
                    .setConfig(requestConfig).build();
        }
        return reqMethod;
    }

    public static void main(String[] args) {

    }

}