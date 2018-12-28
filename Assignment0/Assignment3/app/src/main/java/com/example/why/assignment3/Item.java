package com.example.why.assignment3;

/**
 * Created by Why on 22/10/2017.
 */

public class Item {

    private String itemName;
    private String itemUsername;
    private String itemDescription;
    private String itemUrl;

    public Item(String name, String username, String description, String url) {
        this.itemName = name;
        this.itemUsername = username;
        this.itemDescription = description;
        this.itemUrl = url;
    }

    public String getItemName() {
        return this.itemName;
    }

    public String getItemUsername() {
        return this.itemUsername;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemUrl() {
        return this.itemUrl;
    }
}
