
package server.websocket;

import com.google.gson.Gson;
import dataaccess.SQLAuthDao;
import dataaccess.SQLGameDao;
import dataaccess.SQLUserDao;
import dataaccess.exceptions.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final SQLUserDao userDao = new SQLUserDao();
    private final SQLGameDao gameDao = new SQLGameDao();
    private final SQLAuthDao authDao = new SQLAuthDao();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws ResponseException, IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CONNECT -> connect(new Gson().fromJson(message, Connect.class), session);
            case MAKE_MOVE -> makeMove(new Gson().fromJson(message, MakeMove.class), session);
            case LEAVE -> leave(new Gson().fromJson(message, Leave.class), session);
            case RESIGN -> resign(new Gson().fromJson(message, Resign.class),session);
        }
    }


    private void connect(Connect connect, Session session) {
    }

    private void makeMove(MakeMove makeMove, Session session) {
    }

    private void leave(Leave leave, Session session){

    }

    private void resign(Resign resign, Session session){}


}
