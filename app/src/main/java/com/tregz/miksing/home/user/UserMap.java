package com.tregz.miksing.home.user;

import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.tregz.miksing.R;

import java.io.IOException;
import java.util.List;

public class UserMap extends SupportMapFragment implements OnMapReadyCallback,
        OnSuccessListener<Location> {
    private String TAG = UserMap.class.getSimpleName();

    private Geocoder geocoder;
    private GoogleMap map;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.getMapAsync(this);
        geocoder = new Geocoder(getContext());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setPadding(0, 300, 0, 0);
        UiSettings settings = map.getUiSettings();
        settings.setMapToolbarEnabled(false);
        settings.setCompassEnabled(false);
        settings.setRotateGesturesEnabled(true);
        settings.setScrollGesturesEnabled(true);
        settings.setTiltGesturesEnabled(true);
        settings.setZoomControlsEnabled(false);
        settings.setZoomGesturesEnabled(true);
        if (getContext() != null) try {
            MapStyleOptions mso = MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.gmaps);
            if (!map.setMapStyle(mso)) Log.e(TAG, "Style parsing failed.");
        } catch (Resources.NotFoundException e) {
            if (e.getMessage() != null) Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    @Override
    public void onSuccess(Location location) {
        if (location != null)
            markLocation(null, location.getLatitude(), location.getLongitude());
    }

    void fromLastLocation() {
        if (getActivity() != null) try {
            FusedLocationProviderClient client;
            client = LocationServices.getFusedLocationProviderClient(getActivity());
            client.getLastLocation().addOnSuccessListener(getActivity(), this);
        } catch (SecurityException e) {
            if (e.getMessage() != null) Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    void fromLocationName(String area) {
        double lat = 62.0;
        double lng = 105.0;
        try {
            List<Address> addresses = geocoder.getFromLocationName(area, 5);
            if (addresses != null && !addresses.isEmpty()) {
                lat = addresses.get(0).getLatitude();
                lng = addresses.get(0).getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        markLocation(area, lat, lng);
    }

    private void markLocation(String area, double lat, double lng) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(lat, lng));
        if (area != null) marker.title(area);
        map.addMarker(marker);
        CameraPosition.Builder camera = new CameraPosition.Builder();
        camera.target(new LatLng(lat + 0.03, lng));
        camera.tilt(30f);
        camera.zoom(10f);
        map.animateCamera(CameraUpdateFactory.newCameraPosition(camera.build()));
    }
}
