package indi.daniel.esbcommonservice.common.configuration;

import indi.daniel.esbcommonservice.common.interceptor.OutboundLoggingInterceptor;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;

@Configuration
public class HttpClientConfiguration {

    @Value("${esb.common.http.ssl-enabled}")
    private boolean sslEnabled;

    @Value("${esb.common.http.connect-timeout}")
    private long connectTimeout;

    @Value("${esb.common.http.write-timeout}")
    private long writeTimeout;

    @Value("${esb.common.http.read-timeout}")
    private long readTimeout;

    @Value("${esb.common.http.call-timeout}")
    private long callTimeout;

    @Value("${esb.common.http.keep-alive-duration}")
    private long keepAliveDuration;

    @Value("${esb.common.http.max-idle-connections}")
    private int maxIdleConnections;

    @Autowired(required = false)
    private OutboundLoggingInterceptor outboundLoggingInterceptor;

    @Bean
    public OkHttpClient okHttpClient() throws NoSuchAlgorithmException, KeyManagementException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
//                .callTimeout(callTimeout, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.SECONDS));

        if (!sslEnabled) { // 使用 sslEnabled 判斷是否忽略憑證
            // 忽略憑證
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true);
        }

        if (outboundLoggingInterceptor != null) {
            builder.addInterceptor(outboundLoggingInterceptor);
        }

        return builder.build();
    }

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .option(CONNECT_TIMEOUT_MILLIS, (int)Duration.ofSeconds(connectTimeout).toMillis())
                .doOnConnected(conn ->
                        conn.addHandlerLast(new WriteTimeoutHandler(writeTimeout, TimeUnit.SECONDS))
                                .addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.SECONDS))
                )
                .responseTimeout(Duration.ofSeconds(callTimeout))
                .secure(sslContextSpec -> {
                    try {
                        sslContextSpec.sslContext(
                                SslContextBuilder.forClient()
                                        .trustManager(InsecureTrustManagerFactory.INSTANCE).build()
                        );
                    } catch (SSLException e) {
                        throw new RuntimeException(e);
                    }
                });

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
