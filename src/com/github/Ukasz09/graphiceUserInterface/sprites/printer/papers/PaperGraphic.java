package com.github.Ukasz09.graphiceUserInterface.sprites.printer.papers;

import com.github.Ukasz09.graphiceUserInterface.ViewManager;
import javafx.scene.paint.Color;

public abstract class PaperGraphic implements IPaperGraphic {
    protected static final double DEFAULT_WIDTH = 100;
    protected static final double DEFAULT_HEIGHT = 100;
    protected static final double DEFAULT_STROKE_WIDTH = 3;
    protected static final Color DEFAULT_STROKE_COLOR = Color.BLACK;
    protected static final Color DEFAULT_PAPER_COLOR = Color.WHITE;
    private static final double DEFAULT_TIME_ON_ONE_FRAME_IN_ANIMATION = 5;

    protected ViewManager manager;
    protected double width;
    protected double height;
    protected double positionX;
    protected double positionY;
    private double strokeWidth;
    private Color strokeColor;
    private Color paperColor;

    protected boolean doingAnimationNow;
    private double timeOnFrameInAnimation;
    private double actualCooldownOnFrame;  //cooldown to make animation slower (stop on frame for qty of clock ticks)
    protected double animationSpeed;
    protected boolean canBeDestroyedNow;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public PaperGraphic(double positionX, double positionY, double animationSpeed) {
        manager = ViewManager.getInstance();
        width = DEFAULT_WIDTH;
        height = DEFAULT_HEIGHT;
        strokeWidth = DEFAULT_STROKE_WIDTH;
        strokeColor = DEFAULT_STROKE_COLOR;
        paperColor = DEFAULT_PAPER_COLOR;
        actualCooldownOnFrame = timeOnFrameInAnimation = DEFAULT_TIME_ON_ONE_FRAME_IN_ANIMATION;

        doingAnimationNow = false;
        canBeDestroyedNow = false;
        this.animationSpeed = animationSpeed;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void update() {
        if (doingAnimationNow) {
            if (needToChangeFrame()) {
                nextFrameOfAnimation();
                restoreAnimationTimer();
            } else reduceAnimationTimer();
        }

        if (needToStopAnimation())
            actionWhenAnimationStopped();
    }

    private boolean needToChangeFrame() {
        return actualCooldownOnFrame <= 0;
    }

    protected abstract void nextFrameOfAnimation();

    private void restoreAnimationTimer() {
        actualCooldownOnFrame = timeOnFrameInAnimation;
    }

    private void reduceAnimationTimer() {
        actualCooldownOnFrame -= 1;
    }

    protected abstract boolean needToStopAnimation();

    protected abstract void actionWhenAnimationStopped();

    protected void drawStrokeRect() {
        setColor(strokeColor);
        setLineWidth(strokeWidth);
        manager.getGraphicContext().strokeRect(positionX, positionY, width, height);
    }

    private void setColor(Color color) {
        manager.setFillColor(color);
    }

    private void setLineWidth(double lineWidth) {
        if (lineWidth < 1)
            manager.getGraphicContext().setLineWidth(DEFAULT_STROKE_WIDTH);
        else manager.getGraphicContext().setLineWidth(lineWidth);
    }

    protected void drawFillRect() {
        setColor(paperColor);
        setLineWidth(strokeWidth);
        manager.getGraphicContext().fillRect(positionX, positionY, width, height);
    }

    @Override
    public void doAnimation() {
        doingAnimationNow = true;
    }

    @Override
    public boolean canBeDestroyedNow() {
        return canBeDestroyedNow;
    }
}
