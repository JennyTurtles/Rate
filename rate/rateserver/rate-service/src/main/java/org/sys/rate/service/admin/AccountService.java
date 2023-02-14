package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sys.rate.mapper.AdminMapper;
import org.sys.rate.mapper.HrRoleMapper;
import org.sys.rate.model.Account;
import org.sys.rate.model.Admin;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.service.expert.ExpertService;
import org.sys.rate.service.expert.TeacherService;
import org.sys.rate.utils.HrUtils;

import java.util.List;

@Service("AccountService")
public class AccountService implements UserDetailsService {
    @Autowired
    ExpertService expertService;
    @Autowired
    TeachersService teachersService;
    @Autowired
    AdminService adminService;
    @Autowired
    StudentService studentService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        String[] split = username.split(":",2);
        String username1=split[1];
        String role1=split[0];
        if (role1.equals("admin")) {
            return adminService.loadUserByUsername(username1);
        } else if (role1.equals("teacher")) {
            return teachersService.loadUserByUsername(username1);
        }else if (role1.equals("student")) {
            System.out.println("student..");
            return studentService.loadUserByUsername(username1);
        }
        else
            return new Account();
    }
}
