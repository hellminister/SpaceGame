/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.startscreen;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import spacegame.SpaceGame;
import spacegame.world.GameState;

/**
 *
 * @author user
 */
public class StartScreen extends Scene{
    
    private static final int HEIGHT = 480;
    private static final String BACK_IMAGE_FILE_PATH = "/resources/images/PIA17563-1920x1200.jpg";
    private static final String CREDIT_FILE = "src/resources/data/credits.txt";
    
    private Image background;
    private ImageView backgroundPlate;
    private Pane backgroundImagePane;
    
    private Button enterGame;
    private Button choosePlayer;
    private Button loadGame;
    private Button credits;
    
    private VBox mainButtons;
    private Insets padding;
    
    private StackPane startScreen;
    
    private final StackPane root;
    private TextArea infoBox;
    
    private GridPane startScreenGridPane;
    
    private boolean gameStarted = false;
    private SpaceGame mainTheater;
    
    private MiddlePane middlePane;
    
    private String creditsText;
    
    
    public StartScreen(boolean resize, boolean centering, SpaceGame aThis) {
        super(new StackPane());
        
        mainTheater = aThis;
        
        root = (StackPane) this.getRoot();
        root.setStyle("-fx-background-color: black");
        
        createStartScreen();
        
        root.setPrefSize(backgroundPlate.getLayoutBounds().getWidth(), HEIGHT);
        
        if (resize) {
            linkBackgroundSizeToRootPane();
        }
        
        root.getChildren().add(centering ? createCenteringPanesFor(startScreen) : new Pane(startScreen));
        
        GameState gameData = middlePane.load();
        
        if (gameData == null) {
            infoBox.setText("No Players, please create a player");
        } else {
            mainTheater.setPlayerState(gameData);
            infoBox.setText(gameData.getInfo());
        }
    }

    private void createStartScreen() {
        startScreen = new StackPane();
        startScreen.setAlignment(Pos.CENTER);
        
        loadCreditText();
        
        createBackgroundImagePane();
        startScreen.getChildren().add(backgroundImagePane);
        
        createStartMenuGridPane();
        
        startScreen.getChildren().add(startScreenGridPane);
        
        createButtons();
        startScreenGridPane.add(mainButtons, 0, 0);
        
        createInfoBox();
        startScreenGridPane.add(infoBox, 2, 0);
        
        middlePane = new MiddlePane(this);
        startScreenGridPane.add(middlePane.getRootPane(), 1, 0);
        
    }
    
    private void createStartMenuGridPane() {
        startScreenGridPane = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setHalignment(HPos.CENTER);
        column3.setPercentWidth(50);
        startScreenGridPane.getColumnConstraints().addAll(column1, column2, column3);
        RowConstraints row1 = new RowConstraints();
        row1.setValignment(VPos.CENTER);
        row1.setPercentHeight(100);
        startScreenGridPane.getRowConstraints().add(row1);
        startScreenGridPane.setPrefSize(startScreen.getPrefWidth(), startScreen.getPrefHeight());
    }
    
    private void createInfoBox() {
        infoBox = new TextArea();
        infoBox.setEditable(false);
        infoBox.setWrapText(true);
        infoBox.setPrefSize(200, 330);
        infoBox.setMaxSize(200, 330);
        infoBox.setMinSize(200, 330);
        infoBox.setTranslateX(20);
        
        infoBox.setStyle("-fx-background-color: grey");
    }
    
    private void createBackgroundImagePane() {
        background = new Image(BACK_IMAGE_FILE_PATH);
        backgroundPlate = new ImageView(background);
        backgroundPlate.setPreserveRatio(true);
        
        backgroundPlate.setFitHeight(HEIGHT);
        
        backgroundImagePane = new Pane();
        backgroundImagePane.getChildren().add(backgroundPlate);
    }
    
    private void linkBackgroundSizeToRootPane() {
        backgroundPlate.fitHeightProperty().bind(root.heightProperty());
        backgroundPlate.fitWidthProperty().bind(root.widthProperty());
    }
    
    private VBox createCenteringPanesFor(Pane toCenter) {
        VBox verticalCentering = new VBox();
        verticalCentering.setAlignment(Pos.CENTER);
        
        HBox horizontalCentering = new HBox();
        horizontalCentering.setAlignment(Pos.CENTER);
        
        verticalCentering.getChildren().add(horizontalCentering);
        horizontalCentering.getChildren().add(toCenter);
        
        return verticalCentering;
    }
    
    private void createButtons() {
        enterGame = new Button();
        enterGame.setText("Enter Game");
        setMainButtonSizings(enterGame);
        enterGame.setOnAction(event -> {
            // add a check to see if a gamestate is loaded
            boolean success = mainTheater.changeSceneToSystemScreen();
            
            infoBox.setText("Enter Game");
            middlePane.hide();
        });
        
        choosePlayer = new Button();
        choosePlayer.setText("Choose Player");
        setMainButtonSizings(choosePlayer);
        choosePlayer.setOnAction(event -> {
            infoBox.setText("Choose Player");
            middlePane.showForChoosePlayer();
        });
        
        loadGame = new Button();
        loadGame.setText("Load Game");
        setMainButtonSizings(loadGame);
        loadGame.setOnAction(event -> {
            infoBox.setText("Load Game");
            middlePane.showForLoadGame(gameStarted);
        });
        
        credits = new Button();
        credits.setText("Credits");
        setMainButtonSizings(credits);
        credits.setOnAction(event -> {
            infoBox.setText(creditsText);
            middlePane.hide();
        });
        
        mainButtons = new VBox(12);
        padding = new Insets(0, 0, 10, 16);
        mainButtons.setPadding(padding);
        mainButtons.setAlignment(Pos.CENTER);
        
        mainButtons.getChildren().addAll(enterGame, choosePlayer, loadGame, credits);
        mainButtons.setTranslateX(-10);
    }
    
    private static final Logger LOG = Logger.getLogger(StartScreen.class.getName());
    
    private void setMainButtonSizings(Button btn) {
        btn.setMaxSize(100, 30);
        btn.setMinSize(50, 15);
        btn.setPrefSize(100, 30);
        
    }
    
    public Pane getStartScreenRootPane() {
        return root;
    }
    
    public Pane getStartScreenPane() {
        return startScreen;
    }
    
    private void loadCreditText() {
        String text = "";
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CREDIT_FILE)))) {
            String line = reader.readLine();
            while (line != null) {
                text += line + "\n";
                line = reader.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(StartScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        creditsText = text;
    }
    
    void setPlayerInfo(GameState load) {
        mainTheater.setPlayerState(load);
        infoBox.setText(load.getInfo());
    }
    
    public GameState getPlayerInfo() {
        return mainTheater.getPlayerState();
    }

    
}
