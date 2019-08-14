package org.isd.dao;

import com.isd.oxygenshow.entity.Chapter;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChapterMapper {
    //通过V_id t_id 进行 完整版、分解版 对应的信息
    public List<Chapter> selectChapterOfVideo(@Param("v_id") Integer v_id, @Param("t_id") Integer t_id);
    //更新
    public void update(@Param("chapter") Chapter chapter);
    //通过v_id  serno 获取具体小节视频对应的信息
    public Chapter selectOfSerno(@Param("v_id") Integer v_id, @Param("serno") Integer serno);
    //分页
    //通过id进行查找
    public Chapter selectById(@Param("id") Integer id);
}
