package com.comac.usersupport_tool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class IndexController {
    @GetMapping("/")
    public String homePage() {
        return "layout";
    }
}
