package org.sys.rate.service.admin;
import org.sys.rate.mapper.AdminMapper;
import org.sys.rate.mapper.HrRoleMapper;
import org.sys.rate.model.Account;
import org.sys.rate.model.Admin;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.model.Teacher;
import org.sys.rate.utils.HrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("adminService")
public class AdminService implements UserDetailsService {
    @Autowired
    HrRoleMapper hrRoleMapper;
    @Autowired
    AdminMapper adminMapper;

    public Admin getById(Integer ID){
        Admin adm = adminMapper.getById(ID);
        if(adm != null){
            return adm;
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //accountservice loadUserByUsername
        Admin admin = adminMapper.loadUserByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
        admin.setRoles(adminMapper.getAdminRolesById(admin.getId()));
        return admin;
    }

    public RespPageBean getAllHrs(String keywords, Integer page, Integer size) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Admin> data = adminMapper.getAllHrs(HrUtils.getCurrentHr().getId(),keywords,page, size);
        Long total = adminMapper.getTotal(keywords);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public RespPageBean getAllAdsByAdvancedMethod(String keywords,String keywords_name,Integer page, Integer size) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Admin> data = adminMapper.getAllAdsByAdvancedMethod(keywords,keywords_name,page, size);
        Long total = adminMapper.getTotalByAdvancedMethod(keywords,keywords_name);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public Integer updateAdmin(Admin admin) {
        int result = adminMapper.update(admin);
        return result;
    }

    public Integer deleteAdmin(Admin id) {
        int result = adminMapper.delete(id);
        return result;
    }

    public Integer updateAdminByPrimaryKey(Admin hr) {
        return adminMapper.updateByPrimaryKeySelective(hr);
    }

    public boolean updateHrRole(Integer hrid, Integer[] rids) {
        hrRoleMapper.deleteByHrid(hrid);
        return hrRoleMapper.addRole(hrid, rids) == rids.length;
    }



    public boolean updateAdminPasswd(String oldpass, String pass, Integer adminid) {
        Admin admin = adminMapper.selectByPrimaryKey(adminid);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldpass, admin.getPassword())) {
            String encodePass = encoder.encode(pass);
            Integer result = adminMapper.updatePasswd(adminid, encodePass);
            if (result == 1) {
                return true;
            }
        }
        return false;
    }



    public Integer addNew(Admin hr) {
        hr.setEnabled(true);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePass = encoder.encode(hr.getPassword());
        hr.setPassword(encodePass);
        return adminMapper.insert(hr);
    }
}
