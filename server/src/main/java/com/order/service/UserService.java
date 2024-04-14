package com.order.service;

import com.dto.UserLoginDTO;
import com.vo.UserLoginVO;

public interface UserService {
    UserLoginVO login(UserLoginDTO userLoginDTO);
}
