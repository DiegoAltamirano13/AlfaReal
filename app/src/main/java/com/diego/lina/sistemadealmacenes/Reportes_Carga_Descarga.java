package com.diego.lina.sistemadealmacenes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.animation.AnimationUtils;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
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
import com.diego.lina.sistemadealmacenes.Adaptador.SolicitudesAdapter;
import com.diego.lina.sistemadealmacenes.ClassCanvas.ClassConection;
import com.diego.lina.sistemadealmacenes.Entidades.Solicitudes_Carga_Descarga;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Reportes_Carga_Descarga extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    //Controles de fragment
    RecyclerView recyclerView;
    ArrayList<Solicitudes_Carga_Descarga>listaSolicitudes;

    ImageView imageViewError;
    Button buttonError;
    TextView textViewError;

    /*MENU EXPANDIBLE*/
    /*
    * DIEGO ALTAMIRANO SUAREZ DEV 06/03/2020
    * */
    FloatingActionButton floatingActionButton;
    boolean click = false;
    CardView cardViewCD;
    Spinner spinnerPlaza;
    ArrayList<String> plazaArrayCC;
    Button btnBuscar;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View fragment = inflater.inflate(R.layout.reportes_carga_descarga, container, false);
        listaSolicitudes = new ArrayList<>();

        recyclerView = fragment.findViewById(R.id.idReciclerSolicitudes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        imageViewError = fragment.findViewById(R.id.img_error);
        buttonError = fragment.findViewById(R.id.btnError);
        textViewError = fragment.findViewById(R.id.textError);

        spinnerPlaza = fragment.findViewById(R.id.spinnerPlazaInvCD);
        plazaArrayCC = new ArrayList<>();
        llenar_spinner_plaza();
        cardViewCD = fragment.findViewById(R.id.cardViewFiltrosCD);
        CoordinatorLayout.LayoutParams coordinatorLp = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        coordinatorLp.setMargins(25,25,25,25);
        cardViewCD.setLayoutParams(coordinatorLp);
        cardViewCD.setPadding(10,10,10,10);
        cardViewCD.setCardElevation(6);



        floatingActionButton = fragment.findViewById(R.id.expandablebuttonCD);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = !click;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    v.animate().rotation(click ? 45f :0).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).start();
                }
                if (click){
                    CoordinatorLayout.LayoutParams coordinatorLayout = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
                    coordinatorLayout.setMargins(25,25,25,25);
                    cardViewCD.setLayoutParams(coordinatorLayout);
                    cardViewCD.setPadding(10,10,10,10);
                    cardViewCD.setCardElevation(6);
                }
                else {
                    CoordinatorLayout.LayoutParams coordinatorLayout = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, 200);
                    coordinatorLayout.setMargins(25,25,25,25);
                    cardViewCD.setLayoutParams(coordinatorLayout);
                    cardViewCD.setPadding(10,10,10,10);
                    cardViewCD.setCardElevation(6);
                }
            }
        });
        /*AGREGAR FILTRO PLAZA*/
        btnBuscar = fragment.findViewById(R.id.buscarInvFisCD);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarRegistros();
            }
        });

        return fragment;

    }

    private void consultarRegistros(){
        click = !click;
        floatingActionButton.animate().rotation(0).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).start();
        CoordinatorLayout.LayoutParams coordinatorLayout = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, 200);
        coordinatorLayout.setMargins(25,25,25,25);
        cardViewCD.setLayoutParams(coordinatorLayout);
        cardViewCD.setPadding(10,10,10,10);
        cardViewCD.setCardElevation(6);

        /*Progres dialog*/
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Buscando...");
        progressDialog.setMessage("Esto llevara unos segundos");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        SharedPreferences preferences = this.getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        String sp_n_cliente = preferences.getString("as_nombre", "No Cliente");
        String sp_plaza  = spinnerPlaza.getSelectedItem().toString();

        final String url = "http://187.141.70.76/android_app/Reporte_Consulta_Solicitudes.php?nombrecliente="+sp_n_cliente+"&nombreplaza="+sp_plaza;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Solicitudes_Carga_Descarga listaSolicitud = null;

                try {
                    JSONArray responseJsonArray = response.getJSONArray("usuario");
                    StringBuilder formattedResult = new StringBuilder();
                    for (int i = 0; i < responseJsonArray.length(); i++) {
                        listaSolicitud = new Solicitudes_Carga_Descarga();
                        JSONObject jsonObject = null;
                        jsonObject = responseJsonArray.getJSONObject(i);
                        listaSolicitud.setId_solicitud(jsonObject.optString("ID_SOLICITUD"));
                        listaSolicitud.setD_fec_recepcion(jsonObject.optString("D_FEC_RECEPCION"));
                        listaSolicitud.setD_fec_llegada_aprox(jsonObject.optString("D_FEC_LLEGADA_APROX"));
                        listaSolicitud.setD_fec_llegada_real(jsonObject.optString("D_FEC_LLEGADA_REAL"));
                        listaSolicitud.setD_fec_ini_car_des(jsonObject.optString("D_FEC_INI_CAR_DES"));
                        listaSolicitud.setD_fec_fin_car_des(jsonObject.optString("D_FEC_FIN_CAR_DES"));
                        listaSolicitud.setD_fec_desp_vehic(jsonObject.optString("D_FEC_DESP_VEHIC"));
                        listaSolicitud.setN_status(jsonObject.optString("N_STATUS"));
                        listaSolicitud.setV_descripcion(jsonObject.optString("V_DESCRIPCION"));
                        listaSolicitud.setPlacas(jsonObject.optString("PLACAS"));
                        listaSolicitud.setPlacas2(jsonObject.optString("PLACAS_DOS"));
                        listaSolicitud.setV_mercancia(jsonObject.optString("V_MERCANCIA"));
                        listaSolicitud.setEvento(jsonObject.optString("EVENTO"));
                        listaSolicitud.setChofer(jsonObject.optString("CHOFER"));
                        listaSolicitud.setV_nombre(jsonObject.optString("V_NOMBRE"));
                        listaSolicitud.setIid_almacen(jsonObject.optString("IID_ALMACEN"));
                        listaSolicitud.setCantidad_ume(jsonObject.optString("N_CANTIDAD_UME"));
                        listaSolicitud.setNombre_ume(jsonObject.optString("NOMBREUME"));
                        listaSolicitud.setVid_usuario_cliente(jsonObject.optString("VID_USUARIO_CLIENTE"));
                        listaSolicitudes.add(listaSolicitud);
                        //Toast.makeText(getContext(), jsonObject.optString("D_FEC_LLEGADA_APROX"), Toast.LENGTH_LONG).show();

                        imageViewError.setVisibility(View.INVISIBLE);
                        buttonError.setVisibility(View.INVISIBLE);
                        textViewError.setVisibility(View.INVISIBLE);

                        if (progressDialog == null){

                        }else{
                            progressDialog.hide();

                        }

                    }
                    final SolicitudesAdapter solicitudesAdapter = new SolicitudesAdapter(listaSolicitudes, getContext());
                    recyclerView.setAdapter(solicitudesAdapter);
                } catch (JSONException e) {
                    if (progressDialog == null){

                    }else{
                        progressDialog.hide();

                    }
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), "PARCE NO HAY NADA ", Toast.LENGTH_LONG).show();
                if (progressDialog == null){

                }else{
                    progressDialog.hide();

                }
                imageViewError.setVisibility(View.VISIBLE);
                buttonError.setVisibility(View.VISIBLE);
                textViewError.setVisibility(View.VISIBLE);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void llenar_spinner_plaza() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        String nc_cliente;
        String as_usr_nombre = preferences.getString("as_usr_nombre", "N/A");
        String as_password = preferences.getString("as_password", "N/A");
        nc_cliente  = (preferences.getString("as_nombre", "No tiene nombre "));

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ClassConection.URL_WEBB_SERVICES + "plazas_clientes.php?usr_usuario="+as_usr_nombre+"&usr_password="+as_password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String country = jsonObject1.getString("V_RAZON_SOCIAL");
                        plazaArrayCC.add(country);
                    }
                    spinnerPlaza.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, plazaArrayCC));
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

    }

    @Override
    public void onResponse(JSONObject response) {

    }
}
