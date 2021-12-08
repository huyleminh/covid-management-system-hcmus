USE COVID_MANAGEMENT;

INSERT INTO COVID_MANAGEMENT.Province (provinceName)
VALUES
    (N'Thành phố Hà Nội'),
    (N'Tỉnh Hà Giang'),
    (N'Tỉnh Cao Bằng'),
    (N'Tỉnh Bắc Kạn'),
    (N'Tỉnh Tuyên Quang');

INSERT INTO COVID_MANAGEMENT.District (districtName, provinceId)
VALUES
    (N'Quận Ba Đình', 1),
    (N'Quận Hoàn Kiếm', 1),
    (N'Quận Tây Hồ', 1),
    (N'Quận Long Biên', 1),
    (N'Quận Cầu Giấy', 1),
    (N'Huyện Đồng Văn', 2),
    (N'Huyện Mèo Vạc', 2),
    (N'Huyện Yên Minh', 2),
    (N'Huyện Quản Bạ', 2),
    (N'Huyện Vị Xuyên', 2),
    (N'Huyện Bảo Lâm', 3),
    (N'Huyện Bảo Lạc', 3),
    (N'Huyện Thông Nông', 3),
    (N'Huyện Hà Quảng', 3),
    (N'Huyện Trùng Khánh', 3),
    (N'Huyện Pác Nặm', 4),
    (N'Huyện Ba Bể', 4),
    (N'Huyện Ngân Sơn', 4),
    (N'Huyện Bạch Thông', 4),
    (N'Huyện Chợ Đồn', 4),
    (N'Huyện Lâm Bình', 5),
    (N'Huyện Nà Hang', 5),
    (N'Huyện Chiêm Hóa', 5),
    (N'Huyện Hàm Yên', 5),
    (N'Huyện Sơn Dương', 5);

INSERT INTO COVID_MANAGEMENT.Ward (wardName, districtId)
VALUES
    (N'Phường Cống Vị', 1),
    (N'Phường Điện Biên', 1),
    (N'Phường Đội Cấn', 1),
    (N'Phường Giảng Võ', 1),
    (N'Phường Kim Mã', 1),
    (N'Phường Chương Dương', 2),
    (N'Phường Cửa Đông', 2),
    (N'Phường Cửa Nam', 2),
    (N'Phường Đồng Xuân', 2),
    (N'Phường Hàng Bạc', 2),
    (N'Phường Bưởi', 3),
    (N'Phường Thuỵ Khuê', 3),
    (N'Phường Yên Phụ', 3),
    (N'Phường Tứ Liên', 3),
    (N'Phường Nhật Tân', 3),
    (N'Phường Bồ Đề', 4),
    (N'Phường Cự Khối', 4),
    (N'Phường Đức Giang', 4),
    (N'Phường Gia Thuỵ', 4),
    (N'Phường Giang Biên', 4),
    (N'Phường Dịch Vọng', 5),
    (N'Phường Dịch Vọng Hậu', 5),
    (N'Phường Mai Dịch', 5),
    (N'Phường Nghĩa Đô', 5),
    (N'Phường Nghĩa Tân', 5),
    (N'Xã Hố Quáng Phìn', 6),
    (N'Xã Lũng Cú', 6),
    (N'Xã Lũng Phìn', 6),
    (N'Xã Lũng Táo', 6),
    (N'Xã Lũng Thầu', 6),
    (N'Xã Cán Chu Phìn', 7),
    (N'Xã Giàng Chu Phìn', 7),
    (N'Xã Khâu Vai', 7),
    (N'Xã Lũng Chinh', 7),
    (N'Xã Lũng Pù', 7),
    (N'Xã Bạch Đích', 8),
    (N'Xã Đông Minh', 8),
    (N'Xã Du Già', 8),
    (N'Xã Du Tiến', 8),
    (N'Xã Đường Thượng', 8),
    (N'Xã Đông Hà', 9),
    (N'Xã Lùng Tám', 9),
    (N'Xã Thái An', 9),
    (N'Xã Cán Tỷ', 9),
    (N'Xã Bát Đại Sơn', 9),
    (N'Xã Bạch Ngọc', 10),
    (N'Xã Cao Bồ', 10),
    (N'Xã Đạo Đức', 10),
    (N'Xã Lao Chải', 10),
    (N'Xã Linh Hồ', 10),
    (N'Xã Đức Hạnh', 11),
    (N'Xã Lý Bôn', 11),
    (N'Xã Mông Ân', 11),
    (N'Xã Nam Cao', 11),
    (N'Xã Nam Quang', 11),
    (N'Xã Bảo Toàn', 12),
    (N'Xã Cô Ba', 12),
    (N'Xã Cốc Pàng', 12),
    (N'Xã Đình Phùng', 12),
    (N'Xã Hồng An', 12),
    (N'Xã Đa Thông', 13),
    (N'Xã Lương Thông', 13),
    (N'Xã Cần Yên', 13),
    (N'Xã Lương Can', 13),
    (N'Xã Ngọc Động', 13),
    (N'Xã Cải Viên', 14),
    (N'Xã Đào Ngạn', 14),
    (N'Xã Hạ Thôn', 14),
    (N'Xã Hồng Sỹ', 14),
    (N'Xã Kéo Yên', 14),
    (N'Xã Cao Chương', 15),
    (N'Xã Cao Thăng', 15),
    (N'Xã Chí Viễn', 15),
    (N'Xã Đàm Thuỷ', 15),
    (N'Xã Đình Phong', 15),
    (N'Xã An Thắng', 16),
    (N'Xã Bằng Thành', 16),
    (N'Xã Cao Tân', 16),
    (N'Xã Cổ Linh', 16),
    (N'Xã Công Bằng', 16),
    (N'Xã Bành Trạch', 17),
    (N'Xã Cao Thượng', 17),
    (N'Xã Chu Hương', 17),
    (N'Xã Địa Linh', 17),
    (N'Xã Đồng Phúc', 17),
    (N'Xã Bằng Vân', 18),
    (N'Xã Cốc Đán', 18),
    (N'Xã Đức Vân', 18),
    (N'Xã Hiệp Lục', 18),
    (N'Xã Thuần Mang', 18),
    (N'Xã Cẩm Giàng', 19),
    (N'Xã Cao Sơn', 19),
    (N'Xã Đôn Phong', 19),
    (N'Xã Dương Phong', 19),
    (N'Xã Lục Bình', 19),
    (N'Xã Bản Thi', 20),
    (N'Xã Bằng Lẵng', 20),
    (N'Xã Bằng Phúc', 20),
    (N'Xã Bình Trung', 20),
    (N'Xã Đại Sảo', 20),
    (N'Xã Lăng Can', 21),
    (N'Xã Thượng Lâm', 21),
    (N'Xã Khuôn Hà', 21),
    (N'Xã Phúc Yên', 21),
    (N'Xã Xuân Lập', 21),
    (N'Xã Côn Lôn', 22),
    (N'Xã Đà Vị', 22),
    (N'Xã Hồng Thái', 22),
    (N'Xã Khâu Tinh', 22),
    (N'Xã Năng Khả', 22),
    (N'Xã Yên Nguyên', 23),
    (N'Xã Hoà Phú', 23),
    (N'Xã Phúc Thịnh', 23),
    (N'Xã Tân Thịnh', 23),
    (N'Xã Tân An', 23),
    (N'Xã Hùng Đức', 24),
    (N'Xã Bằng Cốc', 24),
    (N'Xã Thành Long', 24),
    (N'Xã Thái Hoà', 24),
    (N'Xã Đức Ninh', 24),
    (N'Xã Bình Yên', 25),
    (N'Xã Cấp Tiến', 25),
    (N'Xã Chi Thiết', 25),
    (N'Xã Đại Phú', 25),
    (N'Xã Đông Lợi', 25);

INSERT INTO COVID_MANAGEMENT.Location (locationName, capacity, availableSlots)
VALUES
    (N'Khu điều trị thu dung số 1', 1000, 995),
    (N'Bệnh viện dã chiến ngoại ô TP', 2000, 2000),
    (N'Bệnh viện dã chiến trung tâm Sài Gòn', 4500, 4500),
    (N'Khu điều trị thu dung số 2', 950, 950),
    (N'Khu điều trị thu dung số 3', 1500, 1500);

INSERT INTO COVID_MANAGEMENT.`User` (identifierNumber, fullname, yearOfBirth, locationId, `status`, userInvolvedId, street, wardId, districtId, provinceId)
VALUES
    ('011234567', 'SYSTEM ADMIN', null, null, null, null, null, null, null, null),
    ('021234567', N'Phan Minh Hiếu', null, null, null, null, null, null, null, null),
    ('021234568', N'Nguyễn Hứa Hùng', null, null, null, null, null, null, null, null),
    ('021234569', N'Bạch Minh Khôi', 1997, 1, 0, null, N'Lý Thường Kiệt', 26, 6, 2),
    ('011234568', N'Lê Hoàng Anh', 1998, 1, 1, '021234569', N'Trương Định', 66, 14, 3),
    ('011234569', N'Lê Minh Huy', 1999, 1, 1, '021234569', N'Trường Chinh', 17, 4, 1),
    ('011234570', N'Nguyễn Nhật Cường', 2000, 1, 2, '011234568', N'Đinh Tiên Hoàn', 124, 25, 5),
    ('011234571', N'Nguyễn Đinh Hồng Phúc', 2001, 1, 2, '011234568', N'An Dương Vương', 33, 7, 2);

INSERT INTO COVID_MANAGEMENT.`Account` (username, `password`, `role`, `active`)
VALUES
    ('011234567', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 1, 1),
    ('021234567', '6ee4a469cd4e91053847f5d3fcb61dbcc91e8f0ef10be7748da4c4a1ba382d17', 2, 1),
    ('021234568', '6ee4a469cd4e91053847f5d3fcb61dbcc91e8f0ef10be7748da4c4a1ba382d18', 2, 0),
    ('021234569', 'eb95b7a4f763c42d063c14694f315171b0f0c00500957ef5a0ee81cd6988bc15', 3, 1),
    ('011234568', '38fe030a4e9078d229d326abbaa4ac3ecbde843ae5ea09c1b337640a1f786db4', 3, 1),
    ('011234569', null, 3, 1),
    ('011234570', null, 3, 1),
    ('011234571', null, 3, 1);

INSERT INTO COVID_MANAGEMENT.UserHistory (managerId, userId, `date`, `description`, operationType)
VALUES
    ('021234567', '021234569', '2020-9-10 10:05:10', N'Tạo mới', 3),
    ('021234567', '011234568', '2020-9-10 10:12:25', N'Tạo mới', 3),
    ('021234567', '011234569', '2020-9-10 10:31:20', N'Tạo mới', 3),
    ('021234567', '011234570', '2020-9-10 10:40:50', N'Tạo mới', 3),
    ('021234567', '011234571', '2020-9-10 10:45:23', N'Tạo mới', 3);

INSERT INTO COVID_MANAGEMENT.NecessariesHistory (managerId, `date`, `description`, operationType)
VALUES
    ('021234567', '2020-10-10 8:00:19', N'Thêm mới gói Combo 1 (Thịt, Rau củ, Nước uống)', 1),
    ('021234567', '2020-10-10 8:05:23', N'Thêm mới gói Combo 2 (Cá, Rau củ, Nước uống)', 1),
    ('021234567', '2020-10-10 8:10:50', N'Thêm mới gói Combo 3 (Thịt, Cá, Trứng, Nước uống)', 1),
    ('021234567', '2020-10-12 9:20:33', N'Thêm mới gói Combo 4 (Thịt, Cá, Rau Củ, Nước uống)', 1),
    ('021234567', '2020-10-13 8:00:15', N'Thêm mới gói Combo 5 (Thịt, Cá, Trứng, Rau củ, Nước uống)', 1);

INSERT INTO COVID_MANAGEMENT.Debt (debtId, userId, debtDate, totalDebt)
VALUES
	(1, '021234569', '2020-10-14 12:31:20', 300000),
	(2, '011234568', '2020-12-01 07:33:43', 150000),
	(3, '011234569', '2021-03-12 16:22:56', 330000),
	(5, '011234571', '2021-06-10 12:31:20', 200000),
	(6, '011234570', '2021-11-11 07:15:22', 250000),
	(7, '011234570', '2021-11-15 06:30:39', 300000);

INSERT INTO COVID_MANAGEMENT.Necessaries (necessariesName, `limit`, expiredDate, duration, price)
VALUES
    (N'Combo 1 (Thịt, Rau củ, Nước uống)', 8, '2021-12-09 07:15:33', 2, 150000),
    (N'Combo 2 (Cá, Rau củ, Nước uống)', 15, '2021-12-10 08:19:20', 3, 150000),
    (N'Combo 3 (Thịt, Cá, Trứng, Nước uống)', 20, '2021-12-09 07:20:03', 4, 200000),
    (N'Combo 4 (Thịt, Cá, Rau Củ, Nước uống)', 4, '2021-12-09 07:10:10', 1, 180000),
    (N'Combo 5 (Thịt, Cá, Trứng, Rau củ, Nước uống)', 12, '2021-12-12 06:40:27', 3, 250000);

INSERT INTO COVID_MANAGEMENT.`Order` (userId, createdDate, totalPrice)
VALUES
    ('021234569', '2020-10-14 12:31:20', 300000),
    ('011234568', '2020-12-01 07:33:43', 150000),
    ('011234569', '2021-03-12 16:22:56', 330000),
    ('011234568', '2021-05-21 18:20:15', 500000),
    ('011234571', '2021-06-10 12:31:20', 200000),
    ('011234570', '2021-11-11 07:15:22', 250000),
    ('011234570', '2021-11-15 06:30:39', 300000);


INSERT INTO COVID_MANAGEMENT.OrderDetail (orderId, necessariesName, price, quantity)
VALUES
    (1, N'Combo 1 (Thịt, Rau củ, Nước uống)', 150000, 1),
    (1, N'Combo 2 (Cá, Rau củ, Nước uống)', 150000, 1),
    (2, N'Combo 2 (Cá, Rau củ, Nước uống)', 150000, 1),
    (3, N'Combo 1 (Thịt, Rau củ, Nước uống)', 150000, 1),
    (3, N'Combo 4 (Thịt, Cá, Rau Củ, Nước uống)', 180000, 1),
    (4, N'Combo 5 (Thịt, Cá, Trứng, Rau củ, Nước uống)', 250000, 2),
    (5, N'Combo 3 (Thịt, Cá, Trứng, Nước uống)', 200000, 1);

INSERT INTO COVID_MANAGEMENT.PaymentAccount (balance, userId)
VALUES
    (1750000, '021234569'),
    (1350000, '011234568'),
    (1700000, '011234569'),
    (1610000, '011234570'),
    (1820000, '011234571');

INSERT INTO COVID_MANAGEMENT.`Transaction` (sourceAccount, transactionDate, paymentAmount)
VALUES
    (1, '2020-10-14 12:31:20', 250000),
    (2, '2020-12-01 07:33:43', 150000),
    (3, '2021-03-12 16:22:56', 300000),
    (2, '2021-05-21 18:20:15', 500000),
    (5, '2021-06-10 12:31:20', 180000),
    (4, '2021-11-11 07:15:22', 150000),
    (4, '2021-11-15 06:30:39', 240000);

INSERT INTO COVID_MANAGEMENT.PaymentHistory (userId, `date`, paymentAmount)
VALUES
    ('011234568', '2021-05-21 18:20:15', 500000);

INSERT INTO COVID_MANAGEMENT.SystemInfo (id, firstLoggedIn, bankAccountNumber, balance)
VALUES
    (1, 0, '129029974556', 500000);
