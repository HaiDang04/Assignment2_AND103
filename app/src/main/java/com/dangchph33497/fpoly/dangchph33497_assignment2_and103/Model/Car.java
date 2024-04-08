package com.dangchph33497.fpoly.dangchph33497_assignment2_and103.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Car implements Serializable {
    @SerializedName("_id")
    private String id;
    private String tenXe;
    private int gia;
    private String anh;
    private String loaiXe;

    public Car() {
    }
    @Override
    public String toString() {
        return "Car{" +
                "tenXe='" + tenXe + '\'' +
                ", gia=" + gia +
                ", anhXe='" + anh + '\'' +
                ", loaiXe='" + loaiXe + '\'' +
                '}';
    }
    public Car(String tenXe, int gia, String anh, String loaiXe) {
        this.tenXe = tenXe;
        this.gia = gia;
        this.anh = anh;
        this.loaiXe = loaiXe;
    }

    public String getFormattedImageUrl() {
        if (anh != null) {
            return anh.replace("localhost", "192.168.1.249");
        } else {
            return null;
        }
    }
    public Car(String id, String tenXe, int gia, String anh, String loaiXe) {
        this.id = id;
        this.tenXe = tenXe;
        this.gia = gia;
        this.anh = anh;
        this.loaiXe = loaiXe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenXe() {
        return tenXe;
    }

    public void setTenXe(String tenXe) {
        this.tenXe = tenXe;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(String loaiXe) {
        this.loaiXe = loaiXe;
    }
}
