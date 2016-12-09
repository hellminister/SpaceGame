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
public class StartScreen extends Scene {

    private static final Logger LOG = Logger.getLogger(StartScreen.class.getName());
    private static final int HEIGHT = 480;
    private static final String BACK_IMAGE_FILE_PATH = "/resources/images/PIA17563-1920x1200.jpg";
    private static final String CREDIT_FILE = "src/resources/data/credits.txt";

    private static final int GRID_PANE_MENU_THIRD = 50;
    private static final int MAIN_BUTTONS_X_TRANSLATE = -10;
    private static final int LEFT_BUTTON_INSET = 16;
    private static final int BOTTOM_BUTTON_INSET = 10;
    private static final int RIGHT_BUTTON_INSET = 0;
    private static final int TOP_BUTTON_INSET = 0;
    private static final int MAIN_BUTTONS_VERTICAL_SPACING = 12;
    private static final int MAIN_BUTTON_MAX_HEIGHT = 30;
    private static final int MAIN_BUTTON_PREF_HEIGHT = 30;
    private static final int MAIN_BUTTON_MIN_HEIGHT = 15;
    private static final int MAIN_BUTTON_MAX_WIDTH = 100;
    private static final int MAIN_BUTTON_PREF_WIDTH = 100;
    private static final int MAIN_BUTTON_MIN_WIDTH = 50;
    private static final int FIRST_ROW = 0;
    private static final int SECOND_COLUMN = 1;
    private static final int THIRD_COLUMN = 2;
    private static final int FIRST_COLUMN = 0;
    private static final int FIRST_ROW_HEIGHT_PERCENT = 100;
    private static final int MAIN_INFOBOX_MAX_HEIGHT = 330;
    private static final int MAIN_INFOBOX_PREF_HEIGHT = 330;
    private static final int MAIN_INFOBOX_MIN_HEIGHT = 330;
    private static final int MAIN_INFOBOX_MAX_WIDTH = 200;
    private static final int MAIN_INFOBOX_PREF_WIDTH = 200;
    private static final int MAIN_INFOBOX_MIN_WIDTH = 200;
    private static final int MAIN_INFOBOX_X_TRANSLATE = 20;

    private Image background;
    private ImageView backgroundPlate;
    private Pane backgroundImagePane;

    private Button enterGame;
    private Button choosePlayer;
    private Button loadGame;
    private Button credits;

    private VBox mainButtons;
    private Insets padding;

    private StackPane mainPane;

    private final StackPane root;
    private TextArea infoBox;

    private GridPane startScreenGridPane;

    private boolean gameStarted;
    private final SpaceGame mainTheater;

    private MiddlePane middlePane;

    private String creditsText;

    /**
     * Creates the components for the Start Screen
     *
     * @param aThis
     */
    public StartScreen(SpaceGame aThis) {
        super(new StackPane());

        gameStarted = false;

        mainTheater = aThis;

        root = (StackPane) this.getRoot();
        root.setStyle("-fx-background-color: black");

        createStartScreen();

        root.setPrefSize(backgroundPlate.getLayoutBounds().getWidth(), HEIGHT);

        linkBackgroundSizeToRootPane();

        root.getChildren().add(createCenteringPanesFor(mainPane));

        GameState gameData = middlePane.load();

        if (gameData == null) {
            infoBox.setText("No Players, please create a player");
        } else {
            mainTheater.setPlayerState(gameData);
            infoBox.setText(gameData.getInfo());
        }
    }

    private void createStartScreen() {
        mainPane = new StackPane();
        mainPane.setAlignment(Pos.CENTER);

        loadCreditText();

        createBackgroundImagePane();
        mainPane.getChildren().add(backgroundImagePane);

        createStartMenuGridPane();

        mainPane.getChildren().add(startScreenGridPane);

        createButtons();
        startScreenGridPane.add(mainButtons, FIRST_COLUMN, FIRST_ROW);

        createInfoBox();
        startScreenGridPane.add(infoBox, THIRD_COLUMN, FIRST_ROW);

        middlePane = new MiddlePane(this);
        startScreenGridPane.add(middlePane.getRootPane(), SECOND_COLUMN, FIRST_ROW);

    }

    private void createStartMenuGridPane() {
        startScreenGridPane = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(GRID_PANE_MENU_THIRD);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(GRID_PANE_MENU_THIRD);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setHalignment(HPos.CENTER);
        column3.setPercentWidth(GRID_PANE_MENU_THIRD);
        startScreenGridPane.getColumnConstraints().addAll(column1, column2, column3);
        RowConstraints row1 = new RowConstraints();
        row1.setValignment(VPos.CENTER);
        row1.setPercentHeight(FIRST_ROW_HEIGHT_PERCENT);
        startScreenGridPane.getRowConstraints().add(row1);
        startScreenGridPane.setPrefSize(mainPane.getPrefWidth(), mainPane.getPrefHeight());
    }

    private void createInfoBox() {
        infoBox = new TextArea();
        infoBox.setEditable(false);
        infoBox.setWrapText(true);
        infoBox.setPrefSize(MAIN_INFOBOX_PREF_WIDTH, MAIN_INFOBOX_PREF_HEIGHT);
        infoBox.setMaxSize(MAIN_INFOBOX_MAX_WIDTH, MAIN_INFOBOX_MAX_HEIGHT);
        infoBox.setMinSize(MAIN_INFOBOX_MIN_WIDTH, MAIN_INFOBOX_MIN_HEIGHT);
        infoBox.setTranslateX(MAIN_INFOBOX_X_TRANSLATE);

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
            if (mainTheater.getPlayerState() == null){
                infoBox.setText("No loaded game, Please load a game or create a new player!");
            } else if (!gameStarted){
                LOG.info("coming from game not started");
                gameStarted = mainTheater.changeSceneToSystemScreen();
            } else {
                LOG.info("coming from game started");
                gameStarted = mainTheater.returnToPreviousScreen();
            }

            
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

        mainButtons = new VBox(MAIN_BUTTONS_VERTICAL_SPACING);
        padding = new Insets(TOP_BUTTON_INSET, RIGHT_BUTTON_INSET, BOTTOM_BUTTON_INSET, LEFT_BUTTON_INSET);
        mainButtons.setPadding(padding);
        mainButtons.setAlignment(Pos.CENTER);

        mainButtons.getChildren().addAll(enterGame, choosePlayer, loadGame, credits);
        mainButtons.setTranslateX(MAIN_BUTTONS_X_TRANSLATE);
    }

    private void setMainButtonSizings(Button btn) {
        btn.setMaxSize(MAIN_BUTTON_MAX_WIDTH, MAIN_BUTTON_MAX_HEIGHT);
        btn.setMinSize(MAIN_BUTTON_MIN_WIDTH, MAIN_BUTTON_MIN_HEIGHT);
        btn.setPrefSize(MAIN_BUTTON_PREF_WIDTH, MAIN_BUTTON_PREF_HEIGHT);

    }

    public Pane getStartScreenRootPane() {
        return root;
    }

    public Pane getStartScreenPane() {
        return mainPane;
    }

    private void loadCreditText() {
        StringBuilder text = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CREDIT_FILE)))) {
            String line = reader.readLine();
            while (line != null) {
                text.append(line).append("\n");
                line = reader.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(StartScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

        creditsText = text.toString();
    }

    /**
     * Sets the current GameState
     *
     * @param load
     */
    public void setPlayerInfo(GameState load) {
        mainTheater.setPlayerState(load);
        infoBox.setText(load.getInfo());
    }

    /**
     * returns the Current GameState
     *
     * @return
     */
    public GameState getPlayerInfo() {
        return mainTheater.getPlayerState();
    }

}
