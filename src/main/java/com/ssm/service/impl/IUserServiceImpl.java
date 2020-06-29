package com.ssm.service.impl;
import com.ssm.dao.UserDao;
import com.ssm.model.User;
import com.ssm.service.IUserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("userService")
public class IUserServiceImpl implements IUserService {
    private static Logger logger = Logger.getLogger(IUserServiceImpl.class);
    @Resource
    private UserDao userDao;

    public User getUserByUsername(String username) {
        return this.userDao.selectByUsername(username);
    }
}