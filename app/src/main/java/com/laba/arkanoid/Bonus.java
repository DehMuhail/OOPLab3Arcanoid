package com.laba.arkanoid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Bonus {
    private float x, y, width, height;
    private boolean isVisible;

    public Bonus(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isVisible = true;
    }

    public void update() {
        y += 5; // Бонус падає вниз
    }

    public void draw(Canvas canvas, Paint paint) {
        if (isVisible) {
            paint.setColor(Color.BLUE); // Синій колір для бонусу сповільнення
            canvas.drawRect(x, y, x + width, y + height, paint);
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
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
}
