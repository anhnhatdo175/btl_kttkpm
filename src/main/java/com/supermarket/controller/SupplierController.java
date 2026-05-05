package com.supermarket.controller;

import com.supermarket.model.Suppliers;
import com.supermarket.service.SupplierService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * Hiển thị danh sách nhà cung cấp (tìm kiếm theo tên nếu có)
     * GET /supplier/all hoặc /supplier/all?name=...
     */
    @GetMapping("/all")
    public String getDanhSachNCC(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) String mode,
                                  Model model, HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        List<Suppliers> suppliers = supplierService.findSupplier(name);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("searchName", name);
        model.addAttribute("mode", mode != null ? mode : "search");
        return "danhSachNhaCungCap";
    }

    /**
     * Xem chi tiết nhà cung cấp
     * GET /supplier/{id}
     */
    @GetMapping("/{id}")
    public String detailSupplier(@PathVariable Integer id, Model model, HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        Suppliers supplier = supplierService.detailSupplier(id);
        model.addAttribute("supplier", supplier);
        model.addAttribute("mode", "detail");
        return "thongTinNhaCungCap";
    }

    /**
     * Hiển thị form thêm nhà cung cấp
     * GET /supplier/add
     */
    @GetMapping("/add")
    public String getFormThemNCC(Model model, HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        model.addAttribute("supplier", new Suppliers());
        model.addAttribute("mode", "add");
        return "thongTinNhaCungCap";
    }

    /**
     * Xử lý thêm nhà cung cấp
     * POST /supplier/add
     */
    @PostMapping("/add")
    public String addSupplier(@ModelAttribute Suppliers supplier, Model model, HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        Suppliers saved = supplierService.addSupplier(supplier);
        model.addAttribute("supplier", saved);
        model.addAttribute("mode", "add_success");
        model.addAttribute("message", "Thêm thành công!");
        return "thongTinNhaCungCap";
    }

    /**
     * Hiển thị form chỉnh sửa nhà cung cấp
     * GET /supplier/edit/{id}
     */
    @GetMapping("/edit/{id}")
    public String getFormChinhSuaNCC(@PathVariable Integer id, Model model, HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        Suppliers supplier = supplierService.detailSupplier(id);
        model.addAttribute("supplier", supplier);
        model.addAttribute("mode", "edit");
        return "thongTinNhaCungCap";
    }

    /**
     * Xử lý cập nhật nhà cung cấp
     * POST /supplier/edit/{id}
     */
    @PostMapping("/edit/{id}")
    public String updateSupplier(@PathVariable Integer id,
                                  @ModelAttribute Suppliers supplier,
                                  Model model, HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        supplier.setId(id);
        Suppliers updated = supplierService.updateSupplier(supplier);
        model.addAttribute("supplier", updated);
        model.addAttribute("mode", "edit_success");
        model.addAttribute("message", "Cập nhật thành công!");
        return "thongTinNhaCungCap";
    }
}
