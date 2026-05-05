package com.supermarket.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "GoodsReceipts")
public class GoodsReceipts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date receiveDate;

    private String note;

    private Integer employeeId;

    private Integer supplierId;

    // Transient fields cho hiển thị
    @Transient
    private String employeeName;

    @Transient
    private String supplierName;

    @Transient
    private Integer totalQuantity;

    public GoodsReceipts() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Date getReceiveDate() { return receiveDate; }
    public void setReceiveDate(Date receiveDate) { this.receiveDate = receiveDate; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public Integer getSupplierId() { return supplierId; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public Integer getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Integer totalQuantity) { this.totalQuantity = totalQuantity; }
}
