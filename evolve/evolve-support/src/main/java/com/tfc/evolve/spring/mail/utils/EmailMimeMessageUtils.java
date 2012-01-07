/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.spring.mail.utils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.tfc.evolve.spring.mail.Mail;
import com.tfc.evolve.spring.mail.MailAttach;

/**
 * EmailMimeMessageUtils.java
 * 
 * @author taofucheng
 */
public class EmailMimeMessageUtils {
    private static Log log = LogFactory.getLog(EmailMimeMessageUtils.class);

    public static MimeMessageHelper createMimeMessageHelper(final JavaMailSender mailSender) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        return mimeMessageHelper;
    }

    /*
     * (non-Javadoc)
     * @see com.focustech.core.service.MailService#createMimeMessageHelperMutipart()
     */
    public static MimeMessageHelper createMimeMessageHelperMutipart(final JavaMailSender mailSender) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(message, true);
        }
        catch (MessagingException e) {
            log.error("mail:", e);
        }
        return mimeMessageHelper;
    }

    /**
     * 邮件处理方法，从指定模板中读取邮件内容，并将邮件发送出去
     * 
     * @param hasLinkCatalog
     * @return
     */
    public static void sendMail(JavaMailSender mailSender, Mail mail) {
        try {
            MimeMessageHelper helper = createMimeMessageHelper(mailSender);
            if (mail.hasAttachments()) {
                System.setProperty("mail.mime.setdefaulttextcharset", "false");// 将文本文件自动设置编码改为禁用！
                // 有附件，应该创建发送附件的邮件方式
                helper = createMimeMessageHelperMutipart(mailSender);
            }
            helper.setTo(mail.getRevicer().toArray(new String[]{}));
            if (StringUtils.isNotEmpty(mail.getSubject())) {
                helper.setSubject(mail.getSubject());
            }
            if (mail.getBcc() != null && mail.getBcc().size() > 0) {
                helper.setBcc(mail.getBcc().toArray(new String[]{}));
            }
            if (mail.getCc() != null && mail.getCc().size() > 0) {
                helper.setCc(mail.getCc().toArray(new String[]{}));
            }
            if (StringUtils.isNotEmpty(mail.getContent())) {
                helper.setText(mail.getContent(), mail.isHtml());
            }
            if (StringUtils.isNotEmpty(mail.getReplyTo())) {
                helper.setReplyTo(mail.getReplyTo());
            }
            if (StringUtils.isNotEmpty(mail.getSenderAddress())) {
                helper.setFrom(mail.getSenderAddress(), mail.getSenderDispaly());
            }

            if (mail.hasAttachments()) {
                // 普通附件
                if (mail.getAttachs() != null && mail.getAttachs().size() > 0) {
                    for (MailAttach ma : mail.getAttachs()) {
                        helper.addAttachment(MimeUtility.encodeWord(ma.getAttachName(), helper.getEncoding(), null),
                                new ByteArrayResource(ma.getAttach()), ma.getAttachContentType());
                    }
                }
                // 内联附件
                if (mail.getResources() != null && mail.getResources().size() > 0) {
                    for (MailAttach ma : mail.getAttachs()) {
                        helper.addInline(MimeUtility.encodeWord(ma.getAttachName(), helper.getEncoding(), null),
                                new ByteArrayDataSource(ma.getAttach(), ma.getAttachContentType()));
                    }
                }
            }
            mailSender.send(helper.getMimeMessage());
            log.debug("Mail send succ! email={" + mail + "}");
        }
        catch (Exception e) {
            log.error("Mail send failed! email={" + mail + "}", e);
            throw new MailSendException("Mail send failed! email={" + mail + "}", e);
        }
    }
}
