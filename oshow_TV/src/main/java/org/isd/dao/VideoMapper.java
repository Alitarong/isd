package org.isd.dao;


import org.apache.ibatis.annotations.Param;
import org.isd.pojo.Video;

import java.util.List;

/**
 * 视屏展示相关业务
 */
public interface VideoMapper {

    /** 查看所有视频 **/
    List<Video> listAll();

    /** 存取数据 **/
    int insertVideo(@Param("videos") List<Video> videos);

    /** 通过id进行查看视频详情 **/
    Video selectById(@Param("id") Integer id);

}
