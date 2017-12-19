package com.example.cjnote.ae4teamapp;

/**
 * Created by 유제호 on 2017-12-16.
 */

class Users {
    private String email;
    private String nickname;

    public Users() {
        this.email = "";
        this.nickname = "";
    }

    public Users(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
    public String getEmail() {
        return email;
    }
    public String getNickname() {
        return nickname;
    }
    public void setEmail(String email){
        this.email = email;
    }
//함수앞에 String 쓰는이유
}
