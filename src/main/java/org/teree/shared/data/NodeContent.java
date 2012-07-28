package org.teree.shared.data;

public class NodeContent {

    // width and height can be used to determine maximal size of the content
    private int width;
    private int height;
    
    private String text;

    public int getWidth() {
        return width;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
}
