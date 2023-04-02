package org.sys.rate.utils;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.poi.hpsf.Array;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.sys.rate.mapper.ScoreItemMapper;
import org.sys.rate.model.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-11-11 23:25
 */
public class POIUtils {
    @Autowired
    ScoreItemMapper scoreItemMapper;

    public static ResponseEntity<byte[]> employee2Excel(HashPEexport hashPEexport) {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory("教师维度分数");
        //文档管理员
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("教师维度分数");
        //文档作者
        summInfo.setAuthor("东华大学");
        // 文档备注
        summInfo.setComments("本文档由东华大学计算机学院提供");
        //5. 创建样式
        //创建标题行的样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("教师维度分数");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 6 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        for(int i=0;i<hashPEexport.getSmap().size();i++)
        {
            sheet.setColumnWidth(5+i, 20 * 256);
        }
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("组名");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("专家姓名");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("选手序号");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("编号");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellStyle(headerStyle);
        c4.setCellValue("选手姓名");
        //notByE
        Object[] key_null =  hashPEexport.getSNotByEmap().keySet().toArray();
        for(int i = 0; i<key_null.length; i++)
        {
            r0.createCell(5+i).setCellStyle(headerStyle);
            r0.createCell(5+i).setCellValue(hashPEexport.getSNotByEmap().get(key_null[i]));
        }
        //ByE
        Object[] key =  hashPEexport.getSmap().keySet().toArray();
        int count_zf=0;
        for(int i = 0; i<key.length; i++)
        {
            if(hashPEexport.getSmap().get(key[i]).equals("活动得分"))
            {
                r0.createCell(4+hashPEexport.getSmap().size()+hashPEexport.getSNotByEmap().size()).setCellStyle(headerStyle);
                r0.createCell(4+hashPEexport.getSmap().size()+hashPEexport.getSNotByEmap().size()).setCellValue(hashPEexport.getSmap().get(key[i]));
            }
            else
            {
                r0.createCell(5+count_zf+hashPEexport.getSNotByEmap().size()).setCellStyle(headerStyle);
                r0.createCell(5+count_zf+hashPEexport.getSNotByEmap().size()).setCellValue(hashPEexport.getSmap().get(key[i]));
                count_zf++;
            }
        }
        /*HSSFCell c4 = r0.createCell(4+scoreItem.size());
        c4.setCellStyle(headerStyle);
        c4.setCellValue("活动得分");*/
        Integer count=0;
        for (Integer s : hashPEexport.getGmap().keySet()) {//这一层是groupid
            for (Integer ss : hashPEexport.getMap().get(s).keySet()) {//这一层是HashMap<Integer,HashMap<Integer,Participates>>
                for (Integer sss : hashPEexport.getMap().get(s).get(ss).keySet()) {//这一层是HashMap<Integer,Participates>
                    Participates p=hashPEexport.getMap().get(s).get(ss).get(sss);
                    HSSFRow row = sheet.createRow(1+count);
                    row.createCell(0).setCellValue(hashPEexport.getGmap().get(s));//groupName
                    row.createCell(1).setCellValue(hashPEexport.getEmap().get(ss));//ExpertName
                    row.createCell(2).setCellValue(p.getDisplaySequence());//display
                    row.createCell(3).setCellValue(p.getCode());//code
                    row.createCell(4).setCellValue(p.getName());//name
                    HashMap<Integer, ScoreItemValue>  map=p.getScoremap();
                    if(hashPEexport.getNull_map()!=null&&hashPEexport.getNull_map().get(s)!=null&&hashPEexport.getNull_map().get(s).get(sss)!=null)
                    {//非专家打分
                        HashMap<Integer, ScoreItemValue>  map_null=hashPEexport.getNull_map().get(s).get(sss).getScoremap();
                        Object[] key_m =  hashPEexport.getSNotByEmap().keySet().toArray();
                        for(int i= 0; i<key_m.length; i++)
                        {
                            ScoreItemValue value = map_null.get(key_m[i]);
                            if(value!=null&&value.getScore()!=null)
                            {row.createCell(5+i).setCellValue(value.getScore());}
                        }
                    }
                    if(map!=null){//专家打分
                        Object[] key_m =  hashPEexport.getSmap().keySet().toArray();
                        //Arrays.sort(key_m);
                        int count_zongfen=0;
                        for(int it= 0; it<key_m.length; it++)
                        {
                            ScoreItemValue value = map.get(key_m[it]);
                            if(value!=null&&value.getScore()!=null)
                            {
                                if(value.getName().equals("活动得分"))
                                {
                                    row.createCell(4+hashPEexport.getSmap().size()+hashPEexport.getSNotByEmap().size()).setCellValue(value.getScore());
                                    count_zongfen--;
                                }
                                else{
                                row.createCell(5+count_zongfen+hashPEexport.getSNotByEmap().size()).setCellValue(value.getScore());
                                }
                            }
                            count_zongfen++;
                        }
                    }
                    count++;
                }
            }

        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("教师维度分数.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }



    public static RespPageBean excel2p(Integer groupid, Map<String,Integer> mapG, MultipartFile file, List<ScoreItem> ScoreItem, List<InfoItem> InfoItem) {
        List<Participates> list = new ArrayList<>();
        Participates p=new Participates();
        try {
            //1. 创建一个 workbook 对象
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            //2. 获取 workbook 中表单的数量
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                //3. 获取表单
                HSSFSheet sheet = workbook.getSheetAt(i);
                //4. 获取表单中的行数
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                int Cells = sheet.getRow(0).getPhysicalNumberOfCells();
                HashMap<Integer, String> map = new HashMap<>();
                for(int m = 0; m < Cells; m++)
                {
                    if(sheet.getRow(0).getCell(m).getStringCellValue()!=null)
                    map.put(m,sheet.getRow(0).getCell(m).getStringCellValue());
                }
                for (int j = 0; j < physicalNumberOfRows; j++) {
                    //5. 跳过标题行
                    if (j == 0) {
                        continue;//跳过标题行//获得表头，为后续对应位置
                    }
                    //6. 获取行
                    HSSFRow row = sheet.getRow(j);
                    if (row == null) {
                        continue;//防止数据中间有空行
                    }
                    //7. 获取列数
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    p = new Participates();
                    String name = null;
                    String phone= null;
                    String idCard= null;
                    String email= null;
                    String code= null;
                    String isBelonging= "";
                    String username= "";
                    String password= "";
                    String displaySequence= null;
                    String groupName= null;
                    HashMap<String, String> countNameAndContent = new HashMap<>();
                    for (int k = 0; k < Cells; k++) {
                        HSSFCell cell = row.getCell(k);
                        //cell.setCellType(CellType.STRING);
                        if (cell!=null) {
                            cell.setCellType(CellType.STRING);
                            //case STRING:
                                String cellValue = cell.getStringCellValue();
                                switch (map.get(k)) {
                                    case "姓名":
                                        name=cellValue;
                                        break;
                                    case "手机号":
                                        phone=cellValue;
                                        break;
                                    case "身份证号码":
                                        idCard=cellValue;
                                        break;
                                    case "邮箱":
                                        email=cellValue;
                                        break;
                                    case "编号":
                                        code=cellValue;
                                        break;
                                    case "属于本单位":
                                        isBelonging=cellValue;
                                        break;
                                    case "用户名":
                                        username=cellValue;
                                        break;
                                    case "密码":
                                        password=cellValue;
                                        break;
                                    case "序号":
                                        displaySequence=cellValue;
                                        break;
                                    case "组名":
                                        groupName=cellValue;
                                        break;
                                    default:
                                        countNameAndContent.put(map.get(k),cellValue);
                                        break;
                                }
                                //break;
                        }
                        else {
                            switch (map.get(k)) {
                                case "姓名":
                                    name="";
                                    break;
                                case "手机号":
                                    phone="";
                                    break;
                                case "身份证号码":
                                    idCard="";
                                    break;
                                case "邮箱":
                                    email="";
                                    break;
                                case "编号":
                                    code="";
                                    break;
                                case "属于本单位":
                                    isBelonging="";
                                    break;
                                case "用户名":
                                    username="";
                                    break;
                                case "密码":
                                    password="";
                                    break;
                                case "序号":
                                    displaySequence="";
                                    break;
                                case "组名":
                                    groupName="";
                                    break;
                                default:
                                    countNameAndContent.put(map.get(k),"-1");
                                    break;
                            }
                            //break;
                        }
                    }
                    if(name==null||idCard==null||name.equals("")||idCard.equals("")){
                        continue;
//                        System.out.println("头或者内容有问题！");
//                        RespPageBean bean=new RespPageBean();
//                        bean.setTotal((long) j);
//                        return bean;
                    }
                    p.setName(name);
                    p.setTelephone(phone);
                    p.setIDNumber(idCard);
                    p.setEmail(email);
                    p.setCode(code);
                    p.setUsername(username);
                    p.setPassword(password);

                    if(displaySequence==null)
                    {
                        p.setDisplaySequence(-1);//不修改
                    }
                    else if(displaySequence.equals(""))
                    {
                        p.setDisplaySequence(null);
                    }
                    else
                    {p.setDisplaySequence(Integer.parseInt(displaySequence));}
                    if(groupid==0)
                    {//全活动
                        if(groupName==null)
                        {
                            p.setGroupID(-1);//不修改
                        }
                        else if(groupName.equals(""))
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
//                                System.out.println("组名出错"); //输出信息
                            }
                        }
                    }
                    if(isBelonging.equals("是")){
                        p.setInstitutionid(1);
                    }else if(isBelonging.equals("否")){
                        p.setInstitutionid(0);
                    }else {
                        p.setInstitutionid(-1);//不修改
                    }
                    HashMap<Integer, String> map_score = new HashMap<>();
                    for(int n=0;n<ScoreItem.size();n++)
                    {
                        if(countNameAndContent.get("评分项:"+ScoreItem.get(n).getName())!=null)
                        map_score.put(ScoreItem.get(n).getId(),countNameAndContent.get("评分项:"+ScoreItem.get(n).getName()));
                        //key是scoreitem的id，value是插入的分数
                    }
                    HashMap<Integer, String> map_info = new HashMap<>();
                    for(int n=ScoreItem.size();n<ScoreItem.size()+InfoItem.size();n++)
                    {
                        if(countNameAndContent.get("信息项:"+InfoItem.get(n-ScoreItem.size()).getName())!=null)
                        map_info.put(InfoItem.get(n-ScoreItem.size()).getID(),countNameAndContent.get("信息项:"+InfoItem.get(n-ScoreItem.size()).getName()));
                        //key是scoreitem的id，value是插入的分数
                    }
                    p.setScoreItemMap(map_score);
                    p.setInfoItemMap(map_info);
                    list.add(p);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        RespPageBean bean=new RespPageBean();
        bean.setData(list);
        bean.setTotal((long) list.size());
        return bean;
    }

    // 表格由两部分组成：固定信息+附加信息。固定信息是序号、编号、组名、姓名、专家评分、活动得分。附加信息为总分项。
    public static ResponseEntity<byte[]> ExcelExport(HashFianlScore hashFianlScore) {
        // 0.基础配置
        HSSFWorkbook workbook = new HSSFWorkbook(); // 创建一个 Excel 文档
        workbook.createInformationProperties();// 创建文档摘要
        // 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        docInfo.setCategory("组分数");// 文档类别
        docInfo.setManager("rate"); // 文档管理员
        docInfo.setCompany("东华大学"); // 设置公司信息
        // 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        summInfo.setTitle("以后是groupname"); // 文档标题
        summInfo.setAuthor("东华大学计算机学院"); // 文档作者
        summInfo.setComments("本文档由东华大学计算机学院提供"); // 文档备注
        // 创建样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("学生维度分数");

        // 1.获取数据
        List<Participates> list = new ArrayList<>(hashFianlScore.getMap().values());
        List<Integer> itemKey = new ArrayList<>(hashFianlScore.getTmap().keySet()); // 附加信息的key

        // 2.设置列宽
        List<String> columnsBase = Arrays.asList("序号", "编号", "组名", "姓名", "专家评分", "活动得分");
        List<String> columnsAdd = new ArrayList<>(hashFianlScore.getTmap().values());
        for (int i = 0; i < columnsBase.size()+columnsAdd.size(); i++)
            sheet.setColumnWidth(i, 10 * 256);
        sheet.setColumnWidth(1, 18 * 256); // "编号"需要更宽

        // 3. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        // 固定信息
        for (int i = 0; i < columnsBase.size(); i++) {
            HSSFCell cell = r0.createCell(i);
            cell.setCellValue(columnsBase.get(i));
            cell.setCellStyle(headerStyle);
        }
        // 附加信息
        for (int i = columnsBase.size();i < columnsBase.size()+columnsAdd.size(); i++) {
            HSSFCell cell = r0.createCell(i);
            cell.setCellValue(columnsAdd.get(i-columnsBase.size()));
            cell.setCellStyle(headerStyle);
        }

        //4. 填充数据
        for (int i = 0; i < list.size(); i++) {
            Participates emp = list.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            if (emp.getDisplaySequence() != null)
                row.createCell(0).setCellValue(emp.getDisplaySequence());
            if (emp.getCode() != null)
                row.createCell(1).setCellValue(emp.getCode());
            if (emp.getGroupName() != null)
                row.createCell(2).setCellValue(emp.getGroupName());
            if (emp.getName() != null)
                row.createCell(3).setCellValue(emp.getName());
            if (emp.getTotalscorewithdot() != null)
                row.createCell(4).setCellValue(emp.getTotalscorewithdot());
            if (emp.getScore() != null)
                row.createCell(5).setCellValue(emp.getScore());
            for (int j = 0; j < itemKey.size(); j++) {
                if (emp.getFinalmap().get(itemKey.get(j)) != null)
                    row.createCell(columnsBase.size()+j).setCellValue(emp.getFinalmap().get(itemKey.get(j)).getScore());
            }
        }

        // 5.输出
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("学生维度的分数.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    public static ResponseEntity<byte[]> ExpertScore2Excel(List<Participates> list) {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory("组分数");
        //文档管理员
        docInfo.setManager("rate");
        //设置公司信息
        docInfo.setCompany("东华大学");
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("");
        //文档作者
        summInfo.setAuthor("东华大学计算机学院");
        // 文档备注
        summInfo.setComments("本文档由东华大学计算机学院提供");
        //5. 创建样式
        //创建标题行的样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("以后是groupname");
        //设置列的宽度
        sheet.setColumnWidth(0, 5 * 256);
        sheet.setColumnWidth(1, 12 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 5 * 256);
        sheet.setColumnWidth(4, 12 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 16 * 256);
        sheet.setColumnWidth(9, 12 * 256);
        sheet.setColumnWidth(10, 15 * 256);
        sheet.setColumnWidth(11, 20 * 256);
        sheet.setColumnWidth(12, 16 * 256);
        sheet.setColumnWidth(13, 14 * 256);
        sheet.setColumnWidth(14, 14 * 256);
        sheet.setColumnWidth(15, 12 * 256);
        sheet.setColumnWidth(16, 8 * 256);
        sheet.setColumnWidth(17, 20 * 256);
        sheet.setColumnWidth(18, 20 * 256);
        sheet.setColumnWidth(19, 15 * 256);
        sheet.setColumnWidth(20, 8 * 256);
        sheet.setColumnWidth(21, 25 * 256);
        sheet.setColumnWidth(22, 14 * 256);
        sheet.setColumnWidth(23, 15 * 256);
        sheet.setColumnWidth(24, 15 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        //==
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("编号");
        c0.setCellStyle(headerStyle);
        //==
        HSSFCell c1 = r0.createCell(1);
        c1.setCellStyle(headerStyle);
        c1.setCellValue("姓名");
        //==
        HSSFCell c2 = r0.createCell(2);
        c2.setCellStyle(headerStyle);
        c2.setCellValue("学号");
        //==
        HSSFCell c3 = r0.createCell(3);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("性别");
        //==
        HSSFCell c4 = r0.createCell(4);//have
        c4.setCellStyle(headerStyle);
        c4.setCellValue("出生日期");
        //==
        HSSFCell c5 = r0.createCell(5);
        c5.setCellStyle(headerStyle);
        c5.setCellValue("身份证号码");
        for (int i = 0; i < list.size(); i++) {
            Participates emp = list.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            //----
            row.createCell(0).setCellValue(emp.getStudentID());
            row.createCell(1).setCellValue(emp.getName());
            row.createCell(2).setCellValue(emp.getStudentID());
            row.createCell(3).setCellValue(emp.getSex());

        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("成绩登记表.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    public static ResponseEntity<byte[]> writeExcel_group(List<String> datalist,List<String> ScoreItem, List<String> InfoItem) {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("participants");
        //文档作者
        summInfo.setAuthor("东华大学");
        // 文档备注
        summInfo.setComments("本文档由东华大学计算机学院提供");
        //5. 创建样式
        //创建标题行的样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("participants");
        //设置列的宽度
        for(int i=0;i<datalist.size()+InfoItem.size()+ScoreItem.size();i++)
        {
            sheet.setColumnWidth(i, 20 * 256);
        }
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFRow row = sheet.createRow(1);
        for(int i=0;i<datalist.size();i++)
        {
            r0.createCell(i).setCellValue(datalist.get(i));
            switch (datalist.get(i)) {
                case "姓名":
                    row.createCell(i).setCellValue("张三");
                    break;
                case "编号":
                    row.createCell(i).setCellValue("181310000");
                    break;
                case "序号":
                    row.createCell(i).setCellValue("1");
                    break;
                case "手机号":
                    row.createCell(i).setCellValue("13812341234");
                    break;
                case "邮箱":
                    row.createCell(i).setCellValue("123@dhu.edu.cn");
                    break;
                case "属于本单位":
                    row.createCell(i).setCellValue("否");
                    break;
                case "用户名":
                    row.createCell(i).setCellValue("zhangsan");
                    break;
                case "密码":
                    row.createCell(i).setCellValue("123456");
                    break;
                case "身份证号码":
                    row.createCell(i).setCellValue("310111111111111111");
                    break;
                case "组名":
                    row.createCell(i).setCellValue("第1组");
                    break;
                default:
                    break;
            }
        }
        for(int i=0;i<InfoItem.size();i++)
        {
            r0.createCell(datalist.size()+i).setCellValue("信息项:"+InfoItem.get(i));
        }
        for(int i = InfoItem.size(); i<InfoItem.size()+ScoreItem.size(); i++)
        {
            r0.createCell(datalist.size()+i).setCellValue("评分项:"+ScoreItem.get(i-InfoItem.size()));
        }
        row.createCell(datalist.size()).setCellValue("请删除本行，如果student表中已有该选手的记录，则手机号、邮箱、属于本单位三列可为空，“属于本单位”列填是或否。用户名密码可以不填写，若不填写第一次导入将默认为编号和手机号，其余必须填写。如果用户已经存在，则导入数据中的用户名和密码将被忽略。");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("participants模板.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }
    public static ResponseEntity<byte[]> writeMo() {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("experts");
        //文档作者
        summInfo.setAuthor("东华大学");
        // 文档备注
        summInfo.setComments("本文档由东华大学计算机学院提供");
        //5. 创建样式
        //创建标题行的样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("experts");
        //设置列的宽度
        sheet.setColumnWidth(0, 15 * 256);
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 6 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        sheet.setColumnWidth(9, 20 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("工号");
        HSSFCell c1 = r0.createCell(1);
        c1.setCellValue("姓名");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellValue("部门");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellValue("性别");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellValue("身份证号码");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellValue("手机号");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellValue("邮箱");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellValue("属于本单位");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellValue("用户名");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellValue("密码");
        HSSFRow row = sheet.createRow(1);
        row.createCell(0).setCellValue("20131000");
        row.createCell(1).setCellValue("张三");
        row.createCell(2).setCellValue("计算机学院");
        row.createCell(3).setCellValue("男");
        row.createCell(4).setCellValue("123456789123456789");
        row.createCell(5).setCellValue("13812341234");
        row.createCell(6).setCellValue("123@dhu.edu.cn");
        row.createCell(7).setCellValue("否");
        row.createCell(8).setCellValue("zhangsan");
        row.createCell(9).setCellValue("123456");
        sheet.createRow(2).createCell(0).setCellValue("请删除提示行，如果数据库中已有该专家的记录，将根据填写信息进行更新，“属于本单位”列填是或否。用户名密码可以不填写，若不填写第一次导入将默认为手机号和身份证后六位，其余必须填写。");
        sheet.createRow(3).createCell(0).setCellValue("如果用户已经存在，则导入数据中的用户名和密码将被忽略。");
        sheet.createRow(4).createCell(0).setCellValue("请再三检查身份证号，无法进行动态更新！！！");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("experts模板.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    //管理员下载本科生模版excel
    public static ResponseEntity<byte[]> writeUnderGraduate() {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("undergraduate");
        //文档作者
        summInfo.setAuthor("东华大学");
        // 文档备注
        summInfo.setComments("本文档由东华大学计算机学院提供");
        //5. 创建样式
        //创建标题行的样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("undergraduate");
        //设置列的宽度
        sheet.setColumnWidth(0, 15 * 256);
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 6 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 10 * 256);
//        sheet.setColumnWidth(8, 20 * 256);
//        sheet.setColumnWidth(9, 20 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("姓名");
        HSSFCell c1 = r0.createCell(1);
        c1.setCellValue("学号");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellValue("身份证号码");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellValue("手机号");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellValue("邮箱");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellValue("用户名");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellValue("密码");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellValue("入学年份");
        HSSFRow row = sheet.createRow(1);
        row.createCell(0).setCellValue("张三");
        row.createCell(1).setCellValue("1111");
        row.createCell(2).setCellValue("123456789123456789");
        row.createCell(3).setCellValue("13812341234");
        row.createCell(4).setCellValue("123@dhu.edu.cn");
        row.createCell(7).setCellValue("2018");
        sheet.createRow(2).createCell(0).setCellValue("请删除提示行。用户名密码可以不填写，若不填写第一次导入将默认为手机号，其余必须填写。");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("本科生模板.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    //管理员上传本科生模版excel
    public static Map<String,List> readExcel_undergraduate(MultipartFile file) {
        List<UnderGraduate> underList = new ArrayList<>();
        UnderGraduate underGraduate=new UnderGraduate();
        List<Student> studentList = new ArrayList<>();
        Student student=new Student();
        try {//1. 创建一个 workbook 对象
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            //2. 获取 workbook 中表单的数量
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {//3. 获取表单
                HSSFSheet sheet = workbook.getSheetAt(i);//4. 获取表单中的行数
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                int Cells = sheet.getRow(0).getPhysicalNumberOfCells();
                HashMap<Integer, String> map = new HashMap<>();
                for(int m = 0; m < Cells; m++)
                {
                    if(sheet.getRow(0).getCell(m).getStringCellValue()!=null)
                        map.put(m,sheet.getRow(0).getCell(m).getStringCellValue());
                }
                for (int j = 0; j < physicalNumberOfRows; j++) {//5. 跳过标题行
                    if (j == 0) {
                        continue;//跳过标题行//获得表头，为后续对应位置
                    }//6. 获取行
                    HSSFRow row = sheet.getRow(j);
                    if (row == null) {
                        continue;//防止数据中间有空行
                    }//7. 获取列数
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    underGraduate = new UnderGraduate();
                    String stuNumber=null;//学号
                    String name=null;//姓名
                    String phone=null;//手机号
                    String idCard=null;//身份证号
                    String email=null;//邮箱
                    String username=null;
                    String password=null;
                    String year=null;
                    for (int k = 0; k < Cells; k++) {
                        HSSFCell cell = row.getCell(k);
                        if (cell!=null) {
                            cell.setCellType(CellType.STRING);
                            String cellValue = cell.getStringCellValue();
                            switch (map.get(k)) {
                                case "姓名":
                                    name=cellValue;
                                    break;
                                case "学号":
                                    stuNumber=cellValue;
                                    break;
                                case "手机号":
                                    phone=cellValue;
                                    break;
                                case "身份证号码":
                                    idCard=cellValue;
                                    break;
                                case "邮箱":
                                    email=cellValue;
                                    break;
                                case "用户名":
                                    username=cellValue;
                                    break;
                                case "密码":
                                    password=cellValue;
                                    break;
                                case "入学年份":
                                    year=cellValue;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    if(phone==null|| stuNumber==null|| email==null || name==null || idCard==null){
                        continue;
                    }
                    student.setName(name);
                    student.setTelephone(phone);
                    student.setIDNumber(idCard);
                    student.setEmail(email);
                    student.setUsername(username);
                    student.setPassword(password);
                    underGraduate.setStuNumber(stuNumber);
                    underGraduate.setYear(Integer.parseInt(year));
                    underGraduate.setInstitutionID(1);
                    studentList.add(student);
                    underList.add(underGraduate);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String,List> mm = new HashMap<>();
        mm.put("studentlist",studentList);
        mm.put("underlist",underList);
        return mm;
    }

    //管理员下载研究生模版excel
    public static ResponseEntity<byte[]> writeGraduateStudent() {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("graduatestudent");
        //文档作者
        summInfo.setAuthor("东华大学");
        // 文档备注
        summInfo.setComments("本文档由东华大学计算机学院提供");
        //5. 创建样式
        //创建标题行的样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("graduatestudent");
        //设置列的宽度
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 15 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("姓名");
        HSSFCell c1 = r0.createCell(1);
        c1.setCellValue("学号");
        HSSFCell c2 = r0.createCell(2);
        c2.setCellValue("身份证号码");
        HSSFCell c3 = r0.createCell(3);
        c3.setCellValue("手机号");
        HSSFCell c4 = r0.createCell(4);
        c4.setCellValue("邮箱");
        HSSFCell c5 = r0.createCell(5);
        c5.setCellValue("用户名");
        HSSFCell c6 = r0.createCell(6);
        c6.setCellValue("密码");
        HSSFCell c7 = r0.createCell(7);
        c7.setCellValue("入学年份");
        HSSFCell c8 = r0.createCell(8);
        c8.setCellValue("学生类别");
        HSSFCell c9 = r0.createCell(9);
        c9.setCellValue("积分");
        HSSFRow row = sheet.createRow(1);
        row.createCell(0).setCellValue("张三");
        row.createCell(1).setCellValue("1111");
        row.createCell(2).setCellValue("123456789123456789");
        row.createCell(3).setCellValue("13812341234");
        row.createCell(4).setCellValue("123@dhu.edu.cn");
        row.createCell(7).setCellValue("2018");
        row.createCell(8).setCellValue("专硕/学硕");
        row.createCell(9).setCellValue("12");


        sheet.createRow(2).createCell(0).setCellValue("请删除提示行。用户名密码可以不填写，若不填写第一次导入将默认为手机号，其余必须填写。");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String("研究生模板.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    //管理员上传研究生模版excel
    public static Map<String,List> readExcel_graduatestudent(MultipartFile file) {
        //tutorid目前没有处理
        List<GraduateStudent> graduateList = new ArrayList<>();
        GraduateStudent graduateStudent=new GraduateStudent();
        List<Student> studentList = new ArrayList<>();
        Student student=new Student();
        try {//1. 创建一个 workbook 对象
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            //2. 获取 workbook 中表单的数量
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {//3. 获取表单
                HSSFSheet sheet = workbook.getSheetAt(i);//4. 获取表单中的行数
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                int Cells = sheet.getRow(0).getPhysicalNumberOfCells();
                HashMap<Integer, String> map = new HashMap<>();
                for(int m = 0; m < Cells; m++)
                {
                    if(sheet.getRow(0).getCell(m).getStringCellValue()!=null)
                        map.put(m,sheet.getRow(0).getCell(m).getStringCellValue());
                }
                for (int j = 0; j < physicalNumberOfRows; j++) {//5. 跳过标题行
                    if (j == 0) {
                        continue;//跳过标题行//获得表头，为后续对应位置
                    }//6. 获取行
                    HSSFRow row = sheet.getRow(j);
                    if (row == null) {
                        continue;//防止数据中间有空行
                    }//7. 获取列数
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    graduateStudent = new GraduateStudent();
                    String stuNumber=null;//学号
                    String name=null;//姓名
                    String phone=null;//手机号
                    String idCard=null;//身份证号
                    String email=null;//邮箱
                    String username=null;
                    String password=null;
                    String year=null;
                    String studentType=null;
                    String point=null;
                    for (int k = 0; k < Cells; k++) {
                        HSSFCell cell = row.getCell(k);
                        if (cell!=null) {
                            cell.setCellType(CellType.STRING);
                            String cellValue = cell.getStringCellValue();
                            switch (map.get(k)) {
                                case "姓名":
                                    name=cellValue;
                                    break;
                                case "学号":
                                    stuNumber=cellValue;
                                    break;
                                case "手机号":
                                    phone=cellValue;
                                    break;
                                case "身份证号码":
                                    idCard=cellValue;
                                    break;
                                case "邮箱":
                                    email=cellValue;
                                    break;
                                case "用户名":
                                    username=cellValue;
                                    break;
                                case "密码":
                                    password=cellValue;
                                    break;
                                case "入学年份":
                                    year=cellValue;
                                    break;
                                case "学生类别":
                                    studentType=cellValue;
                                    break;
                                case "积分":
                                    point=cellValue;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    if(phone==null|| stuNumber==null|| email==null || name==null || idCard==null || studentType == null || point == null){
                        continue;
                    }
                    student.setName(name);
                    student.setTelephone(phone);
                    student.setIDNumber(idCard);
                    student.setEmail(email);
                    student.setUsername(username);
                    student.setPassword(password);
                    graduateStudent.setStuNumber(stuNumber);
                    graduateStudent.setYear(Integer.parseInt(year));
                    graduateStudent.setInstitutionID(1);
                    graduateStudent.setStudentType(studentType);
                    graduateStudent.setPoint(point);
                    studentList.add(student);
                    graduateList.add(graduateStudent);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String,List> mm = new HashMap<>();
        mm.put("studentlist",studentList);
        mm.put("graduatelist",graduateList);
        return mm;
    }

    public static RespPageBean readExcel_expert(MultipartFile file) {
        List<Experts> list = new ArrayList<>();
        Experts expert=new Experts();
        try {//1. 创建一个 workbook 对象
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            //2. 获取 workbook 中表单的数量
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {//3. 获取表单
                HSSFSheet sheet = workbook.getSheetAt(i);//4. 获取表单中的行数
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                int Cells = sheet.getRow(0).getPhysicalNumberOfCells();
                HashMap<Integer, String> map = new HashMap<>();
                for(int m = 0; m < Cells; m++)
                {
                    if(sheet.getRow(0).getCell(m).getStringCellValue()!=null)
                        map.put(m,sheet.getRow(0).getCell(m).getStringCellValue());
                }
                for (int j = 0; j < physicalNumberOfRows; j++) {//5. 跳过标题行
                    if (j == 0) {
                        continue;//跳过标题行//获得表头，为后续对应位置
                    }//6. 获取行
                    HSSFRow row = sheet.getRow(j);
                    if (row == null) {
                        continue;//防止数据中间有空行
                    }//7. 获取列数
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    expert = new Experts();
                    String jobNumber=null;//工号
                    String name=null;//姓名
                    String department=null;//部门
                    String phone=null;//手机号
                    String idCard=null;//身份证号，主键
                    String email=null;//邮箱
                    String sex=null;//性别
                    String isBelonging=null;//是否是本单位
                    String username=null;
                    String password=null;
                    for (int k = 0; k < Cells; k++) {
                        HSSFCell cell = row.getCell(k);
                        //cell.setCellType(CellType.STRING);
                        if (cell!=null) {
                            cell.setCellType(CellType.STRING);
                            //case STRING:
                            String cellValue = cell.getStringCellValue();
                            switch (map.get(k)) {
                                case "姓名":
                                    name=cellValue;
                                    break;
                                case "手机号":
                                    phone=cellValue;
                                    break;
                                case "身份证号码":
                                    idCard=cellValue;
                                    break;
                                case "邮箱":
                                    email=cellValue;
                                    break;
                                case "部门":
                                    department=cellValue;
                                    break;
                                case "属于本单位":
                                    isBelonging=cellValue;
                                    break;
                                case "用户名":
                                    username=cellValue;
                                    break;
                                case "密码":
                                    password=cellValue;
                                    break;
                                case "性别":
                                    sex=cellValue;
                                    break;
                                case "工号":
                                    jobNumber=cellValue;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    if(phone==null||
                            jobNumber==null||
                            department==null||
                            (!isBelonging.equals("是")&&!isBelonging.equals("否"))||name==null||idCard==null){
                            continue;
//                        System.out.println("csv 编码或者表头或者信息有问题！！");
//                        RespPageBean bean=new RespPageBean();
//                        bean.setTotal((long) j);
//                        return bean;
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
                    if(isBelonging.equals("是")){
                        expert.setInstitutionid(1);
                    }else {
                        expert.setInstitutionid(0);
                    }
                    list.add(expert);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        RespPageBean bean=new RespPageBean();
        bean.setData(list);
        bean.setTotal((long) list.size());
        return bean;
    }

    public static List<String> check(MultipartFile file) {
        List<String> error=new ArrayList<>();
        try {
            //1. 创建一个 workbook 对象
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            //2. 获取 workbook 中表单的数量
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                //3. 获取表单
                HSSFSheet sheet = workbook.getSheetAt(i);
                //4. 获取表单中的行数
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                int Cells = sheet.getRow(0).getPhysicalNumberOfCells();
                HashMap<Integer, String> map = new HashMap<>();
                for(int m = 0; m < Cells; m++)
                {
                    if(sheet.getRow(0).getCell(m).getStringCellValue()!=null)
                        map.put(m,sheet.getRow(0).getCell(m).getStringCellValue());
                }
                //行
                for (int j = 0; j < physicalNumberOfRows; j++) {
                    //5. 跳过标题行
                    if (j == 0) {
                        continue;//跳过标题行//获得表头，为后续对应位置
                    }
                    //6. 获取行
                    HSSFRow row = sheet.getRow(j);
                    if (row == null) {
                        continue;//防止数据中间有空行
                    }
                    //7. 获取列数
                    int rowNullNums = 0;
                    for (int k = 0; k < Cells; k++) {
                        HSSFCell cell = row.getCell(k);
                        //cell.setCellType(CellType.STRING);
                        if (cell == null) {//如果列中cell为空
                            rowNullNums ++;
//                            String tips="【"+map.get(k)+"】列中第【"+j+"】行";
//                            error.add(tips);
                        }else {
                            cell.setCellType(CellType.STRING);
                            String cellValue = cell.getStringCellValue();
                            if (cellValue.equals("")) {//如果列中cell为空
                                rowNullNums ++;
                            }
                        }
                    }
                    if(rowNullNums != Cells && rowNullNums != 0){
                        String tips="第【" + j + "】行有空数据，";
                        error.add(tips);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return error;
    }

    public static ResponseEntity<byte[]> ExportRate(List<Activities> Act,List<Participates> participatesL,List<Scores> scoresList,List<ScoreItem> scoreItems, List<InfoItem> infoItemsShow,List<Infos> infosList) {
        //1. 创建一个 Excel 文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2. 创建文档摘要
        workbook.createInformationProperties();
        //3. 获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory("组分数");
        //文档管理员
        docInfo.setManager("rate");
        //设置公司信息
        docInfo.setCompany("东华大学");
        //4. 获取文档摘要信息
        SummaryInformation summInfo = workbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("");
        //文档作者
        summInfo.setAuthor("东华大学计算机学院");
        // 文档备注
        summInfo.setComments("本文档由东华大学计算机学院提供");
        //5. 创建样式
        //创建标题行的样式
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        //垂直居中，水平居中
        titleStyle.setAlignment(HorizontalAlignment.CENTER);   // 设置左右居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);    // 设置水平居中
        titleStyle.setFillForegroundColor((short) 1);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setBorderRight(BorderStyle.THICK); //设1置右边框
//        headerStyle.setWrapText(true);  // 自动换行
        // 字体
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);// 11号字体
        titleStyle.setFont(font);

        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        HSSFSheet sheet = workbook.createSheet("专家评分");
        //设置列的宽度
        sheet.setColumnWidth(0, 8 * 256);
        sheet.setColumnWidth(1, 18 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 18 * 256);
        sheet.setColumnWidth(4, 18 * 256);
        sheet.setColumnWidth(5, 18 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        sheet.setColumnWidth(10, 16 * 256);
        sheet.setColumnWidth(11, 16 * 256);
        sheet.setColumnWidth(12, 16 * 256);
        sheet.setColumnWidth(13, 14 * 256);
        sheet.setColumnWidth(14, 15 * 256);
        sheet.setColumnWidth(15, 15 * 256);
        sheet.setColumnWidth(16, 10 * 256);
        sheet.setColumnWidth(17, 20 * 256);
        sheet.setColumnWidth(18, 20 * 256);
        sheet.setColumnWidth(19, 15 * 256);
        sheet.setColumnWidth(20, 8 * 256);

        //6. 创建标题行
        HSSFRow r0 = sheet.createRow(0);
        r0.setHeightInPoints(30);
        String title = Act.get(0).getName()+"评分表";
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue(title);
        c0.setCellStyle(titleStyle);
        // 合并单元格，参数依次为起始行，结束行，起始列，结束列 （索引0开始）
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2 + infoItemsShow.size()+scoreItems.size()));//标题合并单元格操作，6为总列数
        //创建表头的样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        //垂直居中，水平居中
        headerStyle.setAlignment(HorizontalAlignment.CENTER);   // 设置左右居中
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);    // 设置水平居中
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.index);//设置背景颜色
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFFont font1 = workbook.createFont();
        font1.setFontName("宋体");
        font1.setFontHeightInPoints((short) 11);// 11号字体
        headerStyle.setFont(font1);
        //==
        HSSFRow r1 = sheet.createRow(1);
        HSSFCell c1 = r1.createCell(0);
        c1.setCellValue("序号");
        c1.setCellStyle(headerStyle);
        //==
        HSSFCell c2 = r1.createCell(1);
        c2.setCellValue("编号");
        c2.setCellStyle(headerStyle);
        //==
        HSSFCell c3 = r1.createCell(2);
        c3.setCellStyle(headerStyle);
        c3.setCellValue("姓名");
        //==infoItem where display=1
        for(int i=0;i<infoItemsShow.size();i++){
            InfoItem Iitem = infoItemsShow.get(i);
            HSSFCell col = r1.createCell(i+3);
            col.setCellStyle(headerStyle);
            col.setCellValue(Iitem.getName());
        }
        //遍历评分项
        for(int i=0;i<scoreItems.size();i++){
            ScoreItem sitem = scoreItems.get(i);
            HSSFCell col = r1.createCell(i+3+infoItemsShow.size());
            col.setCellStyle(headerStyle);
            if(sitem.getName().equals("活动得分")){
                col.setCellValue("总评分");
            }else {
                col.setCellValue(sitem.getName());
            }
        }
        for (int i = 0; i < participatesL.size(); i++) {
            Participates emp = participatesL.get(i);
            Student stu = participatesL.get(i).getStudent();
            HSSFRow row = sheet.createRow(i + 2);
            //----
            row.createCell(0).setCellValue(emp.getDisplaySequence());
            row.createCell(1).setCellValue(emp.getCode());
            row.createCell(2).setCellValue(stu.getName());
            for(int j=0;j<infoItemsShow.size();j++) {
                InfoItem Iitem = infoItemsShow.get(j);
                for (Infos infoL : infosList) {
                    if (Iitem.getID().equals(infoL.getInfoItemID()) && infoL.getParticipantID().equals(emp.getID())) {
                        if (infoL.getContent()!=null) row.createCell(j + 3).setCellValue(infoL.getContent());

                        break;
                    }
                }
            }

            for(int j=0;j<scoreItems.size();j++){
                ScoreItem sitem = scoreItems.get(j);
                for (Scores scoreL : scoresList) {
                    if (scoreL.getScoreItemID().equals(sitem.getId()) && scoreL.getParticipantID().equals(emp.getID())) {
                        if (scoreL.getScore()!=null){
                            row.createCell(j + 3 + infoItemsShow.size()).setCellValue(scoreL.getScore());
                        }
//                        break;
                    }
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", new String((title + ".xls").getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }

    public static void read_(HSSFWorkbook workbook){
        HSSFSheet sheet = workbook.getSheet("Sheet1");
        HSSFRow row = sheet.getRow(17);//Row
        HSSFCell cell = row.getCell(6);
    }

    public static Msg readExcel_rate(MultipartFile file,List<ScoreItem> scoreItemsByE) {
        List<Map<String, String>> rateList = new ArrayList<>();
        ArrayList nullRow = new ArrayList();
        try {//1. 创建一个 workbook 对象
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            //2. 获取 workbook 中表单的数量
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {//3. 获取表单
                HSSFSheet sheet = workbook.getSheetAt(i);//4. 获取表单中的行数
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                int Cells = sheet.getRow(1).getPhysicalNumberOfCells();//获取列数
                HashMap<Integer, String> tableNameMap = new HashMap<>();
                for(int m = 0; m < Cells; m++)
                {
                    if(sheet.getRow(1).getCell(m).getStringCellValue()!=null)
                        tableNameMap.put(m,sheet.getRow(1).getCell(m).getStringCellValue());
                }
                //总分是否为空的标志
                boolean fullScoreFlage;
                //除了总分之外是否为空的标志
                boolean scoreFlage;
                for (int j = 0; j < physicalNumberOfRows; j++) {//5. 跳过标题行
                    fullScoreFlage = false;
                    scoreFlage = false;
                    Map<String,String> rowList = new HashMap<>();
                    if (j == 0 || j==1) {
                        continue;//跳过标题行//获得表头，为后续对应位置
                    }//6. 获取行
                    HSSFRow row = sheet.getRow(j);
                    if (row == null) {
                        continue;//防止数据中间有空行
                    }//7. 获取列数
//                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    for (int k = 0; k <Cells; k ++) {
                        HSSFCell cell = row.getCell(k);
                        if(tableNameMap.get(k).equals("总评分")){
                            //活动得分为空并且别的评分项也有空值,不存数据库
                            if(cell == null && scoreFlage){
                                nullRow.add(row.getCell(0).getStringCellValue());
                                fullScoreFlage = true;
                                break;
                                //j是行数
                            }else if(cell == null && !scoreFlage){//专家疏忽，仍然需要存进去
                                rowList.put(tableNameMap.get(k), "0");
                                continue;
                            }
                        }
                        if (cell!=null) {
                            cell.setCellType(CellType.STRING);
                            String cellValue = cell.getStringCellValue();
                            rowList.put(tableNameMap.get(k), cellValue);
                        }else{
                            if(!tableNameMap.get(k).equals("总评分")){
                                scoreFlage = true;
                                rowList.put(tableNameMap.get(k), "0");
                            }
                        }
                    }
                    //专家对这个选手没打分,设置为null
                    if(fullScoreFlage){
                        for (int k = 0; k < scoreItemsByE.size(); k++) {
                            rowList.put(scoreItemsByE.get(k).getName(), null);
                        }
                    }
                    rateList.add(rowList);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Msg.success().add("rateList",rateList).add("nullRow",nullRow);
    }
}
