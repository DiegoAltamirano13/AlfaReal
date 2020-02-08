package com.diego.lina.sistemadealmacenes.Adaptador;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.diego.lina.sistemadealmacenes.Entidades.Solicitudes_Carga_Descarga;
import com.diego.lina.sistemadealmacenes.R;

import java.util.ArrayList;
import java.util.List;

public class SolicitudesAdapter extends RecyclerView.Adapter<SolicitudesAdapter.SolicitudesHolder>  implements View.OnClickListener{
    List<Solicitudes_Carga_Descarga> solicitudes_carga_descargas;
    RequestQueue requestQueue;
    Context context;
    Button info_Sol;

    private View.OnClickListener listener;

    public SolicitudesAdapter(ArrayList<Solicitudes_Carga_Descarga>solicitudes_carga_descargas, Context context){
        this.solicitudes_carga_descargas = solicitudes_carga_descargas;
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public SolicitudesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View fragmento = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.solicitud_lista, viewGroup, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fragmento.setLayoutParams(layoutParams);
        fragmento.setOnClickListener(this);

        return new SolicitudesHolder(fragmento);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudesHolder solicitudesHolder, int i) {
        solicitudesHolder.id_solicitud = solicitudes_carga_descargas.get(i).getId_solicitud().toString();
        solicitudesHolder.d_fec_recepcion = solicitudes_carga_descargas.get(i).getD_fec_recepcion().toString();
        solicitudesHolder.d_fec_aprox_llegada = (solicitudes_carga_descargas.get(i).getD_fec_llegada_aprox().toString());
        solicitudesHolder.d_fec_llegada_real =(solicitudes_carga_descargas.get(i).getD_fec_llegada_real().toString());
        solicitudesHolder.d_fec_ini_car_des = (solicitudes_carga_descargas.get(i).getD_fec_ini_car_des().toString());
        solicitudesHolder.d_fec_fin_car_des =(solicitudes_carga_descargas.get(i).getD_fec_fin_car_des().toString());
        solicitudesHolder.d_fec_desp_vehic = (solicitudes_carga_descargas.get(i).getD_fec_desp_vehic().toString());
        solicitudesHolder.n_status = (solicitudes_carga_descargas.get(i).getN_status().toString());
        solicitudesHolder.v_descripcion =(solicitudes_carga_descargas.get(i).getV_descripcion().toString());
        solicitudesHolder.placas = (solicitudes_carga_descargas.get(i).getPlacas().toString());
        solicitudesHolder.placas2 = (solicitudes_carga_descargas.get(i).getPlacas2().toString());
        solicitudesHolder.v_mercancia =(solicitudes_carga_descargas.get(i).getV_mercancia().toString());
        solicitudesHolder.evento = (solicitudes_carga_descargas.get(i).getEvento().toString());
        solicitudesHolder.chofer = (solicitudes_carga_descargas.get(i).getChofer().toString());
        solicitudesHolder.v_nombre = (solicitudes_carga_descargas.get(i).getV_nombre().toString());
        solicitudesHolder.iid_almacen = (solicitudes_carga_descargas.get(i).getIid_almacen().toString());
        solicitudesHolder.cantidad_ume = (solicitudes_carga_descargas.get(i).getCantidad_ume().toString());
        solicitudesHolder.nombre_ume = (solicitudes_carga_descargas.get(i).getNombre_ume().toString());
        solicitudesHolder.vid_usuario_cliente = (solicitudes_carga_descargas.get(i).getVid_usuario_cliente().toString());

        solicitudesHolder.n_solic.setText(solicitudes_carga_descargas.get(i).getId_solicitud().toString());
    }

    @Override
    public int getItemCount() {
        return solicitudes_carga_descargas.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class SolicitudesHolder extends RecyclerView.ViewHolder{
        String id_solicitud,  d_fec_recepcion, d_fec_aprox_llegada, d_fec_llegada_real, d_fec_ini_car_des;
        String d_fec_fin_car_des, d_fec_desp_vehic, n_status, v_descripcion, placas, placas2, v_mercancia;
        String evento, chofer, v_nombre, iid_almacen, cantidad_ume, nombre_ume, vid_usuario_cliente;
        TextView n_solic;

        Typeface fuente = Typeface.createFromAsset(context.getAssets(),"fonts/Biysk.ttf" );
        Typeface fuente2 = Typeface.createFromAsset(context.getAssets(),"fonts/boldblue.ttf" );

        public SolicitudesHolder(View fragmento) {
            super(fragmento);
            n_solic = fragmento.findViewById(R.id.texto_Solicitud);
            Toast.makeText(context,id_solicitud, Toast.LENGTH_LONG).show();
        }
    }
}
