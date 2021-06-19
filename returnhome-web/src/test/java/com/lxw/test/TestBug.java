package com.lxw.test;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.runners.JUnit4;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

@SpringBootTest
@RunWith(JUnit4.class)
public class TestBug {

    /**
     * 判断三角形具体的类型
     */
    public void judge(int a,int b ,int c) {
        if(a<1||a>100||b<1||b>100||c<1||c>100) {
            throw new ArithmeticException("三角形边长有误");
        }
        if (a + b >c && a + c >b && b + c >a) {// 判断是否三角形
            // 判断三角形类型
            if (a == b && b == c && c == a) {
                System.out.println("等边三角形");
            } else if (a == b || a == c || b == c) {
                System.out.println("等腰三角形");
            }else{
                System.out.println("普通三角形");
            }
        }else {
            System.out.println("该测试用例非三角形");
        }
    }


    @Test
    public void test1() throws IOException {
        //IO流读入数据用例
        String filePath = "src\\test\\java\\com\\lxw\\test\\测试用例一.txt";
        File file = new File(filePath);
        InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
        BufferedReader bf = new BufferedReader(inputReader);
        String str;
        int i = 0;
        int[] date;
        System.out.println("---普通测试开始---");
        while ((str = bf.readLine()) != null && !str.equals("")) {
            i ++;
            if(i==14){
                System.out.println("---健壮测试开始---");
            }else if(i==20){
                System.out.println("---后面是健壮最坏情况测试---");
            }
            //字符串数组转换成整形数组
            date = Arrays.asList(str.split(" ")).stream().mapToInt(Integer::parseInt).toArray();
            try {
                System.out.print("当前是第"+i+"个测试用例: "+str+" 输出结果: ");
                judge(date[0],date[1],date[2]);
            } catch (ArithmeticException e) {
                System.out.println("异常: "+e.getMessage());
            }
        }

    }



    @Test
    public void test2() throws IOException {
        //IO流读入数据用例
        String filePath = "src\\test\\java\\com\\lxw\\test\\测试用例二.txt";
        File file = new File(filePath);
        InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
        BufferedReader bf = new BufferedReader(inputReader);
        String str;
        int i = 0;
        int[] date;
        System.out.println("---普通测试开始---");
        while ((str = bf.readLine()) != null && !str.equals("")) {
            i ++;
            //字符串数组转换成整形数组
            date = Arrays.asList(str.split(" ")).stream().mapToInt(Integer::parseInt).toArray();
            if(i==14){
                System.out.println("---健壮测试开始---");
            }else if(i==20){
                System.out.println("---后面是健壮最坏情况测试---");
            }

            try {
                System.out.print("当前是第"+i+"个测试用例: "+str+" 输出结果：");
                int[] ints = judgeDate(date);
                System.out.print(ints[0]+"-"+ints[1]+"-"+ints[2]);
            } catch (Exception e) {
                System.out.print("日期格式有误");
            }
            System.out.println();
        }
    }


    /**
     * 判断日期
     */
    public int[] judgeDate(int[] date) throws Exception {
        //月份处理
        int[] a1 = new int[]{1, 3, 5, 7, 8, 10, 12}; //12要特殊判断一下 可能会到下一年去
        int[] a2 = new int[]{4, 6, 9, 11};

        if (date[2] < 1 || date[2] > 31 || date[1] < 1 || date[1] > 12 || date[0] < 1912 || date[0] > 2050){
            throw new Exception();
        }

        //如果是大月
        if (ArrayUtils.contains(a1, date[1])) {
            //12月份
            if (date[1] == 12) {
                if (date[2] < 31) {
                    date[2]++;
                } else if(date[2] == 31) {
                    date[2] = 1;
                    date[1] = 1;
                    date[0]++;
                }else {
                    throw new Exception("日期格式有误");
                }
            } else {
                if (date[2] < 31) {
                    date[2]++;
                } else if(date[2] == 31){
                    date[2] = 1;
                    date[1]++;
                }else{
                    throw new Exception("日期格式有误");
                }
            }
            return date;
        }

        //如果是小月
        if (ArrayUtils.contains(a2, date[1])) {
            if (date[2] < 30) {
                date[2]++;
            } else if(date[2] == 30){
                date[2] = 1;
                date[1]++;
            }else {
                throw new Exception("日期有误");
            }
            return date;
        }

        //如果是2月
        if (date[1] == 2) {
            //如果是闰年的2月(有29天)
            if (date[0] % 400 == 0 || (date[0] % 100 != 0 && date[0] % 4 == 0)) {
                if (date[2] <= 28) {
                    date[2]++;
                } else if(date[2] ==29) {
                    date[2] = 1;
                    date[1] = 3;
                }else {
                    throw new Exception("日期格式有误");
                }
            } else {
                if (date[2] < 28) {
                    date[2]++;
                } else if(date[2]==28){
                    date[2] = 1;
                    date[1] = 3;
                }else{
                    throw new Exception("日期格式有误");
                }
            }
            return date;
        }

        return date;
    }


//    @Test
//    public void test3(){
//
//        int locks, stocks, barrels;
//        double lockPrice, stockPrice, barrelPrice;
//        int totalLocks, totalStocks, totalBarrels;
//        double lockSales, stockSales, barrelSales;
//        double sales, commission;
//
//        lockPrice = 45.0;
//        stockPrice = 30.0;
//        barrelPrice = 25.0;
//
//        totalLocks = 0;
//        totalStocks = 0;
//        totalBarrels = 0;
//
//        System.out.println("Enter locks ");
//        Scanner sc = new Scanner(System.in);
//        locks = sc.nextInt();
//        while(locks != -1)
//        {
//            System.out.println("Enter stocks and barrels:");
//            scanf("%d,%d", &stocks, &barrels);
//            totalLocks += locks;
//            totalStocks += stocks;
//            totalBarrels += barrels;
//            printf("Enter locks:\n");
//            scanf("%d",&locks);
//        }
//
//        lockSales = lockPrice * totalLocks;
//        stockSales = stockPrice * totalStocks;
//        barrelSales = barrelPrice * totalBarrels;
//        sales = lockSales + stockSales + barrelSales;
//
//        if (sales > 1800.0)
//        {
//            commission  = 0.10 * 1000;
//            commission += 0.15 * 800;
//            commission += 0.20 * (sales - 1800);
//        }
//        else if (sales > 1000.0)
//        {
//            commission  = 0.10 * 1000;
//            commission += 0.15 * (sales - 1000);
//        }
//        else
//        {
//            commission  = 0.10 * sales;
//        }
//
//        printf("Commission is $%.2f\n", commission);
//
//    }




}




