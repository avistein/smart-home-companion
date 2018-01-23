package com.avistein.smarthomecompanion;

import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

/**
 * Created by avistein on 29/12/17.
 */

class MqttConnectionClient {

   static MqttConnectOptions getMqttConnectionOption() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(MqttClientConstants.USERNAME);
        mqttConnectOptions.setPassword(MqttClientConstants.PASSWORD.toCharArray());
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setAutomaticReconnect(true);
        return mqttConnectOptions;
    }

    static void publishMessage(MqttAndroidClient client, String msg, int qos, String topic,boolean retained)
            throws MqttException, UnsupportedEncodingException {
        byte[] encodedPayload;
        encodedPayload = msg.getBytes("UTF-8");
        MqttMessage message = new MqttMessage(encodedPayload);
        message.setId(320);
        message.setRetained(retained);
        message.setQos(qos);
        client.publish(topic, message);
    }
    static void subscribeToTopic(MqttAndroidClient client, final String topic, int qos) throws MqttException {
        IMqttToken token = client.subscribe(topic, qos);
        Log.i("mqtt", String.valueOf(token));
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d("pahoClient", "Subscribe Successfully " + topic);
            }

            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.e("pahoClient", "Subscribe Failed " + topic);

            }
        });
    }
}
