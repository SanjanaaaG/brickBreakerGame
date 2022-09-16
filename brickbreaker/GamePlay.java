package brickbreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePlay extends JPanel implements KeyListener,ActionListener{

    private Random random = new Random();

    private boolean play = false;
    private int score = 0;

    private int totalBricks = 21;

    private Timer timer; //setting the time of ball, how fast it should move
    private int delay = 10; // setting the speed for timer

    private int playerX = 310;

    private int ballposX = random.nextInt(650); // position for the ball
    private int ballposY = random.nextInt( 450);
    private int ballXdir = -1; //direction of the ball
    private int ballYdir = -2;

    private boolean success = false;
    private MapGenerator map;


    public GamePlay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay,this);
        timer.start();
    }

    public void paint(Graphics g) {
        //background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        //drawing map
        map.draw((Graphics2D)g);

        // borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(692, 0, 3, 592);

        // scores
        g.setColor(Color.yellow);
        g.setFont(new Font("serif", Font.BOLD,25));
        g.drawString(""+score, 590, 30);


        // the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);



        // the ball
        g.setColor(Color.red);
        g.fillOval(ballposX, ballposY, 20, 20);

        if(totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD,30));
            g.drawString("YOU WON!!!, Scores " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD,20));
            g.drawString("Press Enter to continue to the next level!!", 230, 350);
            delay += 100;
            success = true;
        }

        if(ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD,30));
            g.drawString("Game Over, Scores " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD,20));
            g.drawString("Press Enter to Restart", 230, 350);
            success = false;
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        timer.start();

        if(play) {

            // we have to create a rectangle for the ball for detecting the intersection
            // betw peddle and the ball

            if(new Rectangle(ballposX,ballposY,20,20).intersects(new Rectangle(playerX,550,100,8))) {
                ballYdir = -ballYdir;
            }

            A :for(int i =0; i<map.map.length;i++) {
                // in map.map the first map refers to the obj we have created in this class
                // the 2nd map is the map var in the MapGenerator class
                for(int j = 0; j<map.map[0].length;j++) {
                    if(map.map[i][j] > 0) {
                        // this will detect the collision
                        int brickX = j*map.brickWidth +80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        // to detect the intersection
                        Rectangle rect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX,ballposY,20,20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score +=5;
                            if(ballposX +19 <= brickRect.x || ballposX+1>= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            }else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;
            // for left border
            if(ballposX < 0) {
                ballXdir = -ballXdir;
            }
            // for top
            if(ballposY < 0) {
                ballYdir = -ballYdir;
            }
            // for right border
            if(ballposX > 670 ) {
                ballXdir = -ballXdir;
            }
        }

        repaint(); // this will recall the paint() method with the updated
        //values of coordinates

    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}  // we do not require this but if removed
    // it will throw an error

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(playerX >= 590) {
                playerX = 590;
            }else {
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(playerX <= 10) {
                playerX = 10;
            }else {
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER && success == false) {
            if(!play) {
                initPlay(0);
                repaint();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER && success == true) {
            if(!play) {
                initPlay(score);
                repaint();
            }
        }
    }

    public void initPlay(int score) {
        play = true;
        ballposX = random.nextInt( 650);
        ballposY = random.nextInt( 450);
        ballXdir = -1;
        ballYdir = -2;
        playerX = 310;
        this.score = score;
        totalBricks = 21;
        map = new MapGenerator(3,7);

    }

    public void moveRight() {
        play = true;
        playerX +=40;
    }
    public void moveLeft() {
        play = true;
        playerX -=40;
    }

}
