package com.wabc.controller;

import com.wabc.pojo.User;
import com.wabc.pojo.vo.UserUpdate;
import com.wabc.service.UserService;
import com.wabc.utils.Result;
import com.wabc.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("login")
    public Result login(@RequestBody User user){
        //System.out.println(user);
        User rightUser = userService.queryUserByUserName(user.getUserName());
        if(rightUser==null){
            return Result.build(ResultCodeEnum.USERNAME_NOT_EXIST,null);
        }else if(!user.getUserPwd().equals(rightUser.getUserPwd())){
            return Result.build(ResultCodeEnum.PASSWORD_ERROR,null);
        }else {
            rightUser.setUserPwd(null);
            return Result.ok(rightUser);
        }
    }

    @PutMapping("regist")
    public Result regist(@RequestBody User user){
        User findUser = userService.queryUserByUserName(user.getUserName());
        if(findUser!=null){
            return Result.build(ResultCodeEnum.USERNAME_USED,null);
        }else {
            user.setCreateDate(new Date());
            user.setUserType("emp");
            int rows = userService.addUser(user);
            return Result.ok(null);
        }
    }
    //修改密码
    @PutMapping("user")
    public Result updatePassword(@RequestBody UserUpdate userUpdate){
        User rightUser = userService.queryUserByUserName(userUpdate.getUserName());
        if(!userUpdate.getUserPwd().equals(rightUser.getUserPwd())){
            return Result.build(ResultCodeEnum.PASSWORD_ERROR,null);
        }else {
            rightUser.setUserPwd(userUpdate.getNewUserPwd());
            int rows = userService.updateUser(rightUser);
            return Result.ok(null);
        }
    }

    //删除账号
    @DeleteMapping("user/{userId}")
    public Result deleteUser(@PathVariable Integer userId){
        int rows=userService.deleteUserById(userId);
        if(rows>0){
            return Result.ok(null);
        }else {
            return Result.build(ResultCodeEnum.UNDEFINED_ERROR,null);
        }
    }

}
