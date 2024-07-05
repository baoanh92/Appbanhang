-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th6 23, 2024 lúc 06:13 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `qlbdt`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitietdonhang`
--

CREATE TABLE `chitietdonhang` (
  `id` int(11) NOT NULL,
  `iddonhang` int(11) NOT NULL,
  `idsp` int(11) NOT NULL,
  `soluong` int(11) DEFAULT 1,
  `gia` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `chitietdonhang`
--

INSERT INTO `chitietdonhang` (`id`, `iddonhang`, `idsp`, `soluong`, `gia`) VALUES
(1, 238, 14, 1, '11590000');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `donhang`
--

CREATE TABLE `donhang` (
  `id` int(11) NOT NULL,
  `iduser` int(11) NOT NULL,
  `diachi` text NOT NULL,
  `sodienthoai` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `trangthai` int(2) NOT NULL DEFAULT 0,
  `ngaydat` date NOT NULL DEFAULT current_timestamp(),
  `hotennguoinhan` varchar(255) NOT NULL,
  `NgayGiao` date DEFAULT NULL,
  `GhiChu` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `donhang`
--

INSERT INTO `donhang` (`id`, `iduser`, `diachi`, `sodienthoai`, `email`, `trangthai`, `ngaydat`, `hotennguoinhan`, `NgayGiao`, `GhiChu`) VALUES
(238, 35, 'Ha Noi', '0123456789', 'kh2@gmail.com', 0, '2024-06-23', 'test', NULL, 'oki');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loaisanpham`
--

CREATE TABLE `loaisanpham` (
  `id` int(11) NOT NULL,
  `loaisp` varchar(100) NOT NULL,
  `hinhanh` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `loaisanpham`
--

INSERT INTO `loaisanpham` (`id`, `loaisp`, `hinhanh`) VALUES
(1, 'Giày Nam', 'https://tse4.mm.bing.net/th?id=OIP.c_n3LfIWJ8V2vdbRc6b4VgHaHa&pid=Api&P=0&h=220'),
(2, 'Giày Nữ', 'https://tse2.mm.bing.net/th?id=OIP.vas1XmkC3I5xno1bgERq2gHaJe&pid=Api&P=0&h=220'),
(3, 'Sản phẩm bán chạy\r\n', 'https://png.pngtree.com/png-clipart/20190619/original/pngtree-sale-icon-png-image_3985640.jpg'),
(4, 'Sản phẩm hot', 'https://icons.veryicon.com/png/o/miscellaneous/tool-icon-library/hot-15.png'),
(5, 'Sản phẩm ưu đãi', 'https://png.pngtree.com/png-clipart/20200225/original/pngtree-sale-tag-banner-with-discount-offer-in-orange-outline-style-for-png-image_5307423.jpg');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sanpham`
--

CREATE TABLE `sanpham` (
  `id` int(11) NOT NULL,
  `tensp` varchar(250) NOT NULL,
  `giasp` varchar(100) NOT NULL,
  `hinhanh` varchar(200) NOT NULL,
  `mota` varchar(200) NOT NULL,
  `loai` int(2) NOT NULL,
  `Quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `sanpham`
--

INSERT INTO `sanpham` (`id`, `tensp`, `giasp`, `hinhanh`, `mota`, `loai`, `Quantity`) VALUES
(1, 'NMD_R1 V2', '3790000', 'https://kingshoes.vn/data/upload/media/gz1999-giay-adidas-nmd-r1-v2-gia-tot-den-king-shoes-1.jpg', 'chất lượng phục vụ tốt và giá thành tốt nhất, cùng những “ Chương Trình Khuyến Mãi Đặc Biệt”.', 1, 0),
(2, 'Nike P-6000', '300000', 'https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/61abf749-ed21-43c6-96ed-15e26fbc9453/p-6000-shoes-QcQbpx.png', 'Nike P-6000 dựa trên Nike Air Pegasus 2006, mang đến cho bạn sự kết hợp giữa phong cách mang tính biểu tượng, thoáng khí, thoải mái và gợi lên cảm giác đầu những năm 2000.', 1, 0),
(3, 'Nike InfinityRN 4', '22990000', 'https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/c247d84e-2c5f-4602-b565-3bd1fdf724a3/infinityrn-4-road-running-shoes-mLRjcz.png', 'Đệm tối đa mang lại sự thoải mái cao hơn cho hoạt động hàng ngày. Trải nghiệm nền tảng mềm mại, hình rocker được làm bằng bọt ReactX mới dưới chân', 2, 48),
(4, 'Giày Nike Vaporfly 3', '23290000', 'https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/e3832ea3-e857-4ad1-8626-7c3b8d204a0e/vaporfly-3-road-racing-shoes-xsDgvM.png', 'Nike Vaporfly 3 được tạo ra dành cho những người theo đuổi, những tay đua và những người có tốc độ cao,', 1, 45),
(5, 'Giày Nike Vomero 17', '12550000', 'https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/f06185de-e651-45a7-b4a6-c18091a88818/vomero-17-road-running-shoes-qXFWTq.png', 'Trải nghiệm sự thoải mái được nâng cao trong phiên bản mới nhất này, với đế giữa mật độ kép được làm bằng sự kết hợp giữa ZoomX dưới chân và bọt Cushlon 3.0 xuyên suốt.', 1, 50),
(6, 'Nike Dunk Thấp', '9850000', 'https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/3ff2b05a-1070-41ea-ac43-d199c6cc875b/dunk-low-shoes-bJ5Qff.png', 'Được thiết kế dành cho gỗ cứng nhưng lại được ưa chuộng trên đường phố, biểu tượng bóng rổ của thập niên 80 này trở lại với các chi tiết cổ điển và sự tinh tế của những chiếc vòng cổ điển.', 2, 50),
(7, 'Nike Dunk Thấp', '9350000', 'https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/f36d4e1f-127b-450e-95c8-606e5b57b2fa/dunk-low-shoes-r8QXCd.png', 'Với thiết kế vòng mang tính biểu tượng, Nike Dunk Low mang phong cách cổ điển của thập niên 80 trở lại đường phố trong khi cổ giày có đệm', 2, 48),
(8, 'Nike Dunk Cao', '26990000', 'https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/28980b3e-9986-49a0-b5be-7f29e2b62c4d/dunk-high-shoes-n3vgBk.png', 'Thiết kế vòng cổ điển mang phong cách cổ điển của thập niên 80 trở lại đường phố trong khi cổ áo cao cấp có đệm tạo thêm vẻ cổ điển', 2, 50),
(9, 'Giày thể thao Adidas Originals Retropy F2', '11590000', 'https://m.media-amazon.com/images/I/51PBI-T2N7L._AC_SX500_.jpg', 'Giày nam cổ điển mang lại sự thoải mái hàng ngày\r\nCHẤT LIỆU TRÊN TRÊN: Vải dệt phía trên có lớp phủ da lộn và lưới\r\n', 4, 50),
(10, 'Adidas Zx 1K Boost - Giày nam theo mùa', '26990000', 'https://m.media-amazon.com/images/I/51Ud1iPmdqL._AC_SX500_.jpg', 'GIÀY SIÊU THOẢI MÁI VỚI VẺ NGOÀI TẦM NHÌN CAO.\r\nĐan lưới phía trên\r\nĐế giữa Boost và EVA\r\nĐế giày cao su\r\nCảm giác ổn định', 3, 50),
(11, 'Giày chạy bộ adidas Duramo Sl 2.0', '11590000', 'https://m.media-amazon.com/images/I/813qZHeb1LL._AC_SX500_.jpg', 'Giày chạy bộ siêu nhẹ cho nam với kiểu dáng trung tính\r\nPHẦN TRÊN HỖ TRỢ: Phần trên bằng lưới và dệt mang lại khả năng thở và hỗ trợ', 3, 49),
(12, 'Giày chạy bộ nữ adidas Distancestar W', '11590000', 'https://m.media-amazon.com/images/I/71qT7BQkOwL._AC_SX500_.jpg', 'Giày chạy bộ nam adidas\r\nThương hiệu adidas có lịch sử lâu đời và mối liên hệ sâu sắc với thể thao. Mọi thứ chúng tôi làm đều bắt nguồn từ thể thao.', 4, 50),
(13, 'Giày chạy bộ Questar adidas', '11590000', 'https://m.media-amazon.com/images/I/51bRTGmEipL._AC_SX500_.jpg', 'Giày chạy bộ tối ưu cho nam chạy bộ\r\ngiày thể thao thương hiệu adidas\r\nGiày chạy bộ DNA ULTRABOOST 5.0 (GZ0444)', 5, 49),
(14, 'Giày Golf đinh liền adidas Flopshot', '11590000', 'https://m.media-amazon.com/images/I/713zUs832SL._AC_SX500_.jpg', 'Giày golf nam mang lại cảm giác thoải mái khi chơi golf\r\nMặt trên bằng da không thấm nước để sử dụng lâu dài\r\nĐế giữa tăng cường để đệm đàn hồi', 5, 16),
(15, 'Giày đi bộ adidas Terrex Voyager 21 Ripstop', '11590000', 'https://m.media-amazon.com/images/I/71YJl-U2+0L._AC_SX500_.jpg', 'Giày nam mang lại cảm giác thoải mái khi đi du lịch\r\nLỚP DỆT: Lớp lót dệt mang lại cảm giác mềm mại, thoải mái\r\nWATERPROOF GORE-TEX: Màng GORE-TEX mang lại hiệu quả chống thấm nước, thoáng khí', 4, 49),
(100, 'Giày chạy bộ adidas Lite Racer Adapt 5.0', '23340000', 'https://m.media-amazon.com/images/I/71aqf++AB5L._AC_SX500_.jpg', 'Giày chạy bộ lấy cảm hứng từ nam giới mang lại sự thoải mái nhẹ nhàng\r\nĐẾ GIỮA ĐỆM: Đế giữa bằng mây tạo bọt mang lại sự thoải mái khi bước vào và khả năng giảm chấn vượt trội\r\n', 4, 28);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `thanhvien`
--

CREATE TABLE `thanhvien` (
  `id` int(11) NOT NULL,
  `email` varchar(250) NOT NULL,
  `pass` varchar(250) NOT NULL,
  `username` varchar(100) NOT NULL,
  `mobile` varchar(15) NOT NULL,
  `address` varchar(255) NOT NULL,
  `Decentralization` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `thanhvien`
--

INSERT INTO `thanhvien` (`id`, `email`, `pass`, `username`, `mobile`, `address`, `Decentralization`) VALUES
(2, 'duc89@gmail.com', '123456', 'Admin', '0123456789', 'Ha Noi', 'Admin');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `email` varchar(250) NOT NULL,
  `pass` varchar(250) NOT NULL,
  `username` varchar(100) NOT NULL,
  `mobile` varchar(15) NOT NULL,
  `address` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `user`
--

INSERT INTO `user` (`id`, `email`, `pass`, `username`, `mobile`, `address`) VALUES
(35, 'user@gmail.com', '123', 'Khách hàng 1', '0123456789', 'Ha Noi'),
(39, 'kh2@gmail.com', '123456', 'Khach hang 2', '0123456789', 'Nam Dinh');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD PRIMARY KEY (`id`),
  ADD KEY `iddonhang` (`iddonhang`),
  ADD KEY `idsp` (`idsp`);

--
-- Chỉ mục cho bảng `donhang`
--
ALTER TABLE `donhang`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `loaisanpham`
--
ALTER TABLE `loaisanpham`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `thanhvien`
--
ALTER TABLE `thanhvien`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `donhang`
--
ALTER TABLE `donhang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=248;

--
-- AUTO_INCREMENT cho bảng `loaisanpham`
--
ALTER TABLE `loaisanpham`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT cho bảng `sanpham`
--
ALTER TABLE `sanpham`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=112;

--
-- AUTO_INCREMENT cho bảng `thanhvien`
--
ALTER TABLE `thanhvien`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

--
-- AUTO_INCREMENT cho bảng `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `chitietdonhang`
--
ALTER TABLE `chitietdonhang`
  ADD CONSTRAINT `chitietdonhang_ibfk_1` FOREIGN KEY (`iddonhang`) REFERENCES `donhang` (`id`),
  ADD CONSTRAINT `chitietdonhang_ibfk_2` FOREIGN KEY (`idsp`) REFERENCES `sanpham` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
