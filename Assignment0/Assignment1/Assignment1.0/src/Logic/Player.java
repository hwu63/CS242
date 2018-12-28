package Logic;

import java.util.Vector;

public class Player {
    private String color;
    public int win;

    public Player(String color){
        this.color = color;
    }


    public boolean IsWhite() {
        if (this.color == "White")
            return true;
        return false;
    }

    public String getName(){
        if (this.color == "White")
            return "w";
        else
            return "b";
    }

    public String printName(){
            return color;
    }

}
