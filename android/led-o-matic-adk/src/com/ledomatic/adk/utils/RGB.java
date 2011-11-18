package com.ledomatic.adk.utils;

/**
 * The <code>RGB</code> class represents a color in RGB values
 */
public class RGB {
    private float r = 0;
    private float g = 0;
    private float b = 0;

    public RGB() {
    }

    public RGB(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }

    public void setR(float r) {
        this.r = r;
    }

    public void setG(float g) {
        this.g = g;
    }

    public void setB(float b) {
        this.b = b;
    }
}