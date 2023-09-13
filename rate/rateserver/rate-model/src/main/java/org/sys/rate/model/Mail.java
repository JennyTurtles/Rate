package org.sys.rate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mail {
    private String emailAddress;
    private String IMAPVerifyCode;
    private String IMAPHost;
    private String SMTPHost;
    private int id;

}
