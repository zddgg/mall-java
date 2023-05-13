/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : mall-cart

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 13/05/2023 20:44:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart_item
-- ----------------------------
DROP TABLE IF EXISTS `cart_item`;
CREATE TABLE `cart_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `creator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者',
  `created` datetime NOT NULL COMMENT '创建时间',
  `updater` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者',
  `updated` datetime NOT NULL COMMENT '更新时间',
  `cart_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '购物车编号',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `store_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '店铺编号',
  `sku_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'SKU编号',
  `sku_num` int NOT NULL COMMENT 'SKU数量',
  `add_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '加购价格',
  `selected_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否选中: 1-是, 0-否',
  `state_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标识符 -1:删除 0:启用 1:禁用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_cart_id`(`cart_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '购物车' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cart_item
-- ----------------------------
INSERT INTO `cart_item` VALUES (21, 'xx', '2023-04-19 00:44:01', 'xx', '2023-04-19 00:44:01', '842388b138a744dc95b35b01a557a385', '123456', 'efb37fc884fb4e97b0229e7800fd5ecc', 'd8ea9c568b9b412fbd24afea92eacabd', 2, 56.33, '1', '1');
INSERT INTO `cart_item` VALUES (22, 'xx', '2023-05-13 18:28:49', 'xx', '2023-05-13 18:28:49', '571225a24bdf4e6d870590f820e7c3ce', '123456', 'e88ffce8cdbd465f840ac7d5adcd6642', '02c886c1dc0f41259f62a6740eb1e378', 2, 2999.00, '0', '1');
INSERT INTO `cart_item` VALUES (23, 'xx', '2023-05-13 18:30:00', 'xx', '2023-05-13 18:30:00', '2707c5af21cd4f349985bddf6e7d7920', '123456', 'e88ffce8cdbd465f840ac7d5adcd6642', '8ed9045250d04cd7b0e098602381d761', 15, 2999.00, '0', '1');

SET FOREIGN_KEY_CHECKS = 1;
