package com.supermarket.model;

import jakarta.persistence.*;

@Entity
@Table(name = "GoodsReceiptsItem")
public class GoodsReceiptsItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double priceImport;

    private Integer quantity;

    @Column(name = "ProductId")
    private Integer ProductId;

    private Integer goodsReceiptId;

    // Transient field cho hiển thị
    @Transient
    private String productName;

    @Transient
    private String productBarcode;

    public GoodsReceiptsItem() {}

    public GoodsReceiptsItem(Double priceImport, Integer quantity, Integer productId, Integer goodsReceiptId) {
        this.priceImport = priceImport;
        this.quantity = quantity;
        this.ProductId = productId;
        this.goodsReceiptId = goodsReceiptId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Double getPriceImport() { return priceImport; }
    public void setPriceImport(Double priceImport) { this.priceImport = priceImport; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getProductId() { return ProductId; }
    public void setProductId(Integer productId) { this.ProductId = productId; }

    public Integer getGoodsReceiptId() { return goodsReceiptId; }
    public void setGoodsReceiptId(Integer goodsReceiptId) { this.goodsReceiptId = goodsReceiptId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductBarcode() { return productBarcode; }
    public void setProductBarcode(String productBarcode) { this.productBarcode = productBarcode; }
}
