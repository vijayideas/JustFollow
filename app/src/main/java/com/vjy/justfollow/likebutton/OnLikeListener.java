package com.vjy.justfollow.likebutton;

import com.vjy.justfollow.adapter.FeedRecyclerAdapter;

/**
 * Created by Vijay Kumar on 22-09-2017.
 */

public interface OnLikeListener {

    void liked(LikeButton likeButton, FeedRecyclerAdapter.ViewHolder holder);
    void unLiked(LikeButton likeButton, FeedRecyclerAdapter.ViewHolder holder);
}
