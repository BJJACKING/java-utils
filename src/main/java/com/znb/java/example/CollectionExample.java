package com.znb.java.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangnaibin
 * @Date 2015-12-18 下午5:33
 */
public class CollectionExample {

    // map遍历
    public void bianli() {
        Map<String, String> map = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }

        for (String key : map.keySet()) {
            System.out.println(key);
        }
    }

    // list 排序
    public void listSort() {
        List<Integer> list = new ArrayList<Integer>();
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return 0;
            }
        });
    }
}
