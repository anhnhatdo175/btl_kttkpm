package com.supermarket.controller;

import com.supermarket.model.Employees;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DieuHuongController {

    /**
     * Trang chủ nhân viên kho
     */
    @GetMapping("/trangChuNhanVienKho")
    public String getTrangChuNhanVienKho(HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        return "trangChuNhanVienKho";
    }

    /**
     * Trang tùy chọn quản lý nhà cung cấp
     */
    @GetMapping("/quanLyNhaCungCap")
    public String getQuanLyNhaCungCap(HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        return "quanLyNhaCungCap";
    }

    /**
     * Trang chủ quản lý
     */
    @GetMapping("/trangChuQuanLy")
    public String getTrangChuQuanLy(HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        return "trangChuQuanLy";
    }

    /**
     * Trang tùy chọn báo cáo thống kê
     */
    @GetMapping("/tuyChonBaoCao")
    public String getTuyChonBaoCao(HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        return "tuyChonBaoCao";
    }

    /**
     * Trang thống kê nhà cung cấp (ban đầu bảng trống)
     */
    @GetMapping("/thongKeNCC")
    public String getPageThongKeNCC(HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }
        return "thongKeNhaCungCap";
    }
}
