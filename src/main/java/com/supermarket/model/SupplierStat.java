package com.supermarket.model;

/**
 * DTO kế thừa từ Suppliers, thêm thuộc tính totalImportQuantity
 * để phục vụ cho thống kê nhà cung cấp theo lượng hàng nhập.
 */
public class SupplierStat {

    private Integer id;
    private String name;
    private Long totalImportQuantity;

    public SupplierStat() {}

    public SupplierStat(Integer id, String name, Long totalImportQuantity) {
        this.id = id;
        this.name = name;
        this.totalImportQuantity = totalImportQuantity;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getTotalImportQuantity() { return totalImportQuantity; }
    public void setTotalImportQuantity(Long totalImportQuantity) { this.totalImportQuantity = totalImportQuantity; }
}
