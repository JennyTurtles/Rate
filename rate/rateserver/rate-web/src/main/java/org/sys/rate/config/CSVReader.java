package org.sys.rate.config;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.sys.rate.mapper.GroupsMapper;
import org.sys.rate.model.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


public class CSVReader {
    /**
     * 1. 容器：
     *      1.1   固定大小：数组，先确定大小，再以下标存放数据，最后以下标取数据
     *      1.2 不固定大小：ArrayList，先add(数据)往里面添加数据（不能指定位置，因为是边加边扩，只能加到最后一个），get()以下标取数据
     *      1.3 不固定大小，且要按照标签存放，按照标签取数据：HashMap，先以put(“变量名”，数据)存数据，再以get("变量名")取数据
     *
     * 2. 循环
     *      2.1 for循环：for(变量类型 定义一个变量=初始值;最大值;增量)，知道最大循环次数的情况
     *      2.2 while循环：不知道要多少次，只知道一个结束的标识，循环到false为止
     *
     * 3. if(帅吗？){ok}else if(高吗){ok}else if(有钱吗？){ok}else{滚犊子！}
     *
     * 4. try{业务逻辑}catch(Exception e){异常处理逻辑}
     *      4.1 e.printStackTrace()：打印报错日志信息
     *      4.2 错误日志阅读方式：
     *              4.2.1 从上往下读，也就是找到日志报错开始的地方
     *              4.2.2 第一行是报错类型
     *              4.2.3 后面是具体位置，at在哪儿，然后从后往前读
     *              4.2.4 ()括号里面是哪个java文件的哪一行报错
     *              4.2.5 倒数第一个：方法名
     *              4.2.6 倒数第二个：类名
     *              4.2.7 倒数第三个：包名
     */
    /*public static RespPageBean readCSV(Integer groupid, Map<String,Integer> mapG, MultipartFile file, List<ScoreItem> ScoreItem, List<InfoItem> InfoItem) {
        CsvReader csvReader;
        Participates p;
        //表头属性区{
        String name;
        String phone;
        String idCard;
        String email;
        String code;
        String isBelonging;
        String username;
        String password;
        String displaySequence;
        //}
        List<Participates>participants=new ArrayList<>();
        try {
            csvReader = new CsvReader(file.getInputStream(),',', Charset.forName("GBK"));
            boolean s = csvReader.readHeaders();
            String[] headers = csvReader.getHeaders();
            for (String header : headers) {
                //System.out.println(header);
            }
            Long count= 0L;
            while (csvReader.readRecord()){
                count++;
                p=new Participates();
                name=csvReader.get("姓名");
                if(name.equals(""))
                {break;}
                phone=csvReader.get("手机号").equals("")?csvReader.get("手机号码"):csvReader.get("手机号");
                idCard=csvReader.get("身份证号码").equals("")?csvReader.get("身份证号"):csvReader.get("身份证号码");
                email=csvReader.get("邮箱");
                code=csvReader.get("编号");
                isBelonging=csvReader.get("属于本单位");
                username=csvReader.get("用户名");
                password=csvReader.get("密码");
                displaySequence=csvReader.get("序号");
                if(name.equals("")||idCard.equals("")||isBelonging.equals("")){
                    System.out.println("头或者内容有问题！");
                    RespPageBean bean=new RespPageBean();
                    bean.setTotal(count);
                    return bean;
                }
                p.setName(name);
                p.setTelephone(phone);
                p.setIDNumber(idCard);
                p.setEmail(email);
                p.setCode(code);
                p.setUsername(username);
                p.setPassword(password);
                if(displaySequence.equals(""))
                {
                    p.setDisplaySequence(null);
                }
                else
                {p.setDisplaySequence(Integer.parseInt(displaySequence));}
                if(groupid==0)
                {
                     String groupName=csvReader.get("组名");
                     if(groupName.equals(""))
                     {
                         p.setGroupID(null);
                     }
                     else
                     {
                         boolean contains = mapG.containsKey(groupName);
                         if (contains) { //如果条件为真
                             p.setGroupID(mapG.get(groupName));
                         }
                         else{
                             p.setGroupID(null);
                             System.out.println("组名出错"); //输出信息
                         }
                     }
                }
                HashMap<Integer, String> map = new HashMap<>();
                for(int i=0;i<ScoreItem.size();i++)
                {
                    //System.out.println("score"+ScoreItem.get(i).getName());
                    //System.out.println("score-value"+csvReader.get("评分项:"+ScoreItem.get(i).getName()));
                    map.put(ScoreItem.get(i).getId(),csvReader.get("评分项:"+ScoreItem.get(i).getName()));
                    //key是scoreitem的id，value是插入的分数
                    //System.out.println(map);
                }
                HashMap<Integer, String> map_info = new HashMap<>();
                for(int i=ScoreItem.size();i<ScoreItem.size()+InfoItem.size();i++)
                {
                    //System.out.println("score"+InfoItem.get(i-ScoreItem.size()).getName());
                    //System.out.println("score-value"+csvReader.get("信息项:"+InfoItem.get(i-ScoreItem.size()).getName()));
                    map_info.put(InfoItem.get(i-ScoreItem.size()).getID(),csvReader.get("信息项:"+InfoItem.get(i-ScoreItem.size()).getName()));
                    //key是scoreitem的id，value是插入的分数
                    //System.out.println(map_info);
                }
                p.setScoreItemMap(map);
                p.setInfoItemMap(map_info);
                //如果是本单位，先设置为1，不是设置为0，最后在service中改
                if(isBelonging.equals("是")){
                    p.setInstitutionid(1);
                }else {
                    p.setInstitutionid(0);
                }
                participants.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        RespPageBean bean=new RespPageBean();
        bean.setData(participants);
        return bean;
    }*/
//wyz,csv_teacher原有基础修改readExcel_expert
//增加容错率和错误处理。
//1.csv读取专家信息
/*public static List<Experts> readExcel_expert(MultipartFile file) {
    CsvReader csvReader;
    Experts expert;
    //表头属性区{
    String jobNumber;//工号
    String name;//姓名
    String department;//不门
    String phone;//手机号
    String idCard;//身份证号，主键
    String email;//邮箱
    String sex;//性别
    String isBelonging;//是否是本单位
    String username;
    String password;
    //}
    List<Experts>experts=new ArrayList<>();
    try {
        csvReader = new CsvReader(file.getInputStream(),',', Charset.forName("GBK"));
        boolean s = csvReader.readHeaders();
        String[] headers = csvReader.getHeaders();
        for (String header : headers) {
            //System.out.println(header);
        }
        //读取csv记录
        while (csvReader.readRecord()){
            expert=new Experts();
            name=csvReader.get("姓名");
            department=csvReader.get("部门");
            sex=csvReader.get("性别");
            jobNumber=csvReader.get("工号");
            //简单增加一点容错性
            phone=csvReader.get("手机号").equals("")?csvReader.get("手机号码"):csvReader.get("手机号");
            idCard=csvReader.get("身份证号码").equals("")?csvReader.get("身份证号"):csvReader.get("身份证号码");
            email=csvReader.get("邮箱");
            isBelonging=csvReader.get("属于本单位");
            username=csvReader.get("用户名");
            password=csvReader.get("密码");
            if(name.equals("")||
                    phone.equals("")||
                    idCard.equals("")||
                    jobNumber.equals("")||
                    department.equals("")||
                    (!isBelonging.equals("是")&&!isBelonging.equals("否"))){
                System.out.println("csv 编码或者表头或者信息有问题！");
                return null;
            }
            expert.setName(name);
            expert.setPhone(phone);
            expert.setIdnumber(idCard);
            expert.setEmail(email);
            expert.setDepartment(department);
            expert.setSex(sex);
            expert.setJobNumber(jobNumber);
            expert.setUsername(username);
            expert.setPassword(password);
            //如果是本单位，先设置为1，不是设置为0，最后在service中改
            if(isBelonging.equals("是")){
                expert.setInstitutionid(1);
            }else {
                expert.setInstitutionid(0);
            }
            experts.add(expert);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return experts;
}*/
/*    public static List<Participates> readCSV(String csvFilePath) {
        try {
            // 用来保存数据
            ArrayList<String[]> csvFileList = new ArrayList<String[]>();
            // 定义一个CSV路径
            //String csvFilePath = "D://StemQ.csv";
            // 创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);
            CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("UTF-8"));
            // 跳过表头 如果需要表头的话，这句可以忽略
            reader.readHeaders();
            // 逐行读入除表头的数据
            while (reader.readRecord()) {
                System.out.println(reader.getRawRecord());
                csvFileList.add(reader.getValues());
            }
            reader.close();
            System.out.printf("%d",csvFileList.size());
            List<Participates> p = new ArrayList<>();
            // 遍历读取的CSV文件
            for (int row = 0; row < csvFileList.size(); row++) {
                // 取得第row行第0列的数据
                String cell = csvFileList.get(row)[0];
                System.out.println("------------>"+cell);
                String cell1 = csvFileList.get(row)[1];
                System.out.println("------------>"+cell1);
                String cell2 = csvFileList.get(row)[2];
                System.out.println("------------>"+cell2);
                Participates e=new Participates();
                e.setIDNumber(cell);
                e.setName(cell1);
                e.setinstitutionID(Integer.parseInt(cell2));
                p.add(e);

            }
            return p;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/
    /*public static void writeExcel(OutputStream csvFilePath, List<ScoreItem> ScoreItem,List<InfoItem> InfoItem) {
        try {
            CsvWriter csvWriter = new CsvWriter(csvFilePath, ',', Charset.forName("GBK"));
            // 写表头
            String[] csvHeaders = new String[9+ScoreItem.size()+InfoItem.size()];
            csvHeaders[0]="姓名";
            csvHeaders[1]="编号";
            csvHeaders[2]="序号";
            csvHeaders[3]="手机号";
            csvHeaders[4]="邮箱";
            csvHeaders[5]="属于本单位";
            csvHeaders[6]="用户名";
            csvHeaders[7]="密码";
            for(int i=0;i<InfoItem.size();i++)
            {
                csvHeaders[8+i]="信息项:"+InfoItem.get(i).getName();
            }
            for(int i = InfoItem.size(); i<InfoItem.size()+ScoreItem.size(); i++)
            {
                csvHeaders[8+i]="评分项:"+ScoreItem.get(i-InfoItem.size()).getName();
            }
            csvHeaders[8+InfoItem.size()+ScoreItem.size()]="身份证号码";
            csvWriter.writeRecord(csvHeaders);
            //写内容
            String[] csvContent = { "张三", "181310000","1", "'13812341234","123@dhu.edu.cn","否","zhangsan","'123456","请删除本行，如果student表中已有该选手的记录，则手机号、邮箱、属于本单位三列可为空，“属于本单位”列填是或否。用户名密码可以不填写，若不填写第一次导入将默认为手机号和编码后六位，其余必须填写"};
            csvWriter.writeRecord(csvContent);
            csvWriter.close();
            System.out.println("--------CSV文件已经写入--------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

/*    public static void writeExcel_group(OutputStream csvFilePath, List<ScoreItem> ScoreItem,List<InfoItem> InfoItem) {
        try {
            CsvWriter csvWriter = new CsvWriter(csvFilePath, ',', Charset.forName("GBK"));
            // 写表头
            String[] csvHeaders = new String[10+ScoreItem.size()+InfoItem.size()];
            csvHeaders[0]="姓名";
            csvHeaders[1]="编号";
            csvHeaders[2]="序号";
            csvHeaders[3]="手机号";
            csvHeaders[4]="邮箱";
            csvHeaders[5]="属于本单位";
            csvHeaders[6]="组名";
            csvHeaders[7]="用户名";
            csvHeaders[8]="密码";
            for(int i=0;i<InfoItem.size();i++)
            {
                csvHeaders[9+i]="信息项:"+InfoItem.get(i).getName();
            }
            for(int i = InfoItem.size(); i<InfoItem.size()+ScoreItem.size(); i++)
            {
                csvHeaders[9+i]="评分项:"+ScoreItem.get(i-InfoItem.size()).getName();
            }
            csvHeaders[9+InfoItem.size()+ScoreItem.size()]="身份证号码";
            csvWriter.writeRecord(csvHeaders);
            //写内容
            String[] csvContent = { "张三", "181310000","1", "'13812341234","123@dhu.edu.cn","否","第一组","zhangsan","'123456","请删除本行，如果student表中已有该选手的记录，则手机号、邮箱、属于本单位三列可为空，“属于本单位”列填是或否。用户名密码可以不填写，若不填写第一次导入将默认为手机号和编码后六位，其余必须填写"};
            csvWriter.writeRecord(csvContent);
            csvWriter.close();
            System.out.println("--------CSV文件已经写入--------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static void main(String[] args) {
        //JavaCSV.writeExcel(csvFilePath);
        //CSVReader.readCSV("C:\\Users\\lenovo\\Downloads\\TEST.csv");
    }
}
