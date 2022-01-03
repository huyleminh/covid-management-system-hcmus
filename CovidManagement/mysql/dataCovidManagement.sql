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

INSERT INTO COVID_MANAGEMENT.Location (locationName, capacity, currentSlots)
VALUES
    (N'Khu điều trị thu dung số 1', 1000, 4),
    (N'Bệnh viện dã chiến ngoại ô TP', 2000, 4),
    (N'Bệnh viện dã chiến trung tâm Sài Gòn', 4500, 0),
    (N'Khu điều trị thu dung số 2', 950, 5),
    (N'Khu điều trị thu dung số 3', 1500, 2),
    ('None', 0, 0);

INSERT INTO COVID_MANAGEMENT.`User` (userId, identifierNumber, fullname, yearOfBirth, locationId, `status`, infectiousUserId, address)
VALUES
    (1, '319127121', N'Hoàng Minh Đức', 1997, 4, 0, null, N'Xã Bạch Đích, Huyện Yên Minh, Tỉnh Hà Giang'),
    (2, '319127141', N'Phan Minh Hiếu', 1998, 2, 1, null, N'Xã Cao Thăng, Huyện Trùng Khánh, Tỉnh Cao Bằng'),
    (3, '319127150', N'Nguyễn Hứa Hùng', 1999, 1, 1, null, N'Phường Đội Cấn, Quận Ba Đình, Thành phố Hà Nội'),
    (4, '319127157', N'Lê Minh Huy', 2000, 5, 1, null, N'Xã Hồng Thái, Huyện Nà Hang, Tỉnh Tuyên Quang'),
    (5, '319127181', N'Bạch Minh Khôi', 2001, 2, 1, null, N'Xã Xuân Lập, Huyện Lâm Bình, Tỉnh Tuyên Quang'),
    (6, '341119127203', N'Nguyễn Cao Thiên Long', 2000, 1, 2, 3, N'Xã Linh Hồ, Huyện Vị Xuyên, Tỉnh Hà Giang'),
    (7, '341219127325', N'Nguyễn Hữu Hoàng An', 1990, 1, 2, 4, N'Phường Giang Biên, Quận Long Biên, Thành phố Hà Nội'),
    (8, '341219127329', N'Lê Hoàng Anh', 2001, 4, 3, 6, N'Xã Bạch Ngọc, Huyện Vị Xuyên, Tỉnh Hà Giang'),
    (9, '341119127340', N'Trần Lê Quốc Bảo', 2002, 4, 3, 6, N'Xã Đông Minh, Huyện Yên Minh, Tỉnh Hà Giang'),
    (10, '352219127345', N'Lâm Quốc Cường', 1989, 4, 3, 7, N'Phường Chương Dương, Quận Hoàn Kiếm, Thành phố Hà Nội'),
    (11, '419127346', N'Nguyễn Nhật Cường', 1998, 2, 4, 8, N'Phường Nhật Tân, Quận Tây Hồ, Thành phố Hà Nội'),
    (12, '419127354', N'Lê Thành Đạt', 1995, 2, 4, 9, N'Xã Bình Trung, Huyện Chợ Đồn, Tỉnh Bắc Kạn'),
    (13, '519127377', N'Nguyễn Huỳnh Khánh Duy', 2000, 4, 4, 9, N'Xã Bạch Ngọc, Huyện Vị Xuyên, Tỉnh Hà Giang'),
    (14, '567819127379', N'Phạm Đức Duy', 2001, 5, 5, null, N'Xã Đông Minh, Huyện Yên Minh, Tỉnh Hà Giang'),
    (15, '319127382', N'Đinh Hải Giang', 1999, 1, 5, null, N'Xã Lũng Phìn, Huyện Đồng Văn, Tỉnh Hà Giang');


INSERT INTO COVID_MANAGEMENT.`Account` (username, `password`, `role`, isActive, userId)
VALUES
    ('admin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 0, 0, null),
    ('manager1', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 1, 0, null),
    ('manager2', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 1, 1, null),
    ('319127121', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 2, 0, 1),
    ('319127141', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 2, 0, 2),
    ('319127150', null, 2, 0, 3),
    ('319127157', null, 2, 0, 4),
    ('319127181', null, 2, 0, 5),
    ('341119127203', null, 2, 0, 6),
    ('341219127325', null, 2, 0, 7),
    ('341219127329', null, 2, 0, 8),
    ('341119127340', null, 2, 0, 9),
    ('352219127345', null, 2, 0, 10),
    ('419127346', null, 2, 0, 11),
    ('419127354', null, 2, 0, 12),
    ('519127377', null, 2, 0, 13),
    ('567819127379', null, 2, 0, 14),
    ('319127382', null, 2, 0, 15);
    
INSERT INTO COVID_MANAGEMENT.UserHistory (managerUsername, userId, `date`, `description`, operationType)
VALUES
    ('manager1', 1, '2020-9-10 10:05:10', N'Thêm mới người dùng 319127121', 1),
    ('manager1', 2, '2020-9-10 10:12:25', N'Thêm mới người dùng 319127141', 1),
    ('manager1', 3, '2020-9-10 10:31:20', N'Thêm mới người dùng 319127150', 1),
    ('manager1', 4, '2020-9-10 10:40:50', N'Thêm mới người dùng 319127157', 1),
    ('manager1', 5, '2020-9-10 10:45:23', N'Thêm mới người dùng 319127181', 1),
    ('manager1', 6, '2020-9-10 10:05:10', N'Thêm mới người dùng 341119127203', 1),
    ('manager2', 7, '2020-9-10 10:12:25', N'Thêm mới người dùng 341219127325', 1),
    ('manager2', 8, '2020-9-10 10:31:20', N'Thêm mới người dùng 341219127329', 1),
    ('manager2', 9, '2020-9-10 10:40:50', N'Thêm mới người dùng 341119127340', 1),
    ('manager2', 10, '2020-9-10 10:45:23', N'Thêm mới người dùng 352219127345', 1),
    ('manager1', 11, '2020-9-10 10:05:10', N'Thêm mới người dùng 419127346', 1),
    ('manager1', 12, '2020-9-10 10:12:25', N'Thêm mới người dùng 419127354', 1),
    ('manager1', 13, '2020-9-10 10:31:20', N'Thêm mới người dùng 519127377', 1),
    ('manager1', 14, '2020-9-10 10:40:50', N'Thêm mới người dùng 567819127379', 1),
    ('manager1', 15, '2020-9-10 10:45:23', N'Thêm mới người dùng 319127382', 1);

INSERT INTO COVID_MANAGEMENT.NecessariesHistory (managerUsername, `date`, `description`, operationType)
VALUES
    ('manager1', '2020-10-10 8:00:19', N'Thêm mới gói Combo 1 (Thịt, Rau củ, Nước uống)', 1),
    ('manager1', '2020-10-10 8:05:23', N'Thêm mới gói Combo 2 (Cá, Rau củ, Nước uống)', 1),
    ('manager1', '2020-10-10 8:10:50', N'Thêm mới gói Combo 3 (Thịt, Cá, Trứng, Nước uống)', 1),
    ('manager1', '2020-10-12 9:20:33', N'Thêm mới gói Combo 4 (Thịt, Cá, Rau Củ, Nước uống)', 1),
    ('manager1', '2020-10-13 8:00:15', N'Thêm mới gói Combo 5 (Thịt, Cá, Trứng, Rau củ, Nước uống)', 1);

INSERT INTO COVID_MANAGEMENT.Debt (debtId, userId, debtDate, totalDebt)
VALUES
    (1, 1, '2020-10-14 12:31:20', 300000),
    (2, 2, '2020-12-01 07:33:43', 150000),
    (3, 3, '2021-03-12 16:22:56', 330000),
    (5, 5, '2021-06-10 12:31:20', 200000),
    (6, 4, '2021-11-11 07:15:22', 250000),
    (7, 4, '2021-11-15 06:30:39', 300000);

INSERT INTO COVID_MANAGEMENT.Necessaries (necessariesName, `limit`, expiredDate, duration, price)
VALUES
    (N'Combo 1 (Thịt, Rau củ, Nước uống)', 8, '2021-12-09 07:15:33', 2, 150000),
    (N'Combo 2 (Cá, Rau củ, Nước uống)', 15, '2021-12-10 08:19:20', 3, 150000),
    (N'Combo 3 (Thịt, Cá, Trứng, Nước uống)', 20, '2021-12-09 07:20:03', 4, 200000),
    (N'Combo 4 (Thịt, Cá, Rau Củ, Nước uống)', 4, '2021-12-09 07:10:10', 1, 180000),
    (N'Combo 5 (Thịt, Cá, Trứng, Rau củ, Nước uống)', 12, '2021-12-12 06:40:27', 3, 250000);

INSERT INTO COVID_MANAGEMENT.`Order` (userId, createdDate, totalPrice)
VALUES
    (1, '2020-10-14 12:31:20', 300000),
    (2, '2020-12-01 07:33:43', 150000),
    (3, '2021-03-12 16:22:56', 330000),
    (2, '2021-05-21 18:20:15', 500000),
    (5, '2021-06-10 12:31:20', 200000),
    (4, '2021-11-11 07:15:22', 250000),
    (4, '2021-11-15 06:30:39', 300000);

INSERT INTO COVID_MANAGEMENT.OrderDetail (orderId, necessariesName, price, quantity)
VALUES
    (1, N'Combo 1 (Thịt, Rau củ, Nước uống)', 150000, 1),
    (1, N'Combo 2 (Cá, Rau củ, Nước uống)', 150000, 1),
    (2, N'Combo 2 (Cá, Rau củ, Nước uống)', 150000, 1),
    (3, N'Combo 1 (Thịt, Rau củ, Nước uống)', 150000, 1),
    (3, N'Combo 4 (Thịt, Cá, Rau Củ, Nước uống)', 180000, 1),
    (4, N'Combo 5 (Thịt, Cá, Trứng, Rau củ, Nước uống)', 250000, 2),
    (5, N'Combo 3 (Thịt, Cá, Trứng, Nước uống)', 200000, 1);

INSERT INTO COVID_MANAGEMENT.PaymentHistory (userId, `date`, paymentAmount)
VALUES
    (2, '2021-05-21 18:20:15', 500000);

INSERT INTO COVID_MANAGEMENT.SystemInfo (id, firstLoggedIn)
VALUES
    (1, 1);
