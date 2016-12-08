/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.systemscreen;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import spacegame.SpaceGame;
import spacegame.world.ships.Ship;
import spacegame.world.systems.BubbleSystem;

/**
 *
 * @author user
 */
public class SystemScreen extends Scene {

    private static final Logger LOG = Logger.getLogger(SystemScreen.class.getName());
    private static final String BACK_IMAGE_TILE_FILE_PATH = "/resources/images/tiles/1.jpg";
    private static final double DIMENSION = 10000;

    private final ScrollPane viewport;
    private final StackPane fullSystemArea;
    private final Canvas sizing;

    private Image backgroundTile;
    private BackgroundImage backgroundImage;

    private final AnchorPane userInterface;

    private final StackPane root;

    private SceneAnimator sceneAnimation;

    private BubbleSystem currentSystem;

    private SpaceGame mainTheater;

    private Ship ship;

    /**
     * Creates the System screen that will show the system and where the player 
     * will be able to control/move his ship
     * This is the only screen that has real time action.
     * 
     * @param aThis the main game object
     */
    public SystemScreen(SpaceGame aThis) {
        super(new StackPane());
        root = (StackPane) this.getRoot();
        sceneAnimation = new SceneAnimator(this);

        backgroundTile = new Image(BACK_IMAGE_TILE_FILE_PATH);
        backgroundImage = new BackgroundImage(backgroundTile, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

        mainTheater = aThis;

        fullSystemArea = new StackPane();

        fullSystemArea.setBackground(new Background(backgroundImage));
        sizing = new Canvas(DIMENSION, DIMENSION);

        fullSystemArea.maxHeightProperty().bind(sizing.heightProperty());
        fullSystemArea.maxWidthProperty().bind(sizing.widthProperty());

        viewport = new ScrollPane(fullSystemArea) {
            @Override
            public void requestFocus() {
                /* so theres no focus */ }
        };

        viewport.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        viewport.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        fullSystemArea.getChildren().add(sizing);

        root.getChildren().add(viewport);

        viewport.prefViewportHeightProperty().bind(root.heightProperty());
        viewport.prefViewportWidthProperty().bind(root.widthProperty());

        userInterface = new AnchorPane();

        root.getChildren().add(userInterface);

        userInterface.prefHeightProperty().bind(root.heightProperty());
        userInterface.prefWidthProperty().bind(root.widthProperty());

        userInterface.setPickOnBounds(false);

        ship = new Ship();
        Node nShip = ship.draw();

        fullSystemArea.getChildren().add(nShip);

        this.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case RIGHT:
                    ship.turnRight();
                    break;
                case LEFT:
                    ship.turnLeft();
                    break;
                case UP:
                    ship.accelerate();
                    break;
                case DOWN:
                    ship.reverseDirection();
                    break;
                default:
                    break;
            }
        });

        this.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case UP:
                    ship.stopAccelerate();
                    break;
                case X:
                    mainTheater.changeSceneToStartScreen();
                    sceneAnimation.stop();
                    break;
                default:
                    break;
            }
        });

    }

    /**
     * This method binds the scroll pane so that the players main ship is always centered
     */
    public void finishBindings() {
        DoublePropertyBase halfY = new SimpleDoubleProperty();

        halfY.bind(fullSystemArea.heightProperty().subtract(viewport.heightProperty()));

        DoublePropertyBase halfX = new SimpleDoubleProperty();

        halfX.bind(fullSystemArea.widthProperty().subtract(viewport.widthProperty()));

        viewport.vvalueProperty().bind(ship.getNode().translateYProperty().divide(halfY).add(0.5d));
        viewport.hvalueProperty().bind(ship.getNode().translateXProperty().divide(halfX).add(0.5d));
    }

    /**
     * Set the current system to the specified system and 
     * draws the system on the scene
     * Also starts the animation
     * @param system 
     */
    public void loadSystem(BubbleSystem system) {
        if (currentSystem != system) {
            currentSystem = system;
            fullSystemArea.getChildren().add(currentSystem.getNode());

            ship.getNode().toFront();
        }
        sceneAnimation.start();

    }

    /**
     * update all moving sprites location by 1 tick of time
     */
    public void moveSprites() {
        ship.updatePosition();
    }

    private static class SceneAnimator extends AnimationTimer {

        private static final int NANOSECONDS_IN_A_SECOND = 1000000000;
        private final SystemScreen toAnimate;

        private long last;
        private int nbFrame;

        SceneAnimator(SystemScreen ta) {
            toAnimate = ta;
        }

        @Override
        public void handle(long now) {
            toAnimate.moveSprites();

            nbFrame++;
            long secs = now - last;
            if (secs > NANOSECONDS_IN_A_SECOND) {
                last = now;
                LOG.log(Level.INFO, "{0} secs fps : {1}", new Object[]{(double) secs / NANOSECONDS_IN_A_SECOND, nbFrame});
                nbFrame = 0;
            }
        }
    }

}
