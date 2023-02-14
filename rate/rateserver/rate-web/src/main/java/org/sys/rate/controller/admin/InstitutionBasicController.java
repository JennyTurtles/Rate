package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.model.Institution;
import org.sys.rate.model.RespBean;
import org.sys.rate.model.RespPageBean;
import org.sys.rate.service.admin.InstitutionService;


@RestController
@RequestMapping("/institution/basic")
public class InstitutionBasicController {
    @Autowired
    InstitutionService institutionService;


    @GetMapping("/")
    public RespPageBean getInstitutionByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Institution employee) {
        return institutionService.getInstitutionPage(page, size, employee);
    }

    @PostMapping("/insert")
    public RespBean addInstitution(@RequestBody Institution employee) {
        if (institutionService.addInstitution(employee) == 1) {
            return RespBean.ok("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @PostMapping("/delete")
    public RespBean deleteInstitution(@RequestBody Institution company) {
        if (institutionService.deleteInstitution(company) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @PostMapping("/update")
    public RespBean updateInstitution(@RequestBody Institution company) {
        if (institutionService.updateInstitution(company) == 1) {
            return RespBean.ok("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @GetMapping("/search")
    public RespPageBean searchInstitution(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam String company) {
        return institutionService.searchInstitution(page, size, company);
    }

}
