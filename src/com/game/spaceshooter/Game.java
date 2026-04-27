package com.game.spaceshooter;
import org.fusesource.jansi.AnsiConsole;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;


public class Game {
    private Player player;
    private boolean run;
    private static final int WIDTH = 69;
    private static final int HEIGHT = 50; // Reduced height to fit inside standard terminals
    private Terminal terminal;
    private NonBlockingReader reader;

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
        while(run) {
            updateScreen();

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

        StringBuilder sb = new StringBuilder();
        sb.append("\033[H"); // Move cursor to top-left
        
        for(int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (i == y && j == x) {
                    sb.append("< >");
                } else {
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
                }
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}