package com.example.andrew.softtecodemo;

import java.util.HashMap;

/**
 * Created by Andrew on 06.01.2017.
 */

public class GridItems {

    public int id;
    public String userId;
    public String postId;
    public String title;
    public String body;
    public HashMap<String,String> map;

    public GridItems(int id, HashMap<String,String> map) {
        this.id = id;
        this.userId = map.get("userId");
        this.postId = map.get("id");
        this.title = map.get("title");
        this.body = map.get("body");
    }
}