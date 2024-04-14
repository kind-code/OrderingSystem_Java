package com.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.constant.JwtClaimsConstant;
import com.constant.MessageConstant;
import com.dto.UserLoginDTO;
import com.entity.User;
import com.exception.LoginFailedException;
import com.exception.UserNotLoginException;
import com.order.mapper.UserMapper;
import com.order.service.UserService;
import com.properties.JwtProperties;
import com.properties.WeChatProperties;
import com.utils.HttpClientUtil;
import com.utils.JwtUtil;
import com.vo.UserLoginVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private JwtProperties jwtProperties;
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        /**
         * 获取openid
         */
        String code = userLoginDTO.getCode();
        Map<String,String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_code","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN,map);
        /**
         * 将json格式进行转换  获取openid
         */
        JSONObject jsonObject = JSONObject.parseObject(json);
        String openid = jsonObject.getString("openid");


        if(openid == null)
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);

        User user = userMapper.getUsetByOpenid(openid);
        if(user==null) {
            User build = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
            userMapper.insertUser(build);
        }
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user,userLoginVO);

        //获取jwt令牌
        HashMap<String,Object> cliams = new HashMap();
        cliams.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), cliams);
        userLoginVO.setToken(token);
        return userLoginVO;
    }
}
