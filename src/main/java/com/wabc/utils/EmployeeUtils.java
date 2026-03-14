package com.wabc.utils;

import com.wabc.mapper.DepartmentMapper;
import com.wabc.mapper.EmployeeMapper;
import com.wabc.pojo.Employees;
import com.wabc.pojo.vo.EmployeeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeUtils {
    @Autowired
    private  EmployeeMapper employeeMapper;
    @Autowired
    private  DepartmentMapper departmentMapper;

    public  Employees empInfoToEmp(EmployeeInfo employeeInfo){
        Employees employees = new Employees();
        employees.setEmpId(employeeInfo.getEmpId());
        employees.setName(employeeInfo.getName());
        employees.setSalary(employeeInfo.getSalary());
        employees.setDepartmentId(departmentMapper.queryDepartmentIdByName(employeeInfo.getDepartmentName()));
        employees.setManagerId(employeeMapper.queryIdByName(employeeInfo.getManagerName()));
        return employees;
    }

    public  EmployeeInfo empToEmpInfo(Employees employees){
        EmployeeInfo employeeInfo = new EmployeeInfo();
        employeeInfo.setEmpId(employees.getEmpId());
        employeeInfo.setName(employees.getName());
        employeeInfo.setSalary(employees.getSalary());
        employeeInfo.setDepartmentName(departmentMapper.selectById(employees.getEmpId()).getDepartmentName());
        employeeInfo.setManagerName(employeeMapper.selectById(employees.getManagerId()).getName());
        return employeeInfo;
    }
}
