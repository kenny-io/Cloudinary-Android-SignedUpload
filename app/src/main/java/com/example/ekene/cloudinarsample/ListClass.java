package com.example.ekene.cloudinarsample;

import android.widget.ImageView;

/**
 * Created by EKENE on 3/2/2018.
 */

public class ListClass {

    private String imageUrl;
    private String title;
    private String pprice;

    public ListClass(String imageUrl, String title, String price) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.pprice = price;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }
    public String getPprice() {
        return pprice;
    }
}
