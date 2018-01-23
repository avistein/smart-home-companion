package com.avistein.smarthomecompanion;

import java.util.UUID;

/**
 * Created by avistein on 24/12/17.
 */

interface MqttClientConstants {

    String MQTT_BROKER_URL = "ur_broker_url";
    String USERNAME = "ur_username";
    String PASSWORD = "ur_password";
    /*msg format : ring ON(on=1)/ring OFF(off=0) ; eg - 1*/
    String PUBLISH_TOPIC1 = "ur_topic";
    /*msg format : mode(light=0/fan=1),equipment no,state(off=0,on=1) ; eg - 011*/
    String PUBLISH_TOPIC2 = "ur_topic";
    /*msg format : ring ON(on=1)/ring OFF(off=0) ; eg - 1*/
    String RECEIVE_TOPIC1 ="ur_topic";
    /*msg format : mode(light=0/fan=1),equipment no,state(off=0,on=1) ; eg - 011*/
    String RECEIVE_TOPIC2 = "ur_topic";
    String CLIENT_ID1 = UUID.randomUUID().toString();
    String CLIENT_ID2 = UUID.randomUUID().toString();

}

