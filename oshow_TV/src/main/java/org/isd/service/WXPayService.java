package org.isd.service;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * 微信支付 业务逻辑 service
 *
 * @创建人 Alitarong
 * @创建时间 2019/8/13
 */
public interface WXPayService {
    /** 调用微信接口，获取支付链接 code_url **/
    JSON getDataMap(String out_trade_no, String total_fee, String body);

    /** 客户端调用接口获取支付二维码 **/
    BufferedImage putQRCode(JSONObject jsonObject);

    /** 用户扫码，wx 回调处理 **/
    boolean getCallBack(InputStream inputStream);

    /** wx 支付结果查询 **/
}
