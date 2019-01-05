package com.example.shivam.knowyourlocation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView lat, lon, alt, acc, add;

    public void knowLocation(View view) {

        if (Build.VERSION.SDK_INT > 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnown = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (lastKnown != null) {
                    updateLocation(lastKnown);
                }
            }
        }
    }

    public void updateLocation(Location location) {
        lat = findViewById(R.id.lat);
        lon = findViewById(R.id.lon);
        alt = findViewById(R.id.alt);
        acc = findViewById(R.id.acc);
        add = findViewById(R.id.add);

        lat.setText( "Latitude: "+ String.format("%.2f", location.getLatitude()));
        lon.setText( "  Longitude: "+String.format("%.2f",location.getLongitude()));
        alt.setText( "   Altitude: "+String.valueOf(location.getAltitude()));
        acc.setText( "  Accuracy: "+String.format("%.2f",location.getAccuracy()));

        String address = "Could not find address.";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addressList != null && addressList.size() > 0) {
                address = "Address: \n";
                Log.i("add", addressList.get(0).getCountryName()+addressList.get(0).getAdminArea());

                if (addressList.get(0).getThoroughfare() != null) {
                    address += addressList.get(0).getThoroughfare()+"\n";
                }
                if (addressList.get(0).getLocality() != null) {
                    address += addressList.get(0).getLocality()+"\n";
                }
                if (addressList.get(0).getAdminArea() != null) {
                    address += addressList.get(0).getAdminArea()+"\n";
                }
                if (addressList.get(0).getCountryName() != null) {
                    address += addressList.get(0).getCountryName()+"\n";
                }
                if (addressList.get(0).getPostalCode() != null) {
                    address += addressList.get(0).getPostalCode();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("g", address);
        }
        add.setAlpha(1);
        add.setText(address);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("location: ", location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }
}
