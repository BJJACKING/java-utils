package com.znb.java.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangnaibin
 * @Date 2015-12-18 下午2:31
 */
public class Mail {
    private String from;//发件人
    private StringBuilder toAddress = new StringBuilder();//收件人
    private StringBuilder ccAddress = new StringBuilder();//抄送
    private StringBuilder bccAddress = new StringBuilder();//暗送
    private String subject;//主题
    private String content;//正文
    private List<MailAttach> attachList = new ArrayList<MailAttach>();// 附件列表
    public Mail() {}
    public Mail(String from, String to) {
        this(from, to, null, null);
    }
    public Mail(String from, String to, String subject, String content) {
        this.from = from;
        this.toAddress.append(to);
        this.subject = subject;
        this.content = content;
    }
    public Mail(String to, String subject, String content) {
        this.toAddress.append(to);
        this.subject = subject;
        this.content = content;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getFrom() {
        return from;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getToAddress() {
        return toAddress.toString();
    }
    public String getCcAddress() {
        return ccAddress.toString();
    }
    public String getBccAddress() {
        return bccAddress.toString();
    }
    /**
     * 添加收件人,可以是多个收件人
     * @param to
     */
    public void addToAddress(String to) {
        if(this.toAddress.length() > 0) {
            this.toAddress.append(",");
        }
        this.toAddress.append(to);
    }
    /**
     * 添加抄送人，可以是多个抄送人
     * @param cc
     */
    public void addCcAddress(String cc) {
        if(this.ccAddress.length() > 0) {
            this.ccAddress.append(",");
        }
        this.ccAddress.append(cc);
    }
    /**
     * 添加暗送人，可以是多个暗送人
     * @param bcc
     */
    public void addBccAddress(String bcc) {
        if(this.bccAddress.length() > 0) {
            this.bccAddress.append(",");
        }
        this.bccAddress.append(bcc);
    }
    /**
     * 添加附件，可以添加多个附件
     * @param attachBean
     */
    public void addAttach(MailAttach attachBean) {
        this.attachList.add(attachBean);
    }
    /**
     * 获取所有附件
     * @return
     */
    public List<MailAttach> getAttachs() {
        return this.attachList;
    }
}
