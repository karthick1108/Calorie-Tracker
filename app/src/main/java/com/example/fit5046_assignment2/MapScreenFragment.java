package com.example.fit5046_assignment2;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapScreenFragment extends Fragment implements OnMapReadyCallback {
    MapView mapView;
    GoogleMap mMap;
    private LocationManager locationManager;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    View vMap;
    private String fullAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@NonNull ViewGroup container,@NonNull Bundle savedInstanceState) {
        vMap = inflater.inflate(R.layout.fragment_map, container, false);
        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.frg);

        String address = getArguments().getString("usraddress");
        String postcode = getArguments().getString("usrpostcode");
        Double code = Double.valueOf(postcode);
        Integer usrPostcode = code.intValue();
        String newcode = Integer.toString(usrPostcode);
        fullAddress = address+"-"+newcode;

        System.out.println("Full address is" +" "+fullAddress);
        mapFragment.getMapAsync(this);
        return vMap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap  = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.clear();
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(fullAddress,1);
            Address address = addressList.get(0);
            addMarker(fullAddress,address.getLatitude(),address.getLongitude(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public class GetNearBySearchAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params)  {

            return RestAPIClient.getNearByParks(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(String response) {
            try {
                JSONObject result = new JSONObject(response);
                System.out.println("result"+" "+result);
                JSONArray parks = result.getJSONArray("results");
                System.out.println("Parks"+" "+parks);
                int length = parks.length();

                for(int i=0; i<length; i++) {

                    JSONObject park = parks.getJSONObject(i);
                    JSONObject geometry = park.getJSONObject("geometry");
                    double latitude = geometry.getJSONObject("location").optDouble("lat");
                    double longitude = geometry.getJSONObject("location").optDouble("lng");
                    addMarker(park.optString("name"), latitude, longitude, true);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    void addMarker(String addressName, double lat, double lng, Boolean isPark) {
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.home);
        if(isPark) {
            descriptor =  BitmapDescriptorFactory.fromResource(R.drawable.park);
        }

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat,lng))
                .title(addressName)
                .snippet("Karthick Rajasekaran"))
                .setIcon(descriptor);
        ;
        GetNearBySearchAsync getNearBySearch = new GetNearBySearchAsync();
        getNearBySearch.execute(String.valueOf(lat),String.valueOf(lng));

        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(lat,lng))
                .zoom(15)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null);

    }
}