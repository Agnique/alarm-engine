package com.capstone.alarmengine.service;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.json.JSONObject;
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
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class PullDataService {
    private final WebClient webClient;
    private String loginToken = null;
    private String realTimeToken = null;
    private String alarmToken = null;
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
        login();
        getRealTimeToken();
        getAlarmToken();
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

    public void login(){
        String webResponse;
        webResponse = this.webClient.get().uri("/webhmi/Auth?ReturnUrl=%2fwebhmi").retrieve().bodyToMono(String.class).block();

        this.loginToken = getToken(webResponse, "\"Anti-Forgery-Token\":", "\"");
        ClientResponse clientResponse = this.webClient.post()
                .uri("/PsoDataService/Security/ValidateCredentials")
                .body(BodyInserters.fromValue("{\"userName\":\"demo\",\"password\":\"demo\",\"returnUrl\":\"/webhmi/\",\"setAuthCookie\":true}"))
                .header("Anti-Forgery-Token", this.loginToken)
                .header(HttpHeaders.CONNECTION, "keep-alive")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .exchange()
                .block();
        for (String key: clientResponse.cookies().keySet()) {
            SessionCookies.put(key, Arrays.asList(clientResponse.cookies().get(key).get(0).getValue()));
        }
    }

    public String getRealTimeToken() {
        String response = this.webClient
                .get()
                .uri("/WebHmi/HmiTgml.aspx")
                .cookies(cookies -> cookies.addAll(SessionCookies))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        this.realTimeToken = getToken(response, "antiForgeryToken", "'");
        return this.realTimeToken;
    }

    public String postRealTimeData(List<String> dataPointIds) {
        String response;
        JSONObject body = new JSONObject();
        body.put("Project", "PLS_Example");
        body.put("DocumentId", "TestDocument");
        body.put("DataPointIds", dataPointIds);

        response = this.webClient.post()
                .uri("/PsoDataService/api/realtime-data/read-data-points")
                .header("Anti-Forgery-Token", this.realTimeToken)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(BodyInserters.fromValue(body.toString()))
                .cookies(cookies -> cookies.addAll(SessionCookies))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }

    public String getAlarmToken() {
        String response = this.webClient
                .get()
                .uri("/WebHmi/Alarms")
                .cookies(cookies -> cookies.addAll(SessionCookies))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        this.alarmToken = getToken(response, "antiForgeryToken", "'");
        return this.alarmToken;
    }

    public String postEventData(){
        String response;
        JSONObject body = new JSONObject("{\"SortDirection\":1,\"MaxRecords\":10,\"StartDateUtc\":\"2020-09-17T05:00:00.000Z\",\"EndDateUtc\":\"2020-09-25T05:00:00.000Z\",\"DeviceFilter\":{\"SelectedDevices\":[]},\"TeamIds\":[],\"PriorityMinimum\":0,\"PriorityMaximum\":255}");

        response = this.webClient.post()
                .uri("/PsoDataService/EventData/Events")
                .header("Anti-Forgery-Token", this.alarmToken)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(BodyInserters.fromValue(body.toString()))
                .cookies(cookies -> cookies.addAll(SessionCookies))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }

    public String postAlarmData(){
        String response;
        JSONObject body = new JSONObject("{\"ActiveStatus\":1,\"AcknowledgementStatus\":2,\"StatusCompareAnd\":false,\"AlarmTypes\":[\"ALM_ARC_FLASH\",\"ALM_BACKUPPOWER\",\"ALM_COMMLOSS\",\"ALM_CONTROL_EVENT\",\"ALM_DEVICE_STATUS\",\"ALM_ENERGYMGMT_AIR\",\"ALM_ENERGYMGMT_DEMAND\",\"ALM_ENERGYMGMT_ELECTRICITY\",\"ALM_ENERGYMGMT_GAS\",\"ALM_ENERGYMGMT_STEAM\",\"ALM_ENERGYMGMT_WATER\",\"ALM_FLICKER_VOLTAGE\",\"ALM_FREQUENCY_VOLTAGE\",\"ALM_GENERIC_EVENT\",\"ALM_GENERIC_SETPOINT\",\"ALM_HARMONIC\",\"ALM_HARMONIC_CURRENT\",\"ALM_HARMONIC_POWER\",\"ALM_HARMONIC_VOLTAGE\",\"ALM_INTERRUPTION_VOLTAGE\",\"ALM_OVER_CURRENT\",\"ALM_OVER_VOLTAGE\",\"ALM_POWER_FACTOR\",\"ALM_PROTECTION\",\"ALM_SAG_CURRENT\",\"ALM_SAG_VOLTAGE\",\"ALM_SWELL_CURRENT\",\"ALM_SWELL_VOLTAGE\",\"ALM_SYSTEM\",\"ALM_THERMAL_MONITOR\",\"ALM_TRANSIENT_VOLTAGE\",\"ALM_UNBALANCE\",\"ALM_UNBALANCE_CURRENT\",\"ALM_UNBALANCE_VOLTAGE\",\"ALM_UNCLASSIFIED_PQ\",\"ALM_UNDER_CURRENT\",\"ALM_UNDER_VOLTAGE\"],\"DeviceFilter\":{\"SelectedDevices\":[]},\"TeamIds\":[],\"StartDateUtc\":\"2020-09-17T05:00:00.000Z\",\"EndDateUtc\":\"2020-09-25T05:00:00.000Z\",\"IncludeAllAlarmActivityWithinTimeRange\":false,\"PQFilter\":{\"Enabled\":false,\"DurationSecondsMinimum\":-1,\"DurationSecondsMaximum\":-1,\"MagnitudeGreaterThanOrEqual\":-1,\"MagnitudeLessThanOrEqual\":-1,\"MagnitudeConditionAnd\":true,\"FilterDDDDirection\":0,\"FilterDDDConfidence\":0,\"DDDConfidenceExactMatch\":false},\"LoadChangeFilter\":{\"Enabled\":false,\"MinimumLossPercentThreshold\":-1,\"HasLoadLoss\":false,\"HasLoadGain\":false},\"IncludeEvents\":false,\"IncludePQEvents\":true,\"PriorityFilter\":[\"high\",\"low\",\"med\"],\"MaxRecords\":1000}");

        response = this.webClient.post()
                .uri("/PsoDataService/AlarmData")
                .header("Anti-Forgery-Token", this.alarmToken)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(BodyInserters.fromValue(body.toString()))
                .cookies(cookies -> cookies.addAll(SessionCookies))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }




}
