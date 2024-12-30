package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/29 16:56
 */
public interface UserService {
    //  Implement user login function
    User wxLogin(UserLoginDTO userLoginDTO);
}
