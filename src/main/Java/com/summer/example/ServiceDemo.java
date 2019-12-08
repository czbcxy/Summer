package com.summer.example;

import com.summer.framework.annotation.Service;

@Service
public class ServiceDemo {
    public void hello() {
        System.out.println("service is exectue");
    }

    public String world() {
        return "helloworld";
    }
}
