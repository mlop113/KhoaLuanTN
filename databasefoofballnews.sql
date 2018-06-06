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
  PRIMARY KEY  (`ArticleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `article` */

insert  into `article`(`ArticleID`,`Title`,`Description`,`Content`,`CategoryID`,`DateCreate`,`CoverImage`,`UserID`) values (1524658599895,'Hàng loạt nhà đất công tại TP HCM bị lãng phí','Diện tích đất công đang bị bỏ hoang rất lớn, sử dụng sai mục đích, cho thuê giá \"bèo\"... gây thất thoát lớn cho ngân sách.','<p>Đo&agrave;n gi&aacute;m s&aacute;t HĐND TP HCM chiều 24/4 l&agrave;m việc với Tổng C&ocirc;ng ty N&ocirc;ng nghiệp S&agrave;i G&ograve;n (Sagri) v&agrave; C&ocirc;ng ty Dược S&agrave;i G&ograve;n (Sapharco) về việc quản l&yacute; v&agrave; sử dụng nguồn t&agrave;i nguy&ecirc;n đất do Nh&agrave; nước quản l&yacute;. Động th&aacute;i n&agrave;y nằm trong hoạt động thường ni&ecirc;n của HĐND TP HCM.</p>\r\n\r\n<p>Sagri hiện quản l&yacute; 45 nh&agrave; đất, tổng diện t&iacute;ch hơn 62 triệu m2. Trong đ&oacute;, khối Văn ph&ograve;ng l&agrave; 38 nh&agrave; đất với tổng diện t&iacute;ch hơn 3 triệu m2; số c&ograve;n lại do 3 c&ocirc;ng ty con quản l&yacute; (C&ocirc;ng ty B&ograve; Sữa v&agrave; C&acirc;y trồng chiếm số lượng lớn).</p>\r\n\r\n<p><img alt=\"\" border=\"0\" hspace=\"0\" src=\"https://i-vnexpress.vnecdn.net/2018/04/24/dat-cong-san-JPG-3930-15241400-1660-4333-1524577639.jpg\" style=\"border:0px solid black; height:392px; margin-bottom:0px; margin-left:0px; margin-right:0px; margin-top:0px; width:640px\" vspace=\"0\" /></p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>Theo Tổng Gi&aacute;m đốc Sagri L&ecirc; Tấn H&ugrave;ng, hiện diện t&iacute;ch đất c&acirc;y trồng thực tế c&ograve;n lớn nhưng đa phần kho&aacute;n hết. Đất sản xuất c&ograve;n rất &iacute;t, trồng c&acirc;y kh&ocirc;ng c&oacute; gi&aacute; trị cao.</p>\r\n\r\n<p>C&aacute;c đại biểu đề nghị Sagri l&agrave;m r&otilde; số lượng hộ d&acirc;n thật sự canh t&aacute;c tr&ecirc;n đất được kho&aacute;n như hợp đồng, hay cho người kh&aacute;c thu&ecirc; lại, khả năng thu hồi những diện t&iacute;ch sử dụng sai mục đ&iacute;ch... Ngo&agrave;i ra, đại biểu cũng ph&aacute;n ảnh c&oacute; t&igrave;nh trạng đất c&ocirc;ng do Sagri quản l&yacute; đ&atilde; để người d&acirc;n chuy&ecirc;n đổi mục đ&iacute;ch sử dụng đất kh&ocirc;ng đ&uacute;ng hợp đồng; lấn chiếm, x&acirc;y dựng tr&aacute;i ph&eacute;p.</p>\r\n\r\n<p>Đối với Sapharco, đo&agrave;n gi&aacute;m s&aacute;t y&ecirc;u cầu b&aacute;o c&aacute;o cụ thể hơn về c&ocirc;ng t&aacute;c quản l&yacute; v&agrave; sử dụng hơn 30 nh&agrave; đất c&ocirc;ng đang quản l&yacute;.</p>\r\n\r\n<p><strong>H&agrave;ng loạt khu đất cho thu&ecirc; sai gi&aacute; quy định</strong></p>\r\n',2,'25-04-2018 19:15:00','https://i-vnexpress.vnecdn.net/2018/04/24/dat-cong-san-JPG-3930-15241400-1660-4333-1524577639.jpg',1),(1524726146306,'VKS Cấp cao đề nghị bác tất cả kháng cáo của ông Hà Văn Thắm','Khẳng định bản án sơ thẩm không tuyên oan, không sai, VKS đề nghị giữ nguyên mức hình phạt chung thân với cựu chủ tịch Oceanbank Hà Văn Thắm.','<p>S&aacute;ng 26/4, đề nghị hướng giải quyết với nguyện vọng kh&aacute;ng c&aacute;o của 26 người trong vụ &aacute;n xảy ra tại Oceanbank, đại diện VKS Cấp cao cho rằng bản &aacute;n sơ thẩm do TAND H&agrave; Nội tuy&ecirc;n với c&aacute;c bị l&agrave; c&oacute; căn cứ, kh&ocirc;ng oan sai, mức h&igrave;nh phạt c&oacute; sự ph&acirc;n h&oacute;a hợp l&yacute;.</p>\r\n\r\n<p>Do vậy, VKS đề nghị t&ograve;a ph&uacute;c thẩm kh&ocirc;ng chấp nhận kh&aacute;ng c&aacute;o&nbsp;của 20 bị c&aacute;o trong đ&oacute; c&oacute; c&aacute;c &ocirc;ng, b&agrave;: H&agrave; Văn Thắm, Nguyễn Xu&acirc;n Sơn (cựu tổng gi&aacute;m đốc Oceanbank), Nguyễn Minh Thu (cựu tổng gi&aacute;m đốc Oceanbank), L&ecirc; Thị Thu Thủy (cựu ph&oacute; tổng gi&aacute;m đốc Oceanbank), Nguyễn Văn Ho&agrave;n (cựu ph&oacute; tổng gi&aacute;m đốc Oceanbank), Hứa Thị Phấn (cựu chủ tịch HĐQT C&ocirc;ng ty Ph&uacute; Mỹ), Phạm C&ocirc;ng Danh (cựu chủ tịch Tập đo&agrave;n Thi&ecirc;n Thanh) c&ugrave;ng một số l&atilde;nh đạo chi nh&aacute;nh, ph&ograve;ng giao dịch Oceanbank.</p>\r\n\r\n<p>S&aacute;u người c&ograve;n lại được VKS đề nghị giảm h&igrave;nh phạt, gồm Vũ Th&ugrave;y Dương, Đỗ Đại Kh&ocirc;i Trang, Nguyễn Ho&agrave;i Nam, Nguyễn Thị Loan (c&aacute;c cựu gi&aacute;m đốc khối hội sở Oceanbank) c&ugrave;ng&nbsp;Trần Anh Thiết, Nguyễn Phan Trung Ki&ecirc;n (l&atilde;nh đạo cấp chi nh&aacute;nh).&nbsp;VKS cho hay đề nghị chấp nhận một phần kh&aacute;ng c&aacute;o nhưng kh&ocirc;ng chấp nhận nguyện vọng miễn tr&aacute;ch nhiệm h&igrave;nh sự hoặc đổi h&igrave;nh phạt.</p>\r\n\r\n<p>Theo VKS, suốt bốn ng&agrave;y tranh tụng v&agrave; cho tới tận s&aacute;ng nay, c&aacute;c bị c&aacute;o kh&ocirc;ng tr&igrave;nh b&agrave;y th&ecirc;m được t&igrave;nh tiết n&agrave;o mới.</p>\r\n\r\n<p>Trong suốt gần một tiếng VKS đọc bản luận tội, &ocirc;ng Thắm đứng ngay ngắn, hướng &aacute;nh mắt về ph&iacute;a c&ocirc;ng tố vi&ecirc;n chăm ch&uacute; nghe. &Ocirc;ng Sơn đứng thả lỏng nhưng kh&ocirc;ng nh&igrave;n VKS. B&agrave; Thu sau biết bị b&aacute;c kh&aacute;ng c&aacute;o th&igrave; ngồi lặng im, gương mặt thất thần. Nhiều bị c&aacute;o kh&aacute;c c&uacute;i thấp đầu.</p>\r\n\r\n<p><img alt=\"\" border=\"0\" hspace=\"0\" src=\"https://i-vnexpress.vnecdn.net/2018/04/26/IMG-3965-JPG-2083-1524711458.jpg\" style=\"border:0px solid black; height:67%; margin-bottom:0px; margin-left:0px; margin-right:0px; margin-top:0px; width:100%\" vspace=\"0\" /><strong>D&ugrave;ng t&agrave;i sản chung tạo lợi &iacute;ch c&aacute; nh&acirc;n</strong></p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>Với nh&oacute;m bị c&aacute;o bị tuy&ecirc;n phạm tội Cố &yacute; l&agrave;m tr&aacute;i quy định của nh&agrave; nước về quản l&yacute; kinh tế (điều 165 Bộ luật H&igrave;nh sự 1999), VKS nhận định cấp sơ thẩm &quot;c&oacute; căn cứ&quot; quy buộc &ocirc;ng Thắm giữ vai tr&ograve; đứng đầu, chỉ đạo c&aacute;c thuộc cấp chi hơn 1.500 tỷ đồng l&atilde;i suất ngo&agrave;i hợp đồng, g&acirc;y thiệt hại cho Oceanbank.&nbsp;Việc n&agrave;y&nbsp;g&acirc;y rối loạn an ninh tiền tệ, khiến Oceanbank bị mất kiểm so&aacute;t, nợ xấu chiếm tới gần một nửa dư nợ to&agrave;n hệ thống.&nbsp;</p>\r\n\r\n<p>Kết quả điều tra v&agrave; x&eacute;t hỏi c&ocirc;ng khai tại phi&ecirc;n t&ograve;a cho thấy, ngo&agrave;i số tiền chi l&atilde;i suất ngo&agrave;i hợp đồng cho c&aacute;c kh&aacute;ch h&agrave;ng c&aacute; nh&acirc;n, OceanBank đ&atilde; chi l&atilde;i suất ngo&agrave;i hợp đồng cho c&aacute;c tổ chức kinh tế, đa số l&agrave; c&aacute;c tổ chức kinh tế c&oacute; vốn nh&agrave; nước, nhưng thực chất l&agrave; chi cho l&atilde;nh đạo. Điều n&agrave;y tạo ra lợi &iacute;ch nh&oacute;m, lợi dụng t&agrave;i sản chung để trục lợi c&aacute; nh&acirc;n, tiếp tay cho tham nhũng.</p>\r\n\r\n<p>VKS cho rằng c&oacute; đủ căn cứ kết luận c&aacute;c bị c&aacute;o L&ecirc; Thị Thu Thủy, Nguyễn Minh Thu, Đỗ Đại Kh&ocirc;i Trang, Nguyễn Ho&agrave;i Nam, Nguyễn Thị Thu Ba, Nguyễn Thị Nga l&agrave; đồng phạm gi&uacute;p sức t&iacute;ch cực cho &ocirc;ng Thắm trong h&agrave;nh vi cố &yacute; chi l&atilde;i ngo&agrave;i hợp đồng&nbsp;v&agrave; phải chịu tr&aacute;ch nhiệm với số tiền từ hơn 100 tỷ đồng tới hơn 700 tỷ đồng.</p>\r\n\r\n<p>Nh&oacute;m c&aacute;c bị c&aacute;o l&agrave; l&atilde;nh đạo chi nh&aacute;nh, ph&ograve;ng giao dịch Oceanbank phải chịu tr&aacute;ch nhiệm h&igrave;nh sự với c&aacute;c số tiền đ&atilde; chi l&atilde;i ngo&agrave;i, mỗi người từ 1 đến 15 tỷ đồng.&nbsp;&nbsp;</p>\r\n\r\n<p><strong>Oceanbank mất 500 tỷ đồng v&igrave; cho vay kh&ocirc;ng đủ điều kiện</strong></p>\r\n\r\n<p>Với nh&oacute;m bị c&aacute;o kh&aacute;ng c&aacute;o về tội Vi phạm quy định cho vay g&acirc;y thiệt hại 500 tỷ đồng cho Oceanbank, VKS x&aacute;c định Oceanbank cho c&ocirc;ng ty Trung Dung vay khi kh&ocirc;ng đủ điều kiện vay, t&agrave;i sản đảm bảo kh&ocirc;ng đủ l&agrave; c&oacute; cơ sở&nbsp;song thực tế l&agrave; tiền đổ về cho &ocirc;ng Phạm C&ocirc;ng Danh.&nbsp;</p>\r\n\r\n<p>Theo VKS ph&uacute;c thẩm, cấp sơ thẩm c&oacute; căn cứ khi kết luận c&aacute;c &ocirc;ng, b&agrave; Hứa Thị Phấn, Phạm C&ocirc;ng Danh, Trần Văn B&igrave;nh, H&agrave; Văn Thắm đ&atilde; b&agrave;n bạc sử dụng t&agrave;i sản kh&ocirc;ng đảm bảo để thế chấp cho khoản vay tr&ecirc;n. &Ocirc;ng Thắm giữ vai tr&ograve; ch&iacute;nh, b&agrave; Phấn l&agrave; người gi&uacute;p sức, sử dụng 500 tỷ đồng chiếm đoạt. C&aacute;c bị c&aacute;o Phạm C&ocirc;ng Danh, Nguyễn Văn Ho&agrave;n, Trần Văn B&igrave;nh gi&uacute;p sức t&iacute;ch cực.</p>\r\n\r\n<p>Trong sai phạm n&agrave;y, cơ quan c&ocirc;ng tố b&aacute;c to&agrave;n bộ lập luận cũng như kh&aacute;ng c&aacute;o của c&aacute;c cổ đ&ocirc;ng Oceanbank, người c&oacute; quyền, nghĩa vụ li&ecirc;n quan.</p>\r\n\r\n<p>Chiều nay, HĐXX nghe nội dung b&agrave;o chữa của c&aacute;c luật sư trước c&aacute;c quan điểm tr&ecirc;n của VKS.<img alt=\"\" border=\"0\" hspace=\"0\" src=\"https://i-vnexpress.vnecdn.net/2018/04/26/IMG-3943-JPG-4732-1524711583.jpg\" style=\"border:0px solid black; height:67%; margin-bottom:0px; margin-left:0px; margin-right:0px; margin-top:0px; width:100%\" vspace=\"0\" /></p>\r\n\r\n<p>Theo &aacute;n sơ thẩm cựu chủ tịch Oceanbank H&agrave; Văn Thắm bị tuy&ecirc;n phạt h&igrave;nh phạt chung l&agrave; t&ugrave; chung th&acirc;n v&igrave; bốn tội: Cố &yacute; l&agrave;m tr&aacute;i quy định của Nh&agrave; nước về quản l&yacute; kinh tế g&acirc;y hậu quả nghi&ecirc;m trọng (19 năm t&ugrave;), Tham &ocirc; t&agrave;i sản (t&ugrave; chung th&acirc;n), Lạm dụng chức vụ, quyền hạn chiếm đoạt t&agrave;i sản (20 năm t&ugrave;), Vi phạm quy định về cho vay trong hoạt động c&aacute;c tổ chức t&iacute;n dụng (18 năm t&ugrave;).</p>\r\n\r\n<p>&Ocirc;ng Sơn bị phạt tử h&igrave;nh v&igrave; ba tội: Tham &ocirc; t&agrave;i sản, Cố &yacute; l&agrave;m tr&aacute;i, Lạm dụng chức vụ quyền hạn chiếm đoạt t&agrave;i sản.</p>\r\n\r\n<p>24 bị c&aacute;o c&ograve;n lại bị tuy&ecirc;n mức h&igrave;nh phạt từ 24 th&aacute;ng cải tạo kh&ocirc;ng giam giữ tới 22 năm t&ugrave; giam về c&aacute;c tội: Lạm dụng chức vụ quyền hạn chiếm đoạt t&agrave;i sản, Cố &yacute; l&agrave;m tr&aacute;i, Vi phạm quy định về cho vay.</p>\r\n\r\n<p>Tại phi&ecirc;n ph&uacute;c thẩm &ocirc;ng Thắm kh&aacute;ng c&aacute;o xin xem x&eacute;t hai tội danh Tham &ocirc; t&agrave;i sản v&agrave; Lạm dụng chức vụ quyền hạn chiếm đoạt t&agrave;i sản; xin xem x&eacute;t tr&aacute;ch nhiệm bồi thường d&acirc;n sự với tội Cố &yacute; l&agrave;m tr&aacute;i quy định của Nh&agrave; nước về quản l&yacute; kinh tế g&acirc;y hậu quả nghi&ecirc;m trọng.</p>\r\n\r\n<p>&Ocirc;ng Nguyễn Xu&acirc;n Sơn kh&aacute;ng c&aacute;o k&ecirc;u oan với hai tội Tham &ocirc; v&agrave; Lạm dụng, xem x&eacute;t phần tr&aacute;ch nhiệm d&acirc;n sự.</p>\r\n\r\n<p><iframe allowfullscreen=\"\" frameborder=\"0\" height=\"360\" src=\"https://www.youtube.com/embed/UxZ7yMt6TTg\" width=\"640\"></iframe></p>\r\n\r\n<p>Nh&oacute;m c&aacute;c bị c&aacute;o l&agrave; l&atilde;nh đạo chi nh&aacute;nh, ph&ograve;ng giao dịch Oceanbank tr&ecirc;n cả nước kh&aacute;ng c&aacute;o xin miễn h&igrave;nh phạt, tr&aacute;ch nhiệm h&igrave;nh sự, đổi h&igrave;nh phạt&hellip;</p>\r\n',1,'26-04-2018 13:59:14','https://i-vnexpress.vnecdn.net/2018/04/26/IMG3965JPG-1524711320-4231-1524711458_500x300.jpg',1),(1525363275865,'Thứ trưởng Xây dựng: \'Bản đồ Thủ Thiêm bị thất lạc đã hết hiệu lực\'','Lãnh đạo Bộ Xây dựng cho rằng dự án khu đô thị Thủ Thiêm được triển khai trên cơ sở bản đồ phê duyệt năm 2005 chứ không phải 1996.','<p>Tại cuộc họp b&aacute;o của Văn ph&ograve;ng Ch&iacute;nh phủ chiều 3/5,&nbsp;<em>VnExpress</em>&nbsp;đ&atilde; n&ecirc;u c&acirc;u hỏi về việc thất lạc bản đồ quy hoạch tỷ lệ 1/5.000 Khu đ&ocirc; thị Thủ Thi&ecirc;m (TP HCM) v&agrave; đề nghị đại diện Ch&iacute;nh phủ cho biết &yacute; kiến.</p>\r\n\r\n<p>Thứ trưởng Bộ X&acirc;y dựng L&ecirc; Quang H&ugrave;ng trả lời, theo quy tr&igrave;nh, khu đ&ocirc; thị Thủ Thi&ecirc;m triển khai theo hai bước l&agrave; quy hoạch chung v&agrave; quy hoạch chi tiết. Quy hoạch chung l&agrave; bản đồ 1/5.000 v&agrave; quy hoạch chi tiết l&agrave; bản đồ 1/2.000, sau đ&oacute; cụ thể ho&aacute; v&agrave; ph&acirc;n giới cắm mốc tr&ecirc;n thực địa. Quy hoạch sau ch&iacute;nh x&aacute;c ho&aacute; quy hoạch trước.</p>\r\n\r\n<p>&quot;Đ&ocirc; thị mới Thủ Thi&ecirc;m được điều chỉnh hai lần, lần đầu ti&ecirc;n l&agrave; quy hoạch chung năm 1996 v&agrave; lần thứ hai l&agrave; điều chỉnh quy hoạch năm 2005. Như vậy ở Thủ Thi&ecirc;m c&oacute; nhiều bản đồ&quot;, &ocirc;ng H&ugrave;ng cho hay.</p>\r\n\r\n<p><img alt=\"\" border=\"0\" hspace=\"0\" src=\"https://i-vnexpress.vnecdn.net/2018/05/03/Xa-y-du-ng-4-4618-1525352873.jpg\" style=\"border:0px solid black; height:76%; margin-bottom:0px; margin-left:0px; margin-right:0px; margin-top:0px; width:100%\" vspace=\"0\" /></p>\r\n\r\n<p>Theo Thứ trưởng X&acirc;y dựng, hiện qu&aacute; tr&igrave;nh triển khai dự &aacute;n, x&aacute;c định ranh giới, thu hồi mặt bằng,... của khu đ&ocirc; thị Thủ Thi&ecirc;m đều thực hiện tr&ecirc;n quy hoạch chung được ph&ecirc; duyệt năm 2005.</p>\r\n\r\n<p>&quot;Tất cả bản đồ c&oacute; căn cứ ph&aacute;p l&yacute; từ 2005, như bản đồ quy hoạch chung, chi tiết, x&aacute;c định ranh giới hiện giữ đầy đủ, việc triển khai dự &aacute;n, thu hồi đất l&agrave; dựa tr&ecirc;n cơ sở c&aacute;c bản đồ n&agrave;y. Bản đồ thất lạc l&agrave; bản đồ quy hoạch chung được ph&ecirc; duyệt năm 1996, về ph&aacute;p l&yacute; đ&atilde; được thay thế bởi quy hoạch chung năm 2005&quot;, &ocirc;ng H&ugrave;ng khẳng định.</p>\r\n\r\n<p>Đối với việc thất lạc t&agrave;i liệu, Thứ trưởng X&acirc;y dựng n&oacute;i ảnh hưởng như thế n&agrave;o v&agrave; tr&aacute;ch nhiệm thuộc về ai, li&ecirc;n quan đến triển khai chi tiết của quy hoạch trước ra sao, tất cả sẽ được cơ quan chức năng xem x&eacute;t l&agrave;m r&otilde;.</p>\r\n',1,'03-05-2018 22:55:24','https://i-vnexpress.vnecdn.net/2018/05/03/Xa-y-du-ng-4-4618-1525352873.jpg',1),(1526568354726,'123','123','<p>123</p>\r\n',1,'17-05-2018 21:42:53','',1),(1526568365321,'456','456','<p>456</p>\r\n',1,'17-05-2018 21:45:55','',1),(1526568440066,'789','789','<p>789</p>\r\n',1,'17-05-2018 21:46:06','',1);

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

insert  into `category`(`CategoryID`,`Name`,`Image`) values (1,'Thời sự','images/upload/global-network_303.jpg'),(2,'Thế giới','images/upload/global-network_303.jpg'),(3,'Xe','images/upload/global-network_303.jpg'),(4,'Sức Khỏe','images/upload/global-network_303.jpg'),(1525363719551,'ãcxxcx','images/upload/26168470_1238194766325153_3138664383350499444_n.jpg'),(1525363746393,'adasdsad','https://i-vnexpress.vnecdn.net/2018/05/03/Xa-y-du-ng-4-4618-1525352873.jpg'),(1526223472158,NULL,'');

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

insert  into `comment`(`CommentID`,`Content`,`DateCreate`,`UserID`,`ArticleID`) values (1526565284344,'123123','17-05-2018 20:54:44',1,1524658599895),(1526565305567,'22222','17-05-2018 20:55:05',1,1524658599895),(1526565310263,'3333333333','17-05-2018 20:55:10',1,1524658599895),(1526565313350,'44444444444','17-05-2018 20:55:13',1,1524658599895);

/*Table structure for table `commentlike` */

DROP TABLE IF EXISTS `commentlike`;

CREATE TABLE `commentlike` (
  `CommentID` bigint(255) NOT NULL,
  `UserID` bigint(255) NOT NULL,
  PRIMARY KEY  (`CommentID`,`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `commentlike` */

/*Table structure for table `feedbackcomment` */

DROP TABLE IF EXISTS `feedbackcomment`;

CREATE TABLE `feedbackcomment` (
  `FeedbackCommentID` bigint(255) NOT NULL,
  `CommentID` bigint(255) default NULL,
  `Content` varchar(1000) default NULL,
  `DateCreate` varchar(100) default NULL,
  `UserID` bigint(255) default NULL,
  PRIMARY KEY  (`FeedbackCommentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `feedbackcomment` */

/*Table structure for table `report` */

DROP TABLE IF EXISTS `report`;

CREATE TABLE `report` (
  `ReportID` bigint(255) NOT NULL,
  `CommentID` bigint(255) default NULL,
  `Content` varchar(1000) default NULL,
  `DateCreate` varchar(100) default NULL,
  `UserID` bigint(255) default NULL,
  PRIMARY KEY  (`ReportID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `report` */

/*Table structure for table `tag` */

DROP TABLE IF EXISTS `tag`;

CREATE TABLE `tag` (
  `Name` varchar(1000) default NULL,
  `ArticleID` bigint(255) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tag` */

insert  into `tag`(`Name`,`ArticleID`) values ('1',1526568354726),('2',1526568354726),('3',1526568354726),('4',1526568365321),('5',1526568365321),('6',1526568365321),('2',1526568440066),('4',1526568440066),('3',323232),('4',54545);

/*Table structure for table `transaction` */

DROP TABLE IF EXISTS `transaction`;

CREATE TABLE `transaction` (
  `TransactionID` bigint(255) NOT NULL,
  `MemberID` bigint(255) default NULL,
  `Money` varchar(100) default NULL,
  `Unit` varchar(100) default NULL,
  `Content` varchar(1000) default NULL,
  `AccountCode` varchar(100) default NULL,
  `Bank` varchar(100) default NULL,
  `DateCrate` varchar(100) default NULL,
  `Note` varchar(1000) default NULL,
  PRIMARY KEY  (`TransactionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `transaction` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `UserID` bigint(255) NOT NULL,
  `Email` varchar(100) default NULL,
  `Password` varchar(100) default NULL,
  `FullName` varchar(100) default NULL,
  `Image` varchar(1000) default NULL,
  `DateCreate` varchar(100) default NULL,
  `User_LevelID` int(100) default NULL,
  `Note` varchar(1000) default NULL,
  `Status` varchar(1000) default NULL,
  PRIMARY KEY  (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`UserID`,`Email`,`Password`,`FullName`,`Image`,`DateCreate`,`User_LevelID`,`Note`,`Status`) values (1,'congtuhot9.9@gmail.com','123123','Ngọc Hải','images/upload/ic_todolist.png','25/04/2018 21:43:00',1,'','');

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
