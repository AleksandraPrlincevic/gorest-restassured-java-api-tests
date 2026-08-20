package com.aleksandraprlincevic.gorest.pojo;

public class Post {
    int id;
    int user_id;
    String title;
    String body;

    public Post(){}
    public Post(int id, int user_id, String title, String body){
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.body = body;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
