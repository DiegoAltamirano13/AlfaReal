package com.diego.lina.sistemadealmacenes;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.animation.AnimationUtils;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.TypefaceCompatApi26Impl;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.diego.lina.sistemadealmacenes.Adaptador.ImagenesAdapter;
import com.diego.lina.sistemadealmacenes.ClassCanvas.ClassConection;
import com.diego.lina.sistemadealmacenes.Entidades.Solicitudes_Carga_Descarga;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;

public class Reportes2Fragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    ArrayList<Solicitudes_Carga_Descarga> listaSolicitudes;
    ProgressDialog progress;

    RequestQueue requestQueue;
    private OnFragmentInteractionListener mListener;
    JsonObjectRequest jsonObjectRequest;
    ImagenesAdapter adapter;
    Dialog dialog;
    Typeface typeface;

    ImageButton btn_fec_ini;
    ImageButton btn_fec_fin;
    Button buscar;
    EditText fec_ini;
    EditText fec_fin;

    TextInputLayout layuout_ini;
    TextInputLayout layout_fin;
    String plaza;
    String fechasinicial;
    String fechafinal;
    private  int dia, mes, anio;
    private  int dia2, mes2, anio2;
    //Diego tablas
    TableRow fila;
    TableRow.LayoutParams lp = new TableRow.LayoutParams(500, 500);
    TableLayout tableLayout;

    ImageView img_no_conError;
    TextView textViewError;
    Button buttonError;
    ScrollView horizontalScrollView;
    /**DIEGO ALTAMIRANO SUAREZ DEV**/
    FloatingActionButton floatingActionButton;
    boolean click = false;
    CardView cardViewCD;
    Spinner spinnerPlaza;
    ArrayList<String> plazaArrayCC;
    Button btnBuscar;
    ProgressDialog progressDialog;
    /**06/03/2020**/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_reportes2, container, false);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        plaza = preferences.getString("as_plaza_u", "No estas logueado");
        listaSolicitudes = new ArrayList<>();
        requestQueue= Volley.newRequestQueue(getContext());

        //Fechas
        btn_fec_ini = fragment.findViewById(R.id.btn_ini);
        fec_ini = fragment.findViewById(R.id.edit_ini);
        layuout_ini = fragment.findViewById(R.id.layout_inicio);
        layuout_ini.setHintAnimationEnabled(false);
        fec_ini.setOnFocusChangeListener(null);
        btn_fec_fin = fragment.findViewById(R.id.btn_fin);
        fec_fin = fragment.findViewById(R.id.edit_fin);
        layout_fin = fragment.findViewById(R.id.layout_fin);
        layout_fin.setHintAnimationEnabled(false);
        fec_fin.setOnFocusChangeListener(null);

        //Diego Diseño error
        img_no_conError = fragment.findViewById(R.id.img_error);
        textViewError = fragment.findViewById(R.id.textError);
        buttonError = fragment.findViewById(R.id.btnError);
        buttonError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargaWebService();
            }
        });
        btn_fec_ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_fec_ini){
                    final Calendar calendar = Calendar.getInstance();
                    dia = calendar.get(Calendar.DAY_OF_MONTH);
                    mes = calendar.get(Calendar.MONTH);
                    anio = calendar.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            month = month+1;
                            String fomatoMonth = "" + month;
                            String formatoDia = "" + dayOfMonth;
                            if (month < 10){
                                fomatoMonth = "0"+month;
                            }
                            if (dayOfMonth<10){
                                formatoDia = "0" + dayOfMonth;
                            }
                            fechasinicial = year + "-" + fomatoMonth + "-" + formatoDia;
                            fec_ini.setText(formatoDia +"/" + fomatoMonth +"/" + year);
                            //fec_ini.setText(dayOfMonth +"/" + (month+1) +"/" + year);
                        }
                    }, anio, mes, dia);
                    datePickerDialog.show();
                }
            }
        });

        btn_fec_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_fec_fin){
                    final Calendar calendar = Calendar.getInstance();
                    String recibido;
                    dia2 = calendar.get(Calendar.DAY_OF_MONTH);
                    mes2 = calendar.get(Calendar.MONTH);
                    anio2 = calendar.get(Calendar.YEAR);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            month = month+1;
                            String fomatoMonth = "" + month;
                            String formatoDia = "" + dayOfMonth;
                            if (month+1 < 10){
                                fomatoMonth = "0"+month;
                            }
                            if (dayOfMonth<10){
                                formatoDia = "0" + dayOfMonth;
                            }
                            fechafinal = year + "-" + fomatoMonth + "-" +formatoDia;
                            fec_fin.setText(formatoDia +"/" + fomatoMonth +"/" + year);
                            //fec_fin.setText(dayOfMonth + "/" + (month+1) + "/" + year);

                        }
                    }, anio2, mes2, dia2);
                    datePickerDialog.show();
                }
            }
        });
        //BTON DE BUSQUEDA

        spinnerPlaza = fragment.findViewById(R.id.spinnerPlazaInvCD);
        plazaArrayCC = new ArrayList<>();
        llenar_spinner_plaza();
        cardViewCD = fragment.findViewById(R.id.cardViewHistorico);
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


        buscar = fragment.findViewById(R.id.btn_buscar);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listaSolicitudes.clear();

                ConnectivityManager conn = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = conn.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()) {
                    if (fec_ini.length() == 0 || fec_fin.length() == 0){
                        Toast.makeText(getContext(), "Ingrese Fecha", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        cargaWebService();
                    }
                }
                else{
                    img_no_conError.setVisibility(View.VISIBLE);
                }
            }
        });

        Typeface fuente = Typeface.createFromAsset(getContext().getAssets(),"fonts/SpindleRefined-Regular.otf" );
        ConnectivityManager conn = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {

        }
        else{
            img_no_conError.setVisibility(View.VISIBLE);
        }
        adapter = new ImagenesAdapter(listaSolicitudes, getContext());

        //Diegui
        fila = new TableRow(getActivity());
        fila.setLayoutParams(lp);
        TextView text = new TextView(getContext());

        fila.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_table));
        tableLayout = fragment.findViewById(R.id.tableLayoutPackingList);
        tableLayout.setVisibility(View.INVISIBLE);
        horizontalScrollView = fragment.findViewById(R.id.hscroll);
        horizontalScrollView.setVisibility(View.INVISIBLE);
        //Creacion de encabezados :D DIEGUITO
        String[] encabezado = {"SOLICITUD", "ALMACEN", "LLEGADA", "DESPACHADO", "ESTATUS", "VEHICULO", "PLACAS", "MERCANCIA", "CANTIDAD"};

        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(500, 500);
        layoutParams.setMargins(6,6,6,6);
        fila.setLayoutParams(layoutParams);
        fila.setPadding(5,5,5,5);

        for (int x = 0; x < encabezado.length; x++){
            TextView tv_talla = new TextView(getActivity());
            tv_talla.setText(encabezado[x]);
            tv_talla.setWidth(400);
            tv_talla.setHeight(300);
            tv_talla.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            tv_talla.setBackgroundResource(R.drawable.shape_table);
            fila.addView(tv_talla);
        }
        tableLayout.addView(fila,0);

        return fragment;

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



    private void cargaWebService() {

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

        SharedPreferences preferences = this.getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        String sp_n_cliente = preferences.getString("as_nombre", "No Cliente");
        String sp_plaza  = spinnerPlaza.getSelectedItem().toString();
        String fecha_ini = fec_ini.getText().toString();
        String fecha_fin = fec_fin.getText().toString();
        String url = "http://187.141.70.76/android_app/Reporte_Consulta_Solicitudes_Hist.php?nombrecliente="+sp_n_cliente+"&nombreplaza="+sp_plaza+"&fecha_ini="+fecha_ini+"&fecha_fin="+fecha_fin;
        Log.i("error", url);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println();
        img_no_conError.setVisibility(View.VISIBLE);
        Log.d("Error", "Error no reconocido");
        img_no_conError.setVisibility(View.VISIBLE);
        textViewError.setVisibility(View.VISIBLE);
        buttonError.setVisibility(View.INVISIBLE);
        horizontalScrollView.setVisibility(View.INVISIBLE);
        tableLayout.setVisibility(View.INVISIBLE);
        progressDialog.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("usuario");
        try {

            for (int i = 0 ; i<jsonArray.length();i++){
                JSONObject  jsonObject = null;

                jsonObject = jsonArray.getJSONObject(i);


                TableRow datosCarga = new TableRow(getActivity());
                TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(500, 500);
                datosCarga.setLayoutParams(layoutParams);

                    TextView id_solicitud = new TextView(getActivity());
                    id_solicitud.setText(jsonObject.optString("ID_SOLICITUD"));
                    id_solicitud.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    id_solicitud.setTextColor(Color.WHITE);
                    id_solicitud.setBackgroundResource(R.drawable.shape_black);
                    id_solicitud.setWidth(100);
                    id_solicitud.setHeight(70);
                    datosCarga.addView(id_solicitud);

                    TextView almacen = new TextView(getActivity());
                    almacen.setText(jsonObject.optString("V_NOMBRE"));
                    almacen.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    almacen.setHeight(70);

                    datosCarga.addView(almacen);

                    TextView llegada = new TextView(getActivity());
                    llegada.setText(jsonObject.optString("D_FEC_RECEPCION"));
                    llegada.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    llegada.setHeight(70);
                    datosCarga.addView(llegada);

                    TextView despachado = new TextView(getActivity());
                    despachado.setHeight(200);
                    String despachao = jsonObject.optString("D_FEC_DESP_VEHIC");
                    if (despachao.equals("NO TIENE FECHA")){
                        despachado.setText("");
                    }else {
                        despachado.setText(jsonObject.optString("D_FEC_RECEPCION"));
                    }
                    despachado.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    datosCarga.addView(despachado);

                    TextView status = new TextView(getActivity());
                    status.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    String n_status = jsonObject.optString("N_STATUS");
                    status.setHeight(100);
                    status.setWidth(150);

                if (n_status.equals("1")){
                    status.setText("Registrado");
                    status.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_warning));
                }else if (n_status.equals("2")){
                    status.setText("Llega Vehiculo");
                    status.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_success));
                }else if (n_status.equals("3")){
                    status.setText("Inicio Carga");
                    status.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_success));
                }else if (n_status.equals("4")){
                    status.setText("Carga Finalizada");
                    status.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_success));
                }else if (n_status.equals("5")){
                    status.setText("Despacho Vehiculo");
                    status.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_success));
                }else if (n_status.equals("6")){
                    status.setText("Cancelado");
                    status.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_error));
                }else if (n_status.equals("0")){
                    status.setText("Registrado");
                    status.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_warning));
                }
                datosCarga.addView(status);

                TextView vehiculo = new TextView(getActivity());
                vehiculo.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                vehiculo.setText(jsonObject.optString("V_DESCRIPCION"));
                vehiculo.setHeight(70);
                datosCarga.addView(vehiculo);

                TextView placas = new TextView(getActivity());
                placas.setText(jsonObject.optString("PLACAS"));
                placas.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                placas.setHeight(70);
                datosCarga.addView(placas);

                TextView mercancia = new TextView(getActivity());
                mercancia.setText(jsonObject.optString("V_MERCANCIA"));
                mercancia.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                mercancia.setHeight(70);
                datosCarga.addView(mercancia);

                TextView cantidad = new TextView(getActivity());
                cantidad.setText(jsonObject.optString("N_CANTIDAD_UME"));
                cantidad.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                cantidad.setHeight(70);
                datosCarga.addView(cantidad);
                    tableLayout.addView(datosCarga);
                }


                img_no_conError.setVisibility(View.INVISIBLE);
                textViewError.setVisibility(View.INVISIBLE);
                buttonError.setVisibility(View.INVISIBLE);
                horizontalScrollView.setVisibility(View.VISIBLE);
                tableLayout.setVisibility(View.VISIBLE);
                progressDialog.hide();

        }
        catch (JSONException e) {
            e.printStackTrace();
            progressDialog.hide();
        }
    }


    public interface OnFragmentInteractionListener {
    }
}
