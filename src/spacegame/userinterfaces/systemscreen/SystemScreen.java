/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.systemscreen;

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

    private static final String BACK_IMAGE_TILE_FILE_PATH = "/resources/images/tiles/1.jpg";

    private ScrollPane viewport;
    private StackPane fullSystemArea;
    private Canvas sizing;

    private Image backgroundTile;
    private BackgroundImage backgroundImage;

    private AnchorPane userInterface;

    private final StackPane root;

    private SceneAnimator sceneAnimation;

    private BubbleSystem currentSystem;

    private SpaceGame mainTheater;

    private Ship ship;

    public SystemScreen(SpaceGame aThis) {
        super(new StackPane());
        root = (StackPane) this.getRoot();
        sceneAnimation = new SceneAnimator(this);

        backgroundTile = new Image(BACK_IMAGE_TILE_FILE_PATH);
        backgroundImage = new BackgroundImage(backgroundTile, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

        mainTheater = aThis;

        fullSystemArea = new StackPane();

        fullSystemArea.setBackground(new Background(backgroundImage));
        sizing = new Canvas(5000, 5000);

        viewport = new ScrollPane(fullSystemArea) {
            @Override
            public void requestFocus() { }
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
            switch (e.getCode()){
                case RIGHT:
                    ship.addAngle(2.0);
                    break;
                case LEFT:
                    ship.addAngle(-2.0);
                    break;
                case UP:
                    ship.setAcc(-0.1);
                    break;
                case DOWN:
                    ship.reverseDirection();
                    break;
                default:
                    break;
            }
        });
        
        this.setOnKeyReleased(e -> {
            switch (e.getCode()){
                case UP:
                    ship.setAcc(0.0);
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

    public void finishBindings() {
        DoublePropertyBase halfY = (new SimpleDoubleProperty());
        halfY.bind(viewport.heightProperty().multiply(-1).add(sizing.getHeight()));

        DoublePropertyBase halfX = (new SimpleDoubleProperty());
        halfX.bind(viewport.widthProperty().multiply(-1).add(sizing.getWidth()));

        viewport.vvalueProperty().bind(ship.getNode().translateYProperty().divide(halfY).add(0.5d));
        viewport.hvalueProperty().bind(ship.getNode().translateXProperty().divide(halfX).add(0.5d));
    }

    public void loadSystem(BubbleSystem system) {
        if (currentSystem != system) {
            currentSystem = system;
            currentSystem.draw(this);
            ship.getNode().toFront();
        }
        sceneAnimation.start();

    }

    public void addSystem(StackPane systemPane) {
        fullSystemArea.getChildren().add(systemPane);
    }

    private void moveSprites() {
        ship.updatePosition();
    }

    private static class SceneAnimator extends AnimationTimer {

        private SystemScreen toAnimate;
        
        private long last = 0;
        private int nbFrame = 0;

        public SceneAnimator(SystemScreen ta) {
            toAnimate = ta;
        }

        @Override
        public void handle(long now) {
            toAnimate.moveSprites();
            
            nbFrame++;
            long secs = now-last;
            if (secs > 1000000000){
                last = now;
                LOG.info((double)secs/1000000000 + " fps" + nbFrame);
                nbFrame = 0;
            }
        }
    }
    private static final Logger LOG = Logger.getLogger(SystemScreen.class.getName());

}
