package test2;


import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ${wrd} on 2017/7/31.
 */
public class JudgeCard extends JudgeCardType {

    public JudgeCard() {

    }

    public static void main(String[] args){
        JudgeCard judgeCard = new JudgeCard();
        List<Integer> list = Arrays.asList(10, 40, 55, 18, 33);
        System.out.println(judgeCard.getCardType(list));
    }


    /**
     * 判断牌型
     *
     * @param list .
     * @return .
     */
    public PlayerBean getCardType(List<Integer> list) {

        List<Integer> smallList = sortAndConvertCards(list);

        PlayerBean playerBean = new PlayerBean();

        switch (smallList.size()) {
            case 1:
                playerBean.setResult(PokerType.SINGLE);
                break;

            case 2:
            case 14:
            case 16:
                if (judgePairs(smallList, playerBean)) {
                    playerBean.setResult(PokerType.PAIRS);
                }
                break;

            case 3:
                if (judgeBoom(smallList, playerBean) != 0) {
                    playerBean.setResult(PokerType.FOUR_OF_ONE_KIND);
                } else if (judgeThreeOfOneKind(smallList, playerBean) != 0) {
                    playerBean.setResult(PokerType.THREE);
                }
                break;

            case 4:
                if (1 == judgeBoom(smallList, playerBean)) {
                    playerBean.setResult(PokerType.FOUR_OF_ONE_KIND);
                } else if (judgePairs(smallList, playerBean)) {
                    playerBean.setResult(PokerType.PAIRS);
                }
                break;

            case 5:
                if (judgeStraight(smallList, playerBean)) {
                    playerBean.setResult(PokerType.STRAIGHT);
                } else if (1 == judgeBoom(smallList, playerBean)) {
                    playerBean.setResult(PokerType.FOUR_OF_ONE_KIND);
                } else if (31 == judgeThreeOfOneKind(smallList, playerBean)) {
                    playerBean.setResult(PokerType.THREE_WITH_PAIRS);
                }
                break;

            case 6:
            case 12:
                if (judgePairs(smallList, playerBean)) {
                    playerBean.setResult(PokerType.PAIRS);
                } else if (judgeStraight(smallList, playerBean)) {
                    playerBean.setResult(PokerType.STRAIGHT);
                } else if (32 == judgeThreeOfOneKind(smallList, playerBean)) {
                    playerBean.setResult(PokerType.THREE_WHITOUT_PAIRS);
                }
                break;

            case 7:
            case 11:
            case 13:
                if (judgeStraight(smallList, playerBean)) {
                    playerBean.setResult(PokerType.STRAIGHT);
                }
                break;

            case 8:
                if (judgeStraight(smallList, playerBean)) {
                    playerBean.setResult(PokerType.STRAIGHT);
                } else if (judgePairs(smallList, playerBean)) {
                    playerBean.setResult(PokerType.PAIRS);
                }
                break;

            case 9:
                if (judgeStraight(smallList, playerBean)) {
                    playerBean.setResult(PokerType.STRAIGHT);
                } else if (32 == judgeThreeOfOneKind(smallList, playerBean)) {
                    playerBean.setResult(PokerType.THREE_WHITOUT_PAIRS);
                }
                break;

            case 10:
                if (judgeStraight(smallList, playerBean)) {
                    playerBean.setResult(PokerType.STRAIGHT);
                } else if (34 == judgeThreeOfOneKind(smallList, playerBean)) {
                    playerBean.setResult(PokerType.THREE_WITH_WIND);
                } else if (judgePairs(smallList, playerBean)) {
                    playerBean.setResult(PokerType.PAIRS);
                }
                break;

            case 15:
                if (32 == judgeThreeOfOneKind(smallList, playerBean)) {
                    playerBean.setResult(PokerType.THREE_WHITOUT_PAIRS);
                } else if (34 == judgeThreeOfOneKind(smallList, playerBean)) {
                    playerBean.setResult(PokerType.THREE_WITH_WIND);
                }
                break;

        }

        playerBean.setPoker(optimizePoker(smallList, list, playerBean.getResult()));

        return playerBean;
    }

    /**
     * 比牌
     *
     * @param player1 .
     * @param player2 ,
     * @return 如果1比2大，返回true，否则返回false
     */
    public boolean settlePoker(PlayerBean player1, PlayerBean player2) {
        List<Integer> cardList1 = new ArrayList<>();
        cardList1.addAll(convertToSmallList(player1.getPoker()));

        List<Integer> cardList2 = new ArrayList<>();
        cardList2.addAll(convertToSmallList(player2.getPoker()));

        int result1 = player1.getResult();
        int result2 = player2.getResult();

        if (result1 == 0) {
            return false;
        }
        if (result2 == 0) {
            return true;
        }

        if (result1 != result2 && (result2 != PokerType.FOUR_OF_ONE_KIND)) {
            if (result1 == PokerType.FOUR_OF_ONE_KIND) {
                return true;
            }
        } else if (result1 == result2) {

            switch (result1) {
                case 1:
                    if (cardList1.get(0) > cardList2.get(0)) {
                        return true;
                    }
                    break;

                case 2:
                    if (player1.getPoker().size() == player2.getPoker().size()) {
                        if (player1.getMaxCard() > player2.getMaxCard()) {
                            return true;
                        }
                    }
                    break;

                case 30:
                case 31:
                case 32:
                case 34:
                    if (player1.getPoker().size() == player2.getPoker().size()) {
                        if (player1.getMaxCard() > player2.getMaxCard()) {
                            return true;
                        }
                    }
                    break;

                case 40:
                    if (cardList1.get(0) > cardList2.get(0)) {
                        return true;
                    }
                    break;

                case 5:
                    if (player1.getMaxCard() > player2.getMaxCard()) {
                        return true;
                    }
            }

        }

        return false;
    }

    /**
     * 提示，得到比牌桌更大的单牌
     *
     * @param myCard     .
     * @param playerBean .
     * @return 返回更大牌的集合
     */
    public List<Integer> getBigCard(List<Integer> myCard, PlayerBean playerBean) {
        List<Integer> bigCards = new ArrayList<>();
        List<Integer> playerSmallList = this.convertToSmallList(playerBean.getPoker());
        List<Integer> mySmallList = this.convertToSmallList(myCard);
        Collections.sort(mySmallList);
        if (playerBean != null && playerBean.getResult() == 1) {

            for (Integer card : mySmallList) {
                if (card > playerSmallList.get(0)) {
                    bigCards.add(card);
                    break;
                }
            }
        }
        if (playerBean.getResult() == 0) {
            bigCards.add(mySmallList.get(0));
        }

        List<Integer> temp = optimizePoker(bigCards, myCard, 1);

        return temp;
    }

    /**
     * 提示，得到比牌桌更大的牌
     *
     * @param myCard     .
     * @param playerBean .
     * @return 返回更大牌的集合
     */
    public List<Integer> getBiggestCard(List<Integer> myCard, PlayerBean playerBean) {

        List<List<Integer>> biggestCardList = new ArrayList<>();

        List<Integer> biggestCard = new ArrayList<>();

        HashMap<Integer, Integer> map = new HashMap<>();

        List<Integer> tempList;

        List<Integer> tableCard = convertToSmallList(playerBean.getPoker());

        List<Integer> smallList = this.convertToSmallList(myCard);

        Collections.sort(smallList);

        int result = playerBean.getResult();

        for (Integer card : smallList) {
            if (!map.containsKey(card)) {
                map.put(card, 1);
            } else {
                map.put(card, map.get(card) + 1);
            }
        }

        if (result == 0) {
            for (Integer card : smallList) {
                if (map.get(card) != 4) {
                    biggestCard.add(card);
                    biggestCardList.add(biggestCard);
                    break;
                }
            }
        }

        int length;

        Iterator iterator = map.entrySet().iterator();

        switch (result) {
            //单牌
            case 1:

                for (Integer card : smallList) {
                    if (card > tableCard.get(0)) {
                        if (map.get(card) != 4) {
                            biggestCard.add(card);
                        }
                    }
                }

                tempList = this.removeDuplicate(biggestCard);

                Collections.sort(tempList);

                if (!tempList.isEmpty()) {
                    List<Integer> cardList = new ArrayList<>();

                    for (Integer card : tempList) {
                        cardList.add(card);

                        biggestCardList.add(cardList);

                        cardList = new ArrayList<>();
                    }
                }

                break;

            //对子，连对
            case 2:

                length = tableCard.size() / 2;

                while (iterator.hasNext()) {
                    Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) iterator.next();

                    if (entry.getValue() == 2 || entry.getValue() == 3) {
                        tempList = new ArrayList<>();

                        biggestCard.add(entry.getKey());
                        biggestCard.add(entry.getKey());

                        tempList.addAll(biggestCard);

                        if (biggestCard.size() / 2 == length) {
                            if (isStraight(removeDuplicate(biggestCard)) && playerBean.getMaxCard() < biggestCard.get(biggestCard.size() - 1)) {
                                biggestCardList.add(biggestCard);
                                biggestCard = new ArrayList<>();

                                tempList.remove(0);
                                tempList.remove(0);
                                biggestCard.addAll(tempList);
                            } else {
                                biggestCard = new ArrayList<>();
                                tempList.remove(0);
                                tempList.remove(0);
                                biggestCard.addAll(tempList);
                            }
                        }
                    }
                }
                break;


            //三同牌
            case 30:
                //三同牌带一对
            case 31:

                while (iterator.hasNext()) {
                    Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) iterator.next();

                    if (entry.getValue() == 3) {
                        if (entry.getKey() > playerBean.getMaxCard() && entry.getKey() != 14) {
                            for (int i = 0; i < 3; i++) {
                                biggestCard.add(entry.getKey());
                            }

                            if (biggestCard.size() != tableCard.size()) {

                                biggestCard.addAll(getMinSingleOrPairs(map, biggestCard, PokerType.PAIRS));

                                if (biggestCard.get(0) != biggestCard.get(biggestCard.size() - 1)) {
                                    biggestCardList.add(biggestCard);
                                    biggestCard = new ArrayList<>();
                                } else {
                                    biggestCard = new ArrayList<>();
                                }
                            } else {
                                biggestCardList.add(biggestCard);
                                biggestCard = new ArrayList<>();
                            }
                        }
                    }
                }
                break;

            //飞机不带
            case 32:
                //飞机带翅膀
            case 34:

                if (result == 32) {
                    length = tableCard.size() / 3;
                } else {
                    length = tableCard.size() / 5;
                }

                while (iterator.hasNext()) {
                    Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) iterator.next();

                    if (entry.getValue() == 3 || entry.getValue() == 4) {
                        tempList = new ArrayList<>();

                        biggestCard.add(entry.getKey());
                        biggestCard.add(entry.getKey());
                        biggestCard.add(entry.getKey());

                        tempList.addAll(biggestCard);

                        if (biggestCard.size() / 3 == length) {
                            if (isStraight(removeDuplicate(biggestCard)) && playerBean.getMaxCard() < biggestCard.get(biggestCard.size() - 1)) {
                                if (result == 31) {
                                    biggestCardList.add(biggestCard);
                                    biggestCard = new ArrayList<>();

                                    tempList.remove(0);
                                    tempList.remove(0);
                                    tempList.remove(0);

                                    biggestCard.addAll(tempList);
                                } else {
                                    //判断要带的对子
                                    biggestCard.addAll(getMinSingleOrPairs(map, tempList, result));

                                    if (biggestCard.size() != tableCard.size()) {
                                        tempList.remove(0);
                                        tempList.remove(0);
                                        tempList.remove(0);

                                        biggestCard = new ArrayList<>();
                                        biggestCard.addAll(tempList);
                                    } else {
                                        biggestCardList.add(biggestCard);

                                        tempList.remove(0);
                                        tempList.remove(0);
                                        tempList.remove(0);

                                        biggestCard = new ArrayList<>();
                                        biggestCard.addAll(tempList);
                                    }
                                }
                            } else {
                                biggestCard = new ArrayList<>();

                                tempList.remove(0);
                                tempList.remove(0);
                                tempList.remove(0);

                                biggestCard.addAll(tempList);
                            }
                        }
                    }
                }
                break;

            //炸弹
            case 40:
                //炸弹带一张
            case 41:

                while (iterator.hasNext()) {
                    Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) iterator.next();

                    //A 炸的情况
                    if (entry.getValue() == 3 && entry.getKey() == 14 && playerBean.getMaxCard() != 14) {
                        biggestCard.add(entry.getKey());
                        biggestCard.add(entry.getKey());
                        biggestCard.add(entry.getKey());

                        //炸弹要带一张
                        if (result == 41) {
                            if (map.size() == 1) {
                                return biggestCardList.get(0);
                            } else {
                                biggestCard.addAll(getMinSingleOrPairs(map, PokerType.SINGLE, 1));

                                if (biggestCard.size() == 3) {
                                    return biggestCardList.get(0);
                                }
                            }
                        }
                        biggestCardList.add(biggestCard);
                        biggestCard = new ArrayList<>();
                    }

                    //四张炸弹的情况
                    else if (entry.getValue() == 4) {
                        if (playerBean.getMaxCard() < entry.getKey()) {
                            for (int i = 0; i < 4; i++) {
                                biggestCard.add(entry.getKey());
                            }
                            if (result == 41) {
                                if (1 == map.size()) {
                                    return biggestCardList.get(0);
                                } else {
                                    biggestCard.addAll(getMinSingleOrPairs(map, PokerType.SINGLE, 1));

                                    if (biggestCard.size() == 3) {
                                        return biggestCardList.get(0);
                                    }
                                }
                            }
                            biggestCardList.add(biggestCard);
                            biggestCard = new ArrayList<>();
                        }
                    }
                }
                break;

            //顺子
            case 5:

                length = playerBean.getPoker().size();

                List<Integer> list = removeDuplicate(smallList);

                Collections.sort(list);

                for (int i = 0; i < list.size(); i++) {
                    if (i + length <= list.size()) {
                        biggestCard.addAll(list.subList(i, i + length));

                        if (isStraight(biggestCard) && playerBean.getMaxCard() < biggestCard.get(biggestCard.size() - 1)) {
                            if (biggestCard.get(biggestCard.size() - 1) == 15) {
                                biggestCard = new ArrayList<>();
                            } else {
                                biggestCardList.add(biggestCard);
                                biggestCard = new ArrayList<>();
                            }
                        } else {
                            biggestCard = new ArrayList<>();
                        }
                    } else {
                        break;
                    }
                }
                break;
        }

        //牌桌上的牌不为炸弹时，获得手牌中炸弹的集合
        if (result != PokerType.FOUR_OF_ONE_KIND) {
            Iterator iter = map.entrySet().iterator();

            while (iter.hasNext()) {
                Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) iter.next();

                biggestCard = new ArrayList<>();

                if (entry.getValue() == 4) {
                    for (int i = 0; i < 4; i++) {
                        biggestCard.add(entry.getKey());
                    }
                    biggestCardList.add(biggestCard);
                } else if (entry.getValue() == 3 && entry.getKey() == 14) {
                    for (int i = 0; i < 3; i++) {
                        biggestCard.add(entry.getKey());
                    }
                    biggestCardList.add(biggestCard);
                }
            }
        }

        List<Integer> tempLists = new ArrayList<>();

        if (biggestCardList.size() != 0) {
            JudgeCard judgeCard = new JudgeCard();
            PlayerBean tempPlayerBean;
            tempPlayerBean = judgeCard.getCardType(biggestCardList.get(0));

            tempLists.addAll(optimizePoker(biggestCardList.get(0), myCard, tempPlayerBean.getResult()));
        }

        return tempLists;
    }

    /**
     * 删除打出的牌
     *
     * @param myCardList 手牌
     * @param removeCard 打出的牌
     * @return 。
     */
    public List<Integer> removeSameCard(List<Integer> myCardList, List<Integer> removeCard) {

        //遍历
        for (Integer card : removeCard) {
            for (int i = 0; i < myCardList.size(); i++) {
                if (card == myCardList.get(i)) {
                    myCardList.remove(i);
                    break;
                }
            }
        }

        return myCardList;
    }


    /**
     * 获得单牌或者对子中的最小的牌
     *
     * @param map  .
     * @param type 牌型
     * @param num  需要的对子的数目
     * @return .
     */
    public List<Integer> getMinSingleOrPairs(HashMap<Integer, Integer> map, int type, int num) {
        Iterator iterator = map.entrySet().iterator();

        List<Integer> requireList = new ArrayList<>();

        List<Integer> tempList = new ArrayList<>();

        int cardType;

        if (map.size() == 0) {
            return requireList;
        }

        if (type != PokerType.SINGLE) {
            cardType = 2;
        } else {
            cardType = 1;
        }

        //取出map里需要的单牌或者对子
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) iterator.next();

            if (entry.getValue() == cardType) {
                tempList.add(entry.getKey());
            }
        }

        //如果不存在单牌或者对子
        if (tempList.size() == 0) {
            Iterator iterator1 = map.entrySet().iterator();

            while (iterator1.hasNext()) {
                Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) iterator1.next();

                if (type == PokerType.SINGLE) {
                    tempList.add(entry.getKey());
                    requireList.addAll(tempList);

                    return requireList;
                } else {
                    if (1 == num) {
                        if (entry.getValue() == 3 || entry.getValue() == 4) {
                            tempList.add(entry.getKey());
                            tempList.add(entry.getKey());

                            requireList.addAll(tempList);

                            return requireList;
                        }
                    } else if (2 == num) {

                        //需要连对
                        if (type == PokerType.THREE_WITH_WIND) {
                            if (entry.getValue() == 3 || entry.getValue() == 4) {

                                tempList.add(entry.getKey());
                                tempList.add(entry.getKey());

                                List<Integer> tempAddCard = new ArrayList<>();

                                tempAddCard.addAll(tempList);

                                if (tempList.size() == 4 && isStraight(removeDuplicate(tempList))) {
                                    requireList.addAll(tempList);

                                    return requireList;
                                } else if (tempList.size() == 4) {
                                    tempAddCard.remove(0);
                                    tempAddCard.remove(0);

                                    tempList = new ArrayList<>();
                                    tempList.addAll(tempAddCard);
                                }
                            }
                        } else {
                            if (entry.getValue() == 3 || entry.getValue() == 4) {
                                tempList.add(entry.getKey());
                                tempList.add(entry.getKey());

                                if (tempList.size() == 4) {
                                    requireList.addAll(tempList);

                                    return requireList;
                                }
                            }
                        }
                    } else {
                        return requireList;
                    }
                }
            }
        }

        //有对子但是需要更多对子的情况
        if (tempList.size() != 0 && tempList.size() < num) {

            Iterator iterator2 = map.entrySet().iterator();

            while (iterator2.hasNext()) {

                Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) iterator2.next();

                if (entry.getValue() == 3 || entry.getValue() == 4) {
                    tempList.add(entry.getKey());
                    tempList.add(entry.getKey());
                }
            }
        }

        Collections.sort(tempList);

        //返回单张
        if (type == PokerType.SINGLE) {

            if (tempList.size() < 5) {
                requireList.add(tempList.get(0));
                return requireList;
            } else {
                if (isStraight(tempList)) {
                    requireList.add(tempList.get(0));
                    return requireList;
                }

                for (int i = 0; i < tempList.size(); i++) {

                    if (!isStraight(tempList.subList(i, i + 5))) {
                        if (i == 0) {
                            requireList.add(tempList.get(0));
                            return requireList;
                        } else {
                            requireList.add(tempList.get(i + 4));
                            return requireList;
                        }
                    }

                }
            }


            requireList.add(tempList.get(0));
            return requireList;
        }
        //返回对子
        else {
            //不存在对子时
            if (tempList.size() < num) {
                return requireList;
            }

            //返回一对
            if (num == 1) {
                if (tempList.size() == 1) {
                    requireList.add(tempList.get(0));
                    requireList.add(tempList.get(0));
                    return requireList;
                }

                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(0) + 1 != tempList.get(1)) {
                        requireList.add(tempList.get(0));
                        requireList.add(tempList.get(0));
                        return requireList;
                    } else if (tempList.get(i) - i != tempList.get(0)) {
                        requireList.add(tempList.get(i));
                        requireList.add(tempList.get(i));
                        return requireList;
                    } else {
                        requireList.add(tempList.get(0));
                        requireList.add(tempList.get(0));
                        return requireList;
                    }
                }
            } else if (num == 3) {
                requireList.add(tempList.get(0));
                requireList.add(tempList.get(0));
                requireList.add(tempList.get(1));
                requireList.add(tempList.get(1));
                requireList.add(tempList.get(2));
                requireList.add(tempList.get(2));

                return requireList;
            } else {
                //需要返回连对
                if (type == PokerType.THREE_WITH_WIND) {
                    for (int i = 0; i < tempList.size(); i++) {
                        if (i + 1 < tempList.size() && tempList.get(i) + 1 == tempList.get(i + 1)) {
                            requireList.add(tempList.get(i));
                            requireList.add(tempList.get(i));
                            requireList.add(tempList.get(i + 1));
                            requireList.add(tempList.get(i + 1));

                            return requireList;
                        }
                        //不存在连对
                        else {
                            return requireList;
                        }
                    }

                }
                //返回对子
                else {
                    requireList.add(tempList.get(0));
                    requireList.add(tempList.get(0));
                    requireList.add(tempList.get(1));
                    requireList.add(tempList.get(1));

                    return requireList;
                }
            }
        }

        return requireList;
    }

    /**
     * 获得对子中的连续的牌
     *
     * @param map        .
     * @param selectList .
     * @param type       .
     * @return 。
     */
    public List<Integer> getMinSingleOrPairs(HashMap<Integer, Integer> map, List<Integer> selectList, int type) {
        List<Integer> requireList = new ArrayList<>();

        List<Integer> list = removeDuplicate(selectList);

        HashMap<Integer, Integer> tempMap = new HashMap<>();

        tempMap.putAll(map);

        for (Integer card : list) {
            tempMap.remove(card);
        }

        requireList.addAll(getMinSingleOrPairs(tempMap, type, selectList.size() / 3));

        return requireList;
    }

    /**
     * 优化牌型
     *
     * @param newList
     * @param oldList
     * @param type
     * @return
     */
    public List<Integer> optimizePoker(List<Integer> newList, List<Integer> oldList, int type) {

        List<Integer> tempList = new ArrayList<>();

        if (PokerType.SINGLE == type || PokerType.PAIRS == type || PokerType.THREE_WHITOUT_PAIRS == type || PokerType.THREE == type || PokerType.STRAIGHT == type) {
            tempList = getOldCard(newList, oldList);
            return tempList;
        }

        List<Integer> newL = new CopyOnWriteArrayList<>();
        newL.addAll(newList);

        if (PokerType.THREE_WITH_PAIRS == type || PokerType.THREE_WITH_WIND == type) {
            List<Integer> swapPokerList = swap(newL);

            for (Integer swapPoker : swapPokerList) {
                for (Integer newPoker : newL) {
                    if (swapPoker == newPoker) {
                        newL.remove(newPoker);
                        newL.add(newPoker);
                    }
                }
            }
            tempList = getOldCard(newL, oldList);
        }
        if (PokerType.FOUR_OF_ONE_KIND == type) {
            if (newL.get(0) != newL.get(1)) {
                int temp = newL.get(0);
                newL.remove(0);
                newL.add(temp);
            }
            tempList = getOldCard(newL, oldList);
        }
        return tempList;
    }

    /**
     * 获取原来的牌
     *
     * @param newList
     * @param oldList
     * @return
     */
    public List<Integer> getOldCard(List<Integer> newList, List<Integer> oldList) {
        List<Integer> tempList = new ArrayList<>();

        List<Integer> newL = new CopyOnWriteArrayList<>();
        newL.addAll(newList);

        List<Integer> oldL = new CopyOnWriteArrayList<>();
        oldL.addAll(oldList);

        for (Integer newPoker : newL) {

            for (Integer oldPoker : oldL) {
                if (newPoker == 1) {
                    newPoker = 14;
                }
                if (newPoker == 2) {
                    newPoker = 15;
                }
                if (newPoker == convertTo15(oldPoker)) {
                    tempList.add(oldPoker);
                    newL.remove(newPoker);
                    oldL.remove(oldPoker);
                    break;
                }
            }
        }
        return tempList;
    }

    /**
     * 获得需要调换位置的牌
     *
     * @param list
     * @return
     */
    public List<Integer> swap(List<Integer> list) {
        int mid;
        if (list.size() == 15) {
            mid = (list.size() - 2) / 2;
        } else {
            mid = (list.size() - 1) / 2;
        }
        List<Integer> tempList = new ArrayList<>();

        if (list.get(mid) != list.get(mid - 1)) {
            tempList = list.subList(0, mid);
        }
        return tempList;
    }
}

