package com.game.spaceshooter;

public class Enemy {
    private int x;
    private int y;

    public Enemy (int x, int y) {
        this.x = x;
        this.y = y;
    }

    //public int getHP() {
    //    return HP;
    //}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveDown() {
        y++;
    }
}
