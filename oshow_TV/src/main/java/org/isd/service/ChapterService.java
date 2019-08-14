package org.isd.service;

import org.isd.pojo.Chapter;

import java.util.List;

/**
 * 小节业务
 */
public interface ChapterService {

    /** 添加详情 **/
    int saveChapter(Chapter chapter);

    /** 通过v_id t_id获取对应的。。完整版 分解版 的信息 **/
    List<Chapter> getChapterOfVideo(Integer v_id, Integer t_id);

    /** 通过v_id serno 获取具体小节的信息 **/
    Chapter selectOfSerno(Integer v_id, Integer serno);

    //分页

    /** 通过id查看信息 **/
    Chapter getById(Integer id);

}
