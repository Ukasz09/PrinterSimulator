package com.github.Ukasz09.graphiceUserInterface;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ViewManager {
    private static final String DEFAULT_FONT_FAMILY = "Helvetica";
    private static final FontWeight DEFAULT_FONT_WEIGHT = FontWeight.BOLD;
    private static final int DEFAULT_FONT_SIZE = 34;
    private static final Color DEFAULT_FONT_COLOR = Color.TAN;
    private static final int DEFAULT_GAME_FRAME_WIDTH = 1600;
    private static final int DEFAULT_GAME_FRAME_HEIGHT = 900;

    private Stage mainStage;
    private Canvas canvas;
    private GraphicsContext gc;
    private Group root;
    Scene mainScene;

    private double rightFrameBorder;
    private double bottomFrameBorder;

    private static ViewManager instance;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private ViewManager() {
        //nothing to do
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static ViewManager getInstance() {
        if (instance == null)
            instance = new ViewManager();
        return instance;
    }

    public void initialize(String title, boolean fullScreen) {
        initializeMainStage(title, fullScreen);
        root = new Group();
        setMainStageScene();
        addCanvas();
        gc = canvas.getGraphicsContext2D();
        setStartedGraphicsContextProperties();
        initializeWindowBoundary(canvas);
        scaleToProperResolution();
    }

    private void initializeMainStage(String title, boolean fullScreen) {
        mainStage = new Stage();
        mainStage.setTitle(title);
        mainStage.setWidth(DEFAULT_GAME_FRAME_WIDTH);
        mainStage.setHeight(DEFAULT_GAME_FRAME_HEIGHT);
        mainStage.setFullScreen(fullScreen);
    }

    private void setMainStageScene() {
        mainScene = new Scene(root);
        mainStage.setScene(mainScene);
    }

    private void addCanvas() {
        canvas = new Canvas(mainStage.getWidth(), mainStage.getHeight());
        root.getChildren().add(canvas);
    }

    private void setStartedGraphicsContextProperties() {
        setFillColor(DEFAULT_FONT_COLOR);
        setDefaultFont();
    }

    public void setFillColor(Color color) {
        gc.setFill(color);
    }

    private void setDefaultFont() {
        setFont(DEFAULT_FONT_FAMILY, DEFAULT_FONT_WEIGHT, DEFAULT_FONT_SIZE, DEFAULT_FONT_COLOR);
    }

    public void setFont(String family, FontWeight weight, int size, Color color) {
        try {
            Font font = Font.font(family, weight, size);
            setFont(font, color);
        } catch (Exception e) {
            setDefaultFont();
        }
    }

    private void setFont(Font font, Color color) {
        gc.setFont(font);
        gc.setFill(color);
    }

    private void initializeWindowBoundary(Canvas canvas) {
        Bounds bounds = canvas.getBoundsInLocal();
        rightFrameBorder = bounds.getMaxX();
        bottomFrameBorder = bounds.getMaxY();
    }

    private void scaleToProperResolution() {
        Point2D userResolution = getUserResolution();
        canvas.setScaleX(userResolution.getX() / DEFAULT_GAME_FRAME_WIDTH);
        canvas.setScaleY(userResolution.getY() / DEFAULT_GAME_FRAME_HEIGHT);

        double translateOffsetX = (userResolution.getX() - DEFAULT_GAME_FRAME_WIDTH) / 2;
        double translateOffsetY = (userResolution.getY() - DEFAULT_GAME_FRAME_HEIGHT) / 2;
        translateCanvas(translateOffsetX, translateOffsetY);
    }

    private Point2D getUserResolution() {
        Rectangle2D resolutionBounds = Screen.getPrimary().getBounds();
        return new Point2D(resolutionBounds.getWidth(), resolutionBounds.getHeight());
    }

    private void translateCanvas(double offsetX, double offsetY) {
        canvas.setTranslateX(offsetX);
        canvas.setTranslateY(offsetY);
    }

    public void addNode(Node iv) {
        root.getChildren().add(iv);
    }

    public void removeNode(Node iv) throws NullPointerException {
        root.getChildren().remove(iv);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Stage getMainStage() {
        return mainStage;
    }

    public GraphicsContext getGraphicContext() {
        return gc;
    }

    public double getRightFrameBorder() {
        return rightFrameBorder;
    }

    public double getBottomFrameBorder() {
        return bottomFrameBorder;
    }

}

