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
 * This class contains the information specific to the player
 * Examples of information to put in this file :
 * Character's names, the current game date, 
 * The character's Structures (like buildings and ships)
 * etc.
 * @author user
 */
public class PlayerInfo implements Serializable{
    
    private static final Logger LOG = Logger.getLogger(PlayerInfo.class.getName());
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String first_name;
    private Species species;
    private Gender gender;
    private Stardate currentDate;
    
    private String currentSystem;

    public PlayerInfo(String name, String first_name, Species species, Gender gender) {
        this.name = name;
        this.first_name = first_name;
        this.species = species;
        this.gender = gender;
        currentDate = new Stardate();
        currentSystem = "test";
    }
    
        private void writeObject(ObjectOutputStream s) throws IOException{
        s.defaultWriteObject();
        
        s.writeUTF(name);
        s.writeUTF(first_name);
        s.writeObject(species);
        s.writeObject(gender);
        s.writeObject(currentDate);
        s.writeUTF(currentSystem);
    }
    
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException{
        s.defaultReadObject();
        
        name = s.readUTF();
        first_name = s.readUTF();
        species = (Species) s.readObject();
        gender = (Gender) s.readObject();
        currentDate = (Stardate) s.readObject();
        currentSystem = s.readUTF();
    }

    public String getInfo() {
        return first_name + " " + name + "\n" + gender + " " + species + "\n" + "In System : " + currentSystem + "\n" +currentDate.toString();
    }

    public String getFullName() {
        return first_name + " " + name;
    }
    
    public String getCurrentSystem(){
        return currentSystem;
    }
    
    public void setCurrentSystem(String system){
        currentSystem = system;
    }
}
