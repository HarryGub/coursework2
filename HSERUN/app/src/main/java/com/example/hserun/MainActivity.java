package com.example.hserun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String log;
    ImageView logo_first;
    EditText key;
    Button button;
    private Context context;

    RequestQueue mRequestQueue; // очередь запросов

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo_first = (ImageView) this.findViewById(R.id.logo_first);
        this.logo_first.setImageResource(R.drawable.logo_first);

        key = (EditText) this.findViewById(R.id.key);

        button = (Button) this.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log = key.getText().toString();
                login(getString(R.string.url));
            }
        });
        mRequestQueue = Volley.newRequestQueue(this);
        context = this;
    }

    private void login(String url) {
        JSONObject jsonObjectWithBody = new JSONObject();
        try {
            jsonObjectWithBody.put("key", key.getText());
        } catch (JSONException e) {
            Log.d("Can't make request:", e.toString());
        }
        final String requestBody = jsonObjectWithBody.toString();

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url +"login", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("status").equals("success"))
                    {
                        Intent intent = new Intent(context, Categories.class);
                        intent.putExtra("password", response.getString("password"));
                        startActivity(intent);
                    }
                    else
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Failed to login!", Toast.LENGTH_SHORT);
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
        }){
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("key", log);
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
}
