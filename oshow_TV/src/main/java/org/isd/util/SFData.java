package org.isd.util;

public class SFData {

	/** 获取支付链接请求 **/
	public static final String WXPAYURL						=	"https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	/** 公众号 AppId **/
	public static final String APPID1						=	"wxf75eb89c0081ba93";
	
	/** 第三方 AppId **/
	public static final String APPID3						=	"wxbc75a8eebd647148";
	
	/** mch_id 商户号 **/
	public static final String MCHID						=	"1545086431";
	
	/** 商品描述 body **/
	public static final String BODY							=	"氧秀运动-视频支付";

	/** 通知地址	notify_url **/
	public static final String NOTIFYURL				    =	"http://ai-abc.com/oxygenshow/wxpay/getmsg";

	/** 扫码回调	call_back **/
	public static final String CALL_BACK					=	"http://org.ai-abc.com/oxygenshow/wxpay/payCallback";

	/** 交易类型	trade_type :JSAPI -JSAPI支付、NATIVE -Native支付、APP -APP支付**/
	public static final String TRADETYPE				    =	"NATIVE";

	/** 加密解密 密匙 **/
	public static final String KEY				    		=	"wCjBiVdDzlpG0FP3DLx2eNWWQduNsLkp";

}
