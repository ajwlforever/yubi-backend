package com.yupi.springbootinit.common;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.HttpUrl;
import com.yupi.springbootinit.config.dto.Xfun.MsgDTO;
import com.yupi.springbootinit.config.dto.Xfun.XfunSendRequest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  星火讯飞大模型开放平台API
 */
public class XfunOpenApi {
    //常量定义

    // 地址与鉴权信息  https://spark-api.xf-yun.com/v2.1/chat   2.0地址  domain参数为generalv2
    public static final String hostUrl = "https://spark-api.xf-yun.com/v2.1/chat";
    public static final String appid = "d61943f8";
    public static final String apiSecret = "ODdiMGNiOTQ5MGI4ZTQ1NGJiMGU1Mzc5";
    public static final String apiKey = "c2140c8f6ee2f769a810ec897acaeef9";

    // 连接星火模型，定义prompt


    public XfunSendRequest getSendRequest(String uid, List<MsgDTO> msgs) {
        XfunSendRequest xfunSendRequest = new XfunSendRequest();
        XfunSendRequest.Header header = new XfunSendRequest.Header();
        header.setAppId(appid);
        header.setUid(uid);
        xfunSendRequest.setHeader(header);
        XfunSendRequest.ParameterDTO parameterDTO = new XfunSendRequest.ParameterDTO();
        XfunSendRequest.ParameterDTO.ChatDTO chatDTO = new XfunSendRequest.ParameterDTO.ChatDTO();
        parameterDTO.setChat(chatDTO);
        xfunSendRequest.setParameterDTO(parameterDTO);
        XfunSendRequest.PayloadDTO payloadDTO = new XfunSendRequest.PayloadDTO();
        XfunSendRequest.PayloadDTO.MessageDTO messageDTO = new XfunSendRequest.PayloadDTO.MessageDTO();
        messageDTO.setText(msgs);
        payloadDTO.setMessage(messageDTO);
        xfunSendRequest.setPayload(payloadDTO);
        return xfunSendRequest;

    }
    // 发送消息
    // public WebSocket(String uid,)

    public WebSocket sendMsg(String uid, List<MsgDTO> msgs, WebSocketListener webSocketListener) throws Exception {
        // 获取鉴权url
        String url = getAuthUrl();
        //建立请求
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(url).build();

        WebSocket webSocket = okHttpClient.newWebSocket(request, webSocketListener);

        XfunSendRequest xfunSendRequest = this.getSendRequest(uid, msgs);
        System.out.println("params:" + JSONObject.toJSONString(xfunSendRequest));

        //发送消息
        webSocket.send(JSONObject.toJSONString(xfunSendRequest));

        return webSocket;
    }

    public void test() {

//        List<MsgDTO> msgs = new ArrayList<>();
//        MsgDTO msgDTO = new MsgDTO( );
//        msgDTO.setRole("user");
//        msgDTO.setContent("你好");
//        msgDTO.setIndex(0);
//        msgs.add(msgDTO);
//        XfunListener xfunListener =new XfunListener(hostUrl, appid,apiSecret, apiKey,false, new ArrayList<>(),"Sss");
//
//        try {
//            WebSocket webSocket = this.sendMsg("123", msgs, xfunListener );
//
//
//            System.out.println(webSocket.toString());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

    }


    // 通用鉴权URL生成
    // 鉴权方法
    public static String getAuthUrl() throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
        // System.err.println(preStr);
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // System.err.println(sha);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();

        // System.err.println(httpUrl.toString());
        return httpUrl.toString();
    }


    public static void main(String[] args) {
      //  new XfunOpenApi().test();
    }
}
