package com.vjy.justfollow.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.vjy.justfollow.R;
import com.vjy.justfollow.app.AppController;
import com.vjy.justfollow.custom_view.CircularNetworkImageView;
import com.vjy.justfollow.likebutton.LikeButton;
import com.vjy.justfollow.likebutton.OnLikeListener;
import com.vjy.justfollow.model.FeedItem;
import com.vjy.justfollow.network.request.PostLikeDislikeRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vijay Kumar on 13-09-2017.
 */

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.ViewHolder> implements OnLikeListener{

    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_button";
    public static final String ACTION_LIKE_IMAGE_CLICKED = "action_like_image_button";

    public static final int VIEW_TYPE_DEFAULT = 1;
    public static final int VIEW_TYPE_LOADER = 2;




    private List<FeedItem> feedItems;


    private ImageLoader imageLoader;

    private OnFeedItemClickListener onFeedItemClickListener;

    public FeedRecyclerAdapter(List<FeedItem> feedItems) {
        this.feedItems = feedItems;
        this.imageLoader = AppController.getInstance().getImageLoader();
    }

    @Override
    public FeedRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent,false);

        ViewHolder viewHolder = new ViewHolder(itemView);
        setupClickableViews(itemView, viewHolder);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(FeedRecyclerAdapter.ViewHolder holder, int position) {
        FeedItem item = feedItems.get(position);

        holder.bindView(item);

        holder.name.setText(item.getName());

        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.timestamp.setText(timeAgo);


        // Chcek for empty status message
        if (!TextUtils.isEmpty(item.getStatus())) {
            holder.statusMsg.setText(item.getStatus());
            holder.statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            holder.statusMsg.setVisibility(View.GONE);
        }


        // Checking for null feed url
        if (item.getUrl() != null) {
            holder.url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
                    + item.getUrl() + "</a> "));

            // Making url clickable
            holder.url.setMovementMethod(LinkMovementMethod.getInstance());
            holder.url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            holder.url.setVisibility(View.GONE);
        }


        String p = "https://s3-us-west-1.amazonaws.com/com.localapp.profile.image/5908362ac44dc510a4cfe1bc1502333400497";
        String u = "https://bgcdn.s3.amazonaws.com/wp-content/uploads/2013/04/5-2-God-is-in-Nature-1024x681.jpg";

//        item.setImage(u);//todo fgdggdsg

        // user profile pic
        holder.profilePic.setImageUrl(item.getProfilePic(), imageLoader);

        // Feed image
        if (item.getImage() != null) {
            holder.feedImageView.setImageUrl(item.getImage(), imageLoader);
            holder.feedImageView.setVisibility(View.VISIBLE);
            /*holder.feedImageView
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });*/
        } else {
            holder.feedImageView.setVisibility(View.GONE);
        }




    }





    public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
        this.onFeedItemClickListener = onFeedItemClickListener;
    }





    private void setupClickableViews (final View view, final ViewHolder holder) {

        holder.btnLike.setOnLikeListener(this, holder);

        /*holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
//                feedItems.get(adapterPosition).
                holder.getFeedItem().setLikeCount(feedItems.get(adapterPosition).getLikeCount()+1);
                holder.getFeedItem().setLiked(!holder.getFeedItem().isLiked());

                notifyItemChanged(adapterPosition, ACTION_LIKE_IMAGE_CLICKED);
            }
        });*/


        holder.feedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*int adapterPosition = holder.getAdapterPosition();
                feedItems.get(adapterPosition).setLikeCount(feedItems.get(adapterPosition).getLikeCount()+1);
                notifyItemChanged(adapterPosition, ACTION_LIKE_IMAGE_CLICKED);*/
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFeedItemClickListener.onMoreClick(v, holder.getAdapterPosition());
            }
        });

        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFeedItemClickListener.onCommentsClick(v, holder.getAdapterPosition());
            }
        });

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return feedItems.size();
    }


    @Override
    public void liked(LikeButton likeButton, FeedRecyclerAdapter.ViewHolder holder) {
        int adapterPosition = holder.getAdapterPosition();

        FeedItem feedItem = feedItems.get(adapterPosition);
        feedItem.likeCount++;
        feedItem.setLiked(true);
        likeDislikeRequest(feedItem.getId(), true);
//        holder.getFeedItem().setLikeCount(feedItems.get(adapterPosition).getLikeCount()+1);
        notifyItemChanged(adapterPosition, ACTION_LIKE_IMAGE_CLICKED);
    }

    @Override
    public void unLiked(LikeButton likeButton, FeedRecyclerAdapter.ViewHolder holder) {
        int adapterPosition = holder.getAdapterPosition();

        FeedItem feedItem = feedItems.get(adapterPosition);
        feedItem.likeCount--;
        feedItem.setLiked(false);
        likeDislikeRequest(feedItem.getId(), false);

        notifyItemChanged(adapterPosition, ACTION_LIKE_IMAGE_CLICKED);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.name) TextView name ;
        @BindView(R.id.timestamp)TextView timestamp ;
        @BindView(R.id.txtStatusMsg)TextView statusMsg;
        @BindView(R.id.txtUrl) TextView url;
        @BindView(R.id.profilePic) CircularNetworkImageView profilePic;
        @BindView(R.id.feedImage1) NetworkImageView feedImageView;

        @BindView(R.id.btn_like)
        LikeButton btnLike;

        @BindView(R.id.btn_comment)
        ImageView btnComment;

        @BindView(R.id.btn_more)
        ImageView btnShare;

        @BindView(R.id.tsLikesCounter)
        TextSwitcher tsLikesCounter;



        FeedItem feedItem;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bindView(final FeedItem feedItem) {
            this.feedItem = feedItem;
            final int adapterPosition = getAdapterPosition();
//            tsLikesCounter.setText(feedItem.likeCount+ "likes");
            tsLikesCounter.setText(profilePic.getResources().getQuantityString(
                    R.plurals.likes_count, feedItem.likeCount, feedItem.likeCount
            ));


            btnLike.setLiked(feedItem.isLiked());
        }



        public FeedItem getFeedItem() {
            return feedItem;
        }
    }


    private void likeDislikeRequest(String itemId, boolean like) {
        PostLikeDislikeRequest likeDislikeRequest = new PostLikeDislikeRequest(itemId, like);
        likeDislikeRequest.executeRequest();
    }



    public interface OnFeedItemClickListener {
        void onCommentsClick(View v, int position);

        void onMoreClick(View v, int position);

        void onProfileClick(View v);
    }

}
