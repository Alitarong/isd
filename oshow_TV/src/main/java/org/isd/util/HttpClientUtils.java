package org.isd.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @创建人 Alitarong
 * @创建时间 2019/8/12
 */
public class HttpClientUtils {

    /**
     *发送 post 请求
     *
     *@参数 [url, map]
     *@返回值 java.lang.String
     */
    public static String  doPost(String url, Map<String,Object> map){
        String result = "{}";

        //1.建立 HttpClient对象
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        try {
            //2.建立 POST (GET） 请求
            //HttpGet get = new HttpGet(inurl);
            HttpPost post = new HttpPost(url);

            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String,String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
            }
            if(list.size() > 0){
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list);
                post.setEntity(entity);
            }
            response = client.execute(post);
            if(response != null){
                HttpEntity resEntity = (HttpEntity) response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity);
                }
            }

            if (response != null) {
                response.close();
            }
            client.close();

//            //设置请求报文头部编码
//            post.setHeader(new BasicHeader("Content-type", "application/x-www-from-urlencoded; charset=utf-8"));
//
//            //设置期望服务端返回的编码
//            post.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));

        } catch (
                ClientProtocolException e) {
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

    /**
     *请求参数为 xml 的 post 请求
     *
     *@参数 [url, requestDataXML]
     *@返回值 java.lang.String
     */
    public static String doPostXML(String url, String requestDataXML){
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        String result = null;

        //创建 client 连接对象
        client = HttpClients.createDefault();

        //创建 post 连接对象
        HttpPost post = new HttpPost(url);

        //创建连接请求参数，并设置连接参数
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(15000)
                .setConnectionRequestTimeout(60000)
                .setSocketTimeout(60000)
                .build();

        //为 httpPost 请求设置参数
        post.setConfig(requestConfig);

        //将上传参数存放到 entity 属性中
        post.setEntity(new StringEntity(requestDataXML, "utf-8"));

        //为 post 请求添加头信息
        post.addHeader("Content-type", "text/xml");

        try {
            //发送请求
            response = client.execute(post);

            //从响应获取返回内容
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf-8");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
