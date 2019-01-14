package jvm.test;

public class StaticTest {
    public static void main(String[] args) {
        staticFunction();
    }

    /*
    类的初始化阶段需要做是执行类构造器（类构造器是编译器收集所有静态语句块和类变量的赋值语句按语句在源码中的顺序合并生成类构造器，
    对象的构造方法是<init>()，类的构造方法是<clinit>()，可以在堆栈信息中看到）
     */

    static StaticTest st = new StaticTest(); // 2 3 a110 b0 1 4
    static {
        System.out.println("1");
    }
    //static StaticTest st = new StaticTest(); // 123 a110 b0 4

    {
        System.out.println("2");
    }

    StaticTest() {
        System.out.println("3");
        System.out.println("a=" + a + ",b=" + b);
    }

    public static void staticFunction() {
        System.out.println("4");
    }

    int a = 110;
    static int b = 112;
}


//
//
//class SuperSuper{
//    public static int m = 111;
//    static{
//        System.out.println("执行了supersuper类静态语句块");
//    }
//}
//
//class Super extends SuperSuper{
//    public static int m = 11;
//    static{
//        System.out.println("执行了super类静态语句块");
//    }
//}
//
//
//class Father extends Super{
//    //public static int m = 33;
//    static{
//        System.out.println("执行了父类静态语句块");
//    }
//}
//
//class Child extends Father{
//    //public static int m = 44;
//    static{
//        System.out.println("执行了子类静态语句块");
//    }
//}
//
//public class StaticTest{
//    public static void main(String[] args){
//        System.out.println(Child.m);
//    }
//}