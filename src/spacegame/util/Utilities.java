package spacegame.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by user on 2016-12-16.
 */
public class Utilities {

    private Utilities(){
    }

    /**
     * Creates an returns an Hashset filled with the given elements or empty if no elements is given
     * @param values
     * @param <T>
     * @return
     */
    @SafeVarargs
    public static <T> Set<T> newUnmodifiableSet( T... values){
        Set<T> set = new HashSet<>();
        for (T v : values){
            set.add(v);
        }
        return Collections.unmodifiableSet(set);

    }

}
