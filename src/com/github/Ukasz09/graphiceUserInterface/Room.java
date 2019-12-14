package com.github.Ukasz09.graphiceUserInterface;

import com.github.Ukasz09.applicationLogic.Logger;
import com.github.Ukasz09.graphiceUserInterface.backgrounds.Background;
import com.github.Ukasz09.graphiceUserInterface.backgrounds.RoomBackground;
import com.github.Ukasz09.graphiceUserInterface.sprites.ISpriteGraphic;
import com.github.Ukasz09.graphiceUserInterface.sprites.computer.ComputerSprite;
import com.github.Ukasz09.graphiceUserInterface.sprites.computer.eventKind.EventKind;
import com.github.Ukasz09.graphiceUserInterface.sprites.computer.observerPattern.IEventKindObserver;
import com.github.Ukasz09.graphiceUserInterface.sprites.decorations.*;
import com.github.Ukasz09.graphiceUserInterface.sprites.decorations.animatedDecorations.CatSprite;
import com.github.Ukasz09.graphiceUserInterface.sprites.decorations.animatedDecorations.GlobeSprite;
import com.github.Ukasz09.graphiceUserInterface.sprites.decorations.normalDecorations.DeskSprite;
import com.github.Ukasz09.graphiceUserInterface.sprites.decorations.normalDecorations.FlowerSprite;
import com.github.Ukasz09.graphiceUserInterface.sprites.decorations.normalDecorations.ZingsPosterSprite;
import com.github.Ukasz09.graphiceUserInterface.sprites.printer.PrinterSprite;
import javafx.geometry.Point2D;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.Ukasz09.graphiceUserInterface.sprites.decorations.DecorationsEnum.*;

public class Room implements IRoomGraphic, IEventKindObserver {
    private ViewManager manager;
    private Background background;
    private Map<DecorationsEnum, ISpriteGraphic> decorations;
    private PrinterSprite printerSprite;
    private ComputerSprite computerSprite;
    private ZingsPosterSprite posterSprite;

    public Room() {
        manager = ViewManager.getInstance();
        background = new RoomBackground();
        decorations = new LinkedHashMap<>();
        addDefaultDecorations();
        addPoster();
        addPrinter();
        addComputer(printerSprite);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void addDefaultDecorations() {
        addDesk();
        ISpriteGraphic desk = decorations.get(DESK);
        addGlobe(desk.getPositionX(), desk.getPositionY());
        addCat();
        addFlower();
    }

    private void addDesk() {
        Point2D position = calculateDeskPosition();
        DeskSprite deskSprite=new DeskSprite(position.getX(), position.getY());
        decorations.put(DESK, deskSprite);
    }

    private Point2D calculateDeskPosition() {
        double positionX = manager.getRightFrameBorder() / 2 - DeskSprite.WIDTH_TO_FRAME_PROPORTION * manager.getRightFrameBorder() / 2;
        double positionY = manager.getBottomFrameBorder() - DeskSprite.HEIGHT_TO_FRAME_PROPORTION * manager.getBottomFrameBorder() - background.getFloorHeight();
        return new Point2D(positionX, positionY);
    }

    private void addGlobe(double deskPositionX, double deskPositionY) {
        Point2D position = calculateGlobePosition(deskPositionX, deskPositionY);
        decorations.put(DecorationsEnum.GLOBE, new GlobeSprite(position.getX(), position.getY()));
    }

    private Point2D calculateGlobePosition(double deskPositionX, double deskPositionY) {
        double positionX = deskPositionX;
        double positionY = deskPositionY - GlobeSprite.HEIGHT_TO_FRAME_PROPORTION * manager.getBottomFrameBorder();
        return new Point2D(positionX, positionY);
    }

    private void addCat() {
        Point2D position = calculateCatPosition();
        decorations.put(DecorationsEnum.CAT, new CatSprite(position.getX(), position.getY()));
    }

    private Point2D calculateCatPosition() {
        double positionX = manager.getRightFrameBorder() - CatSprite.WIDTH_TO_FRAME_PROPORTION * manager.getRightFrameBorder();
        double positionY = manager.getBottomFrameBorder() - background.getFloorHeight() - CatSprite.HEIGHT_TO_FRAME_PROPORTION * manager.getBottomFrameBorder();
        return new Point2D(positionX, positionY);
    }

    private void addFlower() {
        Point2D position = calculateFlowerPosition();
        decorations.put(DecorationsEnum.FLOWER, new FlowerSprite(position.getX(), position.getY()));
    }

    private Point2D calculateFlowerPosition() {
        double positionX = 0;
        double positionY = manager.getBottomFrameBorder() - background.getFloorHeight() - FlowerSprite.HEIGHT_TO_FRAME_PROPORTION * manager.getBottomFrameBorder();
        return new Point2D(positionX, positionY);
    }

    //todo: dodac go jako osobny interfejs moze
    private void addPoster() {
        Point2D position = calculatePosterPosition();
        posterSprite = new ZingsPosterSprite(position.getX(), position.getY());
        posterSprite.attachObserver(this);
    }

    private Point2D calculatePosterPosition() {
        double positionX, positionY;
        positionX = positionY = ZingsPosterSprite.FRAME_THICKNESS_TO_FRAME_WIDTH_PROPORTION * manager.getRightFrameBorder() * 2;
        return new Point2D(positionX, positionY);
    }

    private void addPrinter() {
        ISpriteGraphic desk = decorations.get(DESK);
        Point2D position = calculatePrinterPosition(desk.getPositionX(), desk.getPositionY(), desk.getWidth());
        printerSprite = new PrinterSprite(position.getX(), position.getY());
    }

    private void addComputer(PrinterSprite printerSprite) {
        ISpriteGraphic globe = decorations.get(GLOBE);
        ISpriteGraphic desk = decorations.get(DESK);
        Point2D position = calculateComputerPosition(globe.getPositionX(), globe.getWidth(), desk.getPositionY());
        computerSprite = new ComputerSprite(position.getX(), position.getY(), printerSprite, posterSprite.getSpriteImage());
        printerSprite.attachObserver(computerSprite);
    }

    private Point2D calculatePrinterPosition(double deskPositionX, double deskPositionY, double deskWidth) {
        double positionX = deskPositionX + deskWidth * 0.95 - PrinterSprite.WIDTH_TO_FRAME_PROPORTION*manager.getRightFrameBorder();
        double positionY = deskPositionY - PrinterSprite.HEIGHT_TO_FRAME_PROPORTION*manager.getBottomFrameBorder();
        return new Point2D(positionX, positionY);
    }

    private Point2D calculateComputerPosition(double globePositionX, double globeWidth, double deskPositionY) {
        double positionX = globePositionX + globeWidth;
        double positionY = deskPositionY - ComputerSprite.MONITOR_HEIGHT_TO_FRAME_PROPORTION * manager.getBottomFrameBorder();
        return new Point2D(positionX, positionY);
    }

    @Override
    public void update() {
        updateDecorations();
        printerSprite.update();
        computerSprite.update();
        posterSprite.update();
    }

    private void updateDecorations() {
        decorations.forEach((k, v) -> v.update());
    }

    @Override
    public void render() {
        background.render();
        renderDecorations();
        posterSprite.render();
        printerSprite.render();
        computerSprite.render();
    }

    private void renderDecorations() {
        decorations.forEach((k, v) -> v.render());
    }

    @Override
    public void playBackgroundTheme() {
        background.playBackgroundSound();
    }

    @Override
    public void stopBackgroundTheme() {
        background.stopBackgroundSound();
    }

    @Override
    public void updateObserver(EventKind eventKind) {
        switch (eventKind) {
            case POSTER_CHANGE:
                computerSprite.updatePrintImage(posterSprite.getSpriteImage());
                break;

            default:
                Logger.logError(getClass(), " unknown event");
        }
    }
}
