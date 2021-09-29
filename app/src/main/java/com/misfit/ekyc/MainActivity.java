package com.misfit.ekyc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.misfit.ekyc.data.NRCMODEL;
import com.misfit.ekyc.databinding.ActivityMainBinding;
import com.misfit.ekyc.http.ApiService;
import com.misfit.ekyc.http.Controller;
import com.misfit.ekyc.utility.Utility;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Context context;
    Utility utility;
    ApiService apiInterface = Controller.getBaseClient().create(ApiService.class);
    Gson gson = new Gson();
    NRCMODEL model;
    boolean nrcback = false;
    File nrcfrontimage = null;
    Uri nrcfrontimageuri = null;
    File nrcbackimage = null;
    TextView app_tittle;


    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            context = this;
            utility = new Utility(context);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_layout);
            app_tittle = (TextView) findViewById(R.id.tvTitle);
            app_tittle.setText(R.string.reg_string);
            clickwork();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    void clickwork() {
        try {
            binding.submitPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    utility.hideKeyboard(view);
                    if (!TextUtils.isEmpty(binding.phoneInput.getEditableText().toString())) {
                        binding.phoneInputView.setVisibility(View.GONE);
                        binding.submitPhone.setVisibility(View.GONE);
                        binding.view1.setVisibility(View.GONE);
                        binding.view2.setVisibility(View.VISIBLE);
                        binding.otpView.setVisibility(View.VISIBLE);
                    } else {
                        binding.phoneInput.setError(context.getResources().getString(R.string.phonehint_string));
                        binding.phoneInput.requestFocus();
                    }
                }
            });
            binding.submitOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    utility.hideKeyboard(view);
                    if (!TextUtils.isEmpty(binding.otpInput.getOTP().toString()) && binding.otpInput.getOTP().toString().equalsIgnoreCase("0000")) {
                        binding.phoneInputView.setVisibility(View.GONE);
                        binding.submitPhone.setVisibility(View.GONE);
                        binding.otpView.setVisibility(View.GONE);
                        binding.view3.setVisibility(View.GONE);
                        binding.view2.setVisibility(View.GONE);
                        binding.view4.setVisibility(View.VISIBLE);
                    } else if (!binding.otpInput.getOTP().toString().equalsIgnoreCase("0000")) {
                        utility.showToast("Otp is not valid");
                    } else {
                        binding.otpInput.showError();
                        binding.otpInput.requestFocus();
                    }
                }
            });

            binding.nrcFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePicker.with(MainActivity.this)                         //  Initialize ImagePicker with activity or fragment context
                            .setToolbarColor("#ff0e0e")         //  Toolbar color
                            .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                            .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                            .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                            .setProgressBarColor("#4CAF50")     //  ProgressBar color
                            .setBackgroundColor("#212121")      //  Background color
                            .setCameraOnly(false)               //  Camera mode
                            .setMultipleMode(false)              //  Select multiple images or single image
                            .setFolderMode(false)                //  Folder mode
                            .setShowCamera(true)                //  Show camera button
                            .setFolderTitle(getResources().getString(R.string.app_name))           //  Folder title (works with FolderMode = true)
                            .setImageTitle("Select NRC")         //  Image title (works with FolderMode = false)
                            .setDoneTitle("Done")               //  Done button title
                            .setLimitMessage("You have reached selection limit")    // Selection limit message
                            .setMaxSize(1)                     //  Max images can be selected
                            .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                            .setRequestCode(200)                //  Set request code, default Config.RC_PICK_IMAGES
                            .start();
                }
            });
            binding.nrcSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePicker.with(MainActivity.this)                         //  Initialize ImagePicker with activity or fragment context
                            .setToolbarColor("#ff0e0e")         //  Toolbar color
                            .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                            .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                            .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                            .setProgressBarColor("#4CAF50")     //  ProgressBar color
                            .setBackgroundColor("#212121")      //  Background color
                            .setCameraOnly(false)               //  Camera mode
                            .setMultipleMode(false)              //  Select multiple images or single image
                            .setFolderMode(false)                //  Folder mode
                            .setShowCamera(true)                //  Show camera button
                            .setFolderTitle(getResources().getString(R.string.app_name))           //  Folder title (works with FolderMode = true)
                            .setImageTitle("Select NRC")         //  Image title (works with FolderMode = false)
                            .setDoneTitle("Done")               //  Done button title
                            .setLimitMessage("You have reached selection limit")    // Selection limit message
                            .setMaxSize(1)                     //  Max images can be selected
                            .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                            .setRequestCode(201)                //  Set request code, default Config.RC_PICK_IMAGES
                            .start();
                }
            });
            binding.submitNrc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (model != null && nrcfrontimage != null && nrcfrontimageuri != null && !TextUtils.isEmpty(nrcfrontimageuri.toString())) {
                        startActivity(new Intent(context, NRCeditpage.class).putExtra("nrcmodel", gson.toJson(model)).putExtra("NRC_FRONT", nrcfrontimage).putExtra("NRC_FRONT_URI", nrcfrontimageuri.toString()).putExtra("PHONE_NUM", binding.phoneInput.getEditableText().toString()));
                        finish();
                    } else {
                        utility.showToast("Submit All Data");
                    }
                }
            });
            checkPermissions();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            if (images.get(0) != null) {
                try {
                    nrcfrontimageuri = images.get(0).getUri();
                    final InputStream imageStream = getContentResolver().openInputStream(nrcfrontimageuri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    String encodedImage = encodeImage(selectedImage);
                    utility.logger("image base64" + encodedImage);
                    send_nrcfirst(encodedImage);
                    nrcfrontimage = new File(images.get(0).getPath());
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                }
            } else {
                utility.showDialog(context.getResources().getString(R.string.something_went_wrong));
            }
            utility.logger("Image Work");
        } else if (requestCode == 201 && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            if (images.get(0) != null) {
                try {
                    final Uri imageUri = images.get(0).getUri();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    String encodedImage = encodeImage(selectedImage);
                    utility.logger("image base64" + encodedImage);
                    send_nrcsecond(encodedImage);
                    nrcbackimage = new File(images.get(0).getPath());
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                }
            } else {
                utility.showDialog(context.getResources().getString(R.string.something_went_wrong));
            }
            utility.logger("Image Work");
        }

        super.onActivityResult(requestCode, resultCode, data);  // You MUST have this line to be here
        // so ImagePicker can work with fragment mode
    }

    private void send_nrcfirst(String m) {
        try {
            Controller.changeApiBaseUrl("http://ocr-stage.misfit.tech/api/v1/ocr/");
            apiInterface = Controller.getBaseClient().create(ApiService.class);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("image", m);
            hashMap.put("source", "MisfitTech");
            utility.showProgress(false, context.getResources().getString(R.string.wait_string));
            Call<JsonElement> call = apiInterface.Nrc_first("Token eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo0LCJleHAiOjE1OTQxODQxMTl9.anFFYrdAtyzAkGDmzZVYsjVpFNH-g2baV4koFgzsEB0", hashMap);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    utility.hideProgress();
                    try {
                        utility.logger(response.toString());
                        if (response.isSuccessful() && response.code() == 200 && response != null) {
                            utility.logger("nrc first " + response.body().toString());
                            model = gson.fromJson(response.body().toString(), NRCMODEL.class);
                            if (model != null && nrcback) {
                                startActivity(new Intent(context, NRCeditpage.class).putExtra("nrcmodel", gson.toJson(model)));
                                finish();
                            }
                            if (model != null) {
                                Glide.with(context).load(nrcfrontimage).circleCrop().into(binding.nrcFirst);
                                //binding.nrcFirst.setBackground(context.getResources().getDrawable(R.drawable.ic_uploaded));
                            }
                        } else {
                            JSONObject jObj = new JSONObject(response.errorBody().string());
                            JSONObject erobj = new JSONObject(jObj.getString("error"));
                            //utility.logger("result" + gson.toJson(response.body().toString()));
                            utility.showDialog(erobj.getString("message"));
                        }
                    } catch (Exception e) {
                        utility.hideProgress();
                        Log.d("Failed to hit api", Log.getStackTraceString(e));
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    Log.d("On Failure to hit api", t.toString());
                    utility.hideProgress();
                }
            });
        } catch (Exception e) {
            utility.hideProgress();
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    private void send_nrcsecond(String m) {
        try {
            Controller.changeApiBaseUrl("http://ocr-stage.misfit.tech/api/v1/nrc/");
            apiInterface = Controller.getBaseClient().create(ApiService.class);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("image", m);
            hashMap.put("source", "MisfitTech");
            utility.showProgress(false, context.getResources().getString(R.string.wait_string));
            Call<JsonElement> call = apiInterface.Nrc_back("Token eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo0LCJleHAiOjE1OTQxODQxMTl9.anFFYrdAtyzAkGDmzZVYsjVpFNH-g2baV4koFgzsEB0", hashMap);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    utility.hideProgress();
                    try {
                        utility.logger(response.toString());
                        if (response.isSuccessful() && response.code() == 200 && response != null) {
                            utility.logger("nrc back " + response.body().toString());
                            JSONObject jObj = new JSONObject(response.body().toString());
                            nrcback = jObj.getBoolean("success");
                            if (nrcback) {
                                Glide.with(context).load(nrcbackimage).circleCrop().into(binding.nrcSecond);
                                //binding.nrcSecond.setBackground(context.getResources().getDrawable(R.drawable.ic_uploaded));
                                if (model != null) {
                                } else {
                                    binding.nrcSecond.setBackground(context.getResources().getDrawable(R.drawable.ic_upload2));
                                    utility.showDialog("Upload a Valid NRC Front");
                                }
                            } else {
                                utility.showDialog("Not a Valid NRC Back");
                            }
                            //utility.logger("result" + gson.toJson(response.body().toString()));
                        } else {
                            /*JSONObject jObj = new JSONObject(response.errorBody().string());
                            JSONObject erobj = new JSONObject(jObj.getString("error"));
                            //utility.logger("result" + gson.toJson(response.body().toString()));
                            utility.showDialog("result" + erobj.getString("message"));*/
                            utility.showDialog("Not a Valid NRC");
                        }
                    } catch (Exception e) {
                        utility.hideProgress();
                        Log.d("Failed to hit api", Log.getStackTraceString(e));
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    Log.d("On Failure to hit api", t.toString());
                    utility.hideProgress();
                }
            });
        } catch (Exception e) {
            utility.hideProgress();
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }


    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        return;
                    }
                }
                break;
        }
    }

}