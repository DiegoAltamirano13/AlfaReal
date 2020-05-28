package com.diego.lina.sistemadealmacenes;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.diego.lina.sistemadealmacenes.R;
import com.google.firebase.FirebaseApp;

public class Principal_Inicio extends Fragment {
    ImageView imageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmento =  inflater.inflate(R.layout.principal_pagina, container, false);
        imageView = fragmento.findViewById(R.id.imagen_argo);

        /*FirebaseApp fbbApp;
        fbbApp = FirebaseApp.getInstance();
        Log.e("Aviso", fbbApp.getOptions().getApplicationId());
            */
        return fragmento;
    }
}
