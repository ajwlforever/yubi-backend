package com.yupi.springbootinit.config.dto.Xfun;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * 消息对象
 *
 */
@Data
public class MsgDTO {
    /**
     * 角色
     */
    private String role;
    /**
     * 消息内容
     */
    private String content;
    private Integer index;

    @Getter
    public static enum Role {
        SYSTEM("system"),
        USER("user"),
        ASSISTANT("assistant");

        private String name;

        private Role(String name) {
            this.name = name;
        }
    }

}