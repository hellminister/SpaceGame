package spacegame.userinterfaces.systemscreen.trajectories;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import spacegame.world.systems.BubbleSystem;
import spacegame.world.systems.CelestialBody;

/**
 * Created by user on 2016-12-18.
 */
public class FixedPointTrajectory implements Trajectory {

    private static final int X_COORDINATE_POSITION = 1;
    private static final int Y_COORDINATE_POSITION = 2;
    private DoubleProperty posX;
    private DoubleProperty posY;

    FixedPointTrajectory(String[] parts) {
        posX = new SimpleDoubleProperty(Double.valueOf(parts[X_COORDINATE_POSITION]));
        posY = new SimpleDoubleProperty(Double.valueOf(parts[Y_COORDINATE_POSITION]));
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
        return (posX != null) && (posY != null);
    }

    @Override
    public boolean needsRealTimeUpdate() {
        return false;
    }
}
