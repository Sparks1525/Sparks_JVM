package jvm.test;

import java.util.ArrayList;
import java.util.List;

public class Test5 {


    public int nextChsIdx(int chairId) {
        int nextId;
        List<Integer> playGamers = new ArrayList<>();
        playGamers.add(0);
        playGamers.add(1);
        playGamers.add(2);

        // 下一家
        nextId = playGamers.indexOf(chairId) + 1;
        if (nextId >= playGamers.size()) {
            nextId = 0;
        }
        return playGamers.get(nextId);

    }

    public static void main(String[] args){
        Test5 test5 = new Test5();

        System.out.println(test5.nextChsIdx(0));
    }
}
