package com.troyanos.airelibre;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle;

import org.json.JSONException;

import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    FusedLocationProviderClient cliente;
    Button encontrarme;
    private GoogleMap mMap;

    //DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        encontrarme = (Button) findViewById(R.id.baseline_button);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        cliente = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        /*databaseHelper = new DatabaseHelper(this, "espacios.db",1);
        try {
            databaseHelper.CheckDatabase();
        } catch (Exception e) {
        }
        try {
databaseHelper.OpenDatabase();
        } catch (Exception e) {
        }*/

        encontrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    getCurrentLocation();
                }});
        }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = cliente.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());

                            MarkerOptions opciones = new MarkerOptions().position(latLng)
                                    .title("Estoy acá");

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 80));
                            googleMap.addMarker(opciones);


                        }

                    });

                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String [] permissions,@NonNull int [] grantResults) {
        if (requestCode == 44){
            if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }};

    @Override
    public void onMapReady (GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng montevideo = new LatLng(-34.8977, -56.1644);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(montevideo, 13));
        mMap.setMinZoomPreference(10f);
        mMap.setMaxZoomPreference(20f);

        GeoJsonLineStringStyle lineStringStyle = new GeoJsonLineStringStyle();
        lineStringStyle.setColor(Color.RED);
        lineStringStyle.setWidth(6);

        GeoJsonLayer plazas = null;
        try {
            plazas = new GeoJsonLayer(mMap, R.raw.plazas, getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        plazas.addLayerToMap();

        GeoJsonLayer bicicircuitos = null;
        try {
            bicicircuitos = new GeoJsonLayer(mMap, R.raw.bicicircuitos, getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bicicircuitos.addLayerToMap();

        plazas.getDefaultPointStyle();
        bicicircuitos.getDefaultLineStringStyle();

        /*
        // Colorear ciclovia según tipo
        for (GeoJsonFeature bicicircuito : bicicircuitos.getFeatures()) {
            if (bicicircuito.hasProperty("TIPO")) {
                String tipo = bicicircuito.getProperty("TIPO");
                if (tipo == "1"){

                }
            }
        }*/
    }
}