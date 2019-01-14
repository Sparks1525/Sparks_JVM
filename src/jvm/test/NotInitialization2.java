package jvm.test;

public class NotInitialization2 {

    static {
        System.out.println("NotInitialization2 init!");
    }

    public static void main(String[] args) {
        System.out.println(ConstClass.HELLOWORLD);
    }

}
