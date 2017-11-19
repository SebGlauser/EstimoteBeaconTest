package com.example.seg.iotapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private static final String ROOM_1_IDENTIFIER = "Room 1";
    private static final String ROOM_1_UUID = "b9407f30-f5f8-466e-aff9-25556b57fe6d";
    private static final String ROOM_2_IDENTIFIER = "Room 2";
    private static final String ROOM_2_UUID = "b9407f30-f5f8-466e-aff9-25556b57fe6d";

    Button button_room_2, button_room_1;

    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_room_1 = (Button) findViewById(R.id.button_room_1);
        button_room_2 = (Button) findViewById(R.id.button_room_2);

        button_room_1.setEnabled(false);
        button_room_2.setEnabled(false);

        beaconManager = new BeaconManager(getApplicationContext());

        //@todo slow down the scan period to decrease the battery consumption
        beaconManager.setBackgroundScanPeriod(2000L,3000l);
        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {

            @Override
            public void onEnteredRegion(BeaconRegion beaconRegion, List< Beacon > beacons) {
                if (beaconRegion.getIdentifier() == ROOM_1_IDENTIFIER){
                    onRoomEntered(1);
                } else if (beaconRegion.getIdentifier() == ROOM_2_IDENTIFIER){
                    onRoomEntered(2);
                }
            }

            @Override
            public void onExitedRegion(BeaconRegion beaconRegion) {
                if (beaconRegion.getIdentifier() == ROOM_1_IDENTIFIER){
                    onRoomQuited(1);
                } else if (beaconRegion.getIdentifier() == ROOM_2_IDENTIFIER){
                    onRoomQuited(2);
                }
            }

        });


        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new BeaconRegion(ROOM_1_IDENTIFIER,
                        UUID.fromString(ROOM_1_UUID), 64584, 39915));


                beaconManager.startMonitoring(new BeaconRegion(ROOM_2_IDENTIFIER,
                        UUID.fromString(ROOM_2_UUID), 64584, 35823));


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onRoomEntered(int roomIndex) {
        if( roomIndex == 1){
            button_room_1.setEnabled(true);
        }else if (roomIndex == 2){
            button_room_2.setEnabled(true);
        }
    }

    public void onRoomQuited(int roomIndex) {
        if( roomIndex == 1){
            button_room_1.setEnabled(false);
        }else if (roomIndex == 2){
            button_room_2.setEnabled(false);
        }
    }
}
