package com.mygdx.game;

public class BorderWalls {
    public float x, y;
    public float realX, realY;
    public float width, height;

    public BorderWalls(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        realX = x*width;
        realY = y*height;
    }
}
