package org.isd.dao;

import com.isd.oxygenshow.entity.Subscribe;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscribeMapper {
    //添加购买信心
    public int save(Subscribe subscribe);

    //更新购买信息
    public int update(Subscribe subscribe);

    //根据用户id 视频v_id 提取对象
    public Subscribe findByUserVid(@Param("userid") String userid, @Param("v_id") Integer v_id);

    //列出用户购买的视频分页查询
    public List<Subscribe> listPageByUid(@Param("userid") String userid, @Param("page") Integer page, @Param("rows") Integer rows);

    //获取用户购买总条数
    public Integer total(@Param("userid") String userid);
}
