package com.monenco.insights.models;

public class ArticlePart implements Model{
    public static final int TYPE_IMAGE = 2;
    public static final int TYPE_TEXT = 1;

    public String content;
    public String image;
    public String title;
    public int type;
}
