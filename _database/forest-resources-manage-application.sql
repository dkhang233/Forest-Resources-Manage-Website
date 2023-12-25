DROP DATABASE IF EXISTS forest_resources_manage_application;
CREATE DATABASE forest_resources_manage_application;
USE forest_resources_manage_application;

-- -----------------------------------------------------
-- Table `administrative_types`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `administrative_levels`;
CREATE TABLE `administrative_levels` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL UNIQUE COMMENT 'tên các cấp độ hành chính',
    level INT NOT NULL COMMENT 'cấp',
    PRIMARY KEY (id)
)  ENGINE=INNODB CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_BIN COMMENT='cấp độ hành chính' ROW_FORMAT=DYNAMIC;
----------------
-- Records of `administrative_levels`
-- ----------------------------
INSERT INTO `administrative_levels`(`id`,`name`)
VALUES (1,'tỉnh',1),
(2,'thành phố',2),
(3,'huyện',2),
(4,'phường',3),
(5,'thị trấn',3),
(6,'xã',4);


-- -----------------------------------------------------
-- Table administrations
-- -----------------------------------------------------
DROP TABLE IF EXISTS `administrations`;
CREATE TABLE `administrations`(
    `code` VARCHAR(20) NOT NULL,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    `subordinate` VARCHAR(20) NULL COMMENT 'trực thuộc',
    `administrative_level_id` INT NOT NULL,
    PRIMARY KEY (`code`),
    FOREIGN KEY (`administrative_level_id`)
        REFERENCES `administrative_levels` (`id`),
	FOREIGN KEY (`subordinate`)
		REFERENCES `administrations`(`code`),
	INDEX (`name`) USING BTREE
)  ENGINE=INNODB CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_BIN COMMENT='hành chính';

-- ----------------------------
-- Records of administrations
-- ----------------------------
INSERT INTO `administrations`(`code`,`name`,`subordinate`,`administrative_level_id`)
VALUES ('35','Hà Nam',NULL,1),

	 ('347','Phủ Lý','35',2),
	 ('349','Duy Tiên','35',3),
	 ('350','Kim Bảng','35',3),
	 ('351','Thanh Liêm','35',3),
	 ('352','Bình Lục','35',3),
	 ('353','Lý Nhân','35',3),
     
     ('13306','Phù Vân','347',6),
     ('13309','Liêm Chính','347',4),
	 ('13312','Liêm Chung','347',6),
	 ('13315','Thanh Châu','347',4),
	 ('13318','Châu Sơn','347',4),
	 ('13366','Tiên Tân','347',6),
	 ('13372','Tiên Hiệp','347',6),
	 ('13381','Tiên Hải','347',6),
	 ('13426','Kim Bình','347',6),
	 ('13444','Liêm Tuyền','347',6),
	 ('13447','Liêm Tiết','347',6),
	 ('13459','Thanh Tuyền','347',4),
	 ('13507','Đinh Xá','347',6),
	 ('13513','Trịnh Xá','347',6),
	 ('13321','Đồng Văn','349',4),
	 ('13324','Hòa Mạc','349',4),
	 ('13327','Mộc Bắc','349',6),
	 ('13330','Châu Giang','349',4),
	 ('13333','Bạch Thượng','349',4),
	 ('13336','Duy Minh','349',4),
	 ('13339','Mộc Nam','349',6),
	 ('13342','Duy Hải','349',4),
	 ('13345','Chuyên Ngoại','349',6),
	 ('13348','Yên Bắc','349',4),
	 ('13351','Trác Văn','349',6),
	 ('13354','Tiên Nội','349',4),
	 ('13357','Hoàng Đông','349',4),
	 ('13360','Yên Nam','349',6),
	 ('13363','Tiên Ngoại','349',6),
	 ('13369','Tiên Sơn','349',6),
	 ('13384','Quế','350',5),
	 ('13387','Nguyễn Úy','350',6),
	 ('13390','Đại Cương','350',6),
	 ('13393','Lê Hồ','350',6),
	 ('13396','Tượng Lĩnh','350',6),
	 ('13399','Nhật Tựu','350',6),
	 ('13402','Nhật Tân','350',6),
	 ('13405','Đồng Hóa','350',6),
	 ('13408','Hoàng Tây','350',6),
	 ('13411','Tân Sơn','350',6),
	 ('13414','Thụy Lôi','350',6),
	 ('13417','Văn Xá','350',6),
	 ('13420','Khả Phong','350',6),
	 ('13423','Ngọc Sơn','350',6),
	 ('13429','Ba Sao','350',5),
	 ('13432','Liên Sơn','350',6),
	 ('13435','Thi Sơn','350',6),
	 ('13438','Thanh Sơn','350',6),
	 ('13441','Kiện Khê','351',5),
	 ('13450','Liêm Phong','351',6),
	 ('13453','Thanh Hà','351',6),
	 ('13456','Liêm Cần','351',6),
	 ('13465','Liêm Thuận','351',6),
	 ('13468','Thanh Thủy','351',6),
	 ('13471','Thanh Phong','351',6),
	 ('13474','Tân Thanh','351',5),
	 ('13477','Thanh Tân','351',6),
	 ('13480','Liêm Túc','351',6),
	 ('13483','Liêm Sơn','351',6),
	 ('13486','Thanh Hương','351',6),
	 ('13489','Thanh Nghị','351',6),
	 ('13492','Thanh Tâm','351',6),
	 ('13495','Thanh Nguyên','351',6),
	 ('13498','Thanh Hải','351',6),
	 ('13501','Bình Mỹ','352',5),
	 ('13504','Bình Nghĩa','352',6),
	 ('13510','Tràng An','352',6),
	 ('13516','Đồng Du','352',6),
	 ('13519','Ngọc Lũ','352',6),
	 ('13522','Hưng Công','352',6),
	 ('13525','Đồn Xá','352',6),
	 ('13528','An Ninh','352',6),
	 ('13531','Bồ Đề','352',6),
	 ('13534','Bối Cầu','352',6),
	 ('13540','An Nội','352',6),
	 ('13543','Vũ Bản','352',6),
	 ('13546','Trung Lương','352',6),
	 ('13552','An Đổ','352',6),
	 ('13555','La Sơn','352',6),
	 ('13558','Tiêu Động','352',6),
	 ('13561','An Lão','352',6),
	 ('13567','Hợp Lý','353',6),
	 ('13570','Nguyên Lý','353',6),
	 ('13573','Chính Lý','353',6),
	 ('13576','Chân Lý','353',6),
	 ('13579','Đạo Lý','353',6),
	 ('13582','Công Lý','353',6),
	 ('13585','Văn Lý','353',6),
	 ('13588','Bắc Lý','353',6),
	 ('13591','Đức Lý','353',6),
	 ('13594','Trần Hưng Đạo','353',6),
	 ('13597','Vĩnh Trụ','353',5),
	 ('13600','Nhân Thịnh','353',6),
	 ('13606','Nhân Khang','353',6),
	 ('13609','Nhân Mỹ','353',6),
	 ('13612','Nhân Nghĩa','353',6),
	 ('13615','Nhân Chính','353',6),
	 ('13618','Nhân Bình','353',6),
	 ('13621','Phú Phúc','353',6),
	 ('13624','Xuân Khê','353',6),
	 ('13627','Tiến Thắng','353',6),
	 ('13630','Hòa Hậu','353',6);

	 


-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
    `username` VARCHAR(100) NOT NULL,
    `password` VARCHAR(200) NOT NULL,
    `first_name` VARCHAR(100) NOT NULL,
    `last_name` VARCHAR(100) NOT NULL,
    `avatar` VARCHAR(150) NULL COMMENT 'ảnh đại diện',
    `email` VARCHAR(200) NOT NULL,
    `birth_date` DATE NOT NULL,
    `address` VARCHAR(100) NOT NULL,
    `is_active` INT NOT NULL DEFAULT 1 COMMENT 'Trạng thái tài khoản : hoạt động = 1 , không hoạt động = 0 ',
    `role` VARCHAR(50) NOT NULL COMMENT 'Vai trò : admin , user',
    `otp` VARCHAR(20) COMMENT 'mã otp',
    `otp_generated_time` DATETIME COMMENT 'thời gian tạo mã otp',
    `administration_code` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`username`),
    FOREIGN KEY (`administration_code`)
        REFERENCES `administrations`(`code`)
)  ENGINE=INNODB CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_BIN COMMENT='người dùng';

-- ----------------------------
-- Records of `users`
-- ----------------------------
INSERT INTO `users` (`username`,`password`,`first_name`,`last_name`,`email`,`avatar`,`birth_date`,`address`,`is_active`,`role`,`administration_code`)
VALUES ('admin','1','admin','admin','c.aodanhkhang2003@gmail.com','','2003-1-1','',1,'admin','35'),
('a','1','Nguyễn','A','caodanhkhang2003@gmail.com','','2003-1-1','',1,'user','347');

-- ('huy','1','Như','Huy','','','2003-1-1','Bắc Ninh',1,'user','35'),
-- ('khang','1','Danh','Khang','','2003-1-1','Hà Nam',1,'user','347'),
-- ('hminh','1','Hiếu','Minh','','2003-1-1','Hà Nam',1,'user','13306'),
-- ('truong','1','Đức','Trường','','2003-1-1','Hà Nam',1,'user','13306'),
-- ('lminh','1','Lê','Minh','','2003-1-1','Hải Dương',1,'user','13306');


-- ('b','1','Nguyễn','B','','2003-1-1','',1,'user','347'),
-- ('c','1','Nguyễn','C','','2003-1-1','',1,'user','347'),
-- ('d','1','Nguyễn','D','','2003-1-1','',1,'user','347'),
-- ('e','1','Nguyễn','E','','2003-1-1','',1,'user','347'),
-- ('f','1','Nguyễn','F','','2003-1-1','',1,'user','347'),


-- -----------------------------------------------------
-- Table Animal Storage Facilities 
-- -----------------------------------------------------
DROP TABLE IF EXISTS `animal_storage_facilities`;
CREATE TABLE `animal_storage_facilities`(
    `code` VARCHAR(20) NOT NULL,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    `date` DATE NULL,
    `capacity` LONG NOT NULL COMMENT 'sức chứa',
    `adminstration_code` VARCHAR(20) NULL COMMENT 'id hành chính',
    `detail` varchar(256) NULL COMMENT 'thông tin chi tiết',
    `lat` VARCHAR(100) NULL DEFAULT '' COMMENT 'vĩ độ',
    `lng` VARCHAR(100) NULL DEFAULT '' COMMENT 'kinh độ',
    PRIMARY KEY (`code`),
    FOREIGN KEY (`adminstration_code`)
        REFERENCES `administrations`(`code`)
)  ENGINE=INNODB CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_BIN COMMENT='Cơ sở lưu trữ động vật';

-- -----------------------------------------------------
-- Records of Animal Storage Facilities 
-- -----------------------------------------------------
INSERT INTO `animal_storage_facilities`(`code`,`name`,`date`,`capacity`,`adminstration_code`,`detail`)
VALUES ( 'AS001', 'Trung tâm chăm sóc và bảo tồn động vật Phù Vân' , '2021-1-1' , 1000 , '13306' , '(là cơ sở được cấp phép, hoạt động bằng nguồn vốn xã hội)' ),
( 'AS002', 'Viện nghiên cứu và bảo tồn động vật hoang dã Yên Bắc','2021-1-1' , 1000 , '13348' , '(là cơ sở cấp phép, bằng nguồn vốn cá nhân)' ),
('AS003', 'Trung tâm chăm sóc và bảo tồn động vật Nhân Khang', '2021-1-1', 1200, '13606', '(là cơ sở được cấp phép, hoạt động bằng nguồn vốn từ ngành công nghiệp)'),
('AS004', 'Trung tâm chăm sóc và bảo tồn động vật Bắc Lý', '2021-1-1', 800, '13588', '(là cơ sở cấp phép, sử dụng nguồn vốn từ quỹ phát triển khu vực)'),
('AS004', 'Khu chăm sóc và bảo tồn động vật Xuân Khê', '2021-1-1', 800, '13624', '(là cơ sở cấp phép, sử dụng nguồn vốn từ quỹ phát triển khu vực)'),
( 'AS005', 'Trung tâm chăm sóc và bảo tồn động vật Bình Mỹ' , '2021-1-1' , 1000 , '13501' , '(là cơ sở được cấp phép, hoạt động bằng nguồn vốn xã hội)' );

-- -----------------------------------------------------
-- Table fluctuation
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fluctuation`;
CREATE TABLE `fluctuation`(
    `id` INT NOT NULL,
    `name` VARCHAR(100) NOT NULL UNIQUE,
	`detail` VARCHAR(256) NOT NULL,
    PRIMARY KEY (`id`)
)  ENGINE=INNODB CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_BIN COMMENT='Loại biến động';

-- -----------------------------------------------------
-- Records of fluctuation
-- -----------------------------------------------------
INSERT INTO `fluctuation`(`id`,`name`,`detail`)
VALUES (1, 'Theo chu kì' , 'biến động xảy ra do những thay đổi có tính chu kì của môi trường như chu kì ngày, đêm, chu kì mùa hay chu kì thủy triều,…') ,
(2, 'Không theo chu kì' , 'biến động mà số lượng cá thể của quần thể tăng hoặc giảm
một cách đột ngột do những thay đổi bất thường của môi trường tự nhiên hay do hoạt
động khai thác tài nguyên quá mức của con người');

-- -----------------------------------------------------
-- Table Animal Species
-- -----------------------------------------------------
DROP TABLE IF EXISTS `animal_species`;
CREATE TABLE `animal_species`(
    `name` VARCHAR(100) NOT NULL,
    `animal_type` VARCHAR(100) NOT NULL COMMENT 'loài động vật',
	`image` VARCHAR(150) NULL COMMENT 'ảnh',
    `main_food` VARCHAR(100) NULL COMMENT 'thức ăn chính',
    `main_disease` VARCHAR(100) NULL COMMENT 'bệnh chính',
    `longevity` INT NULL COMMENT 'tuổi thọ',
	`fluctuation_id` INT NOT NULL ,
    PRIMARY KEY (`name`),
	FOREIGN KEY (`fluctuation_id`)
        REFERENCES `fluctuation`(`id`)
)  ENGINE=INNODB CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_BIN COMMENT='loài động vật';

-- -----------------------------------------------------
-- Table ASF_AS_relationship 
-- -----------------------------------------------------
DROP TABLE IF EXISTS `asf_as_relationship`;
CREATE TABLE `asf_as_relationship`(
    `id` INT NOT NULL,
    `animal_storage_facilities_code` VARCHAR(20) NOT NULL COMMENT 'code cơ sở lưu trữ động vật',
    `animal_species_name` VARCHAR(100) NOT NULL COMMENT 'tên loài động vật',
    `quantity` LONG NULL COMMENT 'số lượng',
    `date` DATE NULL COMMENT 'ngày',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`animal_storage_facilities_code`)
        REFERENCES `animal_storage_facilities`(`code`)
        ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`animal_species_name`)
        REFERENCES `animal_species`(`name`)
        ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE=INNODB CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_BIN COMMENT='bảng quan hệ CSLTDV - Loài động vật';

-- -----------------------------------------------------
-- Table Operation Form
-- -----------------------------------------------------
DROP TABLE IF EXISTS `operation_form`;
CREATE TABLE `operation_form`(
    `name` VARCHAR(100) NOT NULL,
    `decription` VARCHAR(256) NULL COMMENT 'mô tả',
    PRIMARY KEY (`name`)
)  ENGINE=INNODB CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_BIN COMMENT='hình thức hoạt động';

-- -----------------------------------------------------
-- Table Production Type
-- -----------------------------------------------------
DROP TABLE IF EXISTS `production_type`;
CREATE TABLE `production_type`(
    `wood_type` VARCHAR(100) NOT NULL COMMENT 'loại gỗ',
	`image` VARCHAR(150) NULL COMMENT 'ảnh',
    `capacity` LONG NULL COMMENT 'khả năng sản xuất',
    PRIMARY KEY (`wood_type`)
)  ENGINE=INNODB CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_BIN COMMENT='loại hình sản xuất';

-- -----------------------------------------------------
-- Table Wood Facilities
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wood_facilities`;
CREATE TABLE `wood_facilities`(
    `code` VARCHAR(20) NOT NULL,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    `date` DATE NULL,
	`capacity` LONG NOT NULL COMMENT 'sức chứa',
    `adminstration_code` VARCHAR(20) NULL COMMENT 'id hành chính',
    `operation_form_name` VARCHAR(100) NOT NULL COMMENT 'tên hình thức hoạt động',
    `lat` VARCHAR(100) NULL DEFAULT '' COMMENT 'vĩ độ',
    `lng` VARCHAR(100) NULL DEFAULT '' COMMENT 'kinh độ',
    PRIMARY KEY (`code`),
    FOREIGN KEY (`adminstration_code`)
        REFERENCES `administrations`(`code`),
	FOREIGN KEY (`operation_form_name`)
        REFERENCES `operation_form`(`name`)
)  ENGINE=INNODB CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_BIN COMMENT='cơ sở sản xuất chế biến gỗ';

-- -----------------------------------------------------
-- Table WF-PT Relationship
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wf_pt_relationship`;
CREATE TABLE `wf_pt_relationship`(
    `id` INT NOT NULL,
    `wood_facilities_code` VARCHAR(20) NOT NULL COMMENT 'code cơ sở lưu sản xuất chế biến gỗ',
    `production_type_name` VARCHAR(100) NOT NULL COMMENT 'tên loại hình sản xuất',
    `quantity` LONG NULL COMMENT 'số lượng',
    `date` DATE NULL COMMENT 'ngày',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`wood_facilities_code`)
        REFERENCES `wood_facilities`(`code`)
        ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`production_type_name`)
        REFERENCES `production_type`(`wood_type`)
        ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE=INNODB CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_BIN COMMENT='bảng quan hệ cơ sở gỗ - loại hình sản xuất';


-- -----------------------------------------------------
-- Table Plant Seed
-- -----------------------------------------------------
DROP TABLE IF EXISTS `plant_seed`;
CREATE TABLE `plant_seed`(
    `name` VARCHAR(100) NOT NULL,
    `type` VARCHAR(100) NULL COMMENT 'loại',
	`image` VARCHAR(150) NULL COMMENT 'ảnh',
    `soil_type` VARCHAR(100) NULL COMMENT 'loại đất',
    `main_pest` VARCHAR(100) NULL COMMENT 'sâu bệnh chính',
    `harvesting_period` INT NULL COMMENT 'thời gian thu hoạch',
    `plant_season` VARCHAR(100) NULL COMMENT 'mùa vụ',
    PRIMARY KEY (`name`)
)  ENGINE=INNODB CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_BIN COMMENT='giống cây trồng';

-- -----------------------------------------------------
-- Table Plant Facilities
-- -----------------------------------------------------
DROP TABLE IF EXISTS `plant_facilities`;
CREATE TABLE `plant_facilities`(
    `code` VARCHAR(20) NOT NULL,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    `date` DATE NULL,
	`capacity` long NOT NULL COMMENT 'sức chứa',
    `adminstration_code` VARCHAR(20) NULL COMMENT 'id hành chính',
    `lat` VARCHAR(100) NULL DEFAULT '' COMMENT 'vĩ độ',
    `lng` VARCHAR(100) NULL DEFAULT '' COMMENT 'kinh độ',
    PRIMARY KEY (`code`),
    FOREIGN KEY (`adminstration_code`)
        REFERENCES `administrations`(`code`)
)  ENGINE=INNODB CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_BIN COMMENT='cơ sở sản xuất giống cây trồng';

-- -----------------------------------------------------
-- Table PF-PS Relationship
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pf_ps_relationship`;
CREATE TABLE `pf_ps_relationship`(
    `id` INT NOT NULL,
    `plant_facilities_code` VARCHAR(20) NOT NULL COMMENT 'code cơ sở lưu sản xuất giống cây trồng',
    `plant_seed_name` VARCHAR(100) NOT NULL COMMENT 'tên giống cây trồng',
    `quantity` LONG NULL COMMENT 'số lượng',
    `date` DATE NULL COMMENT 'ngày',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`plant_facilities_code`)
        REFERENCES `plant_facilities`(`code`)
        ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (`plant_seed_name`)
        REFERENCES `plant_seed`(`name`)
        ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE=INNODB CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_BIN COMMENT='bảng quan hệ cơ sở giống cây trồng - giống cây trồng';

-- -----------------------------------------------------
-- Table Access History
-- -----------------------------------------------------
DROP TABLE IF EXISTS `access_history`;
CREATE TABLE `access_history`(
    `id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(100) NULL ,
    `method` VARCHAR(100) NOT NULL COMMENT 'hành động',
    `arguments` VARCHAR(256) NULL COMMENT 'đối số truyền vào',
    `time` VARCHAR(100) NOT NULL COMMENT 'thời gian bắt đầu thực hiện',
    `performance` LONG NULL COMMENT 'hiệu suất',
    PRIMARY KEY (`id`)
)  ENGINE=INNODB CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_BIN COMMENT='lịch sử truy cập';


-- -----------------------------------------------------
-- Table Access Log
-- -----------------------------------------------------
DROP TABLE IF EXISTS `access_log`;
CREATE TABLE `access_log` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(100) NOT NULL,
    `http_method` VARCHAR(10) NOT NULL,
    `request_url` VARCHAR(150) NOT NULL,
    `status_code` INT NOT NULL,
    `request_body` LONGTEXT,
    `response_body` LONGTEXT.
    FOREIGN KEY (`username`)
        REFERENCES `users` (`username`)
)  ENGINE=INNODB CHARACTER SET=UTF8MB4 COLLATE = UTF8MB4_BIN COMMENT='lịch sử tác động'
