package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String visitorName, Session session, Integer gameId) {
        var connection = new Connection(visitorName, session, gameId);
        connections.put(visitorName, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcastButRoot(String excludeVisitorName, ServerMessage notification, Integer gameId) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.visitorName.equals(excludeVisitorName) && Objects.equals(c.gameId, gameId)) {
                    c.send(new Gson().toJson(notification));
                }
            } else {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }

    public void broadcastToRoot(String rootVisitorName, ServerMessage notification, Integer gameId) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.visitorName.equals(rootVisitorName) && Objects.equals(c.gameId, gameId)) {
                    c.send(new Gson().toJson(notification));
                }
            } else {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }

    public void broadcastToAll(ServerMessage notification, Integer gameId) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (Objects.equals(c.gameId, gameId)) {
                    c.send(new Gson().toJson(notification));
                }
            } else {
                removeList.add(c);
            }
        }
        // Clean up any connections that were left open.
//        for (var c : removeList) {
//            connections.remove(c.visitorName);
//        }
    }
}