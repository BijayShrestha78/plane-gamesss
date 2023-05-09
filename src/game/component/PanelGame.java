package game.component;


import game.obj.Bullet;
import game.obj.Effect;
import game.obj.Player;
import game.obj.Rocket;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JComponent;
import java.awt.geom.Area;


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
    private List<Rocket> rockets;
    private List<Effect> boomEffects;

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

    private void addRocket(){
        //to create rocket at random location
        Random ran = new Random();
        int locationY = ran.nextInt(height - 50) + 25;
        Rocket rocket = new Rocket();
        //setting rocket location at (0,randomLocation)
        rocket.changeLocation(0, locationY);
        //for moving from left to right because
        // it is created at left side of screen and have to come to right side
        rocket.changeAngle(0);
        rockets.add(rocket);
        //again creating new random value for new rocket object
        int locationY2 = ran.nextInt(height - 50) + 25;
        Rocket rocket2 = new Rocket();
        //setting new rocket object location xx to the width of the screen
        //and y as random values which means one creates at the top of screen and
        // one at the end(other side) of screen
        rocket2.changeLocation(width, locationY2);
        //for moving from right to left because
        // it is created at right side of screen and have to come to left side
        rocket2.changeAngle(180);
        rockets.add(rocket2);
    }

    private void initObjectGame() {
        player = new Player();
        player.changeLocation(150, 150);
        rockets = new ArrayList<>();
        boomEffects= new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(start){
                    addRocket();
                    sleep(3000);
                }
            }
        }).start();

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
                        for(int i=0;i<rockets.size(); i++){
                            Rocket rocket = rockets.get(i);
                            if(rocket != null){
                                rocket.update();
                                if (!rocket.check(width, height)) {
                                    rockets.remove(rocket);
                                }
                                //else{
//                                    if (player.isAlive()) {
//                                        checkPlayer(rocket);
//                                    }
//                                }
                            }
                        }


                    }
                    sleep(5);

                }
            }
        }).start();
    }

    private void checkBullets(Bullet bullet){
        for (int i = 0; i < rockets.size(); i++){
            Rocket rocket = rockets.get(i);
            if (rocket != null){
                Area area = new Area(bullet.getShape());
                area.intersect(rocket.getShape());
                if (!area.isEmpty()) {
                    boomEffects.add(new Effect(bullet.getCenterX(), bullet.getCenterY(), 3, 5, 60, 0.5f, new Color(230, 207, 105)));
                    if (true) {
                         rockets.remove(rocket);

                        double x = rocket.getX() + Rocket.ROCKET_SIZE / 2;
                        double y = rocket.getY() + Rocket.ROCKET_SIZE / 2;
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(32, 178, 169)));
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(32, 178, 169)));
                        boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(230, 207, 105)));
                        boomEffects.add(new Effect(x, y, 10, 5, 100, 0.5f, new Color(255, 70, 70)));
                        boomEffects.add(new Effect(x, y, 10, 5, 150, 0.2f, new Color(255, 255, 255)));
                        bullets.remove(bullet);
                    }
                }
            }
        }
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
        for (int i = 0; i < rockets.size(); i++) {
            Rocket rocket = rockets.get(i);
            if (rocket != null) {
                rocket.draw(g2);
            }
        }
            for (int i = 0; i < boomEffects.size(); i++) {
                Effect boomEffect = boomEffects.get(i);
                if (boomEffect != null) {
                    boomEffect.draw(g2);
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
                             checkBullets(bullet);
                             //checking if the bullet is outside the screen
                             //if it is then it removes the bullet from the list
                             if(!bullet.check(width,height)){
                                 bullets.remove(bullet);
                             }
                         }else {
                             bullets.remove(bullet);
                         }
                     }
                     for (int i = 0; i < boomEffects.size(); i++) {
                         Effect boomEffect = boomEffects.get(i);
                         if (boomEffect != null) {
                             boomEffect.update();
                             if (!boomEffect.check()) {
                                 boomEffects.remove(boomEffect);
                             }
                         } else {
                             boomEffects.remove(boomEffect);
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