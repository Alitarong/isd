package org.isd.controller;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.isd.pojo.User;
import org.isd.service.UserService;
import org.isd.util.C;
import org.isd.util.Oxygenshow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * 用户相关功能接口——控制器
 */
@RestController
@RequestMapping(value = "/user",method = RequestMethod.GET )
public class UserController {

    @Autowired
    private UserService userService;

    //用户登录
    @RequestMapping(value = "/test.shtml",method = RequestMethod.GET)
    public String test(ModelMap map){
        String uri= C.API_PREFIX +"/ws/tv/index?apikey=1556466165710&terminaltype=20&version=new";

        String sign= Oxygenshow.getSign(uri,"1556466165710");
        String url=uri+"&signature="+sign;
        String result=Oxygenshow.getContentFromURL(url);
        map.put("message",result);

        return "global/message";
    }
    //返回第三方登录地址
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public JSON login(ModelMap map){

        String deviceid=C.getCookie(C.COOKIE_DEVICE);

        if(deviceid==null){
            deviceid= UUID.randomUUID().toString();
            C.setCookie(C.COOKIE_DEVICE,deviceid,2*365*24*3600);
        }

        String cbackUrl = "org.ai-abc.com/oxygenshow/user/callback";
        String logUrl = "https://open.weixin.qq.com/connect/qrconnect?appid=wxf75eb89c0081ba93&redirect_uri=org.ai-abc.com/oxygenshow/user/callback&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        String url="http://user.dig24.cn/login.php?state=Oxygenshow_"+deviceid;

        map.put("message", url);

        return  JSONObject.fromObject(map);
    }

    @RequestMapping(value = "/callback")
    public String cBack(String signature, String timestamp, String nonce,String echostr){
        /*String token = "ishowdata";
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);*/
        System.out.println("signature="+signature);
        System.out.println("timestamp="+timestamp);
        System.out.println("nonce="+nonce);
        System.out.println("echostr="+echostr);
        return echostr;
    }

    @RequestMapping(value = "/test")
    public String test(){
        return "iamR6OTMRFutUwZz";
    }
    
    /**@描述:	监测用户登陆状态**/
    @RequestMapping(value = "/checkstatu",method = RequestMethod.GET)
    public JSON status(ModelMap map) {
        //返回 json中包含 key-value
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        if (userService.checkLogin()) {
            User user = (User) C.getSession(C.SESSION_USER);
            LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("user", user);
            result.put(C.RESULT, C.SUCCESS);
            result.put("data", data);
        } else {
            result.put(C.RESULT, C.SUCCESS);
            result.put("errmsg", "未登录");
        }
        map.put("message", C.object2json(result));
        return JSONObject.fromObject(map);
    }
    
    /**@描述:	退出登陆**/
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public JSON logout(ModelMap map) {
        LinkedHashMap<String,Object>result=new LinkedHashMap<>();
        userService.logOut();
        result.put(C.RESULT, C.SUCCESS);
        map.put("message", C.object2json(result));//登陆二维码
        return JSONObject.fromObject(map);
    }

    //第三方登录时用到的回调函数
    @RequestMapping(value = "/pretend", method = RequestMethod.GET)
    public String pretend(@RequestParam(value="code", required=true)String code, ModelMap map){
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        User user = userService.selectById(code);
        C.setSession(C.SESSION_USER, user);
        result.put(C.RESULT, C.SUCCESS);
        map.put("message", C.object2json(result));
        return "global/message";
    }

    /** @描述 授权事件接收URL **/
    @GetMapping("/revice")
    public String getRevice(){
        return "success";
    }

//    /** @描述 消息与事件接收URL **/
//    @GetMapping("/${APPID}/getmsg")
//    public String getMsg(@PathVariable String APPID){
//        return APPID;
//    }

}
