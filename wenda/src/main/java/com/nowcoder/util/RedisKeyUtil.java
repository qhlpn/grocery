package com.nowcoder.util;

public class RedisKeyUtil {

    private static String SPLIT = ":";
    private static String LIKE = "LIKE";
    private static String DISLIKE = "DISLIKE";
    private static String EVENTQUEUE = "EVENTQUEUE";
    private static String FOLLOWER = "FOLLOWER";
    private static String FOLLOWEE = "FOLLOWEE";
    private static String TIMELINE = "TIMELINE";


    public static String getLikeKey(int entityType,int entityId) {
        return LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityType,int entityId) {
        return DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getEVENTQUEUE() {
        return EVENTQUEUE;
    }

    public static String getFOLLOWER(int entityType, int entityId) {
        return FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getFOLLOWEE(int userId, int entityType) {
        return FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }

    public static String getTimelineKey(int userId) {
        return TIMELINE + SPLIT + String.valueOf(userId);
    }

}
