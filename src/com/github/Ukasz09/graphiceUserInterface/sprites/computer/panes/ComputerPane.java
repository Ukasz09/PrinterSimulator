package com.github.Ukasz09.graphiceUserInterface.sprites.computer.panes;

import com.github.Ukasz09.applicationLogic.Logger;
import com.github.Ukasz09.applicationLogic.observerPattern.IObserver;
import com.github.Ukasz09.graphiceUserInterface.ViewManager;
import com.github.Ukasz09.graphiceUserInterface.sprites.computer.eventKind.EventKind;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Set;

public abstract class ComputerPane implements IPane {
    private final ViewManager manager;
    private Pane pane;

    private Set<IObserver> observers;
    private double positionX;
    private double positionY;
    private double width;
    private double height;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ComputerPane(double positionX, double positionY, double width, double height) {
        manager = ViewManager.getInstance();
        setPosition(positionX, positionY);
        setSize(width, height);
        makeMonitorPane();
        manager.addNode(pane);
        observers = new HashSet<>();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setPosition(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    private void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Use only to create instance of Pane. This method is using to choose a layouts for pane
     */
    protected abstract Pane makePaneInstance();

    private void makeMonitorPane() {
        pane = makePaneInstance();
        if (pane == null) {
            pane = new AnchorPane();
            Logger.logError(getClass(), " pane from makePaneInstance is null. Made pane by AnchorPane");
        }
        setPaneProperties();
    }

    private void setPaneProperties() {
        pane.setPrefSize(width, height);
//        pane.setStyle("-fx-background-color: red;");
        pane.setLayoutX(positionX);
        pane.setLayoutY(positionY);
    }



    @Override
    public Pane getPane() {
        return pane;
    }

    @Override
    public void attachObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detachObserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(EventKind eventKind) {
        for (IObserver observer : observers)
            observer.updateObserver(eventKind);
    }

    protected Button makeButtonWithBackground(double width, double height, Image buttonImage, EventKind eventKind) {
        Button button = new Button();
        setNormalButtonProperty(button, width, height, eventKind);
        setBackgroundToButton(button, buttonImage);
        return button;
    }

    private void setNormalButtonProperty(Button windowButton, double width, double height, EventKind eventKind) {
        windowButton.setMinSize(width, height);
        windowButton.setMaxSize(width, height);
        addButtonEventHandler(windowButton, eventKind);
    }

    private void addButtonEventHandler(Button button, EventKind eventKind) {
        button.setOnMouseClicked(event -> {
            notifyObservers(eventKind);
        });
    }

    private void setBackgroundToButton(Button button, Image buttonImage) {
        BackgroundRepeat noRepeat = BackgroundRepeat.NO_REPEAT;
        BackgroundPosition backgroundPosition = BackgroundPosition.CENTER;
        BackgroundSize backgroundSize = new BackgroundSize(button.getWidth(), button.getHeight(), false, false, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(buttonImage, noRepeat, noRepeat, backgroundPosition, backgroundSize);
        Background background = new Background(backgroundImage);
        button.setBackground(background);
    }

    protected Button makeButtonWithImage(double width, double height, Image buttonImage, EventKind eventKind) {
        Button button = new Button("", getImageViewForButton(width, height, buttonImage));
        setNormalButtonProperty(button, width, height, eventKind);
        return button;
    }

    private ImageView getImageViewForButton(double width, double height, Image buttonImage) {
        ImageView imageView = new ImageView(buttonImage);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    protected Button makeButtonWithImageAndText(double width, double height, Image buttonImage, String buttonText, EventKind eventKind) {
        Button button = new Button("", getImageViewForButton(width * 0.7, height * 0.7, buttonImage));
        setNormalButtonProperty(button, width, height, eventKind);
        setButtonTextProperty(button, buttonText);
        return button;
    }

    private void setButtonTextProperty(Button button, String buttonText) {
        button.setContentDisplay(ContentDisplay.TOP);
        button.setStyle(String.format("-fx-font-size: %dpx;", (int) (button.getMaxHeight() / 8)));
        button.setText(buttonText);
    }

    //todo: moze usunac pozniej
    @Override
    public double getPositionX() {
        return positionX;
    }

    @Override
    public double getPositionY() {
        return positionY;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }


}
