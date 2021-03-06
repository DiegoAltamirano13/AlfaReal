package com.diego.lina.sistemadealmacenes;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diego.lina.sistemadealmacenes.Adaptador.ExpandListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class principal_pagina_menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView nombre;
    List<String> listDataHeader;
    HashMap<String, List<String>>  listDataChild;
    ExpandableListView expListView;

    //Pruebas
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_pagina_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /*Invocar listview expandible*/
        enableExpandableList();
        expListView.setGroupIndicator(null);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Poner nombre de usuario
        View navHeader = navigationView.getHeaderView(0);
        nombre = navHeader.findViewById(R.id.as_usr_nombre2);
        //= findViewById(R.id.as_usr_nombre2);
        Intent intent = getIntent();
        SharedPreferences preferences = principal_pagina_menu.this.getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
        String as_usr_nombre = preferences.getString("as_nombre", "No Cliente");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor, new Principal_Inicio()).commit();

        nombre.setText(as_usr_nombre);

    }


    @SuppressLint("ResourceType")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_camera) {
            // Handle the camera action
            //fragmentManager.beginTransaction().replace(R.id.contenedor, new ImportFragment()).commit();
            ImportFragment importFragment = new ImportFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.contenedor, importFragment);
            transaction.commit();
        } else if (id == R.id.nav_gallery) {
            //fragmentManager.beginTransaction().replace(R.id.contenedor, new ReportesFragment()).commit();
            Reportes_Carga_Descarga reportesFragment = new Reportes_Carga_Descarga();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            transaction.addToBackStack(null);
            //transaction.replace(R.id.contenedor, reportesFragment);
            transaction.replace(R.id.contenedor, reportesFragment);
            transaction.commit();
        } else if (id == R.id.nav_gallery2) {
            //fragmentManager.beginTransaction().replace(R.id.contenedor, new Reportes2Fragment()).commit();
            Reportes2Fragment reportes2Fragment = new Reportes2Fragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            transaction.addToBackStack(null);
            transaction.replace(R.id.contenedor, reportes2Fragment);
            transaction.commit();
            //fragmentManager.beginTransaction().replace(R.id.contenedor, new Reportes2Fragment()).commit();
        }
        else if (id == R.id.nav_log){
            SharedPreferences preferences = getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(this).
                    setIcon(R.drawable.warning).
                    setTitle("Salir").
                    setMessage("¿Quiere salir de la aplicacion").
                    setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences preferences = getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.commit();
                            finish();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*Lista expandible*/
    private void enableExpandableList() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        expListView = (ExpandableListView) findViewById(R.id.left_drawer);

        prepareListData(listDataHeader, listDataChild);
        ExpandListAdapter listAdapter = new ExpandListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (listDataHeader.get(groupPosition).equals("Cerrar sessión")){
                    SharedPreferences preferences = getSharedPreferences("as_usr_nombre", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
                    finish();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();*/

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                // Temporary code:
                System.out.println(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
                if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Crear Solicitud")){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contenedor, new ImportFragment()).commit();
                }else if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("En proceso")){
                    Reportes_Carga_Descarga reportesFragment = new Reportes_Carga_Descarga();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    transaction.addToBackStack(null);
                    //transaction.replace(R.id.contenedor, reportesFragment);
                    transaction.replace(R.id.contenedor, reportesFragment);
                    transaction.commit();
                }else if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Programados")){
                    VehiculosProgramados reportesFragment = new VehiculosProgramados();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    transaction.addToBackStack(null);
                    //transaction.replace(R.id.contenedor, reportesFragment);
                    transaction.replace(R.id.contenedor, reportesFragment);
                    transaction.commit();
                }else if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Historico")){
                    Reportes2Fragment reportes2Fragment = new Reportes2Fragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.contenedor, reportes2Fragment);
                    transaction.commit();
                } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Rep. de Certificación")){
                    Certificacion_reporte_desg certificacion_reporte_desg = new Certificacion_reporte_desg();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.contenedor, certificacion_reporte_desg);
                    transaction.commit();
                }else if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Rep. de Certificación desglosada")){
                    Certificacion_Reporte_Dezglozada certificacion_reporte_dezglozada = new Certificacion_Reporte_Dezglozada();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.contenedor, certificacion_reporte_dezglozada);
                    transaction.commit();
                }else if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Condensado")){
                    InventarioFisico_Reporte inventarioFisico_reporte = new InventarioFisico_Reporte();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.contenedor, inventarioFisico_reporte);
                    transaction.commit();
                }else if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Condensado completo")){
                    InvetarioFisicoCondensado invetarioFisicoCondensado = new InvetarioFisicoCondensado();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.contenedor, invetarioFisicoCondensado);
                    transaction.commit();
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });}


    private void prepareListData(List<String> listDataHeader, Map<String,
            List<String>> listDataChild) {


        // Adding child data
        listDataHeader.add("Vehiculos");
        //listDataHeader.add("Inventarios");
        listDataHeader.add("Certificados");
        /*listDataHeader.add("Comercio Exterior");
        listDataHeader.add("Facturación");*/
        listDataHeader.add("Cerrar sessión");

        // Adding child data
        List<String> vehiculo = new ArrayList<String>();
        vehiculo.add("Crear Solicitud");
        vehiculo.add("En proceso");
        vehiculo.add("Programados");
        vehiculo.add("Historico");

        /*List<String> top = new ArrayList<String>();
        top.add("CD por producto");
        top.add("Producto c/valor");
        top.add("Producto c/valor lote serie");
        top.add("Producto UME");
        top.add("Producto UME y UMC");
        top.add("Condensado");
        top.add("Condensado completo");*/
        /*top.add("Detallado");
        top.add("Detallado completo");
        top.add("Productos por cd");
        top.add("CD's vivos a una fecha");*/

        List<String> mid = new ArrayList<String>();
        mid.add("Rep. de Certificación");
        mid.add("Rep. de Certificación desglosada");

        /*List<String> bottom = new ArrayList<String>();
        bottom.add("Cotización por pedimento");
        bottom.add("Cotización por modelo");*/

        /*List<String> facturacion = new ArrayList<String>();
        facturacion.add("Antiguedad de saldos");*/

        List<String> salir = new ArrayList<String>();

        listDataChild.put(listDataHeader.get(0), vehiculo); // Header, Child data
        //listDataChild.put(listDataHeader.get(1), top);
        listDataChild.put(listDataHeader.get(1), mid);
        /*listDataChild.put(listDataHeader.get(3), bottom);
        listDataChild.put(listDataHeader.get(4), facturacion);*/
        listDataChild.put(listDataHeader.get(2), salir);
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
}
