package com.example.balva.proxecto2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnMapClickListener {
    private static final int TESOURO_REQUEST_CODE = 1;
    private static final int LOCATION_REQUEST_CODE = 1;

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private LatLng centro = new LatLng(42.237023, -8.717944);
    private final LatLng cmarca = new LatLng(42.237558, -8.717285);
    private Location miubicacion;
    Location marcaUbicacion =new Location("mi marca");

    int radio = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //marcaUbicacion.setLatitude(cmarca.latitude);
        //marcaUbicacion.setLongitude(cmarca.longitude);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);


        /*CircleOptions circuloCaracteristicas = new CircleOptions()
                .center(centro)
                .radius(radio)
                .strokeColor(Color.parseColor("#0D47A1"))
                .strokeWidth(4)
                .fillColor(Color.argb(32, 33, 150, 243));*/

       // mMap.addCircle(circuloCaracteristicas).setVisible(true);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centro, 17));


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostrar diálogo explicativo
            } else {
                // Solicitar permiso
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            // ¿Permisos asignados?
            if (permissions.length > 0 &&
                    permissions[0].equals(android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Error de permisos", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onMapClick(LatLng miclick) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        miubicacion = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        float distanciaPremio = marcaUbicacion.distanceTo(miubicacion);



        if (distanciaPremio <= 20) {
            Toast.makeText(this, "Distancia ao premio :" + ((int) distanciaPremio) + "  metros", Toast.LENGTH_LONG).show();
            CircleOptions marca = new CircleOptions()
                    .center(cmarca)
                    .radius(20)
                    .strokeColor(Color.parseColor("#FF4000"))
                    .strokeWidth(4)
                    .fillColor(Color.argb(32, 33, 150, 243));
            mMap.addCircle(marca).setVisible(true);
            Toast.makeText(this, "Distancia ao premio :" +((int) distanciaPremio) + "  metros", Toast.LENGTH_LONG).show();

        }else if(distanciaPremio <10){
            CircleOptions marca = new CircleOptions()
                    .center(cmarca)
                    .radius(20)
                    .strokeColor(Color.parseColor("#FF4000"))
                    .strokeWidth(4)
                    .fillColor(Color.argb(32, 33, 150, 243));
            mMap.addCircle(marca).setVisible(true);
            Toast.makeText(this, "Estas a menos de 10 metros", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Distancia ao premio :" + ((int) distanciaPremio) + "  metros", Toast.LENGTH_LONG).show();


    }}



    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    void lector(View b){
        Intent intent = new Intent(getBaseContext(), ScannerActivity.class);

        startActivityForResult(intent, TESOURO_REQUEST_CODE);
    }
    @Override
    protected  void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == TESOURO_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                String resultado=data.getStringExtra("tesouro");
                Toast.makeText(getBaseContext(),resultado,Toast.LENGTH_LONG).show();

            }
        }
    }
    void originarMarca
}

