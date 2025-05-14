package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.DictService;

@RestController
@RequestMapping("/dict/basic")
public class DictController {
    @Autowired
    private DictService dictService;

    @RequestMapping("/getDictValue")
    public RespBean getDictValue(@RequestParam("item") String item) {
        return RespBean.ok("操作成功",dictService.getDictValue(item));
    }
}
