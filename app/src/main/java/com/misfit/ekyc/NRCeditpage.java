package com.misfit.ekyc;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.misfit.ekyc.data.NRCMODEL;
import com.misfit.ekyc.databinding.ActivityNrceditpageBinding;
import com.misfit.ekyc.http.ApiService;
import com.misfit.ekyc.http.Controller;
import com.misfit.ekyc.utility.KeyWord;
import com.misfit.ekyc.utility.Utility;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NRCeditpage extends AppCompatActivity {

    Context context;
    Utility utility;
    ApiService apiInterface = Controller.getBaseClient().create(ApiService.class);
    Gson gson = new Gson();
    NRCMODEL model;
    ActivityNrceditpageBinding binding;
    String token = "";
    String[] country = {"Male", "Female"};

    File selfiimage = null;
    File nrcfrontimage = null;
    Uri selfiimage_uri = null;
    Uri nrcfrontimage_uri = null;

    String liveliness = "";
    String phone_num2 = "";
    String user_id = "";

    TextView app_tittle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNrceditpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            context = this;
            utility = new Utility(context);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_layout);
            app_tittle = (TextView) findViewById(R.id.tvTitle);
            app_tittle.setText(R.string.nrcinfo_string);
            if (getIntent() != null) {
                model = gson.fromJson(getIntent().getStringExtra("nrcmodel"), NRCMODEL.class);
                binding.nrcideditInput.setText(model.getData().getNrcId().getEn() + "");
                binding.nrcideditbrInput.setText(model.getData().getNrcId().getMm() + "");
                binding.nameeditInput.setText(model.getData().getName().getEn() + "");
                binding.nameeditbrInput.setText(model.getData().getName().getMm() + "");
                binding.dobeditInput.setText(model.getData().getBirthDate().getEn() + "");
                binding.dobeditbrInput.setText(model.getData().getBirthDate().getMm() + "");
                binding.fateditInput.setText(model.getData().getFathersName().getEn() + "");
                binding.fateditbrInput.setText(model.getData().getFathersName().getMm() + "");
                nrcfrontimage = (File) getIntent().getSerializableExtra("NRC_FRONT");
                //nrcimage_uri = (Uri) getIntent().getSerializableExtra("NRC_FRONT_URI");
                nrcfrontimage_uri = Uri.parse(getIntent().getExtras().getString("NRC_FRONT_URI"));
                phone_num2 = getIntent().getExtras().getString("PHONE_NUM");
                utility.logger("pho" + phone_num2);
            } else {
                utility.showDialog(context.getResources().getString(R.string.something_went_wrong));
            }

            //Creating the ArrayAdapter instance having the country list
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, country);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            binding.geneditInput.setAdapter(aa);

            gen_token();
            binding.submitInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePicker.with(NRCeditpage.this)                         //  Initialize ImagePicker with activity or fragment context
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
                            .setImageTitle("Select LIVE IMAGE")         //  Image title (works with FolderMode = false)
                            .setDoneTitle("Done")               //  Done button title
                            .setLimitMessage("You have reached selection limit")    // Selection limit message
                            .setMaxSize(1)                     //  Max images can be selected
                            .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                            .setRequestCode(201)                //  Set request code, default Config.RC_PICK_IMAGES
                            .start();
                }
            });

            binding.submitSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*ImagePicker.with(NRCeditpage.this)                         //  Initialize ImagePicker with activity or fragment context
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
                            .setImageTitle("Select LIVE IMAGE")         //  Image title (works with FolderMode = false)
                            .setDoneTitle("Done")               //  Done button title
                            .setLimitMessage("You have reached selection limit")    // Selection limit message
                            .setMaxSize(1)                     //  Max images can be selected
                            .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                            .setRequestCode(202)                //  Set request code, default Config.RC_PICK_IMAGES
                            .start();*/

                    //get_matchness();

                    if (selfiimage != null) {
                        send_registration();
                    } else {
                        utility.showToast(context.getResources().getString(R.string.selfi_string));
                    }
                }
            });
            binding.selfieNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.view1.setVisibility(View.GONE);
                    binding.view2.setVisibility(View.VISIBLE);
                }
            });

        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 201 && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            if (images.get(0) != null) {
                try {
                    selfiimage = new File(images.get(0).getPath());
                    selfiimage_uri = images.get(0).getUri();
                    get_liveness(selfiimage, selfiimage_uri);
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                }
            } else {
                utility.showDialog(context.getResources().getString(R.string.something_went_wrong));
            }
            utility.logger("Image Work");
        } else if (requestCode == 202 && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            if (images.get(0) != null) {
                try {
                    nrcfrontimage = new File(images.get(0).getPath());
                    nrcfrontimage_uri = images.get(0).getUri();
                    //get_liveness(new File(images.get(0).getPath()), images.get(0).getUri());
                    get_matchness();
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

    private void gen_token() {
        try {
            Controller.changeApiBaseUrl("http://bitanon.pro:8000/api/");
            apiInterface = Controller.getBaseClient().create(ApiService.class);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("password", "misfit");
            hashMap.put("username", "misfit");
            utility.showProgress(false, context.getResources().getString(R.string.wait_string));
            Call<JsonElement> call = apiInterface.gen_token(hashMap);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    utility.hideProgress();
                    try {
                        utility.logger(response.toString());
                        if (response.isSuccessful() && response.code() == 200 && response != null) {
                            utility.logger("gen token " + response.body().toString());
                            JSONObject jObj = new JSONObject(response.body().toString());
                            JSONObject tokobj = new JSONObject(jObj.getString("data"));
                            token = tokobj.getString("token");
                            utility.logger("token " + token);
                        } else {
                            //JSONObject jObj = new JSONObject(response.errorBody().string());
                            //JSONObject erobj = new JSONObject(jObj.getString("error"));
                            //utility.logger("result" + gson.toJson(response.body().toString()));
                            utility.showDialog("Server Not Found");
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

    private void get_liveness(File f, Uri fileUri) {
        try {

            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), f);
            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part image_body = MultipartBody.Part.createFormData("face", f.getName(), requestFile);
            Controller.changeApiBaseUrl("http://bitanon.pro:8000/api/");
            apiInterface = Controller.getBaseClient().create(ApiService.class);
            utility.showProgress(false, context.getResources().getString(R.string.wait_string));
            Call<JsonElement> call = apiInterface.get_liveness(token, image_body);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    utility.hideProgress();
                    try {
                        utility.logger(response.toString());
                        if (response.isSuccessful() && response.code() == 200 && response != null) {
                            utility.logger("gen liveness " + response.body().toString());
                            JSONObject jObj = new JSONObject(response.body().toString());
                            JSONObject erobj = new JSONObject(jObj.getString("data"));
                            liveliness = erobj.getString("confidence");
                            utility.logger("liveness value" + liveliness);
                            /*binding.submitInfoImage.setBackground(context.getResources().getDrawable(R.drawable.ic_uploaded));
                            binding.submitSecondView.setVisibility(View.VISIBLE);*/
                            int percentage = (int) Math.ceil((1 - Float.parseFloat(liveliness)) * 100);
                            if (percentage == 1) {
                                showmatch("Your Liveliness value " + 100);
                            } else {
                                showmatch("Your Liveliness value " + percentage);
                            }
                            Glide.with(context).load(f).circleCrop().into(binding.userImage);
                        } else {
                            //JSONObject jObj = new JSONObject(response.errorBody().string());
                            //JSONObject erobj = new JSONObject(jObj.getString("error"));
                            //utility.logger("result" + gson.toJson(response.body().toString()));

                            JSONObject jObj = new JSONObject(response.errorBody().string());
                            String d = jObj.getString("message");
                            utility.showDialog(d);
                            utility.logger("gen liveness " + response.errorBody().string());
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

    private void get_matchness() {
        try {
            RequestBody requestFile1 = RequestBody.create(MediaType.parse(getContentResolver().getType(selfiimage_uri)), selfiimage);
            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part image_body1 = MultipartBody.Part.createFormData("face1", selfiimage.getName(), requestFile1);


            RequestBody requestFile2 = RequestBody.create(MediaType.parse(getContentResolver().getType(nrcfrontimage_uri)), nrcfrontimage);
            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part image_body2 = MultipartBody.Part.createFormData("face2", nrcfrontimage.getName(), requestFile2);


            Controller.changeApiBaseUrl("http://bitanon.pro:8000/api/");
            apiInterface = Controller.getBaseClient().create(ApiService.class);


            // create RequestBody instance from file
            /*RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), f);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("picture", f.getName(), requestFile);*/
            utility.showProgress(false, context.getResources().getString(R.string.wait_string));
            Call<JsonElement> call = apiInterface.get_macthness(token, image_body1, image_body2);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    utility.hideProgress();
                    try {
                        utility.logger(response.toString());
                        if (response.isSuccessful() && response.code() == 200 && response != null) {
                            utility.logger("gen liveness " + response.body().toString());
                            JSONObject jObj = new JSONObject(response.body().toString());
                            JSONObject erobj = new JSONObject(jObj.getString("data"));
                            liveliness = erobj.getString("confidence");
                            utility.logger("liveness value" + liveliness);
                            //binding.submitSecondImage.setBackground(context.getResources().getDrawable(R.drawable.ic_uploaded));
                            //int percentage = (int) Math.ceil((1 - Float.parseFloat(liveliness)) * 100);
                            int percentage = (int) Math.ceil(Float.parseFloat(liveliness) * 100);
                            utility.showDialog("Your matchness " + percentage);
                            //showDialog("Thank you. Once we have verified the information, we will upgrade your account.Your matchness " + percentage);
                        } else {
                            //JSONObject jObj = new JSONObject(response.errorBody().string());
                            //JSONObject erobj = new JSONObject(jObj.getString("error"));
                            //utility.logger("result" + gson.toJson(response.body().toString()));

                            JSONObject jObj = new JSONObject(response.errorBody().string());
                            String d = jObj.getString("message");
                            utility.showDialog(d);
                            utility.logger("gen liveness " + response.errorBody().string());
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

    public void showmatch(String message) {
        HashMap<String, Integer> screen = utility.getScreenRes();
        int width = screen.get(KeyWord.SCREEN_WIDTH);
        int height = screen.get(KeyWord.SCREEN_HEIGHT);
        int mywidth = (width / 10) * 9;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_toast);
        TextView tvMessage = dialog.findViewById(R.id.tv_message);
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        tvMessage.setText(message);
        LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.width = mywidth;
        ll.setLayoutParams(params);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                get_matchness();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
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
        Button btnOk = dialog.findViewById(R.id.btn_ok);
        LinearLayout ll = dialog.findViewById(R.id.dialog_layout_size);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.width = mywidth;
        ll.setLayoutParams(params);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(context, DashboaredPage.class).putExtra("USER_ID", user_id));
                finish();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void send_registration() {
        try {
            RequestBody requestFile1 = RequestBody.create(MediaType.parse(getContentResolver().getType(selfiimage_uri)), selfiimage);
            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part image_body1 = MultipartBody.Part.createFormData("selfie", selfiimage.getName(), requestFile1);


            RequestBody requestFile2 = RequestBody.create(MediaType.parse(getContentResolver().getType(nrcfrontimage_uri)), nrcfrontimage);
            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part image_body2 = MultipartBody.Part.createFormData("front_nrc", nrcfrontimage.getName(), requestFile2);

            RequestBody name_en = createPartFromString(binding.nameeditInput.getEditableText().toString());
            RequestBody name_mm = createPartFromString(binding.nameeditbrInput.getEditableText().toString());
            RequestBody father_name_en = createPartFromString(binding.fateditInput.getEditableText().toString());
            RequestBody father_name_mm = createPartFromString(binding.fateditbrInput.getEditableText().toString());
            RequestBody nrc_id_en = createPartFromString(binding.nrcideditInput.getEditableText().toString());
            RequestBody nrc_id_mm = createPartFromString(binding.nrcideditbrInput.getEditableText().toString());
            RequestBody dob_en = createPartFromString(binding.dobeditInput.getEditableText().toString());
            RequestBody dob_mm = createPartFromString(binding.dobeditbrInput.getEditableText().toString());
            RequestBody address_en = createPartFromString("");
            RequestBody address_mm = createPartFromString("");
            RequestBody phone_num = createPartFromString(phone_num2);
            RequestBody pic_match_percentage = createPartFromString(liveliness);

            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("name_en", name_en);
            map.put("name_mm", name_mm);
            map.put("father_name_en", father_name_en);
            map.put("father_name_m,", father_name_mm);
            map.put("nrc_id_en", nrc_id_en);
            map.put("nrc_id_mm", nrc_id_mm);
            map.put("dob_en", dob_en);
            map.put("dob_mm", dob_mm);
            map.put("address_en", address_en);
            map.put("address_mm", address_mm);
            map.put("phone_num", phone_num);
            map.put("pic_match_percentage", pic_match_percentage);


            Controller.changeApiBaseUrl("http://ocr-dev.misfit.tech/api/v1/users/");
            apiInterface = Controller.getBaseClient().create(ApiService.class);


            // create RequestBody instance from file
            /*RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), f);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("picture", f.getName(), requestFile);*/
            utility.showProgress(false, context.getResources().getString(R.string.wait_string));
            Call<JsonElement> call = apiInterface.send_registration(map, image_body1, image_body2);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    utility.hideProgress();
                    try {
                        utility.logger(response.toString());
                        if (response.isSuccessful() && response.code() == 201 && response != null) {
                            utility.logger("send registration " + response.body().toString());
                            JSONObject jObj1 = new JSONObject(response.body().toString());
                            JSONObject jObj2 = new JSONObject(jObj1.getString("data"));
                            JSONObject jObj3 = new JSONObject(jObj2.getString("user_details"));
                            user_id = jObj3.getString("id");
                            utility.logger("userid" + user_id);
                            //binding.submitSecondImage.setBackground(context.getResources().getDrawable(R.drawable.ic_uploaded));
                            showDialog("Thank you. Once we have verified the information, we will upgrade your account");
                        } else {
                            //JSONObject jObj = new JSONObject(response.errorBody().string());
                            //JSONObject erobj = new JSONObject(jObj.getString("error"));
                            //utility.logger("result" + gson.toJson(response.body().toString()));

                            JSONObject jObj = new JSONObject(response.errorBody().string());
                            String d = jObj.getString("message");
                            utility.showDialog(d);
                            utility.logger("gen liveness " + jObj.toString());
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
}