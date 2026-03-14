package com.wabc.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    @TableId(value = "user_id",type = IdType.AUTO)
    private Integer UserId;
    private Integer empId;
    private String userName;
    private String userPwd;
    private Date createDate;
    private String userType;
    private Boolean isDeleted;

}
