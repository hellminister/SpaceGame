package spacegame.userinterfaces.systemscreen.trajectories;

import javafx.beans.property.ReadOnlyDoubleProperty;
import spacegame.world.systems.BubbleSystem;
import spacegame.world.systems.CelestialBody;

/**
 * Created by user on 2016-12-18.
 */
public interface Trajectory {
    ReadOnlyDoubleProperty posXProperty();
    ReadOnlyDoubleProperty posYProperty();

    boolean checkIntegrity(BubbleSystem system, CelestialBody me);

    boolean needsRealTimeUpdate();
}
