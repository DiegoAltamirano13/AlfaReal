package com.diego.lina.sistemadealmacenes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.animation.AnimationUtils;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.diego.lina.sistemadealmacenes.ClassCanvas.ClassConection;
import com.diego.lina.sistemadealmacenes.Entidades.inventario_fisico;
import com.diego.lina.sistemadealmacenes.Reportes2Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InventarioFisico_Reporte extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    CardView cardView;
    FloatingActionButton floatingActionButtonAbrir;

    Button btnBuscarInvFin;
    boolean click = false;
    private Reportes2Fragment.OnFragmentInteractionListener mListenes;

    ArrayList<String> arrayListPlazasInvFis;
    Spinner spinnerInvFis;
    String usr_password, usr_usuario, usr_plaza;

    EditText editTextNUsr;

    //Creacion de tablas
    TableRow filasEncabezado;
    TableRow.LayoutParams layoutParamsTR = new TableRow.LayoutParams(500, 550);
    TableLayout tableLayoutInvFis;
    JsonObjectRequest jsonObjectRequest;

    //ArrayList InvFis
    ArrayList<inventario_fisico> inventarioFisArray;
    LinearLayout botonesPag;
    TextView fila_Texto;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        View fragmento = inflater.inflate(R.layout.fragment_inventariofisico, container, false);
        floatingActionButtonAbrir = fragmento.findViewById(R.id.expandablebutton);
        cardView = fragmento.findViewById(R.id.cardInvFis);
        CoordinatorLayout.LayoutParams coordinatorLp = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        coordinatorLp.setMargins(25,25,25,25);
        cardView.setLayoutParams(coordinatorLp);
        cardView.setPadding(10,10,10,10);
        cardView.setCardElevation(6);

        arrayListPlazasInvFis = new ArrayList<>();
        spinnerInvFis = fragmento.findViewById(R.id.plaza_InvFis);

        floatingActionButtonAbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = !click;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    v.animate().rotation(click ? 45f :0).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).start();
                }

                if (click){
                    CoordinatorLayout.LayoutParams coordinatorLayout = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
                    coordinatorLayout.setMargins(25,25,25,25);
                    cardView.setLayoutParams(coordinatorLayout);
                    cardView.setPadding(10,10,10,10);
                    cardView.setCardElevation(6);
                }
                else {
                    Toast.makeText(getContext(), "False" , Toast.LENGTH_LONG).show();
                    //cardView.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
                    CoordinatorLayout.LayoutParams coordinatorLayout = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, 200);
                    coordinatorLayout.setMargins(25,25,25,25);
                    cardView.setLayoutParams(coordinatorLayout);
                    cardView.setPadding(10,10,10,10);
                    cardView.setCardElevation(6);
                }
            }
        });

        requestQueue= Volley.newRequestQueue(getContext());
        btnBuscarInvFin = fragmento.findViewById(R.id.buscarInvFis);
        btnBuscarInvFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = !click;
                floatingActionButtonAbrir.animate().rotation(0).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).start();
                CoordinatorLayout.LayoutParams coordinatorLayout = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, 200);
                coordinatorLayout.setMargins(25,25,25,25);
                cardView.setLayoutParams(coordinatorLayout);
                cardView.setPadding(10,10,10,10);
                cardView.setCardElevation(6);
                cargarTablaInvFis();
            }
        });

        editTextNUsr = fragmento.findViewById(R.id.invFisText);
        editTextNUsr.setText(preferences.getString("as_nombre", "No tiene"));
        tableLayoutInvFis = fragmento.findViewById(R.id.tableLayoutPackingList2);
        llenar_spinnerPlazasInvFis();

        inventarioFisArray = new ArrayList<>();
        botonesPag = fragmento.findViewById(R.id.botonesPagination);

        fila_Texto = fragmento.findViewById(R.id.textFila);
        return fragmento;
    }

    private void cargarTablaInvFis() {
        tableLayoutInvFis.removeAllViews();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Consultando...");
        progressDialog.show();

        filasEncabezado = new TableRow(getActivity());
        filasEncabezado.setLayoutParams(layoutParamsTR);
        TextView textViewEncabezado = new TextView(getContext());
        filasEncabezado.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_table));
        String [] encabezado = {"N° DE PARTE", "DESC DE MERCANCIA", "ENTRADA", "SALIDA", "SALDO", "UNIDADES DE MEDIDA"};

        TableLayout.LayoutParams layoutParamsEncabezado = new TableLayout.LayoutParams(500, 550);
        layoutParamsEncabezado.setMargins(6,6,6,6);
        filasEncabezado.setLayoutParams(layoutParamsEncabezado);
        filasEncabezado.setPadding(5,5,5,5);

        for (int x = 0; x < encabezado.length; x++){
            TextView tvEncabezado = new TextView(getActivity());
            tvEncabezado.setText(encabezado[x]);
            tvEncabezado.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
            tvEncabezado.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_table));
            tvEncabezado.setWidth(450);
            tvEncabezado.setHeight(200);
            filasEncabezado.addView(tvEncabezado);
        }
        tableLayoutInvFis.addView(filasEncabezado, 0);

        //LLamado de funcion en php
        SharedPreferences preferences = this.getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        usr_usuario = editTextNUsr.getText().toString();
        usr_plaza = spinnerInvFis.getSelectedItem().toString();

        String url = ClassConection.URL_WEBB_SERVICES + "inv_fis_condensado.php?cliente="+usr_usuario+ "&plaza="+usr_plaza;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);

    }

    private void llenar_spinnerPlazasInvFis() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        SharedPreferences preferences = this.getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        usr_usuario = (preferences.getString("as_usr_nombre", "No tiene nombre "));
        usr_password = (preferences.getString("as_password", "No tiene nombre "));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ClassConection.URL_WEBB_SERVICES + "plazas_clientes.php?usr_usuario="+usr_usuario+"&usr_password="+usr_password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String country = jsonObject1.getString("V_RAZON_SOCIAL");
                        arrayListPlazasInvFis.add(country);
                    }
                    spinnerInvFis.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayListPlazasInvFis));
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

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("usuario");
        inventario_fisico inventarioFis = null;
        inventarioFisArray.clear();
        Log.e("Valor de json", String.valueOf(jsonArray.length()));
        try {
            for (int x=0; x<jsonArray.length(); x++){

                    JSONObject jsonObject = null;
                    jsonObject = jsonArray.getJSONObject(x);
                    inventarioFis = new inventario_fisico();
                    inventarioFis.setNum_parte(jsonObject.optString("VID_NUM_PARTE"));
                    inventarioFis.setDesc_Merca(jsonObject.optString("V_DESCRIPCION"));
                    inventarioFis.setEntrada(jsonObject.optString("ENTRADA"));
                    inventarioFis.setSalida(jsonObject.optString("SALIDA"));
                    inventarioFis.setSaldo(jsonObject.optString("SALDO"));
                    inventarioFis.setUme(jsonObject.optString("DESCRIPCION"));
                    inventarioFisArray.add(inventarioFis);


            }
            int t;
            for (t = 0; t<20; t++){
                TableRow bodyInvFis = new TableRow(getActivity());
                TableLayout.LayoutParams tableBodyLayoutParams = new TableLayout.LayoutParams(500, 500);
                bodyInvFis.setLayoutParams(tableBodyLayoutParams);

                TextView numParte = new TextView(getActivity());
                numParte.setText(inventarioFisArray.get(t).getNum_parte());
                numParte.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                numParte.setTextColor(Color.WHITE);
                numParte.setWidth(100);
                numParte.setHeight(70);
                numParte.setTextColor(Color.BLACK);
                bodyInvFis.addView(numParte);

                TextView descMerca = new TextView(getActivity());
                descMerca.setText(inventarioFisArray.get(t).getDesc_Merca());
                descMerca.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                descMerca.setTextColor(Color.WHITE);
                descMerca.setWidth(100);
                descMerca.setHeight(70);
                descMerca.setTextColor(Color.BLACK);
                bodyInvFis.addView(descMerca);

                TextView entrada = new TextView(getActivity());
                entrada.setText(inventarioFisArray.get(t).getEntrada());
                entrada.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                entrada.setTextColor(Color.WHITE);
                entrada.setWidth(100);
                entrada.setHeight(70);
                entrada.setTextColor(Color.BLACK);
                bodyInvFis.addView(entrada);

                TextView salida = new TextView(getActivity());
                salida.setText(inventarioFisArray.get(t).getSalida());
                salida.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                salida.setTextColor(Color.WHITE);
                salida.setWidth(100);
                salida.setHeight(70);
                salida.setTextColor(Color.BLACK);
                bodyInvFis.addView(salida);

                TextView saldo = new TextView(getActivity());
                saldo.setText(inventarioFisArray.get(t).getSaldo());
                saldo.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                saldo.setTextColor(Color.WHITE);
                saldo.setWidth(100);
                saldo.setHeight(70);
                saldo.setTextColor(Color.BLACK);
                bodyInvFis.addView(saldo);

                TextView ume = new TextView(getActivity());
                ume.setText(inventarioFisArray.get(t).getUme());
                ume.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                ume.setTextColor(Color.WHITE);
                ume.setWidth(100);
                ume.setHeight(70);
                ume.setTextColor(Color.BLACK);
                bodyInvFis.addView(ume);

                tableLayoutInvFis.addView(bodyInvFis);
            }


            double numero_de_filas_entre20;

            numero_de_filas_entre20 = Math.ceil(jsonArray.length()/20.00);
            Log.e("Valor de filas", String.valueOf(Math.ceil(jsonArray.length()/20.00)));

            for (int y = 0; y<numero_de_filas_entre20; y++){

                TableRow btns = new TableRow(getActivity());
                Button botonPaginado = new Button(getActivity());
                botonPaginado.setText(String.valueOf(y+1));
                botonPaginado.setId(y+1);
                botonPaginado.setBackgroundResource(R.drawable.shape_circle_button);
                botonPaginado.setOnClickListener(btnClick);
                btns.addView(botonPaginado);
                botonesPag.addView(btns);
                fila_Texto.setVisibility(View.VISIBLE);
            }

            progressDialog.hide();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    View.OnClickListener btnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            botonesPag.removeAllViews();
            tableLayoutInvFis.removeAllViews();
            Log.e("El boton es", String.valueOf(v.getId()));
            Log.e("Tamaño de array", String.valueOf(inventarioFisArray.size()));
            filasEncabezado = new TableRow(getActivity());
            filasEncabezado.setLayoutParams(layoutParamsTR);
            TextView textViewEncabezado = new TextView(getContext());
            filasEncabezado.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_table));
            String [] encabezado = {"N° DE PARTE", "DESC DE MERCANCIA", "ENTRADA", "SALIDA", "SALDO", "UNIDADES DE MEDIDA"};

            TableLayout.LayoutParams layoutParamsEncabezado = new TableLayout.LayoutParams(500, 550);
            layoutParamsEncabezado.setMargins(6,6,6,6);
            filasEncabezado.setLayoutParams(layoutParamsEncabezado);
            filasEncabezado.setPadding(5,5,5,5);

            for (int x = 0; x < encabezado.length; x++){
                TextView tvEncabezado = new TextView(getActivity());
                tvEncabezado.setText(encabezado[x]);
                tvEncabezado.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                tvEncabezado.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_table));
                tvEncabezado.setWidth(450);
                tvEncabezado.setHeight(200);
                filasEncabezado.addView(tvEncabezado);
            }
            tableLayoutInvFis.addView(filasEncabezado, 0);

            int validarForInferior = 0;
            int validarForSuperior = 0;
            validarForSuperior = (Integer.parseInt(String.valueOf(v.getId())))*20;
            validarForInferior = validarForSuperior-20;

            if (validarForSuperior > inventarioFisArray.size()){
                validarForSuperior = inventarioFisArray.size();
            }
            Log.e("fOR SUPERIOR" , String.valueOf(validarForSuperior)+ " " + String.valueOf(validarForInferior));
            for (validarForInferior = validarForInferior; validarForInferior<validarForSuperior; validarForInferior++){
                TableRow bodyInvFis = new TableRow(getActivity());
                TableLayout.LayoutParams tableBodyLayoutParams = new TableLayout.LayoutParams(500, 500);
                bodyInvFis.setLayoutParams(tableBodyLayoutParams);

                TextView numParte = new TextView(getActivity());
                numParte.setText(inventarioFisArray.get(validarForInferior).getNum_parte());
                numParte.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                numParte.setTextColor(Color.WHITE);
                numParte.setWidth(100);
                numParte.setHeight(70);
                numParte.setTextColor(Color.BLACK);
                bodyInvFis.addView(numParte);

                TextView descMerca = new TextView(getActivity());
                descMerca.setText(inventarioFisArray.get(validarForInferior).getDesc_Merca());
                descMerca.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                descMerca.setTextColor(Color.WHITE);
                descMerca.setWidth(100);
                descMerca.setHeight(70);
                descMerca.setTextColor(Color.BLACK);
                bodyInvFis.addView(descMerca);

                TextView entrada = new TextView(getActivity());
                entrada.setText(inventarioFisArray.get(validarForInferior).getEntrada());
                entrada.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                entrada.setTextColor(Color.WHITE);
                entrada.setWidth(100);
                entrada.setHeight(70);
                entrada.setTextColor(Color.BLACK);
                bodyInvFis.addView(entrada);

                TextView salida = new TextView(getActivity());
                salida.setText(inventarioFisArray.get(validarForInferior).getSalida());
                salida.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                salida.setTextColor(Color.WHITE);
                salida.setWidth(100);
                salida.setHeight(70);
                salida.setTextColor(Color.BLACK);
                bodyInvFis.addView(salida);

                TextView saldo = new TextView(getActivity());
                saldo.setText(inventarioFisArray.get(validarForInferior).getSaldo());
                saldo.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                saldo.setTextColor(Color.WHITE);
                saldo.setWidth(100);
                saldo.setHeight(70);
                saldo.setTextColor(Color.BLACK);
                bodyInvFis.addView(saldo);

                TextView ume = new TextView(getActivity());
                ume.setText(inventarioFisArray.get(validarForInferior).getUme());
                ume.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                ume.setTextColor(Color.WHITE);
                ume.setWidth(100);
                ume.setHeight(70);
                ume.setTextColor(Color.BLACK);
                bodyInvFis.addView(ume);


                tableLayoutInvFis.addView(bodyInvFis);
            }
            double numero_de_filas_entre20;

            numero_de_filas_entre20 = Math.ceil(inventarioFisArray.size()/20.00);
            Log.e("nUMERO ENTRE 20", String.valueOf(numero_de_filas_entre20));
            for (int y = 0; y<numero_de_filas_entre20; y++){

                TableRow btns = new TableRow(getActivity());
                Button botonPaginado = new Button(getActivity());
                botonPaginado.setText(String.valueOf(y+1));
                botonPaginado.setId(y+1);
                botonPaginado.setBackgroundResource(R.drawable.shape_circle_button);
                botonPaginado.setOnClickListener(btnClick);
                btns.addView(botonPaginado);
                botonesPag.addView(btns);
            }
        }
    };

}
