package com.kyle.support.activitystarter.sample;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelObject implements Parcelable {

    protected ParcelObject(Parcel in) {
    }

    public static final Creator<ParcelObject> CREATOR = new Creator<ParcelObject>() {
        @Override
        public ParcelObject createFromParcel(Parcel in) {
            return new ParcelObject(in);
        }

        @Override
        public ParcelObject[] newArray(int size) {
            return new ParcelObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
