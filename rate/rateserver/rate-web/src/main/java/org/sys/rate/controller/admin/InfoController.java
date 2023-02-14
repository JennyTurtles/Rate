package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.model.Infos;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.InfoItemService;
import org.sys.rate.service.admin.InfosService;

@RestController
@RequestMapping("/info/basic")
public class InfoController {
    @Autowired
    InfosService infosService;

    @PostMapping("/savetextarea")
    @ResponseBody
    public RespBean edit(Infos infos){
        infosService.savetext(infos);
        return RespBean.ok("成功!");
    }
}
