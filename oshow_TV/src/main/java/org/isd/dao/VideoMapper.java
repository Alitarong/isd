package org.isd.dao;

import com.isd.oxygenshow.entity.Video;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoMapper {
    //查看所有视频
    public List<Video> listAll();
    //存取数据
    public int insertVideo(@Param("videos") List<Video> videos);
    //通过id进行查看视频详情
    public Video selectById(@Param("id") Integer id);
}
