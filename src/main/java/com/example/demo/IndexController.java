package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController   {

    @RequestMapping(value = "/index")
    public String index(){
        return "index2";
    }

    @ResponseBody
    @RequestMapping("/home")
    public String home() {

        return "home";
    }

}
