CREATE SCHEMA IF NOT EXISTS PAYMENT_SYSTEM
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_bin;

USE PAYMENT_SYSTEM;


-- ------------------------------------------------ --
--                  CREATE TABLE                    --
-- ------------------------------------------------ --

-- Table: PaymentAccount
CREATE TABLE IF NOT EXISTS PAYMENT_SYSTEM.PaymentAccount (
    paymentId INT NOT NULL AUTO_INCREMENT,
    balance INT NOT NULL,
    userId INT NOT NULL,
    userIdentifierNumber VARCHAR(12) NOT NULL,

    CONSTRAINT PK_PaymentAccount PRIMARY KEY (paymentId)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: Transaction
CREATE TABLE IF NOT EXISTS PAYMENT_SYSTEM.`Transaction` (
    transactionId INT NOT NULL AUTO_INCREMENT,
    sourceAccount INT NOT NULL,  -- paymentId
    transactionDate TIMESTAMP NOT NULL,
    paymentAmount INT NOT NULL,

    CONSTRAINT PK_Transaction PRIMARY KEY (transactionId)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
DEFAULT COLLATE = utf8mb4_bin;

-- Table: SystemInfo
CREATE TABLE IF NOT EXISTS PAYMENT_SYSTEM.SystemInfo (
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

-- Table: Transaction
-- Transaction(sourceAccount) ==> PaymentAccount(paymentId)
ALTER TABLE PAYMENT_SYSTEM.`Transaction`
ADD CONSTRAINT FK_Transaction_PaymentAccount
FOREIGN KEY (sourceAccount)
REFERENCES PAYMENT_SYSTEM.PaymentAccount(paymentId);
