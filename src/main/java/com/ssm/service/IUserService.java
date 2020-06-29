package com.ssm.service;
import com.ssm.model.User;

public interface IUserService {
    public User getUserByUsername(String username);
}