package com.wabc.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Employees {
    @TableId(value = "emp_id",type = IdType.AUTO)
    private Integer empId;
    private String name;
    private double salary;
    private Integer departmentId;
    private Integer managerId;
}
