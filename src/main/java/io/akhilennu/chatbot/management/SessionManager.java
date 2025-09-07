package io.akhilennu.chatbot.management;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {

    // Map to hold all sessions by sessionId
    private final Map<String, Map<String, String>> sessionStore = new ConcurrentHashMap<>();

    // Get the map for a session (or create if not present)
    public Map<String, String> getSessionMap(String sessionId) {
        return sessionStore.computeIfAbsent(sessionId, id -> new ConcurrentHashMap<>());
    }

    // Set a key-value pair for a session
    public void put(String sessionId, String key, String value) {
        getSessionMap(sessionId).put(key, value);
    }

    // Retrieve a value by key for a session
    public String get(String sessionId, String key) {
        return getSessionMap(sessionId).get(key);
    }

    // Clear all data for a session
    public void clearSession(String sessionId) {
        sessionStore.remove(sessionId);
    }

    public void clearAllSessions() {
        sessionStore.clear();
    }
}
