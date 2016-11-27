/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.ships;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

/**
 *
 * @author user
 */
public class Ship {
    
    private static final double ONE_DEGREE_IN_RAD;
    private static final double TWO_PI;
    
    private static final Image rocket;
    private static final Image fire;
    
    static {
        ONE_DEGREE_IN_RAD = Math.toRadians(1.0);
        TWO_PI = Math.PI * 2;
        
        rocket = new Image("/resources/images/ship/rocket_straight_up_no_fire.png");
        fire = new Image("/resources/images/ship/rocket_straight_up_fire.png");
    }
    
    private Node sprite = null;
    GraphicsContext gc;
    
    private double posX;
    private double posY;
    
    private double speedX;
    private double speedY;
    
    private double acc;
    
    private double angle;
    
    public Ship(){
        posX = 0;
        posY = 0;
        speedX = 0;
        speedY = 0;
        acc = 0;
        
        angle = (90);
    }
    
    public void addAngle(double toAdd){
        angle = (angle + (toAdd )) % 360;
    }
    
    public Node draw(){
        sprite = new Canvas(79, 414);
                
        gc = ((Canvas)sprite).getGraphicsContext2D();
        gc.drawImage(rocket, 0, 114);
        
                
        updatePosition();

        return sprite;
    }
    
    
    public void updatePosition(){
        if (acc != 0){
            speedX += acc * Math.cos(Math.toRadians(angle));
            speedY += acc * Math.sin(Math.toRadians(angle));
        }
        
        posX += speedX;
        posY += speedY;
        
        sprite.setTranslateX(posX);
        sprite.setTranslateY(posY);
        
        sprite.setScaleX(0.5);
        sprite.setScaleY(0.5);
        
        sprite.setRotate((angle)-90);
        
    }

    public Node getNode() {
        return sprite;
    }

    public void setAcc(double d) {
        
        if (d != 0){
            gc.drawImage(fire, 0, 300);
        } else {
            gc.clearRect(0, 300, 79, (114));
        }
        
        acc = d;
    }
    
    public void reverseDirection(){
        double target = (Math.round(Math.toDegrees(Math.atan2(speedY, speedX)))+360) %360;
        
        if (angle != target){
            if ((target - angle) > 10){
            addAngle(2.0);
            } else {
                addAngle(1.0);
            }
            
        }
    }


}
