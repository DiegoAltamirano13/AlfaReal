package com.diego.lina.sistemadealmacenes.Adaptador;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.diego.lina.sistemadealmacenes.Entidades.Solicitudes_Carga_Descarga;
import com.diego.lina.sistemadealmacenes.Entidades.Usuario;
import com.diego.lina.sistemadealmacenes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.diego.lina.sistemadealmacenes.R.style.Theme_AppCompat_Dialog;

public class ImagenesAdapter extends RecyclerView.Adapter<ImagenesAdapter.MercanciasHolder> implements View.OnClickListener {

    List<Solicitudes_Carga_Descarga> listaSolicitud;
    RequestQueue requestQueue;
    Context context;
    Button btn_info_img;
    private View.OnClickListener listener;

    public ImagenesAdapter(ArrayList<Solicitudes_Carga_Descarga> lista_solicitudes, Context context){
        this.listaSolicitud = lista_solicitudes;
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }


    @NonNull
    @Override
    public MercanciasHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View fragmento = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mercancia_lista, viewGroup, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        fragmento.setLayoutParams(layoutParams);
        fragmento.setOnClickListener(this);


        return new MercanciasHolder(fragmento);
    }

    @Override
    public void onBindViewHolder(@NonNull MercanciasHolder mercanciasHolder, final int i) {
        mercanciasHolder.id_solicitud.setText(listaSolicitud.get(i).getId_solicitud().toString());
        mercanciasHolder.d_fec_recepcion.setText(listaSolicitud.get(i).getD_fec_recepcion().toString());
        String fecha_aprox_llegada = (listaSolicitud.get(i).getD_fec_llegada_aprox().toString());
        String fecha_llegada_real= (listaSolicitud.get(i).getD_fec_llegada_real().toString());
        String fecha_inicio_descarga =(listaSolicitud.get(i).getD_fec_ini_car_des().toString());
        String fecha_fin_descarga = (listaSolicitud.get(i).getD_fec_fin_car_des().toString());
        String fecha_despedida_vehiculo = (listaSolicitud.get(i).getD_fec_desp_vehic().toString());
        String n_status = (listaSolicitud.get(i).getN_status().toString());
        mercanciasHolder.v_descripcion.setText(listaSolicitud.get(i).getV_descripcion().toString());
        mercanciasHolder.placas.setText(listaSolicitud.get(i).getPlacas().toString());
        mercanciasHolder.v_mercancia.setText(listaSolicitud.get(i).getV_mercancia().toString());
        mercanciasHolder.v_nombre.setText(listaSolicitud.get(i).getV_nombre().toString());
        mercanciasHolder.cantidad_ume.setText(listaSolicitud.get(i).getCantidad_ume().toString());

        if (fecha_despedida_vehiculo.equals("NO TIENE FECHA")){
            mercanciasHolder.d_fec_desp_vehic.setText("");
        }else {
            mercanciasHolder.d_fec_desp_vehic.setText(fecha_despedida_vehiculo);
        }

        if (n_status.equals("1")){
            mercanciasHolder.n_status.setText("Registrado");
            mercanciasHolder.n_status.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_warning));
        }else if (n_status.equals("2")){
            mercanciasHolder.n_status.setText("Llega Vehiculo");
            mercanciasHolder.n_status.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_success));
        }else if (n_status.equals("3")){
            mercanciasHolder.n_status.setText("Inicio Carga");
            mercanciasHolder.n_status.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_success));
        }else if (n_status.equals("4")){
            mercanciasHolder.n_status.setText("Carga Finalizada");
            mercanciasHolder.n_status.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_success));
        }else if (n_status.equals("5")){
            mercanciasHolder.n_status.setText("Despacho Vehiculo");
            mercanciasHolder.n_status.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_success));
        }else if (n_status.equals("6")){
            mercanciasHolder.n_status.setText("Cancelado");
            mercanciasHolder.n_status.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_error));
        }else if (n_status.equals("0")){
            mercanciasHolder.n_status.setText("Registrado");
            mercanciasHolder.n_status.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_warning));
        }
    }

    @Override
    public int getItemCount() {
        return listaSolicitud.size();
    }

    public  void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }


    public class MercanciasHolder extends RecyclerView.ViewHolder{
        TextView id_solicitud,  d_fec_recepcion, d_fec_aprox_llegada;
        TextView d_fec_desp_vehic, n_status, v_descripcion, placas, v_mercancia;
        TextView v_nombre, cantidad_ume;

        TableRow fila = new TableRow(context);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);

        //fila.setLayoutParams(layoutParams);
        public MercanciasHolder(View fragmento) {
            super(fragmento);
            //fila.addView(tvzzzz);
            id_solicitud = fragmento.findViewById(R.id.solicitud);
            id_solicitud.setText("ID_SOLICITUD");
            v_nombre = fragmento.findViewById(R.id.almacen);
            d_fec_recepcion = fragmento.findViewById(R.id.llegada);
            d_fec_desp_vehic = fragmento.findViewById(R.id.despacho);
            n_status = fragmento.findViewById(R.id.estatus);
            v_descripcion = fragmento.findViewById(R.id.vehiculo);
            placas = fragmento.findViewById(R.id.placas);
            v_mercancia = fragmento.findViewById(R.id.mercancia);
            cantidad_ume = fragmento.findViewById(R.id.cantidad);
        }
    }

}
