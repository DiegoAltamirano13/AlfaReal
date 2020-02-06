package com.diego.lina.sistemadealmacenes;


import android.app.AlertDialog;
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
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
    private Spinner spinnerAlmacen, spinner_vehiculo, spinner_marca, spinner_unidad_medida;
    FloatingActionButton btn_registro;
    StringRequest stringRequest;
    TextView reg;
    //finalizar
    //Detalles de conexion y carga
    ProgressDialog progress;
    RequestQueue request;

    //Actualizador
    private AutoUpdater updater;
    private Context context;

    ArrayList<String>clientes;
    ArrayList<String>marcasVehiculos;
    ArrayList<String>tipoVehiculo;
    //variables de url
    String sp_plaza;
    String sp_cliente_num;

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
        eventos = fragmento.findViewById(R.id.eventos);
        lineatrans = fragmento.findViewById(R.id.lineatrans);
        placas = fragmento.findViewById(R.id.placas);
        placas2 = fragmento.findViewById(R.id.placas2);
        conductor = fragmento.findViewById(R.id.conductor);
        identificacion = fragmento.findViewById(R.id.identificacion);
        mercancia = fragmento.findViewById(R.id.mercancia);
        cantidad = fragmento.findViewById(R.id.cantidad);
        observaciones = fragmento.findViewById(R.id.observaciones);
        //Botones de envio
        btn_registro = fragmento.findViewById(R.id.registrar);

        //Volley Library
        request = Volley.newRequestQueue(getContext());

        clientes = new ArrayList<>();
        marcasVehiculos = new ArrayList<>();
        tipoVehiculo = new ArrayList<>();
        spinnerAlmacen = fragmento.findViewById(R.id.spinner_almacen);
        spinner_marca = fragmento.findViewById(R.id.spinner_marca);
        spinner_vehiculo = fragmento.findViewById(R.id.spinner_vehiculo);
        listar();
        listar_marcaV();
        listar_TipoV();

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




        return fragmento;
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
        String n_solicituds = n_solicitud.getText().toString();
        String nombre_clientes = nombre_cliente.getText().toString();
        String fecha_regs = fecha_reg.getText().toString();
        String estatuss = estatus.getText().toString();
        String plazas = plaza.getText().toString();
        String eventoss= eventos.getText().toString();
        String lineatranss = lineatrans.getText().toString();
        String placass = placas.getText().toString();
        String placas2s= placas2.getText().toString();
        String conductors= conductor.getText().toString();
        String identificacions = identificacion.getText().toString();
        String mercancias= mercancia.getText().toString();
        String cantidads = cantidad.getText().toString();
        String observacioness = observaciones.getText().toString();

        boolean a = validar_desc_merca(n_solicituds);
        boolean b = validar_desc_merca(nombre_clientes);
        boolean c = validar_desc_merca(fecha_regs);
        boolean d = validar_desc_merca(estatuss);
        boolean e = validar_desc_merca(plazas);
        boolean f = validar_desc_merca(eventoss);
        boolean g = validar_desc_merca(lineatranss);
        boolean h = validar_desc_merca(placass);
        boolean i = validar_desc_merca(placas2s);
        boolean j = validar_desc_merca(conductors);
        boolean k = validar_desc_merca(identificacions);
        boolean l = validar_desc_merca(mercancias);
        boolean m = validar_desc_merca(cantidads);
        boolean n = validar_desc_merca(observacioness);

        if (a && b && c && d && e && f && g && h && i && j && k && l && m && n){
            cargarService();
        }
    }

    private boolean validar_desc_merca(String desc_mercas) {
        String desc_merca = desc_mercas;
        Pattern patron = Pattern.compile("^[a-zA-ZÁáÀàÉéÈèÍíÌìÓóÒòÚúÙùÑñüÜ00-9ñN !@#\\$%\\^&\\*\\?_~\\/]+$");
        if (!patron.matcher(desc_mercas).matches() || desc_mercas.length()>160) {
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
        final String n_solicituds = n_solicitud.getText().toString();
        final String nombre_clientes = nombre_cliente.getText().toString();
        final String fecha_regs = fecha_reg.getText().toString();
        final String estatuss = estatus.getText().toString();
        final String plazas = plaza.getText().toString();
        final String eventoss= eventos.getText().toString();
        final String lineatranss = lineatrans.getText().toString();
        final String placass = placas.getText().toString();
        final String placas2s= placas2.getText().toString();
        final String conductors= conductor.getText().toString();
        final String identificacions = identificacion.getText().toString();
        final String mercancias= mercancia.getText().toString();
        final String cantidads = cantidad.getText().toString();
        final String observacioness = observaciones.getText().toString();

        if (n_solicituds.length() == 0 || nombre_clientes.length() == 0 || fecha_regs.length() == 0  || estatuss.length() == 0 || plazas.length() == 0 || eventoss.length() == 0 || lineatranss.length() == 0 || placass.length() == 0  || conductors.length() == 0 || identificacions.length() == 0  || mercancias.length() == 0  || cantidads.length() == 0){
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
            builder.setMessage("Es necesario llenar todos los campos").setNegativeButton("Aceptar", null)
                    .create().show();
        }
        else {
            progress = new ProgressDialog(getContext());
            progress.setMessage("Procesando registro...");
            progress.show();
            final String url = "http://sistemasdecontrolderiego.esy.es/Registro_Mercancia.php";

            stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //envioCorreo();
                    if (response.trim().equalsIgnoreCase("registra")) {

                    } else {
                        Toast.makeText(getContext(), "NO SE LLEVO ACABO EL REGISTRO", Toast.LENGTH_SHORT).show();
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
                     String n_solicituds = n_solicitud.getText().toString();
                     String nombre_clientes = nombre_cliente.getText().toString();
                     String fecha_regs = fecha_reg.getText().toString();
                     String estatuss = estatus.getText().toString();
                     String plazas = plaza.getText().toString();
                     String eventoss= eventos.getText().toString();
                     String lineatranss = lineatrans.getText().toString();
                     String placass = placas.getText().toString();
                     String placas2s= placas2.getText().toString();
                     String conductors= conductor.getText().toString();
                     String identificacions = identificacion.getText().toString();
                     String mercancias= mercancia.getText().toString();
                     String cantidads = cantidad.getText().toString();
                     String observacioness = observaciones.getText().toString();
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("n_solicituds", n_solicituds);
                    parametros.put("nombre_clientes", nombre_clientes);
                    parametros.put("fecha_regs", fecha_regs);
                    parametros.put("estatuss", estatuss);
                    parametros.put("plazas", plazas);
                    parametros.put("eventoss", eventoss);
                    parametros.put("lineatranss", lineatranss);
                    parametros.put("placass", placass);
                    parametros.put("placas2s", placas2s);
                    parametros.put("conductors", conductors);
                    parametros.put("identificacions", identificacions);
                    parametros.put("mercancias", mercancias);
                    parametros.put("cantidads", cantidads);
                    parametros.put("observacioness", observacioness);
                    return parametros;
                }
            };
            request.add(stringRequest);


        }
    }

    private void limpiarTodo() {
        progress.hide();
        n_solicitud.setText("");
        nombre_cliente.setText("");
        fecha_reg.setText("");
        estatus.setText("");
        plaza.setText("");
        eventos.setText("");
        lineatrans.setText("");
        placas.setText("");
        placas2.setText("");
        conductor.setText("");
        identificacion.setText("");
        mercancia.setText("");
        cantidad.setText("");
        observaciones.setText("");
        Toast.makeText(getContext(), "Registro Correcto", Toast.LENGTH_LONG).show();
    }

}