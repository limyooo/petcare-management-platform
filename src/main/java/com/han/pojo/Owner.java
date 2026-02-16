package com.han.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Owner {
    private Integer id;
    private String name;
    private String phone;
    private Integer gender;       // 1男 2女
    private String email;
    private String address;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
