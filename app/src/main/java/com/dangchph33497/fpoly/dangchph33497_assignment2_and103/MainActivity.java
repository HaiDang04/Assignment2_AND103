package com.dangchph33497.fpoly.dangchph33497_assignment2_and103;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.dangchph33497.fpoly.dangchph33497_assignment2_and103.Model.Car;
import com.dangchph33497.fpoly.dangchph33497_assignment2_and103.Adapter.CarAdapter;
import com.dangchph33497.fpoly.dangchph33497_assignment2_and103.Service.HttpRequest;
import com.dangchph33497.fpoly.dangchph33497_assignment2_and103.Service.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    RecyclerView lvMain;
    List<Car> carList;
    CarAdapter carAdapter;
    FloatingActionButton fab;
    HttpRequest httpRequest;
    com.google.android.material.textfield.TextInputEditText edSearch;
    ImageView imgEdit;
    File file;
    Button btnUpdate,btnCancel;
    EditText edtTenXe,edtGia;
    Spinner spinnerLoai;
    Uri imageUri;
    Spinner spinnerSort;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        httpRequest = new HttpRequest();
        fab = findViewById(R.id.fab);
        lvMain = findViewById(R.id.listviewMain);
        carAdapter = new CarAdapter(MainActivity.this);
        edSearch = findViewById(R.id.edSearch);
        spinnerSort = findViewById(R.id.spinnerSort);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.sap_xep_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);
        spinnerSort.
                setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String selectedOption = parent.getItemAtPosition(position).toString();
                                sortCars(selectedOption);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                carAdapter.setOnItemClickListener(new CarAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(String id) {
                        deleteCar(id);
                    }

                    @Override
                    public void updateItem(String id, String tenXe, int gia, String anh, String loaiXe) {
                        updateCar(id, tenXe, gia, anh, loaiXe);
                    }
                });

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,AddCar.class);
            startActivity(intent);
        });
        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionID, KeyEvent event) {
                if(actionID == EditorInfo.IME_ACTION_SEARCH){
                    String key = edSearch.getText().toString().trim();
                    if (key.isEmpty()) {
                        onResume();
                    } else {
                        httpRequest.callApi().searchCar(key).enqueue(searchCars);
                    }
                    return true;
                }
                return false;
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lvMain.setLayoutManager(linearLayoutManager);
        onResume();
    }

    Callback<Response<ArrayList<Car>>> getCars = new Callback<Response<ArrayList<Car>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Car>>> call, retrofit2.Response<Response<ArrayList<Car>>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus() == 200 ){
                    carList = new ArrayList<>();
                    carList = response.body().getData();
                    Log.d("Danh sách", "onResponse: "+carList.toString());
                    carAdapter.setData(carList);
                    lvMain.setAdapter(carAdapter);
                    for (Car car: carList
                    ) {
                        Log.d("ssssssssssssss",car.toString());
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Car>>> call, Throwable throwable) {
            Log.d("Kiểu này nè",throwable.getMessage());
        }
    };
    Callback<Response<ArrayList<Car>>> searchCars = new Callback<Response<ArrayList<Car>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Car>>> call, retrofit2.Response<Response<ArrayList<Car>>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus() == 200 ){
                    carList = new ArrayList<>();
                    carList = response.body().getData();

                    carAdapter.setData(carList);
                    lvMain.setAdapter(carAdapter);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Car>>> call, Throwable throwable) {
            Log.d("Kiểu này nè",throwable.getMessage());
        }
    };
    Callback<Response<Car>> deleteCar = new Callback<Response<Car>>() {
        @Override
        public void onResponse(Call<Response<Car>> call, retrofit2.Response<Response<Car>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus() == 200){
                    Toast.makeText(MainActivity.this, "Xoá thành công", Toast.LENGTH_SHORT).show();
                    onResume();

                }
            }
        }
        @Override
        public void onFailure(Call<Response<Car>> call, Throwable throwable) {
            Log.d("sssssssssssssssssss",throwable.getMessage());
        }
    };
    @Override
    protected void onResume(){
        super.onResume();
        httpRequest.callApi().getCars().enqueue(getCars);
    }

    public void deleteCar(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Xóa Distributor");
        builder.setMessage("Bạn có muốn xóa?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        httpRequest.callApi().deleteCarById(id).enqueue(deleteCar);
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
    Callback<Response<Car>> editCar = new Callback<Response<Car>>() {
        @Override
        public void onResponse(Call<Response<Car>> call, retrofit2.Response<Response<Car>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus() == 200){
                    Toast.makeText(MainActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    onResume();

                }
            }
        }
        @Override
        public void onFailure(Call<Response<Car>> call, Throwable throwable) {
            Log.d("sssssssssssssssssss",throwable.getMessage());
        }
    };

    public void updateCar(String id, String tenXe, int gia, String anh, String loaiXe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View view = inflater.inflate(R.layout.update_car, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnCancel = view.findViewById(R.id.btnCancel);
        imgEdit = view.findViewById(R.id.imgEdit);
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        edtTenXe = view.findViewById(R.id.edtTenXe);
        edtGia = view.findViewById(R.id.edtGia);
        spinnerLoai = view.findViewById(R.id.spinnerLoai);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.loai_xe_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoai.setAdapter(adapter);
        edtTenXe.setText(tenXe);
        edtGia.setText(String.valueOf(gia));
        if (loaiXe != null) {
            int spinnerPosition = adapter.getPosition(loaiXe);
            spinnerLoai.setSelection(spinnerPosition);
        }
        if (anh != null) {
            imageUri = Uri.parse(anh);
            Log.d("zzzzzzzzzzzzzzzzzz", "updateCar: "+imageUri.toString());
            Log.d("ssssssssssssssss",anh);
            Glide.with(MainActivity.this) .load(imageUri)
                    .thumbnail(Glide.with(MainActivity.this).load(R.drawable.warning))
                    .skipMemoryCache(false)
                    .into(imgEdit);
        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenXe = edtTenXe.getText().toString();
                int gia = Integer.parseInt(edtGia.getText().toString());
                String loaiXe = spinnerLoai.getSelectedItem().toString();
                if (tenXe.isEmpty() || gia == 0) {
                    Toast.makeText(MainActivity.this, "Mời không để trống trường dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (anh == null) {
                    Toast.makeText(MainActivity.this, "Xin chọn ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("anh", file.getName(), requestFile);
                RequestBody tenXeRequestBody = RequestBody.create(MediaType.parse("text/plain"), tenXe);
                RequestBody giaRequestBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(gia));
                RequestBody loaiXeRequestBody = RequestBody.create(MediaType.parse("text/plain"), loaiXe);
                httpRequest.callApi().updateCarById(id,tenXeRequestBody,giaRequestBody,loaiXeRequestBody,body).enqueue(editCar);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            imageUri = data.getData();
                            file = createFileFromUri(imageUri, "anh");
                            Glide.with(MainActivity.this)
                                    .load(imageUri)
                                    .thumbnail(Glide.with(MainActivity.this).load(R.drawable.warning))
                                    .skipMemoryCache(false)
                                    .into(imgEdit);
                        }
                    }
                }
            });


    private File createFileFromUri(Uri path, String name) {
        File _file = new File(MainActivity.this.getCacheDir(), name + ".png");
        try {
            InputStream in = MainActivity.this.getContentResolver().openInputStream(path);
            if (in == null) {
                Log.e("createFileFromUri", "Input stream is null");
                return null;
            }
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            Log.d("createFileFromUri", "File created successfully: " + _file.getAbsolutePath());
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("createFileFromUri", "File not found: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("createFileFromUri", "IOException: " + e.getMessage());
        }
        return null;
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);
    }
    private void sortCars(String sortOption) {
        String sortOrder;
        if (sortOption.equals("Bot to Top")) {
            sortOrder = "asc";
        } else {
            sortOrder = "desc";
        }

        httpRequest.callApi().sortCarsByPrice(sortOrder).enqueue(new Callback<Response<ArrayList<Car>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Car>>> call, retrofit2.Response<Response<ArrayList<Car>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        ArrayList<Car> sortedCars = response.body().getData();
                        carAdapter.setData(sortedCars);
                        carAdapter.notifyDataSetChanged();
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Car>>> call, Throwable throwable) {
                // Handle failure
            }
        });
    }

}