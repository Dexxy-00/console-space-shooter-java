package com.game.spaceshooter;
import org.fusesource.jansi.AnsiConsole;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;


public class Game {
    private Player player;
    private boolean run;
    private static final int WIDTH = 60;
    private static final int HEIGHT = 50;
    private Terminal terminal;
    private NonBlockingReader reader;

    public void start() {
        try {
            AnsiConsole.systemInstall();

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
            AnsiConsole.systemUninstall();
        }
    }

    public void gameLoop() {
        while(run) {
            updateScreen();

            System.out.print("\033[H");

            renderScreen();

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateScreen() {
        handleInput();
    }

    public void renderScreen() {
        int x = player.getX();
        int y = player.getY();

        System.out.print("\033[H");
        for(int i = 0; i < HEIGHT; i++) {
            for(int j = 0; j < WIDTH; j++) {
                if(i == y && j == x) {
                    System.out.print("< >");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }

        System.out.flush();
    }

    public void handleInput() {
        try {
            int ch = reader.read(0); // non-blocking read

            if (ch == -1) return; // no key pressed

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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
