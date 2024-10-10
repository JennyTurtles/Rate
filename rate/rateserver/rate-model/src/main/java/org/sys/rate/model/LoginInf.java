package org.sys.rate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInf {
    private Integer ID;
    private Integer institutionID;
    private String username;
    private String password;
    private String role;
    private String roleName;
    private String token;
    private String name;
    private Boolean isCasLogin;
}
