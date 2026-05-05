-- Seed data cho hệ thống quản lý bán hàng siêu thị
-- Chỉ insert nếu chưa có dữ liệu

-- Nhân viên
INSERT IGNORE INTO Employees (id, name, role, userName, passWord) VALUES
(1, 'Nguyễn Văn An', 'NHANVIENKHO', 'nhanvien1', '123456'),
(2, 'Trần Thị Bình', 'QUANLY', 'quanly1', '123456');

-- Nhà cung cấp
INSERT IGNORE INTO Suppliers (id, name, contactName, phone, email, address, taxCode, bankAccount, active) VALUES
(1, 'Công ty TNHH Vinamilk', 'Nguyễn Minh Đức', '0901234567', 'duc@vinamilk.vn', '10 Tân Trào, Q7, TP.HCM', '0300588049', '1234567890', true),
(2, 'Công ty CP TH True Milk', 'Phạm Thị Hoa', '0912345678', 'hoa@thtruemilk.vn', 'Nghĩa Đàn, Nghệ An', '2901234567', '2345678901', true),
(3, 'Công ty TNHH Masan', 'Lê Văn Cường', '0923456789', 'cuong@masan.vn', '12 Tân Thuận, Q7, TP.HCM', '0301234568', '3456789012', true),
(4, 'Công ty CP Acecook Việt Nam', 'Trần Văn Dũng', '0934567890', 'dung@acecook.vn', 'KCN Tân Bình, TP.HCM', '0302345679', '4567890123', true),
(5, 'Công ty TNHH Unilever Việt Nam', 'Hoàng Thị Em', '0945678901', 'em@unilever.vn', 'KCN Tây Bắc Củ Chi, TP.HCM', '0303456780', '5678901234', false);

-- Sản phẩm
INSERT IGNORE INTO Products (id, name, barcode, price, quantity) VALUES
(1, 'Sữa tươi Vinamilk 1L', '8934673581019', 35000, 100),
(2, 'Sữa chua Vinamilk', '8934673581026', 6000, 200),
(3, 'Sữa tươi TH True Milk 1L', '8935049500117', 38000, 80),
(4, 'Mì Hảo Hảo tôm chua cay', '8934563338015', 4500, 500),
(5, 'Nước mắm Chin-Su 500ml', '8934561097017', 25000, 150),
(6, 'Bột giặt OMO 4kg', '8934868063209', 155000, 50),
(7, 'Dầu ăn Tường An 1L', '8936007100116', 42000, 70),
(8, 'Nước tương Maggi 300ml', '8934804019185', 12000, 120),
(9, 'Kem đánh răng P/S 180g', '8934868100508', 32000, 90),
(10, 'Sữa đặc Ông Thọ 380g', '8934673580012', 22000, 160);

-- Phiếu nhập hàng
INSERT IGNORE INTO GoodsReceipts (id, receiveDate, note, employeeId, supplierId) VALUES
(1, '2026-04-01', 'Nhập hàng đợt 1 tháng 4', 1, 1),
(2, '2026-04-10', 'Nhập mì và nước mắm', 1, 3),
(3, '2026-04-20', 'Nhập sữa TH', 1, 2);

-- Chi tiết phiếu nhập
INSERT IGNORE INTO GoodsReceiptsItem (id, priceImport, quantity, ProductId, goodsReceiptId) VALUES
(1, 28000, 50, 1, 1),
(2, 4500, 100, 2, 1),
(3, 3500, 200, 4, 2),
(4, 20000, 80, 5, 2),
(5, 30000, 40, 3, 3),
(6, 18000, 60, 10, 1);
