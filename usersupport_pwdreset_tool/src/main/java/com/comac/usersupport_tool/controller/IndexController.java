package com.comac.usersupport_tool.controller;

import com.comac.usersupport_tool.constants.UserConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class IndexController {
    @GetMapping("/")
    public String homePage(HttpServletRequest request) {
        HttpSession session=request.getSession();
        session.setAttribute(UserConstants.IDENTIFIES,"500958");
        return "layout";
    }


}
