package com.dnastack.beacon.core.service.test;

import javax.enterprise.context.Dependent;

/**
 * @author Artem (tema.voskoboynick@gmail.com)
 * @version 1.0
 */
@Dependent
public class MyInterfaceImpl implements MyInterface {
    @Override
    public void hello() {
        System.out.println("hello");
    }
}
