/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame;

import java.util.logging.Level;
import spacegame.userinterfaces.startscreen.StartScreen;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import spacegame.userinterfaces.ReachStartScreen;
import spacegame.userinterfaces.planetscreen.PlanetScreen;
import spacegame.userinterfaces.systemscreen.SystemScreen;
import spacegame.world.GameState;
import spacegame.world.GameWorld;
import spacegame.world.systems.celestialbodies.CelestialBody;

/**
 *
 * @author user
 */
public class SpaceGame extends Application {

    private static final Logger LOG = Logger.getLogger(SpaceGame.class.getName());

    private final StartScreen startScreen;

    private final SystemScreen systemScreen;
    
    private final PlanetScreen planetScreen;

    private final GameWorld gameWorld;

    private Stage stage;
    
    private ReachStartScreen comesFrom;

    /**
     * main game constructor that instanciates all the differents scenes
     */
    public SpaceGame() {
        gameWorld = new GameWorld();

        startScreen = new StartScreen(this);
        systemScreen = new SystemScreen(this);
        planetScreen = new PlanetScreen(this);
        comesFrom = systemScreen;
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        stage.setTitle("My magic space game (find a better title)!");
        stage.setScene(startScreen);
        stage.sizeToScene();

        stage.show();

        stage.setMinHeight(primaryStage.getHeight());
        stage.setMinWidth(primaryStage.getWidth());

        stage.setScene(systemScreen);

        systemScreen.finishBindings();

        stage.setScene(startScreen);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void setPlayerState(GameState gameData) {
        gameWorld.setPlayerState(gameData);
    }

    public GameState getPlayerState() {
        return gameWorld.getPlayerState();
    }

    /**
     * Change the current scene to the system screen scene
     * @return 
     */
    public boolean changeSceneToSystemScreen() {
        systemScreen.loadSystem(gameWorld.getSystem());
        LOG.log(Level.INFO, "Switch to System Screen for {0}", gameWorld.getSystem().getName());

        stage.setScene(systemScreen);
        systemScreen.giveFocusBack();

        return true;
    }
    
    /**
     * Sets the Scene to the previous one
     * @return 
     */
    public boolean returnToPreviousScreen(){
        stage.setScene(comesFrom);
        comesFrom.giveFocusBack();
        return true;
    }

    /**
     * Change the current scene to the Start screen scene
     * @param previous
     * @return 
     */
    public boolean changeSceneToStartScreen(ReachStartScreen previous) {
        LOG.info("Switch to Start Screen");
        stage.setScene(startScreen);
        comesFrom = previous;

        return true;
    }

    public void changeSceneToPlanetScreen(CelestialBody t) {
        LOG.log(Level.INFO, "Switch to Planet Screen for {0}", t.getId());
        
        planetScreen.loadPlanet(t);
    }

}
