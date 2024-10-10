package org.sys.rate.service;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sys.rate.mapper.LoginMapper;
import org.sys.rate.mapper.RoleMapper;
import org.sys.rate.model.*;
import org.sys.rate.utils.PasswordUtils;
import org.sys.rate.utils.TokenUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class LoginService {
    @Autowired
    private LoginMapper loginMapper;
    @Resource
    private RoleMapper roleMapper;

    public LoginInf login(LoginInf loginInf) {
        String role = loginInf.getRole();
        Account account = null;
        if (role.equals("admin")) {
            account = loginMapper.loginAdmin(loginInf);
        } else if (role.equals("student")) {
            account = loginMapper.loginStudent(loginInf);
        } else if (role.equals("teacher")) {
            account = loginMapper.loginTeacher(loginInf);
        } else
            return null;

        if (account == null)
            return null; // 用户名不存在

        if (check(loginInf.getPassword(), account.getPassword())) {
            BeanUtil.copyProperties(account, loginInf, true);
            String token = TokenUtils.genToken(account.getID().toString(), account.getPassword(), role);
            loginInf.setToken(token);
            loginInf.setID(account.getID());
            loginInf.setInstitutionID(account.getInstitutionID());
            List<String> rs = Arrays.asList(loginInf.getRole().split(";"));
            List<String> roleNameList = roleMapper.selectNameByRoleID(rs);
            String loginInfRoleName = "";
            for (int i = 0; i < roleNameList.size(); i++) {
                loginInfRoleName += roleNameList.get(i) + ";";
            }
            loginInf.setRoleName(loginInfRoleName);
            return loginInf;
        } else
            return loginInf; // 如果返回不带token的Inf，表示密码错误
    }

    public LoginInf casLogin(String number) {
        LoginInf loginInf = new LoginInf();
        Account account = null;
        String role = "";
        if (number.length() == 7){  //研究生
            account = loginMapper.loginGraduateByCas(number);
            role = "student";
        } else if (number.length() == 9){ //本科生
            account = loginMapper.loginUnderGraduateByCas(number);
            role = "student";
        } else if (number.length() == 8) { //教师
            account = loginMapper.loginTeacherByCas(number);
            role = "teacher";
        } else
            return null;

        System.out.print("account:");
        System.out.println(account);
        if (account == null)
            return null; // 用户不存在

        if (account.getUsername() == null) {
            account.setUsername("defaultName");
        }
        if (account.getPassword() == null || account.getPassword().isEmpty())
            account.setPassword("defaultKey");
        loginInf.setUsername(account.getUsername());
        loginInf.setPassword(account.getPassword());
        loginInf.setRole(role);

        BeanUtil.copyProperties(account, loginInf, true);

        System.out.println("loginInf:");
        System.out.println(loginInf);

//        String sign = account.getPassword() == null || Objects.equals(account.getPassword(), "") ? number : account.getPassword();
//        String token = TokenUtils.genToken(account.getID().toString(), sign, role);

        String token = TokenUtils.genToken(account.getID().toString(), account.getPassword(), role);

        System.out.println("token:");
        System.out.println(token);

        loginInf.setToken(token);
        loginInf.setID(account.getID());
        loginInf.setInstitutionID(account.getInstitutionID());
        loginInf.setIsCasLogin(true);
        List<String> rs = Arrays.asList(loginInf.getRole().split(";"));
        List<String> roleNameList = roleMapper.selectNameByRoleID(rs);
        String loginInfRoleName = "";
        for (int i = 0; i < roleNameList.size(); i++) {
            loginInfRoleName += roleNameList.get(i) + ";";
        }
        loginInf.setRoleName(loginInfRoleName);
        return loginInf;
    }

    // 核对密码是否正确
    private boolean check(String targetPassword, String truePassword) {
//        return passwordEncoder.matches(targetPassword,truePassword); // 后端不再进行加密
        return targetPassword.equals(truePassword);
    }

    public int updatePassword(String userRole, Integer ID, String password) {
        String table = "";
        if (userRole.indexOf("admin") >= 0) {
            table = "admin";
        } else if (userRole.indexOf("teacher") >= 0) {
            table = "teacher";
        } else if (userRole.indexOf("student") >= 0) {
            table = "student";
        }
        return loginMapper.updatePassword(table, ID, password);
    }

    public boolean checkPassword(String userRole, Integer id, String password) {
        String table = "";
        if (userRole.indexOf("admin") >= 0) {
            table = "admin";
        } else if (userRole.indexOf("teacher") >= 0) {
            table = "teacher";
        } else if (userRole.indexOf("student") >= 0) {
            table = "student";
        }
        return loginMapper.checkPassword(table, id, password) != 0;
    }
}
