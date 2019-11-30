package com.pakdev.fileuploading;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pakdev.fileuploading.api.WebServiceFactory;


import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnSelectImage)
    Button btnSelectImage;
    @BindView(R.id.btnUploadImage)
    Button btnUploadImage;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.IpAddress)
    EditText IpAddress;
    private int REQUEST_GET_SINGLE_FILE = 01;

    File imageUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

    @OnClick({R.id.btnSelectImage, R.id.btnUploadImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSelectImage:


            {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);


            }
            break;
            case R.id.btnUploadImage:

                if (TextUtils.isEmpty(IpAddress.getText().toString())) {
                    Toast.makeText(this, "Please enter Ip address with port number ", Toast.LENGTH_SHORT).show();
                    return;
                }
                pd = new ProgressDialog(MainActivity.this);
                pd.setMessage("uploading ...");
                pd.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        uploadFileToServer(imageUpload, IpAddress.getText().toString().trim());
                    }
                }, 2000);

                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GET_SINGLE_FILE);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_GET_SINGLE_FILE) {
                    Uri selectedImageUri = data.getData();
                    // Get the path from the Uri
                    final String path = FileUtils.getRealPath(this, selectedImageUri);
                    if (path != null) {
                        imageUpload = new File(path);
                        selectedImageUri = Uri.fromFile(imageUpload);
                    }
                    img.setImageURI(selectedImageUri);
                }
            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    ProgressDialog pd;

    private void uploadFileToServer(File file, String ipAddress) {


        MultipartBody.Part profilePicture = null;

//            File file = new File(profileImage);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        profilePicture = MultipartBody.Part.createFormData("file", file.getName(), reqFile);

        WebServiceFactory.getInstance(this, ipAddress).uploadFileToServer(profilePicture).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                pd.dismiss();
                if (response.code() == 200) {


                    showDialog("File uploaded successfully");
                } else {

                    showDialog("Failed to upload file");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                t.printStackTrace();

            }
        });
    }

    public void showDialog(String message) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Alert")
                .setMessage(message)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        dialog.dismiss();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
