/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.systemscreen;

import javafx.scene.input.KeyEvent;
import spacegame.userinterfaces.ReachStartScreen;

import java.util.logging.Logger;

import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import spacegame.SpaceGame;
import spacegame.userinterfaces.systemscreen.interfacepane.UserInterface;
import spacegame.world.GameWorld;
import spacegame.world.ships.Fleet;
import spacegame.world.ships.Ship;
import spacegame.world.systems.BubbleSystem;
import spacegame.world.systems.CelestialBody;

/**
 * @author user
 */
public class SystemScreen extends ReachStartScreen {

    private static final Logger LOG = Logger.getLogger(SystemScreen.class.getName());
    private static final double DIMENSION = 100000000;
    private static final double FIFTY_PERCENT = 0.5d;

    private final ScrollPane viewport;
    private final StackPane fullSystemArea;

    private final UserInterface userInterface;

    private final StackPane root;
    private final MovingBackground background;
    private SceneAnimator sceneAnimation;
    private SpaceGame mainTheater;
    private SystemPane currentSystem;

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
        root = (StackPane) getRoot();
        sceneAnimation = new SceneAnimator();

        mainTheater = aThis;

        fullSystemArea = new StackPane();

        background = new MovingBackground();
        fullSystemArea.getChildren().add(background);
        StackPane.setAlignment(background, Pos.CENTER);

        Rectangle sizing = new Rectangle(DIMENSION, DIMENSION);
        sizing.setFill(Color.TRANSPARENT);

        fullSystemArea.maxHeightProperty().bind(sizing.heightProperty());
        fullSystemArea.maxWidthProperty().bind(sizing.widthProperty());


        viewport = new ScrollPane(fullSystemArea) {
            @Override
            public void requestFocus() {
                /* so theres no focus */
            }
        };

        viewport.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        viewport.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        fullSystemArea.getChildren().add(sizing);

        root.getChildren().add(viewport);

        viewport.prefViewportHeightProperty().bind(root.heightProperty());
        viewport.prefViewportWidthProperty().bind(root.widthProperty());

        userInterface = new UserInterface(this);

        root.getChildren().add(userInterface);

        userInterface.prefHeightProperty().bind(root.heightProperty());
        userInterface.prefWidthProperty().bind(root.widthProperty());

        userInterface.setPickOnBounds(false);

        userInterface.toFront();

        setOnKeyActions();

    }

    private void setOnKeyActions() {
        setOnKeyPressed(this::onKeyPressedActions);

        setOnKeyReleased(this::onKeyReleasedActions);
    }

    private void onKeyReleasedActions(KeyEvent e) {
        if (sceneAnimation.isStarted()) {
            switch (e.getCode()) {
                case UP:
                    ship.stopAccelerate();
                    break;
                default:
                    break;
            }
        }
        switch (e.getCode()) {
            case X:
                mainTheater.changeSceneToStartScreen(this);
                sceneAnimation.stop();
                break;
            case P:
                sceneAnimation.toggle();
                break;
            default:
                break;
        }
    }

    private void onKeyPressedActions(KeyEvent e) {
        if (sceneAnimation.isStarted()) {
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
        }
    }

    /**
     * This method binds the scroll pane so that the players main ship is always centered
     */
    public void finishBindings() {
        DoublePropertyBase halfY = new SimpleDoubleProperty();

        halfY.bind(fullSystemArea.heightProperty().subtract(viewport.heightProperty()));

        DoublePropertyBase halfX = new SimpleDoubleProperty();

        halfX.bind(fullSystemArea.widthProperty().subtract(viewport.widthProperty()));

        viewport.vvalueProperty().bind(ship.getNode().translateYProperty().divide(halfY).add(FIFTY_PERCENT));
        viewport.hvalueProperty().bind(ship.getNode().translateXProperty().divide(halfX).add(FIFTY_PERCENT));

        background.bindTo2(ship.posXProperty(), ship.posYProperty());

    }

    /**
     * Set the current system to the specified system and
     * draws the system on the scene
     * will need to change the constructions here to separate the drawings with the details
     *
     * @param system System to be shown on the screen
     */
    public void loadSystem(BubbleSystem system) {
        ObservableList<Node> children = fullSystemArea.getChildren();
        children.remove(currentSystem);
        currentSystem = new SystemPane(system, this);
        children.add(currentSystem);
        Fleet playerFleet = GameWorld.accessGameWorld().getPlayerState().getPlayerState().getFleet();
        ship = playerFleet.getFlagship();
        fullSystemArea.getChildren().addAll(playerFleet.allShipSprites());
        playerFleet.putToFront();
        userInterface.populateRadar(system, ship);
        userInterface.toFront();
        finishBindings();
    }

    @Override
    public void giveFocusBack() {
        sceneAnimation.start();
    }


    public void selectedCelestialBody(CelestialBody bodies) {
        LOG.info("selected : " + bodies.getId());
    }
}
