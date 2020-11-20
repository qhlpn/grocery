package com.example.cleandata.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author QiuHongLong
 */
public class LruUtils<K, V> extends LinkedHashMap<K, V> {

    private int maxSize;

    public LruUtils(int maxSize) {
        // initialCapacity 容量
        // loadFactor      负载因子
        // accessOrder     排序模式, true 为访问, false 为插入
        super((int) Math.ceil(maxSize / 0.75) + 1, 0.75f, true);
        this.maxSize = maxSize;
    }

    /**
     * 钩子方法，Map 插入元素时调用
     *
     * @param eldest 当前最老的 Entry
     * @return 是否删除最老的 Entry
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        // 将不会扩容
        return size() > maxSize;
    }

}
