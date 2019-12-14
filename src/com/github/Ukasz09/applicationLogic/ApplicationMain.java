package com.github.Ukasz09.applicationLogic;

import com.github.Ukasz09.graphiceUserInterface.IDrawingGraphic;
import com.github.Ukasz09.graphiceUserInterface.IRoomGraphic;
import com.github.Ukasz09.graphiceUserInterface.Room;
import com.github.Ukasz09.graphiceUserInterface.ViewManager;
import com.github.Ukasz09.graphiceUserInterface.sprites.ISpriteGraphic;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class ApplicationMain extends Application {
    private final static String applicationTitle = "Printer Game Application";
    private final boolean fullScreen = true;

    private ViewManager manager;
    private double elapsedTime;
    private Long lastNanoTime;

    private IRoomGraphic room;

    public ApplicationMain() {
        manager = ViewManager.getInstance();
        manager.initialize(applicationTitle, fullScreen);
        lastNanoTime = System.nanoTime();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage = manager.getMainStage();
        initializeApplication();

        class gameAnimationTimer extends AnimationTimer {
            @Override
            public void handle(long currentNanoTime) {
                room.update();
                room.render();
            }
        }
        new gameAnimationTimer().start();
        primaryStage.show();
    }

    private void initializeApplication() {
        room = new Room();
        room.playBackgroundTheme();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
