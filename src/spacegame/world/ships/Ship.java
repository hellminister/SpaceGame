/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.ships;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import spacegame.userinterfaces.ImageLibrary;
import spacegame.util.GameTimeActivatedIncrementableDoubleBinding;
import spacegame.util.SingleValueComplexFunctionDoubleBinding;
import spacegame.world.ships.shipcontrols.ShipControl;

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
        rocket = ImageLibrary.getImage("ship/rocket_straight_up_no_fire.png");
        fire = ImageLibrary.getImage("ship/rocket_straight_up_fire.png");
    }

    private ShipSprite sprite;

    private final DoubleProperty posX;
    private final DoubleProperty posY;

    private DoubleBinding speedX;
    private DoubleBinding speedY;

    private boolean acc;

    private final DoubleProperty angle;
    private final DoubleBinding angleCos;
    private final DoubleBinding angleSin;

    private double accThruster;
    private double turnSpeed;

    public Ship() {

        ShipControl ai;

        acc = false;
        accThruster = 0.1;
        turnSpeed = 2.0;

        angle = new SimpleDoubleProperty(RIGHT_ANGLE);

        angleCos = new SingleValueComplexFunctionDoubleBinding(angle, () -> Math.cos(Math.toRadians(angle.get())));

        angleSin = new SingleValueComplexFunctionDoubleBinding(angle, () -> Math.sin(Math.toRadians(angle.get())));

        speedX = new GameTimeActivatedIncrementableDoubleBinding(0.0, () -> {
            if (acc) {
                return accThruster * angleCos.get() * -1;
            }
            return 0;
        });

        speedY = new GameTimeActivatedIncrementableDoubleBinding(0.0, () -> {
            if (acc) {
                return accThruster * angleSin.get() * -1;
            }
            return 0;
        });

        posX = new SimpleDoubleProperty();
        posX.bind(new GameTimeActivatedIncrementableDoubleBinding(0.0, speedX::get));

        posY = new SimpleDoubleProperty();
        posY.bind(new GameTimeActivatedIncrementableDoubleBinding(0.0, speedY::get));

        sprite = new ShipSprite(this, rocket, fire);
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

    public ReadOnlyDoubleProperty posXProperty() {
        return posX;
    }

    public ReadOnlyDoubleProperty posYProperty() {
        return posY;
    }

    public ReadOnlyDoubleProperty angleProperty() {
        return angle;
    }

    public ShipSprite getNode() {
        return sprite;
    }

    public void accelerate() {
        sprite.drawPropulsion();
        acc = true;
    }

    public void stopAccelerate() {
        sprite.removePropulsion();
        acc = false;
    }


    public void reverseDirection() {
        double target = Math.round(Math.toDegrees(Math.atan2(speedY.get(), speedX.get())));
        target = target < 0 ? target + FULL_ANGLE : target;
        double delta = (target - angle.get()) % FULL_ANGLE;
        double absDelta = Math.abs(delta);
        LOG.log(Level.INFO, " target angle difference : " + delta + " - " + " - " + target + " - " + angle.get());

        if (absDelta > TOLERANCE) {
            if (absDelta >= turnSpeed) {
                turnRight();
            } else {
                addAngle(delta);
            }

        }
    }

}
