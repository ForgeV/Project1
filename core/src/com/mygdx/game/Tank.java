package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tank extends Project1{
    public float x, y;
    public float targetX, targetY;
    public float realX, realY;
    public float stepX, stepY;
    public int steps = 15;
    public float width, height;
    public int hitpoints;
    SpriteBatch batch;
    public Tank(int x, int y, float width, float height, int hitpoints) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitpoints = hitpoints;
        batch = new SpriteBatch();
        realX = x*WIDTH;
        realY = y*WIDTH;
        stepX = width/ steps;
        stepY = height/ steps;
        targetX = x;
        targetY = y;
    }
    void moveLeftX(){
            realX -= stepX;

    }
    void moveRightX(){
        realX += stepX;


    }
    void moveUpY() {
        realY += stepY;


    }
    void moveDownY(){
        realY -= stepY;


    }
    }


