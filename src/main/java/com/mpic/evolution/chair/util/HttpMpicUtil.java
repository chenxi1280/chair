package com.mpic.evolution.chair.util;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
* @author 作者 SJ:
* @date 创建时间：2020-8-14 16:22:23
*/

public class HttpMpicUtil {
	/**
     * 返回base64位
     *
     * @param uri 发送请求的 uri
     * @param param  请求参数
     * @return 所代表远程资源的响应结果
     */
    public static String sendPostForBase64(String uri, JSONObject param) {
        PrintWriter printWriter = null;
        InputStream inStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        String result = "";
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            printWriter = new PrintWriter(urlConnection.getOutputStream());
            printWriter.write(param.toString());
			//flush输出流的缓冲
			printWriter.flush();
            inStream = urlConnection.getInputStream();
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byte[] data;
            data = byteArrayOutputStream.toByteArray();
            String content = new String(data);
            // 能被转成json 表示获取二维码失败
            return HttpMpicUtil.isJsonObject(content) ? content : Base64.encodeBase64String(data);
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (inStream != null) {
                    inStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
    * 判断字符串是否可以转化为json对象
    * @param content
    * @return
    */
    public static boolean isJsonObject(String content) {
        // 此处应该注意，不要使用StringUtils.isEmpty(),因为当content为"  "空格字符串时，JSONObject.parseObject可以解析成功，
        // 实际上，这是没有什么意义的。所以content应该是非空白字符串且不为空，判断是否是JSON数组也是相同的情况。
        if(StringUtils.isBlank(content)){
            return false;
        }
        try {
            JSONObject jsonStr = JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static String post(JSONObject json,String URL) {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL);
        post.setHeader("Content-Type", "application/json");
        String result = "";

        try {

            StringEntity s = new StringEntity(json.toString());
            s.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
                    "utf-8"));
            post.setEntity(s);

            // 发送请求
            HttpResponse httpResponse = client.execute(post);
            // 获取响应输入流
            InputStream inStream = httpResponse.getEntity().getContent();
//            byte[] buffer = toByteArray(inStream);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byte[] data;
            data = byteArrayOutputStream.toByteArray();
            String content = new String(data);
            // 能被转成json 表示获取二维码失败

            inStream.close();

            result = new String(buffer);
//            System.out.println(result);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                System.out.println("请求服务器成功，做相应处理");

            } else {

                System.out.println("请求服务端失败");

            }

            return HttpMpicUtil.isJsonObject(content) ? content : Base64.encodeBase64String(data);

        } catch (Exception e) {
            System.out.println("请求异常");
            throw new RuntimeException(e);
        }

//        return result;
    }

    public static String sendPost(String url, JSONObject param) {
        System.out.println("sendPost url:" + url + ",postData:" + JSON.toJSONString(param));
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 设置连接超时时间
            conn.setConnectTimeout(3000);
            conn.setRequestProperty("Content-type", "application/json;charset=utf-8");
            conn.setRequestProperty("Charset", "UTF-8");

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param.toString());
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (ConnectException e) {
//            log.error(e.getMessage());getMessage
        } catch (IOException e) {
            e.printStackTrace();
//            log.error(e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
//        log.info("res=" + result);
        return result;
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024*4];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
