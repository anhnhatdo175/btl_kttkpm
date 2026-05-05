package com.supermarket.repository;

import com.supermarket.model.GoodsReceipts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipts, Integer> {

    /**
     * Tìm phiếu nhập theo nhà cung cấp và khoảng thời gian
     */
    List<GoodsReceipts> findBySupplierIdAndReceiveDateBetween(
            Integer supplierId, Date fromDate, Date toDate);

    /**
     * Tìm tất cả phiếu nhập trong khoảng thời gian
     */
    List<GoodsReceipts> findByReceiveDateBetween(Date fromDate, Date toDate);

    /**
     * Thống kê tổng lượng hàng nhập theo nhà cung cấp trong khoảng thời gian
     */
    @Query("SELECT gr.supplierId, SUM(gri.quantity) FROM GoodsReceipts gr " +
           "JOIN GoodsReceiptsItem gri ON gr.id = gri.goodsReceiptId " +
           "WHERE gr.receiveDate BETWEEN :fromDate AND :toDate " +
           "GROUP BY gr.supplierId " +
           "ORDER BY SUM(gri.quantity) DESC")
    List<Object[]> sumQtyBySupplierInDateRange(
            @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
}
