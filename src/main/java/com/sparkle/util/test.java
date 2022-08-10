package com.sparkle.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class test {

    private static Map<String, Long> fileLineNums = new HashMap<>();

    public static void main(String[] args) {
        File dir = new File("D:\\Git\\Fine-Analysis");
        traverse(dir);
        //map.entrySet()转换成list
        List<Map.Entry<String, Long>> list = new ArrayList<>(fileLineNums.entrySet());
        //降序 比较器
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        long totalLine = 0;
        for (Map.Entry<String, Long> mapping : list) {
            System.out.println(mapping.getKey() + " " + mapping.getValue());
            totalLine += mapping.getValue();
        }
        System.out.println(list.size());
        System.out.println(totalLine);
        System.out.println(totalLine / list.size());
    }

    private static void traverse(File file) {
        File[] files = file.listFiles();
        for (File f : files) {
            //目录 递归该目录下的文件
            if (f.isDirectory()) {
                traverse(f);
            }
            if (f.isFile()) {
                String fileName = f.getAbsolutePath();
                if (fileName.endsWith(".java")) {
                    fileLineNums.put(fileName, getFileLineNum(fileName));
                }
            }
        }
    }

    public static long getFileLineNum(String filePath) {
        try (
                Stream<String> stream = Files.lines(Paths.get(filePath))
        ) {
            return stream.count();
        } catch (IOException e) {
            return -1;
        }
    }

}