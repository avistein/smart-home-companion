package com.avistein.smarthomecompanion;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;


/**
 * A simple {@link Fragment} subclass.
 */
public class RingBellFragment extends Fragment implements View.OnClickListener {
    private MqttAndroidClient mqttAndroidClient = new MqttAndroidClient(getContext(),MqttClientConstants.MQTT_BROKER_URL,MqttClientConstants.CLIENT_ID2);
    @Override
    public void onResume() {
        super.onResume();
        try {
            mqttAndroidClient.connect(MqttConnectionClient.getMqttConnectionOption(),getContext(),null);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    Button ringOn, ringOff;
    TextView serverStatus, ringStatus;
    ProgressBar spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ring_bell, container, false);
        ringOn = view.findViewById(R.id.ringon);
        ringOff = view.findViewById(R.id.ringoff);
        ringStatus = view.findViewById(R.id.ringStatus);
        serverStatus = view.findViewById(R.id.serverStatus);
        spinner = view.findViewById(R.id.progressBar);
        ringOn.setOnClickListener(this);
        ringOff.setOnClickListener(this);
        ringOn.setEnabled(false);
        ringOff.setEnabled(false);
        mqttAndroidClient.setCallback(new MqttMessages());
        return view;
    }


    @Override
    public void onClick(View view) {
        spinner.setVisibility(View.VISIBLE);
        String msg;
        if (view.getId() == R.id.ringon) {
            msg = "1";
        }
        else {
            msg = "0";
        }
        ringOn.setEnabled(false);
        ringOff.setEnabled(false);
        try {
            MqttConnectionClient.publishMessage(mqttAndroidClient, msg, 1, MqttClientConstants.PUBLISH_TOPIC1,true);
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private class MqttMessages implements MqttCallbackExtended {
        @Override
        public void connectComplete(boolean b, String s) {
            spinner.setVisibility(View.INVISIBLE);
            try {
                MqttConnectionClient.subscribeToTopic(mqttAndroidClient,MqttClientConstants.RECEIVE_TOPIC1,1);
            } catch (MqttException e) {
                e.printStackTrace();
            }
            if(b)
                Toast.makeText(getContext(), "Re-Connected", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Connected", Toast.LENGTH_SHORT).show();
            serverStatus.setText(R.string.serverUp);
            serverStatus.setTextColor(getResources().getColor(R.color.colorGreen));
        }

        @Override
        public void connectionLost(Throwable throwable) {
            Toast.makeText(getContext(),"Connection to server lost!", Toast.LENGTH_LONG).show();
            spinner.setVisibility(View.VISIBLE);
            serverStatus.setText(getText(R.string.serverDown));
            serverStatus.setTextColor(getResources().getColor(R.color.colorRed));
            ringStatus.setText(getText(R.string.ringStatusUnknown));
            ringStatus.setTextColor(getResources().getColor(R.color.colorRed));
            ringOn.setEnabled(false);
            ringOff.setEnabled(false);
        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            spinner.setVisibility(View.INVISIBLE);
            Log.i("mqtt","arrived");
            if ((new String(mqttMessage.getPayload())).equals("1")) {
                Log.i("mqtt", "ring");
                ringStatus.setText(R.string.ringStatusRinging);
                ringStatus.setTextColor(getResources().getColor(R.color.colorGreen));
                ringOn.setEnabled(false);
                ringOff.setEnabled(true);
            } else {
                Log.i("mqtt", "notRing");
                ringStatus.setText(R.string.ringStatusNotRinging);
                ringStatus.setTextColor(getResources().getColor(R.color.colorRed));
                ringOn.setEnabled(true);
                ringOff.setEnabled(false);
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        }
    }
}

