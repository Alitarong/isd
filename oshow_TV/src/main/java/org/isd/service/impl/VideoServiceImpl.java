package org.isd.service.impl;

import com.isd.oxygenshow.dao.VideoMapper;
import com.isd.oxygenshow.entity.Video;
import com.isd.oxygenshow.service.VideoService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
    @Resource
    private VideoMapper videoMapper;

    //查看所有图书
    @Override
    public List<Video> getVideo() {
        return videoMapper.listAll();
    }

    //首页
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

    @Override
    public Video getById(Integer id) {
        return videoMapper.selectById(id);
    }
}
