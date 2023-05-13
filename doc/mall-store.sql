/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : mall-store

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 13/05/2023 20:44:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for store_meta
-- ----------------------------
DROP TABLE IF EXISTS `store_meta`;
CREATE TABLE `store_meta`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `creator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者',
  `created` datetime NOT NULL COMMENT '创建时间',
  `updater` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者',
  `updated` datetime NOT NULL COMMENT '更新时间',
  `store_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '店铺编号',
  `store_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '店铺名称',
  `mer_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户编号',
  `state_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态标识符 0:启用 1:禁用 2:删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_store_meta_store_id`(`store_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '店铺信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of store_meta
-- ----------------------------
INSERT INTO `store_meta` VALUES (1, 'xx', '2023-03-11 14:10:03', 'xx', '2023-03-11 14:10:03', 'efb37fc884fb4e97b0229e7800fd5ecc', '华为手机旗舰店', '6043e0147c934c278fc009a3c5e82f24', '1');
INSERT INTO `store_meta` VALUES (2, 'xx', '2023-03-11 14:10:20', 'xx', '2023-03-11 14:10:20', 'e88ffce8cdbd465f840ac7d5adcd6642', '小米手机旗舰店', 'b047b4ae475845eeb3f425dc2bf27a6e', '1');

SET FOREIGN_KEY_CHECKS = 1;
