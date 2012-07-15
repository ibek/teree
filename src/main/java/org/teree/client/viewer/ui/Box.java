package org.teree.client.viewer.ui;

public class Box {
    
    public int x, y, w, h;

    public Box() {

    }

    public Box(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Box(Box b) {
        this.x = b.x;
        this.y = b.y;
        this.w = b.w;
        this.h = b.h;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "," + "," + w + "," + h + "]";
    }
    
}
