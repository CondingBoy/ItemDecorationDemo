package com.wang.administrator.itemdecorationdemo.entity;

import com.github.promeg.pinyinhelper.Pinyin;

/**
 * 作者： WangWei.
 * 时间： 2016/12/5 0005 上午 11:05
 * 描述：
 */
public class CityBean {
    private String tag;
    private String cityName;
    public CityBean(String cityName){
        char c = cityName.charAt(0);
        if(Pinyin.isChinese(c)){
            String s = Pinyin.toPinyin(c);
            tag=s.substring(0,1);
            this.cityName=cityName;
        }
    }
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
