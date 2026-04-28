package com.game.spaceshooter;
import org.fusesource.jansi.AnsiConsole;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Game {
    private Player player;
    private boolean run;
    private static final int WIDTH = 69;
    private static final int HEIGHT = 50;
    private Terminal terminal;
    private NonBlockingReader reader;
    private List<Bullet> bullets = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();

    public void start() {
        try {
            AnsiConsole.systemInstall();

            System.out.print("\033[?1049h"); // enter alternate screen buffer (prevents scrollback mess)
            System.out.print("\033[?25l"); // hide cursor

            terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();

            terminal.enterRawMode();
            reader = terminal.reader();

            System.out.print("\033[2J");
            System.out.print("\033[H");

            run = true;
            player = new Player(WIDTH / 2, HEIGHT - 2, WIDTH, HEIGHT);
            gameLoop();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.print("\033[?25h"); // restore cursor
            System.out.print("\033[?1049l"); // exit alternate screen buffer
            AnsiConsole.systemUninstall();
        }
    }

    public void gameLoop() {
        int frameCount = 0;
        while(run) {
            updateScreen(frameCount);

            renderScreen();

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frameCount++;
        }
    }

    public void updateScreen(int frameCount) {
        handleInput();

        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet b = bulletIterator.next();
            b.moveUp();
            if (b.getY() < 0) {
                bulletIterator.remove();
            }
        }

        double spawnRate = 0.02;
        if (Math.random() < spawnRate) {
            enemies.add(new Enemy((int)(Math.random() * WIDTH), 0));
        }

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy e = enemyIterator.next();
            boolean removed = false;

            Iterator<Bullet> collisionIterator = bullets.iterator();
            while (collisionIterator.hasNext()) {
                Bullet b = collisionIterator.next();

                if (b.getX() == e.getX() && b.getY() <= e.getY() && (b.getY() + 1) >= e.getY()) {
                    enemyIterator.remove();
                    collisionIterator.remove();
                    removed = true;
                    break;
                }
            }

            if (removed) {
                continue;
            }

            if (frameCount % 5 == 0) {
                e.moveDown();
            }

            if (e.getY() >= HEIGHT) {
                enemyIterator.remove();
            }
        }
    }

    public void renderScreen() {
        int x = player.getX();
        int y = player.getY();

        StringBuilder sb = new StringBuilder();
        sb.append("\033[H"); // Move cursor to top-left
        
        for(int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (i == y && j == x) {
                    sb.append("[-]");
                }
                else if(enemyExists(enemies, j, i)) {
                    sb.append("(-)");
                }
                else if(bulletExists(bullets, j, i)) {
                    sb.append(" | ");
                }
                else {
                    sb.append("   ");
                }
            }
            // Avoid printing a newline on the very last line to prevent scrolling
            if (i < HEIGHT - 1) {
                sb.append("\n");
            }
        }
        System.out.print(sb.toString());
        System.out.flush();
    }

    private boolean bulletExists(List<Bullet> bullets, int x, int y) {
        for (Bullet bullet : bullets) {
            if (bullet.getX() == x && bullet.getY() == y) {
                return true;
            }
        }
        return false;
    }

    private boolean enemyExists(List<Enemy> enemies, int x, int y) {
        for (Enemy enemy : enemies) {
            if (enemy.getX() == x && enemy.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public void handleInput() {
        try {
            int ch = reader.read(1);

            if (ch != -1) {
                switch (ch) {
                    case 'a':
                        player.moveLeft();
                        break;
                    case 'd':
                        player.moveRight();
                        break;
                    case 'q':
                        run = false;
                        break;
                    case ' ':
                        bullets.add(new Bullet(player.getX(), player.getY() - 1));
                        break;
                }
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}