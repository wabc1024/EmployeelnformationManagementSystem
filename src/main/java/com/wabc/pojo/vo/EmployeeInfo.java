package com.wabc.pojo.vo;

import lombok.Data;

@Data
public class EmployeeInfo {
    private Integer empId;
    private String name;
    private double salary;
    private String departmentName;
    private String managerName;
}
