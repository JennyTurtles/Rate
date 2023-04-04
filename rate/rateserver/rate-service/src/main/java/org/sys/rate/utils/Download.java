package org.sys.rate.utils;


import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;

@Service
public class Download {
    public void getDownload(HttpServletResponse response, String filePath, boolean isOnLine) throws Exception {
        File f = new File(filePath);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;

        response.reset(); // 非常重要
        String filename = f.getName().replace(" ","");
        if (isOnLine) { // 在线打开方式
            URL u = new URL("file:///" + filePath);
            response.setContentType(u.openConnection().getContentType());
            response.setHeader("Content-Disposition", "inline; filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
            // 文件名应该编码成UTF-8
        } else { // 纯下载方式
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0)
            out.write(buf, 0, len);
        br.close();
        out.close();
        deleteAllFiles("upload/paperComment");
    }

    public void deleteAllFiles(String path){
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    public void removePageFromPDF(String path, String tempPath, int page) throws Exception {
        PdfReader reader = new PdfReader(path);
        File tmpNewFile = new File(tempPath);
        FileOutputStream fos = new FileOutputStream(tmpNewFile);
        com.itextpdf.text.Document d = new com.itextpdf.text.Document();
        PdfCopy copy = new PdfCopy(d, fos);
        d.open();
        for (int i = 1; i <= page; ++i) {
            copy.addPage(copy.getImportedPage(reader, i));
        }
        copy.freeReader(reader);
        reader.close();
        d.close();
        fos.close();
    }

    public String adaptRows(String origin, int ROWSLIMIT) {
        int rows = 0, count = 0;
        // 0.清理首尾的空白符和\n && 清理多余的\n
        origin = origin.trim();
        origin = origin.replace("\n\n", "\n");
        // 1.统计有多少行数
        for (int i = 0; i < origin.length(); ++i) {
            if (count > 35 || origin.charAt(i) == '\n') {
                count = 0;
                rows++;
            } else {
                count++;
            }
        }
        ++rows;
        // 2.返回修改后的字符串
        if (rows <= ROWSLIMIT) {
            return origin;
        } else {
            return origin.replace("\n", "");
        }
    }

}