package game.obj;

import java.awt.*;
//describes an ellipse that is defined by a framing rectangle.
//package to define ellipse shape
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;

public class Bullet {

    private double x;
    private double y;
    private final Shape shape;
    private final Color color = new Color(255,255,255);
    private final float angle;
    private double size;
    private float speed = 1f;

    public Bullet(double x, double y,float angle , double size, float speed){
        //to calculate the location of player to location of bullet
        x += Player.PLAYER_SIZE /2 - (size/2);
        y+= Player.PLAYER_SIZE/2-(size/2);
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.size = size;
        this.speed = speed;
        shape = new Ellipse2D.Double(0, 0, size, size);

    }

    //To move location of the bullet
    public void update(){
        x += Math.cos(Math.toRadians(angle)) * speed;
        y += Math.sin(Math.toRadians(angle)) * speed;

    }
    public void draw(Graphics2D g2) {
        AffineTransform oldTransform = g2.getTransform();
        g2.setColor(color);
        g2.translate(x, y);

        //to draw shape of bullet
        g2.fill(shape);
        g2.setTransform(oldTransform);
    }

    public boolean check(int width,int height){
        if(x<= -size|| y<-size||x>width||y>height){
            return false;
        }else{
            return true;
        }

    }

    public Shape getShape(){
        return new Area(new Ellipse2D.Double(x,y,size,size));
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSize(){
        return size;
    }

    public double getCenterX(){
        return x+size/2;
    }

    public double getCenterY(){
        return y+size/2;
    }

}