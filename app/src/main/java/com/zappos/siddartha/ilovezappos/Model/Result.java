package com.zappos.siddartha.ilovezappos.Model;

/**
 * Created by Siddartha on 2/8/2017.
 *
 * parcelable since to pass between activities
 */

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.databinding.library.baseAdapters.BR;
import java.util.HashMap;
import java.util.Map;

public class Result extends BaseObservable implements Parcelable{

    private String brandName;
    private String thumbnailImageUrl;
    private String productId;
    private String originalPrice;
    private String styleId;
    private String colorId;
    private String price;
    private String percentOff;
    private String productUrl;
    private String productName;

    public Result(Parcel parcel){
        brandName = parcel.readString();
        thumbnailImageUrl = parcel.readString();
        productId = parcel.readString();
        originalPrice = parcel.readString();
        styleId = parcel.readString();
        colorId = parcel.readString();
        price = parcel.readString();
        percentOff = parcel.readString();
        productUrl = parcel.readString();
        productName = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brandName);
        dest.writeString(thumbnailImageUrl);
        dest.writeString(productId);
        dest.writeString(originalPrice);
        dest.writeString(styleId);
        dest.writeString(colorId);
        dest.writeString(price);
        dest.writeString(percentOff);
        dest.writeString(productUrl);
        dest.writeString(productName);
    }

    public static final Parcelable.Creator<Result> CREATOR
            = new Parcelable.Creator<Result>() {

        @Override
        public Result createFromParcel(Parcel parcel) {
            return new Result(parcel);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    @Bindable
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {

        this.brandName = brandName;
        notifyPropertyChanged(BR.brandName);
    }

    @Bindable
    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
        notifyPropertyChanged(BR.thumbnailImageUrl);
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
    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {

        this.originalPrice = originalPrice;
        notifyPropertyChanged(BR.originalPrice);
    }

    @Bindable
    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {

        this.styleId = styleId;
        notifyPropertyChanged(BR.styleId);
    }

    @Bindable
    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {

        this.colorId = colorId;
        notifyPropertyChanged(BR.colorId);
    }

    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {

        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    @Bindable
    public String getPercentOff() {
        return percentOff;
    }

    public void setPercentOff(String percentOff) {

        this.percentOff = percentOff;
        notifyPropertyChanged(BR.percentOff);
    }

    @Bindable
    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {

        this.productUrl = productUrl;
        notifyPropertyChanged(BR.productUrl);
    }

    @Bindable
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {

        this.productName = productName;
        notifyPropertyChanged(BR.productName);
    }
}




