package com.vjy.justfollow.custom_view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.github.siyamed.shapeimageview.ShaderImageView;
import com.github.siyamed.shapeimageview.shader.CircleShader;
import com.github.siyamed.shapeimageview.shader.ShaderHelper;

/**
 * Created by Vijay Kumar on 03-09-2017.
 */

public class CircularNetworkImageView extends ShaderImageView {
    private CircleShader shader;


    /**
     * The URL of the network image to load
     */
    private String mUrl;

    /**
     * Resource ID of the image to be used as a placeholder until the network
     * image is loaded.
     */
    private int mDefaultImageId;

    /**
     * Resource ID of the image to be used if the network response fails.
     */
    private int mErrorImageId;

    /**
     * Local copy of the ImageLoader.
     */
    private ImageLoader mImageLoader;

    /**
     * Current ImageContainer. (either in-flight or finished)
     */
    private ImageLoader.ImageContainer mImageContainer;



    public CircularNetworkImageView(Context context) {
        super(context);
    }

    public CircularNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        shader = new CircleShader();
        return shader;
    }

    public float getBorderRadius() {
        if(shader != null) {
            return shader.getBorderRadius();
        }
        return 0;
    }

    public void setBorderRadius(final float borderRadius) {
        if(shader != null) {
            shader.setBorderRadius(borderRadius);
            invalidate();
        }
    }



    public void setImageUrl(String url, ImageLoader imageLoader) {
        mUrl = url;
        mImageLoader = imageLoader;
        // The URL has potentially changed. See if we need to load it.
        loadImageIfNecessary(false);
    }



    private void loadImageIfNecessary(final boolean isInLayoutPass) {
        final int width = getWidth();
        int height = getHeight();

        boolean isFullyWrapContent = getLayoutParams() != null
                && getLayoutParams().height == LinearLayout.LayoutParams.WRAP_CONTENT
                && getLayoutParams().width == LinearLayout.LayoutParams.WRAP_CONTENT;
        // if the view's bounds aren't known yet, and this is not a
        // wrap-content/wrap-content
        // view, hold off on loading the image.
        if (width == 0 && height == 0 && !isFullyWrapContent) {
            return;
        }

        // if the URL to be loaded in this view is empty, cancel any old
        // requests and clear the
        // currently loaded image.
        if (TextUtils.isEmpty(mUrl)) {
            if (mImageContainer != null) {
                mImageContainer.cancelRequest();
                mImageContainer = null;
            }
            setDefaultImageOrNull();
            return;
        }

        // if there was an old request in this view, check if it needs to be
        // canceled.
        if (mImageContainer != null && mImageContainer.getRequestUrl() != null) {
            if (mImageContainer.getRequestUrl().equals(mUrl)) {
                // if the request is from the same URL, return.
                return;
            } else {
                // if there is a pre-existing request, cancel it if it's
                // fetching a different URL.
                mImageContainer.cancelRequest();
                setDefaultImageOrNull();
            }
        }

        // The pre-existing content of this view didn't match the current URL.
        // Load the new image
        // from the network.

        // update the ImageContainer to be the new bitmap container.
        mImageContainer = mImageLoader.get(mUrl,
                new ImageLoader.ImageListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (mErrorImageId != 0) {
                            setImageResource(mErrorImageId);
                        }

                        /*if (mObserver != null) {
                            mObserver.onError();
                        }*/
                    }

                    @Override
                    public void onResponse(final ImageLoader.ImageContainer response,
                                           boolean isImmediate) {
                        // If this was an immediate response that was delivered
                        // inside of a layout
                        // pass do not set the image immediately as it will
                        // trigger a requestLayout
                        // inside of a layout. Instead, defer setting the image
                        // by posting back to
                        // the main thread.
                        if (isImmediate && isInLayoutPass) {
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    onResponse(response, false);
                                }
                            });
                            return;
                        }

                        int bWidth = 0, bHeight = 0;
                        if (response.getBitmap() != null) {

                            setImageBitmap(response.getBitmap());
                            bWidth = response.getBitmap().getWidth();
                            bHeight = response.getBitmap().getHeight();
                            adjustImageAspect(bWidth, bHeight);

                        } else if (mDefaultImageId != 0) {
                            setImageResource(mDefaultImageId);
                        }

                        /*if (mObserver != null) {
                            mObserver.onSuccess();

                        }*/
                    }
                });

    }



    private void setDefaultImageOrNull() {
        if (mDefaultImageId != 0) {
            setImageResource(mDefaultImageId);
        } else {
            setImageBitmap(null);
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        loadImageIfNecessary(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mImageContainer != null) {
            // If the view was bound to an image request, cancel it and clear
            // out the image from the view.
            mImageContainer.cancelRequest();
            setImageBitmap(null);
            // also clear out the container so we can reload the image if
            // necessary.
            mImageContainer = null;
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }


    /*
     * Adjusting imageview height
     * */
    private void adjustImageAspect(int bWidth, int bHeight) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();

        if (bWidth == 0 || bHeight == 0)
            return;

        int swidth = getWidth();
        int new_height = 0;
        new_height = swidth * bHeight / bWidth;
        params.width = swidth;
        params.height = new_height;
        setLayoutParams(params);
    }


}
