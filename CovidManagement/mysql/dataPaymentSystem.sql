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
    (10000000, N'Đinh Hải Giang', '319127382'),
    (10000000, N'Tô Vũ Thái Hào', '319127391'),
    (10000000, N'Nguyễn Hữu Hiển', '341119127394'),
    (10000000, N'Trần Minh Hiếu', '319127405'),
    (10000000, N'Nguyễn Huy Hoàng', '319127407'),
    (10000000, N'Đặng Duy Khang', '353219127431'),
    (10000000, N'Hồng Kiện Khang', '567819127434'),
    (10000000, N'Huỳnh Nhật Khang', '319127434'),
    (10000000, N'Nguyễn Trần Gia Khang', '441219127435'),
    (10000000, N'Tăng Tường Khang', '319127436'),
    (10000000, N'Hồ Đăng Khoa', '319127443');

INSERT INTO PAYMENT_SYSTEM.`Transaction` (sourceAccount, transactionDate, paymentAmount)
VALUES
    (2, '2021-05-21 18:20:15', 500000);

INSERT INTO PAYMENT_SYSTEM.SystemInfo (id, firstLoggedIn, bankAccountNumber, balance, defaultBalanceOfNewAccount)
VALUES
    (1, 1, '129029974556', 500000, 10000000);
