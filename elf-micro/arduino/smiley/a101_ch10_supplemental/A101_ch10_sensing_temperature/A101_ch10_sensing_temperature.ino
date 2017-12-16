// A101_ch10_sensing_temperature 8/15/14

#define VOLTS_PER_ADC_STEP 5.0/1024.0
#define VOLTS_PER_DEGREE 0.01 
#define temp_BASE -40.0
#define LOW_ADC 20.49
#define HIGH_ADC 358.61
#define LOW_VOLTAGE 0.5

int tempSensorPin = A1;  // analog input pin for temperature sensor

float tempSensorADC = 0.0; // variable to for the ADC reading
float tempertureSensorVoltage = 0.0; // variable for the converted voltage
float tempSensorVoltage = 0.0; // variable for the sensor voltage
float tempCelcius = 0.0; // variable for degrees Celcius

void setup()
{
  Serial.begin(57600);
  Serial.println("Sensing Temperature. rev 1.0");
}

void loop()
{
  tempSensorADC = analogRead(tempSensorPin);
  tempSensorVoltage = (tempSensorADC * VOLTS_PER_ADC_STEP);
  tempCelcius = ((tempSensorVoltage - 0.5)/ VOLTS_PER_DEGREE); 
  
  Serial.print("Temperature in Celcius = ");
  Serial.println(tempCelcius); 
  Serial.print("Temperature in Fahrenheit = ");
  // °F = °C  x  9/5 + 32 
  Serial.println( ((tempCelcius * 9)/5) + 32);  
  
  delay(5000);
}


