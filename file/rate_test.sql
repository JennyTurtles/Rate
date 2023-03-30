/*
 Navicat Premium Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 80030 (8.0.30)
 Source Host           : localhost:3306
 Source Schema         : rate_test

 Target Server Type    : MySQL
 Target Server Version : 80030 (8.0.30)
 File Encoding         : 65001

 Date: 30/03/2023 21:19:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activities
-- ----------------------------
DROP TABLE IF EXISTS `activities`;
CREATE TABLE `activities` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(60) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `scoreItemCount` int DEFAULT NULL,
  `score` double(10,2) unsigned DEFAULT NULL,
  `groupCount` int unsigned DEFAULT NULL,
  `expertCount` int unsigned DEFAULT NULL,
  `participantCount` int unsigned DEFAULT NULL,
  `comment` varchar(2000) DEFAULT NULL,
  `deleteFlag` tinyint unsigned NOT NULL,
  `institutionID` int DEFAULT NULL,
  `status` varchar(10) NOT NULL DEFAULT 'open',
  `parent` int DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `institutionID` (`institutionID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of activities
-- ----------------------------
BEGIN;
INSERT INTO `activities` (`ID`, `name`, `startDate`, `scoreItemCount`, `score`, `groupCount`, `expertCount`, `participantCount`, `comment`, `deleteFlag`, `institutionID`, `status`, `parent`) VALUES (34, '2023年计算机学院硕士研究生招生复试', '2023-03-28 18:18:10', 5, 120.00, 7, 8, 148, '面试总分120分，少于72分自动淘汰。可以调整表格各列的宽度，使得不出现横向滚动条。', 0, 1, 'open', NULL);
INSERT INTO `activities` (`ID`, `name`, `startDate`, `scoreItemCount`, `score`, `groupCount`, `expertCount`, `participantCount`, `comment`, `deleteFlag`, `institutionID`, `status`, `parent`) VALUES (35, '1', '2023-03-26 13:20:00', 0, 120.00, 0, 0, 0, '面试总分120分，少于72分自动淘汰。可以调整表格各列的宽度，使得不出现横向滚动条。', 0, 1, 'open', NULL);
INSERT INTO `activities` (`ID`, `name`, `startDate`, `scoreItemCount`, `score`, `groupCount`, `expertCount`, `participantCount`, `comment`, `deleteFlag`, `institutionID`, `status`, `parent`) VALUES (36, '2', '2023-03-28 12:01:00', 0, 120.00, 0, 0, 0, '面试总分120分，少于72分自动淘汰。可以调整表格各列的宽度，使得不出现横向滚动条。', 0, 1, 'open', NULL);
INSERT INTO `activities` (`ID`, `name`, `startDate`, `scoreItemCount`, `score`, `groupCount`, `expertCount`, `participantCount`, `comment`, `deleteFlag`, `institutionID`, `status`, `parent`) VALUES (37, 's', '2023-03-27 00:15:00', 0, 0.00, 0, 0, 0, '活动备注example：关于xxx的活动', 0, 1, 'open', NULL);
COMMIT;

-- ----------------------------
-- Table structure for adjustsalary
-- ----------------------------
DROP TABLE IF EXISTS `adjustsalary`;
CREATE TABLE `adjustsalary` (
  `id` int NOT NULL AUTO_INCREMENT,
  `eid` int DEFAULT NULL,
  `asDate` date DEFAULT NULL COMMENT '调薪日期',
  `beforeSalary` int DEFAULT NULL COMMENT '调前薪资',
  `afterSalary` int DEFAULT NULL COMMENT '调后薪资',
  `reason` varchar(255) DEFAULT NULL COMMENT '调薪原因',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `pid` (`eid`),
  CONSTRAINT `adjustsalary_ibfk_1` FOREIGN KEY (`eid`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of adjustsalary
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `institutionID` int NOT NULL COMMENT '外键',
  `name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `username` varchar(30) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱地址',
  `comment` varchar(200) DEFAULT NULL COMMENT '备注',
  `role` varchar(1) NOT NULL COMMENT '角色',
  `enabled` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`ID`,`username`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE,
  KEY `institutionID` (`institutionID`) USING BTREE,
  KEY `ID` (`ID`) USING BTREE,
  CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`institutionID`) REFERENCES `institution` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
BEGIN;
INSERT INTO `admin` (`ID`, `institutionID`, `name`, `username`, `password`, `phone`, `email`, `comment`, `role`, `enabled`) VALUES (2, 1, '龚三', 'admin', '7c4a8d09ca3762af61e59520943dc26494f8941b', '18568887789', '555@dhu.edu.cn', '123', '1', 1);
COMMIT;

-- ----------------------------
-- Table structure for appraise
-- ----------------------------
DROP TABLE IF EXISTS `appraise`;
CREATE TABLE `appraise` (
  `id` int NOT NULL AUTO_INCREMENT,
  `eid` int DEFAULT NULL,
  `appDate` date DEFAULT NULL COMMENT '考评日期',
  `appResult` varchar(32) DEFAULT NULL COMMENT '考评结果',
  `appContent` varchar(255) DEFAULT NULL COMMENT '考评内容',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `pid` (`eid`),
  CONSTRAINT `appraise_ibfk_1` FOREIGN KEY (`eid`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of appraise
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for award
-- ----------------------------
DROP TABLE IF EXISTS `award`;
CREATE TABLE `award` (
  `ID` int NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `indicatorID` int DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `award` (`indicatorID`) USING BTREE,
  CONSTRAINT `award_ibfk_1` FOREIGN KEY (`indicatorID`) REFERENCES `indicator` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of award
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for awardresult
-- ----------------------------
DROP TABLE IF EXISTS `awardresult`;
CREATE TABLE `awardresult` (
  `ID` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `awardname` varchar(50) NOT NULL,
  `year` int DEFAULT NULL,
  `member` varchar(100) DEFAULT NULL,
  `unit` varchar(100) DEFAULT NULL,
  `total` int DEFAULT NULL,
  `rank` int DEFAULT NULL,
  `awardID` int NOT NULL,
  `state` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `awardID` (`awardID`) USING BTREE,
  CONSTRAINT `awardresult_ibfk_1` FOREIGN KEY (`awardID`) REFERENCES `award` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of awardresult
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for bookresult
-- ----------------------------
DROP TABLE IF EXISTS `bookresult`;
CREATE TABLE `bookresult` (
  `ID` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `unit` varchar(50) DEFAULT NULL,
  `member` varchar(100) DEFAULT NULL,
  `time` varchar(50) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `place` varchar(50) DEFAULT NULL,
  `indicaterID` int NOT NULL,
  `state` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `book` (`indicaterID`) USING BTREE,
  CONSTRAINT `bookresult_ibfk_1` FOREIGN KEY (`indicaterID`) REFERENCES `indicater` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of bookresult
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for decision
-- ----------------------------
DROP TABLE IF EXISTS `decision`;
CREATE TABLE `decision` (
  `ID` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `indicatorID` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `decision` (`indicatorID`) USING BTREE,
  CONSTRAINT `decision_ibfk_1` FOREIGN KEY (`indicatorID`) REFERENCES `indicator` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of decision
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL COMMENT '部门名称',
  `parentId` int DEFAULT NULL,
  `depPath` varchar(255) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  `isParent` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of department
-- ----------------------------
BEGIN;
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (1, '股东会', -1, '.1', 1, 1);
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (4, '董事会', 1, '.1.4', 1, 1);
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (5, '总办', 4, '.1.4.5', 1, 1);
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (8, '财务部', 5, '.1.4.5.8', 1, 0);
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (78, '市场部', 5, '.1.4.5.78', 1, 1);
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (81, '华北市场部', 78, '.1.4.5.78.81', 1, 1);
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (82, '华南市场部', 78, '.1.4.5.78.82', 1, 0);
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (85, '石家庄市场部', 81, '.1.4.5.78.81.85', 1, 0);
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (86, '西北市场部', 78, '.1.4.5.78.86', 1, 1);
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (87, '西安市场', 86, '.1.4.5.78.86.87', 1, 1);
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (89, '莲湖区市场', 87, '.1.4.5.78.86.87.89', 1, 0);
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (91, '技术部', 5, '.1.4.5.91', 1, 0);
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (92, '运维部', 5, '.1.4.5.92', 1, 1);
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (93, '运维1部', 92, '.1.4.5.92.93', 1, 0);
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (94, '运维2部', 92, '.1.4.5.92.94', 1, 0);
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (96, 'bbb', 1, '.1.96', 1, 1);
INSERT INTO `department` (`id`, `name`, `parentId`, `depPath`, `enabled`, `isParent`) VALUES (104, '111', 96, '.1.96.104', 1, 0);
COMMIT;

-- ----------------------------
-- Table structure for displayitem
-- ----------------------------
DROP TABLE IF EXISTS `displayitem`;
CREATE TABLE `displayitem` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `activityID` int DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '展示的别名',
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '计算公式',
  `displaySequence` int DEFAULT NULL,
  `fullScore` int DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of displayitem
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '员工编号',
  `name` varchar(10) DEFAULT NULL COMMENT '员工姓名',
  `gender` char(4) DEFAULT NULL COMMENT '性别',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `idCard` char(18) DEFAULT NULL COMMENT '身份证号',
  `wedlock` enum('已婚','未婚','离异') DEFAULT NULL COMMENT '婚姻状况',
  `nationId` int DEFAULT NULL COMMENT '民族',
  `nativePlace` varchar(20) DEFAULT NULL COMMENT '籍贯',
  `politicId` int DEFAULT NULL COMMENT '政治面貌',
  `email` varchar(20) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(11) DEFAULT NULL COMMENT '电话号码',
  `address` varchar(64) DEFAULT NULL COMMENT '联系地址',
  `departmentId` int DEFAULT NULL COMMENT '所属部门',
  `jobLevelId` int DEFAULT NULL COMMENT '职称ID',
  `posId` int DEFAULT NULL COMMENT '职位ID',
  `engageForm` varchar(8) DEFAULT NULL COMMENT '聘用形式',
  `tiptopDegree` enum('博士','硕士','本科','大专','高中','初中','小学','其他') DEFAULT NULL COMMENT '最高学历',
  `specialty` varchar(32) DEFAULT NULL COMMENT '所属专业',
  `school` varchar(32) DEFAULT NULL COMMENT '毕业院校',
  `beginDate` date DEFAULT NULL COMMENT '入职日期',
  `workState` enum('在职','离职') DEFAULT '在职' COMMENT '在职状态',
  `workID` char(8) DEFAULT NULL COMMENT '工号',
  `contractTerm` double DEFAULT NULL COMMENT '合同期限',
  `conversionTime` date DEFAULT NULL COMMENT '转正日期',
  `notWorkDate` date DEFAULT NULL COMMENT '离职日期',
  `beginContract` date DEFAULT NULL COMMENT '合同起始日期',
  `endContract` date DEFAULT NULL COMMENT '合同终止日期',
  `workAge` int DEFAULT NULL COMMENT '工龄',
  PRIMARY KEY (`id`),
  KEY `departmentId` (`departmentId`),
  KEY `jobId` (`jobLevelId`),
  KEY `dutyId` (`posId`),
  KEY `nationId` (`nationId`),
  KEY `politicId` (`politicId`),
  KEY `workID_key` (`workID`),
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`departmentId`) REFERENCES `department` (`id`),
  CONSTRAINT `employee_ibfk_2` FOREIGN KEY (`jobLevelId`) REFERENCES `joblevel` (`id`),
  CONSTRAINT `employee_ibfk_3` FOREIGN KEY (`posId`) REFERENCES `position` (`id`),
  CONSTRAINT `employee_ibfk_4` FOREIGN KEY (`nationId`) REFERENCES `nation` (`id`),
  CONSTRAINT `employee_ibfk_5` FOREIGN KEY (`politicId`) REFERENCES `politicsstatus` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1942 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of employee
-- ----------------------------
BEGIN;
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1, '江南一点雨', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 5, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000001', 2, '2018-04-01', NULL, '2018-01-01', '2020-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (2, '陈静', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 91, 12, 29, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000002', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (3, '赵琳浩', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000003', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (4, '鹿存亮', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000004', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (5, '姚森', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '硕士', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000005', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (6, '云星', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000006', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (7, '贾旭明', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000007', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (8, '张黎明', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000008', 7, '2018-04-01', NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (9, '薛磊', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000009', 6, '2018-04-01', NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (10, '张洁', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000010', 1, '2018-01-31', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (11, '江南一点雨2', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 91, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000011', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (12, '陈静2', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000012', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (13, '赵琳浩2', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000013', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (14, '鹿存亮2', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000014', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (15, '姚森2', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000015', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (16, '云星2', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000016', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (17, '贾旭明2', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000017', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (18, '王一亭', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000018', 7, '2018-04-01', NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (19, '薛磊2', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000019', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (20, '张洁2', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000020', 1, '2018-01-31', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (21, '江南一点雨3', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000021', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (22, '陈静3', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000022', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (24, '鹿存亮3', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000024', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (25, '姚森3', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000025', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (26, '云星3', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000026', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (27, '贾旭明3', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000027', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (28, '张黎明3', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000028', 7, '2018-04-01', NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (29, '薛磊3', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000029', 6, '2018-04-01', NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (31, '江南一点雨4', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000031', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (32, '陈静4', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000032', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (33, '赵琳浩4', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000033', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (34, '鹿存亮4', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000034', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (35, '姚森4', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000035', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (36, '云星4', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000036', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (37, '贾旭明4', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000037', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (38, '张黎明2', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000038', 7, '2018-04-01', NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (39, '薛磊4', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000039', 6, '2018-04-01', NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (40, '张洁4', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000040', 1, '2018-01-31', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (41, '江南一点雨5', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000041', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (42, '陈静5', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000042', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (43, '赵琳浩5', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000043', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (44, '鹿存亮5', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000044', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (45, '姚森5', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000045', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (46, '云星5', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000046', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (47, '贾旭明5', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000047', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (48, '张黎明5', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000048', 7, '2018-04-01', NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (49, '薛磊5', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000049', 6, '2018-04-01', NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (50, '张洁5', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000050', 1, '2018-01-31', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (51, '江南一点雨6', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000051', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (52, '陈静6', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000052', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (53, '赵琳浩6', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000053', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (54, '鹿存亮6', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000054', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (55, '姚森6', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000055', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (56, '云星6', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000056', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1351, '江南一点雨', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000001', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1352, '陈静', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000002', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1353, '赵琳浩', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000003', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1354, '鹿存亮', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000004', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1355, '姚森', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000005', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1357, '贾旭明', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000007', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1358, '张黎明', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000008', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1359, '薛磊', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000009', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1360, '张洁', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000010', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1361, '江南一点雨2', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000011', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1362, '陈静2', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000012', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1363, '赵琳浩2', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000013', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1364, '鹿存亮2', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000014', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1365, '姚森2', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000015', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1366, '云星2', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000016', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1367, '贾旭明2', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000017', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1368, '王一亭', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000018', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1369, '薛磊2', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000019', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1370, '张洁2', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000020', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1371, '江南一点雨3', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000021', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1372, '陈静3', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000022', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1373, '鹿存亮3', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000024', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1374, '姚森3', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000025', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1375, '云星3', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000026', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1376, '贾旭明3', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000027', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1377, '张黎明3', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000028', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1378, '薛磊3', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000029', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1379, '江南一点雨4', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000031', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1380, '陈静4', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000032', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1381, '赵琳浩4', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000033', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1382, '鹿存亮4', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000034', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1383, '姚森4', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000035', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1384, '云星4', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000036', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1385, '贾旭明4', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000037', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1411, '谢工', '女', '1970-01-01', '618177197001011234', '离异', 1, '江苏', 1, '584991843@qq.com', '18558887788', '北京', 91, 12, 29, '劳动合同', '本科', '计算机软件', '南华大学', '2018-01-01', '在职', '00000038', 5, '2019-01-01', NULL, '2018-01-01', '2023-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1412, '林昭亮', '男', '1990-01-01', '610122199809091234', '已婚', 1, '广东', 13, '584991843@qq.com', '16767776654', '广东深圳', 91, 15, 33, '劳动合同', '大专', '计算机软件', '广东职业技术学院', '2018-01-01', '在职', '00000039', 5, '2018-04-01', NULL, '2018-01-01', '2023-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1413, '11', '男', '2018-01-01', '610122201801011234', '已婚', 1, '1', 1, '584991843@qq.com', '111', '1', 89, 9, 29, '劳动合同', '本科', '1', '1', '2018-01-01', '在职', '00000040', 4, '2018-04-01', NULL, '2018-01-01', '2022-01-26', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1414, '1', '男', '2018-01-01', '610188199809091234', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 89, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000041', 1, '2018-01-31', NULL, '2018-01-31', '2019-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1415, '1', '男', '2018-01-01', '610122199909090000', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 78, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000042', 1, '2018-01-31', NULL, '2018-01-31', '2019-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1416, '1', '男', '2018-01-01', '610122199909090000', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 81, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000043', 0, '2018-01-31', NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1417, '1', '男', '2018-01-01', '610122199909099999', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 87, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-01', '在职', '00000044', 0, '2018-01-01', NULL, '2018-01-01', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1418, '1', '男', '2018-01-01', '610122199909099999', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 78, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-01', '在职', '00000045', 0, '2018-01-01', NULL, '2018-01-01', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1419, '林伯渠', '男', '2018-01-01', '610122199909099999', '未婚', 1, '1', 1, '584991843@qq.com', '1', '1', 8, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000046', 0, '2018-01-31', NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1420, '1', '男', '2018-01-01', '610122199909091234', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 8, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000047', 0, '2018-01-31', NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1421, '江南一点雨', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 5, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000001', 2, NULL, NULL, '2018-01-01', '2020-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1422, '陈静', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 8, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000002', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1423, '赵琳浩', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000003', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1424, '鹿存亮', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000004', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1425, '姚森', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '硕士', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000005', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1426, '云星', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000006', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1427, '贾旭明', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000007', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1428, '张黎明', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000008', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1429, '薛磊', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000009', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1430, '张洁', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000010', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1431, '江南一点雨2', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 91, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000011', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1432, '陈静2', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000012', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1433, '赵琳浩2', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000013', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1434, '鹿存亮2', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000014', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1435, '姚森2', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000015', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1436, '云星2', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000016', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1437, '贾旭明2', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000017', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1438, '王一亭', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000018', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1439, '薛磊2', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000019', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1440, '张洁2', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000020', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1441, '江南一点雨3', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000021', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1442, '陈静3', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000022', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1443, '鹿存亮3', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000024', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1444, '姚森3', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000025', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1445, '云星3', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000026', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1446, '贾旭明3', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000027', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1447, '张黎明3', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000028', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1448, '薛磊3', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000029', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1449, '江南一点雨4', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000031', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1450, '陈静4', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000032', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1451, '赵琳浩4', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000033', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1452, '鹿存亮4', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000034', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1453, '姚森4', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000035', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1454, '云星4', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000036', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1455, '贾旭明4', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000037', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1456, '张黎明2', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000038', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1457, '薛磊4', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000039', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1458, '张洁4', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000040', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1459, '江南一点雨5', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000041', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1460, '陈静5', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000042', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1461, '赵琳浩5', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000043', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1462, '鹿存亮5', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000044', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1463, '姚森5', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000045', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1464, '云星5', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000046', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1465, '贾旭明5', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000047', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1466, '张黎明5', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000048', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1467, '薛磊5', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000049', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1468, '张洁5', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000050', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1469, '江南一点雨6', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000051', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1470, '陈静6', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000052', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1471, '赵琳浩6', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000053', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1472, '鹿存亮6', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000054', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1473, '姚森6', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000055', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1474, '云星6', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000056', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1475, '江南一点雨', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000001', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1476, '陈静', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000002', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1477, '赵琳浩', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000003', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1478, '鹿存亮', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000004', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1479, '姚森', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000005', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1480, '贾旭明', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000007', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1481, '张黎明', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000008', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1482, '薛磊', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000009', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1483, '张洁', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000010', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1484, '江南一点雨2', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000011', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1485, '陈静2', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000012', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1486, '赵琳浩2', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000013', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1487, '鹿存亮2', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000014', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1488, '姚森2', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000015', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1489, '云星2', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000016', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1490, '贾旭明2', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000017', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1491, '王一亭', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000018', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1492, '薛磊2', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000019', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1493, '张洁2', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000020', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1494, '江南一点雨3', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000021', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1495, '陈静3', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000022', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1496, '鹿存亮3', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000024', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1497, '姚森3', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000025', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1498, '云星3', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000026', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1499, '贾旭明3', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000027', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1500, '张黎明3', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000028', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1501, '薛磊3', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000029', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1502, '江南一点雨4', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000031', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1503, '陈静4', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000032', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1504, '赵琳浩4', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000033', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1505, '鹿存亮4', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000034', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1506, '姚森4', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000035', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1507, '云星4', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000036', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1508, '贾旭明4', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000037', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1509, '谢工', '女', '1970-01-01', '618177197001011234', '离异', 1, '江苏', 1, '584991843@qq.com', '18558887788', '北京', 91, 12, 29, '劳动合同', '本科', '计算机软件', '南华大学', '2018-01-01', '在职', '00000038', 5, NULL, NULL, '2018-01-01', '2023-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1510, '林昭亮', '男', '1990-01-01', '610122199809091234', '已婚', 1, '广东', 13, '584991843@qq.com', '16767776654', '广东深圳', 91, 15, 33, '劳动合同', '大专', '计算机软件', '广东职业技术学院', '2018-01-01', '在职', '00000039', 5, NULL, NULL, '2018-01-01', '2023-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1511, '11', '男', '2018-01-01', '610122201801011234', '已婚', 1, '1', 1, '584991843@qq.com', '111', '1', 89, 9, 29, '劳动合同', '本科', '1', '1', '2018-01-01', '在职', '00000040', 4, NULL, NULL, '2018-01-01', '2022-01-26', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1512, '1', '男', '2018-01-01', '610188199809091234', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 89, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000041', 1, NULL, NULL, '2018-01-31', '2019-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1513, '1', '男', '2018-01-01', '610122199909090000', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 78, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000042', 1, NULL, NULL, '2018-01-31', '2019-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1514, '1', '男', '2018-01-01', '610122199909090000', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 81, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000043', 0, NULL, NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1515, '1', '男', '2018-01-01', '610122199909099999', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 87, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-01', '在职', '00000044', 0, NULL, NULL, '2018-01-01', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1516, '1', '男', '2018-01-01', '610122199909099999', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 78, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-01', '在职', '00000045', 0, NULL, NULL, '2018-01-01', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1517, '林伯渠', '男', '2018-01-01', '610122199909099999', '未婚', 1, '1', 1, '584991843@qq.com', '1', '1', 8, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000046', 0, NULL, NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1518, '1', '男', '2018-01-01', '610122199909091234', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 8, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000047', 0, NULL, NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1519, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 5, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2017-12-31', '在职', '00000057', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1520, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 93, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2019-11-05', '在职', '00000058', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1521, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 93, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2019-11-05', '在职', '00000059', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1522, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowan@123.com', '18565558897', '深圳市南山区', 5, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2017-12-31', '在职', '00000060', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1523, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowan@123.com', '18565558897', '深圳市南山区', 5, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2017-12-31', '在职', '00000060', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1526, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 91, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2017-12-31', '在职', '00000061', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1527, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 91, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2017-12-31', '在职', '00000062', 6.17, '2018-03-31', NULL, '2017-12-31', '2024-02-29', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1528, 'javaboy666', '男', '2019-11-10', '610122199911111111', '已婚', 1, '广东', 13, '123@qq.com', '12345678901', '广东深圳', 78, 9, 29, '劳动合同', '本科', '信管', '深圳大学', '2019-11-10', '在职', '00000063', 3.25, '2020-02-10', NULL, '2019-11-10', '2023-02-22', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1529, 'javaboy666', '男', '2019-11-10', '610122199911111111', '已婚', 1, '广东', 13, '123@qq.com', '12345678901', '广东深圳', 78, 9, 29, '劳动合同', '本科', '信管', '深圳大学', '2019-11-10', '在职', '00000063', 3.25, '2020-02-10', NULL, '2019-11-10', '2023-02-22', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1530, '江南一点雨', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 5, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000001', 2, '2018-04-01', NULL, '2018-01-01', '2020-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1531, '陈静', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 91, 12, 29, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000002', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1532, '赵琳浩', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000003', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1533, '鹿存亮', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000004', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1534, '姚森', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '硕士', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000005', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1535, '云星', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000006', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1536, '贾旭明', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000007', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1537, '张黎明', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000008', 7, '2018-04-01', NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1538, '薛磊', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000009', 6, '2018-04-01', NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1539, '张洁', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000010', 1, '2018-01-31', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1540, '江南一点雨2', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 91, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000011', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1541, '陈静2', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000012', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1542, '赵琳浩2', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000013', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1543, '鹿存亮2', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000014', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1544, '姚森2', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000015', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1545, '云星2', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000016', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1546, '贾旭明2', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000017', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1547, '王一亭', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000018', 7, '2018-04-01', NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1548, '薛磊2', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000019', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1549, '张洁2', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000020', 1, '2018-01-31', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1550, '江南一点雨3', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000021', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1551, '陈静3', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000022', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1552, '鹿存亮3', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000024', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1553, '姚森3', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000025', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1554, '云星3', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000026', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1555, '贾旭明3', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000027', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1556, '张黎明3', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000028', 7, '2018-04-01', NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1557, '薛磊3', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000029', 6, '2018-04-01', NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1558, '江南一点雨4', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000031', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1559, '陈静4', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000032', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1560, '赵琳浩4', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000033', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1561, '鹿存亮4', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000034', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1562, '姚森4', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000035', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1563, '云星4', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000036', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1564, '贾旭明4', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000037', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1565, '张黎明2', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000038', 7, '2018-04-01', NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1566, '薛磊4', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000039', 6, '2018-04-01', NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1567, '张洁4', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000040', 1, '2018-01-31', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1568, '江南一点雨5', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000041', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1569, '陈静5', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000042', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1570, '赵琳浩5', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000043', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1571, '鹿存亮5', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000044', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1572, '姚森5', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000045', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1573, '云星5', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000046', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1574, '贾旭明5', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000047', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1575, '张黎明5', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000048', 7, '2018-04-01', NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1576, '薛磊5', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000049', 6, '2018-04-01', NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1577, '张洁5', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000050', 1, '2018-01-31', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1578, '江南一点雨6', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000051', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1579, '陈静6', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000052', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1580, '赵琳浩6', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000053', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1581, '鹿存亮6', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000054', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1582, '姚森6', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000055', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1583, '云星6', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000056', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1584, '江南一点雨', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000001', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1585, '陈静', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000002', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1586, '赵琳浩', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000003', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1587, '鹿存亮', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000004', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1588, '姚森', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000005', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1589, '贾旭明', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000007', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1590, '张黎明', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000008', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1591, '薛磊', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000009', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1592, '张洁', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000010', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1593, '江南一点雨2', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000011', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1594, '陈静2', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000012', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1595, '赵琳浩2', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000013', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1596, '鹿存亮2', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000014', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1597, '姚森2', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000015', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1598, '云星2', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000016', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1599, '贾旭明2', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000017', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1600, '王一亭', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000018', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1601, '薛磊2', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000019', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1602, '张洁2', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000020', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1603, '江南一点雨3', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000021', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1604, '陈静3', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000022', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1605, '鹿存亮3', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000024', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1606, '姚森3', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000025', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1607, '云星3', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000026', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1608, '贾旭明3', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000027', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1609, '张黎明3', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000028', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1610, '薛磊3', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000029', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1611, '江南一点雨4', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000031', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1612, '陈静4', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000032', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1613, '赵琳浩4', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000033', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1614, '鹿存亮4', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000034', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1615, '姚森4', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000035', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1616, '云星4', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000036', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1617, '贾旭明4', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000037', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1618, '谢工', '女', '1970-01-01', '618177197001011234', '离异', 1, '江苏', 1, '584991843@qq.com', '18558887788', '北京', 91, 12, 29, '劳动合同', '本科', '计算机软件', '南华大学', '2018-01-01', '在职', '00000038', 5, '2019-01-01', NULL, '2018-01-01', '2023-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1619, '林昭亮', '男', '1990-01-01', '610122199809091234', '已婚', 1, '广东', 13, '584991843@qq.com', '16767776654', '广东深圳', 91, 15, 33, '劳动合同', '大专', '计算机软件', '广东职业技术学院', '2018-01-01', '在职', '00000039', 5, '2018-04-01', NULL, '2018-01-01', '2023-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1620, '11', '男', '2018-01-01', '610122201801011234', '已婚', 1, '1', 1, '584991843@qq.com', '111', '1', 89, 9, 29, '劳动合同', '本科', '1', '1', '2018-01-01', '在职', '00000040', 4, '2018-04-01', NULL, '2018-01-01', '2022-01-26', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1621, '1', '男', '2018-01-01', '610188199809091234', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 89, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000041', 1, '2018-01-31', NULL, '2018-01-31', '2019-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1622, '1', '男', '2018-01-01', '610122199909090000', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 78, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000042', 1, '2018-01-31', NULL, '2018-01-31', '2019-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1623, '1', '男', '2018-01-01', '610122199909090000', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 81, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000043', 0, '2018-01-31', NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1624, '1', '男', '2018-01-01', '610122199909099999', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 87, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-01', '在职', '00000044', 0, '2018-01-01', NULL, '2018-01-01', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1625, '1', '男', '2018-01-01', '610122199909099999', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 78, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-01', '在职', '00000045', 0, '2018-01-01', NULL, '2018-01-01', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1626, '林伯渠', '男', '2018-01-01', '610122199909099999', '未婚', 1, '1', 1, '584991843@qq.com', '1', '1', 8, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000046', 0, '2018-01-31', NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1627, '1', '男', '2018-01-01', '610122199909091234', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 8, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000047', 0, '2018-01-31', NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1628, '江南一点雨', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 5, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000001', 2, NULL, NULL, '2018-01-01', '2020-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1629, '陈静', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 8, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000002', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1630, '赵琳浩', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000003', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1631, '鹿存亮', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000004', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1632, '姚森', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '硕士', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000005', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1633, '云星', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000006', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1634, '贾旭明', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000007', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1635, '张黎明', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000008', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1636, '薛磊', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000009', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1637, '张洁', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000010', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1638, '江南一点雨2', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 91, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000011', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1639, '陈静2', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000012', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1640, '赵琳浩2', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000013', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1641, '鹿存亮2', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000014', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1642, '姚森2', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000015', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1643, '云星2', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000016', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1644, '贾旭明2', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000017', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1645, '王一亭', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000018', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1646, '薛磊2', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000019', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1647, '张洁2', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000020', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1648, '江南一点雨3', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000021', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1649, '陈静3', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000022', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1650, '鹿存亮3', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000024', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1651, '姚森3', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000025', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1652, '云星3', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000026', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1653, '贾旭明3', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000027', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1654, '张黎明3', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000028', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1655, '薛磊3', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000029', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1656, '江南一点雨4', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000031', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1657, '陈静4', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000032', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1658, '赵琳浩4', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000033', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1659, '鹿存亮4', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000034', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1660, '姚森4', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000035', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1661, '云星4', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000036', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1662, '贾旭明4', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000037', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1663, '张黎明2', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000038', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1664, '薛磊4', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000039', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1665, '张洁4', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000040', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1666, '江南一点雨5', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000041', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1667, '陈静5', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000042', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1668, '赵琳浩5', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000043', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1669, '鹿存亮5', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000044', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1670, '姚森5', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000045', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1671, '云星5', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000046', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1672, '贾旭明5', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000047', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1673, '张黎明5', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000048', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1674, '薛磊5', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000049', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1675, '张洁5', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000050', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1676, '江南一点雨6', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000051', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1677, '陈静6', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000052', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1678, '赵琳浩6', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000053', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1679, '鹿存亮6', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000054', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1680, '姚森6', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000055', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1681, '云星6', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000056', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1682, '江南一点雨', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000001', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1683, '陈静', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000002', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1684, '赵琳浩', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000003', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1685, '鹿存亮', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000004', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1686, '姚森', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000005', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1687, '贾旭明', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000007', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1688, '张黎明', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000008', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1689, '薛磊', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000009', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1690, '张洁', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000010', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1691, '江南一点雨2', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000011', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1692, '陈静2', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000012', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1693, '赵琳浩2', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000013', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1694, '鹿存亮2', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000014', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1695, '姚森2', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000015', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1696, '云星2', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000016', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1697, '贾旭明2', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000017', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1698, '王一亭', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000018', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1699, '薛磊2', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000019', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1700, '张洁2', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000020', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1701, '江南一点雨3', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000021', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1702, '陈静3', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000022', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1703, '鹿存亮3', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000024', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1704, '姚森3', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000025', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1705, '云星3', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000026', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1706, '贾旭明3', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000027', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1707, '张黎明3', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000028', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1708, '薛磊3', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000029', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1709, '江南一点雨4', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000031', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1710, '陈静4', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000032', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1711, '赵琳浩4', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000033', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1712, '鹿存亮4', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000034', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1713, '姚森4', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000035', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1714, '云星4', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000036', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1715, '贾旭明4', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000037', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1716, '谢工', '女', '1970-01-01', '618177197001011234', '离异', 1, '江苏', 1, '584991843@qq.com', '18558887788', '北京', 91, 12, 29, '劳动合同', '本科', '计算机软件', '南华大学', '2018-01-01', '在职', '00000038', 5, NULL, NULL, '2018-01-01', '2023-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1717, '林昭亮', '男', '1990-01-01', '610122199809091234', '已婚', 1, '广东', 13, '584991843@qq.com', '16767776654', '广东深圳', 91, 15, 33, '劳动合同', '大专', '计算机软件', '广东职业技术学院', '2018-01-01', '在职', '00000039', 5, NULL, NULL, '2018-01-01', '2023-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1718, '11', '男', '2018-01-01', '610122201801011234', '已婚', 1, '1', 1, '584991843@qq.com', '111', '1', 89, 9, 29, '劳动合同', '本科', '1', '1', '2018-01-01', '在职', '00000040', 4, NULL, NULL, '2018-01-01', '2022-01-26', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1719, '1', '男', '2018-01-01', '610188199809091234', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 89, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000041', 1, NULL, NULL, '2018-01-31', '2019-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1720, '1', '男', '2018-01-01', '610122199909090000', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 78, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000042', 1, NULL, NULL, '2018-01-31', '2019-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1721, '1', '男', '2018-01-01', '610122199909090000', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 81, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000043', 0, NULL, NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1722, '1', '男', '2018-01-01', '610122199909099999', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 87, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-01', '在职', '00000044', 0, NULL, NULL, '2018-01-01', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1723, '1', '男', '2018-01-01', '610122199909099999', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 78, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-01', '在职', '00000045', 0, NULL, NULL, '2018-01-01', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1724, '林伯渠', '男', '2018-01-01', '610122199909099999', '未婚', 1, '1', 1, '584991843@qq.com', '1', '1', 8, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000046', 0, NULL, NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1725, '1', '男', '2018-01-01', '610122199909091234', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 8, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000047', 0, NULL, NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1726, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 5, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2017-12-31', '在职', '00000057', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1727, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 93, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2019-11-05', '在职', '00000058', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1728, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 93, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2019-11-05', '在职', '00000059', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1729, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowan@123.com', '18565558897', '深圳市南山区', 5, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2017-12-31', '在职', '00000060', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1730, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowan@123.com', '18565558897', '深圳市南山区', 5, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2017-12-31', '在职', '00000060', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1731, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 91, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2017-12-31', '在职', '00000061', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1732, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 91, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2017-12-31', '在职', '00000062', 6.17, '2018-03-31', NULL, '2017-12-31', '2024-02-29', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1733, 'javaboy666', '男', '2019-11-10', '610122199911111111', '已婚', 1, '广东', 13, '123@qq.com', '12345678901', '广东深圳', 78, 9, 29, '劳动合同', '本科', '信管', '深圳大学', '2019-11-10', '在职', '00000063', 3.25, '2020-02-10', NULL, '2019-11-10', '2023-02-22', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1734, 'javaboy666', '男', '2019-11-10', '610122199911111111', '已婚', 1, '广东', 13, '123@qq.com', '12345678901', '广东深圳', 78, 9, 29, '劳动合同', '本科', '信管', '深圳大学', '2019-11-10', '在职', '00000063', 3.25, '2020-02-10', NULL, '2019-11-10', '2023-02-22', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1735, '江南一点雨', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 5, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000001', 2, '2018-04-01', NULL, '2018-01-01', '2020-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1736, '陈静', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 91, 12, 29, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000002', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1737, '赵琳浩', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000003', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1738, '鹿存亮', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000004', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1739, '姚森', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '硕士', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000005', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1740, '云星', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000006', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1741, '贾旭明', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000007', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1742, '张黎明', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000008', 7, '2018-04-01', NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1743, '薛磊', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000009', 6, '2018-04-01', NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1744, '张洁', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000010', 1, '2018-01-31', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1745, '江南一点雨2', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 91, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000011', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1746, '陈静2', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000012', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1747, '赵琳浩2', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000013', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1748, '鹿存亮2', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000014', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1749, '姚森2', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000015', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1750, '云星2', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000016', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1751, '贾旭明2', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000017', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1752, '王一亭', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000018', 7, '2018-04-01', NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1753, '薛磊2', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000019', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1754, '张洁2', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000020', 1, '2018-01-31', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1755, '江南一点雨3', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000021', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1756, '陈静3', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000022', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1757, '鹿存亮3', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000024', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1758, '姚森3', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000025', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1759, '云星3', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000026', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1760, '贾旭明3', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000027', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1761, '张黎明3', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000028', 7, '2018-04-01', NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1762, '薛磊3', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000029', 6, '2018-04-01', NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1763, '江南一点雨4', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000031', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1764, '陈静4', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000032', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1765, '赵琳浩4', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000033', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1766, '鹿存亮4', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000034', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1767, '姚森4', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000035', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1768, '云星4', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000036', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1769, '贾旭明4', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000037', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1770, '张黎明2', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000038', 7, '2018-04-01', NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1771, '薛磊4', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000039', 6, '2018-04-01', NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1772, '张洁4', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000040', 1, '2018-01-31', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1773, '江南一点雨5', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000041', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1774, '陈静5', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000042', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1775, '赵琳浩5', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000043', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1776, '鹿存亮5', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000044', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1777, '姚森5', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000045', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1778, '云星5', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000046', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1779, '贾旭明5', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000047', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1780, '张黎明5', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000048', 7, '2018-04-01', NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1781, '薛磊5', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000049', 6, '2018-04-01', NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1782, '张洁5', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000050', 1, '2018-01-31', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1783, '江南一点雨6', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000051', 1, '2018-04-01', NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1784, '陈静6', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000052', 3, '2015-09-10', NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1785, '赵琳浩6', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000053', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1786, '鹿存亮6', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000054', 3.5, '2018-04-01', NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1787, '姚森6', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000055', 7, '2017-04-02', NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1788, '云星6', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000056', 5.25, '2018-04-01', NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1789, '江南一点雨', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000001', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1790, '陈静', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000002', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1791, '赵琳浩', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000003', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1792, '鹿存亮', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000004', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1793, '姚森', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000005', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1794, '贾旭明', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000007', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1795, '张黎明', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000008', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1796, '薛磊', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000009', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1797, '张洁', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000010', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1798, '江南一点雨2', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000011', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1799, '陈静2', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000012', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1800, '赵琳浩2', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000013', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1801, '鹿存亮2', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000014', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1802, '姚森2', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000015', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1803, '云星2', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000016', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1804, '贾旭明2', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000017', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1805, '王一亭', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000018', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1806, '薛磊2', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000019', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1807, '张洁2', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000020', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1808, '江南一点雨3', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000021', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1809, '陈静3', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000022', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1810, '鹿存亮3', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000024', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1811, '姚森3', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000025', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1812, '云星3', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000026', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1813, '贾旭明3', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000027', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1814, '张黎明3', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000028', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1815, '薛磊3', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000029', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1816, '江南一点雨4', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000031', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1817, '陈静4', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000032', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1818, '赵琳浩4', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000033', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1819, '鹿存亮4', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000034', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1820, '姚森4', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000035', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1821, '云星4', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000036', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1822, '贾旭明4', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000037', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1823, '谢工', '女', '1970-01-01', '618177197001011234', '离异', 1, '江苏', 1, '584991843@qq.com', '18558887788', '北京', 91, 12, 29, '劳动合同', '本科', '计算机软件', '南华大学', '2018-01-01', '在职', '00000038', 5, '2019-01-01', NULL, '2018-01-01', '2023-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1824, '林昭亮', '男', '1990-01-01', '610122199809091234', '已婚', 1, '广东', 13, '584991843@qq.com', '16767776654', '广东深圳', 91, 15, 33, '劳动合同', '大专', '计算机软件', '广东职业技术学院', '2018-01-01', '在职', '00000039', 5, '2018-04-01', NULL, '2018-01-01', '2023-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1825, '11', '男', '2018-01-01', '610122201801011234', '已婚', 1, '1', 1, '584991843@qq.com', '111', '1', 89, 9, 29, '劳动合同', '本科', '1', '1', '2018-01-01', '在职', '00000040', 4, '2018-04-01', NULL, '2018-01-01', '2022-01-26', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1826, '1', '男', '2018-01-01', '610188199809091234', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 89, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000041', 1, '2018-01-31', NULL, '2018-01-31', '2019-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1827, '1', '男', '2018-01-01', '610122199909090000', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 78, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000042', 1, '2018-01-31', NULL, '2018-01-31', '2019-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1828, '1', '男', '2018-01-01', '610122199909090000', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 81, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000043', 0, '2018-01-31', NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1829, '1', '男', '2018-01-01', '610122199909099999', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 87, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-01', '在职', '00000044', 0, '2018-01-01', NULL, '2018-01-01', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1830, '1', '男', '2018-01-01', '610122199909099999', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 78, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-01', '在职', '00000045', 0, '2018-01-01', NULL, '2018-01-01', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1831, '林伯渠', '男', '2018-01-01', '610122199909099999', '未婚', 1, '1', 1, '584991843@qq.com', '1', '1', 8, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000046', 0, '2018-01-31', NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1832, '1', '男', '2018-01-01', '610122199909091234', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 8, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000047', 0, '2018-01-31', NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1833, '江南一点雨', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 5, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000001', 2, NULL, NULL, '2018-01-01', '2020-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1834, '陈静', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 8, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000002', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1835, '赵琳浩', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000003', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1836, '鹿存亮', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000004', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1837, '姚森', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '硕士', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000005', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1838, '云星', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000006', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1839, '贾旭明', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000007', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1840, '张黎明', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000008', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1841, '薛磊', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000009', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1842, '张洁', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000010', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1843, '江南一点雨2', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 91, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000011', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1844, '陈静2', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000012', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1845, '赵琳浩2', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000013', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1846, '鹿存亮2', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000014', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1847, '姚森2', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000015', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1848, '云星2', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000016', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1849, '贾旭明2', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000017', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1850, '王一亭', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000018', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1851, '薛磊2', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000019', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1852, '张洁2', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000020', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1853, '江南一点雨3', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000021', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1854, '陈静3', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000022', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1855, '鹿存亮3', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000024', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1856, '姚森3', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000025', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1857, '云星3', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000026', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1858, '贾旭明3', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000027', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1859, '张黎明3', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000028', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1860, '薛磊3', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000029', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1861, '江南一点雨4', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000031', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1862, '陈静4', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000032', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1863, '赵琳浩4', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000033', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1864, '鹿存亮4', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000034', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1865, '姚森4', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000035', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1866, '云星4', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000036', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1867, '贾旭明4', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000037', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1868, '张黎明2', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000038', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1869, '薛磊4', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000039', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1870, '张洁4', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000040', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1871, '江南一点雨5', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000041', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1872, '陈静5', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000042', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1873, '赵琳浩5', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000043', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1874, '鹿存亮5', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000044', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1875, '姚森5', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000045', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1876, '云星5', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000046', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1877, '贾旭明5', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000047', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1878, '张黎明5', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000048', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1879, '薛磊5', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000049', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1880, '张洁5', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000050', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1881, '江南一点雨6', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000051', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1882, '陈静6', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000052', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1883, '赵琳浩6', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000053', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1884, '鹿存亮6', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000054', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1885, '姚森6', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000055', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1886, '云星6', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000056', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1887, '江南一点雨', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000001', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1888, '陈静', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000002', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1889, '赵琳浩', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000003', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1890, '鹿存亮', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000004', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1891, '姚森', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000005', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1892, '贾旭明', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000007', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1893, '张黎明', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000008', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1894, '薛磊', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000009', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1895, '张洁', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000010', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1896, '江南一点雨2', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000011', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1897, '陈静2', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000012', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1898, '赵琳浩2', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000013', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1899, '鹿存亮2', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000014', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1900, '姚森2', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000015', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1901, '云星2', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000016', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1902, '贾旭明2', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000017', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1903, '王一亭', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000018', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1904, '薛磊2', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000019', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1905, '张洁2', '女', '1990-10-09', '420177199010093652', '未婚', 1, '海南', 5, 'zhangjie@qq.com', '13695557742', '海口市美兰区', 92, 16, 34, '劳动合同', '高中', '无', '海南侨中', '2018-01-01', '在职', '00000020', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1906, '江南一点雨3', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000021', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1907, '陈静3', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000022', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1908, '鹿存亮3', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000024', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1909, '姚森3', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000025', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1910, '云星3', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000026', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1911, '贾旭明3', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000027', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1912, '张黎明3', '男', '1991-02-01', '610144199102014569', '已婚', 1, '广东', 6, 'zhangliming@qq.com', '18979994478', '广东珠海', 91, 15, 33, '劳动合同', '高中', '考古', '清华大学', '2018-01-01', '在职', '00000028', 7, NULL, NULL, '2018-01-01', '2025-01-30', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1913, '薛磊3', '男', '1992-07-01', '610144199207017895', '已婚', 1, '陕西西安', 13, 'xuelei@qq.com', '15648887741', '西安市雁塔区', 92, 14, 34, '劳动合同', '初中', '无', '华胥中学', '2018-01-01', '在职', '00000029', 6, NULL, NULL, '2018-01-01', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1914, '江南一点雨4', '男', '1990-01-01', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 8, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2018-01-01', '在职', '00000031', 1, NULL, NULL, '2018-01-01', '2019-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1915, '陈静4', '女', '1989-02-01', '421288198902011234', '已婚', 1, '海南', 1, 'chenjing@qq.com', '18795556693', '海南省海口市美兰区', 82, 12, 30, '劳动合同', '高中', '市场营销', '武汉大学', '2015-06-09', '在职', '00000032', 3, NULL, NULL, '2015-06-09', '2018-06-08', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1916, '赵琳浩4', '男', '1993-03-04', '610122199303041456', '未婚', 1, '陕西', 3, 'zhao@qq.com', '15698887795', '陕西省西安市莲湖区', 91, 12, 33, '劳动合同', '博士', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000033', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1917, '鹿存亮4', '男', '1990-01-03', '610122199001031456', '已婚', 1, '陕西', 3, 'zhao@qq.com', '15612347795', '陕西省西安市莲湖区', 92, 12, 34, '劳动合同', '高中', '电子工程', '哈尔滨理工大学', '2018-01-01', '在职', '00000034', 3.5, NULL, NULL, '2018-01-01', '2021-07-14', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1918, '姚森4', '男', '1991-02-05', '610122199102058952', '已婚', 1, '河南', 3, 'yaosen@qq.com', '14785559936', '河南洛阳人民大道58号', 92, 15, 34, '劳动合同', '初中', '室内装修设计', '西北大学', '2017-01-02', '在职', '00000035', 7, NULL, NULL, '2017-01-02', '2024-01-17', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1919, '云星4', '女', '1993-01-05', '610122199301054789', '已婚', 1, '陕西西安', 1, 'yunxing@qq.com', '15644442252', '陕西西安新城区', 92, 16, 34, '劳务合同', '硕士', '通信工程', '西安电子科技学校', '2018-01-01', '在职', '00000036', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1920, '贾旭明4', '男', '1993-11-11', '610122199311111234', '已婚', 1, '广东广州', 4, 'jiaxuming@qq.com', '15644441234', '广东省广州市天河区冼村路', 78, 15, 33, '劳务合同', '初中', '通信工程', '西北大学', '2018-01-01', '在职', '00000037', 5.25, NULL, NULL, '2018-01-01', '2023-04-13', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1921, '谢工', '女', '1970-01-01', '618177197001011234', '离异', 1, '江苏', 1, '584991843@qq.com', '18558887788', '北京', 91, 12, 29, '劳动合同', '本科', '计算机软件', '南华大学', '2018-01-01', '在职', '00000038', 5, NULL, NULL, '2018-01-01', '2023-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1922, '林昭亮', '男', '1990-01-01', '610122199809091234', '已婚', 1, '广东', 13, '584991843@qq.com', '16767776654', '广东深圳', 91, 15, 33, '劳动合同', '大专', '计算机软件', '广东职业技术学院', '2018-01-01', '在职', '00000039', 5, NULL, NULL, '2018-01-01', '2023-01-01', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1923, '11', '男', '2018-01-01', '610122201801011234', '已婚', 1, '1', 1, '584991843@qq.com', '111', '1', 89, 9, 29, '劳动合同', '本科', '1', '1', '2018-01-01', '在职', '00000040', 4, NULL, NULL, '2018-01-01', '2022-01-26', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1924, '1', '男', '2018-01-01', '610188199809091234', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 89, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000041', 1, NULL, NULL, '2018-01-31', '2019-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1925, '1', '男', '2018-01-01', '610122199909090000', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 78, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000042', 1, NULL, NULL, '2018-01-31', '2019-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1926, '1', '男', '2018-01-01', '610122199909090000', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 81, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000043', 0, NULL, NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1927, '1', '男', '2018-01-01', '610122199909099999', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 87, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-01', '在职', '00000044', 0, NULL, NULL, '2018-01-01', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1928, '1', '男', '2018-01-01', '610122199909099999', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 78, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-01', '在职', '00000045', 0, NULL, NULL, '2018-01-01', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1929, '林伯渠', '男', '2018-01-01', '610122199909099999', '未婚', 1, '1', 1, '584991843@qq.com', '1', '1', 8, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000046', 0, NULL, NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1930, '1', '男', '2018-01-01', '610122199909091234', '已婚', 1, '1', 1, '584991843@qq.com', '1', '1', 8, 9, 29, '劳动合同', '大专', '1', '1', '2018-01-31', '在职', '00000047', 0, NULL, NULL, '2018-01-31', '2018-01-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1931, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 5, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2017-12-31', '在职', '00000057', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1932, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 93, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2019-11-05', '在职', '00000058', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1933, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 93, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2019-11-05', '在职', '00000059', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1934, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowan@123.com', '18565558897', '深圳市南山区', 5, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2017-12-31', '在职', '00000060', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1935, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowan@123.com', '18565558897', '深圳市南山区', 5, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2017-12-31', '在职', '00000060', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1936, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 91, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2017-12-31', '在职', '00000061', 2, '2018-03-31', NULL, '2017-12-31', '2019-12-31', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1937, 'javaboy', '男', '1989-12-31', '610122199001011256', '已婚', 1, '陕西', 13, 'laowang@qq.com', '18565558897', '深圳市南山区', 91, 9, 29, '劳务合同', '本科', '信息管理与信息系统', '深圳大学', '2017-12-31', '在职', '00000062', 6.17, '2018-03-31', NULL, '2017-12-31', '2024-02-29', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1938, 'javaboy666', '男', '2019-11-10', '610122199911111111', '已婚', 1, '广东', 13, '123@qq.com', '12345678901', '广东深圳', 78, 9, 29, '劳动合同', '本科', '信管', '深圳大学', '2019-11-10', '在职', '00000063', 3.25, '2020-02-10', NULL, '2019-11-10', '2023-02-22', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1939, 'javaboy666', '男', '2019-11-10', '610122199911111111', '已婚', 1, '广东', 13, '123@qq.com', '12345678901', '广东深圳', 78, 9, 29, '劳动合同', '本科', '信管', '深圳大学', '2019-11-10', '在职', '00000063', 3.25, '2020-02-10', NULL, '2019-11-10', '2023-02-22', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1940, 'javaboy', '男', '2017-11-01', '610144199905059999', '已婚', 1, '广东', 13, '584991843@qq.com', '18564447789', '广东深圳', 85, 9, 29, '劳动合同', '本科', '信息管理与信息系统', '深圳大学', '2019-11-24', '在职', '00000064', 3, '2020-01-10', NULL, '2019-11-24', '2022-11-27', NULL);
INSERT INTO `employee` (`id`, `name`, `gender`, `birthday`, `idCard`, `wedlock`, `nationId`, `nativePlace`, `politicId`, `email`, `phone`, `address`, `departmentId`, `jobLevelId`, `posId`, `engageForm`, `tiptopDegree`, `specialty`, `school`, `beginDate`, `workState`, `workID`, `contractTerm`, `conversionTime`, `notWorkDate`, `beginContract`, `endContract`, `workAge`) VALUES (1941, 'javaboy', '男', '2019-11-24', '610144199905056666', '已婚', 1, '广东', 13, '584991843@qq.com', '18566667777', '广东深圳', 89, 9, 29, '劳动合同', '本科', '计算机科学', '深圳大学', '2019-11-24', '在职', '00000065', 3, '2020-02-23', NULL, '2019-11-24', '2022-11-27', NULL);
COMMIT;

-- ----------------------------
-- Table structure for employeeec
-- ----------------------------
DROP TABLE IF EXISTS `employeeec`;
CREATE TABLE `employeeec` (
  `id` int NOT NULL AUTO_INCREMENT,
  `eid` int DEFAULT NULL COMMENT '员工编号',
  `ecDate` date DEFAULT NULL COMMENT '奖罚日期',
  `ecReason` varchar(255) DEFAULT NULL COMMENT '奖罚原因',
  `ecPoint` int DEFAULT NULL COMMENT '奖罚分',
  `ecType` int DEFAULT NULL COMMENT '奖罚类别，0：奖，1：罚',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `pid` (`eid`),
  CONSTRAINT `employeeec_ibfk_1` FOREIGN KEY (`eid`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of employeeec
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for employeeremove
-- ----------------------------
DROP TABLE IF EXISTS `employeeremove`;
CREATE TABLE `employeeremove` (
  `id` int NOT NULL AUTO_INCREMENT,
  `eid` int DEFAULT NULL,
  `afterDepId` int DEFAULT NULL COMMENT '调动后部门',
  `afterJobId` int DEFAULT NULL COMMENT '调动后职位',
  `removeDate` date DEFAULT NULL COMMENT '调动日期',
  `reason` varchar(255) DEFAULT NULL COMMENT '调动原因',
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pid` (`eid`),
  CONSTRAINT `employeeremove_ibfk_1` FOREIGN KEY (`eid`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of employeeremove
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for employeetrain
-- ----------------------------
DROP TABLE IF EXISTS `employeetrain`;
CREATE TABLE `employeetrain` (
  `id` int NOT NULL AUTO_INCREMENT,
  `eid` int DEFAULT NULL COMMENT '员工编号',
  `trainDate` date DEFAULT NULL COMMENT '培训日期',
  `trainContent` varchar(255) DEFAULT NULL COMMENT '培训内容',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `pid` (`eid`),
  CONSTRAINT `employeetrain_ibfk_1` FOREIGN KEY (`eid`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of employeetrain
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for empsalary
-- ----------------------------
DROP TABLE IF EXISTS `empsalary`;
CREATE TABLE `empsalary` (
  `id` int NOT NULL AUTO_INCREMENT,
  `eid` int DEFAULT NULL,
  `sid` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `eid` (`eid`),
  KEY `empsalary_ibfk_2` (`sid`),
  CONSTRAINT `empsalary_ibfk_1` FOREIGN KEY (`eid`) REFERENCES `employee` (`id`),
  CONSTRAINT `empsalary_ibfk_2` FOREIGN KEY (`sid`) REFERENCES `salary` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of empsalary
-- ----------------------------
BEGIN;
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (6, 4, 10);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (10, 5, 9);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (11, 6, 13);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (12, 7, 13);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (14, 8, 10);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (15, 9, 10);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (20, 10, 13);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (21, 11, 9);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (22, 3, 13);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (24, 2, 9);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (25, 1, 13);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (26, 33, 10);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (28, 34, 9);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (29, 44, 10);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (30, 45, 10);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (31, 43, 10);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (32, 47, 10);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (33, 52, 13);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (34, 53, 10);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (35, 54, 10);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (36, 56, 10);
INSERT INTO `empsalary` (`id`, `eid`, `sid`) VALUES (38, 21, 9);
COMMIT;

-- ----------------------------
-- Table structure for expertactivities
-- ----------------------------
DROP TABLE IF EXISTS `expertactivities`;
CREATE TABLE `expertactivities` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `teacherID` int DEFAULT NULL,
  `activityID` int DEFAULT NULL,
  `groupID` int DEFAULT NULL,
  `groupID_sub` int DEFAULT NULL,
  `finished` tinyint DEFAULT '0',
  `role` varchar(255) DEFAULT '专家',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `expertID` (`teacherID`) USING BTREE,
  KEY `activityID_2` (`activityID`) USING BTREE,
  KEY `scoreItemID` (`groupID`) USING BTREE,
  CONSTRAINT `expertactivities_ibfk_1` FOREIGN KEY (`activityID`) REFERENCES `activities` (`ID`),
  CONSTRAINT `expertactivities_ibfk_2` FOREIGN KEY (`groupID`) REFERENCES `groups` (`ID`),
  CONSTRAINT `expertactivities_ibfk_3` FOREIGN KEY (`teacherID`) REFERENCES `teacher` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of expertactivities
-- ----------------------------
BEGIN;
INSERT INTO `expertactivities` (`ID`, `teacherID`, `activityID`, `groupID`, `groupID_sub`, `finished`, `role`) VALUES (107, 1054, 34, 40, NULL, 0, '组长');
INSERT INTO `expertactivities` (`ID`, `teacherID`, `activityID`, `groupID`, `groupID_sub`, `finished`, `role`) VALUES (108, 1055, 34, 40, NULL, 0, '专家');
INSERT INTO `expertactivities` (`ID`, `teacherID`, `activityID`, `groupID`, `groupID_sub`, `finished`, `role`) VALUES (109, 1056, 34, 40, NULL, 0, '专家');
INSERT INTO `expertactivities` (`ID`, `teacherID`, `activityID`, `groupID`, `groupID_sub`, `finished`, `role`) VALUES (110, 1057, 34, 40, NULL, 0, '专家');
INSERT INTO `expertactivities` (`ID`, `teacherID`, `activityID`, `groupID`, `groupID_sub`, `finished`, `role`) VALUES (111, 1058, 34, 40, NULL, 0, '专家');
INSERT INTO `expertactivities` (`ID`, `teacherID`, `activityID`, `groupID`, `groupID_sub`, `finished`, `role`) VALUES (112, 1059, 34, 40, NULL, 0, '专家');
INSERT INTO `expertactivities` (`ID`, `teacherID`, `activityID`, `groupID`, `groupID_sub`, `finished`, `role`) VALUES (113, 1060, 34, 40, NULL, 0, '专家');
INSERT INTO `expertactivities` (`ID`, `teacherID`, `activityID`, `groupID`, `groupID_sub`, `finished`, `role`) VALUES (114, 1061, 34, 40, NULL, 1, '秘书');
INSERT INTO `expertactivities` (`ID`, `teacherID`, `activityID`, `groupID`, `groupID_sub`, `finished`, `role`) VALUES (115, 1061, 34, 39, NULL, 0, '秘书');
INSERT INTO `expertactivities` (`ID`, `teacherID`, `activityID`, `groupID`, `groupID_sub`, `finished`, `role`) VALUES (116, 1061, 36, NULL, NULL, 0, '秘书');
COMMIT;

-- ----------------------------
-- Table structure for flyway_schema_history
-- ----------------------------
DROP TABLE IF EXISTS `flyway_schema_history`;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`) USING BTREE,
  KEY `flyway_schema_history_s_idx` (`success`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of flyway_schema_history
-- ----------------------------
BEGIN;
INSERT INTO `flyway_schema_history` (`installed_rank`, `version`, `description`, `type`, `script`, `checksum`, `installed_by`, `installed_on`, `execution_time`, `success`) VALUES (1, '1', 'vhr', 'SQL', 'V1__vhr.sql', -1039138481, 'root', '2023-03-20 17:46:05', 1020, 1);
COMMIT;

-- ----------------------------
-- Table structure for groups
-- ----------------------------
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `activityID` int DEFAULT NULL,
  `name` varchar(60) DEFAULT NULL,
  `expertCount` int DEFAULT NULL,
  `participantCount` int DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `activityIDgroup` (`activityID`) USING BTREE,
  CONSTRAINT `groups_ibfk_1` FOREIGN KEY (`activityID`) REFERENCES `activities` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of groups
-- ----------------------------
BEGIN;
INSERT INTO `groups` (`ID`, `activityID`, `name`, `expertCount`, `participantCount`) VALUES (34, 34, '第1组', 0, 22);
INSERT INTO `groups` (`ID`, `activityID`, `name`, `expertCount`, `participantCount`) VALUES (35, 34, '第2组', 0, 22);
INSERT INTO `groups` (`ID`, `activityID`, `name`, `expertCount`, `participantCount`) VALUES (36, 34, '第3组', 0, 21);
INSERT INTO `groups` (`ID`, `activityID`, `name`, `expertCount`, `participantCount`) VALUES (37, 34, '第4组', 0, 22);
INSERT INTO `groups` (`ID`, `activityID`, `name`, `expertCount`, `participantCount`) VALUES (38, 34, '第5组', 0, 22);
INSERT INTO `groups` (`ID`, `activityID`, `name`, `expertCount`, `participantCount`) VALUES (39, 34, '第6组', 1, 22);
INSERT INTO `groups` (`ID`, `activityID`, `name`, `expertCount`, `participantCount`) VALUES (40, 34, '第7组', 8, 17);
COMMIT;

-- ----------------------------
-- Table structure for hr
-- ----------------------------
DROP TABLE IF EXISTS `hr`;
CREATE TABLE `hr` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'hrID',
  `name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `phone` char(11) DEFAULT NULL COMMENT '手机号码',
  `telephone` varchar(16) DEFAULT NULL COMMENT '住宅电话',
  `address` varchar(64) DEFAULT NULL COMMENT '联系地址',
  `enabled` tinyint(1) DEFAULT '1',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `userface` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of hr
-- ----------------------------
BEGIN;
INSERT INTO `hr` (`id`, `name`, `phone`, `telephone`, `address`, `enabled`, `username`, `password`, `userface`, `remark`) VALUES (3, '系统管理员', '18568887789', '029-82881234', '深圳南山', 1, 'admin', '$2a$10$ySG2lkvjFHY5O0./CPIE1OI8VJsuKYEzOYzqIa7AJR6sEgSzUFOAm', 'http://bpic.588ku.com/element_pic/01/40/00/64573ce2edc0728.jpg', NULL);
INSERT INTO `hr` (`id`, `name`, `phone`, `telephone`, `address`, `enabled`, `username`, `password`, `userface`, `remark`) VALUES (5, '李白', '18568123489', '029-82123434', '海口美兰', 1, 'libai', '$2a$10$oE39aG10kB/rFu2vQeCJTu/V/v4n6DRR0f8WyXRiAYvBpmadoOBE.', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1514093920321&di=913e88c23f382933ef430024afd9128a&imgtype=0&src=http%3A%2F%2Fp.3761.com%2Fpic%2F9771429316733.jpg', NULL);
INSERT INTO `hr` (`id`, `name`, `phone`, `telephone`, `address`, `enabled`, `username`, `password`, `userface`, `remark`) VALUES (10, '韩愈', '18568123666', '029-82111555', '广州番禺', 1, 'hanyu', '$2a$10$oE39aG10kB/rFu2vQeCJTu/V/v4n6DRR0f8WyXRiAYvBpmadoOBE.', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1517070040185&di=be0375e0c3db6c311b837b28c208f318&imgtype=0&src=http%3A%2F%2Fimg2.soyoung.com%2Fpost%2F20150213%2F6%2F20150213141918532.jpg', NULL);
INSERT INTO `hr` (`id`, `name`, `phone`, `telephone`, `address`, `enabled`, `username`, `password`, `userface`, `remark`) VALUES (11, '柳宗元', '18568123377', '029-82111333', '广州天河', 1, 'liuzongyuan', '$2a$10$oE39aG10kB/rFu2vQeCJTu/V/v4n6DRR0f8WyXRiAYvBpmadoOBE.', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1515233756&di=0856d923a0a37a87fd26604a2c871370&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.qqzhi.com%2Fuploadpic%2F2014-09-27%2F041716704.jpg', NULL);
INSERT INTO `hr` (`id`, `name`, `phone`, `telephone`, `address`, `enabled`, `username`, `password`, `userface`, `remark`) VALUES (12, '曾巩', '18568128888', '029-82111222', '广州越秀', 1, 'zenggong', '$2a$10$oE39aG10kB/rFu2vQeCJTu/V/v4n6DRR0f8WyXRiAYvBpmadoOBE.', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1517070040185&di=be0375e0c3db6c311b837b28c208f318&imgtype=0&src=http%3A%2F%2Fimg2.soyoung.com%2Fpost%2F20150213%2F6%2F20150213141918532.jpg', NULL);
COMMIT;

-- ----------------------------
-- Table structure for hr_role
-- ----------------------------
DROP TABLE IF EXISTS `hr_role`;
CREATE TABLE `hr_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `hrid` int DEFAULT NULL,
  `rid` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rid` (`rid`),
  KEY `hr_role_ibfk_1` (`hrid`),
  CONSTRAINT `hr_role_ibfk_1` FOREIGN KEY (`hrid`) REFERENCES `hr` (`id`) ON DELETE CASCADE,
  CONSTRAINT `hr_role_ibfk_2` FOREIGN KEY (`rid`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of hr_role
-- ----------------------------
BEGIN;
INSERT INTO `hr_role` (`id`, `hrid`, `rid`) VALUES (1, 3, 6);
INSERT INTO `hr_role` (`id`, `hrid`, `rid`) VALUES (35, 12, 4);
INSERT INTO `hr_role` (`id`, `hrid`, `rid`) VALUES (36, 12, 3);
INSERT INTO `hr_role` (`id`, `hrid`, `rid`) VALUES (37, 12, 2);
INSERT INTO `hr_role` (`id`, `hrid`, `rid`) VALUES (43, 11, 3);
INSERT INTO `hr_role` (`id`, `hrid`, `rid`) VALUES (44, 11, 2);
INSERT INTO `hr_role` (`id`, `hrid`, `rid`) VALUES (45, 11, 4);
INSERT INTO `hr_role` (`id`, `hrid`, `rid`) VALUES (46, 11, 5);
INSERT INTO `hr_role` (`id`, `hrid`, `rid`) VALUES (48, 10, 3);
INSERT INTO `hr_role` (`id`, `hrid`, `rid`) VALUES (49, 10, 4);
INSERT INTO `hr_role` (`id`, `hrid`, `rid`) VALUES (72, 5, 1);
INSERT INTO `hr_role` (`id`, `hrid`, `rid`) VALUES (73, 5, 2);
INSERT INTO `hr_role` (`id`, `hrid`, `rid`) VALUES (74, 5, 3);
COMMIT;

-- ----------------------------
-- Table structure for indicater
-- ----------------------------
DROP TABLE IF EXISTS `indicater`;
CREATE TABLE `indicater` (
  `ID` int NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `order` varchar(10) DEFAULT NULL,
  `score` int DEFAULT NULL,
  `father` int DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `father` (`father`) USING BTREE,
  CONSTRAINT `indicater_ibfk_1` FOREIGN KEY (`father`) REFERENCES `indicater` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of indicater
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for indicator
-- ----------------------------
DROP TABLE IF EXISTS `indicator`;
CREATE TABLE `indicator` (
  `ID` int NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `order` varchar(20) DEFAULT NULL,
  `score` int DEFAULT NULL,
  `father` int DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `father` (`father`) USING BTREE,
  CONSTRAINT `indicator_ibfk_1` FOREIGN KEY (`father`) REFERENCES `indicator` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of indicator
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for infoitem
-- ----------------------------
DROP TABLE IF EXISTS `infoitem`;
CREATE TABLE `infoitem` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `activityID` int DEFAULT NULL,
  `name` varchar(60) DEFAULT NULL,
  `contentType` varchar(255) DEFAULT NULL COMMENT '值为textbox, textarea, image，分别表示单行文本、多行文本、图片\r\n\r\n',
  `sizelimit` varchar(255) DEFAULT NULL,
  `byParticipant` tinyint NOT NULL,
  `display` tinyint NOT NULL COMMENT '是否显示在评分表中',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `activityID` (`activityID`) USING BTREE,
  CONSTRAINT `infoitem_ibfk_1` FOREIGN KEY (`activityID`) REFERENCES `activities` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of infoitem
-- ----------------------------
BEGIN;
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (66, 34, '性别', 'label,', '500', 0, 0);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (67, 34, '出生日期', 'label,', '500', 0, 0);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (68, 34, '民族', 'label,', '500', 0, 0);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (69, 34, '政治面貌', 'label,', '500', 0, 0);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (70, 34, '考生来源', 'label,', '500', 0, 0);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (71, 34, '毕业单位名称', 'label,', '500', 0, 1);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (72, 34, '毕业专业名称', 'label,', '500', 0, 1);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (73, 34, '毕业年月', 'label,', '500', 0, 0);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (74, 34, '学习/工作单位', 'label,', '500', 0, 0);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (75, 34, '报考专业代码', 'label,', '500', 0, 0);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (76, 34, '报考专业名称', 'label,', '500', 0, 1);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (77, 34, '初试总分', 'label,', '500', 0, 0);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (78, 34, '自我介绍', 'textarea,', '300', 1, 0);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (79, 34, '教育经历', 'label,', '200', 0, 0);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (80, 34, '项目经验', 'textarea,', '800', 1, 0);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (81, 34, '获奖情况', 'label,', '200', 0, 0);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (82, 34, '本科成绩单', 'pdf,jpg,zip,', '3', 1, 0);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (83, 34, '机考成绩', 'label,', '500', 0, 0);
INSERT INTO `infoitem` (`ID`, `activityID`, `name`, `contentType`, `sizelimit`, `byParticipant`, `display`) VALUES (84, 34, '请输入信息项名称1', 'label,', '500', 0, 1);
COMMIT;

-- ----------------------------
-- Table structure for infos
-- ----------------------------
DROP TABLE IF EXISTS `infos`;
CREATE TABLE `infos` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `activityID` int DEFAULT NULL,
  `participantID` int DEFAULT NULL,
  `infoItemID` int DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `activityID_2` (`activityID`) USING BTREE,
  KEY `participantID` (`participantID`) USING BTREE,
  KEY `infoItemID` (`infoItemID`) USING BTREE,
  CONSTRAINT `infos_ibfk_1` FOREIGN KEY (`activityID`) REFERENCES `activities` (`ID`),
  CONSTRAINT `infos_ibfk_2` FOREIGN KEY (`participantID`) REFERENCES `participants` (`ID`),
  CONSTRAINT `infos_ibfk_3` FOREIGN KEY (`infoItemID`) REFERENCES `infoitem` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2789 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of infos
-- ----------------------------
BEGIN;
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (719, 34, 340, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (720, 34, 340, 67, '20000812');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (721, 34, 340, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (722, 34, 340, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (723, 34, 340, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (724, 34, 340, 71, '扬州大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (725, 34, 340, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (726, 34, 340, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (727, 34, 340, 74, '扬州大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (728, 34, 340, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (729, 34, 340, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (730, 34, 340, 77, '389');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (731, 34, 341, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (732, 34, 341, 67, '19990924');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (733, 34, 341, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (734, 34, 341, 69, '中国共产党党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (735, 34, 341, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (736, 34, 341, 71, '上海立信会计金融学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (737, 34, 341, 72, '电子商务');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (738, 34, 341, 73, '20220617');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (739, 34, 341, 74, '暂无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (740, 34, 341, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (741, 34, 341, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (742, 34, 341, 77, '383');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (743, 34, 342, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (744, 34, 342, 67, '20010817');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (745, 34, 342, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (746, 34, 342, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (747, 34, 342, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (748, 34, 342, 71, '上海应用技术大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (749, 34, 342, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (750, 34, 342, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (751, 34, 342, 74, '上海应用技术大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (752, 34, 342, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (753, 34, 342, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (754, 34, 342, 77, '383');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (755, 34, 343, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (756, 34, 343, 67, '19980106');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (757, 34, 343, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (758, 34, 343, 69, '群众');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (759, 34, 343, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (760, 34, 343, 71, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (761, 34, 343, 72, '服装设计与工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (762, 34, 343, 73, '20200715');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (763, 34, 343, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (764, 34, 343, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (765, 34, 343, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (766, 34, 343, 77, '379');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (767, 34, 344, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (768, 34, 344, 67, '20010219');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (769, 34, 344, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (770, 34, 344, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (771, 34, 344, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (772, 34, 344, 71, '南通大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (773, 34, 344, 72, '物联网工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (774, 34, 344, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (775, 34, 344, 74, '南通大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (776, 34, 344, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (777, 34, 344, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (778, 34, 344, 77, '379');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (779, 34, 345, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (780, 34, 345, 67, '20010423');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (781, 34, 345, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (782, 34, 345, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (783, 34, 345, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (784, 34, 345, 71, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (785, 34, 345, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (786, 34, 345, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (787, 34, 345, 74, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (788, 34, 345, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (789, 34, 345, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (790, 34, 345, 77, '374');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (791, 34, 346, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (792, 34, 346, 67, '19991007');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (793, 34, 346, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (794, 34, 346, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (795, 34, 346, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (796, 34, 346, 71, '山东理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (797, 34, 346, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (798, 34, 346, 73, '20220616');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (799, 34, 346, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (800, 34, 346, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (801, 34, 346, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (802, 34, 346, 77, '365');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (803, 34, 347, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (804, 34, 347, 67, '20000810');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (805, 34, 347, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (806, 34, 347, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (807, 34, 347, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (808, 34, 347, 71, '重庆科技学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (809, 34, 347, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (810, 34, 347, 73, '20230630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (811, 34, 347, 74, '重庆科技学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (812, 34, 347, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (813, 34, 347, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (814, 34, 347, 77, '365');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (815, 34, 348, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (816, 34, 348, 67, '20010213');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (817, 34, 348, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (818, 34, 348, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (819, 34, 348, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (820, 34, 348, 71, '江苏师范大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (821, 34, 348, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (822, 34, 348, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (823, 34, 348, 74, '江苏师范大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (824, 34, 348, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (825, 34, 348, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (826, 34, 348, 77, '363');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (827, 34, 349, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (828, 34, 349, 67, '19960915');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (829, 34, 349, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (830, 34, 349, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (831, 34, 349, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (832, 34, 349, 71, '上海工程技术大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (833, 34, 349, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (834, 34, 349, 73, '20220624');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (835, 34, 349, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (836, 34, 349, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (837, 34, 349, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (838, 34, 349, 77, '363');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (839, 34, 350, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (840, 34, 350, 67, '20000603');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (841, 34, 350, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (842, 34, 350, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (843, 34, 350, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (844, 34, 350, 71, '河北北方学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (845, 34, 350, 72, '信息管理与信息系统');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (846, 34, 350, 73, '20220630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (847, 34, 350, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (848, 34, 350, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (849, 34, 350, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (850, 34, 350, 77, '374');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (851, 34, 351, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (852, 34, 351, 67, '20001224');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (853, 34, 351, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (854, 34, 351, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (855, 34, 351, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (856, 34, 351, 71, '南京信息工程大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (857, 34, 351, 72, '数据科学与大数据技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (858, 34, 351, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (859, 34, 351, 74, '南京信息工程大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (860, 34, 351, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (861, 34, 351, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (862, 34, 351, 77, '418');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (863, 34, 352, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (864, 34, 352, 67, '19990827');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (865, 34, 352, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (866, 34, 352, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (867, 34, 352, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (868, 34, 352, 71, '中原工学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (869, 34, 352, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (870, 34, 352, 73, '20220701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (871, 34, 352, 74, '河南省南阳市卧龙区石桥镇贾寨村大贾寨11组67号');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (872, 34, 352, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (873, 34, 352, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (874, 34, 352, 77, '399');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (875, 34, 353, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (876, 34, 353, 67, '20020305');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (877, 34, 353, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (878, 34, 353, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (879, 34, 353, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (880, 34, 353, 71, '南华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (881, 34, 353, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (882, 34, 353, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (883, 34, 353, 74, '南华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (884, 34, 353, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (885, 34, 353, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (886, 34, 353, 77, '399');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (887, 34, 354, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (888, 34, 354, 67, '20001216');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (889, 34, 354, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (890, 34, 354, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (891, 34, 354, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (892, 34, 354, 71, '盐城工学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (893, 34, 354, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (894, 34, 354, 73, '20230630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (895, 34, 354, 74, '盐城工学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (896, 34, 354, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (897, 34, 354, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (898, 34, 354, 77, '395');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (899, 34, 355, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (900, 34, 355, 67, '19991226');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (901, 34, 355, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (902, 34, 355, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (903, 34, 355, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (904, 34, 355, 71, '上海第二工业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (905, 34, 355, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (906, 34, 355, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (907, 34, 355, 74, '上海第二工业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (908, 34, 355, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (909, 34, 355, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (910, 34, 355, 77, '394');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (911, 34, 356, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (912, 34, 356, 67, '20010907');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (913, 34, 356, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (914, 34, 356, 69, '中国共产党党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (915, 34, 356, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (916, 34, 356, 71, '东华理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (917, 34, 356, 72, '工程管理');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (918, 34, 356, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (919, 34, 356, 74, '东华理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (920, 34, 356, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (921, 34, 356, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (922, 34, 356, 77, '389');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (923, 34, 357, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (924, 34, 357, 67, '20000810');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (925, 34, 357, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (926, 34, 357, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (927, 34, 357, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (928, 34, 357, 71, '长江大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (929, 34, 357, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (930, 34, 357, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (931, 34, 357, 74, '长江大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (932, 34, 357, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (933, 34, 357, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (934, 34, 357, 77, '372');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (935, 34, 358, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (936, 34, 358, 67, '20001226');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (937, 34, 358, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (938, 34, 358, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (939, 34, 358, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (940, 34, 358, 71, '江苏第二师范学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (941, 34, 358, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (942, 34, 358, 73, '20230630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (943, 34, 358, 74, '江苏第二师范学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (944, 34, 358, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (945, 34, 358, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (946, 34, 358, 77, '372');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (947, 34, 359, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (948, 34, 359, 67, '20010202');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (949, 34, 359, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (950, 34, 359, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (951, 34, 359, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (952, 34, 359, 71, '上海师范大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (953, 34, 359, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (954, 34, 359, 73, '20220520');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (955, 34, 359, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (956, 34, 359, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (957, 34, 359, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (958, 34, 359, 77, '368');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (959, 34, 360, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (960, 34, 360, 67, '20001219');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (961, 34, 360, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (962, 34, 360, 69, '中国共产党党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (963, 34, 360, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (964, 34, 360, 71, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (965, 34, 360, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (966, 34, 360, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (967, 34, 360, 74, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (968, 34, 360, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (969, 34, 360, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (970, 34, 360, 77, '367');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (971, 34, 361, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (972, 34, 361, 67, '19981227');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (973, 34, 361, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (974, 34, 361, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (975, 34, 361, 70, '其它在职');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (976, 34, 361, 71, '成都信息工程大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (977, 34, 361, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (978, 34, 361, 73, '20200623');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (979, 34, 361, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (980, 34, 361, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (981, 34, 361, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (982, 34, 361, 77, '374');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (983, 34, 362, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (984, 34, 362, 67, '20010304');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (985, 34, 362, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (986, 34, 362, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (987, 34, 362, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (988, 34, 362, 71, '青岛理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (989, 34, 362, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (990, 34, 362, 73, '20230624');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (991, 34, 362, 74, '青岛理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (992, 34, 362, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (993, 34, 362, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (994, 34, 362, 77, '372');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (995, 34, 363, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (996, 34, 363, 67, '20020125');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (997, 34, 363, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (998, 34, 363, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (999, 34, 363, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1000, 34, 363, 71, '大连工业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1001, 34, 363, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1002, 34, 363, 73, '20230620');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1003, 34, 363, 74, '大连工业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1004, 34, 363, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1005, 34, 363, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1006, 34, 363, 77, '372');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1007, 34, 364, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1008, 34, 364, 67, '20000228');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1009, 34, 364, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1010, 34, 364, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1011, 34, 364, 70, '其它在职');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1012, 34, 364, 71, '上海对外经贸大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1013, 34, 364, 72, '信息管理与信息系统');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1014, 34, 364, 73, '20220525');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1015, 34, 364, 74, '上海对外经贸大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1016, 34, 364, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1017, 34, 364, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1018, 34, 364, 77, '369');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1019, 34, 365, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1020, 34, 365, 67, '20001226');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1021, 34, 365, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1022, 34, 365, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1023, 34, 365, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1024, 34, 365, 71, '南京财经大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1025, 34, 365, 72, '物联网工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1026, 34, 365, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1027, 34, 365, 74, '南京财经大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1028, 34, 365, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1029, 34, 365, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1030, 34, 365, 77, '367');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1031, 34, 366, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1032, 34, 366, 67, '20011223');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1033, 34, 366, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1034, 34, 366, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1035, 34, 366, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1036, 34, 366, 71, '江西农业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1037, 34, 366, 72, '电子商务');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1038, 34, 366, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1039, 34, 366, 74, '江西农业大学计算机与信息工程学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1040, 34, 366, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1041, 34, 366, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1042, 34, 366, 77, '365');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1043, 34, 367, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1044, 34, 367, 67, '20000829');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1045, 34, 367, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1046, 34, 367, 69, '群众');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1047, 34, 367, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1048, 34, 367, 71, '上海海事大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1049, 34, 367, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1050, 34, 367, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1051, 34, 367, 74, '上海海事大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1052, 34, 367, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1053, 34, 367, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1054, 34, 367, 77, '365');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1055, 34, 368, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1056, 34, 368, 67, '20010504');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1057, 34, 368, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1058, 34, 368, 69, '群众');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1059, 34, 368, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1060, 34, 368, 71, '上海海事大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1061, 34, 368, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1062, 34, 368, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1063, 34, 368, 74, '上海海事大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1064, 34, 368, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1065, 34, 368, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1066, 34, 368, 77, '363');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1067, 34, 369, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1068, 34, 369, 67, '20001118');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1069, 34, 369, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1070, 34, 369, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1071, 34, 369, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1072, 34, 369, 71, '浙江万里学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1073, 34, 369, 72, '通信工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1074, 34, 369, 73, '20230610');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1075, 34, 369, 74, '浙江万里学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1076, 34, 369, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1077, 34, 369, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1078, 34, 369, 77, '417');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1079, 34, 370, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1080, 34, 370, 67, '19990925');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1081, 34, 370, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1082, 34, 370, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1083, 34, 370, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1084, 34, 370, 71, '上海应用技术大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1085, 34, 370, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1086, 34, 370, 73, '20220506');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1087, 34, 370, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1088, 34, 370, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1089, 34, 370, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1090, 34, 370, 77, '400');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1091, 34, 371, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1092, 34, 371, 67, '20010420');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1093, 34, 371, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1094, 34, 371, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1095, 34, 371, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1096, 34, 371, 71, '燕山大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1097, 34, 371, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1098, 34, 371, 73, '20230630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1099, 34, 371, 74, '燕山大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1100, 34, 371, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1101, 34, 371, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1102, 34, 371, 77, '398');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1103, 34, 372, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1104, 34, 372, 67, '20000216');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1105, 34, 372, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1106, 34, 372, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1107, 34, 372, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1108, 34, 372, 71, '中国民航大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1109, 34, 372, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1110, 34, 372, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1111, 34, 372, 74, '中国民航大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1112, 34, 372, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1113, 34, 372, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1114, 34, 372, 77, '382');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1115, 34, 373, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1116, 34, 373, 67, '20010510');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1117, 34, 373, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1118, 34, 373, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1119, 34, 373, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1120, 34, 373, 71, '江西师范大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1121, 34, 373, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1122, 34, 373, 73, '20230630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1123, 34, 373, 74, '江西师范大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1124, 34, 373, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1125, 34, 373, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1126, 34, 373, 77, '381');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1127, 34, 374, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1128, 34, 374, 67, '20010722');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1129, 34, 374, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1130, 34, 374, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1131, 34, 374, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1132, 34, 374, 71, '济南大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1133, 34, 374, 72, '信息管理与信息系统');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1134, 34, 374, 73, '20230630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1135, 34, 374, 74, '济南大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1136, 34, 374, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1137, 34, 374, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1138, 34, 374, 77, '379');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1139, 34, 375, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1140, 34, 375, 67, '20010516');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1141, 34, 375, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1142, 34, 375, 69, '中国共产党党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1143, 34, 375, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1144, 34, 375, 71, '莆田学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1145, 34, 375, 72, '智能科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1146, 34, 375, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1147, 34, 375, 74, '莆田学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1148, 34, 375, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1149, 34, 375, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1150, 34, 375, 77, '374');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1151, 34, 376, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1152, 34, 376, 67, '19991230');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1153, 34, 376, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1154, 34, 376, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1155, 34, 376, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1156, 34, 376, 71, '上海海事大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1157, 34, 376, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1158, 34, 376, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1159, 34, 376, 74, '上海海事大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1160, 34, 376, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1161, 34, 376, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1162, 34, 376, 77, '362');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1163, 34, 377, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1164, 34, 377, 67, '20001229');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1165, 34, 377, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1166, 34, 377, 69, '群众');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1167, 34, 377, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1168, 34, 377, 71, '桂林电子科技大学信息科技学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1169, 34, 377, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1170, 34, 377, 73, '20220625');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1171, 34, 377, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1172, 34, 377, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1173, 34, 377, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1174, 34, 377, 77, '395');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1175, 34, 378, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1176, 34, 378, 67, '20000108');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1177, 34, 378, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1178, 34, 378, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1179, 34, 378, 70, '其它在职');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1180, 34, 378, 71, '上海理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1181, 34, 378, 72, '新媒体技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1182, 34, 378, 73, '20220615');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1183, 34, 378, 74, '暂无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1184, 34, 378, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1185, 34, 378, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1186, 34, 378, 77, '393');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1187, 34, 379, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1188, 34, 379, 67, '20010617');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1189, 34, 379, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1190, 34, 379, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1191, 34, 379, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1192, 34, 379, 71, '南通大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1193, 34, 379, 72, '物联网工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1194, 34, 379, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1195, 34, 379, 74, '南通大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1196, 34, 379, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1197, 34, 379, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1198, 34, 379, 77, '389');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1199, 34, 380, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1200, 34, 380, 67, '20010120');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1201, 34, 380, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1202, 34, 380, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1203, 34, 380, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1204, 34, 380, 71, '怀化学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1205, 34, 380, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1206, 34, 380, 73, '20230612');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1207, 34, 380, 74, '怀化学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1208, 34, 380, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1209, 34, 380, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1210, 34, 380, 77, '388');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1211, 34, 381, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1212, 34, 381, 67, '20001216');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1213, 34, 381, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1214, 34, 381, 69, '中国共产党党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1215, 34, 381, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1216, 34, 381, 71, '南京信息工程大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1217, 34, 381, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1218, 34, 381, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1219, 34, 381, 74, '南京信息工程大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1220, 34, 381, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1221, 34, 381, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1222, 34, 381, 77, '384');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1223, 34, 382, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1224, 34, 382, 67, '20000504');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1225, 34, 382, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1226, 34, 382, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1227, 34, 382, 70, '其它在职');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1228, 34, 382, 71, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1229, 34, 382, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1230, 34, 382, 73, '20220620');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1231, 34, 382, 74, '中国电信股份有限公司上海分公司');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1232, 34, 382, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1233, 34, 382, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1234, 34, 382, 77, '378');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1235, 34, 383, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1236, 34, 383, 67, '20000628');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1237, 34, 383, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1238, 34, 383, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1239, 34, 383, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1240, 34, 383, 71, '兰州理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1241, 34, 383, 72, '机械设计制造及其自动化');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1242, 34, 383, 73, '20230625');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1243, 34, 383, 74, '兰州理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1244, 34, 383, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1245, 34, 383, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1246, 34, 383, 77, '375');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1247, 34, 384, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1248, 34, 384, 67, '20011126');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1249, 34, 384, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1250, 34, 384, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1251, 34, 384, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1252, 34, 384, 71, '湖南工业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1253, 34, 384, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1254, 34, 384, 73, '20220625');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1255, 34, 384, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1256, 34, 384, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1257, 34, 384, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1258, 34, 384, 77, '374');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1259, 34, 385, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1260, 34, 385, 67, '20001229');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1261, 34, 385, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1262, 34, 385, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1263, 34, 385, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1264, 34, 385, 71, '南京信息工程大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1265, 34, 385, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1266, 34, 385, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1267, 34, 385, 74, '南京信息工程大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1268, 34, 385, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1269, 34, 385, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1270, 34, 385, 77, '372');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1271, 34, 386, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1272, 34, 386, 67, '20000104');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1273, 34, 386, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1274, 34, 386, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1275, 34, 386, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1276, 34, 386, 71, '福建工程学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1277, 34, 386, 72, '物联网工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1278, 34, 386, 73, '20220614');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1279, 34, 386, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1280, 34, 386, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1281, 34, 386, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1282, 34, 386, 77, '371');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1283, 34, 387, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1284, 34, 387, 67, '20010609');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1285, 34, 387, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1286, 34, 387, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1287, 34, 387, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1288, 34, 387, 71, '山东理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1289, 34, 387, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1290, 34, 387, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1291, 34, 387, 74, '山东理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1292, 34, 387, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1293, 34, 387, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1294, 34, 387, 77, '369');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1295, 34, 388, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1296, 34, 388, 67, '20000915');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1297, 34, 388, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1298, 34, 388, 69, '群众');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1299, 34, 388, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1300, 34, 388, 71, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1301, 34, 388, 72, '自动化');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1302, 34, 388, 73, '20220620');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1303, 34, 388, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1304, 34, 388, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1305, 34, 388, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1306, 34, 388, 77, '415');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1307, 34, 389, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1308, 34, 389, 67, '20010907');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1309, 34, 389, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1310, 34, 389, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1311, 34, 389, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1312, 34, 389, 71, '南京审计大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1313, 34, 389, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1314, 34, 389, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1315, 34, 389, 74, '南京审计大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1316, 34, 389, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1317, 34, 389, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1318, 34, 389, 77, '392');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1319, 34, 390, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1320, 34, 390, 67, '20010607');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1321, 34, 390, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1322, 34, 390, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1323, 34, 390, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1324, 34, 390, 71, '安徽工程大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1325, 34, 390, 72, '数据科学与大数据技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1326, 34, 390, 73, '20230630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1327, 34, 390, 74, '安徽工程大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1328, 34, 390, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1329, 34, 390, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1330, 34, 390, 77, '390');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1331, 34, 391, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1332, 34, 391, 67, '20001209');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1333, 34, 391, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1334, 34, 391, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1335, 34, 391, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1336, 34, 391, 71, '上海应用技术大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1337, 34, 391, 72, '材料科学与工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1338, 34, 391, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1339, 34, 391, 74, '上海应用技术大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1340, 34, 391, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1341, 34, 391, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1342, 34, 391, 77, '387');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1343, 34, 392, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1344, 34, 392, 67, '19990129');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1345, 34, 392, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1346, 34, 392, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1347, 34, 392, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1348, 34, 392, 71, '南京工程学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1349, 34, 392, 72, '信息工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1350, 34, 392, 73, '20220613');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1351, 34, 392, 74, '暂无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1352, 34, 392, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1353, 34, 392, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1354, 34, 392, 77, '365');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1355, 34, 393, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1356, 34, 393, 67, '19990323');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1357, 34, 393, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1358, 34, 393, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1359, 34, 393, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1360, 34, 393, 71, '徐州工程学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1361, 34, 393, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1362, 34, 393, 73, '20220630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1363, 34, 393, 74, '家里');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1364, 34, 393, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1365, 34, 393, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1366, 34, 393, 77, '363');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1367, 34, 394, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1368, 34, 394, 67, '20011006');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1369, 34, 394, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1370, 34, 394, 69, '中国共产党党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1371, 34, 394, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1372, 34, 394, 71, '安徽建筑大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1373, 34, 394, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1374, 34, 394, 73, '20230601');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1375, 34, 394, 74, '安徽建筑大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1376, 34, 394, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1377, 34, 394, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1378, 34, 394, 77, '362');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1379, 34, 395, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1380, 34, 395, 67, '20000916');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1381, 34, 395, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1382, 34, 395, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1383, 34, 395, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1384, 34, 395, 71, '安徽财经大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1385, 34, 395, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1386, 34, 395, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1387, 34, 395, 74, '安徽财经大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1388, 34, 395, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1389, 34, 395, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1390, 34, 395, 77, '385');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1391, 34, 396, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1392, 34, 396, 67, '19990628');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1393, 34, 396, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1394, 34, 396, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1395, 34, 396, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1396, 34, 396, 71, '山东交通学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1397, 34, 396, 72, '信息与计算科学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1398, 34, 396, 73, '20210630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1399, 34, 396, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1400, 34, 396, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1401, 34, 396, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1402, 34, 396, 77, '382');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1403, 34, 397, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1404, 34, 397, 67, '20010312');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1405, 34, 397, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1406, 34, 397, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1407, 34, 397, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1408, 34, 397, 71, '安徽理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1409, 34, 397, 72, '城市地下空间工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1410, 34, 397, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1411, 34, 397, 74, '安徽理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1412, 34, 397, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1413, 34, 397, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1414, 34, 397, 77, '381');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1415, 34, 398, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1416, 34, 398, 67, '20000919');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1417, 34, 398, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1418, 34, 398, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1419, 34, 398, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1420, 34, 398, 71, '浙江工业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1421, 34, 398, 72, '信息与计算科学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1422, 34, 398, 73, '20230618');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1423, 34, 398, 74, '浙江工业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1424, 34, 398, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1425, 34, 398, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1426, 34, 398, 77, '400');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1427, 34, 399, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1428, 34, 399, 67, '20010310');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1429, 34, 399, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1430, 34, 399, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1431, 34, 399, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1432, 34, 399, 71, '江西财经大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1433, 34, 399, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1434, 34, 399, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1435, 34, 399, 74, '江西财经大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1436, 34, 399, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1437, 34, 399, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1438, 34, 399, 77, '398');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1439, 34, 400, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1440, 34, 400, 67, '20010420');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1441, 34, 400, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1442, 34, 400, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1443, 34, 400, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1444, 34, 400, 71, '江西理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1445, 34, 400, 72, '信息与计算科学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1446, 34, 400, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1447, 34, 400, 74, '江西理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1448, 34, 400, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1449, 34, 400, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1450, 34, 400, 77, '396');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1451, 34, 401, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1452, 34, 401, 67, '20000902');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1453, 34, 401, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1454, 34, 401, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1455, 34, 401, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1456, 34, 401, 71, '兰州理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1457, 34, 401, 72, '物联网工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1458, 34, 401, 73, '20220620');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1459, 34, 401, 74, '江苏省徐州市云龙区尚仕名邸二期');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1460, 34, 401, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1461, 34, 401, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1462, 34, 401, 77, '367');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1463, 34, 402, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1464, 34, 402, 67, '19980730');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1465, 34, 402, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1466, 34, 402, 69, '中国共产党党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1467, 34, 402, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1468, 34, 402, 71, '东华理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1469, 34, 402, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1470, 34, 402, 73, '20220701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1471, 34, 402, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1472, 34, 402, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1473, 34, 402, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1474, 34, 402, 77, '366');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1475, 34, 403, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1476, 34, 403, 67, '20010611');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1477, 34, 403, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1478, 34, 403, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1479, 34, 403, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1480, 34, 403, 71, '南京林业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1481, 34, 403, 72, '金融工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1482, 34, 403, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1483, 34, 403, 74, '南京林业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1484, 34, 403, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1485, 34, 403, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1486, 34, 403, 77, '371');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1487, 34, 404, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1488, 34, 404, 67, '19990511');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1489, 34, 404, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1490, 34, 404, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1491, 34, 404, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1492, 34, 404, 71, '中北大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1493, 34, 404, 72, '物联网工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1494, 34, 404, 73, '20220622');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1495, 34, 404, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1496, 34, 404, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1497, 34, 404, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1498, 34, 404, 77, '369');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1499, 34, 405, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1500, 34, 405, 67, '20021016');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1501, 34, 405, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1502, 34, 405, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1503, 34, 405, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1504, 34, 405, 71, '湖南农业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1505, 34, 405, 72, '数据科学与大数据技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1506, 34, 405, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1507, 34, 405, 74, '湖南农业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1508, 34, 405, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1509, 34, 405, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1510, 34, 405, 77, '367');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1511, 34, 406, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1512, 34, 406, 67, '20001028');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1513, 34, 406, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1514, 34, 406, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1515, 34, 406, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1516, 34, 406, 71, '陕西科技大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1517, 34, 406, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1518, 34, 406, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1519, 34, 406, 74, '陕西科技大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1520, 34, 406, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1521, 34, 406, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1522, 34, 406, 77, '366');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1523, 34, 407, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1524, 34, 407, 67, '20010216');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1525, 34, 407, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1526, 34, 407, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1527, 34, 407, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1528, 34, 407, 71, '南通大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1529, 34, 407, 72, '软件工程（嵌入式培养）');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1530, 34, 407, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1531, 34, 407, 74, '江苏省南通市崇川区南通大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1532, 34, 407, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1533, 34, 407, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1534, 34, 407, 77, '365');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1535, 34, 408, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1536, 34, 408, 67, '20011121');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1537, 34, 408, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1538, 34, 408, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1539, 34, 408, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1540, 34, 408, 71, '郑州大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1541, 34, 408, 72, '地理信息科学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1542, 34, 408, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1543, 34, 408, 74, '郑州大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1544, 34, 408, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1545, 34, 408, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1546, 34, 408, 77, '414');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1547, 34, 409, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1548, 34, 409, 67, '20010915');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1549, 34, 409, 68, '布依族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1550, 34, 409, 69, '群众');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1551, 34, 409, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1552, 34, 409, 71, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1553, 34, 409, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1554, 34, 409, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1555, 34, 409, 74, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1556, 34, 409, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1557, 34, 409, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1558, 34, 409, 77, '387');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1559, 34, 410, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1560, 34, 410, 67, '20010206');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1561, 34, 410, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1562, 34, 410, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1563, 34, 410, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1564, 34, 410, 71, '扬州大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1565, 34, 410, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1566, 34, 410, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1567, 34, 410, 74, '扬州大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1568, 34, 410, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1569, 34, 410, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1570, 34, 410, 77, '363');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1571, 34, 411, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1572, 34, 411, 67, '20020212');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1573, 34, 411, 68, '瑶族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1574, 34, 411, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1575, 34, 411, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1576, 34, 411, 71, '武汉理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1577, 34, 411, 72, '能源与动力工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1578, 34, 411, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1579, 34, 411, 74, '武汉理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1580, 34, 411, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1581, 34, 411, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1582, 34, 411, 77, '362');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1583, 34, 412, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1584, 34, 412, 67, '20000112');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1585, 34, 412, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1586, 34, 412, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1587, 34, 412, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1588, 34, 412, 71, '郑州工商学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1589, 34, 412, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1590, 34, 412, 73, '20220701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1591, 34, 412, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1592, 34, 412, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1593, 34, 412, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1594, 34, 412, 77, '362');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1595, 34, 413, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1596, 34, 413, 67, '20010302');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1597, 34, 413, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1598, 34, 413, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1599, 34, 413, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1600, 34, 413, 71, '安徽理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1601, 34, 413, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1602, 34, 413, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1603, 34, 413, 74, '安徽理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1604, 34, 413, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1605, 34, 413, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1606, 34, 413, 77, '385');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1607, 34, 414, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1608, 34, 414, 67, '20000524');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1609, 34, 414, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1610, 34, 414, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1611, 34, 414, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1612, 34, 414, 71, '山东理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1613, 34, 414, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1614, 34, 414, 73, '20220616');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1615, 34, 414, 74, '山东理工大学文学与新闻传播学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1616, 34, 414, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1617, 34, 414, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1618, 34, 414, 77, '382');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1619, 34, 415, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1620, 34, 415, 67, '20010526');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1621, 34, 415, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1622, 34, 415, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1623, 34, 415, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1624, 34, 415, 71, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1625, 34, 415, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1626, 34, 415, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1627, 34, 415, 74, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1628, 34, 415, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1629, 34, 415, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1630, 34, 415, 77, '381');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1631, 34, 416, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1632, 34, 416, 67, '20000124');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1633, 34, 416, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1634, 34, 416, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1635, 34, 416, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1636, 34, 416, 71, '浙江中医药大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1637, 34, 416, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1638, 34, 416, 73, '20220610');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1639, 34, 416, 74, '艾依佳纸业有限公司');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1640, 34, 416, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1641, 34, 416, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1642, 34, 416, 77, '377');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1643, 34, 417, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1644, 34, 417, 67, '20010914');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1645, 34, 417, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1646, 34, 417, 69, '群众');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1647, 34, 417, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1648, 34, 417, 71, '南通大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1649, 34, 417, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1650, 34, 417, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1651, 34, 417, 74, '南通大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1652, 34, 417, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1653, 34, 417, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1654, 34, 417, 77, '375');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1655, 34, 418, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1656, 34, 418, 67, '20010408');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1657, 34, 418, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1658, 34, 418, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1659, 34, 418, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1660, 34, 418, 71, '中国计量大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1661, 34, 418, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1662, 34, 418, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1663, 34, 418, 74, '中国计量大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1664, 34, 418, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1665, 34, 418, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1666, 34, 418, 77, '402');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1667, 34, 419, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1668, 34, 419, 67, '20010109');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1669, 34, 419, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1670, 34, 419, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1671, 34, 419, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1672, 34, 419, 71, '深圳技术大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1673, 34, 419, 72, '物联网工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1674, 34, 419, 73, '20230630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1675, 34, 419, 74, '深圳技术大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1676, 34, 419, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1677, 34, 419, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1678, 34, 419, 77, '398');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1679, 34, 420, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1680, 34, 420, 67, '20001007');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1681, 34, 420, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1682, 34, 420, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1683, 34, 420, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1684, 34, 420, 71, '上海理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1685, 34, 420, 72, '信息管理与信息系统');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1686, 34, 420, 73, '20230630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1687, 34, 420, 74, '上海理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1688, 34, 420, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1689, 34, 420, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1690, 34, 420, 77, '396');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1691, 34, 421, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1692, 34, 421, 67, '20000104');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1693, 34, 421, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1694, 34, 421, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1695, 34, 421, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1696, 34, 421, 71, '武汉科技大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1697, 34, 421, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1698, 34, 421, 73, '20210630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1699, 34, 421, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1700, 34, 421, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1701, 34, 421, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1702, 34, 421, 77, '392');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1703, 34, 422, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1704, 34, 422, 67, '20010608');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1705, 34, 422, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1706, 34, 422, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1707, 34, 422, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1708, 34, 422, 71, '湘潭大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1709, 34, 422, 72, '环保设备工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1710, 34, 422, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1711, 34, 422, 74, '湘潭大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1712, 34, 422, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1713, 34, 422, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1714, 34, 422, 77, '390');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1715, 34, 423, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1716, 34, 423, 67, '19990928');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1717, 34, 423, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1718, 34, 423, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1719, 34, 423, 70, '其它在职');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1720, 34, 423, 71, '郑州航空工业管理学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1721, 34, 423, 72, '电气工程及其自动化');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1722, 34, 423, 73, '20210701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1723, 34, 423, 74, '中国能源建设集团江苏省电力建设第一工程有限公司');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1724, 34, 423, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1725, 34, 423, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1726, 34, 423, 77, '373');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1727, 34, 424, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1728, 34, 424, 67, '20010119');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1729, 34, 424, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1730, 34, 424, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1731, 34, 424, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1732, 34, 424, 71, '南通大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1733, 34, 424, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1734, 34, 424, 73, '20230630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1735, 34, 424, 74, '南通大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1736, 34, 424, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1737, 34, 424, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1738, 34, 424, 77, '373');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1739, 34, 425, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1740, 34, 425, 67, '20010731');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1741, 34, 425, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1742, 34, 425, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1743, 34, 425, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1744, 34, 425, 71, '上海工程技术大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1745, 34, 425, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1746, 34, 425, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1747, 34, 425, 74, '上海工程技术大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1748, 34, 425, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1749, 34, 425, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1750, 34, 425, 77, '381');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1751, 34, 426, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1752, 34, 426, 67, '20001014');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1753, 34, 426, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1754, 34, 426, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1755, 34, 426, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1756, 34, 426, 71, '南京林业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1757, 34, 426, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1758, 34, 426, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1759, 34, 426, 74, '南京林业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1760, 34, 426, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1761, 34, 426, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1762, 34, 426, 77, '377');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1763, 34, 427, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1764, 34, 427, 67, '20001217');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1765, 34, 427, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1766, 34, 427, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1767, 34, 427, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1768, 34, 427, 71, '河北经贸大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1769, 34, 427, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1770, 34, 427, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1771, 34, 427, 74, '河北经贸大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1772, 34, 427, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1773, 34, 427, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1774, 34, 427, 77, '375');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1775, 34, 428, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1776, 34, 428, 67, '20010323');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1777, 34, 428, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1778, 34, 428, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1779, 34, 428, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1780, 34, 428, 71, '山东科技大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1781, 34, 428, 72, '信息与计算科学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1782, 34, 428, 73, '20230630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1783, 34, 428, 74, '山东科技大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1784, 34, 428, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1785, 34, 428, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1786, 34, 428, 77, '373');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1787, 34, 429, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1788, 34, 429, 67, '20001228');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1789, 34, 429, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1790, 34, 429, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1791, 34, 429, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1792, 34, 429, 71, '汕头大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1793, 34, 429, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1794, 34, 429, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1795, 34, 429, 74, '汕头大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1796, 34, 429, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1797, 34, 429, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1798, 34, 429, 77, '407');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1799, 34, 430, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1800, 34, 430, 67, '20010601');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1801, 34, 430, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1802, 34, 430, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1803, 34, 430, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1804, 34, 430, 71, '常州工学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1805, 34, 430, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1806, 34, 430, 73, '20230630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1807, 34, 430, 74, '常州工学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1808, 34, 430, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1809, 34, 430, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1810, 34, 430, 77, '404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1811, 34, 431, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1812, 34, 431, 67, '20010918');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1813, 34, 431, 68, '布依族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1814, 34, 431, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1815, 34, 431, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1816, 34, 431, 71, '上海理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1817, 34, 431, 72, '数据科学与大数据技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1818, 34, 431, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1819, 34, 431, 74, '上海理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1820, 34, 431, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1821, 34, 431, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1822, 34, 431, 77, '397');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1823, 34, 432, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1824, 34, 432, 67, '19990810');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1825, 34, 432, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1826, 34, 432, 69, '中国共产党党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1827, 34, 432, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1828, 34, 432, 71, '中国民航大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1829, 34, 432, 72, '信息安全');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1830, 34, 432, 73, '20210621');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1831, 34, 432, 74, '山东学而行教育培训学校');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1832, 34, 432, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1833, 34, 432, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1834, 34, 432, 77, '396');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1835, 34, 433, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1836, 34, 433, 67, '20010512');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1837, 34, 433, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1838, 34, 433, 69, '中国共产党党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1839, 34, 433, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1840, 34, 433, 71, '重庆理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1841, 34, 433, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1842, 34, 433, 73, '20230628');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1843, 34, 433, 74, '重庆理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1844, 34, 433, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1845, 34, 433, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1846, 34, 433, 77, '392');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1847, 34, 434, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1848, 34, 434, 67, '20020113');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1849, 34, 434, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1850, 34, 434, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1851, 34, 434, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1852, 34, 434, 71, '齐鲁工业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1853, 34, 434, 72, '物联网工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1854, 34, 434, 73, '20230626');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1855, 34, 434, 74, '齐鲁工业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1856, 34, 434, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1857, 34, 434, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1858, 34, 434, 77, '390');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1859, 34, 435, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1860, 34, 435, 67, '19981211');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1861, 34, 435, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1862, 34, 435, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1863, 34, 435, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1864, 34, 435, 71, '上海政法学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1865, 34, 435, 72, '财务管理');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1866, 34, 435, 73, '20210622');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1867, 34, 435, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1868, 34, 435, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1869, 34, 435, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1870, 34, 435, 77, '386');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1871, 34, 436, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1872, 34, 436, 67, '20000716');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1873, 34, 436, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1874, 34, 436, 69, '中国共产党党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1875, 34, 436, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1876, 34, 436, 71, '合肥学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1877, 34, 436, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1878, 34, 436, 73, '20220627');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1879, 34, 436, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1880, 34, 436, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1881, 34, 436, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1882, 34, 436, 77, '366');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1883, 34, 437, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1884, 34, 437, 67, '19991202');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1885, 34, 437, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1886, 34, 437, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1887, 34, 437, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1888, 34, 437, 71, '安庆师范大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1889, 34, 437, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1890, 34, 437, 73, '20220710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1891, 34, 437, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1892, 34, 437, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1893, 34, 437, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1894, 34, 437, 77, '364');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1895, 34, 438, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1896, 34, 438, 67, '20010704');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1897, 34, 438, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1898, 34, 438, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1899, 34, 438, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1900, 34, 438, 71, '安徽工业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1901, 34, 438, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1902, 34, 438, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1903, 34, 438, 74, '安徽工业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1904, 34, 438, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1905, 34, 438, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1906, 34, 438, 77, '364');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1907, 34, 439, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1908, 34, 439, 67, '19991206');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1909, 34, 439, 68, '壮族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1910, 34, 439, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1911, 34, 439, 70, '其它在职');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1912, 34, 439, 71, '杭州电子科技大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1913, 34, 439, 72, '信息管理与信息系统');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1914, 34, 439, 73, '20220614');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1915, 34, 439, 74, '普华永道信息技术（上海）有限公司');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1916, 34, 439, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1917, 34, 439, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1918, 34, 439, 77, '362');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1919, 34, 440, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1920, 34, 440, 67, '20010219');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1921, 34, 440, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1922, 34, 440, 69, '中国共产党党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1923, 34, 440, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1924, 34, 440, 71, '闽南师范大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1925, 34, 440, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1926, 34, 440, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1927, 34, 440, 74, '闽南师范大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1928, 34, 440, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1929, 34, 440, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1930, 34, 440, 77, '362');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1931, 34, 441, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1932, 34, 441, 67, '19971013');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1933, 34, 441, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1934, 34, 441, 69, '群众');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1935, 34, 441, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1936, 34, 441, 71, '东华理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1937, 34, 441, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1938, 34, 441, 73, '20210701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1939, 34, 441, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1940, 34, 441, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1941, 34, 441, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1942, 34, 441, 77, '386');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1943, 34, 442, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1944, 34, 442, 67, '20010725');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1945, 34, 442, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1946, 34, 442, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1947, 34, 442, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1948, 34, 442, 71, '江苏大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1949, 34, 442, 72, '智能科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1950, 34, 442, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1951, 34, 442, 74, '江苏大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1952, 34, 442, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1953, 34, 442, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1954, 34, 442, 77, '382');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1955, 34, 443, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1956, 34, 443, 67, '20010316');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1957, 34, 443, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1958, 34, 443, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1959, 34, 443, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1960, 34, 443, 71, '黄山学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1961, 34, 443, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1962, 34, 443, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1963, 34, 443, 74, '黄山学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1964, 34, 443, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1965, 34, 443, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1966, 34, 443, 77, '373');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1967, 34, 444, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1968, 34, 444, 67, '20001112');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1969, 34, 444, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1970, 34, 444, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1971, 34, 444, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1972, 34, 444, 71, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1973, 34, 444, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1974, 34, 444, 73, '20230619');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1975, 34, 444, 74, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1976, 34, 444, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1977, 34, 444, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1978, 34, 444, 77, '371');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1979, 34, 445, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1980, 34, 445, 67, '20010801');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1981, 34, 445, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1982, 34, 445, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1983, 34, 445, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1984, 34, 445, 71, '黄山学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1985, 34, 445, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1986, 34, 445, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1987, 34, 445, 74, '黄山学院率水校区');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1988, 34, 445, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1989, 34, 445, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1990, 34, 445, 77, '369');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1991, 34, 446, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1992, 34, 446, 67, '20010119');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1993, 34, 446, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1994, 34, 446, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1995, 34, 446, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1996, 34, 446, 71, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1997, 34, 446, 72, '信息安全');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1998, 34, 446, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (1999, 34, 446, 74, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2000, 34, 446, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2001, 34, 446, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2002, 34, 446, 77, '367');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2003, 34, 447, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2004, 34, 447, 67, '20000828');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2005, 34, 447, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2006, 34, 447, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2007, 34, 447, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2008, 34, 447, 71, '衡阳师范学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2009, 34, 447, 72, '信息与计算科学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2010, 34, 447, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2011, 34, 447, 74, '衡阳师范学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2012, 34, 447, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2013, 34, 447, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2014, 34, 447, 77, '373');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2015, 34, 448, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2016, 34, 448, 67, '19961119');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2017, 34, 448, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2018, 34, 448, 69, '群众');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2019, 34, 448, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2020, 34, 448, 71, '南京财经大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2021, 34, 448, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2022, 34, 448, 73, '20190619');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2023, 34, 448, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2024, 34, 448, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2025, 34, 448, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2026, 34, 448, 77, '371');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2027, 34, 449, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2028, 34, 449, 67, '20001215');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2029, 34, 449, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2030, 34, 449, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2031, 34, 449, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2032, 34, 449, 71, '安徽建筑大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2033, 34, 449, 72, '网络工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2034, 34, 449, 73, '20220701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2035, 34, 449, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2036, 34, 449, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2037, 34, 449, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2038, 34, 449, 77, '370');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2039, 34, 450, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2040, 34, 450, 67, '20010124');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2041, 34, 450, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2042, 34, 450, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2043, 34, 450, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2044, 34, 450, 71, '上海师范大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2045, 34, 450, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2046, 34, 450, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2047, 34, 450, 74, '上海师范大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2048, 34, 450, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2049, 34, 450, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2050, 34, 450, 77, '367');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2051, 34, 451, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2052, 34, 451, 67, '20011013');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2053, 34, 451, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2054, 34, 451, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2055, 34, 451, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2056, 34, 451, 71, '南京林业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2057, 34, 451, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2058, 34, 451, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2059, 34, 451, 74, '南京林业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2060, 34, 451, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2061, 34, 451, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2062, 34, 451, 77, '366');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2063, 34, 452, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2064, 34, 452, 67, '19991226');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2065, 34, 452, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2066, 34, 452, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2067, 34, 452, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2068, 34, 452, 71, '常熟理工学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2069, 34, 452, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2070, 34, 452, 73, '20230630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2071, 34, 452, 74, '常熟理工学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2072, 34, 452, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2073, 34, 452, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2074, 34, 452, 77, '364');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2075, 34, 453, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2076, 34, 453, 67, '20010421');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2077, 34, 453, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2078, 34, 453, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2079, 34, 453, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2080, 34, 453, 71, '浙江理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2081, 34, 453, 72, '计算机科学与技术(全英文授课班)(3658)');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2082, 34, 453, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2083, 34, 453, 74, '浙江理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2084, 34, 453, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2085, 34, 453, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2086, 34, 453, 77, '405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2087, 34, 454, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2088, 34, 454, 67, '20021203');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2089, 34, 454, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2090, 34, 454, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2091, 34, 454, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2092, 34, 454, 71, '武汉工程大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2093, 34, 454, 72, '智能科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2094, 34, 454, 73, '20230630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2095, 34, 454, 74, '武汉工程大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2096, 34, 454, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2097, 34, 454, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2098, 34, 454, 77, '404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2099, 34, 455, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2100, 34, 455, 67, '19990428');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2101, 34, 455, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2102, 34, 455, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2103, 34, 455, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2104, 34, 455, 71, '南京信息工程大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2105, 34, 455, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2106, 34, 455, 73, '20210620');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2107, 34, 455, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2108, 34, 455, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2109, 34, 455, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2110, 34, 455, 77, '397');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2111, 34, 456, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2112, 34, 456, 67, '19990927');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2113, 34, 456, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2114, 34, 456, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2115, 34, 456, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2116, 34, 456, 71, '常熟理工学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2117, 34, 456, 72, '经济与金融');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2118, 34, 456, 73, '20210621');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2119, 34, 456, 74, '自由职业');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2120, 34, 456, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2121, 34, 456, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2122, 34, 456, 77, '397');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2123, 34, 457, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2124, 34, 457, 67, '19990205');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2125, 34, 457, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2126, 34, 457, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2127, 34, 457, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2128, 34, 457, 71, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2129, 34, 457, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2130, 34, 457, 73, '20200629');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2131, 34, 457, 74, '安徽省-合肥市-肥东县-肥东新城开发区-镇西路禹洲中央广场a区2栋');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2132, 34, 457, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2133, 34, 457, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2134, 34, 457, 77, '391');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2135, 34, 458, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2136, 34, 458, 67, '20010215');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2137, 34, 458, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2138, 34, 458, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2139, 34, 458, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2140, 34, 458, 71, '盐城工学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2141, 34, 458, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2142, 34, 458, 73, '20220616');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2143, 34, 458, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2144, 34, 458, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2145, 34, 458, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2146, 34, 458, 77, '375');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2147, 34, 459, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2148, 34, 459, 67, '19981124');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2149, 34, 459, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2150, 34, 459, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2151, 34, 459, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2152, 34, 459, 71, '南京大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2153, 34, 459, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2154, 34, 459, 73, '20210630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2155, 34, 459, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2156, 34, 459, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2157, 34, 459, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2158, 34, 459, 77, '373');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2159, 34, 460, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2160, 34, 460, 67, '19971110');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2161, 34, 460, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2162, 34, 460, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2163, 34, 460, 70, '其它在职');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2164, 34, 460, 71, '江苏大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2165, 34, 460, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2166, 34, 460, 73, '20220617');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2167, 34, 460, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2168, 34, 460, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2169, 34, 460, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2170, 34, 460, 77, '364');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2171, 34, 461, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2172, 34, 461, 67, '20020309');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2173, 34, 461, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2174, 34, 461, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2175, 34, 461, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2176, 34, 461, 71, '上海电力大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2177, 34, 461, 72, '信息安全');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2178, 34, 461, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2179, 34, 461, 74, '上海市浦东新区南汇新城镇沪城环路1851号上海电力大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2180, 34, 461, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2181, 34, 461, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2182, 34, 461, 77, '362');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2183, 34, 462, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2184, 34, 462, 67, '20010220');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2185, 34, 462, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2186, 34, 462, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2187, 34, 462, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2188, 34, 462, 71, '南京邮电大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2189, 34, 462, 72, '软件工程（嵌入式培养）');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2190, 34, 462, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2191, 34, 462, 74, '南京邮电大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2192, 34, 462, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2193, 34, 462, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2194, 34, 462, 77, '362');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2195, 34, 463, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2196, 34, 463, 67, '20010508');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2197, 34, 463, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2198, 34, 463, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2199, 34, 463, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2200, 34, 463, 71, '九江学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2201, 34, 463, 72, '数据科学与大数据技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2202, 34, 463, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2203, 34, 463, 74, '九江学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2204, 34, 463, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2205, 34, 463, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2206, 34, 463, 77, '391');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2207, 34, 464, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2208, 34, 464, 67, '20010329');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2209, 34, 464, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2210, 34, 464, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2211, 34, 464, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2212, 34, 464, 71, '江苏海洋大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2213, 34, 464, 72, '数据科学与大数据技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2214, 34, 464, 73, '20230630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2215, 34, 464, 74, '江苏海洋大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2216, 34, 464, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2217, 34, 464, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2218, 34, 464, 77, '386');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2219, 34, 465, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2220, 34, 465, 67, '20010514');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2221, 34, 465, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2222, 34, 465, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2223, 34, 465, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2224, 34, 465, 71, '东华理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2225, 34, 465, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2226, 34, 465, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2227, 34, 465, 74, '东华理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2228, 34, 465, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2229, 34, 465, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2230, 34, 465, 77, '386');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2231, 34, 466, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2232, 34, 466, 67, '20000704');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2233, 34, 466, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2234, 34, 466, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2235, 34, 466, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2236, 34, 466, 71, '中国石油大学(华东)');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2237, 34, 466, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2238, 34, 466, 73, '20220620');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2239, 34, 466, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2240, 34, 466, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2241, 34, 466, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2242, 34, 466, 77, '382');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2243, 34, 467, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2244, 34, 467, 67, '20001125');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2245, 34, 467, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2246, 34, 467, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2247, 34, 467, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2248, 34, 467, 71, '青岛理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2249, 34, 467, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2250, 34, 467, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2251, 34, 467, 74, '青岛理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2252, 34, 467, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2253, 34, 467, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2254, 34, 467, 77, '381');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2255, 34, 468, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2256, 34, 468, 67, '20010608');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2257, 34, 468, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2258, 34, 468, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2259, 34, 468, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2260, 34, 468, 71, '青岛工学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2261, 34, 468, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2262, 34, 468, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2263, 34, 468, 74, '青岛工学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2264, 34, 468, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2265, 34, 468, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2266, 34, 468, 77, '376');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2267, 34, 469, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2268, 34, 469, 67, '20000906');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2269, 34, 469, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2270, 34, 469, 69, '群众');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2271, 34, 469, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2272, 34, 469, 71, '华北理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2273, 34, 469, 72, '过程装备与控制工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2274, 34, 469, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2275, 34, 469, 74, '华北理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2276, 34, 469, 75, '081200');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2277, 34, 469, 76, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2278, 34, 469, 77, '327');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2279, 34, 470, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2280, 34, 470, 67, '20010319');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2281, 34, 470, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2282, 34, 470, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2283, 34, 470, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2284, 34, 470, 71, '杭州电子科技大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2285, 34, 470, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2286, 34, 470, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2287, 34, 470, 74, '杭州电子科技大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2288, 34, 470, 75, '081200');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2289, 34, 470, 76, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2290, 34, 470, 77, '324');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2291, 34, 471, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2292, 34, 471, 67, '20010313');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2293, 34, 471, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2294, 34, 471, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2295, 34, 471, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2296, 34, 471, 71, '南通大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2297, 34, 471, 72, '数据科学与大数据技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2298, 34, 471, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2299, 34, 471, 74, '南通大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2300, 34, 471, 75, '081200');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2301, 34, 471, 76, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2302, 34, 471, 77, '320');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2303, 34, 472, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2304, 34, 472, 67, '20010622');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2305, 34, 472, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2306, 34, 472, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2307, 34, 472, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2308, 34, 472, 71, '东华理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2309, 34, 472, 72, '信息管理与信息系统');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2310, 34, 472, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2311, 34, 472, 74, '东华理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2312, 34, 472, 75, '081200');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2313, 34, 472, 76, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2314, 34, 472, 77, '308');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2315, 34, 473, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2316, 34, 473, 67, '20011013');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2317, 34, 473, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2318, 34, 473, 69, '中国共产党预备党员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2319, 34, 473, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2320, 34, 473, 71, '南京邮电大学通达学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2321, 34, 473, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2322, 34, 473, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2323, 34, 473, 74, '南京邮电大学通达学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2324, 34, 473, 75, '081200');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2325, 34, 473, 76, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2326, 34, 473, 77, '305');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2327, 34, 474, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2328, 34, 474, 67, '20001212');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2329, 34, 474, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2330, 34, 474, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2331, 34, 474, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2332, 34, 474, 71, '上海应用技术大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2333, 34, 474, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2334, 34, 474, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2335, 34, 474, 74, '上海应用技术大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2336, 34, 474, 75, '081200');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2337, 34, 474, 76, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2338, 34, 474, 77, '303');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2339, 34, 475, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2340, 34, 475, 67, '20010126');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2341, 34, 475, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2342, 34, 475, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2343, 34, 475, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2344, 34, 475, 71, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2345, 34, 475, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2346, 34, 475, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2347, 34, 475, 74, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2348, 34, 475, 75, '083500');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2349, 34, 475, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2350, 34, 475, 77, '301');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2351, 34, 476, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2352, 34, 476, 67, '20001026');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2353, 34, 476, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2354, 34, 476, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2355, 34, 476, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2356, 34, 476, 71, '许昌学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2357, 34, 476, 72, '机械电子工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2358, 34, 476, 73, '20220701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2359, 34, 476, 74, '在家备考');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2360, 34, 476, 75, '081200');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2361, 34, 476, 76, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2362, 34, 476, 77, '318');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2363, 34, 477, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2364, 34, 477, 67, '20010301');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2365, 34, 477, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2366, 34, 477, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2367, 34, 477, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2368, 34, 477, 71, '上海理工大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2369, 34, 477, 72, '信息管理与信息系统');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2370, 34, 477, 73, '20220615');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2371, 34, 477, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2372, 34, 477, 75, '083500');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2373, 34, 477, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2374, 34, 477, 77, '317');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2375, 34, 478, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2376, 34, 478, 67, '20000129');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2377, 34, 478, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2378, 34, 478, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2379, 34, 478, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2380, 34, 478, 71, '上海应用技术大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2381, 34, 478, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2382, 34, 478, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2383, 34, 478, 74, '上海应用技术大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2384, 34, 478, 75, '083500');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2385, 34, 478, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2386, 34, 478, 77, '368');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2387, 34, 479, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2388, 34, 479, 67, '20000629');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2389, 34, 479, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2390, 34, 479, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2391, 34, 479, 70, '其他人员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2392, 34, 479, 71, '江苏第二师范学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2393, 34, 479, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2394, 34, 479, 73, '20220630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2395, 34, 479, 74, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2396, 34, 479, 75, '081200');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2397, 34, 479, 76, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2398, 34, 479, 77, '363');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2399, 34, 480, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2400, 34, 480, 67, '20010820');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2401, 34, 480, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2402, 34, 480, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2403, 34, 480, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2404, 34, 480, 71, '海南大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2405, 34, 480, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2406, 34, 480, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2407, 34, 480, 74, '海南大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2408, 34, 480, 75, '081200');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2409, 34, 480, 76, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2410, 34, 480, 77, '354');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2411, 34, 481, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2412, 34, 481, 67, '20010320');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2413, 34, 481, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2414, 34, 481, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2415, 34, 481, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2416, 34, 481, 71, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2417, 34, 481, 72, '信息安全');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2418, 34, 481, 73, '20230619');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2419, 34, 481, 74, '东华大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2420, 34, 481, 75, '081200');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2421, 34, 481, 76, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2422, 34, 481, 77, '345');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2423, 34, 482, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2424, 34, 482, 67, '20001109');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2425, 34, 482, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2426, 34, 482, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2427, 34, 482, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2428, 34, 482, 71, '常州大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2429, 34, 482, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2430, 34, 482, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2431, 34, 482, 74, '常州大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2432, 34, 482, 75, '081200');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2433, 34, 482, 76, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2434, 34, 482, 77, '342');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2435, 34, 483, 66, '女');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2436, 34, 483, 67, '20010722');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2437, 34, 483, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2438, 34, 483, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2439, 34, 483, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2440, 34, 483, 71, '上海建桥学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2441, 34, 483, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2442, 34, 483, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2443, 34, 483, 74, '上海建桥学院');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2444, 34, 483, 75, '083500');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2445, 34, 483, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2446, 34, 483, 77, '329');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2447, 34, 484, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2448, 34, 484, 67, '19991014');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2449, 34, 484, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2450, 34, 484, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2451, 34, 484, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2452, 34, 484, 71, '河北科技大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2453, 34, 484, 72, '大数据管理与应用');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2454, 34, 484, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2455, 34, 484, 74, '河北科技大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2456, 34, 484, 75, '081200');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2457, 34, 484, 76, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2458, 34, 484, 77, '312');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2459, 34, 485, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2460, 34, 485, 67, '20000812');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2461, 34, 485, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2462, 34, 485, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2463, 34, 485, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2464, 34, 485, 71, '扬州大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2465, 34, 485, 72, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2466, 34, 485, 73, '20230701');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2467, 34, 485, 74, '扬州大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2468, 34, 485, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2469, 34, 485, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2470, 34, 485, 77, '389');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2471, 34, 486, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2472, 34, 486, 67, '19990206');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2473, 34, 486, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2474, 34, 486, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2475, 34, 486, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2476, 34, 486, 71, '上海海事大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2477, 34, 486, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2478, 34, 486, 73, '20230710');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2479, 34, 486, 74, '上海海事大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2480, 34, 486, 75, '085405');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2481, 34, 486, 76, '软件工程');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2482, 34, 486, 77, '322');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2483, 34, 487, 66, '男');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2484, 34, 487, 67, '19990604');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2485, 34, 487, 68, '汉族');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2486, 34, 487, 69, '中国共产主义青年团团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2487, 34, 487, 70, '应届本科毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2488, 34, 487, 71, '上海第二工业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2489, 34, 487, 72, '计算机科学与技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2490, 34, 487, 73, '20230630');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2491, 34, 487, 74, '上海第二工业大学');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2492, 34, 487, 75, '085404');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2493, 34, 487, 76, '计算机技术');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2494, 34, 487, 77, '361');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2495, 34, 340, 79, '2022-2023|扬州大学|学习委员#2020-2021|扬州大学|宣传中心副部长#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2496, 34, 340, 81, '2020-2021扬州大学三好学生;2020-2021扬州大学校长奖学金二等奖;2019-2020扬州大学学习优秀奖;2020-2021扬州大学素质拓展奖;2020-2021扬州大学创新创业奖;2020-2021扬州大学学科竞赛奖;2020-2021扬州大学学习优秀奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2497, 34, 341, 79, '2015年9月到2018年6月|莘庄中学|学生#2018年9月到2022年6月|上海立信会计金融学院|学生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2498, 34, 341, 81, '2018~2022年内连续多次获得优秀学生奖学金二等奖、三等奖、社会实践奖学金，获得“优秀学生会干部”、“先进个人”、“优秀共青团员”、“优秀学生干部”、“三好学生”、“2022年度上海市优秀毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2499, 34, 342, 79, '201609-201906|上海市五爱高级中学|副班长#201909-202306|上海应用技术大学|学生会副部长#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2500, 34, 342, 81, '上海应用技术大学“校综合奖学金”多次，“优秀团员”两次，“校优秀学生”一次，“英语报刊阅读大赛”三等奖。');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2501, 34, 343, 79, '2013年9月1日-2016年6月30日|上海市位育高中|学生#2016年9月1日-2020年7月15日|东华大学|学生#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2502, 34, 343, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2503, 34, 344, 79, '2019年9月-2023年7月|南通大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2504, 34, 344, 81, '2019-2020学年校二等奖学金2020-2021校二等奖学金2021-2022校二等奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2505, 34, 345, 79, '2019.9.10-2023.6.10|东华大学|软件1902心理委员#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2506, 34, 345, 81, '2022年秋获得东华大学二等奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2507, 34, 346, 79, '2019年9月-2022年6月|山东理工大学|实验室助理#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2508, 34, 346, 81, '2022年5月27日,山东理工大学,蓝桥杯省赛二等奖，2022年4月12日,山东理工大学,山东省优秀毕业生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2509, 34, 347, 79, '2019年9月-2022年9月|本科学习|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2510, 34, 347, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2511, 34, 348, 79, '2019.9-2023.6|江苏师范大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2512, 34, 348, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2513, 34, 349, 79, '2015年9月-2016年6月|福建工程学院|无#2017年9月-2022年6月|上海工程技术大学|无#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2514, 34, 349, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2515, 34, 350, 79, '2018.9--2022.6|河北北方学院|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2516, 34, 350, 81, '2018-2019学年获二等奖学金和三好学生称号；2020-2021学年获一等奖学金和三好学生称号');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2517, 34, 351, 79, '2019年9月-2023年7月|南京信息工程大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2518, 34, 351, 81, '2020年10月校二等奖学金、2021年10月校二等奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2519, 34, 352, 79, '2018年9月-2022年7月|中原工学院|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2520, 34, 352, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2521, 34, 353, 79, '2019.9-2023.6|南华大学|双创委员#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2522, 34, 353, 81, '2020-12-南华大学-国家励志奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2523, 34, 354, 79, '2019年9月1日-2023年6月30日|盐城工学院|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2524, 34, 354, 81, '获得2019年国家励志奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2525, 34, 355, 79, '2019.9-今|上海第二工业大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2526, 34, 355, 81, '2020-2021获得上海第二工业大学校二等奖学金；2021-2022获得上海第二工业大学校三等奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2527, 34, 356, 79, '2019年9月-2023年6月|东华理工大学|班长#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2528, 34, 356, 81, '2019-2021学年均获得“三好学生”、“优秀学生干部”、“校二等奖学金”，2021-2022学年“首届全国大学生工业化建筑与智慧建造竞赛三等奖”、“第三届品茗杯全国高校BIM应用毕业设计大赛二等奖”');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2529, 34, 357, 79, '2019年9月-2023年7月|长江大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2530, 34, 357, 81, '2022年9月在长江大学参加互联网大赛获得校级铜奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2531, 34, 358, 79, '2019年9月-2023年6月|江苏第二师范学院|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2532, 34, 358, 81, '2019年获1次校奖学金；2020年获2次校奖学金及1次三好学生；2021年获2次校奖学金及1次优秀共青团员及1次三好学生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2533, 34, 359, 79, '2018年08月-2022年05月|上海师范大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2534, 34, 359, 81, '2019年10月，获“2018-2019学年上海师范大学专业奖学金二等奖”；2020年5月，获”上海师范大学优秀团员“；2020年10月，获“2019-2020学年上海师范大学专业奖学金二等奖”');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2535, 34, 360, 79, '2019年9月-2023年7月|东华大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2536, 34, 360, 81, '2021年5月在东华大学获“优秀团员”称号');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2537, 34, 361, 79, '2016年9月-2020年6月|成都信息工程大学|学生#2021年5月-2022年5月|深圳新锐居品科技有限公司|软件开发工程师#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2538, 34, 361, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2539, 34, 362, 79, '2019.9.-2023.6|青岛理工大学|班级组织委员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2540, 34, 362, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2541, 34, 363, 79, '2019-2022|大连工业大学|组织委员#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2542, 34, 363, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2543, 34, 364, 79, '2018-2022|上海对外经贸大学|本科生#2022|上海对外经贸大学|科研助理#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2544, 34, 364, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2545, 34, 365, 79, '2019年9月—2023年6月|南京财经大学|宣传委员#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2546, 34, 365, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2547, 34, 366, 79, '2019年9月-2020年6月|江西农业大学|院学生会办公室志愿者#2020年9月-2021年7月|江西农业大学|院学生会办公室工作人员#2021年9月-2022年7月|江西农业大学|院学生会办公室负责人');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2548, 34, 366, 81, '2019-2020学年，在江西农业大学，获得校级优秀学生，国家励志奖学金；2020-2021学年，在江西农业大学，获得校级优秀学生干部，国家励志奖学金，特长奖学金；2021-2022学年，在江西农业大学，获得优秀共青团员。');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2549, 34, 367, 79, '2019年9月-2023年6月|上海海事大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2550, 34, 367, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2551, 34, 368, 79, '2019年9月-今|上海海事大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2552, 34, 368, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2553, 34, 369, 79, '2019.9-至今|浙江万里学院|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2554, 34, 369, 81, '浙江万里学院校二等奖学金；浙江万里学院”优秀团员“；2021年浙江万里学院“挑战杯”大学生课外学术科技作品竞赛中获得银奖；2021年浙江万里学院“互联网+”大学生创新创业大赛获得铜奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2555, 34, 370, 79, '2018年9月-2022年5月|上海应用技术大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2556, 34, 370, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2557, 34, 371, 79, '2019年9月-2023年6月|燕山大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2558, 34, 371, 81, '在燕山大学2021-2022学年第一学期获得一次校三等奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2559, 34, 372, 79, '2019年9月-2023年7月|中国民航大学|团委宣传委员#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2560, 34, 372, 81, '团体程序设计天梯赛天津市团队三等奖、全国大学生数学建模竞赛天津市一等奖、中国民航大学程序设计天梯赛一等奖、普通高校大学物理竞赛天津市三等奖、中国民航大学数学建模竞赛一等奖、年度优秀大学生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2561, 34, 373, 79, '2019.09|2023.06|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2562, 34, 373, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2563, 34, 374, 79, '2019.9—2023.6|济南大学|学生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2564, 34, 374, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2565, 34, 375, 79, '2019年9月-2023年7月|莆田学院|学习委员#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2566, 34, 375, 81, '2021年11月获得国家励志奖学金;2021年12月获得福建省大学生人工智能行业分析与实践大赛省级二等奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2567, 34, 376, 79, '2019年9月-2020年9月|上海海事大学|无#2020年9月-2021年9月|上海海事大学|无#2021年9月-2022年9月|上海海事大学|宣传委员#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2568, 34, 376, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2569, 34, 377, 79, '2018年9月-2022年6月|桂林电子科技大学信息科技学院|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2570, 34, 377, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2571, 34, 378, 79, '2018.09-2022.06|上海理工大学|#2022.01-2022.08|上海基立讯科技有限公司|Java开发#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2572, 34, 378, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2573, 34, 379, 79, '2019年9月-2023年6月|南通大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2574, 34, 379, 81, '大一三等奖学金，大二三好学生和一等奖学金，大三三好学生标兵和一等奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2575, 34, 380, 79, '2019年10月-2020年10月|在计算机科学与技术学院|担任院级学习部委员#2019年9月-至今|在计算机科学与技术学院|担任班级学习委员#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2576, 34, 380, 81, '2020年获得国家励志奖学金；2019-2020和2020-2021年度获得优秀团员称号；获得普通话二甲证书');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2577, 34, 381, 79, '2019.9-2023.7|南京信息工程大学|心理委员#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2578, 34, 381, 81, '2020年11月24日，南京信息工程大学，校一等奖学金；2020年11月24日，南京信息工程大学，校三好学生称号；2021年9月，南京信息工程大学，高教杯数学建模江苏省一；2021年12月15日，南京信息工程大学，校三等奖学金；');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2579, 34, 382, 79, '2018.9-2022.6|东华大学|无#2022.8-至今|中国电信股份有限公司上海分公司|员工#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2580, 34, 382, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2581, 34, 383, 79, '2019-09-01——至今|兰州理工大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2582, 34, 383, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2583, 34, 384, 79, '2006年9月-2012年6月|水东中心小学|#2012年9月-2015年6月|文昌中学|#2015年9月-2018年6月|洞口一中|#2018年10月-2022年6月|湖南工业大学|寝室长#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2584, 34, 384, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2585, 34, 385, 79, '2019年9月-2023年7月|南京信息工程大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2586, 34, 385, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2587, 34, 386, 79, '2018年9月-2022年6月|福建工程学院|学习委员#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2588, 34, 386, 81, '2018-2021连续三年获得国家励志奖学金，2018下半学期到毕业连续五次获得校二等奖学金，2020年获得优秀学生干部');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2589, 34, 387, 79, '2019年09月01日-2023年06年30日|山东理工大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2590, 34, 387, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2591, 34, 388, 79, '2018年9月-2022年6月|东华大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2592, 34, 388, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2593, 34, 389, 79, '2019.10-2023.7|南京审计大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2594, 34, 389, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2595, 34, 390, 79, '2019.9-2023.6|安徽工程大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2596, 34, 390, 81, '2020年11月3日，安徽工程大学，获得校一等奖学金；2021年11月9日，安徽工程大学，获得校二等奖学金；2021年12月16日，安徽工程大学，获得“三好学生”称号；');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2597, 34, 391, 79, '2016.9-2019.6|淮滨高级中学|无#2019.9-2023.7|上海应用技术大学|无#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2598, 34, 391, 81, '无处分，校优秀综合奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2599, 34, 392, 79, '2018年9月-2022年6月|南京工程学院|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2600, 34, 392, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2601, 34, 393, 79, '2018年9月-2022年6月|徐州工程学院|院学生会学习部副部长#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2602, 34, 393, 81, '2018-2022获校一、二等奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2603, 34, 394, 79, '2019年9月至今|就读于安徽建筑大学|班长#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2604, 34, 394, 81, '2019年获得优秀学生干部，2020年获得道德修养先进个人和三等奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2605, 34, 395, 79, '2019年9月-2023年7月|安徽财经大学|本科学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2606, 34, 395, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2607, 34, 396, 79, '2014年9月-2017年6月|莒南县第一中心|无#2017年9月1日-2021年6月30日|山东交通学院|无#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2608, 34, 396, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2609, 34, 397, 79, '201909-202306|安徽理工大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2610, 34, 397, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2611, 34, 398, 79, '2019年7月-2019年2月|浙江工业大学|高数课代表#2019年7月-2022年7月|浙江工业大学|学生#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2612, 34, 398, 81, '大一学年校三等学习奖学金，大二学年校三等学习奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2613, 34, 399, 79, '2019年6月-2023年7月|江西财经大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2614, 34, 399, 81, '2021年6月8日，在江西财经大学荣获第十二届蓝桥杯全国软件和信息技术专业人才大赛全国总决赛c/c++程序设计竞赛B组三等奖,2021年6月，在湖南湘潭市，湘潭大学获得2021年中国大学生程序设计竞赛铜奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2615, 34, 400, 79, '2019年9月-2022年9月|学习|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2616, 34, 400, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2617, 34, 401, 79, '2018.9-2022.6|兰州理工大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2618, 34, 401, 81, '“互联网+”第五届创新创业大赛校级铜奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2619, 34, 402, 79, '2018年9月-2022年7月|东华理工大学|班级团支书#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2620, 34, 402, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2621, 34, 403, 79, '2019-09--2023-07|南京林业大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2622, 34, 403, 81, '2021-06江苏省高等数学竞赛一等奖2020-09优秀学生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2623, 34, 404, 79, '2018年9月-2022年6月|中北大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2624, 34, 404, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2625, 34, 405, 79, '2019年9月-2023年7月|湖南农业大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2626, 34, 405, 81, '2021年在湖南农业大学因为成绩优异表现突出获得了校二等奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2627, 34, 406, 79, '2018年9月-2023年7月|陕西科技大学|舍长#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2628, 34, 406, 81, '2021年获得陕西科技大学数学建模竞赛二等奖，2021年获得陕西科技大学二等学业奖学金，2022年获得十三届蓝桥杯陕西赛区c/c++程序设计大学B组省赛一等奖。');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2629, 34, 407, 79, '2019.6-2023.7|南通大学|学习委员#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2630, 34, 407, 81, '南通大学2019-2020学年度，荣获优秀学生三等奖学金。南通大学2010-2021学年度，荣获优秀学生三等奖学金。南通大学2021-2022学年度，荣获优秀学生三等奖学金。');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2631, 34, 408, 79, '2016年9月-2019年6月|宜春市第一中学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2632, 34, 408, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2633, 34, 409, 79, '2019年9月-2023年7月|东华大学|本科在读学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2634, 34, 409, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2635, 34, 410, 79, '2019年9月-2022年七月|扬州大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2636, 34, 410, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2637, 34, 411, 79, '2019年9月-2023年6月|武汉理工大学|无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2638, 34, 411, 81, '校三好学生，校优秀共青团干。');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2639, 34, 412, 79, '2018年9月-2022年7月|郑州工商学院|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2640, 34, 412, 81, '参加第三届信息安全铁人三项赛获得国家信息安全水平证书（一级）。有郑州数字人才证书');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2641, 34, 413, 79, '2019年9月-2023年7月|安徽理工大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2642, 34, 413, 81, '2019-2020学年在安徽理工大学大一获国家励志奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2643, 34, 414, 79, '2018.9-2022.6|山东理工大学计算机科学与技术学院|学生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2644, 34, 414, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2645, 34, 415, 79, '2019.9.16-2023.71|东华大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2646, 34, 415, 81, '2019-2020年度东华大学学习优秀奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2647, 34, 416, 79, '2017年9月-2020年6月|浙江交通职业技术学院|学生#2020年9月-2022年6月|浙江中医药大学|学生#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2648, 34, 416, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2649, 34, 417, 79, '2019年9月-2023年7月 |南通大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2650, 34, 417, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2651, 34, 418, 79, '2019.9-2023.9|中国计量大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2652, 34, 418, 81, '大学大一获得社会实践奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2653, 34, 419, 79, '2019年9月-2023年7月|深圳技术大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2654, 34, 419, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2655, 34, 420, 79, '2019年9月-至今|上海理工大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2656, 34, 420, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2657, 34, 421, 79, '2017年9月-2021年6月|武汉科技大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2658, 34, 421, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2659, 34, 422, 79, '2019.7-2023.7|湘潭大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2660, 34, 422, 81, '2020年获得三好学生称号，得乙等奖学金，2021年通过英语四级。');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2661, 34, 423, 79, '2017.9-2021.6|郑州航空工业管理学院|学生#2021.7-至今|中国能源建设集团江苏省电力建设第一工程有限公司|员工#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2662, 34, 423, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2663, 34, 424, 79, '2019年9月-2023年7月|南通大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2664, 34, 424, 81, '两次三等奖学金；校级优秀团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2665, 34, 425, 79, '2019年9月1日-2023年7月1日|上海工程技术大学|班级团组织委员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2666, 34, 425, 81, '上海市文汇青春创新大赛三等奖，上海工程技术大学物理竞赛二等奖，上海工程技术大学数学竞赛三等奖，上海工程技术大学优秀学生奖，上海工程技术大学优秀团员奖，上海工程技术大学20年二等奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2667, 34, 426, 79, '2019年9月-2023年6月|南京林业大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2668, 34, 426, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2669, 34, 427, 79, '2019年9月-2023年7月|河北经贸大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2670, 34, 427, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2671, 34, 428, 79, '2019年9月-2023年7月|山东科技大学|科技委员#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2672, 34, 428, 81, '2019年在学校因成绩良好获得校级二等奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2673, 34, 429, 79, '2019年10月-2023年7月|汕头大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2674, 34, 429, 81, '2019年学业优秀奖学金;2020年学业优秀奖学金;2021年学业优秀奖学金;2019外研社英语辩论公开赛·黄石站暨第二届华中英语辩论公开赛季军;2019外研社英语辩论公开赛·汕头站暨第五届汕头校际英语辩论赛一等奖;');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2675, 34, 430, 79, '2019年9月-2023年6月|常州工学院|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2676, 34, 430, 81, '2021年获得学业奖学金三等奖，国家励志奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2677, 34, 431, 79, '2019.9-2023.6|上海理工大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2678, 34, 431, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2679, 34, 432, 79, '2017年9月-2021年7月|中国民航大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2680, 34, 432, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2681, 34, 433, 79, '2019年9月-2023年6月|重庆理工大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2682, 34, 433, 81, '2021年获得美国数学建模大赛一等奖；2021年获得第五届西南地区大学生物理学术竞赛二等奖；2021年获得重庆市团体天体程序设计大赛二等奖；2020年获得重庆理工大学一等奖学金；2021年获得国家励志奖学金；无处分。');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2683, 34, 434, 79, '2019年8月-2023年7月|齐鲁工业大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2684, 34, 434, 81, '2019-2020学年学习成绩优秀二等奖学金，校三好学生，校优秀共青团员；2020-2021学年校素质拓展奖，校三好学生标兵，校三好学生，学习成绩优秀一等奖学金。无处分。');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2685, 34, 435, 79, '2017.9·2021.6|上海政法学院|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2686, 34, 435, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2687, 34, 436, 79, '2018年9月-2022年6月|合肥学院|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2688, 34, 436, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2689, 34, 437, 79, '2018年9月-2022年7月|安庆师范大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2690, 34, 437, 81, '2019年在安庆师范大学获校程序设计竞赛一等奖；2021年在安庆师范大学获校程序设计竞赛一等奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2691, 34, 438, 79, '2019年9月-至今|安徽工业大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2692, 34, 438, 81, '2020-2021学年，获得“安徽工业大学文明宿舍”称号；2022学年，获得“微信小程序应用开发赛”参与奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2693, 34, 439, 79, '2018.9-2022.6|杭州电子科技大学|无#2022.1-2022.3|浙江大华技术股份有限公司|流程系统部实习生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2694, 34, 439, 81, '国家级大学生创新创业训练项目立项、三次校级奖学金、校优秀团员、全国大学生数学竞赛二等奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2695, 34, 440, 79, '2019年9月-2023年7月|闽南师范大学|舍长#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2696, 34, 440, 81, '2021年5月于闽南师范大学获“校优秀共青团员”2022年5月于闽南师范大学获“校优秀共青团员”2022年9月于闽南师范大学获“校三等奖学金”');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2697, 34, 441, 79, '201709-202107|东华理工大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2698, 34, 441, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2699, 34, 442, 79, '2019.9-2023.7|江苏大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2700, 34, 442, 81, '2022年，获得蓝桥杯省赛二等奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2701, 34, 443, 79, '2019年9月-2023年7月|黄山学院|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2702, 34, 443, 81, '在2019-2020学年荣获“三好学生”；在2019-2020学年荣获国家励志奖学金；在2020-2021学年荣获“三好学生”；在2020-2021学年荣获国家励志奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2703, 34, 444, 79, '2019.9-2023.6|东华大学|院学生会部长#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2704, 34, 444, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2705, 34, 445, 79, '2019.9-2023.7|黄山学院|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2706, 34, 445, 81, '2021年11月获2021全国大学生软件测试大赛嵌入式测试总决赛国家三等奖2021年11月获2021全国大学生软件测试大赛嵌入式测试省赛一等奖2021年12月获安徽省大学生服务外包创新创业大赛省二等奖2020年2021年国家励志奖两次');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2707, 34, 446, 79, '2019-9至2023-6|东华大学|学生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2708, 34, 446, 81, '在2019学年获得东华大学学习优秀奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2709, 34, 447, 79, '2007.9.1-2011.6|冷水江市明礼实验中学|无#2011.9.1-2013.6|长沙市华夏实验小学|数学课代表#2013.9.1-2016.6|湖南省广益实验中学|数学课代表#2016.9.1-2019.6|冷水市第一中学|数学课代表#2019.9.1-2023.6|衡阳师范学院|学习委员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2710, 34, 447, 81, '大一19-20学年衡阳师范学院校级三等奖，大三21-22学年衡阳师范学院校级三等奖，2021年全国数学建模竞赛湖南省三等奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2711, 34, 448, 79, '201509-201906|南京财经大学|无#201811-201904|南京秉谦科技有限公司|开发#202006-202206|阿纳塔兹起重机（上海）有限公司|业务专员#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2712, 34, 448, 81, '2016年获得校三等奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2713, 34, 449, 79, '2018年9月-2022年7月|安徽建筑大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2714, 34, 449, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2715, 34, 450, 79, '2019年9月-2023年6月|上海师范大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2716, 34, 450, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2717, 34, 451, 79, '2019年9月-2023年7月|南京林业大学|宣传委员#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2718, 34, 451, 81, '2019-2020学年南京林业大学“优秀学生”称号及奖学金、2020-2021学年南京林业大学“优秀学生”称号及奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2719, 34, 452, 79, '2019.9-2023.6|常熟理工学院|组织委员#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2720, 34, 452, 81, '2020-2021学年在常熟理工学院获校三好学生；2020-2021学年在常熟理工学院获一等奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2721, 34, 453, 79, '2019.9~至今|浙江理工大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2722, 34, 453, 81, '2020浙江理工大学校三等奖学金，2021浙江理工大学校三等奖学金，2021浙江理工大学院优秀团员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2723, 34, 454, 79, '2019年9月-2023年6月|武汉工程大学|团支书#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2724, 34, 454, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2725, 34, 455, 79, '2017.9-2021.6|南京信息工程大学|无#2021.7-2022.6|新奥新智科技有限公司|后端开发#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2726, 34, 455, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2727, 34, 456, 79, '2017/9-2018/12|常数理工学院|学生#2018/12-2021/5|美国威斯康星大学欧克莱尔分校|学生#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2728, 34, 456, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2729, 34, 457, 79, '2016.9-2020.7|东华大学|学生#2021.1-2022.7|安徽讯飞寰语科技有限公司|引擎测试工程师');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2730, 34, 457, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2731, 34, 458, 79, '2018年9月-2022年6月|盐城工学院|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2732, 34, 458, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2733, 34, 459, 79, '2017年9月-2021年6月|南京大学|无#2021年7月-2022年2月|上海市钧正网络科技有限公司|软件开发工程师#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2734, 34, 459, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2735, 34, 460, 79, '2018.9-2022.6|江苏大学|学生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2736, 34, 460, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2737, 34, 461, 79, '2019年9月到2023年6月|上海电力大学临港校区|学习委员#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2738, 34, 461, 81, '2021上海市阳光体育大联赛高校女子组一等奖，2020上海市阳光体育大联赛高校女子组三等奖；无处分');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2739, 34, 462, 79, '2019.09-2023.06|南京邮电大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2740, 34, 462, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2741, 34, 463, 79, '2019年9月1日至2023年7月1日|九江学院|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2742, 34, 463, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2743, 34, 464, 79, '2019.9-2023.6|江苏海洋大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2744, 34, 464, 81, '第十二届蓝桥杯全国软件和信息技术专业人才大赛江苏赛区Java软件开发三等奖。全国大学生英语竞赛三等奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2745, 34, 465, 79, '2019年9月-2023年7月|东华理工大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2746, 34, 465, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2747, 34, 466, 79, '2018年9月-2022年6月|中国石油大学（华东）|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2748, 34, 466, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2749, 34, 467, 79, '2019年9月-2023年7月|青岛理工大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2750, 34, 467, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2751, 34, 468, 79, '2007年9月-2013年6月|涟城镇中心小学|学生#2013年9月-2016年6月|郑梁梅中学|学生#2016年9月-2019年6月|江苏省涟水中学|学生#2019年9月-2023年6月|青岛工学院|学生#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2752, 34, 468, 81, '2020年荣获青岛工学院优秀学生荣誉称号');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2753, 34, 469, 79, '2019年9月-2020年6月|华北理工大学|学生#2020年9月-2021年6月|华北理工大学|学生#2021年9月-2022年6月|华北理工大学|学生#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2754, 34, 469, 81, '2019-2020学年度获奖学金及优秀大学生称号');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2755, 34, 470, 79, '2019年9月-2023年7月|杭州电子科技大学|学习#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2756, 34, 470, 81, '2020年6月获第一学期三等奖学金和文艺活动奖2020年9月获第二学期二等奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2757, 34, 471, 79, '2019.9-2023.6|南通大学|班级文体委员');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2758, 34, 471, 81, '2020年于南通大学因成绩优异获三等奖学金，2022年于南通大学因成绩优异获三等奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2759, 34, 472, 79, '2019年9月-2023年6月|东华理工大学|学习委员#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2760, 34, 472, 81, '2021年6月在校获第九届“泰迪杯”全国数据挖掘挑战赛全国三等奖2021年校“三好学生”“优秀学生干部”');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2761, 34, 473, 79, '2019年9月-2023年7月|南京邮电大学通达学院|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2762, 34, 473, 81, '2021年全国大学生计算机能力挑战赛决赛三等奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2763, 34, 474, 79, '2019.9-2023.6|上海应用技术大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2764, 34, 474, 81, '入学奖学金三等奖，2次国家助学金，1次期末奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2765, 34, 475, 79, '2019年9月-2023年6月|东华大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2766, 34, 475, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2767, 34, 476, 79, '2018-2022|许昌学院|本科生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2768, 34, 476, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2769, 34, 477, 79, '2018年9月-2022年6月|上海理工大学|无#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2770, 34, 477, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2771, 34, 478, 79, '2019年九月-2023年七月|上海应用技术大学|学生');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2772, 34, 478, 81, '入学奖学金三等奖，国家励志奖学金，5次期末奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2773, 34, 479, 79, '2018年9月-2022年6月|江苏第二师范学院|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2774, 34, 479, 81, '2019年三等奖学金');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2775, 34, 480, 79, '2019年9月-2023年7月|海南大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2776, 34, 480, 81, '2021年5月在海南大学因积极参加志愿活动获优秀志愿者证书');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2777, 34, 481, 79, '2019.9-2023.6|东华大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2778, 34, 481, 81, '2021.5荣获东华大学计算机科学与技术学院五四表彰“优秀志愿者”荣誉称号2022.5荣获东华大学计算机科学与技术学院五四表彰“优秀团员”荣誉称号');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2779, 34, 482, 79, '2019年9月-2020年6月|常州大学校自律中心学风部|干事#2020年9月-2021年6月|常州大学计算机与人工智能学院自律中心外宣部|部长#2021年9月-2022年6月|常州大学计算机与人工智能学院自律中心|副主任#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2780, 34, 482, 81, '2019-2020学年二等奖学金，院级三好学生；2020-2021学年一等奖学金，院级三好学生；2021-2022学年特等奖学金，校级三好学生。');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2781, 34, 483, 79, '2019年9月-2023年7月|上海建桥学院|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2782, 34, 483, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2783, 34, 484, 79, '2019年9月-2023年6月|河北科技大学|学生#||#||#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2784, 34, 484, 81, '大一、大二均获得校三等奖学金，河北科技大学第十四届学科竞赛数学建模一等奖，2021年全国数学建模竞赛校内选拔赛一等奖获奖证书，“浪潮杯”商务大数据比赛校三等奖，学院“英语月”比赛一等奖，学院辩论赛一等奖');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2785, 34, 486, 79, '2017年9月-2018年9月|上海海事大学|学生#2018年9月-2020年9月|海军92619部队|战士#2020年9月-2022年9月|上海海事大学|学生#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2786, 34, 486, 81, '无');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2787, 34, 487, 79, '201709-201809|上海第二工业大学|学生#201809-202009|73087部队|战士#202009-至今|上海第二工业大学|学生#||#||');
INSERT INTO `infos` (`ID`, `activityID`, `participantID`, `infoItemID`, `content`) VALUES (2788, 34, 487, 81, '2021年09月25日于上海第二工业大学获校二等奖学金');
COMMIT;

-- ----------------------------
-- Table structure for institution
-- ----------------------------
DROP TABLE IF EXISTS `institution`;
CREATE TABLE `institution` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '员工编号',
  `company` varchar(50) NOT NULL COMMENT '单位名称',
  `activityCount` int DEFAULT NULL COMMENT '单位总计活动数',
  `uplimit` int DEFAULT NULL COMMENT '单位活动数上限',
  `comment` varchar(2000) DEFAULT NULL COMMENT '备注',
  `deleteFlag` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `company` (`company`) USING BTREE,
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of institution
-- ----------------------------
BEGIN;
INSERT INTO `institution` (`id`, `company`, `activityCount`, `uplimit`, `comment`, `deleteFlag`) VALUES (1, 'D大学计算机学院', 12, 50, 'D大学计算机学院', 0);
COMMIT;

-- ----------------------------
-- Table structure for joblevel
-- ----------------------------
DROP TABLE IF EXISTS `joblevel`;
CREATE TABLE `joblevel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL COMMENT '职称名称',
  `titleLevel` enum('正高级','副高级','中级','初级','员级') DEFAULT NULL,
  `createDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `enabled` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of joblevel
-- ----------------------------
BEGIN;
INSERT INTO `joblevel` (`id`, `name`, `titleLevel`, `createDate`, `enabled`) VALUES (9, '教授', '正高级', '2018-01-11 00:00:00', 1);
INSERT INTO `joblevel` (`id`, `name`, `titleLevel`, `createDate`, `enabled`) VALUES (10, '副教授', '副高级', '2018-01-11 21:19:20', 1);
INSERT INTO `joblevel` (`id`, `name`, `titleLevel`, `createDate`, `enabled`) VALUES (12, '助教', '初级', '2018-01-11 21:35:39', 1);
INSERT INTO `joblevel` (`id`, `name`, `titleLevel`, `createDate`, `enabled`) VALUES (13, '讲师', '中级', '2018-01-11 00:00:00', 0);
INSERT INTO `joblevel` (`id`, `name`, `titleLevel`, `createDate`, `enabled`) VALUES (14, '初级工程师', '初级', '2018-01-14 00:00:00', 1);
INSERT INTO `joblevel` (`id`, `name`, `titleLevel`, `createDate`, `enabled`) VALUES (15, '中级工程师66', '中级', '2018-01-14 00:00:00', 1);
INSERT INTO `joblevel` (`id`, `name`, `titleLevel`, `createDate`, `enabled`) VALUES (16, '高级工程师', '副高级', '2018-01-14 16:19:14', 1);
INSERT INTO `joblevel` (`id`, `name`, `titleLevel`, `createDate`, `enabled`) VALUES (17, '骨灰级工程师', '正高级', '2018-01-14 16:19:24', 1);
COMMIT;

-- ----------------------------
-- Table structure for logs
-- ----------------------------
DROP TABLE IF EXISTS `logs`;
CREATE TABLE `logs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `addDate` timestamp NULL DEFAULT NULL COMMENT '添加日期',
  `operator` int DEFAULT NULL COMMENT '操作内容',
  `category` varchar(10) DEFAULT NULL,
  `content` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `oplog_ibfk_1` (`operator`) USING BTREE,
  CONSTRAINT `logs_ibfk_1` FOREIGN KEY (`operator`) REFERENCES `institution` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=584 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of logs
-- ----------------------------
BEGIN;
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (391, '2023-03-20 18:12:37', 1, '活动', '添加成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (392, '2023-03-20 18:12:43', 1, '活动', '删除成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (393, '2023-03-22 12:41:26', 1, '活动', '添加成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (394, '2023-03-22 13:18:18', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (395, '2023-03-22 13:18:20', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (396, '2023-03-22 13:18:21', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (397, '2023-03-22 13:18:21', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (398, '2023-03-22 13:18:25', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (399, '2023-03-22 13:18:27', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (400, '2023-03-22 13:18:31', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (401, '2023-03-22 13:18:33', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (402, '2023-03-22 13:18:58', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (403, '2023-03-22 13:19:01', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (404, '2023-03-22 13:19:02', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (405, '2023-03-22 13:19:04', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (406, '2023-03-22 13:19:05', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (407, '2023-03-22 13:19:19', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (408, '2023-03-22 13:19:22', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (409, '2023-03-22 13:19:23', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (410, '2023-03-22 13:19:25', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (411, '2023-03-22 13:19:25', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (412, '2023-03-22 13:19:48', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (413, '2023-03-22 13:19:49', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (414, '2023-03-22 13:19:50', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (415, '2023-03-22 13:19:54', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (416, '2023-03-22 13:19:54', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (417, '2023-03-22 13:20:30', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (418, '2023-03-22 13:20:32', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (419, '2023-03-22 13:20:33', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (420, '2023-03-22 13:20:50', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (421, '2023-03-22 13:20:53', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (422, '2023-03-22 13:20:54', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (423, '2023-03-22 13:21:07', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (424, '2023-03-22 13:21:08', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (425, '2023-03-22 13:21:10', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (426, '2023-03-22 13:21:11', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (427, '2023-03-22 13:21:26', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (428, '2023-03-22 13:21:28', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (429, '2023-03-22 13:21:29', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (430, '2023-03-22 13:21:46', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (431, '2023-03-22 13:21:48', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (432, '2023-03-22 13:21:49', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (433, '2023-03-22 13:22:02', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (434, '2023-03-22 13:22:04', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (435, '2023-03-22 13:22:05', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (436, '2023-03-22 13:22:18', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (437, '2023-03-22 13:22:33', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (438, '2023-03-22 13:22:34', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (439, '2023-03-22 13:22:47', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (440, '2023-03-22 13:22:52', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (441, '2023-03-22 13:22:53', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (442, '2023-03-22 13:23:01', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (443, '2023-03-22 13:23:01', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (444, '2023-03-22 13:23:03', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (445, '2023-03-22 13:23:03', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (446, '2023-03-22 13:23:07', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (447, '2023-03-22 13:23:07', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (448, '2023-03-22 13:23:08', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (449, '2023-03-22 13:23:08', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (450, '2023-03-22 13:23:09', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (451, '2023-03-22 13:23:10', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (452, '2023-03-22 13:23:11', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (453, '2023-03-22 13:23:12', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (454, '2023-03-22 13:23:13', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (455, '2023-03-22 13:23:13', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (456, '2023-03-22 13:23:14', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (457, '2023-03-22 13:23:15', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (458, '2023-03-22 13:23:34', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (459, '2023-03-22 13:23:37', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (460, '2023-03-22 13:23:38', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (461, '2023-03-22 13:23:52', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (462, '2023-03-22 13:23:54', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (463, '2023-03-22 13:23:55', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (464, '2023-03-22 13:24:38', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (465, '2023-03-22 13:24:41', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (466, '2023-03-22 13:24:45', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (467, '2023-03-22 13:32:23', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (468, '2023-03-22 13:32:27', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (469, '2023-03-22 13:32:28', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (470, '2023-03-22 13:32:41', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (471, '2023-03-22 13:32:44', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (472, '2023-03-22 13:32:45', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (473, '2023-03-22 13:32:46', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (474, '2023-03-22 13:32:47', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (475, '2023-03-22 13:36:40', 1, '评分项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (476, '2023-03-22 13:38:04', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (477, '2023-03-22 13:38:08', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (478, '2023-03-22 13:38:37', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (479, '2023-03-22 13:39:24', 1, '评分项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (480, '2023-03-22 13:39:34', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (481, '2023-03-22 13:39:38', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (482, '2023-03-22 13:39:50', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (483, '2023-03-22 13:40:17', 1, '评分项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (484, '2023-03-22 13:40:25', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (485, '2023-03-22 13:40:31', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (486, '2023-03-22 13:40:45', 1, '评分项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (487, '2023-03-22 13:40:58', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (488, '2023-03-22 13:41:09', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (489, '2023-03-22 13:41:13', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (490, '2023-03-22 13:41:29', 1, '评分项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (491, '2023-03-22 13:41:35', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (492, '2023-03-22 13:41:40', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (493, '2023-03-22 13:42:27', 1, '分组', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (494, '2023-03-22 13:42:34', 1, '分组', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (495, '2023-03-22 13:42:39', 1, '分组', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (496, '2023-03-22 13:42:44', 1, '分组', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (497, '2023-03-22 13:42:49', 1, '分组', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (498, '2023-03-22 13:42:55', 1, '分组', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (499, '2023-03-22 13:43:01', 1, '分组', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (500, '2023-03-22 14:24:05', 1, '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (501, '2023-03-22 14:24:17', 1, '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (502, '2023-03-22 14:35:02', 1, '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (503, '2023-03-22 15:22:56', 1, '专家管理', '导入成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (504, '2023-03-22 15:35:10', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (505, '2023-03-22 15:35:11', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (506, '2023-03-22 15:35:15', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (507, '2023-03-22 15:35:15', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (508, '2023-03-22 15:35:16', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (509, '2023-03-22 15:35:17', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (510, '2023-03-22 15:35:18', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (511, '2023-03-22 15:35:18', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (512, '2023-03-22 15:35:39', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (513, '2023-03-22 15:35:39', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (514, '2023-03-22 15:35:44', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (515, '2023-03-22 15:35:44', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (516, '2023-03-22 15:35:47', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (517, '2023-03-22 15:35:47', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (518, '2023-03-22 15:36:10', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (519, '2023-03-22 15:36:11', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (520, '2023-03-22 15:36:11', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (521, '2023-03-22 15:36:12', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (522, '2023-03-22 15:36:13', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (523, '2023-03-22 15:36:13', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (524, '2023-03-22 15:36:14', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (525, '2023-03-22 15:36:14', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (526, '2023-03-22 15:36:15', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (527, '2023-03-22 15:36:15', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (528, '2023-03-22 15:36:16', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (529, '2023-03-22 15:36:17', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (530, '2023-03-22 15:36:18', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (531, '2023-03-22 15:36:18', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (532, '2023-03-22 17:57:04', 1, '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (533, '2023-03-22 18:04:16', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (534, '2023-03-22 18:05:42', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (535, '2023-03-22 18:07:28', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (536, '2023-03-22 18:07:29', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (537, '2023-03-22 18:07:32', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (538, '2023-03-23 13:33:34', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (539, '2023-03-23 13:33:37', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (540, '2023-03-23 13:33:51', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (541, '2023-03-23 13:33:53', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (542, '2023-03-23 13:33:57', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (543, '2023-03-23 13:33:58', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (544, '2023-03-23 13:34:01', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (545, '2023-03-23 13:34:03', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (546, '2023-03-23 13:34:06', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (547, '2023-03-23 13:34:08', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (548, '2023-03-23 13:34:30', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (549, '2023-03-23 13:34:35', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (550, '2023-03-23 13:34:38', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (551, '2023-03-23 13:34:38', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (552, '2023-03-23 13:34:40', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (553, '2023-03-23 13:34:40', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (554, '2023-03-23 13:34:43', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (555, '2023-03-23 13:34:44', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (556, '2023-03-23 13:34:48', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (557, '2023-03-23 13:34:48', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (558, '2023-03-23 13:34:49', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (559, '2023-03-23 13:34:50', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (560, '2023-03-23 13:34:52', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (561, '2023-03-23 13:34:52', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (562, '2023-03-23 13:46:47', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (563, '2023-03-23 13:46:56', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (564, '2023-03-23 13:47:07', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (565, '2023-03-23 13:47:30', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (566, '2023-03-23 13:48:09', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (567, '2023-03-23 14:04:51', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (568, '2023-03-23 14:04:56', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (569, '2023-03-23 14:05:01', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (570, '2023-03-23 14:05:06', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (571, '2023-03-23 14:05:11', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (572, '2023-03-23 14:05:18', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (573, '2023-03-23 14:05:24', 1, '评分项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (574, '2023-03-23 18:05:27', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (575, '2023-03-23 18:05:29', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (576, '2023-03-23 18:16:25', 1, '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (577, '2023-03-23 18:29:27', 1, '信息项', '新增成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (578, '2023-03-23 20:18:42', 1, '活动', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (579, '2023-03-24 10:47:51', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (580, '2023-03-24 10:47:52', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (581, '2023-03-24 11:00:25', 1, '信息项', '更新成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (582, '2023-03-28 00:31:04', 1, '活动', '添加成功');
INSERT INTO `logs` (`id`, `addDate`, `operator`, `category`, `content`) VALUES (583, '2023-03-30 13:14:00', 1, '信息项', '新增成功');
COMMIT;

-- ----------------------------
-- Table structure for mail
-- ----------------------------
DROP TABLE IF EXISTS `mail`;
CREATE TABLE `mail` (
  `mailAddress` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL COMMENT '邮箱地址',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL COMMENT 'SMTP的验证码',
  PRIMARY KEY (`mailAddress`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of mail
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for mail_send_log
-- ----------------------------
DROP TABLE IF EXISTS `mail_send_log`;
CREATE TABLE `mail_send_log` (
  `msgId` varchar(255) DEFAULT NULL,
  `empId` int DEFAULT NULL,
  `status` int DEFAULT '0' COMMENT '0发送中，1发送成功，2发送失败',
  `routeKey` varchar(255) DEFAULT NULL,
  `exchange` varchar(255) DEFAULT NULL,
  `count` int DEFAULT '0' COMMENT '重试次数',
  `tryTime` datetime DEFAULT NULL COMMENT '第一次重试时间',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of mail_send_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `url` varchar(64) DEFAULT NULL,
  `path` varchar(64) DEFAULT NULL,
  `component` varchar(64) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `iconCls` varchar(64) DEFAULT NULL,
  `keepAlive` tinyint(1) DEFAULT NULL,
  `requireAuth` tinyint(1) DEFAULT NULL,
  `parentId` int DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `parentId` (`parentId`) USING BTREE,
  CONSTRAINT `menu_ibfk_1` FOREIGN KEY (`parentId`) REFERENCES `menu` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
BEGIN;
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (1, '/', NULL, NULL, '所有', NULL, NULL, NULL, NULL, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (2, '/', '/home', 'Home', '员工资料', 'fa fa-user-circle-o', NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (3, '/', '/home', 'Home', '单位管理', 'fa fa-address-card-o', NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (4, '/', '/home', 'Home', '活动管理', 'fa fa-money', NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (5, '/', '/home', 'Home', '统计管理', 'fa fa-bar-chart', NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (6, '/', '/home', 'Home', '系统管理', 'fa fa-windows', NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (7, '/employee/basic/**', '/emp/basic', 'EmpBasic', '基本资料', NULL, NULL, 1, 2, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (8, '/employee/advanced/**', '/emp/adv', 'EmpAdv', '高级资料', NULL, NULL, 1, 2, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (9, '/personnel/emp/**', '/per/emp', 'PerEmp', '查看所有单位', NULL, NULL, 1, 3, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (10, '/personnel/ec/**', '/per/ec', 'PerEc', '查看各个单位管理员列表', NULL, 0, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (11, '/personnel/train/**', '/per/train', 'PerTrain', '员工培训', NULL, NULL, 1, 9, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (12, '/personnel/salary/**', '/per/salary', 'PerSalary', '员工调薪', NULL, NULL, 1, 3, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (13, '/personnel/remove/**', '/per/mv', 'PerMv', '员工调动', NULL, NULL, 1, 3, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (14, '/salary/sob/**', '/ActivitM/group', 'SalGroup', '导入选手', NULL, NULL, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (15, '/salary/sobcfg/**', '/ActivitM/sobcfg', 'SalSobCfg', '专家活动设置', NULL, NULL, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (16, '/salary/table/**', '/ActivitM/table', 'SalTable', '分组管理', NULL, NULL, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (17, '/salary/month/**', '/ActivitM/month', 'SalMonth', '评分项设置', NULL, NULL, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (18, '', '/ActivitM/search', 'ActManage', '活动管理', NULL, NULL, 1, 4, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (19, '/statistics/all/**', '/sta/all', 'StaAll', '综合信息统计', NULL, NULL, 1, 5, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (20, '/statistics/score/**', '/sta/score', 'StaScore', '员工积分统计', NULL, NULL, 1, 5, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (21, '/statistics/personnel/**', '/sta/pers', 'StaPers', '人事信息统计', NULL, NULL, 1, 5, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (22, '/statistics/recored/**', '/sta/record', 'StaRecord', '人事记录统计', NULL, NULL, 1, 5, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (23, '/system/basic/**', '/sys/basic', 'SysBasic', '基础信息设置', NULL, NULL, 1, 6, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (24, '/system/cfg/**', '/sys/cfg', 'SysCfg', '系统管理', NULL, NULL, 1, 6, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (25, '/system/log/**', '/sys/log', 'SysLog', '操作日志管理', NULL, NULL, 1, 6, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (26, '/system/hr/**', '/sys/hr', 'SysHr', '查看各个单位管理员列表', NULL, NULL, 1, 6, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (27, '/system/data/**', '/sys/data', 'SysData', '备份恢复数据库', NULL, NULL, 1, 6, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (28, '/system/init/**', '/sys/init', 'SysInit', '初始化数据库', NULL, NULL, 1, 6, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (29, '/school/basic/**', '/school/basic', 'SchoolBasic', '活动/专家管理', NULL, NULL, 1, 30, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (30, '/', '/home', 'Home', '活动/专家管理', 'fa fa-plus-square', NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (31, NULL, '/participantsM', 'SalPar', '选手管理', NULL, NULL, NULL, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (32, NULL, '/home', 'Home', '学生管理', '', NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (33, NULL, '/student/basic', 'Home', 'www', NULL, NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (34, NULL, '/student/view', 'StuView', '查看学生', NULL, NULL, 1, 32, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (35, '', '/StuView', 'StuView1', 'StuView', '', NULL, 1, 6, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (36, '/', '/home', 'Home', '隐藏显示', NULL, NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (37, '/salary/situation/**', '/ActivitM/situation', 'SalSituation', '查看专家评分', NULL, NULL, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (38, '/salary/situation/**', '/ActivitM/infos', 'SalInfos', '信息项设置', NULL, NULL, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (39, NULL, NULL, NULL, '日志', NULL, NULL, 1, 4, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (40, NULL, '/SalLog', 'SalLog', '日志', NULL, NULL, 1, 4, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (41, NULL, '/Student/Product', 'Product', '项目', NULL, NULL, NULL, NULL, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (42, '/', '/home', 'Home', '个人资料', 'fa-fa-windows', NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (43, '/', '/home', 'Home', '研究生学术成果', 'fa-fa-windows', NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (44, '/', '/home', 'Home', '本科生管理', 'fa-fa-windows', NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (45, '/salary/search/**', '/UserInfo', 'UserInfo', '个人资料', NULL, NULL, 1, 42, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (46, '/salary/search/**', '/student/Paper', 'Paper', '论文', NULL, NULL, 1, 69, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (47, '/salary/search/**', '/student/Patent', 'Patent', '专利', NULL, NULL, 1, 69, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (48, '/salary/search/**', '/student/ResearchAward', 'ResearchAward', '奖励', NULL, NULL, 1, 69, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (49, '/salary/search/**', '/student/Product', 'Product', '科研项目', NULL, NULL, 1, 69, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (50, '/salary/search/**', '/student/AcademicMonograph', 'AcademicMonograph', '学术专著和教材', NULL, NULL, 1, 69, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (51, '/salary/search/**', '/student/ResearchProject', 'ResearchProject', '制造或设计的产品', NULL, NULL, 1, 69, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (52, '/salary/search/**', '/student/Achievements', 'Achievements', '成果列表', NULL, NULL, 1, 70, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (53, '/', '/home', 'Home', '待审核', 'fa fa-windows', NULL, 1, 44, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (54, '/', '/home', 'Home', '学生成果', 'fa fa-windows', NULL, 1, 44, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (55, '/', '/home', 'SalSearch', '所有成果', 'fa fa-windows', NULL, 1, 43, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (56, '/salary/search/**', '/teacher/tPaper', 'Paper', '论文', NULL, NULL, 1, 55, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (57, '/salary/search/**', '/teacher/tPatent', 'Patent', '专利', NULL, NULL, 1, 55, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (58, '/salary/search/**', '/teacher/tResearchAward', 'tResearchAward', '科研获奖', NULL, NULL, 1, 55, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (59, '/salary/search/**', '/teacher/tProduct', 'tProduct', '科研项目', NULL, NULL, 1, 55, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (60, '/salary/search/**', '/teacher/tAcademicMonograph', 'tAcademicMonograph', '学术专著和教材', NULL, NULL, 1, 55, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (61, '/salary/search/**', '/teacher/tResearchProject', 'tResearchProject', '制造或设计的产品', NULL, NULL, 1, 55, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (62, '/salary/search/**', '/teacher/tAchievements', 'tAchievements', '学术成果列表', NULL, NULL, 1, 54, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (63, '/salary/search/**', '/teacher/tExamine', 'tExamine', '待审核列表', NULL, NULL, 1, 53, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (64, '/', '/home', 'Home', '指标点管理', 'fa fa-money', NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (65, '/', '/bookview', 'SalBookView', '指标点管理', NULL, NULL, 1, 64, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (66, '/', '/home', 'Home', '成果管理', 'fa fa-money', NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (67, '/', '/admin/SalExamine', 'SalExamine', '待审核', NULL, NULL, 1, 66, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (68, '/', '/admin/SalPaper', 'Paper', '所有成果', NULL, NULL, 1, 66, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (69, '/', '/home', 'Home', '成果申报', 'fa-fa-windows', NULL, 1, 43, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (70, '/', '/home', 'Home', '成果列表', 'fa-fa-windows', NULL, 1, 43, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (71, '/', '/home', 'Home', '毕业论文评审', 'fa fa-windows', NULL, 1, 44, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (72, '/salary/search/**', '/student/PaperComment', 'PaperComment', '毕业论文评审记录', 'fa fa-windows', NULL, 1, 71, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (73, '/salary/search/**', '/teacher/tperact/actList', 'ActList', '活动列表', NULL, NULL, 1, 81, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (74, '/salary/search/**', '/student/infos', 'Infos', '信息项设置', NULL, NULL, 1, 92, 0);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (75, '/salary/search/**', '/teacher/tperact/score', 'Score', '分数', NULL, NULL, 1, 81, 0);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (76, '/salary/search/**', '/student/search', 'Stusearch', '活动列表', NULL, NULL, 1, 92, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (77, '/salary/score/**', '/ActivitM/score', 'SalScore', '分数统计', NULL, NULL, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (78, '', '/ActivitM/detail', 'SalDetail', '小组分数明细', NULL, NULL, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (79, NULL, '/ActivitM/SubActManage', 'SubActManage', '子活动管理', NULL, NULL, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (80, '/salary/search/**', '/secretary/ActManage', 'ActManage', '活动列表', NULL, NULL, 1, 82, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (81, '/', '/home', 'Home', '活动评分', 'fa fa-money', NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (82, '/', '/home', 'Home', '活动管理', 'fa fa-money', NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (83, NULL, '/ActivitM/total', 'SalTotal', '总分项设置', NULL, NULL, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (84, NULL, '/ActivitM/expertScore', 'ExpertScore', '专家打分情况', NULL, NULL, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (85, NULL, '/home', 'Home', '秘书管理', NULL, NULL, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (86, '/salary/search/**', '/teacher/tperact/InformationDetails', 'InformationDetails', '详细信息', NULL, NULL, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (87, NULL, '/secretary/SubActManage', 'SubActManage', '子活动管理', NULL, NULL, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (88, '/salary/search/**', '/teacher/tPaperComment', 'tPaperComment', '教师毕业论文评审', 'fa fa-windows', NULL, 1, 71, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (89, '/salary/sobcfg/**', '/ActivitM/sobcfg', 'SalSobCfg', '专家活动设置', NULL, NULL, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (91, '/salary/sob/**', '/ActivitM/group', 'SalGroup', '导入选手', NULL, NULL, 1, 36, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (92, '/', '/home', 'Home', '参与活动', NULL, NULL, 1, 1, 1);
INSERT INTO `menu` (`id`, `url`, `path`, `component`, `name`, `iconCls`, `keepAlive`, `requireAuth`, `parentId`, `enabled`) VALUES (93, NULL, '/ActivitM/final', 'SalFinalScore', '选手总分', NULL, NULL, NULL, 36, 1);
COMMIT;

-- ----------------------------
-- Table structure for menu_role
-- ----------------------------
DROP TABLE IF EXISTS `menu_role`;
CREATE TABLE `menu_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mid` int DEFAULT NULL,
  `rid` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `mid` (`mid`),
  KEY `rid` (`rid`),
  CONSTRAINT `menu_role_ibfk_1` FOREIGN KEY (`mid`) REFERENCES `menu` (`id`),
  CONSTRAINT `menu_role_ibfk_2` FOREIGN KEY (`rid`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=302 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of menu_role
-- ----------------------------
BEGIN;
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (162, 7, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (163, 9, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (164, 10, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (165, 11, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (166, 12, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (167, 13, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (168, 14, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (169, 15, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (170, 16, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (171, 17, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (172, 18, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (173, 19, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (174, 20, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (175, 21, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (176, 22, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (177, 23, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (178, 25, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (179, 26, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (180, 27, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (181, 28, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (182, 24, 6);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (250, 7, 2);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (251, 8, 2);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (252, 9, 2);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (253, 10, 2);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (254, 12, 2);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (255, 13, 2);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (263, 14, 1);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (264, 15, 1);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (265, 16, 1);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (266, 17, 1);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (267, 18, 1);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (280, 7, 14);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (281, 8, 14);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (282, 9, 14);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (283, 38, 1);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (284, 77, 1);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (285, 31, 1);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (286, 74, 7);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (287, 76, 7);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (288, 78, 1);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (289, 84, 1);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (290, 83, 1);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (291, 73, 3);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (292, 85, 4);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (293, 87, 4);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (294, 16, 4);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (295, 89, 4);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (296, 91, 4);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (297, 31, 4);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (298, 37, 4);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (299, 93, 1);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (300, 93, 4);
INSERT INTO `menu_role` (`id`, `mid`, `rid`) VALUES (301, 80, 4);
COMMIT;

-- ----------------------------
-- Table structure for msgcontent
-- ----------------------------
DROP TABLE IF EXISTS `msgcontent`;
CREATE TABLE `msgcontent` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(64) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of msgcontent
-- ----------------------------
BEGIN;
INSERT INTO `msgcontent` (`id`, `title`, `message`, `createDate`) VALUES (14, '2222222222', '11111111111111111', '2018-02-02 20:46:19');
INSERT INTO `msgcontent` (`id`, `title`, `message`, `createDate`) VALUES (15, '22222222', '3333333333333333333333', '2018-02-02 21:45:57');
INSERT INTO `msgcontent` (`id`, `title`, `message`, `createDate`) VALUES (16, '通知标题1', '通知内容1', '2018-02-03 11:41:39');
INSERT INTO `msgcontent` (`id`, `title`, `message`, `createDate`) VALUES (17, '通知标题2', '通知内容2', '2018-02-03 11:52:37');
INSERT INTO `msgcontent` (`id`, `title`, `message`, `createDate`) VALUES (18, '通知标题3', '通知内容3', '2018-02-03 12:19:41');
COMMIT;

-- ----------------------------
-- Table structure for nation
-- ----------------------------
DROP TABLE IF EXISTS `nation`;
CREATE TABLE `nation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of nation
-- ----------------------------
BEGIN;
INSERT INTO `nation` (`id`, `name`) VALUES (1, '汉族');
INSERT INTO `nation` (`id`, `name`) VALUES (2, '蒙古族');
INSERT INTO `nation` (`id`, `name`) VALUES (3, '回族');
INSERT INTO `nation` (`id`, `name`) VALUES (4, '藏族');
INSERT INTO `nation` (`id`, `name`) VALUES (5, '维吾尔族');
INSERT INTO `nation` (`id`, `name`) VALUES (6, '苗族');
INSERT INTO `nation` (`id`, `name`) VALUES (7, '彝族');
INSERT INTO `nation` (`id`, `name`) VALUES (8, '壮族');
INSERT INTO `nation` (`id`, `name`) VALUES (9, '布依族');
INSERT INTO `nation` (`id`, `name`) VALUES (10, '朝鲜族');
INSERT INTO `nation` (`id`, `name`) VALUES (11, '满族');
INSERT INTO `nation` (`id`, `name`) VALUES (12, '侗族');
INSERT INTO `nation` (`id`, `name`) VALUES (13, '瑶族');
INSERT INTO `nation` (`id`, `name`) VALUES (14, '白族');
INSERT INTO `nation` (`id`, `name`) VALUES (15, '土家族');
INSERT INTO `nation` (`id`, `name`) VALUES (16, '哈尼族');
INSERT INTO `nation` (`id`, `name`) VALUES (17, '哈萨克族');
INSERT INTO `nation` (`id`, `name`) VALUES (18, '傣族');
INSERT INTO `nation` (`id`, `name`) VALUES (19, '黎族');
INSERT INTO `nation` (`id`, `name`) VALUES (20, '傈僳族');
INSERT INTO `nation` (`id`, `name`) VALUES (21, '佤族');
INSERT INTO `nation` (`id`, `name`) VALUES (22, '畲族');
INSERT INTO `nation` (`id`, `name`) VALUES (23, '高山族');
INSERT INTO `nation` (`id`, `name`) VALUES (24, '拉祜族');
INSERT INTO `nation` (`id`, `name`) VALUES (25, '水族');
INSERT INTO `nation` (`id`, `name`) VALUES (26, '东乡族');
INSERT INTO `nation` (`id`, `name`) VALUES (27, '纳西族');
INSERT INTO `nation` (`id`, `name`) VALUES (28, '景颇族');
INSERT INTO `nation` (`id`, `name`) VALUES (29, '柯尔克孜族');
INSERT INTO `nation` (`id`, `name`) VALUES (30, '土族');
INSERT INTO `nation` (`id`, `name`) VALUES (31, '达斡尔族');
INSERT INTO `nation` (`id`, `name`) VALUES (32, '仫佬族');
INSERT INTO `nation` (`id`, `name`) VALUES (33, '羌族');
INSERT INTO `nation` (`id`, `name`) VALUES (34, '布朗族');
INSERT INTO `nation` (`id`, `name`) VALUES (35, '撒拉族');
INSERT INTO `nation` (`id`, `name`) VALUES (36, '毛难族');
INSERT INTO `nation` (`id`, `name`) VALUES (37, '仡佬族');
INSERT INTO `nation` (`id`, `name`) VALUES (38, '锡伯族');
INSERT INTO `nation` (`id`, `name`) VALUES (39, '阿昌族');
INSERT INTO `nation` (`id`, `name`) VALUES (40, '普米族');
INSERT INTO `nation` (`id`, `name`) VALUES (41, '塔吉克族');
INSERT INTO `nation` (`id`, `name`) VALUES (42, '怒族');
INSERT INTO `nation` (`id`, `name`) VALUES (43, '乌孜别克族');
INSERT INTO `nation` (`id`, `name`) VALUES (44, '俄罗斯族');
INSERT INTO `nation` (`id`, `name`) VALUES (45, '鄂温克族');
INSERT INTO `nation` (`id`, `name`) VALUES (46, '崩龙族');
INSERT INTO `nation` (`id`, `name`) VALUES (47, '保安族');
INSERT INTO `nation` (`id`, `name`) VALUES (48, '裕固族');
INSERT INTO `nation` (`id`, `name`) VALUES (49, '京族');
INSERT INTO `nation` (`id`, `name`) VALUES (50, '塔塔尔族');
INSERT INTO `nation` (`id`, `name`) VALUES (51, '独龙族');
INSERT INTO `nation` (`id`, `name`) VALUES (52, '鄂伦春族');
INSERT INTO `nation` (`id`, `name`) VALUES (53, '赫哲族');
INSERT INTO `nation` (`id`, `name`) VALUES (54, '门巴族');
INSERT INTO `nation` (`id`, `name`) VALUES (55, '珞巴族');
INSERT INTO `nation` (`id`, `name`) VALUES (56, '基诺族');
COMMIT;

-- ----------------------------
-- Table structure for oplog
-- ----------------------------
DROP TABLE IF EXISTS `oplog`;
CREATE TABLE `oplog` (
  `id` int NOT NULL AUTO_INCREMENT,
  `addDate` date DEFAULT NULL COMMENT '添加日期',
  `operate` varchar(255) DEFAULT NULL COMMENT '操作内容',
  `hrid` int DEFAULT NULL COMMENT '操作员ID',
  PRIMARY KEY (`id`),
  KEY `hrid` (`hrid`),
  CONSTRAINT `oplog_ibfk_1` FOREIGN KEY (`hrid`) REFERENCES `hr` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of oplog
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for paper
-- ----------------------------
DROP TABLE IF EXISTS `paper`;
CREATE TABLE `paper` (
  `ID` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `studentID` int DEFAULT NULL COMMENT '学号',
  `name` varchar(20) DEFAULT NULL COMMENT '论文题目',
  `year` varchar(50) DEFAULT NULL COMMENT '出版年',
  `month` int DEFAULT NULL COMMENT '出版月',
  `author` varchar(255) DEFAULT NULL COMMENT '作者列表',
  `rank` int DEFAULT NULL COMMENT '排名',
  `total` int DEFAULT NULL COMMENT '总人数',
  `point` int DEFAULT NULL COMMENT '积分',
  `have_score` int DEFAULT NULL,
  `publicationID` int DEFAULT NULL COMMENT '刊物id',
  `pubPage` varchar(128) DEFAULT NULL COMMENT '页码',
  `state` varchar(128) NOT NULL DEFAULT 'commit' COMMENT '状态',
  `url` varchar(255) DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `publication_paper` (`publicationID`) USING BTREE,
  CONSTRAINT `publication_paper` FOREIGN KEY (`publicationID`) REFERENCES `publication` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of paper
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for paper_oper
-- ----------------------------
DROP TABLE IF EXISTS `paper_oper`;
CREATE TABLE `paper_oper` (
  `ID` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `operatorRole` varchar(128) DEFAULT NULL COMMENT '操作人角色',
  `operatorID` int DEFAULT NULL COMMENT '操作人ID',
  `operatorName` varchar(128) DEFAULT NULL COMMENT '操作人姓名',
  `paperID` int DEFAULT NULL COMMENT '论文ID',
  `paperName` varchar(256) DEFAULT NULL COMMENT '论文名称',
  `pubID` int DEFAULT NULL COMMENT '出版物ID',
  `pubName` varchar(256) DEFAULT NULL COMMENT '出版物名称',
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `operation` varchar(128) DEFAULT NULL COMMENT '操作',
  `state` varchar(128) DEFAULT NULL COMMENT '状态',
  `remark` text COMMENT '备注',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of paper_oper
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for papercomment
-- ----------------------------
DROP TABLE IF EXISTS `papercomment`;
CREATE TABLE `papercomment` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `thesisID` int NOT NULL COMMENT '毕业论文或设计的ID',
  `dateStu` date DEFAULT NULL COMMENT '学生的提交的time',
  `dateTea` date DEFAULT NULL COMMENT '老师评价的time',
  `preSum` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL COMMENT '上个阶段工作完成情况',
  `nextPlan` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL COMMENT '下次工作安排',
  `tutorComment` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL COMMENT '导师评价',
  `num` int NOT NULL COMMENT '在单张表单中是第几次',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `thesisID` (`thesisID`) USING BTREE,
  CONSTRAINT `thesisID` FOREIGN KEY (`thesisID`) REFERENCES `thesis` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of papercomment
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for participants
-- ----------------------------
DROP TABLE IF EXISTS `participants`;
CREATE TABLE `participants` (
  `activityID` int DEFAULT NULL,
  `groupID` int DEFAULT NULL,
  `groupID_sub` int DEFAULT NULL,
  `studentID` int DEFAULT NULL,
  `code` varchar(20) DEFAULT NULL,
  `school` varchar(255) DEFAULT NULL COMMENT '本科学校',
  `major` varchar(255) DEFAULT NULL COMMENT '本科专业',
  `sex` varchar(255) DEFAULT NULL COMMENT '性别',
  `examscore` varchar(255) DEFAULT NULL COMMENT '初试成绩',
  `displaySequence` int DEFAULT NULL,
  `score` double(10,2) DEFAULT NULL COMMENT '可以外键scores',
  `ID` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `partidforp` (`studentID`) USING BTREE,
  KEY `activity` (`activityID`) USING BTREE,
  KEY `activityID` (`activityID`,`ID`) USING BTREE,
  CONSTRAINT `participants_ibfk_1` FOREIGN KEY (`activityID`) REFERENCES `activities` (`ID`),
  CONSTRAINT `participants_ibfk_2` FOREIGN KEY (`studentID`) REFERENCES `student` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=488 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of participants
-- ----------------------------
BEGIN;
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2283, '102553230003569', NULL, NULL, NULL, NULL, 1, NULL, 340);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2284, '102553230000979', NULL, NULL, NULL, NULL, 2, NULL, 341);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2285, '102553230001732', NULL, NULL, NULL, NULL, 3, NULL, 342);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2286, '102553230001737', NULL, NULL, NULL, NULL, 4, NULL, 343);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2287, '102553230004339', NULL, NULL, NULL, NULL, 5, NULL, 344);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2288, '102553230000999', NULL, NULL, NULL, NULL, 6, NULL, 345);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2289, '102553230007041', NULL, NULL, NULL, NULL, 7, NULL, 346);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2290, '102553230009989', NULL, NULL, NULL, NULL, 8, NULL, 347);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2291, '102553230003987', NULL, NULL, NULL, NULL, 9, NULL, 348);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2292, '102553230008109', NULL, NULL, NULL, NULL, 10, NULL, 349);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2293, '102553230003229', NULL, NULL, NULL, NULL, 11, NULL, 350);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2294, '102553230003380', NULL, NULL, NULL, NULL, 12, NULL, 351);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2295, '102553230008024', NULL, NULL, NULL, NULL, 13, NULL, 352);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2296, '102553230009331', NULL, NULL, NULL, NULL, 14, NULL, 353);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2297, '102553230004130', NULL, NULL, NULL, NULL, 15, NULL, 354);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2298, '102553230000994', NULL, NULL, NULL, NULL, 16, NULL, 355);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2299, '102553230006435', NULL, NULL, NULL, NULL, 17, NULL, 356);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2300, '102553230008774', NULL, NULL, NULL, NULL, 18, NULL, 357);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2301, '102553230003382', NULL, NULL, NULL, NULL, 19, NULL, 358);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2302, '102553230005315', NULL, NULL, NULL, NULL, 20, NULL, 359);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2303, '102553230001037', NULL, NULL, NULL, NULL, 21, NULL, 360);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2304, '102553230009500', NULL, NULL, NULL, NULL, 1, NULL, 361);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2305, '102553230007051', NULL, NULL, NULL, NULL, 2, NULL, 362);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2306, '102553230002957', NULL, NULL, NULL, NULL, 3, NULL, 363);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2307, '102553230010716', NULL, NULL, NULL, NULL, 4, NULL, 364);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2308, '102553230003381', NULL, NULL, NULL, NULL, 5, NULL, 365);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2309, '102553230006438', NULL, NULL, NULL, NULL, 6, NULL, 366);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2310, '102553230001758', NULL, NULL, NULL, NULL, 7, NULL, 367);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2311, '102553230000967', NULL, NULL, NULL, NULL, 8, NULL, 368);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2312, '102553230004830', NULL, NULL, NULL, NULL, 9, NULL, 369);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2313, '102553230001008', NULL, NULL, NULL, NULL, 10, NULL, 370);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2314, '102553230002417', NULL, NULL, NULL, NULL, 11, NULL, 371);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2315, '102553230002166', NULL, NULL, NULL, NULL, 12, NULL, 372);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2316, '102553230006658', NULL, NULL, NULL, NULL, 13, NULL, 373);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2317, '102553230006803', NULL, NULL, NULL, NULL, 14, NULL, 374);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2318, '102553230006221', NULL, NULL, NULL, NULL, 15, NULL, 375);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2319, '102553230001039', NULL, NULL, NULL, NULL, 16, NULL, 376);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2320, '102553230009101', NULL, NULL, NULL, NULL, 17, NULL, 377);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2321, '102553230001773', NULL, NULL, NULL, NULL, 18, NULL, 378);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2322, '102553230004344', NULL, NULL, NULL, NULL, 19, NULL, 379);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2323, '102553230009527', NULL, NULL, NULL, NULL, 20, NULL, 380);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2324, '102553230003378', NULL, NULL, NULL, NULL, 21, NULL, 381);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2325, '102553230001052', NULL, NULL, NULL, NULL, 1, NULL, 382);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2326, '102553230010706', NULL, NULL, NULL, NULL, 2, NULL, 383);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2327, '102553230009522', NULL, NULL, NULL, NULL, 3, NULL, 384);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2328, '102553230003383', NULL, NULL, NULL, NULL, 4, NULL, 385);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2329, '102553230002580', NULL, NULL, NULL, NULL, 5, NULL, 386);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2330, '102553230007133', NULL, NULL, NULL, NULL, 6, NULL, 387);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2331, '102553230005446', NULL, NULL, NULL, NULL, 7, NULL, 388);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2332, '102553230003398', NULL, NULL, NULL, NULL, 8, NULL, 389);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2333, '102553230006011', NULL, NULL, NULL, NULL, 9, NULL, 390);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2334, '102553230000961', NULL, NULL, NULL, NULL, 10, NULL, 391);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2335, '102553230004184', NULL, NULL, NULL, NULL, 11, NULL, 392);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2336, '102553230004123', NULL, NULL, NULL, NULL, 12, NULL, 393);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2337, '102553230006142', NULL, NULL, NULL, NULL, 13, NULL, 394);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2338, '102553230006119', NULL, NULL, NULL, NULL, 14, NULL, 395);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2339, '102553230007474', NULL, NULL, NULL, NULL, 15, NULL, 396);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2340, '102553230005934', NULL, NULL, NULL, NULL, 16, NULL, 397);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2341, '102553230004672', NULL, NULL, NULL, NULL, 17, NULL, 398);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2342, '102553230006639', NULL, NULL, NULL, NULL, 18, NULL, 399);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2343, '102553230006557', NULL, NULL, NULL, NULL, 19, NULL, 400);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2344, '102553230003985', NULL, NULL, NULL, NULL, 20, NULL, 401);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 36, NULL, 2345, '102553230007828', NULL, NULL, NULL, NULL, 21, NULL, 402);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2346, '102553230003393', NULL, NULL, NULL, NULL, 1, NULL, 403);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2347, '102553230002702', NULL, NULL, NULL, NULL, 2, NULL, 404);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2348, '102553230009284', NULL, NULL, NULL, NULL, 3, NULL, 405);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2349, '102553230010486', NULL, NULL, NULL, NULL, 4, NULL, 406);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2350, '102553230004338', NULL, NULL, NULL, NULL, 5, NULL, 407);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2351, '102553230008043', NULL, NULL, NULL, NULL, 6, NULL, 408);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2352, '102553230001029', NULL, NULL, NULL, NULL, 7, NULL, 409);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2353, '102553230003571', NULL, NULL, NULL, NULL, 8, NULL, 410);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2354, '102553230008740', NULL, NULL, NULL, NULL, 15, NULL, 411);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2355, '102553230007747', NULL, NULL, NULL, NULL, 10, NULL, 412);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2356, '102553230005933', NULL, NULL, NULL, NULL, 11, NULL, 413);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2357, '102553230007130', NULL, NULL, NULL, NULL, 12, NULL, 414);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2358, '102553230001015', NULL, NULL, NULL, NULL, 13, NULL, 415);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2359, '102553230005009', NULL, NULL, NULL, NULL, 14, NULL, 416);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2360, '102553230004350', NULL, NULL, NULL, NULL, 15, NULL, 417);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2361, '102553230004676', NULL, NULL, NULL, NULL, 16, NULL, 418);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2362, '102553230009766', NULL, NULL, NULL, NULL, 17, NULL, 419);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2363, '102553230001041', NULL, NULL, NULL, NULL, 18, NULL, 420);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2364, '102553230009158', NULL, NULL, NULL, NULL, 19, NULL, 421);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2365, '102553230009256', NULL, NULL, NULL, NULL, 20, NULL, 422);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2366, '102553230003363', NULL, NULL, NULL, NULL, 21, NULL, 423);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2367, '102553230004336', NULL, NULL, NULL, NULL, 22, NULL, 424);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2368, '102553230001007', NULL, NULL, NULL, NULL, 1, NULL, 425);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2369, '102553230003374', NULL, NULL, NULL, NULL, 2, NULL, 426);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2370, '102553230002353', NULL, NULL, NULL, NULL, 3, NULL, 427);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2371, '102553230007052', NULL, NULL, NULL, NULL, 4, NULL, 428);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2372, '102553230009731', NULL, NULL, NULL, NULL, 5, NULL, 429);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2373, '102553230003909', NULL, NULL, NULL, NULL, 6, NULL, 430);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2374, '102553230000972', NULL, NULL, NULL, NULL, 7, NULL, 431);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2375, '102553230007291', NULL, NULL, NULL, NULL, 8, NULL, 432);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2376, '102553230009984', NULL, NULL, NULL, NULL, 9, NULL, 433);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2377, '102553230006809', NULL, NULL, NULL, NULL, 10, NULL, 434);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2378, '102553230003212', NULL, NULL, NULL, NULL, 11, NULL, 435);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2379, '102553230005572', NULL, NULL, NULL, NULL, 12, NULL, 436);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2380, '102553230005445', NULL, NULL, NULL, NULL, 13, NULL, 437);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2381, '102553230005903', NULL, NULL, NULL, NULL, 14, NULL, 438);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2382, '102553230001790', NULL, NULL, NULL, NULL, 15, NULL, 439);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2383, '102553230006353', NULL, NULL, NULL, NULL, 16, NULL, 440);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2384, '102553230006630', NULL, NULL, NULL, NULL, 17, NULL, 441);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2385, '102553230003506', NULL, NULL, NULL, NULL, 18, NULL, 442);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2386, '102553230005607', NULL, NULL, NULL, NULL, 19, NULL, 443);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2387, '102553230001009', NULL, NULL, NULL, NULL, 20, NULL, 444);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2388, '102553230005608', NULL, NULL, NULL, NULL, 21, NULL, 445);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 38, NULL, 2389, '102553230001045', NULL, NULL, NULL, NULL, 22, NULL, 446);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2390, '102553230009503', NULL, NULL, NULL, NULL, 1, NULL, 447);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2391, '102553230004316', NULL, NULL, NULL, NULL, 2, NULL, 448);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2392, '102553230007793', NULL, NULL, NULL, NULL, 3, NULL, 449);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2393, '102553230000951', NULL, NULL, NULL, NULL, 4, NULL, 450);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2394, '102553230003401', NULL, NULL, NULL, NULL, 5, NULL, 451);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2395, '102553230003724', NULL, NULL, NULL, NULL, 6, NULL, 452);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2396, '102553230004677', NULL, NULL, NULL, NULL, 7, NULL, 453);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2397, '102553230009084', NULL, NULL, NULL, NULL, 8, NULL, 454);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2398, '102553230003498', NULL, NULL, NULL, NULL, 9, NULL, 455);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2399, '102553230004125', NULL, NULL, NULL, NULL, 10, NULL, 456);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2400, '102553230005307', NULL, NULL, NULL, NULL, 11, NULL, 457);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2401, '102553230003988', NULL, NULL, NULL, NULL, 12, NULL, 458);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2402, '102553230007745', NULL, NULL, NULL, NULL, 13, NULL, 459);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2403, '102553230003979', NULL, NULL, NULL, NULL, 14, NULL, 460);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 37, NULL, 2404, '102553230000971', NULL, NULL, NULL, NULL, 9, NULL, 461);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2405, '102553230003387', NULL, NULL, NULL, NULL, 16, NULL, 462);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2406, '102553230006478', NULL, NULL, NULL, NULL, 17, NULL, 463);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2407, '102553230004187', NULL, NULL, NULL, NULL, 18, NULL, 464);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2408, '102553230006429', NULL, NULL, NULL, NULL, 19, NULL, 465);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2409, '102553230007046', NULL, NULL, NULL, NULL, 20, NULL, 466);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2410, '102553230007048', NULL, NULL, NULL, NULL, 21, NULL, 467);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 39, NULL, 2411, '102553230004038', NULL, NULL, NULL, NULL, 22, NULL, 468);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2412, '102553230002390', NULL, NULL, NULL, NULL, 1, 55.00, 469);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2413, '102553230004487', NULL, NULL, NULL, NULL, 2, 20.00, 470);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2414, '102553230004208', NULL, NULL, NULL, NULL, 3, 50.00, 471);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2415, '102553230006367', NULL, NULL, NULL, NULL, 4, NULL, 472);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2416, '102553230003533', NULL, NULL, NULL, NULL, 5, NULL, 473);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2417, '102553230001247', NULL, NULL, NULL, NULL, 6, 10.00, 474);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2418, '102553230000177', NULL, NULL, NULL, NULL, 7, 50.00, 475);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2419, '102553230009132', NULL, NULL, NULL, NULL, 8, NULL, 476);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2420, '102553230009405', NULL, NULL, NULL, NULL, 9, NULL, 477);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2421, '102553230000174', NULL, NULL, NULL, NULL, 10, NULL, 478);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2422, '102553230003532', NULL, NULL, NULL, NULL, 11, NULL, 479);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2423, '102553230009905', NULL, NULL, NULL, NULL, 12, NULL, 480);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2424, '102553230000178', NULL, NULL, NULL, NULL, 13, 50.00, 481);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2425, '102553230003838', NULL, NULL, NULL, NULL, 14, 50.00, 482);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2426, '102553230000181', NULL, NULL, NULL, NULL, 15, NULL, 483);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2427, '102553230002304', NULL, NULL, NULL, NULL, 16, 50.00, 484);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 40, NULL, 2428, '102553231110000', NULL, NULL, NULL, NULL, 17, 50.00, 485);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 35, NULL, 2429, '102553230001785', NULL, NULL, NULL, NULL, 22, NULL, 486);
INSERT INTO `participants` (`activityID`, `groupID`, `groupID_sub`, `studentID`, `code`, `school`, `major`, `sex`, `examscore`, `displaySequence`, `score`, `ID`) VALUES (34, 34, NULL, 2430, '102553230001748', NULL, NULL, NULL, NULL, 22, NULL, 487);
COMMIT;

-- ----------------------------
-- Table structure for patentresult
-- ----------------------------
DROP TABLE IF EXISTS `patentresult`;
CREATE TABLE `patentresult` (
  `ID` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `type` varchar(50) NOT NULL,
  `time` varchar(50) DEFAULT NULL,
  `rank` int DEFAULT NULL,
  `total` int DEFAULT NULL,
  `rights` varchar(10) DEFAULT NULL,
  `indicaterID` int NOT NULL,
  `state` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `patent` (`indicaterID`) USING BTREE,
  CONSTRAINT `patentresult_ibfk_1` FOREIGN KEY (`indicaterID`) REFERENCES `indicater` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of patentresult
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for politicsstatus
-- ----------------------------
DROP TABLE IF EXISTS `politicsstatus`;
CREATE TABLE `politicsstatus` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of politicsstatus
-- ----------------------------
BEGIN;
INSERT INTO `politicsstatus` (`id`, `name`) VALUES (1, '中共党员');
INSERT INTO `politicsstatus` (`id`, `name`) VALUES (2, '中共预备党员');
INSERT INTO `politicsstatus` (`id`, `name`) VALUES (3, '共青团员');
INSERT INTO `politicsstatus` (`id`, `name`) VALUES (4, '民革党员');
INSERT INTO `politicsstatus` (`id`, `name`) VALUES (5, '民盟盟员');
INSERT INTO `politicsstatus` (`id`, `name`) VALUES (6, '民建会员');
INSERT INTO `politicsstatus` (`id`, `name`) VALUES (7, '民进会员');
INSERT INTO `politicsstatus` (`id`, `name`) VALUES (8, '农工党党员');
INSERT INTO `politicsstatus` (`id`, `name`) VALUES (9, '致公党党员');
INSERT INTO `politicsstatus` (`id`, `name`) VALUES (10, '九三学社社员');
INSERT INTO `politicsstatus` (`id`, `name`) VALUES (11, '台盟盟员');
INSERT INTO `politicsstatus` (`id`, `name`) VALUES (12, '无党派民主人士');
INSERT INTO `politicsstatus` (`id`, `name`) VALUES (13, '普通公民');
COMMIT;

-- ----------------------------
-- Table structure for position
-- ----------------------------
DROP TABLE IF EXISTS `position`;
CREATE TABLE `position` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL COMMENT '职位',
  `createDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `enabled` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of position
-- ----------------------------
BEGIN;
INSERT INTO `position` (`id`, `name`, `createDate`, `enabled`) VALUES (29, '技术总监', '2018-01-11 21:13:42', 1);
INSERT INTO `position` (`id`, `name`, `createDate`, `enabled`) VALUES (30, '运营总监', '2018-01-11 21:13:48', 1);
INSERT INTO `position` (`id`, `name`, `createDate`, `enabled`) VALUES (31, '市场总监', '2018-01-11 00:00:00', 1);
INSERT INTO `position` (`id`, `name`, `createDate`, `enabled`) VALUES (33, '研发工程师', '2018-01-14 00:00:00', 0);
INSERT INTO `position` (`id`, `name`, `createDate`, `enabled`) VALUES (34, '运维工程师', '2018-01-14 16:11:41', 1);
INSERT INTO `position` (`id`, `name`, `createDate`, `enabled`) VALUES (36, 'Java研发经理', '2019-10-01 00:00:00', 1);
COMMIT;

-- ----------------------------
-- Table structure for program
-- ----------------------------
DROP TABLE IF EXISTS `program`;
CREATE TABLE `program` (
  `ID` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `indicatorID` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `program` (`indicatorID`) USING BTREE,
  CONSTRAINT `program_ibfk_1` FOREIGN KEY (`indicatorID`) REFERENCES `indicator` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of program
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for programresult
-- ----------------------------
DROP TABLE IF EXISTS `programresult`;
CREATE TABLE `programresult` (
  `ID` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `source` varchar(100) DEFAULT NULL,
  `sub` varchar(100) DEFAULT NULL,
  `head` varchar(100) DEFAULT NULL,
  `division` varchar(100) DEFAULT NULL,
  `year` int DEFAULT NULL,
  `way` varchar(100) DEFAULT NULL,
  `starttime` varchar(50) DEFAULT NULL,
  `endtime` varchar(50) DEFAULT NULL,
  `programID` int NOT NULL,
  `state` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `programID` (`programID`) USING BTREE,
  CONSTRAINT `programresult_ibfk_1` FOREIGN KEY (`programID`) REFERENCES `program` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of programresult
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `ID` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `studentID` int DEFAULT NULL COMMENT '学号',
  `name` varchar(100) NOT NULL COMMENT '项目名称',
  `source` varchar(100) DEFAULT NULL COMMENT '项目来源',
  `sub` varchar(100) DEFAULT NULL COMMENT '项目来源子类',
  `head` varchar(100) DEFAULT NULL COMMENT '参与人',
  `division` varchar(100) DEFAULT NULL COMMENT '建议',
  `year` int DEFAULT NULL COMMENT '申报年份',
  `way` varchar(100) DEFAULT NULL COMMENT '结项方式',
  `starttime` varchar(50) DEFAULT NULL COMMENT '起始日期',
  `endtime` varchar(50) DEFAULT NULL COMMENT '结束日期',
  `projectTypeID` int NOT NULL COMMENT '项目ID',
  `state` varchar(10) NOT NULL COMMENT '状态',
  `url` varchar(255) DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `projectTypeID` (`projectTypeID`) USING BTREE,
  CONSTRAINT `project_ibfk_1` FOREIGN KEY (`projectTypeID`) REFERENCES `projecttype` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of project
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for project_oper
-- ----------------------------
DROP TABLE IF EXISTS `project_oper`;
CREATE TABLE `project_oper` (
  `ID` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `operatorRole` varchar(128) DEFAULT NULL COMMENT '操作人角色',
  `operatorID` int DEFAULT NULL COMMENT '操作人ID',
  `operatorName` varchar(128) DEFAULT NULL COMMENT '操作人姓名',
  `projectID` int DEFAULT NULL COMMENT '项目ID',
  `projectName` varchar(255) DEFAULT NULL COMMENT '项目名称',
  `projectTypeID` int DEFAULT NULL COMMENT '项目类别ID',
  `projectTypeName` varchar(255) DEFAULT NULL COMMENT '项目类别名称',
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `operation` varchar(128) DEFAULT NULL COMMENT '操作',
  `state` varchar(128) DEFAULT NULL COMMENT '状态',
  `remark` text COMMENT '备注',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of project_oper
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for projecttype
-- ----------------------------
DROP TABLE IF EXISTS `projecttype`;
CREATE TABLE `projecttype` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,
  `indicatorId` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `projectType_Indi` (`indicatorId`) USING BTREE,
  CONSTRAINT `projectType_Indi` FOREIGN KEY (`indicatorId`) REFERENCES `indicator` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of projecttype
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for properties
-- ----------------------------
DROP TABLE IF EXISTS `properties`;
CREATE TABLE `properties` (
  `host` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,
  `id` int NOT NULL,
  `sendHost` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of properties
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for publication
-- ----------------------------
DROP TABLE IF EXISTS `publication`;
CREATE TABLE `publication` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `abbr` varchar(50) DEFAULT NULL,
  `publisher` varchar(50) DEFAULT NULL,
  `url` varchar(50) DEFAULT NULL,
  `level` varchar(50) DEFAULT NULL,
  `indicatorID` int NOT NULL,
  `year` int DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `publication` (`indicatorID`) USING BTREE,
  CONSTRAINT `publication_ibfk_1` FOREIGN KEY (`indicatorID`) REFERENCES `indicator` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of publication
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `nameZh` varchar(64) DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` (`id`, `name`, `nameZh`) VALUES (1, 'ROLE_manager', '部门经理');
INSERT INTO `role` (`id`, `name`, `nameZh`) VALUES (2, 'ROLE_personnel', '人事专员');
INSERT INTO `role` (`id`, `name`, `nameZh`) VALUES (3, 'ROLE_recruiter', 'expert');
INSERT INTO `role` (`id`, `name`, `nameZh`) VALUES (4, 'ROLE_train', 'secretary');
INSERT INTO `role` (`id`, `name`, `nameZh`) VALUES (5, 'ROLE_performance', '薪酬绩效主管');
INSERT INTO `role` (`id`, `name`, `nameZh`) VALUES (6, 'ROLE_admin', '系统管理员');
INSERT INTO `role` (`id`, `name`, `nameZh`) VALUES (7, 'ROLE_participants', 'participant');
INSERT INTO `role` (`id`, `name`, `nameZh`) VALUES (13, 'ROLE_test2', '测试角色2');
INSERT INTO `role` (`id`, `name`, `nameZh`) VALUES (14, 'ROLE_test1', '测试角色1');
INSERT INTO `role` (`id`, `name`, `nameZh`) VALUES (17, 'ROLE_test3', '测试角色3');
INSERT INTO `role` (`id`, `name`, `nameZh`) VALUES (18, 'ROLE_test4', '测试角色4');
INSERT INTO `role` (`id`, `name`, `nameZh`) VALUES (19, 'ROLE_test4', '测试角色4');
INSERT INTO `role` (`id`, `name`, `nameZh`) VALUES (20, 'ROLE_test5', '测试角色5');
INSERT INTO `role` (`id`, `name`, `nameZh`) VALUES (21, 'ROLE_test6', '测试角色6');
INSERT INTO `role` (`id`, `name`, `nameZh`) VALUES (22, 'ROLE_participants', 'participant');
INSERT INTO `role` (`id`, `name`, `nameZh`) VALUES (23, 'ROLE_expert', 'expert');
COMMIT;

-- ----------------------------
-- Table structure for salary
-- ----------------------------
DROP TABLE IF EXISTS `salary`;
CREATE TABLE `salary` (
  `id` int NOT NULL AUTO_INCREMENT,
  `basicSalary` int DEFAULT NULL COMMENT '基本工资',
  `bonus` int DEFAULT NULL COMMENT '奖金',
  `lunchSalary` int DEFAULT NULL COMMENT '午餐补助',
  `trafficSalary` int DEFAULT NULL COMMENT '交通补助',
  `allSalary` int DEFAULT NULL COMMENT '应发工资',
  `pensionBase` int DEFAULT NULL COMMENT '养老金基数',
  `pensionPer` float DEFAULT NULL COMMENT '养老金比率',
  `createDate` timestamp NULL DEFAULT NULL COMMENT '启用时间',
  `medicalBase` int DEFAULT NULL COMMENT '医疗基数',
  `medicalPer` float DEFAULT NULL COMMENT '医疗保险比率',
  `accumulationFundBase` int DEFAULT NULL COMMENT '公积金基数',
  `accumulationFundPer` float DEFAULT NULL COMMENT '公积金比率',
  `name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of salary
-- ----------------------------
BEGIN;
INSERT INTO `salary` (`id`, `basicSalary`, `bonus`, `lunchSalary`, `trafficSalary`, `allSalary`, `pensionBase`, `pensionPer`, `createDate`, `medicalBase`, `medicalPer`, `accumulationFundBase`, `accumulationFundPer`, `name`) VALUES (9, 9000, 4000, 800, 500, NULL, 2000, 0.07, '2018-01-24 00:00:00', 2000, 0.07, 2000, 0.07, '市场部工资账套');
INSERT INTO `salary` (`id`, `basicSalary`, `bonus`, `lunchSalary`, `trafficSalary`, `allSalary`, `pensionBase`, `pensionPer`, `createDate`, `medicalBase`, `medicalPer`, `accumulationFundBase`, `accumulationFundPer`, `name`) VALUES (10, 2000, 2000, 400, 1000, NULL, 2000, 0.07, '2018-01-01 00:00:00', 2000, 0.07, 2000, 0.07, '人事部工资账套');
INSERT INTO `salary` (`id`, `basicSalary`, `bonus`, `lunchSalary`, `trafficSalary`, `allSalary`, `pensionBase`, `pensionPer`, `createDate`, `medicalBase`, `medicalPer`, `accumulationFundBase`, `accumulationFundPer`, `name`) VALUES (13, 20000, 3000, 500, 500, NULL, 4000, 0.07, '2018-01-25 00:00:00', 4000, 0.07, 4000, 0.07, '运维部工资账套');
COMMIT;

-- ----------------------------
-- Table structure for scoreitem
-- ----------------------------
DROP TABLE IF EXISTS `scoreitem`;
CREATE TABLE `scoreitem` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `activityID` int DEFAULT NULL,
  `name` varchar(60) DEFAULT NULL,
  `score` double(10,2) DEFAULT NULL,
  `coef` double DEFAULT NULL,
  `comment` varchar(2000) DEFAULT NULL,
  `byExpert` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `activityID` (`activityID`) USING BTREE,
  KEY `activityID_2` (`activityID`,`name`) USING BTREE,
  CONSTRAINT `scoreitem_ibfk_1` FOREIGN KEY (`activityID`) REFERENCES `activities` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of scoreitem
-- ----------------------------
BEGIN;
INSERT INTO `scoreitem` (`ID`, `activityID`, `name`, `score`, `coef`, `comment`, `byExpert`) VALUES (110, 34, '活动得分', 100.00, 1, NULL, 1);
INSERT INTO `scoreitem` (`ID`, `activityID`, `name`, `score`, `coef`, `comment`, `byExpert`) VALUES (111, 34, '生源素养', 30.00, 1, '985或211院校30分，省重点20分，其他10分', 1);
INSERT INTO `scoreitem` (`ID`, `activityID`, `name`, `score`, `coef`, `comment`, `byExpert`) VALUES (112, 34, '自我介绍', 20.00, 1, '本科阶段综合表现与申请材料', 1);
INSERT INTO `scoreitem` (`ID`, `activityID`, `name`, `score`, `coef`, `comment`, `byExpert`) VALUES (113, 34, '英文翻译', 20.00, 1, NULL, 1);
INSERT INTO `scoreitem` (`ID`, `activityID`, `name`, `score`, `coef`, `comment`, `byExpert`) VALUES (114, 34, 'TOPIC', 20.00, 1, NULL, 1);
INSERT INTO `scoreitem` (`ID`, `activityID`, `name`, `score`, `coef`, `comment`, `byExpert`) VALUES (115, 34, '回答问题', 30.00, 1, NULL, 1);
INSERT INTO `scoreitem` (`ID`, `activityID`, `name`, `score`, `coef`, `comment`, `byExpert`) VALUES (116, 37, '活动得分', 100.00, 1, NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for scores
-- ----------------------------
DROP TABLE IF EXISTS `scores`;
CREATE TABLE `scores` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `expertID` int DEFAULT NULL,
  `activityID` int DEFAULT NULL,
  `participantID` int DEFAULT NULL,
  `scoreItemID` int DEFAULT NULL,
  `score` double(10,2) DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `expertID` (`expertID`) USING BTREE,
  KEY `activityID_2` (`activityID`) USING BTREE,
  KEY `participantID` (`participantID`) USING BTREE,
  KEY `scoreItemID` (`scoreItemID`) USING BTREE,
  KEY `participantID_2` (`participantID`,`scoreItemID`) USING BTREE,
  CONSTRAINT `scores_ibfk_1` FOREIGN KEY (`activityID`) REFERENCES `activities` (`ID`),
  CONSTRAINT `scores_ibfk_2` FOREIGN KEY (`scoreItemID`) REFERENCES `scoreitem` (`ID`),
  CONSTRAINT `scores_ibfk_3` FOREIGN KEY (`participantID`) REFERENCES `participants` (`ID`),
  CONSTRAINT `scores_ibfk_4` FOREIGN KEY (`expertID`) REFERENCES `teacher` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1356 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of scores
-- ----------------------------
BEGIN;
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (816, NULL, 34, 340, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (817, NULL, 34, 341, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (818, NULL, 34, 342, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (819, NULL, 34, 343, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (820, NULL, 34, 344, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (821, NULL, 34, 345, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (822, NULL, 34, 346, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (823, NULL, 34, 347, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (824, NULL, 34, 348, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (825, NULL, 34, 349, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (826, NULL, 34, 350, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (827, NULL, 34, 351, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (828, NULL, 34, 352, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (829, NULL, 34, 353, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (830, NULL, 34, 354, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (831, NULL, 34, 355, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (832, NULL, 34, 356, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (833, NULL, 34, 357, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (834, NULL, 34, 358, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (835, NULL, 34, 359, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (836, NULL, 34, 360, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (837, NULL, 34, 361, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (838, NULL, 34, 362, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (839, NULL, 34, 363, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (840, NULL, 34, 364, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (841, NULL, 34, 365, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (842, NULL, 34, 366, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (843, NULL, 34, 367, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (844, NULL, 34, 368, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (845, NULL, 34, 369, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (846, NULL, 34, 370, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (847, NULL, 34, 371, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (848, NULL, 34, 372, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (849, NULL, 34, 373, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (850, NULL, 34, 374, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (851, NULL, 34, 375, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (852, NULL, 34, 376, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (853, NULL, 34, 377, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (854, NULL, 34, 378, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (855, NULL, 34, 379, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (856, NULL, 34, 380, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (857, NULL, 34, 381, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (858, NULL, 34, 382, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (859, NULL, 34, 383, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (860, NULL, 34, 384, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (861, NULL, 34, 385, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (862, NULL, 34, 386, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (863, NULL, 34, 387, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (864, NULL, 34, 388, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (865, NULL, 34, 389, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (866, NULL, 34, 390, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (867, NULL, 34, 391, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (868, NULL, 34, 392, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (869, NULL, 34, 393, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (870, NULL, 34, 394, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (871, NULL, 34, 395, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (872, NULL, 34, 396, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (873, NULL, 34, 397, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (874, NULL, 34, 398, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (875, NULL, 34, 399, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (876, NULL, 34, 400, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (877, NULL, 34, 401, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (878, NULL, 34, 402, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (879, NULL, 34, 403, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (880, NULL, 34, 404, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (881, NULL, 34, 405, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (882, NULL, 34, 406, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (883, NULL, 34, 407, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (884, NULL, 34, 408, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (885, NULL, 34, 409, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (886, NULL, 34, 410, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (887, NULL, 34, 411, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (888, NULL, 34, 412, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (889, NULL, 34, 413, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (890, NULL, 34, 414, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (891, NULL, 34, 415, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (892, NULL, 34, 416, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (893, NULL, 34, 417, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (894, NULL, 34, 418, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (895, NULL, 34, 419, 111, 15.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (896, NULL, 34, 420, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (897, NULL, 34, 421, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (898, NULL, 34, 422, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (899, NULL, 34, 423, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (900, NULL, 34, 424, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (901, NULL, 34, 425, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (902, NULL, 34, 426, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (903, NULL, 34, 427, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (904, NULL, 34, 428, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (905, NULL, 34, 429, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (906, NULL, 34, 430, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (907, NULL, 34, 431, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (908, NULL, 34, 432, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (909, NULL, 34, 433, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (910, NULL, 34, 434, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (911, NULL, 34, 435, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (912, NULL, 34, 436, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (913, NULL, 34, 437, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (914, NULL, 34, 438, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (915, NULL, 34, 439, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (916, NULL, 34, 440, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (917, NULL, 34, 441, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (918, NULL, 34, 442, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (919, NULL, 34, 443, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (920, NULL, 34, 444, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (921, NULL, 34, 445, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (922, NULL, 34, 446, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (923, NULL, 34, 447, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (924, NULL, 34, 448, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (925, NULL, 34, 449, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (926, NULL, 34, 450, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (927, NULL, 34, 451, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (928, NULL, 34, 452, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (929, NULL, 34, 453, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (930, NULL, 34, 454, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (931, NULL, 34, 455, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (932, NULL, 34, 456, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (933, NULL, 34, 457, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (934, NULL, 34, 458, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (935, NULL, 34, 459, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (936, NULL, 34, 460, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (937, NULL, 34, 461, 111, 15.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (938, NULL, 34, 462, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (939, NULL, 34, 463, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (940, NULL, 34, 464, 111, 15.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (941, NULL, 34, 465, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (942, NULL, 34, 466, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (943, NULL, 34, 467, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (944, NULL, 34, 468, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (945, NULL, 34, 469, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (946, NULL, 34, 470, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (947, NULL, 34, 471, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (948, NULL, 34, 472, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (949, NULL, 34, 473, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (950, NULL, 34, 474, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (951, NULL, 34, 475, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (952, NULL, 34, 476, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (953, NULL, 34, 477, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (954, NULL, 34, 478, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (955, NULL, 34, 479, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (956, NULL, 34, 480, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (957, NULL, 34, 481, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (958, NULL, 34, 482, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (959, NULL, 34, 483, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (960, NULL, 34, 484, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (961, NULL, 34, 485, 111, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (968, NULL, 34, 486, 111, 30.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (969, NULL, 34, 487, 111, 15.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1259, 1061, 34, 470, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1260, 1061, 34, 470, 112, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1261, 1061, 34, 470, 110, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1262, 1061, 34, 470, 114, 0.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1263, 1061, 34, 470, 113, 0.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1264, 1061, 34, 470, 115, 0.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1265, 1061, 34, 471, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1266, 1061, 34, 471, 112, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1267, 1061, 34, 471, 110, 50.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1268, 1061, 34, 471, 114, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1269, 1061, 34, 471, 113, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1270, 1061, 34, 471, 115, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1271, 1061, 34, 473, 111, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1272, 1061, 34, 473, 112, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1273, 1061, 34, 473, 110, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1274, 1061, 34, 473, 114, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1275, 1061, 34, 473, 113, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1276, 1061, 34, 473, 115, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1277, 1061, 34, 474, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1278, 1061, 34, 474, 112, 0.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1279, 1061, 34, 474, 110, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1280, 1061, 34, 474, 114, 0.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1281, 1061, 34, 474, 113, 0.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1282, 1061, 34, 474, 115, 0.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1283, 1061, 34, 475, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1284, 1061, 34, 475, 112, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1285, 1061, 34, 475, 110, 50.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1286, 1061, 34, 475, 114, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1287, 1061, 34, 475, 113, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1288, 1061, 34, 475, 115, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1289, 1061, 34, 476, 111, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1290, 1061, 34, 476, 112, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1291, 1061, 34, 476, 110, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1292, 1061, 34, 476, 114, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1293, 1061, 34, 476, 113, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1294, 1061, 34, 476, 115, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1295, 1061, 34, 477, 111, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1296, 1061, 34, 477, 112, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1297, 1061, 34, 477, 110, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1298, 1061, 34, 477, 114, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1299, 1061, 34, 477, 113, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1300, 1061, 34, 477, 115, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1301, 1061, 34, 478, 111, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1302, 1061, 34, 478, 112, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1303, 1061, 34, 478, 110, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1304, 1061, 34, 478, 114, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1305, 1061, 34, 478, 113, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1306, 1061, 34, 478, 115, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1307, 1061, 34, 479, 111, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1308, 1061, 34, 479, 112, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1309, 1061, 34, 479, 110, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1310, 1061, 34, 479, 114, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1311, 1061, 34, 479, 113, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1312, 1061, 34, 479, 115, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1313, 1061, 34, 480, 111, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1314, 1061, 34, 480, 112, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1315, 1061, 34, 480, 110, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1316, 1061, 34, 480, 114, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1317, 1061, 34, 480, 113, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1318, 1061, 34, 480, 115, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1319, 1061, 34, 481, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1320, 1061, 34, 481, 112, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1321, 1061, 34, 481, 110, 50.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1322, 1061, 34, 481, 114, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1323, 1061, 34, 481, 113, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1324, 1061, 34, 481, 115, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1325, 1061, 34, 482, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1326, 1061, 34, 482, 112, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1327, 1061, 34, 482, 110, 50.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1328, 1061, 34, 482, 114, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1329, 1061, 34, 482, 113, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1330, 1061, 34, 482, 115, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1331, 1061, 34, 483, 111, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1332, 1061, 34, 483, 112, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1333, 1061, 34, 483, 110, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1334, 1061, 34, 483, 114, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1335, 1061, 34, 483, 113, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1336, 1061, 34, 483, 115, NULL);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1337, 1061, 34, 484, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1338, 1061, 34, 484, 112, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1339, 1061, 34, 484, 110, 50.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1340, 1061, 34, 484, 114, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1341, 1061, 34, 484, 113, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1342, 1061, 34, 484, 115, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1343, 1061, 34, 485, 111, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1344, 1061, 34, 485, 112, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1345, 1061, 34, 485, 110, 50.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1346, 1061, 34, 485, 114, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1347, 1061, 34, 485, 113, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1348, 1061, 34, 485, 115, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1349, 1061, 34, 469, 111, 5.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1350, 1061, 34, 469, 112, 20.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1351, 1061, 34, 469, 110, 55.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1352, 1061, 34, 469, 114, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1353, 1061, 34, 469, 113, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1354, 1061, 34, 469, 115, 10.00);
INSERT INTO `scores` (`ID`, `expertID`, `activityID`, `participantID`, `scoreItemID`, `score`) VALUES (1355, 1061, 34, 472, 110, NULL);
COMMIT;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  `institutionID` int DEFAULT NULL COMMENT '外键',
  `studentnumber` varchar(20) DEFAULT NULL,
  `IDNumber` varchar(30) NOT NULL,
  `telephone` varchar(11) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `year` int DEFAULT NULL,
  `deleteflag` int NOT NULL,
  `tutorID` int DEFAULT NULL,
  `role` varchar(255) NOT NULL DEFAULT '7;',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `tutorID` (`tutorID`) USING BTREE,
  KEY `ID` (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2431 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of student
-- ----------------------------
BEGIN;
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2283, '吴辰宇', NULL, NULL, '321002200008121830', '18361822357', '1049639741@qq.com', '102553230003569', '95b4bd2d75f10ea707053e1585eca269def6c740', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2284, '沈俞吉', NULL, NULL, '310112199909243616', '18964983416', 'm18964983416@163.com', '102553230000979', 'dc096777ca57cc77986ad86142d2cc3356f7f359', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2285, '陈琳', NULL, NULL, '532801200108170661', '17701711835', '2862085054@qq.com', '102553230001732', '2893509d81d224a5659666404298042e8653c3a5', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2286, '方寓卓', NULL, NULL, '310106199801062816', '13585626597', 'fangyuzhuo98@163.com', '102553230001737', 'ac290f21fda2771ec06a143a864ac997593c3016', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2287, '叶竹文', NULL, NULL, '340827200102190051', '15851998747', '3507746221@qq.com', '102553230004339', '117e2464e9add2b48181c3088d2256dcd7bbdcf3', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2288, '王儒', NULL, NULL, '510722200104232248', '19145639790', 'hauixuwr@163.com', '102553230000999', '29f0953a1e887773d21289799b0f0c7ec2997ac7', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2289, '王合庆', NULL, NULL, '370284199910071215', '18354266479', '1870893649@qq.com', '102553230007041', 'f1d15eb6543d19448a7f636ccbd5107d2d1eea38', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2290, '叶鹏', NULL, NULL, '500221200008102413', '13896182471', '1977804614@qq.com', '102553230009989', 'f09c794e7fd9b16644b7afd5b42989cd3afebac4', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2291, '丁徐杰', NULL, NULL, '321084200102137016', '18118262963', '1633292312@qq.com', '102553230003987', 'f389a95be510780ce733da88da6e1ec77a9d31b6', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2292, '李昌鑫', NULL, NULL, '410181199609156058', '15638919887', '975138693@qq.com', '102553230008109', '9086105c69796eb384c39296d8fa9e559040aa1a', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2293, '侯佳琪', NULL, NULL, '232326200006030042', '17332312215', '2472854140@qq.com', '102553230003229', 'f94be8419bd3cae538e9c88271f20b84deda48b0', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2294, '严哲', NULL, NULL, '320585200012240055', '15850294280', '2697119095@qq.com', '102553230003380', '336ec94d53345adb026cfe24d9f1868b7bdfd4a2', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2295, '周磊', NULL, NULL, '411303199908276810', '17719866814', '1696231875@qq.com', '102553230008024', '1787390f8b1c791e6e7ad11109490fcb1a47eed7', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2296, '郑洛豪', NULL, NULL, '430521200203054275', '13973918707', '1936018865@qq.com', '102553230009331', '5d24b4a63d44a1456766cd1b553cf5659e1f59ec', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2297, '金天龙', NULL, NULL, '320682200012167296', '15050631947', '2135687434@qq.com', '102553230004130', 'ad5fcc69f21498dffda8b0d28042614267824764', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2298, '王才强', NULL, NULL, '340123199912263612', '18255153754', '2043913633@qq.com', '102553230000994', '2a4de6245a6ced43600f16482459bc363ef4922f', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2299, '洪良昆', NULL, NULL, '360124200109070913', '18172814895', '1973898638@qq.com', '102553230006435', '26dbedd10cdbca54077fb86989e99b0ab3e12573', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2300, '汤宇', NULL, NULL, '421022200008103615', '15827725472', '498605707@qq.com', '102553230008774', '7857854e4784b8953ae14ddefd9303fdfde1f4ee', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2301, '王逸琪', NULL, NULL, '320282200012264422', '18168026562', '1522135255@qq.com', '102553230003382', '45d54bbb229d723b932bed4ac8f7f60d462b9715', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2302, '王存鑫', NULL, NULL, '340323200102020017', '15055159352', '1531722340@qq.com', '102553230005315', 'ebf792a859bade486a49bc6b37cab804068a9320', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2303, '张雨晨', NULL, NULL, '210203200012196027', '15840677245', '2819220237@qq.com', '102553230001037', 'b07f2a3e452e37f4a66d1a653f09b9384ea8c914', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2304, '唐世文', NULL, NULL, '430482199812278575', '18007479036', 'fkcaikengren@163.com', '102553230009500', '2dec8f600599becf1895aec1bf18570279615ce4', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2305, '刘明涵', NULL, NULL, '371202200103047413', '19863737193', '2564524683@qq.com', '102553230007051', '91bfaaa3ff76689012eefb728049ce5d1b3455d5', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2306, '高洋', NULL, NULL, '411426200201258032', '18336982506', 'gy18336982506@163.com', '102553230002957', '63bbfd0a5fab88505c65b3b98adb0cdbe01f3ad6', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2307, '白福渊', NULL, NULL, '620122200002281017', '18609494928', '2542208373@qq.com', '102553230010716', 'e3e2937bdd156ae7420c7846ccc9f023accfdf32', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2308, '王宇', NULL, NULL, '320621200012266320', '19851398163', '2461537371@qq.com', '102553230003381', '5fa7b3f430171c3769b870311d053001d7b514c9', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2309, '刘水萍', NULL, NULL, '360781200112234229', '14717520124', '3140242154@qq.com', '102553230006438', 'a921ba9ca89bcd62b9900f814d4be4d98d3cc110', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2310, '石峰', NULL, NULL, '310108200008290010', '13524891768', '1527332400@qq.com', '102553230001758', '519238fb85e68abb142dd04d8566240eb0c71ce4', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2311, '陆嘉玮', NULL, NULL, '310105200105043610', '18918595115', '2708595115@qq.com', '102553230000967', '0415e3899ce3abbfb73516e7b0460c3b4a9ab85d', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2312, '赵杭凯', NULL, NULL, '339005200011180610', '18329190581', '312327943@qq.com', '102553230004830', 'f125e69699f1132481966424ede3ae903df6c4fb', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2313, '吴晓辉', NULL, NULL, '310115199909258614', '13817557701', '2285881481@qq.com', '102553230001008', '600b2ce112e41d1b33c5a903baf9fdc5d9ced5d5', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2314, '张泽', NULL, NULL, '130224200104207311', '18733510550', '1006449886@qq.com', '102553230002417', 'd2365d86fbc9279786dba67dbfaa6db9e61f7583', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2315, '韩银鑫', NULL, NULL, '411303200002166899', '15660026891', '419884298@qq.com', '102553230002166', '6f87fb7b5b022f07e0679c3b72e1d61173f95872', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2316, '罗杰豪', NULL, NULL, '360121200105101250', '17511683035', '1787033211@qq.com', '102553230006658', 'ba3e78eb44980c9a2a96e11bab3f659a2f6f978b', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2317, '黄好阳', NULL, NULL, '341623200107228316', '17805687065', '2291284425@qq.com', '102553230006803', '96f5c7418508a12dcb96708c42df5a5e74aa25c0', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2318, '阮湘舒', NULL, NULL, '352202200105165146', '15160276697', '2899958233@qq.com', '102553230006221', '532a15b2935367ebf7360f620446088b00cbd43e', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2319, '赵龙', NULL, NULL, '41042219991230913X', '15737580821', '1403599025@qq.com', '102553230001039', '11be4b7fde0b84de5bdbf529f17d0d5363cab674', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2320, '贺永权', NULL, NULL, '421083200012290014', '15572150608', '962602309@qq.com', '102553230009101', 'e9cf103852e1acedc50cc73c3a632276147bbbd3', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2321, '颜斯敖', NULL, NULL, '310110200001083715', '18817802151', '2683279477@qq.com', '102553230001773', '41fc868d2d52ee90db9dc81e804a6f281711e158', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2322, '张爱琳', NULL, NULL, '220302200106170822', '18743402288', '1281787058@qq.com', '102553230004344', '57bcbf55bf631268d63ad0887eea2cb66a68f4f3', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2323, '易宇琼', NULL, NULL, '430624200101200029', '18774983570', '1113155736@qq.com', '102553230009527', 'db7181300341f3533fbbd40e0654b8b04591fba6', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2324, '王雅菲', NULL, NULL, '321201200012160220', '19825035338', '1821177658@qq.com', '102553230003378', '7734c61fd21aa05043c6214f22d38343b990390f', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2325, '朱天成', NULL, NULL, '310104200005042817', '19946284323', '1825174643@qq.com', '102553230001052', 'ac5924e122be9e788134e95f5870aa827111b70d', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2326, '孙益强', NULL, NULL, '360731200006280092', '18379706166', '2741445561@qq.com', '102553230010706', '2627c512f7052da8d8fc7a72abd17824c0a29bdd', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2327, '谭栋荣', NULL, NULL, '430525200111264537', '14786666099', '3281568064@qq.com', '102553230009522', '30fdfe97af505d3c06e335e5ba2e386752f1a0ea', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2328, '鲍子安', NULL, NULL, '321283200012292033', '19822653270', '1196867477@qq.com', '102553230003383', 'e0711eafaaf186f1912abc7855f597964c0cd026', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2329, '杨昊', NULL, NULL, '140302200001041231', '18335300852', '2399014211@qq.com', '102553230002580', '7187ec718fe7281338eb5a6fc5c631a3d10e11f6', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2330, '王洪斌', NULL, NULL, '371326200106096110', '15865136192', '2898102824@qq.com', '102553230007133', 'fab59f374a7ca08da2a1d34a5efdd01c3de0cf1a', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2331, '李潇', NULL, NULL, '340403200009151637', '18205547415', '961154757@qq.com', '102553230005446', 'a7b426d4a3f637d868c03faff91c896f6ffc5fc1', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2332, '杨聪', NULL, NULL, '429006200109072739', '15861807239', '2572006236@qq.com', '102553230003398', 'ec778c4a971812b811f842e840ada5d9438fcda2', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2333, '朱子杨', NULL, NULL, '340123200106073131', '18110963385', '2482613489@qq.com', '102553230006011', '58014782466093abb7f885a1bbdc4b7c93a86297', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2334, '刘亮', NULL, NULL, '41152720001209101X', '13253813093', '1720076693@qq.com', '102553230000961', '09bf1fea976f85bed20ef6593e9cae01d2a7470c', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2335, '张渝', NULL, NULL, '320721199901291630', '15751765993', '1483727037@qq.com', '102553230004184', 'c448a94915eae3b1687f6ab61964eb6fa0860af9', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2336, '丁洋', NULL, NULL, '320981199903234215', '18361133958', '761631967@qq.com', '102553230004123', 'c23db5e09075df20dd1072913abf3e596ddafab3', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2337, '王浩宇', NULL, NULL, '340123200110064334', '19165523031', '1730304643@qq.com', '102553230006142', '6f9d44535a9efcea8c65c1fb6c18f142000e569d', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2338, '江纯杰', NULL, NULL, '340823200009160813', '18019575617', '1546627264@qq.com', '102553230006119', 'af220e563631a66c21d7074b58174a8e2fe23fa9', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2339, '汤昊', NULL, NULL, '371327199906280013', '17861403103', 'capcn17@yeah.net', '102553230007474', 'c6af8d2395abe3e4944d0f0a05d3a823afbc353f', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2340, '吴嘉伟', NULL, NULL, '340104200103120511', '19810654312', '2565216907@qq.com', '102553230005934', '672acc61349f7885b31caacd44e2a4ee1fc682c1', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2341, '毛徐彬', NULL, NULL, '33250220000919517X', '18516123529', '1325386318@qq.com', '102553230004672', '23ca15babedf62d47fb6d657f283e35832b57348', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2342, '饶木平', NULL, NULL, '362331200103101017', '17879389253', '2805175042@qq.com', '102553230006639', '03b54c934e7f6f4c06b802e11f11675be6a98932', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2343, '谢伟', NULL, NULL, '360731200104204835', '18720829200', '2574028857@qq.com', '102553230006557', 'f9a736184c6262fc625f609c86bc1d198c269e55', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2344, '姜龙翔', NULL, NULL, '341126200009025912', '19551643475', '2573129775@qq.com', '102553230003985', 'aafdc2268a50a3af3d380fb6075c3709b8af9f7c', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2345, '刘家宁', NULL, NULL, '411282199807300025', '18070043761', '3070992601@qq.com', '102553230007828', 'e507fe35707875bccef0f8d7a3da93d295776128', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2346, '周颖苓', NULL, NULL, '320404200106112843', '13016852735', '1483644421@qq.com', '102553230003393', 'a1f530c848cd1b37f1ff7b2858eff9a2bbd72d27', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2347, '崔逸', NULL, NULL, '140123199905114419', '15234112026', '403801745@qq.com', '102553230002702', '6299de12d4c9f994b91dc1e044e1999328d3662a', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2348, '陆慧', NULL, NULL, '430523200210167625', '19848042886', '246315426@qq.com', '102553230009284', 'f5cf57d8abcfad9b7d594266787549cba8b97d3b', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2349, '廖建权', NULL, NULL, '362424200010286413', '18720682658', '2023252977@qq.com', '102553230010486', '130b9493eb8df36f8c7299c71c8080fbb96aa945', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2350, '潘杰', NULL, NULL, '320911200102160312', '18800573668', '1414937284@qq.com', '102553230004338', '130a68bc134292add80b00844e15c4f8ae746204', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2351, '胡定方', NULL, NULL, '362201200111215416', '15807055042', '1486425992@qq.com', '102553230008043', '4786666b2f19ec07bf81022060dfba91e47823ef', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2352, '张颢译', NULL, NULL, '520103200109154031', '13984864338', '1844019086@qq.com', '102553230001029', '04100f7cecfcb4aa4504ce69304f1858053de73e', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2353, '刘佳康', NULL, NULL, '320324200102066835', '15851204758', '1850671659@qq.com', '102553230003571', '637cc3ddbb3df2256b58423fe4bb9113a2b63ff8', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2354, '刘骏', NULL, NULL, '431129200202122213', '13789222589', '674408862@qq.com', '102553230008740', '2eef3865672188907e868ce1d3737177b8dd3963', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2355, '丁炜晨', NULL, NULL, '410702200001122012', '17630152639', 'Dingwc2000@163.com', '102553230007747', '913870518b365fffbe1ad8db1e2d252655cc09c3', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2356, '龚佳伟', NULL, NULL, '341222200103025978', '19154083207', '1487714568@qq.com', '102553230005933', 'c26338055ae65eb1f7081843511185e0756c9e88', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2357, '李晓康', NULL, NULL, '372325200005241213', '18754396031', '1315380424@qq.com', '102553230007130', '1fecb00da5fdc25b197f21a548950ac6801234d2', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2358, '徐艺彤', NULL, NULL, '370921200105262420', '15069855734', '137550421@qq.com', '102553230001015', '1957420fe1384b303e75521cfc2ed82f56d54f46', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2359, '谢继', NULL, NULL, '34132320000124141X', '18157194718', '623748469@qq.com', '102553230005009', '1fdb7b5830dd03b398694e0f95c264383d1532ae', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2360, '叶曼杰', NULL, NULL, '320803200109143851', '19827038029', '1379496685@qq.com', '102553230004350', '9b1b58448c1690fec718c8e7b45b4f8e2931ffac', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2361, '刘楚海', NULL, NULL, '42108120010408061X', '17366638495', '1078332391@qq.com', '102553230004676', 'f17cbfa2b7a47e9c3b0252fd16e26428d0fd8d2f', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2362, '赵亿', NULL, NULL, '370902200101093915', '18764871726', '799266424@qq.com', '102553230009766', 'c7c66aad0edd01142be27ae81b01ee062408eb02', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2363, '赵亚东', NULL, NULL, '310113200010076532', '13916796672', 'iamzy17@126.com', '102553230001041', '8b2a4459fb85c909bc0eea5ef0b965a11160069f', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2364, '张航', NULL, NULL, '420921200001045717', '15871312316', '3067489861@qq.com', '102553230009158', 'fe442be5bf46f2e78638bf434025cd072c453428', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2365, '杨东林', NULL, NULL, '430525200106081111', '19967871807', '2394917615@qq.com', '102553230009256', 'ec69cb6d25936a2eae9574827ccaa17a257b2516', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2366, '成实', NULL, NULL, '320925199909280617', '18261938688', '924947322@qq.com', '102553230003363', '97018edb1cddb54bc85fe73733881f19c4fb24dc', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2367, '应俊', NULL, NULL, '320902200101191014', '15996553205', '1715128787@qq.com', '102553230004336', '430bfd635dfcf353697eeab4e303681d8e5bc94b', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2368, '吴航', NULL, NULL, '340822200107310719', '15015104311', '1439111092@qq.com', '102553230001007', '672ff629288aadd9b63a1ca2ed3c6131ef49662e', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2369, '马忠海', NULL, NULL, '342626200010140510', '18895377530', '1736186579@qq.com', '102553230003374', 'ee7d1015e038b679204848c0bd57263d5a536734', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2370, '胡欣怡', NULL, NULL, '131025200012170060', '15512640822', '1097486747@qq.com', '102553230002353', 'fc9f6685302da8e614ad10d0d05c8825c57cb67d', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2371, '孙琳', NULL, NULL, '371002200103231041', '19862289754', '1134257738@qq.com', '102553230007052', '06169262c6e7d7dea11f1923cc3749c1309fbeb0', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2372, '项辰琪', NULL, NULL, '330825200012283719', '18857024780', '19cqxiang@stu.edu.cn', '102553230009731', '8fa671c362f6851a6d6a91a28e591f6738abb723', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2373, '王学一', NULL, NULL, '320802200106012518', '13952381657', '1732988791@qq.com', '102553230003909', '56f0d7a2c74c18797bb2ddaee6808c177f803fd3', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2374, '莫振高', NULL, NULL, '522722200109180014', '18085426679', '1226029007@qq.com', '102553230000972', '8510db0bfa445dcee7fcd99d736ef9ede163bc2b', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2375, '李魁彦', NULL, NULL, '370702199908102222', '13752779769', '894392425@qq.com', '102553230007291', '90610fbd3e7d8e0060d69d693c6d9e4a7463214a', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2376, '徐裕鹏', NULL, NULL, '362502200105122814', '18879446585', '1436188652@qq.com', '102553230009984', 'f85011f0a41c2053d85b394a11ab8e8dac893afd', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2377, '李馨语', NULL, NULL, '371328200201130041', '18766165810', '1792708234@qq.com', '102553230006809', 'e115bc98f2d86ae6a845d588d8cf651c468aa424', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2378, '李昕阳', NULL, NULL, '231002199812112022', '19946290025', '811399512@qq.com', '102553230003212', '163bd0a74e092081707e53b15602ae4d9f9babeb', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2379, '檀文文', NULL, NULL, '340827200007166045', '13275875912', '1252228606@qq.com', '102553230005572', 'a3fcce175c4ed62ad101f32278be18bd96215ea1', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2380, '孟春光', NULL, NULL, '34242219991202463X', '18256459224', '66137883@qq.com', '102553230005445', 'ac3b02e7d685b28a137a6f1e988a036c9088585a', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2381, '徐鹏义', NULL, NULL, '340721200107040614', '19810621029', '2428507302@qq.com', '102553230005903', 'ea79b2315c174f1858118e66148d6f59a7b4026d', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2382, '邵诗雨', NULL, NULL, '450104199912060520', '13777891325', '283917015@qq.com', '102553230001790', '7ce3747b8e9b5b32607e9aa3ec5017238dc83fe5', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2383, '陈祥', NULL, NULL, '350623200102194517', '15059228607', '414823207@qq.com', '102553230006353', '7d60a5e07c47506211cf6e43b5c630994f37b436', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2384, '饶志刚', NULL, NULL, '362502199710135837', '18870421453', '445750285@qq.com', '102553230006630', '138a4cbb312ac94e0b211c75476a7df4ffe7616d', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2385, '庄乐', NULL, NULL, '320981200107255977', '13390747509', '997831097@qq.com', '102553230003506', '20b8218b338ec8394fe2858220657fc2838fc50a', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2386, '尹文秀', NULL, NULL, '340826200103164027', '18297718297', '2811408134@qq.com', '102553230005607', 'd187f3de07c675a9963f903adcb72b82e4d174d0', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2387, '吴茵茵', NULL, NULL, '330726200011122324', '19121736512', '1344337528@qq.com', '102553230001009', '59b372cfcac49f0129539c213ecc266e9feda5fd', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2388, '安唯', NULL, NULL, '342422200108010507', '19855956590', 'anwsry@163.com', '102553230005608', '83ebdeb14d1affc596c214bcf2c6674100483f3d', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2389, '郑洋', NULL, NULL, '33070320010119241X', '18857933490', '2032117314@qq.com', '102553230001045', 'dd7273cdff7f541793cf9908f99e5d3e02fb88d8', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2390, '李沐阳', NULL, NULL, '432502200008283032', '18573807102', 'y000828@qq.com', '102553230009503', '671471e3033976f1932d0a7eef9875b661eec496', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2391, '何小亮', NULL, NULL, '320684199611196416', '15162711892', '1021287060@qq.com', '102553230004316', 'a6f763762643246723a66af42e9a35d9e2ac229a', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2392, '曹承源', NULL, NULL, '410526200012150318', '17629560578', '1357190069@qq.com', '102553230007793', '32094e911dfe300edc5388f3b4cef64959eb68f7', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2393, '李一晨', NULL, NULL, '310117200101244816', '15026598062', '2304013419@qq.com', '102553230000951', 'b6f1e957996c2b6c6d6f7c9b19fc6fb9b0626cb6', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2394, '吴杨洋', NULL, NULL, '320681200110133026', '17312387786', '1663390836@qq.com', '102553230003401', 'c8c0b9b291489a0abf7583eb6814d98d3672a765', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2395, '杨亮瑞', NULL, NULL, '412828199912266928', '17761907513', '2801826541@qq.com', '102553230003724', 'c4e199f09d97c77697c0316451d6d32f8f3da1f0', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2396, '房超', NULL, NULL, '320882200104216412', '18114941511', '939915473@qq.com', '102553230004677', 'da51ced78f53b0b803c80175735ea92b2e7032f5', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2397, '李溢', NULL, NULL, '420222200212033714', '15335885760', '1269498754@qq.com', '102553230009084', 'a1101c2280ab5da347c0e6e15ef953fb4470e383', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2398, '张李祺', NULL, NULL, '321183199904280010', '19850067978', '1589913461@qq.com', '102553230003498', '7ce32296cbd697a3f3403f6ad5cd4c92227e3b2d', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2399, '赵衍', NULL, NULL, '320922199909270815', '13915417259', '2019684871@qq.com', '102553230004125', '6f50d74fbcbe713f2a12eb68158e8ab476bb4abc', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2400, '梁文慧', NULL, NULL, '340123199902052600', '18302139738', '2497053828@qq.com', '102553230005307', 'b4c5f72f058cbc5925106ba632e29969231a107c', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2401, '汤婕', NULL, NULL, '32032420010215566X', '13775928135', '2432146359@qq.com', '102553230003988', '60878816f729d712475ee50dce667581bc6fadf4', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2402, '王兆源', NULL, NULL, '410702199811242010', '15802152056', '1044783331@qq.com', '102553230007745', 'fc19e18d103deab0d5fd04125aff163574721902', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2403, '戈大志', NULL, NULL, '320324199711100351', '18944156762', '359703760@qq.com', '102553230003979', 'fc89723f54991dc1b64c8f020e2bada2b2601730', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2404, '米冉', NULL, NULL, '431202200203090220', '17873780927', '1785274074@qq.com', '102553230000971', '701adf7960738e9862b61c896c89e7097b695cd6', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2405, '陆晟轩', NULL, NULL, '320602200102200510', '13348098220', '1747181819@qq.com', '102553230003387', '49a8739e89aed14fb04127cf841fa1f0a3bab7ef', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2406, '张百昊', NULL, NULL, '371522200105088413', '15979901307', '1431767166@qq.com', '102553230006478', '705a759640a8cb83139f457e624aa3da70ed9f9b', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2407, '王玉涛', NULL, NULL, '321084200103290039', '13348128506', '905880024@qq.com', '102553230004187', 'cd7394dd15f13ec034d2a08c0d773b3c46f56bb6', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2408, '陈鑫', NULL, NULL, '360724200105142513', '18470778456', '3503737656@qq.com', '102553230006429', '7b79f03ce24ff6f60e1c24e4d3ee24a895b1bb43', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2409, '肖文涛', NULL, NULL, '430525200007040015', '18152856827', '921036503@qq.com', '102553230007046', 'f6a348df0ec6115ded7b9e5390f00cfa8ee3cc9b', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2410, '田文皓', NULL, NULL, '131126200011250917', '15533184806', '1050901182@qq.com', '102553230007048', '2237766df0fbe0542af95c7cb09e96543104a9b6', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2411, '霍登杰', NULL, NULL, '320826200106082250', '18762710789', '1805659620@qq.com', '102553230004038', 'b72406d90a7f2e1adc94758d69801e8d137adfb7', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2412, '段云飞', NULL, NULL, '130423200009061814', '15232839728', '2972220302@qq.com', '102553230002390', 'e3bf90709b03da5245ac9c312f2e6df2904f54df', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2413, '张伟业', NULL, NULL, '330421200103194415', '18857319682', '1581028675@qq.com', '102553230004487', '5ae93fd705a3ef29ae7f1ecb0418fe1fa8677ae4', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2414, '曹思琦', NULL, NULL, '321088200103138526', '18952513408', '1461155961@qq.com', '102553230004208', '7d53bf0dadf4dd25cdc765f59e80e73bcb922fb1', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2415, '彭航', NULL, NULL, '362227200106223511', '15070377352', '1158688903@qq.com', '102553230006367', '91f139f5b294f3fc9189530a6821a67b0964d337', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2416, '谷兴权', NULL, NULL, '32082120011013391X', '19816001836', '1844725172@qq.com', '102553230003533', '58267dc2a1435884bfc80137cfbb3b7526cda7b5', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2417, '刘少菲', NULL, NULL, '410423200012129012', '18516754665', '1346483328@qq.com', '102553230001247', 'fe0a8b937aff420cd534854363160228eb54f02c', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2418, '刘萍', NULL, NULL, '370112200101262925', '18363083817', 'liu_lping2001@163.com', '102553230000177', '64a10482d7c73b91a3198de3f5c4d5d29444dc24', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2419, '童冀', NULL, NULL, '421121200010262016', '15897916919', '1306105051@qq.com', '102553230009132', 'e6183bc2fdcdb2ac43099d533b06bb3a1dd13e6d', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2420, '吴轶硕', NULL, NULL, '430224200103010016', '13203300781', '1700172455@qq.com', '102553230009405', '8aad5646eb51d48889060355a10d7c283147dffc', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2421, '蒋绍卿', NULL, NULL, '411481200001292716', '15993971189', '2248757993@qq.com', '102553230000174', '07eddee82af458b8d337c9877f859846bf8c7330', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2422, '孙浩', NULL, NULL, '321084200006296519', '18994090870', '484363695@qq.com', '102553230003532', '758439f7d638cb79664ea6ce0a662ed0399ec481', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2423, '李明亮', NULL, NULL, '341221200108204119', '15256805023', '908137837@qq.com', '102553230009905', '4af7c8a3fe4344f1a0b4d10027c6de85fb48bbcf', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2424, '刘源', NULL, NULL, '620102200103203629', '13919022875', '2478093305@qq.com', '102553230000178', '28e0f282ca1bde15f7756639b984c9cd0badaad9', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2425, '张李欣', NULL, NULL, '320582200011090324', '18662239788', 'zlxink@outlook.com', '102553230003838', 'ca42c63d3734e4b61900e8785dc6f468c5441f41', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2426, '沈璐婷', NULL, NULL, '330501200107226840', '15757216335', '1838460643@163.com', '102553230000181', '91a29d8763093b370f93fbf604f90fa39c5ab49f', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2427, '黄达', NULL, NULL, '130302199910143510', '13933569895', '1637452377@qq.com', '102553230002304', 'f94d2e3313a31a46da2898a9c273f2efad89d658', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2428, '测试人员', NULL, NULL, '431002200008121000', '18311112222', 'test@qq.com', 'huang', '7c4a8d09ca3762af61e59520943dc26494f8941b', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2429, '祖朝翔', NULL, NULL, '522501199902061215', '123', 'a@163.com', '102553230001785', '37c4019c426dca3166ed9aca8985ea2bec07f59e', NULL, 0, 0, '7');
INSERT INTO `student` (`ID`, `name`, `institutionID`, `studentnumber`, `IDNumber`, `telephone`, `email`, `username`, `password`, `year`, `deleteflag`, `tutorID`, `role`) VALUES (2430, '李腾', NULL, NULL, '141181199906040030', '123', 'b@163.com', '102553230001748', 'e541f5ef3e2bfcc9392211b6889d2bca774dc8f6', NULL, 0, 0, '7');
COMMIT;

-- ----------------------------
-- Table structure for sysmsg
-- ----------------------------
DROP TABLE IF EXISTS `sysmsg`;
CREATE TABLE `sysmsg` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mid` int DEFAULT NULL COMMENT '消息id',
  `type` int DEFAULT '0' COMMENT '0表示群发消息',
  `hrid` int DEFAULT NULL COMMENT '这条消息是给谁的',
  `state` int DEFAULT '0' COMMENT '0 未读 1 已读',
  PRIMARY KEY (`id`),
  KEY `hrid` (`hrid`),
  KEY `sysmsg_ibfk_1` (`mid`),
  CONSTRAINT `sysmsg_ibfk_1` FOREIGN KEY (`mid`) REFERENCES `msgcontent` (`id`),
  CONSTRAINT `sysmsg_ibfk_2` FOREIGN KEY (`hrid`) REFERENCES `hr` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of sysmsg
-- ----------------------------
BEGIN;
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (57, 14, 0, 3, 1);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (58, 14, 0, 5, 1);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (59, 14, 0, 10, 1);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (60, 14, 0, 11, 0);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (61, 14, 0, 12, 0);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (62, 15, 0, 3, 1);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (63, 15, 0, 5, 1);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (64, 15, 0, 10, 1);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (65, 15, 0, 11, 0);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (66, 15, 0, 12, 0);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (67, 16, 0, 3, 1);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (68, 16, 0, 5, 1);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (69, 16, 0, 10, 1);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (70, 16, 0, 11, 0);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (71, 16, 0, 12, 0);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (72, 17, 0, 3, 1);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (73, 17, 0, 5, 1);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (74, 17, 0, 10, 1);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (75, 17, 0, 11, 0);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (76, 17, 0, 12, 0);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (77, 18, 0, 3, 1);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (78, 18, 0, 5, 0);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (79, 18, 0, 10, 0);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (80, 18, 0, 11, 0);
INSERT INTO `sysmsg` (`id`, `mid`, `type`, `hrid`, `state`) VALUES (81, 18, 0, 12, 0);
COMMIT;

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `institutionID` int DEFAULT NULL COMMENT '外键',
  `name` varchar(40) NOT NULL,
  `jobnumber` varchar(20) DEFAULT NULL,
  `sex` varchar(4) DEFAULT NULL,
  `department` varchar(20) DEFAULT NULL,
  `IDNumber` varchar(30) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `deleteflag` int NOT NULL,
  `role` varchar(20) NOT NULL COMMENT '角色',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `experts_ibfk_1` (`institutionID`) USING BTREE,
  CONSTRAINT `teacher_ibfk_1` FOREIGN KEY (`institutionID`) REFERENCES `institution` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1062 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of teacher
-- ----------------------------
BEGIN;
INSERT INTO `teacher` (`ID`, `institutionID`, `name`, `jobnumber`, `sex`, `department`, `IDNumber`, `phone`, `email`, `username`, `password`, `deleteflag`, `role`) VALUES (1054, 1, '刘国华', '10219067', '男', '软件系', '230206196606091656', '15216822311', 'ghliu@dhu.edu.cn', '15216822311', 'fc28659340b3b66e56c53490b6a2c3e1f189dcca', 0, '3');
INSERT INTO `teacher` (`ID`, `institutionID`, `name`, `jobnumber`, `sex`, `department`, `IDNumber`, `phone`, `email`, `username`, `password`, `deleteflag`, `role`) VALUES (1055, 1, '李锋', '10104403', '男', '软件系', '610113196911192217', '13818989114', 'lifeng@dhu.edu.cn', '13818989114', '587f0a10b754bcaacc2006ffc8c0dc5d5036de67', 0, '3');
INSERT INTO `teacher` (`ID`, `institutionID`, `name`, `jobnumber`, `sex`, `department`, `IDNumber`, `phone`, `email`, `username`, `password`, `deleteflag`, `role`) VALUES (1056, 1, '石秀金', '10216301', '男', '软件系', '420619197506295591', '18964657899', 'sxj@dhu.edu.cn', '18964657899', '53e0a2170cfac2c4c7e7d50c7b4610784e44b37d', 0, '3');
INSERT INTO `teacher` (`ID`, `institutionID`, `name`, `jobnumber`, `sex`, `department`, `IDNumber`, `phone`, `email`, `username`, `password`, `deleteflag`, `role`) VALUES (1057, 1, '李悦', '10104967', '男', '软件系', '310227198112240050', '15821203702', 'frankyueli@dhu.edu.cn', '15821203702', 'ed33402986e21b27faf57e7c70dbcda6fb7aeb6b', 0, '3');
INSERT INTO `teacher` (`ID`, `institutionID`, `name`, `jobnumber`, `sex`, `department`, `IDNumber`, `phone`, `email`, `username`, `password`, `deleteflag`, `role`) VALUES (1058, 1, '李玮', '10104867', '女', '软件系', '340404198008242429', '13795438126', 'weili@dhu.edu.cn', '13795438126', '38f5b330c50bc2805e0bbd4eabd4541a12108b17', 0, '3');
INSERT INTO `teacher` (`ID`, `institutionID`, `name`, `jobnumber`, `sex`, `department`, `IDNumber`, `phone`, `email`, `username`, `password`, `deleteflag`, `role`) VALUES (1059, 1, '丁祥武', '10104141', '男', '软件系', '422601196305130057', '13391196486', 'dingxw@dhu.edu.cn', '13391196486', '74b9cf81f745f201cdc273fd9f85b44c3c11be2f', 0, '3');
INSERT INTO `teacher` (`ID`, `institutionID`, `name`, `jobnumber`, `sex`, `department`, `IDNumber`, `phone`, `email`, `username`, `password`, `deleteflag`, `role`) VALUES (1060, 1, '姚砺', '10104220', '男', '软件系', '340104196711021577', '15800877821', 'yaoli@dhu.edu.cn', '15800877821', '1b42ed6e31d036286467270f80ba586b7dcb17e2', 0, '3');
INSERT INTO `teacher` (`ID`, `institutionID`, `name`, `jobnumber`, `sex`, `department`, `IDNumber`, `phone`, `email`, `username`, `password`, `deleteflag`, `role`) VALUES (1061, 1, '黄秋波', '10104299', '男', '软件系', '43052219750724191X', '13816629138', 'huangturbo@dhu.edu.cn', '1', 'e933bdb30ab016f981663364d08dac83f20b3bd1', 0, '3');
COMMIT;

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test` (
  `id` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `no` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `no` (`no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of test
-- ----------------------------
BEGIN;
INSERT INTO `test` (`id`, `name`, `no`) VALUES (1, 'grz', 1);
INSERT INTO `test` (`id`, `name`, `no`) VALUES (2, 'grz', 2);
INSERT INTO `test` (`id`, `name`, `no`) VALUES (3, 'grz', 3);
COMMIT;

-- ----------------------------
-- Table structure for thesis
-- ----------------------------
DROP TABLE IF EXISTS `thesis`;
CREATE TABLE `thesis` (
  `ID` int NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of thesis
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for totalitem
-- ----------------------------
DROP TABLE IF EXISTS `totalitem`;
CREATE TABLE `totalitem` (
  `ID` int NOT NULL,
  `activityID` int DEFAULT NULL,
  `addParticipantScore` int DEFAULT NULL,
  `infoItemIDs` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `totalItemIDs` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `displaySequence` int DEFAULT NULL,
  `fullScore` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `totalitem_ibfk_1` (`activityID`),
  CONSTRAINT `totalitem_ibfk_1` FOREIGN KEY (`activityID`) REFERENCES `activities` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- ----------------------------
-- Records of totalitem
-- ----------------------------
BEGIN;
INSERT INTO `totalitem` (`ID`, `activityID`, `addParticipantScore`, `infoItemIDs`, `totalItemIDs`, `name`, `displaySequence`, `fullScore`) VALUES (1, 34, 1, '83,', NULL, '复试总分', 1, 220);
INSERT INTO `totalitem` (`ID`, `activityID`, `addParticipantScore`, `infoItemIDs`, `totalItemIDs`, `name`, `displaySequence`, `fullScore`) VALUES (2, 34, 0, '77,', '1,', '考试总分', 2, 720);
COMMIT;

-- ----------------------------
-- Table structure for underthesisdesign
-- ----------------------------
DROP TABLE IF EXISTS `underthesisdesign`;
CREATE TABLE `underthesisdesign` (
  `ID` int NOT NULL AUTO_INCREMENT COMMENT '毕业设计或论文的ID，主键',
  `studentID` int NOT NULL COMMENT '学生学号',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL COMMENT '毕业设计或论文的主题',
  `year` varchar(4) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL COMMENT '上传年份',
  `url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `studentID` (`studentID`) USING BTREE,
  CONSTRAINT `studentID` FOREIGN KEY (`studentID`) REFERENCES `student` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of underthesisdesign
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Procedure structure for addDep
-- ----------------------------
DROP PROCEDURE IF EXISTS `addDep`;
delimiter ;;
CREATE PROCEDURE `addDep`(in depName varchar(32),in parentId int,in enabled boolean,out result int,out result2 int)
begin
  declare did int;
  declare pDepPath varchar(64);
  insert into department set name=depName,parentId=parentId,enabled=enabled;
  select row_count() into result;
  select last_insert_id() into did;
  set result2=did;
  select depPath into pDepPath from department where id=parentId;
  update department set depPath=concat(pDepPath,'.',did) where id=did;
  update department set isParent=true where id=parentId;
end
;;
delimiter ;

-- ----------------------------
-- Procedure structure for deleteDep
-- ----------------------------
DROP PROCEDURE IF EXISTS `deleteDep`;
delimiter ;;
CREATE PROCEDURE `deleteDep`(in did int,out result int)
begin
  declare ecount int;
  declare pid int;
  declare pcount int;
  declare a int;
  select count(*) into a from department where id=did and isParent=false;
  if a=0 then set result=-2;
  else
  select count(*) into ecount from employee where departmentId=did;
  if ecount>0 then set result=-1;
  else
  select parentId into pid from department where id=did;
  delete from department where id=did and isParent=false;
  select row_count() into result;
  select count(*) into pcount from department where parentId=pid;
  if pcount=0 then update department set isParent=false where id=pid;
  end if;
  end if;
  end if;
end
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
