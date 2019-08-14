package org.isd.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import net.sf.json.*;
import org.isd.dao.TransactionMapper;
import org.isd.pojo.Transaction;
import org.isd.pojo.User;
import org.isd.service.WXPayService;
import org.isd.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * 微信支付 业务逻辑 service 实现
 */
@Service
public class WXPayServiceImpl implements WXPayService {

    /** 订单 **/
    @Autowired
    private TransactionMapper transactionMapper;

    /**
     *调用微信接口，获取支付链接
     *
     *@参数 [out_trade_no, total_fee, body]
     *@返回值 net.sf.json.JSON
     */
    @Override
    public JSON getDataMap(String out_trade_no, String total_fee, String body) {
        InetAddress localhost = null;
        Map<String, String> responseDataMap = null;

        Map<String, String> map = new HashMap<String, String>();
        map.put("appid", SFData.APPID1); //公众号 id
        map.put("mch_id", SFData.MCHID); //商户号

        //生成随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        map.put("nonce_str", nonce_str); //随机字符串

        //map.put("body", SFData.BODY); //订单信息
        map.put("body", body);

        //map.put("out_trade_no", "2134321sdf"); //订单号
        map.put("out_trade_no", out_trade_no);

        //map.put("total_fee", "0"); //消费金额
        map.put("total_fee", total_fee);


        try {
            //获取服务器 ip
            localhost = InetAddress.getLocalHost();

            String hostAddr = localhost.getHostAddress();
            map.put("spbill_create_ip", hostAddr); //服务器 ip

            map.put("notify_url", SFData.NOTIFYURL); //回调接口
            map.put("trade_type", SFData.TRADETYPE); // 支付类型
            map.put("product_id", "2134321sdf"); //商品 id

            //生成签名
            String sign = WXPayUtil.generateSignature(map, SFData.KEY);
            map.put("sign", sign);

            //新建订单对象并赋值订单数据
            Transaction transaction = new Transaction();
            transaction.setStatus(0);
            transaction.setChannel(0);
            transaction.setCtime(new Date());
            transaction.setUtime(new Date());
            transaction.setName(body);
            transaction.setOrderid(out_trade_no);
            transaction.setPrice(Integer.parseInt(total_fee));

//            User user = (User) C.getSession(C.SESSION_USER);
//            transaction.setUserid(user.getId());

//            transaction.setV_id();
//            transactionMapper.save(transaction);

            //将 map 参数转换成 xml
            String requestXML = WXPayUtil.mapToXml(map);

            //调用统一下单 API 接口，响应的为 xml 数据
            String responseDataXML = HttpClientUtils.doPostXML(SFData.WXPAYURL, requestXML);

            //及那个 xml 字符串转化为 map
            responseDataMap = WXPayUtil.xmlToMap(responseDataXML);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return JSONObject.fromObject(responseDataMap);

    }

    /**
     *生成二维码
     *
     *@参数 [json]
     *@返回值 void
     */
    @Override
    public BufferedImage putQRCode(JSONObject jsonObject) {
        //获取 return_code
        String return_code = jsonObject.getString("return_code");

        //判断通信是否成功
        if ("SUCCESS".equals(return_code)){
            //获取 result_code 业务处理结果
            String result_code = jsonObject.getString("result_code");

            //判断处理结果
            if("SUCCESS".equals(result_code)){
                //获取 code_url
                String code_url = jsonObject.getString("code_url");

                //将 code_url 生成 二维码图片
                //二维码的宽高
                int width = 200;
                int height = 200;

                //创建 map
                Map<EncodeHintType,Object> hits = new HashMap<EncodeHintType,Object>();
                hits.put(EncodeHintType.CHARACTER_SET, "utf-8");

                try {
                    //创建一个 矩阵 对象
                    BitMatrix bitMatrix = new MultiFormatWriter().encode(code_url, BarcodeFormat.QR_CODE, width, height, hits);

                    //创建字节数组输出流
                    ByteOutputStream imgOut = new ByteOutputStream();

                    //将矩阵对象转换为流
                    MatrixToImageWriter.writeToStream(bitMatrix, "jpg", imgOut);

                    //字节数组输入流
                    ByteArrayInputStream imgIn = new ByteArrayInputStream(imgOut.getBytes());

                    //创建图片缓存对象
                    BufferedImage bImage = ImageIO.read(imgIn);
                    return bImage;

                } catch (WriterException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }else {
//                return "通信失败";
            }

        }else {
//            return "通信失败";
        }

        return null;
    }

    /**
     *用户扫码，wx 回调信息处理
     *
     *@参数 [inputStream]
     *@返回值 boolean
     */
    @Override
    public boolean getCallBack(InputStream inputStream) {
        //扫码结果
        boolean result = false;

        try {
            //使用输入流获取接口调用的返回结果
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

            //将微信调用接口返回信息转换为字符串
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;

            //循环获取信息
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }

            //关闭资源
            bufferedReader.close();
            inputStream.close();

            //将 xml 转换 map
            Map<String, String> cbMap = WXPayUtil.xmlToMap(stringBuffer.toString());

            //将 map 转换为有序map
            //SortedMap<String, String> sortedMap = WXPayUtil.getSortMap(cbMap);

            //判断签名是否正确
            if(WXPayUtil.isSignatureValid(cbMap, SFData.KEY)){

                //5、判断回调信息是否成功
                if("SUCCESS".equals(cbMap.get("result_code"))){

                    //获取商户订单号
                    //商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
                    String outTradeNo = cbMap.get("out_trade_no");

                    //6、数据库查找订单,如果存在则根据订单号更新该订单
                    Transaction transaction = transactionMapper.selectByTranID(outTradeNo);

                    //判断逻辑看业务场景
                    if(transaction != null && transaction.getStatus()==0){
                       Transaction transaction1 = new Transaction();

                        //修改支付状态，之前生成的订单支付状态是未支付，这里表面已经支付成功的订单
                        transaction1.setStatus(1);

                        //根据商户订单号更新订单
                        int rows = transactionMapper.updateByTranId(transaction1);

                        //7、通知微信订单处理成功
                        if(rows == 0){
                            return result;
                        }
                    }}
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
