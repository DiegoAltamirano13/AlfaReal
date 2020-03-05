package com.diego.lina.sistemadealmacenes.Adaptador;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.diego.lina.sistemadealmacenes.Entidades.CertificacionNormal;
import com.diego.lina.sistemadealmacenes.Entidades.Solicitudes_Carga_Descarga;
import com.diego.lina.sistemadealmacenes.R;

import java.util.ArrayList;
import java.util.List;

public class CertDesglozadaAdapter extends RecyclerView.Adapter<CertDesglozadaAdapter.CertDesglozadaHolder> implements View.OnClickListener {

    List<CertificacionNormal> certificacionNormals;
    RequestQueue requestQueue;
    Context context;

    public CertDesglozadaAdapter(ArrayList<CertificacionNormal> certificacionNormals, Context context){
        this.certificacionNormals = certificacionNormals;
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public CertDesglozadaAdapter.CertDesglozadaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View fragmentCert = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_cert_desglozada, viewGroup, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fragmentCert.setLayoutParams(layoutParams);
        fragmentCert.setOnClickListener(this);
        return new CertDesglozadaHolder(fragmentCert);
    }

    @Override
    public void onBindViewHolder(@NonNull CertDesglozadaAdapter.CertDesglozadaHolder certDesglozadaHolder, int i ) {

        if (i == 0){
            Log.e("I es mayor que 1 " , "CUIDADO ");
            certDesglozadaHolder.filasEncabezado = new TableRow(context);
            String [] cabezal =  {"CERTIF", "VALOR", "CANTIDAD UME", "UME",  "UMC X UME", "UMC", "TONELADAS"};

            TableLayout.LayoutParams layoutParamsEncabezado = new TableLayout.LayoutParams(500, 550);
            layoutParamsEncabezado.setMargins(6,6,6,6);
            certDesglozadaHolder.filasEncabezado.setLayoutParams(layoutParamsEncabezado);
            certDesglozadaHolder.filasEncabezado.setPadding(5,5,5,5);

            for (int x = 0; x < cabezal.length; x++){
                TextView tvEncabezado = new TextView(context);
                tvEncabezado.setText(cabezal[x]);
                tvEncabezado.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                tvEncabezado.setWidth(450);
                tvEncabezado.setHeight(200);
                tvEncabezado.setPaintFlags(tvEncabezado.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                certDesglozadaHolder.filasEncabezado.addView(tvEncabezado);
            }
            certDesglozadaHolder.tableLayoutCerDesg.addView(certDesglozadaHolder.filasEncabezado, 0);

            for (int z = 0; z < certificacionNormals.size(); z++){
                Log.e("TAMAÑO DE ARRAY", String.valueOf(certificacionNormals.size()));
                TableRow bodyInvFis = new TableRow(context);
                TableLayout.LayoutParams tableBodyLayoutParams = new TableLayout.LayoutParams(500, 500);
                bodyInvFis.setLayoutParams(tableBodyLayoutParams);
                bodyInvFis.setPadding(10,10,10,10);
                //No CERT
                TextView iid_cert = new TextView(context);
                iid_cert.setText( certificacionNormals.get(z).getIID_NUM_CERT_N());
                iid_cert.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                iid_cert.setTextColor(Color.WHITE);
                iid_cert.setWidth(100);
                iid_cert.setHeight(70);
                iid_cert.setTextColor(Color.BLACK);
                bodyInvFis.addView(iid_cert);


                String n_cantidad_valor = certificacionNormals.get(z).getN_CANT_VALOR().toString();
                Double numCantidadValor = Double.parseDouble(n_cantidad_valor);
                String numerocv = String.format("%, .2f", numCantidadValor);
                TextView valor = new TextView(context);
                valor.setText(numerocv);
                valor.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                valor.setTextColor(Color.WHITE);
                valor.setWidth(100);
                valor.setHeight(70);
                valor.setTextColor(Color.BLACK);
                bodyInvFis.addView(valor);

                String cantidadUme = certificacionNormals.get(z).getN_CANT_BULTOS().toString();
                Double cantidadUmeForm = Double.parseDouble(cantidadUme);
                String cantidadUmeForma = String.format("%, .2f", cantidadUmeForm);
                TextView cantidad_ume = new TextView(context);
                cantidad_ume.setText(cantidadUmeForma);
                cantidad_ume.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                cantidad_ume.setTextColor(Color.WHITE);
                cantidad_ume.setWidth(100);
                cantidad_ume.setHeight(70);
                cantidad_ume.setTextColor(Color.BLACK);
                bodyInvFis.addView(cantidad_ume);


                TextView ume = new TextView(context);
                ume.setText(certificacionNormals.get(z).getV_UME());
                ume.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                ume.setTextColor(Color.WHITE);
                ume.setWidth(100);
                ume.setHeight(70);
                ume.setTextColor(Color.BLACK);
                bodyInvFis.addView(ume);


                TextView umexumc = new TextView(context);
                String cantidadUmcxUme = certificacionNormals.get(z).getN_CANT_UMC().toString();
                Double cantidadUmcxUmeForm = Double.parseDouble(cantidadUmcxUme);
                String cantidadUmcxUmeForma = String.format("%, .2f", cantidadUmcxUmeForm);
                umexumc.setText(cantidadUmcxUmeForma);
                umexumc.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                umexumc.setTextColor(Color.WHITE);
                umexumc.setWidth(100);
                umexumc.setHeight(70);
                umexumc.setTextColor(Color.BLACK);
                bodyInvFis.addView(umexumc);

                TextView umc = new TextView(context);
                umc.setText(certificacionNormals.get(z).getV_UMC());
                umc.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                umc.setTextColor(Color.WHITE);
                umc.setWidth(100);
                umc.setHeight(70);
                umc.setTextColor(Color.BLACK);
                bodyInvFis.addView(umc);

                TextView ton = new TextView(context);
                String cantidad_Tons = certificacionNormals.get(z).getN_CANT_TONS().toString();
                Double cantidad_TonsForm = Double.parseDouble(cantidad_Tons);
                String cantidad_TonsForma = String.format("%, .2f", cantidad_TonsForm);
                ton.setText(certificacionNormals.get(z).getN_CANT_TONS());
                ton.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                ton.setTextColor(Color.WHITE);
                ton.setWidth(100);
                ton.setHeight(70);
                ton.setTextColor(Color.BLACK);
                bodyInvFis.addView(ton);

                certDesglozadaHolder.tableLayoutCerDesg.addView(bodyInvFis);

            }
        }else {
            Log.e("I es menor que 1 " , "CUIDADO ");
        }
    }

    @Override
    public int getItemCount() {
        return certificacionNormals.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class CertDesglozadaHolder extends RecyclerView.ViewHolder {
        TableLayout tableLayoutCerDesg;
        TableRow filasEncabezado;
        TableRow.LayoutParams layoutParamsCertDesg = new TableRow.LayoutParams(500, 500);

        public CertDesglozadaHolder(View fragmentCert) {
            super(fragmentCert);
            tableLayoutCerDesg = fragmentCert.findViewById(R.id.tableLayoutPackingList);

            tableLayoutCerDesg.removeAllViews();

        }
    }
}
