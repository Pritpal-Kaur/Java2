DROP database IF EXISTS superherosightingsdb ;

CREATE database superherosightingsdb;
USE superherosightingsdb ;

CREATE TABLE superpower(
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE hero (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) NOT NULL,
  `description` VARCHAR(150) NULL,
  `superpowerId` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_hero_superpower1_idx` (`superpowerId` ASC),
  CONSTRAINT `fk_hero_superpower1`
    FOREIGN KEY (`superpowerId`)
    REFERENCES `superpower` (`id`));

CREATE TABLE Location(
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(150) NULL,
  `addressInfo` VARCHAR(150) NOT NULL,
  `latitude` VARCHAR(10) NULL,
  `longitude` VARCHAR(10) NULL,
  PRIMARY KEY (`id`));

CREATE TABLE Organization(
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(150) NULL,
  `address` VARCHAR(150) NULL,
  `contact` VARCHAR(20) NULL,
  PRIMARY KEY (`id`));

CREATE TABLE sighting(
  `heroId` INT NOT NULL,
  `locationId` INT NOT NULL,
  `date` DATETIME NOT NULL,
  `id` INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  INDEX `fk_hero_has_Location_Location1_idx` (`locationId` ASC),
  INDEX `fk_hero_has_Location_hero_idx` (`heroId` ASC),
  CONSTRAINT `fk_hero_has_Location_hero`
    FOREIGN KEY (`heroId`)
    REFERENCES `hero` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_hero_has_Location_Location1`
    FOREIGN KEY (`locationId`)
    REFERENCES `Location` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE OrganizationMember(
  `organizationId` INT NOT NULL,
  `heroId` INT NOT NULL,
  PRIMARY KEY (`organizationId`, `heroId`),
  INDEX `fk_Organization_has_hero_hero1_idx` (`heroId` ASC),
  INDEX `fk_Organization_has_hero_Organization1_idx` (`organizationId` ASC),
  CONSTRAINT `fk_Organization_has_hero_Organization1`
    FOREIGN KEY (`organizationId`)
    REFERENCES `Organization` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Organization_has_hero_hero1`
    FOREIGN KEY (`heroId`)
    REFERENCES `hero` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);