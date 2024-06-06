#include <SoftwareSerial.h>       // Include software serial library
#include <TinyGPS++.h>            // Include TinyGPS++ library
#include <Adafruit_MPU6050.h>
#include <Adafruit_Sensor.h>
#include <Wire.h>
#include <WiFi.h>
#include <HTTPClient.h>

const char* ssid = "VIETTEL";
const char* password = "lamgicomatkhau";

// const char* ssid = "ThinhLe";
// const char* password = "abcxyzqwe";

// const char* ssid = "DOLPHIN WIFI 2.4G";
// const char* password = "Dtvc124@";

//Your Domain name with URL path or IP address with path
String serverNameMPU = "http://113.23.79.192:1234/update-mpu";
String serverNameGPS = "http://113.23.79.192:1234/update-gps";
String serverDebugMPU = "http://192.168.1.3:1880/update-mpu";
String serverDebugGPS = "http://192.168.1.3:1880/update-gps";

// Basic demo for accelerometer readings from Adafruit MPU6050
Adafruit_MPU6050 mpu;

// the following variables are unsigned longs because the time, measured in
// milliseconds, will quickly become a bigger number than can be stored in an int.
unsigned long lastTime = 0;
// Timer set to 10 minutes (600000)
//unsigned long timerDelay = 600000;
// Set timer to 5 seconds (5000)
unsigned long timerDelay = 2000;

#define LED_PIN 2

// Interfacing Arduino with NEO-6M GPS module
#define S_TX 17 // Define software serial TX pin
#define S_RX 16 // Define software serial RX pin

// The TinyGPS++ object
TinyGPSPlus gps;

// The serial connection to the GPS device
SoftwareSerial ss(S_RX, S_TX);

#define WIFI_TIMEOUT_MS 20000

void connectToWiFi() {
  Serial.print("Connecting to WiFi");
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  unsigned long startAttemptTime = millis();
  int i = 0;

  Serial.print("Connecting to WiFi");
  while ((WiFi.status() != WL_CONNECTED) && ((millis() - startAttemptTime) < WIFI_TIMEOUT_MS)) {
    delay(1000);
    if (i%2 == 0) {
      digitalWrite (LED_PIN, HIGH);
    }
    else {
      digitalWrite (LED_PIN, LOW);
    }
    Serial.print(i++);
    Serial.println("s");
  }

  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("Failed!");
  }
  else {
    Serial.println("Connected!");
    Serial.println(WiFi.localIP());
  }
}

void initMPU() {
  if (!mpu.begin()) {
    Serial.println("Failed to find MPU6050 chip");
    while (1) {
      delay(10);
    }
  }
  Serial.println("MPU6050 Found!");

  mpu.setAccelerometerRange(MPU6050_RANGE_8_G);
  Serial.print("Accelerometer range set to: ");
  switch (mpu.getAccelerometerRange()) {
  case MPU6050_RANGE_2_G:
    Serial.println("+-2G");
    break;
  case MPU6050_RANGE_4_G:
    Serial.println("+-4G");
    break;
  case MPU6050_RANGE_8_G:
    Serial.println("+-8G");
    break;
  case MPU6050_RANGE_16_G:
    Serial.println("+-16G");
    break;
  }
  mpu.setGyroRange(MPU6050_RANGE_500_DEG);
  Serial.print("Gyro range set to: ");
  switch (mpu.getGyroRange()) {
  case MPU6050_RANGE_250_DEG:
    Serial.println("+- 250 deg/s");
    break;
  case MPU6050_RANGE_500_DEG:
    Serial.println("+- 500 deg/s");
    break;
  case MPU6050_RANGE_1000_DEG:
    Serial.println("+- 1000 deg/s");
    break;
  case MPU6050_RANGE_2000_DEG:
    Serial.println("+- 2000 deg/s");
    break;
  }

  mpu.setFilterBandwidth(MPU6050_BAND_21_HZ);
  Serial.print("Filter bandwidth set to: ");
  switch (mpu.getFilterBandwidth()) {
  case MPU6050_BAND_260_HZ:
    Serial.println("260 Hz");
    break;
  case MPU6050_BAND_184_HZ:
    Serial.println("184 Hz");
    break;
  case MPU6050_BAND_94_HZ:
    Serial.println("94 Hz");
    break;
  case MPU6050_BAND_44_HZ:
    Serial.println("44 Hz");
    break;
  case MPU6050_BAND_21_HZ:
    Serial.println("21 Hz");
    break;
  case MPU6050_BAND_10_HZ:
    Serial.println("10 Hz");
    break;
  case MPU6050_BAND_5_HZ:
    Serial.println("5 Hz");
    break;
  }

  Serial.println("");
  delay(100);
}

void print_speed() {
  Serial.print("lat: ");
  Serial.println(gps.location.lat(),6);

  Serial.print("lng: ");
  Serial.println(gps.location.lng(),6);

  Serial.print("speed: ");
  Serial.println(gps.speed.kmph());
  
  Serial.print("sat:");
  Serial.println(gps.satellites.value());

  Serial.print("alt:");
  Serial.println(gps.altitude.meters(), 0);

  Serial.print(gps.date.day());
  Serial.print("-");
  Serial.print(gps.date.month());
  Serial.print("-");
  Serial.println(gps.date.year());

  int hour = gps.time.hour();
  if (hour + 7 >= 24) {
    hour = hour - 17;
  }
  else hour = hour + 7;
  Serial.print(hour);
  Serial.print(":");
  Serial.print(gps.time.minute());
  Serial.print(":");
  Serial.println(gps.time.second());
}

void setup() {
  // put your setup code here, to run once:
  pinMode(LED_PIN, OUTPUT);
  Serial.begin(9600);
  ss.begin(9600);

  // Connect to Wi-Fi and Get IP local
  connectToWiFi();

  // Try to initialize!
  initMPU();
}

void loop() {
  // put your main code here, to run repeatedly:
  WiFiClient client;
  HTTPClient http;
  String httpRequestData;
  int httpResponseCode;
  String api_key;
  String sensor;

  //Send an HTTP POST request every 1 second
  
  while (ss.available()) {
    // get the byte data from the GPS
    // byte gpsData = ss.read();
    // Serial.write(gpsData);
    gps.encode(ss.read());
  }

  //Check WiFi connection status
  if(WiFi.status()== WL_CONNECTED){
    digitalWrite (LED_PIN, HIGH);
    if (gps.location.isUpdated() || gps.satellites.isUpdated()){
      // Debug GPS
      print_speed();

      // Data to send with HTTP POST
      api_key = "\"api_key\":\"tPmAT5Ab3j7F9\",";
      sensor = "\"sensor\":\"GPS Neo M8N\",";
      String lat = "\"lat\":\"" + String(gps.location.lat(),6) + "\",";
      String lng = "\"lng\":\"" + String(gps.location.lng(),6) + "\",";
      String speed = "\"speed\":\"" + String(gps.speed.kmph()) + "\",";
      String sat = "\"sat\":\"" + String(gps.satellites.value()) + "\",";
      String alt = "\"alt\":\"" + String(gps.altitude.meters(), 0) + "\",";
      int month = gps.date.month();
      String stringMonth = String(month);
      if (month < 10) {
        stringMonth = "0" + stringMonth;
      }
      int day = gps.date.day();
      String stringDay = String(day);
      if (day < 10) {
        stringDay = "0" + stringDay;
      }
      String date = "\"date\":\"" + String(gps.date.year()) + "-" + stringMonth + "-" + stringDay + "\",";
      int hour = gps.time.hour() + 7;
      String stringHour = String(hour);
      if (hour >= 24) {
        hour = hour - 24;
      }
      if (hour < 10) {
        stringHour = "0" + stringHour;
      }
      int minute = gps.time.minute();
      String stringMinute = String(minute);
      if (minute < 10) {
        stringMinute = "0" + stringMinute;
      }
      int second = gps.time.second();
      String stringSecond = String(second);
      if (second < 10) {
        stringSecond = "0" + stringSecond;
      }
      String time = "\"time\":\"" + stringHour + ":" + stringMinute + ":" + stringSecond + "\"";
      httpRequestData = "{" + api_key + sensor + lat + lng + speed + sat + alt + date + time + "}";

      // ----------------------------------------------------------------------
      // Your Domain name with URL path or IP address with path
      http.begin(client, serverNameGPS);
      
      // Specify content-type header
      http.addHeader("Content-Type", "application/json");
      
      // Send HTTP POST request
      httpResponseCode = http.POST(httpRequestData);

      Serial.println();
      Serial.print("GPS HTTP Response code: ");
      Serial.println(httpResponseCode);

      // Free resources
      http.end();

      // ----------------------------------------------------------------------
      // Your Domain name with URL path or IP address with path
      http.begin(client, serverDebugGPS);
      
      // Specify content-type header
      http.addHeader("Content-Type", "application/json");
      
      // Send HTTP POST request
      httpResponseCode = http.POST(httpRequestData);

      Serial.println();
      Serial.print("GPS Debug HTTP Response code: ");
      Serial.println(httpResponseCode);

      // Free resources
      http.end();
    }

    /* Get new sensor events with the readings */
    sensors_event_t a, g, temp;
    mpu.getEvent(&a, &g, &temp);

    // Data to send with HTTP POST
    api_key = "\"api_key\":\"tPmAT5Ab3j7F9\",";
    sensor = "\"sensor\":\"MPU6050\",";
    String accx = "\"accx\":\"" + String(a.acceleration.x) + "\",";
    String accy = "\"accy\":\"" + String(a.acceleration.y) + "\",";
    String accz = "\"accz\":\"" + String(a.acceleration.z) + "\",";
    String gyrox = "\"gyrox\":\"" + String(g.gyro.x) + "\",";
    String gyroy = "\"gyroy\":\"" + String(g.gyro.y) + "\",";
    String gyroz = "\"gyroz\":\"" + String(g.gyro.z) + "\",";
    String temperature = "\"temperature\":\"" + String(temp.temperature) + "\"";
    httpRequestData = "{" + api_key + sensor + accx + accy + accz + gyrox + gyroy + gyroz + temperature + "}";

    // ----------------------------------------------------------------------
    // Your Domain name with URL path or IP address with path
    http.begin(client, serverNameMPU);
    
    // Specify content-type header
    http.addHeader("Content-Type", "application/json");
    
    // Send HTTP POST request
    httpResponseCode = http.POST(httpRequestData);

    Serial.print("MPU HTTP Response code: ");
    Serial.println(httpResponseCode);

    // Free resources
    http.end();

    // ----------------------------------------------------------------------
    // Your Domain name with URL path or IP address with path
    http.begin(client, serverDebugMPU);
    
    // Specify content-type header
    http.addHeader("Content-Type", "application/json");
    
    // Send HTTP POST request
    httpResponseCode = http.POST(httpRequestData);

    Serial.print("MPU Debug HTTP Response code: ");
    Serial.println(httpResponseCode);

    // Free resources
    http.end();
  }
  else {
    digitalWrite (LED_PIN, LOW);
    Serial.println("WiFi Disconnected");
  }
}
