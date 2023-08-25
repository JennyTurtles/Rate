package org.sys.rate;

//import org.junit.Test;
//import org.junit.runner.RunWith;

import lombok.var;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.sys.rate.controller.admin.ActivitiesBasicController;
import org.sys.rate.model.Activities;
import org.sys.rate.model.RespBean;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
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

    public static <T extends Serializable> T deepCopy(T object) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(object);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (T) ois.readObject();
    }
    
    @Test
    public void testDeepCopy() throws IOException, ClassNotFoundException {
        Address address = new Address("New York");
        Person person1 = new Person("Alice", address);

        // 使用序列化和反序列化实现深拷贝
        Person person2 = deepCopy(person1);

        // 修改 person2 的地址
        person2.address.city = "San Francisco";

        // person1 和 person2 拥有各自独立的 address 对象引用
        System.out.println(person1.address.city);  // 输出 "New York"
        System.out.println(person2.address.city);  // 输出 "San Francisco"
    }


}

class Address implements Serializable {
    String city;

    public Address(String city) {
        this.city = city;
    }
}

class Person implements Serializable {
    String name;
    Address address;

    public Person(String name, Address address) {
        this.name = name;
        this.address = address;
    }
}
