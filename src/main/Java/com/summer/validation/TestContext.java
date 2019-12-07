package com.summer.validation;

import com.summer.framework.context.support.SunGenericApplicationContext;

public class TestContext  {

    public static void main(String[] args) {
        SunGenericApplicationContext context = new SunGenericApplicationContext(new String[]{"Applicaiton.properties"});
        ControllerDemo controllerDemo = (ControllerDemo) context.getBean("controllerDemo");
        System.out.println(controllerDemo.hello());
    }
}
