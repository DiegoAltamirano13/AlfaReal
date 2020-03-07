package com.diego.lina.sistemadealmacenes;

import android.animation.TimeInterpolator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Interpolator;
import android.os.Build;
import android.os.Bundle;
import android.support.design.animation.AnimationUtils;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.diego.lina.sistemadealmacenes.Adaptador.CertDesglozadaAdapter;
import com.diego.lina.sistemadealmacenes.ClassCanvas.ClassConection;
import com.diego.lina.sistemadealmacenes.Entidades.CertificacionNormal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class Certificacion_reporte_desg extends Fragment {
    /*Recycler*/
    RecyclerView recycler;
    /* DIEGO ALTAMIRANO SUAREZ */
    FloatingActionButton floatingActionButton;
    CardView cardView;
    TextInputEditText n_cliente_cc;
    //array  para spinner Plaza
    ArrayList<String> plazaArrayCC;
    Spinner plazaCert;
    // Array para spinner almacen
    ArrayList<String> almacenArrCC;
    Spinner almacenCert;
    //Array para moneda
    ArrayList<String> monedaArray;
    Spinner monedaSpinner;
    //Array Spinner area
    Spinner regimenSpinner;
    //Raadio buttons tipo cert

    RadioGroup rg_todos, rg_N, rg_S;
    RadioGroup rg_Extras;

    ImageButton btn_fec_ini;
    ImageButton btn_fec_fin;
    EditText fecha_1, fecha2;
    String fechasinicial;
    String fechafinal;
    Button btnAceptar;
    private  int dia, mes, anio;
    private  int dia2, mes2, anio2;

    ProgressDialog progressDialog ;

    TextView id_almacen ,nombre_almacen ,tipo_cert;
    ArrayList<CertificacionNormal> certificacionNormalArrayList;
    CardView cardView2;
    boolean click = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.certificacion_desglozada_reporte, container, false);
        //preferencias guardadas
        SharedPreferences preferences = this.getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        n_cliente_cc = fragment.findViewById(R.id.nombre_cliente_cc);
        n_cliente_cc.setText(preferences.getString("as_nombre", "No tiene nombre "));

        /*creacion de recycler view*/
        recycler = fragment.findViewById(R.id.idReciclerCertDesglozada);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setHasFixedSize(true);
        /*Diego altamirano Suarez */
        floatingActionButton = fragment.findViewById(R.id.expandablebutton);

        cardView = fragment.findViewById(R.id.certDesg);

        n_cliente_cc = fragment.findViewById(R.id.nombre_cliente_cc);
        n_cliente_cc.setText(preferences.getString("as_nombre", "No tiene nombre "));
        //Creacion de spinner por folio

        plazaArrayCC = new ArrayList<>();
        plazaCert = fragment.findViewById(R.id.spinner_plaza_cc);

        almacenArrCC = new ArrayList<>();
        almacenCert = fragment.findViewById(R.id.spinner_almacen_cc);
        certificacionNormalArrayList = new ArrayList<>();
        //Llenado de spinner de plaza
        llenar_spinner_plaza();

        plazaCert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                almacenArrCC.clear();
                almacenCert.setAdapter(null);
                llenar_spinner_almacen_cd();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        monedaArray = new ArrayList<>();
        monedaSpinner = fragment.findViewById(R.id.spinnerMoneda);
        llenarSpinnerMoneda();



        rg_todos = fragment.findViewById(R.id.rg_Todos);
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
        regimenSpinner = fragment.findViewById(R.id.regimenSpinner);
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
        });
        btn_fec_ini = fragment.findViewById(R.id.btn_ini);
        fecha_1 = fragment.findViewById(R.id.fecha_inicial);
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
                            fecha_1.setText(formatoDia +"/" + fomatoMonth +"/" + year);
                            //fec_ini.setText(dayOfMonth +"/" + (month+1) +"/" + year);
                        }
                    }, anio, mes, dia);
                    datePickerDialog.show();
                }
            }
        });
        btn_fec_fin = fragment.findViewById(R.id.btn_fin);
        fecha2 = fragment.findViewById(R.id.fecha_final);
        btn_fec_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_fec_fin){
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
                            fechafinal = year + "-" + fomatoMonth + "-" + formatoDia;
                            fecha2.setText(formatoDia +"/" + fomatoMonth +"/" + year);
                            //fec_ini.setText(dayOfMonth +"/" + (month+1) +"/" + year);
                        }
                    }, anio, mes, dia);
                    datePickerDialog.show();
                }
            }
        });
        btnAceptar = fragment.findViewById(R.id.btn_aceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mostrarRegistros();
            }
        });
        return fragment;
    }

    private void mostrarRegistros() {
        click = !click;
        floatingActionButton.animate().rotation(0).setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR).start();
        CoordinatorLayout.LayoutParams coordinatorLayout = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, 200);
        coordinatorLayout.setMargins(25,25,25,25);
        cardView.setLayoutParams(coordinatorLayout);
        cardView.setPadding(10,10,10,10);
        cardView.setCardElevation(6);

        //Recuperar el valor del almacen Y LA PLAZA
        String nombreCliente = n_cliente_cc.getText().toString();
        String plaza = plazaCert.getSelectedItem().toString();
        String almacen = almacenCert.getSelectedItem().toString();

        //Recuperar el valor de rg tipo convenio / contrato
        String textTipo = "";

        //Recupera el valor de moneda y regimen
        String moneda = monedaSpinner.getSelectedItem().toString();
        String regimen = regimenSpinner.getSelectedItem().toString();

        //Recuperar fecha inicialñ y final
        String primeraFecha = fecha_1.getText().toString();
        String ultimaFecha = fecha2.getText().toString();

        String textTipoCD = "";
        //Validar Radio buttons
        if (rg_todos.getCheckedRadioButtonId() == -1 ){
            Log.e("Nada en todos", "Nada en todos");
        } else{
            int radioButtonsTodosId = rg_todos.getCheckedRadioButtonId();
            View radioButtonTodos = rg_todos.findViewById(radioButtonsTodosId);
            int indiceTodos = rg_todos.indexOfChild(radioButtonTodos);
            RadioButton rbTodos = (RadioButton) rg_todos.getChildAt(indiceTodos);
            textTipoCD = rbTodos.getText().toString();
        }

        if (rg_N.getCheckedRadioButtonId() == -1){
            Log.e("No hay nada en N", "Nada en N");
        }else {
            int radioButtonsNid = rg_N.getCheckedRadioButtonId();
            View radioButtonsN = rg_N.findViewById(radioButtonsNid);
            int indiceN = rg_N.indexOfChild(radioButtonsN);
            RadioButton rbN = (RadioButton) rg_N.getChildAt(indiceN);
            textTipoCD = rbN.getText().toString();

        }
        if (rg_S.getCheckedRadioButtonId() == -1){
            Log.e("No hay nada en S", "Nada en S");
        }else {
            int radioButtonsSid = rg_S.getCheckedRadioButtonId();
            View radioButtonsS = rg_S.findViewById(radioButtonsSid);
            int indiceS = rg_S.indexOfChild(radioButtonsS);
            RadioButton rbS = (RadioButton) rg_S.getChildAt(indiceS);
            textTipoCD = rbS.getText().toString();
        }


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Buscando...");
        progressDialog.setMessage("Esto llevara unos segundos");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        final String query = "http://187.141.70.76/android_app/cert_Desglozada.php?cliente=" + nombreCliente + "&plaza=" + plaza + "&almacen=" + almacen + "&moneda=" + moneda + "&regimen=" + regimen + "&primeraFe=" + primeraFecha + "&ultimaFe=" + ultimaFecha + "&tipocd=" + textTipoCD;
        Log.e("URL",  query);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, query, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                CertificacionNormal certificacionNormal = null;
                certificacionNormalArrayList.clear();
                try {
                    JSONArray responseJsonArray  = response.getJSONArray("usuario");
                    StringBuilder formattedResult = new StringBuilder();

                    for (int x = 0; x<responseJsonArray.length(); x++){
                        certificacionNormal = new CertificacionNormal();
                        JSONObject jsonObject1 =  null;
                        jsonObject1 = responseJsonArray.getJSONObject(x);
                        certificacionNormal.setIID_ALMACEN(Integer.valueOf(jsonObject1.getString("IID_ALMACEN")));
                        certificacionNormal.setIID_NUM_CLIENTE(Integer.valueOf(jsonObject1.getString("IID_NUM_CLIENTE")));
                        certificacionNormal.setN_CANT_VALOR(jsonObject1.getString("N_CANT_VALOR"));
                        certificacionNormal.setN_CANT_BULTOS(jsonObject1.getString("N_CANT_BULTOS"));
                        certificacionNormal.setV_UME(jsonObject1.getString("V_UME"));
                        certificacionNormal.setN_CANT_UMC(jsonObject1.getString("N_CANT_UMC"));
                        certificacionNormal.setV_UMC(jsonObject1.getString("V_UMC"));
                        certificacionNormal.setN_CANT_TONS(jsonObject1.getString("N_CANT_TONS"));
                        certificacionNormal.setV_RAZON_SOCIAL(jsonObject1.getString("V_RAZON_SOCIAL"));
                        certificacionNormal.setV_DIRECCION(jsonObject1.getString("V_DIRECCION"));
                        certificacionNormal.setSIMBOLO(jsonObject1.getString("SIMBOLO"));
                        certificacionNormal.setS_TIPO_ALMACEN(jsonObject1.getString("S_TIPO_ALMACEN"));
                        certificacionNormal.setC_TIPO_CAMBIO(jsonObject1.getString("C_TIPO_CAMBIO"));
                        certificacionNormal.setIID_MONEDA(jsonObject1.getString("IID_MONEDA"));
                        certificacionNormal.setIID_NUM_CERT_N(jsonObject1.getString("IID_NUM_CERT_N"));
                        certificacionNormalArrayList.add(certificacionNormal);
                        if (progressDialog == null){

                        }else{
                            progressDialog.hide();

                        }
                    }
                    final CertDesglozadaAdapter certDesglozadaAdapter = new CertDesglozadaAdapter(certificacionNormalArrayList, getContext());
                    recycler.setAdapter(certDesglozadaAdapter);

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
                    String country;
                    country = "ALL";
                    plazaArrayCC.add(country);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        country = jsonObject1.getString("V_RAZON_SOCIAL");
                        plazaArrayCC.add(country);
                    }
                    plazaCert.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, plazaArrayCC));
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
                        monedaArray.add(country);
                    }
                    monedaSpinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, monedaArray));
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

    private void llenar_spinner_almacen_cd() {

        SharedPreferences preferences = this.getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        String nc_cliente, plaza;
        nc_cliente  = (preferences.getString("as_nombre", "No tiene nombre "));
        plaza = plazaCert.getSelectedItem().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ClassConection.URL_WEBB_SERVICES + "almacen_cc.php?cliente="+nc_cliente+"&plaza="+plaza, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                    String country = "ALL";
                    almacenArrCC.add(country);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        country = jsonObject1.getString("V_NOMBRE");
                        almacenArrCC.add(country);
                    }
                    almacenCert.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, almacenArrCC));
                    almacenCert.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, almacenArrCC));
                } catch (JSONException e) {
                    String country = "ALL";
                    almacenArrCC.add(country);
                    almacenCert.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, almacenArrCC));
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String country = "ALL";
                almacenArrCC.add(country);
                almacenCert.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, almacenArrCC));
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
}
