package com.laba.arkanoid;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private Thread gameThread;
    private boolean isPlaying;
    private boolean isGameOver = false;
    private boolean loose = false;

    private SurfaceHolder holder;
    private Paint paint;
    private Random random;

    private Ball ball;
    private Paddle paddle;
    private ArrayList<Block> blocks;
    private ArrayList<Bonus> bonuses;

    private static int BLOCK_ROWS;
    private int BLOCK_COLUMNS;
    private int blocksSize;
    private int blockCount = 1;

    private boolean startGame = true;

    public GameView(Context context) {
        super(context);
        holder = getHolder();
        paint = new Paint();
        random = new Random();
        bonuses = new ArrayList<>();
        blocks = new ArrayList<>();

        // Ініціалізація м'яча та платформи
        ball = new Ball(540, 960, 20, 10, 10);
        paddle = new Paddle(440, 1800, 200, 30);
    }

    // Генерація кількості блоків
    private void generateBlocksCount() {
        BLOCK_ROWS = 1 + random.nextInt(blockCount);
        BLOCK_COLUMNS = 1 + random.nextInt(blockCount);
    }

    // Генерація блоків
    private void generateBlocks() {
        blocks.clear();
        generateBlocksCount();
        float blockWidth = getWidth() / (BLOCK_COLUMNS + 1);
        float blockHeight = 60;

        for (int row = 0; row < BLOCK_ROWS; row++) {
            for (int col = 0; col < BLOCK_COLUMNS; col++) {
                float x = col * blockWidth + blockWidth / 2;
                float y = row * (blockHeight + 10) + 50;
                blocks.add(new Block(x, y, blockWidth - 20, blockHeight));
            }
        }
        blocksSize = blocks.size();
    }

    // Генерація бонусу
    private void generateBonus(float x, float y) {
        if (random.nextInt(10) < 3) { // 30% шанс бонусу
            bonuses.add(new Bonus(x, y, 50, 20));
        }
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            control();
            if (startGame) {
                restartGame();
                startGame = false;
            }
        }
    }

    private void update() {
        // Оновлення положення м'яча
        ball.update();
        if (ball.getY()+10 > paddle.getY()) {
            isGameOver = true; // Встановлюємо стан програшу
            loose = true;
        }
        // Перевірка зіткнення м'яча з платформою
        if (ball.getY() + ball.getRadius() > paddle.getY() &&
                ball.getX() > paddle.getX() &&
                ball.getX() < paddle.getX() + paddle.getWidth()) {
            ball.reverseYSpeed();
            ball.increaseSpeed(1.1f); // Збільшення швидкості на 10%
        }

        // Оновлення бонусів і перевірка зіткнень з платформою
        for (Bonus bonus : bonuses) {
            if (bonus.isVisible()) {
                bonus.update();

                if (bonus.getY() + bonus.getHeight() > paddle.getY() &&
                        bonus.getX() < paddle.getX() + paddle.getWidth() &&
                        bonus.getX() + bonus.getWidth() > paddle.getX()) {
                    bonus.setVisible(false); // Бонус зникає
                    ball.decreaseSpeed(0.95f); // Зменшення швидкості м'яча
                }
            }
        }

        // Перевірка блоків на зіткнення з м'ячем
        for (Block block : blocks) {
            if (block.isVisible() &&
                    ball.getX() + ball.getRadius() > block.getX() &&
                    ball.getX() - ball.getRadius() < block.getX() + block.getWidth() &&
                    ball.getY() + ball.getRadius() > block.getY() &&
                    ball.getY() - ball.getRadius() < block.getY() + block.getHeight()) {
                block.setVisible(false); // Блок зникає
                blocksSize--; // Зменшення кількості блоків
                ball.reverseYSpeed(); // Зміна напрямку руху м'яча

                generateBonus(block.getX(), block.getY()); // Генерація бонусу
            }
        }

        // Перевірка програшу
        if (ball.getY() + ball.getRadius() > getHeight()) {
            isGameOver = true;
            loose = true;
        }

        // Перевірка виграшу (всі блоки знищено)
        if (blocksSize == 0) {
            restartGame();
        }
    }


//    @Override
    public void draw() {
        if (holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            if (!isGameOver) {
                ball.draw(canvas, paint);
                paddle.draw(canvas, paint);

                for (Block block : blocks) {
                    block.draw(canvas, paint);
                }

                for (Bonus bonus : bonuses) {
                    if (bonus.isVisible()) {
                        bonus.draw(canvas, paint);
                    }
                }
            } else {
                paint.setColor(Color.RED);
                paint.setTextSize(100);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(getResources().getString(R.string.game_over), getWidth() / 2, getHeight() / 2 - 100, paint);

                paint.setColor(Color.WHITE);
                paint.setTextSize(50);
                canvas.drawText(getResources().getString(R.string.restart), getWidth() / 2, getHeight() / 2 + 50, paint);
                canvas.drawText(getResources().getString(R.string.back_to_menu), getWidth() / 2, getHeight() / 2 + 150, paint);
            }

            holder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver && event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            if (y > getHeight() / 2 + 20 && y < getHeight() / 2 + 80) {
                restartGame(); // Якщо натиснуто "Restart"
            } else if (y > getHeight() / 2 + 120 && y < getHeight() / 2 + 180) {
                // Якщо натиснуто "Main Menu"
                Context context = getContext();
                if (context instanceof Activity) {
                    ((Activity) context).finish(); // Повернутися до MainActivity
                }
            }
            return true;
        }

        if (!isGameOver && event.getAction() == MotionEvent.ACTION_MOVE) {
            paddle.setPosition(event.getX(), getWidth());
        }
        return true;
    }


    private void control() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void updateLanguage() {
        // Просто перерисувати екран
        invalidate();  // Оновлюємо вигляд екрана, щоб відобразити нові строки
    }

    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    public void updateLanguage(String languageCode) {
        setLocale(languageCode);  // Update the locale of the game

        // Update any game-related text elements if needed (e.g., GAME OVER message)
        invalidate();  // Redraw the screen to show the updated text
    }


    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }


    private void restartGame() {
        isGameOver = false;
        ball.resetPosition(540, 960);
        paddle.resetPosition(440);

        if (loose) {
            ball.resetSpeed();
            blockCount = 1;
            loose = false;
        }

        blockCount++;
        generateBlocks();
        bonuses.clear();
    }
}