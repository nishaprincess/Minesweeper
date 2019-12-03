/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.ImageIcon;

public class BoardBuild extends JFrame implements ActionListener {

    static BoardBuild board = new BoardBuild();

    /**
     *
     */
    static JPanel p1, p2, p3;
    int miss = 0;
    public int count = 0;
    // top label

    JLabel lbl1 = new JLabel("Minesweeper");

    public static final int BOARD_HEIGHT = 10;
    public static final int BOARD_WIDTH = 10;
    public static final int NUMBER_OF_CELLS = BOARD_HEIGHT * BOARD_WIDTH;
    public static final int BOMB_VALUE = 99;
    public static final int NO_MINE = 0;
    public static Image img;
    // all 100 of the board pieces to click
    JButton[][] btn = new JButton[BOARD_WIDTH][BOARD_HEIGHT];

    int mines[][] = new int[BOARD_WIDTH][BOARD_HEIGHT];// board piece values
    private GenerateMines mineGen;
    JButton btnReset = new JButton("Reset");
    JButton b = new JButton("Save");
    JButton l = new JButton("Load");
    ArrayList<NewClass> saveValle = new ArrayList<>();

    public BoardBuild() {
        p1 = new JPanel();
        p1.add(lbl1, BorderLayout.CENTER);

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    FileOutputStream fileOut = new FileOutputStream("object.ser");
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    for (int i = 0; i < saveValle.size(); i++) {
                        out.writeObject(saveValle.get(i));

                    }

                    out.close();
                    fileOut.close();
                } catch (IOException e1) {
                }
                setVisible(false);
            }
        });

        l.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    FileInputStream fileIn = null;

                    fileIn = new FileInputStream("object.ser");

                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    saveValle = new ArrayList<>();
                    while (true) {
                        NewClass c = (NewClass) in.readObject();
                        if (c == null) {
                            break;
                        }
                        saveValle.add(c);
                        System.out.println(c);
                         btn[c.getX()][c.getY()].removeActionListener(this);
                        btn[c.getX()][c.getY()].setBackground(Color.GRAY);

                    }
                    for (int i = 0; i < saveValle.size(); i++) {
                        btn[saveValle.get(i).getX()][saveValle.get(i).getY()].removeActionListener(this);
                        btn[saveValle.get(i).getX()][saveValle.get(i).getY()].setBackground(Color.GRAY);
                        System.out.println("Hello ");

                    }

                   // repaint();
//                   showBoard();
                    in.close();
                    fileIn.close();

                } catch (ClassNotFoundException ex) {

                } catch (IOException ex) {

                } catch (NullPointerException ex) {

                }

            }

            //          private void showBoard() {
            //           throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            //      }
        });
        p2 = new JPanel();

        p2.setLayout(new GridLayout(BOARD_WIDTH, BOARD_HEIGHT));// Gridlayout in panel 2

        for (int x = 0; x < BOARD_HEIGHT; x++) {
            for (int y = 0; y < BOARD_WIDTH; y++) {
                btn[x][y] = new JButton("");
                btn[x][y].addActionListener(this);
                p2.add(btn[x][y]);

            }
        }
       /* for (int i = 0; i < saveValle.size(); i++) {
            btn[saveValle.get(i).getX()][saveValle.get(i).getY()].removeActionListener(this);
            btn[saveValle.get(i).getX()][saveValle.get(i).getY()].setBackground(Color.GRAY);

        }*/

        getMines();
        p3 = new JPanel();
        btnReset.addActionListener(this);
        p3.add(btnReset, BorderLayout.EAST);
        p3.add(b, BorderLayout.WEST);
        p3.add(l, BorderLayout.NORTH);
    }

    public void getMines() {
        mineGen = new GenerateMines();//get mine positions in btn
        System.out.println("New Mine Values Generated");

        // sets indicator according to how many bombs around
        for (int x = 0; x < BOARD_HEIGHT; x++) {
            for (int y = 0; y < BOARD_WIDTH; y++) {
                btn[x][y].setText("");
                mines[x][y] = mineGen.getMinePos(x, y);
                if (mines[x][y] >= BOMB_VALUE) {
                    btn[x][y].setForeground(Color.RED);
                } else {
                    btn[x][y].setBackground(null);
                    if (mines[x][y] == 1) {
                        btn[x][y].setForeground(Color.BLUE);
                    } else if (mines[x][y] == 2) {
                        btn[x][y].setForeground(Color.GREEN);
                    } else if (mines[x][y] == 3) {
                        btn[x][y].setForeground(Color.YELLOW);
                    } else if (mines[x][y] == 4) {
                        btn[x][y].setForeground(Color.ORANGE);
                    } else if (mines[x][y] == 5) {
                        btn[x][y].setForeground(Color.RED);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int x = 0; x < BOARD_HEIGHT; x++) {
            for (int y = 0; y < BOARD_WIDTH; y++) {
                if (e.getSource() == btn[x][y]) {
                    btn[x][y].removeActionListener(this);
                    NewClass c = new NewClass(x, y);
                    saveValle.add(c);
                    if (mines[x][y] >= BOMB_VALUE) {
                        count++;
                        showBoard(count);
                        return;
                    } else if (mines[x][y] != NO_MINE) {
                        btn[x][y].setText("" + mines[x][y]);
                        btn[x][y].setBackground(Color.GRAY);
                        miss++;
                    } else if (mines[x][y] == NO_MINE) {
                        floodFill(x, y);
                    }
                    checkWin();
                    System.out.println(miss);
                }
            }
        }
        if (e.getSource() == btnReset) {
            miss = 0;
            System.out.println(miss);
            getMines();
            for (int x = 0; x < BOARD_HEIGHT; x++) {
                for (int y = 0; y < BOARD_WIDTH; y++) {
                    btn[x][y].addActionListener(this);
                    btn[x][y].setBackground(null);
                    btn[x][y].setIcon(null);
                    count = 0;
                }
            }
        }
    }

    public void floodFill(int x, int y) {
        if (x >= 0 && x <= 9 && y >= 0 && y <= 9) {
            for (int z = 1; z < 6; z++) {
                if (mines[x][y] == z) {
                    miss++;
                    System.out.println(miss);
                    btn[x][y].setText(z + "");
                    btn[x][y].setBackground(Color.GRAY);
                    NewClass c = new NewClass(x, y);
                    saveValle.add(c);
                    return;
                }
            }
            if (mines[x][y] == 0 && btn[x][y].getBackground() != Color.GRAY) {
                miss++;
                System.out.println(miss);
                btn[x][y].setBackground(Color.GRAY);
                btn[x][y].removeActionListener(this);
                NewClass c = new NewClass(x, y);
                saveValle.add(c);
                floodFill(x - 1, y);
                floodFill(x + 1, y);
                floodFill(x, y - 1);
                floodFill(x, y + 1);
            } else {
                return;
            }
        }
    }

    public void showBoard(int count) {
        if (count < 3) {
            JOptionPane.showMessageDialog(this, "you have" + (3 - count) + "lives left", "", JOptionPane.PLAIN_MESSAGE);
        }
        if (count == 3) {
            System.out.println("BOOOM");
            JOptionPane.showMessageDialog(this, "Unfortunately, You Lose", "",
                    JOptionPane.PLAIN_MESSAGE);
            for (int x = 0; x < BOARD_HEIGHT; x++) {
                for (int y = 0; y < BOARD_WIDTH; y++) {
                    btn[x][y].setBackground(Color.GRAY);
                    if (mines[x][y] != NO_MINE) {
                        if (mines[x][y] >= BOMB_VALUE) {   // btn[x][y].setText("B");
                            ImageIcon img;
                            img = new ImageIcon("D:\\JavaApplication2\\src\\javaapplication2\\rsz_unnamed.png");
                            btn[x][y].setIcon(img);
                        } else {
                            btn[x][y].setText("" + mines[x][y]);
                        }
                    }
                }
            }
            miss = 0;
        }
    }

    public void checkWin() {
        if (miss == 90) {
            System.out.println("WIN");
            JOptionPane.showMessageDialog(this, "Congratulations, You Win!!",
                    "", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - 450) / 2);
        int y = (int) ((dimension.getHeight() - 500) / 2);

        board.setLocation(x, y);
        board.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        board.add(p1, BorderLayout.NORTH);
        board.add(p2, BorderLayout.CENTER);
        board.add(p3, BorderLayout.SOUTH);
        board.setSize(450, 500);
        board.setVisible(true);
        board.setResizable(true);
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
