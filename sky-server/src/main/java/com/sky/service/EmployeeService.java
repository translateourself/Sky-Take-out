package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /*
     * function create a new employee
     *
     * @date 2024/12/22 21:41
     */
    public void save (EmployeeDTO employeeDTO);

    /*
     * function page query
     *
     * @date 2024/12/23 21:01
     * @param employeePageQueryDTO
     * @return com.sky.result.PageResult
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /*
     * function stop or start employee account.
     *
     * @date 2024/12/23 21:01
     * @param id
     * @param status
     */
    void startOrStop(Long id, Integer status);

    Employee getEmployeeById(Long id);

    void updateInfo(EmployeeDTO employeeDTO);
}
