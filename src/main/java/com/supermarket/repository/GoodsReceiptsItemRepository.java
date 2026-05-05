package com.supermarket.repository;

import com.supermarket.model.GoodsReceiptsItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsReceiptsItemRepository extends JpaRepository<GoodsReceiptsItem, Integer> {

    /**
     * Tìm tất cả chi tiết của một phiếu nhập
     */
    List<GoodsReceiptsItem> findByGoodsReceiptId(Integer goodsReceiptId);

    /**
     * Tính tổng số lượng hàng nhập theo phiếu nhập
     */
    @Query("SELECT COALESCE(SUM(gri.quantity), 0) FROM GoodsReceiptsItem gri WHERE gri.goodsReceiptId = :receiptId")
    Long sumQuantityByReceiptId(@Param("receiptId") Integer receiptId);
}
