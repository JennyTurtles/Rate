package org.sys.rate.utils;

import cn.afterturn.easypoi.word.WordExportUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.sys.rate.model.Comment;
import org.sys.rate.model.GradeForm;
import org.sys.rate.model.ScoreItem;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @projectName: Rate
 * @package: org.sys.rate.utils
 * @className: exportWord
 * @author: ZYK
 * @description: TODO
 * @date: 2023/4/28 20:45
 * @version: 1.0
 */
@Service
public class ExportWord {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ExportWord.class);
    private static final String TEMPLATE_PATH = "upload/templete/GradingTable.docx";
    private static final String DEST = "upload/exportFiles/";
    private static final SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
    private static final SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
    private static final String[] GRADELEVELARRAY = {"优", "良", "中", "及格", "不及格"};
    private static final double[] SCORES = {85.0, 75.0, 67.0, 60.0, 0.0};
    private static final int wordTitlePageRowLength = 40;
    private static final int commentMaxLength = 230;
    private static final int commentMinLength = 180;
    private static final int commentMaxRows = 8;
    private static final int commentMinRows = 4;
    private static final int perRowMaxCount = 30 * 2;
    private boolean necessaryFilesAndDirectoriesExist;

    public ExportWord() {
        this.necessaryFilesAndDirectoriesExist = checkIfNecessaryFilesAndDirectoriesExist();
    }

    private boolean checkIfNecessaryFilesAndDirectoriesExist() {
        // 检查目录是否存在
        File directory = new File(DEST);
        if (!directory.exists()) {
            boolean result = directory.mkdirs();
            if (result) {
                logger.info("目录 " + DEST + " 创建成功！");
            } else {
                logger.error("目录 " + DEST + " 创建失败！");
                return false;
            }
        }
        // 检查TEMPLATE_PATH是否存在
        File file10 = new File(TEMPLATE_PATH);
        if (!file10.exists()) {
            logger.error("模版文件 " + TEMPLATE_PATH + " 不存在！！！");
            return false;
        }
        return true;
    }

    public static int calculateChars(String sentence) {
        int count = 0;
        for (int i = 0; i < sentence.length(); i++) {
            char c = sentence.charAt(i);
            if (Character.isDigit(c) || Character.isLowerCase(c) || c == 'I' || c == 'J') {
                // 这里实际上算的字符个数肯定是大于原有的
                count += 1;
            } else {
                count += 2;
            }
        }
        return count;
    }

    // 格式化扉页
    public static String formatTitlePageContent(String s) {
        if (calculateChars(s) >= wordTitlePageRowLength) {
            return s;
        }
        int numSpaces = wordTitlePageRowLength - calculateChars(s);
        String padding = String.join("", Collections.nCopies(numSpaces / 2, " "));
        return String.format("%s%s%s", padding, s, padding + (numSpaces % 2 == 1 ? " " : ""));
    }


    private Map<String, Object> createGeneralModel(GradeForm gradeForm) {
        Map<String, Object> generalModel = new HashMap<>(5);
        int thesisNameCount = calculateChars(gradeForm.getThesisName());
        if (thesisNameCount > wordTitlePageRowLength) {
            generalModel.put("ThesisName", gradeForm.getThesisName().substring(0, wordTitlePageRowLength / 2));
            String padding = String.join("", Collections.nCopies(2*wordTitlePageRowLength-thesisNameCount, " "));
            generalModel.put("AdditionalName", String.format("%s%s", gradeForm.getThesisName().substring(wordTitlePageRowLength / 2), padding));
        } else {
            generalModel.put("ThesisName", formatTitlePageContent(gradeForm.getThesisName()));
        }
        generalModel.put("Major", formatTitlePageContent(gradeForm.getSpecialty()));
        generalModel.put("Class", formatTitlePageContent(gradeForm.getClassName()));
        generalModel.put("StuName", formatTitlePageContent(gradeForm.getName()));
        generalModel.put("StuID", formatTitlePageContent(gradeForm.getStuNumber()));
        return generalModel;
    }

    private static String countRows(String comment, int commentRows) {
        int rows = 0, count = 0;
        comment = comment.trim().replace("\n\n", "\n");
        for (int i = 0; i < comment.length(); i++) {
            if (count > perRowMaxCount || comment.charAt(i) == '\n') {
                count = 2;
                rows++;
            } else {
                char c = comment.charAt(i);
                if (Character.isDigit(c) || Character.isLowerCase(c) || c == 'I' || c == 'J') {
                    // 这里实际上算的字符个数肯定是大于原有的
                    count += 1;
                } else {
                    count += 2;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(comment);
        for (int i = 0; i < commentRows - rows; i++) {
            sb.append(" \r\n");
        }

        return rows <= commentRows ? sb.toString() : sb.toString().replace("\n", "");
    }

    // 格式化comment
    public static String formatComment(String comment, int commentLength, int commentRows) {
        return countRows(comment, commentRows);
    }

    private Map<String, Object> createCommentModel(Map<Integer, List<Comment>> comments) {
        Map<String, Object> commentModel = new HashMap<>();

        if (comments.isEmpty() || comments.size() == 0) {
            return commentModel;
        }

        List<Comment> instructors = comments.get(GradeForm.Type.INSTRUCTOR.ordinal());
        if (instructors.size() > 0) {
            commentModel.put("instructorComment", formatComment(instructors.get(0).getContent(), commentMaxLength, commentMaxRows));
            Date instructorDate = instructors.get(0).getDate();
            commentModel.put("instructorYear", sdfYear.format(instructorDate));
            commentModel.put("instructorMonth", sdfMonth.format(instructorDate));
            commentModel.put("instructorDay", sdfDay.format(instructorDate));
        }
        List<Comment> reviewers = comments.get(GradeForm.Type.REVIEWER.ordinal());
        if (reviewers.size() > 0) {
            commentModel.put("reviewerComment", formatComment(reviewers.get(0).getContent(), commentMaxLength, commentMaxRows));
            Date reviewDate = reviewers.get(0).getDate();
            commentModel.put("reviewerYear", sdfYear.format(reviewDate));
            commentModel.put("reviewerMonth", sdfMonth.format(reviewDate));
            commentModel.put("reviewerDay", sdfDay.format(reviewDate));
        }
        List<Comment> leaders = comments.get(GradeForm.Type.DEFENSE.ordinal());
        if (leaders.size() > 0) {
            commentModel.put("leaderComment", formatComment(leaders.get(0).getContent(), commentMinLength, commentMinRows));
            Date leaderDate = leaders.get(0).getDate();
            commentModel.put("leaderYear", sdfYear.format(leaderDate));
            commentModel.put("leaderMonth", sdfMonth.format(leaderDate));
            commentModel.put("leaderDay", sdfDay.format(leaderDate));
        }
        return commentModel;
    }

    private Map<String, Object> createScoreModel(Map<Integer, List<ScoreItem>> scoreItems) {
        Map<String, Object> scoreModel = new HashMap<>();

        if (scoreItems.isEmpty() || scoreItems.size() == 0) {
            return scoreModel;
        }

        int gradeLevelIndex = 0;
        double tmpCoef = 0.0;
        double tmpScore = 0.0;
        double tmpScoreCoef = 0.0;
        double tmpScoreCoefSum = 0.0;
        DecimalFormat df = new DecimalFormat("#.00");
        String suffixShort = "";
        String suffixLong = "";

        int typeInstructor = GradeForm.Type.INSTRUCTOR.ordinal();
        List<ScoreItem> instructors = scoreItems.get(typeInstructor);
        if (!instructors.isEmpty()) {
            for (int i = 0; i < instructors.size(); i++) {
                tmpCoef = instructors.get(i).getCoef();
                tmpScore = instructors.get(i).getScore();
                suffixShort = String.valueOf(typeInstructor) + String.valueOf(i);
                // 获得coef
                scoreModel.put("coef" + suffixShort, tmpCoef);
                // 计算单项折合后分数
                tmpScoreCoef = tmpCoef * tmpScore;
                scoreModel.put("scoreCoef" + suffixShort, df.format(tmpScoreCoef));
                // 为分数挑一个位置
                gradeLevelIndex = getGradeLevelIndex(tmpScore);
                suffixLong = suffixShort + String.valueOf(gradeLevelIndex);
                scoreModel.put("s" + suffixLong, tmpScore);
                // 计算总分
                tmpScoreCoefSum += tmpScoreCoef;
            }
        }

        int typeReviewers = GradeForm.Type.REVIEWER.ordinal();
        List<ScoreItem> reviewers = scoreItems.get(typeReviewers);
        if (!reviewers.isEmpty()) {
            for (int i = 0; i < reviewers.size(); i++) {
                tmpCoef = reviewers.get(i).getCoef();
                tmpScore = reviewers.get(i).getScore();
                suffixShort = String.valueOf(typeReviewers) + String.valueOf(i);
                // 获得coef
                scoreModel.put("coef" + suffixShort, tmpCoef);
                // 计算单项折合后分数
                tmpScoreCoef = tmpCoef * tmpScore;
                scoreModel.put("scoreCoef" + suffixShort, df.format(tmpScoreCoef));
                // 为分数挑一个位置
                gradeLevelIndex = getGradeLevelIndex(tmpScore);
                suffixLong = suffixShort + String.valueOf(gradeLevelIndex);
                scoreModel.put("s" + suffixLong, tmpScore);
                // 计算总分
                tmpScoreCoefSum += tmpScoreCoef;
            }
        }

        int typeLeader = GradeForm.Type.DEFENSE.ordinal();
        List<ScoreItem> leaders = scoreItems.get(typeLeader);
        if (!leaders.isEmpty()) {
            for (int i = 0; i < leaders.size(); i++) {
                tmpCoef = leaders.get(i).getCoef();
                tmpScore = leaders.get(i).getScore();
                suffixShort = String.valueOf(typeLeader) + String.valueOf(i);
                // 获得coef
                scoreModel.put("coef" + suffixShort, tmpCoef);
                // 计算单项折合后分数
                tmpScoreCoef = tmpCoef * tmpScore;
                scoreModel.put("scoreCoef" + suffixShort, df.format(tmpScoreCoef));
                // 为分数挑一个位置
                gradeLevelIndex = getGradeLevelIndex(tmpScore);
                suffixLong = suffixShort + String.valueOf(gradeLevelIndex);
                scoreModel.put("s" + suffixLong, tmpScore);
                // 计算总分
                tmpScoreCoefSum += tmpScoreCoef;
            }
        }

        scoreModel.put("totalSum", tmpScoreCoefSum);
        scoreModel.put("gradeLevel", getGradeLevel(tmpScoreCoefSum));

        return scoreModel;
    }

    public static String getGradeLevel(double score) {
        for (int i = 0; i < SCORES.length; i++) {
            if (score >= SCORES[i]) {
                return GRADELEVELARRAY[i];
            }
        }
        logger.error("you got wrong score!");
        return GRADELEVELARRAY[4];
    }

    public static int getGradeLevelIndex(double score) {
        for (int i = 0; i < SCORES.length; i++) {
            if (score >= SCORES[i]) {
                return i;
            }
        }
        logger.error("you got wrong score!");
        return 4;
    }

    public void getDownload(HttpServletResponse response, String filePath, boolean isOnLine) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            // 判断文件是否存在
            throw new Exception("文件不存在！");
        }
        String fileName = "成绩评定表.zip";
        InputStream ins = new FileInputStream(filePath);
        OutputStream os = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int len = 0;
        response.reset();
        if (isOnLine) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
        } else {
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        }
        while ((len = ins.read(buffer)) > 0) {
            os.write(buffer, 0, len);
        }
        os.flush();
        os.close();
        ins.close();
    }

    public boolean generateListWord(HttpServletResponse response, List<GradeForm> gradeForms) throws Exception {
        if (!necessaryFilesAndDirectoriesExist) {
            return false;
        }

        String tmpDirName = DEST + UUID.randomUUID().toString();
//        logger.info("成绩评定表文件夹正在导出：" + tmpDirName);
        File tmpDir = new File(tmpDirName);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }

        List<File> fileList = new ArrayList<>();

        for (GradeForm gradeForm : gradeForms) {
            Map<String, Object> params = new HashMap<>();
            params.putAll(createGeneralModel(gradeForm));
            params.putAll(createCommentModel(gradeForm.getComments()));
            params.putAll(createScoreModel(gradeForm.getScoreItems()));

            String tmpFileName = tmpDirName + "/" + gradeForm.getName() + "成绩评定表.docx";
            XWPFDocument doc = null;
            FileOutputStream fos = null;
            try {
                doc = WordExportUtil.exportWord07(TEMPLATE_PATH, params);
                fos = new FileOutputStream(tmpFileName);
                doc.write(fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    fos.close();
                }
                if (doc != null) {
                    doc.close();
                }
            }

            fileList.add(new File(tmpFileName));
        }

        String zipFileName = DEST + UUID.randomUUID() + ".zip";
        File zipFile = compressFiles(fileList, zipFileName);
        getDownload(response, zipFileName, false);
//        File directory = new File(DEST);
//        deleteDir(directory);
        return true;
    }

    public static File compressFiles(List<File> fileList, String zipFileName) {
        File zipFile = new File(zipFileName);
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (File file : fileList) {
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(zipEntry);

                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }

                fileInputStream.close();
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipFile;
    }

    public static void deleteDir(File dir) {
        if (!dir.exists()) {
            return;
        }
        if (dir.isFile()) {
            dir.delete();
            return;
        }
        File[] subFiles = dir.listFiles();
        for (File sub : subFiles) {
            deleteDir(sub);
        }
        dir.delete();
    }

}
