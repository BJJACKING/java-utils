package com.znb.java.utils;


import com.znb.java.model.Mail;
import com.znb.java.model.MailAttach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author zhangnaibin
 * @Date 2015-12-18 上午11:41
 *
   <dependency>
    <groupId>javax.mail</groupId>
    <artifactId>mail</artifactId>
    <version>1.4.5</version>
   </dependency>
 *
 */
public class MailUtil {
    protected static final Logger LOG = LoggerFactory.getLogger(MailUtil.class);

    public static final String HOSTNAME_163 = "smtp.163.com";

    /**
     * 通过163邮箱发送邮件
     *
     * @param subject    邮件主题
     * @param content    邮件正文内容
     * @param receivers  收件人数组
     * @param retryTimes 重试次数
     * @param user       发件人地址
     * @param password   发件人邮箱密码
     * @throws Exception
     */
    public static void sendMailBy163(String subject, String content, String[] receivers, int retryTimes, String user, String password) throws Exception {
        Properties properties = new Properties();
        // 发送邮件协议
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties);
        session.setDebug(true);

        // 邮件信息
        Message messgae = new MimeMessage(session);
        // 设置发送人
        messgae.setFrom(new InternetAddress(user));
        // 设置邮件内容
        messgae.setText(content);
        // 设置邮件主题
        messgae.setSubject(subject);

        while (retryTimes-- != 0) {
            try {
                // 发送邮件
                Transport tran = session.getTransport();
                // 连接到新浪邮箱服务器
                tran.connect(HOSTNAME_163, 25, user, password);

                Address[] receiveAddrs = new Address[receivers.length];
                for (int i = 0; i < receivers.length; i++) {
                    receiveAddrs[i] = new InternetAddress(receivers[i]);
                }
                // 设置邮件接收人
                tran.sendMessage(messgae, receiveAddrs);
                tran.close();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 更高级的邮件发送,支持抄送,附件等功能.需要将发送的信息设置为Mail对象
     * 注意:邮件正文内容换行使用<br>,貌似是设置问题
     *
     * @param mail
     * @param hostName
     * @param mailName
     * @param mailPassword
     * @throws MessagingException
     * @throws IOException
     */
    public static void send(final Mail mail, String hostName, String mailName, String mailPassword) throws MessagingException, IOException {
        Session session = createSession(hostName, mailName, mailPassword);
        final MimeMessage msg = new MimeMessage(session);// 创建邮件对象
        msg.setFrom(new InternetAddress(mailName));// 设置发件人
        msg.addRecipients(RecipientType.TO, mail.getToAddress());// 设置收件人
        // 设置抄送
        String cc = mail.getCcAddress();
        if (!cc.isEmpty()) {
            System.out.println(cc);
            msg.addRecipients(RecipientType.CC, cc);
        }
        // 设置暗送
        String bcc = mail.getBccAddress();
        if (!bcc.isEmpty()) {
            msg.addRecipients(RecipientType.BCC, bcc);
        }
        // 设置主题
        msg.setSubject(mail.getSubject());
        // 设置邮件发送时间
        msg.setSentDate(new Date());
        // 创建部件集对象
        MimeMultipart parts = new MimeMultipart();
        // 创建一个部件
        MimeBodyPart part = new MimeBodyPart();
        // 设置邮件文本内容
        part.setContent(mail.getContent(), "text/html;charset=utf-8");
        // 把部件添加到部件集中
        parts.addBodyPart(part);
        // 添加附件
        List<MailAttach> attachBeanList = mail.getAttachs();// 获取所有附件
        if (attachBeanList != null) {
            for (MailAttach attach : attachBeanList) {
                // 创建一个部件
                MimeBodyPart attachPart = new MimeBodyPart();
                // 设置附件文件
                attachPart.attachFile(attach.getFile());
                // 设置附件文件名
                attachPart.setFileName(MimeUtility.encodeText(attach.getFileName()));
                parts.addBodyPart(attachPart);
            }
        }
        // 给邮件设置内容
        msg.setContent(parts);
        try {
            // 发邮件
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static Session createSession(String host, final String username, final String password) {
        Properties prop = new Properties();
        // 指定主机
        prop.setProperty("mail.host", host);
        // 进行用户名和密码验证的设置
        prop.setProperty("mail.smtp.auth", "true");
        // 创建验证器
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
        // 获取session实例
        Session mailSession = Session.getInstance(prop, auth);
        // 发送邮件的过程输出在控制台上（Console）
        mailSession.setDebug(true);
        return mailSession;
    }

    public static void main(String[] args) throws Exception {
        Mail mail = new Mail("", "获取订单文件信息", "");
        MailUtil.send(mail, MailUtil.HOSTNAME_163, "", "");
    }
}
