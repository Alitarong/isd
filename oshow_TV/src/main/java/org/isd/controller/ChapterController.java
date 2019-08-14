package org.isd.controller;

import com.isd.oxygenshow.entity.Chapter;
import com.isd.oxygenshow.entity.User;
import com.isd.oxygenshow.entity.Video;
import com.isd.oxygenshow.service.ChapterService;
import com.isd.oxygenshow.service.SubscribeService;
import com.isd.oxygenshow.service.UserService;
import com.isd.oxygenshow.service.VideoService;
import com.isd.oxygenshow.util.C;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class ChapterController {
    @Resource
    private ChapterService chapterService;
    @Resource
    private VideoService videoService;

    //通过v_id t_id获取 小节 完整版  分解版 详细信息
    @RequestMapping(value = "/getChapterOfVideo")
    @ResponseBody
    public JSON getChapterOfVideo(@RequestParam(name="v_id",defaultValue = "1")Integer v_id,@RequestParam(name="t_id",defaultValue = "1")Integer t_id, ModelMap map){
        Video video=videoService.getById(v_id);
        List<Chapter>list=chapterService.getChapterOfVideo(v_id,t_id);
        map.put("chapters",list);
        map.put("text",video.getText());
        return JSONObject.fromObject(map);
    }
    //通过v_id serno 查询详细的
    @RequestMapping(value ="/selectOfSerno" )
    @ResponseBody
    public Chapter selectOfSerno(int v_id,int serno){
        return chapterService.selectOfSerno(v_id,serno);
    }

    //通过id获取对应 三类视频的信息
    @RequestMapping("/selectById")
    @ResponseBody
    public Chapter selectById(@RequestParam("id") Integer id){
        return chapterService.getById(id);
    }
    //分页查询

    /**
     * 添加购买
     */
    @Resource
    private SubscribeService subscribeService;
    @Resource
    private UserService userService;
    @RequestMapping("/url")
    @ResponseBody
    public JSON url(@RequestParam(value = "id",required = true)int id,ModelMap map){
        boolean logged =userService.checkLogin();
        JSONObject jsonObject=new JSONObject();
        JSONArray jsonArray=new JSONArray();
        //视频地址
        Chapter chapter=chapterService.getById(id);
        if(logged==true){
            User user= (User) C.getSession(C.SESSION_USER);
            //如果是已购买
            if(subscribeService.isPurchased(user.getId(),chapter.getV_id())){
                JSONObject object=new JSONObject();
                JSONArray array=new JSONArray();
                jsonObject.put("v_id",chapter.getV_id());
                jsonObject.put("name",chapter.getName());
                jsonObject.put("url",chapter.getUrl());

            }else {
                jsonObject.put("msg","未购买");
            }
        }
        return null;
    }
}
