package jvm.test;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BTraceTest {
    public  int add(int i,int j){

        return i +j;
    }

    public  static  void main(String []args) throws  Exception{
        BTraceTest traceTest = new BTraceTest();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        for( int i =0 ;i<10;i++){
            reader.readLine();
            int a = (int) Math.round(Math.random() *1000);
            int b = (int) Math.round(Math.random() *1000);
            System.out.println(traceTest.add(a,b));
        }
    }

}
