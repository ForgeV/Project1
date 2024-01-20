package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;


public class Project1 extends ApplicationAdapter {
    public static final float SCR_WIDTH = 1280, SCR_HEIGHT = 720;
    public static final int LAB_WIDTH = 32, LAB_HEIGHT = 18;
    public static final float WIDTH = SCR_WIDTH / LAB_WIDTH, HEIGHT = SCR_HEIGHT / LAB_HEIGHT;
    public int[][] BuildMassive = new int[LAB_WIDTH][LAB_HEIGHT + 1];
    public int[] BasicAmmoChecker = new int[10000];
    OrthographicCamera camera;
    Vector3 touch;

    SpriteBatch batch;
    Texture BackgroundImg, BrickWallImg, PlayerUpYImg, PlayerDownYImg, PlayerLeftXImg, PlayerRightXImg, SteelWallImg, BasicProjectileUpYImg, BasicProjectileDownYImg, BasicProjectileLeftXImg, BasicProjectileRightXImg;

    Walls[] walls = new Walls[LAB_HEIGHT * LAB_WIDTH];
    BorderWalls[] borderwalls = new BorderWalls[LAB_HEIGHT * 2 + LAB_WIDTH * 2];

    Tank tank;
    Projectile[] basicAmmo;
    Projectile[] enemyBasicAmmo;
    Enemy[] enemies = new Enemy[5];
    double PlayerHeight = SCR_HEIGHT / 40.5, PlayerWidth = SCR_WIDTH / 72;
    int wallsCounter = 0, finalwallsCounter = 0, borderwallsCounter = 0, sideCounter = 0, basicAmmoCounter = 0, lastSideCounter = 0, freeCoridorKey;


    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
        touch = new Vector3();

        BackgroundImg = new Texture("Borderwall.jpg");
        BrickWallImg = new Texture("BrickWall.jpg");
        PlayerUpYImg = new Texture("GoatPlayerUpY.png");
        PlayerDownYImg = new Texture("GoatPlayerDownY.png");
        PlayerLeftXImg = new Texture("GoatPlayerLeftX.png");
        PlayerRightXImg = new Texture("GoatPlayerRightX.png");
        SteelWallImg = new Texture("SteelWall.jpg");
        BasicProjectileUpYImg = new Texture("BasicProjectileUpY.png");
        BasicProjectileDownYImg = new Texture("BasicProjectileDownY.png");
        BasicProjectileLeftXImg = new Texture("BasicProjectileLeftX.png");
        BasicProjectileRightXImg = new Texture("BasicProjectileRightX.png");

        tank = new Tank(MathUtils.random(2, LAB_WIDTH - 3), MathUtils.random(2, LAB_HEIGHT - 3), (float) PlayerWidth, (float) PlayerHeight, 100);
        basicAmmo = new Projectile[1000];
        enemyBasicAmmo = new Projectile[4000];
        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Enemy(MathUtils.random(2, LAB_WIDTH - 3), 16, (float) PlayerWidth - (float) 0.01, (float) PlayerHeight - (float) 0.01, 0, MathUtils.random(674, 5763), (enemyBasicAmmo.length / (enemies.length * 2)) * i, 100);
        }
        for (int i = 0; i < LAB_WIDTH; i++) {
            for (int j = 0; j < LAB_HEIGHT; j++) {
                BuildMassive[i][j] = MathUtils.random(0, 1);
                BuildMassive[0][j] = 2;
                BuildMassive[LAB_WIDTH - 1][j] = 2;
            }
            BuildMassive[i][0] = 2;
            BuildMassive[i][LAB_HEIGHT - 1] = 2;
        }
        for (int k = 0; k < enemies.length; k++) {
            if (BuildMassive[enemies[k].x][16] == 1) {
                BuildMassive[enemies[k].x][16] = 0;
            }
        }
        for (int i = 0; i < LAB_WIDTH; i++) {
            for (int j = 0; j < LAB_HEIGHT; j++) {

                if (BuildMassive[i][j] == 1) {
                    walls[wallsCounter] = new Walls(WIDTH * i, HEIGHT * j, WIDTH, HEIGHT, 5);
                    wallsCounter++;
                }
                if (BuildMassive[i][j] == 2) {
                    borderwalls[borderwallsCounter] = new BorderWalls(WIDTH * i, HEIGHT * j, WIDTH, HEIGHT);
                    borderwallsCounter++;

                }
            }
        }
        for (int i = 0; i < wallsCounter; i++) {
            if (walls[i].y <= tank.realY & tank.realY <= walls[i].y + walls[i].height & walls[i].x <= tank.realX & tank.realX <= walls[i].x + walls[i].width) {
                walls[i].y = SCR_HEIGHT + 100;
                walls[i].x = SCR_WIDTH + 100;
            }
        }
        for (int i = 0; i < basicAmmo.length; i++) {
            basicAmmo[i] = new Projectile(SCR_WIDTH + 100, SCR_HEIGHT + 100, 1, LAB_WIDTH / 4, LAB_HEIGHT / 2, 3, 0, false);
            BasicAmmoChecker[i] = 0;
            basicAmmo[i].antiEnemyDamage = MathUtils.random(1, 10);
        }
        for (int i = 0; i < enemyBasicAmmo.length; i++) {
            enemyBasicAmmo[i] = new Projectile(SCR_WIDTH + 1000, SCR_HEIGHT + 1000, 1, LAB_WIDTH / 4, LAB_HEIGHT / 2, 3, 0, false);
            enemyBasicAmmo[i].antiEnemyDamage = MathUtils.random(1, 10);
        }


        finalwallsCounter = wallsCounter;


    }

    @Override
    public void render() {

        //касание
        if (Gdx.input.isKeyPressed(-1)) {
            camera.unproject(touch);
            if (Gdx.input.isKeyPressed(29) & !OverlapWallsLeftX(tank.realX, tank.realY, tank.height, tank.width) & !OverlapBordersLeftX(tank.realX, tank.realY, tank.height, tank.width) & !DamageDealtToEnemy(tank.realX, tank.realY, tank.height, tank.width, 1)) {
                tank.moveLeftX();
                sideCounter = 1;


            } else if (Gdx.input.isKeyPressed(32) & !OverlapWallsRightX(tank.realX, tank.realY, tank.height, tank.width) & !OverlapBordersRightX(tank.realX, tank.realY, tank.height, tank.width) & !DamageDealtToEnemy(tank.realX, tank.realY, tank.height, tank.width, 1)) {
                tank.moveRightX();
                sideCounter = 2;

            } else if (Gdx.input.isKeyPressed(51) & !OverlapWallsUpY(tank.realX, tank.realY, tank.height, tank.width) & !OverlapBordersUpY(tank.realX, tank.realY, tank.height, tank.width) & !DamageDealtToEnemy(tank.realX, tank.realY, tank.height, tank.width, 1)) {
                tank.moveUpY();
                sideCounter = 3;

            } else if (Gdx.input.isKeyPressed(47) & !OverlapWallsDownY(tank.realX, tank.realY, tank.height, tank.width) & !OverlapBordersDownY(tank.realX, tank.realY, tank.height, tank.width) & !DamageDealtToEnemy(tank.realX, tank.realY, tank.height, tank.width, 1)) {
                tank.moveDownY();
                sideCounter = 4;

            }
        }

        for (int i = 0; i < basicAmmoCounter; i++) {
            // System.out.println(basicAmmo[i].sideCounter);

            if (basicAmmo[i].sideCounter == 1 & !OverlapWallsLeftX(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width) & !OverlapBordersLeftX(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width)) {
                basicAmmo[i].moveLeftX();

            }
            if (OverlapWallsLeftX(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width) | OverlapBordersLeftX(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width)) {
                basicAmmo[i].hit();
            }

            if (basicAmmo[i].sideCounter == 2 & !OverlapWallsRightX(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width) & !OverlapBordersRightX(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width)) {
                basicAmmo[i].moveRightX();

            }
            if (OverlapWallsRightX(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width) | OverlapBordersRightX(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width)) {
                basicAmmo[i].hit();

            }
            if ((basicAmmo[i].sideCounter == 3 | basicAmmo[i].sideCounter == 0) & !OverlapWallsUpY(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width) & !OverlapBordersUpY(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width)) {
                basicAmmo[i].moveUpY();

            }
            if (OverlapWallsUpY(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width) | OverlapBordersUpY(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width)) {
                basicAmmo[i].hit();
            }
            if (basicAmmo[i].sideCounter == 4 & !OverlapWallsDownY(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width) & !OverlapBordersDownY(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width)) {
                basicAmmo[i].moveDownY();

            }
            if (OverlapWallsDownY(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width) | OverlapBordersDownY(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width)) {
                basicAmmo[i].hit();
            }
            if (DamageDealtToEnemy(basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].height, basicAmmo[i].width, i)) {
                basicAmmo[i].hit();
            }
        }

        if (Gdx.input.isKeyJustPressed(33)) {
            basicAmmoCounter++;
        }

        if (sideCounter == 1) {
            basicAmmo[basicAmmoCounter].sideCounter = 1;
            basicAmmo[basicAmmoCounter].x = tank.realX;
            basicAmmo[basicAmmoCounter].y = tank.realY + (tank.height - basicAmmo[1].height) / 2;
        } else if (sideCounter == 2) {
            basicAmmo[basicAmmoCounter].sideCounter = 2;
            basicAmmo[basicAmmoCounter].x = tank.realX + tank.width;
            basicAmmo[basicAmmoCounter].y = tank.realY + (tank.height - basicAmmo[1].height) / 2;
        } else if (sideCounter == 3) {
            basicAmmo[basicAmmoCounter].sideCounter = 3;
            basicAmmo[basicAmmoCounter].x = tank.realX + (tank.width - basicAmmo[1].width) / 2;
            basicAmmo[basicAmmoCounter].y = tank.realY + tank.height;
        } else if (sideCounter == 4) {
            basicAmmo[basicAmmoCounter].sideCounter = 4;
            basicAmmo[basicAmmoCounter].x = tank.realX + (tank.width - basicAmmo[1].width) / 2;
            basicAmmo[basicAmmoCounter].y = tank.realY;
        } else if (sideCounter == 0) {
            basicAmmo[basicAmmoCounter].sideCounter = 3;
            basicAmmo[basicAmmoCounter].x = tank.realX + (tank.width - basicAmmo[1].width) / 2;
            basicAmmo[basicAmmoCounter].y = tank.realY + tank.height;
        }

        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].sideCounter == 1 & !OverlapWallsLeftX(enemies[i].realX, enemies[i].realY, enemies[i].height, enemies[i].width) & !OverlapBordersLeftX(enemies[i].realX, enemies[i].realY, enemies[i].height, enemies[i].width) & !DamageDealtToTank(enemies[i].realX, enemies[i].realY, enemies[i].height, enemies[i].width, 1)) {
                enemies[i].moveLeftX();

            } else if (enemies[i].sideCounter == 2 & !OverlapWallsRightX(enemies[i].realX, enemies[i].realY, enemies[i].height, enemies[i].width) & !OverlapBordersRightX(enemies[i].realX, enemies[i].realY, enemies[i].height, enemies[i].width) & !DamageDealtToTank(enemies[i].realX, enemies[i].realY, enemies[i].height, enemies[i].width, 1)) {
                enemies[i].moveRightX();


            } else if ((enemies[i].sideCounter == 3 | enemies[i].sideCounter == 0) & !OverlapWallsUpY(enemies[i].realX, enemies[i].realY, enemies[i].height, enemies[i].width) & !OverlapBordersUpY(enemies[i].realX, enemies[i].realY, enemies[i].height, enemies[i].width) & !DamageDealtToTank(enemies[i].realX, enemies[i].realY, enemies[i].height, enemies[i].width, 1)) {
                enemies[i].moveUpY();


            } else if (enemies[i].sideCounter == 4 & !OverlapWallsDownY(enemies[i].realX, enemies[i].realY, enemies[i].height, enemies[i].width) & !OverlapBordersDownY(enemies[i].realX, enemies[i].realY, enemies[i].height, enemies[i].width) & !DamageDealtToTank(enemies[i].realX, enemies[i].realY, enemies[i].height, enemies[i].width, 1)) {
                enemies[i].moveDownY();

            }
        }

        for (int i = 0; i < enemyBasicAmmo.length; i++) {

            if (enemyBasicAmmo[i].sideCounter == 1 & !OverlapWallsLeftX(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width) & !OverlapBordersLeftX(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width)) {
                enemyBasicAmmo[i].moveLeftX();

            }
            if (OverlapWallsLeftX(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width) | OverlapBordersLeftX(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width)) {
                enemyBasicAmmo[i].hit();

            }
            if (enemyBasicAmmo[i].sideCounter == 2 & !OverlapWallsRightX(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width) & !OverlapBordersRightX(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width)) {
                enemyBasicAmmo[i].moveRightX();
            }
            if (OverlapWallsRightX(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width) | OverlapBordersRightX(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width)) {
                enemyBasicAmmo[i].hit();

            }
            if ((enemyBasicAmmo[i].sideCounter == 3 | enemyBasicAmmo[i].sideCounter == 0) & !OverlapWallsUpY(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width) & !OverlapBordersUpY(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width)) {
                enemyBasicAmmo[i].moveUpY();

            }
            if (OverlapWallsUpY(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width) | OverlapBordersUpY(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width)) {
                enemyBasicAmmo[i].hit();

            }
            if (enemyBasicAmmo[i].sideCounter == 4 & !OverlapWallsDownY(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width) & !OverlapBordersDownY(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width)) {
                enemyBasicAmmo[i].moveDownY();

            }
            if (OverlapWallsDownY(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width) | OverlapBordersDownY(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width)) {
                enemyBasicAmmo[i].hit();

            }
            if (DamageDealtToTank(enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].height, enemyBasicAmmo[i].width, i)) {
                enemyBasicAmmo[i].hit();

            }
        }


        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].shootTrigger) {
                enemies[i].enemyBasicAmmoCounter++;
                enemies[i].shootTrigger = false;
            }
        }
        for (int i = 0; i < enemies.length; i++) {

            if (enemies[i].sideCounter == 1 & !enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].hit) {
                enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].sideCounter = 1;
                enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].x = enemies[i].realX + enemies[i].width / 2;
                enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].y = enemies[i].realY + (enemies[i].height - enemyBasicAmmo[1].height) / 2;
            } else if (enemies[i].sideCounter == 2 & !enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].hit) {
                enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].sideCounter = 2;
                enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].x = enemies[i].realX + enemies[i].width / 2;
                enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].y = enemies[i].realY + (enemies[i].height - enemyBasicAmmo[1].height) / 2;
            } else if (enemies[i].sideCounter == 3 & !enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].hit) {
                enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].sideCounter = 3;
                enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].x = enemies[i].realX + (enemies[i].width - enemyBasicAmmo[1].width) / 2;
                enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].y = enemies[i].realY + enemies[i].height / 2;
            } else if (enemies[i].sideCounter == 4 & !enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].hit) {
                enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].sideCounter = 4;
                enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].x = enemies[i].realX + (enemies[i].width - enemyBasicAmmo[1].width) / 2;
                enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].y = enemies[i].realY + enemies[i].height / 2;
            } else if (enemies[i].sideCounter == 0 & !enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].hit) {
                enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].sideCounter = 3;
                enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].x = enemies[i].realX + (enemies[i].width - enemyBasicAmmo[1].width) / 2;
                enemyBasicAmmo[enemies[i].enemyBasicAmmoCounter].y = enemies[i].realY + enemies[i].height / 2;
            }
        }


        //управление ботами

        for (int i = 0; i < enemies.length; i++) {
            System.out.println(i + " " + enemies[i].hitpoints);
            enemies[i].tics++;
            if (enemies[i].tics % 40 == 0) {
                enemies[i].sideCounter = MathUtils.random(1, 4);
            }
            //управление огнем
            if (MathUtils.random(1, 50) == 1) {
                enemies[i].shootTrigger = true;
            }
        }

        //отрисовка
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(BackgroundImg, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        for (int i = 0; i < basicAmmoCounter; i++) {
            if (!basicAmmo[i].hit) {
                if (basicAmmo[i].sideCounter == 1) {
                    batch.draw(BasicProjectileLeftXImg, basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].width, basicAmmo[i].height);
                }
                if (basicAmmo[i].sideCounter == 2) {
                    batch.draw(BasicProjectileRightXImg, basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].width, basicAmmo[i].height);
                }
                if (basicAmmo[i].sideCounter == 3 | basicAmmo[i].sideCounter == 0) {
                    batch.draw(BasicProjectileUpYImg, basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].width, basicAmmo[i].height);
                }
                if (basicAmmo[i].sideCounter == 4) {
                    batch.draw(BasicProjectileDownYImg, basicAmmo[i].x, basicAmmo[i].y, basicAmmo[i].width, basicAmmo[i].height);
                }
            }
        }
        for (int j = 0; j < enemies.length; j++) {
            for (int i = 0; i < enemies[j].enemyBasicAmmoCounter; i++) {
                if (!enemyBasicAmmo[i].hit) {
                    if (enemyBasicAmmo[i].sideCounter == 1) {
                        batch.draw(BasicProjectileLeftXImg, enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].width, enemyBasicAmmo[i].height);
                    }
                    if (enemyBasicAmmo[i].sideCounter == 2) {
                        batch.draw(BasicProjectileRightXImg, enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].width, enemyBasicAmmo[i].height);
                    }
                    if (enemyBasicAmmo[i].sideCounter == 3 | enemyBasicAmmo[i].sideCounter == 0) {
                        batch.draw(BasicProjectileUpYImg, enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].width, enemyBasicAmmo[i].height);
                    }
                    if (enemyBasicAmmo[i].sideCounter == 4) {
                        batch.draw(BasicProjectileDownYImg, enemyBasicAmmo[i].x, enemyBasicAmmo[i].y, enemyBasicAmmo[i].width, enemyBasicAmmo[i].height);
                    }
                }
            }
        }
        System.out.println(tank.hitpoints);
        for (int j = 0; j < wallsCounter; j++) {
            if (walls[j].durability <= 0) {
                walls[j].x = SCR_WIDTH + 100;
                walls[j].y = SCR_HEIGHT + 100;
            }
            batch.draw(BrickWallImg, walls[j].x, walls[j].y, walls[j].width, walls[j].height);
        }
        for (int i = 0; i < borderwallsCounter; i++) {
            batch.draw(SteelWallImg, borderwalls[i].x, borderwalls[i].y, borderwalls[i].width, borderwalls[i].height);
        }
        //System.out.println(sideCounter);
        if (tank.hitpoints <= 0) {
            tank.realX = SCR_WIDTH + 100;
            tank.realY = SCR_HEIGHT + 100;
        }
        if (sideCounter == 1) {
            batch.draw(PlayerLeftXImg, tank.realX, tank.realY, tank.width, tank.height);
            lastSideCounter = 1;
        }
        if (sideCounter == 2) {
            batch.draw(PlayerRightXImg, tank.realX, tank.realY, tank.width, tank.height);
            lastSideCounter = 2;
        }
        if (sideCounter == 3 | sideCounter == 0) {
            batch.draw(PlayerUpYImg, tank.realX, tank.realY, tank.width, tank.height);
            lastSideCounter = 3;
        }
        if (sideCounter == 4) {
            batch.draw(PlayerDownYImg, tank.realX, tank.realY, tank.width, tank.height);
            lastSideCounter = 4;
        }

        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].hitpoints <= 0) {
                enemies[i].realX = SCR_WIDTH + 100;
                enemies[i].realY = SCR_HEIGHT + 100;
            }
            if (enemies[i].sideCounter == 1) {
                batch.draw(PlayerLeftXImg, enemies[i].realX, enemies[i].realY, enemies[i].width, enemies[i].height);
            }
            if (enemies[i].sideCounter == 2) {
                batch.draw(PlayerRightXImg, enemies[i].realX, enemies[i].realY, enemies[i].width, enemies[i].height);
            }
            if (enemies[i].sideCounter == 3 | enemies[i].sideCounter == 0) {
                batch.draw(PlayerUpYImg, enemies[i].realX, enemies[i].realY, enemies[i].width, enemies[i].height);
            }
            if (enemies[i].sideCounter == 4) {
                batch.draw(PlayerDownYImg, enemies[i].realX, enemies[i].realY, enemies[i].width, enemies[i].height);
            }
        }


        batch.end();

    }

    public boolean OverlapWallsLeftX(float x, float y, float height, float width) {
        for (int i = 0; i < wallsCounter; i++) {
            if (walls[i].x <= x - 1.25 & x - 1.25 <= walls[i].x + walls[i].width) {
                if ((walls[i].y <= y & y <= walls[i].y + walls[i].height) | (walls[i].y <= y + height & y + height <= walls[i].y + walls[i].height)) {
                    if (height == LAB_HEIGHT / 2) {
                        walls[i].durability -= basicAmmo[1].antiWallDamage;
                        //System.out.println(walls[i].durability);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean OverlapWallsRightX(float x, float y, float height, float width) {
        for (int i = 0; i < wallsCounter; i++) {
            if (walls[i].x <= x + width + 1.25 & x + width + 1.25 <= walls[i].x + walls[i].width) {
                if ((walls[i].y <= y & y <= walls[i].y + walls[i].height) | (walls[i].y <= y + height & y + height <= walls[i].y + walls[i].height)) {
                    if (height == LAB_HEIGHT / 2) {
                        walls[i].durability -= basicAmmo[1].antiWallDamage;
                        //  System.out.println(walls[i].durability);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean OverlapWallsUpY(float x, float y, float height, float width) {
        for (int i = 0; i < wallsCounter; i++) {
            if (walls[i].y <= y + height + 1.25 & y + height + 1.25 <= walls[i].y + walls[i].height) {
                if ((walls[i].x <= x & x <= walls[i].x + walls[i].width) | (walls[i].x <= x + width & x + width <= walls[i].x + walls[i].width)) {
                    if (height == LAB_HEIGHT / 2) {
                        walls[i].durability -= basicAmmo[1].antiWallDamage;
                        //System.out.println(walls[i].durability);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean OverlapWallsDownY(float x, float y, float height, float width) {
        for (int i = 0; i < wallsCounter; i++) {
            if (walls[i].y <= y - 1.25 & y - 1.25 <= walls[i].y + walls[i].height) {
                if ((walls[i].x <= x & x <= walls[i].x + walls[i].width) | (walls[i].x <= x + width & x + width <= walls[i].x + walls[i].width)) {
                    if (height == LAB_HEIGHT / 2) {
                        walls[i].durability -= basicAmmo[1].antiWallDamage;
                        //  System.out.println(walls[i].durability);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean OverlapBordersLeftX(float x, float y, float height, float width) {
        for (int i = 0; i < borderwallsCounter; i++) {
            if (borderwalls[i].x <= x - 1 & x - 1 <= borderwalls[i].x + borderwalls[i].width) {
                if ((borderwalls[i].y <= y & y <= borderwalls[i].y + borderwalls[i].height) | (borderwalls[i].y <= y + height & y + height <= borderwalls[i].y + borderwalls[i].height)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean OverlapBordersRightX(float x, float y, float height, float width) {
        for (int i = 0; i < borderwallsCounter; i++) {
            if (borderwalls[i].x <= x + width + 1 & x + width + 1 <= borderwalls[i].x + borderwalls[i].width) {
                if ((borderwalls[i].y <= y & y <= borderwalls[i].y + borderwalls[i].height) | (borderwalls[i].y <= y + height & y + height <= borderwalls[i].y + borderwalls[i].height)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean OverlapBordersUpY(float x, float y, float height, float width) {
        for (int i = 0; i < borderwallsCounter; i++) {
            if (borderwalls[i].y <= y + height + 1.25 & y + height + 1.25 <= borderwalls[i].y + borderwalls[i].height) {
                if ((borderwalls[i].x <= x & x <= borderwalls[i].x + borderwalls[i].width) | (borderwalls[i].x <= x + width & x + width <= borderwalls[i].x + borderwalls[i].width)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean OverlapBordersDownY(float x, float y, float height, float width) {
        for (int i = 0; i < borderwallsCounter; i++) {
            if (borderwalls[i].y <= y - 1 & y - 1 <= borderwalls[i].y + borderwalls[i].height) {
                if ((borderwalls[i].x <= x & x <= borderwalls[i].x + borderwalls[i].width) | (borderwalls[i].x <= x + width & x + width <= borderwalls[i].x + borderwalls[i].width)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean DamageDealtToEnemy(float x, float y, float height, float width, int bulletNumber) {
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i].realY <= y - 1 & y - 1 <= enemies[i].realY + enemies[i].height) {
                if ((enemies[i].realX <= x & x <= enemies[i].realX + enemies[i].width) | (enemies[i].realX <= x + width & x + width <= enemies[i].realX + enemies[i].width)) {
                    if (height == LAB_HEIGHT / 2) {
                        enemies[i].hitpoints -= basicAmmo[bulletNumber].antiEnemyDamage;
                    }
                    return true;
                }
            }
            if (enemies[i].realY <= y + height + 1.25 & y + height + 1.25 <= enemies[i].realY + enemies[i].height) {
                if ((enemies[i].realX <= x & x <= enemies[i].realX + enemies[i].width) | (enemies[i].realX <= x + width & x + width <= enemies[i].realX + enemies[i].width)) {
                    if (height == LAB_HEIGHT / 2) {
                        enemies[i].hitpoints -= basicAmmo[bulletNumber].antiEnemyDamage;
                    }
                    return true;
                }
            }
            if (enemies[i].realX <= x + width + 1 & x + width + 1 <= enemies[i].realX + enemies[i].width) {
                if ((enemies[i].realY <= y & y <= enemies[i].realY + enemies[i].height) | (enemies[i].realY <= y + height & y + height <= enemies[i].realY + enemies[i].height)) {
                    if (height == LAB_HEIGHT / 2) {
                        enemies[i].hitpoints -= basicAmmo[bulletNumber].antiEnemyDamage;
                    }
                    return true;
                }
            }
            if (enemies[i].realX <= x - 1 & x - 1 <= enemies[i].realX + enemies[i].width) {
                if ((enemies[i].realY <= y & y <= enemies[i].realY + enemies[i].height) | (enemies[i].realY <= y + height & y + height <= enemies[i].realY + enemies[i].height)) {
                    if (height == LAB_HEIGHT / 2) {
                        enemies[i].hitpoints -= basicAmmo[bulletNumber].antiEnemyDamage;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean DamageDealtToTank(float x, float y, float height, float width, int bulletNumber) {
        if (tank.realY <= y - 1 & y - 1 <= tank.realY + tank.height) {
            if ((tank.realX <= x & x <= tank.realX + tank.width) | (tank.realX <= x + width & x + width <= tank.realX + tank.width)) {
                if (height == LAB_HEIGHT / 2) {
                    tank.hitpoints -= enemyBasicAmmo[bulletNumber].antiEnemyDamage;
                }
                return true;
            }
        }
        if (tank.realY <= y + height + 1.25 & y + height + 1.25 <= tank.realY + tank.height) {
            if ((tank.realX <= x & x <= tank.realX + tank.width) | (tank.realX <= x + width & x + width <= tank.realX + tank.width)) {
                if (height == LAB_HEIGHT / 2) {
                    tank.hitpoints -= enemyBasicAmmo[bulletNumber].antiEnemyDamage;
                }
                return true;
            }
        }
        if (tank.realX <= x + width + 1 & x + width + 1 <= tank.realX + tank.width) {
            if ((tank.realY <= y & y <= tank.realY + tank.height) | (tank.realY <= y + height & y + height <= tank.realY + tank.height)) {
                if (height == LAB_HEIGHT / 2) {
                    tank.hitpoints -= enemyBasicAmmo[bulletNumber].antiEnemyDamage;
                }
                return true;
            }
        }
        if (tank.realX <= x - 1 & x - 1 <= tank.realX + tank.width) {
            if ((tank.realY <= y & y <= tank.realY + tank.height) | (tank.realY <= y + height & y + height <= tank.realY + tank.height)) {
                if (height == LAB_HEIGHT / 2) {
                    tank.hitpoints -= enemyBasicAmmo[bulletNumber].antiEnemyDamage;
                }
                return true;
            }
        }
        return false;
    }


    @Override
    public void dispose() {
        batch.dispose();
        BackgroundImg.dispose();
        BrickWallImg.dispose();
        PlayerUpYImg.dispose();
        //font.dispose();

    }

}





