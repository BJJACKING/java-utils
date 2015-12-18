package com.znb.java.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangnaibin
 * @Date 2015-12-18 上午11:41
 */
public class HttpClientUtil {
    static final Logger LOG = LoggerFactory.getLogger(HttpClientUtil.class);

    private static final int MAX_CONNECTION = 640;

    private static final int CONNECTION_TIMEOUT = 10000;

    private static final int SOCKET_TIMEOUT = 5000;

    private static final int MAX_CONTENT_SIZE = 8 * 1024 * 1024;

    private static final int MAX_CONNECTION_PER_IP = 64;

    private static Map<String, HttpClient> httpClientMap = new ConcurrentHashMap<String, HttpClient>();

    private static HttpClient getSharedHttpClient(int socketTimeout, int connTimeout) {
        String key = "HC_" + socketTimeout + "_" + connTimeout;
        HttpClient httpClient = httpClientMap.get(key);
        if (null == httpClient) {
            synchronized (HttpClientUtil.class) {
                httpClient = getNewInstance(socketTimeout, connTimeout);
                httpClient.getParams().setContentCharset("utf-8");
                httpClientMap.put(key, httpClient);
            }
        }
        return httpClient;
    }

    public static HttpClient getNewInstance(int socTimeout, int connTimeout) {
        return getNewInstance(socTimeout, connTimeout, MAX_CONNECTION,
                MAX_CONNECTION_PER_IP, MAX_CONTENT_SIZE);
    }

    public static HttpClient getNewInstance(int socketTimeout, int connTimeout,
                                            int maxConn, int maxConnPerIp, int maxContentSize) {
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setMaxTotalConnections(maxConn);
        connectionManager.getParams().setConnectionTimeout(connTimeout);
        connectionManager.getParams().setSoTimeout(socketTimeout);
        connectionManager.getParams().setSendBufferSize(maxContentSize);
        connectionManager.getParams().setReceiveBufferSize(maxContentSize);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnPerIp);
        return new HttpClient(connectionManager);
    }

    public static String encode(String value) {
        try {
            return URLEncoder.encode(value, "utf-8");
        } catch (Exception e) {
            LOG.error("url encode exception, value:{} exception:{}", value, e);
        }
        return value;
    }

    private static HttpClient getSharedHttpClient() {
        return getSharedHttpClient(SOCKET_TIMEOUT, CONNECTION_TIMEOUT);
    }


    /**
     * http post请求,可以在此基础上使用默认参数封装
     *
     * @param url
     * @param params
     * @param requestEntity
     * @param retryCount
     * @param soTimeout
     * @param connectTimeout
     * @param headerMap
     * @return
     */
    public static String getResponseBodyAsStringByPost(String url, Map<String, String> params, RequestEntity requestEntity, int retryCount, int soTimeout, int connectTimeout, Map<String, String> headerMap) {
        StringBuffer buffer = new StringBuffer(url);
        PostMethod method = new PostMethod(buffer.toString());
        if (null != headerMap) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                method.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        if (null != requestEntity) {
            method.setRequestEntity(requestEntity);
        }
        if (null != params) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                method.addParameter(entry.getKey(), entry.getValue());
            }
        }
        do {
            try {
                try {
                    int code = getSharedHttpClient(soTimeout, connectTimeout).executeMethod(method);
                    if (code == HttpStatus.SC_OK) {
                        return method.getResponseBodyAsString();
                    }
                } finally {
                    method.releaseConnection();
                }
            } catch (Throwable e) {
                LOG.warn("http error, url:{} exception:{}", url, e);
            }
        } while (--retryCount > 0);
        return null;
    }

    /**
     * 基础的get请求,不需要这么多参数可以再进行所见封装
     * 方法已经对参数进行了encode
     *
     * @param url
     * @param params         请求参数
     * @param retryCount     重试次数
     * @param soTimeout      socket超时时间
     * @param connectTimeout 连接超时时间
     * @param headerMap      请求header参数
     * @return
     */
    public static String getResponseBodyAsStringByGet(String url, Map<String, String> params, int retryCount, int soTimeout, int connectTimeout, Map<String, String> headerMap) {
        StringBuffer buffer = new StringBuffer(url);
        if (null != params) {
            if (!StringUtils.contains(url, "?")) {
                buffer.append("?");
            }
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buffer.append(entry.getKey() + "=" + encode(entry.getValue()) + "&");
            }
        }
        GetMethod method = new GetMethod(buffer.toString());
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                method.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        do {
            try {
                try {
                    int code = getSharedHttpClient(soTimeout, connectTimeout).executeMethod(method);
                    if (code == HttpStatus.SC_OK) {
                        return method.getResponseBodyAsString();
                    }
                } finally {
                    method.releaseConnection();
                }
            } catch (Throwable e) {
                LOG.warn("http error, url:{} exception:{}", url, e);
            }
        } while (--retryCount > 0);
        return null;
    }
}
