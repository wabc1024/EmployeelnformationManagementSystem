package com.wabc.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wabc.mapper.TaskMapper;
import com.wabc.pojo.Employees;
import com.wabc.pojo.Task;
import com.wabc.service.EmployeeService;
import com.wabc.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private EmployeeService employeeService;
    @Override
    public List<Task> queryTaskByEmpId(Integer empId) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        Employees employee = employeeService.queryEmpByEmpId(empId);
        queryWrapper.eq("create_emp_id",employee.getManagerId()).or().eq("create_emp_id",employee.getEmpId());
        return taskMapper.selectList(queryWrapper);
    }

    @Override
    public List<Task> queryAllTask() {
        return taskMapper.selectList(null);
    }

    @Override
    public int addTask(Task task) {
        task.setCreateDate(new Date());
        return taskMapper.insert(task);
    }

    @Override
    public int updateTask(Task task) {
        return taskMapper.updateById(task);
    }

    @Override
    public int deleteTaskById(Integer id) {
        return taskMapper.deleteById(id);
    }

}
