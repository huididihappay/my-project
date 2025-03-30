package com.xk;

import org.testng.TestNG;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ParallelTestNGExecutorbak {
    public static void main(String[] args) {
        // 获取测试资源路径
        //String testResourcePath = "src/test/resources/";
        // 根据执行环境选择路径
        // 动态获取测试类路径
        String testClasspath = System.getProperty("maven.test.classpath");
        if (testClasspath == null) {
            // 开发环境直接运行
            testClasspath = Arrays.stream(System.getProperty("java.class.path")
                            .split(File.pathSeparator))
                    .filter(path -> path.contains("test-classes"))
                    .collect(Collectors.joining(File.pathSeparator));
        }

        // 设置测试资源路径
        String testResourcePath = testClasspath.contains("test-classes")
                ? "target/test-classes/"
                : "src/test/resources/";


        // 定义要执行的XML文件列表
        List<String> group1 = new ArrayList<>();
        group1.add(testResourcePath + "testcase1.xml");
        group1.add(testResourcePath + "testcase2.xml");
        group1.add(testResourcePath + "testcase3.xml");
        group1.add(testResourcePath + "testcase4.xml");
        group1.add(testResourcePath + "testcase5.xml");
        group1.add(testResourcePath + "testcase6.xml");
        group1.add(testResourcePath + "testcase7.xml");
        group1.add(testResourcePath + "testcase8.xml");
        group1.add(testResourcePath + "testcase9.xml");
        group1.add(testResourcePath + "testcase10.xml");


        // 创建固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        int len = group1.size();
        // 提交每个XML文件到线程池执行
        int count = 0;
        List<String> xmlFileList = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            //判断循环5次加，给线程池执行，或者最后一个循环
            if (count == 5 || i == len - 1) {
                //如果没有xml文件，则跳过
                if(!xmlFileList.isEmpty()) {
                    executorService.submit(() -> {
                        TestNG testNG = new TestNG();
                        List<String> suites = new ArrayList<>();
                        suites.addAll(xmlFileList);
                        testNG.setTestSuites(suites);
                        try {
                            testNG.run();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    //清空xmlFileList
                    xmlFileList.clear();
                }
                //重新计数
                count = 0;
            } else {
                xmlFileList.add(group1.get(i));
                count++;
            }

        }
        // 优雅关闭线程池
        executorService.shutdown();
    }
}