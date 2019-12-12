package com.github.Ukasz09.graphiceUserInterface.sprites.computer;

import com.github.Ukasz09.graphiceUserInterface.sprites.computer.observerPattern.IEventKindObserver;
import com.github.Ukasz09.graphiceUserInterface.sprites.ImageSprite;
import com.github.Ukasz09.graphiceUserInterface.sprites.computer.eventKind.EventKind;
import com.github.Ukasz09.graphiceUserInterface.sprites.computer.panes.MonitorPane;
import com.github.Ukasz09.graphiceUserInterface.sprites.properites.ImagesProperties;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class MonitorSprite extends ImageSprite implements IEventKindObserver {
    private final static Image DEFAULT_IMAGE = ImagesProperties.monitorSprite();
    private final static double DEFAULT_MONITOR_FRAME_THICKNESS = 15;
    private final static double DEFAULT_DISPLAY_TO_MONITOR_PROPORTION = 0.68;

    private final double frameThickness;
    private final double displayToMonitorProportion;

    private MonitorPane monitorPane;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public MonitorSprite(double width, double height, double positionX, double positionY) {
        super(DEFAULT_IMAGE, width, height, positionX, positionY);
        frameThickness = DEFAULT_MONITOR_FRAME_THICKNESS;
        displayToMonitorProportion = DEFAULT_DISPLAY_TO_MONITOR_PROPORTION;
        initializeMonitorPane();

        monitorPane.attachObserver(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initializeMonitorPane() {
        Point2D panePosition = calculateDisplayPanePosition();
        Point2D paneSize = calculateDisplayPaneSize();
        monitorPane = new MonitorPane(panePosition.getX(), panePosition.getY(), paneSize.getX(), paneSize.getY());
    }

    private Point2D calculateDisplayPaneSize() {
        double paneSizeX = width - frameThickness * 2;
        double paneSizeY = height * displayToMonitorProportion;
        return new Point2D(paneSizeX, paneSizeY);
    }

    private Point2D calculateDisplayPanePosition() {
        double panePositionX = positionX + frameThickness;
        double panePositionY = positionY + frameThickness;
        return new Point2D(panePositionX, panePositionY);
    }

    @Override
    public void render() {
        super.render();
        monitorPane.render();
    }

    @Override
    public void update() {
        super.update();
        monitorPane.update();
    }

    @Override
    public void updateObserver(EventKind eventKind) {
        switch (eventKind){
//            //todo: tmp
//            case CHOOSE_PRINT_COLOR_OPTION:
//
//                    System.out.println("tell up layer to apply sepia effect");
//                break;
            default: System.out.println("Sth other");
        }
    }

    public MonitorPane getMonitorPane() {
        return monitorPane;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
