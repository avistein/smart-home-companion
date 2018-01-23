package com.avistein.smarthomecompanion;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;


/**
 * A simple {@link Fragment} subclass.
 */
public class LightControlsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private MqttAndroidClient  mqttAndroidClient = new MqttAndroidClient(getContext(),MqttClientConstants.MQTT_BROKER_URL,MqttClientConstants.CLIENT_ID2);
    Switch light1,fan1,fan2;
    TextView serverStatus;
    ProgressBar spinner;

    @Override
    public void onResume() {
        super.onResume();
        try {
            mqttAndroidClient.connect(MqttConnectionClient.getMqttConnectionOption(),getContext(),null);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light_controls, container, false);
        light1  = view.findViewById(R.id.light1);
        fan1 = view.findViewById(R.id.fan1);
        fan2 = view.findViewById(R.id.fan2);
        serverStatus = view.findViewById(R.id.serverStatus);
        spinner = view.findViewById(R.id.progressBar);
        light1.setOnCheckedChangeListener(this);
        fan1.setOnCheckedChangeListener(this);
        fan2.setOnCheckedChangeListener(this);
        light1.setEnabled(false);
        fan1.setEnabled(false);
        fan2.setEnabled(false);
        mqttAndroidClient.setCallback(new MqttMessages());
        return view;
    }
    @Override
    public void onCheckedChanged(CompoundButton switchView, boolean b) {
        if(switchView.isPressed()) {
            spinner.setVisibility(View.VISIBLE);
            String msg = "";
            switch (switchView.getId()) {
                case R.id.light1:
                    if (switchView.isChecked()) {
                        msg = "011";
                    } else {
                        msg = "010";
                    }
                    break;
                case R.id.fan1:
                    if (switchView.isChecked()) {
                        msg = "111";
                    } else {
                        msg = "110";
                    }
                    break;
                case R.id.fan2:
                    if (switchView.isChecked()) {
                        msg = "121";
                    } else {
                        msg = "120";
                    }
            }
            switchView.setEnabled(false);
            try {
                MqttConnectionClient.publishMessage(mqttAndroidClient, msg, 1, MqttClientConstants.PUBLISH_TOPIC2,true);
            } catch (MqttException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private class MqttMessages implements MqttCallbackExtended {
        @Override
        public void connectComplete(boolean b, String s) {
            spinner.setVisibility(View.INVISIBLE);
            try {
                MqttConnectionClient.subscribeToTopic(mqttAndroidClient,MqttClientConstants.RECEIVE_TOPIC2,1);
            } catch (MqttException e) {
                e.printStackTrace();
            }
            light1.setEnabled(true);
            /*not yet implemented*/
//            fan1.setEnabled(true);
//            fan2.setEnabled(true);
            serverStatus.setText(R.string.serverUp);
            serverStatus.setTextColor(getResources().getColor(R.color.colorGreen));
        }

        @Override
        public void connectionLost(Throwable throwable) {
            spinner.setVisibility(View.VISIBLE);
            serverStatus.setText(getText(R.string.serverDown));
            serverStatus.setTextColor(getResources().getColor(R.color.colorRed));
            light1.setEnabled(false);
            fan1.setEnabled(false);
            fan2.setEnabled(false);
        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            spinner.setVisibility(View.INVISIBLE);
            String msg  = (new String(mqttMessage.getPayload()));
            switch (msg){
                case "011":
                    light1.setTextColor(getResources().getColor(R.color.colorGreen));
                    light1.setEnabled(true);
                    light1.setChecked(true);
                    break;
                case "010":
                    light1.setTextColor(getResources().getColor(R.color.colorRed));
                    light1.setEnabled(true);
                    light1.setChecked(false);
                    break;
                case "111":
                    fan1.setTextColor(getResources().getColor(R.color.colorGreen));
                    fan1.setEnabled(true);
                    fan1.setChecked(true);
                    break;
                case "110":
                    fan1.setTextColor(getResources().getColor(R.color.colorRed));
                    fan1.setEnabled(true);
                    fan1.setChecked(false);
                    break;
                case "121":
                    fan2.setTextColor(getResources().getColor(R.color.colorGreen));
                    fan2.setEnabled(true);
                    fan2.setChecked(true);
                    break;
                case "120":
                    fan2.setTextColor(getResources().getColor(R.color.colorRed));
                    fan2.setEnabled(true);
                    fan2.setChecked(false);
                    break;
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        }
    }



}
