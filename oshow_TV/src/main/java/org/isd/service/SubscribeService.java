package org.isd.service;

import com.isd.oxygenshow.entity.Subscribe;

import java.util.List;

public interface SubscribeService {
    //添加购买记录
    public void save(String userid, Integer v_id);

    //获取某用户订阅的视频 分页显示
    public List<Subscribe> listByPageUid(String userid, Integer page, Integer rows);

    //显示购买总条数
    public Integer total(String userid);

    //判断用户是否购买
    public boolean isPurchased(String userid, Integer v_id);



}
