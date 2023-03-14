/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 80028
Source Host           : localhost:3306
Source Database       : rate_test

Target Server Type    : MYSQL
Target Server Version : 80028
File Encoding         : 65001

Date: 2023-03-14 16:43:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for activities
-- ----------------------------
DROP TABLE IF EXISTS `activities`;
CREATE TABLE `activities` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `scoreItemCount` int DEFAULT NULL,
  `score` double(10,2) unsigned DEFAULT NULL,
  `groupCount` int unsigned DEFAULT NULL,
  `expertCount` int unsigned DEFAULT NULL,
  `participantCount` int unsigned DEFAULT NULL,
  `comment` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `deleteFlag` tinyint unsigned NOT NULL,
  `institutionID` int DEFAULT NULL,
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'open',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `institutionID` (`institutionID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of activities
-- ----------------------------
INSERT INTO `activities` VALUES ('15', '2022年计算机学院硕士研究生综合面试', '2022-03-30 00:00:00', '8', '0.00', '2', '13', '17', '2022年计算机学院硕士研究生综合面试', '0', '21', 'open');
INSERT INTO `activities` VALUES ('16', '测试', '2022-03-15 00:00:00', '2', '0.00', '3', '0', '0', '活动备注example：关于xxx的活动', '0', '21', 'open');
INSERT INTO `activities` VALUES ('19', '测试总分', '2022-03-27 00:00:00', '1', '0.00', '0', '0', '0', '活动备注example：关于xxx的活动', '0', '21', 'open');
INSERT INTO `activities` VALUES ('20', '测试', '2022-04-08 00:00:00', '0', '0.00', '0', '0', '0', '活动备注example：关于xxx的活动', '0', '21', 'open');
INSERT INTO `activities` VALUES ('21', '测试', '2022-04-08 00:00:00', '0', '0.00', '0', '0', '0', '活动备注example：关于xxx的活动', '0', '21', 'close');
INSERT INTO `activities` VALUES ('22', 'o', '2022-04-08 00:00:00', '3', '0.00', '0', '0', '0', '活动备注example：关于xxx的活动', '0', '21', 'close');
INSERT INTO `activities` VALUES ('24', 'fff', '2022-04-16 00:00:00', '0', '0.00', '0', '0', '0', '活动备注example：关于xxx的活动', '0', '21', 'open');
INSERT INTO `activities` VALUES ('25', 'admin测试', '2022-04-15 00:00:00', '0', '0.00', '0', '0', '0', '活动备注example：关于xxx的活动', '0', '21', 'open');
INSERT INTO `activities` VALUES ('26', '2022年D大学计算机学院硕士研究生综合面试', '2022-03-30 00:00:00', '3', '100.00', '3', '12', '9', '2022年111', '0', '1', 'open');
INSERT INTO `activities` VALUES ('27', '2022校园歌手大赛', '2022-04-22 00:00:00', '0', '0.00', '0', '0', '0', '关于2022校园歌手大赛的活动', '0', '1', 'open');
INSERT INTO `activities` VALUES ('28', '1', '2022-05-23 00:00:00', '0', '0.00', '0', '0', '0', '活动备注example：关于xxx的活动', '0', '1', 'open');

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `institutionID` int NOT NULL COMMENT '外键',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '姓名',
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号码',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱地址',
  `comment` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `role` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色',
  `enabled` tinyint(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`ID`,`username`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE,
  KEY `institutionID` (`institutionID`) USING BTREE,
  KEY `ID` (`ID`) USING BTREE,
  CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`institutionID`) REFERENCES `institution` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', '1', 'wow', 'admin1', '$2a$10$/VauH0rmJH0BlAwZhTzkv.ELdAQP8sVrghoqoNrQ0s8mEwUjjiSOa', '135266135', '123456@126.COM', null, '6', '1');
INSERT INTO `admin` VALUES ('2', '1', '龚三', 'admin', '7c4a8d09ca3762af61e59520943dc26494f8941b', '18568887789', '555@dhu.edu.cn', '123', '1', '1');
INSERT INTO `admin` VALUES ('3', '2', 'idk', 'test123', '$2a$10$a1kxYXCCJAGCa3LQD64M4ujZ0vBhX.3a5I487XpMORNLh6v2len5O', '18568128889', '123@126.com', '3', '1', '1');
INSERT INTO `admin` VALUES ('4', '2', 'ah', 'xht', '$2a$10$/9xtdM5Gv6YZkO2PTA9CCOmZZ.C3Gb93OAmXfFT6jxUXf.btROtKu', '18568128889', '123@126.com', null, '1', '1');
INSERT INTO `admin` VALUES ('5', '2', '小海豚a', 'xht2a', '$2a$10$SKKEQJRYlggIeBjdASCqzOvx8vjmUDFEUPp/9k9.xbFr0rz5mVzcm', '13816913672', 'zhangyining098765432a@126.com', 'a', '1', '1');
INSERT INTO `admin` VALUES ('6', '2', 'javaboy12', 'admin3', '$2a$10$WeZi6XzS65Nu7yZTefqi1OAmFnV4DOKPGG8qLxdNxLGjnvI1hbRai', '18568128889', '123@126.com', null, '1', '0');
INSERT INTO `admin` VALUES ('11', '3', '小海豚1', 'user', '$2a$10$LKrKNJpmMBjydEMnbuPD9.YgqS.Gu6C.SMZ1Cy0x0mViOqoOgTHUy', '1800000', '1023@126.com', 'usertest', '1', '1');
INSERT INTO `admin` VALUES ('12', '3', '测试', 'tes', '$2a$10$jywEA9Y9szPWJ.h6CvHhSO/05NkDCRl7WHIklRyLRuzsgBcuWAt86', '18568128889', '123@126.com', 'role2', '2', '1');
INSERT INTO `admin` VALUES ('13', '13', 'msgmang', 'msgmang', '$2a$10$95r1A3U6vv9N3c6qXNOPHeZddyMhjehsYpJHIrm4Yq3ECaseBPSdy', '18568128889', '123@126.com', null, '1', '1');
INSERT INTO `admin` VALUES ('14', '21', '李老师', 'cstadmin', '$2a$10$lUl5tWLBEEjIKYjB9Vl6uOKtLwfifKkSaB.nUFEa6AWesj4DLvtmS', '12311111111', '123@dhu.edu.cn', null, '1', '1');
INSERT INTO `admin` VALUES ('16', '23', '1你好', 'hola', '$2a$10$HS1AITfVel9NyTqRmmOx7.eePqNwJg9Aj2Q/dueL.uPsBzL54QFwW', '20568128889', '123@126.com', null, '1', '1');
INSERT INTO `admin` VALUES ('21', '23', '1', 'holaaaa', '$2a$10$3IkLmZ9eZX5ZY3b9pzk6le8jHtM5KqzGozikcwqT/DH/keMAN6gWm', '1234567', '123@126.com', null, '1', '1');
INSERT INTO `admin` VALUES ('24', '2', 'hol', 'hola1', '$2a$10$Ce/.JCeLpovDP59HnUz.h.L5YlZ5qdp6T0w49lLFKnz3Yash4jAkC', '22568128889', 'h@126.com', null, '1', '1');
INSERT INTO `admin` VALUES ('27', '23', 'hola', 'holadog', '$2a$10$0bCDIQPT5KOTiDhs6zgJpuEUvyA2tVJL/j3u1p64iWjmJFdI0f1ze', '66568128889', '123@126.com', 'holadog', '1', '1');
INSERT INTO `admin` VALUES ('28', '23', '12333333', 'test111123', '$2a$10$LPYdkl2mx/4mexG7ZXoX9uUV4.fKmae94eIoOP.CRhCgvJcVwUJGe', '33368128889', '123@126.com', null, '1', '1');
INSERT INTO `admin` VALUES ('29', '23', '333', '3st123', '$2a$10$psg0Iebk.wHQyaIblkBgAOekN4gmYYo0HVtRKz8qB3HtsPWxZhlM2', '38568128889', '323@126.com', null, '1', '1');
INSERT INTO `admin` VALUES ('30', '1', '1boy', '3test123', '$2a$10$7F89FTPPWRiPsBcza2J.zeld4atxaQGpsaroMpjDbo/Jbkt4OCMCK', '28568128889', '323@126.com', null, '1', '1');
INSERT INTO `admin` VALUES ('31', '1', '3avaboy', '2est123', '$2a$10$XRfpRBrTg2RYJN4OwpiNKOT1M4rZT5q2t3CP99vFJR3WBjaIj6pxS', '38568128889', '223@126.com', '123', '1', '1');
INSERT INTO `admin` VALUES ('32', '22', '的对的', '5est123', '$2a$10$7eoqrEBcqJya72nGQnyvJOqA8yhg1fbHsNs/LOvjBFYa8cdKJzlRa', '55568128889', '523@126.com', null, '1', '1');
INSERT INTO `admin` VALUES ('33', '24', '测试删除', 'estinsertdelete', '$2a$10$mSJSy2GZSdbERsIF3.54POjG5D4B4WaC.y8FC1wyHaih9hcT8aQqS', '18888128889', 'test3@126.com', null, '1', '0');
INSERT INTO `admin` VALUES ('34', '1', '龚三old', 'adminold', '$2a$10$CQ0EgGSxJhnQ/NLSNUsuWeIPLaLeyFdLh6NgLCVJpEjPRdA64CRXq', '18568887789', '555@dhu.edu.cn', '123', '1', '1');

-- ----------------------------
-- Table structure for award
-- ----------------------------
DROP TABLE IF EXISTS `award`;
CREATE TABLE `award` (
  `ID` int NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `indicatorID` int DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `award` (`indicatorID`) USING BTREE,
  CONSTRAINT `award_ibfk_1` FOREIGN KEY (`indicatorID`) REFERENCES `indicator` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of award
-- ----------------------------
INSERT INTO `award` VALUES ('1', '国家技术发明奖1', '13');
INSERT INTO `award` VALUES ('2', '国家自然科学奖', '13');
INSERT INTO `award` VALUES ('3', '中国纺织工业联合会科学技术奖', '14');
INSERT INTO `award` VALUES ('4', '222', '13');

-- ----------------------------
-- Table structure for awardresult
-- ----------------------------
DROP TABLE IF EXISTS `awardresult`;
CREATE TABLE `awardresult` (
  `ID` int NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `awardname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `year` int DEFAULT NULL,
  `member` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `unit` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `total` int DEFAULT NULL,
  `rank` int DEFAULT NULL,
  `awardID` int NOT NULL,
  `state` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `awardID` (`awardID`) USING BTREE,
  CONSTRAINT `awardresult_ibfk_1` FOREIGN KEY (`awardID`) REFERENCES `award` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of awardresult
-- ----------------------------
INSERT INTO `awardresult` VALUES ('2131243', '张三丰', '国家奖学金', null, null, null, null, null, '100001', '');

-- ----------------------------
-- Table structure for bookresult
-- ----------------------------
DROP TABLE IF EXISTS `bookresult`;
CREATE TABLE `bookresult` (
  `ID` int NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `unit` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `member` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `time` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `place` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `indicaterID` int NOT NULL,
  `state` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `book` (`indicaterID`) USING BTREE,
  CONSTRAINT `bookresult_ibfk_1` FOREIGN KEY (`indicaterID`) REFERENCES `indicater` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of bookresult
-- ----------------------------
INSERT INTO `bookresult` VALUES ('12315654', '微积分', null, null, null, null, null, '3013', '');

-- ----------------------------
-- Table structure for decision
-- ----------------------------
DROP TABLE IF EXISTS `decision`;
CREATE TABLE `decision` (
  `ID` int NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `indicatorID` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `decision` (`indicatorID`) USING BTREE,
  CONSTRAINT `decision_ibfk_1` FOREIGN KEY (`indicatorID`) REFERENCES `indicator` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of decision
-- ----------------------------
INSERT INTO `decision` VALUES ('1', '中共中央政治局常委肯定性批示', '16');
INSERT INTO `decision` VALUES ('2', '中共中央、国务院、全国人大、全国政协采纳', '17');
INSERT INTO `decision` VALUES ('3', '入编全国哲学社会科学规划办公室《成果要报》', '17');

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '部门名称',
  `parentId` int DEFAULT NULL,
  `depPath` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  `isParent` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES ('1', '股东会', '-1', '.1', '1', '1');
INSERT INTO `department` VALUES ('4', '董事会', '1', '.1.4', '1', '1');
INSERT INTO `department` VALUES ('5', '总办', '4', '.1.4.5', '1', '1');
INSERT INTO `department` VALUES ('8', '财务部', '5', '.1.4.5.8', '1', '0');
INSERT INTO `department` VALUES ('78', '市场部', '5', '.1.4.5.78', '1', '1');
INSERT INTO `department` VALUES ('81', '华北市场部', '78', '.1.4.5.78.81', '1', '1');
INSERT INTO `department` VALUES ('82', '华南市场部', '78', '.1.4.5.78.82', '1', '0');
INSERT INTO `department` VALUES ('85', '石家庄市场部', '81', '.1.4.5.78.81.85', '1', '0');
INSERT INTO `department` VALUES ('86', '西北市场部', '78', '.1.4.5.78.86', '1', '1');
INSERT INTO `department` VALUES ('87', '西安市场', '86', '.1.4.5.78.86.87', '1', '1');
INSERT INTO `department` VALUES ('89', '莲湖区市场', '87', '.1.4.5.78.86.87.89', '1', '0');
INSERT INTO `department` VALUES ('91', '技术部', '5', '.1.4.5.91', '1', '0');
INSERT INTO `department` VALUES ('92', '运维部', '5', '.1.4.5.92', '1', '1');
INSERT INTO `department` VALUES ('93', '运维1部', '92', '.1.4.5.92.93', '1', '0');
INSERT INTO `department` VALUES ('94', '运维2部', '92', '.1.4.5.92.94', '1', '0');
INSERT INTO `department` VALUES ('96', 'bbb', '1', '.1.96', '1', '1');
INSERT INTO `department` VALUES ('104', '111', '96', '.1.96.104', '1', '0');

-- ----------------------------
-- Table structure for expertactivities
-- ----------------------------
DROP TABLE IF EXISTS `expertactivities`;
CREATE TABLE `expertactivities` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `teacherID` int DEFAULT NULL,
  `activityID` int DEFAULT NULL,
  `groupID` int DEFAULT NULL,
  `finished` tinyint DEFAULT '0',
  `is_secretary` int DEFAULT '0',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `expertID` (`teacherID`) USING BTREE,
  KEY `activityID_2` (`activityID`) USING BTREE,
  KEY `scoreItemID` (`groupID`) USING BTREE,
  CONSTRAINT `expertactivities_ibfk_1` FOREIGN KEY (`activityID`) REFERENCES `activities` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `expertactivities_ibfk_2` FOREIGN KEY (`groupID`) REFERENCES `groups` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `expertactivities_ibfk_3` FOREIGN KEY (`teacherID`) REFERENCES `teacher` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of expertactivities
-- ----------------------------
INSERT INTO `expertactivities` VALUES ('75', '1022', '15', '17', '0', '0');
INSERT INTO `expertactivities` VALUES ('76', '1023', '15', '17', '1', '0');
INSERT INTO `expertactivities` VALUES ('77', '1024', '15', '17', '0', '0');
INSERT INTO `expertactivities` VALUES ('78', '1025', '15', '17', '1', '0');
INSERT INTO `expertactivities` VALUES ('79', '1026', '15', '18', '0', '0');
INSERT INTO `expertactivities` VALUES ('80', '1027', '15', '18', '0', '0');
INSERT INTO `expertactivities` VALUES ('81', '1028', '15', '18', '1', '0');
INSERT INTO `expertactivities` VALUES ('82', '1029', '15', '18', '0', '0');
INSERT INTO `expertactivities` VALUES ('83', '1030', '15', '17', '0', '0');
INSERT INTO `expertactivities` VALUES ('84', '1031', '15', '17', '0', '0');
INSERT INTO `expertactivities` VALUES ('85', '1031', '15', '18', '0', '0');
INSERT INTO `expertactivities` VALUES ('87', null, '15', '17', '0', '0');
INSERT INTO `expertactivities` VALUES ('91', '1033', '15', '17', '0', '0');
INSERT INTO `expertactivities` VALUES ('92', '1034', '26', '24', '1', '0');
INSERT INTO `expertactivities` VALUES ('93', '1035', '26', '24', '1', '0');
INSERT INTO `expertactivities` VALUES ('94', '1036', '26', '24', '1', '0');
INSERT INTO `expertactivities` VALUES ('95', '1037', '26', '25', '1', '0');
INSERT INTO `expertactivities` VALUES ('96', '1038', '26', '25', '1', '0');
INSERT INTO `expertactivities` VALUES ('97', '1039', '26', '25', '0', '0');
INSERT INTO `expertactivities` VALUES ('98', '1040', '26', '26', '1', '0');
INSERT INTO `expertactivities` VALUES ('99', '1041', '26', '26', '1', '0');
INSERT INTO `expertactivities` VALUES ('100', '1042', '26', '26', '1', '0');
INSERT INTO `expertactivities` VALUES ('101', null, '26', '24', '0', '0');
INSERT INTO `expertactivities` VALUES ('102', null, '26', '24', '0', '0');
INSERT INTO `expertactivities` VALUES ('103', null, '26', '24', '0', '0');
INSERT INTO `expertactivities` VALUES ('104', '1044', '26', '26', '0', '0');
INSERT INTO `expertactivities` VALUES ('105', '1043', '26', '26', '1', '1');

-- ----------------------------
-- Table structure for flyway_schema_history
-- ----------------------------
DROP TABLE IF EXISTS `flyway_schema_history`;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `script` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`) USING BTREE,
  KEY `flyway_schema_history_s_idx` (`success`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of flyway_schema_history
-- ----------------------------
INSERT INTO `flyway_schema_history` VALUES ('1', '1', 'vhr', 'SQL', 'V1__vhr.sql', '-1039138481', 'root', '2021-12-21 23:20:11', '3040', '1');
INSERT INTO `flyway_schema_history` VALUES ('2', '2', 'rate', 'SQL', 'V2__rate.sql', '1941999512', 'root', '2022-01-31 14:48:58', '7871', '1');
INSERT INTO `flyway_schema_history` VALUES ('3', '2', 'rate', 'SQL', 'V2__rate.sql', '-1691689598', 'root', '2022-02-09 19:52:57', '3002', '1');

-- ----------------------------
-- Table structure for groups
-- ----------------------------
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `activityID` int DEFAULT NULL,
  `name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `expertCount` int DEFAULT NULL,
  `participantCount` int DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `activityIDgroup` (`activityID`) USING BTREE,
  CONSTRAINT `groups_ibfk_1` FOREIGN KEY (`activityID`) REFERENCES `activities` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of groups
-- ----------------------------
INSERT INTO `groups` VALUES ('17', '15', '第一组', '8', '10');
INSERT INTO `groups` VALUES ('18', '15', '第二组', '5', '7');
INSERT INTO `groups` VALUES ('20', '16', '123', '0', '0');
INSERT INTO `groups` VALUES ('22', '16', '666', '0', '0');
INSERT INTO `groups` VALUES ('23', '16', 'cs', '0', '0');
INSERT INTO `groups` VALUES ('24', '26', '第1组', '6', '3');
INSERT INTO `groups` VALUES ('25', '26', '第2组', '3', '3');
INSERT INTO `groups` VALUES ('26', '26', '第3组', '3', '3');
INSERT INTO `groups` VALUES ('27', '26', '第X组', '0', '0');

-- ----------------------------
-- Table structure for hr_role
-- ----------------------------
DROP TABLE IF EXISTS `hr_role`;
CREATE TABLE `hr_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `hrid` int DEFAULT NULL,
  `rid` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `rid` (`rid`) USING BTREE,
  KEY `hr_role_ibfk_1` (`hrid`) USING BTREE,
  CONSTRAINT `hr_role_ibfk_1` FOREIGN KEY (`hrid`) REFERENCES `hr` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `hr_role_ibfk_2` FOREIGN KEY (`rid`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of hr_role
-- ----------------------------
INSERT INTO `hr_role` VALUES ('1', '3', '6');
INSERT INTO `hr_role` VALUES ('35', '12', '4');
INSERT INTO `hr_role` VALUES ('36', '12', '3');
INSERT INTO `hr_role` VALUES ('37', '12', '2');
INSERT INTO `hr_role` VALUES ('43', '11', '3');
INSERT INTO `hr_role` VALUES ('44', '11', '2');
INSERT INTO `hr_role` VALUES ('45', '11', '4');
INSERT INTO `hr_role` VALUES ('46', '11', '5');
INSERT INTO `hr_role` VALUES ('48', '10', '3');
INSERT INTO `hr_role` VALUES ('49', '10', '4');
INSERT INTO `hr_role` VALUES ('72', '5', '1');
INSERT INTO `hr_role` VALUES ('73', '5', '2');
INSERT INTO `hr_role` VALUES ('74', '5', '3');
INSERT INTO `hr_role` VALUES ('75', '13', '6');
INSERT INTO `hr_role` VALUES ('76', '15', '3');
INSERT INTO `hr_role` VALUES ('77', '14', '1');

-- ----------------------------
-- Table structure for indicater
-- ----------------------------
DROP TABLE IF EXISTS `indicater`;
CREATE TABLE `indicater` (
  `ID` int NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `order` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `score` int DEFAULT NULL,
  `father` int DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `father` (`father`) USING BTREE,
  CONSTRAINT `indicater_ibfk_1` FOREIGN KEY (`father`) REFERENCES `indicater` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of indicater
-- ----------------------------
INSERT INTO `indicater` VALUES ('3001', '1', '', '1', '0', null);
INSERT INTO `indicater` VALUES ('3002', '1.1', null, '1.1', '0', '3001');
INSERT INTO `indicater` VALUES ('3003', '1.2', null, '1.2', '0', '3001');
INSERT INTO `indicater` VALUES ('3004', '《东华大学科技论文认定办法》中所规定的A类学术论文', '论文', '1.1.1', '12', '3002');
INSERT INTO `indicater` VALUES ('3005', '《东华大学\r\n哲学社会科学成果认定办法》中所规定的T类、A类学术论文', '论文', '1.1.2', '12', '3002');
INSERT INTO `indicater` VALUES ('3006', ' 中国计算机学会推荐的CCF A类国际会议或国际期刊论文', '论文', '1.2.1', '12', '3003');
INSERT INTO `indicater` VALUES ('3007', '1.6', '', '1.6', null, '3001');
INSERT INTO `indicater` VALUES ('3008', '《东华大学科技纵向项目与科技奖认定暂行办法》所规定的国家级科\r\n技奖（排名不限）', '获奖', '1.6.1', null, '3007');
INSERT INTO `indicater` VALUES ('3009', '省部级科技奖一等奖（排名前3）', '获奖', '1.6.2', null, null);
INSERT INTO `indicater` VALUES ('3010', '1.3', null, '1.3', null, '3001');
INSERT INTO `indicater` VALUES ('3011', '《东华大学科技纵向项目与科技奖认定暂行办法》中所规定的A类纵向\r\n科研项目', '项目', '1.3.1', null, '3010');
INSERT INTO `indicater` VALUES ('3012', '1.5', null, '1.5', null, '3001');
INSERT INTO `indicater` VALUES ('3013', '获评全国优秀教材奖的教材', '著作或教材成果', '1.5.1', null, '3012');
INSERT INTO `indicater` VALUES ('3014', '1.8', '专利', '1.8', null, '3001');
INSERT INTO `indicater` VALUES ('3015', '制定所在领域已执行或试行的国际标准', '专利', '1.8.1', null, '3014');

-- ----------------------------
-- Table structure for indicator
-- ----------------------------
DROP TABLE IF EXISTS `indicator`;
CREATE TABLE `indicator` (
  `ID` int NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `order` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `score` int DEFAULT NULL,
  `father` int DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `father` (`father`) USING BTREE,
  CONSTRAINT `indicator_ibfk_1` FOREIGN KEY (`father`) REFERENCES `indicator` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of indicator
-- ----------------------------
INSERT INTO `indicator` VALUES ('1', '1类', null, '1', '12', null);
INSERT INTO `indicator` VALUES ('2', '论文', '论文', '1.1', '12', '1');
INSERT INTO `indicator` VALUES ('3', '纵向科研项目', '纵向科研项目', '1.2', '12', '1');
INSERT INTO `indicator` VALUES ('4', '《科技论文认定方法》A类', '论文', '1.1.1', '12', '2');
INSERT INTO `indicator` VALUES ('5', '《哲学社会科学成果认定办法》T类', '论文', '1.1.2', '12', '2');
INSERT INTO `indicator` VALUES ('6', '《哲学社会科学成果认定办法》A类', '论文', '1.1.3', '12', '2');
INSERT INTO `indicator` VALUES ('7', '《科技纵向项目与科技奖认定暂行办法》A类', '纵向科研项目', '1.2.2', '12', '3');
INSERT INTO `indicator` VALUES ('8', '《哲学社会科学成果认定办法》T类', '纵向科研项目', '1.2.3', '12', '3');
INSERT INTO `indicator` VALUES ('9', '《哲学社会科学成果认定办法》A类', '纵向科研项目', '1.2.1', '12', '3');
INSERT INTO `indicator` VALUES ('10', '2类', null, '2', '6', null);
INSERT INTO `indicator` VALUES ('11', '论文', '论文', '2.1', '6', '10');
INSERT INTO `indicator` VALUES ('12', '科技奖', '科技奖', '1.3', '12', '1');
INSERT INTO `indicator` VALUES ('13', '国家科技奖', '科技奖', '1.3.1', '12', '12');
INSERT INTO `indicator` VALUES ('14', '省部科技奖', '科技奖', '1.3.2', '12', '12');
INSERT INTO `indicator` VALUES ('15', '决策咨询成果', '决策咨询成果', '1.4', '12', '1');
INSERT INTO `indicator` VALUES ('16', 'T类决策咨询成果', '决策咨询成果', '1.4.1', '12', '15');
INSERT INTO `indicator` VALUES ('17', 'A类决策咨询成果', '决策咨询成果', '1.4.2', '12', '15');
INSERT INTO `indicator` VALUES ('18', '《东华大学科技论文认定办法》B类', '论文', '2.1.1', '6', '11');
INSERT INTO `indicator` VALUES ('19', '333', null, '3', '8', null);
INSERT INTO `indicator` VALUES ('20', '3-1', '论文', '3.1', '8', '19');
INSERT INTO `indicator` VALUES ('21', '3-1-1', '论文', '3.1.1', '8', '20');
INSERT INTO `indicator` VALUES ('22', '3-1-2', '论文', '3.1.2', '8', '20');

-- ----------------------------
-- Table structure for infoitem
-- ----------------------------
DROP TABLE IF EXISTS `infoitem`;
CREATE TABLE `infoitem` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `activityID` int DEFAULT NULL,
  `name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `contentType` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '值为textbox, textarea, image，分别表示单行文本、多行文本、图片\r\n\r\n',
  `sizelimit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `byParticipant` tinyint NOT NULL,
  `display` tinyint NOT NULL COMMENT '是否显示在评分表中',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `activityID` (`activityID`) USING BTREE,
  CONSTRAINT `infoitem_ibfk_1` FOREIGN KEY (`activityID`) REFERENCES `activities` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of infoitem
-- ----------------------------
INSERT INTO `infoitem` VALUES ('23', '15', '报考专业', 'textarea', null, '0', '1');
INSERT INTO `infoitem` VALUES ('24', '15', '毕业单位', null, null, '0', '1');
INSERT INTO `infoitem` VALUES ('25', '15', '毕业专业', null, null, '0', '1');
INSERT INTO `infoitem` VALUES ('26', '15', '考生来源', null, null, '0', '0');
INSERT INTO `infoitem` VALUES ('27', '15', '初试分数', null, null, '0', '0');
INSERT INTO `infoitem` VALUES ('28', '15', '上机练习', null, null, '0', '0');
INSERT INTO `infoitem` VALUES ('29', '15', '竞赛分', null, null, '0', '0');
INSERT INTO `infoitem` VALUES ('30', '15', '四六级', null, null, '0', '0');
INSERT INTO `infoitem` VALUES ('31', '15', '个人介绍', 'textarea', '500', '1', '0');
INSERT INTO `infoitem` VALUES ('32', '15', '教育经历', 'textarea', '500', '1', '0');
INSERT INTO `infoitem` VALUES ('33', '15', '获奖信息', 'textarea', '500', '1', '0');
INSERT INTO `infoitem` VALUES ('34', '15', '项目经验', 'textarea', '1000', '1', '0');
INSERT INTO `infoitem` VALUES ('35', '15', '读研规划', 'textarea', '500', '1', '0');
INSERT INTO `infoitem` VALUES ('36', '15', '本科成绩单', 'image', '3M', '1', '0');
INSERT INTO `infoitem` VALUES ('37', '15', '证书1', 'image', '500K', '1', '0');
INSERT INTO `infoitem` VALUES ('38', '15', '证书2', 'image', '500K', '1', '0');
INSERT INTO `infoitem` VALUES ('39', '15', '证书3', 'image', '500K', '1', '0');
INSERT INTO `infoitem` VALUES ('40', '15', '证书4', 'image', '500K', '1', '0');
INSERT INTO `infoitem` VALUES ('41', '15', '证书5', 'image', '500K', '1', '0');
INSERT INTO `infoitem` VALUES ('42', '15', '测试1', '', '', '1', '1');
INSERT INTO `infoitem` VALUES ('43', null, '测试', 'textbox', null, '1', '1');
INSERT INTO `infoitem` VALUES ('46', '16', '23', 'textarea,', '500', '0', '1');
INSERT INTO `infoitem` VALUES ('47', '16', '更新', 'textarea,pdf,', '500M', '1', '1');
INSERT INTO `infoitem` VALUES ('48', '16', '51', 'textbox', null, '1', '1');
INSERT INTO `infoitem` VALUES ('49', '16', '56', 'textbox', null, '1', '1');
INSERT INTO `infoitem` VALUES ('50', '16', '看log', 'image,textbox,', null, '0', '0');
INSERT INTO `infoitem` VALUES ('52', '16', 'new', 'zip,jpg,', '500', '0', '0');
INSERT INTO `infoitem` VALUES ('53', '26', '报考专业', 'textbox,', '500', '0', '1');
INSERT INTO `infoitem` VALUES ('54', '26', '上机练习', 'textarea', '500', '1', '1');
INSERT INTO `infoitem` VALUES ('55', '26', '本科成绩单', 'pdf,jpg,zip,', '10M', '1', '0');
INSERT INTO `infoitem` VALUES ('56', '26', '文本输入框', 'textbox', '10', '1', '1');
INSERT INTO `infoitem` VALUES ('57', '26', 'pdf文件上传', 'pdf', '10M', '1', '1');

-- ----------------------------
-- Table structure for infos
-- ----------------------------
DROP TABLE IF EXISTS `infos`;
CREATE TABLE `infos` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `activityID` int DEFAULT NULL,
  `participantID` int DEFAULT NULL,
  `infoItemID` int DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `activityID_2` (`activityID`) USING BTREE,
  KEY `participantID` (`participantID`) USING BTREE,
  KEY `infoItemID` (`infoItemID`) USING BTREE,
  CONSTRAINT `infos_ibfk_1` FOREIGN KEY (`activityID`) REFERENCES `activities` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `infos_ibfk_2` FOREIGN KEY (`participantID`) REFERENCES `participants` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `infos_ibfk_3` FOREIGN KEY (`infoItemID`) REFERENCES `infoitem` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=522 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of infos
-- ----------------------------
INSERT INTO `infos` VALUES ('378', '15', '120', '23', '计算机科学与技术');
INSERT INTO `infos` VALUES ('379', '15', '120', '24', '浙江工商大学');
INSERT INTO `infos` VALUES ('380', '15', '120', '25', '计算机科学与技术');
INSERT INTO `infos` VALUES ('381', '15', '120', '26', '应届本科毕业生');
INSERT INTO `infos` VALUES ('382', '15', '120', '27', '372');
INSERT INTO `infos` VALUES ('383', '15', '121', '23', '计算机科学与技术');
INSERT INTO `infos` VALUES ('384', '15', '121', '24', '中北大学');
INSERT INTO `infos` VALUES ('385', '15', '121', '25', '计算机科学与技术');
INSERT INTO `infos` VALUES ('386', '15', '121', '26', '应届本科毕业生');
INSERT INTO `infos` VALUES ('387', '15', '121', '27', '361');
INSERT INTO `infos` VALUES ('388', '15', '122', '23', '软件工程');
INSERT INTO `infos` VALUES ('389', '15', '122', '24', '湖北工业大学');
INSERT INTO `infos` VALUES ('390', '15', '122', '25', '计算机科学与技术');
INSERT INTO `infos` VALUES ('391', '15', '122', '26', '应届本科毕业生');
INSERT INTO `infos` VALUES ('392', '15', '122', '27', '370');
INSERT INTO `infos` VALUES ('393', '15', '123', '23', '软件工程');
INSERT INTO `infos` VALUES ('394', '15', '123', '24', '河北经贸大学');
INSERT INTO `infos` VALUES ('395', '15', '123', '25', '软件工程');
INSERT INTO `infos` VALUES ('396', '15', '123', '26', '应届本科毕业生');
INSERT INTO `infos` VALUES ('397', '15', '123', '27', '381');
INSERT INTO `infos` VALUES ('398', '15', '124', '23', '2');
INSERT INTO `infos` VALUES ('399', '15', '124', '24', '2');
INSERT INTO `infos` VALUES ('400', '15', '124', '25', '2');
INSERT INTO `infos` VALUES ('401', '15', '124', '26', '5');
INSERT INTO `infos` VALUES ('402', '15', '124', '27', '2');
INSERT INTO `infos` VALUES ('403', '15', '125', '23', '2');
INSERT INTO `infos` VALUES ('404', '15', '125', '24', '3');
INSERT INTO `infos` VALUES ('405', '15', '125', '25', '4');
INSERT INTO `infos` VALUES ('406', '15', '125', '26', '5');
INSERT INTO `infos` VALUES ('407', '15', '125', '27', '6');
INSERT INTO `infos` VALUES ('408', '15', '126', '23', '计算机科学与技术');
INSERT INTO `infos` VALUES ('409', '15', '126', '24', '东华大学');
INSERT INTO `infos` VALUES ('410', '15', '126', '25', '计算机科学与技术');
INSERT INTO `infos` VALUES ('411', '15', '126', '26', '应届本科毕业生');
INSERT INTO `infos` VALUES ('412', '15', '126', '27', '345');
INSERT INTO `infos` VALUES ('413', '15', '127', '23', '计算机科学与技术');
INSERT INTO `infos` VALUES ('414', '15', '127', '24', '南华大学');
INSERT INTO `infos` VALUES ('415', '15', '127', '25', '信息管理与信息系统');
INSERT INTO `infos` VALUES ('416', '15', '127', '26', '应届本科毕业生');
INSERT INTO `infos` VALUES ('417', '15', '127', '27', '358');
INSERT INTO `infos` VALUES ('418', '15', '128', '23', '计算机科学与技术');
INSERT INTO `infos` VALUES ('419', '15', '128', '24', '宁波工程学院');
INSERT INTO `infos` VALUES ('420', '15', '128', '25', '网络工程');
INSERT INTO `infos` VALUES ('421', '15', '128', '26', '应届本科毕业生');
INSERT INTO `infos` VALUES ('422', '15', '128', '27', '373');
INSERT INTO `infos` VALUES ('423', '15', '129', '23', '计算机科学与技术');
INSERT INTO `infos` VALUES ('424', '15', '129', '24', '华北水利水电大学');
INSERT INTO `infos` VALUES ('425', '15', '129', '25', '计算机科学与技术');
INSERT INTO `infos` VALUES ('426', '15', '129', '26', '应届本科毕业生');
INSERT INTO `infos` VALUES ('427', '15', '129', '27', '358');
INSERT INTO `infos` VALUES ('428', '15', '120', '28', '30');
INSERT INTO `infos` VALUES ('429', '15', '120', '29', '0');
INSERT INTO `infos` VALUES ('430', '15', '120', '30', '20');
INSERT INTO `infos` VALUES ('431', '15', '121', '28', '28');
INSERT INTO `infos` VALUES ('432', '15', '121', '29', '12');
INSERT INTO `infos` VALUES ('433', '15', '121', '30', '10');
INSERT INTO `infos` VALUES ('434', '15', '122', '28', '19');
INSERT INTO `infos` VALUES ('435', '15', '122', '29', '10');
INSERT INTO `infos` VALUES ('436', '15', '122', '30', '20');
INSERT INTO `infos` VALUES ('437', '15', '123', '28', '20');
INSERT INTO `infos` VALUES ('438', '15', '123', '29', '0');
INSERT INTO `infos` VALUES ('439', '15', '123', '30', '16');
INSERT INTO `infos` VALUES ('440', '15', '124', '28', '2');
INSERT INTO `infos` VALUES ('441', '15', '124', '29', '2');
INSERT INTO `infos` VALUES ('442', '15', '124', '30', '3');
INSERT INTO `infos` VALUES ('443', '15', '125', '28', '7');
INSERT INTO `infos` VALUES ('444', '15', '125', '29', '8');
INSERT INTO `infos` VALUES ('445', '15', '125', '30', '9');
INSERT INTO `infos` VALUES ('446', '15', '126', '28', '28');
INSERT INTO `infos` VALUES ('447', '15', '126', '29', '12');
INSERT INTO `infos` VALUES ('448', '15', '126', '30', '10');
INSERT INTO `infos` VALUES ('449', '15', '127', '28', '19');
INSERT INTO `infos` VALUES ('450', '15', '127', '29', '10');
INSERT INTO `infos` VALUES ('451', '15', '127', '30', '20');
INSERT INTO `infos` VALUES ('452', '15', '128', '28', '20');
INSERT INTO `infos` VALUES ('453', '15', '128', '29', '0');
INSERT INTO `infos` VALUES ('454', '15', '128', '30', '16');
INSERT INTO `infos` VALUES ('455', '15', '129', '28', '20');
INSERT INTO `infos` VALUES ('456', '15', '129', '29', '0');
INSERT INTO `infos` VALUES ('457', '15', '129', '30', '20');
INSERT INTO `infos` VALUES ('458', '15', '130', '23', '计算机科学与技术');
INSERT INTO `infos` VALUES ('459', '15', '130', '24', '宁波大学');
INSERT INTO `infos` VALUES ('460', '15', '130', '25', '信息管理与信息系统');
INSERT INTO `infos` VALUES ('461', '15', '130', '26', '其他人员');
INSERT INTO `infos` VALUES ('462', '15', '130', '27', '342');
INSERT INTO `infos` VALUES ('463', '15', '130', '28', '0');
INSERT INTO `infos` VALUES ('464', '15', '130', '29', '0');
INSERT INTO `infos` VALUES ('465', '15', '130', '30', '0');
INSERT INTO `infos` VALUES ('466', '15', '132', '23', 'test');
INSERT INTO `infos` VALUES ('467', '15', '132', '24', '测试');
INSERT INTO `infos` VALUES ('468', '15', '132', '25', '测试2');
INSERT INTO `infos` VALUES ('469', '15', '132', '28', '测试3');
INSERT INTO `infos` VALUES ('470', '15', '132', '26', '测试1');
INSERT INTO `infos` VALUES ('471', '15', '132', '27', '测试21');
INSERT INTO `infos` VALUES ('472', '15', '133', '23', '软件工程');
INSERT INTO `infos` VALUES ('473', '15', '133', '24', 'dhu');
INSERT INTO `infos` VALUES ('474', '15', '133', '25', '软件工程');
INSERT INTO `infos` VALUES ('475', '15', '133', '26', '谁知道');
INSERT INTO `infos` VALUES ('476', '15', '133', '27', '还挺好');
INSERT INTO `infos` VALUES ('477', '15', '133', '28', '哦吼');
INSERT INTO `infos` VALUES ('478', '15', '133', '29', '666');
INSERT INTO `infos` VALUES ('479', '15', '133', '30', '66666');
INSERT INTO `infos` VALUES ('497', '26', '139', '53', '软件工程');
INSERT INTO `infos` VALUES ('498', '26', '140', '53', '软件工程');
INSERT INTO `infos` VALUES ('499', '26', '141', '53', '软件工程');
INSERT INTO `infos` VALUES ('500', '26', '142', '53', '软件工程');
INSERT INTO `infos` VALUES ('501', '26', '143', '53', '软件工程');
INSERT INTO `infos` VALUES ('502', '26', '144', '53', '软件工程');
INSERT INTO `infos` VALUES ('503', '26', '145', '53', '软件工程');
INSERT INTO `infos` VALUES ('504', '26', '146', '53', '软件工程');
INSERT INTO `infos` VALUES ('505', '26', '147', '53', '软件工程');
INSERT INTO `infos` VALUES ('506', '26', '139', '54', 'niahidhasdjjjj');
INSERT INTO `infos` VALUES ('507', '15', '148', '24', 'fff反反复复发');
INSERT INTO `infos` VALUES ('508', '15', null, '23', '软件工程');
INSERT INTO `infos` VALUES ('509', '15', null, '23', '软件工程');
INSERT INTO `infos` VALUES ('510', '15', '149', '23', '3');
INSERT INTO `infos` VALUES ('511', '15', '149', '25', '1');
INSERT INTO `infos` VALUES ('512', '15', '149', '24', '5');
INSERT INTO `infos` VALUES ('513', '15', '149', '26', '2');
INSERT INTO `infos` VALUES ('514', '15', '149', '27', '3');
INSERT INTO `infos` VALUES ('515', '15', '149', '28', '5');
INSERT INTO `infos` VALUES ('516', '15', '149', '29', '1');
INSERT INTO `infos` VALUES ('517', '15', '149', '30', '2');
INSERT INTO `infos` VALUES ('518', '15', '150', '23', 'aha');
INSERT INTO `infos` VALUES ('519', '15', '150', '24', 'bhabalaba');
INSERT INTO `infos` VALUES ('520', '26', '139', '56', 'hhhhh');
INSERT INTO `infos` VALUES ('521', '26', '139', '57', '');

-- ----------------------------
-- Table structure for institution
-- ----------------------------
DROP TABLE IF EXISTS `institution`;
CREATE TABLE `institution` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '员工编号',
  `company` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '单位名称',
  `activityCount` int DEFAULT NULL COMMENT '单位总计活动数',
  `uplimit` int DEFAULT NULL COMMENT '单位活动数上限',
  `comment` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `deleteFlag` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `company` (`company`) USING BTREE,
  KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of institution
-- ----------------------------
INSERT INTO `institution` VALUES ('1', 'D大学计算机学院', '5', '50', 'D大学计算机学院', '0');
INSERT INTO `institution` VALUES ('2', '123', '6', '10', '3', '0');
INSERT INTO `institution` VALUES ('3', '东华大学', '0', '10', '啊哈', '0');
INSERT INTO `institution` VALUES ('4', '1', '1', '1', '1', '0');
INSERT INTO `institution` VALUES ('5', '3', '1', '1', '1', '0');
INSERT INTO `institution` VALUES ('6', '2', '0', '2', 'xht', '0');
INSERT INTO `institution` VALUES ('7', '5', '0', '2', 'xht', '0');
INSERT INTO `institution` VALUES ('8', '6', '1', '1', '1', '1');
INSERT INTO `institution` VALUES ('12', 'wow', '0', '5', '0', '0');
INSERT INTO `institution` VALUES ('13', 'ahahaha', '0', '2', 'xht', '1');
INSERT INTO `institution` VALUES ('14', 'www', '1', '2', 'xht', '1');
INSERT INTO `institution` VALUES ('15', 'we', '0', '3', 'xht', '1');
INSERT INTO `institution` VALUES ('16', 'AH', '0', '3', 'xht', '1');
INSERT INTO `institution` VALUES ('17', 'gg', '0', '2', 'xht', '1');
INSERT INTO `institution` VALUES ('19', '12345', '0', '1', '12345', '1');
INSERT INTO `institution` VALUES ('20', 'msg', '0', '50', 'msg', '1');
INSERT INTO `institution` VALUES ('21', '东华大学计算机学院', '10', '8', '东华大学计算机学院', '0');
INSERT INTO `institution` VALUES ('22', '的', '0', '2', 'xht', '0');
INSERT INTO `institution` VALUES ('23', '测试总活动数', '1', '10', 'xht', '0');
INSERT INTO `institution` VALUES ('24', '测试删除之后', '0', '3', '测试删除之后', '0');
INSERT INTO `institution` VALUES ('25', '测试', '0', '11', 'xht', '0');

-- ----------------------------
-- Table structure for logs
-- ----------------------------
DROP TABLE IF EXISTS `logs`;
CREATE TABLE `logs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `addDate` timestamp NULL DEFAULT NULL COMMENT '添加日期',
  `operator` int DEFAULT NULL COMMENT '操作内容',
  `category` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `content` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `oplog_ibfk_1` (`operator`) USING BTREE,
  CONSTRAINT `logs_ibfk_1` FOREIGN KEY (`operator`) REFERENCES `institution` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=273 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of logs
-- ----------------------------
INSERT INTO `logs` VALUES ('1', '2022-04-15 00:00:00', '21', '活动', '新增');
INSERT INTO `logs` VALUES ('2', '2022-04-15 00:00:00', '21', '活动', '添加成功');
INSERT INTO `logs` VALUES ('3', '2022-04-15 00:00:00', '21', '活动', '结束成功');
INSERT INTO `logs` VALUES ('4', '2022-04-15 00:00:00', '21', '活动', '删除成功');
INSERT INTO `logs` VALUES ('5', '2022-04-15 00:00:00', '21', '活动', '删除成功');
INSERT INTO `logs` VALUES ('6', '2022-04-15 00:00:00', null, null, null);
INSERT INTO `logs` VALUES ('7', '2022-04-15 00:00:00', '21', '活动', '结束成功');
INSERT INTO `logs` VALUES ('8', '2022-04-15 10:00:00', '21', '活动', '结束成功');
INSERT INTO `logs` VALUES ('9', '2022-04-15 00:00:00', '21', '活动', '添加成功');
INSERT INTO `logs` VALUES ('10', '2022-04-15 00:00:00', '21', '活动', '更新成功');
INSERT INTO `logs` VALUES ('11', '2022-04-15 00:00:00', '21', '活动', '添加成功');
INSERT INTO `logs` VALUES ('12', '2022-04-15 18:42:43', '21', '活动', '更新成功');
INSERT INTO `logs` VALUES ('13', '2022-04-15 21:09:16', '21', '评分项', '删除成功');
INSERT INTO `logs` VALUES ('14', '2022-04-15 21:09:18', '21', '评分项', '更新成功');
INSERT INTO `logs` VALUES ('15', '2022-04-15 21:09:19', '21', '评分项', '更新成功');
INSERT INTO `logs` VALUES ('16', '2022-04-15 21:09:20', '21', '评分项', '更新成功');
INSERT INTO `logs` VALUES ('17', '2022-04-15 21:10:15', '21', '评分项', '新增成功');
INSERT INTO `logs` VALUES ('18', '2022-04-15 23:46:51', '21', '评分项', '删除成功');
INSERT INTO `logs` VALUES ('19', '2022-04-17 16:55:48', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('20', '2022-04-17 16:55:49', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('21', '2022-04-17 16:55:51', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('22', '2022-04-17 16:56:06', '21', '信息项', '删除成功');
INSERT INTO `logs` VALUES ('23', '2022-04-17 16:56:10', '21', '信息项', '新增成功');
INSERT INTO `logs` VALUES ('24', '2022-04-17 18:50:04', '21', '分组', '新增成功');
INSERT INTO `logs` VALUES ('25', '2022-04-17 18:50:14', '21', '分组', '更新成功');
INSERT INTO `logs` VALUES ('26', '2022-04-17 18:51:17', '21', '分组', '新增成功');
INSERT INTO `logs` VALUES ('27', '2022-04-17 18:51:21', '21', '分组', '更新成功');
INSERT INTO `logs` VALUES ('28', '2022-04-17 22:14:25', '21', '带组名选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('29', '2022-04-17 22:14:51', '21', '带组名选手管理', '删除失败');
INSERT INTO `logs` VALUES ('30', '2022-04-17 22:16:33', '21', '带组名选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('31', '2022-04-17 22:16:38', '21', '带组名选手管理', '删除失败');
INSERT INTO `logs` VALUES ('32', '2022-04-17 22:18:46', '21', '带组名选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('33', '2022-04-17 22:27:06', '21', '带组名选手管理', '删除失败');
INSERT INTO `logs` VALUES ('34', '2022-04-17 22:27:45', '21', '带组名选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('35', '2022-04-17 22:28:36', '21', '带组名选手管理', '更新成功');
INSERT INTO `logs` VALUES ('36', '2022-04-17 22:37:13', '21', '带组名选手管理', '更新成功');
INSERT INTO `logs` VALUES ('37', '2022-04-17 22:37:48', '21', '带组名选手管理', '更新成功');
INSERT INTO `logs` VALUES ('38', '2022-04-17 22:37:52', '21', '带组名选手管理', '更新成功');
INSERT INTO `logs` VALUES ('39', '2022-04-17 22:37:55', '21', '带组名选手管理', '删除成功');
INSERT INTO `logs` VALUES ('40', '2022-04-17 22:49:10', null, '带组名选手管理', '更新成功');
INSERT INTO `logs` VALUES ('41', '2022-04-17 22:54:54', '21', '带组名选手管理', '更新成功');
INSERT INTO `logs` VALUES ('42', '2022-04-17 22:57:01', '21', '带组名选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('43', '2022-04-17 23:02:13', '21', '带组名选手管理', '顺序更新失败');
INSERT INTO `logs` VALUES ('44', '2022-04-17 23:02:42', '21', '带组名选手管理', '顺序更新失败');
INSERT INTO `logs` VALUES ('45', '2022-04-17 23:02:49', '21', '带组名选手管理', '顺序更新失败');
INSERT INTO `logs` VALUES ('46', '2022-04-17 23:03:36', '21', '带组名选手管理', '删除失败');
INSERT INTO `logs` VALUES ('47', '2022-04-17 23:16:53', '21', '带组名选手管理', '顺序更改成功');
INSERT INTO `logs` VALUES ('48', '2022-04-17 23:40:35', '21', '专家管理', '未读取到有效导入数据，可能第1行csv编码或者表头或者内容错误！');
INSERT INTO `logs` VALUES ('49', '2022-04-17 23:41:29', '21', '专家管理', '未读取到有效导入数据，可能第1行csv编码或者表头或者内容错误！');
INSERT INTO `logs` VALUES ('50', '2022-04-17 23:42:13', '21', '专家管理', '未读取到有效导入数据，可能第1行csv编码或者表头或者内容错误！');
INSERT INTO `logs` VALUES ('51', '2022-04-17 23:42:55', '21', '专家管理', '未读取到有效导入数据，可能第1行csv编码或者表头或者内容错误！');
INSERT INTO `logs` VALUES ('52', '2022-04-17 23:44:10', '21', '专家管理', '未读取到有效导入数据，可能第1行csv编码或者表头或者内容错误！');
INSERT INTO `logs` VALUES ('53', '2022-04-17 23:45:07', '21', '专家管理', '导入成功');
INSERT INTO `logs` VALUES ('54', '2022-04-17 23:45:51', '21', '专家管理', '已将密码设置为证件号码后6位');
INSERT INTO `logs` VALUES ('55', '2022-04-17 23:45:56', '21', '专家管理', '更新成功');
INSERT INTO `logs` VALUES ('56', '2022-04-17 23:46:06', '21', '专家管理', '删除成功');
INSERT INTO `logs` VALUES ('57', '2022-04-17 23:49:55', '21', '专家管理', '未读取到有效导入数据，可能第1行csv编码或者表头或者内容错误！');
INSERT INTO `logs` VALUES ('58', '2022-04-17 23:51:08', '21', '专家管理', '导入失败');
INSERT INTO `logs` VALUES ('59', '2022-04-17 23:55:16', '21', '专家管理', '导入失败');
INSERT INTO `logs` VALUES ('60', '2022-04-17 23:55:39', '21', '专家管理', '未读取到有效导入数据，可能第1行csv编码或者表头或者内容错误！');
INSERT INTO `logs` VALUES ('61', '2022-04-18 00:00:52', '21', '专家管理', '删除成功');
INSERT INTO `logs` VALUES ('62', '2022-04-18 00:03:55', '21', '专家管理', '导入成功');
INSERT INTO `logs` VALUES ('63', '2022-04-18 00:04:00', '21', '专家管理', '导入成功');
INSERT INTO `logs` VALUES ('64', '2022-04-18 00:04:11', '21', '专家管理', '导入成功');
INSERT INTO `logs` VALUES ('65', '2022-04-18 00:05:20', '21', '专家管理', '导入成功');
INSERT INTO `logs` VALUES ('66', '2022-04-18 00:10:33', '21', '专家管理', '删除成功');
INSERT INTO `logs` VALUES ('67', '2022-04-18 00:10:38', '21', '专家管理', '导入成功');
INSERT INTO `logs` VALUES ('68', '2022-04-18 00:10:55', '21', '专家管理', '删除成功');
INSERT INTO `logs` VALUES ('69', '2022-04-18 00:11:15', '21', '专家管理', '导入成功');
INSERT INTO `logs` VALUES ('70', '2022-04-18 20:22:18', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('71', '2022-04-18 20:22:18', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('72', '2022-04-18 20:22:19', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('73', '2022-04-18 20:22:18', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('74', '2022-04-18 20:22:46', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('75', '2022-04-18 20:29:20', '21', '专家管理', '更新失败');
INSERT INTO `logs` VALUES ('76', '2022-04-18 20:31:07', '21', '专家管理', '更新失败');
INSERT INTO `logs` VALUES ('77', '2022-04-18 20:32:36', '21', '专家评分状态', '撤回成功');
INSERT INTO `logs` VALUES ('78', '2022-04-18 20:46:33', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('79', '2022-04-18 20:46:36', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('80', '2022-04-19 13:17:57', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('81', '2022-04-19 13:18:06', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('82', '2022-04-19 13:46:34', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('83', '2022-04-19 13:46:36', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('84', '2022-04-19 13:47:14', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('85', '2022-04-19 13:59:30', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('86', '2022-04-19 14:05:36', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('87', '2022-04-19 14:15:16', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('88', '2022-04-19 14:15:56', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('89', '2022-04-19 14:21:37', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('90', '2022-04-19 14:34:02', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('91', '2022-04-19 14:34:55', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('92', '2022-04-19 14:35:22', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('93', '2022-04-19 14:46:46', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('94', '2022-04-19 14:53:54', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('95', '2022-04-19 14:58:55', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('96', '2022-04-19 15:09:57', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('97', '2022-04-19 17:48:11', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('98', '2022-04-19 17:48:12', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('99', '2022-04-19 17:48:14', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('100', '2022-04-19 17:50:16', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('101', '2022-04-19 17:50:21', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('102', '2022-04-19 17:50:34', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('103', '2022-04-19 17:53:30', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('104', '2022-04-19 17:53:38', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('105', '2022-04-19 17:53:41', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('106', '2022-04-19 17:54:03', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('107', '2022-04-19 17:58:02', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('108', '2022-04-19 18:08:56', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('109', '2022-04-19 18:08:57', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('110', '2022-04-19 18:51:13', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('111', '2022-04-19 18:51:14', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('112', '2022-04-19 18:51:57', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('113', '2022-04-19 18:53:52', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('114', '2022-04-19 18:53:53', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('115', '2022-04-19 18:53:55', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('116', '2022-04-19 18:59:09', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('117', '2022-04-19 18:59:20', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('118', '2022-04-19 19:09:32', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('119', '2022-04-19 19:09:49', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('120', '2022-04-19 19:10:59', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('121', '2022-04-19 20:52:22', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('122', '2022-04-19 20:52:28', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('123', '2022-04-19 20:52:31', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('124', '2022-04-19 20:53:02', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('125', '2022-04-19 20:53:04', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('126', '2022-04-19 20:53:05', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('127', '2022-04-19 20:53:09', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('128', '2022-04-19 20:53:12', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('129', '2022-04-19 20:53:38', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('130', '2022-04-19 20:53:46', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('131', '2022-04-19 20:54:00', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('132', '2022-04-19 20:54:44', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('133', '2022-04-19 20:54:49', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('134', '2022-04-19 20:54:50', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('135', '2022-04-19 20:54:54', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('136', '2022-04-19 20:55:11', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('137', '2022-04-19 20:55:12', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('138', '2022-04-19 20:55:14', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('139', '2022-04-19 20:55:14', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('140', '2022-04-19 20:55:15', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('141', '2022-04-19 20:56:05', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('142', '2022-04-19 20:56:08', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('143', '2022-04-19 20:56:10', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('144', '2022-04-19 20:56:11', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('145', '2022-04-19 20:56:12', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('146', '2022-04-19 20:56:22', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('147', '2022-04-19 20:56:30', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('148', '2022-04-19 20:56:32', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('149', '2022-04-19 20:56:35', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('150', '2022-04-19 21:00:06', '21', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('151', '2022-04-21 11:41:18', '1', '活动', '添加成功');
INSERT INTO `logs` VALUES ('152', '2022-04-21 11:41:58', '1', '评分项', '新增成功');
INSERT INTO `logs` VALUES ('153', '2022-04-21 11:43:33', '1', '评分项', '更新成功');
INSERT INTO `logs` VALUES ('154', '2022-04-21 11:43:40', '1', '评分项', '新增成功');
INSERT INTO `logs` VALUES ('155', '2022-04-21 11:43:50', '1', '评分项', '新增成功');
INSERT INTO `logs` VALUES ('156', '2022-04-21 11:43:53', '1', '评分项', '更新成功');
INSERT INTO `logs` VALUES ('157', '2022-04-21 11:43:57', '1', '评分项', '更新成功');
INSERT INTO `logs` VALUES ('158', '2022-04-21 11:44:13', '1', '评分项', '更新成功');
INSERT INTO `logs` VALUES ('159', '2022-04-21 11:44:14', '1', '评分项', '更新成功');
INSERT INTO `logs` VALUES ('160', '2022-04-21 11:44:29', '1', '评分项', '更新成功');
INSERT INTO `logs` VALUES ('161', '2022-04-21 11:44:44', '1', '评分项', '更新成功');
INSERT INTO `logs` VALUES ('162', '2022-04-21 11:45:53', '1', '评分项', '更新成功');
INSERT INTO `logs` VALUES ('163', '2022-04-21 11:46:25', '1', '信息项', '新增成功');
INSERT INTO `logs` VALUES ('164', '2022-04-21 11:46:29', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('165', '2022-04-21 11:46:31', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('166', '2022-04-21 11:47:55', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('167', '2022-04-21 11:47:55', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('168', '2022-04-21 11:48:40', '1', '分组', '新增成功');
INSERT INTO `logs` VALUES ('169', '2022-04-21 11:48:46', '1', '分组', '新增成功');
INSERT INTO `logs` VALUES ('170', '2022-04-21 11:48:52', '1', '分组', '新增成功');
INSERT INTO `logs` VALUES ('171', '2022-04-21 12:34:04', '1', '专家管理', '导入成功');
INSERT INTO `logs` VALUES ('172', '2022-04-21 14:14:18', '1', '专家管理', '导入成功');
INSERT INTO `logs` VALUES ('173', '2022-04-21 14:14:41', '1', '专家管理', '导入成功');
INSERT INTO `logs` VALUES ('174', '2022-04-21 17:11:35', '1', '专家管理', '导入成功');
INSERT INTO `logs` VALUES ('175', '2022-04-21 17:14:25', '1', '专家管理', '未读取到有效导入数据，可能第4行csv编码或者表头或者内容错误！');
INSERT INTO `logs` VALUES ('176', '2022-04-21 17:15:15', '1', '专家管理', '未读取到有效导入数据，可能第4行csv编码或者表头或者内容错误！');
INSERT INTO `logs` VALUES ('177', '2022-04-21 17:16:27', '1', '专家管理', '未读取到有效导入数据，可能第4行csv编码或者表头或者内容错误！');
INSERT INTO `logs` VALUES ('178', '2022-04-21 17:17:52', '1', '专家管理', '未读取到有效导入数据，可能第4行csv编码或者表头或者内容错误！');
INSERT INTO `logs` VALUES ('179', '2022-04-21 17:18:06', '1', '专家管理', '未读取到有效导入数据，可能第4行csv编码或者表头或者内容错误！');
INSERT INTO `logs` VALUES ('180', '2022-04-21 17:19:35', '1', '专家管理', '导入成功');
INSERT INTO `logs` VALUES ('181', '2022-04-21 17:26:52', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('182', '2022-04-21 17:26:53', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('183', '2022-04-21 17:27:00', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('184', '2022-04-21 17:27:01', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('185', '2022-04-21 17:27:01', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('186', '2022-04-21 17:34:38', '1', '选手管理', '未读取到有效导入数据，可能第9行csv编码或者表头或者内容错误！');
INSERT INTO `logs` VALUES ('187', '2022-04-21 17:35:33', '1', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('188', '2022-04-21 17:47:34', '1', '专家评分状态', '撤回成功');
INSERT INTO `logs` VALUES ('189', '2022-04-22 12:41:33', '1', '专家管理', '导入成功');
INSERT INTO `logs` VALUES ('190', '2022-04-22 12:42:04', '1', '专家管理', '未读取到有效导入数据，可能第2行csv编码或者表头或者内容错误！');
INSERT INTO `logs` VALUES ('191', '2022-04-22 12:50:15', '1', '专家管理', '未读取到有效导入数据，可能第2行csv编码或者表头或者内容错误！');
INSERT INTO `logs` VALUES ('192', '2022-04-22 12:53:14', '1', '专家管理', '未读取到有效导入数据，可能第2行csv编码或者表头或者内容错误！');
INSERT INTO `logs` VALUES ('193', '2022-04-22 12:55:23', '1', '专家管理', '导入失败');
INSERT INTO `logs` VALUES ('194', '2022-04-22 12:56:11', '1', '专家管理', '导入失败');
INSERT INTO `logs` VALUES ('195', '2022-04-22 13:19:32', '1', '专家管理', '已将密码设置为证件号码后6位');
INSERT INTO `logs` VALUES ('196', '2022-04-22 13:19:47', '1', '专家管理', '已将密码设置为证件号码后6位');
INSERT INTO `logs` VALUES ('197', '2022-04-22 13:20:17', '1', '专家管理', '已将密码设置为证件号码后6位');
INSERT INTO `logs` VALUES ('198', '2022-04-22 13:21:03', '1', '专家管理', '已将密码设置为证件号码后6位');
INSERT INTO `logs` VALUES ('199', '2022-04-22 13:22:34', '1', '专家管理', '已将密码设置为证件号码后6位');
INSERT INTO `logs` VALUES ('200', '2022-04-22 14:22:01', '1', '专家管理', '导入失败');
INSERT INTO `logs` VALUES ('201', '2022-04-22 17:16:37', '1', '分组', '新增成功');
INSERT INTO `logs` VALUES ('202', '2022-04-22 20:09:33', '1', '活动', '添加成功');
INSERT INTO `logs` VALUES ('203', '2022-04-22 20:43:01', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('204', '2022-04-22 20:46:20', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('205', '2022-04-22 20:46:38', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('206', '2022-04-22 21:14:15', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('207', '2022-04-22 21:15:57', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('208', '2022-04-22 21:17:13', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('209', '2022-04-24 12:58:35', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('210', '2022-04-24 12:58:36', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('211', '2022-04-24 12:59:14', '1', '选手管理', '更新成功');
INSERT INTO `logs` VALUES ('212', '2022-04-24 12:59:23', '1', '信息项', '更新成功');
INSERT INTO `logs` VALUES ('213', '2022-04-24 13:10:47', '1', '专家评分状态', '撤回成功');
INSERT INTO `logs` VALUES ('214', '2022-04-24 13:30:20', '1', '专家评分状态', '撤回成功');
INSERT INTO `logs` VALUES ('215', '2022-04-25 15:22:41', '1', '专家管理', '已将密码设置为证件号码后6位');
INSERT INTO `logs` VALUES ('216', '2022-05-03 13:19:32', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('217', '2022-05-03 13:22:04', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('218', '2022-05-03 14:21:32', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('219', '2022-05-03 15:02:42', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('220', '2022-05-03 16:40:47', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('221', '2022-05-03 16:43:32', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('222', '2022-05-03 16:56:32', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('223', '2022-05-03 17:18:30', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('224', '2022-05-03 17:22:01', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('225', '2022-05-03 17:23:11', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('226', '2022-05-03 17:28:03', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('227', '2022-05-03 17:28:53', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('228', '2022-05-03 17:29:28', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('229', '2022-05-03 18:21:49', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('230', '2022-05-03 18:33:37', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('231', '2022-05-03 18:35:23', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('232', '2022-05-03 19:04:47', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('233', '2022-05-03 19:19:33', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('234', '2022-05-03 19:28:56', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('235', '2022-05-03 19:34:52', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('236', '2022-05-03 19:38:53', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('237', '2022-05-03 19:40:31', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('238', '2022-05-03 19:41:46', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('239', '2022-05-03 19:42:36', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('240', '2022-05-03 19:43:13', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('241', '2022-05-03 19:52:43', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('242', '2022-05-03 20:03:00', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('243', '2022-05-03 20:05:12', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('244', '2022-05-03 20:10:14', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('245', '2022-05-03 20:10:37', '21', '选手管理', '更新成功');
INSERT INTO `logs` VALUES ('246', '2022-05-03 20:11:30', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('247', '2022-05-03 20:17:28', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('248', '2022-05-03 20:17:51', '21', '选手管理', '更新成功');
INSERT INTO `logs` VALUES ('249', '2022-05-03 20:18:02', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('250', '2022-05-03 20:18:45', '21', '选手管理', '更新成功');
INSERT INTO `logs` VALUES ('251', '2022-05-03 20:18:56', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('252', '2022-05-03 21:19:53', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('253', '2022-05-03 21:21:28', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('254', '2022-05-03 21:23:01', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('255', '2022-05-03 21:24:33', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('256', '2022-05-03 21:24:50', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('257', '2022-05-03 21:25:34', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('258', '2022-05-03 21:26:18', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('259', '2022-05-03 21:27:43', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('260', '2022-05-03 21:28:39', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('261', '2022-05-03 21:29:06', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('262', '2022-05-03 21:30:32', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('263', '2022-05-03 21:34:09', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('264', '2022-05-03 21:41:31', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('265', '2022-05-03 21:52:13', '21', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('266', '2022-05-23 21:03:42', '21', '活动', '活动到达上限无法添加');
INSERT INTO `logs` VALUES ('267', '2022-05-23 21:03:59', '21', '活动', '更新成功');
INSERT INTO `logs` VALUES ('268', '2022-05-23 21:04:27', '1', '活动', '添加成功');
INSERT INTO `logs` VALUES ('269', '2022-07-01 18:12:28', '1', '活动', '更新成功');
INSERT INTO `logs` VALUES ('270', '2022-07-05 19:24:55', '1', '选手管理', '导入完成，无姓名行已经被忽略');
INSERT INTO `logs` VALUES ('271', '2022-11-16 15:18:00', '1', '活动', '更新成功');
INSERT INTO `logs` VALUES ('272', '2023-03-12 23:21:25', '1', '分组', '删除失败');

-- ----------------------------
-- Table structure for mail
-- ----------------------------
DROP TABLE IF EXISTS `mail`;
CREATE TABLE `mail` (
  `mailAddress` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '邮箱地址',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'SMTP的验证码',
  PRIMARY KEY (`mailAddress`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of mail
-- ----------------------------
INSERT INTO `mail` VALUES ('ratemail@126.com', 'IOLMDKJXAQJQUAMN');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int NOT NULL AUTO_INCREMENT,
  `url` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `path` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `component` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `iconCls` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `keepAlive` tinyint(1) DEFAULT NULL,
  `requireAuth` tinyint(1) DEFAULT NULL,
  `parentId` int DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `parentId` (`parentId`) USING BTREE,
  CONSTRAINT `menu_ibfk_1` FOREIGN KEY (`parentId`) REFERENCES `menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '/', null, null, '所有', null, null, null, null, '1');
INSERT INTO `menu` VALUES ('2', '/', '/home', 'Home', '员工资料', 'fa fa-user-circle-o', null, '1', '1', '1');
INSERT INTO `menu` VALUES ('3', '/', '/home', 'Home', '单位管理', 'fa fa-address-card-o', null, '1', '1', '1');
INSERT INTO `menu` VALUES ('4', '/', '/home', 'Home', '活动管理', 'fa fa-money', null, '1', '1', '1');
INSERT INTO `menu` VALUES ('5', '/', '/home', 'Home', '统计管理', 'fa fa-bar-chart', null, '1', '1', '1');
INSERT INTO `menu` VALUES ('6', '/', '/home', 'Home', '系统管理', 'fa fa-windows', null, '1', '1', '1');
INSERT INTO `menu` VALUES ('7', '/employee/basic/**', '/emp/basic', 'EmpBasic', '基本资料', null, null, '1', '2', '1');
INSERT INTO `menu` VALUES ('8', '/employee/advanced/**', '/emp/adv', 'EmpAdv', '高级资料', null, null, '1', '2', '1');
INSERT INTO `menu` VALUES ('9', '/personnel/emp/**', '/per/emp', 'PerEmp', '查看所有单位', null, null, '1', '3', '1');
INSERT INTO `menu` VALUES ('10', '/personnel/ec/**', '/per/ec', 'PerEc', '查看各个单位管理员列表', null, '0', '1', '36', '1');
INSERT INTO `menu` VALUES ('11', '/personnel/train/**', '/per/train', 'PerTrain', '员工培训', null, null, '1', '9', '1');
INSERT INTO `menu` VALUES ('12', '/personnel/salary/**', '/per/salary', 'PerSalary', '员工调薪', null, null, '1', '3', '1');
INSERT INTO `menu` VALUES ('13', '/personnel/remove/**', '/per/mv', 'PerMv', '员工调动', null, null, '1', '3', '1');
INSERT INTO `menu` VALUES ('14', '/salary/sob/**', '/ActivitM/group', 'SalGroup', '导入选手', null, null, '1', '36', '1');
INSERT INTO `menu` VALUES ('15', '/salary/sobcfg/**', '/ActivitM/sobcfg', 'SalSobCfg', '专家活动设置', null, null, '1', '36', '1');
INSERT INTO `menu` VALUES ('16', '/salary/table/**', '/ActivitM/table', 'SalTable', '分组管理', null, null, '1', '36', '1');
INSERT INTO `menu` VALUES ('17', '/salary/month/**', '/ActivitM/month', 'SalMonth', '评分项设置', null, null, '1', '36', '1');
INSERT INTO `menu` VALUES ('18', '', '/ActivitM/search', 'SalSearch', '活动管理', null, null, '1', '4', '1');
INSERT INTO `menu` VALUES ('19', '/statistics/all/**', '/sta/all', 'StaAll', '综合信息统计', null, null, '1', '5', '1');
INSERT INTO `menu` VALUES ('20', '/statistics/score/**', '/sta/score', 'StaScore', '员工积分统计', null, null, '1', '5', '1');
INSERT INTO `menu` VALUES ('21', '/statistics/personnel/**', '/sta/pers', 'StaPers', '人事信息统计', null, null, '1', '5', '1');
INSERT INTO `menu` VALUES ('22', '/statistics/recored/**', '/sta/record', 'StaRecord', '人事记录统计', null, null, '1', '5', '1');
INSERT INTO `menu` VALUES ('23', '/system/basic/**', '/sys/basic', 'SysBasic', '基础信息设置', null, null, '1', '6', '1');
INSERT INTO `menu` VALUES ('24', '/system/cfg/**', '/sys/cfg', 'SysCfg', '系统管理', null, null, '1', '6', '1');
INSERT INTO `menu` VALUES ('25', '/system/log/**', '/sys/log', 'SysLog', '操作日志管理', null, null, '1', '6', '1');
INSERT INTO `menu` VALUES ('26', '/system/hr/**', '/sys/hr', 'SysHr', '查看各个单位管理员列表', null, null, '1', '6', '1');
INSERT INTO `menu` VALUES ('27', '/system/data/**', '/sys/data', 'SysData', '备份恢复数据库', null, null, '1', '6', '1');
INSERT INTO `menu` VALUES ('28', '/system/init/**', '/sys/init', 'SysInit', '初始化数据库', null, null, '1', '6', '1');
INSERT INTO `menu` VALUES ('29', '/school/basic/**', '/school/basic', 'SchoolBasic', '活动/专家管理', null, null, '1', '30', '1');
INSERT INTO `menu` VALUES ('30', '/', '/home', 'Home', '活动/专家管理', 'fa fa-plus-square', null, '1', '1', '1');
INSERT INTO `menu` VALUES ('31', null, '/participantsM', 'SalPar', '选手管理', null, null, null, '36', '1');
INSERT INTO `menu` VALUES ('32', null, '/home', 'Home', '学生管理', '', null, '1', '1', '1');
INSERT INTO `menu` VALUES ('33', null, '/student/basic', 'Home', 'www', null, null, '1', '1', '1');
INSERT INTO `menu` VALUES ('34', null, '/student/view', 'StuView', '查看学生', null, null, '1', '32', '1');
INSERT INTO `menu` VALUES ('35', '', '/StuView', 'StuView1', 'StuView', '', null, '1', '6', '1');
INSERT INTO `menu` VALUES ('36', '/', '/home', 'Home', '隐藏显示', null, null, '1', '1', '1');
INSERT INTO `menu` VALUES ('37', '/salary/situation/**', '/ActivitM/situation', 'SalSituation', '查看专家评分', null, null, '1', '36', '1');
INSERT INTO `menu` VALUES ('38', '/salary/situation/**', '/ActivitM/infos', 'SalInfos', '信息项设置', null, null, '1', '36', '1');
INSERT INTO `menu` VALUES ('39', null, null, null, '日志', null, null, '1', '4', '1');
INSERT INTO `menu` VALUES ('40', null, '/SalLog', 'SalLog', '日志', null, null, '1', '4', '1');
INSERT INTO `menu` VALUES ('41', null, '/Student/Product', 'Product', '项目', null, null, null, null, '1');
INSERT INTO `menu` VALUES ('42', '/', '/home', 'Home', '个人资料', 'fa-fa-windows', null, '1', '1', '1');
INSERT INTO `menu` VALUES ('43', '/', '/home', 'Home', '研究生管理', 'fa-fa-windows', null, '1', '1', '1');
INSERT INTO `menu` VALUES ('44', '/', '/home', 'Home', '本科生管理', 'fa-fa-windows', null, '1', '1', '1');
INSERT INTO `menu` VALUES ('45', '/salary/search/**', '/UserInfo', 'UserInfo', '个人资料', null, null, '1', '42', '1');
INSERT INTO `menu` VALUES ('46', '/salary/search/**', '/student/Paper', 'Paper', '论文', null, null, '1', '69', '1');
INSERT INTO `menu` VALUES ('47', '/salary/search/**', '/student/Patent', 'Patent', '专利', null, null, '1', '69', '1');
INSERT INTO `menu` VALUES ('48', '/salary/search/**', '/student/ResearchAward', 'ResearchAward', '奖励', null, null, '1', '69', '1');
INSERT INTO `menu` VALUES ('49', '/salary/search/**', '/student/Product', 'Product', '科研项目', null, null, '1', '69', '1');
INSERT INTO `menu` VALUES ('50', '/salary/search/**', '/student/AcademicMonograph', 'AcademicMonograph', '学术专著和教材', null, null, '1', '69', '1');
INSERT INTO `menu` VALUES ('51', '/salary/search/**', '/student/ResearchProject', 'ResearchProject', '制造或设计的产品', null, null, '1', '69', '1');
INSERT INTO `menu` VALUES ('52', '/salary/search/**', '/student/Achievements', 'Achievements', '成果列表', null, null, '1', '70', '1');
INSERT INTO `menu` VALUES ('53', '/', '/home', 'Home', '待审核', 'fa fa-windows', null, '1', '44', '1');
INSERT INTO `menu` VALUES ('54', '/', '/home', 'Home', '学生成果', 'fa fa-windows', null, '1', '44', '1');
INSERT INTO `menu` VALUES ('55', '/', '/home', 'SalSearch', '所有成果', 'fa fa-windows', null, '1', '43', '1');
INSERT INTO `menu` VALUES ('56', '/salary/search/**', '/teacher/tPaper', 'Paper', '论文', null, null, '1', '55', '1');
INSERT INTO `menu` VALUES ('57', '/salary/search/**', '/teacher/tPatent', 'Patent', '专利', null, null, '1', '55', '1');
INSERT INTO `menu` VALUES ('58', '/salary/search/**', '/teacher/tResearchAward', 'tResearchAward', '科研获奖', null, null, '1', '55', '1');
INSERT INTO `menu` VALUES ('59', '/salary/search/**', '/teacher/tProduct', 'tProduct', '科研项目', null, null, '1', '55', '1');
INSERT INTO `menu` VALUES ('60', '/salary/search/**', '/teacher/tAcademicMonograph', 'tAcademicMonograph', '学术专著和教材', null, null, '1', '55', '1');
INSERT INTO `menu` VALUES ('61', '/salary/search/**', '/teacher/tResearchProject', 'tResearchProject', '制造或设计的产品', null, null, '1', '55', '1');
INSERT INTO `menu` VALUES ('62', '/salary/search/**', '/teacher/tAchievements', 'tAchievements', '学术成果列表', null, null, '1', '54', '1');
INSERT INTO `menu` VALUES ('63', '/salary/search/**', '/teacher/tExamine', 'tExamine', '待审核列表', null, null, '1', '53', '1');
INSERT INTO `menu` VALUES ('64', '/', '/home', 'Home', '指标点管理', 'fa fa-money', null, '1', '1', '1');
INSERT INTO `menu` VALUES ('65', '/', '/bookview', 'SalBookView', '指标点管理', null, null, '1', '64', '1');
INSERT INTO `menu` VALUES ('66', '/', '/home', 'Home', '成果管理', 'fa fa-money', null, '1', '1', '1');
INSERT INTO `menu` VALUES ('67', '/', '/admin/SalExamine', 'SalExamine', '待审核', null, null, '1', '66', '1');
INSERT INTO `menu` VALUES ('68', '/', '/admin/SalPaper', 'Paper', '所有成果', null, null, '1', '66', '1');
INSERT INTO `menu` VALUES ('69', '/', '/home', 'Home', '成果申报', 'fa-fa-windows', null, '1', '43', '1');
INSERT INTO `menu` VALUES ('70', '/', '/home', 'Home', '成果列表', 'fa-fa-windows', null, '1', '43', '1');
INSERT INTO `menu` VALUES ('71', '/', '/home', 'Home', '毕业论文评审', 'fa fa-windows', null, '1', '44', '1');
INSERT INTO `menu` VALUES ('72', '/salary/search/**', '/student/PaperComment', 'PaperCommit', '毕业论文评审记录', 'fa fa-windows', null, '1', '71', '1');
INSERT INTO `menu` VALUES ('73', '/salary/search/**', '/teacher/tperact/actList', 'ActList', '活动列表', null, null, '1', '81', '1');
INSERT INTO `menu` VALUES ('74', '/salary/search/**', '/student/infos', 'Infos', '信息项设置', null, null, '1', '4', '0');
INSERT INTO `menu` VALUES ('75', '/salary/search/**', '/teacher/tperact/score', 'Score', '分数', null, null, '1', '81', '0');
INSERT INTO `menu` VALUES ('76', '/salary/search/**', '/student/search', 'Stusearch', '活动列表', null, null, '1', '4', '1');
INSERT INTO `menu` VALUES ('77', '/salary/score/**', '/ActivitM/score', 'SalScore', '分数统计', null, null, '1', '36', '1');
INSERT INTO `menu` VALUES ('78', '', '/ActivitM/detail', 'SalDetail', '小组分数明细', null, null, '1', '36', '1');
INSERT INTO `menu` VALUES ('79', null, '/ActivitM/subActivity', 'SalSubActivity', '子活动管理', null, null, null, '36', '1');
INSERT INTO `menu` VALUES ('80', '/salary/search/**', '/secretary/tperact/actList', 'ActList', '活动列表', null, null, '1', '82', '1');
INSERT INTO `menu` VALUES ('81', '/', '/home', 'Home', '专家活动管理', 'fa fa-money', null, '1', '1', '1');
INSERT INTO `menu` VALUES ('82', '/', '/home', 'Home', '秘书活动管理', 'fa fa-money', null, '1', '1', '1');
INSERT INTO `menu` VALUES ('83', null, '/ActivitM/total', 'SalTotal', '总分项设置', null, null, '1', '36', '1');
INSERT INTO `menu` VALUES ('84', null, '/ActivitM/expertScore', 'ExpertScore', '专家打分情况', null, null, '1', '36', '1');

-- ----------------------------
-- Table structure for menu_role
-- ----------------------------
DROP TABLE IF EXISTS `menu_role`;
CREATE TABLE `menu_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mid` int DEFAULT NULL,
  `rid` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `mid` (`mid`) USING BTREE,
  KEY `rid` (`rid`) USING BTREE,
  CONSTRAINT `menu_role_ibfk_1` FOREIGN KEY (`mid`) REFERENCES `menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `menu_role_ibfk_2` FOREIGN KEY (`rid`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=333 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of menu_role
-- ----------------------------
INSERT INTO `menu_role` VALUES ('258', '9', '6');
INSERT INTO `menu_role` VALUES ('259', '10', '6');
INSERT INTO `menu_role` VALUES ('264', '15', '1');
INSERT INTO `menu_role` VALUES ('265', '16', '1');
INSERT INTO `menu_role` VALUES ('266', '17', '1');
INSERT INTO `menu_role` VALUES ('267', '18', '1');
INSERT INTO `menu_role` VALUES ('268', '14', '1');
INSERT INTO `menu_role` VALUES ('284', '31', '1');
INSERT INTO `menu_role` VALUES ('286', '9', '2');
INSERT INTO `menu_role` VALUES ('289', '37', '1');
INSERT INTO `menu_role` VALUES ('290', '38', '1');
INSERT INTO `menu_role` VALUES ('291', '40', '1');
INSERT INTO `menu_role` VALUES ('292', '45', '2');
INSERT INTO `menu_role` VALUES ('293', '46', '11');
INSERT INTO `menu_role` VALUES ('294', '47', '11');
INSERT INTO `menu_role` VALUES ('295', '48', '11');
INSERT INTO `menu_role` VALUES ('296', '49', '11');
INSERT INTO `menu_role` VALUES ('297', '50', '11');
INSERT INTO `menu_role` VALUES ('298', '51', '11');
INSERT INTO `menu_role` VALUES ('299', '52', '11');
INSERT INTO `menu_role` VALUES ('300', '56', '9');
INSERT INTO `menu_role` VALUES ('301', '57', '9');
INSERT INTO `menu_role` VALUES ('302', '58', '9');
INSERT INTO `menu_role` VALUES ('303', '59', '9');
INSERT INTO `menu_role` VALUES ('304', '60', '9');
INSERT INTO `menu_role` VALUES ('305', '61', '9');
INSERT INTO `menu_role` VALUES ('306', '62', '8');
INSERT INTO `menu_role` VALUES ('307', '63', '8');
INSERT INTO `menu_role` VALUES ('309', '65', '1');
INSERT INTO `menu_role` VALUES ('310', '67', '1');
INSERT INTO `menu_role` VALUES ('311', '68', '1');
INSERT INTO `menu_role` VALUES ('312', '72', '10');
INSERT INTO `menu_role` VALUES ('313', '53', '8');
INSERT INTO `menu_role` VALUES ('314', '54', '8');
INSERT INTO `menu_role` VALUES ('315', '55', '9');
INSERT INTO `menu_role` VALUES ('316', '73', '3');
INSERT INTO `menu_role` VALUES ('317', '74', '11');
INSERT INTO `menu_role` VALUES ('318', '69', '11');
INSERT INTO `menu_role` VALUES ('319', '70', '11');
INSERT INTO `menu_role` VALUES ('321', '75', '3');
INSERT INTO `menu_role` VALUES ('322', '76', '11');
INSERT INTO `menu_role` VALUES ('323', '76', '7');
INSERT INTO `menu_role` VALUES ('324', '72', '8');
INSERT INTO `menu_role` VALUES ('325', '71', '8');
INSERT INTO `menu_role` VALUES ('326', '71', '10');
INSERT INTO `menu_role` VALUES ('327', '77', '1');
INSERT INTO `menu_role` VALUES ('328', '78', '1');
INSERT INTO `menu_role` VALUES ('329', '79', '1');
INSERT INTO `menu_role` VALUES ('330', '80', '4');
INSERT INTO `menu_role` VALUES ('331', '83', '1');
INSERT INTO `menu_role` VALUES ('332', '84', '1');

-- ----------------------------
-- Table structure for paper
-- ----------------------------
DROP TABLE IF EXISTS `paper`;
CREATE TABLE `paper` (
  `ID` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `studentID` int DEFAULT NULL COMMENT '学号',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '论文题目',
  `year` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '出版年',
  `month` int DEFAULT NULL COMMENT '出版月',
  `author` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '作者列表',
  `rank` int DEFAULT NULL COMMENT '排名',
  `total` int DEFAULT NULL COMMENT '总人数',
  `point` int DEFAULT NULL COMMENT '积分',
  `no_score` int DEFAULT NULL,
  `publicationID` int DEFAULT NULL COMMENT '刊物id',
  `pubPage` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '页码',
  `state` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'commit' COMMENT '状态',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `publication_paper` (`publicationID`) USING BTREE,
  CONSTRAINT `publication_paper` FOREIGN KEY (`publicationID`) REFERENCES `publication` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=6117 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of paper
-- ----------------------------
INSERT INTO `paper` VALUES ('6095', '2086', '1', '2022', '1', '卢三', '1', '1', '12', null, '1', '1-2', 'tea_pass', '');
INSERT INTO `paper` VALUES ('6096', '2086', '2', '2022', '1', '卢三;李四', '1', '2', '12', null, '3', '1-20', 'adm_pass', '');
INSERT INTO `paper` VALUES ('6097', '2086', '3', '2022', '2', '卢三', '1', '1', '12', null, '2', '1-3', 'commit', 'F:\\Rate\\upload/Pruning_Survey_MLA21.pdf');
INSERT INTO `paper` VALUES ('6098', '2086', '4', '2022', '4', '卢三', '1', '1', '12', null, '2', '2-3', 'tea_reject', '');
INSERT INTO `paper` VALUES ('6099', '2086', '5', '2022', '2', '卢三;zz', '1', '2', '12', null, '5', '2-3', 'adm_reject', '/Users/luyiru/Desktop/研究生网站开发/Rate/work2/rate/upload/测试.docx');
INSERT INTO `paper` VALUES ('6100', '2086', '6', '2022', '2', '卢三;', '1', '2', '12', null, '4', '2-22', 'adm_pass', '/Users/luyiru/Desktop/研究生网站开发/Rate/work2/rate/upload/知识产权作业.pdf');
INSERT INTO `paper` VALUES ('6103', '2086', '撒大苏打', '2023', '2', '卢三', '1', '1', '12', null, '60', '1-121', 'commit', 'F:\\Rate\\upload/6289e6dccfb7a.jpg');
INSERT INTO `paper` VALUES ('6104', '2086', '撒大苏打', '2023', '2', '卢三', '1', '1', '12', null, '60', '1-121', 'commit', 'F:\\Rate\\upload/shufflenet.pdf');
INSERT INTO `paper` VALUES ('6106', '2086', '撒大苏打', '2023', '2', '卢三', '1', '1', '12', null, '60', '1-121', 'commit', 'F:\\Rate\\upload/shufflenet.pdf');
INSERT INTO `paper` VALUES ('6107', '2086', '撒大苏打', '2023', '2', '卢三', '1', '1', '12', null, '60', '1-121', 'commit', 'F:\\Rate\\upload/shufflenet.pdf');
INSERT INTO `paper` VALUES ('6108', '2086', '撒大苏打', '2023', '2', '卢三', '1', '1', '12', null, '60', '1-121', 'commit', 'F:\\Rate\\upload/shufflenet.pdf');
INSERT INTO `paper` VALUES ('6109', '2086', '撒大苏打', '2023', '2', '卢三', '1', '1', '12', null, '60', '1-121', 'commit', 'F:\\Rate\\upload/shufflenet.pdf');
INSERT INTO `paper` VALUES ('6110', '2086', '撒大苏打', '2023', '2', '卢三', '1', '1', '12', null, '60', '1-121', 'commit', 'F:\\Rate\\upload/shufflenet.pdf');
INSERT INTO `paper` VALUES ('6111', '2086', '阿斯顿阿萨v的', '2023', '1', '卢三', '1', '1', '12', null, '3', '1-121', 'commit', 'F:\\Rate\\upload/2201-2222738-张亚魁.pdf');
INSERT INTO `paper` VALUES ('6112', '2086', '阿斯顿阿萨v的', '2023', '1', '卢三', '1', '1', '12', null, '3', '1-121', 'commit', 'F:\\Rate\\upload/2201-2222738-张亚魁.pdf');
INSERT INTO `paper` VALUES ('6113', '2086', '阿斯顿阿萨v的', '2023', '1', '卢三', '1', '1', '12', null, '3', '1-121', 'commit', 'F:\\Rate\\upload/2201-2222738-张亚魁.pdf');
INSERT INTO `paper` VALUES ('6114', '2086', '阿斯顿阿萨v的', '2023', '1', '卢三', '1', '1', '12', null, '3', '1-121', 'commit', 'F:\\Rate\\upload/2201-2222738-张亚魁.pdf');
INSERT INTO `paper` VALUES ('6115', '2086', '阿斯顿阿萨v的', '2023', '1', '卢三', '1', '1', '12', null, '3', '1-121', 'commit', 'F:\\Rate\\upload/2201-2222738-张亚魁.pdf');
INSERT INTO `paper` VALUES ('6116', '2086', '发电公司给', '2023', '2', '卢三', '1', '1', '12', null, '3', '1-121', 'commit', 'F:\\Rate\\upload/L181310122-段煜峰.pdf');

-- ----------------------------
-- Table structure for papercomment
-- ----------------------------
DROP TABLE IF EXISTS `papercomment`;
CREATE TABLE `papercomment` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `thesisID` int NOT NULL COMMENT '毕业论文或设计的ID',
  `dateStu` date DEFAULT NULL COMMENT '学生的提交的time',
  `dateTea` date DEFAULT NULL COMMENT '老师评价的time',
  `preSum` varchar(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上个阶段工作完成情况',
  `nextPlan` varchar(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '下次工作安排',
  `tutorComment` varchar(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '导师评价',
  `num` int NOT NULL COMMENT '在单张表单中是第几次',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `thesisID` (`thesisID`) USING BTREE,
  CONSTRAINT `thesisID` FOREIGN KEY (`thesisID`) REFERENCES `thesis` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=746 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of papercomment
-- ----------------------------
INSERT INTO `papercomment` VALUES ('745', '1', '2023-03-08', null, '岁的法国地方是否', '吧v你发给更换', '', '1');

-- ----------------------------
-- Table structure for paper_oper
-- ----------------------------
DROP TABLE IF EXISTS `paper_oper`;
CREATE TABLE `paper_oper` (
  `ID` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `operatorRole` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作人角色',
  `operatorID` int DEFAULT NULL COMMENT '操作人ID',
  `operatorName` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作人姓名',
  `paperID` int DEFAULT NULL COMMENT '论文ID',
  `paperName` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '论文名称',
  `pubID` int DEFAULT NULL COMMENT '出版物ID',
  `pubName` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '出版物名称',
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `operation` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作',
  `state` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '状态',
  `remark` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '备注',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=217 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of paper_oper
-- ----------------------------
INSERT INTO `paper_oper` VALUES ('178', 'student', '2086', '张三', '6095', '1', '1', '《中国社会科学》', '2022-12-12 16:40:53', '提交论文', 'commit', '');
INSERT INTO `paper_oper` VALUES ('179', 'student', '2086', '张三', '6096', '2', '3', '《中共党史研究》', '2022-12-12 16:41:35', '提交论文', 'commit', '');
INSERT INTO `paper_oper` VALUES ('180', 'student', '2086', '张三', '6097', '3', '2', '《International Conference on Data Engineering(ICDE)》', '2022-12-12 16:41:49', '提交论文', 'commit', '');
INSERT INTO `paper_oper` VALUES ('181', 'student', '2086', '张三', '6098', '4', '2', '《International Conference on Data Engineering(ICDE)》', '2022-12-12 16:42:03', '提交论文', 'commit', '');
INSERT INTO `paper_oper` VALUES ('182', 'student', '2086', '张三', '6099', '5', '5', '刊物全称2', '2022-12-12 16:42:22', '提交论文', 'commit', '');
INSERT INTO `paper_oper` VALUES ('183', 'teacher', '1043', '刘哈哈', '6099', '5', '5', '刊物全称2', '2022-12-12 16:42:44', '教师审核通过', 'tea_pass', '');
INSERT INTO `paper_oper` VALUES ('184', 'teacher', '1043', '刘哈哈', '6095', '1', '1', '《中国社会科学》', '2022-12-12 16:42:46', '教师审核通过', 'tea_pass', '');
INSERT INTO `paper_oper` VALUES ('204', 'teacher', '1043', '刘哈哈', '6098', '4', '2', '《International Conference on Data Engineering(ICDE)》', '2022-12-12 17:27:19', '教师驳回', 'tea_reject', '');
INSERT INTO `paper_oper` VALUES ('205', 'admin', '2', 'wow1', '6099', '5', '5', '刊物全称2', '2022-12-12 17:27:37', '管理员驳回', 'adm_reject', '');
INSERT INTO `paper_oper` VALUES ('206', 'admin', '2', 'wow1', '6096', '2', '3', '《中共党史研究》', '2022-12-12 17:27:40', '管理员审核通过', 'adm_pass', '');
INSERT INTO `paper_oper` VALUES ('207', 'student', '2086', '张三', '6100', '6', '4', '刊物全称1', '2022-12-13 20:56:26', '提交论文', 'commit', '');
INSERT INTO `paper_oper` VALUES ('208', 'student', '2098', '卢三', '6101', '666', '1', '《中国社会科学》', '2022-12-26 22:47:49', '提交论文', 'commit', '');
INSERT INTO `paper_oper` VALUES ('209', 'student', '2098', '卢三', '6102', 'asa', '3', '《中共党史研究》', '2022-12-26 23:00:26', '提交论文', 'commit', '');
INSERT INTO `paper_oper` VALUES ('210', 'student', '2098', '卢三', '6103', 'asas', '1', '《中国社会科学》', '2022-12-26 23:08:15', '提交论文', 'commit', '');
INSERT INTO `paper_oper` VALUES ('211', 'student', '2098', '卢三', '6104', 'sss', '2', '《International Conference on Data Engineering(ICDE)》', '2022-12-26 23:08:54', '提交论文', 'commit', '');
INSERT INTO `paper_oper` VALUES ('212', 'student', '2098', '卢三', '6105', 'dsdsa', '1', '《中国社会科学》', '2022-12-26 23:09:59', '提交论文', 'commit', '');
INSERT INTO `paper_oper` VALUES ('213', 'admin', '34', '龚三', '6100', '6', '4', '刊物全称1', '2022-12-26 23:14:19', '管理员审核通过', 'adm_pass', '');
INSERT INTO `paper_oper` VALUES ('214', 'student', '2086', '卢三', '6101', '7', '2', '《International Conference on Data Engineering(ICDE)》', '2023-02-17 15:18:49', '提交论文', 'commit', '');
INSERT INTO `paper_oper` VALUES ('215', 'student', '2086', '卢三', '6102', '7', '2', '《International Conference on Data Engineering(ICDE)》', '2023-02-17 15:18:51', '提交论文', 'commit', '');
INSERT INTO `paper_oper` VALUES ('216', 'student', '2086', '卢三', '6116', '发电公司给', '3', '《中共党史研究》', '2023-03-05 12:49:00', '提交论文', 'commit', '');

-- ----------------------------
-- Table structure for participants
-- ----------------------------
DROP TABLE IF EXISTS `participants`;
CREATE TABLE `participants` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `activityID` int DEFAULT NULL,
  `groupID` int DEFAULT NULL,
  `groupID2` int DEFAULT NULL COMMENT '秘书分组后的ID',
  `studentID` int DEFAULT NULL,
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `school` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '本科学校',
  `major` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '本科专业',
  `sex` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '性别',
  `examscore` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '初试成绩',
  `displaySequence` int DEFAULT NULL,
  `score` double(10,2) DEFAULT NULL COMMENT '可以外键scores',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `partidforp` (`studentID`) USING BTREE,
  KEY `activity` (`activityID`) USING BTREE,
  KEY `activityID` (`activityID`,`ID`) USING BTREE,
  CONSTRAINT `participants_ibfk_1` FOREIGN KEY (`activityID`) REFERENCES `activities` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `participants_ibfk_2` FOREIGN KEY (`studentID`) REFERENCES `student` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of participants
-- ----------------------------
INSERT INTO `participants` VALUES ('120', '15', '17', null, '2071', '102551210004479', null, null, null, null, '2', '12371.10');
INSERT INTO `participants` VALUES ('121', '15', '17', null, '2072', '102551210002476', null, null, null, null, '4', '44.43');
INSERT INTO `participants` VALUES ('122', '15', '17', null, '2073', '102551210008659', null, null, null, null, '3', '43.10');
INSERT INTO `participants` VALUES ('123', '15', '17', null, '2074', '102551210002424', null, null, null, null, '5', '53.10');
INSERT INTO `participants` VALUES ('124', '15', '17', null, '2075', '102551210003963', null, null, null, null, '1', '249.10');
INSERT INTO `participants` VALUES ('125', '15', '18', null, '2076', '102551210008999', null, null, null, null, '1', '144.43');
INSERT INTO `participants` VALUES ('126', '15', '18', null, '2077', '102551210000145', null, null, null, null, '4', '136.07');
INSERT INTO `participants` VALUES ('127', '15', '18', null, '2078', '102551210009175', null, null, null, null, '3', '215.10');
INSERT INTO `participants` VALUES ('128', '15', '18', null, '2079', '102551210004782', null, null, null, null, '2', '250.40');
INSERT INTO `participants` VALUES ('129', '15', '18', null, '2080', '102551210008226', null, null, null, null, '1', '285.73');
INSERT INTO `participants` VALUES ('130', '15', '17', null, '2081', '181310000', null, null, null, null, '1', '69879.00');
INSERT INTO `participants` VALUES ('131', '15', '17', null, '2082', '181310001', null, null, null, null, '1', null);
INSERT INTO `participants` VALUES ('132', '15', '18', null, '2083', '181310002', null, null, null, null, '1', null);
INSERT INTO `participants` VALUES ('133', '15', '17', null, '2084', '1813100008', null, null, null, null, '8', null);
INSERT INTO `participants` VALUES ('139', '26', '24', null, '2086', '181310000', null, null, null, null, '1', '96.67');
INSERT INTO `participants` VALUES ('140', '26', '24', null, '2087', '181310002', null, null, null, null, '2', '88.67');
INSERT INTO `participants` VALUES ('141', '26', '24', null, '2088', '181310003', null, null, null, null, '3', '84.67');
INSERT INTO `participants` VALUES ('142', '26', '25', null, '2089', '181310004', null, null, null, null, '1', '89.00');
INSERT INTO `participants` VALUES ('143', '26', '25', null, '2090', '181310005', null, null, null, null, '2', '83.00');
INSERT INTO `participants` VALUES ('144', '26', '25', null, '2091', '181310006', null, null, null, null, '3', '78.00');
INSERT INTO `participants` VALUES ('145', '26', '26', null, '2092', '181310007', null, null, null, null, '1', '82.25');
INSERT INTO `participants` VALUES ('146', '26', '26', null, '2093', '181310008', null, null, null, null, '2', '80.33');
INSERT INTO `participants` VALUES ('147', '26', '26', null, '2094', '181310009', null, null, null, null, '3', '78.33');
INSERT INTO `participants` VALUES ('148', '15', null, null, '2086', '181310000', null, null, null, null, '1', null);
INSERT INTO `participants` VALUES ('149', '15', '17', null, '2095', '191310000', null, null, null, null, '1', null);
INSERT INTO `participants` VALUES ('150', '15', '17', null, '2096', '1991310000', null, null, null, null, null, '21.00');
INSERT INTO `participants` VALUES ('151', '15', '17', null, '2098', null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for patentresult
-- ----------------------------
DROP TABLE IF EXISTS `patentresult`;
CREATE TABLE `patentresult` (
  `ID` int NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `time` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `rank` int DEFAULT NULL,
  `total` int DEFAULT NULL,
  `rights` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `indicaterID` int NOT NULL,
  `state` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `patent` (`indicaterID`) USING BTREE,
  CONSTRAINT `patentresult_ibfk_1` FOREIGN KEY (`indicaterID`) REFERENCES `indicater` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of patentresult
-- ----------------------------
INSERT INTO `patentresult` VALUES ('11114444', '青蒿素', '国家级', null, null, null, null, '3015', '');

-- ----------------------------
-- Table structure for program
-- ----------------------------
DROP TABLE IF EXISTS `program`;
CREATE TABLE `program` (
  `ID` int NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `indicatorID` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `program` (`indicatorID`) USING BTREE,
  CONSTRAINT `program_ibfk_1` FOREIGN KEY (`indicatorID`) REFERENCES `indicator` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of program
-- ----------------------------
INSERT INTO `program` VALUES ('1', '国家自然科学基金面上项目(研究期限三年及以上)', '7');
INSERT INTO `program` VALUES ('2', '国家杰出青年科学基金项目', '7');
INSERT INTO `program` VALUES ('3', '国家社会科学基金重大项目', '8');
INSERT INTO `program` VALUES ('4', '教育部哲学社会科学研究重大课题攻关项目', '8');
INSERT INTO `program` VALUES ('5', '国家社会科学基金项目(含重点项目、一般项目、青年项目、后期资助项目、中华学术外译项目，以及教育学、艺术学和军事学单列学科项目)', '9');

-- ----------------------------
-- Table structure for programresult
-- ----------------------------
DROP TABLE IF EXISTS `programresult`;
CREATE TABLE `programresult` (
  `ID` int NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `source` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sub` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `head` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `division` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `year` int DEFAULT NULL,
  `way` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `starttime` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `endtime` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `programID` int NOT NULL,
  `state` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `programID` (`programID`) USING BTREE,
  CONSTRAINT `programresult_ibfk_1` FOREIGN KEY (`programID`) REFERENCES `program` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of programresult
-- ----------------------------
INSERT INTO `programresult` VALUES ('21432134', '学生信息管理系统', null, null, null, null, null, null, null, null, '111111', '');

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `ID` int NOT NULL,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `source` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sub` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `head` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `division` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `year` int DEFAULT NULL,
  `way` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `starttime` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `endtime` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `projectTypeID` int NOT NULL,
  `state` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `projectTypeID` (`projectTypeID`) USING BTREE,
  CONSTRAINT `project_ibfk_1` FOREIGN KEY (`projectTypeID`) REFERENCES `projecttype` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of project
-- ----------------------------
INSERT INTO `project` VALUES ('1', '学生信息管理系统', null, null, null, null, null, null, null, null, '111111', '');

-- ----------------------------
-- Table structure for projecttype
-- ----------------------------
DROP TABLE IF EXISTS `projecttype`;
CREATE TABLE `projecttype` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `indicatorId` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `projectType_Indi` (`indicatorId`) USING BTREE,
  CONSTRAINT `projectType_Indi` FOREIGN KEY (`indicatorId`) REFERENCES `indicator` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of projecttype
-- ----------------------------

-- ----------------------------
-- Table structure for properties
-- ----------------------------
DROP TABLE IF EXISTS `properties`;
CREATE TABLE `properties` (
  `host` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `id` int NOT NULL,
  `sendHost` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of properties
-- ----------------------------
INSERT INTO `properties` VALUES ('imap.126.com', 'ratemail@126.com', 'IOLMDKJXAQJQUAMN', '1', 'smtp.126.com');
INSERT INTO `properties` VALUES ('imap.163.com', '2222738@mail.dhu.edu.cn', 'efYRe5Nf414VTAz4', '2', 'smtp.163.com');
INSERT INTO `properties` VALUES ('imap.163.com', 'yakuizhang1111@163.com', 'MWCQFMLCWCNNPIOF', '3', 'smtp.163.com');
INSERT INTO `properties` VALUES ('imap.163.com', 'yakuizhang1111@163.com', 'MWCQFMLCWCNNPIOF', '4', 'smtp.163.com');
INSERT INTO `properties` VALUES ('imap.126.com', 'sw21706091085@126.com', 'CKJRUHJRNBHADFIJ', '5', 'smtp.126.com');

-- ----------------------------
-- Table structure for publication
-- ----------------------------
DROP TABLE IF EXISTS `publication`;
CREATE TABLE `publication` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `abbr` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `publisher` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `url` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `level` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `indicatorID` int NOT NULL,
  `year` int DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `publication` (`indicatorID`) USING BTREE,
  CONSTRAINT `publication_ibfk_1` FOREIGN KEY (`indicatorID`) REFERENCES `indicator` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of publication
-- ----------------------------
INSERT INTO `publication` VALUES ('1', '《中国社会科学》', '简称1', '出版社1', '网址1', '级别1', '4', '2022');
INSERT INTO `publication` VALUES ('2', '《International Conference on Data Engineering(ICDE)》', null, null, null, null, '5', '2022');
INSERT INTO `publication` VALUES ('3', '《中共党史研究》', '简称2', '出版社2', '网址2', '级别2', '4', '2023');
INSERT INTO `publication` VALUES ('4', '刊物全称1', '刊物简称1', '出版社1', '网址1', '收录级别1', '4', '2023');
INSERT INTO `publication` VALUES ('5', '刊物全称2', '刊物简称2', '出版社2', '网址2', '收录级别2', '4', '2023');
INSERT INTO `publication` VALUES ('6', '刊物全称3', '刊物简称3', '出版社3', '网址3', '收录级别3', '4', '2023');
INSERT INTO `publication` VALUES ('7', '刊物全称4', '刊物简称4', '出版社4', '网址4', '收录级别4', '4', '2022');
INSERT INTO `publication` VALUES ('8', '刊物全称5', '刊物简称5', '出版社5', '网址5', '收录级别5', '4', '2021');
INSERT INTO `publication` VALUES ('9', '1', '刊物简称6', '出版社6', '网址6', '收录级别6', '4', '2023');
INSERT INTO `publication` VALUES ('10', '刊物全称7', '刊物简称x', '出版社x', '网址x', '收录级别x', '4', '2023');
INSERT INTO `publication` VALUES ('11', '刊物全称8', '刊物简称x', '出版社x', '网址x', '收录级别x', '4', '2021');
INSERT INTO `publication` VALUES ('12', '刊物全称a', '刊物简称x', '出版社x', '网址x', '收录级别x', '4', '2022');
INSERT INTO `publication` VALUES ('13', '刊物全称abc', '刊物简称x', '出版社x', '网址x', '收录级别x', '4', '2020');
INSERT INTO `publication` VALUES ('14', '2', '刊物简称x', '出版社x', '网址x', '收录级别x', '4', '2019');
INSERT INTO `publication` VALUES ('15', '刊物全称abcde', '刊物简称x', '出版社x', '网址x', '收录级别x', '4', '2022');
INSERT INTO `publication` VALUES ('16', '刊物全称abcdef', '刊物简称x', '出版社x', '网址x', '收录级别x', '4', '2023');
INSERT INTO `publication` VALUES ('18', '《International Conference on Data Engineering(ICDE)》', null, null, null, null, '5', '2001');
INSERT INTO `publication` VALUES ('59', '《中共党史研究》', '你的刊物简称', '你的刊物出版社', '你的刊物网址', '收录级别1;级别2;（请用分号隔开）', '5', '2023');
INSERT INTO `publication` VALUES ('60', 'test2', 'sad', 'sd', 'dsd', 'dsa', '5', '2023');
INSERT INTO `publication` VALUES ('61', 'test3', 'sd', 'sdfsfd', 'fddf', 'saef', '5', '2023');
INSERT INTO `publication` VALUES ('62', 'test4', 'sad', 'dsa', 'daw', 'asd', '5', '2023');
INSERT INTO `publication` VALUES ('63', 'test5', 'sd', 'dsa', 'sd', 'sd', '5', '2023');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `nameZh` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'ROLE_manager', '管理员');
INSERT INTO `role` VALUES ('2', 'ROLE_personnel', '人事专员');
INSERT INTO `role` VALUES ('3', 'ROLE_expert', '专家');
INSERT INTO `role` VALUES ('4', 'ROLE_secretary', '秘书');
INSERT INTO `role` VALUES ('5', 'ROLE_performance', '薪酬绩效主管');
INSERT INTO `role` VALUES ('6', 'ROLE_admin', '系统管理员');
INSERT INTO `role` VALUES ('7', 'ROLE_participants', '选手');
INSERT INTO `role` VALUES ('8', 'ROLE_teacher_under', '本科生导师');
INSERT INTO `role` VALUES ('9', 'ROLE_teacher_post', '研究生导师');
INSERT INTO `role` VALUES ('10', 'ROLE_student_under', '本科生');
INSERT INTO `role` VALUES ('11', 'ROLE_student_post', '研究生');
INSERT INTO `role` VALUES ('12', 'ROLE_tmp', '临时人员');

-- ----------------------------
-- Table structure for scoreitem
-- ----------------------------
DROP TABLE IF EXISTS `scoreitem`;
CREATE TABLE `scoreitem` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `activityID` int DEFAULT NULL,
  `name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `score` double(10,2) DEFAULT NULL,
  `coef` double DEFAULT NULL,
  `comment` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `byExpert` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `activityID` (`activityID`) USING BTREE,
  KEY `activityID_2` (`activityID`,`name`) USING BTREE,
  CONSTRAINT `scoreitem_ibfk_1` FOREIGN KEY (`activityID`) REFERENCES `activities` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of scoreitem
-- ----------------------------
INSERT INTO `scoreitem` VALUES ('45', '15', '学校来源', '45.00', '1', '双一流（30-45，默认45）、有推免资质高校：（20-35，默认30）其他（15-30，默认15）', '1');
INSERT INTO `scoreitem` VALUES ('47', '15', '专业英语翻译', '30.00', '1', null, '1');
INSERT INTO `scoreitem` VALUES ('48', '15', '代码讲解', '40.00', '1', '选第2题上限40分、选第1题上限30分', '1');
INSERT INTO `scoreitem` VALUES ('50', '15', '程序填空', '10.00', '1', null, '1');
INSERT INTO `scoreitem` VALUES ('51', '15', '个人介绍与回答问题', '25.00', '1', null, '1');
INSERT INTO `scoreitem` VALUES ('54', '15', '总分', '100.00', '1', null, '1');
INSERT INTO `scoreitem` VALUES ('68', '19', '总分', '100.00', '1', null, '1');
INSERT INTO `scoreitem` VALUES ('73', '19', '人', '100.00', '1', null, '1');
INSERT INTO `scoreitem` VALUES ('75', '22', '总分', '100.00', '1', null, '1');
INSERT INTO `scoreitem` VALUES ('76', '22', '2222称', '100.00', '1', null, '1');
INSERT INTO `scoreitem` VALUES ('82', '22', '啊哈', '123.00', '1', null, '0');
INSERT INTO `scoreitem` VALUES ('83', '22', '发', '5.00', '1', null, '0');
INSERT INTO `scoreitem` VALUES ('84', '15', '材料', '100.00', '1', null, '0');
INSERT INTO `scoreitem` VALUES ('85', '15', '材料again', '100.00', '1', null, '0');
INSERT INTO `scoreitem` VALUES ('86', '15', '测试', '100.00', '1', null, '1');
INSERT INTO `scoreitem` VALUES ('87', '16', '123', '100.00', '1', null, '1');
INSERT INTO `scoreitem` VALUES ('89', '24', '总分', '100.00', '1', null, '1');
INSERT INTO `scoreitem` VALUES ('90', '25', '总分', '100.00', '1', null, '1');
INSERT INTO `scoreitem` VALUES ('92', '26', '活动总分', '100.00', '1', null, '1');
INSERT INTO `scoreitem` VALUES ('93', '26', '基准分数', '30.00', '1', null, '0');
INSERT INTO `scoreitem` VALUES ('94', '26', '程序填空', '20.00', '1', '选第2题上限20分、选第1题上限10分', '1');
INSERT INTO `scoreitem` VALUES ('95', '26', '个人介绍与回答问题', '50.00', '1', null, '1');
INSERT INTO `scoreitem` VALUES ('96', '27', '总分', '100.00', '1', null, '1');
INSERT INTO `scoreitem` VALUES ('97', '28', '总分', '100.00', '1', null, '1');

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
  CONSTRAINT `scores_ibfk_1` FOREIGN KEY (`activityID`) REFERENCES `activities` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `scores_ibfk_2` FOREIGN KEY (`scoreItemID`) REFERENCES `scoreitem` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `scores_ibfk_3` FOREIGN KEY (`participantID`) REFERENCES `participants` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `scores_ibfk_4` FOREIGN KEY (`expertID`) REFERENCES `teacher` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=738 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of scores
-- ----------------------------
INSERT INTO `scores` VALUES ('424', null, '15', '120', '45', '11.10');
INSERT INTO `scores` VALUES ('425', null, '15', '121', '45', '21.10');
INSERT INTO `scores` VALUES ('426', null, '15', '122', '45', '31.10');
INSERT INTO `scores` VALUES ('427', null, '15', '123', '45', '41.10');
INSERT INTO `scores` VALUES ('428', null, '15', '124', '45', '51.10');
INSERT INTO `scores` VALUES ('429', null, '15', '125', '45', '11.10');
INSERT INTO `scores` VALUES ('430', null, '15', '126', '45', '21.10');
INSERT INTO `scores` VALUES ('431', null, '15', '127', '45', '31.10');
INSERT INTO `scores` VALUES ('432', null, '15', '128', '45', '41.10');
INSERT INTO `scores` VALUES ('433', null, '15', '129', '45', '51.10');
INSERT INTO `scores` VALUES ('434', '1027', '15', '129', '45', '51.00');
INSERT INTO `scores` VALUES ('435', '1027', '15', '129', '47', '52.00');
INSERT INTO `scores` VALUES ('436', '1027', '15', '129', '48', '53.00');
INSERT INTO `scores` VALUES ('437', '1027', '15', '129', '50', '54.00');
INSERT INTO `scores` VALUES ('438', '1027', '15', '129', '51', '55.00');
INSERT INTO `scores` VALUES ('439', '1027', '15', '129', '54', '265.00');
INSERT INTO `scores` VALUES ('440', '1027', '15', '128', '45', '41.00');
INSERT INTO `scores` VALUES ('441', '1027', '15', '128', '47', '42.00');
INSERT INTO `scores` VALUES ('442', '1027', '15', '128', '48', '43.00');
INSERT INTO `scores` VALUES ('443', '1027', '15', '128', '50', '44.00');
INSERT INTO `scores` VALUES ('444', '1027', '15', '128', '51', '45.00');
INSERT INTO `scores` VALUES ('445', '1027', '15', '128', '54', '215.00');
INSERT INTO `scores` VALUES ('446', '1027', '15', '127', '45', '31.10');
INSERT INTO `scores` VALUES ('447', '1027', '15', '127', '47', '32.00');
INSERT INTO `scores` VALUES ('448', '1027', '15', '127', '48', '33.00');
INSERT INTO `scores` VALUES ('449', '1027', '15', '127', '50', '34.00');
INSERT INTO `scores` VALUES ('450', '1027', '15', '127', '51', '35.00');
INSERT INTO `scores` VALUES ('451', '1027', '15', '127', '54', '165.10');
INSERT INTO `scores` VALUES ('452', '1027', '15', '126', '45', '21.10');
INSERT INTO `scores` VALUES ('453', '1027', '15', '126', '47', '22.00');
INSERT INTO `scores` VALUES ('454', '1027', '15', '126', '48', '23.00');
INSERT INTO `scores` VALUES ('455', '1027', '15', '126', '50', '24.00');
INSERT INTO `scores` VALUES ('456', '1027', '15', '126', '51', '25.00');
INSERT INTO `scores` VALUES ('457', '1027', '15', '126', '54', '115.10');
INSERT INTO `scores` VALUES ('458', '1027', '15', '125', '45', '11.10');
INSERT INTO `scores` VALUES ('459', '1027', '15', '125', '47', '12.00');
INSERT INTO `scores` VALUES ('460', '1027', '15', '125', '48', '13.00');
INSERT INTO `scores` VALUES ('461', '1027', '15', '125', '50', '14.00');
INSERT INTO `scores` VALUES ('462', '1027', '15', '125', '51', '15.00');
INSERT INTO `scores` VALUES ('463', '1027', '15', '125', '54', '65.10');
INSERT INTO `scores` VALUES ('464', '1028', '15', '129', '45', '51.10');
INSERT INTO `scores` VALUES ('465', '1028', '15', '129', '47', '56.00');
INSERT INTO `scores` VALUES ('466', '1028', '15', '129', '48', '57.00');
INSERT INTO `scores` VALUES ('467', '1028', '15', '129', '50', '58.00');
INSERT INTO `scores` VALUES ('468', '1028', '15', '129', '51', '59.00');
INSERT INTO `scores` VALUES ('469', '1028', '15', '129', '54', '281.10');
INSERT INTO `scores` VALUES ('470', '1028', '15', '128', '45', '41.10');
INSERT INTO `scores` VALUES ('471', '1028', '15', '128', '47', '46.00');
INSERT INTO `scores` VALUES ('472', '1028', '15', '128', '48', '47.00');
INSERT INTO `scores` VALUES ('473', '1028', '15', '128', '50', '48.00');
INSERT INTO `scores` VALUES ('474', '1028', '15', '128', '51', '49.00');
INSERT INTO `scores` VALUES ('475', '1028', '15', '128', '54', '231.10');
INSERT INTO `scores` VALUES ('476', '1028', '15', '127', '45', '31.10');
INSERT INTO `scores` VALUES ('477', '1028', '15', '127', '47', '36.00');
INSERT INTO `scores` VALUES ('478', '1028', '15', '127', '48', '37.00');
INSERT INTO `scores` VALUES ('479', '1028', '15', '127', '50', '38.00');
INSERT INTO `scores` VALUES ('480', '1028', '15', '127', '51', '39.00');
INSERT INTO `scores` VALUES ('481', '1028', '15', '127', '54', '181.10');
INSERT INTO `scores` VALUES ('482', '1028', '15', '125', '45', '11.10');
INSERT INTO `scores` VALUES ('483', '1028', '15', '125', '47', '16.00');
INSERT INTO `scores` VALUES ('484', '1028', '15', '125', '48', '17.00');
INSERT INTO `scores` VALUES ('485', '1028', '15', '125', '50', '18.00');
INSERT INTO `scores` VALUES ('486', '1028', '15', '125', '51', '19.00');
INSERT INTO `scores` VALUES ('487', '1028', '15', '125', '54', '81.10');
INSERT INTO `scores` VALUES ('488', '1026', '15', '125', '45', '11.10');
INSERT INTO `scores` VALUES ('489', '1026', '15', '125', '47', '84.00');
INSERT INTO `scores` VALUES ('490', '1026', '15', '125', '48', '74.00');
INSERT INTO `scores` VALUES ('491', '1026', '15', '126', '45', '21.10');
INSERT INTO `scores` VALUES ('492', '1026', '15', '125', '50', '64.00');
INSERT INTO `scores` VALUES ('493', '1026', '15', '126', '47', '83.00');
INSERT INTO `scores` VALUES ('494', '1026', '15', '125', '51', '54.00');
INSERT INTO `scores` VALUES ('495', '1026', '15', '126', '48', '73.00');
INSERT INTO `scores` VALUES ('496', '1026', '15', '125', '54', '287.10');
INSERT INTO `scores` VALUES ('497', '1026', '15', '126', '50', '63.00');
INSERT INTO `scores` VALUES ('498', '1026', '15', '126', '51', '53.00');
INSERT INTO `scores` VALUES ('499', '1026', '15', '126', '54', '293.10');
INSERT INTO `scores` VALUES ('500', '1026', '15', '127', '45', '31.10');
INSERT INTO `scores` VALUES ('501', '1026', '15', '127', '47', '82.00');
INSERT INTO `scores` VALUES ('502', '1026', '15', '127', '48', '72.00');
INSERT INTO `scores` VALUES ('503', '1026', '15', '127', '50', '62.00');
INSERT INTO `scores` VALUES ('504', '1026', '15', '127', '51', '52.00');
INSERT INTO `scores` VALUES ('505', '1026', '15', '127', '54', '299.10');
INSERT INTO `scores` VALUES ('506', '1026', '15', '128', '45', '41.10');
INSERT INTO `scores` VALUES ('507', '1026', '15', '128', '47', '81.00');
INSERT INTO `scores` VALUES ('508', '1026', '15', '128', '48', '71.00');
INSERT INTO `scores` VALUES ('509', '1026', '15', '128', '50', '61.00');
INSERT INTO `scores` VALUES ('510', '1026', '15', '128', '51', '51.00');
INSERT INTO `scores` VALUES ('511', '1026', '15', '129', '45', '51.10');
INSERT INTO `scores` VALUES ('512', '1026', '15', '129', '47', '80.00');
INSERT INTO `scores` VALUES ('513', '1026', '15', '128', '54', '305.10');
INSERT INTO `scores` VALUES ('514', '1026', '15', '129', '48', '70.00');
INSERT INTO `scores` VALUES ('515', '1026', '15', '129', '50', '60.00');
INSERT INTO `scores` VALUES ('516', '1026', '15', '129', '51', '50.00');
INSERT INTO `scores` VALUES ('517', '1026', '15', '129', '54', '311.10');
INSERT INTO `scores` VALUES ('518', '1025', '15', '124', '45', '51.10');
INSERT INTO `scores` VALUES ('519', '1025', '15', '124', '47', '3.00');
INSERT INTO `scores` VALUES ('520', '1025', '15', '124', '48', '3.00');
INSERT INTO `scores` VALUES ('521', '1025', '15', '124', '50', '3.00');
INSERT INTO `scores` VALUES ('522', '1025', '15', '124', '51', '10.00');
INSERT INTO `scores` VALUES ('523', '1025', '15', '124', '54', '515.10');
INSERT INTO `scores` VALUES ('524', '1025', '15', '123', '45', '41.10');
INSERT INTO `scores` VALUES ('525', '1025', '15', '123', '47', '3.00');
INSERT INTO `scores` VALUES ('526', '1025', '15', '123', '48', '3.00');
INSERT INTO `scores` VALUES ('527', '1025', '15', '123', '50', '3.00');
INSERT INTO `scores` VALUES ('528', '1025', '15', '123', '51', '3.00');
INSERT INTO `scores` VALUES ('529', '1025', '15', '123', '54', '53.10');
INSERT INTO `scores` VALUES ('530', '1025', '15', '122', '45', '31.10');
INSERT INTO `scores` VALUES ('531', '1025', '15', '122', '47', '3.00');
INSERT INTO `scores` VALUES ('532', '1025', '15', '122', '48', '3.00');
INSERT INTO `scores` VALUES ('533', '1025', '15', '122', '50', '3.00');
INSERT INTO `scores` VALUES ('534', '1025', '15', '122', '51', '3.00');
INSERT INTO `scores` VALUES ('535', '1025', '15', '122', '54', '43.10');
INSERT INTO `scores` VALUES ('536', '1025', '15', '121', '45', '21.10');
INSERT INTO `scores` VALUES ('537', '1025', '15', '121', '47', '3.00');
INSERT INTO `scores` VALUES ('538', '1025', '15', '121', '48', '3.00');
INSERT INTO `scores` VALUES ('539', '1025', '15', '121', '50', '3.00');
INSERT INTO `scores` VALUES ('540', '1025', '15', '121', '51', '3.00');
INSERT INTO `scores` VALUES ('541', '1025', '15', '121', '54', '33.10');
INSERT INTO `scores` VALUES ('542', '1025', '15', '120', '45', '11.10');
INSERT INTO `scores` VALUES ('543', '1025', '15', '120', '47', '3.00');
INSERT INTO `scores` VALUES ('544', '1025', '15', '120', '48', '3.00');
INSERT INTO `scores` VALUES ('545', '1025', '15', '120', '50', '3.00');
INSERT INTO `scores` VALUES ('546', '1025', '15', '120', '51', '3.00');
INSERT INTO `scores` VALUES ('547', '1025', '15', '120', '54', '12371.10');
INSERT INTO `scores` VALUES ('548', '1024', '15', '124', '45', '51.10');
INSERT INTO `scores` VALUES ('549', '1024', '15', '124', '47', '4.00');
INSERT INTO `scores` VALUES ('550', '1024', '15', '124', '48', '5.00');
INSERT INTO `scores` VALUES ('551', '1024', '15', '124', '50', null);
INSERT INTO `scores` VALUES ('552', '1024', '15', '124', '51', '0.00');
INSERT INTO `scores` VALUES ('553', '1024', '15', '124', '54', null);
INSERT INTO `scores` VALUES ('554', '1024', '15', '123', '45', '41.10');
INSERT INTO `scores` VALUES ('555', '1024', '15', '123', '47', null);
INSERT INTO `scores` VALUES ('556', '1024', '15', '123', '48', '5.00');
INSERT INTO `scores` VALUES ('557', '1024', '15', '123', '50', '5.00');
INSERT INTO `scores` VALUES ('558', '1024', '15', '123', '51', '5.00');
INSERT INTO `scores` VALUES ('559', '1024', '15', '123', '54', '60.10');
INSERT INTO `scores` VALUES ('560', '1023', '15', '124', '45', '51.10');
INSERT INTO `scores` VALUES ('561', '1023', '15', '124', '47', '40.00');
INSERT INTO `scores` VALUES ('562', '1023', '15', '124', '48', null);
INSERT INTO `scores` VALUES ('563', '1023', '15', '124', '50', null);
INSERT INTO `scores` VALUES ('564', '1023', '15', '124', '51', null);
INSERT INTO `scores` VALUES ('565', '1023', '15', '124', '54', null);
INSERT INTO `scores` VALUES ('566', '1024', '15', '121', '45', '21.10');
INSERT INTO `scores` VALUES ('567', '1024', '15', '121', '47', '10.00');
INSERT INTO `scores` VALUES ('568', '1024', '15', '121', '48', '10.00');
INSERT INTO `scores` VALUES ('569', '1024', '15', '121', '50', '30.00');
INSERT INTO `scores` VALUES ('570', '1024', '15', '121', '51', '0.00');
INSERT INTO `scores` VALUES ('571', '1024', '15', '121', '54', '71.10');
INSERT INTO `scores` VALUES ('572', '1023', '15', '121', '45', '21.10');
INSERT INTO `scores` VALUES ('573', '1023', '15', '121', '47', '2.00');
INSERT INTO `scores` VALUES ('574', '1023', '15', '121', '48', null);
INSERT INTO `scores` VALUES ('575', '1023', '15', '121', '50', '0.00');
INSERT INTO `scores` VALUES ('576', '1023', '15', '121', '51', '0.00');
INSERT INTO `scores` VALUES ('577', '1023', '15', '121', '54', '29.10');
INSERT INTO `scores` VALUES ('578', '1022', '15', '124', '45', '51.10');
INSERT INTO `scores` VALUES ('579', '1022', '15', '124', '47', null);
INSERT INTO `scores` VALUES ('580', '1022', '15', '124', '48', '20.00');
INSERT INTO `scores` VALUES ('581', '1022', '15', '124', '50', '20.00');
INSERT INTO `scores` VALUES ('582', '1022', '15', '124', '51', '30.00');
INSERT INTO `scores` VALUES ('583', '1022', '15', '124', '54', '121.10');
INSERT INTO `scores` VALUES ('584', '1030', '15', '124', '45', '51.10');
INSERT INTO `scores` VALUES ('585', '1030', '15', '124', '47', null);
INSERT INTO `scores` VALUES ('586', '1030', '15', '124', '48', '20.00');
INSERT INTO `scores` VALUES ('587', '1030', '15', '124', '50', '20.00');
INSERT INTO `scores` VALUES ('588', '1030', '15', '124', '51', '20.00');
INSERT INTO `scores` VALUES ('589', '1030', '15', '124', '54', '111.10');
INSERT INTO `scores` VALUES ('590', null, '15', '124', '84', '123.00');
INSERT INTO `scores` VALUES ('591', null, '15', '130', '84', '66666.00');
INSERT INTO `scores` VALUES ('592', null, '15', '122', '84', '66666.00');
INSERT INTO `scores` VALUES ('593', null, '15', '124', '85', '321.00');
INSERT INTO `scores` VALUES ('594', null, '15', '130', '85', '3211.00');
INSERT INTO `scores` VALUES ('595', null, '15', '120', '85', '12345.00');
INSERT INTO `scores` VALUES ('596', null, '15', '125', '84', '12.00');
INSERT INTO `scores` VALUES ('597', '1025', '15', '124', '86', '1.00');
INSERT INTO `scores` VALUES ('598', '1025', '15', '130', '45', null);
INSERT INTO `scores` VALUES ('599', '1025', '15', '130', '47', null);
INSERT INTO `scores` VALUES ('600', '1025', '15', '130', '48', null);
INSERT INTO `scores` VALUES ('601', '1025', '15', '130', '50', null);
INSERT INTO `scores` VALUES ('602', '1025', '15', '130', '51', null);
INSERT INTO `scores` VALUES ('603', '1025', '15', '130', '86', '2.00');
INSERT INTO `scores` VALUES ('604', '1025', '15', '130', '54', '69879.00');
INSERT INTO `scores` VALUES ('605', '1025', '15', '120', '86', '3.00');
INSERT INTO `scores` VALUES ('606', null, '15', '132', '50', '1.00');
INSERT INTO `scores` VALUES ('607', null, '15', '132', '85', '3.00');
INSERT INTO `scores` VALUES ('608', null, '15', '132', '47', '2.00');
INSERT INTO `scores` VALUES ('609', null, '15', '125', '85', '1.00');
INSERT INTO `scores` VALUES ('610', null, '15', '132', '84', '1.00');
INSERT INTO `scores` VALUES ('611', null, '15', '133', '48', '88.00');
INSERT INTO `scores` VALUES ('612', null, '15', '133', '84', '100.00');
INSERT INTO `scores` VALUES ('613', null, '15', '133', '85', '200.00');
INSERT INTO `scores` VALUES ('614', null, '15', '133', '45', '45.00');
INSERT INTO `scores` VALUES ('615', null, '15', '133', '47', '54.00');
INSERT INTO `scores` VALUES ('617', null, '26', '139', '93', '30.00');
INSERT INTO `scores` VALUES ('618', null, '26', '139', '94', '20.00');
INSERT INTO `scores` VALUES ('619', null, '26', '139', '95', '50.00');
INSERT INTO `scores` VALUES ('620', null, '26', '140', '93', '28.00');
INSERT INTO `scores` VALUES ('621', null, '26', '140', '94', '18.00');
INSERT INTO `scores` VALUES ('622', null, '26', '140', '95', '48.00');
INSERT INTO `scores` VALUES ('623', null, '26', '141', '93', '26.00');
INSERT INTO `scores` VALUES ('624', null, '26', '141', '94', '16.00');
INSERT INTO `scores` VALUES ('625', null, '26', '141', '95', '46.00');
INSERT INTO `scores` VALUES ('626', null, '26', '142', '93', '24.00');
INSERT INTO `scores` VALUES ('627', null, '26', '142', '94', '14.00');
INSERT INTO `scores` VALUES ('628', null, '26', '142', '95', '44.00');
INSERT INTO `scores` VALUES ('629', null, '26', '143', '93', '22.00');
INSERT INTO `scores` VALUES ('630', null, '26', '143', '94', '12.00');
INSERT INTO `scores` VALUES ('631', null, '26', '143', '95', '42.00');
INSERT INTO `scores` VALUES ('632', null, '26', '144', '93', '20.00');
INSERT INTO `scores` VALUES ('633', null, '26', '144', '94', '10.00');
INSERT INTO `scores` VALUES ('634', null, '26', '144', '95', '40.00');
INSERT INTO `scores` VALUES ('635', null, '26', '145', '93', '18.00');
INSERT INTO `scores` VALUES ('636', null, '26', '145', '94', '8.00');
INSERT INTO `scores` VALUES ('637', null, '26', '145', '95', '38.00');
INSERT INTO `scores` VALUES ('638', null, '26', '146', '93', '16.00');
INSERT INTO `scores` VALUES ('639', null, '26', '146', '94', '6.00');
INSERT INTO `scores` VALUES ('640', null, '26', '146', '95', '36.00');
INSERT INTO `scores` VALUES ('641', null, '26', '147', '93', '14.00');
INSERT INTO `scores` VALUES ('642', null, '26', '147', '94', '4.00');
INSERT INTO `scores` VALUES ('643', null, '26', '147', '95', '34.00');
INSERT INTO `scores` VALUES ('644', '1034', '26', '139', '94', '20.00');
INSERT INTO `scores` VALUES ('645', '1034', '26', '139', '95', '50.00');
INSERT INTO `scores` VALUES ('646', '1034', '26', '139', '92', '100.00');
INSERT INTO `scores` VALUES ('647', '1034', '26', '140', '94', '20.00');
INSERT INTO `scores` VALUES ('648', '1034', '26', '140', '95', '48.00');
INSERT INTO `scores` VALUES ('649', '1034', '26', '140', '92', '96.00');
INSERT INTO `scores` VALUES ('650', '1034', '26', '141', '94', '20.00');
INSERT INTO `scores` VALUES ('651', '1034', '26', '141', '95', '46.00');
INSERT INTO `scores` VALUES ('652', '1034', '26', '141', '92', '92.00');
INSERT INTO `scores` VALUES ('653', '1035', '26', '139', '94', '10.00');
INSERT INTO `scores` VALUES ('654', '1035', '26', '139', '95', '50.00');
INSERT INTO `scores` VALUES ('655', '1035', '26', '139', '92', '90.00');
INSERT INTO `scores` VALUES ('656', '1035', '26', '140', '94', '20.00');
INSERT INTO `scores` VALUES ('657', '1035', '26', '140', '95', '30.00');
INSERT INTO `scores` VALUES ('658', '1035', '26', '140', '92', '78.00');
INSERT INTO `scores` VALUES ('659', '1035', '26', '141', '94', '16.00');
INSERT INTO `scores` VALUES ('660', '1035', '26', '141', '95', '46.00');
INSERT INTO `scores` VALUES ('661', '1035', '26', '141', '92', '88.00');
INSERT INTO `scores` VALUES ('662', '1036', '26', '139', '94', '20.00');
INSERT INTO `scores` VALUES ('663', '1036', '26', '139', '95', '50.00');
INSERT INTO `scores` VALUES ('664', '1036', '26', '139', '92', '100.00');
INSERT INTO `scores` VALUES ('665', '1036', '26', '140', '94', '16.00');
INSERT INTO `scores` VALUES ('666', '1036', '26', '140', '95', '48.00');
INSERT INTO `scores` VALUES ('667', '1036', '26', '140', '92', '92.00');
INSERT INTO `scores` VALUES ('668', '1036', '26', '141', '94', '18.00');
INSERT INTO `scores` VALUES ('669', '1036', '26', '141', '95', '30.00');
INSERT INTO `scores` VALUES ('670', '1036', '26', '141', '92', '74.00');
INSERT INTO `scores` VALUES ('671', '1037', '26', '142', '94', '20.00');
INSERT INTO `scores` VALUES ('672', '1037', '26', '142', '95', '50.00');
INSERT INTO `scores` VALUES ('673', '1037', '26', '142', '92', '94.00');
INSERT INTO `scores` VALUES ('674', '1037', '26', '143', '94', '20.00');
INSERT INTO `scores` VALUES ('675', '1037', '26', '143', '95', '48.00');
INSERT INTO `scores` VALUES ('676', '1037', '26', '143', '92', '90.00');
INSERT INTO `scores` VALUES ('677', '1037', '26', '144', '94', '10.00');
INSERT INTO `scores` VALUES ('678', '1037', '26', '144', '95', '46.00');
INSERT INTO `scores` VALUES ('679', '1037', '26', '144', '92', '76.00');
INSERT INTO `scores` VALUES ('680', '1038', '26', '142', '94', '10.00');
INSERT INTO `scores` VALUES ('681', '1038', '26', '142', '95', '45.00');
INSERT INTO `scores` VALUES ('682', '1038', '26', '142', '92', '79.00');
INSERT INTO `scores` VALUES ('683', '1038', '26', '143', '94', '10.00');
INSERT INTO `scores` VALUES ('684', '1038', '26', '143', '95', '40.00');
INSERT INTO `scores` VALUES ('685', '1038', '26', '143', '92', '72.00');
INSERT INTO `scores` VALUES ('686', '1038', '26', '144', '94', '20.00');
INSERT INTO `scores` VALUES ('687', '1038', '26', '144', '95', '50.00');
INSERT INTO `scores` VALUES ('688', '1038', '26', '144', '92', '90.00');
INSERT INTO `scores` VALUES ('689', '1039', '26', '144', '94', '10.00');
INSERT INTO `scores` VALUES ('690', '1039', '26', '144', '95', '38.00');
INSERT INTO `scores` VALUES ('691', '1039', '26', '144', '92', '68.00');
INSERT INTO `scores` VALUES ('692', '1039', '26', '143', '94', '15.00');
INSERT INTO `scores` VALUES ('693', '1039', '26', '143', '95', '50.00');
INSERT INTO `scores` VALUES ('694', '1039', '26', '143', '92', '87.00');
INSERT INTO `scores` VALUES ('695', '1039', '26', '142', '94', '20.00');
INSERT INTO `scores` VALUES ('696', '1039', '26', '142', '95', '50.00');
INSERT INTO `scores` VALUES ('697', '1039', '26', '142', '92', '94.00');
INSERT INTO `scores` VALUES ('698', '1040', '26', '145', '94', '10.00');
INSERT INTO `scores` VALUES ('699', '1040', '26', '145', '95', '50.00');
INSERT INTO `scores` VALUES ('700', '1040', '26', '145', '92', '78.00');
INSERT INTO `scores` VALUES ('701', '1040', '26', '146', '94', '20.00');
INSERT INTO `scores` VALUES ('702', '1040', '26', '146', '95', '45.00');
INSERT INTO `scores` VALUES ('703', '1040', '26', '146', '92', '81.00');
INSERT INTO `scores` VALUES ('704', '1040', '26', '147', '94', '18.00');
INSERT INTO `scores` VALUES ('705', '1040', '26', '147', '95', '35.00');
INSERT INTO `scores` VALUES ('706', '1040', '26', '147', '92', '67.00');
INSERT INTO `scores` VALUES ('707', '1041', '26', '145', '94', '18.00');
INSERT INTO `scores` VALUES ('708', '1041', '26', '145', '95', '48.00');
INSERT INTO `scores` VALUES ('709', '1041', '26', '145', '92', '84.00');
INSERT INTO `scores` VALUES ('710', '1041', '26', '146', '94', '16.00');
INSERT INTO `scores` VALUES ('711', '1041', '26', '146', '95', '49.00');
INSERT INTO `scores` VALUES ('712', '1041', '26', '146', '92', '81.00');
INSERT INTO `scores` VALUES ('713', '1041', '26', '147', '94', '20.00');
INSERT INTO `scores` VALUES ('714', '1041', '26', '147', '95', '50.00');
INSERT INTO `scores` VALUES ('715', '1041', '26', '147', '92', '84.00');
INSERT INTO `scores` VALUES ('716', '1042', '26', '145', '94', '14.00');
INSERT INTO `scores` VALUES ('717', '1042', '26', '145', '95', '40.00');
INSERT INTO `scores` VALUES ('718', '1042', '26', '145', '92', '72.00');
INSERT INTO `scores` VALUES ('719', '1042', '26', '146', '94', '18.00');
INSERT INTO `scores` VALUES ('720', '1042', '26', '146', '95', '45.00');
INSERT INTO `scores` VALUES ('721', '1042', '26', '146', '92', '79.00');
INSERT INTO `scores` VALUES ('722', '1042', '26', '147', '94', '20.00');
INSERT INTO `scores` VALUES ('723', '1042', '26', '147', '95', '50.00');
INSERT INTO `scores` VALUES ('724', '1042', '26', '147', '92', '84.00');
INSERT INTO `scores` VALUES ('725', null, '15', '148', '84', '50.00');
INSERT INTO `scores` VALUES ('727', null, '15', '149', '84', '1.00');
INSERT INTO `scores` VALUES ('728', null, '15', '149', '85', '2.00');
INSERT INTO `scores` VALUES ('729', null, '15', '150', '84', '10.00');
INSERT INTO `scores` VALUES ('730', null, '15', '150', '85', null);
INSERT INTO `scores` VALUES ('731', '1022', '15', '150', '45', null);
INSERT INTO `scores` VALUES ('732', '1022', '15', '150', '47', null);
INSERT INTO `scores` VALUES ('733', '1022', '15', '150', '48', null);
INSERT INTO `scores` VALUES ('734', '1022', '15', '150', '50', null);
INSERT INTO `scores` VALUES ('735', '1022', '15', '150', '51', null);
INSERT INTO `scores` VALUES ('736', '1022', '15', '150', '86', '11.00');
INSERT INTO `scores` VALUES ('737', '1022', '15', '150', '54', '21.00');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `institutionID` int DEFAULT NULL COMMENT '外键',
  `studentnumber` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `IDNumber` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `telephone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `year` int DEFAULT NULL,
  `deleteflag` int NOT NULL,
  `tutorID` int DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `score` int DEFAULT '0',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `tutorID` (`tutorID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2099 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('2071', '陈锦豪', '21', null, '102551210004479', '17376569429', 'jinhaochan365@163.com', '102551210004479', '$2a$10$/VauH0rmJH0BlAwZhTzkv.ELdAQP8sVrghoqoNrQ0s8mEwUjjiSOa', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2072', '李烨', '21', null, '102551210002476', '18834165165', '1023568236@qq.com', '102551210002476', '$2a$10$DYbdcC5AeaJnil3wKR15UuUtFql7z94AWSmUKTmYvK0qdE7hGTdrS', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2073', '王双红', '21', null, '102551210008659', '17754442425', '1394756525@qq.com', '102551210008659', '$2a$10$TjK9t0/JSOhcd7.VnN7XGerj5cR75KDm/sRiCOxXukHs40OYpuKBS', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2074', '史航', '21', null, '102551210002424', '15532175718', '1031576719@qq.com', '102551210002424', '$2a$10$YrVVNooFp4lK/49wdEBFy.fl/iqkvwn8kMCFWnqAyS7MCpd.WUUDi', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2075', '陈奎佑', '21', null, '102551210003963', '18717967816', 'chenkuiyou95@163.com', '102551210003963', '$2a$10$zbX7K2JyZQX5sj2Q5bXMlOgRTrvQL3uhIypyrLrLMdoFtSilkDa2y', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2076', '朱冰竹', '21', null, '102551210008999', '17621666942', '17621666942@163.com', '102551210008999', '$2a$10$LI89BhBJDOcLbPLlKsLLY.Di.9j7wns/ooG4pvZLIgQ7KO7xuqnwK', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2077', '杨彬', '21', null, '102551210000145', '18621333050', '2211265445@qq.com', '102551210000145', '$2a$10$SUjg/Gc2iH22243QSA1Sf.i9dTL7wC8UPQPI5nyEcwcMrLPowGgFu', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2078', '赵宇成', '21', null, '102551210009175', '15727466250', '1464330884@qq.com', '102551210009175', '$2a$10$3wOHTbDP7ou9TD.se29o.u3YHHzyhg6JCj.NeCX83SKHdUMCvt/ry', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2079', '於柏安', '21', null, '102551210004782', '18892626454', '837720757@qq.com', '102551210004782', '$2a$10$Kw0mg5aoI8pG.ka02IDQduGb/7mVFbjVItnskgl1awANPtkBMf/mu', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2080', '王启源', '21', null, '102551210008226', '15690866680', '1831977837@qq.com', '102551210008226', '$2a$10$oEZixbDNYxZ7.ow7/KhQZentWCmIbR3tDWjAH/wfOKEkni83cWJXe', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2081', '张三3', null, null, '181310000', '13812341234', '123@dhu.edu.cn', 'zhangsan', '$2a$10$RkUhuabY5KPb7.K9oGeDbOnd8bNYtS2p8SFiN0atlkrAkHMsRMQtG', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2082', '张狗', null, null, '181310001', '12812341234', '123@dhu.edu.cn', 'zhangdog', '$2a$10$RkUhuabY5KPb7.K9oGeDbOnd8bNYtS2p8SFiN0atlkrAkHMsRMQtG', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2083', '张狗1', null, null, '181310002', '12812341554', '123@dhu.edu.cn', 'zhangdog1', '$2a$10$RkUhuabY5KPb7.K9oGeDbOnd8bNYtS2p8SFiN0atlkrAkHMsRMQtG', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2084', '张三2', null, null, '310111111111111000', '13812341204', '123@dhu.edu.cn', '第一组', '$2a$10$KhfsQjXcxefZhGsrASFYe.kFUiyCNivVMkTSYAxEkmMzT6521VLTi', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2085', '张三测试', null, null, '310111111111661111', '13812341244', '123@dhu.edu.cn', 'zhangtestsan', '$2a$10$Xk4SCIJPnJ.RHN7y7Il0BOI5/qQI5/UnYgYuDAmQJKK4csHhfHfPy', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2086', '卢三', '1', null, '310111111111111111', '13812341231', '123@dhu.edu.cn', 'student', '7c4a8d09ca3762af61e59520943dc26494f8941b', null, '0', '1003', '11', '0');
INSERT INTO `student` VALUES ('2087', '钟平灵', null, null, '310111111111111112', '13812341232', '124@dhu.edu.cn', '181310002', '$2a$10$XGhQ5ESI1h1/U3Mbe3pPCOPtwX9L8u2MsLKuST8H9Th.BP4Qcoane', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2088', '陈俊驰', null, null, '310111111111111113', '13812341233', '125@dhu.edu.cn', '181310003', '$2a$10$Ktx07OtruoNd1P2LW6/75.O7HS7j5IT86AJzHiA59o7uuZZf.LeA2', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2089', '张冷丹', null, null, '310111111111111114', '13812341234', '126@dhu.edu.cn', '181310004', '$2a$10$hA4xnuVJIOoAKd5XAxorS.qLpGSHUiOOYZX1qKk9AYHYFyg32Ymby', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2090', '张良策', null, null, '310111111111111115', '13812341235', '127@dhu.edu.cn', '181310005', '$2a$10$JPi5BEL/0N5e4KkpH3aG7ORmM.l4e8/RE2H95IoeJJd68wH3unQ7u', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2091', '王若琳', null, null, '310111111111111116', '13812341236', '128@dhu.edu.cn', '181310006', '$2a$10$.SNSk9ilnxlDfo33isnvtOppto9z7jKtE2v0.tKRLHEJYYR3l5dOG', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2092', '陈嘉菲', null, null, '310111111111111117', '13812341237', '129@dhu.edu.cn', '181310007', '$2a$10$mh8I02.vy40xuOCqnNxWNOzKpjiSYDKpDZd3S0PKzOZgdJvY9gLAi', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2093', '钟安卉', null, null, '310111111111111118', '13812341238', '130@dhu.edu.cn', '181310008', '$2a$10$/LV3EbAdNLz61GikPkIqe.ExN5F3UVHHdW3Y0.yDI5Lquc/qfscTS', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2094', '王青芙', null, null, '310111111111111119', '13812341239', '131@dhu.edu.cn', '181310009', '$2a$10$MSARsU7QH6uvFDgUfuWcSO1lsmE/mqGJ1MLlccmNkXeFAQMdm.yeO', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2095', '测试序号', '21', null, '310111111111121000', '10012341234', '123@dhu.edu.cn', '1191310000', '$2a$10$zYoet0Y0hIt65XHVWy6svewahcA5inu8CfcxCYRh9ukUK4Ylxtat.', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2096', '测试分组', null, null, '310111111111111222', null, null, '1991310000', '$2a$10$CGlijEQCIwiSWg2bn26l6OmISrugSqQEoxqNgIE5LvJX5iYSlz3Oi', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2097', '卢三old', '1', null, '310111111111111111', '13812341231', '123@dhu.edu.cn', 'studentold', '7c4a8d09ca3762af61e59520943dc26494f8941b', null, '0', '1003', '10', '0');
INSERT INTO `student` VALUES ('2098', '选手test', '1', null, '2222776', null, null, 'participants', '7c4a8d09ca3762af61e59520943dc26494f8941b', null, '0', null, '12', '0');

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `institutionID` int DEFAULT NULL COMMENT '外键',
  `name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `jobnumber` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sex` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `department` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `IDNumber` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `deleteflag` int NOT NULL,
  `role` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `experts_ibfk_1` (`institutionID`) USING BTREE,
  CONSTRAINT `teacher_ibfk_1` FOREIGN KEY (`institutionID`) REFERENCES `institution` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=1046 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES ('1003', null, '张三', '1310092', '女', '计算机学院', '310110100000000022', '13800000011', '3311980798@qq.com', 'teazhang', '$2a$10$uHDCc/t1wijNiOcZZucpbecsV0O61/iKk8mo95w5u43uek1Grz3za', '0', '8');
INSERT INTO `teacher` VALUES ('1022', '21', '李晓霞', '10104974', '女', '院办', '370881198112271123', '13818216367', 'lixx@dhu.edu.cn', '10104974', '$2a$10$.XnYhm/jjjOhR3O1e8Olvek7tnSjK7dM7tVvJPNL4i/u69XS1kSOu', '0', '3');
INSERT INTO `teacher` VALUES ('1023', '21', '黄利利', '10105114', '女', '院办', '310230198008140023', '13817912108', 'huangll@dhu.edu.cn', '10105114', '$2a$10$31hDCiVPSI7aL1gl23XxG.WjihO.Uf0PxN2J3ajLsB1fiYiAAWA7e', '0', '3');
INSERT INTO `teacher` VALUES ('1024', '21', '李倩', '10105172', '女', '院办', '370303198506081721', '18221223785', 'liqian@dhu.edu.cn', '10105172', '$2a$10$BiVUvhrXLDd2IazftdRZqez0.0YEx7Pm00ITcIioe/JME/AjS4SBO', '0', '3');
INSERT INTO `teacher` VALUES ('1025', '21', '戴群', '10219404', '女', '院办', '370306199107281527', '13162094729', 'daiqun@dhu.edu.cn', '10219404', '$2a$10$n7R0GQwceyKh8aTLAoemq.x3asmAwl87tzYAi.XEHP5UR.7KDM9hK', '0', '3');
INSERT INTO `teacher` VALUES ('1026', '21', '李华婷', '10105027', '女', '辅办', '41052619851207004X', '13611936219', 'lihuating@dhu.edu.cn', '10105027', '$2a$10$twEFTDazxw9UrY39csvvhORqIALasl09HpY6MSwDIdeOGOepeu0sq', '0', '3');
INSERT INTO `teacher` VALUES ('1027', '21', '唐菲菲', '10105135', '女', '辅办', '410822198706180045', '15921440948', 'tangfeifei@dhu.edu.cn', '10105135', '$2a$10$72xYQNbv/euYHQzol9VGy.CECA/dOhUJxs.cypksooBj/ImT152Tq', '0', '3');
INSERT INTO `teacher` VALUES ('1028', '21', '姜霖霖', '10105426', '女', '辅办', '320684199203205424', '15201994969', 'jurinalin@163.com', '10105426', '$2a$10$M6RMpkH6ZbHynJwkqssWRexhdKbNfu/uFC3kIcf62ST/oFfFQvp5a', '0', '3');
INSERT INTO `teacher` VALUES ('1029', '21', '彭湘陵', '10105471', '女', '辅办', '421024199207080046', '13262276126', 'pengxiangling@dhu.edu.cn', '10105471', '$2a$10$CumnbjBLl6EDGYVfbooJluUEq7LJgsofKHGwhywFiq1VtuAQsqnoC', '0', '3');
INSERT INTO `teacher` VALUES ('1030', null, '张三', '20131000', '男', '计算机学院', '123456789123456000', '13812341234', '123@dhu.edu.cn', '\'zhangsan', '$2a$10$tCv5A9uLWq9MQajnJ6NLZesIzEy5K6ZX0OZR4jgXQ0ji2p.V2AmgK', '0', '3');
INSERT INTO `teacher` VALUES ('1031', null, '张三测试', '18331000', '男', '测试学院', '332156789123456000', '13812341555', '123@dhu.edu.cn', 'zhangdd', '$2a$10$GR8CxDOHzagZoyq.nKqDcekocGRo/zj64eB4kR0xa5G70WJdgQKk.', '0', '3');
INSERT INTO `teacher` VALUES ('1032', '21', '地导弹', '181310406', '女', '地导弹', '310118888888888881', '18888888888', '126@16.com', '18888888888', '$2a$10$vGW9MDbLr7pexNxkn78wPel0TRtr84S3yeljxxHahvryHX0lcEv2K', '0', '3');
INSERT INTO `teacher` VALUES ('1033', '21', '1', '1', '女', '1', '1', '1', '1', '1', '7c4a8d09ca3762af61e59520943dc26494f8941b', '0', '3');
INSERT INTO `teacher` VALUES ('1034', null, '张海芹', '131001', '女', '计算机学院', '310110100000000001', '13800000001', '001@dhu.edu.cn', '131001', '$2a$10$HAukfXderwGItm62h7YT6OvjWHSppyMCv2W.mTpyjvv4YvRUFD3oy', '0', '3');
INSERT INTO `teacher` VALUES ('1035', '1', '王千风', '131002', '女', '计算机学院', '310110100000000002', '13800000002', '002@dhu.edu.cn', '131002', '$2a$10$yHmQOUPMwhzlPWB8.nNZ8eQ773h0F4KevSIezwKqmxzVmNib93o9W', '0', '3');
INSERT INTO `teacher` VALUES ('1036', null, '王尚振', '131003', '男', '计算机学院', '310110100000000003', '13800000003', '003@dhu.edu.cn', '131003', '$2a$10$S5Ys8dR/dY3jl9Nzir8wkeIZ0vnuS26RuMDkGhc3eAWeFar6jRrFy', '0', '3');
INSERT INTO `teacher` VALUES ('1037', null, '孙锐志', '131004', '男', '计算机学院', '310110100000000004', '13800000004', '004@dhu.edu.cn', '131004', '$2a$10$0FE0oL6tppSTsrzD/bqKzuJ1Fok/aGZxeZmNUEVipyG9q8b/R8ztq', '0', '3');
INSERT INTO `teacher` VALUES ('1038', '1', '李乐松', '131005', '男', '计算机学院', '310110100000000005', '13800000005', '005@dhu.edu.cn', '131005', '$2a$10$12EDuwVCfXyCQj0GAFPBHevxntiILgNFZGZtwY.r5yvK4O9iaAhei', '0', '3');
INSERT INTO `teacher` VALUES ('1039', null, '王博木', '131006', '男', '计算机学院', '310110100000000006', '13800000006', '006@dhu.edu.cn', '131006', '$2a$10$/FKiIsrsuzlMNSBIt6Ts7uAFE3ytMfzURNLA1.G3fwOVECsoj1Gvy', '0', '3');
INSERT INTO `teacher` VALUES ('1040', null, '孙钰真', '131007', '女', '计算机学院', '310110100000000007', '13800000007', '007@dhu.edu.cn', '131007', '$2a$10$c9/th17g5qsgWYMUusDzbOsX70b0x.gfpTsK6g406fYy/h6meo7B.', '0', '3');
INSERT INTO `teacher` VALUES ('1041', '1', '王曦芸', '131008', '女', '计算机学院', '310110100000000008', '13800000008', '008@dhu.edu.cn', '131008', '7c4a8d09ca3762af61e59520943dc26494f8941b', '0', '3');
INSERT INTO `teacher` VALUES ('1042', null, '陈宏大', '131009', '男', '计算机学院', '310110100000000009', '13800000009', '009@dhu.edu.cn', '131009', '7c4a8d09ca3762af61e59520943dc26494f8941b', '0', '9');
INSERT INTO `teacher` VALUES ('1043', null, '黄三', '131025', '男', '计算机学院', '310110100000000010', '13800000010', '010@dhu.edu.cn', 'teacher', '7c4a8d09ca3762af61e59520943dc26494f8941b', '0', '8');
INSERT INTO `teacher` VALUES ('1044', null, '黄三old', '131025', '男', '计算机学院', '310110100000000010', '13800000010', '010@dhu.edu.cn', 'teacherold', '7c4a8d09ca3762af61e59520943dc26494f8941b', '0', '8;9;3');
INSERT INTO `teacher` VALUES ('1045', null, '专家', null, null, null, null, null, null, null, null, '0', '12');

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
INSERT INTO `test` VALUES ('1', 'grz', '1');
INSERT INTO `test` VALUES ('2', 'grz', '2');
INSERT INTO `test` VALUES ('3', 'grz', '3');

-- ----------------------------
-- Table structure for totalitem
-- ----------------------------
DROP TABLE IF EXISTS `totalitem`;
CREATE TABLE `totalitem` (
  `ID` int NOT NULL,
  `activityID` int DEFAULT NULL,
  `infoSumIDs` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `scoreSumIDs` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of totalitem
-- ----------------------------
INSERT INTO `totalitem` VALUES ('1', '26', '53,54', '92,93');

-- ----------------------------
-- Table structure for underthesisdesign
-- ----------------------------
DROP TABLE IF EXISTS `underthesisdesign`;
CREATE TABLE `underthesisdesign` (
  `ID` int NOT NULL AUTO_INCREMENT COMMENT '毕业设计或论文的ID，主键',
  `studentID` int NOT NULL COMMENT '学生学号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '毕业设计或论文的主题',
  `year` varchar(4) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上传年份',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `studentID` (`studentID`) USING BTREE,
  CONSTRAINT `studentID` FOREIGN KEY (`studentID`) REFERENCES `student` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of underthesisdesign
-- ----------------------------
INSERT INTO `underthesisdesign` VALUES ('1', '2097', '这是一个本科生的毕业设计或者论文', '2022', 'http://drive.onchu6.info/ArtsHandicraftsSewing');
INSERT INTO `underthesisdesign` VALUES ('2', '2084', 'Fukuda Hana', null, 'http://drive.onchu6.info/ArtsHandicraftsSewing');
INSERT INTO `underthesisdesign` VALUES ('3', '2082', 'Steve Ward', null, 'http://image.sherrykim.us/Books');
INSERT INTO `underthesisdesign` VALUES ('4', '2084', 'April Turner', null, 'http://video.campblucille214.cn/CollectiblesFineArt');
INSERT INTO `underthesisdesign` VALUES ('5', '2075', 'Elizabeth Price', null, 'https://video.yuto414.us/ToysGames');
INSERT INTO `underthesisdesign` VALUES ('6', '2092', 'Kikuchi Momoka', null, 'http://drive.porternicole.us/ToolsHomeDecoration');
INSERT INTO `underthesisdesign` VALUES ('7', '2097', 'Leung Suk Yee', null, 'http://auth.ryotamar.xyz/ToolsHomeDecoration');
INSERT INTO `underthesisdesign` VALUES ('8', '2089', 'Lau Ka Keung', null, 'http://image.jonathanro825.info/CDsVinyl');
INSERT INTO `underthesisdesign` VALUES ('9', '2075', 'Maria Coleman', null, 'http://drive.kfchoi1002.us/ToolsHomeDecoration');
INSERT INTO `underthesisdesign` VALUES ('10', '2075', 'Shirley Vasquez', null, 'https://drive.zeng1947.co.jp/BeautyPersonalCare');
INSERT INTO `underthesisdesign` VALUES ('11', '2084', 'Wong Chi Ming', null, 'https://drive.mikimur.net/Food');

-- ----------------------------
-- Procedure structure for addDep
-- ----------------------------
DROP PROCEDURE IF EXISTS `addDep`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `addDep`(in depName varchar(32),in parentId int,in enabled boolean,out result int,out result2 int)
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
DELIMITER ;

-- ----------------------------
-- Procedure structure for deleteDep
-- ----------------------------
DROP PROCEDURE IF EXISTS `deleteDep`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteDep`(in did int,out result int)
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
DELIMITER ;
