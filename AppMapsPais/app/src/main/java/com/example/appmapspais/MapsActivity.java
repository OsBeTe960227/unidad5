package com.example.appmapspais;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    public void onUbicar(View view){
        Ubicacion ubicacion= new Ubicacion(this);
        StringBuilder build= ubicacion.getLocation();
        LatLng miUbicacion = ubicacion.getLatLong();
        try {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi Ubicacion"));
            mMap.animateCamera((CameraUpdateFactory.newLatLng(miUbicacion)));
        }catch (Exception e){
            Toast.makeText(this,"Fallo la ubicacion actual",Toast.LENGTH_SHORT).show();
        }


        Toast.makeText(this,build.toString(),Toast.LENGTH_SHORT).show();
    }

    public void onSearch(View view){
        EditText etPais = (EditText) findViewById(R.id.etBuscar);
        String lugar = etPais.getText().toString();
        List<Address> addressList = null;
        if (lugar != null || !lugar.equals("")){
            Geocoder geocoder = new Geocoder(this);
            try{
                addressList = geocoder.getFromLocationName(lugar,1);
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marcador en " +lugar));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            }catch (IOException e){
                e.printStackTrace();
            }


        }else{
            Toast.makeText(MapsActivity.this,"Ingrese su busqueda",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            try {
                //Pedir que activen el permiso de leer contactos y llamadas
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION},0);

            }catch (Exception e){
                Toast.makeText(MapsActivity.this, "Falló la aplicación. Intente de nuevo", Toast.LENGTH_SHORT).show();
            }
            return;
        }else{
            //mMap.setMyLocationEnabled(true);
        }
    }
}
