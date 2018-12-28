package Piece;

import Logic.Player;
import javax.swing.*;

public class Bishop extends Piece {

    private boolean alive;
    private Player player;
    private String name;
    private boolean first_move;

    public Bishop(Player player) {
        this.player = player;
        this.alive = true;
        name = "B";
    }

    @Override
    public String getName(){
        return name;
    }

    public void setFirst_move(boolean first_move){
        this.first_move = first_move;
    }

    public String printName(){
        return "Bishop";
    }

    public Player getPlayer(){
        return this.player;
    }

    public boolean check_valid_move(Piece[][] spots, int srcX, int srcY, int destX, int destY) {


        //not diagonal
        if (Math.abs(destX - srcX) != Math.abs(destY - srcY)) {
            return false;
        }

        //check leap over
        //vars for easier loop
        int stepX, stepY;

        if (srcX < destX)
            stepX = 1;
        else
            stepX = -1;


        if (srcY < destY)
            stepY = 1;
        else
            stepY = -1;

        int j = srcY + stepY;
        for(int i = srcX + stepX; i != destX; i += stepX){
                if(i>8||j>8||i<0||j<0)
                    return false;
                else if(spots[i][j] != null)
                    return false;

                j += stepY;

        }

        return true;

    }

    public void setIcon() {
        if (getPlayer().IsWhite()) {
            icon = new ImageIcon(getClass().getResource("/images/bishop_w.png"));
        } else {
            icon = new ImageIcon(getClass().getResource("/images/bishop_b.png"));
        }
    }
}
