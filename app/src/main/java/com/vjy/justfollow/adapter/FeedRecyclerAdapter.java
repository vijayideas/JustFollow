package com.vjy.justfollow.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.vjy.justfollow.R;
import com.vjy.justfollow.app.AppController;
import com.vjy.justfollow.custom_view.CircularNetworkImageView;
import com.vjy.justfollow.custom_view.FeedImageView;
import com.vjy.justfollow.model.FeedItem;

import java.util.List;

/**
 * Created by Vijay Kumar on 13-09-2017.
 */

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.ViewHolder> {


    private List<FeedItem> feedItems;


    private ImageLoader imageLoader;

    public FeedRecyclerAdapter(List<FeedItem> feedItems) {
        this.feedItems = feedItems;
        this.imageLoader = AppController.getInstance().getImageLoader();
    }

    @Override
    public FeedRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent,false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(FeedRecyclerAdapter.ViewHolder holder, int position) {
        FeedItem item = feedItems.get(position);

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

        item.setImage(u);//todo fgdggdsg

        // user profile pic
        holder.profilePic.setImageUrl(p, imageLoader);

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

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name ;
        TextView timestamp ;
        TextView statusMsg;
        TextView url;
        CircularNetworkImageView profilePic;
        NetworkImageView feedImageView;


        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            timestamp = (TextView) itemView
                    .findViewById(R.id.timestamp);

            statusMsg = (TextView) itemView
                    .findViewById(R.id.txtStatusMsg);

            url = (TextView) itemView.findViewById(R.id.txtUrl);

            profilePic =  itemView
                    .findViewById(R.id.profilePic);

            feedImageView = (NetworkImageView) itemView
                    .findViewById(R.id.feedImage1);
        }
    }
}
