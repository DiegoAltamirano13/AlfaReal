package com.diego.lina.sistemadealmacenes.Adaptador;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.diego.lina.sistemadealmacenes.Entidades.CertificacionDesglozada;
import com.diego.lina.sistemadealmacenes.R;

import java.util.ArrayList;
import java.util.List;

public class CertificacionDesglozadaAdapter extends RecyclerView.Adapter<CertificacionDesglozadaAdapter.CertificacionDesglozadaHolder> implements View.OnClickListener {
    List<CertificacionDesglozada> certificacionDesglozadas;
    RequestQueue requestQueue;
    Context context;


    public CertificacionDesglozadaAdapter(ArrayList<CertificacionDesglozada> certificacionDesglozadas, Context context) {
        this.certificacionDesglozadas = certificacionDesglozadas;
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);

    }

    @NonNull
    @Override
    public CertificacionDesglozadaAdapter.CertificacionDesglozadaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View fragmentCert = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_cert_desglozada, viewGroup, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fragmentCert.setLayoutParams(layoutParams);
        fragmentCert.setOnClickListener(this);
        return new CertificacionDesglozadaHolder(fragmentCert);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificacionDesglozadaAdapter.CertificacionDesglozadaHolder certificacionDesglozadaHolder, int i) {
            if (i == 0){
                int iid_almacen = 0;
                int area_almacen = 0;

                for (int z = 0; z< certificacionDesglozadas.size(); z ++ ){
                    if (iid_almacen != certificacionDesglozadas.get(z).getIID_ALMACEN()){
                        TableRow encabezadoIni = new TableRow(context);
                        TableLayout.LayoutParams tableBD = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 200);
                        encabezadoIni.setLayoutParams(tableBD);
                        encabezadoIni.setPadding(10,10,10,10);
                        TextView textViewAlm = new TextView(context);
                        textViewAlm.setText("ALMACEN");
                        textViewAlm.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                        textViewAlm.setTextColor(Color.BLACK);
                        textViewAlm.setWidth(100);
                        textViewAlm.setHeight(70);
                        encabezadoIni.addView(textViewAlm);

                        TextView textViewIIDAlm = new TextView(context);
                        textViewIIDAlm.setText(String.valueOf(certificacionDesglozadas.get(z).getIID_ALMACEN()));
                        textViewIIDAlm.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                        textViewIIDAlm.setTextColor(Color.BLACK);
                        textViewIIDAlm.setWidth(100);
                        textViewIIDAlm.setHeight(70);
                        encabezadoIni.addView(textViewIIDAlm);

                        TextView textNombreAlmacen = new TextView(context);
                        textNombreAlmacen.setText(certificacionDesglozadas.get(z).getV_DIRECCION());
                        textNombreAlmacen.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                        textNombreAlmacen.setTextColor(Color.BLACK);
                        textNombreAlmacen.setWidth(1100);
                        textNombreAlmacen.setHeight(70);
                        encabezadoIni.addView(textNombreAlmacen);

                        TextView txtTipo = new TextView(context);

                        switch (certificacionDesglozadas.get(z).getS_TIPO_ALMACEN()){
                            case "2":
                                txtTipo.setText("NACIONAL");
                                break;
                            case "3":
                                txtTipo.setText("FISCAL");
                                break;
                            case  "5":
                                txtTipo.setText("HABILITADO");
                                break;
                            case "6":
                                txtTipo.setText("NACIONAL/FISCAL");
                                break;
                            case "15":
                                txtTipo.setText("FISCAL/HABILITADO");
                                break;
                            default:
                                txtTipo.setText("TIPO UNDEFINED");
                                break;
                        }

                        txtTipo.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                        txtTipo.setTextColor(Color.BLACK);
                        txtTipo.setWidth(100);
                        txtTipo.setHeight(70);
                        encabezadoIni.addView(txtTipo);

                        encabezadoIni.setBackgroundResource(R.drawable.shape_table);

                        certificacionDesglozadaHolder.tableLayoutCertDesg.addView(encabezadoIni);

                        iid_almacen = certificacionDesglozadas.get(z).getIID_ALMACEN();
                        TableRow encabezadoIni2 = new TableRow(context);
                        TableLayout.LayoutParams tableBD2 = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 200);
                        encabezadoIni2.setLayoutParams(tableBD2);
                        encabezadoIni2.setPadding(10,10,10,10);
                        TextView CERTIFICADO = new TextView(context);
                        CERTIFICADO.setText("CERTIFICADO");
                        CERTIFICADO.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                        CERTIFICADO.setTextColor(Color.BLACK);
                        CERTIFICADO.setWidth(450);
                        CERTIFICADO.setHeight(200);
                        encabezadoIni2.addView(CERTIFICADO);

                        TextView VALOR = new TextView(context);
                        VALOR.setText("RECIBO");
                        VALOR.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                        VALOR.setTextColor(Color.BLACK);
                        VALOR.setWidth(450);
                        VALOR.setHeight(200);
                        encabezadoIni2.addView(VALOR);

                        TextView CANTIDADUME = new TextView(context);
                        CANTIDADUME.setText("CANTIDAD");
                        CANTIDADUME.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                        CANTIDADUME.setTextColor(Color.BLACK);
                        CANTIDADUME.setWidth(450);
                        CANTIDADUME.setHeight(200);
                        encabezadoIni2.addView(CANTIDADUME);

                        TextView UME = new TextView(context);
                        UME.setText("PESO");
                        UME.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                        UME.setTextColor(Color.BLACK);
                        UME.setWidth(450);
                        UME.setHeight(200);
                        encabezadoIni2.addView(UME);

                        TextView UMEXUMC = new TextView(context);
                        UMEXUMC.setText("VALOR");
                        UMEXUMC.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                        UMEXUMC.setTextColor(Color.BLACK);
                        UMEXUMC.setWidth(450);
                        UMEXUMC.setHeight(200);
                        encabezadoIni2.addView(UMEXUMC);

                        TextView UMEXUMC2 = new TextView(context);
                        UMEXUMC2.setText("VALOR");
                        UMEXUMC2.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                        UMEXUMC2.setTextColor(Color.BLACK);
                        UMEXUMC2.setWidth(450);
                        UMEXUMC2.setHeight(200);
                        encabezadoIni2.addView(UMEXUMC2);

                        certificacionDesglozadaHolder.tableLayoutCertDesg.addView(encabezadoIni2);
                    }

                    TableRow bodyInvFis = new TableRow(context);
                    TableLayout.LayoutParams tableBodyLayoutParams = new TableLayout.LayoutParams(500, 500);
                    bodyInvFis.setLayoutParams(tableBodyLayoutParams);
                    bodyInvFis.setPadding(10,10,10,10);

                    TextView iid_cert = new TextView(context);
                    iid_cert.setText( certificacionDesglozadas.get(z).getVID_CERTIFICADO());
                    iid_cert.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    iid_cert.setTextColor(Color.WHITE);
                    iid_cert.setWidth(100);
                    iid_cert.setHeight(70);
                    iid_cert.setTextColor(Color.BLACK);
                    bodyInvFis.addView(iid_cert);


                    String n_cantidad_valor = certificacionDesglozadas.get(z).getREFERENCIA().toString();
                    TextView valor = new TextView(context);
                    valor.setText(n_cantidad_valor);
                    valor.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    valor.setTextColor(Color.WHITE);
                    valor.setWidth(100);
                    valor.setHeight(70);
                    valor.setTextColor(Color.BLACK);
                    bodyInvFis.addView(valor);

                    if (certificacionDesglozadas.get(z).getC_CANTIDAD().toString() == "null"){
                        TextView cantidad_ume = new TextView(context);
                        cantidad_ume.setText("0.00");
                        cantidad_ume.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                        cantidad_ume.setTextColor(Color.WHITE);
                        cantidad_ume.setWidth(100);
                        cantidad_ume.setHeight(70);
                        cantidad_ume.setTextColor(Color.BLACK);
                        bodyInvFis.addView(cantidad_ume);
                    }else {
                        String cantidadUme = certificacionDesglozadas.get(z).getC_CANTIDAD().toString();
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
                    }



                    TextView ume = new TextView(context);
                    ume.setText(certificacionDesglozadas.get(z).getC_PESO_TOTAL()
                    );
                    ume.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    ume.setTextColor(Color.WHITE);
                    ume.setWidth(100);
                    ume.setHeight(70);
                    ume.setTextColor(Color.BLACK);
                    bodyInvFis.addView(ume);

                    if (certificacionDesglozadas.get(z).getN_VALOR_INI().toString() == "null"){
                        TextView umexumc = new TextView(context);
                        umexumc.setText("0.00");
                        umexumc.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                        umexumc.setTextColor(Color.WHITE);
                        umexumc.setWidth(100);
                        umexumc.setHeight(70);
                        umexumc.setTextColor(Color.BLACK);
                        bodyInvFis.addView(umexumc);
                    }else {
                        TextView umexumc = new TextView(context);
                        String cantidadUmcxUme = certificacionDesglozadas.get(z).getN_VALOR_INI().toString();
                        Double cantidadUmcxUmeForm = Double.parseDouble(cantidadUmcxUme);
                        String cantidadUmcxUmeForma = String.format("%, .2f", cantidadUmcxUmeForm);
                        umexumc.setText(cantidadUmcxUmeForma);
                        umexumc.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                        umexumc.setTextColor(Color.WHITE);
                        umexumc.setWidth(100);
                        umexumc.setHeight(70);
                        umexumc.setTextColor(Color.BLACK);
                        bodyInvFis.addView(umexumc);
                    }

                    if (certificacionDesglozadas.get(z).getC_VALOR_TOTAL().toString() == "null"){
                        TextView umexumc = new TextView(context);
                        umexumc.setText("0.00");
                        umexumc.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                        umexumc.setTextColor(Color.WHITE);
                        umexumc.setWidth(100);
                        umexumc.setHeight(70);
                        umexumc.setTextColor(Color.BLACK);
                        bodyInvFis.addView(umexumc);
                    }else {
                        TextView umexumc = new TextView(context);
                        String cantidadUmcxUme = certificacionDesglozadas.get(z).getC_VALOR_TOTAL().toString();
                        Double cantidadUmcxUmeForm = Double.parseDouble(cantidadUmcxUme);
                        String cantidadUmcxUmeForma = String.format("%, .2f", cantidadUmcxUmeForm);
                        umexumc.setText(cantidadUmcxUmeForma);
                        umexumc.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                        umexumc.setTextColor(Color.WHITE);
                        umexumc.setWidth(100);
                        umexumc.setHeight(70);
                        umexumc.setTextColor(Color.BLACK);
                        bodyInvFis.addView(umexumc);
                    }

                    certificacionDesglozadaHolder.tableLayoutCertDesg.addView(bodyInvFis);
                }
            }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void onClick(View v) {

    }

    public class CertificacionDesglozadaHolder extends RecyclerView.ViewHolder{
        TableLayout tableLayoutCertDesg;
        public CertificacionDesglozadaHolder(@NonNull View fragmentCert) {
            super(fragmentCert);
            tableLayoutCertDesg = fragmentCert.findViewById(R.id.tableLayoutPackingList);
            tableLayoutCertDesg.removeAllViews();
        }
    }
}
