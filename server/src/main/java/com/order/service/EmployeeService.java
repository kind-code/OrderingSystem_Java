package com.order.service;

import com.dto.EmployeeDTO;
import com.dto.EmployeeEditPasswordDTO;
import com.dto.EmployeeLoginDTO;
import com.dto.EmployeePageQueryDTO;
import com.entity.Employee;
import com.result.PageResult;
import com.vo.EmployeeLoginVO;

import java.io.UnsupportedEncodingException;

public interface EmployeeService {
    EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO) throws UnsupportedEncodingException;

    void editPassword(EmployeeEditPasswordDTO employeeEditPasswordDTO);

    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void startOrStop(Integer status, Integer id);

    void addEmployee(EmployeeDTO employeeDTO);

    Employee getEmployeeById(Integer id);

    void updateEmployee(EmployeeDTO employeeDTO);
}
