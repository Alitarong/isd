package org.isd.util;

import com.isd.oxygenshow.entity.User;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Base64Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Oxygenshow {
    //通过接口获取用户的id
    public static String getAccountno(String userid, String username, String avatar) {
        String accountno = null;
        //从Oxygenshow获取用户信息
//        String uri = C.API_PREFIX + "/ws/content/thirdPartyLogin?apikey=guest&terminaltype=20&version=new";
//        String sign = Oxygenshow.getSign(uri, "guest");
//        String url = uri + "&signature=" + sign;
//        String result = Oxygenshow.getPostResponse(url, "uniqueId=" + C.MD5(userid).substring(16) + "&avatarUrl=" + C.encode(avatar) + "&userName=" + C.encode(username));
//        JSONObject json = JSONObject.fromObject(result);
//        if (json.getString("result").equals("0")) {
//            accountno = json.getString("accountNo");
//        }
        return accountno;
    }
    //获得用户id
    public static String getAccountno(){
        if(C.getSession(C.SESSION_USER)!=null){
            User user= (User) C.getSession(C.SESSION_USER);
            if(user.getAccountno()!=null){
                return user.getAccountno();
            }else{
                String accountno=getAccountno(user.getId(),user.getNickname(),user.getAvatar());
                user.setAccountno(accountno);
                C.setSession(C.SESSION_USER,user);
                return accountno;
            }
        }
        if(C.getFromApplication(C.APP_ACCOUNTNO)!=null){
            return (String) C.getFromApplication(C.APP_ACCOUNTNO);
        }else{
            String username="iShowData";
            String avatar="http://www.ishowdata.com/images/logo.png";
            String userid="ishowdata";
            String accountno=getAccountno(userid,username,avatar);
            if(accountno!=null){
                C.saveToApplication(C.APP_ACCOUNTNO,accountno);
            }
            return accountno;
        }
    }
    //获取menu
    public static String getMenu(){
        String accountno= Oxygenshow.getAccountno();
        String uri=C.API_PREFIX+"/ws/tv/index?apikey="+accountno+"&terminaltype=20&version=new";
        String sign= Oxygenshow.getSign(uri,accountno);
        String url=uri+"&signature="+sign;
        String result= Oxygenshow.getContentFromURL(url);
        return result;
    }

    public static String getContentFromURL(String inurl) {
        String result = "{}";

        // 1.建立HttpClient对象
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {


            // 2.建立Get请求
            HttpGet get = new HttpGet(inurl);


            //设置请求的报文头部的编码
            get.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));


            //设置channel=sanxing的header
            get.setHeader(new BasicHeader("channel", "sanxing"));

            // 3.发送Get请求
            response = client.execute(get);

            // 4.处理请求结果
            HttpEntity entity = response.getEntity();
            ContentType contentType = ContentType.getOrDefault(entity);
            Charset charset = contentType.getCharset();
            // 获取字节数组
            byte[] content = EntityUtils.toByteArray(entity);

            if(charset==null) {
                result = new String(content);
            }else {
                result = new String(content, charset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(response!=null) {
                    response.close();
                }
                client.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String getPostResponse(String link, String params) {
        String result = "";
        //创建一个httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            //创建一个post对象
            HttpPost post = new HttpPost(link);
            //包装成一个Entity对象
            StringEntity entity = new StringEntity(params, "utf-8");
            //设置请求的内容
            post.setEntity(entity);
            //设置请求的报文头部的编码
            post.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
            //设置channel=sanxing的header
            post.setHeader(new BasicHeader("channel", "sanxing"));
            //执行post请求
            response = client.execute(post);
            //获取响应码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity httpentity = response.getEntity();
                ContentType contentType = ContentType.getOrDefault(httpentity);
                Charset charset = contentType.getCharset();
                // 获取字节数组
                byte[] content = EntityUtils.toByteArray(httpentity);
                result = new String(content, charset);
            } else {
                HttpEntity httpentity = response.getEntity();
                ContentType contentType = ContentType.getOrDefault(httpentity);
                Charset charset = contentType.getCharset();
                // 获取字节数组
                byte[] content = EntityUtils.toByteArray(httpentity);
                result = new String(content, charset);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                client.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return result;
    }

    //根据一组参数 获取签名
    public static String getSign(String path, String apikey) {
        String result = "";
        String[] array = path.split("\\?");
        String uri = array[0];
        String params = array[1];
        array = params.split("&");
        Arrays.sort(array);
        String sorted = StringUtils.join(array, "&");
        sorted = StringFilter(sorted);
        try {
            result = Oxygenshow.generator(sorted, Oxygenshow.getMD5(C.KEY + apikey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getMD5(String pSource) {
        String lDigest = "None";
        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.reset();
            mDigest.update(pSource.getBytes());
            byte[] byteArray = mDigest.digest();

            StringBuffer md5StringBuffer = new StringBuffer();

            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StringBuffer.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StringBuffer.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
            lDigest = md5StringBuffer.toString();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return lDigest;
    }

    public static String generator(String pStringToSign, String pKey) throws IllegalStateException, UnsupportedEncodingException {
        String lSignature = "None";
        try {
            Mac lMac = Mac.getInstance("HmacSHA1");
            SecretKeySpec lSecret = new SecretKeySpec(pKey.getBytes(), "HmacSHA1");
            lMac.init(lSecret);

            byte[] lDigest = lMac.doFinal(pStringToSign.getBytes());
            lSignature = new String(Base64Utils.encode(lDigest));
        } catch (NoSuchAlgorithmException lEx) {
            throw new RuntimeException("Problems calculating HMAC", lEx);
        } catch (InvalidKeyException lEx) {
            throw new RuntimeException("Problems calculating HMAC", lEx);
        }
        return lSignature;
    }

    public static String StringFilter(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\s*|\t|\r|\n]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
