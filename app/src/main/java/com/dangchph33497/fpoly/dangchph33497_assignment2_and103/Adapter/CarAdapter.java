package com.dangchph33497.fpoly.dangchph33497_assignment2_and103.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dangchph33497.fpoly.dangchph33497_assignment2_and103.Model.Car;
import com.dangchph33497.fpoly.dangchph33497_assignment2_and103.R;


import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolderOfCars> {
    public interface OnItemClickListener{
        void onItemClick(String id);
        void updateItem(String id, String tenXe, int gia, String anh, String loaiXe);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.click = onItemClickListener;
    }
    private OnItemClickListener click;
    public List<Car> carList = new ArrayList<>();
    public void setData(List<Car> carList){
        this.carList = carList;
    }
    private Context context;
    @NonNull
    @Override
    public ViewHolderOfCars onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car,parent,false);
        return new ViewHolderOfCars(view);
    }
    public CarAdapter(Context context) {
        this.context = context;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolderOfCars holder, int position) {
        Car car = carList.get(position);
        if(car == null){
            Log.d("sssssssssssssssss","Danh sách trống"+car.toString());
            return;
        }
        holder.tvTenXe.setText(car.getTenXe());
        holder.tvGiaXe.setText(String.valueOf(car.getGia()));
        holder.tvLoaiXe.setText(car.getLoaiXe());
        String imageUrl = car.getFormattedImageUrl();
        if (imageUrl != null) {
            String newUrl = imageUrl.replace("localhost", "192.168.1.249");
            Log.d("sssssssssssssssssssssssssss",newUrl);
            Glide.with(context)
                    .load(newUrl)
                    .thumbnail(Glide.with(context).load(R.drawable.warning))
                    .into(holder.imgAvatar);
        } else {
            Log.e("Image URL Error", "Image URL is null");
        }
        holder.btnXoa.setOnClickListener(v -> {
            String id = carList.get(holder.getAdapterPosition()).getId();
            click.onItemClick(id);
        });
        holder.btnSua.setOnClickListener(v -> {
            String Id = carList.get(holder.getAdapterPosition()).getId();
            String tenXe = carList.get(holder.getAdapterPosition()).getTenXe();
            int gia = carList.get(holder.getAdapterPosition()).getGia();
            String anh = imageUrl;
            Log.d("ssssssssssssssss",anh);
            String loaiXe = carList.get(holder.getAdapterPosition()).getLoaiXe();
            click.updateItem(Id,tenXe,gia,anh,loaiXe);
        });
    }



    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class ViewHolderOfCars extends RecyclerView.ViewHolder {
        TextView tvTenXe,tvGiaXe,tvLoaiXe;
        ImageView imgAvatar;
        TextView btnXoa,btnSua;
        public ViewHolderOfCars(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.ivAnhXe);
            tvTenXe = itemView.findViewById(R.id.tvTenXe);
            tvGiaXe =  itemView.findViewById(R.id.tvGiaXe);
            tvLoaiXe =  itemView.findViewById(R.id.tvLoaiXe);
            btnSua = itemView.findViewById(R.id.btnSua);
            btnXoa = itemView.findViewById(R.id.btnXoa);
        }
    }
}
