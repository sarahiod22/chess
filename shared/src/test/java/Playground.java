import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import org.junit.jupiter.api.Test;

public class Playground {
    @Test
    public void test() {
        ChessBoard board = new ChessBoard();

        {

            var x = 2;
            var y = 2;
            for(int delta = -Math.min(x, y); Math.max(x + delta, y + delta) < 8 && Math.min(x + delta, y + delta) >= 0; delta++) {
                board.addPiece(new ChessPosition(y + delta, x + delta), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
            }
            for(int delta = 0; Math.max(x + delta, y - delta) < 8  && Math.min(x + delta, y - delta) >= 0; delta++) {
                board.addPiece(new ChessPosition(y - delta, x + delta), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
            }
            for(int delta = 0; Math.max(x - delta, y + delta) < 8  && Math.min(x - delta, y + delta) >= 0; delta++) {
                board.addPiece(new ChessPosition(y + delta, x - delta), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
            }
        }

        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                System.out.print(board.getPiece(new ChessPosition(y, x)) != null ? "o" : "-");
            }
            System.out.println();
        }
    }
}
