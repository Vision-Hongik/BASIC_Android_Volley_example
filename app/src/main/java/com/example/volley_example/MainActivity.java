package com.example.volley_example;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 로그인시 리스너 객체
        // Response received from the server 서버에서 내용을 받았을때 처리할 내용!
        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    //로그인 성공 여부 변수
                    int success = jsonResponse.getInt("success");
                    //로그인 성공했을때
                    if (success == 1) {
                        String msg = jsonResponse.getString("msg");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Login Succes! "+msg)
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                    //비밀번호가 틀렸을 때
                    else if(success == 2){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Login Failed 비밀번호가 틀립니다.")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                    //아이디가 존재하지 않을때
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Login Failed 아이디가 없습니다.")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                }
                // 서버 접근 에러
                catch (JSONException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Login error")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                    e.printStackTrace();
                }
            }
        };

        setContentView(R.layout.activity_main);

        // activity 객체들
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bSignIn);

        //로그인 버튼!!
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                // EditText에 들어온 ID와 PW로 로그인 시도!
                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(loginRequest);
            }
        });
    }
}
