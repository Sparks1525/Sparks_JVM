package jvm.test;

public class Empty {

    private int b = 0;
    public static void main(String[] args){
        Empty empty = new Empty();
        int a = 1;
        double c = 0;
        empty.add(c, a);
    }

    private double add(double c, int a){
        return c + a;
    }
}
