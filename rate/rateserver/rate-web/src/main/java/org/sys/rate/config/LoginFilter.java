package org.sys.rate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sys.rate.model.Account;
import org.sys.rate.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    SessionRegistry sessionRegistry;
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        /*String verify_code = (String) request.getSession().getAttribute("verify_code");*/
        if (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE) || request.getContentType().contains(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
            Map<String, String> loginData = new HashMap<>();
            try {
                loginData = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            } catch (IOException e) {
            }finally {
                /*String code = loginData.get("code");
                checkCode(response, code, verify_code);*/
            }
            String username = loginData.get(getUsernameParameter());
            String password = loginData.get(getPasswordParameter());
            String login_role = loginData.get("role");
            StringBuilder sb = new StringBuilder();
            sb.append(login_role).append(":").append(username);
            username = sb.toString();
            //System.out.println(loginData.get("role"));
            if (username == null) {
                username = "";
            }
            if (password == null) {
                password = "";
            }
            username = username.trim();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    username, password);
            setDetails(request, authRequest);
            Account principal = new Account();
            principal.setUsername(username);
            //principal.setRole(Account.login_role);
//            System.out.println("principal.getUsername()"+principal.getUsername());
            //System.out.println("test用户名密码验证通过后,注册session");
            //用户名密码验证通过后,注册session
            sessionRegistry.registerNewSession(request.getSession(true).getId(), principal);
            //System.out.println("test用户名密码session");
            return this.getAuthenticationManager().authenticate(authRequest);//attemptAuthentication () 方法将 request 中的 username 和 password 生成 UsernamePasswordAuthenticationToken 对象，用于 AuthenticationManager 的验证（即 this.getAuthenticationManager().authenticate(authRequest) ）。
        } else {
            /*checkCode(response, request.getParameter("code"), verify_code);*/
            return super.attemptAuthentication(request, response);
        }
    }

    public void checkCode(HttpServletResponse resp, String code, String verify_code) {
        if (code == null || verify_code == null || "".equals(code) || !verify_code.toLowerCase().equals(code.toLowerCase())) {
            //验证码不正确str.equalsIgnoreCase(personStr)
            throw new AuthenticationServiceException("验证码不正确");
        }
    }
}
