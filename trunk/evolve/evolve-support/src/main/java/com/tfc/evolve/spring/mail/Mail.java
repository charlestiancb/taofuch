package com.tfc.evolve.spring.mail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 单个邮件信息实体，可以直接用于发送邮件
 * 
 * @author taofucheng
 */
public class Mail {
    private String senderAddress;
    private String senderDispaly;
    private List<String> revicer = new ArrayList<String>();
    /** 邮件普通附件 */
    private List<MailAttach> attachs = new ArrayList<MailAttach>();
    /** 邮件的内联附件，即邮件内容中可以显示的图片等附件 */
    private List<MailAttach> resources = new ArrayList<MailAttach>();
    private List<String> cc = new ArrayList<String>();
    private List<String> bcc = new ArrayList<String>();
    private String subject;
    private String replyTo;
    private String content;
    /** 邮件内容是否以HTML形式发送，默认为true */
    private boolean isHtml = true;

    /**
     * 是否有附件。
     * 
     * @return true-有内联附件或普通附件；false-没有任何附件
     */
    public boolean hasAttachments() {
        return !(attachs.isEmpty() && resources.isEmpty());
    }

    public void addRevicer(String revicer) {
        if (StringUtils.isNotEmpty(revicer)) {
            this.revicer.add(revicer);
        }
    }

    public void addBcc(String bcc) {
        if (StringUtils.isNotEmpty(bcc)) {
            this.bcc.add(bcc);
        }
    }

    public void setRevicer(List<String> list) {
        this.revicer = list;
    }

    public void setCc(List<String> list) {
        if (list != null && list.size() > 0) {
            this.cc = list;
        }
    }

    public void addAttachs(MailAttach attach) {
        if (attach != null) {
            this.attachs.add(attach);
        }
    }

    public void addCc(String cc) {
        if (StringUtils.isNotEmpty(cc)) {
            this.cc.add(cc);
        }
    }

    /**
     * @return the senderAddress
     */
    public String getSenderAddress() {
        return senderAddress;
    }

    /**
     * @param senderAddress the senderAddress to set
     */
    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    /**
     * @return the senderDispaly
     */
    public String getSenderDispaly() {
        return senderDispaly;
    }

    /**
     * @param senderDispaly the senderDispaly to set
     */
    public void setSenderDispaly(String senderDispaly) {
        this.senderDispaly = senderDispaly;
    }

    /**
     * @return the cc
     */
    public List<String> getCc() {
        return cc;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the revicer
     */
    public List<String> getRevicer() {
        return revicer;
    }

    public void setResources(List<MailAttach> resources) {
        this.resources = resources;
    }

    public void addResource(MailAttach resource) {
        this.resources.add(resource);
    }

    public List<MailAttach> getResources() {
        return resources;
    }

    public List<MailAttach> getAttachs() {
        return attachs;
    }

    public void setAttachs(List<MailAttach> attachs) {
        this.attachs = attachs;
    }

    @Override
    public String toString() {
        StringBuilder sbd = new StringBuilder();
        sbd.append("{");
        sbd.append("发件人:");
        sbd.append(this.senderAddress);
        sbd.append("; ");
        sbd.append("收件人:");
        for (String recv : this.revicer) {
            sbd.append(recv);
            sbd.append(", ");
        }
        sbd.append("; ");
        if (this.cc.size() > 0) {
            sbd.append("抄送:");
            for (String c : this.cc) {
                sbd.append(c);
                sbd.append(", ");
            }
            sbd.append("; ");
        }
        sbd.append("主题:");
        sbd.append(this.subject);
        sbd.append("; ");
        if (this.attachs.size() > 0) {
            sbd.append("附件：");
            for (MailAttach attach : this.attachs) {
                sbd.append(attach.getAttachName());
                sbd.append(", ");
            }
            sbd.append("; ");
        }
        sbd.append("}");
        return sbd.toString();
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean isHtml) {
        this.isHtml = isHtml;
    }
}
