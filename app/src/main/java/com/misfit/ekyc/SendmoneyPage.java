package com.misfit.ekyc;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.misfit.ekyc.databinding.ActivitySendmoneyPageBinding;
import com.misfit.ekyc.http.ApiService;
import com.misfit.ekyc.http.Controller;
import com.misfit.ekyc.utility.FileUtil;
import com.misfit.ekyc.utility.KeyWord;
import com.misfit.ekyc.utility.Utility;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.mingxi.mediapicker.MXMediaPicker;
import tech.mingxi.mediapicker.models.PickerConfig;
import tech.mingxi.mediapicker.models.ResultItem;

public class SendmoneyPage extends AppCompatActivity {
    Context context;
    Utility utility;
    ApiService apiInterface = Controller.getBaseClient().create(ApiService.class);
    Gson gson = new Gson();
    ActivitySendmoneyPageBinding binding;
    String user_idString = "";
    File selfiimage = null;
    Uri selfiimage_uri = null;
    TextView app_tittle;


    MXMediaPicker picker;


    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendmoneyPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            context = this;
            utility = new Utility(context);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_layout);
            app_tittle = (TextView) findViewById(R.id.tvTitle);
            app_tittle.setText("Send Money");
            if (getIntent() != null) {
                user_idString = getIntent().getExtras().getString("USER_ID");
                utility.logger("user send money" + user_idString);
            } else {
                utility.showDialog(context.getResources().getString(R.string.something_went_wrong));
            }
            MXMediaPicker.init(getApplicationContext());
            picker = MXMediaPicker.getInstance();
            PickerConfig pickerConfig = new PickerConfig();
            pickerConfig.setFileType(MXMediaPicker.FILE_TYPE_IMAGE);
            pickerConfig.setAllowCamera(true);
            pickerConfig.setFolderMode(MXMediaPicker.FOLDER_MODE_FULL_PATH);
            pickerConfig.setMultiSelect(false);
            pickerConfig.setMultiSelectMaxCount(1);
            picker.setPickerConfig(pickerConfig);
            click_work();
            checkPermissions();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    private void click_work() {
        try {
            binding.step1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(binding.rcvInput.getEditableText().toString())) {
                        binding.view1.setVisibility(View.GONE);
                        binding.view2.setVisibility(View.VISIBLE);
                        utility.hideKeyboard(view);
                    } else {
                        binding.rcvInput.setError(context.getResources().getString(R.string.phonehint2_string));
                        binding.rcvInput.requestFocus();
                    }
                }
            });
            binding.step2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(binding.amountInput.getEditableText().toString())) {
                        binding.view1.setVisibility(View.GONE);
                        binding.view2.setVisibility(View.GONE);
                        binding.view3.setVisibility(View.VISIBLE);
                        utility.hideKeyboard(view);
                    } else {
                        binding.amountInput.setError(context.getResources().getString(R.string.amount_string));
                        binding.amountInput.requestFocus();
                    }
                }
            });
            binding.sendSelfie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    utility.hideKeyboard(view);
                    if (!TextUtils.isEmpty(binding.rcvInput.getEditableText().toString())) {
                        if (!TextUtils.isEmpty(binding.amountInput.getEditableText().toString())) {
                            /*ImagePicker.with(SendmoneyPage.this)                         //  Initialize ImagePicker with activity or fragment context
                                    .setToolbarColor("#ff0e0e")         //  Toolbar color
                                    .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                                    .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                                    .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                                    .setProgressBarColor("#4CAF50")     //  ProgressBar color
                                    .setBackgroundColor("#212121")      //  Background color
                                    .setCameraOnly(true)               //  Camera mode
                                    .setMultipleMode(false)              //  Select multiple images or single image
                                    .setFolderMode(false)                //  Folder mode
                                    .setShowCamera(false)                //  Show camera button
                                    .setFolderTitle(getResources().getString(R.string.app_name))           //  Folder title (works with FolderMode = true)
                                    .setImageTitle("Select LIVE IMAGE")         //  Image title (works with FolderMode = false)
                                    .setDoneTitle("Done")               //  Done button title
                                    .setLimitMessage("You have reached selection limit")    // Selection limit message
                                    .setMaxSize(1)                     //  Max images can be selected
                                    .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                                    .setRequestCode(201)                //  Set request code, default Config.RC_PICK_IMAGES
                                    .start();*/

                            //ker.chooseImage(SendmoneyPage.this, 101);
                            ImagePicker.with(SendmoneyPage.this)
                                    .cameraOnly()    //User can only capture image using Camera
                                    .start(102);
                        } else {
                            binding.amountInput.setError(context.getResources().getString(R.string.amount_string));
                            binding.amountInput.requestFocus();
                        }

                    } else {
                        binding.rcvInput.setError(context.getResources().getString(R.string.phonehint2_string));
                        binding.rcvInput.requestFocus();
                    }
                }
            });

            binding.sendMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    utility.hideKeyboard(view);
                    if (!TextUtils.isEmpty(binding.rcvInput.getEditableText().toString())) {
                        if (!TextUtils.isEmpty(binding.amountInput.getEditableText().toString())) {
                            if (selfiimage != null) {
                                send_money();
                            } else {
                                utility.showToast("Upload Selfi");
                            }
                        } else {
                            binding.amountInput.setError(context.getResources().getString(R.string.amount_string));
                            binding.amountInput.requestFocus();
                        }

                    } else {
                        binding.rcvInput.setError(context.getResources().getString(R.string.phonehint2_string));
                        binding.rcvInput.requestFocus();
                    }
                }
            });

        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == 201 && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            if (images.get(0) != null) {
                try {
                    //selfiimage = new File(images.get(0).getPath());
                    selfiimage_uri = images.get(0).getUri();
                    selfiimage = new File(FileUtil.getPath(selfiimage_uri, this));
                    //send_money();
                    Glide.with(context).load(selfiimage).circleCrop().into(binding.userImage);
                    //showDialog("You have send " + binding.amountInput.getEditableText().toString() + " Money to " + binding.rcvInput.getEditableText().toString());
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                }
            } else {
                utility.showDialog(context.getResources().getString(R.string.something_went_wrong));
            }
            utility.logger("Image Work");
        }*/

        super.onActivityResult(requestCode, resultCode, data);  // You MUST have this line to be here
        // so ImagePicker can work with fragment mode

        if (requestCode == 101) {
            List<ResultItem> selectedItems = MXMediaPicker.getInstance().getSelectedItems(resultCode, data);
            if (selectedItems != null) {
                for (ResultItem item : selectedItems) {
                    String uri = item.getUri();
                    String path = item.getPath();
                    selfiimage_uri = Uri.parse(uri);
                    selfiimage = new File(FileUtil.getPath(selfiimage_uri, this));
                    Glide.with(context).load(selfiimage).circleCrop().into(binding.userImage);
                    //selfiimage = new File(selfiimage_uri.toString());
                    //send_money();
                    //Please read data from uri. Path is only used to get file name and extension.
                }
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == 102) {
            //Image Uri will not be null for RESULT_OK
            selfiimage_uri = data.getData();
            selfiimage = new File(FileUtil.getPath(selfiimage_uri, this));
            Glide.with(context).load(selfiimage).circleCrop().into(binding.userImage);
            if (selfiimage_uri!=null){
                utility.logger("pc5");
            }else {
                utility.logger("pc6");
            }
            if (selfiimage!=null){
                utility.logger("pc7");
            }else {
                utility.logger("pc8");
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void send_money() {
        try {
            if (selfiimage_uri!=null){
                utility.logger("pc1");
            }else {
                utility.logger("pc2");
            }
            if (selfiimage!=null){
                utility.logger("pc3");
            }else {
                utility.logger("pc4");
            }
            RequestBody requestFile1 = RequestBody.create(selfiimage,MediaType.parse("image/*"));
            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part image_body1 = MultipartBody.Part.createFormData("selfie", selfiimage.getName(), requestFile1);
            RequestBody user_id = createPartFromString(user_idString);
            RequestBody reciever_num = createPartFromString(binding.rcvInput.getEditableText().toString());
            RequestBody amount = createPartFromString(binding.amountInput.getEditableText().toString());
            RequestBody match_percentage = createPartFromString("");

            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("user_id", user_id);
            map.put("reciever_num", reciever_num);
            map.put("amount", amount);
            map.put("match_percentage,", match_percentage);


            Controller.changeApiBaseUrl("http://ocr-dev.misfit.tech/api/v1/transactions/");
            apiInterface = Controller.getBaseClient().create(ApiService.class);
            utility.showProgress(false, context.getResources().getString(R.string.wait_string));
            Call<JsonElement> call = apiInterface.send_money(map, image_body1);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    utility.hideProgress();
                    try {
                        utility.logger(response.toString());
                        if (response.isSuccessful() && response.code() == 201 && response != null) {
                            utility.logger("send money " + response.body().toString());
                            showDialog(binding.rcvInput.getEditableText().toString());
                        } else {
                            //JSONObject jObj = new JSONObject(response.errorBody().string());
                            //JSONObject erobj = new JSONObject(jObj.getString("error"));
                            //utility.logger("result" + gson.toJson(response.body().toString()));

                            JSONObject jObj = new JSONObject(response.errorBody().string());
                            String d = jObj.getString("message");
                            utility.showDialog(d);
                            utility.logger("gen send money " + response.errorBody().string());
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

    public RequestBody createPartFromString(String string) {
        return RequestBody.create(MultipartBody.FORM, string);
    }


    public void showDialog(String message) {
        HashMap<String, Integer> screen = utility.getScreenRes();
        int width = screen.get(KeyWord.SCREEN_WIDTH);
        int height = screen.get(KeyWord.SCREEN_HEIGHT);
        int mywidth = (width / 10) * 9;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_success);
        TextView tvMessage = dialog.findViewById(R.id.txt_info);
        TextView tvMessage2 = dialog.findViewById(R.id.txt_info_tittle);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        tvMessage.setText("Congratulations!");
        tvMessage2.setText("Your mony has been sent. Your new balance is " + message);
        LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.width = mywidth;
        ll.setLayoutParams(params);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setCancelable(false);
        dialog.show();

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