package com.tfc.evolve.spring.mail;

/**
 * 邮件对应的附件信息
 * 
 * @author taofucheng
 */
public class MailAttach {
    /** 内嵌资源或附件 */
    private byte[] attach;
    /** 附件的类型，如：application/vnd.ms-excel */
    private String attachContentType;
    /** 附件名字或者内嵌资源标记 */
    private String attachName;

    /**
     * @return the attach
     */
    public byte[] getAttach() {
        return attach;
    }

    /**
     * @param attach the attach to set
     */
    public void setAttach(byte[] attach) {
        this.attach = attach;
    }

    /**
     * @return the attachName
     */
    public String getAttachName() {
        return attachName;
    }

    /**
     * @param attachName the attachName to set
     */
    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public void setAttachContentType(String attachContentType) {
        this.attachContentType = attachContentType;
    }

    public String getAttachContentType() {
        return attachContentType;
    }
}
