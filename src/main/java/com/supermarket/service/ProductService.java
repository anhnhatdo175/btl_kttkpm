package com.supermarket.service;

import com.supermarket.model.Products;
import com.supermarket.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Tìm sản phẩm theo barcode
     */
    public List<Products> findByBarcode(String barcode) {
        return productRepository.findByBarcodeContaining(barcode);
    }

    /**
     * Lưu sản phẩm (thêm mới hoặc cập nhật)
     */
    public Products save(Products product) {
        return productRepository.save(product);
    }

    /**
     * Tìm sản phẩm theo id
     */
    public Products findById(Integer id) {
        Optional<Products> opt = productRepository.findById(id);
        return opt.orElse(null);
    }

    /**
     * Cập nhật tồn kho: cộng thêm số lượng nhập
     */
    public void updateStock(Products product, Integer importQuantity) {
        if (product.getQuantity() == null) {
            product.setQuantity(0);
        }
        product.setQuantity(product.getQuantity() + importQuantity);
        productRepository.save(product);
    }
}
