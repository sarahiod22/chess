package chess.piecesmoves;

import chess.*;
import java.util.HashSet;

public class KnightMoves {
    public static HashSet<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        int x = myPosition.getColumn();
        int y = myPosition.getRow();

        ChessPosition[] possiblePositions = new ChessPosition[] {
                new ChessPosition(y+1, x+2),
                new ChessPosition(y+2, x+1),
                new ChessPosition(y+2, x-1),
                new ChessPosition(y+1, x-2),
                new ChessPosition(y-1, x+2),
                new ChessPosition(y-2, x+1),
                new ChessPosition(y-2, x-1),
                new ChessPosition(y-1, x-2),
        };

        HashSet<ChessMove> validPositions = new HashSet<>();

        for(ChessPosition position : possiblePositions) {
            if (position.getRow() < 1 || position.getRow() > 8 || position.getColumn() < 1 || position.getColumn() > 8)
                continue;

            if (board.getPiece(position) == null || board.getPiece(myPosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                validPositions.add(new ChessMove(myPosition, position, null));
            }
        }

        return validPositions;
    }
}
