package chess.piecesmoves;

import chess.*;

import java.util.HashSet;

public class BishopMoves {
    public static HashSet<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        int x = myPosition.getColumn();
        int y = myPosition.getRow();

        HashSet<ChessMove> moves = new HashSet<>();
        ChessPosition newPosition = myPosition;

        for(int delta = -Math.min(x, y); Math.max(x + delta, y + delta) <= 7 && Math.min(x + delta, y + delta) >= 0; delta++) {
            newPosition = new ChessPosition(y + delta, x + delta);
            if (board.getPiece(newPosition) == null || (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())){
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
            if (board.getPiece(newPosition) != null){
                break;
            }
        }
        for(int delta = 0; Math.max(x + delta, y - delta) <= 7  && Math.min(x + delta, y - delta) >= 0; delta++) {
            newPosition = new ChessPosition(y - delta, x + delta);
            if (board.getPiece(newPosition) == null || (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
            if (board.getPiece(newPosition) != null){
                break;
            }
        }
        for(int delta = 0; Math.max(x - delta, y + delta) <= 7  && Math.min(x - delta, y + delta) >= 0; delta++) {
            newPosition =  new ChessPosition(y + delta, x - delta);
            if (board.getPiece(newPosition) == null || (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
            if (board.getPiece(newPosition) != null){
                break;
            }
        }

        return moves;
    }
}