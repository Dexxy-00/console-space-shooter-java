package com.game.spaceshooter;

import java.util.List;

public class Player {
    private float x;
    private float y;
    private float speed;

    public Player(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void setPosition (float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void moveLeft(float deltaTime) {
        x -= speed * deltaTime;
    }

    public void moveRight(float deltaTime) {
        x += speed * deltaTime;
    }
}
