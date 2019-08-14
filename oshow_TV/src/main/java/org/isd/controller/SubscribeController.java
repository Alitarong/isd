package org.isd.controller;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.isd.pojo.User;
import org.isd.service.SubscribeService;
import org.isd.service.UserService;
import org.isd.util.C;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class SubscribeController {
    @Resource
    private UserService userService;
    @Resource
    private SubscribeService subscribeService;
    @RequestMapping("/list")
    @ResponseBody
    public JSON list(String userid,Integer page,Integer rows,ModelMap map) {
        if(userService.checkLogin()){
            User user= (User) C.getSession(C.SESSION_USER);
            if(subscribeService.listByPageUid(userid,page,rows).size()>0){
                map.put("errno", 0);
                map.put("errmsg", "");
                map.put("list",subscribeService.listByPageUid(user.getId(),page,rows));
            }else{
                map.put("errno", 1);
                map.put("msg","用户未登录！");
            }
        }
        return JSONObject.fromObject(map);
    }
}
