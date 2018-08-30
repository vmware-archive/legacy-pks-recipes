package com.pivotalservices.sample.impl;

import com.pivotalservices.sample.api.Hello;

public class HelloImpl implements Hello {
    
    @Override
    public String hello() {
        return "Hello World!";
    }
}
