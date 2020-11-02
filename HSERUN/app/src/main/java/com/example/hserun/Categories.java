package com.example.hserun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Categories extends Activity {
    private int mInterval = 5000;
    private Handler mHandler;
    private Context context;
    Button btn2, btn3, btn4, btn5;
    EditText score;
    String password;
    int n;
    Intent intent;
    RequestQueue mRequestQueue; // очередь запросов

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);
        password = getIntent().getStringExtra("password");
        context = this;
        mHandler = new Handler();
        mRequestQueue = Volley.newRequestQueue(this);
        getQuest(context.getString(R.string.url));
        n = 0;
        intent = new Intent(context, Questions.class);
        score = findViewById(R.id.score);
        score.setText(String.valueOf(Counter.score));
        btn2 = (Button) this.findViewById(R.id.c2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n = 0;
                intent.putExtra("password", password);
                showPopupMenu(v);
            }
        });
        btn3 = (Button) this.findViewById(R.id.c3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n = 4;
                intent.putExtra("password", password);
                showPopupMenu(v);
            }
        });
        btn4 = (Button) this.findViewById(R.id.c4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n = 8;
                intent.putExtra("password", password);
                showPopupMenu(v);
            }
        });
        btn5 = (Button) this.findViewById(R.id.c5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n = 12;
                intent.putExtra("password", password);
                showPopupMenu(v);
            }
        });
        startAsking();
    }

    Runnable mScoreChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (!score.getText().equals(String.valueOf(Counter.score)))
                    score.setText(String.valueOf(Counter.score));
            } finally {
                mHandler.postDelayed(mScoreChecker, mInterval);
            }
        }
    };

    void startAsking() {
        mScoreChecker.run();
    }

    private void getQuest(String url) {
        JSONObject jsonObjectWithBody = new JSONObject();
        final String requestBody = jsonObjectWithBody.toString();
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url +"get_tasks", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("data", context.MODE_PRIVATE));
                    outputStreamWriter.write(response.toString());
                    outputStreamWriter.close();
                }
                catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", String.valueOf(error));
            }
        }){
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept","application/json");
                headers.put("Content-Type","application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    Log.d("Error", "UTF-8 encoding error");
                    return null;
                }
            }
        };

        mRequestQueue.add(request); // добавляем запрос в очередь
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.inflate(R.menu.popupmenu);

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                n += 0;
                                intent.putExtra("number", n);
                                startActivity(intent);
                                return true;
                            case R.id.menu2:
                                n += 1;
                                intent.putExtra("number", n);
                                startActivity(intent);
                                return true;
                            case R.id.menu3:
                                n += 2;
                                intent.putExtra("number", n);
                                startActivity(intent);
                                return true;
                            case R.id.menu4:
                                n += 3;
                                intent.putExtra("number", n);
                                startActivity(intent);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
        popupMenu.show();
    }
}
