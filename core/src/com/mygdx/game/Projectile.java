package com.mygdx.game;
public class Projectile extends Project1{
    public float x,y;
    public int antiEnemyDamage, antiWallDamage;
    public float width, height;
    public float speed;
   public int sideCounter = 0;
   public boolean hit, ammoSummon = true;
    public Projectile(float x, float y, int antiWallDamage, float width, float height, float speed, int sideCounter, boolean hit) {
        this.x = x;
        this.y = y;
        this.antiWallDamage = antiWallDamage;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.sideCounter = sideCounter;
        this.hit =hit;

    }
    void hit(){
        x = SCR_WIDTH+1000;
        y = SCR_HEIGHT+1000;

        hit = true;

    }
    void moveLeftX(){
        x -= speed;
    }
    void moveRightX(){

        x += speed;
    }
    void moveUpY(){
        y += speed;
    }
    void moveDownY(){
        y -= speed;
    }
}
