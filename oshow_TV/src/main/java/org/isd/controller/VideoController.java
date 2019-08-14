package org.isd.controller;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import org.isd.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class VideoController {

    @Resource
    private VideoService videoService;

    //首页展示
    @RequestMapping(value = "/getFirstPage")
    @ResponseBody
    public JSON getFirstPage() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(videoService.getFirstPage());
        return jsonArray;
    }
}
