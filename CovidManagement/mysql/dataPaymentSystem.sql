USE PAYMENT_SYSTEM;

INSERT INTO PAYMENT_SYSTEM.PaymentAccount (balance, fullname, userIdentifierNumber)
VALUES
    (10000000, N'Hoàng Minh Đức', '319127121'),
    (9500000, N'Phan Minh Hiếu', '319127141'),
    (10000000, N'Nguyễn Hứa Hùng', '319127150'),
    (10000000, N'Lê Minh Huy', '319127157'),
    (10000000, N'Bạch Minh Khôi', '319127181'),
    (10000000, N'Nguyễn Cao Thiên Long', '341119127203'),
    (10000000, N'Nguyễn Hữu Hoàng An', '341219127325'),
    (10000000, N'Lê Hoàng Anh', '341219127329'),
    (10000000, N'Trần Lê Quốc Bảo', '341119127340'),
    (10000000, N'Lâm Quốc Cường', '352219127345'),
    (10000000, N'Nguyễn Nhật Cường', '419127346'),
    (10000000, N'Lê Thành Đạt', '419127354'),
    (10000000, N'Nguyễn Huỳnh Khánh Duy', '519127377'),
    (10000000, N'Phạm Đức Duy', '567819127379'),
    (10000000, N'Đinh Hải Giang', '319127382');

INSERT INTO PAYMENT_SYSTEM.`Transaction` (sourceAccount, transactionDate, paymentAmount)
VALUES
    (2, '2021-05-21 18:20:15', 500000);

INSERT INTO PAYMENT_SYSTEM.SystemInfo (id, firstLoggedIn, bankAccountNumber, balance, defaultBalanceOfNewAccount)
VALUES
    (1, 1, '129029974556', 500000, 10000000);
