package ar.lamansys.sgx.shared.restclient.configuration;

import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class RestUtils {

    public static HttpClient httpClient(boolean trustInvalidCertificate) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                new SSLContextBuilder().loadTrustMaterial(null, trustInvalidCertificate ? acceptingTrustStrategy : new TrustSelfSignedStrategy()).build());
        return HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider)
                .setSSLSocketFactory(socketFactory).build();
    }
}
