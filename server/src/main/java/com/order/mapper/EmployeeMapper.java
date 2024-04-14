package com.order.mapper;

import com.dto.EmployeeEditPasswordDTO;
import com.dto.EmployeeLoginDTO;
import com.dto.EmployeePageQueryDTO;
import com.entity.Employee;
import com.enumeration.OperationType;
import com.github.pagehelper.Page;
import com.order.annotation.AutoFill;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {
    @Select("select * from employee where username=#{username} and password=#{password}")
    Employee login(EmployeeLoginDTO employeeLoginDTO);
    @Select("select * from employee where id=#{id}")
    Employee selectUserById(long id);
    @Update("update employee set password=#{newPassword} where id=#{empId}")
    void updatePassword(EmployeeEditPasswordDTO employeeEditPasswordDTO);

    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
    @AutoFill(OperationType.UPDATE)
    @Update("update employee set status=#{status} where id = #{id}")
    void startOrStop(Integer status, Integer id);

//    @Insert("insert into employee(name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user) VALUES " +
//            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser})")
//    @AutoFill(OperationType.INSERT)
//    void insertEmployee(Employee employee);
@Insert("insert into employee(name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user,status)" +
        " values(#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser}, #{status})")
@AutoFill(value = OperationType.INSERT)
void insertEmployee(Employee employee);
    @AutoFill(OperationType.UPDATE)
    @Update("update employee set id_number=#{idNumber},name=#{name},phone=#{phone},sex=#{sex},username=#{username} where id = #{id}")
    void updateEmployee(Employee employee);
}
