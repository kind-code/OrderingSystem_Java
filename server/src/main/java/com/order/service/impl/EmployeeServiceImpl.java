package com.order.service.impl;

import com.constant.JwtClaimsConstant;
import com.constant.MessageConstant;
import com.constant.PasswordConstant;
import com.constant.StatusConstant;
import com.context.BaseContext;
import com.dto.EmployeeDTO;
import com.dto.EmployeeEditPasswordDTO;
import com.dto.EmployeeLoginDTO;
import com.dto.EmployeePageQueryDTO;
import com.entity.Employee;
import com.exception.BaseException;
import com.exception.LoginFailedException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.order.mapper.EmployeeMapper;
import com.order.service.EmployeeService;
import com.properties.JwtProperties;
import com.result.PageResult;
import com.utils.JwtUtil;
import com.vo.EmployeeLoginVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private JwtProperties jwtProperties;
    @Override
    public void editPassword(EmployeeEditPasswordDTO employeeEditPasswordDTO) {
        long id = BaseContext.getCurrentId();
        BaseContext.removeCurrentId();
        Employee employee = employeeMapper.selectUserById(id);

        String oldPassword = employeeEditPasswordDTO.getOldPassword();
        String newPassword = employeeEditPasswordDTO.getNewPassword();

        try {
            employeeEditPasswordDTO.setEmpId(id);
            employeeEditPasswordDTO.setOldPassword( DigestUtils.md5DigestAsHex(oldPassword.getBytes("utf-8")));
            employeeEditPasswordDTO.setNewPassword( DigestUtils.md5DigestAsHex(newPassword.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        if(employee!=null && employee.getPassword().equals(employeeEditPasswordDTO.getOldPassword())){

            employeeMapper.updatePassword(employeeEditPasswordDTO);
        } else {
            throw new BaseException(MessageConstant.PASSWORD_EDIT_FAILED);
        }
    }

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        Page<Employee> page= employeeMapper.pageQuery(employeePageQueryDTO);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getResult());


        return pageResult;
    }

    @Override
    public void startOrStop(Integer status, Integer id) {
        Long adminId = BaseContext.getCurrentId();
        BaseContext.removeCurrentId();
        Employee employee = employeeMapper.selectUserById(adminId);
        if(employee.getUsername().equals("admin")){
//                status = status>0?new Integer(0):new Integer(1);
                employeeMapper.startOrStop(status,id);
        }else {
            throw new BaseException("非管理员无法禁用账号");
        }
    }

    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //使用属性拷贝的方法
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setStatus(StatusConstant.ENABLE);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employeeMapper.insertEmployee(employee);
    }

    @Override
    public Employee getEmployeeById(Integer id) {
        Employee employee = employeeMapper.selectUserById(id);
        return employee;
    }

    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employeeMapper.updateEmployee(employee);
    }

    @Override
    public EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO) throws UnsupportedEncodingException {
        String password = employeeLoginDTO.getPassword();
        String md5_password = DigestUtils.md5DigestAsHex(password.getBytes("utf-8"));
        employeeLoginDTO.setPassword(md5_password);
        Employee employee = employeeMapper.login(employeeLoginDTO);
        if(employee!=null){
            //登录成功
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
            String token = JwtUtil.createJWT(
                    jwtProperties.getAdminSecretKey(),
                    jwtProperties.getAdminTtl(),
                    claims
            );
            EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                    .token(token)
                    .id(employee.getId())
                    .name(employee.getName())
                    .userName(employee.getUsername())
                    .build();
            return employeeLoginVO;
        }else{
           throw new LoginFailedException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
    }
}
