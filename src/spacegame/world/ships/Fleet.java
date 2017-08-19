package spacegame.world.ships;

import javafx.scene.Node;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by user on 2017-07-24.
 */
public class Fleet implements Serializable {

    private static final long serialVersionUID = 7963229515762724586L;

    private transient List<Ship> ships;

    public Fleet(){
        ships = new LinkedList<>();
        ships.add(new Ship());
        ships.add(new Ship());
    }

    public Ship getFlagship(){
        return ships.isEmpty() ? null : ships.get(0);
    }

    public void putToFront(){
        ships.forEach( (ship) -> ship.getNode().toFront());
    }

    public Set<Node> allShipSprites() {
        return ships.stream().map( (ship) -> ship.getNode()).collect(Collectors.toSet());
    }
}
