CREATE DATABASE QLCHXEDAP
use QLCHXEDAP;

-- Dang nhap
CREATE TABLE DangNhap (
	HoTen NVARCHAR(100),
    SoDienThoai NVARCHAR(15),
    ID NVARCHAR(30) PRIMARY KEY,       
    PASS NVARCHAR(64) NOT NULL,        
    ROLE NVARCHAR(10) NOT NULL        
);

INSERT INTO DangNhap VALUES
('Do Hoang K', '0977853401', 'admin01', 'passql123', 'Quan ly'),
('Hoang Van J', '0945678901','admin02', 'passql123', 'Quan ly'),
('Le Thi G', '0912340987','admin03', 'passql123', 'Quan ly'),
('Nguyen Van F', '0909876543', 'nv01', 'passnv01', 'Nhan vien'),
('Tran Van H', '0923456789', 'nv02', 'passnv02', 'Nhan vien'),
('Pham Thi I', '0934567890', 'nv03', 'passnv03', 'Nhan vien');

DELETE FROM DangNhap;

-- 1. Thông tin Xe đạp (Bicycles)
CREATE TABLE ThongTinXeDap (
    MaXe VARCHAR(10) PRIMARY KEY,
    TenXe NVARCHAR(100),
    HangXe NVARCHAR(100),
    LoaiXe NVARCHAR(50),
    GiaBan DECIMAL(18, 2),
    SoLuongTonKho INT
);

INSERT INTO ThongTinXeDap VALUES
('Xe01', 'Roadster', 'BrandA', 'Xe đạp đường trường', 5000000, 10),
('Xe02', 'Mountain Pro', 'BrandB', 'Xe đạp địa hình', 7000000, 0),
('Xe03', 'City Cruiser', 'BrandC', 'Xe đạp thành phố', 4500000, 5),
('Xe04', 'Hybrid X', 'BrandD', 'Xe đạp lai', 6000000, 15),
('Xe05', 'Speedster', 'BrandE', 'Xe đạp thể thao', 8000000, 7);

-- 2. Khách hàng (Customers)
CREATE TABLE KhachHang (
    MaKH VARCHAR(10) PRIMARY KEY,
    HoTen NVARCHAR(100),
    SoDienThoai NVARCHAR(15),
    Email NVARCHAR(100),
    DiaChi NVARCHAR(200)
);

INSERT INTO KhachHang VALUES
('KH01', 'Nguyen Van A', '0901234567', 'nguyenvana@gmail.com', '123 Đường A, TP. HCM'),
('KH02', 'Tran Thi B', '0912345678', 'tranthib@gmail.com', '456 Đường B, Hà Nội'),
('KH03', 'Le Quang C', '0938765432', 'lequangc@yahoo.com', '789 Đường C, Đà Nẵng'),
('KH04', 'Pham Thi D', '0923456789', 'phamthid@hotmail.com', '111 Đường D, Hải Phòng'),
('KH05', 'Hoang Minh E', '0945678901', 'hoangminhe@gmail.com', '222 Đường E, Cần Thơ');

-- 3. Thuê xe (Rentals)
CREATE TABLE ThueXe (
    MaPhieuThue VARCHAR(10) PRIMARY KEY,
    MaKH VARCHAR(10) FOREIGN KEY REFERENCES KhachHang(MaKH),
    MaXe VARCHAR(10) FOREIGN KEY REFERENCES ThongTinXeDap(MaXe),
    NgayThue DATE,
    GiaThue DECIMAL(18, 2),
    TongThoiGianThue CHAR(10)
);

INSERT INTO ThueXe VALUES
('PT1', 'KH01', 'Xe03', '2024-09-01', 200000, '3.5 giờ' ),
('PT2', 'KH02', 'Xe02', '2024-08-15', 300000, '4 giờ'),
('PT3', 'KH03', 'Xe01', '2024-09-10', 150000, '1 giờ'),
('PT4', 'KH04', 'Xe04', '2024-09-20', 250000, '5 giờ'),
('PT5', 'KH05', 'Xe05', '2024-09-25', 350000, '7 giờ');

-- 4. Kho hàng (Inventory)
CREATE TABLE KhoHang (
    MaKho INT PRIMARY KEY,
    MaXe INT FOREIGN KEY REFERENCES ThongTinXeDap(MaXe),
    MaNCC INT,
    SoLuongNhap INT,
    NgayNhap DATE,
    GiaNhap DECIMAL(18, 2)
);

INSERT INTO KhoHang VALUES
(1, 1, 1, 20, '2024-08-01', 4500000),
(2, 2, 2, 15, '2024-07-20', 6000000),
(3, 3, 3, 10, '2024-08-15', 4000000),
(4, 4, 4, 25, '2024-09-05', 5500000),
(5, 5, 5, 30, '2024-09-10', 7000000);

-- 5. Thanh toán (Payments)
CREATE TABLE ThanhToan (
    MaThanhToan VARCHAR(10) PRIMARY KEY,
    MaXe VARCHAR(10) FOREIGN KEY REFERENCES ThongTinXeDap(MaXe),
    MaKH VARCHAR(10) FOREIGN KEY REFERENCES KhachHang(MaKH),
    NgayThanhToan DATE,
    PhuongThucThanhToan NVARCHAR(50),
    SoLuongXeMua INT,
    ThanhTien DECIMAL(18, 2)
);

INSERT INTO ThanhToan VALUES
('TT01', 'Xe01', 'KH01', '2024-09-01', 'Tiền mặt', 1, 5000000),
('TT02', 'Xe02', 'KH02', '2024-08-18', 'Thẻ', 1, 7000000),
('TT03', 'Xe03', 'KH03', '2024-09-11', 'Chuyển khoản', 1, 4500000),
('TT04', 'Xe04', 'KH04', '2024-09-20', 'Tiền mặt', 1, 6000000),
('TT05', 'Xe05', 'KH05', '2024-09-25', 'Chuyển khoản', 1, 8000000);

-- 6. Nhân viên (Employees)
CREATE TABLE NhanVien (
    MaNV VARCHAR(10) PRIMARY KEY,
    HoTen NVARCHAR(100),
    ChucVu NVARCHAR(50),
    SoDienThoai NVARCHAR(15),
    Email NVARCHAR(100)
);

INSERT INTO NhanVien VALUES
('NV01', 'Nguyen Van F', 'Bán hàng', '0909876543', 'nguyenvanf@store.com'),
('NV02', 'Le Thi G', 'Quản lý thuê xe', '0912340987', 'lethig@store.com'),
('NV03', 'Tran Van H', 'Nhân viên kho', '0923456789', 'tranvanh@store.com'),
('NV04', 'Pham Thi I', 'Kế toán', '0934567890', 'phamthii@store.com'),
('NV05', 'Hoang Van J', 'Quản lý cửa hàng', '0945678901', 'hoangvanj@store.com'),
('NV06', 'Do Hoang K', 'Quản lý trưởng', '0977853401', 'dohoangj@store.com');

DELETE FROM NhanVien;

-- 7. Nhà cung cấp (Suppliers)
CREATE TABLE NhaCungCap (
    MaNCC INT PRIMARY KEY,
    TenNCC NVARCHAR(100),
    ThongTinLienHe NVARCHAR(100),
    DiaChi NVARCHAR(200)
);

INSERT INTO NhaCungCap VALUES
(1, 'NCC A', '0901234567', '123 Đường A, TP. HCM'),
(2, 'NCC B', '0912345678', '456 Đường B, Hà Nội'),
(3, 'NCC C', '0923456789', '789 Đường C, Đà Nẵng'),
(4, 'NCC D', '0934567890', '111 Đường D, Hải Phòng'),
(5, 'NCC E', '0945678901', '222 Đường E, Cần Thơ');

-- 8. Bảo dưỡng Xe cho thuê (Service)
CREATE TABLE BaoDuongXe (
    MaDichVu INT PRIMARY KEY,
    MaXe INT FOREIGN KEY REFERENCES ThongTinXeDap(MaXe),
    NgayBaoDuong DATE,
    MoTa NVARCHAR(255),
    ChiPhiBaoDuong DECIMAL(18, 2)
);

INSERT INTO BaoDuongXe VALUES
(1, 1, '2024-08-01', 'Thay lốp', 200000),
(2, 2, '2024-08-15', 'Bảo dưỡng định kỳ', 150000),
(3, 3, '2024-09-01', 'Sửa phanh', 300000),
(4, 4, '2024-09-10', 'Thay xích', 250000),
(5, 5, '2024-09-20', 'Sửa thắng đĩa', 350000);

SELECT * FROM INFORMATION_SCHEMA.TABLES