DROP SCHEMA IF EXISTS starter_db;
CREATE SCHEMA IF NOT EXISTS starter_db DEFAULT CHARACTER SET utf8;
USE starter_db;

CREATE TABLE IF NOT EXISTS starter_db.address (
  id INT NOT NULL AUTO_INCREMENT,  
  street VARCHAR(40) NOT NULL,
  city VARCHAR(40) NOT NULL,
  county VARCHAR(40) NOT NULL,
  postcode VARCHAR(8) NOT NULL,
  PRIMARY KEY (id));
  
CREATE TABLE IF NOT EXISTS starter_db.user_image (
  id INT NOT NULL AUTO_INCREMENT,
  s3_key VARCHAR(200) NOT NULL,
  url VARCHAR(2000) NOT NULL,
  PRIMARY KEY (id));  
  
CREATE TABLE IF NOT EXISTS starter_db.user (
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(30) NOT NULL,
  last_name VARCHAR(30) NOT NULL,
  email VARCHAR(30) NOT NULL,
  date_of_birth DATE NULL,
  user_image_id INT NULL,
  address_id INT NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_ADDRESS_ID
    FOREIGN KEY (address_id)
    REFERENCES starter_db.address (id),
  CONSTRAINT FK_USER_IMAGE_ID
    FOREIGN KEY (user_image_id)
    REFERENCES starter_db.user_image (id));    