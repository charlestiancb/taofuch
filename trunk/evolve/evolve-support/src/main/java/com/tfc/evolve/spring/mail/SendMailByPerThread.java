/*
 * Copyright 2010 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.spring.mail;

import org.springframework.mail.javamail.JavaMailSender;

import com.tfc.evolve.spring.mail.utils.EmailMimeMessageUtils;

public class SendMailByPerThread implements Runnable {

    private Mail mail;
    private JavaMailSender mailSender;

    public SendMailByPerThread(Mail mail, JavaMailSender mailSender) {
        this.mail = mail;
        this.mailSender = mailSender;
    }

    @Override
    public void run() {
        EmailMimeMessageUtils.sendMail(mailSender, mail);
    }

}
