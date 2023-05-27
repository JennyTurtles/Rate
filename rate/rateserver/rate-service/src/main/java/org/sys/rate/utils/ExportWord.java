package org.sys.rate.utils;

import cn.afterturn.easypoi.word.WordExportUtil;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.sys.rate.model.Comment;
import org.sys.rate.model.GradeForm;
import org.sys.rate.model.ScoreItem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
//    private static final String TEMPLATE_PATH = "rate/rateserver/rate-service/src/main/java/org/sys/rate/utils/GradingTable.docx";
    private static final String TEMPLATE_PATH = "upload/templete/GradingTable.docx";
//    private static final String DEST = "upload/exportFiles/";
    private static final SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
    private static final SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
    private static final String[] GRADELEVELARRAY = {"优", "良", "中", "及格", "不及格"};
    private static final double[] SCORES = {85.0, 75.0, 67.0, 60.0, 0.0};
    private static final int wordTitlePageRowLength = 40;
    private static final int[] maxWordsCountPerRow = {30, 30, 35, 40, 45, 50}; // 分别对应12号字体22磅，12号字体单倍，11号字体单倍，10号字体，9号字体，8号字体
    private static final int[] commentRowslimit = {11, 16}; // 对应22磅行距可使用的行数，单倍行距可使用的行数
    private static final int[] commentRowslimitLeader = {7, 9}; // 对应答辩组长，22磅行距可使用的行数，单倍行距可使用的行数
    private static final String[] commentStart = {"指导教师评语", "评阅教师评语", "答辩评语"};
    private static final String[] commentEnd = {"指导教师（签名）", "评阅教师（签名）", "答辩小组组长（签名）"};
    private int[] commentFontSize = {12, 12, 12};
    private boolean necessaryFilesAndDirectoriesExist;

    public ExportWord() {
        this.necessaryFilesAndDirectoriesExist = checkIfNecessaryFilesAndDirectoriesExist();
    }

    private boolean checkIfNecessaryFilesAndDirectoriesExist() {
//        // 检查目录是否存在
//        File directory = new File(DEST);
//        if (!directory.exists()) {
//            boolean result = directory.mkdirs();
//            if (result) {
//                logger.info("目录 " + DEST + " 创建成功！");
//            } else {
//                logger.error("目录 " + DEST + " 创建失败！");
//                return false;
//            }
//        }
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
//        int thesisNameCount = calculateChars(gradeForm.getThesisName());
//        if (thesisNameCount > wordTitlePageRowLength) {
//            generalModel.put("ThesisName", gradeForm.getThesisName().isEmpty()?"":gradeForm.getThesisName().substring(0, wordTitlePageRowLength / 2));
//            String padding = String.join("", Collections.nCopies(2 * wordTitlePageRowLength - thesisNameCount, " "));
//            generalModel.put("AdditionalName", String.format("%s%s", gradeForm.getThesisName().substring(wordTitlePageRowLength / 2), padding));
//        } else {
//            generalModel.put("ThesisName", gradeForm.getThesisName().isEmpty()?"":formatTitlePageContent(gradeForm.getThesisName()));
//        }
        generalModel.put("ThesisName", formatTitlePageContent(" "));
        generalModel.put("Major", gradeForm.getSpecialty() == null ? "":formatTitlePageContent(gradeForm.getSpecialty()));
        generalModel.put("Class",gradeForm.getClassName() == null? "":formatTitlePageContent(gradeForm.getClassName()));
        generalModel.put("StuName", gradeForm.getName() == null? "":formatTitlePageContent(gradeForm.getName()));
        generalModel.put("StuID", gradeForm.getStuNumber() == null? "":formatTitlePageContent(gradeForm.getStuNumber()));
        return generalModel;
    }

    // 格式化评语项

    // 1. 获取评论在word中显示的行数
    private int getCommentRowsInWord(String comment, int maxNumsPerRow) {
        int rows = 0, count = 0;
        maxNumsPerRow = maxNumsPerRow << 1;
        for (int i = 0; i < comment.length(); i++) {
            if (count > maxNumsPerRow || comment.charAt(i) == '\n') {
                count = 4;
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
        return rows;
    }

    // 2. 选择合适的字体大小
    // so, there is a trick, because you have 2 type of 12 font size, so I make when font size = 12 && spaceline = 22, font size = 13
    private int chooseProperFontSize(String comment, boolean isLeaderComment) {
        //  when font size = 12 && spaceline = 22, set font size = 13
        int maxRows = isLeaderComment ? commentRowslimitLeader[0] : commentRowslimit[0];
        if (getCommentRowsInWord(comment, maxWordsCountPerRow[0]) <= maxRows) {
            return 13;
        }

        maxRows = isLeaderComment ? commentRowslimitLeader[1] : commentRowslimit[1];
        for (int i = 1; i < maxWordsCountPerRow.length; ++i) {
            if (getCommentRowsInWord(comment, maxWordsCountPerRow[i]) <= maxRows) {
                return 13 - i;
            }
        }
        return 7;
    }


    // 格式化comment
    private String formatComment(String comment, int fontSize, boolean isLeaderComment) {
        comment = comment.replaceAll("\r?\n[ ]*", "\r\n    ").replaceAll("\n\n", "\n").trim();
        // 加上空白行
        int maxRows = isLeaderComment ? commentRowslimitLeader[fontSize == 13 ? 0 : 1] : commentRowslimit[fontSize == 13 ? 0 : 1];
        int blankRows = maxRows - getCommentRowsInWord(comment, fontSize == 7 ? 52 : maxWordsCountPerRow[13 - fontSize]) - 1;
        for (int i = 0; i < blankRows; i++) {
            comment += "\r\n ";
        }
        return comment;
    }


    private Map<String, Object> createCommentModel(Map<Integer, List<Comment>> comments) {
        Map<String, Object> commentModel = new HashMap<>();

        if (comments.isEmpty()) {
            return commentModel;
        }

        List<Comment> instructors = comments.get(GradeForm.Type.INSTRUCTOR.ordinal());
        if (instructors.size() > 0) {
            commentFontSize[0] = chooseProperFontSize(instructors.get(0).getContent(), false);
            commentModel.put("instructorComment", formatComment(instructors.get(0).getContent(), commentFontSize[0], false));
            Date instructorDate = instructors.get(0).getDate();
            commentModel.put("instructorYear", sdfYear.format(instructorDate));
            commentModel.put("instructorMonth", sdfMonth.format(instructorDate));
            commentModel.put("instructorDay", sdfDay.format(instructorDate));
        }
        List<Comment> reviewers = comments.get(GradeForm.Type.REVIEWER.ordinal());
        if (reviewers.size() > 0) {
            commentFontSize[1] = chooseProperFontSize(reviewers.get(0).getContent(), false);
            commentModel.put("reviewerComment", formatComment(reviewers.get(0).getContent(), commentFontSize[1], false));
            Date reviewDate = reviewers.get(0).getDate();
            commentModel.put("reviewerYear", sdfYear.format(reviewDate));
            commentModel.put("reviewerMonth", sdfMonth.format(reviewDate));
            commentModel.put("reviewerDay", sdfDay.format(reviewDate));
        }
        List<Comment> leaders = comments.get(GradeForm.Type.DEFENSE.ordinal());
        int leaderIndex = 0;
        for (int i = 0; i < leaders.size(); ++i) {
            if (leaders.get(i).getRole().equals("组长")) {
                leaderIndex = i;
            }
        }
        if (leaders.size() > 0) {
            commentFontSize[2] = chooseProperFontSize(leaders.get(0).getContent(), false);
            commentModel.put("leaderComment", formatComment(leaders.get(leaderIndex).getContent(), commentFontSize[2], true));
            Date leaderDate = leaders.get(0).getDate();
            commentModel.put("leaderYear", sdfYear.format(leaderDate));
            commentModel.put("leaderMonth", sdfMonth.format(leaderDate));
            commentModel.put("leaderDay", sdfDay.format(leaderDate));
        }
        return commentModel;
    }

    private Map<String, Object> createScoreModel(Map<Integer, List<ScoreItem>> scoreItems) {
        Map<String, Object> scoreModel = new HashMap<>();

        if (scoreItems.isEmpty()) {
            return scoreModel;
        }

        int gradeLevelIndex;
        double tmpCoef;
        double tmpScore;
        double tmpScoreCoef;
        double tmpScoreCoefSum = 0.0;
        DecimalFormat df = new DecimalFormat("#.00");
        String suffixShort;
        String suffixLong;

        int typeInstructor = GradeForm.Type.INSTRUCTOR.ordinal();
        List<ScoreItem> instructors = scoreItems.get(typeInstructor);
        if (instructors != null) {
            for (int i = 0; i < instructors.size(); i++) {
                tmpCoef = instructors.get(i).getCoef();
                if (instructors.get(i).getScore() == null)
                    tmpScore = 0.0;
                else
                    tmpScore = instructors.get(i).getScore();
                suffixShort = typeInstructor + String.valueOf(i);
                // 获得coef
                scoreModel.put("coef" + suffixShort, tmpCoef);
                // 计算单项折合后分数
                tmpScoreCoef = tmpCoef * tmpScore;
                scoreModel.put("scoreCoef" + suffixShort, df.format(tmpScoreCoef));
                // 为分数挑一个位置
                gradeLevelIndex = getGradeLevelIndex(tmpScore);
                suffixLong = suffixShort + gradeLevelIndex;
                scoreModel.put("s" + suffixLong, tmpScore);
                // 计算总分
                tmpScoreCoefSum += tmpScoreCoef;
            }
        }

        int typeReviewers = GradeForm.Type.REVIEWER.ordinal();
        List<ScoreItem> reviewers = scoreItems.get(typeReviewers);
        if (reviewers != null) {
            for (int i = 0; i < reviewers.size(); i++) {
                tmpCoef = reviewers.get(i).getCoef();
                if (reviewers.get(i).getScore() == null)
                    tmpScore = 0.0;
                else
                    tmpScore = reviewers.get(i).getScore();
                suffixShort = typeReviewers + String.valueOf(i);
                // 获得coef
                scoreModel.put("coef" + suffixShort, tmpCoef);
                // 计算单项折合后分数
                tmpScoreCoef = tmpCoef * tmpScore;
                scoreModel.put("scoreCoef" + suffixShort, df.format(tmpScoreCoef));
                // 为分数挑一个位置
                gradeLevelIndex = getGradeLevelIndex(tmpScore);
                suffixLong = suffixShort + gradeLevelIndex;
                scoreModel.put("s" + suffixLong, tmpScore);
                // 计算总分
                tmpScoreCoefSum += tmpScoreCoef;
            }
        }

        int typeLeader = GradeForm.Type.DEFENSE.ordinal();
        List<ScoreItem> leaders = scoreItems.get(typeLeader);
        if (leaders != null) {
            for (int i = 0; i < leaders.size(); i++) {
                tmpCoef = leaders.get(i).getCoef();
                if (leaders.get(i).getScore() == null)
                    tmpScore = 0.0;
                else
                    tmpScore = leaders.get(i).getScore();
                suffixShort = typeLeader + String.valueOf(i);
                // 获得coef
                scoreModel.put("coef" + suffixShort, tmpCoef);
                // 计算单项折合后分数
                tmpScoreCoef = tmpCoef * tmpScore;
                scoreModel.put("scoreCoef" + suffixShort, df.format(tmpScoreCoef));
                // 为分数挑一个位置
                gradeLevelIndex = getGradeLevelIndex(tmpScore);
                suffixLong = suffixShort + gradeLevelIndex;
                scoreModel.put("s" + suffixLong, tmpScore);
                // 计算总分
                tmpScoreCoefSum += tmpScoreCoef;
            }
        }

        scoreModel.put("totalSum", df.format(tmpScoreCoefSum));
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

    public byte[] generateListWord(List<GradeForm> gradeForms) throws Exception {
        if (!necessaryFilesAndDirectoriesExist) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        for (GradeForm gradeForm : gradeForms) {
            Map<String, Object> params = new HashMap<>();
            // 对gradeForm进行排序，将role="组长"
            params.putAll(createGeneralModel(gradeForm));
            params.putAll(createCommentModel(gradeForm.getComments()));
            params.putAll(createScoreModel(gradeForm.getScoreItems()));

            // 获取字号是否需要改变；
            boolean isChangeInstCommentFont = (commentFontSize[0] != 12);
            boolean isChangeReviewerCommentFont = (commentFontSize[1] != 12);
            boolean isChangeLeaderCommentFont = (commentFontSize[2] != 12);

            boolean[] inRange = {false, false, false};
            boolean changeLineSpace = false;

            try (XWPFDocument doc = WordExportUtil.exportWord07(TEMPLATE_PATH, params)) {
                // change the size of comment
                if (isChangeInstCommentFont || isChangeReviewerCommentFont || isChangeLeaderCommentFont) {
                    loop:
                    for (XWPFTable table : doc.getTables()) {
                        for (XWPFTableRow row : table.getRows()) {
                            for (XWPFTableCell cell : row.getTableCells()) {
                                for (XWPFParagraph para : cell.getParagraphs()) {
                                    for (XWPFRun run : para.getRuns()) {
                                        String text = run.getText(0);

                                        if (text == null) {
                                            continue;
                                        }
                                        for (int i = 0; i < commentStart.length; ++i) {
                                            if (text.contains(commentStart[i])) {
                                                inRange[i] = true;
                                                if (commentFontSize[i] < 13) {
                                                    changeLineSpace = true;
                                                    para.setSpacingBetween(1, LineSpacingRule.AUTO);
                                                }
                                            } else if (inRange[i] && text.contains(commentEnd[i])) {
                                                inRange[i] = false;
                                                changeLineSpace = false;
                                                break;
                                            } else if (inRange[i] && !text.contains(commentEnd[i])) {
                                                run.setFontSize(commentFontSize[i] == 13 ? 12 : commentFontSize[i]);
                                                break;
                                            }
                                            if (text.contains(commentEnd[2])) {
                                                break loop;
                                            }
                                        }

                                    }
                                    if (changeLineSpace) {
                                        para.setSpacingBetween(1, LineSpacingRule.AUTO);
                                    }
                                }
                            }
                        }
                    }
                }


                ByteArrayOutputStream out = new ByteArrayOutputStream();
                doc.write(out); // 将word写入byte流
                byte[] xwpfDocumentBytes = out.toByteArray();
                ByteArrayInputStream bis = new ByteArrayInputStream(xwpfDocumentBytes);

//                ZipEntry zipEntry = new ZipEntry(gradeForm.getStuNumber() + gradeForm.getName() +
//                         "成绩评定表.docx");
                ZipEntry zipEntry = new ZipEntry(gradeForm.getName() +
                        "成绩评定表.docx");
                zos.putNextEntry(zipEntry);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = bis.read(buffer)) >= 0) {
                    zos.write(buffer, 0, length);
                }
                out.close();
                zos.closeEntry();
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        zos.close();
        byte[] bytes = baos.toByteArray();
        baos.close();
        return bytes;
    }


}
