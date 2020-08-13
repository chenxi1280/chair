package chair;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class QRcodeTest {

    //appid: 'wxa001a9842ad0f851',
    //secret: '335ecf3ab57b04828d9003bf144f1369'
    //这里要发一个get请求，这里没写

    /**
     * 返回内容：
     * {"access_token":"36__h45mu6_20MC7dbJE8fNdc0zaa8MrQESUYkdMFRmoq-wzgWurveqRswGwZjAj_oCEi0PXLaoQ3yR3lDqS9UB-MQfkPFjOoyHrAICN-1UbZM_1WVSMsRM9Y-1lVq4a3n7RqYxYk7F4TKD9QO9CGCeAJAMQM",
     * "expires_in":7200}
     *
     * 此处要根据过期时间，存redis，有人请求，先从redis里面拿token，没有再请求
     *
     */
    private static String wxGetQrcode(String appid, String secret) {
        String getQrcode = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                + appid + "&secret=" + secret + "";
        return getQrcode;
    }


    /**
     * 这里面的
     * scene
     * 参数
     * 是前台要传过来的业务参数
     */
    @Test
    public void test2() {

        String acc_code = wxGetQrcode("","");

        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + acc_code;

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("page","pages/play/play");
        paramMap.put("scene","12345");

        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(paramMap);
        wxPost(url, jsonObject, "asd");

    }

    private static void wxPost(String uri, JSONObject paramJson, String fileName) {
        try {
            URL url = new URL(uri);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            // conn.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(
                    httpURLConnection.getOutputStream());
            printWriter.write(paramJson.toString());
            // flush输出流的缓冲
            printWriter.flush();
            // 开始获取数据
            BufferedInputStream bis = new BufferedInputStream(
                    httpURLConnection.getInputStream());
            OutputStream os = new FileOutputStream(new File("D:/"
                    + fileName + ".png"));
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                os.write(arr, 0, len);
                os.flush();
            }
            os.close();
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
