package com.ww.ll.db;

/**
 *
 * @author Ww
 * @date 2018/5/14
 */
public class LeftList {
    private String name;

    private int imageId;

    public LeftList(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}