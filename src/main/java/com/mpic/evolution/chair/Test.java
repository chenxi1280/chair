package com.mpic.evolution.chair;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class Test {

    @ResponseBody
    @RequestMapping("/test")
    String test(){

        return "test";
    }



}
