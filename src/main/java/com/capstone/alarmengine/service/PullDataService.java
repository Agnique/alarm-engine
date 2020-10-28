package com.capstone.alarmengine.service;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import javax.net.ssl.SSLException;
import java.util.Arrays;
import java.util.Map;

@Service
public class PullDataService {
    private final WebClient webClient;
    private String antiForgerytoken;
    private MultiValueMap<String, String> SessionCookies = new LinkedMultiValueMap<>();
    public PullDataService(WebClient.Builder webClientBuilder) throws SSLException {
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        HttpClient httpClient = HttpClient
                                .create()
                                .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient.followRedirect(true)))
                .baseUrl("https://52.165.21.112")
                .build();
    }

    private String getToken(String resp, String tokenName, String delim) {
        int i = resp.indexOf(tokenName);
        if (i == -1) {
            return "";
        }
        i += tokenName.length() + 2;
        int j = resp.indexOf(delim, i);
        return resp.substring(i, j);
    }

    public String validateCredentials() {
        String resp;
        resp = this.webClient.get().uri("/webhmi/Auth?ReturnUrl=%2fwebhmi").retrieve().bodyToMono(String.class).block();

        this.antiForgerytoken = getToken(resp, "\"Anti-Forgery-Token\":", "\"");
        ClientResponse r = this.webClient.post()
                .uri("/PsoDataService/Security/ValidateCredentials")
                .body(BodyInserters.fromValue("{\"userName\":\"demo\",\"password\":\"demo\",\"returnUrl\":\"/webhmi/\",\"setAuthCookie\":true}"))
                .header("Anti-Forgery-Token", this.antiForgerytoken)
                .header(HttpHeaders.CONNECTION, "keep-alive")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .exchange()
                .block();
        for (String key: r.cookies().keySet()) {
            SessionCookies.put(key, Arrays.asList(r.cookies().get(key).get(0).getValue()));
        };

        String res = this.webClient
                .get()
                .uri("/WebHmi/HmiTgml.aspx")
                .cookies(cookies -> cookies.addAll(SessionCookies))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        this.antiForgerytoken = getToken(res, "antiForgeryToken", "'");
        return this.antiForgerytoken;
    }

    public String getTagData() {
        String resp;
        resp = this.webClient.post()
                .uri("/PsoDataService/api/realtime-data/read-data-points")
                .header("Anti-Forgery-Token", this.antiForgerytoken)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(BodyInserters.fromValue("{\n" +
                        "    \"Project\": \"PLS_Example\",\n" +
                        "    \"DocumentId\": \"TestDocument\",\n" +
                        "    \"DataPointIds\": [\"PLSDCluster.Sources.Generators.West1.BusColor\"]\n" +
                        "}"))
                .cookies(cookies -> cookies.addAll(SessionCookies))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return resp;
    }


}
