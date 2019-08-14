package org.isd.service.impl;

import net.sf.json.JSONObject;
import org.isd.dao.UserMapper;
import org.isd.pojo.User;
import org.isd.service.UserService;
import org.isd.util.C;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * 用户相关功能接口——业务逻辑实现层
 */
@Service
public class UserServiceImpl implements UserService {

    /** 注入userMapper **/
    @Resource
    private UserMapper userMapper;

    /**
     *获取用户信息
     *
     *@参数 []
     *@返回值 org.isd.pojo.User
     */
    @Override
    public User getUser() {

        String userid = null;

        if(C.getCookie(C.COOKIE_DEVICE)==null) {

            userid = C.randomString(30);

            C.setCookie(C.COOKIE_DEVICE, userid, 2*365*24*3600);

        }else {

            userid = C.getCookie(C.COOKIE_DEVICE).toString();

        }

        User user = new User();
        user.setId(userid);

        return user;

    }

    /**
     *通过接口返回access_token
     *
     *@参数 [code, state]
     *@返回值 org.isd.pojo.User
     */
    @Override
    public User getAccessToken(String code, String state) {

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + C.WXAPPID + "&secret=" + C.WXSECRET + "&code=" + code + "&grant_type=authorization_code";

        String content = C.getContentFromURL(url);

        //对用户进行检测
        User user = this.retrieve(content);

        if (user != null) {
            user.setDeviceid(state.replace("Oxygenshow_",""));
            user.setLastlogin(new Date());
            user=this.detail(user);
            user=this.addUser(user);
        }

        return user;

    }

    /**
     *通过接口刷新Token
     *
     *@参数 [user]
     *@返回值 org.isd.pojo.User
     */
    private User refresh_token(User user) {

        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + C.WXAPPID + "&grant_type=refresh_token&refresh_token=" + user.getRefresh_token();
        String content = C.getContentFromURL(url);

        user = this.retrieve(content);

        if (user != null) {

            user = this.addUser(user);
            user = this.detail(user);

        }

        return user;

    }

    /**
     *添加用户信息
     *
     *@参数 [user]
     *@返回值 org.isd.pojo.User
     */
    private User addUser(User user) {

        User existed = userMapper.selectById(user.getId());

        //判断用户是否存在
        if (existed == null) {

            user.setCreate_time(new Date());
            user.setModify_time(new Date());//更改时间

            userMapper.insert(user);

            return user;

        } else {

            existed.setAccess_token(user.getAccess_token());
            existed.setDeviceid(user.getDeviceid());
            existed.setRefresh_token(user.getRefresh_token());
            existed.setRefresh_expire(user.getRefresh_expire());
            existed.setAccess_expire(user.getAccess_expire());
            existed.setModify_time(new Date());

            //获取用户的Oxygenshow的id
            //existed.setAccountno(Oxygenshow.getAccountno(user.getId(),user.getNickname(),user.getAvatar()));
            userMapper.update(existed);
            return existed;
        }

    }

    /**
     *详情
     *
     *@参数 [user]
     *@返回值 org.isd.pojo.User
     */
    private User detail(User user) {

        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + user.getAccess_token() + "&openid=" + user.getId();
        String content = C.getContentFromURL(url);

        JSONObject json = JSONObject.fromObject(content);

        if (!json.containsKey("errcode")) {

            user.setNickname(json.getString("nickname"));
            user.setAvatar(json.getString("headimgurl"));
            user.setLocation(json.getString("province") + "-" +
                    json.getString("city"));

            userMapper.update(user);

        }

        return user;

    }

    /**
     *检索
     *
     *@参数 [resp]
     *@返回值 org.isd.pojo.User
     */
    public User retrieve(String resp) {

        User user = null;
        JSONObject json = JSONObject.fromObject(resp);

        //判断是否含有关键字“errcode”
        if (!json.containsKey("errcode")) {

            user = new User();

            user.setAccess_token(json.getString("access_token"));
            System.out.println("access_token="+json.getString("access_token"));

            user.setRefresh_token(json.getString("refresh_token"));
            System.out.println("refresh_token="+json.getString("refresh_token"));

            int expire=json.getInt("expires_in");
            Date access_expire=new Date(new Date().getTime()+expire*1000l);
            Date refresh_expire=new Date(new Date().getTime()+30 * 24 * 3600l * 1000);

            user.setAccess_expire(access_expire);
            user.setRefresh_expire(refresh_expire);
            user.setId(json.getString("openid"));

        }

        return user;

    }

    /**
     *检查登录状态
     *
     *@参数 []
     *@返回值 boolean
     */
    @Override
    public boolean checkLogin() {

        User user = null;

        //获取session中对象
        if (C.getSession(C.SESSION_USER) != null) {

            user = (User) C.getSession(C.SESSION_USER);

            if (user.getId() == null || user.getNickname() == null || user.getId().trim().length() < 1 || user.getNickname().trim().length() < 1) {
                C.removeSession(C.SESSION_USER);
                user = null;
            }

        }

        if (user == null) {
            //根据cookie中的设备  id搜索用户或者refresh_token已过期重新登录
            String deviceid = C.getCookie(C.COOKIE_DEVICE);

            if (deviceid == null) {
                //自动生成
                deviceid = UUID.randomUUID().toString();
                C.setCookie(C.COOKIE_DEVICE, deviceid, 2 * 365 * 24 * 3600);
            }

            user = userMapper.findByDeviceId(deviceid);

        }

        Date now = new Date();

        if (user == null || user.getId() == null || user.getRefresh_expire() == null || user.getRefresh_token() == null || user.getRefresh_expire().before(now)) {
            return false;
        }

        //如果access_token已过期，则刷新token
        if (user.getAccess_expire().before(now)) {
            user = this.refresh_token(user);
        }

        if (user != null && user.getNickname() != null && user.getNickname().trim().length() > 0) {
            C.setSession(C.SESSION_USER, user);
            return true;
        } else {
            return false;
        }
    }

    /**
     *用户退出
     *
     *@参数 []
     *@返回值 void
     */
    @Override
    public void logOut() {

        this.checkLogin();

        User user = (User) C.getSession(C.SESSION_USER);

        if (user != null) {

            user = userMapper.selectById(user.getId());

            user.setRefresh_token(null);
            user.setRefresh_expire(null);

            userMapper.update(user);

            C.removeSession(C.SESSION_USER);

        }

    }

    /**
     *通过 id 获取用户信息
     *
     *@参数 [uid]
     *@返回值 org.isd.pojo.User
     */
    @Override
    public User selectById(String uid) {

        return userMapper.selectById(uid);

    }

}
