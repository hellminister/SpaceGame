/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.startscreen;

import java.util.Optional;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import spacegame.world.GameState;
import spacegame.world.player.Gender;
import spacegame.world.player.PlayerInfo;
import spacegame.world.player.Species;

/**
 *
 * @author user
 */
class MiddlePane {

    private static final int INFOBOX_MAX_HEIGHT = 288;
    private static final int INFOBOX_PREF_HEIGHT = 288;
    private static final int INFOBOX_MIN_HEIGHT = 288;
    private static final int INFOBOX_MAX_WIDTH = 200;
    private static final int INFOBOX_PREF_WIDTH = 200;
    private static final int INFOBOX_MIN_WIDTH = 200;
    private static final int MAINPNANE_VERTICAL_SPACING = 12;
    private static final int BUTTONS_HORIZONTAL_SPACING = 10;
    private static final int BUTTONS_MAX_HEIGHT = 30;
    private static final int BUTTONS_PREF_HEIGHT = 30;
    private static final int BUTTONS_MIN_HEIGHT = 15;
    private static final int BUTTONS_MAX_WIDTH = 95;
    private static final int BUTTONS_PREF_WIDTH = 95;
    private static final int BUTTONS_MIN_WIDTH = 50;
    private static final int NEW_PLAYER_BUTTON_MAX_HEIGHT = 30;
    private static final int NEW_PLAYER_BUTTON_PREF_HEIGHT = 30;
    private static final int NEW_PLAYER_BUTTON_MIN_HEIGHT = 15;
    private static final int NEW_PLAYER_BUTTON_MAX_WIDTH = 200;
    private static final int NEW_PLAYER_BUTTON_PREF_WIDTH = 200;
    private static final int NEW_PLAYER_BUTTON_MIN_WIDTH = 50;

    private VBox mainPane;

    private ListView<String> infoBox2;
    private Button newPlayer;
    private Button saveButton;

    private Button acLoadGame;
    private Button selectPlayer;

    private TextInputDialog userAnswer;
    private PlayerList playerList;
    private final StartScreen startScreen;

    /**
     * Creates the middle pane components
     *
     * @param sS
     */
    MiddlePane(StartScreen sS) {
        startScreen = sS;

        userAnswer = new TextInputDialog();
        userAnswer.setHeaderText(null);
        userAnswer.initStyle(StageStyle.UNDECORATED);

        playerList = new PlayerList();

        createMiddlePane();

    }

    private void createMiddlePane() {
        mainPane = new VBox(MAINPNANE_VERTICAL_SPACING);
        mainPane.setAlignment(Pos.CENTER);

        infoBox2 = new ListView<>();
        infoBox2.setEditable(false);
        infoBox2.setPrefSize(INFOBOX_PREF_WIDTH, INFOBOX_PREF_HEIGHT);
        infoBox2.setMaxSize(INFOBOX_MAX_WIDTH, INFOBOX_MAX_HEIGHT);
        infoBox2.setMinSize(INFOBOX_MIN_WIDTH, INFOBOX_MIN_HEIGHT);
        infoBox2.setStyle("-fx-background-color: grey");
        infoBox2.setEditable(false);

        mainPane.getChildren().add(infoBox2);

        HBox middlePaneButtons = new HBox(BUTTONS_HORIZONTAL_SPACING);
        middlePaneButtons.setAlignment(Pos.CENTER);

        StackPane selectButtons = new StackPane();

        selectPlayer = new Button();
        selectPlayer.setText("Select");
        setMiddleButtonSizings(selectPlayer);
        selectPlayer.setOnAction(event -> {
            String selected = infoBox2.getSelectionModel().getSelectedItem();
            if ((selected != null) && !selected.isEmpty()) {
                playerList.selectPlayer(selected);
                startScreen.setPlayerInfo(playerList.load());

            }
        });

        acLoadGame = new Button();
        acLoadGame.setText("Load");
        setMiddleButtonSizings(acLoadGame);
        acLoadGame.setOnAction(event -> {
            String selected = infoBox2.getSelectionModel().getSelectedItem();
            if ((selected != null) && !selected.isEmpty()) {
                startScreen.setPlayerInfo(playerList.loadSavedGame(selected));
            }
        });

        selectButtons.getChildren().addAll(selectPlayer, acLoadGame);

        Button close = new Button();
        close.setText("Close");
        setMiddleButtonSizings(close);
        close.setOnAction(event -> {
            mainPane.setVisible(false);
        });

        middlePaneButtons.getChildren().addAll(selectButtons, close);
        mainPane.getChildren().add(middlePaneButtons);

        StackPane switchButton = new StackPane();

        newPlayer = new Button();
        newPlayer.setText("New Player");
        newPlayer.setOnAction(event -> {
            createPlayer();
            startScreen.setPlayerInfo(playerList.load());
        });

        setNewPlayerButtonSizings(newPlayer);

        switchButton.getChildren().add(newPlayer);

        saveButton = new Button();
        saveButton.setText("Save Game");
        saveButton.setOnAction(event -> {
            String saveName = getnewSaveGameName();
            playerList.saveGame(saveName, startScreen.getPlayerInfo());
        });

        setNewPlayerButtonSizings(saveButton);

        switchButton.getChildren().add(saveButton);

        mainPane.getChildren().add(switchButton);

        mainPane.setVisible(false);
    }

    private void createPlayer() {
        String firstName = getAnswer("What is your first name?");
        String name = getAnswer("What is you last name");
        Gender gender = Gender.valueOf(getAnswer("What is your gender? (Male Female Neuter)").toUpperCase());
        Species species = Species.valueOf(getAnswer("What is your species? (Human)").toUpperCase());

        PlayerInfo newPlayerInfo = new PlayerInfo(name, firstName, species, gender);
        GameState newGame = new GameState(newPlayerInfo);

        playerList.createPlayer(newGame);
    }

    /**
     * will need to refactor this for integrity checks.....
     *
     * @param question
     * @return
     */
    private String getAnswer(String question) {
        userAnswer.setContentText(question);
        userAnswer.getEditor().clear();
        Optional<String> result = userAnswer.showAndWait();
        return result.orElse("");
    }

    private void setMiddleButtonSizings(Button btn) {
        btn.setMaxSize(BUTTONS_MAX_WIDTH, BUTTONS_MAX_HEIGHT);
        btn.setMinSize(BUTTONS_MIN_WIDTH, BUTTONS_MIN_HEIGHT);
        btn.setPrefSize(BUTTONS_PREF_WIDTH, BUTTONS_PREF_HEIGHT);

    }

    private void setNewPlayerButtonSizings(Button btn) {
        btn.setMaxSize(NEW_PLAYER_BUTTON_MAX_WIDTH, NEW_PLAYER_BUTTON_MAX_HEIGHT);
        btn.setMinSize(NEW_PLAYER_BUTTON_MIN_WIDTH, NEW_PLAYER_BUTTON_MIN_HEIGHT);
        btn.setPrefSize(NEW_PLAYER_BUTTON_PREF_WIDTH, NEW_PLAYER_BUTTON_PREF_HEIGHT);
    }

    private String getnewSaveGameName() {
        return getAnswer("Enter new save name");
    }

    public Node getRootPane() {
        return mainPane;
    }

    /**
     * Loads the current player's recent GameState
     *
     * @return
     */
    public GameState load() {
        return playerList.load();
    }

    /**
     * Hides the middle Pane
     */
    public void hide() {
        mainPane.setVisible(false);
    }

    /**
     * Shows the components for the choose player option
     */
    public void showForChoosePlayer() {
        mainPane.setVisible(true);
        saveButton.setVisible(false);
        newPlayer.setVisible(true);
        acLoadGame.setVisible(false);
        selectPlayer.setVisible(true);
        infoBox2.setItems(playerList.getPlayerList());
    }

    /**
     * Shows the components for the load game option
     *
     * @param gameStarted
     */
    void showForLoadGame(boolean gameStarted) {
        mainPane.setVisible(true);
        newPlayer.setVisible(false);
        if (gameStarted) {
            saveButton.setVisible(true);
        } else {
            saveButton.setVisible(false);
        }
        selectPlayer.setVisible(false);
        acLoadGame.setVisible(true);
        infoBox2.setItems(playerList.getSaveList());
    }
}
