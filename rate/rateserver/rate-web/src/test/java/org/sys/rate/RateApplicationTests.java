package org.sys.rate;

//import org.junit.Test;
//import org.junit.runner.RunWith;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.sys.rate.controller.admin.ActivitiesBasicController;
import org.sys.rate.model.Activities;
import org.sys.rate.model.RespBean;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class RateApplicationTests {

    @Resource
    ActivitiesBasicController activitiesBasicController;
    @Test
    public void contextLoads() {
        String input = "scoreitem.127+1*displayitem.33+1*displayitem.34";
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?\\*)?(\\w+)\\.(\\d+)");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            double coefficient = matcher.group(1) != null ? Double.parseDouble(matcher.group(1).replace("*", "")) : 1.0;
            String type = matcher.group(3);
            int id = Integer.parseInt(matcher.group(4));
            System.out.println(coefficient + " " + type + " " + id);
        }
    }

    @Test
    public void testCloneActivity(){
        Activities activities = new Activities();
        activities.setId(40);
        activities.setAdminID(3);
        activities.setComment("test");
        RespBean aLl = activitiesBasicController.cloneActivity(activities);
        return;
    }

}
