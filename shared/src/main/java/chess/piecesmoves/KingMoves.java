package chess.piecesmoves;

import chess.*;
import java.util.HashSet;

public class KingMoves {
    public static HashSet<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        int x = myPosition.getColumn();
        int y = myPosition.getRow();

        ChessPosition[] possiblePositions = new ChessPosition[] {
                new ChessPosition(y, x+1),
                new ChessPosition(y, x-1),
                new ChessPosition(y+1, x),
                new ChessPosition(y-1, x),
                new ChessPosition(y+1, x+1),
                new ChessPosition(y+1, x-1),
                new ChessPosition(y-1, x+1),
                new ChessPosition(y-1, x-1),
        };

        HashSet<ChessMove> validPositions = new HashSet<>();

        for(ChessPosition position : possiblePositions) {
            if(position.getRow() < 1 || position.getRow() > 8 || position.getColumn() < 1 || position.getColumn() > 8) continue;

            if(board.getPiece(position) == null || board.getPiece(myPosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, position, null));
            }
        }

        return validPositions;
    }
}
