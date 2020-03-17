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
import android.widget.Switch;
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

            int almacen = 0;
            double valort = 0.00;
            double cantidad_umeva = 0.00;
            double tonelaje = 0.00;

            for (int z = 0; z < certificacionNormals.size(); z++){

                /**REVISA TODO CORRECTO*/
                if (almacen != 0 && almacen != certificacionNormals.get(z).getIID_ALMACEN()){
                    Log.e("Valores ", String.format("%, .2f", valort) + "   " + String.format("%, .2f", cantidad_umeva) + "    " + String.format("%, .2f", tonelaje));

                    TableRow encabezadoIni = new TableRow(context);
                    TableLayout.LayoutParams tableBD = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 200);
                    encabezadoIni.setLayoutParams(tableBD);
                    encabezadoIni.setPadding(10,10,10,10);
                    TextView textViewAlm = new TextView(context);
                    textViewAlm.setText("");
                    textViewAlm.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    textViewAlm.setTextColor(Color.BLACK);
                    textViewAlm.setWidth(100);
                    textViewAlm.setHeight(70);
                    encabezadoIni.addView(textViewAlm);

                    TextView textViewIIDAlm = new TextView(context);
                    textViewIIDAlm.setText(String.format("%, .2f", valort));
                    textViewIIDAlm.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    textViewIIDAlm.setTextColor(Color.BLACK);
                    textViewIIDAlm.setWidth(100);
                    textViewIIDAlm.setHeight(70);
                    encabezadoIni.addView(textViewIIDAlm);

                    TextView textNombreAlmacen = new TextView(context);
                    textNombreAlmacen.setText(String.format("%, .2f",cantidad_umeva));
                    textNombreAlmacen.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    textNombreAlmacen.setTextColor(Color.BLACK);
                    textNombreAlmacen.setWidth(100);
                    textNombreAlmacen.setHeight(70);
                    encabezadoIni.addView(textNombreAlmacen);

                    TextView txtTipo = new TextView(context);
                    txtTipo.setText("");
                    txtTipo.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    txtTipo.setTextColor(Color.BLACK);
                    txtTipo.setWidth(100);
                    txtTipo.setHeight(70);
                    encabezadoIni.addView(txtTipo);

                    TextView txtTipUMo = new TextView(context);
                    txtTipUMo.setText("");
                    txtTipUMo.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    txtTipUMo.setTextColor(Color.BLACK);
                    txtTipUMo.setWidth(100);
                    txtTipUMo.setHeight(70);
                    encabezadoIni.addView(txtTipUMo);

                    TextView txtTipUMo1 = new TextView(context);
                    txtTipUMo1.setText("");
                    txtTipUMo1.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    txtTipUMo1.setTextColor(Color.BLACK);
                    txtTipUMo1.setWidth(100);
                    txtTipUMo1.setHeight(70);
                    encabezadoIni.addView(txtTipUMo1);

                    TextView tone = new TextView(context);
                    tone.setText(String.format("%, .2f",tonelaje));
                    tone.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    tone.setTextColor(Color.BLACK);
                    tone.setWidth(100);
                    tone.setHeight(70);
                    encabezadoIni.addView(tone);
                    encabezadoIni.setBackgroundResource(R.drawable.shape_table_total);
                    certDesglozadaHolder.tableLayoutCerDesg.addView(encabezadoIni);

                    valort = 0;
                    cantidad_umeva = 0;
                    tonelaje = 0;
                }

                /**GENERAR POR TODOS LOS ALMACENES**/
                if (almacen != certificacionNormals.get(z).getIID_ALMACEN()){

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
                    textViewIIDAlm.setText(String.valueOf(certificacionNormals.get(z).getIID_ALMACEN()));
                    textViewIIDAlm.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    textViewIIDAlm.setTextColor(Color.BLACK);
                    textViewIIDAlm.setWidth(100);
                    textViewIIDAlm.setHeight(70);
                    encabezadoIni.addView(textViewIIDAlm);

                    TextView textNombreAlmacen = new TextView(context);
                    textNombreAlmacen.setText(certificacionNormals.get(z).getV_DIRECCION());
                    textNombreAlmacen.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    textNombreAlmacen.setTextColor(Color.BLACK);
                    textNombreAlmacen.setWidth(1100);
                    textNombreAlmacen.setHeight(70);
                    encabezadoIni.addView(textNombreAlmacen);

                    TextView txtTipo = new TextView(context);

                    switch (certificacionNormals.get(z).getS_TIPO_ALMACEN()){
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

                    certDesglozadaHolder.tableLayoutCerDesg.addView(encabezadoIni);
                    almacen = certificacionNormals.get(z).getIID_ALMACEN();

                    //Sub encabezado papi
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
                    VALOR.setText("VALOR");
                    VALOR.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    VALOR.setTextColor(Color.BLACK);
                    VALOR.setWidth(450);
                    VALOR.setHeight(200);
                    encabezadoIni2.addView(VALOR);

                    TextView CANTIDADUME = new TextView(context);
                    CANTIDADUME.setText("CANTIDAD UME");
                    CANTIDADUME.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    CANTIDADUME.setTextColor(Color.BLACK);
                    CANTIDADUME.setWidth(450);
                    CANTIDADUME.setHeight(200);
                    encabezadoIni2.addView(CANTIDADUME);

                    TextView UME = new TextView(context);
                    UME.setText("UME");
                    UME.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    UME.setTextColor(Color.BLACK);
                    UME.setWidth(450);
                    UME.setHeight(200);
                    encabezadoIni2.addView(UME);

                    TextView UMEXUMC = new TextView(context);
                    UMEXUMC.setText("UMEXUMC");
                    UMEXUMC.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    UMEXUMC.setTextColor(Color.BLACK);
                    UMEXUMC.setWidth(450);
                    UMEXUMC.setHeight(200);
                    encabezadoIni2.addView(UMEXUMC);

                    TextView UMC = new TextView(context);
                    UMC.setText("UMC");
                    UMC.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    UMC.setTextColor(Color.BLACK);
                    UMC.setWidth(450);
                    UMC.setHeight(200);
                    encabezadoIni2.addView(UMC);


                    TextView TONELADAS = new TextView(context);
                    TONELADAS.setText("TONELADAS");
                    TONELADAS.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    TONELADAS.setTextColor(Color.BLACK);
                    TONELADAS.setWidth(450);
                    TONELADAS.setHeight(200);
                    encabezadoIni2.addView(TONELADAS);



                    certDesglozadaHolder.tableLayoutCerDesg.addView(encabezadoIni2);
                    almacen = certificacionNormals.get(z).getIID_ALMACEN();

                }

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

                //Prueba valor RT
                valort = valort + numCantidadValor;

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

                //Valor de cantidad UME FORM
                cantidad_umeva = cantidad_umeva + cantidadUmeForm;

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

                tonelaje = tonelaje  + cantidad_TonsForm;

                certDesglozadaHolder.tableLayoutCerDesg.addView(bodyInvFis);

                if (certificacionNormals.size() -1 == z){
                    Log.e("Valores2 final  ", String.format("%, .2f", valort) + "   " + String.format("%, .2f", cantidad_umeva) + "    " + String.format("%, .2f", tonelaje));
                    TableRow encabezadoIni = new TableRow(context);
                    TableLayout.LayoutParams tableBD = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 200);
                    encabezadoIni.setLayoutParams(tableBD);
                    encabezadoIni.setPadding(10,10,10,10);
                    TextView textViewAlm = new TextView(context);
                    textViewAlm.setText("");
                    textViewAlm.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    textViewAlm.setTextColor(Color.BLACK);
                    textViewAlm.setWidth(100);
                    textViewAlm.setHeight(70);
                    encabezadoIni.addView(textViewAlm);

                    TextView textViewIIDAlm = new TextView(context);
                    textViewIIDAlm.setText(String.format("%, .2f",valort));
                    textViewIIDAlm.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    textViewIIDAlm.setTextColor(Color.BLACK);
                    textViewIIDAlm.setWidth(100);
                    textViewIIDAlm.setHeight(70);
                    encabezadoIni.addView(textViewIIDAlm);

                    TextView textNombreAlmacen = new TextView(context);
                    textNombreAlmacen.setText(String.format("%, .2f",cantidad_umeva));
                    textNombreAlmacen.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    textNombreAlmacen.setTextColor(Color.BLACK);
                    textNombreAlmacen.setWidth(100);
                    textNombreAlmacen.setHeight(70);
                    encabezadoIni.addView(textNombreAlmacen);

                    TextView txtTipo = new TextView(context);
                    txtTipo.setText("");
                    txtTipo.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    txtTipo.setTextColor(Color.BLACK);
                    txtTipo.setWidth(100);
                    txtTipo.setHeight(70);
                    encabezadoIni.addView(txtTipo);

                    TextView txtTipUMo = new TextView(context);
                    txtTipUMo.setText("");
                    txtTipUMo.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    txtTipUMo.setTextColor(Color.BLACK);
                    txtTipUMo.setWidth(100);
                    txtTipUMo.setHeight(70);
                    encabezadoIni.addView(txtTipUMo);

                    TextView txtTipUMo1 = new TextView(context);
                    txtTipUMo1.setText("");
                    txtTipUMo1.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    txtTipUMo1.setTextColor(Color.BLACK);
                    txtTipUMo1.setWidth(100);
                    txtTipUMo1.setHeight(70);
                    encabezadoIni.addView(txtTipUMo1);

                    TextView tone = new TextView(context);
                    tone.setText(String.format("%, .2f",tonelaje));
                    tone.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                    tone.setTextColor(Color.BLACK);
                    tone.setWidth(100);
                    tone.setHeight(70);
                    encabezadoIni.addView(tone);

                    encabezadoIni.setBackgroundResource(R.drawable.shape_table_total);
                    certDesglozadaHolder.tableLayoutCerDesg.addView(encabezadoIni);
                }

            }
            return;
        }else {
            Log.e("I es menor que 1 " , "CUIDADO ");
            return;
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void onClick(View v) {

    }

    public class CertDesglozadaHolder extends RecyclerView.ViewHolder {
        TableLayout tableLayoutCerDesg;

        public CertDesglozadaHolder(View fragmentCert) {
            super(fragmentCert);
            tableLayoutCerDesg = fragmentCert.findViewById(R.id.tableLayoutPackingList);
            tableLayoutCerDesg.removeAllViews();

        }
    }
}
