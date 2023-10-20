package org.sys.rate.service;
import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Repeat;
import org.sys.rate.mapper.LoginMapper;
import org.sys.rate.mapper.RoleMapper;
import org.sys.rate.model.*;
import org.sys.rate.utils.TokenUtils;
import cn.hutool.core.bean.BeanUtil;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class LoginService {
    @Autowired
    private LoginMapper loginMapper;
    @Resource
    private RoleMapper roleMapper;

    public LoginInf login(LoginInf loginInf)
    {
        String role = loginInf.getRole();
        Account account = null;
        if(role.equals("admin")) {
            account = loginMapper.loginAdmin(loginInf);
        }else if(role.equals("student")) {
            account = loginMapper.loginStudent(loginInf);
        }else if(role.equals("teacher")){
            account = loginMapper.loginTeacher(loginInf);
        }else
            return null;

        if (account == null)
            return null; // 用户名不存在

        if(check(loginInf.getPassword(),account.getPassword())){
            BeanUtil.copyProperties(account, loginInf, true);
            String token = TokenUtils.genToken(account.getID().toString(),account.getPassword(), role);
            loginInf.setToken(token);
            loginInf.setID(account.getID());
            loginInf.setInstitutionID(account.getInstitutionID());
            List<String> rs = Arrays.asList(loginInf.getRole().split(";"));
            List<String> roleNameList = roleMapper.selectNameByRoleID(rs);
            String loginInfRoleName = "";
            for(int i = 0;i < roleNameList.size();i ++) {
                loginInfRoleName += roleNameList.get(i) + ";";
            }
            loginInf.setRoleName(loginInfRoleName);
            return loginInf;
        }
        else
            return loginInf; // 如果返回不带token的Inf，表示密码错误
    }

    // 核对密码是否正确
    private boolean check(String targetPassword, String truePassword){
//        return passwordEncoder.matches(targetPassword,truePassword); // 后端不再进行加密
        return targetPassword.equals(truePassword);
    }

    public int updatePassword(Integer ID, String password) {
        return loginMapper.updatePassword(ID,password);
    }
}