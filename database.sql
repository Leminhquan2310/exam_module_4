-- SQL QUERY
-- Tạo database
CREATE DATABASE IF NOT EXISTS auction_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE auction_db;

-- Xóa các bảng nếu tồn tại
DROP TABLE IF EXISTS san_pham;
DROP TABLE IF EXISTS loai_san_pham;

-- Tạo bảng loại sản phẩm
CREATE TABLE loai_san_pham (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               name VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tạo bảng sản phẩm
CREATE TABLE san_pham (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(200) NOT NULL,
                          price DOUBLE NOT NULL,
                          status VARCHAR(100) NOT NULL,
                          id_loai_sp BIGINT NOT NULL,
                          FOREIGN KEY (id_loai_sp) REFERENCES loai_san_pham(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Thêm dữ liệu mẫu cho loại sản phẩm
INSERT INTO loai_san_pham (name) VALUES
                                     ('Điện tử'),
                                     ('Đồ gia dụng'),
                                     ('Thời trang'),
                                     ('Đồ cổ'),
                                     ('Nghệ thuật'),
                                     ('Xe cộ'),
                                     ('Bất động sản'),
                                     ('Đồ chơi'),
                                     ('Sách và tài liệu'),
                                     ('Trang sức');

-- Thêm dữ liệu mẫu cho sản phẩm
INSERT INTO san_pham (name, price, status, id_loai_sp) VALUES
                                                           ('iPhone 15 Pro Max 256GB', 25000000, 'Chưa đấu giá', 1),
                                                           ('Laptop Dell XPS 13', 20000000, 'Đang đấu giá', 1),
                                                           ('Máy giặt Samsung 9kg', 8500000, 'Chưa đấu giá', 2),
                                                           ('Tủ lạnh LG Inverter', 12000000, 'Đã đấu giá', 2),
                                                           ('Áo khoác da cao cấp', 3500000, 'Chưa đấu giá', 3),
                                                           ('Giày thể thao Nike Air', 2500000, 'Đang đấu giá', 3),
                                                           ('Bình gốm sứ cổ', 15000000, 'Đã đấu giá', 4),
                                                           ('Đồng hồ cổ Thụy Sĩ', 35000000, 'Chưa đấu giá', 4),
                                                           ('Tranh sơn dầu phong cảnh', 8000000, 'Đang đấu giá', 5),
                                                           ('Tượng gỗ thủ công', 5500000, 'Chưa đấu giá', 5),
                                                           ('Xe máy Honda SH 2023', 95000000, 'Đã bán', 6),
                                                           ('Xe đạp thể thao Giant', 12000000, 'Chưa đấu giá', 6),
                                                           ('Căn hộ 2PN quận 7', 3500000000, 'Đang đấu giá', 7),
                                                           ('Nhà phố Thủ Đức', 8500000000, 'Chưa đấu giá', 7),
                                                           ('Robot biến hình Gundam', 850000, 'Đã bán', 8),
                                                           ('Lego Star Wars', 2500000, 'Chưa đấu giá', 8),
                                                           ('Bộ sách Harry Potter', 1200000, 'Đang đấu giá', 9),
                                                           ('Tài liệu học tiếng Anh', 450000, 'Chưa đấu giá', 9),
                                                           ('Nhẫn vàng 18K', 15000000, 'Đã đấu giá', 10),
                                                           ('Dây chuyền bạc', 2800000, 'Chưa đấu giá', 10);

-- Thêm nhiều sản phẩm để test phân trang
INSERT INTO san_pham (name, price, status, id_loai_sp) VALUES
                                                           ('Smart TV Samsung 55 inch', 15000000, 'Chưa đấu giá', 1),
                                                           ('Máy ảnh Canon EOS R6', 45000000, 'Đang đấu giá', 1),
                                                           ('Tai nghe Sony WH-1000XM5', 7500000, 'Chưa đấu giá', 1),
                                                           ('Nồi chiên không dầu', 3200000, 'Đã đấu giá', 2),
                                                           ('Máy hút bụi Dyson V15', 18000000, 'Chưa đấu giá', 2),
                                                           ('Bộ đồ vest nam', 4500000, 'Đang đấu giá', 3),
                                                           ('Túi xách Louis Vuitton', 25000000, 'Đã bán', 3),
                                                           ('Bàn ghế gỗ cổ', 22000000, 'Chưa đấu giá', 4),
                                                           ('Đèn chùm pha lê cổ điển', 12500000, 'Đang đấu giá', 5),
                                                           ('Ô tô Toyota Camry 2022', 1250000000, 'Đã đấu giá', 6);
