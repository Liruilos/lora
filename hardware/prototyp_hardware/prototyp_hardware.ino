#include <RCSwitch.h>
#include <ESP8266WiFi.h>
#include <ESPAsyncTCP.h>
#include <ESPAsyncWebServer.h>

const char* ssid     = "Garage";
const char* password = "123456789";


AsyncWebServer server(80);

RCSwitch mySwitch = RCSwitch();

const char index_html[] PROGMEM = R"rawliteral(
<!DOCTYPE HTML><html>
  <head>
    <style>
      body {
        width:100%;
        display: flex;
        flex-direction: column;
      }
      .d-flex {
        display: flex;
        flex-direction: column;
      }
      .d-flex > div > button {
        margin: 0.5rem
      }
      .t-center {
        text-align: center;
      }
      button {
        width: 80px;
        height: 80px;
      }
      .a {
        background-color: blue;
      }
      .b {
        background-color: green;
      }
      .c {
        background-color: yellow;
      }
      .d {
        background-color: red;
      }
    </style>
  </head>
    <body>
      <div class="t-center">
        <h2>ESP8266</h2>
        <div class="d-flex">
          <div>
            <button class="a" onclick="sendButton('A')">A</button>
            <button class="b" onclick="sendButton('B')">B</button>
          </div>
          <div>
            <button class="c" onclick="sendButton('C')">C</button>
            <button class="d" onclick="sendButton('D')">D</button>
          </div>
        </div>
      </div>
    </body>
    <script>
        function sendButton(type) {
            var xhttp = new XMLHttpRequest();
            xhttp.open("GET",`/${type}`);
            xhttp.send();
        }
    </script>
</html>)rawliteral";

void setup() {

 Serial.begin(9600);

 Serial.print("Setting AP (Access Point)…");
  WiFi.softAP(ssid, password);

  IPAddress IP = WiFi.softAPIP();
  Serial.print("AP IP address: ");
  Serial.println(IP);

  Serial.println(WiFi.localIP());

  // Route for root / web page
  server.on("/", HTTP_GET, [](AsyncWebServerRequest *request){
    request->send_P(200, "text/html", index_html);
  });

  server.on("/A", HTTP_GET, [](AsyncWebServerRequest *request){
    sendA();
    request->send_P(200, "text/html", "");
  });

  server.on("/B", HTTP_GET, [](AsyncWebServerRequest *request){
    sendB();
    request->send_P(200, "text/html", "");
  });

    server.on("/C", HTTP_GET, [](AsyncWebServerRequest *request){
    sendC();
    request->send_P(200, "text/html", "");
  });

    server.on("/D", HTTP_GET, [](AsyncWebServerRequest *request){
    sendD();
    request->send_P(200, "text/html", "");
  });

  mySwitch.enableTransmit(4);
  server.begin();

}

void sendA() {
  mySwitch.send("011110100111000000100001");
}

void sendB() {
  mySwitch.send("011110100111000000100010");
}

void sendC() {
  mySwitch.send("011110100111000000100100");
}

void sendD() {
  mySwitch.send("011110100111000000101000");
}

void loop() {

}
