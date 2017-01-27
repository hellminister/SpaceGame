/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.player;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    public static final int STARTING_YEAR = 3000;
    public static final int WEEKS_IN_MONTH = 4;
    public static final int DAYS_IN_WEEK = DayNames.values().length;
    public static final int MONTHS_IN_YEARS = 13;
    public static final int SECONDS_IN_MINUTES = 60;
    public static final int MINUTES_IN_HOURS = 60;
    public static final int HOURS_IN_DAY = 24;
    public static final int SECONDS_IN_HOURS = SECONDS_IN_MINUTES * MINUTES_IN_HOURS;
    public static final int SECONDS_IN_DAY = SECONDS_IN_MINUTES * MINUTES_IN_HOURS * HOURS_IN_DAY;
    public static final int DAYS_IN_MONTH = DAYS_IN_WEEK * WEEKS_IN_MONTH;
    public static final int DAYS_IN_YEAR = DAYS_IN_WEEK * WEEKS_IN_MONTH * MONTHS_IN_YEARS;

    private transient LongProperty date;
    private transient IntegerProperty time;

    private transient DoubleProperty fractionalDate;

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
        time = new SimpleIntegerProperty(0);
        fractionalDate = new SimpleDoubleProperty();
        fractionalDate.bind(this.date.add(time.divide((double)SECONDS_IN_DAY)));
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

    public void addSeconds(int seconds){
        int newTime = time.get() + seconds;
        int addingDays = newTime / SECONDS_IN_DAY;
        newTime = newTime % SECONDS_IN_DAY;
        time.set(newTime);
        add(addingDays);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();

        s.writeLong(date.get());
        s.writeInt(time.get());
        s.writeDouble(fractionalDate.get());
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();

        date = new SimpleLongProperty(s.readLong());
        time = new SimpleIntegerProperty(s.readInt());
        fractionalDate = new SimpleDoubleProperty(s.readDouble());
        fractionalDate.bind(date.add(time.divide((double)SECONDS_IN_DAY)));
    }

    public ReadOnlyLongProperty starDateProperty(){
        return date;
    }

    public ReadOnlyIntegerProperty timeProperty() {
        return time;
    }

    public ReadOnlyDoubleProperty fractionalDateProperty() {
        return fractionalDate;
    }

    public DayNames getCurrentDayName(){
        return DayNames.values()[(int) date.get() % DAYS_IN_WEEK];
    }

    public int getCurrentSeconds(){
        return time.get() % SECONDS_IN_MINUTES;
    }

    public int getCurrentMinutes(){
        return (time.get() / SECONDS_IN_MINUTES) % MINUTES_IN_HOURS;
    }

    public int getCurrentHours(){
        return time.get() / SECONDS_IN_HOURS;
    }

    public long getCurrentDay(){
        return (date.get() % DAYS_IN_MONTH) + 1;
    }

    public long getCurrentMonth(){
        return ((date.get() / DAYS_IN_MONTH) % MONTHS_IN_YEARS) + 1;
    }

    public long getCurrentYear(){
        return STARTING_YEAR + (date.get() / DAYS_IN_YEAR);
    }

    @Override
    public String toString() {
        return String.format("%s, %d-%d-%d %02d:%02d",getCurrentDayName(), getCurrentDay(), getCurrentMonth(), getCurrentYear(), getCurrentHours(), getCurrentMinutes());
    }

    public String toLongString(){
        return String.format("%s, %d-%d-%d %02d:%02d:%02d",getCurrentDayName(), getCurrentDay(), getCurrentMonth(), getCurrentYear(), getCurrentHours(), getCurrentMinutes(), getCurrentSeconds());
    }

    public enum DayNames{
        Sunday,
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday,
        ;
    }

}
