package com.han;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
        @Test
        public void testGenerateJwt(){
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("id",1);
            dataMap.put("username","admin");
            String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS256,"bGlueWFv")//签名算法
                    .addClaims(dataMap)//自定义信息
                    .setExpiration(new Date(System.currentTimeMillis()+3600*1000))//过期时间
                    .compact();// 生成令牌
            System.out.println(jwt);

    }
    /*解析jwt令牌,被篡改或者过期都会报错*/
    @Test
    public void testParseJwt(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJhZG1pbiIsImV4cCI6MTc3MTI4MzQ2Nn0.i2A7x6dlXtF_yQO4UIhzN8Jxk2Wt0-MfwOgoWXWUYKw";
        Claims claims = Jwts.parser().setSigningKey("bGlueWFv")
                .parseClaimsJws(token)
                .getBody();
        System.out.println(claims);
    }


}
