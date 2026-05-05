# Hệ thống Quản lý Bán hàng Siêu thị — Implementation Plan

## Mô tả
Triển khai hệ thống quản lý bán hàng siêu thị theo đúng kiến trúc MVC được thiết kế trong báo cáo. Hệ thống bao gồm 3 module:
1. **Quản lý nhà cung cấp** (Supplier Management)
2. **Nhân viên nhập hàng từ nhà cung cấp** (Goods Receipt)
3. **Thống kê nhà cung cấp theo lượng hàng nhập** (Supplier Statistics)

**Công nghệ:** Spring Boot + Spring MVC + Spring Data JPA + Thymeleaf + MySQL

## User Review Required

> [!IMPORTANT]
> Cần xác nhận thông tin kết nối MySQL:
> - Host, port (mặc định: `localhost:3306`)
> - Tên database (đề xuất: `supermarket_management`)
> - Username/password MySQL (mặc định: `root` / trống hoặc `root`)

> [!WARNING]
> Cần cài đặt sẵn: **JDK 17+**, **Maven**, **MySQL Server** trên máy.

## Open Questions

1. Phiên bản Spring Boot ưu tiên? (Đề xuất: **3.2.x**)
2. Có cần seed data mẫu (nhân viên, nhà cung cấp, sản phẩm) để test không? (Đề xuất: **Có**)
3. Password MySQL là gì? (Đề xuất dùng `root`/`root`)

---

## Proposed Changes

### Database Schema (MySQL)

#### [NEW] `schema.sql` — Tạo database và các bảng

5 bảng chính theo báo cáo thiết kế:

| Bảng | Mô tả | Các trường |
|------|--------|------------|
| `employees` | Nhân viên | id, name, role, user_name, pass_word |
| `suppliers` | Nhà cung cấp | id, name, contact_name, phone, email, address, tax_code, bank_account, active |
| `products` | Sản phẩm | id, name, barcode, price, quantity |
| `goods_receipts` | Phiếu nhập hàng | id, receive_date, note, employee_id (FK), supplier_id (FK) |
| `goods_receipts_item` | Chi tiết phiếu nhập | id, price_import, quantity, product_id (FK), goods_receipt_id (FK) |

Quan hệ:
- `employees` 1-n `goods_receipts`
- `suppliers` 1-n `goods_receipts`  
- `goods_receipts` 1-n `goods_receipts_item`
- `products` 1-n `goods_receipts_item`

---

### Spring Boot Project Structure

```
src/main/java/com/supermarket/
├── SupermarketApplication.java          # Main class
├── model/                                # Entity classes
│   ├── Employees.java
│   ├── Suppliers.java
│   ├── Products.java
│   ├── GoodsReceipts.java
│   ├── GoodsReceiptsItem.java
│   └── SupplierStat.java               # DTO cho thống kê
├── repository/                           # JPA Repositories
│   ├── EmployeeRepository.java
│   ├── SupplierRepository.java
│   ├── ProductRepository.java
│   ├── GoodsReceiptRepository.java
│   └── GoodsReceiptsItemRepository.java
├── service/                              # Business Logic
│   ├── AuthService.java
│   ├── SupplierService.java
│   ├── ProductService.java
│   ├── GoodsReceiptService.java
│   └── StatisticsService.java
└── controller/                           # MVC Controllers
    ├── AuthController.java
    ├── DieuHuongController.java
    ├── SupplierController.java
    ├── GoodsReceiptController.java
    └── StatisticsController.java

src/main/resources/
├── application.properties                # Cấu hình MySQL, Thymeleaf
├── data.sql                              # Seed data
└── templates/                            # Thymeleaf HTML templates
    ├── dangNhap.html
    ├── trangChuNhanVienKho.html
    ├── trangChuQuanLy.html
    ├── quanLyNhaCungCap.html
    ├── danhSachNhaCungCap.html
    ├── thongTinNhaCungCap.html
    ├── lapPhieuNhapHang.html
    ├── timKiemSanPham.html
    ├── danhSachSanPham.html
    ├── chiTietNhapHang.html
    ├── themMoiSanPham.html
    ├── tuyChonBaoCao.html
    ├── thongKeNhaCungCap.html
    └── chiTietNhaCungCapThongKe.html
```

---

### Model Layer (Entity Classes)

#### [NEW] `Employees.java`
- `id` (Integer, PK, auto-increment)
- `name` (String)
- `role` (String) — "NHANVIENKHO" hoặc "QUANLY"
- `userName` (String)
- `passWord` (String)

#### [NEW] `Suppliers.java`
- `id` (Integer, PK, auto-increment)
- `name` (String)
- `contactName` (String)
- `phone` (String)
- `email` (String)
- `address` (String)
- `taxCode` (String)
- `bankAccount` (String)
- `active` (Boolean)

#### [NEW] `Products.java`
- `id` (Integer, PK, auto-increment)
- `name` (String)
- `barcode` (String)
- `price` (Double)
- `quantity` (Integer)

#### [NEW] `GoodsReceipts.java`
- `id` (Integer, PK, auto-increment)
- `receiveDate` (Date)
- `note` (String)
- `employeeId` (Integer, FK → employees)
- `supplierId` (Integer, FK → suppliers)

#### [NEW] `GoodsReceiptsItem.java`
- `id` (Integer, PK, auto-increment)
- `priceImport` (Double)
- `quantity` (Integer)
- `productId` (Integer, FK → products)
- `goodsReceiptId` (Integer, FK → goods_receipts)

#### [NEW] `SupplierStat.java`
- DTO kế thừa/wrap từ Suppliers, thêm `totalImportQuantity`

---

### Repository Layer

#### [NEW] `EmployeeRepository.java`
```java
public interface EmployeeRepository extends JpaRepository<Employees, Integer> {
    Employees findByUserName(String userName);
}
```

#### [NEW] `SupplierRepository.java`
```java
public interface SupplierRepository extends JpaRepository<Suppliers, Integer> {
    List<Suppliers> findAll();
    List<Suppliers> findByNameContaining(String name);
    Optional<Suppliers> findById(Integer id);
    Suppliers save(Suppliers supplier);
}
```

#### [NEW] `ProductRepository.java`
```java
public interface ProductRepository extends JpaRepository<Products, Integer> {
    List<Products> findByBarcodeContaining(String barcode);
}
```

#### [NEW] `GoodsReceiptRepository.java`
```java
public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipts, Integer> {
    List<GoodsReceipts> findBySupplierIdAndReceiveDateBetween(
        Integer supplierId, Date fromDate, Date toDate);
}
```

#### [NEW] `GoodsReceiptsItemRepository.java`
```java
public interface GoodsReceiptsItemRepository extends JpaRepository<GoodsReceiptsItem, Integer> {
    List<GoodsReceiptsItem> findByGoodsReceiptId(Integer goodsReceiptId);
}
```

---

### Service Layer

#### [NEW] `AuthService.java`
- `dangNhap(userName, passWord)` → Employees
- `checkRole(Employees)` → boolean

#### [NEW] `SupplierService.java`
- `findSupplier(name)` → List<Suppliers>
- `detailSupplier(id)` → Suppliers
- `addSupplier(Suppliers)` → Suppliers
- `updateSupplier(Suppliers)` → Suppliers
- `findAll()` → List<Suppliers>

#### [NEW] `ProductService.java`
- `findByBarcode(barcode)` → List<Products>
- `save(Products)` → Products
- `updateStock(Products, quantity)` → void

#### [NEW] `GoodsReceiptService.java`
- `createReceipt(GoodsReceipts, List<GoodsReceiptsItem>)` → GoodsReceipts
- `findSupplier()` → List<Suppliers>
- `searchProduct(barcode)` → List<Products>
- `addItemToReceipt(GoodsReceiptsItem)` → GoodsReceiptsItem

#### [NEW] `StatisticsService.java`
- `getSupplierStats(fromDate, toDate)` → List<SupplierStat> (sắp xếp giảm dần)
- `getReceiptsBySupplier(supplierId, fromDate, toDate)` → List<GoodsReceipts>
- `sumQtyBySupplierId(supplierId, fromDate, toDate)` → Integer

---

### Controller Layer

#### [NEW] `AuthController.java`
| Endpoint | Method | Chức năng |
|----------|--------|-----------|
| `/login` | GET | `getDangNhap()` → dangNhap.html |
| `/login` | POST | `postDangNhap(userName, passWord)` → redirect trang chủ |

#### [NEW] `DieuHuongController.java`
| Endpoint | Method | Chức năng |
|----------|--------|-----------|
| `/trangChuNhanVienKho` | GET | `getTrangChuNhanVienKho()` |
| `/quanLyNhaCungCap` | GET | `getQuanLyNhaCungCap()` |
| `/trangChuQuanLy` | GET | `getTrangChuQuanLy()` |
| `/tuyChonBaoCao` | GET | `getTuyChonBaoCao()` |
| `/thongKeNCC` | GET | `getPageThongKeNCC()` |

#### [NEW] `SupplierController.java`
| Endpoint | Method | Chức năng |
|----------|--------|-----------|
| `/supplier/all` | GET | `getDanhSachNCC(name?)` → danh sách / tìm kiếm |
| `/supplier/{id}` | GET | `detailSupplier(id)` → chi tiết |
| `/supplier/add` | GET | `getFormThemNCC()` → form thêm |
| `/supplier/add` | POST | `addSupplier(Suppliers)` → lưu |
| `/supplier/edit/{id}` | GET | `getFormChinhSuaNCC(id)` → form sửa |
| `/supplier/edit/{id}` | POST | `updateSupplier(Suppliers)` → cập nhật |

#### [NEW] `GoodsReceiptController.java`
| Endpoint | Method | Chức năng |
|----------|--------|-----------|
| `/goodsReceipt/create` | GET | `getFormPhieuNhap()` |
| `/goodsReceipt/suppliers` | GET | `getDanhSachNCC(name?)` |
| `/goodsReceipt/chooseSupplier` | GET | `chonNhaCungCap(id)` |
| `/goodsReceipt/searchProduct` | GET | `timKiemSanPham(barcode?)` |
| `/goodsReceipt/itemDetail` | GET | `getChiTietNhapHang(id)` |
| `/goodsReceipt/addItem` | POST | `themVaoPhieu(...)` |
| `/goodsReceipt/newProduct` | GET | `getFormThemMoiSP()` |
| `/goodsReceipt/saveProduct` | POST | `luuSanPhamMoi(...)` |
| `/goodsReceipt/create` | POST | `createGoodsReceipt(...)` |

#### [NEW] `StatisticsController.java`
| Endpoint | Method | Chức năng |
|----------|--------|-----------|
| `/statistics/suppliers` | GET | `thongKeNCC(fromDate, toDate)` |
| `/statistics/suppliers/detail` | GET | `getChiTietNCC(supplierId, fromDate, toDate)` |

---

### View Layer (Thymeleaf Templates)

14 templates HTML theo báo cáo thiết kế:

1. `dangNhap.html` — Đăng nhập
2. `trangChuNhanVienKho.html` — Trang chủ nhân viên kho
3. `trangChuQuanLy.html` — Trang chủ quản lý
4. `quanLyNhaCungCap.html` — Tùy chọn QLNCC (Thêm/Sửa/Tìm kiếm)
5. `danhSachNhaCungCap.html` — Danh sách NCC + tìm kiếm
6. `thongTinNhaCungCap.html` — Chi tiết/Thêm/Sửa NCC
7. `lapPhieuNhapHang.html` — Lập phiếu nhập
8. `timKiemSanPham.html` — Tìm kiếm sản phẩm
9. `danhSachSanPham.html` — Danh sách sản phẩm tìm được
10. `chiTietNhapHang.html` — Chi tiết nhập hàng (số lượng, giá nhập)
11. `themMoiSanPham.html` — Thêm mới sản phẩm
12. `tuyChonBaoCao.html` — Tùy chọn báo cáo
13. `thongKeNhaCungCap.html` — Thống kê NCC
14. `chiTietNhaCungCapThongKe.html` — Chi tiết phiếu nhập theo NCC

---

## Verification Plan

### Automated Tests
```bash
# Build project
mvn clean compile

# Run application
mvn spring-boot:run
```

### Manual Verification
1. Truy cập `http://localhost:8080/login` → Đăng nhập
2. **Module 1:** Quản lý NCC (CRUD)
3. **Module 2:** Lập phiếu nhập hàng
4. **Module 3:** Thống kê NCC theo lượng hàng nhập
5. Kiểm tra dữ liệu trong MySQL

### Seed Data
- 2 nhân viên: 1 nhân viên kho + 1 quản lý
- 5 nhà cung cấp mẫu
- 10 sản phẩm mẫu
- 3 phiếu nhập hàng mẫu
