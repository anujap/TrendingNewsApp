package com.example.anuja.trendingnewsapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.anuja.trendingnewsapp.BR;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This is the class for Articles model
 */
public class Articles extends BaseObservable implements Parcelable {

    @SerializedName("source")
    @Expose
    private Source source;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("urlToImage")
    @Expose
    private String urlToImage;

    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;

    @SerializedName("content")
    @Expose
    private String content;

    private String articleId;

    private String isFav;

    public Articles() {}

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    @Bindable
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
        notifyPropertyChanged(BR.author);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.url);
    }

    @Bindable
    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
        notifyPropertyChanged(BR.urlToImage);
    }

    @Bindable
    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
        notifyPropertyChanged(BR.publishedAt);
    }

    @Bindable
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        notifyPropertyChanged(BR.content);
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getIsFav() {
        return isFav;
    }

    public void setIsFav(String isFav) {
        this.isFav = isFav;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //parcel.writeO(this.sourceArrayList);
        parcel.writeString(this.author);
        parcel.writeString(this.title);
        parcel.writeString(this.description);
        parcel.writeString(this.url);
        parcel.writeString(this.urlToImage);
        parcel.writeString(this.publishedAt);
        parcel.writeString(this.content);
        parcel.writeString(this.articleId);
        parcel.writeString(this.isFav);
    }

    private Articles(Parcel in) {
        this.author = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.url = in.readString();
        this.urlToImage = in.readString();
        this.publishedAt = in.readString();
        this.content = in.readString();
        this.articleId = in.readString();
        this.isFav = in.readString();
    }

    /**
     * generates instances of the Parcelable class from a Parcel
     */
    public static final Parcelable.Creator<Articles> CREATOR = new Parcelable.Creator<Articles>() {

        /**
         * create a new instance of the Parcelable class, instantiating it
         * from the given Parcel whose data had previously been written by
         */
        @Override
        public Articles createFromParcel(Parcel parcel) {
            return new Articles(parcel);
        }

        // create a new array of the Parcelable class.
        @Override
        public Articles[] newArray(int size) {
            return new Articles[size];
        }
    };

    /**
     * describe the kinds of special objects contained in this
     * Parcelable instance's marshaled representation.
     */
    @Override
    public int describeContents() {
        return 0;
    }
}
