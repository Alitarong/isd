package org.isd.service;

import org.isd.pojo.User;

/**
 * 用户相关功能接口 —— 业务逻辑层
 */
public interface UserService {
    /** 获取用户信息 **/
    User getUser();

    /** 通过接口返回获得存取令牌 **/
    User getAccessToken(String code, String state);

    /** 检测用户登录状态 决定是否登录成功 **/
    boolean checkLogin();

    /** 退出 **/
    void logOut();

    /** 通过用户id查找 **/
    User selectById(String uid);
}
