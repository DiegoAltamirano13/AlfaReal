package com.diego.lina.sistemadealmacenes.Adaptador;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    int bandera = 0;
    String nu_sol;

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
    public void onBindViewHolder(@NonNull final SolicitudesHolder solicitudesHolder, int i) {
        solicitudesHolder.id_solicitud.setText(solicitudes_carga_descargas.get(i).getId_solicitud().toString());
        solicitudesHolder.d_fec_recepcion.setText(solicitudes_carga_descargas.get(i).getD_fec_recepcion().toString());
        String fecha_aprox_llegada = (solicitudes_carga_descargas.get(i).getD_fec_llegada_aprox().toString());
        String fecha_llegada_real= (solicitudes_carga_descargas.get(i).getD_fec_llegada_real().toString());
        String fecha_inicio_descarga =(solicitudes_carga_descargas.get(i).getD_fec_ini_car_des().toString());
        String fecha_fin_descarga = (solicitudes_carga_descargas.get(i).getD_fec_fin_car_des().toString());
        String fecha_despedida_vehiculo = (solicitudes_carga_descargas.get(i).getD_fec_desp_vehic().toString());
        solicitudesHolder.v_descripcion.setText(solicitudes_carga_descargas.get(i).getV_descripcion().toString());
        solicitudesHolder.placas.setText(solicitudes_carga_descargas.get(i).getPlacas().toString());
        solicitudesHolder.chofer.setText(solicitudes_carga_descargas.get(i).getChofer().toString());
        solicitudesHolder.v_nombre.setText(solicitudes_carga_descargas.get(i).getV_nombre().toString());
        if (fecha_llegada_real.equals("NO TIENE FECHA")){
           solicitudesHolder.d_fec_llegada_real.setText(fecha_aprox_llegada);
            solicitudesHolder.l_enrampado.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_circle_red));
            solicitudesHolder.truck2.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_circle_red));
        }else {
            solicitudesHolder.d_fec_llegada_real.setText(fecha_llegada_real);
        }

        if (fecha_inicio_descarga.equals("NO TIENE FECHA")){
            solicitudesHolder.d_fec_ini_car_des.setText("");
            solicitudesHolder.l_carga.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_circle_red));
            solicitudesHolder.truck3.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_circle_red));
        }else {
            solicitudesHolder.d_fec_ini_car_des.setText(fecha_inicio_descarga);
        }

        if (fecha_fin_descarga.equals("NO TIENE FECHA")){
            solicitudesHolder.d_fec_fin_car_des.setText("");
            solicitudesHolder.l_descarga.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_circle_red));
            solicitudesHolder.truck4.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_circle_red));
        }else {
            solicitudesHolder.d_fec_fin_car_des.setText(fecha_fin_descarga);
        }

        if (fecha_despedida_vehiculo.equals("NO TIENE FECHA")){
            solicitudesHolder.d_fec_desp_vehic.setText("");
            solicitudesHolder.l_vehiculo_desp.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_circle_red));
            solicitudesHolder.truck5.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_circle_red));
        }else {
            solicitudesHolder.d_fec_desp_vehic.setText(fecha_despedida_vehiculo);
        }
        solicitudesHolder.vista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //Si es 0 es nuevo si es 1 es visible si es 2 es invisible
                if (solicitudesHolder.vista2.getVisibility() == View.INVISIBLE){
                    solicitudesHolder.vista2.setVisibility(View.VISIBLE);
                    solicitudesHolder.vista2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    /*CardView cw = new CardView(context);
                    cw.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));*/
                }else {
                    solicitudesHolder.vista2.setVisibility(View.INVISIBLE);
                    solicitudesHolder.vista2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return solicitudes_carga_descargas.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class SolicitudesHolder extends RecyclerView.ViewHolder{
        TextView id_solicitud,  d_fec_recepcion, d_fec_aprox_llegada, d_fec_llegada_real, d_fec_ini_car_des;
        TextView d_fec_fin_car_des, d_fec_desp_vehic, n_status, v_descripcion, placas, placas2, v_mercancia;
        TextView evento, chofer, v_nombre, iid_almacen, cantidad_ume, nombre_ume, vid_usuario_cliente;
        ImageView truc_1, truck2, truck3, truck4, truck5;
        LinearLayout l_registrado, l_enrampado, l_carga, l_descarga, l_vehiculo_desp;

        View vista;
        CardView vista2;
        public SolicitudesHolder(View fragmento) {
            super(fragmento);

            id_solicitud = fragmento.findViewById(R.id.texto_Solicitud);
            v_nombre = fragmento.findViewById(R.id.texto_Almacen);
            v_descripcion = fragmento.findViewById(R.id.texto_Vehiculo);
            placas = fragmento.findViewById(R.id.texto_Placas);
            chofer = fragmento.findViewById(R.id.texto_Conductor);

            //fechas mamalonas
            d_fec_recepcion = fragmento.findViewById(R.id.fecha_reg);
            d_fec_llegada_real = fragmento.findViewById(R.id.fecha_enrampado);
            d_fec_ini_car_des = fragmento.findViewById(R.id.fecha_descarga);
            d_fec_fin_car_des = fragmento.findViewById(R.id.fecha_cierre);
            d_fec_desp_vehic = fragmento.findViewById(R.id.fecha_despachado);

            l_registrado = fragmento.findViewById(R.id.layout_registrado);
            l_enrampado = fragmento.findViewById(R.id.layout_enrampado);
            l_carga = fragmento.findViewById(R.id.layout_carga);
            l_descarga = fragmento.findViewById(R.id.layout_descargado);
            l_vehiculo_desp = fragmento.findViewById(R.id.layout_vehiculo_desp);

            truc_1 = fragmento.findViewById(R.id.truck_ini);
            truck2 = fragmento.findViewById(R.id.truck_ini_carga);
            truck3 = fragmento.findViewById(R.id.truck_carga);
            truck4 = fragmento.findViewById(R.id.truck_fin_carga);
            truck5 = fragmento.findViewById(R.id.truck_despacho);

            vista = fragmento.findViewById(R.id.card);
            vista2 = fragmento.findViewById(R.id.cardview);
        }
    }
}
