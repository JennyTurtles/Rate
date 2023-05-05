package org.sys.rate.model;


public class Mail {
    private String emailAddress;
    private String IMAPVerifyCode;
    private String IMAPHost;
    private String SMTPHost;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getIMAPVerifyCode() {
        return IMAPVerifyCode;
    }

    public void setIMAPVerifyCode(String IMAPVerifyCode) {
        this.IMAPVerifyCode = IMAPVerifyCode;
    }

    public String getIMAPHost() {
        return IMAPHost;
    }

    public void setIMAPHost(String IMAPHost) {
        this.IMAPHost = IMAPHost;
    }

    public String getSMTPHost() {
        return SMTPHost;
    }

    public void setSMTPHost(String SMTPHost) {
        this.SMTPHost = SMTPHost;
    }

    public Mail(){

    }

}
