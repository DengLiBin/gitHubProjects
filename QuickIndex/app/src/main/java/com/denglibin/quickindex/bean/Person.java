package com.denglibin.quickindex.bean;

import com.denglibin.quickindex.util.PinyinUtils;

import java.util.Comparator;

/**
 * Created by DengLibin on 2016/3/8.
 */
public class Person implements Comparable<Person> {
    private String name;
    private String pinyin;
    public Person(String name){
        super();
        this.name=name;
        this.pinyin= PinyinUtils.getPinyin(name);
    }

    public void setPinyin(String name) {
        this.pinyin = PinyinUtils.getPinyin(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
}

    public String getPinyin() {
        return pinyin;
    }

    //按拼音排序
    @Override
    public int compareTo(Person another) {
        return this.pinyin.compareTo(another.pinyin);
    }
}
