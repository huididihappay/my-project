package com.xk;

import org.testng.TestNG;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelTestNGExecutor {
    public static void main(String[] args) {
        // 根据执行环境选择路径
        String testResourcePath = System.getProperty("maven.execution.id") != null
                ? "target/test-classes/"  // 打包后执行
                : "src/test/resources/";   // 开发环境直接运行

        List<String> xmlFiles = new ArrayList<>();
        xmlFiles.add(testResourcePath + "testcase1.xml");
        xmlFiles.add(testResourcePath + "testcase2.xml");

        ExecutorService executorService = Executors.newFixedThreadPool(xmlFiles.size());

        for (String xmlFile : xmlFiles) {
            executorService.submit(() -> {
                // 创建TestNG实例
                TestNG testNG = new TestNG();
                List<String> suites = new ArrayList<>();
                // 添加测试套件
                suites.add(xmlFile);
                // 设置测试套件
                testNG.setTestSuites(suites);
                // 执行测试
                testNG.run();
            });
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}