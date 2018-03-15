package com.example.adminibm.speeddetector;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Context context;
    LocationManager locManager;
    LocationListener li;
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                double lat = location.getLatitude();
                double longitude = location.getLongitude();
                double speed= (location.getSpeed())+100; //converting speed from mps to kmph
                Toast.makeText(MainActivity.this,
                        "Latitude is: " + lat + " \n Longitude is: " + longitude+ " \n Speed is= "+speed+" km/h",
                        Toast.LENGTH_LONG).show();
                int flag=0;
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if(speed>=60)
                {

                    //long[] pattern = {0, 100, 1000};

                    v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
                    Toast.makeText(MainActivity.this,"You are over-speeding! Slow down.",Toast.LENGTH_LONG).show();
                    flag=1;
                }
                if(flag==1)
                {
                    double SpeedLat = location.getLatitude();
                    double SpeedLongitude = location.getLongitude();
                    double overSpeed=(location.getSpeed())-50;
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date(location.getTime());
                    String formatted = dateFormat.format(date);
                    Toast.makeText(MainActivity.this,
                            "You crossed speed limit of 60km/h at- "+
                                    "\n Latitude: "+SpeedLat+" \n Longitude: "+SpeedLongitude+
                                    "\n at Speed of "+overSpeed+" km/h \n on date and time: "+formatted,
                            Toast.LENGTH_LONG).show();
                    if(overSpeed<60)
                    {
                        v.cancel();
                        flag=0;
                        //Toast.makeText(MainActivity.this,"Speed maintained at "+overSpeed,Toast.LENGTH_LONG).show();
                    }
                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }
}
