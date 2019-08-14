package org.isd.service.impl;

import com.isd.oxygenshow.dao.ChapterMapper;
import com.isd.oxygenshow.dao.VideoMapper;
import com.isd.oxygenshow.entity.Chapter;
import com.isd.oxygenshow.service.ChapterService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ChapterServiceImpl implements ChapterService {

    @Resource
    private ChapterMapper chapterMapper;
    @Resource
    private VideoMapper videoMapper;

    @Override
    public int saveChapter(Chapter chapter) {
        return 0;
    }

    //通过v_id t_id获取对应  。。完整版 分解版  的信息
    @Override
    public List<Chapter> getChapterOfVideo(Integer v_id, Integer t_id) {
        List<Chapter> chapters=chapterMapper.selectChapterOfVideo(v_id,t_id);
        JSONArray array=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        for(Chapter cha:chapters){
            JSONObject object=new JSONObject();
            object.put("id",cha.getId());
            object.put("v_id",cha.getV_id());
            object.put("name",cha.getName());//第几戏   完整版  分解版
            object.put("url",cha.getUrl());
            object.put("serno",cha.getSerno());
        }
        jsonObject.put("title","视频详情");
        jsonObject.put("id","chapter");
        jsonObject.put("childs",array);
        return chapters;
    }

    @Override
    public Chapter selectOfSerno(Integer v_id, Integer serno) {
        Chapter chapter=chapterMapper.selectOfSerno(v_id,serno);
        return chapter;
    }

    @Override
    public Chapter getById(Integer id) {
        return chapterMapper.selectById(id);
    }
}
