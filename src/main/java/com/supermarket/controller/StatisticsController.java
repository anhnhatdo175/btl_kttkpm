package com.supermarket.controller;

import com.supermarket.model.GoodsReceipts;
import com.supermarket.model.Suppliers;
import com.supermarket.model.SupplierStat;
import com.supermarket.service.StatisticsService;
import com.supermarket.service.SupplierService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private SupplierService supplierService;

    /**
     * Thống kê nhà cung cấp theo lượng hàng nhập trong khoảng thời gian
     */
    @GetMapping("/suppliers")
    public String thongKeNCC(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            Model model, HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }

        List<SupplierStat> stats = statisticsService.getSupplierStats(fromDate, toDate);
        model.addAttribute("stats", stats);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        return "thongKeNhaCungCap";
    }

    /**
     * Xem chi tiết phiếu nhập của nhà cung cấp trong khoảng thời gian
     */
    @GetMapping("/suppliers/detail")
    public String getChiTietNCC(
            @RequestParam Integer id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            Model model, HttpSession session) {
        if (session.getAttribute("loggedInEmployee") == null) {
            return "redirect:/login";
        }

        Suppliers supplier = supplierService.detailSupplier(id);
        List<GoodsReceipts> receipts = statisticsService.getReceiptsBySupplier(id, fromDate, toDate);

        model.addAttribute("supplier", supplier);
        model.addAttribute("receipts", receipts);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        return "chiTietNhaCungCap";
    }
}
