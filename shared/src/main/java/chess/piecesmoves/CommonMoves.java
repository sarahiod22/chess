package chess.piecesmoves;

import chess.*;
import java.util.HashSet;

public class CommonMoves {
    public static HashSet<ChessMove> checkDiagonal(ChessBoard board, ChessPosition currentPosition, int colDirection, int rowDirection) {
        HashSet<ChessMove> validPositions = new HashSet<>();
        int x = currentPosition.getColumn();
        int y = currentPosition.getRow();

        for(int delta = 1; Math.max(x + delta * colDirection, y + delta * rowDirection) <= 8  && Math.min(x + delta * colDirection, y + delta * rowDirection) >= 1; delta++) {
            ChessPosition newPosition = new ChessPosition(y + delta * rowDirection, x + delta * colDirection);

            if(board.getPiece(newPosition) == null || board.getPiece(currentPosition).getTeamColor() != board.getPiece(newPosition).getTeamColor()) {
                validPositions.add(new ChessMove(currentPosition, newPosition, null));
            }

            if (board.getPiece(newPosition) != null) {
                break;
            }
        }

        return validPositions;
    }
}
