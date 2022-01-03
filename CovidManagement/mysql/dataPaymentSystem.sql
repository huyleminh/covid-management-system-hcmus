USE PAYMENT_SYSTEM;

INSERT INTO PAYMENT_SYSTEM.PaymentAccount (balance, userId, userIdentifierNumber)
VALUES
    (3750000, 1, '021234569'),
    (4350000, 2, '011234568'),
    (2700000, 3, '011234569'),
    (5610000, 4, '011234570'),
    (4820000, 5, '011234571');

INSERT INTO PAYMENT_SYSTEM.`Transaction` (sourceAccount, transactionDate, paymentAmount)
VALUES
    (1, '2020-10-14 12:31:20', 250000),
    (2, '2020-12-01 07:33:43', 150000),
    (3, '2021-03-12 16:22:56', 300000),
    (2, '2021-05-21 18:20:15', 500000),
    (5, '2021-06-10 12:31:20', 180000),
    (4, '2021-11-11 07:15:22', 150000),
    (4, '2021-11-15 06:30:39', 240000);

INSERT INTO PAYMENT_SYSTEM.SystemInfo (id, firstLoggedIn, bankAccountNumber, balance)
VALUES
    (1, 1, '129029974556', 500000);
