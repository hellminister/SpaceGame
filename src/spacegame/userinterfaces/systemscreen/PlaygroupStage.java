/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.systemscreen;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class PlaygroupStage extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        Playground pg = new Playground();
        primaryStage.setScene(pg);
        primaryStage.setWidth(640);
        primaryStage.setHeight(480);
        primaryStage.show();
        
        Automove am = new Automove(pg);
        am.start();
    }
    
       /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static class Automove extends AnimationTimer{
        
        Playground pg;
        
        public Automove(Playground p){
            pg = p;
        }
        

        private long last = 0;
        private int nbFrame = 0;
        
        @Override
        public void handle(long now) {
            pg.moveScrollPane();
            nbFrame++;
            long secs = now-last;
            if (secs > 1000000000){
                last = now;
                System.out.println((double)secs/1000000000 + " fps" + nbFrame);
                nbFrame = 0;
            }
        }
        
    }
    
}
