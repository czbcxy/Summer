package com.summer.test;

import com.summer.example.ControllerDemo;
import com.summer.framework.context.support.SunGenericApplicationContext;
import org.junit.jupiter.api.Test;

public class ApplicationContentTest {

    @Test
    public void content() {
        SunGenericApplicationContext context = new SunGenericApplicationContext(new String[]{"Applicaiton.properties"});
        ControllerDemo controllerDemo = (ControllerDemo) context.getBean("controllerDemo");
        System.out.println(controllerDemo.world());
    }
}
