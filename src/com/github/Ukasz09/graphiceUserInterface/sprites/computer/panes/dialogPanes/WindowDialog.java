package com.github.Ukasz09.graphiceUserInterface.sprites.computer.panes.dialogPanes;

import com.github.Ukasz09.graphiceUserInterface.sprites.computer.panes.ComputerPane;
import com.github.Ukasz09.graphiceUserInterface.sprites.computer.panes.ComputerPaneWithGraphicContext;
import com.github.Ukasz09.graphiceUserInterface.sprites.computer.panes.contentPanes.ContentPane;
import com.github.Ukasz09.graphiceUserInterface.sprites.computer.panes.taskbars.Taskbar;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

public abstract class WindowDialog extends ComputerPaneWithGraphicContext {
    protected static final String DEFAULT_DARK_THEME_COLOR = "#303030";
    private static final String DEFAULT_BRIGHT_THEME_COLOR = "#d5e5f8";
    protected static final double DEFAULT_TASKBAR_HEIGHT_TO_WINDOW_PROPORTION = 0.12;

    private static String actualThemeColor = DEFAULT_BRIGHT_THEME_COLOR;
    private final ContentPane contentPane;
    private final Taskbar windowTaskbarPane;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public WindowDialog(double positionX, double positionY, double width, double height, double opacity) {
        super(positionX, positionY, width, height);
        getPane().setOpacity(opacity);
        windowTaskbarPane = makeWindowTaskbarPane();
        addNodeToPane(windowTaskbarPane.getPane());
        contentPane = makeContentPaneInstance();
        addContentPaneToNode();

        windowTaskbarPane.attachObserver(this);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected Pane makePaneInstance() {
        AnchorPane pane = new AnchorPane();
        setPanelColor(pane, actualThemeColor);
        return pane;
    }

    protected abstract ContentPane makeContentPaneInstance();

    protected abstract Taskbar makeWindowTaskbarPane();

    private void addContentPaneToNode() {
        AnchorPane.setTopAnchor(contentPane.getPane(), windowTaskbarPane.getHeight());
        addNodeToPane(contentPane.getPane());
    }

    @Override
    public void update() {
        setPanelColor(getPane(), actualThemeColor);
        System.out.println("Zmieniono na: "+actualThemeColor);
    }

    @Override
    public void render() {
        //nothing to do
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void addButtonToContentPane(Button button) {
        contentPane.addNodeToPane(button);
    }

    protected double getWindowTaskbarHeight() {
        return windowTaskbarPane.getHeight();
    }

    protected static void changeThemeColor() {
        changeActualThemeColor();
        System.out.println("Aktualny: "+actualThemeColor);
    }

    private static void changeActualThemeColor() {
        if (WindowDialog.actualThemeColor.equals(DEFAULT_BRIGHT_THEME_COLOR))
            WindowDialog.actualThemeColor = DEFAULT_DARK_THEME_COLOR;
        else WindowDialog.actualThemeColor = DEFAULT_BRIGHT_THEME_COLOR;
    }
}
