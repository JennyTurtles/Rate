package org.sys.rate.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.sys.rate.model.Account;
import org.sys.rate.service.expert.ExpertService;

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
        String[] split = username.split(":",2);
        String username1=split[1];
        String role1=split[0];
        if (role1.equals("admin")) {
            return adminService.loadUserByUsername(username1);
        } else if (role1.equals("teacher")) {
            return teachersService.loadUserByUsername(username1);
        }else if (role1.equals("student")) {
            return studentService.loadUserByUsername(username1);
        }
        else
            return new Account();
    }
}
