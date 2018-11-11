package com.example.anuja.trendingnewsapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This is the class for Source model
 */
public class Source implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    public Source() {}

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

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);

    }

    private Source(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    /**
     * generates instances of the Parcelable class from a Parcel
     */
    public static final Parcelable.Creator<Source> CREATOR = new Parcelable.Creator<Source>() {

        /**
         * create a new instance of the Parcelable class, instantiating it
         * from the given Parcel whose data had previously been written by
         */
        @Override
        public Source createFromParcel(Parcel parcel) {
            return new Source(parcel);
        }

        // create a new array of the Parcelable class.
        @Override
        public Source[] newArray(int size) {
            return new Source[size];
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
