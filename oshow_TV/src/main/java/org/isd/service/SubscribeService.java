package org.isd.service;


import org.isd.pojo.Subscribe;

import java.util.List;

/**
 * 购买
 */
public interface SubscribeService {

    /** 添加购买记录 **/
    void save(String userid, Integer v_id);

    /** 获取某用户订阅的视频 分页显示 **/
    List<Subscribe> listByPageUid(String userid, Integer page, Integer rows);

    /** 显示购买总条数 **/
    Integer total(String userid);

    /** 判断用户是否购买 **/
    boolean isPurchased(String userid, Integer v_id);

}
