#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#define SSIDWIFI "dlink 1503"
#define PASSWORD "sarkar@6625!#"
#define PORT 1883
#define USER "aviksarkar15@gmail.com"
#define PASS "623f9bae"

char SUBSCRIBE_TOPIC1[] = "/aviksarkar15@gmail.com/ringBell/ring";
char PUBLISH_TOPIC1[] = "/aviksarkar15@gmail.com/ringBell/status";
char SUBSCRIBE_TOPIC2[] = "/aviksarkar15@gmail.com/light/switch";
char PUBLISH_TOPIC2[] = "/aviksarkar15@gmail.com/light/status";

const char SERVER[] = "mqtt.dioty.co";

WiFiClient espClient;
PubSubClient client(espClient);

void callback(char topic[], byte payload[], unsigned int length) {
  //Serial.print("Message arrived [");
  //Serial.print(topic);
  //Serial.print("] ");
  char receivedMsg[10]; 
  int i; 
  //Serial.print(receivedChar);
  for(i = 0; i < length; i++){
    receivedMsg[i] = (char)payload[i]; 
    //Serial.print(receivedMsg);
  }
  receivedMsg[i] = '\0';
   //Serial.print(receivedMsg);
  if (strcmp(receivedMsg,"0") == 0) {
    Serial.write(0);
    delay(10);
    publishMsgs("0",PUBLISH_TOPIC1);
  }

  else if (strcmp(receivedMsg,"1") == 0) {
    Serial.write(1);
    delay(10);
    publishMsgs("1",PUBLISH_TOPIC1);
  }

  else if (strcmp(receivedMsg,"011") == 0) {
    Serial.write(011);
    delay(10);
    publishMsgs("011",PUBLISH_TOPIC2);
  }

  else if (strcmp(receivedMsg,"010") == 0) {
    Serial.write(010);
    delay(10);
    publishMsgs("010",PUBLISH_TOPIC2);
  }

  else if (strcmp(receivedMsg,"111") == 0) {

    Serial.write(111);
    delay(10);
    publishMsgs("111",PUBLISH_TOPIC2);

  }

  else if (strcmp(receivedMsg,"110") == 0) {
    Serial.write(110);
    delay(10);
    publishMsgs("110",PUBLISH_TOPIC2);
  }

  else if (strcmp(receivedMsg,"121") == 0) {
    Serial.write(121);
    delay(10);
    publishMsgs("121",PUBLISH_TOPIC2);
  }

  else {
    Serial.write(120);
    delay(10);
    publishMsgs("120",PUBLISH_TOPIC2);
  }

}

void publishMsgs(String payload,char topic[]) {
  char buf[5];
  payload.toCharArray(buf, 5);
  client.publish(topic, buf, true);
}

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    //Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("ESP8266 Client", USER, PASS)) {
      //Serial.println("connected");
      // ... and subscribe to topic
      client.subscribe(SUBSCRIBE_TOPIC1, 1);
      client.subscribe(SUBSCRIBE_TOPIC2, 1);
    } else {
      //Serial.print("failed, rc=");
      //Serial.print(client.state());
      // Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

void printWifiStatus()
{
  // print the SSID of the network you're attached to
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());
}
void setup()
{
  // initialize Serial for debugging
  Serial.begin(9600);
  delay(10);
  // check for the presence of the shield
  if (WiFi.status() == WL_NO_SHIELD) {
    //Serial.println("WiFi shield not present");
    // don't continue
    while (true);
  }

  //Serial.println();
  //Serial.println();
  //Serial.print("Connecting to ");
  //Serial.println(SSIDWIFI);
  WiFi.mode(WIFI_STA);
  WiFi.begin(SSIDWIFI, PASSWORD);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    //Serial.print(".");
  }
  //Serial.println("");
  //Serial.println("WiFi connected");
  //printWifiStatus();
  client.setServer(SERVER, PORT);
  client.setCallback(callback);
}


void loop()
{
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
  //delay(1000);
}
