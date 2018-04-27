package com.example.aizat.smartcar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by aizat on 4/20/18.
 */

public class OpenFragment extends Fragment {
    private static final String TAG = "Open Fragment";
    private Switch SwitchStateBtn;
    private Button oilBag,baggageBtn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.open_fragment,container,false);
        SwitchStateBtn = (Switch) view.findViewById(R.id.lockState);
        SwitchStateBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    if (b) {
                        showToast("Car is locked.");
                        BluetoothActivity.outputStream.write("L".getBytes());
                    } else {
                        showToast("Car is unlocked.");
                        BluetoothActivity.outputStream.write("l".getBytes());
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        oilBag = view.findViewById(R.id.oilBagBtn);
        oilBag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    showToast("Oil Bag is opened.");
                    BluetoothActivity.outputStream.write("O".getBytes());
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        baggageBtn = view.findViewById(R.id.baggageBtn);
        baggageBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    showToast("Baggage is opened.");
                    BluetoothActivity.outputStream.write("B".getBytes());
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
