package test2;

import java.util.*;


/**
 * Created by ${wrd} on 2017/7/31.
 */
public class JudgeCardType extends Poker {
    public JudgeCardType() {
    }

    /**
     * 判断是否是顺子
     *
     * @param list 选中的牌
     * @return 是返回1，否返回0
     */
    protected boolean judgeStraight(List<Integer> list, PlayerBean playerBean) {

        //当只有一张 2 传进来时
        if (list.size() == 1 && list.get(0) == 15) {
            playerBean.setMaxCard(15);
            return true;
        }

        if (list.contains(14) && list.contains(15)) {

            int temp1 = list.get(list.size() - 1);
            int temp2 = list.get(list.size() - 2);

            list.remove(list.size() - 1);
            list.remove(list.size() - 1);
            list.add(0, 2);
            list.add(0, 1);

            if (isStraight(list)) {
                playerBean.setMaxCard(list.get(list.size() - 1));
                return true;
            }

            list.remove(0);
            list.remove(0);
            list.add(temp2);
            list.add(temp1);

        } else if (list.contains(15)) {
            list.remove(list.size() - 1);
            list.add(0, 2);

            if (isStraight(list)) {
                playerBean.setMaxCard(list.get(list.size() - 1));
                return true;
            }

            list.remove(0);
            list.add(15);

        } else {
            if (isStraight(list)) {
                playerBean.setMaxCard(list.get(list.size() - 1));
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否是对子，连对
     *
     * @param list 选中的牌
     * @return .
     */
    protected boolean judgePairs(List<Integer> list, PlayerBean playerBean) {

        if (isPairs(list)) {
            List<Integer> newList = removeDuplicate(list);

            if (judgeStraight(newList, playerBean)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否是炸弹
     *
     * @param list 选中的牌
     * @return 是为 1，否为 0
     */
    protected int judgeBoom(List<Integer> list, PlayerBean playerBean) {

        boolean flag = false;

        //三张 A 的情况
        if (list.size() == 3) {
            for (Integer card : list) {
                if (card != 14) {
                    flag = false;
                    break;
                } else {
                    flag = true;
                }
            }
        }

        //炸弹不带 或 三张 A 带一张
        else if (list.size() == 4) {
            if (isSameKind(list)) {
                flag = true;
            } else {
                if (isBoomWithSingle(list, playerBean)) {
                    return 1;
                }
            }
        }

        //四带一
        else if (list.size() == 5) {
            if (isBoomWithSingle(list, playerBean)) {
                return 1;
            }
        }

        if (flag) {
            playerBean.setMaxCard(list.get(0));
            return 1;
        }

        return 0;
    }

    /**
     * 判断是否是三同牌,飞机，飞机带翅膀
     *
     * @param list 选中的牌
     * @return 三同牌为30， 带一对为31，飞机不带为32,飞机带翅为34 否为0
     */
    protected int judgeThreeOfOneKind(List<Integer> list, PlayerBean playerBean) {

        if (list.size() % 3 != 0 || list.size() == 15) {
            HashMap<Integer, Integer> map = new HashMap<>();

            for (Integer card : list) {
                if (!map.containsKey(card)) {
                    map.put(card, 1);
                } else {
                    map.put(card, map.get(card) + 1);
                }
            }

            //先将 map 集合转换成 list
            List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>();

            entryList.addAll(map.entrySet());

            //再对 key 与 value 进行降序排序
            Collections.sort(entryList, new Comparator<Map.Entry<Integer, Integer>>() {
                @Override
                public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                    if (o1.getValue() == o2.getValue()) {
                        return o1.getKey() - o2.getKey();
                    }

                    return o1.getValue() - o2.getValue();
                }
            });

            List<Integer> threeType = new ArrayList<>();
            List<Integer> twoType = new ArrayList<>();

            Iterator iterator = entryList.iterator();

            while (iterator.hasNext()) {

                Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>) iterator.next();

                if (entry.getValue() == 3 && entry.getKey() != 14) {
                    threeType.add(entry.getKey());
                } else if (entry.getValue() == 2) {
                    twoType.add(entry.getKey());
                } else if (entry.getKey() == 14) {
                    return 0;
                }

            }

            if (threeType.size() == 0) {
                return 0;
            }

            if (threeType.size() == 1) {
                if (twoType.size() == 0) {
                    playerBean.setMaxCard(threeType.get(0));
                    return 30;
                } else {
                    playerBean.setMaxCard(threeType.get(0));
                    return 31;
                }
            }

            if (threeType.size() > 1 && twoType.size() == 0) {
                playerBean.setMaxCard(threeType.get(threeType.size() - 1));
                return 32;
            }

            if (threeType.size() != twoType.size()) {
                return 0;
            }

            if (judgeStraight(threeType, playerBean)) {
                if (twoType.size() != 0) {
                    if (isStraight(twoType)) {
                        return 34;
                    } else {
                        return 33;
                    }
                } else {
                    return 32;
                }
            }
        }

        //三同牌不带的情况
        else {
            if (list.size() == 3) {
                if (isSameKind(list)) {
                    playerBean.setMaxCard(list.get(0));
                    return 31;
                } else {
                    return 0;
                }

            } else if (isPlane(list, playerBean)) {
                return 32;
            }
        }
        return 0;
    }

    /**
     * 判断牌点数是否相同
     *
     * @param list .
     * @return .
     */
    protected boolean isSameKind(List<Integer> list) {

        int temp = list.get(0);

        for (int i = 1; i < list.size(); i++) {
            if (temp != list.get(i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断是否是 炸弹带一张
     *
     * @param list .
     * @return .
     */
    protected boolean isBoomWithSingle(List<Integer> list, PlayerBean playerBean) {

        int temp = list.get(0);
        list.remove(0);

        if (list.size() == 3) {
            if (removeDuplicate(list).size() == 1 && list.get(0) == 14) {
                playerBean.setMaxCard(list.get(0));

                list.add(0, temp);
                return true;
            }
        } else if (isSameKind(list)) {
            playerBean.setMaxCard(list.get(0));
            list.add(0, temp);
            return true;
        }

        list.add(0, temp);
        temp = list.get(list.size() - 1);
        list.remove(list.size() - 1);

        if (list.size() == 3) {
            if (removeDuplicate(list).size() == 1 && list.get(0) == 14) {
                playerBean.setMaxCard(list.get(0));
                list.add(temp);
                return true;
            }
        } else if (isSameKind(list)) {
            playerBean.setMaxCard(list.get(0));
            list.add(temp);
            return true;
        }

        list.add(temp);

        return false;
    }


    /**
     * 去重
     *
     * @param list .
     * @return .
     */
    protected List<Integer> removeDuplicate(List<Integer> list) {
        List<Integer> newList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {

            if (!newList.contains(list.get(i))) {
                newList.add(list.get(i));
            }

        }

        return newList;
    }

    /**
     * 判断是不是连续的牌
     *
     * @param list .
     * @return .
     */
    protected boolean isStraight(List<Integer> list) {

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) - i != list.get(0)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断是不是对子
     *
     * @param list .
     * @return .
     */
    protected boolean isPairs(List<Integer> list) {

        int num = removeDuplicate(list).size();

        if (num != list.size() / 2) {
            return false;
        }

        for (int i = 0; i < list.size() / 2; i++) {
            if (list.get(2 * i + 1) - list.get(2 * i) != 0) {
                return false;
            }
        }

        return true;
    }


    /**
     * 判断是否是飞机不带牌
     *
     * @param list .
     * @return .
     */
    protected boolean isPlane(List<Integer> list, PlayerBean playerBean) {

        //四带二的情况
        if (isSameKind(list.subList(0, 4))) {
            return false;
        } else if (isSameKind(list.subList(2, 6))) {
            return false;
        }


        if (judgeStraight(removeDuplicate(list), playerBean) && list.size() / 3 == removeDuplicate(list).size()) {
            return true;
        }

        return false;
    }


    /**
     * 将集合转换为小牌
     *
     * @param list .
     * @return .
     */
    protected List<Integer> convertToSmallList(List<Integer> list) {
        List<Integer> smallList = new ArrayList<>();

        int tempCard;

        for (Integer card : list) {
            tempCard = convertTo15(card);

            smallList.add(tempCard);
        }

        return smallList;
    }

    public static List<Integer> getList(String str) {

        List<Integer> list = new ArrayList<>();
        String[] data = str.split(",");
        for (int i = 0; i < data.length; i++) {
            list.add(Integer.valueOf(data[i].trim()));
        }

        return list;
    }
}














