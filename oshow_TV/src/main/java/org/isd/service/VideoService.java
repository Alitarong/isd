package org.isd.service;

import com.isd.oxygenshow.entity.Video;
import net.sf.json.JSONObject;

import java.util.List;

public interface VideoService {
    //查看所有的视频列表
    public List<Video> getVideo();
    //获取视频首页
    public JSONObject getFirstPage();
    //通过id查询视频
    public Video getById(Integer id);
}
