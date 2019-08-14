package org.isd.service.impl;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.isd.dao.ChapterMapper;
import org.isd.dao.VideoMapper;
import org.isd.pojo.Chapter;
import org.isd.service.ChapterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 小节业务实现
 */
@Service
public class ChapterServiceImpl implements ChapterService {

    /** 导入小节 dao **/
    @Resource
    private ChapterMapper chapterMapper;

    /** 导入视频 dao **/
    @Resource
    private VideoMapper videoMapper;

    /**
     *保存小节
     *
     *@参数 [chapter]
     *@返回值 int
     */
    @Override
    public int saveChapter(Chapter chapter) {

        return 0;

    }


    /**
     *通过v_id t_id获取对应  。。完整版 分解版  的信息
     *
     *@参数 [v_id, t_id]
     *@返回值 java.util.List<org.isd.pojo.Chapter>
     */
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

    /**
     *通过序号查询
     *
     *@参数 [v_id, serno]
     *@返回值 org.isd.pojo.Chapter
     */
    @Override
    public Chapter selectOfSerno(Integer v_id, Integer serno) {

        Chapter chapter=chapterMapper.selectOfSerno(v_id,serno);

        return chapter;

    }

    /**
     *通过 id 查询
     *
     *@参数 [id]
     *@返回值 org.isd.pojo.Chapter
     */
    @Override
    public Chapter getById(Integer id) {

        return chapterMapper.selectById(id);

    }
}
