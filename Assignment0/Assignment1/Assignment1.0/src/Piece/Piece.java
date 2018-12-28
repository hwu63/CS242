package Piece;

import javax.swing.*;
import Logic.Player;

public abstract class Piece {

    private boolean alive;
    private Player player;
    private String name;
    private boolean first_move;
    public ImageIcon icon;

    public Player getPlayer(){
        return this.player;
    }

    public void setAlive(boolean alive){
        this.alive = alive;
    }

    public abstract String getName();

    public abstract String printName();

    public abstract void setFirst_move(boolean first_move);

    public abstract boolean check_valid_move(Piece [][] spots, int srcX, int srcY, int destX, int destY);

    public abstract void setIcon();
}
