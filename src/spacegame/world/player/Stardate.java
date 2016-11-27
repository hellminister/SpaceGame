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

    public Stardate(){
        this(1);
    }
    
    public Stardate(long date){
        this.date = date;
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
    public String toString(){
        return Long.toString(date);
    }
    

}
