-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th5 01, 2023 lúc 10:51 AM
-- Phiên bản máy phục vụ: 10.4.27-MariaDB
-- Phiên bản PHP: 8.1.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `aitechc2`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cart_details`
--

CREATE TABLE `cart_details` (
  `id_cart_details` int(11) NOT NULL,
  `code_cart` int(10) NOT NULL,
  `name_product` varchar(100) NOT NULL,
  `cost_product` varchar(100) NOT NULL,
  `name_user` varchar(20) NOT NULL,
  `status` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `cart_details`
--

INSERT INTO `cart_details` (`id_cart_details`, `code_cart`, `name_product`, `cost_product`, `name_user`, `status`) VALUES
(1, 964209, 'Quản lý kết cấu hạ tầng đường bộ', '7000000', 'hoangpho', 0),
(2, 913511, 'Quản lý thư viện', '7000000', 'thành công', 0);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cart_payments`
--

CREATE TABLE `cart_payments` (
  `id_cart` int(11) NOT NULL,
  `name_user` varchar(100) NOT NULL,
  `code_cart` varchar(10) NOT NULL,
  `cart_status` int(11) NOT NULL,
  `cart_date` varchar(50) NOT NULL,
  `cart_number` int(11) NOT NULL,
  `cart_cost` int(50) NOT NULL,
  `cart_payment` varchar(50) NOT NULL,
  `id_shipping` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `cart_payments`
--

INSERT INTO `cart_payments` (`id_cart`, `name_user`, `code_cart`, `cart_status`, `cart_date`, `cart_number`, `cart_cost`, `cart_payment`, `id_shipping`) VALUES
(1, 'Hoàng phố', '964209', 0, '2023-04-30', 1, 7000000, 'Thanh Toán Trực Tiếp', 1),
(2, 'Thành công', '913511', 0, '2023-04-30', 1, 7000000, 'Thanh Toán Trực Tiếp', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `categories`
--

CREATE TABLE `categories` (
  `id_category` int(11) NOT NULL,
  `category` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `categories`
--

INSERT INTO `categories` (`id_category`, `category`) VALUES
(1, 'Công Nghệ'),
(2, 'Trường học'),
(3, 'Nhà hàng'),
(4, 'Khách sạn'),
(5, 'Thương mại'),
(6, 'Y tế'),
(7, 'Giao thông'),
(8, 'Thời tiết');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `comments`
--

CREATE TABLE `comments` (
  `id_comment` int(11) NOT NULL,
  `name_user` varchar(100) NOT NULL,
  `name_product` varchar(100) NOT NULL,
  `comment_product` text NOT NULL,
  `rate` float NOT NULL,
  `date_comment` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `comments`
--

INSERT INTO `comments` (`id_comment`, `name_user`, `name_product`, `comment_product`, `rate`, `date_comment`) VALUES
(1, 'hoangpho', 'Quản lý kết cấu hạ tầng đường bộ', 'Sản phẩm chất lượng', 5, '2023-04-30'),
(2, 'thành công', 'Quản lý thư viện', 'sản phẩm tốt', 5, '2023-04-30');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `products`
--

CREATE TABLE `products` (
  `id_product` int(11) NOT NULL,
  `name_product` varchar(100) NOT NULL,
  `cost_product` varchar(200) NOT NULL,
  `image_product` text NOT NULL,
  `detail_product` varchar(200) NOT NULL,
  `category` varchar(200) NOT NULL,
  `rate` float NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `products`
--

INSERT INTO `products` (`id_product`, `name_product`, `cost_product`, `image_product`, `detail_product`, `category`, `rate`, `status`) VALUES
(1, 'Quản lý thư viện', '13000000', '1682843420_thuvien.png', 'Phần mềm quản lý thư viện hỗ trợ đắc lực cho cán bộ quản lý, quản lý nguồn tài nguyên hiện có trong thư viện nhanh chóng và chính xác.', 'Trường học', 5, 1),
(2, 'Quản lý kết cấu hạ tầng đường bộ', '7000000', '1682843616_giaothong.png', 'Giúp phát huy và nâng cao hiệu quả công tác quản lý bảo trì kết cấu hạ tầng đường bộ. Góp phần vào lộ trình triển khai và hoàn thiện hệ thống giao thông', 'Giao thông', 5, 1),
(3, 'Phần mềm quản lý bệnh viện', '40890000', '1682843718_yte.png', 'Phần mềm quản lý bệnh viện là một trong những thành tựu công nghệ được ứng dụng rộng rãi trong ngành Y tế. Đây là sự kết hợp các giải pháp công nghệ vào hoạt động quản lý của bệnh viện. Phần mềm được ', 'Y tế', 0, 1),
(4, 'Nhận diện khuôn mặt', '99000000', '1682843843_nhandienkhuongmat.png', 'Tăng cường thêm tính năng nhận dạng được khuôn mặt khi đeo khẩu trang, đảm bảo an toàn nơi làm việc với vai trò là một camera an ninh cảnh báo xâm nhập lạ, tình huống nguy hiểm', 'Công Nghệ', 0, 1),
(5, 'Quản lý nhà hàng', '20000000', '1682843914_banhang.png', 'Quản lý nhà hàng', 'Nhà hàng', 0, 1),
(6, 'Quản lý trường học', '3000000', '1682843959_truonghoc.png', 'Phần mềm quản lý trường học', 'Trường học', 0, 1),
(7, 'Quản lý khách sạn', '6100000', '1682844017_khachsan.png', 'Phần mềm quản lý khách sạn', 'Khách sạn', 0, 1),
(8, 'Chăm sóc sức khỏe', '23900000', '1682844074_suckhoe.png', 'Ứng dụng chăm sóc sức khỏe', 'Y tế', 0, 1),
(9, 'Phần mềm bán quần áo', '80000000', '1682844180_thuongmai.png', 'Phầm mềm thương mại điện tử', 'Thương mại', 0, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `shipping`
--

CREATE TABLE `shipping` (
  `id_shipping` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone` varchar(50) NOT NULL,
  `address` varchar(200) NOT NULL,
  `note` varchar(255) DEFAULT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `shipping`
--

INSERT INTO `shipping` (`id_shipping`, `name`, `phone`, `address`, `note`, `id_user`) VALUES
(1, 'Thành công', '1234567890', '470 Trần Đại Nghĩa - Hòa Quý - Ngũ hành Sơn - Đà Nẵng', 'abc', 4);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` int(6) UNSIGNED NOT NULL,
  `name` text DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `address` varchar(100) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `gender` char(9) DEFAULT NULL,
  `image` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `address`, `phone`, `password`, `gender`, `image`) VALUES
(1, 'hoangpho', 'hoangpho@gmail.com', 'đà nẵng', '1234567890', '$2y$10$tZKtDqPfFQrzgtZTabOGfew3c9J00Te57sXDv8Dk5jE4mrP7G5rgu', 'Nam', 'pho.jpg'),
(2, 'thuphuong', 'thuphuong@gmail.com', 'Đà Nẵng', '0987654321', '$2y$10$lB07wvGPxGh5fzayaqn7CuwZJZdC4GVQvtQUIkwC0l/wrzHRREPVu', 'Nữ', NULL),
(3, 'huong', 'huong@gmail.com', 'Đà nẵng', '123456789', '$2y$10$1lpdcxqKqg/ulBjhmTj8fe6//wJPj5hq8InaY9LI.niIbkvLlwhKW', 'Nữ', NULL),
(4, 'thành công', 'thanhcong@gmail.com', 'Nam Định', '1234567890', '$2y$10$Mk9jMoHRvLwhDvYYla6lDuJKzlXzHMYrL6k777gx5EgAa026WLCDS', 'Nam', 'anhdaidienuser.png');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `cart_details`
--
ALTER TABLE `cart_details`
  ADD PRIMARY KEY (`id_cart_details`);

--
-- Chỉ mục cho bảng `cart_payments`
--
ALTER TABLE `cart_payments`
  ADD PRIMARY KEY (`id_cart`);

--
-- Chỉ mục cho bảng `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id_category`);

--
-- Chỉ mục cho bảng `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`id_comment`);

--
-- Chỉ mục cho bảng `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id_product`);

--
-- Chỉ mục cho bảng `shipping`
--
ALTER TABLE `shipping`
  ADD PRIMARY KEY (`id_shipping`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `cart_details`
--
ALTER TABLE `cart_details`
  MODIFY `id_cart_details` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `cart_payments`
--
ALTER TABLE `cart_payments`
  MODIFY `id_cart` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `categories`
--
ALTER TABLE `categories`
  MODIFY `id_category` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT cho bảng `comments`
--
ALTER TABLE `comments`
  MODIFY `id_comment` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `products`
--
ALTER TABLE `products`
  MODIFY `id_product` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT cho bảng `shipping`
--
ALTER TABLE `shipping`
  MODIFY `id_shipping` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` int(6) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
