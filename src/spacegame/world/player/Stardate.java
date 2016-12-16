/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Stardate implements Serializable {

    private static final Logger LOG = Logger.getLogger(Stardate.class.getName());
    private static final long serialVersionUID = 1L;

    private long date;

    /**
     * Creates a new stardate starting on the first game day
     */
    public Stardate() {
        this(0);
    }

    /**
     * creates a new stardate starting at the specified date
     * @param date 
     */
    public Stardate(long date) {
        this.date = date;
    }
    
    /**
     * Adds the number of given days to the date
     * @param nbDay MUST be positive or zero
     */
    public void add(long nbDay){
        if (nbDay < 0){
            throw new IllegalArgumentException("Cannot add a negative number of days : " + nbDay);
        }
        date += nbDay;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();

        s.writeLong(date);
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();

        date = s.readLong();
    }

    @Override
    public String toString() {
        return Long.toString(date);
    }

}
