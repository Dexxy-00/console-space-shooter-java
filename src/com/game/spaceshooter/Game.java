package com.game.spaceshooter;

import java.util.Scanner;

public class Game {
    private Player player;
    private boolean run;
    private Scanner in = new Scanner(System.in);

    public void start() {
        run = true;
        player = new Player(10, 5, 10f);
        gameLoop();
    }

    public void gameLoop() {
        long lastTime = System.nanoTime();
        while(run) {
            long currentTime = System.nanoTime();
            float deltaTime = (currentTime - lastTime) / 1000000000f;
            lastTime = currentTime;
            updateScreen(deltaTime);
            renderScreen();
        }
    }

    public void updateScreen(float deltaTime) {
        handleInput(deltaTime);
    }

    public void renderScreen() {
        System.out.println("Player's X coords : " + player.getX());
    }

    public void handleInput(float deltaTime) {
        if(in.hasNextLine()) {
            String input = in.nextLine();

            switch(input.toLowerCase()) {
                case "a":
                    player.moveLeft(deltaTime);
                    break;
                case "d":
                    player.moveRight(deltaTime);
                    break;
            }
        }
    }
}
