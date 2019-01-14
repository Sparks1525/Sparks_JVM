package jvm.test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test4 {


    public static void main(String[] args) {

        Test4 test = new Test4();

        List<Integer> list = Arrays.asList(14, 39, 56, 50, 55, 11, 54, 20, 28, 26, 25, 57, 12, 15, 58, 24);

        System.out.println(test.sortGuanpaiCards2(list));
    }

    public List<Integer> sortGuanpaiCards2(List<Integer> inPokers) {
        List<Integer> rsList = new ArrayList<>();

        List<Integer> judgeList = new ArrayList<>();
        judgeList.addAll(inPokers);

        int twoCard = 0;
        //获取最大 2
        if (judgeList.contains(15)) {
            twoCard = 15;
            judgeList.remove(new Integer(15));
        }
        if (twoCard != 0) {
            rsList.add(twoCard);
        }

        //获取其他牌循序
        List<Integer> smallCards = new ArrayList<>();
        smallCards.addAll(sortAndConvertCards2(judgeList));
        //花色排序
        Collections.sort(judgeList);

        for (int i = 0; i < smallCards.size(); i++) {

            for (int j = 0; j < judgeList.size(); j++) {
                if (smallCards.get(i) == convertTo15(judgeList.get(j))) {
                    rsList.add(judgeList.get(j));
                    judgeList.remove(j);
                    break;
                }
            }
        }

        return rsList;
    }

    //排序
    public List<Integer> sortAndConvertCards2(List<Integer> inPokers) {

        List<Integer> judgeList = new ArrayList<>();

        for (Integer inPoker : inPokers) {
            judgeList.add(convertTo15(inPoker));
        }

        Collections.sort(judgeList, Collections.<Integer>reverseOrder());

        System.out.println("judgeList="+judgeList);

        return judgeList;
    }

    /**
     * 将大码转标准扑克小码
     *
     * @param card .
     * @return .
     */
    public int convertTo15(int card) {
        return (card - 1) % 15 + 1;
    }
}
