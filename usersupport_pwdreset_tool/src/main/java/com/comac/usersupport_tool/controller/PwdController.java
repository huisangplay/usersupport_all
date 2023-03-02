package com.comac.usersupport_tool.controller;

import com.comac.usersupport_tool.constants.UserConstants;
import com.comac.usersupport_tool.service.PwdService;
import com.comac.usersupport_tool.util.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/pwd")
public class PwdController {

    @Autowired
    PwdService pwdService;

    @PostMapping("/resetpwd")
    public String resetpwd(HttpServletRequest request, Model model){
        HttpSession session=request.getSession();
        String enumber=(String)session.getAttribute(UserConstants.IDENTIFIES);
        model.addAttribute("enumber",enumber);
        return "account/resetpwd";
    }

    @RequestMapping("/resetpwdsubmit")
    public String resetpwdsubmit(HttpServletRequest request,  Model model){
        String vercode=request.getParameter("vercode");
        System.out.println(vercode);
        HttpSession session=request.getSession();
        String enumber=(String)session.getAttribute(UserConstants.IDENTIFIES);

        Captcha captcha=new Captcha(enumber,vercode);

        String message;

        try {
            message=pwdService.pwdResetFromIDEAL(captcha);
        } catch (Exception e) {
            message=UserConstants.PWDRESETFAILED;
        }

        model.addAttribute("info",message);

        return "home";
    }
}
