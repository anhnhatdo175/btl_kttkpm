package com.supermarket.controller;

import com.supermarket.model.Employees;
import com.supermarket.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Hiển thị trang đăng nhập
     */
    @GetMapping("/login")
    public String getDangNhap() {
        return "dangNhap";
    }

    /**
     * Xử lý đăng nhập
     */
    @PostMapping("/login")
    public String postDangNhap(@RequestParam String userName,
                               @RequestParam String passWord,
                               HttpSession session,
                               Model model) {
        Employees employee = authService.dangNhap(userName, passWord);
        if (employee != null) {
            session.setAttribute("loggedInEmployee", employee);
            // Kiểm tra role để điều hướng
            if (authService.checkRole(employee, "QUANLY")) {
                return "redirect:/trangChuQuanLy";
            } else {
                return "redirect:/trangChuNhanVienKho";
            }
        }
        model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
        return "dangNhap";
    }

    /**
     * Đăng xuất
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    /**
     * Trang mặc định chuyển về login
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
}
