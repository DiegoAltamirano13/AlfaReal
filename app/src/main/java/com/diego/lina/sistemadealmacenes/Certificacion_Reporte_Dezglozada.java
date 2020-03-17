package com.diego.lina.sistemadealmacenes;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.animation.AnimationUtils;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.diego.lina.sistemadealmacenes.Adaptador.CertificacionDesglozadaAdapter;
import com.diego.lina.sistemadealmacenes.ClassCanvas.ClassConection;
import com.diego.lina.sistemadealmacenes.Entidades.CertificacionDesglozada;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Certificacion_Reporte_Dezglozada extends Fragment {
    TextView nombre_cliente_cc;

    Spinner spinner_almacen_cc;
    ArrayList<String> arrayspinner_almacen_cc;

    Spinner spinner_area_cc;
    ArrayList<String> arrayspinner_area_cc;

    ArrayList<String> arrayspinnerMoneda;
    Spinner spinnerMoneda;
    Spinner spinnerRegimen;
    //Para tamaño filtros
    FloatingActionButton floatingActionButton;
    CardView cardView;
    Boolean click = false;
    //Botones de calendario y fechas
    ImageButton btn_ini, btn_fin;
    EditText fecha_inicial, fecha_final;
    String fechasinicial;
    String fechasfinal;
    Button btnAceptar;
    private  int dia, mes, anio;
    private  int dia2, mes2, anio2;

    //Radio grupos
    RadioGroup rg_todos, rg_N, rg_S;

    //bOTON DE BUSCAR
    Button btn_buscar;

    //Barra de progreso
    ProgressDialog progressDialog;
    EditText ncdText;
    ArrayList<CertificacionDesglozada> certificacionDesglozadas;
    RecyclerView recycler;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.certificacion_reporte, container, false);
        //Creacion de visualizacion oculta
        floatingActionButton = fragment.findViewById(R.id.expandablebutton);
        cardView = fragment.findViewById(R.id.certDesg);
        CoordinatorLayout.LayoutParams coordinatorLayout = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, 200);
        coordinatorLayout.setMargins(25,25,25,25);
        cardView.setLayoutParams(coordinatorLayout);
        cardView.setPadding(10,10,10,10);
        cardView.setCardElevation(6);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redimensoniarFormulario();
            }
        });
        //SHARED PREF
        SharedPreferences preferences = this.getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        certificacionDesglozadas = new ArrayList<>();
        //NC
        nombre_cliente_cc = fragment.findViewById(R.id.nombre_cliente_cc);
        nombre_cliente_cc.setText(preferences.getString("as_nombre", "No tiene nombre "));;
        //Spinner Almacen
        spinner_almacen_cc = fragment.findViewById(R.id.spinner_almacen_cc);
        arrayspinner_almacen_cc = new ArrayList<>();
        llenarSpinnerAlmacen();
        //Spinner Area Almacen
        spinner_area_cc = fragment.findViewById(R.id.spinner_area_cc);
        arrayspinner_area_cc = new ArrayList<>();

        //Recycler
        recycler = fragment.findViewById(R.id.idReciclerCertDesglozada);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setHasFixedSize(true);

        spinner_almacen_cc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_area_cc.setAdapter(null);
                arrayspinner_area_cc.clear();
                llenarSpinnerAreaAlmacen();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerMoneda = fragment.findViewById(R.id.spinnerMoneda);
        arrayspinnerMoneda = new ArrayList<>();
        llenarSpinnerMoneda();

        //Botones fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault());
        Date date = new Date();
        String fecha_real = dateFormat.format(date);
        btn_ini = fragment.findViewById(R.id.btn_ini);
        fecha_inicial = fragment.findViewById(R.id.fecha_inicial);
        fecha_inicial.setText(fecha_real);
        btn_ini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_ini){
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
                            fecha_inicial.setText(formatoDia +"/" + fomatoMonth +"/" + year);
                            //fec_ini.setText(dayOfMonth +"/" + (month+1) +"/" + year);
                        }
                    }, anio, mes, dia);
                    datePickerDialog.show();
                }
            }
        });

        btn_fin = fragment.findViewById(R.id.btn_fin);
        fecha_final = fragment.findViewById(R.id.fecha_final);
        fecha_final.setText(fecha_real);
        btn_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_fin){
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
                            fechasfinal = year + "-" + fomatoMonth + "-" + formatoDia;
                            fecha_final.setText(formatoDia +"/" + fomatoMonth +"/" + year);
                            //fec_ini.setText(dayOfMonth +"/" + (month+1) +"/" + year);
                        }
                    }, anio, mes, dia);
                    datePickerDialog.show();
                }
            }
        });


        rg_todos = fragment.findViewById(R.id.rg_Todos);

        ((RadioButton) rg_todos.getChildAt(0)).setChecked(true);

        rg_N = fragment.findViewById(R.id.groupRbN);
        rg_S = fragment.findViewById(R.id.groupRbS);
        //Radio group todos
        rg_todos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedTodosRb = group.findViewById(checkedId);
                if (rg_todos.getCheckedRadioButtonId() == -1 ) {
                    Log.e("No tiene checado nada ", "Nada ");
                }else{

                    boolean isChecked = checkedTodosRb.isChecked();
                    if (isChecked) {
                        if (rg_N.getCheckedRadioButtonId() == -1) {
                            Log.d("N ", "NO TIENE VALOR");
                        } else {
                            rg_N.clearCheck();
                        }

                        if (rg_S.getCheckedRadioButtonId() == -1) {
                            Log.d("S ", "NO TIENE VALOR");
                        } else {
                            rg_S.clearCheck();
                        }
                    }
                }
            }
        });
        // Radio group N
        rg_N.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedTodosRb = group.findViewById(checkedId);
                if (rg_N.getCheckedRadioButtonId() == -1){
                    Log.e("No tiene checado nada N", "Nada en n");
                }else{
                    boolean isChecked = false;
                    isChecked = checkedTodosRb.isChecked();
                    if (isChecked){
                        if (rg_todos.getCheckedRadioButtonId() == -1 ){
                            Log.d("Todos", "NO TIENE VALOR");
                        }else {
                            rg_todos.clearCheck();
                        }

                        if (rg_S.getCheckedRadioButtonId() == -1){
                            Log.d("S ", "NO TIENE VALOR");
                        }else{
                            rg_S.clearCheck();
                        }
                    }
                }
            }
        });
        rg_S.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedTodosRb = group.findViewById(checkedId);
                if (rg_S.getCheckedRadioButtonId() == -1){
                    Log.e("No tiene checado nada N", "Nada en n");
                }else {
                    boolean isChecked = false;
                    isChecked = checkedTodosRb.isChecked();
                    if (isChecked){
                        if (rg_todos.getCheckedRadioButtonId() == -1 ){
                            Log.d("N ", "NO TIENE VALOR");
                        }else {
                            rg_todos.clearCheck();
                        }

                        if (rg_N.getCheckedRadioButtonId() == -1){
                            Log.d("S ", "NO TIENE VALOR");
                        }else{
                            rg_N.clearCheck();
                        }
                    }
                }
            }
        });


        btn_buscar = fragment.findViewById(R.id.btn_aceptar);

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Llamar llenado de tablas
                llenar_Tabla();
            }
        });

        spinnerRegimen = fragment.findViewById(R.id.regimenSpinner);

        ncdText = fragment.findViewById(R.id.textCertificado);
        return fragment;

    }

    private void llenar_Tabla() {
        redimensoniarFormulario();

        //Recuperacion del cliente plaza y area.
        String nombreCliente = nombre_cliente_cc.getText().toString();
        String almacen = spinner_almacen_cc.getSelectedItem().toString();
        //Moneda y regimen
        String moneda = spinnerMoneda.getSelectedItem().toString();
        String regimen = spinnerRegimen.getSelectedItem().toString();
        //Fecha Inicial y Fecha Final
        String primeraFecha = fecha_final.getText().toString();
        String ultimaFecha = fecha_final.getText().toString();
        //Tipo CD
        String textTipoCd = "";
        if (rg_todos.getCheckedRadioButtonId() == -1){
            //Podria poner Log pero esta al 100
        }else{
            int radioButtonsTodosId = rg_todos.getCheckedRadioButtonId();
            View radioButtonTodos = rg_todos.findViewById(radioButtonsTodosId);
            int indiceTodos = rg_todos.indexOfChild(radioButtonTodos);
            RadioButton rbTodos = (RadioButton) rg_todos.getChildAt(indiceTodos);
            textTipoCd = rbTodos.getText().toString();
        }

        if (rg_N.getCheckedRadioButtonId() == -1){

        }else{
            int radioButtonsNId = rg_N.getCheckedRadioButtonId();
            View radioButtonN = rg_N.findViewById(radioButtonsNId);
            int indiceN = rg_N.indexOfChild(radioButtonN);
            RadioButton rbN = (RadioButton) rg_N.getChildAt(indiceN);
            textTipoCd = rbN.getText().toString();
        }

        if (rg_S.getCheckedRadioButtonId() == -1){

        }else {
            int radioButtonsSID = rg_S.getCheckedRadioButtonId();
            View radioButtonS = rg_S.findViewById(radioButtonsSID);
            int indiceS = rg_S.indexOfChild(radioButtonS);
            RadioButton rbS = (RadioButton) rg_S.getChildAt(indiceS);
            textTipoCd = rbS.getText().toString();
        }

        String ncd = ncdText.getText().toString();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Buscando...");
        progressDialog.setMessage("Esto llevara unos segundos");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        //Conexion  A URL
        //plaza
        String plaza = "";
        if (almacen.equals("ALL")){
            plaza = "ALL";
        }else {
            plaza = "NALL";
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        final String query = ClassConection.URL_WEBB_SERVICES+"certificacion_Desglozada.php?cliente="+nombreCliente+"&plaza="+plaza+"&almacen="+almacen+"&moneda="+moneda+"&regimen="+regimen+"&primeraFe="+primeraFecha+"&ultimaFe="+ultimaFecha+"&tipocd="+textTipoCd+"&ncd="+ncd;
        Log.e("URL" , query);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, query, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                CertificacionDesglozada certificacionDesglozada = null;
                certificacionDesglozadas.clear();
                try {
                    JSONArray responseJsonArray = response.getJSONArray("usuario");
                    StringBuilder formattedResult = new StringBuilder();

                    for (int x = 0; x < responseJsonArray.length(); x++) {
                        certificacionDesglozada = new CertificacionDesglozada();
                        JSONObject jsonObject = null;
                        jsonObject = responseJsonArray.getJSONObject(x);
                        certificacionDesglozada.setIID_ALMACEN(Integer.valueOf(jsonObject.getString("IID_ALMACEN")));
                        certificacionDesglozada.setIID_NUM_CLIENTE(Integer.valueOf(jsonObject.getString("IID_NUM_CLIENTE")));
                        certificacionDesglozada.setD_FEC_SAL_CERO(jsonObject.getString("D_FEC_SAL_CERO"));
                        certificacionDesglozada.setI_SAL_CERO(jsonObject.getString("I_SAL_CERO"));
                        certificacionDesglozada.setVID_RECIBO(jsonObject.getString("VID_RECIBO"));
                        certificacionDesglozada.setSALIDA(jsonObject.getString("SALIDA"));
                        certificacionDesglozada.setC_CANTIDAD(jsonObject.getString("C_CANTIDAD"));
                        certificacionDesglozada.setC_PESO_TOTAL(jsonObject.getString("C_PESO_TOTAL"));
                        certificacionDesglozada.setC_VALOR_TOTAL(jsonObject.getString("C_VALOR_TOTAL"));
                        certificacionDesglozada.setD_FECHA_REG(jsonObject.getString("D_FECHA_REG"));
                        certificacionDesglozada.setD_PLAZO_DEP_INI(jsonObject.getString("D_PLAZO_DEP_INI"));
                        certificacionDesglozada.setVID_CERTIFICADO(jsonObject.getString("VID_CERTIFICADO"));
                        certificacionDesglozada.setV_RAZON_SOCIAL(jsonObject.getString("V_RAZON_SOCIAL"));
                        certificacionDesglozada.setV_NOMBRE(jsonObject.getString("V_NOMBRE"));
                        certificacionDesglozada.setS_TIPO_ALMACEN(jsonObject.getString("S_TIPO_ALMACEN"));
                        certificacionDesglozada.setIID_MONEDA(jsonObject.getString("IID_MONEDA"));
                        certificacionDesglozada.setDESCRIPCION(jsonObject.getString("DESCRIPCION"));
                        certificacionDesglozada.setV_DIRECCION(jsonObject.getString("V_DIRECCION"));
                        certificacionDesglozada.setC_TIPO_CAMBIO(jsonObject.getString("C_TIPO_CAMBIO"));
                        certificacionDesglozada.setN_CANT_INI(jsonObject.getString("N_CANT_INI"));
                        certificacionDesglozada.setN_PESO_INI(jsonObject.getString("N_PESO_INI"));
                        certificacionDesglozada.setN_VALOR_INI(jsonObject.getString("N_VALOR_INI"));
                        certificacionDesglozada.setN_CANT_FIN(jsonObject.getString("N_CANT_FIN"));
                        certificacionDesglozada.setN_PESO_FIN(jsonObject.getString("N_PESO_FIN"));
                        certificacionDesglozada.setN_VALOR_FIN(jsonObject.getString("N_VALOR_FIN"));
                        certificacionDesglozada.setN_CIRCULACION(jsonObject.getString("N_CIRCULACION"));
                        certificacionDesglozada.setV_INICIALES(jsonObject.getString("V_INICIALES"));
                        certificacionDesglozada.setV_CVE_SIDEFI(jsonObject.getString("V_CVE_SIDEFI"));
                        certificacionDesglozada.setV_DEPOSITANTE(jsonObject.getString("V_DEPOSITANTE"));
                        certificacionDesglozada.setS_AREA(jsonObject.getString("S_AREA"));
                        certificacionDesglozada.setV_DESCRIPCION(jsonObject.getString("V_DESCRIPCION"));
                        certificacionDesglozada.setREFERENCIA(jsonObject.getString("REFERENCIA"));
                        certificacionDesglozada.setV_RENOVO_A(jsonObject.getString("V_RENOVO_A"));
                        certificacionDesglozada.setCD_AJUSTADO(jsonObject.getString("CD_AJUSTADO"));
                        certificacionDesglozada.setV_DOMICILIO(jsonObject.getString("V_DOMICILIO"));
                        certificacionDesglozada.setTIPO(jsonObject.getString("TIPO"));

                        certificacionDesglozadas.add(certificacionDesglozada);

                        if (progressDialog == null){

                        }else{
                            progressDialog.hide();
                        }
                    }

                  final CertificacionDesglozadaAdapter certificacionDesglozadaAdapter = new CertificacionDesglozadaAdapter(certificacionDesglozadas, getContext());
                    recycler.setAdapter(certificacionDesglozadaAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (progressDialog == null){

                    }else{
                        progressDialog.hide();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR","Error Response"+  error.getMessage());
                if (progressDialog == null){

                }else{
                    progressDialog.hide();
                }
            }
        });

        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        requestQueue.add(jsonObjectRequest);
    }


    private void llenarSpinnerMoneda() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ClassConection.URL_WEBB_SERVICES + "moneda_cc.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String country = jsonObject1.getString("MONEDAS");
                        arrayspinnerMoneda.add(country);
                    }
                    spinnerMoneda.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayspinnerMoneda));
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

    private void redimensoniarFormulario() {
        click = !click;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            floatingActionButton.animate().rotation(click ? 45f :0).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).start();
        }
        if (click){
            CoordinatorLayout.LayoutParams coordinatorLayout = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
            coordinatorLayout.setMargins(25,25,25,25);
            cardView.setLayoutParams(coordinatorLayout);
            cardView.setPadding(10,10,10,10);
            cardView.setCardElevation(6);
        }
        else {
            CoordinatorLayout.LayoutParams coordinatorLayout = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, 200);
            coordinatorLayout.setMargins(25,25,25,25);
            cardView.setLayoutParams(coordinatorLayout);
            cardView.setPadding(10,10,10,10);
            cardView.setCardElevation(6);
        }

    }

    private void llenarSpinnerAreaAlmacen() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        String nc_cliente;
        nc_cliente  = spinner_almacen_cc.getSelectedItem().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Log.e("Valor de almacen URL", ClassConection.URL_WEBB_SERVICES + "area_almacen_cd.php?almacen="+nc_cliente);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ClassConection.URL_WEBB_SERVICES + "area_almacen_cd.php?almacen="+nc_cliente, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                    String country = "ALL";
                    arrayspinner_area_cc.add(country);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        country = jsonObject1.getString("V_DESCRIPCION");
                        arrayspinner_area_cc.add(country);
                    }
                    spinner_area_cc.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayspinner_area_cc));
                } catch (JSONException e) {
                    String country = "ALL";
                    arrayspinner_area_cc.add(country);
                    spinner_area_cc.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayspinner_area_cc));
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String country = "ALL";
                arrayspinner_area_cc.add(country);
                spinner_area_cc.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayspinner_area_cc));
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void llenarSpinnerAlmacen() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        String nc_cliente;
        nc_cliente  = (preferences.getString("as_nombre", "No tiene nombre "));

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ClassConection.URL_WEBB_SERVICES + "almacen_cc_all.php?cliente="+nc_cliente, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                    String country = "ALL";
                    arrayspinner_almacen_cc.add(country);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        country = jsonObject1.getString("V_NOMBRE");
                        arrayspinner_almacen_cc.add(country);
                    }
                    spinner_almacen_cc.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayspinner_almacen_cc));
                    spinner_almacen_cc.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayspinner_almacen_cc));
                } catch (JSONException e) {
                    String country = "ALL";
                    arrayspinner_almacen_cc.add(country);
                    spinner_almacen_cc.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayspinner_almacen_cc));
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String country = "ALL";
                arrayspinner_almacen_cc.add(country);
                spinner_almacen_cc.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, arrayspinner_almacen_cc));
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
}
