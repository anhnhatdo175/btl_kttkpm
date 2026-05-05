package com.supermarket.service;

import com.supermarket.model.*;
import com.supermarket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatisticsService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private GoodsReceiptRepository goodsReceiptRepository;

    @Autowired
    private GoodsReceiptsItemRepository goodsReceiptsItemRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Thống kê nhà cung cấp theo tổng lượng hàng nhập,
     * sắp xếp từ cao xuống thấp
     */
    public List<SupplierStat> getSupplierStats(Date fromDate, Date toDate) {
        List<Object[]> results = goodsReceiptRepository.sumQtyBySupplierInDateRange(fromDate, toDate);
        List<SupplierStat> stats = new ArrayList<>();

        for (Object[] row : results) {
            Integer supplierId = (Integer) row[0];
            Long totalQty = (Long) row[1];

            Suppliers supplier = supplierRepository.findById(supplierId).orElse(null);
            if (supplier != null) {
                stats.add(new SupplierStat(supplier.getId(), supplier.getName(), totalQty));
            }
        }

        return stats;
    }

    /**
     * Lấy danh sách phiếu nhập hàng của nhà cung cấp trong khoảng thời gian
     */
    public List<GoodsReceipts> getReceiptsBySupplier(Integer supplierId, Date fromDate, Date toDate) {
        List<GoodsReceipts> receipts = goodsReceiptRepository
                .findBySupplierIdAndReceiveDateBetween(supplierId, fromDate, toDate);

        // Bổ sung thông tin hiển thị
        for (GoodsReceipts receipt : receipts) {
            // Tên người lập phiếu
            Employees emp = employeeRepository.findById(receipt.getEmployeeId()).orElse(null);
            if (emp != null) {
                receipt.setEmployeeName(emp.getName());
            }

            // Tổng số lượng hàng trong phiếu
            Long totalQty = goodsReceiptsItemRepository.sumQuantityByReceiptId(receipt.getId());
            receipt.setTotalQuantity(totalQty != null ? totalQty.intValue() : 0);
        }

        return receipts;
    }

    /**
     * Tính tổng số lượng hàng nhập của một nhà cung cấp
     */
    public Integer sumQtyBySupplierId(Integer supplierId, Date fromDate, Date toDate) {
        List<GoodsReceipts> receipts = goodsReceiptRepository
                .findBySupplierIdAndReceiveDateBetween(supplierId, fromDate, toDate);

        int total = 0;
        for (GoodsReceipts receipt : receipts) {
            Long qty = goodsReceiptsItemRepository.sumQuantityByReceiptId(receipt.getId());
            if (qty != null) total += qty.intValue();
        }
        return total;
    }
}
