package org.FriendRequests;

public class FriendRequest {

    private String name;

    public FriendRequest(String username){
        this.name = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
