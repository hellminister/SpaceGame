package spacegame.userinterfaces.systemscreen.interfacepane;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import spacegame.world.ships.Ship;
import spacegame.world.systems.BubbleSystem;
import spacegame.world.systems.CelestialBody;

/**
 * Created by user on 2016-12-22.
 */
public class Radar extends StackPane {

    private static final int RADAR_RADIUS = 200;
    private static final int RADAR_SCALE_FACTOR = 50;
    private static final double DIVISION_BY_ZERO_PREVENTOR = 0.0000000001;
    private static final int SQUARE_POWER = 2;
    private static final int SHIP_RADIUS = 2;
    private static final double RADAR_OPACITY_VALUE = 0.5;

    public Radar() {
        Circle circle = new Circle(RADAR_RADIUS, Color.BLACK);
        circle.setOpacity(RADAR_OPACITY_VALUE);
        getChildren().add(circle);
        Circle clip = new Circle();
        clip.radiusProperty().bind(circle.radiusProperty());
        clip.layoutXProperty().bind(circle.layoutXProperty());
        clip.layoutYProperty().bind(circle.layoutYProperty());
        setClip(clip);
    }

    public void populate(BubbleSystem system, Ship sh){
        ObservableList<Node> children = getChildren();
        Circle ship = new Circle(SHIP_RADIUS, Color.GREEN);
        children.add(ship);

        for (CelestialBody bod : system.getAllBodies()){
            Color color;
            Node body;
            switch (bod.getType()) {
                case STAR:
                    body = new Circle(bod.getSystemScreenSprite().getSize() / RADAR_SCALE_FACTOR, Color.GOLDENROD);
                    body.translateXProperty().bind(bod.getSystemScreenSprite().posXProperty().subtract(sh.posXProperty()).divide(RADAR_SCALE_FACTOR));
                    body.translateYProperty().bind(bod.getSystemScreenSprite().posYProperty().subtract(sh.posYProperty()).divide(RADAR_SCALE_FACTOR));
                    children.add(body);

                    DoubleBinding distance = new DoubleBinding() {

                        {
                            super.bind(bod.getSystemScreenSprite().posXProperty(), sh.posXProperty(), bod.getSystemScreenSprite().posYProperty(), sh.posYProperty());
                        }

                        @Override
                        protected double computeValue() {
                            return Math.max(DIVISION_BY_ZERO_PREVENTOR, Math.sqrt(Math.pow((bod.getSystemScreenSprite().posXProperty().get() - sh.posXProperty().get())/(RADAR_SCALE_FACTOR), SQUARE_POWER) + Math.pow((bod.getSystemScreenSprite().posYProperty().get() - sh.posYProperty().get())/(RADAR_SCALE_FACTOR), SQUARE_POWER)));
                        }
                    };

                    body = new Circle(bod.getSystemScreenSprite().getSize() / RADAR_SCALE_FACTOR, Color.ORANGE);
                    body.translateXProperty().bind(bod.getSystemScreenSprite().posXProperty().subtract(sh.posXProperty()).divide(RADAR_SCALE_FACTOR).divide(distance).multiply(RADAR_RADIUS));
                    body.translateYProperty().bind(bod.getSystemScreenSprite().posYProperty().subtract(sh.posYProperty()).divide(RADAR_SCALE_FACTOR).divide(distance).multiply(RADAR_RADIUS));
                    body.visibleProperty().bind(Bindings.when(distance.greaterThan(RADAR_RADIUS)).then(true).otherwise(false));
                    break;
                case PLANETOID:
                    color = Color.BLUE;
                    body = new Circle(bod.getSystemScreenSprite().getSize() / RADAR_SCALE_FACTOR, color);
                    body.translateXProperty().bind(bod.getSystemScreenSprite().posXProperty().subtract(sh.posXProperty()).divide(RADAR_SCALE_FACTOR));
                    body.translateYProperty().bind(bod.getSystemScreenSprite().posYProperty().subtract(sh.posYProperty()).divide(RADAR_SCALE_FACTOR));
                    break;
                default:
                    color = Color.RED;
                    body = new Circle(bod.getSystemScreenSprite().getSize() / RADAR_SCALE_FACTOR, color);
                    body.translateXProperty().bind(bod.getSystemScreenSprite().posXProperty().subtract(sh.posXProperty()).divide(RADAR_SCALE_FACTOR));
                    body.translateYProperty().bind(bod.getSystemScreenSprite().posYProperty().subtract(sh.posYProperty()).divide(RADAR_SCALE_FACTOR));
            }
            children.add(body);
        }

    }



}
