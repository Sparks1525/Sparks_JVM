package test2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${wrd} on 2017/7/31.
 * 玩家选中手牌的牌型
 */
public class PlayerBean {

    private int result = 0;

    private List<Integer> poker = new ArrayList<>();

    private int maxCard = 0;

    public int getMaxCard() {
        return maxCard;
    }

    public void setMaxCard(int maxCard) {
        this.maxCard = maxCard;
    }

    public List<Integer> getPoker() {
        return poker;
    }

    public void setPoker(List<Integer> poker) {
        this.poker = poker;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "PlayerBean{" +
                "result=" + result +
                ", poker=" + poker +
                ", maxCard=" + maxCard +
                '}';
    }
}
