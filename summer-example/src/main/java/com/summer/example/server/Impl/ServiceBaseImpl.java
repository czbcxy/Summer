package com.summer.example.server.Impl;

import com.summer.annotation.Service;
import com.summer.example.entry.User;
import com.summer.example.server.ServiceBase;

@Service
public class ServiceBaseImpl implements ServiceBase {

    public User motto() {
        User user = new User();
        user.setUsername("程征波");
        user.setMotto("一个今天胜过两个明天!");
        return user;
    }

    public String world() {
        return "<html><a href='https://github.com/czbcxy/Summer/tree/master'>This is Summer link</a></html>";
    }
}
