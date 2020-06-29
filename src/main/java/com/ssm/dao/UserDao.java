
package com.ssm.dao;
import com.ssm.model.User;
import javax.annotation.Resource;

@Resource
public interface UserDao {
    User selectByUsername(String username);

}