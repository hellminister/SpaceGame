/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame;

import spacegame.startscreen.StartScreen;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class SpaceGame extends Application {

    private static final Logger LOG = Logger.getLogger(SpaceGame.class.getName());
    
    static {
        
    }

    private StartScreen startScreen;

    @Override
    public void start(Stage primaryStage) {

        startScreen = new StartScreen(true, true);
        Scene scene = new Scene(startScreen.getStartScreenRootPane());

        primaryStage.setTitle("My magic space game (find a better title)!");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();

        primaryStage.show();

        primaryStage.setMinHeight(primaryStage.getHeight());
        primaryStage.setMinWidth(primaryStage.getWidth());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
