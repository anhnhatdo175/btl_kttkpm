package com.supermarket.service;

import com.supermarket.model.*;
import com.supermarket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsReceiptService {

    @Autowired
    private GoodsReceiptRepository goodsReceiptRepository;

    @Autowired
    private GoodsReceiptsItemRepository goodsReceiptsItemRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Tìm tất cả nhà cung cấp
     */
    public List<Suppliers> findSupplier() {
        return supplierRepository.findAll();
    }

    /**
     * Tìm nhà cung cấp theo tên
     */
    public List<Suppliers> findSupplierByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return supplierRepository.findAll();
        }
        return supplierRepository.findByNameContaining(name);
    }

    /**
     * Tìm sản phẩm theo barcode
     */
    public List<Products> searchProduct(String barcode) {
        return productRepository.findByBarcodeContaining(barcode);
    }

    /**
     * Thêm chi tiết sản phẩm vào phiếu nhập (lưu vào DB)
     */
    public GoodsReceiptsItem addItemToReceipt(GoodsReceiptsItem item) {
        return goodsReceiptsItemRepository.save(item);
    }

    /**
     * Tạo phiếu nhập hàng và lưu tất cả chi tiết
     */
    @Transactional
    public GoodsReceipts createReceipt(GoodsReceipts receipt, List<GoodsReceiptsItem> items) {
        // Lưu phiếu nhập
        GoodsReceipts savedReceipt = goodsReceiptRepository.save(receipt);

        // Lưu từng chi tiết sản phẩm trong phiếu
        for (GoodsReceiptsItem item : items) {
            item.setGoodsReceiptId(savedReceipt.getId());
            goodsReceiptsItemRepository.save(item);

            // Cập nhật tồn kho sản phẩm
            Products product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                if (product.getQuantity() == null) product.setQuantity(0);
                product.setQuantity(product.getQuantity() + item.getQuantity());
                productRepository.save(product);
            }
        }

        return savedReceipt;
    }
}
