/*
 * Copyright 2010 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.spring.mail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;

import com.tfc.evolve.spring.mail.utils.EmailMimeMessageUtils;
import com.tfc.evolve.spring.mail.utils.VelocityEngineUtils;

/**
 * 异步发送邮件
 * 
 * @author taofucheng
 */
public class SendMailThread implements Runnable {
    private static final Log log = LogFactory.getLog(SendMailThread.class);
    private Mail mail;
    private Map<String, Object> model;
    private VelocityEngine velocityEngine;
    private JavaMailSender mailSender;

    public SendMailThread(Mail mail, Map<String, Object> model, VelocityEngine velocityEngine, JavaMailSender mailSender) {
        this.mail = mail;
        this.model = model;
        this.velocityEngine = velocityEngine;
        this.mailSender = mailSender;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {

        // 解析邮件模板配置文件，跟据mailId取得邮件模板
        Map<String, Object> map = new HashMap<String, Object>();
        // 取得模板路径
        String templatePath = StringUtils.trimToEmpty((String) map.get("location"));
        // 取得邮件默认主题
        String subject = StringUtils.trimToEmpty((String) map.get("subject"));
        // 取得邮件的发送地址
        String senderAddress = StringUtils.trimToEmpty((String) map.get("senderAddress"));
        // 取得回复地址
        String replyTo = StringUtils.trimToEmpty((String) map.get("replyTo"));
        // 取得邮件的发送人的名字
        String senderDispaly = StringUtils.trimToEmpty((String) map.get("senderDispaly"));
        mail.setSubject(StringUtils.isEmpty(mail.getSubject()) ? subject : mail.getSubject());
        mail.setSenderAddress(StringUtils.isEmpty(mail.getSenderAddress()) ? senderAddress : mail.getSenderAddress());
        mail.setSenderDispaly(StringUtils.isEmpty(mail.getSenderDispaly()) ? senderDispaly : mail.getSenderDispaly());
        mail.setReplyTo(StringUtils.isEmpty(mail.getReplyTo()) ? replyTo : mail.getReplyTo());

        // 取得内嵌资源
        Map<String, String> mapInlineResource = new HashMap<String, String>();
        // 封装内嵌资源
        if (mapInlineResource != null && !mapInlineResource.isEmpty()) {
            for (Iterator<String> it = mapInlineResource.keySet().iterator(); it.hasNext();) {
                // 得到内嵌资源的key
                String key = it.next();
                // 得到key对应的内嵌资源的路径
                String keyValue = mapInlineResource.get(key);
                mail.addAttachs(getMailAttachByFilePath(key, keyValue));
            }
        }
        // 取得附件资源
        Map<String, String> mapAttach = (Map<String, String>) map.get("attach");
        // 封装附件资源
        if (mapAttach != null && !mapAttach.isEmpty()) {
            for (Iterator<String> it = mapAttach.keySet().iterator(); it.hasNext();) {
                // 得到内嵌资源的key
                String fileName = it.next();
                // 得到key对应的内嵌资源的路径
                String filePath = mapAttach.get(fileName);
                mail.addResource(getMailAttachByFilePath(fileName, filePath));
            }
        }
        String mailContent = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templatePath, "UTF-8", model);
        mail.setContent(mailContent);
        // 调用邮件处理接口
        EmailMimeMessageUtils.sendMail(mailSender, mail);
    }

    /**
     * 根据名称路径及名称构建对应的附件信息
     * 
     * @param nameOrId 附件的名称 或 inline附件的id。
     * @param filePath 文件路径及名称
     */
    private MailAttach getMailAttachByFilePath(String nameOrId, String filePath) {
        try {
            // 封装成DataSource
            DataSource dataSource = new FileDataSource(filePath);
            MailAttach ma = new MailAttach();
            ma.setAttachContentType(dataSource.getContentType());
            ma.setAttachName(nameOrId);
            byte[] bts = new byte[dataSource.getInputStream().available()];
            dataSource.getInputStream().read(bts);
            ma.setAttach(bts);
            return ma;
        }
        catch (Exception e) {
            log.error("", e);
        }
        return null;
    }
}
