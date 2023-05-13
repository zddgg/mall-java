/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : mall-oms

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 13/05/2023 20:44:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oms_permission
-- ----------------------------
DROP TABLE IF EXISTS `oms_permission`;
CREATE TABLE `oms_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `creator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者',
  `created` datetime NOT NULL COMMENT '创建时间',
  `updater` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者',
  `updated` datetime NOT NULL COMMENT '更新时间',
  `permission_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源编号',
  `permission_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源类型',
  `permission_desc` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源描述',
  `permission_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源值',
  `state_flag` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态: 0-停用, 1-启用, -1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_oms_permission_id`(`permission_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'OMS资源表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oms_permission
-- ----------------------------

-- ----------------------------
-- Table structure for oms_role
-- ----------------------------
DROP TABLE IF EXISTS `oms_role`;
CREATE TABLE `oms_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `creator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者',
  `created` datetime NOT NULL COMMENT '创建时间',
  `updater` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者',
  `updated` datetime NOT NULL COMMENT '更新时间',
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编号',
  `role_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `state_flag` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态: 0-停用, 1-启用, -1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_oms_role_role_id`(`role_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'OMS角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oms_role
-- ----------------------------
INSERT INTO `oms_role` VALUES (1, 'xx', '2023-04-01 17:59:08', 'xx', '2023-04-01 17:59:12', 'ADMIN', '管理员', '1');
INSERT INTO `oms_role` VALUES (2, 'xx', '2023-04-01 17:59:08', 'xx', '2023-04-01 17:59:12', 'YW', '运维', '1');
INSERT INTO `oms_role` VALUES (3, 'xx', '2023-04-01 17:59:08', 'xx', '2023-04-01 17:59:12', 'KF', '客服', '1');
INSERT INTO `oms_role` VALUES (4, 'xx', '2023-04-01 17:59:08', 'xx', '2023-04-01 17:59:12', 'YY', '运营', '1');
INSERT INTO `oms_role` VALUES (5, 'xx', '2023-04-01 17:59:08', 'xx', '2023-04-01 17:59:12', 'YYJL', '运营经理', '1');

-- ----------------------------
-- Table structure for oms_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `oms_role_permission`;
CREATE TABLE `oms_role_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `creator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者',
  `created` datetime NOT NULL COMMENT '创建时间',
  `updater` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者',
  `updated` datetime NOT NULL COMMENT '更新时间',
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编号',
  `permission_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源编号',
  `state_flag` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态: 0-停用, 1-启用, -1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_oms_role_permission_id`(`role_id` ASC, `permission_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'OMS角色资源表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oms_role_permission
-- ----------------------------

-- ----------------------------
-- Table structure for oms_user
-- ----------------------------
DROP TABLE IF EXISTS `oms_user`;
CREATE TABLE `oms_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `creator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者',
  `created` datetime NOT NULL COMMENT '创建时间',
  `updater` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者',
  `updated` datetime NOT NULL COMMENT '更新时间',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名称',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `mobile` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号码',
  `state_flag` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态: 0-停用, 1-启用, -1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_oms_user_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'OMS用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oms_user
-- ----------------------------
INSERT INTO `oms_user` VALUES (1, 'xx', '2023-04-01 19:48:42', 'xx', '2023-04-01 19:48:42', 'admin', '管理员', 'admin', '123@qq.com', '110', '1');

-- ----------------------------
-- Table structure for oms_user_role
-- ----------------------------
DROP TABLE IF EXISTS `oms_user_role`;
CREATE TABLE `oms_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `creator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者',
  `created` datetime NOT NULL COMMENT '创建时间',
  `updater` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者',
  `updated` datetime NOT NULL COMMENT '更新时间',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编号',
  `state_flag` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态: 0-停用, 1-启用, -1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_oms_user_role_id`(`user_id` ASC, `role_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'OMS用户角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oms_user_role
-- ----------------------------
INSERT INTO `oms_user_role` VALUES (1, 'xx', '2023-04-01 18:29:19', 'xx', '2023-04-01 18:29:19', 'admin', 'ADMIN', '1');

SET FOREIGN_KEY_CHECKS = 1;
