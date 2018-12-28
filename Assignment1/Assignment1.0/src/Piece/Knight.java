package Piece;

import javax.swing.*;
import Logic.Player;

public class Knight extends Piece {

    private boolean alive;
    private Player player;
    private String name;
    private boolean first_move;

    public Knight(Player player){
        this.player = player;
        this.alive = true;
        name = "N";
    }

    @Override
    public String getName(){
        return name;
    }

    public String printName(){
        return "Knight";
    }

    public void setFirst_move(boolean first_move){
        this.first_move = first_move;
    }

    public Player getPlayer(){
        return this.player;
    }

    public boolean check_valid_move(Piece[][] spots, int srcX, int srcY, int destX, int destY){
        int stepX = Math.abs(destX - srcX);
        int stepY = Math.abs(destY - srcY);

        if( stepX == 2 && stepY == 1 ){
            return true;
        }

        else if( stepX == 1 && stepY == 2 ){
            return true;
        }

        return false;
    }

    public void setIcon() {
        if (getPlayer().IsWhite()) {
            icon = new ImageIcon(getClass().getResource("/images/knight_w.png"));
        } else {
            icon = new ImageIcon(getClass().getResource("/images/knight_b.png"));
        }
    }
}
