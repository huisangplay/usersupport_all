package com.comac.usersupport_tool.service;

import com.comac.usersupport_tool.constants.HttpConstants;
import com.comac.usersupport_tool.constants.UserConstants;
import com.comac.usersupport_tool.util.Captcha;

import org.apache.hc.client5.http.auth.AuthCache;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.auth.BasicScheme;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URI;

@Service
public class PwdServiceImp implements PwdService {

    Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public String pwdResetFromIDEAL(Captcha captcha){

        final BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope("localhost", 8088)
                , new UsernamePasswordCredentials("user", "password".toCharArray()));
        try (final CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build()) {

            URI uri = new URIBuilder("http://localhost:8088/pwdtest")
                    .addParameter("enumber", captcha.getNumber())
                    .addParameter("code", captcha.getCode())
                    .addParameter("createTime", String.valueOf(captcha.getCreateTime()))
                    .addParameter("digest", captcha.getDigest())
                    .build();

            HttpGet httpGet = new HttpGet(uri);

            logger.info("Executing request url: " + httpGet.getUri());

            try (final CloseableHttpResponse response = httpclient.execute(httpGet)) {

                logger.info(response.getCode() + " " + response.getReasonPhrase());
                logger.info(EntityUtils.toString(response.getEntity()));

                if(response.getCode()== HttpConstants.SUCCESS){
                    return UserConstants.PWDRESETSUCCESS;
                }
            }
        }
        catch (Exception e){
            logger.info(UserConstants.PWDRESETFAILED+"\n"+e.getMessage());
        }
        return UserConstants.PWDRESETFAILED;
    }
}
