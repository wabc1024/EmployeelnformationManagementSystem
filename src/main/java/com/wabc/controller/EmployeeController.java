package com.wabc.controller;

import com.wabc.pojo.Employees;
import com.wabc.pojo.vo.EmployeeInfo;
import com.wabc.service.EmployeeService;
import com.wabc.utils.Result;
import com.wabc.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    //获取员工信息
    @GetMapping("emp/{empId}")
    public Result selectEmpDetails(@PathVariable Integer empId){
        EmployeeInfo employeeInfo = employeeService.queryEmpInfoByEmpId(empId);
        if(employeeInfo!=null){
            return Result.ok(employeeInfo);
        }else {
            return Result.build(ResultCodeEnum.UNDEFINED_ERROR,null);
        }
    }
    //获取所有员工信息
    @GetMapping("admin/emp")
    public Result selectAllEmp(){
        List<EmployeeInfo> employeeInfoList = employeeService.queryAllEmpInfo();
        Map<String,List<EmployeeInfo>> map = new HashMap<>();
        map.put("emps",employeeInfoList);
        return Result.ok(map);
    }

    //修改员工信息
    @PutMapping("admin/emp")
    public Result updateEmp(@RequestBody EmployeeInfo employeeInfo){
        int rows = employeeService.updateEmp(employeeInfo);
        if(rows>0){
            return Result.ok(null);
        }else {
            return Result.build(ResultCodeEnum.UNDEFINED_ERROR,null);
        }
    }

    //删除员工
    @DeleteMapping("admin/emp/{empId}")
    public Result deleteEmp(@PathVariable String empId){
        int rows = employeeService.deleteEmpByEmpId(empId);
        if(rows>0){
            return Result.ok(null);
        }else {
            return Result.build(ResultCodeEnum.UNDEFINED_ERROR,null);
        }
    }

    //新增员工
    @PostMapping("admin/emp")
    public Result addEmp(@RequestBody EmployeeInfo employeeInfo){
        int rows = employeeService.insertEmp(employeeInfo);
        if(rows>0){
            return Result.ok(null);
        }else {
            return Result.build(ResultCodeEnum.UNDEFINED_ERROR,null);
        }
    }
}
