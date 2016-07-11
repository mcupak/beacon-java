package com.dnastack.beacon.core.service.test;

import com.dnastack.beacon.core.service.BeaconService;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

/**
 * @author Artem (tema.voskoboynick@gmail.com)
 * @version 1.0
 */
@Decorator
public class MyInterrfaceSecondImpl implements MyInterface {

    @Inject
    @Delegate
    @Any
    private MyInterface MyInterface;

    @Override
    public void hello() {
        System.out.println("hello EXTENDED");
    }
}
