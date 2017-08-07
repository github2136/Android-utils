package com.github2136.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 集合工具类
 */
public class CollectionsUtil {
    public static boolean isEmpty(List list) {
        return (list == null || list.isEmpty());
    }

    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(Map map) {
        return (map == null || map.isEmpty());
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(Set set) {
        return (set == null || set.isEmpty());
    }

    public static boolean isNotEmpty(Set set) {
        return !isEmpty(set);
    }

    public static <T> boolean isEmpty(T[] t) {
        return (t == null || t.length == 0);
    }

    public static <T> boolean isNotEmpty(T[] t) {
        return !isEmpty(t);
    }
}