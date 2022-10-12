/*
 Navicat MySQL Data Transfer

 Source Server         : renren_fast
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 23/09/2021 16:32:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, '张三', 12, 'newcoder', 'e10adc3949ba59abbe56e057f20f883e', '广东广州', '男');
INSERT INTO `user` VALUES (3, '李四', 32, 'linux', 'e10adc3949ba59abbe56e057f20f883e', '安徽', '女');
INSERT INTO `user` VALUES (4, '王五', 22, 'coder', 'e10adc3949ba59abbe56e057f20f883e', '陕西', '男');
INSERT INTO `user` VALUES (5, '赵六', 17, 'xie', 'e10adc3949ba59abbe56e057f20f883e', '北京', '男');

SET FOREIGN_KEY_CHECKS = 1;
