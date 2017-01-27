package spacegame.util;

import javafx.beans.binding.DoubleBinding;
import spacegame.world.GameWorld;

/**
 * Created by user on 2016-12-23.
 */
public class IncrementableDoubleBinding extends DoubleBinding {
    private double oldValue;
    private ComputeFunction value;

    public IncrementableDoubleBinding(double initialValue, ComputeFunction incrementFunction) {
        super.bind(GameWorld.accessGameWorld().getCurrentStarDate().fractionalDateProperty());
        oldValue = initialValue;
        value = incrementFunction;
    }

    @Override
    protected double computeValue() {
        oldValue += value.getValue();
        return oldValue;
    }
}
