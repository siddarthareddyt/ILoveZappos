package com.zappos.siddartha.ilovezappos.Model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by Siddartha on 2/8/2017.
 *
 *
 * to hold external activity's call for product page
 */

public class Product extends BaseObservable{

    private String defaultProductUrl;
    private String defaultImageUrl;
    private String productId;
    private String productName;
    private String brandName;
    private String brandId;
    private String price;

    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }



    @Bindable
    public String getDefaultProductUrl() {
        return defaultProductUrl;
    }

    public void setDefaultProductUrl(String defaultProductUrl) {
        this.defaultProductUrl = defaultProductUrl;
        notifyPropertyChanged(BR.defaultProductUrl);
    }

    @Bindable
    public String getDefaultImageUrl() {
        return defaultImageUrl;
    }

    public void setDefaultImageUrl(String defaultImageUrl) {
        this.defaultImageUrl = defaultImageUrl;
        notifyPropertyChanged(BR.defaultImageUrl);
    }

    @Bindable
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
        notifyPropertyChanged(BR.productId);
    }

    @Bindable
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
        notifyPropertyChanged(BR.productName);
    }

    @Bindable
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
        notifyPropertyChanged(BR.brandName);
    }

    @Bindable
    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
        notifyPropertyChanged(BR.brandId);
    }

}
