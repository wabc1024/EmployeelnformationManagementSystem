package com.wabc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wabc.pojo.Employees;
import com.wabc.pojo.vo.EmployeeInfo;

import java.util.List;

public interface EmployeeMapper extends BaseMapper<Employees> {



    List<EmployeeInfo> queryAllEmpInfo();

    Integer queryIdByName(String empName);
}
