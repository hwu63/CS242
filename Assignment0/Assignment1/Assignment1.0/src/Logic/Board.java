package Logic;


import Piece.Piece;
import Piece.Hopper;
import Piece.King;
import Piece.Bishop;
import Piece.Cannon;
import Piece.Knight;
import Piece.Pawn;
import Piece.Queen;
import Piece.Rook;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Board {
    private Piece[][] spots = new Piece[8][8];
    private int[] whitelist = new int[16];
    private int[] blacklist = new int[16];
    private int[] KingPos = new int[]{04, 74};

    private int[] checkingPiece_W = new int[16];
    private int[] checkingPiece_B = new int[16];
    private Piece[] last_pieces = new Piece[2];
    public static Vector<Piece[]> moved_pieces = new Vector<>();
    public static Vector<int[]> Undos = new Vector<>();



    public Board(Player white, Player black, boolean normal_mode){

        Undos.removeAllElements();
        moved_pieces.removeAllElements();
        //initializing grids and lists
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(i == 0)
                    whitelist[j] = i*10+j;

                if(i == 7)
                    blacklist[j+8] = i*10+j;

                if(i == 1){
                    spots[i][j] = new Pawn(white);
                    whitelist[j+8] = i*10+j;

                } else if (i == 6){
                    spots[i][j] = new Pawn(black);
                    blacklist[j] = i*10+j;

                } else
                    this.spots[i][j] = null;

            }
        }



        //Bishop
        spots[0][2] = new Bishop(white);
        spots[0][5] = new Bishop(white);
        spots[7][2] = new Bishop(black);
        spots[7][5] = new Bishop(black);

        //Knight
        spots[0][1] = new Knight(white);
        spots[0][6] = new Knight(white);
        spots[7][1] = new Knight(black);
        spots[7][6] = new Knight(black);

        //Queen
        spots[0][3] = new Queen(white);
        spots[7][3] = new Queen(black);

        //King
        spots[0][4] = new King(white);
        spots[7][4] = new King(black);

        for(int i=0; i<16; i++){
            checkingPiece_B[i] = -1;
            checkingPiece_W[i] = -1;
        }

        if(normal_mode){
            //Rook
            spots[0][0] = new Rook(white);
            spots[0][7] = new Rook(white);
            spots[7][0] = new Rook(black);
            spots[7][7] = new Rook(black);


        }else{

            //switch rooks to custom piece

            //Hopper
            spots[0][0] = new Hopper(white);
            spots[7][0] = new Hopper(black);

            //Pao
            spots[0][7] = new Cannon(white);
            spots[7][7] = new Cannon(black);
        }
    }

    public Board(Board board){

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.spots[i][j] = board.spots[i][j];
            }
        }
        for(int i = 0; i < 16; i++){
            this.checkingPiece_W[i] = board.checkingPiece_W[i];
            this.checkingPiece_B[i] = board.checkingPiece_B[i];
            this.blacklist[i] = board.blacklist[i];
            this.whitelist[i] = board.whitelist[i];
        }

        for (int i=0; i<2; i++) {
            this.KingPos[0] = KingPos[0];
            this.KingPos[1] = KingPos[1];
        }


    }

    public Piece[][] getspots(){
        return spots;
    }

    public void setspots(Piece[][] spots){
        this.spots = spots;

    }

    public Integer getKingPos(Player player){

        if(player.IsWhite())
            return KingPos[0];
        else
            return KingPos[1];
    }

    public void setKingPos(Player player, Integer value){
        if(player.IsWhite())
            KingPos[0] = value;
        else
            KingPos[1] = value;
    }

    public int[] getplayerlist(Player player){
        if(player.IsWhite()){
            return whitelist;
        }else{
            return blacklist;
        }
    }

    public int[] getCheckingPiece(Player player){
        if(player.IsWhite()){
            return checkingPiece_B;
        }else{
            return checkingPiece_W;
        }
    }

    public Vector<int[]> possible_dest(int srcX, int srcY, Player player){
        Vector<int[]> ret = new Vector<>();

        for(int i = 0; i < 8; i++)
            for(int j=0; j < 8; j++)
                if(spots[srcX][srcY] !=null)
                    if(spots[srcX][srcY].check_valid_move(spots, srcX, srcY, i, j)
                            && spots[srcX][srcY] != null
                            && spots[srcX][srcY].getPlayer() == player
                            && !(spots[i][j] != null && spots[i][j].getPlayer() == player) ){
                        ret.add(new int[]{i, j});
                    }



        return ret;
    }

    public boolean inCheck(Player player){

        int kingX = getKingPos(player)/10;
        int kingY = getKingPos(player)%10;

        int[] pieceList = getplayerlist(player);
        int other_pieceX;
        int other_pieceY;
        boolean ret = false;

        int counter = 0;
        for(int i = 0; i < 16; i++){
            if(pieceList[i] >= 0){
                other_pieceX = pieceList[i]/10;
                other_pieceY = pieceList[i]%10;


                if(spots[other_pieceX][other_pieceY] !=null)
                    if(spots[other_pieceX][other_pieceY].check_valid_move(spots,
                            other_pieceX, other_pieceY, kingX, kingY)){
                    ret =  true;
                    //save to checking piece list
                    setcheckingPiece(player, counter++, pieceList[i]);
                }
            }
        }

        return ret;
    }

    public void printBoard() {

        //print the Logic.Board
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(i == 0 )
                    System.out.print(" "+ j +" ");

                else if(j == 0 )
                    System.out.print(" "+ i +" ");

                else if(spots[i-1][j-1] == null)
                    System.out.print("__ ");

                else
                    System.out.print(spots[i-1][j-1].getPlayer().getName() + spots[i-1][j-1].getName()+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void setcheckingPiece(Player player, int index, int value) {

        if(player.IsWhite())
            this.checkingPiece_B[index] = value;
        else
            this.checkingPiece_W[index] = value;
    }

    public void move(int[] positions, Player player) throws IOException{
        int srcX = positions[0];
        int srcY = positions[1];
        int destX = positions[2];
        int destY = positions[3];

        //do nothing when go to self
        if(srcX == destX && srcY == destY)
            throw new IOException("Not going to move on self ):");


        //invalid moves
        if(spots[srcX][srcY] == null ){
            throw new IOException("No piece to be moved!");
        }

        if(spots[srcX][srcY].getPlayer() != player ){
            throw new IOException("Wrong Player!");
        }

        if(spots[destX][destY] != null && spots[destX][destY].getPlayer() == player ){
            throw new IOException("Can't eat pieces of yourself!");
        }


        boolean actuallyMove = spots[srcX][srcY].check_valid_move(spots, srcX, srcY, destX, destY);

        if(!actuallyMove){
            throw new IOException("Invalid move of "+spots[srcX][srcY].printName()+"!!");
        }
        else{
            //Valid move

            last_pieces[0] = spots[srcX][srcY];
            last_pieces[1] = spots[destX][destY];

            Undos.add(new int[]{srcX, srcY, destX, destY});


            if(spots[srcX][srcY].getName().equals("K")) {
                setKingPos(player, destX * 10 + destY);
            }

            if(spots[destX][destY]!=null && spots[destX][destY].getName().equals("K")) {
                setKingPos(spots[destX][destY].getPlayer(), -1);
            }

            //if is Pawn
            if(spots[srcX][srcY].getName().equals("P")){
                spots[srcX][srcY].setFirst_move(false);
            }

            moved_pieces.add(new Piece[]{ spots[srcX][srcY], spots[destX][destY]});

            dest_to_src(player, destX, destY, srcX, srcY);

            //ate pieces
            if(spots[destX][destY] != null){
                if(!player.IsWhite()) {
                    for (int i = 0; i < whitelist.length; i++) {
                        if (whitelist[i] == destX*10 + destY){
                            whitelist[i] = -1;
                            break;
                        }
                    }
                }else{
                    for (int i = 0; i < blacklist.length; i++) {
                        if (blacklist[i] == destX*10 + destY){
                            blacklist[i] = -1;
                            break;
                        }
                    }
                }
            }

            spots[destX][destY] = spots[srcX][srcY];
            spots[srcX][srcY] = null;

        }

    }

    public int[] undo( Player player){

        //undo as well as return undid positions;

        if(Undos.size()==0){
            return new int[]{-1,-1,-1,-1};
        }

        int [] positions = Undos.elementAt(Undos.size()-1);
        Undos.remove(Undos.size()-1);

        int srcX = positions[0];//the position moved from
        int srcY = positions[1];
        int destX = positions[2];// the position moved to
        int destY = positions[3];


        spots[srcX][srcY] = last_pieces[0];//original moving piece

        spots[destX][destY] = last_pieces[1];//original eaten piece or empty place

        //if K is the moving piece
        if(last_pieces[0].getName().equals("K")) {
            setKingPos(player, srcX * 10 + srcY);
        }

        //if K is eaten
        if(last_pieces[1]!=null && last_pieces[0].getName().equals("K")) {
            setKingPos(last_pieces[1].getPlayer(), -1);
        }

        //if is Pawn and it is in the first_move row (say row 1 and 6)
        if(last_pieces[0].getName().equals("P") ){
            if((srcX == 1 && player.IsWhite())   ||
                    (srcX == 6 && !player.IsWhite())     )
                last_pieces[0].setFirst_move(true);
        }

        //reversing back position tracking
        //refactored
        dest_to_src(player, srcX, srcY, destX, destY);

        if(last_pieces[1] != null){
            if(!player.IsWhite()) {
                for (int i = 0; i < whitelist.length; i++) {
                    if (whitelist[i] == -1){
                        whitelist[i] = destX*10 + destY;
                        break;
                    }
                }
            }else{
                for (int i = 0; i < blacklist.length; i++) {
                    if (blacklist[i] == -1){
                        blacklist[i] = destX*10 + destY;
                        break;
                    }
                }
            }
        }



        moved_pieces.remove(moved_pieces.size()-1);
        if(moved_pieces.size()>=1)
            last_pieces = moved_pieces.elementAt(moved_pieces.size()-1);

        return positions;

    }

    private void dest_to_src(Player player, int srcX, int srcY, int destX, int destY) {
        if(player.IsWhite()) {
            for (int i = 0; i < whitelist.length; i++) {
                if (whitelist[i] == destX*10 + destY){
                    whitelist[i] = srcX*10 + srcY;
                    break;
                }
            }
        }else{
            for (int i = 0; i < blacklist.length; i++) {
                if (blacklist[i] == destX*10 + destY){
                    blacklist[i] = srcX*10 + srcY;
                    break;
                }
            }
        }
    }


}