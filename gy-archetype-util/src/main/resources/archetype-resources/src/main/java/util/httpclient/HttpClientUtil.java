package ${package}.util.httpclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http交互工具类
 */
public class HttpClientUtil {

    protected static final Logger                           logger                          = LoggerFactory.getLogger(HttpClientUtil.class);

    public static final String                              METHOD_POST                     = "POST";
    public static final String                              METHOD_GET                      = "GET";
    public static final String                              DEFAULT_CHARSET                 = "utf-8";
    public static final String                              DEFAULT_CONTENT_TYPE            = "application/json;charset=UTF-8";
    public static final int                                 DEFAULT_CONNECT_TIMEOUT         = 5000;
    public static final int                                 DEFAULT_READ_TIMEOUT            = 5000;
    public static final int                                 DEFAULT_CONNECT_REQUEST_TIMEOUT = 5000;

    private static final int                                MAX_TOTAL                       = 64;

    private static final int                                MAX_PER_ROUTE                   = 32;

    private static final RequestConfig                      requestConfig;

    private static final PoolingHttpClientConnectionManager connectionManager;

    private static final HttpClientBuilder                  httpBuilder;

    private static final CloseableHttpClient                httpClient;

    static {
        requestConfig = RequestConfig.custom().setSocketTimeout(DEFAULT_READ_TIMEOUT).setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).setConnectionRequestTimeout(DEFAULT_CONNECT_REQUEST_TIMEOUT).build();
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_TOTAL);
        connectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        httpBuilder = HttpClientBuilder.create();
        httpBuilder.setDefaultRequestConfig(requestConfig);
        httpBuilder.setConnectionManager(connectionManager);
        httpClient = httpBuilder.build();
    }

    public static void main(String[] args) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx1c460a2ee998cefb&secret=4b88500e02b8bd1ce4a20108bb660e81";
        String result = defaultPost(url, null);
        System.out.println(result);
    }

    public static String defaultGet(String getUrl) {
        return get(getUrl, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_CHARSET);
    }

    public static String defaultPost(String postUrl,
                                     String param) {
        return post(postUrl, param, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_CHARSET);
    }

    /**
     * 功能描述: 向百度主动推送链接
     * 
     * @param pushUrl 百度推送调用地址
     * @param paramUrl 待推送的链接地址集合
     * @return
     */
    public static String baiduPush(String pushUrl,
                                   List<String> paramUrl) {
        if (pushUrl == null || paramUrl == null || paramUrl.size() == 0) {
            return null;
        }
        // 百度要求提交的链接必须一行一个
        StringBuilder builder = new StringBuilder();
        for (String s : paramUrl) {
            builder.append(s).append("\n");
        }
        return defaultPost(pushUrl, builder.toString());
    }

    /**
     * 功能描述: 发送get请求
     * 
     * @param getUrl 请求url
     * @param connectTimeout 链接超时
     * @param readTimeout 读超时
     * @param charset 编码格式
     * @return 响应字符串
     */
    public static String get(String getUrl,
                             int connectTimeout,
                             int readTimeout,
                             String charset) {
        String result = null;
        HttpURLConnection conn = null;
        try {
            conn = getConnection(getUrl, METHOD_GET, connectTimeout, readTimeout);
            return getStreamAsString(conn.getInputStream(), charset);
        } catch (Exception e) {
            logger.error("发送GET请求异常：" + e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    /**
     * 功能描述: 发送post请求
     * 
     * @param postUrl 请求url
     * @param param 请求参数
     * @param connectTimeout 链接超时
     * @param readTimeout 读超时
     * @param charset 编码格式
     * @return 响应字符串
     */
    public static String post(String postUrl,
                              String param,
                              int connectTimeout,
                              int readTimeout,
                              String charset) {
        String result = null;
        HttpURLConnection conn = null;
        DataOutputStream out = null;
        try {
            conn = getConnection(postUrl, METHOD_POST, connectTimeout, readTimeout);
            // 发送请求参数
            if (param != null) {
                // 获取URLConnection对象对应的输出流
                out = new DataOutputStream(conn.getOutputStream());
                out.write(param.getBytes(charset));
                // flush输出流的缓冲
                out.flush();
            }
            return getStreamAsString(conn.getInputStream(), charset);
        } catch (Exception e) {
            logger.error("发送POST请求异常：" + e.getMessage(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    private static String getStreamAsString(InputStream stream,
                                            String charset) throws IOException {
        StringWriter writer = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(stream, charset));
            writer = new StringWriter();

            char[] chars = new char[256];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }
            return writer.toString();
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
        }
    }

    private static HttpURLConnection getConnection(String urlPath,
                                                   String method,
                                                   int connectTimeout,
                                                   int readTimeout) throws NoSuchAlgorithmException, KeyManagementException, IOException {
        HttpURLConnection conn = null;
        URL url = new URL(urlPath);
        if (isHttps(url)) {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] {
                new DefaultTrustManager()
            }, new SecureRandom());
            HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
            connHttps.setSSLSocketFactory(ctx.getSocketFactory());
            connHttps.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname,
                                      SSLSession session) {
                    return true;
                }
            });
            conn = connHttps;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        conn.setRequestMethod(method);
        conn.setDoOutput(true);// 使用 URL 连接进行输出
        conn.setDoInput(true); // 使用 URL 连接进行输入
        conn.setUseCaches(false);// 忽略缓存
        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);
        conn.setRequestProperty("Content-Type", DEFAULT_CONTENT_TYPE);
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("Connection", "keep-alive");
        return conn;
    }

    private static boolean isHttps(URL url) {
        if ("https".equals(url.getProtocol())) {
            return true;
        } else {
            return false;
        }
    }

    private static class DefaultTrustManager implements X509TrustManager {

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }
    }

    public static String get(String url,
                             Map<String, String> headers) {
        HttpGet request = new HttpGet(url);
        try {
            wrapHeader(request, headers);// 设置请求头
            return execute(request);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            request.releaseConnection();
        }
        return null;
    }

    public static String postBody(String url,
                                  String body,
                                  Map<String, String> headers) {
        HttpPost request = new HttpPost(url);
        try {
            wrapHeader(request, headers);// 设置请求头
            wrapStringEntity(request, body);// 设置body
            return execute(request);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            request.releaseConnection();
        }
        return null;
    }

    public static String postForm(String url,
                                  Map<String, String> params,
                                  Map<String, String> headers) {
        HttpPost request = new HttpPost(url);
        try {
            wrapHeader(request, headers);// 设置请求头
            wrapFormEntity(request, params);
            return execute(request);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            request.releaseConnection();
        }
        return null;
    }

    private static String execute(HttpRequestBase request) {
        String respJson = null;
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = response.getEntity();
                respJson = EntityUtils.toString(httpEntity, DEFAULT_CHARSET);
                EntityUtils.consume(httpEntity);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return respJson;
    }

    private static void wrapHeader(HttpRequestBase request,
                                   Map<String, String> headers) {
        // 设置请求头
        if (null != headers) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void wrapStringEntity(HttpPost request,
                                         String body) {
        // 设置body
        if (body != null) {
            StringEntity entity = new StringEntity(body, DEFAULT_CHARSET);// 解决中文乱码问题
            entity.setContentEncoding(DEFAULT_CHARSET);
            request.setEntity(entity);
        }
    }

    private static void wrapFormEntity(HttpPost request,
                                       Map<String, String> params) throws UnsupportedEncodingException {
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            request.setEntity(new UrlEncodedFormEntity(nvps, DEFAULT_CHARSET));
        }
    }
}
