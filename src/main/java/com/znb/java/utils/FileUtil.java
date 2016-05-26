package com.znb.java.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author zhangnaibin@xiaomi.com
 * @time 2016-01-18 下午4:48
 */
public class FileUtil {

    // 获取目录下part-r-开头的所有文件
    public File[] listFiles(String inputDir) {
        File dir = new File(inputDir);
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("part-r-");
            }
        });
        return files;
    }

    public static void main(String[] args) {
        System.out.println("znb");
    }
 }
