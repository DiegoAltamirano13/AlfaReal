package com.diego.lina.sistemadealmacenes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.animation.AnimationUtils;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.diego.lina.sistemadealmacenes.ClassCanvas.ClassCanvas;
import com.diego.lina.sistemadealmacenes.ClassCanvas.ClassConection;
import com.diego.lina.sistemadealmacenes.Entidades.inventario_fisico;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FirmaEnFragment extends Fragment {

    private ClassCanvas canvas;
    Button btn_aceptar, btn_limpiar ;
    Bitmap bitmaps;


    public FirmaEnFragment() {

    }


    public static FirmaEnFragment newInstance(String param1, String param2) {
        FirmaEnFragment fragment = new FirmaEnFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmento = inflater.inflate(R.layout.fragment_firma_en, container, false);
        btn_aceptar = fragmento.findViewById(R.id.firma_aceptar);
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              canvas.Guardar();
                ImportFragment rp = new ImportFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.contenedor, rp);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        btn_limpiar = fragmento.findViewById(R.id.Limpiar);
        btn_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.Limpiar();
            }
        });
        canvas = fragmento.findViewById(R.id.view);
        return fragmento;
    }

    //public void Limpiar(View view) {
      //  canvas.Limpiar();
    //}
    //public void Guardar2(View view){
      //  canvas.Guardar();
    //}
}
