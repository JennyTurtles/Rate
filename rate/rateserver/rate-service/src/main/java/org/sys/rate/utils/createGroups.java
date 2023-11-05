package org.sys.rate.utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class createGroups {
    public static List<List<double []>> groups = new ArrayList<List<double []>>();
    public static double avg = 0;
    public static double[][] meanValueOfGroups;

    public static int allGroupsNums() {
        int sum = 0;
        for (List<double []> item : groups) {
            sum += item.size();
        }
        return sum;
    }

    public void isVerse(List<Integer> arr){
        List<List<double []>> groups_new = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++){
            for (List<double[]> item: groups)
            {
                if (item.size() == arr.get(i)) {
                    groups_new.add(item);
                    groups.remove(item);
                    break;
                }
            }
        }
        groups = groups_new;
    }

    List<List<double []>> temparr = new ArrayList<List<double []>>();
    public List<List<double []>> devideGroupsFixedNums(List<Integer> arr,List<Double> point,
                                                       Integer exChangeNums,Integer groupsNums,
                                                       Integer studentNums,List<double []> point_participant) {
        temparr.clear();
        List<Integer> arr_temp = new ArrayList<>();
        for (int i = 0; i < groupsNums; i++) {
            temparr.add(new ArrayList<>());
            arr_temp.add(arr.get(i));
        }
        groups.clear();
        avg = 0;
        meanValueOfGroups = new double[groupsNums][];
        int index = 0;
        int i = 0;
        int j = point_participant.size()-1;
        boolean flag = false;
        while (allGroupsNums() < studentNums) {
            if (temparr.get(index).size() < arr.get(index)) { //arr：每组人数
                if(flag){
//                    temparr.get(index).add(point_participant.get(point_participant.size() - 1 - i + 1));
                    temparr.get(index).add(point_participant.get(i));
                }
                if (temparr.get(index).size() < arr.get(index)){
                    if (flag)
                        i++;
                    temparr.get(index).add(point_participant.get(i));
                }
                if (temparr.get(index).size() < arr.get(index)) {
                    flag = false;
                    if (i < j) {
                        temparr.get(index).add(point_participant.get(j));
                        j--;
                    }
                }
                else {
                    flag = true;
                }
            }
            else {
                groups.add(temparr.get(index));
                for(int lll = 0;lll < groups.get(groups.size()-1).size();lll++){
                    groups.get(groups.size()-1).get(lll)[2] = groups.size() -1;
                }
                temparr.remove(index);
                arr.remove(index);
                index--;
                i--;
            }
            if (index == temparr.size() - 1) {
                Collections.reverse(temparr);
                Collections.reverse(arr);
            }
            if(temparr.size() > 0){
                index = (index + 1) % temparr.size();
            }else {
                isVerse(arr_temp);
                adjust_order();
                return groups;
            }
            i++;
        }
        isVerse(arr_temp);
        adjust_order();
        return groups;
    }

    public List<double[]> mean(Integer groupsNums) {
        List<double[]> meanValueOfGroups = new ArrayList<>();
        for (int i = 0; i < groupsNums; i++) {
            double[] temp = new double[4];
            double sum = 0;
            for (int k = 0; k < groups.get(i).size(); k++){
                sum += groups.get(i).get(k)[0];
            }
            temp[0] = sum / groups.get(i).size() - avg;//距离理想均值的差距
            temp[1] = groups.get(i).size();//每组长度
            temp[2] = i;//每组下标
            temp[3] = sum / groups.get(i).size();//每组均值
            meanValueOfGroups.add(temp);
        }
        Collections.sort(meanValueOfGroups, (a, b) -> Double.compare(b[0], a[0]));
        return meanValueOfGroups;
    }

    public static ArrayList<double[]> calD_ValueBetweenGroups(List<double[]> arr1, List<double[]> arr2) {
        List<double[]> D_ValueBetweenGroups = new ArrayList<>();
        for (int j = 0; j < arr1.size(); j++) {
            if (arr1.get(j)[3] != -1)
                continue;
            for (int k = 0; k < arr2.size(); k++) {
                if (arr2.get(k)[3] != -1)
                    continue;
                D_ValueBetweenGroups.add(new double[]{arr1.get(j)[0] - arr2.get(k)[0], j, k}); //两两之间相差的分数
            }
        }
        Collections.sort(D_ValueBetweenGroups, (a, b) -> Double.compare(b[0], a[0]));
        return (ArrayList<double[]>) D_ValueBetweenGroups;
    }

    public static void exChange(List<double[]> arr1, List<double[]> arr2, int idx1, int idx2) {
        double[] temp = arr1.get(idx1);
        arr1.set(idx1, arr2.get(idx2));
        arr2.set(idx2,temp);
        System.out.println("交换数字为：" + arr1.get(idx1)[0] + "and" + arr2.get(idx2)[0]);
    }

    public static double sqrtMean(int groupsNums, double[][] meanValueOfGroups, double avg) {
        double sum = 0;
        for(int i = 0; i < groupsNums; i++) {
            double temp = ((meanValueOfGroups[i][3] - avg) * (meanValueOfGroups[i][3] - avg));
            sum += temp;
        }
        double standMean = Math.sqrt(sum / groupsNums);
        return standMean;
    }

    public void printMeanValueOfGroups(double[][]  m){
        for (int jj = 0;jj<m.length;jj++){
            for (int mm = 0;mm<m[jj].length;mm++){
                System.out.print(m[jj][mm] + ",");
            }
            System.out.println();
        }
    }

    public List<List<double []>> createG(List<Double> point,Integer exChangeNums,Integer groupsNums,Integer studentNums){
        double standMean = 0;
        int i = 0;
        for (double items : point) {
            avg += items;
        }
        avg /= studentNums;
        System.out.println("理想均值：");
        System.out.println(avg);
        meanValueOfGroups = mean(groupsNums).toArray(new double[0][]);
        System.out.println("初始每组均值离理想均值差距meanValueOfGroups:");
        printMeanValueOfGroups(meanValueOfGroups);
        System.out.println("标准差：");
        standMean = sqrtMean(groupsNums,meanValueOfGroups,avg);
        System.out.println(standMean);
        if(standMean == 0){
            return groups;
        }
        for(int nums = 0; nums < exChangeNums; nums++){
            List<double[]> arr1 = groups.get((int) meanValueOfGroups[i][2]);
            List<double[]> arr2 = groups.get((int) meanValueOfGroups[groupsNums - i - 1][2]);
            ArrayList<double[]> D_ValueBetweenGroups = calD_ValueBetweenGroups(arr1, arr2);
            for(int k = 0; k < D_ValueBetweenGroups.size(); k++){//先找一遍有没有相等的
                if(D_ValueBetweenGroups.get(k)[0] == Math.abs(meanValueOfGroups[i][0] * meanValueOfGroups[i][1])){//能找到正好相差xx分的
                    int idx1 = (int)D_ValueBetweenGroups.get(k)[1];
                    int idx2 = (int)D_ValueBetweenGroups.get(k)[2];
                    exChange(arr1, arr2, idx1, idx2);
                    break;
                }
            }
            for(int k = 0; k < D_ValueBetweenGroups.size(); k++){
                if(D_ValueBetweenGroups.get(0)[0] < (meanValueOfGroups[i][0] * meanValueOfGroups[i][1])){//比最大差值还要大
                    exChange(arr1, arr2, (int)D_ValueBetweenGroups.get(0)[1], (int)D_ValueBetweenGroups.get(0)[2]);
                    break;
                } //比最小差值还小
                if(D_ValueBetweenGroups.get(D_ValueBetweenGroups.size() - 1)[0] > (meanValueOfGroups[i][0] * meanValueOfGroups[i][1])){
                    exChange(arr1, arr2, (int) D_ValueBetweenGroups.get(D_ValueBetweenGroups.size() - 1)[1], (int) D_ValueBetweenGroups.get(D_ValueBetweenGroups.size() - 1)[2]);
                    break;
                } //3(差值) 1(理想均值差距) -2 (差值) 3 -1 -2
                if(D_ValueBetweenGroups.get(k)[0] > (meanValueOfGroups[i][0] * meanValueOfGroups[i][1]) && D_ValueBetweenGroups.get(k + 1)[0] < (meanValueOfGroups[i][0] * meanValueOfGroups[i][1])){
                    double mid = meanValueOfGroups[i][0] * meanValueOfGroups[i][1];
                    double max_ = D_ValueBetweenGroups.get(k)[0];
                    double min_ = D_ValueBetweenGroups.get(k + 1)[0];
                    if(max_ == 0 && min_ != 0){
                        k = k + 1;
                    }else if(min_ != 0 && max_ != 0){
                        k = Math.abs(mid - max_) > Math.abs(mid - min_) ? k + 1 : k;
                    } else if(min_ == 0 && max_ == 0){
                        break;
                    }
                    exChange(arr1, arr2, (int) D_ValueBetweenGroups.get(k)[1], (int) D_ValueBetweenGroups.get(k)[2]);
                    break;
                }
            }
            System.out.println("第nums次交换:" + nums);
            meanValueOfGroups = mean(groupsNums).toArray(new double[0][]);
            System.out.println("每组均值离理想均值差距meanValueOfGroups:");
            printMeanValueOfGroups(meanValueOfGroups);
            standMean = sqrtMean(groupsNums,meanValueOfGroups,avg);
            System.out.println("标准差：");
            System.out.println(standMean);
            if(standMean == 0){
                break;
            }
        }
        return groups;
    }

    public static double findClosestNumber(List<double []> arr, double target, int order) { //二分查找最接近的成绩
        List<double []> unorder = new ArrayList<>();
        for (double [] per : arr){
            if (per[3] != order)
                unorder.add(per);
        }
        int left = 0;
        int right = unorder.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (unorder.get(mid)[0] == target) {
                return mid;
            } else if (unorder.get(mid)[0] > target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        // 当左边界大于右边界时，返回左边界
        return unorder.get(left)[0];
    }

    //将指定学生换回原来的组
    public void adjust_order(){
        for (int i = 0;i < groups.size(); i++) {  //item：当前遍历的组
            List<double[]> item = groups.get(i);
            for (int j = 0;j < item.size(); j++){
                double[] curr = item.get(j);  //curr: 当前组遍历的学生
                if (curr[3] == i || curr[3] == -1)
                    continue;
                int order = (int) curr[3];
                List<double[]> order_group = groups.get(order);  //order_group: curr本应该在的组
                order_group.sort((a, b) -> Double.compare(b[0], a[0]));
                double closest = findClosestNumber(order_group,curr[0],order);
                int index2 = -1;
                for (int k = 0; k < order_group.size(); k++){
                    if (order_group.get(k)[0] == closest){
                        index2 = k;
                        break;
                    }
                }
                exChange(item,order_group,j,index2);
            }
        }
    }
}
