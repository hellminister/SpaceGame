/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.systemscreen;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Screen;

/**
 *
 * @author user
 */
public class MovingBackground extends Parent {

    private static final Logger LOG = Logger.getLogger(MovingBackground.class.getName());

    private static final String BACK_IMAGE_TILE_FILE_PATH = "/resources/images/tiles/1.jpg";
    private static final int EXTRA_TILES = 2;
    private static final double HALF = 0.5;

    private final Image backgroundTile;
    private final Canvas drawnBackground;

    private DoubleBinding horizontalTranslate;
    private DoubleBinding verticalTranslate;

    /**
     * Initialize a moving background with the default values
     */
    public MovingBackground() {
        backgroundTile = new Image(BACK_IMAGE_TILE_FILE_PATH);

        double maxScreenWidth = Screen.getScreens().stream().mapToDouble(value -> {
                return value.getBounds().getWidth();
        }).max().orElse(0.0);

        double maxScreenHeight = Screen.getScreens().stream().mapToDouble(value -> {
                return value.getBounds().getHeight();
        }).max().orElse(0.0);

        double width = backgroundTile.getWidth();
        double height = backgroundTile.getHeight();

        int nbTileForWidth = (int) Math.round(maxScreenWidth / width) + EXTRA_TILES;
        int nbTileForHeight = (int) Math.round(maxScreenHeight / height) + EXTRA_TILES;

        LOG.log(Level.INFO, "Max sizes of screen (w*h) {0} * {1}", new Object[]{maxScreenWidth, maxScreenHeight});
        LOG.log(Level.INFO, "Number of background tiles (w*h) {0} * {1}", new Object[]{nbTileForWidth, nbTileForHeight});

        drawnBackground = new Canvas(nbTileForWidth * width, nbTileForHeight * height);
        GraphicsContext gc = drawnBackground.getGraphicsContext2D();
        for (int i = 0; i < nbTileForWidth; i++) {
            for (int j = 0; j < nbTileForHeight; j++) {
                gc.drawImage(backgroundTile, width * i, height * j);
            }
        }
        super.getChildren().add(drawnBackground);

    }

    /**
     * Binds the background squares so that the ship is mostly always centered
     * in the squares
     *
     * @param posXProperty
     * @param posYProperty
     */
    public void bindTo2(ReadOnlyDoubleProperty posXProperty, ReadOnlyDoubleProperty posYProperty) {
        horizontalTranslate = new TranslateBinding(posXProperty, getTranslateX(), backgroundTile.getWidth());
        translateXProperty().bind(horizontalTranslate);

        verticalTranslate = new TranslateBinding(posYProperty, getTranslateY(), backgroundTile.getHeight());
        translateYProperty().bind(verticalTranslate);

    }

    private static class TranslateBinding extends DoubleBinding {

        protected double trans;
        protected ReadOnlyDoubleProperty position;
        protected double dimension;

        TranslateBinding(ReadOnlyDoubleProperty pos, double translate, double imgDim) {
            super.bind(pos);
            trans = translate;
            dimension = imgDim;
            position = pos;
        }

        @Override
        protected double computeValue() {
            double pos = position.get();
            if (pos < (trans - (dimension * HALF))) {
                trans -= dimension;
            } else if (pos > (trans + (dimension * HALF))) {
                trans += dimension;
            }
            return trans;
        }
    }

}
