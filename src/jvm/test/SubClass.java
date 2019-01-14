package jvm.test;

public class SubClass extends SuperClass {

    static {
        System.out.println("SubClass init!");
    }

    public static int subValue = 456;
}
