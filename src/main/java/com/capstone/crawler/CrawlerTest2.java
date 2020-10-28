package com.capstone.crawler;
//this test performs okhttp test
import okhttp3.*;

import java.io.IOException;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

class SSLSocketClient {

    public static SSLSocketFactory getSocketFactory(TrustManager manager) {
        SSLSocketFactory socketFactory = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{manager}, new SecureRandom());
            socketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return socketFactory;
    }

    public static X509TrustManager getX509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    public static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        return hostnameVerifier;
    }
}

public class CrawlerTest2 {

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            SSLSocketClient sc = new SSLSocketClient();
            X509TrustManager manager = sc.getX509TrustManager();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sc.getSocketFactory(manager), manager);
            builder.hostnameVerifier(sc.getHostnameVerifier());
            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final static void main(String[] args) throws Exception {
        //1 GET Anti-Forgery-Token from "https://52.165.21.112/WebHmi/Auth?ReturnUrl=%2fwebhmi"
        OkHttpClient client = getUnsafeOkHttpClient();
        Request request = new Request.Builder()
                .url("https://52.165.21.112/WebHmi/Auth?ReturnUrl=%2fwebhmi")
                .method("GET", null)
                .addHeader("Cookie", "forceLogout/WebHmi=false; .APPFRAMEWORK=AB66BEEA6D3F5CBA6BF94003D2AB369B2E9D5A9DE4807F94239BF66CD9FE1615AE8AFA20020D70486DDDE93267BE12D60576A5CADD951DD939C1A5B84CA774DFD2B797459CF18218E7EB55E325B7D4B0039A208C91538ED78652EA39BE639982EE77CB834FF332473500D9CD17F8F037036E785BE4A2B2842A8CCD0C2EB4868D23B177517B6860D071D4A5BE0A9FF6D9638EBA22EC67FC5D0B1598FD836099FE286CA93B6A23873EEE38A36B3817E46A13B7B0F440A257D76BDC65284315F7CC33B1B44EEE98CEED8724EAFE6C4241533A78E94165F7D25DC7E9F79998742B29; .APPSESSIONID=gf2ccu2j4sha20kibfpxhw5a")
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        String result1 = response.body().string();
        //System.out.println(result1);
        int start = result1.indexOf("Anti-Forgery-Token") + 22;
        int end = start + 260;
        String anti_forgery_token = result1.substring(start, end);
        System.out.println("Login Anti-Forgery-Token GET: "+ anti_forgery_token);
        //2. POST Login with token from "https://52.165.21.112/PsoDataService/Security/ValidateCredentials"

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body2 = RequestBody.create(mediaType, "{\"userName\":\"demo\",\"password\":\"demo\",\"returnUrl\":\"/webhmi/\",\"setAuthCookie\":true} ");
        Request request2 = new Request.Builder()
                .url("https://52.165.21.112/PsoDataService/Security/ValidateCredentials")
                .method("POST", body2)
                .addHeader("Anti-Forgery-Token", anti_forgery_token)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", "forceLogout/WebHmi=false; .APPSESSIONID=gf2ccu2j4sha20kibfpxhw5a; .APPFRAMEWORK=F47CDB1CAA6922C9208B6660C2B7ADCB1929A7479A3B2138076249E6D55A86CD8113F3D255A938F28384B39AB206846E96C513EB2A699A52574376C37AC1C14502A9FEEAF5B29C2092BAB2640A79BA32DA1188A2A0F4F52061D1C18C0E4B956EA4391C5B6846605528A56E1CCA4E768E27F9ED70D9D1FB787CF39558DB43051F64A51E1DEB87323B7273CB6DAEF6804A45121527A6D3C508562294AA561B10F2E0CE98123FE28BB6025B75385507D85DA8C807A846706A9A34B0188344D484246F616236F08888426ED399655C5D2DBEB37C0878CF4881EBC98345801F015936")
                .build();
        Response response2 = client.newCall(request2).execute();
        String result2 = response2.body().string();
        System.out.println(result2);

        //3. GET fwk.antiForgeryToken from https://52.165.21.112/WebHmi/HmiTgml.aspx
        Request request3 = new Request.Builder()
                .url("https://52.165.21.112/WebHmi/HmiTgml.aspx")
                .method("GET", null)
                .addHeader("Cookie", "forceLogout/WebHmi=false; .APPSESSIONID=gf2ccu2j4sha20kibfpxhw5a; .APPFRAMEWORK=412D0B6AF8B07BC6F51F62765F03DCB98061269F1C08E7450453B3111D26F9E25A6DD63C28495B067BF08B73424F8E59638ADE09C2C049023880DC4EE13EC30A95693C20E2776304F2905E538BE2FA7A5C30CEE760F4F64BBCE5D63CC9E9F6979E6AA323776B3968E05FCFF94B5E4A3B03CF606220495749E96794EDF03BDC7DA9C680710B78A08A05F2CB4613C338F048A53296311858D7DD97FC83C1FEA9F9B0751C66A9147F9A00E5741275F736F5C0F417EB3A975D012B58C091BA1530428492F12C2EBCDD7CA066DEAF893EF3AF1BF5EBF903DC6E458C2D8686A713683B")
                .build();
        Response response3 = client.newCall(request3).execute();
        String result3 = response3.body().string();
        //System.out.println(result3);
        start = result3.indexOf("fwk.antiForgeryToken") + 22;
        end = start + 260;
        String fwk_antiForgeryToken = result3.substring(start, end);
        System.out.println("Real Time antiForgeryToken: " + fwk_antiForgeryToken);
        // 4. POST Data from "https://52.165.21.112/PsoDataService/api/realtime-data/read-data-points"

        RequestBody body4 = RequestBody.create(mediaType, "{\"Project\":\"PLS_Example\",\"DocumentId\":\"TestDocument\",\"DataPointIds\":[\"PLSDCluster.Sources.Generators.West1.BusColor\",\"Gen1Trans.TopBusColor\",\"Gen1Trans.BottomBusColor\"]}");
        Request request4 = new Request.Builder()
                .url("https://52.165.21.112/PsoDataService/api/realtime-data/read-data-points")
                .method("POST", body4)
                .addHeader("anti-forgery-token", fwk_antiForgeryToken)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", "forceLogout/WebHmi=false; .APPFRAMEWORK=AB66BEEA6D3F5CBA6BF94003D2AB369B2E9D5A9DE4807F94239BF66CD9FE1615AE8AFA20020D70486DDDE93267BE12D60576A5CADD951DD939C1A5B84CA774DFD2B797459CF18218E7EB55E325B7D4B0039A208C91538ED78652EA39BE639982EE77CB834FF332473500D9CD17F8F037036E785BE4A2B2842A8CCD0C2EB4868D23B177517B6860D071D4A5BE0A9FF6D9638EBA22EC67FC5D0B1598FD836099FE286CA93B6A23873EEE38A36B3817E46A13B7B0F440A257D76BDC65284315F7CC33B1B44EEE98CEED8724EAFE6C4241533A78E94165F7D25DC7E9F79998742B29; .APPSESSIONID=gf2ccu2j4sha20kibfpxhw5a")
                .build();
        Response response4 = client.newCall(request4).execute();
        String result4 = response4.body().string();
        System.out.println("Real time data: " + result4);
        // 5. GET alarms token from
        Request request5 = new Request.Builder()
                .url("https://52.165.21.112/WebHmi/Alarms")
                .method("GET", null)
                .addHeader("Cookie", "forceLogout/WebHmi=false; .APPSESSIONID=gf2ccu2j4sha20kibfpxhw5a; .APPFRAMEWORK=E277A06DDEF16D29FDB52C8D06F01027BDB21556B70D430657B736FCD5D85CFE4D3A8FF1E42C180B0A9E770D29C65226B58DB21AC4EC6F466445619D6A3ABC6DD9F9F8BE1329B93C62B98540F177B9223DD01189271F687753269AE69DC4DA4358139F60C46B86EE9CE536DCBA359F41A9D07DBEAF1238B14DA318979BE699BD6AD5E201A5D9D03A645F7148ED634A9F52B5189742928F84F81D4887E71FB098628577D6CD79BDFFD44EBAEC498300CC7727E9FA7A99ECBB72BF199FBDC79FB500424950A9490FBE4457BDA1D57A04DF34D1C8A8FEE5892B142FA2CB39EAA740")
                .build();
        Response response5 = client.newCall(request5).execute();
        String result5 = response5.body().string();
        //System.out.println("Alarms" + result5);
        start = result5.indexOf("fwk.antiForgeryToken") + 22;
        end = start + 260;
        System.out.println(start + " " + end);
        String antiForgeryToken = result5.substring(start, end);
        System.out.println("Alarms antiForgeryToken: " + antiForgeryToken);
        //6. POST events data

        RequestBody body6 = RequestBody.create(mediaType, "{\"SortDirection\":1,\"MaxRecords\":2000,\"StartDateUtc\":\"2020-09-17T05:00:00.000Z\",\"EndDateUtc\":\"2020-09-25T05:00:00.000Z\",\"DeviceFilter\":{\"SelectedDevices\":[]},\"TeamIds\":[],\"PriorityMinimum\":0,\"PriorityMaximum\":255}");
        Request request6 = new Request.Builder()
                .url("https://52.165.21.112/PsoDataService/EventData/Events")
                .method("POST", body6)
                .addHeader("anti-forgery-token", antiForgeryToken)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", "forceLogout/WebHmi=false; .APPSESSIONID=gf2ccu2j4sha20kibfpxhw5a; .APPFRAMEWORK=412D0B6AF8B07BC6F51F62765F03DCB98061269F1C08E7450453B3111D26F9E25A6DD63C28495B067BF08B73424F8E59638ADE09C2C049023880DC4EE13EC30A95693C20E2776304F2905E538BE2FA7A5C30CEE760F4F64BBCE5D63CC9E9F6979E6AA323776B3968E05FCFF94B5E4A3B03CF606220495749E96794EDF03BDC7DA9C680710B78A08A05F2CB4613C338F048A53296311858D7DD97FC83C1FEA9F9B0751C66A9147F9A00E5741275F736F5C0F417EB3A975D012B58C091BA1530428492F12C2EBCDD7CA066DEAF893EF3AF1BF5EBF903DC6E458C2D8686A713683B")
                .build();
        Response response6 = client.newCall(request6).execute();
        String result6 = response6.body().string();
        System.out.println("Events" + result6);
        //7. POST alarms data from
        RequestBody body7 = RequestBody.create(mediaType, "{\"ActiveStatus\":1,\"AcknowledgementStatus\":2,\"StatusCompareAnd\":false,\"AlarmTypes\":[\"ALM_ARC_FLASH\",\"ALM_BACKUPPOWER\",\"ALM_COMMLOSS\",\"ALM_CONTROL_EVENT\",\"ALM_DEVICE_STATUS\",\"ALM_ENERGYMGMT_AIR\",\"ALM_ENERGYMGMT_DEMAND\",\"ALM_ENERGYMGMT_ELECTRICITY\",\"ALM_ENERGYMGMT_GAS\",\"ALM_ENERGYMGMT_STEAM\",\"ALM_ENERGYMGMT_WATER\",\"ALM_FLICKER_VOLTAGE\",\"ALM_FREQUENCY_VOLTAGE\",\"ALM_GENERIC_EVENT\",\"ALM_GENERIC_SETPOINT\",\"ALM_HARMONIC\",\"ALM_HARMONIC_CURRENT\",\"ALM_HARMONIC_POWER\",\"ALM_HARMONIC_VOLTAGE\",\"ALM_INTERRUPTION_VOLTAGE\",\"ALM_OVER_CURRENT\",\"ALM_OVER_VOLTAGE\",\"ALM_POWER_FACTOR\",\"ALM_PROTECTION\",\"ALM_SAG_CURRENT\",\"ALM_SAG_VOLTAGE\",\"ALM_SWELL_CURRENT\",\"ALM_SWELL_VOLTAGE\",\"ALM_SYSTEM\",\"ALM_THERMAL_MONITOR\",\"ALM_TRANSIENT_VOLTAGE\",\"ALM_UNBALANCE\",\"ALM_UNBALANCE_CURRENT\",\"ALM_UNBALANCE_VOLTAGE\",\"ALM_UNCLASSIFIED_PQ\",\"ALM_UNDER_CURRENT\",\"ALM_UNDER_VOLTAGE\"],\"DeviceFilter\":{\"SelectedDevices\":[]},\"TeamIds\":[],\"StartDateUtc\":\"2020-09-17T05:00:00.000Z\",\"EndDateUtc\":\"2020-09-25T05:00:00.000Z\",\"IncludeAllAlarmActivityWithinTimeRange\":false,\"PQFilter\":{\"Enabled\":false,\"DurationSecondsMinimum\":-1,\"DurationSecondsMaximum\":-1,\"MagnitudeGreaterThanOrEqual\":-1,\"MagnitudeLessThanOrEqual\":-1,\"MagnitudeConditionAnd\":true,\"FilterDDDDirection\":0,\"FilterDDDConfidence\":0,\"DDDConfidenceExactMatch\":false},\"LoadChangeFilter\":{\"Enabled\":false,\"MinimumLossPercentThreshold\":-1,\"HasLoadLoss\":false,\"HasLoadGain\":false},\"IncludeEvents\":false,\"IncludePQEvents\":true,\"PriorityFilter\":[\"high\",\"low\",\"med\"],\"MaxRecords\":1000}");
        Request request7 = new Request.Builder()
                .url("https://52.165.21.112/PsoDataService/AlarmData")
                .method("POST", body7)
                .addHeader("anti-forgery-token", antiForgeryToken)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", "forceLogout/WebHmi=false; .APPSESSIONID=gf2ccu2j4sha20kibfpxhw5a; .APPFRAMEWORK=E277A06DDEF16D29FDB52C8D06F01027BDB21556B70D430657B736FCD5D85CFE4D3A8FF1E42C180B0A9E770D29C65226B58DB21AC4EC6F466445619D6A3ABC6DD9F9F8BE1329B93C62B98540F177B9223DD01189271F687753269AE69DC4DA4358139F60C46B86EE9CE536DCBA359F41A9D07DBEAF1238B14DA318979BE699BD6AD5E201A5D9D03A645F7148ED634A9F52B5189742928F84F81D4887E71FB098628577D6CD79BDFFD44EBAEC498300CC7727E9FA7A99ECBB72BF199FBDC79FB500424950A9490FBE4457BDA1D57A04DF34D1C8A8FEE5892B142FA2CB39EAA740")
                .build();
        Response response7 = client.newCall(request7).execute();
        String result7 = response7.body().string();
        System.out.println("Alarms" + result7);
    }
}