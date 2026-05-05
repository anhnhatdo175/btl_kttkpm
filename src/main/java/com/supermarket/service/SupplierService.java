package com.supermarket.service;

import com.supermarket.model.Suppliers;
import com.supermarket.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    /**
     * Tìm nhà cung cấp theo tên (nếu name rỗng thì trả về tất cả)
     */
    public List<Suppliers> findSupplier(String name) {
        if (name == null || name.trim().isEmpty()) {
            return supplierRepository.findAll();
        }
        return supplierRepository.findByNameContaining(name);
    }

    /**
     * Lấy tất cả nhà cung cấp
     */
    public List<Suppliers> findAll() {
        return supplierRepository.findAll();
    }

    /**
     * Xem chi tiết nhà cung cấp theo id
     */
    public Suppliers detailSupplier(Integer id) {
        Optional<Suppliers> opt = supplierRepository.findById(id);
        return opt.orElse(null);
    }

    /**
     * Thêm nhà cung cấp mới
     */
    public Suppliers addSupplier(Suppliers supplier) {
        return supplierRepository.save(supplier);
    }

    /**
     * Cập nhật thông tin nhà cung cấp
     */
    public Suppliers updateSupplier(Suppliers supplier) {
        return supplierRepository.save(supplier);
    }
}
