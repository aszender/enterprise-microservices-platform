package com.aszender.spring_backend;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component              
public class MyBean {
    public String sayHello() {
        return "Hello from MyBean!";
    }
}

@Service
class GreetingService {
    private final MyBean myBean;

    public GreetingService(MyBean myBean) {
        this.myBean = myBean;
    }

    public String getGreeting() {
        return myBean.sayHello();
    }
}