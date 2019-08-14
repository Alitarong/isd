package org.isd.service.impl;

import net.sf.json.*;
import org.isd.dao.VideoMapper;
import org.isd.pojo.Video;
import org.isd.service.VideoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 视频相关业务实现
 */
@Service
public class VideoServiceImpl implements VideoService {

    /** 导入视频 dao **/
    @Resource
    private VideoMapper videoMapper;

    /**
     *查看所有图书
     *
     *@参数 []
     *@返回值 java.util.List<org.isd.pojo.Video>
     */
    @Override
    public List<Video> getVideo() {

        return videoMapper.listAll();

    }

    /**
     *首页
     *
     *@参数 []
     *@返回值 net.sf.json.JSONObject
     */
    @Override
    public JSONObject getFirstPage() {

        JSONArray array=new JSONArray();
        JSONObject jsonObject=new JSONObject();

        List<Video>list=videoMapper.listAll();

        for(Video v:list){

            JSONObject object=new JSONObject();

            object.put("id",v.getId());
            object.put("name",v.getName());
            object.put("image",v.getCover());

            array.add(object);

        }

        jsonObject.put("title","氧秀运动");
        jsonObject.put("id","allVideo");
        jsonObject.put("childs",array);

        return jsonObject;
    }

    /**
     *通过 id 查询视频
     *
     *@参数 [id]
     *@返回值 org.isd.pojo.Video
     */
    @Override
    public Video getById(Integer id) {

        return videoMapper.selectById(id);

    }
}
