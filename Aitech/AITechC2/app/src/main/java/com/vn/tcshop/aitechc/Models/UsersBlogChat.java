package com.vn.tcshop.aitechc.Models;

public class UsersBlogChat {
    String profilepic,mail,userName,password,userId,lastMessage;

    public UsersBlogChat(){}

    public UsersBlogChat(String userId, String userName, String maill, String password, String profilepic) {
        this.userId = userId;
        this.userName = userName;
        this.mail = maill;
        this.password = password;
        this.profilepic = profilepic;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
