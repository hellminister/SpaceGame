/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacegame.userinterfaces.systemscreen;

import java.util.EnumMap;
import java.util.Set;
import javafx.scene.layout.StackPane;
import spacegame.SpaceGame;
import spacegame.world.systems.BubbleSystem;

/**
 *
 * This Pane contains the stars and celectial bodies of a bubble system
 * 
 * @author user
 */
public class SystemPane extends StackPane{
    
    private EnumMap<UIAction, Set<SystemScreenSprite>> sprites;
    
    
    public SystemPane(BubbleSystem system, SpaceGame mainTheater) {
        sprites = new EnumMap<>(UIAction.class);
    }
    
}
