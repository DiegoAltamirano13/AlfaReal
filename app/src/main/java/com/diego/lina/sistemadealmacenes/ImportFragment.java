package com.diego.lina.sistemadealmacenes;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.StrictMode;

import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.diego.lina.sistemadealmacenes.Actualizador.AutoUpdater;
import com.diego.lina.sistemadealmacenes.Adaptador.ListaImagenesAdapter;
import com.diego.lina.sistemadealmacenes.ClassCanvas.ClassConection;
import com.sun.mail.iap.ByteArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_CANCELED;

public class ImportFragment extends Fragment{
    String plaza_c;
    EditText n_solicitud, nombre_cliente, fecha_reg, estatus, plaza, eventos, lineatrans, placas, placas2, conductor,identificacion , mercancia, cantidad, observaciones;
    Button btn_ini, btn_hora;
    private Spinner spinnerAlmacen, spinner_vehiculo, spinner_marca, spinner_unidad_medida, spinner_fecha;
    FloatingActionButton btn_registro;
    StringRequest stringRequest;
    TextView reg;
    ProgressDialog progress;
    RequestQueue request;

    RadioButton carga, descarga;
    //Actualizador
    private AutoUpdater updater;
    private Context context;

    ArrayList<String>clientes;
    ArrayList<String>marcasVehiculos;
    ArrayList<String>tipoVehiculo;
    ArrayList<String>tipo_mercancia;
    ArrayList<String>hora;
    //variables de url
    String sp_plaza;
    String sp_cliente_num;
    private  int dia, mes, anio;
    String fechasinicial;
    EditText fec_ini;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ACTUALIZACION
        Toast.makeText(getContext(),"Buscando Actualizaciones",Toast.LENGTH_LONG);
        try {
            //comenzarActualizacion()
            Toast.makeText(getContext(),"AVISO",Toast.LENGTH_LONG);
        }catch (Exception ex){
            Toast.makeText(getContext(),ex.getMessage(),Toast.LENGTH_LONG);
        }

        //Shared preferences
        SharedPreferences preferences = this.getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        String usuario = preferences.getString("as_usr_nombre", "No estas logueado");
        plaza_c = preferences.getString("as_plaza_u", "No estas logueado");

        //Asignar variables con interfaz
        View fragmento =  inflater.inflate(R.layout.fragment_import, container, false);
        reg = fragmento.findViewById(R.id.reg);
        //Campos de registro
        n_solicitud = fragmento.findViewById(R.id.n_solicitud);
        nombre_cliente = fragmento.findViewById(R.id.nombre_cliente);
        fecha_reg = fragmento.findViewById(R.id.fecha_reg);
        estatus = fragmento.findViewById(R.id.estatus);
        plaza = fragmento.findViewById(R.id.plaza);
        //eventos = fragmento.findViewById(R.id.eventos);
        lineatrans = fragmento.findViewById(R.id.lineatrans);
        placas = fragmento.findViewById(R.id.placas);
        placas2 = fragmento.findViewById(R.id.placas2);
        conductor = fragmento.findViewById(R.id.conductor);
        identificacion = fragmento.findViewById(R.id.identificacion);
        mercancia = fragmento.findViewById(R.id.mercancia);
        cantidad = fragmento.findViewById(R.id.cantidad);
        observaciones = fragmento.findViewById(R.id.observaciones);
        fec_ini = fragmento.findViewById(R.id.fecha_inicial);
        //Botones de envio
        btn_registro = fragmento.findViewById(R.id.registrar);

        //Volley Library
        request = Volley.newRequestQueue(getContext());

        clientes = new ArrayList<>();
        marcasVehiculos = new ArrayList<>();
        tipoVehiculo = new ArrayList<>();
        tipo_mercancia = new ArrayList<>();
        hora = new ArrayList<>();
        spinnerAlmacen = fragmento.findViewById(R.id.spinner_almacen);
        spinner_marca = fragmento.findViewById(R.id.spinner_marca);
        spinner_vehiculo = fragmento.findViewById(R.id.spinner_vehiculo);
        spinner_unidad_medida = fragmento.findViewById(R.id.spinner_unidad_medida);
        spinner_fecha = fragmento.findViewById(R.id.spinner_fecha);
        carga = fragmento.findViewById(R.id.carga);
        descarga = fragmento.findViewById(R.id.descarga);
        listar();
        listar_marcaV();
        listar_TipoV();
        listar_TipoM();

        String sp_n_cliente = preferences.getString("as_nombre", "No Cliente");
        nombre_cliente.setText(sp_n_cliente);
        estatus.setText("Preregistro");
        sp_plaza = preferences.getString("as_plaza", "No tienes plaza ");
        plaza.setText(sp_plaza);
        sp_cliente_num = preferences.getString("as_cliente", "No Tiene Cliente");
        //En caso de dar click sobre el boton de registro
        btn_registro.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    validarCampos();
                }
            });

        btn_ini= fragmento.findViewById(R.id.btn_ini);

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
                            String fecha_datapicker = (formatoDia +"/" + fomatoMonth +"/" + year);
                            fec_ini.setText(formatoDia +"/" + fomatoMonth +"/" + year);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            Date date = new Date();
                            String fecha_comparativa = dateFormat.format(date);
                            if (fecha_comparativa.equals(fecha_datapicker) ){
                                Toast.makeText(getContext(), "No hay disponibilidad de citas en el almacen", Toast.LENGTH_LONG).show();
                            }

                        }
                    }, anio, mes, dia);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                    datePickerDialog.show();
                }
            }
        });

        fec_ini.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mostrar_Horas_Spinner();
            }
        });

        return fragmento;
    }

    private void mostrar_Horas_Spinner() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String sp_almacen;
        sp_almacen = "";
        sp_almacen = spinnerAlmacen.getSelectedItem().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Date date = new Date();
        String fecha_comparativa = dateFormat.format(date);
        String fec_inis;
        fec_inis = "";
        fec_inis = fec_ini.getText().toString();
        String tipo_mvto;
        tipo_mvto = "";
        if(carga.isChecked())
            tipo_mvto = "1";
        if (descarga.isChecked())
            tipo_mvto = "2";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ClassConection.URL_WEBB_SERVICES + "horas_disponibles_spinnes.php?almacen="+sp_almacen+"&tipo="+tipo_mvto+"&fecha_actual="+fecha_comparativa+"&fecha_cita="+fec_inis, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String country = jsonObject1.getString("HORA_DIA");
                        hora.add(country);
                    }
                    spinner_fecha.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, hora));
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

    private void listar_TipoM() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ClassConection.URL_WEBB_SERVICES + "tipo_mercancia_cliente.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String country = jsonObject1.getString("V_DESCRIPCION");
                        tipo_mercancia.add(country);
                    }
                    spinner_unidad_medida.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, tipo_mercancia));
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

    private void listar_TipoV() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ClassConection.URL_WEBB_SERVICES + "vehiculo_tipo_cliente.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String country = jsonObject1.getString("V_DESCRIPCION");
                        tipoVehiculo.add(country);
                    }
                    spinner_vehiculo.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, tipoVehiculo));
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

    private void listar_marcaV() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ClassConection.URL_WEBB_SERVICES + "vehiculo_marca_cliente.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String country = jsonObject1.getString("V_DESCRIPCION");
                        marcasVehiculos.add(country);
                    }
                    spinner_marca.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, marcasVehiculos));
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

    public void listar() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        SharedPreferences preferences = this.getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        sp_plaza = preferences.getString("as_plaza", "No tienes plaza ");
        sp_cliente_num =preferences.getString("as_cliente", "No Tiene Cliente");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ClassConection.URL_WEBB_SERVICES + "almacen_cliente.php?plaza="+sp_plaza+"&cliente="+sp_cliente_num, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("usuario");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String country = jsonObject1.getString("V_NOMBRE");
                        clientes.add(country);
                    }
                    spinnerAlmacen.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, clientes));
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

    private void validarCampos() {
        String nombre_clientes = nombre_cliente.getText().toString();
        String estatuss = estatus.getText().toString();
        String plazas = plaza.getText().toString();
        String lineatranss = lineatrans.getText().toString();
        String placass = placas.getText().toString();
        String placas2s= placas2.getText().toString();
        String conductors= conductor.getText().toString();
        String identificacions = identificacion.getText().toString();
        String mercancias= mercancia.getText().toString();
        String cantidads = cantidad.getText().toString();
        String observacioness = observaciones.getText().toString();

       // plazas = plazas.replace("(", "");
       // plazas = plazas.replace(")", "");
        String sp_almacen;
        sp_almacen = "";
        sp_almacen = spinnerAlmacen.getSelectedItem().toString();

        String sp_vehiculo;
        sp_vehiculo = "";
        sp_vehiculo = spinner_vehiculo.getSelectedItem().toString();

        String sp_marca;
        sp_marca = "";
        sp_marca = spinner_marca.getSelectedItem().toString();

        String sp_unidad_medida;
        sp_unidad_medida = "";
        sp_unidad_medida = spinner_unidad_medida.getSelectedItem().toString();

        String tipo_mvto;
        tipo_mvto = "";
        if(carga.isChecked())
            tipo_mvto = "1";
        if (descarga.isChecked())
            tipo_mvto = "2";

        boolean b = validar_desc_merca(nombre_clientes);
        boolean d = validar_desc_merca(estatuss);
        boolean e = validar_desc_merca(plazas);
        boolean g = validar_desc_merca(lineatranss);
        boolean h = validar_desc_merca(placass);
        boolean i = validar_desc_merca(placas2s);
        boolean j = validar_desc_merca(conductors);
        boolean k = validar_desc_merca(identificacions);
        boolean l = validar_desc_merca(mercancias);
        boolean m = validar_desc_merca(cantidads);
        boolean o = validar_desc_merca(sp_almacen);
        boolean q = validar_desc_merca(sp_vehiculo);
        boolean r = validar_desc_merca(sp_marca);
        boolean s = validar_desc_merca(sp_unidad_medida);
        boolean t = validar_desc_merca(tipo_mvto);

        if ( b && d && e && g && h && i && j && k && l && m && o && q && r && s && t){
            cargarService();
        }
        else {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
            builder.setMessage("Es necesario llenar todos los campos").setNegativeButton("Aceptar", null)
                    .create().show();
        }
    }

    private boolean validar_desc_merca(String desc_mercas) {
        String desc_mercas2 = desc_mercas;
        Pattern patron = Pattern.compile("^[a-zA-ZÁáÀàÉéÈèÍíÌìÓóÒòÚúÙùÑñüÜ00-9ñN !@#\\$%\\^&\\*\\?_~\\(\\)\\.\\/]+$");
        if (!patron.matcher(desc_mercas2).matches() || desc_mercas2.length()>160 || desc_mercas2.length() == 0) {

            return false;
        }
        else {

        }
        return true;
    }

    static String importFrag;
    public static ImportFragment entrar (String importFrag) {
        importFrag = importFrag;
        return new ImportFragment();
    }
    //Guardar instancia
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

    }

    private void cargarService() {
        final String nombre_clientes = nombre_cliente.getText().toString();
        final String estatuss = estatus.getText().toString();
        final String plazas = plaza.getText().toString();
        final String lineatranss = lineatrans.getText().toString();
        final String placass = placas.getText().toString();
        final String placas2s= placas2.getText().toString();
        final String conductors= conductor.getText().toString();
        final String identificacions = identificacion.getText().toString();
        final String mercancias= mercancia.getText().toString();
        final String cantidads = cantidad.getText().toString();

        if (nombre_clientes.length() == 0  || estatuss.length() == 0 || plazas.length() == 0  || lineatranss.length() == 0 || placass.length() == 0  || conductors.length() == 0 || identificacions.length() == 0  || mercancias.length() == 0  || cantidads.length() == 0){
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
            builder.setMessage("Es necesario llenar todos los campos").setNegativeButton("Aceptar", null)
                    .create().show();
        }
        else {
            progress = new ProgressDialog(getContext());
            progress.setMessage("Procesando registro...");
            progress.show();
            //final String url = "http://sistemasdecontrolderiego.esy.es/Registro_Mercancia.php";
            final String url = "http://187.141.70.76/android_app/registro_solicitud.php";

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //envioCorreo();
                    if (response.trim().equalsIgnoreCase("registra")) {
                        Toast.makeText(getContext(), "REGISTRO EXITOSO", Toast.LENGTH_SHORT).show();
                        //progress.hide();
                        limpiarTodo();
                    } else {
                        Toast.makeText(getContext(), "NO SE LLEVO ACABO EL REGISTRO", Toast.LENGTH_SHORT).show();
                        progress.hide();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.hide();
                    Toast.makeText(getContext(), n_solicitud.getText().toString() + "  " + nombre_cliente.getText().toString(), Toast.LENGTH_LONG).show();

                    Toast.makeText(getContext(), "Conexion incorrecta" + error, Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    SharedPreferences preferences = getActivity().getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
                     String nombreplaza = plaza.getText().toString();
                     String nombrealmacen = spinnerAlmacen.getSelectedItem().toString();
                     String nombrecliente = nombre_cliente.getText().toString();
                        String tipo;
                        tipo = "";
                        if(carga.isChecked())
                            tipo = "1";
                        if (descarga.isChecked())
                            tipo = "2";
                     String tipovehiculo = spinner_vehiculo.getSelectedItem().toString();
                     String marcavehiculo = spinner_marca.getSelectedItem().toString();
                     String placass = placas.getText().toString();
                     String chofer= conductor.getText().toString();
                     String id_ch = identificacion.getText().toString();
                     String cantidads = cantidad.getText().toString();
                     String mercancias= mercancia.getText().toString();
                     String fecha_ctl = fec_ini.getText().toString();
                     String hora_ctl = spinner_fecha.getSelectedItem().toString();
                     String observacion = observaciones.getText().toString();
                     String transportista = lineatrans.getText().toString();
                     String placas2T = placas2.getText().toString();
                     String nombreume = spinner_unidad_medida.getSelectedItem().toString();

                     String usuario = "";
                     usuario = preferences.getString("as_cliente", "No cliente");
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("nombreplaza", nombreplaza);
                    parametros.put("nombrealmacen", nombrealmacen);
                    parametros.put("nombrecliente", nombrecliente);
                    parametros.put("tipo", tipo);
                    parametros.put("tipovehiculo", tipovehiculo);
                    parametros.put("marcavehiculo", marcavehiculo);
                    parametros.put("placas", placass);
                    parametros.put("chofer", chofer);
                    parametros.put("id_ch", id_ch);
                    parametros.put("cantidad", cantidads);
                    parametros.put("mercancia", mercancias);
                    parametros.put("fecha_clt", fecha_ctl);
                    parametros.put("hora_clt", hora_ctl);
                    parametros.put("observacion", observacion);
                    parametros.put("transportistas", transportista);
                    parametros.put("placas2", placas2T);
                    parametros.put("usuario", usuario);
                    parametros.put("nombreume", nombreume);

                    return parametros;
                }
            };
            request.add(stringRequest);


        }
    }

    private void limpiarTodo() {
        carga.setChecked(false);
        descarga.setChecked(false);
        fec_ini.setText("");
        placas.setText("");
        placas2.setText("");
        conductor.setText("");
        identificacion.setText("");
        mercancia.setText("");
        cantidad.setText("");
        observaciones.setText("");
        lineatrans.setText("");
        progress.hide();
    }

}