/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.systemscreen;

import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author user
 */
public class Playground extends Scene{
    
    private static final Logger LOG = Logger.getLogger(Playground.class.getName());
    
    private static final int HEIGHT = 1200;
        private static final int WIDTH = 1920;

    private static final String BACK_IMAGE_FILE_PATH = "/resources/images/PIA17563-1920x1200.jpg";
    
    private Image background;
    private ImageView backgroundPlate;
    private ScrollPane backgroundImagePane;
    
    private StackPane root;
    
    private Canvas canvas;
    
    
    public Playground() {
        super(new StackPane());
        root = (StackPane) this.getRoot();
        
        
        canvas = new Canvas(640, 480);
       
        
        createBackgroundImagePane();
        
        root.getChildren().add(backgroundImagePane);
        
//         backgroundImagePane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)-> {
//            double x = e.getSceneX();
//            double y = e.getSceneY();
//            moveCanvas(x, y);
//            
//            System.out.println("trying to move canvas to " + x + " " + y);
//    });
        
//        root.getChildren().add(canvas);
    }
    
    
    
    private void createBackgroundImagePane() {
        background = new Image(BACK_IMAGE_FILE_PATH);
        backgroundPlate = new ImageView(background);
                
        backgroundImagePane = new ScrollPane();
        backgroundImagePane.setContent(backgroundPlate);
        
        backgroundImagePane.setPrefViewportHeight(canvas.getHeight());
        backgroundImagePane.setPrefViewportWidth(canvas.getWidth());
        
        backgroundImagePane.setPannable(true);
     //   backgroundImagePane.setHmax(WIDTH);
     //   backgroundImagePane.setVmax(HEIGHT);
    }
    
    
    private void moveCanvas(double x, double y){
        backgroundImagePane.setHvalue(x/WIDTH);
        backgroundImagePane.setVvalue(y/HEIGHT);
   //     System.out.println("new values :" + backgroundImagePane.getHvalue() + " " + backgroundImagePane.getVvalue());
      //  backgroundPlate.setTranslateX(x);
      // backgroundPlate.setTranslateY(y);
       
    }

    private double x = 0;
    private double y = 0;
    
    void moveScrollPane() {
        x += 4;
        y += 4;
        moveCanvas(x, y);
    }
}
