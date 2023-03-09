package org.sys.rate.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.sys.rate.model.InfoItem;
import org.sys.rate.model.Infos;
import org.sys.rate.model.Participates;
import org.sys.rate.model.RespBean;
import org.sys.rate.service.admin.InfoItemService;
import org.sys.rate.service.admin.InfosService;

import java.util.List;
import java.util.Map;

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
    @PostMapping("/getPartipicantIDByInfos")
    @ResponseBody
    public RespBean getInfoItemIdByNameAndActivityId(@RequestBody Map<String,List<String>> data){
        List<String> activity =data.get("activityID");
        List<String> infoItem =data.get("infoItemID");
        List<String> infoContent =data.get("infoContent");
        Integer activityID = Integer.parseInt(activity.get(0));
        Integer infoItemID = Integer.parseInt(infoItem.get(0));

        List<Participates> participates = infosService.getPartipicantByActivityId(activityID,infoItemID,infoContent);
        return RespBean.ok("success",participates);
    }

}
