package com.yupi.springbootinit.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账
号
     */
    private String userAccount;

    /**
     * 密
码
     */
    private String userPassword;

    /**
     * 用户昵
称
     */
    private String userName;

    /**
     * 用户头
像
     */
    private String userAvatar;

    /**
     * 用
户角色：user/admin
     */
    private String userRole;

    /**
     * 创
建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是
否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}