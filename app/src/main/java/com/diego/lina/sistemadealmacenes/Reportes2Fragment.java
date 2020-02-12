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
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.diego.lina.sistemadealmacenes.Adaptador.ImagenesAdapter;
import com.diego.lina.sistemadealmacenes.Adaptador.ListaImagenesAdapter;
import com.diego.lina.sistemadealmacenes.Adaptador.ListaImagenesAdapterConsulta;
import com.diego.lina.sistemadealmacenes.Adaptador.ListaImagenesAdapterConsultaMensaje;
import com.diego.lina.sistemadealmacenes.Adaptador.MercanciasAdapter;
import com.diego.lina.sistemadealmacenes.Entidades.Solicitudes_Carga_Descarga;
import com.diego.lina.sistemadealmacenes.Entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Reportes2Fragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    RecyclerView recyclerMercancias;
    ArrayList<Solicitudes_Carga_Descarga> listaSolicitudes;
    ProgressDialog progress;

    RequestQueue requestQueue;
    ImageView img_no_con;
    private OnFragmentInteractionListener mListener;
    JsonObjectRequest jsonObjectRequest;
    ImagenesAdapter adapter;
    Dialog dialog;
    Typeface typeface;

    Button btn_fec_ini;
    Button btn_fec_fin;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_reportes2, container, false);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        plaza = preferences.getString("as_plaza_u", "No estas logueado");
        img_no_con = fragment.findViewById(R.id.no_conect);
        listaSolicitudes = new ArrayList<>();
        //recyclerMercancias = fragment.findViewById(R.id.idReciclerMercancia);
        //recyclerMercancias.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //recyclerMercancias.setHasFixedSize(true);
        img_no_con.setVisibility(View.INVISIBLE);
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
        buscar = fragment.findViewById(R.id.btn_buscar);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listaSolicitudes.clear();

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
                //recyclerMercancias.setLayoutManager(gridLayoutManager);

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
                    img_no_con.setVisibility(View.VISIBLE);
                }
            }
        });

        Typeface fuente = Typeface.createFromAsset(getContext().getAssets(),"fonts/SpindleRefined-Regular.otf" );
        ConnectivityManager conn = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {

        }
        else{
            img_no_con.setVisibility(View.VISIBLE);
        }
        adapter = new ImagenesAdapter(listaSolicitudes, getContext());

        //Diegui
        fila = new TableRow(getActivity());
        fila.setLayoutParams(lp);
        TextView text = new TextView(getContext());

        fila.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_table));
        tableLayout = fragment.findViewById(R.id.tableLayoutPackingList);

        //Creacion de encabezados :D DIEGUITO
        String[] encabezado = {"SOLICITUD", "ALMACEN", "LLEGADA", "DESPACHADO", "ESTATUS", "VEHICULO", "PLACAS", "MERCANCIA", "CANTIDAD"};

        for (int x = 0; x < encabezado.length; x++){
            TextView tv_talla = new TextView(getActivity());
            tv_talla.setText(encabezado[x]);
            tv_talla.setWidth(300);
            tv_talla.setHeight(200);
            tv_talla.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            fila.addView(tv_talla);
        }
        tableLayout.addView(fila,0);
        return fragment;

    }

    private void cargaWebService() {
        progress = new ProgressDialog(getContext());
        progress.setMessage("Consulta");
        progress.show();

        SharedPreferences preferences = this.getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        String sp_n_cliente = preferences.getString("as_nombre", "No Cliente");
        String sp_plaza  = preferences.getString("as_plaza", "No tienes plaza ");
        String fecha_ini = fec_ini.getText().toString();
        String fecha_fin = fec_fin.getText().toString();
        String url = "http://187.141.70.76/android_app/Reporte_Consulta_Solicitudes_Hist.php?nombrecliente="+sp_n_cliente+"&nombreplaza="+sp_plaza+"&fecha_ini="+fecha_ini+"&fecha_fin="+fecha_fin;
        Log.i("error", url);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Sin registros", Toast.LENGTH_SHORT).show();
        System.out.println();
        img_no_con.setImageResource(R.drawable.search);
        img_no_con.setVisibility(View.VISIBLE);
        Log.d("Error", "Error no reconocido");
        progress.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        Solicitudes_Carga_Descarga solicitudCD = null;
        JSONArray jsonArray = response.optJSONArray("usuario");
        try {

            for (int i = 0 ; i<jsonArray.length();i++){
                solicitudCD = new Solicitudes_Carga_Descarga();
                JSONObject  jsonObject = null;

                    jsonObject = jsonArray.getJSONObject(i);
                solicitudCD.setId_solicitud(jsonObject.optString("ID_SOLICITUD"));
                solicitudCD.setD_fec_recepcion(jsonObject.optString("D_FEC_RECEPCION"));
                solicitudCD.setD_fec_llegada_aprox(jsonObject.optString("D_FEC_LLEGADA_APROX"));
                solicitudCD.setD_fec_llegada_real(jsonObject.optString("D_FEC_LLEGADA_REAL"));
                solicitudCD.setD_fec_ini_car_des(jsonObject.optString("D_FEC_INI_CAR_DES"));
                solicitudCD.setD_fec_fin_car_des(jsonObject.optString("D_FEC_FIN_CAR_DES"));
                solicitudCD.setD_fec_desp_vehic(jsonObject.optString("D_FEC_DESP_VEHIC"));
                solicitudCD.setN_status(jsonObject.optString("N_STATUS"));
                solicitudCD.setV_descripcion(jsonObject.optString("V_DESCRIPCION"));
                solicitudCD.setPlacas(jsonObject.optString("PLACAS"));
                solicitudCD.setPlacas2(jsonObject.optString("PLACAS_DOS"));
                solicitudCD.setV_mercancia(jsonObject.optString("V_MERCANCIA"));
                solicitudCD.setEvento(jsonObject.optString("EVENTO"));
                solicitudCD.setChofer(jsonObject.optString("CHOFER"));
                solicitudCD.setV_nombre(jsonObject.optString("V_NOMBRE"));
                solicitudCD.setIid_almacen(jsonObject.optString("IID_ALMACEN"));
                solicitudCD.setCantidad_ume(jsonObject.optString("N_CANTIDAD_UME"));
                solicitudCD.setNombre_ume(jsonObject.optString("NOMBREUME"));
                solicitudCD.setVid_usuario_cliente(jsonObject.optString("VID_USUARIO_CLIENTE"));

                    listaSolicitudes.add(solicitudCD);

                    TableRow datosCarga = new TableRow(getActivity());
                    datosCarga.setLayoutParams(lp);


                    TextView id_solicitud = new TextView(getActivity());
                    id_solicitud.setText(jsonObject.optString("ID_SOLICITUD"));
                    id_solicitud.setHeight(100);
                    id_solicitud.setWidth(150);
                    id_solicitud.setBackgroundResource(R.drawable.shape_black);

                    id_solicitud.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    datosCarga.addView(id_solicitud);

                    TextView almacen = new TextView(getActivity());
                    almacen.setText(jsonObject.optString("V_NOMBRE"));
                    almacen.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    almacen.setHeight(200);
                    datosCarga.addView(almacen);

                    TextView llegada = new TextView(getActivity());
                    llegada.setText(jsonObject.optString("D_FEC_RECEPCION"));
                    llegada.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    llegada.setHeight(200);
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
                vehiculo.setHeight(200);
                datosCarga.addView(vehiculo);

                TextView placas = new TextView(getActivity());
                placas.setText(jsonObject.optString("PLACAS"));
                placas.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                placas.setHeight(200);
                datosCarga.addView(placas);

                TextView mercancia = new TextView(getActivity());
                mercancia.setText(jsonObject.optString("V_MERCANCIA"));
                mercancia.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                mercancia.setHeight(200);
                datosCarga.addView(mercancia);

                TextView cantidad = new TextView(getActivity());
                cantidad.setText(jsonObject.optString("N_CANTIDAD_UME"));
                cantidad.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                cantidad.setHeight(200);
                datosCarga.addView(cantidad);
                    tableLayout.addView(datosCarga);
                }

                progress.hide();
            //final ImagenesAdapter mercanciasAdapter = new ImagenesAdapter(listaSolicitudes, getContext());
            //recyclerMercancias.setAdapter(mercanciasAdapter);

        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No Hay Registros", Toast.LENGTH_SHORT).show();

            progress.hide();
        }
    }


    public interface OnFragmentInteractionListener {
    }
}
