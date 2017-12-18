package com.qianlu.cache;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

/**
 * websocket session 链接缓存
 *
 * @author Qianlu
 * @date 2017/12/11 11:20
 **/
public class WsSessionCache {

    private static WsSessionCache instance = new WsSessionCache();

    private static Map<String, Session> webSocketSessionMap;

    private WsSessionCache() {
        webSocketSessionMap = new HashMap<>(1000);
    }

    public static WsSessionCache getInstance() {
        return instance;
    }

    public static Map<String, Session> getWebSocketSessionMap() {
        return webSocketSessionMap;
    }

    public static Session getWsSessionByUsername(String username) {
        if (username != null) {
            return webSocketSessionMap.get(username);
        }
        return null;
    }

    public static void setWsSession(String username, Session session) {
        webSocketSessionMap.put(username, session);
    }

    public static void removeWsSession(String username) {
        webSocketSessionMap.remove(username);
    }

    public static Integer size() {
        return webSocketSessionMap.size();
    }
}
