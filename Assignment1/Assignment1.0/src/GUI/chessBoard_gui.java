package GUI;

import Logic.Game;
import Logic.Player;
import Logic.Board;
import java.util.Vector;

import Piece.Piece;
import Piece.Hopper;
import Piece.King;
import Piece.Bishop;
import Piece.Cannon;
import Piece.Knight;
import Piece.Pawn;
import Piece.Queen;
import Piece.Rook;

import sun.rmi.rmic.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;


public class chessBoard_gui extends JPanel implements Constants {

    private static JFrame f = new JFrame("ChessBoard");

    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JButton[][] buttons = new JButton[8][8];

    //private JPanel subpanel=  new JPanel();
    private JPanel chessBoard, scoreBoard;

    JSplitPane split_into_board_score = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);


    private JLabel message = new JLabel("Hello! Welcome to the chess board. A new game has been started.");

    private Player white = new Player("White");
    private Player black = new Player("Black");
    public Player player = white;

    JLabel cur_ply = new JLabel();

    private Game game = new Game();

    private boolean mode = true;
    private boolean src_dest_switch = false;
    private boolean tomove = false;

    protected Board board = new Board(white, black, mode);
    protected Piece[][]spots = board.getspots();
    int[] pos = new int[2];
    int[] fromto = new int[4];
    public static Vector<int[]> possible_dest = new Vector<>();

    int draw = 0;


    public static void main(String[] args) {


        Runnable r = new Runnable() {

            @Override
            public void run() {
                chessBoard_gui cb =
                        new chessBoard_gui();


                f.add(cb.getGui());
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setLocationByPlatform(true);

                // ensures the frame is the minimum size it needs to be
                // in order display the components within it
                f.pack();

                // ensures the minimum size is enforced.
                f.setMinimumSize(new Dimension(750, 797));
                f.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }

    public chessBoard_gui(){
        initializeGui();
    }

    /*public JFrame getFrame(){
        return frame;
    }*/

    public void switchplayer(){
        if(player.IsWhite()){
            player = black;
            switchlabel("Black");
        }else{
            player = white;
            switchlabel("White");
        }
    }

    public void switchlabel( String s){
        cur_ply.setText(s);
    }

    public Player otherplayer(){
        if(player.IsWhite()){
            return black;
        }else
            return white;
    }

    public void setpos(int i , int j, boolean is_src){
        if(is_src){
            fromto[0] = i;
            fromto[1] = j;
        }
        else{
            fromto[2] = i;
            fromto[3] = j;

        }
    }

    public void restart(){
        gui.removeAll();
        f.pack();
        game = new Game();
        board = new Board(white, black, mode);
        spots = board.getspots();
        pos = new int[2];
        fromto = new int[4];
        possible_dest.removeAllElements();
        tomove = false;
        src_dest_switch = false;
        player = white;
        initializeGui();
        //chessBoard.updateUI();
    }


    public final void initializeGui(){

        //setup

        gui.setBorder(new EmptyBorder(5,5,5,5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);

        gui.add(tools, BorderLayout.PAGE_START);

            //buttons
        JButton Mode = new JButton("Normal Mode");
        Mode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(mode){
                    mode = !mode;
                    Mode.setText("Fairy Mode");
                }else{
                    mode = !mode;
                    Mode.setText("Normal Mode");
                    //GUI.chessBoard_gui.this.pack();
                }
            }
        });
        tools.add(Mode);

        JButton Restart = new JButton("Restart");
        Restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart();
                f.pack();
            }
        });
        tools.add(Restart);

        JButton Forfeit = new JButton("Forfeit");
        Forfeit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, player.printName()+" forfeit! "+otherplayer().printName()+", do you agree?", player.printName()+" Forfeit!!!!", JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Congratulations! "+otherplayer().printName()+" win!");
                    otherplayer().win++;
                    restart();
                    f.pack();
                }
            }
        });
        tools.add(Forfeit);

        JButton Undo = new JButton("Undo");
        Undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int [] undid = board.undo(otherplayer());
                if(undid[0] == -1){
                    //if unsuccessful
                    JOptionPane.showMessageDialog(null, "Unable to undo anymore!!!", "Warning", JOptionPane.PLAIN_MESSAGE);
                }
                else{
                    //if successful
                    switchplayer();
                    refresh_button(undid[0], undid[1]);
                    refresh_button(undid[2], undid[3]);
                    f.pack();
                }
            }
        });
        tools.add(Undo);


        set_chessBoard();
        set_scoreBoard();

        split_into_board_score.setDividerLocation(0.8d);
        split_into_board_score.setBorder(new EmptyBorder(0, 0, 0, 0));
        gui.add(split_into_board_score, BorderLayout.CENTER);
    }

    private void set_scoreBoard() {

        //initializing scoreBoard including layout and components

        scoreBoard = new JPanel();
        BoxLayout boxlayout = new BoxLayout(scoreBoard, BoxLayout.Y_AXIS);
        // Set the Boxayout to be Y_AXIS from top to down

        scoreBoard.setLayout(boxlayout);

        scoreBoard.setBorder(
                BorderFactory.createCompoundBorder(
                        new EmptyBorder(0, 15, 0, 15),
                        new TitledBorder(" Score Board ")));

        JLabel l1 = new JLabel("Current player:");
        l1.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        l1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel l2 = new JLabel("White: ");
        l2.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        l2.setAlignmentX(Component.RIGHT_ALIGNMENT);

        HintTextField l3 = new HintTextField("Enter your name");
        l3.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel l4 = new JLabel("Black: ");
        l4.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        l4.setAlignmentX(Component.RIGHT_ALIGNMENT);

        HintTextField l5 = new HintTextField("Enter your name");
        l5.setAlignmentX(Component.CENTER_ALIGNMENT);


        cur_ply.setText(String.valueOf(player.printName()));
        cur_ply.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        cur_ply.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel w_win = new JLabel();
        w_win.setText("White won :" + String.valueOf(white.win));
        w_win.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        w_win.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel b_win = new JLabel();
        b_win.setText("Black won :"+String.valueOf(black.win));
        b_win.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        b_win.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel draw_ = new JLabel();
        draw_.setText("Draw:"+String.valueOf(draw));
        draw_.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
        draw_.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreBoard.add(Box.createRigidArea(new Dimension(150, 80)));

        scoreBoard.add(l1);
        scoreBoard.add(cur_ply);

        scoreBoard.add(Box.createRigidArea(new Dimension(150, 80)));

        scoreBoard.add(l2);
        scoreBoard.add(l3);

        scoreBoard.add(Box.createRigidArea(new Dimension(150, 80)));

        scoreBoard.add(l4);
        scoreBoard.add(l5);

        scoreBoard.add(Box.createRigidArea(new Dimension(150, 80)));

        scoreBoard.add(w_win);
        scoreBoard.add(Box.createRigidArea(new Dimension(150, 20)));
        scoreBoard.add(b_win);
        scoreBoard.add(Box.createRigidArea(new Dimension(150, 20)));
        scoreBoard.add(draw_);

        scoreBoard.add(Box.createRigidArea(new Dimension(150, 80)));

        split_into_board_score.setRightComponent(scoreBoard);

    }

    private void set_chessBoard() {

        //initialing chessBoard including buttons and layout
        chessBoard = new JPanel(new GridLayout(0,9));
        chessBoard.setBorder(new TitledBorder(" Chess Board "));
        split_into_board_score.setLeftComponent(chessBoard);


        Insets buttonMargin = new Insets(10,10,10,10);

        //initializing background
        for (int i = 0; i < 8; i++) {
            for (int j  = 0; j < 8; j++) {

                JButton button = new JButton();
                button.setMargin(buttonMargin);
                button.setBorderPainted(false);
                button.setOpaque(true);

                draw_act_button(i, j, button);


            }
        }

        //fill the chess board
        chessBoard.add(new JLabel(""));

        // fill the piece row
        for (int i = 0; i < 8; i++) {
            chessBoard.add(
                    new JLabel("" + (i + 1) ,
                            SwingConstants.CENTER));
        }
        // fill the black non-pawn piece row
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j< 9; j++) {
                if(j==0){
                        chessBoard.add(new JLabel("" + (i + 1),
                                SwingConstants.CENTER));}
                else{
                        if(buttons[i][j-1]!=null)
                            chessBoard.add(buttons[i][j-1]);}
                }
            }
    }

    private void draw_act_button(int i, int j, JButton button) {
        //initializing each button;
        button = drawButton(i, j, button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spots = board.getspots();
                int[] tmp;

                if(src_dest_switch == false){
                    if(spots[i][j] != null){
                        src_dest_switch = true;// is src

                        //set possible dest
                        possible_dest = board.possible_dest(i, j, player);

                        for(int k=0; k<possible_dest.size(); k++) {
                            tmp = possible_dest.elementAt(k);
                            BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
                            Graphics2D graphics = image.createGraphics();
                            graphics.setColor(Color.lightGray);
                            graphics.fillRect(0, 0, 50, 50);
                            buttons[tmp[0]][tmp[1]].setIcon(new ImageIcon(image));
                        }
                    }}
                else{
                    src_dest_switch = false;// is dest
                    tomove = true;
                }

                setpos(i, j, src_dest_switch);


                if(tomove)
                    try {

                        board.move(fromto, player);
                        refresh_button(fromto[0], fromto[1]);
                        refresh_button(fromto[2], fromto[3]);
                        f.pack();
                        switchplayer();
                        tomove = false;

                        //set back to black
                        for(int k=0; k<possible_dest.size(); k++){
                            tmp = possible_dest.elementAt(k);
                            refresh_button(tmp[0], tmp[1]);
                        }
                        possible_dest.removeAllElements();

                    } catch (IOException e1) {
                        //Alert
                        if(e1.getMessage().equals("Wrong Player!")){
                            cur_ply.setForeground(Color.RED);
                            cur_ply.setFont(new Font("Lucida Grande", Font.BOLD, 30));
                        }

                        JOptionPane.showMessageDialog(null, e1.getMessage(), "Warning", JOptionPane.PLAIN_MESSAGE);

                        refresh_button(fromto[0],fromto[1]);
                        refresh_button(fromto[2], fromto[3]);
                        tomove = false;

                        if(e1.getMessage().equals("Wrong Player!")){
                            cur_ply.setForeground(Color.black);
                            cur_ply.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
                        }

                        //set back to black
                        for(int k=0; k<possible_dest.size(); k++){
                            tmp = possible_dest.elementAt(k);
                            refresh_button(tmp[0], tmp[1]);
                        }
                        possible_dest.removeAllElements();
                }


            }
        });

        buttons[i][j] = button;



    }

    private static void printlist(int[] list) {
        for(int i=0; i<4; i++){
            System.out.print(list[i]+" ");
        }
        System.out.println();
    }


    private JButton drawButton(int i, int j, JButton button) {
        //check corresponding image to the button

        //refresh board
        spots = board.getspots();
        Piece temp = spots[i][j];


        if ((i+j)% 2 == 0) {
            button.setBackground(Color.white);
        } else {
            button.setBackground(Color.orange);
        }

        ImageIcon icon;

        if(temp != null){
            temp.setIcon();
            button.setIcon(resize_ImageIcon(temp.icon));
        }
        else{
            //for non pieces
            BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setColor(new Color(0, true));
            graphics.fillRect(0, 0, 50, 50);

            icon = new ImageIcon(image);
            button.setIcon(icon);
        }


        return button;
    }

    private void refresh_button(int srcX, int srcY){

        buttons[srcX][srcY] = drawButton(srcX, srcY, buttons[srcX][srcY]);

    }

    private ImageIcon resize_ImageIcon( ImageIcon icon) {
        //set image icon
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(50,50, Image.SCALE_SMOOTH);
        icon = new ImageIcon(newimg);
        return icon;
    }

    public final JComponent getChessBoard() {
        return chessBoard;
    }

    public final JComponent getGui() {
        return gui;
    }


}
