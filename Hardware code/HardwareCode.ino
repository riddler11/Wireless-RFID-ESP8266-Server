/************************************************************************
  项目名称/Project          : 基于ESP8266无线传输射频识别信息传输到服务器/Wireless transmission of RFID identification information to server based on ESP8266.
  程序名称/Program name     : PN532ESP8266打卡传输信息
  作者/Author               : 燊哥仔/Leo
  版本/Version              : 1.0
  日期/Date（YYYYMMDD）     : 20200713
  程序目的/Purpose          :通过传感器来识别进行无线传输数据到服务器/The data identify by that sensor is transmitted to the serv through the wireless network.
***********************************************************************/
#include <SPI.h>

#include <SoftwareSerial.h>



String setCWMODE = "AT+CWMODE=1\r\n";
String setRST = "AT+RST\r\n";
String setCWJAP = "AT+CWJAP=\"name\",\"password\"\r\n";  //热点wifi名字密码
String setCIPMUX = "AT+CIPMUX=0\r\n";  //单连接
String setCIPSTART = "AT+CIPSTART=\"TCP\",\"IP\",Port\r\n"; //TCP协议 中间是服务端IP（上位机）最后是连接端口
String setCIPMODE = "AT+CIPMODE=1\r\n";  //透传模式
//String setCIPSEND = "AT+CIPSEND\r\n";
//String setString = "ID01\r\n";
//String setEnd = "+++";

void setAT() {
  SoftwareSerial softSerial(1, 0);//RX=1 TX=0
  softSerial.begin(115200);
  softSerial.print(setCWMODE);//将8266设置为STA模式
  delay(3000);
  softSerial.print(setRST);//设置完之后重启
  delay(3000);
  int i;
  for(i=0;i<5;i++){
  softSerial.print(setCWJAP);//8266连接路由器发出的WiFi
  delay(3000);
  }
  softSerial.print(setCIPMUX);//启动多连接
  delay(3000);
  softSerial.print(setCIPSTART);//通过协议、IP和端口连接服务器
  delay(3000);
  softSerial.print(setCIPMODE);//发送进入透传模式指令
  delay(3000);
//  softSerial.print(setCIPSEND);//发送进入透传发送模式指令
//  delay(3000);
//  softSerial.print(setString);//发送数据
//  delay(3000);
//  softSerial.print(setEnd);//退出透传发送模式
//  delay(3000);
}

void suid(uint8_t setU) {
  SoftwareSerial softSerial(1, 0);//RX=1 TX=0
  softSerial.begin(115200);
  
    
  softSerial.print(setU);//发送数据
  

}
void stopU() {
  SoftwareSerial softSerial(1, 0);//RX=1 TX=0
  softSerial.begin(115200);
  String setID="&ID01"; //设备ID
   String setS = "\n";
  String setEnd = "+++"; //发送终止符合
    softSerial.print(setID);//发送数据
  softSerial.print(setS);
  delay(1000);
  softSerial.print(setEnd);//退出透传发送模式
  delay(3000);
}



void setup(void) {

  setAT();

}

void loop(void) {
  boolean success;
  uint8_t uid[] = { 1, 0, 1, 0, 1, 0, 1 };  // Buffer to store the returned UID
  uint8_t uidLength = 7;                        // Length of the UID (4 or 7 bytes depending on ISO14443A card type)

 
  success = true;

  if (success) {
    SoftwareSerial softSerial(1, 0);//RX=1 TX=0
    softSerial.begin(115200);  //设置这两个通信口的波特率
     String setCIPSEND = "AT+CIPSEND\r\n";  
    softSerial.print(setCIPSEND);//发送进入透传发送模式指令
    delay(3000);
    for (uint8_t i = 0; i < uidLength; i++)
    {
      uint8_t setU = uid[i];
      suid(setU);

    }
    stopU();
    // Wait 1 second before continuing
    
  }
  else
  {
    
    Serial.println("No data");
  }
}
