package com.example.compasapp.repos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.compasapp.R;
import com.example.compasapp.interfaces.LocalTrackInterRepo;
import com.example.compasapp.model.Options;

public class LocalTrackRepo implements LocalTrackInterRepo {

    private LocationManager mLocationManager;
    private Activity mActivity;
    private Options mOptions;

    private Double mLatitude;
    private Double mLongitude;

    private Float mDistanceInMeters;

    private Location mLocationFirst;
    private Location mLocationSecond;

    private Double mDistanceLatitude;
    private Double mDistangeLongitude;

    public LocalTrackRepo(Double mDistanceLatitude, Double mDistangeLongitude){
        this.mDistanceLatitude = mDistanceLatitude;
        this.mDistangeLongitude = mDistangeLongitude;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void getLocation(Context context, Activity activity) {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                2000,
                1, locationLogic(context, activity));
    }

    @Override
    public void deleteLocationTracker(Context context){
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.removeUpdates(locationLogic(context, mActivity));
    }

    public LocationListener locationLogic(final Context context, final Activity activity) {
        LocationListener locationListenerGPS = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                TextView tv = activity.findViewById(R.id.tvHeading);
                RelativeLayout relativeLayout = activity.findViewById(R.id.progressBarContainer);
                ProgressBar progressBar = activity.findViewById(R.id.progressBar);

                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();

                if(mLatitude != null && mLongitude != null) {
                    mOptions = new Options(mLatitude, mLongitude);

                    mLocationFirst = new Location("");
                    mLocationSecond = new Location("");

                    mLocationFirst.setLatitude(mLatitude);
                    mLocationFirst.setLongitude(mLongitude);

                    mLocationSecond.setLatitude(mDistanceLatitude);
                    mLocationSecond.setLongitude(mDistangeLongitude);
                    mDistanceInMeters = mLocationFirst.distanceTo(mLocationSecond);

                    mOptions = new Options(mLatitude, mLongitude, mDistanceInMeters);

                    mLocationFirst.bearingTo(mLocationSecond);

                    progressBar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.GONE);

                    if(mDistanceInMeters != null) {
                        tv.setVisibility(View.VISIBLE);
                        tv.setText("Distance from the destination: " + Math.round(mDistanceInMeters) + "m");
                    }
                }
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
        return locationListenerGPS;
    }
}
