package com.order.controller.admin;


import com.dto.EmployeeDTO;
import com.dto.EmployeeEditPasswordDTO;
import com.dto.EmployeeLoginDTO;
import com.dto.EmployeePageQueryDTO;
import com.entity.Employee;
import com.order.service.EmployeeService;

import com.result.PageResult;
import com.result.Result;
import com.vo.EmployeeLoginVO;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@Slf4j
@RequestMapping("/admin/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/login")
    public Result<EmployeeLoginVO> lgoin(@RequestBody EmployeeLoginDTO employeeLoginDTO) throws UnsupportedEncodingException {

        EmployeeLoginVO employeeLoginVO = employeeService.login(employeeLoginDTO);
        return Result.success(employeeLoginVO);
    }

    @PutMapping("/editPassword")
    public Result editPassword(@RequestBody EmployeeEditPasswordDTO employeeEditPasswordDTO){
        employeeService.editPassword(employeeEditPasswordDTO);
        return Result.success();
    }
    @GetMapping("/page")
    public Result<PageResult> pageQuery(EmployeePageQueryDTO employeePageQueryDTO){
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable("status") Integer status, Integer id){
        employeeService.startOrStop(status,id);
        return  Result.success();
    }
    @PostMapping("/logout")
    public Result logout(){
        return Result.success();
    }


    @PostMapping()
    public Result insert(@RequestBody EmployeeDTO employeeDTO){
        log.info("员工信息为{}",employeeDTO.toString());
        employeeService.addEmployee(employeeDTO);
        return  Result.success();
    }

    @GetMapping("/{id}")
    public Result<Employee> getEmployeeById(@PathVariable("id")Integer id){
        Employee employee = employeeService.getEmployeeById(id);
        return Result.success(employee);
    }

    @PutMapping()
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO){
        log.info("修改员工信息为{}",employeeDTO.toString());
        employeeService.updateEmployee(employeeDTO);
        return Result.success();
    }

}
