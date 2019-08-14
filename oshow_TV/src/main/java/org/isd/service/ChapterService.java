package org.isd.service;

import com.isd.oxygenshow.entity.Chapter;

import java.util.List;

public interface ChapterService {
    //添加详情
    public int saveChapter(Chapter chapter);

    //通过v_id t_id获取对应的。。完整版 分解版 的信息
    public List<Chapter> getChapterOfVideo(Integer v_id, Integer t_id);

    //通过v_id serno 获取具体小节的信息
    public Chapter selectOfSerno(Integer v_id, Integer serno);

    //分页

    //通过id查看信息
    public Chapter getById(Integer id);
}
