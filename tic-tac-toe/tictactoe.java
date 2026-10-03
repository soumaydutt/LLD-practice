import java.util.*;

//block 1
enum PieceType{
    O,
    X
}

abstract class PlayingPiece{
    private final PieceType pieceType;
    public PlayingPiece(PieceType pieceType){
        this.pieceType=pieceType;
    }
    public PieceType getPieceType(){
        return pieceType;
    }
}
class PlayingPieceX extends PlayingPiece{
    public PlayingPieceX(){
        super (PieceType.X);
    }
}
class PlayingPieceO extends PlayingPiece{
    public PlayingPieceO(){
        super(PieceType.O);
    }
}
public class tictactoe{
    public static void main (String[] args){
        PlayingPiece pieceX=new PlayingPieceX();
        PlayingPiece pieceO= new PlayingPieceO();
        System.out.println("Piece 1 type:"+pieceX.getPieceType());
        System.out.println("Piece 2 type:"+pieceO.getPieceType());
    }
}