package test2;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 扑克牌
 * 3~A~2
 * 3~15,黑桃|红桃|梅花|方片
 * 花色之间+15
 * Created by Administrator on 2017/3/28.
 */
public class Poker {

    //扑克牌
    private List<Integer> poker = new ArrayList<>();
    private List<Integer> oldPoker = new ArrayList<>();

    public Poker() {
        initPoker();
    }

    //洗牌
    public void shuffle()
    {
        //置空
        this.poker = new ArrayList<>();
        //赋牌
        poker.addAll(oldPoker);
        //洗牌
        Collections.shuffle(poker);
    }

    //装载扑克牌
    private void initPoker()
    {
        int card = 0;
        int radio = 0;
        //四色牌
        for(int i = 0; i < 4; i++)
        {
            radio = i * 15;
            for(int j = 3; j <= 15; j++)
            {
                card = j + radio;
                this.poker.add(card);
            }
        }

        this.poker.remove(this.poker.lastIndexOf(60));
        this.poker.remove(this.poker.lastIndexOf(30));
        this.poker.remove(this.poker.lastIndexOf(45));
        this.poker.remove(this.poker.lastIndexOf(59));

        this.oldPoker.addAll(this.poker);
    }

    //发牌
    public List<Integer> deal(int inNum)
    {
        int num;
        num = inNum;
        List<Integer> result = new ArrayList<>(17);
        if(poker.size() >= num){
            Iterator<Integer> it = poker.iterator();
            while (it.hasNext()){
                if(num == 0){
                    break;
                }
                result.add(it.next());
                it.remove();
                num--;
            }
        }
        //容错机制
        if(result.size() != inNum){
            return repeatDeal(inNum,result);
        }
        return result;
    }

    //重新发牌
    public List<Integer> repeatDeal(int num,List<Integer> inPoker)
    {
        if(!inPoker.isEmpty()){
            poker.addAll(inPoker);
        }
        //重新洗牌
        Collections.shuffle(poker);

        return this.deal(num);
    }

    /**
     * 将大码转标准扑克小码
     * @param card .
     * @return .
     */
    public int convertTo15(int card)
    {
        return (card - 1) % 15 + 1;
    }

    //排序
    public List<Integer> sortAndConvertCards(List<Integer> inPokers)
    {

        List<Integer> judgeList = new ArrayList<>();

        for (Integer inPoker : inPokers)
        {
            judgeList.add(convertTo15(inPoker));
        }

        Collections.sort(judgeList);

        return judgeList;
    }


    public List<Integer> sortGuanpaiCards(List<Integer> inPokers){

        List<Integer> rsList = new ArrayList<>();

        List<Integer> judgeList = new ArrayList<>();
        judgeList.addAll(inPokers);

        int twoCard = 0;
        //获取最大 2
        if(judgeList.contains(15)){
            twoCard = 15;
            judgeList.remove(new Integer(15));
        }

        //获取其他牌循序
        List<Integer> smallCards = new ArrayList<>();
        smallCards.addAll(sortAndConvertCards(judgeList));
        //花色排序
        Collections.sort(judgeList,Collections.<Integer>reverseOrder());

        for(int i=0;i<smallCards.size();i++){

            for(int j=0;j<judgeList.size();j++){
                if(smallCards.get(i) == convertTo15(judgeList.get(j))){
                    rsList.add(judgeList.get(j));
                    judgeList.remove(j);
                    break;
                }
            }
        }
        if(twoCard != 0) {
            rsList.add(twoCard);
        }
        return rsList;
    }

    //排序
    public List<Integer> sortAndConvertCards2(List<Integer> inPokers)
    {

        List<Integer> judgeList = new ArrayList<>();

        for (Integer inPoker : inPokers)
        {
            judgeList.add(convertTo15(inPoker));
        }

        Collections.sort(judgeList,Collections.<Integer>reverseOrder());

        return judgeList;
    }

    public List<Integer>sortGuanpaiCards2(List<Integer> inPokers){
        List<Integer> rsList = new ArrayList<>();

        List<Integer> judgeList = new ArrayList<>();
        judgeList.addAll(inPokers);

        int twoCard = 0;
        //获取最大 2
        if(judgeList.contains(15)){
            twoCard = 15;
            judgeList.remove(new Integer(15));
        }
        if(twoCard != 0) {
            rsList.add(twoCard);
        }

        //获取其他牌循序
        List<Integer> smallCards = new ArrayList<>();
        smallCards.addAll(sortAndConvertCards2(judgeList));
        //花色排序
        Collections.sort(judgeList);

        for(int i=0;i<smallCards.size();i++){

            for(int j=0;j<judgeList.size();j++){
                if(smallCards.get(i) == convertTo15(judgeList.get(j))){
                    rsList.add(judgeList.get(j));
                    judgeList.remove(j);
                    break;
                }
            }
        }

        return rsList;
    }
}

