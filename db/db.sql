-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: kuangquanmiao
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_auth_user`
--

DROP TABLE IF EXISTS `t_auth_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_auth_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `openid` varchar(255) DEFAULT NULL,
  `phone_number` varchar(32) DEFAULT NULL COMMENT '手机号码',
  `address` varchar(512) DEFAULT NULL COMMENT '地址',
  `avatar` varchar(128) DEFAULT NULL COMMENT '用户头像',
  `type` tinyint(1) DEFAULT '0' COMMENT '0为普通用户 1为管理员',
  `sex` tinyint(1) DEFAULT '0' COMMENT '性别 0为未知 1为男 2为女',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态 1:启用 0:禁用',
  `create_by` bigint DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_auth_user_pk` (`openid`),
  KEY `t_auth_user_openid_index` (`openid`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_auth_user`
--

LOCK TABLES `t_auth_user` WRITE;
/*!40000 ALTER TABLE `t_auth_user` DISABLE KEYS */;
INSERT INTO `t_auth_user` VALUES (1,'翁卓群','$2a$10$OttaARR/x5rg6ytK124ZjuvISzDhrxHf3B4dlNyvq4iFbjVdlGMWe','otFZY61cfj5NV0PHEp8UuBfGreqU','13828140579',NULL,'http://dummyimage.com/100x100',1,0,1,NULL,NULL,NULL,NULL,0),(2,NULL,'$2a$10$DzYfVuSlSfZVxne8XIzmNOVLUseg2tH4gEXJDiDAgb5NUHUZxndJK','otFZY6y0SBGeMAhhzfv4Er_PWrT8',NULL,NULL,NULL,0,0,1,NULL,NULL,1,'2024-03-07 17:25:32',0),(3,NULL,NULL,'otFZY64UCgJMGOwBU18nJtOM75m4',NULL,NULL,NULL,0,0,1,NULL,NULL,NULL,NULL,0),(4,NULL,NULL,'otFZY6zV1kSwrrz_ZkTlZUcKY0S8',NULL,NULL,NULL,0,0,1,NULL,NULL,NULL,NULL,0),(9,NULL,NULL,'olBS462bIpe1J-etbMxIYb192TzQ',NULL,NULL,NULL,1,0,1,-1,'2024-02-17 00:45:51',-1,'2024-02-17 00:45:51',0);
/*!40000 ALTER TABLE `t_auth_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_comment`
--

DROP TABLE IF EXISTS `t_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '评论类型（0代表帖子）',
  `post_id` bigint DEFAULT NULL COMMENT '帖子id',
  `content` varchar(512) DEFAULT NULL COMMENT '评论内容',
  `root_id` bigint NOT NULL DEFAULT '-1' COMMENT '根评论id',
  `to_comment_user_id` bigint DEFAULT NULL COMMENT '所回复的目标评论的userid',
  `to_comment_id` bigint DEFAULT NULL COMMENT '所回复评论id',
  `create_by` bigint DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_comment`
--

LOCK TABLES `t_comment` WRITE;
/*!40000 ALTER TABLE `t_comment` DISABLE KEYS */;
INSERT INTO `t_comment` VALUES (1,31,83,'occaecat ipsum sit est deserunt',-1,77,28,9,'2024-03-06 20:54:32',9,'2024-03-06 20:54:32',0),(2,31,83,'occaecat ipsum sit est deserunt',-1,77,28,9,'2024-03-06 21:04:02',9,'2024-03-06 21:04:02',0);
/*!40000 ALTER TABLE `t_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_message`
--

DROP TABLE IF EXISTS `t_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `uuid` bigint NOT NULL COMMENT '分布式ID',
  `content` varchar(512) DEFAULT NULL COMMENT '消息内容',
  `msg_from` bigint NOT NULL COMMENT '发送者',
  `msg_to` bigint NOT NULL COMMENT '接收者',
  `type` tinyint(1) DEFAULT NULL COMMENT '消息类型',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志（0为保留，1为删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_message`
--

LOCK TABLES `t_message` WRITE;
/*!40000 ALTER TABLE `t_message` DISABLE KEYS */;
INSERT INTO `t_message` VALUES (21,2344221505817088,'你好aaaaaaaaaaaaa',1,2,0,'1970-01-01 11:07:03',0),(22,2344222135921152,'你好aaaaaaaaaaaaa',1,2,0,'1970-01-01 11:07:03',0),(23,2344222153714176,'你好aaaaaaaaaaaaa',1,2,0,'1970-01-01 11:07:03',0),(24,2344222247782912,'你好aaaaaaaaaaaaa',1,2,0,'1970-01-01 11:07:03',0),(25,2344224598051328,'你好aaaaaaaaaaaaa',1,2,0,'1970-01-01 11:07:03',0),(26,2344236409309696,'你好aaaaaaaaaaaaa',1,2,0,'1970-01-01 11:07:03',0),(27,2344241249282560,'你好aaaaaaaaaaaaa',1,2,0,'1970-01-01 11:07:03',0),(28,2344262767938048,'你好啊，小号',1,2,0,'1970-01-01 11:07:03',0),(29,2344266982664704,'你好啊，小号',1,2,0,'1970-01-01 11:07:03',0),(30,2344268204993024,'你好啊，大号',2,1,0,'1970-01-01 11:07:03',0),(31,2344269856459264,'你好啊，小号',1,2,0,'1970-01-01 11:07:03',0),(32,2344269876513280,'你好啊，小号',1,2,0,'1970-01-01 11:07:03',0),(33,2344283443161600,'你好啊，小号',1,2,0,'1970-01-01 11:07:03',0),(34,2345549190908416,'你好啊哥们',2,3,0,'1970-01-01 11:05:11',0),(35,2345589706156544,'你好啊哥们',2,3,0,'1970-01-01 11:05:11',0),(36,2345620481231360,'你好啊哥们',2,3,0,'1970-01-01 11:05:11',0),(37,2345621140908544,'这是HZS发送的',3,2,0,'2024-01-28 00:18:24',0),(38,2345624799545856,'这是HZS发送的',3,2,0,'2024-01-28 00:25:51',0),(39,2345625595726336,'这是HZS发送的',3,2,0,'2024-01-28 00:27:28',0),(40,2345626461972992,'你好啊哥们',2,3,0,'1970-01-01 11:05:11',0),(41,2345630799063552,'你好啊哥们',2,3,0,'1970-01-01 11:05:11',0),(42,2345632985655808,'这是HZS发送的',3,2,0,'2024-01-28 00:42:30',0),(43,2345637165828608,'这是HZS发送的',3,2,0,'2024-01-28 00:51:00',0),(44,2345644832385536,'这是HZS发送的',3,2,0,'2024-01-28 01:06:27',0),(45,2345650061609472,'这是HZS发送的',3,2,0,'2024-01-28 01:17:15',0),(46,2345650493573632,'这是HZS发送的',3,2,0,'2024-01-28 01:18:07',0),(47,2345650957330944,'这是HZS发送的',3,2,0,'2024-01-28 01:19:04',0),(48,2349293109520896,'消息内容',2,1,0,'2024-01-28 00:25:51',0),(49,2349294356818432,'消息内容',2,1,0,'2024-01-28 00:25:51',0),(50,2349295581096448,'消息内容11111',2,1,0,'2024-01-28 00:25:51',0),(51,2349296152717824,'消息内容11111',2,1,0,'2024-01-28 00:25:51',0),(52,2349298548681216,'你好啊大号',2,1,0,'2024-01-28 00:25:51',0),(53,2349298747615744,'你好啊大号',2,1,0,'2024-01-28 00:25:51',0),(54,2350697592236544,'好好好',1,2,1,'2024-01-28 00:25:51',0),(55,2350699963886080,'可以',2,1,2,'2024-01-28 00:25:51',0),(56,2351683354356224,'',3,2,0,'2024-02-05 13:52:01',0),(57,2351685069097472,'',3,2,0,'2024-02-05 13:55:30',0),(58,2351687270050304,'123213',3,2,0,'2024-02-05 13:59:59',0),(59,2351688133134848,'http://tmp/3jWDKrbs35bn8504c2b843751f26c9b3bae1ecb63946.jpg',3,2,0,'2024-02-05 14:01:44',0),(60,2351688630004224,'http://tmp/kynHo87x2Nzo391a37e4400ff5ea80c1f98ed01f7f74.jpg',3,2,0,'2024-02-05 14:02:45',0),(61,2351689510242816,'http://tmp/7GJMxZvfPGOE8504c2b843751f26c9b3bae1ecb63946.jpg',3,2,0,'2024-02-05 14:04:32',0),(62,2351716318136832,'123123',3,2,0,'2024-02-05 14:58:58',0),(63,2356318385900032,'消息测试',1,2,0,'1970-01-21 02:21:18',0),(64,2356319379040768,'消息测试',1,2,0,'2024-02-12 03:03:50',0);
/*!40000 ALTER TABLE `t_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_post`
--

DROP TABLE IF EXISTS `t_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_post` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `poster_id` bigint NOT NULL COMMENT '贴主ID',
  `title` varchar(256) NOT NULL COMMENT '标题',
  `content` longtext COMMENT '帖子内容',
  `meet_address` varchar(512) DEFAULT NULL COMMENT '集合地点',
  `latitude` varchar(32) DEFAULT NULL COMMENT '纬度',
  `longitude` varchar(32) DEFAULT NULL COMMENT '经度',
  `begin_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态（0待匹配，1匹配完成）',
  `version` int DEFAULT '0' COMMENT '乐观锁',
  `create_by` bigint DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
  PRIMARY KEY (`id`),
  KEY `t_post_poster_id_index` (`poster_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='帖子表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_post`
--

LOCK TABLES `t_post` WRITE;
/*!40000 ALTER TABLE `t_post` DISABLE KEYS */;
INSERT INTO `t_post` VALUES (1,1,'找篮球搭子','找篮球搭子','松山湖',NULL,NULL,'2024-02-15 18:44:10','2024-03-15 18:44:21',0,0,NULL,'2024-02-06 05:10:08',NULL,'2024-02-06 05:09:56',0),(2,1,'华却次','tempor','澳门特别行政区吉林市纳雍县',NULL,NULL,'2024-02-15 18:44:12','2024-06-15 18:44:24',NULL,0,NULL,NULL,NULL,NULL,0),(3,1,'认精非','nisi adipisicing Excepteur dolore','西藏自治区佳木斯市耀州区',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,0),(4,1,'华却次','tempor','澳门特别行政区吉林市纳雍县',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,NULL,0),(5,1,'认精非','nisi adipisicing Excepteur dolore','西藏自治区佳木斯市耀州区',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,0),(6,1,'华却次','tempor','澳门特别行政区吉林市纳雍县',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,'2024-02-06 06:43:45',1),(7,1,'华却次','tempor','澳门特别行政区吉林市纳雍县',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,0),(8,1,'认精非','nisi adipisicing Excepteur dolore','西藏自治区佳木斯市耀州区',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,0),(9,1,'华却次','tempor','澳门特别行政区吉林市纳雍县',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,0),(10,1,'华却次','tempor','澳门特别行政区吉林市纳雍县',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,0),(11,1,'华却次','tempor','澳门特别行政区吉林市纳雍县',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,0),(12,1,'华却次','tempor','澳门特别行政区吉林市纳雍县',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,NULL,0),(13,1,'华却次','tempor','澳门特别行政区吉林市纳雍县',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,0),(14,1,'华却次','tempor','澳门特别行政区吉林市纳雍县',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,0),(15,1,'华却次','tempor','澳门特别行政区吉林市纳雍县',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,0),(16,1,'华却次','tempor','澳门特别行政区吉林市纳雍县',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,0),(21,2,'身总技条社布','qui laboris magna esse','山西省孝感市青铜峡市',NULL,NULL,'2022-05-21 13:04:46','2024-01-11 09:14:46',NULL,0,NULL,NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `t_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_post_tag`
--

DROP TABLE IF EXISTS `t_post_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_post_tag` (
  `post_id` bigint NOT NULL COMMENT '帖子ID',
  `tag_id` bigint NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`post_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='帖子标签关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_post_tag`
--

LOCK TABLES `t_post_tag` WRITE;
/*!40000 ALTER TABLE `t_post_tag` DISABLE KEYS */;
INSERT INTO `t_post_tag` VALUES (1,1),(2,1),(4,1),(4,2),(6,1),(7,1),(9,1),(10,1),(11,1),(12,1),(13,1),(14,1),(15,1),(16,1);
/*!40000 ALTER TABLE `t_post_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_post_user`
--

DROP TABLE IF EXISTS `t_post_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_post_user` (
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `status` tinyint NOT NULL COMMENT '0为帖子与用户匹配关系 1为帖子与用户收藏关系',
  PRIMARY KEY (`post_id`,`user_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='帖子与用户匹配成功关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_post_user`
--

LOCK TABLES `t_post_user` WRITE;
/*!40000 ALTER TABLE `t_post_user` DISABLE KEYS */;
INSERT INTO `t_post_user` VALUES (1,1,0),(1,2,0),(1,2,1),(4,1,1);
/*!40000 ALTER TABLE `t_post_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_tag`
--

DROP TABLE IF EXISTS `t_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL DEFAULT '' COMMENT '标签名',
  `create_by` bigint DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT '0',
  `update_time` datetime DEFAULT NULL,
  `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '删除标志（0为未删除，1为已删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_tag`
--

LOCK TABLES `t_tag` WRITE;
/*!40000 ALTER TABLE `t_tag` DISABLE KEYS */;
INSERT INTO `t_tag` VALUES (1,'篮球',0,NULL,0,NULL,0),(2,'足球',0,NULL,0,NULL,0);
/*!40000 ALTER TABLE `t_tag` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-07 22:49:41
