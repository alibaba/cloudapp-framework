package com.alibaba.cloudapp.hsf.demo.service;


import java.io.Serializable;

public interface HelloService extends Serializable {
    String echo(String string);
}
