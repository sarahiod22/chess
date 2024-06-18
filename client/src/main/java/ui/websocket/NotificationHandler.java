package ui.websocket;

import chess.InvalidMoveException;
import websocket.messages.*;
import websocket.messages.Error;

public interface NotificationHandler {
    void notify(Notification notification);
    void warn(Error error);
    void loadGame(LoadGame loadGame);
}
