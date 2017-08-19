package spacegame.world.ships;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import spacegame.userinterfaces.systemscreen.SystemScreenSprite;
import spacegame.userinterfaces.systemscreen.UIAction;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by user on 2017-07-25.
 */
public class ShipSprite extends Parent implements SystemScreenSprite{

    private static final Logger LOG = Logger.getLogger(ShipSprite.class.getName());

    private static final double RIGHT_ANGLE = 90;



    private Ship owner;
    private Canvas sprite;
    private GraphicsContext gc;
    private Image propulsion;

    private double propXOffset;
    private double propYOffset;


    public ShipSprite(Ship isOwned, Image ship, Image propulsion){
        owner = isOwned;
        this.propulsion = propulsion;

        double width = Math.max(ship.getWidth(), propulsion.getWidth());
        double height = ship.getHeight() + (2 * propulsion.getHeight());

        double shipXOffset = (width - ship.getWidth()) / 2;
        double shipYOffset = height - ship.getHeight() - propulsion.getHeight();

        propXOffset = (width - propulsion.getWidth()) / 2;
        propYOffset = height - propulsion.getHeight();


        sprite = new Canvas(width, height);

        gc = sprite.getGraphicsContext2D();
        gc.drawImage(ship, shipXOffset, shipYOffset);

        translateXProperty().bind(posXProperty());
        translateYProperty().bind(posYProperty());
        rotateProperty().bind(owner.angleProperty().subtract(RIGHT_ANGLE));

        sprite.setScaleX(0.5);
        sprite.setScaleY(0.5);

        sprite.setPickOnBounds(false);

        sprite.setOnMouseClicked(event -> {
            LOG.info("SHIP HAS BEEN CLICKED!");
        });

        super.getChildren().add(sprite);


    }


    @Override
    public ReadOnlyDoubleProperty posXProperty() {
        return owner.posXProperty();
    }

    @Override
    public ReadOnlyDoubleProperty posYProperty() {
        return owner.posYProperty();
    }

    @Override
    public Set<UIAction> getAcceptableActions() {
        return new HashSet<>();
    }

    @Override
    public Node getNode() {
        return this;
    }

    @Override
    public double getSize() {
        return 0;
    }

    public void drawPropulsion() {
        gc.drawImage(propulsion, propXOffset, propYOffset);
    }

    public void removePropulsion() {
        gc.clearRect(propXOffset, propYOffset, propulsion.getWidth(), propulsion.getHeight());

    }
}
