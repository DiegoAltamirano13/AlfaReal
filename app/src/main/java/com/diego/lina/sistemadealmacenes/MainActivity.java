package com.diego.lina.sistemadealmacenes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.diego.lina.sistemadealmacenes.ClassCanvas.ClassConection;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

import static com.diego.lina.sistemadealmacenes.R.drawable.ic_action_cancel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_usr, et_password;
    Button btn_aceptar;
    private Spinner spinner;
    ArrayList<String>plaza;
    //CircularProgressButton btn_aceptar;
    String usr_usuario = "";
    String usr_password = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        et_usr = findViewById(R.id.usuario);
        et_password = findViewById(R.id.usuario_pwd);
        btn_aceptar = findViewById(R.id.btn_aceptar);
        final Context context = getApplicationContext();

        spinner = findViewById(R.id.spinner_plaza);


        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    String Tamanio_Caracteres = String.valueOf(editable);
                    plaza = new ArrayList<>();
                    listar();
            }
        });

        btn_aceptar.setOnClickListener(this);

        btn_aceptar.setText("Aceptar");
        //btn_aceptar.revertAnimation();

        cargarPreferencias();
    }

    private void listar() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        usr_usuario = et_usr.getText().toString();
        usr_password = et_password.getText().toString();
        //Toast.makeText(getApplicationContext(), usr_usuario+usr_password, Toast.LENGTH_LONG).show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ClassConection.URL_WEBB_SERVICES + "plazas_clientes.php?usr_usuario="+usr_usuario+"&usr_password="+usr_password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String country = jsonObject1.getString("V_RAZON_SOCIAL");
                        plaza.add(country);
                    }
                    spinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, plaza));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        String  as_usr_nombre =  preferences.getString("as_usr_nombre", "No existe");
        if (as_usr_nombre != "No existe"){
            Intent intent = new Intent(MainActivity.this, principal_pagina_menu.class);
            intent.putExtra("as_usr_nombre", as_usr_nombre);
            MainActivity.this.startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        btn_aceptar.setText("Entrando");
        //btn_aceptar.startAnimation();
        final SharedPreferences preferences = getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        usr_usuario = et_usr.getText().toString();
        usr_password = et_password.getText().toString();

        if(usr_password.length() == 0|| usr_usuario.length() == 0){
            btn_aceptar.setText("Error");
        } else {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        Integer as_access = Integer.parseInt(jsonResponse.getString("as_access"));
                        boolean success;
                        if (as_access == 0){
                             success = false;
                        }
                        else{
                            success = true;
                        }
                         // boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            String as_usr_nombre = jsonResponse.getString("as_usr_nombre");
                            String as_password = jsonResponse.getString("as_password");
                            String as_cliente = jsonResponse.getString("as_cliente");
                            String as_nombre = jsonResponse.getString("as_nombre");
                            String as_i_adm = jsonResponse.getString("as_i_adm");
                            String as_plaza = spinner.getSelectedItem().toString();
                            //as_access = jsonResponse.getString("as_access");
                            Intent intent = new Intent(MainActivity.this, principal_pagina_menu.class);
                            intent.putExtra("as_usr_nombre", as_usr_nombre);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("as_usr_nombre", usr_usuario);
                            editor.putString("as_password", as_password);
                            editor.putString("as_cliente", as_cliente );
                            editor.putString("as_nombre", as_nombre );
                            editor.putString("as_i_adm", as_i_adm );
                            editor.putString("as_access", Integer.toString(as_access));
                            editor.putString("as_plaza", as_plaza);
                            editor.commit();
                            MainActivity.this.startActivity(intent);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Error No Se Encuentra Registrado")
                                    .setNegativeButton("Volver a intentar", null)
                                    .create().show();
                            //btn_aceptar.revertAnimation();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };


            LoginReq loginReq = new LoginReq(usr_usuario, usr_password, responseListener);

            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            queue.add(loginReq);

        }
    }
}
