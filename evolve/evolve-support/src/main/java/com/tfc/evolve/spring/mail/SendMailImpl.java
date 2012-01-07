/*
 * Copyright 2010 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.spring.mail;

import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.Assert;

import com.tfc.evolve.common.utils.StringUtils;
import com.tfc.evolve.spring.mail.utils.VelocityEnginePropertyFactory;

/**
 * @author taofucheng
 */
public class SendMailImpl implements SendMail, InitializingBean {

    private VelocityEngine velocityEngine;
    private JavaMailSender mailSender;
    /** 扩展的账号信息，可以为空，但如果有值，请填写成json格式，如“{"mail":"password"}” */
    private String extendAccounts;

    /**
     * 实现对外的邮件发送接口,采用模板渲染
     */
    @Override
    public String sendMailByTemplate(String mailId, Mail mail, Map<String, Object> model) {
        VelocityEnginePropertyFactory.pushPropertiesToMap(model);
        SendMailThread s = new SendMailThread(mail, model, velocityEngine, mailSender);
        Thread thread = new Thread(s);
        thread.setName("sendMailThread");
        thread.start();
        return "success";
    }

    /**
     * 人工发送邮件的实现方法
     * 
     * @param Mail
     */
    @Override
    public String sendMailByPeople(Mail mail) {
        SendMailByPerThread s = new SendMailByPerThread(mail, mailSender);
        Thread thread = new Thread(s, "sendMailByPeop");
        thread.start();
        return "success";
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setExtendAccounts(String extendAccounts) {
        this.extendAccounts = StringUtils.trim(extendAccounts);
    }

    public String getExtendAccounts() {
        return extendAccounts;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(mailSender);
    }
}
