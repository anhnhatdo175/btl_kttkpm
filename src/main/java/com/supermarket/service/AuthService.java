package com.supermarket.service;

import com.supermarket.model.Employees;
import com.supermarket.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Đăng nhập: tìm nhân viên theo userName và kiểm tra passWord
     * @return đối tượng Employees nếu đăng nhập thành công, null nếu thất bại
     */
    public Employees dangNhap(String userName, String passWord) {
        Employees employee = employeeRepository.findByUserName(userName);
        if (employee != null && employee.getPassWord().equals(passWord)) {
            return employee;
        }
        return null;
    }

    /**
     * Kiểm tra quyền thực hiện nghiệp vụ
     * @return true nếu nhân viên có role phù hợp
     */
    public boolean checkRole(Employees employee, String requiredRole) {
        if (employee == null) return false;
        return employee.getRole().equalsIgnoreCase(requiredRole);
    }
}
