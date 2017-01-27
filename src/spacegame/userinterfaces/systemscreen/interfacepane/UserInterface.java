package spacegame.userinterfaces.systemscreen.interfacepane;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import spacegame.userinterfaces.systemscreen.SystemScreen;
import spacegame.world.ships.Ship;
import spacegame.world.systems.BubbleSystem;

/**
 * Created by user on 2016-12-22.
 */
public class UserInterface extends AnchorPane{

    private SystemScreen parent;
    private Radar radar;

    public UserInterface(SystemScreen attachedTo){
        parent = attachedTo;
        radar = new Radar();
        ObservableList<Node> children = getChildren();
        children.add(radar);
        setTopAnchor(radar, 10.0);
        setLeftAnchor(radar, 10.0);

    }

    public void populateRadar(BubbleSystem system, Ship ship) {
        radar.populate(system, ship);
    }
}
