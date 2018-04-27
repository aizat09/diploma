package com.example.aizat.smartcar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by aizat on 4/20/18.
 */

public class LightsFragment extends Fragment {
    private static final String TAG = "Light Fragment";
    private ToggleButton LightStateBtn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lights_fragment,container,false);
        LightStateBtn = (ToggleButton) view.findViewById(R.id.HeadLampStateToggle);
        LightStateBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    if (b) {
                        showToast("Head Lamps are on.");
                        BluetoothActivity.outputStream.write("H".getBytes());
                    } else {
                        showToast("Head Lamps are off.");
                        BluetoothActivity.outputStream.write("h".getBytes());
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        LightStateBtn = (ToggleButton) view.findViewById(R.id.FogLampsStateToggle);
        LightStateBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    if (b) {
                        showToast("Fog Lamps are on.");
                        BluetoothActivity.outputStream.write("G".getBytes());
                    } else {
                        showToast("Fog Lamps are off.");
                        BluetoothActivity.outputStream.write("g".getBytes());
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        LightStateBtn = (ToggleButton) view.findViewById(R.id.LeftLampStateToggle);
        LightStateBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    if (b) {
                        showToast("Left Lamp is on.");
                        BluetoothActivity.outputStream.write("T".getBytes());
                    } else {
                        showToast("Left Lamp is off.");
                        BluetoothActivity.outputStream.write("t".getBytes());
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        LightStateBtn = (ToggleButton) view.findViewById(R.id.RightLampStateToggle);
        LightStateBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    if (b) {
                        showToast("Right Lamp is on.");
                        BluetoothActivity.outputStream.write("R".getBytes());
                    } else {
                        showToast("Right Lamp is off.");
                        BluetoothActivity.outputStream.write("r".getBytes());
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        LightStateBtn = (ToggleButton) view.findViewById(R.id.StopLampStateToggle);
        LightStateBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    if (b) {
                        showToast("Stop Lamp is on.");
                        BluetoothActivity.outputStream.write("P".getBytes());
                    } else {
                        showToast("Stop Lamp is off.");
                        BluetoothActivity.outputStream.write("p".getBytes());
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        return view;
    }
    private void showToast(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
