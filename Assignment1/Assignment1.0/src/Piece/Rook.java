package Piece;
import Logic.Player;

import javax.swing.*;

public class Rook extends Piece {

    private boolean alive;
    private Player player;
    private String name;
    private boolean first_move;

    public Rook(Player player){
        this.player = player;
        this.alive = true;
        name = "R";
    }

    @Override
    public String getName(){
        return name;
    }

    public String printName(){
        return "Rook";
    }

    public void setFirst_move(boolean first_move){
        this.first_move = first_move;
    }

    public Player getPlayer(){
        return this.player;
    }

    public boolean check_valid_move(Piece[][] spots, int srcX, int srcY, int destX, int destY){

        //check straight
        if(srcX != destX && srcY != destY){
            return false;
        }

        int step;//for loop
        if(srcY != destY){
            //check horizontally if there is piece

            if(srcY < destY)
                step = 1;
            else
                step = -1;

            for(int i = srcY + step; i != destY; i += step){
                if(spots[destX][i] != null)
                    return false;
            }
        }

        if(srcX != destX){
            //check vertically if there is piece

            if(srcX < destX)
                step = 1;
            else
                step = -1;

            for(int i = srcX + step; i != destX; i += step){
                if(spots[destY][i] != null)
                    return false;
            }

        }

        return true;
    }

    public void setIcon() {
        if (getPlayer().IsWhite()) {
            icon = new ImageIcon(getClass().getResource("/images/rook_w.png"));
        } else {
            icon = new ImageIcon(getClass().getResource("/images/rook_b.png"));
        }
    }
}
