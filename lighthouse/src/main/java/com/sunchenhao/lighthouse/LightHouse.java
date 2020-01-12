package com.sunchenhao.lighthouse;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: 孙晨昊
 * @date: 2020/1/11
 **/
public class LightHouse {
    private static class Holder{
        private static final LightHouse INSTANCE = new LightHouse();
    }

    private LightHouse() {
    }

    public static LightHouse getInstance() {
        return Holder.INSTANCE;
    }

    private Map<String, Class> mActivityClassMap = new HashMap<>();

    public void addActivityClassMap(Map<String, Class> map) {
        mActivityClassMap.putAll(map);
    }

    public Class getActivityClass(String path) {
        return mActivityClassMap.get(path);
    }

}
