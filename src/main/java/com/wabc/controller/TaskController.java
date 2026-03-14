package com.wabc.controller;

import com.wabc.pojo.Task;
import com.wabc.service.TaskService;
import com.wabc.utils.Result;
import com.wabc.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class TaskController {
    @Autowired
    private TaskService taskService;

    //员工查看自己的任务
    @GetMapping("emp/{empId}/task")
    public Result queryTask(@PathVariable Integer empId){
        List<Task> tasks = taskService.queryTaskByEmpId(empId);
        Map<String,List<Task>> map = new HashMap<>();
        map.put("task",tasks);
        return Result.ok(map);
    }

    //管理员查看所有任务
    @GetMapping("admin/task")
    public Result queryAllTask(){
        List<Task> tasks = taskService.queryAllTask();
        Map<String,List<Task>> map = new HashMap<>();
        map.put("task",tasks);
        return Result.ok(map);
    }

    //新增任务
    @PostMapping({"admin/task","emp/{empId}/task"})
    public Result addTask(@RequestBody Task task){
        int rows = taskService.addTask(task);
        if(rows>0){
            return Result.ok(null);
        }else {
            return Result.build(ResultCodeEnum.UNDEFINED_ERROR,null);
        }
    }

    //修改任务
    @PutMapping({"admin/task","task/{id}"})
    public Result updateTask(@RequestBody Task task){
        int rows = taskService.updateTask(task);
        if(rows>0){
            return Result.ok(null);
        }else {
            return Result.build(ResultCodeEnum.UNDEFINED_ERROR,null);
        }
    }

    //删除任务
    @DeleteMapping({"admin/task/{id}","task/{id}"})
    public Result deleteTask(@PathVariable Integer id){
        int rows = taskService.deleteTaskById(id);
        if(rows>0){
            return Result.ok(null);
        }else {
            return Result.build(ResultCodeEnum.UNDEFINED_ERROR,null);
        }
    }
}
