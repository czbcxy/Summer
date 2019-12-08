package com.summer.example.server.Impl;

import com.summer.example.entry.User;
import com.summer.example.server.ServiceBase;
import com.summer.framework.annotation.Service;

@Service
public class ServiceBaseImpl implements ServiceBase {

    public User hello() {
        User user = new User();
        user.setUsername("程征波");
        user.setMotto("一个今天胜过两个明天!");
        return user;
    }

    public String world() {
        return "<html><a href='https://github.com/czbcxy/Summer/tree/master'>This is Summer link</a></html>";
    }
}
