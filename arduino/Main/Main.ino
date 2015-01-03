#include <I2Cdev.h>

#include <Wire.h>
#include <Adafruit_AM2315.h>
#include <Adafruit_MPL115A2.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_HMC5883_U.h>
#include <OneWire.h>  
#include <DallasTemperature.h>
#include <MPU6050.h>
#include <DueTimer.h>
#include <stdio.h>
#include <string.h>

//PIN-Belegung Weather Station
#define PIN_ANEMOMETER 15
#define PIN_RAIN 14
#define PIN_VANE A5

// Data wire is plugged into pin 0 on the Arduino due
#define ONE_WIRE_BUS 10


Adafruit_AM2315 am2315;
Adafruit_MPL115A2 mpl115a2;
MPU6050 mpu6050;

/* Assign a unique ID to this sensor at the same time */
Adafruit_HMC5883_Unified mag = Adafruit_HMC5883_Unified(12345);

// Setup a oneWire instance to communicate with any OneWire devices (not just Maxim/Dallas temperature ICs)
OneWire oneWire(ONE_WIRE_BUS);
 
// Pass our oneWire reference to Dallas Temperature. 
DallasTemperature sensors(&oneWire);

const int xAxis = A5;
const int yAxis = A6;
const int zAxis = A7;
String serialInput;

volatile int numRevsAnemometer = 0;
volatile int numClicksRain = 0;
volatile float WindSpeed = 0;
volatile float Percipitation = 0;

/*below counts the number of times we go to the 
timer interrupt, allows for calcualtion
of multiples of the timer frequency*/
int visits_counter = 0;
/*The value below * 5 seconds = defines
timer frequency for rain calculation
e.g. top_times = 6 (* 5 = 30 seconds)*/
int top_times = 10;

void displayHMC5883Details(void)
{
  sensor_t sensor;
  mag.getSensor(&sensor);
  Serial.println("------------------------------------");
  Serial.print  ("Sensor:       "); Serial.println(sensor.name);
  Serial.print  ("Driver Ver:   "); Serial.println(sensor.version);
  Serial.print  ("Unique ID:    "); Serial.println(sensor.sensor_id);
  Serial.print  ("Max Value:    "); Serial.print(sensor.max_value); Serial.println(" uT");
  Serial.print  ("Min Value:    "); Serial.print(sensor.min_value); Serial.println(" uT");
  Serial.print  ("Resolution:   "); Serial.print(sensor.resolution); Serial.println(" uT");  
  Serial.println("------------------------------------");
  Serial.println("");
  delay(500);
}

void printHMC5883Values(void) 
{
  /* Get a new sensor event */ 
  sensors_event_t event; 
  mag.getEvent(&event);
 
  /* Display the results (magnetic vector values are in micro-Tesla (uT)) */
  Serial.print("X: "); Serial.print(event.magnetic.x); Serial.print("  ");
  Serial.print("Y: "); Serial.print(event.magnetic.y); Serial.print("  ");
  Serial.print("Z: "); Serial.print(event.magnetic.z); Serial.print("  ");Serial.println("uT");

}

void printMPU6050Values(void) 
{
  
  int16_t x = -1;
  int16_t y = -1;
  int16_t z = -1;
 
  mpu6050.getRotation(&x,&y,&z);
  /* Display the results (magnetic vector values are in micro-Tesla (uT)) */
  Serial.print("X: "); Serial.print(x); Serial.print("  ");
  Serial.print("Y: "); Serial.print(y); Serial.print("  ");
  Serial.print("Z: "); Serial.print(z); Serial.print("  ");Serial.println("uT");

}

void setup() {
  Serial.begin(9600);
  
  analogReference((eAnalogReference)EXTERNAL);
  
  Serial.println("AM2315 Test!");

  if (! am2315.begin()) {
     Serial.println("AM2315 not connected, check wiring & pullups!");
     //while (1);
  }
  
  mpl115a2.begin();
  mag.begin();
  mpu6050.initialize();
  
  if (! mpu6050.testConnection()) {
     Serial.println("MPU6050 not connected, check wiring & pullups!");
     //while (1);
  }
  
  sensors.begin();
  //Serial.println(sensors.getDeviceCount());
  
  pinMode(PIN_ANEMOMETER, INPUT);
  digitalWrite(PIN_ANEMOMETER, HIGH);
  attachInterrupt(PIN_ANEMOMETER, countAnemometer, FALLING);
  
  pinMode(PIN_RAIN, INPUT);
  digitalWrite(PIN_RAIN, HIGH);
  attachInterrupt(PIN_RAIN, countRain, FALLING);
  
  
  Timer1.attachInterrupt(callback).start(10000000);  // attaches callback() as a timer overflow interrupt
  
}

void countAnemometer() {
   numRevsAnemometer++;
}

void countRain() {
  numClicksRain++;
  // Serial.print("countRain");
}

void callback()
{
  /*the ISR for the timer interrupt, called every 10 seconds*/
//check how many times the switch was closed on this period? this value is on numRevsAnemometer
  WindSpeed = (numRevsAnemometer) * 2.4 / 10 / 3.6; //2.4 K/h for one switch closure per second, / 3.6 to convert from km/h to m/s
  numRevsAnemometer = 0;

/*rain caculation*/
  if (visits_counter < top_times) visits_counter++;
  else{
    Percipitation = 0.28*(numClicksRain*60 / 10) * 60;
    //0.2794 mm per contact closure; this step gives number of clicks per minute unit = mm per minute;  the last *60 to convert from mm/min to mm/h (equals mm/square meter)
    numClicksRain = 0;
    visits_counter = 0;
  }
}


void loop() {
  
}

int getIntForKey(String string){
	if (strcmp(string.c_str(),"smoje.battery") == 0){
		return 0;
	}
	else if (strcmp(string.c_str(), "air.temperature") == 0){
		return 1;
	}
	else if (strcmp(string.c_str(), "air.humidity") == 0){
		return 2;
	}
	else if (strcmp(string.c_str(), "air.athmosphericpressure") == 0){
		return 3;
	}
	else if (strcmp(string.c_str(), "air.geiger") == 0){
		return 4;
	}
	else if (strcmp(string.c_str(), "air.acceleration") == 0){
		return 5;
	}
	else if (strcmp(string.c_str(), "air.windspeed") == 0){
		return 6;
	}
	else if (strcmp(string.c_str(), "air.winddirection") == 0){
		return 7;
	}
	else if (strcmp(string.c_str(), "air.rainamount") == 0){
		return 8;
	}
	else if (strcmp(string.c_str(), "air.uvlight") == 0){
		return 9;
	}
	else if (strcmp(string.c_str(), "air.compass") == 0){
		return 10;
	}	
	else if (strcmp(string.c_str(), "water.temperature") == 0){
		return 11;
	}
	else if (strcmp(string.c_str(), "water.salinity") == 0){
		return 12;
	}
	else if (strcmp(string.c_str(), "water.dissolvedoxygen") == 0){
		return 13;
	}
	else if (strcmp(string.c_str(), "water.drift") == 0){
		return 14;
	}
        else
        {
          return 0;
        }
}

void serialEvent()
{  
  if (Serial.available())
  {
    serialInput = "";
    delay(25);  //delay to allow buffer to fill 
    
    while (Serial.available()) {
    	char c = Serial.read();  	//gets one byte from serial buffer
      	serialInput += c; 			//makes the string readString
    } 
    
    Serial.println(serialInput.c_str());
    
    switch(getIntForKey(serialInput))
    {
      case 1:
        Serial.println(am2315.readTemperature()); 
        break;

      case 2:
        Serial.println(am2315.readHumidity());
        break;
        
      case 3:
        Serial.println(mpl115a2.getPressure());
        break;
        
      case 6:
        Serial.println(WindSpeed);
        break;
        
      case 7:
        Serial.println(analogRead(PIN_VANE));
        break;
        
      case 8:
        Serial.println(Percipitation);
        break;
        
      case 11:
        sensors.requestTemperatures(); // Send the command to get temperatures
        Serial.println(sensors.getTempCByIndex(0)); // Why "byIndex"? You can have more than one IC on the same bus. 0 refers to the first IC on the wire
        break;
        
      default:
        Serial.println("no Device on this Number");
    }
  }
}
