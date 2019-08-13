package org.isd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 *
 * @创建人 Alitarong
 * @创建时间 2019/8/13
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {

    /**
     *测试请求
     *
     *@参数 []
     *@返回值 java.lang.String
     */
    @GetMapping(value = "/test.do")
    public String getTest(){
        return "asdf";
    }
}
