package com.samplemap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.DrawStatus;
import com.esri.arcgisruntime.mapping.view.DrawStatusChangedEvent;
import com.esri.arcgisruntime.mapping.view.DrawStatusChangedListener;
import com.esri.arcgisruntime.mapping.view.MapView;

public class MainActivity extends AppCompatActivity implements DrawStatusChangedListener {
    
    private MapView mapView;
    private Button btnOpen;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        btnOpen = findViewById(R.id.btn_open);
        btnOpen.setEnabled(false);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startActivity(new Intent(MainActivity.this, NewActivity.class));
            }
        });
    
    
        mapView = findViewById(R.id.mapView);
        
        ArcGISMap map = new ArcGISMap(Basemap.createNationalGeographic());
        mapView.setMap(map);
        
        mapView.addDrawStatusChangedListener(this);
        
    }
    
    private void zoomMap () {
        // Zoom to particular position
        SpatialReference spatialReferences = SpatialReference.create(3857);
        Point centerPoint = new Point(-12467731.269351, 4959005.266259, spatialReferences);
        Viewpoint viewpoint = new Viewpoint(centerPoint, 100000);
        
        mapView.setViewpointAsync(viewpoint);
    }
    
    @Override
    protected void onPause () {
        super.onPause();
        mapView.pause();
    }
    
    @Override
    protected void onResume () {
        super.onResume();
        mapView.resume();
    }
    
    @Override
    public void drawStatusChanged (DrawStatusChangedEvent drawStatusChangedEvent) {
        if (drawStatusChangedEvent.getDrawStatus() == DrawStatus.COMPLETED) {
            mapView.removeDrawStatusChangedListener(this);
    
            btnOpen.setEnabled(true);
            
            // Zoom the map to a ViewPoint
            zoomMap();
        }
    }
}
