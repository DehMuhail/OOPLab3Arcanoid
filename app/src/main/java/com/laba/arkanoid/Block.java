package com.laba.arkanoid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Block {
    private Random random = new Random();
    private float x, y;
    private float width, height;
    private boolean isVisible;

    public Block(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isVisible = true; // Блок спочатку видимий
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

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public void draw(Canvas canvas, Paint paint) {

        if (isVisible) {
            int red = random.nextInt(256); // Від 0 до 255
            int green = random.nextInt(256);
            int blue = random.nextInt(256);
            paint.setColor(Color.rgb(red,green,blue));
            canvas.drawRect(x, y, x + width, y + height, paint);
        }
    }
}
