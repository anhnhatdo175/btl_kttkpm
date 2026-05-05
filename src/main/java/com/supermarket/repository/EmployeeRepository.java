package com.supermarket.repository;

import com.supermarket.model.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employees, Integer> {

    /**
     * Tìm nhân viên theo tên đăng nhập
     */
    Employees findByUserName(String userName);
}
