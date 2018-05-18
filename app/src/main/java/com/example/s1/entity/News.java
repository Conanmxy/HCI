package com.example.s1.entity;

/**
 * Created by 郑剑楠 on 2018/3/20.
 */

public class News {
    private String title;
    private String link;
    private int ImageSrc;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLink()
    {
        return link;
    }
    public void setLink(String link)
    {
        this.link=link;
    }
    public int getImageSrc()
    {
        return ImageSrc;
    }
    public void setImageSrc(int ImageSrc)
    {
        this.ImageSrc=ImageSrc;
    }
}
