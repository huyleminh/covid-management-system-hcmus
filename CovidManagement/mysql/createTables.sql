CREATE SCHEMA IF NOT EXISTS COVID_MANAGEMENT
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_bin;

USE COVID_MANAGEMENT;


-- ------------------------------------------------ --
--                  CREATE TABLE                    --
-- ------------------------------------------------ --

-- Table: Account
CREATE TABLE IF NOT EXISTS COVID_MANAGEMENT.`Account` (
    username VARCHAR(12) NOT NULL,
    `password` VARCHAR(64),
    `role` TINYINT NOT NULL,
    isActive TINYINT NOT NULL,
    userId INT NOT NULL,

    CONSTRAINT PK_Account PRIMARY KEY (username)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: User
CREATE TABLE IF NOT EXISTS COVID_MANAGEMENT.`User` (
    userId INT NOT NULL AUTO_INCREMENT,
    identifierNumber VARCHAR(12),
    fullname NVARCHAR(50) NOT NULL,
    yearOfBirth SMALLINT,
    locationId INT,
    `status` TINYINT,
    userInvolvedId INT,
    street NVARCHAR(50),
    wardId INT,
    districtId INT,
    provinceId INT,

    CONSTRAINT PK_User PRIMARY KEY (userId),
    FULLTEXT FT_User (fullname)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: Location
CREATE TABLE IF NOT EXISTS COVID_MANAGEMENT.Location (
    locationId INT NOT NULL AUTO_INCREMENT,
    locationName NVARCHAR(50) NOT NULL,
    capacity SMALLINT NOT NULL,
    availableSlots SMALLINT NOT NULL,

    CONSTRAINT PK_Location PRIMARY KEY (locationId)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: Province
CREATE TABLE IF NOT EXISTS COVID_MANAGEMENT.Province (
    provinceId INT NOT NULL AUTO_INCREMENT,
    provinceName NVARCHAR(30) NOT NULL,

    CONSTRAINT PK_Province PRIMARY KEY (provinceId)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: District
CREATE TABLE IF NOT EXISTS COVID_MANAGEMENT.District (
    districtId INT NOT NULL AUTO_INCREMENT,
    districtName NVARCHAR(30) NOT NULL,
    provinceId INT NOT NULL,

    CONSTRAINT PK_District PRIMARY KEY (districtId)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: Ward
CREATE TABLE IF NOT EXISTS COVID_MANAGEMENT.Ward (
    wardId INT NOT NULL AUTO_INCREMENT,
    wardName NVARCHAR(30) NOT NULL,
    districtId INT NOT NULL,

    CONSTRAINT PK_Ward PRIMARY KEY (wardId)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: UserHistory
CREATE TABLE IF NOT EXISTS COVID_MANAGEMENT.UserHistory (
    historyId INT NOT NULL AUTO_INCREMENT,
    managerId INT NOT NULL,
    userId INT NOT NULL,
    `date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- auto init
    `description` TEXT NOT NULL,
    operationType TINYINT NOT NULL,

    CONSTRAINT PK_UserHistory PRIMARY KEY (historyId)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: NecessariesHistory
CREATE TABLE IF NOT EXISTS COVID_MANAGEMENT.NecessariesHistory (
    historyId INT NOT NULL AUTO_INCREMENT,
    managerId INT NOT NULL,
    `date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- auto init
    `description` TEXT NOT NULL,
    operationType TINYINT NOT NULL,

    CONSTRAINT PK_NecessariesHistory PRIMARY KEY (historyId)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: PaymentHistory
CREATE TABLE IF NOT EXISTS COVID_MANAGEMENT.PaymentHistory (
    historyId INT NOT NULL AUTO_INCREMENT,
    userId INT NOT NULL,
    `date` TIMESTAMP NOT NULL,
    paymentAmount INT NOT NULL,

    CONSTRAINT PK_PaymentHistory PRIMARY KEY (historyId)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: Debt
CREATE TABLE IF NOT EXISTS COVID_MANAGEMENT.Debt (
    debtId INT NOT NULL AUTO_INCREMENT,
    userId INT NOT NULL,
    debtDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- auto init
    totalDebt MEDIUMINT NOT NULL,

    CONSTRAINT PK_Debt PRIMARY KEY (debtId)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: PaymentAccount
CREATE TABLE IF NOT EXISTS COVID_MANAGEMENT.PaymentAccount (
    paymentId INT NOT NULL AUTO_INCREMENT,
    balance INT NOT NULL,
    userId INT NOT NULL,

    CONSTRAINT PK_PaymentAccount PRIMARY KEY (paymentId)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: Transaction
CREATE TABLE IF NOT EXISTS COVID_MANAGEMENT.`Transaction` (
    transactionId INT NOT NULL AUTO_INCREMENT,
    sourceAccount INT NOT NULL,
    transactionDate TIMESTAMP NOT NULL,
    paymentAmount INT NOT NULL,

    CONSTRAINT PK_Transaction PRIMARY KEY (transactionId)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: Order
CREATE TABLE IF NOT EXISTS COVID_MANAGEMENT.`Order` (
    orderId INT NOT NULL AUTO_INCREMENT,
    userId INT NOT NULL,
    createdDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- auto init
    totalPrice INT NOT NULL,

    CONSTRAINT PK_Order PRIMARY KEY (orderId)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: OrderDetail
CREATE TABLE IF NOT EXISTS COVID_MANAGEMENT.OrderDetail (
    detailNo INT NOT NULL AUTO_INCREMENT,
    orderId INT NOT NULL,
    necessariesName NVARCHAR(50) NOT NULL,
    price MEDIUMINT NOT NULL,
    quantity TINYINT NOT NULL,

    CONSTRAINT PK_OrderDetail PRIMARY KEY (detailNo)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: Necessaries
CREATE TABLE IF NOT EXISTS COVID_MANAGEMENT.Necessaries (
    necessariesId INT NOT NULL AUTO_INCREMENT,
    necessariesName NVARCHAR(50) NOT NULL,
    `limit` TINYINT,
    expiredDate DATE,
    duration TINYINT,
    price MEDIUMINT NOT NULL,

    CONSTRAINT PK_Necessaries PRIMARY KEY (necessariesId),
    FULLTEXT FT_Necessaries (necessariesName)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: SystemInfo
CREATE TABLE IF NOT EXISTS COVID_MANAGEMENT.SystemInfo (
    id INT NOT NULL AUTO_INCREMENT,
    firstLoggedIn TINYINT NOT NULL,
    bankAccountNumber CHAR(12) NOT NULL,
    balance INT NOT NULL,

    CONSTRAINT PK_SystemInfo PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;


-- ------------------------------------------------ --
--              CREATE FOREIGN KEY                  --
-- ------------------------------------------------ --

-- Table: Account
-- Account(userId) ==> User(userId)
ALTER TABLE COVID_MANAGEMENT.`Account`
ADD CONSTRAINT FK_Account_User
FOREIGN KEY (userId)
REFERENCES COVID_MANAGEMENT.`User`(userId);

-- Table: User
-- User(locationId) ==> Location(locationId)
ALTER TABLE COVID_MANAGEMENT.`User`
ADD CONSTRAINT FK_User_Location
FOREIGN KEY (locationId)
REFERENCES COVID_MANAGEMENT.Location(locationId);

-- Table: User
-- User(userInvolvedId) ==> User(userId)
ALTER TABLE COVID_MANAGEMENT.`User`
ADD CONSTRAINT FK_User_User
FOREIGN KEY (userInvolvedId)
REFERENCES COVID_MANAGEMENT.`User`(userId);

-- Table: User
-- User(wardId) ==> Ward(wardId)
ALTER TABLE COVID_MANAGEMENT.`User`
ADD CONSTRAINT FK_User_Ward
FOREIGN KEY (wardId)
REFERENCES COVID_MANAGEMENT.`Ward`(wardId);

-- Table: User
-- User(districtId) ==> District(districtId)
ALTER TABLE COVID_MANAGEMENT.`User`
ADD CONSTRAINT FK_User_District
FOREIGN KEY (districtId)
REFERENCES COVID_MANAGEMENT.District(districtId);

-- Table: User
-- User(provinceId) ==> Province(provinceId)
ALTER TABLE COVID_MANAGEMENT.`User`
ADD CONSTRAINT FK_User_Province
FOREIGN KEY (provinceId)
REFERENCES COVID_MANAGEMENT.Province(provinceId);

-- Table: District
-- District(provinceId) ==> Province(provinceId)
ALTER TABLE COVID_MANAGEMENT.District
ADD CONSTRAINT FK_District_Province
FOREIGN KEY (provinceId)
REFERENCES COVID_MANAGEMENT.Province(provinceId);

-- Table: Ward
-- Ward(districtId) ==> District(districtId)
ALTER TABLE COVID_MANAGEMENT.Ward
ADD CONSTRAINT FK_Ward_District
FOREIGN KEY (districtId)
REFERENCES COVID_MANAGEMENT.District(districtId);

-- Table: UserHistory
-- UserHistory(managerId) ==> User(userId)
ALTER TABLE COVID_MANAGEMENT.UserHistory
ADD CONSTRAINT FK_UserHistory_User_Manager
FOREIGN KEY (managerId)
REFERENCES COVID_MANAGEMENT.`User`(userId);

-- Table: UserHistory
-- UserHistory(userId) ==> User(userId)
ALTER TABLE COVID_MANAGEMENT.UserHistory
ADD CONSTRAINT FK_UserHistory_User
FOREIGN KEY (userId)
REFERENCES COVID_MANAGEMENT.`User`(userId);

-- Table: NecessariesHistory
-- NecessariesHistory(managerId) ==> User(userId)
ALTER TABLE COVID_MANAGEMENT.NecessariesHistory
ADD CONSTRAINT FK_NecessariesHistory_User_Manager
FOREIGN KEY (managerId)
REFERENCES COVID_MANAGEMENT.`User`(userId);

-- Table: PaymentHistory
-- PaymentHistory(userId) ==> User(userId)
ALTER TABLE COVID_MANAGEMENT.PaymentHistory
ADD CONSTRAINT FK_PaymentHistory_User
FOREIGN KEY (userId)
REFERENCES COVID_MANAGEMENT.`User`(userId);

-- Table: Debt
-- Debt(userId) ==> User(userId)
ALTER TABLE COVID_MANAGEMENT.Debt
ADD CONSTRAINT FK_Debt_User
FOREIGN KEY (userId)
REFERENCES COVID_MANAGEMENT.`User`(userId);

-- Table: PaymentAccount
-- PaymentAccount(userId) ==> User(userId)
ALTER TABLE COVID_MANAGEMENT.PaymentAccount
ADD CONSTRAINT FK_PaymentAccount_User
FOREIGN KEY (userId)
REFERENCES COVID_MANAGEMENT.`User`(userId);

-- Table: Transaction
-- Transaction(sourceAccount) ==> PaymentAccount(paymentId)
ALTER TABLE COVID_MANAGEMENT.`Transaction`
ADD CONSTRAINT FK_Transaction_PaymentAccount
FOREIGN KEY (sourceAccount)
REFERENCES COVID_MANAGEMENT.PaymentAccount(paymentId);

-- Table: Order
-- Order(userId) ==> User(userId)
ALTER TABLE COVID_MANAGEMENT.`Order`
ADD CONSTRAINT FK_Order_User
FOREIGN KEY (userId)
REFERENCES COVID_MANAGEMENT.`User`(userId);

-- Table: OrderDetail
-- OrderDetail(orderId) ==> Order(orderId)
ALTER TABLE COVID_MANAGEMENT.OrderDetail
ADD CONSTRAINT FK_OrderDetail_User
FOREIGN KEY (orderId)
REFERENCES COVID_MANAGEMENT.`Order`(orderId);
