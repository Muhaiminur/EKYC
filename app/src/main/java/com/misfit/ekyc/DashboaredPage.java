package com.misfit.ekyc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.misfit.ekyc.databinding.ActivityDashboaredPageBinding;
import com.misfit.ekyc.http.ApiService;
import com.misfit.ekyc.http.Controller;
import com.misfit.ekyc.utility.Utility;

public class DashboaredPage extends AppCompatActivity {

    Context context;
    Utility utility;
    ApiService apiInterface = Controller.getBaseClient().create(ApiService.class);
    Gson gson = new Gson();
    ActivityDashboaredPageBinding binding;
    String user_id = "";
    TextView app_tittle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboaredPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            context = this;
            utility = new Utility(context);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_layout);
            app_tittle = (TextView) findViewById(R.id.tvTitle);
            app_tittle.setText("Wave Money");
            if (getIntent() != null) {
                user_id = getIntent().getExtras().getString("USER_ID");
                utility.logger("user id dash" + user_id);
            } else {
                utility.showDialog(context.getResources().getString(R.string.something_went_wrong));
            }
            binding.sendMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(context, SendmoneyPage.class).putExtra("USER_ID", user_id));
                }
            });
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }
}