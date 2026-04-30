package com.game.spaceshooter;

public class Player {
    private int x;
    private int y;
    private int maxX;
    private int maxY;

    public Player(int x, int y, int maxX, int maxY) {
        this.x = x;
        this.y = y;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public void setPosition (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveLeft() {
        x -= 1;
        if(x < 0) x = 0;
    }

    public void moveRight() {
        x += 1;
        if(x >= maxX) x = maxX - 1;
    }
}
