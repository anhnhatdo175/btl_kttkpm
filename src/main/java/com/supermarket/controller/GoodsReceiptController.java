package com.supermarket.controller;

import com.supermarket.model.*;
import com.supermarket.service.GoodsReceiptService;
import com.supermarket.service.ProductService;
import com.supermarket.service.SupplierService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/goodsReceipt")
public class GoodsReceiptController {

    @Autowired
    private GoodsReceiptService goodsReceiptService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierService supplierService;

    /**
     * Hiển thị form lập phiếu nhập hàng
     */
    @GetMapping("/create")
    public String getFormPhieuNhap(HttpSession session, Model model) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        // Khởi tạo phiếu nhập mới trong session nếu chưa có
        if (session.getAttribute("currentReceipt") == null) {
            session.setAttribute("currentReceipt", new GoodsReceipts());
            session.setAttribute("currentItems", new ArrayList<GoodsReceiptsItem>());
        }
        model.addAttribute("receipt", session.getAttribute("currentReceipt"));
        model.addAttribute("items", session.getAttribute("currentItems"));

        // Lấy tên nhà cung cấp nếu đã chọn
        GoodsReceipts receipt = (GoodsReceipts) session.getAttribute("currentReceipt");
        if (receipt.getSupplierId() != null) {
            Suppliers supplier = supplierService.detailSupplier(receipt.getSupplierId());
            if (supplier != null) {
                model.addAttribute("supplierName", supplier.getName());
            }
        }
        return "lapPhieuNhapHang";
    }

    /**
     * Hiển thị danh sách nhà cung cấp để chọn
     */
    @GetMapping("/suppliers")
    public String getDanhSachNCC(@RequestParam(required = false) String name,
                                  Model model, HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        List<Suppliers> suppliers = goodsReceiptService.findSupplierByName(name);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("searchName", name);
        model.addAttribute("mode", "receipt");
        return "danhSachNhaCungCap";
    }

    /**
     * Chọn nhà cung cấp cho phiếu nhập
     */
    @GetMapping("/chooseSupplier")
    public String chonNhaCungCap(@RequestParam Integer id, HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        GoodsReceipts receipt = (GoodsReceipts) session.getAttribute("currentReceipt");
        if (receipt == null) {
            receipt = new GoodsReceipts();
            session.setAttribute("currentItems", new ArrayList<GoodsReceiptsItem>());
        }
        receipt.setSupplierId(id);
        session.setAttribute("currentReceipt", receipt);
        return "redirect:/goodsReceipt/create";
    }

    /**
     * Hiển thị trang tìm kiếm sản phẩm
     */
    @GetMapping("/searchProduct")
    public String timKiemSanPham(@RequestParam(required = false) String barcode,
                                  Model model, HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        if (barcode != null && !barcode.trim().isEmpty()) {
            List<Products> products = goodsReceiptService.searchProduct(barcode);
            if (products.isEmpty()) {
                model.addAttribute("notFound", true);
                model.addAttribute("barcode", barcode);
            } else {
                model.addAttribute("products", products);
            }
        }
        return "timKiemSanPham";
    }

    /**
     * Hiển thị danh sách sản phẩm tìm được
     */
    @GetMapping("/productList")
    public String getDanhSachSP(@RequestParam String barcode,
                                 Model model, HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        List<Products> products = goodsReceiptService.searchProduct(barcode);
        model.addAttribute("products", products);
        model.addAttribute("barcode", barcode);
        if (products.isEmpty()) {
            model.addAttribute("notFound", true);
        }
        return "danhSachSanPham";
    }

    /**
     * Hiển thị chi tiết nhập hàng (nhập số lượng, giá nhập)
     */
    @GetMapping("/itemDetail")
    public String getChiTietNhapHang(@RequestParam Integer id,
                                      Model model, HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        Products product = productService.findById(id);
        model.addAttribute("product", product);
        return "chiTietNhapHang";
    }

    /**
     * Thêm sản phẩm vào phiếu nhập (lưu tạm trong session)
     */
    @PostMapping("/addItem")
    public String themVaoPhieu(@RequestParam Integer productId,
                                @RequestParam Integer quantity,
                                @RequestParam Double priceImport,
                                HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }

        Products product = productService.findById(productId);
        GoodsReceiptsItem item = new GoodsReceiptsItem();
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setPriceImport(priceImport);
        if (product != null) {
            item.setProductName(product.getName());
            item.setProductBarcode(product.getBarcode());
        }

        @SuppressWarnings("unchecked")
        List<GoodsReceiptsItem> items = (List<GoodsReceiptsItem>) session.getAttribute("currentItems");
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        session.setAttribute("currentItems", items);

        return "redirect:/goodsReceipt/create";
    }

    /**
     * Hiển thị form thêm mới sản phẩm
     */
    @GetMapping("/newProduct")
    public String getFormThemMoiSP(Model model, HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        model.addAttribute("product", new Products());
        return "themMoiSanPham";
    }

    /**
     * Lưu sản phẩm mới và chuyển tới chi tiết nhập hàng
     */
    @PostMapping("/saveProduct")
    public String luuSanPhamMoi(@ModelAttribute Products product,
                                 HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        if (product.getQuantity() == null) {
            product.setQuantity(0);
        }
        Products saved = productService.save(product);
        return "redirect:/goodsReceipt/itemDetail?id=" + saved.getId();
    }

    /**
     * Xác nhận nhập hàng - lưu phiếu nhập và chi tiết
     */
    @PostMapping("/confirm")
    public String createGoodsReceipt(@RequestParam(required = false) String receiveDate,
                                      @RequestParam(required = false) String note,
                                      HttpSession session, Model model) {
        Employees employee = (Employees) session.getAttribute("loggedInEmployee");
        if (employee == null) {
            return "redirect:/login";
        }

        GoodsReceipts receipt = (GoodsReceipts) session.getAttribute("currentReceipt");
        @SuppressWarnings("unchecked")
        List<GoodsReceiptsItem> items = (List<GoodsReceiptsItem>) session.getAttribute("currentItems");

        if (receipt == null || items == null || items.isEmpty()) {
            model.addAttribute("error", "Phiếu nhập chưa có sản phẩm!");
            model.addAttribute("receipt", receipt);
            model.addAttribute("items", items != null ? items : new ArrayList<>());
            return "lapPhieuNhapHang";
        }

        receipt.setReceiveDate(new Date());
        receipt.setNote(note);
        receipt.setEmployeeId(employee.getId());

        goodsReceiptService.createReceipt(receipt, items);

        // Xóa phiếu nhập khỏi session
        session.removeAttribute("currentReceipt");
        session.removeAttribute("currentItems");

        model.addAttribute("success", true);
        model.addAttribute("message", "Nhập hàng thành công!");
        return "lapPhieuNhapHang";
    }

    /**
     * Hủy phiếu nhập hiện tại
     */
    @GetMapping("/cancel")
    public String cancelReceipt(HttpSession session) {
        session.removeAttribute("currentReceipt");
        session.removeAttribute("currentItems");
        return "redirect:/trangChuNhanVienKho";
    }
}
