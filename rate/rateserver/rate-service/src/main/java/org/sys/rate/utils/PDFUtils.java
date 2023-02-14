package org.sys.rate.utils;


import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Document;


import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.sys.rate.model.*;


import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.Image;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFUtils {
    /**
     * @作者 cx
     * @时间 2022-08-18 15:02
     */

    public static void ExportPDF(HttpServletResponse response, Map<String, Object> model)
            throws Exception {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.addTitle("评分表");
            document.addAuthor("scurry");
            document.addSubject("product sheet.");
            document.addKeywords("product.");
            document.open();
            // Title
            List<Activities> Act = (List<Activities>) model.get("Act");
            String title = Act.get(0).getName()+"评分表";
            Paragraph Title = createTitle(title);
            document.add(Title);
            // 表格
            // 表头
            List<ScoreItem> scoreItems = (List<ScoreItem>) model.get("scoreItems");
            List<InfoItem> infoItemsShow =(List<InfoItem>) model.get("infoItemsShow");
            List<String> tableHeadList = tableHead (scoreItems, infoItemsShow) ;

            PdfPTable dataTable = getPdfPTable01(3 + infoItemsShow.size()+scoreItems.size(),600) ;

            for(String HeadList : tableHeadList){
                dataTable.addCell(createCell(HeadList));
            }

            List<Participates> participatesL = (List<Participates>) model.get("participatesL");
            List<Scores> scoresList =(List<Scores>) model.get("scoresList");
            List<Infos> infosList =(List<Infos>) model.get("infosList");

            List<String[]> tableDataList = getTableData (participatesL, scoresList, scoreItems, infoItemsShow, infosList) ;

            for (String[] tableData : tableDataList) {
                for (String content:tableData) {
                    dataTable.addCell(createCellContent(content));
                }

            }
            document.add((Element) dataTable);

            // Chapter 1
            String name = (String) model.get("name");
            document.add(createChapterH1("签名："+name));
            Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int date = c.get(Calendar.DATE);
            String nowDate = year + "年" + (month+1) + "月" + date + "日";
            document.add(createChapterH1("日期："+nowDate));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private static List<String> tableHead ( List<ScoreItem> scoreItems, List<InfoItem> infoItemsShow){
        List<String> tableHeadList = new ArrayList<>() ;
        tableHeadList.add("序号") ;
        tableHeadList.add("编号") ;
        tableHeadList.add("姓名") ;
        for(int i=0;i<infoItemsShow.size();i++){
            InfoItem Iitem = infoItemsShow.get(i);
            tableHeadList.add(Iitem.getName()) ;
        }
        //遍历评分项
        for(int i=0;i<scoreItems.size();i++){
            ScoreItem sitem = scoreItems.get(i);
            tableHeadList.add(sitem.getName());
        }
        return tableHeadList ;
    }
    private static List<String[]> getTableData ( List<Participates> participatesL, List<Scores> scoresList, List<ScoreItem> scoreItems, List<InfoItem> infoItemsShow, List<Infos> infosList){
        List<String[]> tableDataList = new ArrayList<>() ;
        for (int i = 0; i < participatesL.size(); i++) {
            String[] tableData = new String[3 + infoItemsShow.size()+scoreItems.size()];
            Participates emp = participatesL.get(i);
            Student stu = participatesL.get(i).getStudent();
            tableData[0] = emp.getDisplaySequence() + "";
            tableData[1] = emp.getCode() + "";
            tableData[2] = stu.getName() + "";

            for(int j=0;j<infoItemsShow.size();j++) {
                InfoItem Iitem = infoItemsShow.get(j);
                for (Infos infoL : infosList) {
                    if (Iitem.getID().equals(infoL.getInfoItemID()) && infoL.getParticipantID().equals(emp.getID())) {
                        if (infoL.getContent()!=null) tableData[3+j] = infoL.getContent()+"";
                        break;
                    }
                }
            }
            for(int j=0;j<scoreItems.size();j++){
                ScoreItem sitem = scoreItems.get(j);
                for (Scores scoreL : scoresList) {
                    if (scoreL.getScoreItemID().equals(sitem.getId()) && scoreL.getParticipantID().equals(emp.getID())) {
                        if (scoreL.getScore()!=null) tableData[3+infoItemsShow.size()+j] = scoreL.getScore() + "";
                        break;
                    }
                }
            }
            tableDataList.add(tableData) ;
        }
        return tableDataList ;
    }


    private static Paragraph createTitle(String content) throws IOException, DocumentException {
        Font font = new Font(getBaseFont(), 22, Font.BOLD);
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }

    public static Paragraph getParagraph (String content, Font font,Integer alignment){
        Paragraph paragraph = new Paragraph(content,font) ;
        if (alignment != null && alignment >= 0){
            paragraph.setAlignment(alignment);
        }
        return paragraph ;
    }

    public static PdfPTable getPdfPTable01 (int numColumns,float totalWidth){
        // 表格处理
        PdfPTable table = new PdfPTable(numColumns);
        // 设置表格宽度比例为%100
        table.setWidthPercentage(100);
        // 设置宽度:宽度平均
        table.setTotalWidth(totalWidth);
        // 锁住宽度
        table.setLockedWidth(false);
        // 设置表格上面空白宽度
        table.setSpacingBefore(10f);
        // 设置表格下面空白宽度
        table.setSpacingAfter(10f);
        // 设置表格默认为无边框
//        table.getDefaultCell().setBorder(1);
        table.setPaddingTop(50);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

//        table.setSplitLate(false);
        return table ;
    }

    private static Paragraph createChapterH1(String content) throws IOException, DocumentException {
        Font font = new Font(getBaseFont(), 14, Font.BOLD);
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        return paragraph;
    }


    public static PdfPCell createCell(String content) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        Font font = new Font(getBaseFont(), 11, Font.NORMAL);
        cell.setPhrase(new Phrase(content, font));
        return cell;
    }

    public static PdfPCell createCellContent(String content) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        Font font = new Font(getBaseFont(), 10, Font.NORMAL);
        cell.setPhrase(new Phrase(content, font));
        return cell;
    }

    private static BaseFont getBaseFont() throws IOException, DocumentException {
        return BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
    }
}
