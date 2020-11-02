package com.example.hserun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Questions extends Activity {
    Context context;
    int qnumber;
    String password;
    Question task;

    Button send;
    ImageButton hint;
    EditText ans, score;
    TextView question;
    ImageView imgq;

    RequestQueue mRequestQueue; // очередь запросов


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions);

        qnumber = getIntent().getIntExtra("number", -1);
        password = getIntent().getStringExtra("password");
        String json = "";
        mRequestQueue = Volley.newRequestQueue(this);

        send = findViewById(R.id.send);
        if (Counter.solved.contains(qnumber)) {
            send.setEnabled(false);
            send.setText("DONE");
            send.setBackgroundColor(Color.rgb(0, 255, 0));
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send(context.getString(R.string.url));
            }
        });
        hint = findViewById(R.id.hint);
        ans = findViewById(R.id.ans);
        question = findViewById(R.id.question);
        imgq = findViewById(R.id.imgq);
        score = findViewById(R.id.score);
        score.setText(String.valueOf(Counter.score));

        try {
            InputStream inputStream = context.openFileInput("data");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                json = stringBuilder.toString();
                Log.d("read", json);
                HashMap<String, Object> q;
                Map<String, Object> result =
                        new ObjectMapper().readValue(json, HashMap.class);
                try {
                    q = (HashMap<String, Object>) result.get(String.valueOf(qnumber));
                    task = new Question((int) q.get("number"), (String) q.get("category"), (String) q.get("question"), (int) q.get("type"));
//                    if (task.type == 1) {
//                        JSONObject jsonObjectWithBody = new JSONObject();
//                        try {
//                            jsonObjectWithBody.put("n", qnumber);
//                        } catch (JSONException e) {
//                            Log.d("Can't make request:", e.toString());
//                        }
//                        final String requestBody = jsonObjectWithBody.toString();
//                        final StringRequest request = new StringRequest(Request.Method.PUT, context.getString(R.string.url) + "get_res",
//                                new Response.Listener<String>() {
//                                    @Override
//                                    public void onResponse(String response) {
//                                        Log.d("hui", response);
//                                        byte[] bits = response.getBytes();
//                                        Bitmap bmp= BitmapFactory.decodeByteArray(bits, 0, bits.length);
//                                        try (FileOutputStream out = new FileOutputStream(qnumber + ".png")) {
//                                            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
////                                        Picasso.with(context).load(String.valueOf(bmp)).into(imgq);
//                                        Log.d("hui", "ura");
//                                    }
//                                }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.d("Error.Response", String.valueOf(error));
//                            }
//                        }){
//                            @Override
//                            public Map<String, String> getHeaders() throws AuthFailureError {
//                                HashMap<String, String> headers = new HashMap<String, String>();
//                                headers.put("Accept", "application/json");
//                                headers.put("Content-Type", "application/json");
//                                return headers;
//                            }
//
//                            @Override
//                            public byte[] getBody() {
//                                return requestBody.getBytes();
//                            }
//                        };
//
//                        mRequestQueue.add(request); // добавляем запрос в очередь
//                    }
                } catch (Exception e) {
                    Log.e("EREWREWREWR", e.toString());
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("read json", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("read json", "Can not read file: " + e.toString());
        }

        if (task.type == 0) {
            question.setText(task.question);
        } else {
            switch (qnumber)
            {
                case 0: imgq.setForeground(ContextCompat.getDrawable(context, R.drawable.p0)); break;
                case 1: imgq.setForeground(ContextCompat.getDrawable(context, R.drawable.p1)); break;
                case 2: imgq.setForeground(ContextCompat.getDrawable(context, R.drawable.p2)); break;
                case 5: imgq.setForeground(ContextCompat.getDrawable(context, R.drawable.p5)); break;
                case 6: imgq.setForeground(ContextCompat.getDrawable(context, R.drawable.p6)); break;
                case 7: imgq.setForeground(ContextCompat.getDrawable(context, R.drawable.p7)); break;
                case 10: imgq.setForeground(ContextCompat.getDrawable(context, R.drawable.p10)); break;
                case 11: imgq.setForeground(ContextCompat.getDrawable(context, R.drawable.p11)); break;
                case 13: imgq.setForeground(ContextCompat.getDrawable(context, R.drawable.p13)); break;
                case 14: imgq.setForeground(ContextCompat.getDrawable(context, R.drawable.p14)); break;
                case 15: imgq.setForeground(ContextCompat.getDrawable(context, R.drawable.p15)); break;
            }
        }

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Counter.hints.get(qnumber) == null)
                    Hint(context.getString(R.string.url));
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(Counter.hints.get(qnumber));
                    builder.create();
                    builder.show();
                }
            }
        });
    }

    public void Send(String url) {
        JSONObject jsonObjectWithBody = new JSONObject();
        try {
            jsonObjectWithBody.put("number", qnumber);
            jsonObjectWithBody.put("answer", ans.getText());
            jsonObjectWithBody.put("password", password);
        } catch (JSONException e) {
            Log.d("Can't make request:", e.toString());
        }
        final String requestBody = jsonObjectWithBody.toString();

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url + "send_answer", null, new Response.Listener<JSONObject>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Log.d("Response", String.valueOf(response.get("status")));
                        Counter.solved.add(qnumber);
                        send.setEnabled(false);
                        send.setText("DONE");
                        send.setBackgroundColor(Color.rgb(0, 255, 0));
                        ans.setFocusable(false);
                        Counter.score += 10;
                        score.setText(String.valueOf(Counter.score));
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Wrong answer!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", String.valueOf(error));
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("number", String.valueOf(qnumber));
                params.put("answer", ans.getText().toString());
                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
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

    public void Hint(String url) {
        JSONObject jsonObjectWithBody = new JSONObject();
        try {
            jsonObjectWithBody.put("number", qnumber);
            jsonObjectWithBody.put("password", password);
        } catch (JSONException e) {
            Log.d("Can't make request:", e.toString());
        }
        final String requestBody = jsonObjectWithBody.toString();

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url + "get_hint", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Log.d("Response", String.valueOf(response.get("status")));
                        final String hint = response.getString("hint");
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage("Подсказка стоит 5 очков! Вы хотите воспользоваться ей?");
                        builder1.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage(hint);
                                builder.create();
                                builder.show();
                                Counter.score-=5;
                                score.setText(String.valueOf(Counter.score));
                                Counter.hints.put(qnumber, hint);
                            }
                        });
                        builder1.setNegativeButton("Нет", null);
                        builder1.create();
                        builder1.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Not enough points!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", String.valueOf(error));
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("number", String.valueOf(qnumber));
                params.put("answer", ans.getText().toString());
                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
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
}