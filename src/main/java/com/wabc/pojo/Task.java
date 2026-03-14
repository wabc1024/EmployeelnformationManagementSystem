package com.wabc.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Task {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer createEmpId;
    private String content;
    private Date createDate;
}
