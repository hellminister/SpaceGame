/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.startscreen;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import spacegame.world.GameState;

/**
 *
 * @author user
 */
public class StartScreen {

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

    private VBox middlePane;

    private StackPane startScreen;

    private final StackPane root;
    private TextArea infoBox;
    private ListView<String> infoBox2;
    private Button newPlayer;
    private Button saveButton;

    private Button acLoadGame;
    private Button selectPlayer;

    private GridPane startScreenGridPane;

    private boolean gameStarted = false;
    private GameState playerInfo;

    private final PlayerList playerList;
    
    private String creditsText;
    
    private final TextInputDialog userAnswer;

    public StartScreen(boolean resize, boolean centering) {
        root = new StackPane();
        root.setStyle("-fx-background-color: black");


        createStartScreen();
        
        userAnswer = new TextInputDialog();
        userAnswer.setHeaderText(null);
        userAnswer.initStyle(StageStyle.UNDECORATED);
        

        root.setPrefSize(backgroundPlate.getLayoutBounds().getWidth(), HEIGHT);

        if (resize) {
            linkBackgroundSizeToRootPane();
        }

        root.getChildren().add(centering ? createCenteringPanesFor(startScreen) : new Pane(startScreen));

        playerList = new PlayerList();
        playerInfo = playerList.load();

        infoBox.setText(playerInfo.toString());

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

        createMiddlePane();
        startScreenGridPane.add(middlePane, 1, 0);

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

    private void createMiddlePane() {
        middlePane = new VBox(12);
        middlePane.setAlignment(Pos.CENTER);

        infoBox2 = new ListView<>();
        infoBox2.setEditable(false);
        infoBox2.setPrefSize(200, 288);
        infoBox2.setMaxSize(200, 288);
        infoBox2.setMinSize(200, 288);
        infoBox2.setStyle("-fx-background-color: grey");
        infoBox2.setEditable(false);

        middlePane.getChildren().add(infoBox2);

        HBox middlePaneButtons = new HBox(10);
        middlePaneButtons.setAlignment(Pos.CENTER);

        StackPane selectButtons = new StackPane();

        selectPlayer = new Button();
        selectPlayer.setText("Select");
        setMiddleButtonSizings(selectPlayer);
        selectPlayer.setOnAction(event -> {
            String selected = infoBox2.getSelectionModel().getSelectedItem();
            if (selected != null && !selected.isEmpty()) {
                playerList.selectPlayer(selected);
                playerInfo = playerList.load();
                infoBox.setText(playerInfo.getInfo());
            }
        });

        acLoadGame = new Button();
        acLoadGame.setText("Load");
        setMiddleButtonSizings(acLoadGame);
        acLoadGame.setOnAction(event -> {
            String selected = infoBox2.getSelectionModel().getSelectedItem();
            if (selected != null && !selected.isEmpty()) {
                playerInfo = playerList.loadSavedGame(selected);
                infoBox.setText(playerInfo.getInfo());
            }
        });

        selectButtons.getChildren().addAll(selectPlayer, acLoadGame);

        Button close = new Button();
        close.setText("Close");
        setMiddleButtonSizings(close);
        close.setOnAction(event -> {
            middlePane.setVisible(false);
        });

        middlePaneButtons.getChildren().addAll(selectButtons, close);
        middlePane.getChildren().add(middlePaneButtons);

        StackPane switchButton = new StackPane();

        newPlayer = new Button();
        newPlayer.setText("New Player");
        newPlayer.setOnAction(event -> {
            createPlayer();
            infoBox.setText(playerInfo.toString());
        });

        setNewPlayerButtonSizings(newPlayer);

        switchButton.getChildren().add(newPlayer);

        saveButton = new Button();
        saveButton.setText("Save Game");
        saveButton.setOnAction(event -> {
            String saveName = getnewSaveGameName();
            playerList.saveGame(saveName, playerInfo);
            infoBox.setText(playerInfo.getInfo());
        });

        setNewPlayerButtonSizings(saveButton);

        switchButton.getChildren().add(saveButton);

        middlePane.getChildren().add(switchButton);

        middlePane.setVisible(false);
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
            infoBox.setText("Enter Game");
            middlePane.setVisible(false);
        });

        choosePlayer = new Button();
        choosePlayer.setText("Choose Player");
        setMainButtonSizings(choosePlayer);
        choosePlayer.setOnAction(event -> {
            infoBox.setText("Choose Player");
            middlePane.setVisible(true);
            saveButton.setVisible(false);
            newPlayer.setVisible(true);
            acLoadGame.setVisible(false);
            selectPlayer.setVisible(true);
            infoBox2.setItems(playerList.getPlayerList());
        });

        loadGame = new Button();
        loadGame.setText("Load Game");
        setMainButtonSizings(loadGame);
        loadGame.setOnAction(event -> {
            infoBox.setText("Load Game");
            middlePane.setVisible(true);
            newPlayer.setVisible(false);
            if (gameStarted) {
                saveButton.setVisible(true);
            } else {
                saveButton.setVisible(false);
            }
            selectPlayer.setVisible(false);
            acLoadGame.setVisible(true);
            infoBox2.setItems(playerList.getSaveList());
        });

        credits = new Button();
        credits.setText("Credits");
        setMainButtonSizings(credits);
        credits.setOnAction(event -> {
            infoBox.setText(creditsText);
            middlePane.setVisible(false);
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

    private void setMiddleButtonSizings(Button btn) {
        btn.setMaxSize(95, 30);
        btn.setMinSize(50, 15);
        btn.setPrefSize(95, 30);

    }

    public Pane getStartScreenRootPane() {
        return root;
    }

    public Pane getStartScreenPane() {
        return startScreen;
    }

    private void setNewPlayerButtonSizings(Button btn) {
        btn.setMaxSize(200, 30);
        btn.setMinSize(50, 15);
        btn.setPrefSize(200, 30);
    }

    private void createPlayer() {
        userAnswer.setContentText("Enter your new player's name");
        userAnswer.getEditor().clear();
        Optional<String> result = userAnswer.showAndWait();
        if (result.isPresent()){
            String name = result.get();
            if (!name.isEmpty()){
                if (!playerList.nameExists(name)){
                    playerList.createPlayer(name);
                    playerInfo = playerList.load();
                }
            }
        }
    }

    private String getnewSaveGameName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void loadCreditText() {
        String text = "";
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CREDIT_FILE)))){
            String line = reader.readLine();
            while (line != null){
                text += line + "\n";
                line = reader.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(StartScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        creditsText = text;
    }

}
