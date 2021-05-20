package com.amir.redoneiv.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amir.redoneiv.Activity.Main.MainActivity;
import com.amir.redoneiv.Common.LoadingDialog;
import com.amir.redoneiv.Common.PreferenceManagerLogin;
import com.amir.redoneiv.R;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.volley.Request.Method.GET;

public class LoginActivity extends AppCompatActivity {

    private static long back_pressed;
    @BindView(R.id.editText_caller_id) EditText editText_caller_id;
    @BindView(R.id.editText_password) EditText editText_password;
    @BindView(R.id.button_login) Button button_login;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        loadingDialog = new LoadingDialog(LoginActivity.this);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setCancelable(false);


        editText_caller_id.setText("018009999");
        editText_password.setText("imredone");

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_caller_id.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter caller id",Toast.LENGTH_SHORT).show();
                }else if(editText_password.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_SHORT).show();
                }else {
                    loadingDialog.show();
                    login();
                }
            }
        });
    }

    private void login(){
        StringRequest stringRequest = new StringRequest(GET, "https://devmobileapp.redone.com.my/mockconsumer/Authentication/access-token",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loadingDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            PreferenceManagerLogin sessionLogin = new PreferenceManagerLogin(getApplicationContext());
                            sessionLogin.createLoginSession(editText_caller_id.getText().toString(),object.getString("result"));

                            Intent next = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(next);
                            LoginActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingDialog.dismiss();
                        parseVolleyError(error);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(
                        "Authorization",
                        String.format("Basic %s", Base64.encodeToString(
                                String.format("%s:%s", editText_caller_id.getText().toString(), editText_password.getText().toString()).getBytes(), Base64.DEFAULT)));
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            Toast.makeText(getApplicationContext(),responseBody,Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException errorr) {
        }
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())  moveTaskToBack(true);
        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
}
