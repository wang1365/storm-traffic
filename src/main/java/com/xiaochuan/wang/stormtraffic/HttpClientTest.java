package com.xiaochuan.wang.stormtraffic;


import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpClientTest {

    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                public boolean isTrusted(X509Certificate[] chain,
                                         String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        CloseableHttpClient httpClient = HttpClientTest.createSSLClientDefault();
        HttpGet get = new HttpGet();
        get.setURI(new URI("https://www.audi.cn/cn/web/zh/models.html"));
        CloseableHttpResponse req = httpClient.execute(get);
        System.out.println(req.getEntity().getContentLength());


        HttpPost post = new HttpPost("http://www.rt-mart.com.cn/ajax/getcitys");

        Arrays.asList("1", "2", "3", "4").forEach(province -> {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("province_no", province));

            try {
                post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(post);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity httpEntity = response.getEntity();
                    //取出应答字符串
                    String result = EntityUtils.toString(httpEntity);
                    System.out.println(result);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }
}
