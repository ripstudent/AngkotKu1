package com.example.yogadarma.angkotku;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.yogadarma.angkotku.POJO.MyLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PenumpangMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Button logout, order;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penumpang_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

//        logout = findViewById(R.id.btnLogout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                firebaseAuth.signOut();
//                Intent intent = new Intent(PenumpangMapsActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//                return;
//            }
//        });
//
//        order = findViewById(R.id.btnOrder);
//        order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                order.setText("Mencari Angkot....");
//
//                getSopirTerdekat();
//            }
//        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));


        // menambahkan koordinat sopir ke fire base
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        MyLocation myLocation = new MyLocation(latitude, longitude, true);

        String userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("Users").child("Penumpang").child(userId).setValue(myLocation);
        //

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
//    private void getSopirTerdekat(){
//        userId = "Rob5UjDF8DMD8KKqydly9NTNPzR2";
//        databaseReference.child("Users").child("Sopir").child(userId).child("latlng").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String LATLNG = dataSnapshot.getValue(String.class);
//
//                String[] split = LATLNG.split("_");
//
//                double latitude;
//                double longitude;
//
//                latitude = Double.parseDouble(split[0]);
//                longitude = Double.parseDouble(split[2]);
//
//                LatLng angkot = new LatLng(latitude, longitude);
//                mMap.addMarker(new MarkerOptions().position(angkot).title("Angkot"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(angkot));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

}
