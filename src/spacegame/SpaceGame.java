/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame;

import spacegame.userinterfaces.startscreen.StartScreen;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import spacegame.userinterfaces.systemscreen.SystemScreen;
import spacegame.world.GameState;
import spacegame.world.GameWorld;

/**
 *
 * @author user
 */
public class SpaceGame extends Application {

    private static final Logger LOG = Logger.getLogger(SpaceGame.class.getName());

    private final StartScreen startScreen;

    private final SystemScreen systemScreen;

    private final GameWorld gameWorld;

    private Stage stage;

    public SpaceGame() {
        gameWorld = new GameWorld();

        startScreen = new StartScreen(true, true, this);
        systemScreen = new SystemScreen(this);
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

    public boolean changeSceneToSystemScreen() {
        systemScreen.loadSystem(gameWorld.getSystem());

        stage.setScene(systemScreen);

        return true;
    }

    public boolean changeSceneToStartScreen() {
        stage.setScene(startScreen);

        return true;
    }

}
