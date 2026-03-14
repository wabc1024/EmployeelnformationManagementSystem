package com.wabc.service;

import com.wabc.pojo.Employees;
import com.wabc.pojo.vo.EmployeeInfo;

import java.util.List;

public interface EmployeeService {
    Employees queryEmpByEmpId(Integer empId);

    List<Employees> queryAllEmp();

    EmployeeInfo queryEmpInfoByEmpId(Integer empId);

    List<EmployeeInfo> queryAllEmpInfo();

    int updateEmp(EmployeeInfo employeeInfo);

    int deleteEmpByEmpId(String empId);

    int insertEmp(EmployeeInfo employeeInfo);
}
