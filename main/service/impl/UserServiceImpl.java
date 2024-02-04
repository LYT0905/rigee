package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapper.UserMapper;
import com.pojo.User;
import com.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Mark
 * @date 2024/2/4
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
