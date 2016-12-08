/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.ships;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * Will need to split this class into a sprite and the ship data
 * 
 * @author user
 */
public class Ship {

    private static final Logger LOG = Logger.getLogger(Ship.class.getName());

    private static final Image rocket;
    private static final Image fire;
    private static final double TOLERANCE = 0.00001;
    private static final double RIGHT_ANGLE = 90;
    private static final double FULL_ANGLE = 360;

    static {
        rocket = new Image("/resources/images/ship/rocket_straight_up_no_fire.png");
        fire = new Image("/resources/images/ship/rocket_straight_up_fire.png");
    }

    private Node sprite;
    private GraphicsContext gc;

    private final DoubleProperty posX;
    private final DoubleProperty posY;

    private double speedX;
    private double speedY;

    private boolean acc;

    private final DoubleProperty angle;

    private double accThruster;
    private double turnSpeed;

    public Ship() {
        posX = new SimpleDoubleProperty(0.0);
        posY = new SimpleDoubleProperty(0.0);
        speedX = 0;
        speedY = 0;
        acc = false;

        accThruster = 0.1;
        turnSpeed = 2.0;

        angle = new SimpleDoubleProperty(RIGHT_ANGLE);
    }

    private void addAngle(double toAdd) {
        angle.set((angle.get() + toAdd) % FULL_ANGLE);
    }

    public void turnRight() {
        addAngle(turnSpeed);
    }

    public void turnLeft() {
        addAngle(-turnSpeed);
    }

    public Node draw() {
        sprite = new Canvas(79, 414);

        gc = ((Canvas) sprite).getGraphicsContext2D();
        gc.drawImage(rocket, 0, 114);

        sprite.translateXProperty().bind(posX);
        sprite.translateYProperty().bind(posY);
        sprite.rotateProperty().bind(angle.subtract(RIGHT_ANGLE));

        sprite.setScaleX(0.5);
        sprite.setScaleY(0.5);

        return sprite;
    }

    public ReadOnlyDoubleProperty posXProperty() {
        return posX;
    }

    public ReadOnlyDoubleProperty posYProperty() {
        return posY;
    }

    public ReadOnlyDoubleProperty angleProperty() {
        return angle;
    }

    public void updatePosition() {
        if (acc) {
            speedX -= accThruster * Math.cos(Math.toRadians(angle.get()));
            speedY -= accThruster * Math.sin(Math.toRadians(angle.get()));
        }

        posX.set(posX.get() + speedX);
        posY.set(posY.get() + speedY);
    }

    public Node getNode() {
        return sprite;
    }

    public void accelerate() {
        gc.drawImage(fire, 0, 300);
        acc = true;
    }

    public void stopAccelerate() {
        gc.clearRect(0, 300, 79, 114);
        acc = false;
    }

    public void reverseDirection() {
        double target = (Math.round(Math.toDegrees(Math.atan2(speedY, speedX))) + FULL_ANGLE) % FULL_ANGLE;

        double delta = (target - angle.get()) % FULL_ANGLE;
        double absDelta = Math.abs(delta);
        LOG.log(Level.FINE, " target angle difference : {0}", delta);

        if (absDelta > TOLERANCE) {
            if (absDelta >= turnSpeed) {
                turnRight();
            } else {
                addAngle(delta);
            }

        }
    }

}
