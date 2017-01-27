/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.world.systems.datablocks;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import spacegame.userinterfaces.ImageLibrary;
import spacegame.userinterfaces.systemscreen.SystemScreenSprite;
import spacegame.userinterfaces.systemscreen.UIAction;
import spacegame.userinterfaces.systemscreen.trajectories.Trajectory;
import spacegame.userinterfaces.systemscreen.trajectories.TrajectoryFactory;
import spacegame.util.Utilities;
import spacegame.world.systems.BubbleSystem;
import spacegame.world.systems.CelestialBody;

import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class SpriteDataBlock extends DataBlock implements SystemScreenSprite{

    private static final Logger LOG = Logger.getLogger(SpriteDataBlock.class.getName());
    private static final Set<String> treatedProperties = Utilities.newUnmodifiableSet("size", "shape", "trajectory", "image");

    private double size;
    private Node sprite;
    private boolean toResize;
    private Trajectory position;

    private double originalSpriteWidth;
    private double originalSpriteHeight;

    private Set<UIAction> possibleUIActions;

    SpriteDataBlock() {
        super(InfoType.SPRITE);
        possibleUIActions = Utilities.newUnmodifiableSet(UIAction.NONE);
    }

    public static Set<String> treatsProperty(){
        return treatedProperties;
    }

    @Override
    Set<String> treatsInfo() {
        return treatsProperty();
    }

    @Override
    public Properties toProperties() {
        return new Properties();
    }

    @Override
    public void treatProperty(String prop, String value) {
        switch (prop) {
            case "size":
                size = Double.valueOf(value);
                break;
            case "shape":
                createShape(value);
                break;
            case "trajectory":
                position = TrajectoryFactory.createTrajectory(value);
                break;
            case "image":
                setSpriteImage(value);
                break;
            default:
                LOG.warning("No treatment for property : " + prop + " with value : " + value);
        }
    }

    private void setSpriteImage(String value) {
        String[] parts = value.split(",");
        Group node = new Group();
        ImageView image = new ImageView(ImageLibrary.getImage(parts[0]));
        originalSpriteHeight = image.getImage().getHeight();
        originalSpriteWidth = image.getImage().getWidth();
        node.getChildren().add(image);
        if (parts.length > 1) {
            ImageView shadow = new ImageView(ImageLibrary.getImage(parts[1]));
            shadow.rotateProperty().bind(new ShadowRotateBinding());
            node.getChildren().add(shadow);
        }
        sprite = node;
        toResize = true;

    }

    private void createShape(String value) {
        String[] parts = value.split(",");
        if ("CIRCLE".equals(parts[0])){
            toResize = size < 1.0;
            double radius = toResize ? 50 : (size / 2);
            originalSpriteHeight = radius * 2;
            originalSpriteWidth = radius * 2;
            sprite = new Circle(radius, Color.valueOf(parts[1]));
            LOG.info("Made a Circle of color " + parts[1]);
        } else {
            LOG.severe(parts[0] + " is not treated!! full value : " + value);
        }
    }

    @Override
    public boolean checkIntegrity(BubbleSystem system, CelestialBody me) {
        boolean good = true;

        if ((position == null) || !position.checkIntegrity(system, me) || (sprite == null) || (size <= 0)) {
            good = false;
        } else {
            if (toResize) {
                double scaleX = size / Math.max(originalSpriteWidth, originalSpriteHeight);


                sprite.setScaleX(scaleX);
                sprite.setScaleY(scaleX);
                toResize = false;

            }

            sprite.translateXProperty().bind(posXProperty());
            sprite.translateYProperty().bind(posYProperty());
        }

        return good;
    }

    @Override
    public ReadOnlyDoubleProperty posXProperty() {
        return position.posXProperty();
    }

    @Override
    public ReadOnlyDoubleProperty posYProperty() {
        return position.posYProperty();
    }

    @Override
    public Set<UIAction> getAcceptableActions() {
        return possibleUIActions;
    }

    @Override
    public Node getNode() {
        return sprite;
    }

    @Override
    public double getSize() {
        return size;
    }

    private final class ShadowRotateBinding extends DoubleBinding {
        private ShadowRotateBinding(){
            super.bind(posXProperty(), posYProperty());
        }

        @Override
        protected double computeValue() {
            return (Math.toDegrees(Math.atan2(posYProperty().get(), posXProperty().get())) - 90) % 360;
        }
    }
}
