package com.diego.lina.sistemadealmacenes;

import android.animation.TimeInterpolator;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.util.ArrayList;

public class Certificacion_reporte_desg extends Fragment {
    FloatingActionButton floatingActionButton;
    CardView cardView;
    TextInputEditText n_cliente_cc, extrasIt;

    //Array para spinners folio
    ArrayList<String> folioCC;
    Spinner folioCert;
    // Array para spinner almacen
    ArrayList<String> almacenArrCC;
    Spinner almacenCert;
    //Array para moneda
    ArrayList<String> monedaArray;
    Spinner monedaSpinner;
    //Array Spinner area
    ArrayList<String> areaArray;
    Spinner areaSpinner;
    //Raadio buttons tipo cert
    RadioButton rb_convenio, rb_contrato;
    RadioGroup rg_convenio_contrato;

    RadioGroup rg_todos, rg_N, rg_S;
    RadioGroup rg_Extras;

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

        floatingActionButton = fragment.findViewById(R.id.expandablebutton);

        cardView = fragment.findViewById(R.id.certDesg);

        n_cliente_cc = fragment.findViewById(R.id.nombre_cliente_cc);
        n_cliente_cc.setText(preferences.getString("as_nombre", "No tiene nombre "));
        //Creacion de spinner por folio
        extrasIt = fragment.findViewById(R.id.Afavor_IET);

        folioCC = new ArrayList<>();
        folioCert = fragment.findViewById(R.id.spinner_folio_cc);

        almacenArrCC = new ArrayList<>();
        almacenCert = fragment.findViewById(R.id.spinner_almacen_cc);

        areaArray = new ArrayList<>();
        areaSpinner = fragment.findViewById(R.id.areaSpinner);

        folioCert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                almacenArrCC.clear();
                almacenCert.setAdapter(null);
                areaArray.clear();
                areaSpinner.setAdapter(null);
                llenar_spinner_almacen_cd();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                almacenArrCC.clear();
                almacenCert.setAdapter(null);
            }
        });


        almacenCert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaArray.clear();
                areaSpinner.setAdapter(null);
                llenarSpinnerAreaAlmacen();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        
        monedaArray = new ArrayList<>();
        monedaSpinner = fragment.findViewById(R.id.spinnerMoneda);
        llenarSpinnerMoneda();

        rb_convenio = fragment.findViewById(R.id.rb_convenio);
        rb_contrato = fragment.findViewById(R.id.rb_contrato);

        rg_convenio_contrato = fragment.findViewById(R.id.tipo_certificacion_rb);
        rg_convenio_contrato.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRb = group.findViewById(checkedId);

                boolean isChecked = checkedRb.isChecked();
                if (isChecked){
                    llenarSpinnerFolio();
                }
            }
        });

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

        rg_Extras = fragment.findViewById(R.id.extrasRg);
        rg_Extras.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb_Extras = group.findViewById(checkedId);
                if (rg_Extras.getCheckedRadioButtonId() == -1 ){
                    Log.e("IF ", "NO CHECO NADA");
                }else{
                    boolean isChecked = false;
                    isChecked = rb_Extras.isChecked();
                    if (isChecked){
                        int valor_rg = rg_Extras.getCheckedRadioButtonId();
                        String s_valor_rg = "";
                        if (valor_rg != -1 ){
                            RadioButton radioButton = rg_Extras.findViewById(valor_rg);
                            if (radioButton != null){
                                s_valor_rg = radioButton.getText().toString();
                                Log.e("Valor de rg extras", s_valor_rg);
                                if (s_valor_rg.equals("A favor de")){
                                    ViewGroup.LayoutParams layoutParams = extrasIt.getLayoutParams();
                                    layoutParams.height = 140;
                                    extrasIt.setLayoutParams(layoutParams);
                                }else {
                                    ViewGroup.LayoutParams layoutParams = extrasIt.getLayoutParams();
                                    layoutParams.height = 0;
                                    extrasIt.setLayoutParams(layoutParams);
                                }

                            }
                        }
                    }else {
                        ViewGroup.LayoutParams layoutParams = extrasIt.getLayoutParams();
                        layoutParams.height = 0;
                        extrasIt.setLayoutParams(layoutParams);
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
                    Toast.makeText(getContext(), "True" , Toast.LENGTH_LONG).show();
                    CoordinatorLayout.LayoutParams coordinatorLayout = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
                    coordinatorLayout.setMargins(25,25,25,25);
                    cardView.setLayoutParams(coordinatorLayout);
                    cardView.setPadding(10,10,10,10);
                    cardView.setCardElevation(6);
                }
                else {
                    Toast.makeText(getContext(), "False" , Toast.LENGTH_LONG).show();
                    CoordinatorLayout.LayoutParams coordinatorLayout = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, 200);
                    coordinatorLayout.setMargins(25,25,25,25);
                    cardView.setLayoutParams(coordinatorLayout);
                    cardView.setPadding(10,10,10,10);
                    cardView.setCardElevation(6);
                }

            }
        });
        return fragment;
    }

    private void llenarSpinnerAreaAlmacen() {
        String almacenString =  almacenCert.getSelectedItem().toString();
        Integer posicionInteger = almacenString.indexOf(" ");
        String fff = almacenString.substring(0, posicionInteger);
        Log.e("Valor de lenght almacen", fff);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ClassConection.URL_WEBB_SERVICES + "area_almacen_cc.php?almacen="+fff, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String country = jsonObject1.getString("V_DESCRIPCION");
                        areaArray.add(country);
                    }
                    areaSpinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, areaArray));
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
        String fff =  folioCert.getSelectedItem().toString();
        Log.e("Valor de lenght", String.valueOf(fff.length()));
        String folio_solo =fff.substring(fff.length()-9, fff.length());

        Toast.makeText(getContext(), folio_solo, Toast.LENGTH_LONG).show();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        int valor_rg = rg_convenio_contrato.getCheckedRadioButtonId();


        String s_valor_rg = "";
        if (valor_rg != -1 ){
            RadioButton radioButton = rg_convenio_contrato.findViewById(valor_rg);
            if (radioButton != null){
                s_valor_rg = radioButton.getText().toString();
            }
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ClassConection.URL_WEBB_SERVICES + "almacen_cc.php?folio="+folio_solo+"&tipo="+s_valor_rg, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String country = jsonObject1.getString("ALMACEN_INFO");
                        almacenArrCC.add(country);
                    }
                    almacenCert.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, almacenArrCC));
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

    private void llenarSpinnerFolio() {
            folioCert.setAdapter(null);
            folioCC.clear();
            almacenArrCC.clear();
            almacenCert.setAdapter(null);
            areaArray.clear();
            areaSpinner.setAdapter(null);

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            int valor_rg = rg_convenio_contrato.getCheckedRadioButtonId();
            String s_valor_rg = "";
            if (valor_rg != -1 ){
                RadioButton radioButton = rg_convenio_contrato.findViewById(valor_rg);
                if (radioButton != null){
                    s_valor_rg = radioButton.getText().toString();
                }
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, ClassConection.URL_WEBB_SERVICES + "cc_folio.php?cliente="+n_cliente_cc.getText().toString()+"&tipo="+s_valor_rg, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String country = jsonObject1.getString("NID_FOLIO");
                            folioCC.add(country);
                        }
                        folioCert.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, folioCC));
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
}
