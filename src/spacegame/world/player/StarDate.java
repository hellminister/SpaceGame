/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.player;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleLongProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class StarDate implements Serializable {

    private static final Logger LOG = Logger.getLogger(StarDate.class.getName());
    private static final long serialVersionUID = 1L;

    private transient LongProperty date;

    /**
     * Creates a new stardate starting on the first game day
     */
    public StarDate() {
        this(0);
    }

    /**
     * creates a new stardate starting at the specified date
     * @param date 
     */
    public StarDate(long date) {
        this.date = new SimpleLongProperty(date);
    }
    
    /**
     * Adds the number of given days to the date
     * @param nbDay MUST be positive or zero
     */
    public void add(long nbDay){
        if (nbDay < 0){
            throw new IllegalArgumentException("Cannot add a negative number of days : " + nbDay);
        }
        date.set(date.get()+nbDay);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();

        s.writeLong(date.get());
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();

        date = new SimpleLongProperty(s.readLong());
    }

    public ReadOnlyLongProperty starDateProperty(){
        return date;
    }

    @Override
    public String toString() {
        return date.toString();
    }

}
