package com.order.mapper;

import com.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;

@Mapper
public interface UserMapper {
    @Select("select * from user where openid = #{openid}")
    User getUsetByOpenid(String openid);

    void insertUser(User build);

    Integer countByMap(HashMap<Object, Object> map);
}
