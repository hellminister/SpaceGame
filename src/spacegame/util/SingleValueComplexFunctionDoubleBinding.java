package spacegame.util;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;

/**
 * Created by user on 2016-12-23.
 */
public class SingleValueComplexFunctionDoubleBinding extends DoubleBinding {
    private ComputeFunction function;

    public SingleValueComplexFunctionDoubleBinding(DoubleProperty value, ComputeFunction funct) {
        super.bind(value);
        function = funct;
    }

    @Override
    protected double computeValue() {
        return function.getValue();
    }
}
