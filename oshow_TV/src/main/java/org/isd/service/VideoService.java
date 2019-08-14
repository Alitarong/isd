package org.isd.service;

import net.sf.json.JSONObject;
import org.isd.pojo.Video;

import java.util.List;

/**
 * 视频业务
 */
public interface VideoService {

    /** 查看所有的视频列表 **/
    List<Video> getVideo();

    /** 获取视频首页 **/
    JSONObject getFirstPage();

    /** 通过id查询视频 **/
    Video getById(Integer id);

}
