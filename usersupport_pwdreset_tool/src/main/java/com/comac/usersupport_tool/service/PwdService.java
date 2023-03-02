package com.comac.usersupport_tool.service;

import com.comac.usersupport_tool.util.Captcha;

import java.io.IOException;
import java.net.URISyntaxException;

public interface PwdService {
    public String pwdResetFromIDEAL(Captcha captcha);
}
