package com.wabc.service.Impl;

import com.wabc.mapper.EmployeeMapper;
import com.wabc.pojo.Employees;
import com.wabc.pojo.vo.EmployeeInfo;
import com.wabc.service.EmployeeService;
import com.wabc.utils.EmployeeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeUtils employeeUtils;

    @Override
    public Employees queryEmpByEmpId(Integer empId) {
        return employeeMapper.selectById(empId);
    }

    @Override
    public List<Employees> queryAllEmp() {
        return employeeMapper.selectList(null);
    }

    @Override
    public EmployeeInfo queryEmpInfoByEmpId(Integer empId) {
        return employeeUtils.empToEmpInfo(employeeMapper.selectById(empId));
    }

    @Override
    public List<EmployeeInfo> queryAllEmpInfo() {
        return employeeMapper.queryAllEmpInfo();
    }

    @Override
    public int updateEmp(EmployeeInfo employeeInfo) {
        return employeeMapper.updateById(employeeUtils.empInfoToEmp(employeeInfo));
    }

    @Override
    public int deleteEmpByEmpId(String empId) {
        return employeeMapper.deleteById(empId);
    }

    @Override
    public int insertEmp(EmployeeInfo employeeInfo) {
        return employeeMapper.insert(employeeUtils.empInfoToEmp(employeeInfo));
    }
}
