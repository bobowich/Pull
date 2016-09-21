package com.zy.xmlparser;

import java.io.Serializable;

/**
 * Created by bobowich
 * Time: 2016/9/21.
 */
public class Book implements Serializable{
    private String name;
    private int id;
    private int page;
    private float price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "book name = " + name + "/id = " + id + "/page = " + page + "/price = " + price;
    }
}
