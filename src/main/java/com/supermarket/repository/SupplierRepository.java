package com.supermarket.repository;

import com.supermarket.model.Suppliers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Suppliers, Integer> {

    /**
     * Tìm nhà cung cấp theo tên chứa từ khóa
     */
    List<Suppliers> findByNameContaining(String name);
}
