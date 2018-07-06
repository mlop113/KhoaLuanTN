/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.0.82-community-nt : Database - footballnews
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`footballnews` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `footballnews`;

/*Table structure for table `article` */

DROP TABLE IF EXISTS `article`;

CREATE TABLE `article` (
  `ArticleID` bigint(255) NOT NULL,
  `Title` varchar(100) default NULL,
  `Description` varchar(1000) default NULL,
  `Content` longtext,
  `CategoryID` bigint(255) default NULL,
  `DateCreate` varchar(100) default NULL,
  `CoverImage` varchar(1000) default NULL,
  `UserID` bigint(255) default NULL,
  `ContentOffLine` longtext,
  `CoverImageOffLine` blob,
  PRIMARY KEY  (`ArticleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `article` */

/*Table structure for table `article_bookmark` */

DROP TABLE IF EXISTS `article_bookmark`;

CREATE TABLE `article_bookmark` (
  `ArticleID` bigint(255) NOT NULL,
  `UserID` bigint(255) NOT NULL,
  PRIMARY KEY  (`ArticleID`,`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `article_bookmark` */

/*Table structure for table `article_like` */

DROP TABLE IF EXISTS `article_like`;

CREATE TABLE `article_like` (
  `ArticleID` bigint(255) NOT NULL,
  `UserID` bigint(255) NOT NULL,
  PRIMARY KEY  (`ArticleID`,`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `article_like` */

/*Table structure for table `article_tag` */

DROP TABLE IF EXISTS `article_tag`;

CREATE TABLE `article_tag` (
  `ArticleID` bigint(255) NOT NULL,
  `TagID` bigint(255) NOT NULL,
  PRIMARY KEY  (`ArticleID`,`TagID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `article_tag` */

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `CategoryID` bigint(255) NOT NULL,
  `Name` varchar(1000) collate utf8_unicode_ci default NULL,
  `Image` varchar(1000) collate utf8_unicode_ci default NULL,
  PRIMARY KEY  (`CategoryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `category` */

/*Table structure for table `comment` */

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `CommentID` bigint(255) NOT NULL,
  `Content` varchar(1000) default NULL,
  `DateCreate` varchar(100) default NULL,
  `UserID` bigint(255) default NULL,
  `ArticleID` bigint(255) default NULL,
  PRIMARY KEY  (`CommentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `comment` */

/*Table structure for table `comment_like` */

DROP TABLE IF EXISTS `comment_like`;

CREATE TABLE `comment_like` (
  `CommentID` bigint(255) NOT NULL,
  `UserID` bigint(255) NOT NULL,
  PRIMARY KEY  (`CommentID`,`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `comment_like` */

/*Table structure for table `feedback_comment` */

DROP TABLE IF EXISTS `feedback_comment`;

CREATE TABLE `feedback_comment` (
  `FeedbackCommentID` bigint(255) NOT NULL,
  `CommentID` bigint(255) default NULL,
  `Content` varchar(1000) default NULL,
  `DateCreate` varchar(100) default NULL,
  `UserID` bigint(255) default NULL,
  PRIMARY KEY  (`FeedbackCommentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `feedback_comment` */

/*Table structure for table `report_comment` */

DROP TABLE IF EXISTS `report_comment`;

CREATE TABLE `report_comment` (
  `ReportID` bigint(255) NOT NULL,
  `CommentID` bigint(255) default NULL,
  `Content` varchar(1000) default NULL,
  `DateCreate` varchar(100) default NULL,
  `UserID` bigint(255) default NULL,
  PRIMARY KEY  (`ReportID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `report_comment` */

/*Table structure for table `report_feedback` */

DROP TABLE IF EXISTS `report_feedback`;

CREATE TABLE `report_feedback` (
  `ReportID` bigint(255) NOT NULL,
  `FeedbackCommentID` bigint(255) default NULL,
  `Content` varchar(1000) default NULL,
  `DateCreate` varchar(100) default NULL,
  `UserID` bigint(255) default NULL,
  PRIMARY KEY  (`ReportID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `report_feedback` */

/*Table structure for table `tag` */

DROP TABLE IF EXISTS `tag`;

CREATE TABLE `tag` (
  `Name` varchar(1000) default NULL,
  `ArticleID` bigint(255) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tag` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `UserID` bigint(255) NOT NULL,
  `Email` varchar(100) default NULL,
  `FullName` varchar(100) default NULL,
  `Image` varchar(100) default NULL,
  `DateCreate` varchar(100) default NULL,
  `User_LevelID` int(100) default NULL,
  PRIMARY KEY  (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

/*Table structure for table `user_level` */

DROP TABLE IF EXISTS `user_level`;

CREATE TABLE `user_level` (
  `User_LevelID` int(100) NOT NULL,
  `Name` varchar(100) default NULL,
  PRIMARY KEY  (`User_LevelID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_level` */

insert  into `user_level`(`User_LevelID`,`Name`) values (1,'Member'),(2,'Vip'),(3,'Admin');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
