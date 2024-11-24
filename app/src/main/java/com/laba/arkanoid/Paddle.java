package com.laba.arkanoid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Paddle {
    private float x, y;
    private float width, height;

    public Paddle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setPosition(float x, int screenWidth) {
        // Обмежуємо платформу в межах екрана
        this.x = Math.max(0, Math.min(x - width / 2, screenWidth - width));
    }

    public void resetPosition(float x) {
        this.x = x; // Відновлюємо початкову позицію
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.WHITE);
        canvas.drawRect(x, y, x + width, y + height, paint);
    }
}
