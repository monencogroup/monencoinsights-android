package com.monenco.insights.models;

import java.util.List;

public class Article implements Model {
    public int id;
    public String title;
    public String image;
    public String leadText;
    public String creationDate;
    public boolean isBookmarked;
    public List<Tag> tags;
    public List<ArticlePart> parts;
    public Category category;
    public Author author;
    public boolean bookmarkLoading;
    public boolean purchasable;
    public boolean purchased;
    public int price;
    public String cafeBazarPaymentId;
}

