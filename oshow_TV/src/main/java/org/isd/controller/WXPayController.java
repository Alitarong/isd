package org.isd.controller;

import net.sf.json.JSONObject;
import org.isd.service.TransactionService;
import org.isd.service.UserService;
import org.isd.service.WXPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付接口
 */
@RestController
@RequestMapping(value = "/wxpay")
public class WXPayController {
    /** 微信支付业务处理 **/
    @Autowired
    private WXPayService wxPayService;

    @Autowired
    private UserService userService;

    /** 用户点击支付生成订单 **/
    @Autowired
    private TransactionService transactionService;

    /**
     *获取支付二维码接口 -- 给客户端返回支付链接
     *
     *@参数 [request, response, out_trade_no, total_fee, body]
     *@返回值 void
     */
    @GetMapping(value = "/putqrcode")
    public void putQRCode(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "out_trade_no", required = true) String out_trade_no,
                          @RequestParam(value = "total_fee", required = true) String total_fee,
                          @RequestParam(value = "body", required = true) String body){

        if(!userService.checkLogin()){

            try {
                response.getWriter().println("用户未登录");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        //调用微信接口，获取支付链接
        JSONObject jsonObject = (JSONObject) wxPayService.getDataMap(out_trade_no, total_fee, body);

        //使用支付链接，生成支付二维码传给客户端
        BufferedImage bImage = wxPayService.putQRCode(jsonObject);

        //输出流对象
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ImageIO.write(bImage, "jpg", outputStream);

            bImage.flush();
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *wx 通过 notify_url 告知支付结果
     *
     *@参数 []
     *@返回值 void
     */
    @GetMapping(value = "/getmsg")
    public void getCBack(HttpServletRequest request, HttpServletResponse response){

        try {
            //使用输入流获取接口调用的返回结果
            InputStream inputStream = request.getInputStream();

            boolean result = wxPayService.getCallBack(inputStream);

            if (result){
                response.getWriter().println("success");
                return;
            }
            response.getWriter().println("fail");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *通知地址,查询订单状态，调用微信接口查看最终订单状态
     *
     *@参数 []
     *@返回值 void
     */
    @GetMapping(value = "/checkpay")
    public Map<String, String> getMSG(@RequestParam(value = "", required = true)String out_trade_no){
        boolean result = transactionService.selectByNo(out_trade_no);

        Map<String,String> map = new HashMap<>();

        if (result){
            map.put("error","0");
            map.put("msg", "充值成功！");
            return map;
        }

        map.put("error","1");
        map.put("msg", "充值失败！");
        return map;
    }


}
