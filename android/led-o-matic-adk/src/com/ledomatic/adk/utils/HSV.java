package com.ledomatic.adk.utils;

public class HSV {
    private float h = 0;
    private float s = 0;
    private float v = 0;

    public HSV() {
    }

    public HSV(float h, float s, float v) {
        this.h = h;
        this.s = s;
        this.v = v;
    }

    public float getH() {
        return h;
    }

    public float getS() {
        return s;
    }

    public float getV() {
        return v;
    }

    public void setH(float h) {
        this.h = h;
    }

    public void setS(float s) {
        this.s = s;
    }

    public void setV(float v) {
        this.v = v;
    }
}
