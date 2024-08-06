package com.duyle.asm1;

import android.os.Parcel;
import android.os.Parcelable;

public class CarModel implements Parcelable {
    private String _id;
    private String ten;
    private int namSX;
    private String hang;
    private double gia;
    private String imageUrl;

    public CarModel(String _id, String ten, int namSX, String hang, double gia, String imageUrl) {
        this._id = _id;
        this.ten = ten;
        this.namSX = namSX;
        this.hang = hang;
        this.gia = gia;
        this.imageUrl = imageUrl;
    }

    public CarModel(String ten, int namSX, String hang, double gia, String imageUrl) {
        this.ten = ten;
        this.namSX = namSX;
        this.hang = hang;
        this.gia = gia;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getNamSX() {
        return namSX;
    }

    public void setNamSX(int namSX) {
        this.namSX = namSX;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Parcelable implementation
    protected CarModel(Parcel in) {
        _id = in.readString();
        ten = in.readString();
        namSX = in.readInt();
        hang = in.readString();
        gia = in.readDouble();
        imageUrl = in.readString();
    }

    public static final Creator<CarModel> CREATOR = new Creator<CarModel>() {
        @Override
        public CarModel createFromParcel(Parcel in) {
            return new CarModel(in);
        }

        @Override
        public CarModel[] newArray(int size) {
            return new CarModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(ten);
        dest.writeInt(namSX);
        dest.writeString(hang);
        dest.writeDouble(gia);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
