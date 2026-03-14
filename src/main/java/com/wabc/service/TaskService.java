package com.wabc.service;

import com.wabc.pojo.Task;

import java.util.List;

public interface TaskService {
    //根据员工id查看自己收到的任务
    public List<Task> queryTaskByEmpId(Integer empId);
    //查看所有任务
    public List<Task> queryAllTask();

    int addTask(Task task);

    int updateTask(Task task);

    int deleteTaskById(Integer id);
}
