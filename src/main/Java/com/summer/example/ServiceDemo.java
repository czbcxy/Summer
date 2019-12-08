package com.summer.example;

import com.summer.framework.annotation.Service;

@Service
public class ServiceDemo {
    public String hello() {
        return "service is exectue";
    }

    public String world() {
        return "<html><a href='https://github.com/czbcxy/Summer/tree/master'>This is Summer link</a></html>";
    }
}
