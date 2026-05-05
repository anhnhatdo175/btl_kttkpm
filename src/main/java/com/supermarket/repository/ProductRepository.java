package com.supermarket.repository;

import com.supermarket.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {

    /**
     * Tìm sản phẩm theo mã vạch (barcode)
     */
    List<Products> findByBarcodeContaining(String barcode);
}
