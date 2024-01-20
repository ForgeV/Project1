package com.mygdx.game;

public class Walls {
    public float x, y;
    public float realX, realY;
    public float width, height;
    public int durability;
    public Walls(float x, float y, float width, float height, int durability) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.durability = durability;
        realX = x*width;
        realY = y*height;
    }

}
