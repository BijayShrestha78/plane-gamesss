package game.component;


import game.obj.Bullet;
import game.obj.Player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

import static java.lang.Thread.sleep;
//import static jdk.internal.org.jline.utils.Log.render;

public class  PanelGame extends JComponent {

    private Graphics2D g2;
    private BufferedImage image;
    private int width;
    private int height;
    private Thread thread;
    private final boolean start = true;
    private Key key;
    private int shotTime;


    //  Game FPS
    private final int FPS = 60;
    private final int TARGET_TIME = 1000000000 / FPS;
    //  Game Object

    private Player player;
    private List<Bullet> bullets;

    public void start() {
        width = getWidth();
        height = getHeight();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    long startTime = System.nanoTime();
                    drawBackground();
                    drawGame();
                    render();
                    long time = System.nanoTime() - startTime;
                    if (time < TARGET_TIME) {
                        long sleep = (TARGET_TIME - time) / 1000000;
                       // try {
                            sleep(sleep);
                        //} catch (InterruptedException e) {
                         //   throw new RuntimeException(e);
                       // }
                    }
                }
            }
        });
        initObjectGame();
        initKeyboard();
        initBullets();
        thread.start();
    }






    private void initObjectGame() {
        player = new Player();
        player.changeLocation(150, 150);
    }


    private void initKeyboard() {
        key = new Key();
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    key.setKey_left(true);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    key.setKey_right(true);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    key.setKey_space(true);
                } else if (e.getKeyCode() == KeyEvent.VK_J) {
                    key.setKey_j(true);
                } else if (e.getKeyCode() == KeyEvent.VK_K) {
                    key.setKey_k(true);
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    key.setKey_enter(true);
                }
            }


            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    key.setKey_left(false);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    key.setKey_right(false);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    key.setKey_space(false);
                } else if (e.getKeyCode() == KeyEvent.VK_J) {
                    key.setKey_j(false);
                } else if (e.getKeyCode() == KeyEvent.VK_K) {
                    key.setKey_k(false);
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    key.setKey_enter(false);
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                float s = 0.5f;
                while (start) {
                    if (player.isAlive()) {
                        float angle = player.getAngle();
                        if (key.isKey_left()) {
                            angle -= s;
                        }
                        if (key.isKey_right()) {
                            angle += s;
                        }
                        if(key.isKey_j()||key.isKey_k()){
                            if (shotTime == 0){
                                if (key.isKey_j()) {
                                    bullets.add(0, new Bullet(player.getX(), player.getY(), player.getAngle(), 5, 3f));
                                } else {
                                    bullets.add(0, new Bullet(player.getX(), player.getY(), player.getAngle(), 20, 3f));
                                }

                            }
                            shotTime++;
                            if (shotTime == 15) {
                                shotTime = 0;
                            }
                        }else{
                            shotTime = 0;
                        }
                        if(key.isKey_space()){
                            player.speedUp();
                        }else{
                            player.speedDown();
                        }
                        player.update();
                        player.changeAngle(angle);


                    }
                    sleep(5);

                }
            }
        }).start();
    }

        private void drawBackground() {
            g2.setColor(new Color(30, 30, 30));
            g2.fillRect(0, 0, width, height);
        }

        private void drawGame() {
        if(player.isAlive()){
            player.draw(g2);
        }
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet != null) {
                bullet.draw(g2);
            }
        }

        }


        //initializing bullets into panel
        private void initBullets(){
         bullets = new ArrayList<>();
         new Thread(new Runnable() {
             @Override
             public void run() {
                 while(start){
                     //to show that all the bullets in list collection is moving
                     for(int i=0;i< bullets.size();i++){
                         Bullet bullet = bullets.get(i);
                         if(bullet!= null){
                             //to increase the location or make the bullet is moving
                             bullet.update();
                             //checking if the bullet is outside the screen
                             //if it is then it removes the bullet from the list
                             if(!bullet.check(width,height)){
                                 bullets.remove(bullet);
                             }
                         }else {
                             bullets.remove(bullet);
                         }
                     }
                     sleep(1);
                 }
             }
         }).start();

        }



    private void render() {
        Graphics g = getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }

    private void sleep( long speed) {
        try {
           Thread.sleep(speed);
        } catch (InterruptedException ex) {
            System.err.println(ex);
        }
    }
}