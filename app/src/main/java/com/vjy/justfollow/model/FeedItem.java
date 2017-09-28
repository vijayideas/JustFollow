package com.vjy.justfollow.model;

/**
 * Created by Vijay Kumar on 13-09-2017.
 */

public class FeedItem {

    private String id;
    public int likeCount;
    private String name, status, image, profilePic, timeStamp, url;
    private boolean isLiked;

    public FeedItem() {
    }

    public FeedItem(String id, String name, String status, String image, String profilePic, String timeStamp, String url) {
        super();

        this.id = id;
        this.name = name;
        this.status = status;
        this.image = image;
        this.profilePic = profilePic;
        this.timeStamp = timeStamp;
        this.url = url;

        this.likeCount = 0;
        this.isLiked = false;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
