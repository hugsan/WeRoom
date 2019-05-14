package com.itcom202.weroom.account.onboarding.views.widget;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.itcom202.weroom.R;

import java.util.Objects;

/**
 * Fragment that display a Google Map V2.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng mInitialPosition;

    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        View v = inflater.inflate( R.layout.map_fragment, container, false );

        // Gets the MapView from the XML layout and creates it
        mapView = v.findViewById( R.id.mapview );
        mapView.onCreate( savedInstanceState );

        fusedLocationClient = LocationServices.getFusedLocationProviderClient( Objects.requireNonNull( getActivity( ) ) );
        if ( ContextCompat.checkSelfPermission( Objects.requireNonNull( getContext( ) ), Manifest.permission.WRITE_CALENDAR )
                == PackageManager.PERMISSION_GRANTED ) {
            // Permission is not granted
            fusedLocationClient.getLastLocation( )
                    .addOnSuccessListener( getActivity( ), new OnSuccessListener<Location>( ) {
                        @Override
                        public void onSuccess( Location location ) {
                            // Got last known location. In some rare situations this can be null.
                            // Logic to handle location object
                        }
                    } );
        } else {
            Log.d( "MapFragment", "Permissio not granted" );
        }

        mapView.getMapAsync( this );


        return v;
    }

    @Override
    public void onMapReady( GoogleMap googleMap ) {
        map = googleMap;
        //FIXME remove the magnifing glass from the map. It also crashes when you click on it.
        map.getUiSettings( ).setMyLocationButtonEnabled( false );
        map.getUiSettings( ).setMapToolbarEnabled( false );
        map.getUiSettings( ).setZoomControlsEnabled( true );
        map.getUiSettings( ).setAllGesturesEnabled( false );
        map.getUiSettings( ).setCompassEnabled( false );
        map.getUiSettings( ).setIndoorLevelPickerEnabled( false );
        map.getUiSettings( ).setZoomControlsEnabled( true );

        if ( mInitialPosition != null ) {
            updateSite( mInitialPosition );
        }
    }

    /**
     * Initialize the pinned position of the map with the given LatLng.
     *
     * @param position Position in LatLng class.
     */
    public void initializeSite( LatLng position ) {
        mInitialPosition = position;
    }

    /**
     * Update the pinned position of the map, and recenter the fragment to the pinner location.
     *
     * @param position LatLang object to create the pin.
     */
    public void updateSite( LatLng position ) {
        map.animateCamera( CameraUpdateFactory.newLatLngZoom( new LatLng( position.latitude, position.longitude ), 15 ) );
        map.moveCamera( CameraUpdateFactory.newLatLng( position ) );
        map.moveCamera( CameraUpdateFactory.newLatLngZoom( position, 15 ) );
        map.addMarker( new MarkerOptions( ).position( new LatLng( position.latitude, position.longitude ) ).title( "Marker" ) );
    }

    @Override
    public void onResume( ) {
        mapView.onResume( );
        super.onResume( );
    }


    @Override
    public void onPause( ) {
        super.onPause( );
        mapView.onPause( );
    }

    @Override
    public void onDestroy( ) {
        super.onDestroy( );
        mapView.onDestroy( );
    }

    @Override
    public void onLowMemory( ) {
        super.onLowMemory( );
        mapView.onLowMemory( );
    }


}
