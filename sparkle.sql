/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : sparkle

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 26/12/2019 14:16:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `administrator` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`administrator`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog`  (
  `blogId` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `label` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `author` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `createTime` varchar(23) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`blogId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of blog
-- ----------------------------
INSERT INTO `blog` VALUES ('d3f80e3797ec11e9bf2b00ff8cd0e1d6', 'java,String', 'Java StringUtils', '打上花火', '2019年06月26日 16:28:39', '\n       			\n       		<p id=\"title\">Java StringUtils</p><br><span id=\"time\">2019年06月26日 16:28:39</span>&nbsp;&nbsp;&nbsp;&nbsp;<a id=\"author\">打上花火</a><hr><span class=\"tag\">java<span></span></span><span class=\"tag\">String<span></span></span><br><br><strong class=\"subTitle\">StringUtils工具类使用<strong></strong><br></strong><br><pre class=\"paraGraph\">Commons包下的 StringUtils下提供了两个方法，isNotBlank（）、isNotEmpty（）\n\n用途：用于判断传进来的名称是否为 null 并且还要判断是否为空串\n\n\n\nisNotBlank：顾名思义，判断是否为空白 “” &nbsp;和 “ &nbsp;”两个都算是空串，返回 false\n\n\n\nisNotEmpty：判断“”算是空串，但“ &nbsp;”不为空串，返回 true\n\n\n\n例如在判断从前台传入的 name 属性是否为空串\n\nif(StringUtils.isNotBlank(name)) 这样写用于判断是否为空更为合理。\n--------------------- \n作者：zengrui_0337 \n来源：CSDN \n原文：https://blog.csdn.net/baidu_32872293/article/details/78175843 \n版权声明：本文为博主原创文章，转载请附上博文链接！</pre><br><img class=\"picture\" src=\"/img/测试.jpg\"><br><br><pre class=\"paraGraph\"> s = s.toLowerCase();\n\n StringBuilder sb = new StringBuilder(s.length());\n boolean upperCase = false;\n for (int i = 0; i &lt; s.length(); i++) {\n     char c = s.charAt(i);\n\n     if (c == SEPARATOR) {\n         upperCase = true;\n     } else if (upperCase) {\n         sb.append(Character.toUpperCase(c));\n         upperCase = false;\n     } else {\n         sb.append(c);\n     }\n }\n\n return sb.toString();</pre><br><br><pre class=\"weightParaGraph\">8、判断字符串内容的类型\n\nStringUtils.isNumeric( str);&nbsp;\n如果str全由数字组成返回True.\n\nStringUtils.isAlpha( str);&nbsp;\n如果str全由字母组成返回True.\n\nStringUtils.isAlphanumeric( str);&nbsp;\n如果str全由数字或数字组成返回True.\n\nStringUtils.isAlphaspace( str);&nbsp;\n如果str全由字母或空格组成返回True.\n\nStringUtils.isAlphanumericSpace(String str);&nbsp;\n只由字母数字和空格组成\n\nStringUtils.isNumericSpace(String str);&nbsp;\n只由数字和空格组成\n\n9、取得某字符串在另一字符串中出现的次数\n\nStringUtils.countMatches(str,seqString);&nbsp;\n取得seqString在str中出现的次数,未发现则返回零\n--------------------- \n作者：fengfengchen95 \n来源：CSDN \n原文：https://blog.csdn.net/fengfengchen95/article/details/89135456 \n版权声明：本文为博主原创文章，转载请附上博文链接！</pre><br>');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `username` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `address` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `email` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `createtime` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `updatetime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `status` int(1) NULL DEFAULT 0,
  PRIMARY KEY (`phone`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('13407547940', 'root', 'bF3Dv9NNFjA=', '南京', '1120965621@qq.com', '2019-12-23 14:28:10', '2019-12-23 14:28:34', 0);

SET FOREIGN_KEY_CHECKS = 1;
