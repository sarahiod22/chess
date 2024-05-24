package chess.piecesmoves;

import chess.*;
import java.util.HashSet;

public class CommonMoves {
    public static HashSet<ChessMove> checkMovements(ChessBoard board, ChessPosition currentPosition, int colDirection, int rowDirection) {
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

    public static HashSet<ChessMove> checkPositions(ChessPosition[] possiblePositions, ChessBoard board, ChessPosition myPosition) {
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
