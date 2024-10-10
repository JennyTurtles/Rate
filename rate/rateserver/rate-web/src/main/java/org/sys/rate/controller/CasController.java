package org.sys.rate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.model.LoginInf;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/cas")
public class CasController {
    @Autowired
    private LoginService loginService;

    @GetMapping("/casLogin")
    public void  casLogin(@RequestParam(value = "callback", required = false) String callback, HttpServletRequest request,HttpServletResponse response) throws IOException {

        System.out.println("进入单点登录的方法 in sso method");

        String username = request.getRemoteUser();

        LoginInf res = loginService.casLogin(username);

        ObjectMapper objectMapper = new ObjectMapper();
        String resJson = objectMapper.writeValueAsString(res);

        String jsonResponse = "{\"status\":\"success\",\"username\":\"" + username + "\", \"obj\": " + resJson + "}";

//        // 登录成功后，重定向到网站首页
//        response.sendRedirect("http://up.dhu.edu.cn");
//
//        // 返回 JSONP 响应
//        String jsonpResponse = callback + "(" + jsonResponse + ")";
//        response.setContentType("application/javascript;charset=UTF-8");
//        response.getWriter().write(jsonpResponse);
        String encodedJson = URLEncoder.encode(jsonResponse, "UTF-8");

        String redirectUrl = "https://up.dhu.edu.cn?casResponse=" + encodedJson;
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/casLogout")
    public void casLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        clearCookies(request, response);

//        String casLogoutUrl = "https://cas.dhu.edu.cn/authserver/logout";
//        String redirectUrl = "http://up.dhu.edu.cn";
//        String logoutRedirectUrl = casLogoutUrl + "?service=" + redirectUrl;
//
//        response.sendRedirect(logoutRedirectUrl);
    }

    private void clearCookies(HttpServletRequest request, HttpServletResponse response) {
        javax.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (javax.servlet.http.Cookie cookie : cookies) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

}
