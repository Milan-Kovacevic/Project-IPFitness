SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ip_fitness
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `ip_fitness` ;

-- -----------------------------------------------------
-- Schema ip_fitness
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ip_fitness` DEFAULT CHARACTER SET utf8 ;
USE `ip_fitness` ;

-- -----------------------------------------------------
-- Table `ip_fitness`.`FITNESS_USER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`FITNESS_USER` (
  `UserId` INT NOT NULL AUTO_INCREMENT,
  `Mail` VARCHAR(128) NOT NULL,
  `City` VARCHAR(128) NOT NULL,
  `Avatar` VARCHAR(128) NULL,
  `Username` VARCHAR(64) NOT NULL,
  `Password` VARCHAR(256) NOT NULL,
  `FirstName` VARCHAR(64) NOT NULL,
  `LastName` VARCHAR(96) NOT NULL,
  `Active` TINYINT(1) NOT NULL,
  `Enabled` TINYINT(1) NOT NULL,
  `Biography` VARCHAR(512) NULL,
  `ContactInfo` VARCHAR(128) NULL,
  PRIMARY KEY (`UserId`),
  UNIQUE INDEX `Username_UNIQUE` (`Username` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip_fitness`.`ADMINISTRATOR`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`ADMINISTRATOR` (
  `AdminId` INT NOT NULL AUTO_INCREMENT,
  `Username` VARCHAR(64) NOT NULL,
  `Password` VARCHAR(256) NOT NULL,
  `FirstName` VARCHAR(64) NOT NULL,
  `LastName` VARCHAR(96) NOT NULL,
  PRIMARY KEY (`AdminId`),
  UNIQUE INDEX `Username_UNIQUE` (`Username` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip_fitness`.`ADVISOR`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`ADVISOR` (
  `AdvisorId` INT NOT NULL AUTO_INCREMENT,
  `Username` VARCHAR(128) NOT NULL,
  `Password` VARCHAR(256) NOT NULL,
  `DisplayName` VARCHAR(128) NOT NULL,
  `Email` VARCHAR(128) NOT NULL,
  `AdminId` INT NOT NULL,
  PRIMARY KEY (`AdvisorId`),
  UNIQUE INDEX `Username_UNIQUE` (`Username` ASC) VISIBLE,
  INDEX `fk_ADVISOR_ADMINISTRATOR1_idx` (`AdminId` ASC) VISIBLE,
  CONSTRAINT `fk_ADVISOR_ADMINISTRATOR1`
    FOREIGN KEY (`AdminId`)
    REFERENCES `ip_fitness`.`ADMINISTRATOR` (`AdminId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip_fitness`.`CATEGORY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`CATEGORY` (
  `CategoryId` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`CategoryId`),
  UNIQUE INDEX `Name_UNIQUE` (`Name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip_fitness`.`FITNESS_PROGRAM`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`FITNESS_PROGRAM` (
  `ProgramId` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(128) NOT NULL,
  `Description` VARCHAR(512) NULL,
  `Price` DECIMAL(6,2) NOT NULL,
  `Difficulty` TINYINT(4) NOT NULL,
  `StartDate` DATETIME NOT NULL,
  `EndDate` DATETIME NOT NULL,
  `Location` TINYINT(4) NOT NULL,
  `CategoryId` INT NULL,
  `UserId` INT NOT NULL,
  `Deleted` TINYINT(1) NOT NULL,
  `CreatedAt` DATETIME NOT NULL,
  PRIMARY KEY (`ProgramId`),
  INDEX `fk_FITNESS_PROGRAM_CATEGORY1_idx` (`CategoryId` ASC) VISIBLE,
  INDEX `fk_FITNESS_PROGRAM_FITNESS_USER1_idx` (`UserId` ASC) VISIBLE,
  CONSTRAINT `fk_FITNESS_PROGRAM_CATEGORY1`
    FOREIGN KEY (`CategoryId`)
    REFERENCES `ip_fitness`.`CATEGORY` (`CategoryId`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_FITNESS_PROGRAM_FITNESS_USER1`
    FOREIGN KEY (`UserId`)
    REFERENCES `ip_fitness`.`FITNESS_USER` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip_fitness`.`COMMENT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`COMMENT` (
  `CommentId` INT NOT NULL AUTO_INCREMENT,
  `Content` VARCHAR(512) NOT NULL,
  `DatePosted` DATETIME NOT NULL,
  `UserId` INT NOT NULL,
  `ProgramId` INT NOT NULL,
  PRIMARY KEY (`CommentId`),
  INDEX `fk_COMMENT_FITNESS_USER1_idx` (`UserId` ASC) VISIBLE,
  INDEX `fk_COMMENT_FITNESS_PROGRAM1_idx` (`ProgramId` ASC) VISIBLE,
  CONSTRAINT `fk_COMMENT_FITNESS_USER1`
    FOREIGN KEY (`UserId`)
    REFERENCES `ip_fitness`.`FITNESS_USER` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_COMMENT_FITNESS_PROGRAM1`
    FOREIGN KEY (`ProgramId`)
    REFERENCES `ip_fitness`.`FITNESS_PROGRAM` (`ProgramId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip_fitness`.`CATEGORY_ATTRIBUTE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`CATEGORY_ATTRIBUTE` (
  `AttributeId` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(128) NOT NULL,
  `CategoryId` INT NOT NULL,
  PRIMARY KEY (`AttributeId`),
  INDEX `fk_CATEGORY_ATTRIBUTE_CATEGORY1_idx` (`CategoryId` ASC) VISIBLE,
  CONSTRAINT `fk_CATEGORY_ATTRIBUTE_CATEGORY1`
    FOREIGN KEY (`CategoryId`)
    REFERENCES `ip_fitness`.`CATEGORY` (`CategoryId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip_fitness`.`PROGRAM_PURCHASE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`PROGRAM_PURCHASE` (
  `PurchaseId` INT NOT NULL AUTO_INCREMENT,
  `PaymentType` TINYINT(4) NOT NULL,
  `Price` DECIMAL(6,2) NOT NULL,
  `UserId` INT NOT NULL,
  `ProgramId` INT NOT NULL,
  `PurchaseDate` DATETIME NOT NULL,
  PRIMARY KEY (`PurchaseId`),
  INDEX `fk_PROGRAM_PURCHASE_FITNESS_USER1_idx` (`UserId` ASC) VISIBLE,
  INDEX `fk_PROGRAM_PURCHASE_FITNESS_PROGRAM1_idx` (`ProgramId` ASC) VISIBLE,
  CONSTRAINT `fk_PROGRAM_PURCHASE_FITNESS_USER1`
    FOREIGN KEY (`UserId`)
    REFERENCES `ip_fitness`.`FITNESS_USER` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PROGRAM_PURCHASE_FITNESS_PROGRAM1`
    FOREIGN KEY (`ProgramId`)
    REFERENCES `ip_fitness`.`FITNESS_PROGRAM` (`ProgramId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip_fitness`.`QUESTION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`QUESTION` (
  `QuestionId` INT NOT NULL AUTO_INCREMENT,
  `UserId` INT NOT NULL,
  `IsRead` TINYINT(4) NOT NULL,
  `Content` VARCHAR(512) NOT NULL,
  `SendDate` DATETIME NOT NULL,
  PRIMARY KEY (`QuestionId`),
  INDEX `fk_QUESTION_FITNESS_USER1_idx` (`UserId` ASC) VISIBLE,
  CONSTRAINT `fk_QUESTION_FITNESS_USER1`
    FOREIGN KEY (`UserId`)
    REFERENCES `ip_fitness`.`FITNESS_USER` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip_fitness`.`PROGRAM_DEMONSTRATION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`PROGRAM_DEMONSTRATION` (
  `DemonstrationId` INT NOT NULL AUTO_INCREMENT,
  `VideoUrl` VARCHAR(256) NOT NULL,
  `OrderNumber` INT NOT NULL,
  `ProgramId` INT NOT NULL,
  PRIMARY KEY (`DemonstrationId`),
  INDEX `fk_ONLINE_DEMONSTARTION_FITNESS_PROGRAM1_idx` (`ProgramId` ASC) VISIBLE,
  CONSTRAINT `fk_ONLINE_DEMONSTARTION_FITNESS_PROGRAM1`
    FOREIGN KEY (`ProgramId`)
    REFERENCES `ip_fitness`.`FITNESS_PROGRAM` (`ProgramId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip_fitness`.`MESSAGE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`MESSAGE` (
  `MessageId` INT NOT NULL AUTO_INCREMENT,
  `Content` VARCHAR(512) NOT NULL,
  `TimeSent` DATETIME NOT NULL,
  `UserFromId` INT NOT NULL,
  `UserToId` INT NOT NULL,
  PRIMARY KEY (`MessageId`),
  INDEX `fk_MESSAGE_FITNESS_USER1_idx` (`UserFromId` ASC) VISIBLE,
  INDEX `fk_MESSAGE_FITNESS_USER2_idx` (`UserToId` ASC) VISIBLE,
  CONSTRAINT `fk_MESSAGE_FITNESS_USER1`
    FOREIGN KEY (`UserFromId`)
    REFERENCES `ip_fitness`.`FITNESS_USER` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_MESSAGE_FITNESS_USER2`
    FOREIGN KEY (`UserToId`)
    REFERENCES `ip_fitness`.`FITNESS_USER` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip_fitness`.`ACTIVITY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`ACTIVITY` (
  `ActivityId` INT NOT NULL AUTO_INCREMENT,
  `UserId` INT NOT NULL,
  `ActivityType` VARCHAR(128) NOT NULL,
  `DatePosted` DATETIME NOT NULL,
  `TrainingDuration` INT NOT NULL,
  `PercentageCompleted` INT NOT NULL,
  `Result` DECIMAL(6,3) NOT NULL,
  `Summary` VARCHAR(512) NULL,
  PRIMARY KEY (`ActivityId`),
  INDEX `fk_ACTIVITY_FITNESS_USER1_idx` (`UserId` ASC) VISIBLE,
  CONSTRAINT `fk_ACTIVITY_FITNESS_USER1`
    FOREIGN KEY (`UserId`)
    REFERENCES `ip_fitness`.`FITNESS_USER` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip_fitness`.`PROGRAM_PICTURE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`PROGRAM_PICTURE` (
  `PictureId` INT NOT NULL AUTO_INCREMENT,
  `PictureLocation` VARCHAR(256) NOT NULL,
  `OrderNumber` INT NOT NULL,
  `ProgramId` INT NOT NULL,
  PRIMARY KEY (`PictureId`),
  INDEX `fk_PICTURE_FITNESS_PROGRAM1_idx` (`ProgramId` ASC) VISIBLE,
  CONSTRAINT `fk_PICTURE_FITNESS_PROGRAM1`
    FOREIGN KEY (`ProgramId`)
    REFERENCES `ip_fitness`.`FITNESS_PROGRAM` (`ProgramId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip_fitness`.`TOKEN`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`TOKEN` (
  `TokenId` VARCHAR(128) NOT NULL,
  `Value` VARCHAR(512) NOT NULL,
  `TokenType` TINYINT(4) NOT NULL,
  `UserId` INT NOT NULL,
  PRIMARY KEY (`TokenId`),
  INDEX `fk_TOKEN_FITNESS_USER1_idx` (`UserId` ASC) VISIBLE,
  CONSTRAINT `fk_TOKEN_FITNESS_USER1`
    FOREIGN KEY (`UserId`)
    REFERENCES `ip_fitness`.`FITNESS_USER` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip_fitness`.`CATEGORY_SUBSCRIPTION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`CATEGORY_SUBSCRIPTION` (
  `UserId` INT NOT NULL,
  `CategoryId` INT NOT NULL,
  PRIMARY KEY (`UserId`, `CategoryId`),
  INDEX `fk_FITNESS_USER_has_CATEGORY_CATEGORY1_idx` (`CategoryId` ASC) VISIBLE,
  INDEX `fk_FITNESS_USER_has_CATEGORY_FITNESS_USER1_idx` (`UserId` ASC) VISIBLE,
  CONSTRAINT `fk_FITNESS_USER_has_CATEGORY_FITNESS_USER1`
    FOREIGN KEY (`UserId`)
    REFERENCES `ip_fitness`.`FITNESS_USER` (`UserId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_FITNESS_USER_has_CATEGORY_CATEGORY1`
    FOREIGN KEY (`CategoryId`)
    REFERENCES `ip_fitness`.`CATEGORY` (`CategoryId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip_fitness`.`ATTRIBUTE_VALUE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`ATTRIBUTE_VALUE` (
  `ValueId` INT NOT NULL AUTO_INCREMENT,
  `AttributeId` INT NOT NULL,
  `ProgramId` INT NOT NULL,
  `Value` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`ValueId`),
  INDEX `fk_ATTRIBUTE_VALUE_FITNESS_PROGRAM1_idx` (`ProgramId` ASC) VISIBLE,
  CONSTRAINT `fk_ATTRIBUTE_VALUE_CATEGORY_ATTRIBUTE1`
    FOREIGN KEY (`AttributeId`)
    REFERENCES `ip_fitness`.`CATEGORY_ATTRIBUTE` (`AttributeId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ATTRIBUTE_VALUE_FITNESS_PROGRAM1`
    FOREIGN KEY (`ProgramId`)
    REFERENCES `ip_fitness`.`FITNESS_PROGRAM` (`ProgramId`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ip_fitness`.`USER_ACTION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ip_fitness`.`USER_ACTION` (
  `ActionId` INT NOT NULL AUTO_INCREMENT,
  `IpAddress` VARCHAR(64) NOT NULL,
  `Endpoint` VARCHAR(128) NOT NULL,
  `Message` LONGTEXT NOT NULL,
  `Severity` TINYINT(4) NOT NULL,
  `ActionTime` DATETIME NOT NULL,
  PRIMARY KEY (`ActionId`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
