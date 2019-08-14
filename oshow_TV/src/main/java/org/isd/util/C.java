package org.isd.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * create by Alitarong from youshu
 *
 * @author Administrator
 */
public class C {

    public final static String SESSION_USER                 = "sess_user";
    public static final String COOKIE_DEVICE                = "device";
    public static final String COOKIE_ORDERID               = "orderid";
    public static final String WXAPPID                      = "wxf75eb89c0081ba93";
    public static final String USERID                       = "userid";
    public static final String WXSECRET                     = "24119ba5db75b9f2511212677c753e10";
    public static final String WEIXINPAY                    = "https://api.mch.weixin.qq.com/pay";
    public static final String RESULT                       = "result";
    public static final String SUCCESS                      = "SUCCESS";
    public static final String ERRMSG                       = "errmsg";
    public static final String STATUSCHECK                  = "http://master.dig88.cn:8080/payment/transaction/status";
    public static String VERSION 					        = "20170412";
    public final static String KEY					= "cf13aa4fbd2d7da7cef6f65f6d632758";
    public final static String API_PREFIX			= "http://if.douguo.com/interfacetest2";
    public final static String APP_ACCOUNTNO		= "app_accountno";
    //at 2019.5.14 for
    //提取 session
    public static Object getSession(String key) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(true);
        return session.getAttribute(key);
    }

    //清除 Session
    public static void removeSession(String key) {
        // TODO Auto-generated method stub
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(true);
        session.removeAttribute(key);

    }

    //读取 cookie
    public static String getCookie(String name) {
        // TODO Auto-generated method stub
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Cookie[] cookies = request.getCookies();
        try {
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    Cookie c = cookies[i];
                    if (c.getName().equalsIgnoreCase(name)) {
                        return URLDecoder.decode(c.getValue(), "ISO-8859-1");

                    }

                }
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    //设置 cookie， cookie有效期为一年
    public static void setCookie(String name, String value, int life) {
        // TODO Auto-generated method stub
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        try {
            Cookie cookie = new Cookie(name, URLEncoder.encode(value, "ISO-8859-1"));
            cookie.setMaxAge(life);
            cookie.setPath("/");
            response.addCookie(cookie);

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getContentFromURL(String inurl) {
        // TODO Auto-generated method stub
        String result = "{}";

        //1.建立 HttpClient对象
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            //2.建立 GET请求
            HttpGet get = new HttpGet(inurl);
            //设置请求报文头部编码
            get.setHeader(new BasicHeader("Content-type", "application/x-www-from-urlencoded; charset=utf-8"));
            //设置期望服务端返回的编码
            get.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
            //3.发送 GET请求
            response = client.execute(get);
            //4.处理请求结果
            HttpEntity entity = response.getEntity();
            ContentType contentType = ContentType.getOrDefault(entity);
            Charset charset = contentType.getCharset();
            //获取字节数组
            byte[] content = EntityUtils.toByteArray(entity);
            if (charset == null) {
                result = new String(content);
            } else {
                result = new String(content, charset);
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // 保存session
    public static void setSession(String key, Object value) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(true);
        session.setAttribute(key, value);
    }


    //删除cookie
    static public void removeCookie(String name) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    //at 2019.5.20 for get gender
    static public String getGender(String key) {
        String result = "";
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(true);
        if ("男".equals(session.getAttribute(key))) {
            result = "m";
        } else if ("女".equals(session.getAttribute(key))) {
            result = "f";
        } else {
            result = "a";
        }
        return result;
    }

    public static String getCourseid(String key) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(true);
        return session.getAttribute(key).toString();
    }

    //对 url 处理一下
    //单参
    public final static String encode(String url) {
        String resp = "";
        try {
            resp = URLEncoder.encode(url, "ISO-8859-1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    //双参
    public final static String encode(String str, String code) {
        String resp = "";
        try {
            resp = URLEncoder.encode(str, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    //getRequest
    public static ServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request;
    }
    //getResponse
    public static ServletResponse getResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        return response;
    }

    //getPostResponse
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

            //设置期望服务端返回的编码
            post.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));

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

    static public String formatParameter(Map<String, Object> params) {
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(C.encode(param.getKey(), "utf-8"));
            postData.append('=');
            postData.append(C.encode(String.valueOf(param.getValue()), "utf-8"));
        }
        return postData.toString();
    }

    public final static String decode(String str, String code) {
        String resp = "";
        try {
            resp = URLDecoder.decode(str, code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    public static String object2json(Object object) {
        //用于处理JSON中的时间戳
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());

        String result = JSONObject.fromObject(object, jsonConfig).toString();
        return result;
    }

    public static String array2json(Object object) {
        //用于处理JSON中的时间戳
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());

        String result = JSONArray.fromObject(object, jsonConfig).toString();
        return result;
    }


    //获取指定位数的随机字符串(包含小写字母、大写字母、数字,0<length)
    public static String randomString(int length) {
        //随机字符串的随机字符库
        String KeyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuffer sb = new StringBuffer();
        int len = KeyString.length();
        for (int i = 0; i < length; i++) {
            sb.append(KeyString.charAt((int) Math.round(Math.random() * (len - 1))));
        }
        return sb.toString();
    }
    public static String MD5(String str) {
        String ret = "";
        ret = DigestUtils.md5Hex(str);
        return ret;
    }
    static public Object getFromApplication(String key){
        ServletContext context = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        return context.getAttribute(key);
    }
    static public void saveToApplication(String key, Object object){
        ServletContext context = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        context.setAttribute(key, object);
    }
    //订单ID
    public final static String getOrderid(){
        String result =C.dateToString(new Date(),"yyyyMMddHHmmss");
        Random random = new Random();
        for(int i=0;i<3;i++){
            result +=random.nextInt(10);
        }
        return result;
    }
    public static  String dateToString(Date time,String pattern){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(time);
        return calendarToString(calendar,pattern);
    }
    //获取天气预报
    public static String calendarToString(Calendar time, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.CHINA);
        String returnValue = formatter.format(time.getTime());
        return returnValue;
    }
}
