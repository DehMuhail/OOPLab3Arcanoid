package com.laba.arkanoid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private Thread gameThread;
    private boolean isPlaying;
    private ArrayList<Bonus> bonuses; // Список бонусів

    private boolean loose = false;
    private boolean isGameOver = false; // Позначка програшу
    private SurfaceHolder holder;
    private Paint paint;

    private Ball ball;
    private Paddle paddle;
    private ArrayList<Block> blocks; // Список блоків
    private Random random;          // Генератор випадкових чисел
    private static int BLOCK_ROWS; // Кількість рядів блоків
    private int BLOCK_COLUMNS; // Кількість колонок блоків
    private int blockCount = 1;
    private int blocksSize = 1;
    private boolean startGame = true;

    public void generateBlocksCount(int i) {
        BLOCK_ROWS = 1 + random.nextInt(i + 1);
        BLOCK_COLUMNS = 1 + random.nextInt(i + 1);
    }

    public GameView(Context context) {
        super(context);
        holder = getHolder();
        paint = new Paint();
        random = new Random();
        bonuses = new ArrayList<>();

        // Ініціалізація об'єктів
        ball = new Ball(540, 960, 20, 10, 10); // Початкові координати м'яча
        paddle = new Paddle(440, 1800, 200, 30); // Початкові координати платформи
        // Генерація блоків
        blocks = new ArrayList<>();
        generateBlocks();
    }

    private void generateBlocks() {
        System.out.println(blockCount);
        generateBlocksCount(blockCount);
        float blockWidth = getWidth() / (BLOCK_COLUMNS + 1); // Ширина блоку
        float blockHeight = 60; // Висота блоку

        for (int row = 0; row < BLOCK_ROWS; row++) {
            for (int col = 0; col < BLOCK_COLUMNS; col++) {
                float x = col * blockWidth + blockWidth / 2;
                float y = row * (blockHeight + 10) + 50; // Відступ зверху
                blocks.add(new Block(x, y, blockWidth - 20, blockHeight));
            }
        }
    }

    private void generateBonus(float x, float y) {
        if (random.nextInt(10) < 3) { // 30% шанс появи бонусу
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
        // Оновлення м'яча
        ball.update();

        // Перевірка зіткнення м'яча з платформою
        if (ball.getY() + ball.getRadius() > paddle.getY() &&
                ball.getX() > paddle.getX() &&
                ball.getX() < paddle.getX() + paddle.getWidth()) {
            ball.reverseYSpeed(); // Змінюємо напрямок руху м'яча

            // Збільшуємо швидкість м'яча
            ball.increaseSpeed(1.1f); // Наприклад, збільшення швидкості на 10%
        }
        for (Bonus bonus : bonuses) {
            if (bonus.isVisible()) {
                bonus.update();

                // Перевірка на зіткнення платформи з бонусом
                if (bonus.getY() + bonus.getHeight() > paddle.getY() &&
                        bonus.getX() < paddle.getX() + paddle.getWidth() &&
                        bonus.getX() + bonus.getWidth() > paddle.getX()) {
                    bonus.setVisible(false); // Бонус зникає

                    // Сповільнення м'яча
                    ball.decreaseSpeed(0.7f); // Зменшення швидкості на 30%
                }
            }
        }
        // Перевірка зіткнення м'яча з блоками
        for (Block block : blocks) {
            if (block.isVisible() &&
                    ball.getX() + ball.getRadius() > block.getX() &&
                    ball.getX() - ball.getRadius() < block.getX() + block.getWidth() &&
                    ball.getY() + ball.getRadius() > block.getY() &&
                    ball.getY() - ball.getRadius() < block.getY() + block.getHeight()) {

                block.setVisible(false); // Блок зникає
                blocksSize -= 1;
                ball.reverseYSpeed();    // Змінюємо напрямок м'яча
                // Генерація бонусу
                generateBonus(block.getX(), block.getY());
            }
        }

        // Перевірка програшу (якщо м'яч вийшов за нижню межу)
        if (ball.getY() > paddle.getY()) {
            isGameOver = true; // Встановлюємо стан програшу
            loose = true;
        }
        if (blocksSize == 0) {
            restartGame();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (blocks.isEmpty()) { // Генеруємо блоки лише при першій ініціалізації
            generateBlocks();
            blocksSize = blocks.size();
        }
    }


    private void draw() {
        if (holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK); // Фон гри

            if (!isGameOver) {
                // Малювання м'яча
                ball.draw(canvas, paint);

                // Малювання платформи
                paddle.draw(canvas, paint);

                // Малювання блоків
                for (Block block : blocks) {
                    block.draw(canvas, paint);
                }
                for (Bonus bonus : bonuses) {
                    if (bonus.isVisible()) {
                        bonus.draw(canvas, paint);
                    }
                }

            } else {
                // Відображення екрану програшу
                paint.setColor(Color.RED);
                paint.setTextSize(100);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("GAME OVER", getWidth() / 2, getHeight() / 2, paint);

                paint.setColor(Color.WHITE);
                paint.setTextSize(50);
                canvas.drawText("Tap to Restart", getWidth() / 2, getHeight() / 2 + 100, paint);
            }

            holder.unlockCanvasAndPost(canvas);
        }
    }


    private void control() {
        try {
            Thread.sleep(17); // ~60 FPS
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

    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver && event.getAction() == MotionEvent.ACTION_DOWN) {
            restartGame(); // Перезапуск гри при тапі
            return true;
        }

        if (!isGameOver && event.getAction() == MotionEvent.ACTION_MOVE) {
            // Переміщення платформи по горизонталі, з урахуванням ширини екрана
            paddle.setPosition(event.getX(), getWidth());
        }
        return true;
    }


    private void restartGame() {
        isGameOver = false; // Скидаємо стан програшу
        ball.resetPosition(540, 960); // Початкові координати м'яча
        paddle.resetPosition(440); // Початкові координати платформи
        blocks.clear(); // Очищаємо старі блоки
        if (loose) {
            ball.resetSpeed();
            blockCount = 0;
            loose = false;
        }
        blockCount++;
        generateBlocks(); // Генеруємо нові блоки
        blocksSize = blocks.size();
        bonuses.clear(); // Видаляємо всі бонуси

    }

}
