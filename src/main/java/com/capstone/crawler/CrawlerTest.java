package com.capstone.crawler;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.neo4j.driver.internal.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class CrawlerTest {

    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[] { trustManager }, null);
        return sc;
    }

    public final static void main(String[] args) throws Exception {

        String body = "";

        //create https request ignoring ssl
        SSLContext sslcontext = createIgnoreVerifySSL();


        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);



        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();
        //CloseableHttpClient client = HttpClients.createDefault();

        try {
            //Create get object
            HttpGet get = new HttpGet("https://52.165.21.112/webhmi/Auth?ReturnUrl=%2fwebhmi");

            //Set Content-type, User-Agent
            //get.setHeader("Content-type", "application/x-www-form-urlencoded");
            get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");

            //Create response object (synchronous blocking)
            CloseableHttpResponse response = client.execute(get);

            //Obtain http entity
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                //Encoding
                body = EntityUtils.toString(entity);//, "UTF-8");
            }

            EntityUtils.consume(entity);
            //Release conn
            response.close();
            System.out.println("body:" + body);
        } finally {
            client.close();
        }
    }

    //public static void main(String[] args) {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        String url = "https://52.165.21.112/WebHmi/Auth?ReturnUrl=%2fwebhmi";
//        HttpGet httpGet = new HttpGet(url);
//        CloseableHttpResponse response = httpClient.execute(httpGet);
//        if(response.getStatusLine().getStatusCode() == 200){
//            HttpEntity httpEntity = response.getEntity();
//            String content = EntityUtils.toString(httpEntity);
//            System.out.println(content);
//        }

//    }
}
