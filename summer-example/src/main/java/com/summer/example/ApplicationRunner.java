package com.summer.example;

import com.summer.annotation.ApplicationRun;
import com.summer.context.SunApplicationContext;
import com.summer.context.support.SunWebApplicationContext;
import com.summer.example.server.Impl.ServiceBaseImpl;

@ApplicationRun
public class ApplicationRunner {

    public static void main(String[] args) throws Exception {
        SunApplicationContext context = SunWebApplicationContext.run(ApplicationRunner.class);
        final ServiceBaseImpl serviceBaseImpl = (ServiceBaseImpl) context.getBean("serviceBaseImpl");
        System.err.println(serviceBaseImpl.motto());
    }
}
