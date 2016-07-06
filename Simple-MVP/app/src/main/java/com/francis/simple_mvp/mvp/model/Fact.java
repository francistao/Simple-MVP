package com.francis.simple_mvp.mvp.model;

/**
 * Created by dream on 16/7/6.
 */
public class Fact {
    /**
     * These members will be used by Retrofit, so you must use the same name with json
     */
    private String title;
    private String description;
    private String imageHref;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageHref() {
        return imageHref;
    }

    public void setImageHref(String imageHref) {
        this.imageHref = imageHref;
    }
}
