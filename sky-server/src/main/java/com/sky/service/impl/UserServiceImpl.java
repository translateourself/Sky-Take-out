package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuxunne
 * @description:
 * @since 2024/12/29 16:58
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;


    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //  call the wxInterface service to get current wxUser openId
        String openid = getOpenId(userLoginDTO.getCode());


        // judge current user whether is empty
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //  judge current user if  new user
        User user = userMapper.getUserByOpenId(openid);

        //  current is new user, automatic register account
        if (user == null) {
             user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
             userMapper.insert(user);
        }
        //  return the user object

        return  user;
    }
    /*
     * function:  call the wxInterface service to get current wxUser openId
     *
     * @date 2024/12/29 18:01
     * @param authenticateCode
     * @return java.lang.String
     */
    private String getOpenId(String authenticateCode){
        Map<String, String> parameter = new HashMap<>();
        parameter.put("appid",weChatProperties.getAppid());
        parameter.put("secret",weChatProperties.getSecret());
        parameter.put("js_code",authenticateCode);
        parameter.put("grant_type","authorization_code");
        String responseJson = HttpClientUtil.doGet(WX_LOGIN, parameter);

        JSONObject jsonObject = JSON.parseObject(responseJson);
        return jsonObject.getString("openid");
    }
}
