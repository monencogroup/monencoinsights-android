package com.monenco.insights.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.monenco.insights.StringHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.InputStream;

public class ImageViewHolder extends AbstractViewHolder {
    private int placeHolderResId;
    private static final int TRY_COUNT = 5;
    private int remainingTryCount;
    private String url;

    public ImageViewHolder(View rootView, int placeHolderResId) {
        super(rootView);
        this.placeHolderResId = placeHolderResId;
    }

    public void setImage(String url) {
        this.url = url;
        if (StringHelper.notNone(url)) {
//            new DownloadImageFromInternet((ImageView) getRootView())
//                    .execute(url);
            Picasso.with(getContext()).load(url).into((ImageView) getRootView());
        }
        reset();
        tryLoading();
    }

    private void reset() {
//        ((ImageView) getRootView()).setImageResource(placeHolderResId);
        remainingTryCount = TRY_COUNT;
    }

    private void tryLoading() {
        if (StringHelper.notNone(url)) {
            Log.e("IMAGE VIEW", url);
            if (remainingTryCount > 0) {

                Log.e("IMAGE VIEW TRY COUNT", remainingTryCount + "");
                remainingTryCount--;
                Picasso.with(getContext()).load(url).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Log.e("IMAGE VIEW", url + "\t" + "SUCCESS");
                        if (isAlive()) {
                            ((ImageView) getRootView()).setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        ((ImageView) getRootView()).setImageDrawable(errorDrawable);
                        Log.e("IMAGE VIEW", url + "\t" + "FAILURE");
                        if (isAlive()) {
                            tryLoading();
                        }
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        }
    }
}

class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;

    public DownloadImageFromInternet(ImageView imageView) {
        this.imageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        String imageURL = urls[0];
        Bitmap bimage = null;
        try {
            InputStream in = new java.net.URL(imageURL).openStream();
            bimage = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            Log.e("Error Message", e.getMessage());
            e.printStackTrace();
        }
        return bimage;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}
