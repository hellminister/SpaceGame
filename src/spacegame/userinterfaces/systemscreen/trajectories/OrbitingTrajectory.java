/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.systemscreen.trajectories;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import spacegame.world.GameWorld;
import spacegame.world.systems.BubbleSystem;
import spacegame.world.systems.CelestialBody;

/**
 *
 * @author user
 */
public class OrbitingTrajectory implements Trajectory {

    private static final int ORBIT_PARENT_POSITION = 1;
    private static final int ORBIT_DISTANCE_POSITION = 2;
    private static final int ANGULAR_SPEED_POSITION = 3;
    private CelestialBody gravityCenter;
    private String orbitParent;
    private double orbitDistance;
    private double angularSpeed;

    private DoubleProperty posX;
    private DoubleProperty posY;

    OrbitingTrajectory(String[] parts){
        orbitParent = parts[ORBIT_PARENT_POSITION];
        orbitDistance = Double.valueOf(parts[ORBIT_DISTANCE_POSITION]);
        angularSpeed = Double.valueOf(parts[ANGULAR_SPEED_POSITION]);
    }

    @Override
    public ReadOnlyDoubleProperty posXProperty() {
        return posX;
    }

    @Override
    public ReadOnlyDoubleProperty posYProperty() {
        return posY;
    }

    @Override
    public boolean checkIntegrity(BubbleSystem system, CelestialBody me) {
        boolean good = true;

        if (orbitDistance < 0 || orbitParent == null){
            good = false;
        } else {
            orbitParent = system.getName() + "." + orbitParent;
            CelestialBody bod = system.getCelestialBodyForName(orbitParent);
            if (bod == null || bod == me){
                good = false;
            } else {
                gravityCenter = bod;
                bindPositions();
            }
        }

        return good;
    }

    private void bindPositions() {
        posX = new SimpleDoubleProperty();
        TranslationBinding tbX = new TranslationBinding(gravityCenter.getSystemScreenSprite().posXProperty(),
                                     GameWorld.accessGameWorld().getCurrentStarDate().starDateProperty(),
                                     Math::cos);
        posX.bind(tbX);

        posY = new SimpleDoubleProperty();
        TranslationBinding tbY = new TranslationBinding(gravityCenter.getSystemScreenSprite().posYProperty(),
                GameWorld.accessGameWorld().getCurrentStarDate().starDateProperty(),
                Math::sin);
        posY.bind(tbY);
    }

    @Override
    public boolean needsRealTimeUpdate() {
        return false;
    }

    private class TranslationBinding extends DoubleBinding {

        private static final int FULL_CIRCLE = 360;
        private ReadOnlyDoubleProperty pos;
        private ReadOnlyLongProperty date;
        private VectorAngleTransformation transformation;

        private TranslationBinding(ReadOnlyDoubleProperty gravityCenterPos, ReadOnlyLongProperty starDate, VectorAngleTransformation trans) {
            super.bind(gravityCenterPos, starDate);
            date = starDate;
            pos = gravityCenterPos;
            transformation = trans;
        }

        @Override
        protected double computeValue() {
            return pos.get() + (orbitDistance * transformation.transformAngle(Math.toRadians(angularSpeed*date.get())% FULL_CIRCLE));
        }
    }

    @FunctionalInterface
    private interface VectorAngleTransformation{
        double transformAngle(double angle);
    }
}
