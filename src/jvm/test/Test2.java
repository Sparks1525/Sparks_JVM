package jvm.test;

import java.util.ArrayList;
import java.util.List;

public class Test2 {
    public static void main(String[] args) {
//        for (int i = 0; i < 100000000; i++) {
//            List<Integer> list;
//        }
        //recursive();
        test1();

    }


//    public static int recursive() {
//        return recursive();
//    }

//    Document[] old ......//这是数据源
//    EntityDocument[] newArray = new EntityDocument[old.length];//自定义的类，为了把Document里数据保留下来避免Document被关联对象关闭而导致无法取出数据。
//    EntityDocument d = new EntityDocument();
//    for(int i = 0;i<old.length;i++){
//        d.content = old[i].getContent();
//        d.key = old[i].getKey();
//        d......................
//        newArray[i] = d;//如此对象重用.....
//    }


    public static void test1(){
        Student stu = new Student();
        Student[] stuList = new Student[5];

        for(int i = 0; i < 5; i++){
            stu.age = i;
            stu.name = "name:" + i * 1000;
            stuList[i] = stu;
        }

        if(stuList[0] == stuList[1]){
            System.out.println("equal");
        }else {
            System.out.println("no equal");
        }

        for(int i = 0; i < 5; i++){
            System.out.println(stuList[i].toString());
        }

    }
}
