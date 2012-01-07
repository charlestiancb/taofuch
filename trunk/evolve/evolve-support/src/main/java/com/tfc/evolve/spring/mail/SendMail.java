/*
 * Copyright 2010 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.spring.mail;

import java.util.Map;

/**
 * 发送邮件的对外接口
 * 
 * @author taofucheng
 */
public interface SendMail {
    /** 系统配置的默认邮箱用户名 */
    public static final String defaultMailUsername = "_defaultMailUsername";
    /** 系统配置的默认邮箱密码 */
    public static final String defaultMailPassword = "_defaultMailPassword";

    /**
     * 采用模板渲染的发送邮件的对外接口
     * 
     * @author yuanchao
     * @param mailId:模板Id
     * @param from : 发件人
     * @param to : 收件人
     * @param model : 模板参数
     */
    public String sendMailByTemplate(String mailId, Mail mail, Map<String, Object> model);

    /**
     * 人工发送邮件的对外接口
     * 
     * @param mail
     * @return
     * @throws Exception
     */
    public String sendMailByPeople(Mail mail);
}
