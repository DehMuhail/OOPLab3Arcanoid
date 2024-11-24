package com.laba.arkanoid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball  {
    private float x, y, radius, speedX, speedY,defaultSpeedX,defaultSpeedY;

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public Ball(float x, float y, float radius, float speedX, float speedY) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speedX = speedX;
        this.speedY = speedY;
        this.defaultSpeedX = speedX;
        this.defaultSpeedY = speedY;
    }

    public void increaseSpeed(float factor) {
        speedX *= factor; // Збільшення горизонтальної швидкості
        speedY *= factor; // Збільшення вертикальної швидкості
    }

    public void update() {
        x += speedX;
        y += speedY;

        // Перевірка зіткнень зі стінами
        if (x - radius < 0 || x + radius > 1080) speedX = -speedX; // Ширина екрану - 1080
        if (y - radius < 0 || y + radius > 1920) speedY = -speedY; // Висота екрану - 1920
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, radius, paint);
    }

    public void reverseYSpeed() {
        this.speedY = this.speedY*-1;
    }

    public void resetPosition(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public void resetSpeed() {
        this.speedX=defaultSpeedX;
        this.speedY=defaultSpeedY;
    }


    public void decreaseSpeed(float factor) {
        speedX *= factor; // Збільшення горизонтальної швидкості
        speedY *= factor; // Збільшення вертикальної швидкості
    }
}
