package com.wabc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wabc.pojo.Departments;

public interface DepartmentMapper extends BaseMapper<Departments> {

    Integer queryDepartmentIdByName(String departmentName);
}
