// A101_ch10_volts_to_temp_test 8/14/14

#define VOLTS_PER_ADC_STEP 5.0/1024.0
#define VOLTS_PER_DEGREE 0.01 
#define temp_BASE -40.0
#define LOW_ADC 20.49
#define HIGH_ADC 358.61
#define LOW_VOLTAGE 0.5

int temperatueSensorPin = A0;  // analog input pin
float tempSensorADC = 0.0; // variable to for the ADC reading
float tempertureSensorVoltage = 0.0; // variable for the converted voltage
float tempSensorVoltage = 0.0; // variable for the sensor voltage
float tempCelcius = 0.0; // variable for degrees Celcius

void setup()
{
  Serial.begin(57600);
  Serial.println("Volts to temp test. rev 1.0");
 
 // Look from lowest ADC reading in +10 steps
 for(float i = LOW_ADC; i < HIGH_ADC; i += 10.0)
 { 
  tempSensorADC = i;
  tempSensorVoltage = (tempSensorADC * VOLTS_PER_ADC_STEP);
  tempCelcius = ((tempSensorVoltage-0.5) / VOLTS_PER_DEGREE);

  showIt(); 
 }
 
  // Look at highest acceptable ADC reading
  tempSensorADC = HIGH_ADC;
  tempSensorVoltage = (tempSensorADC * VOLTS_PER_ADC_STEP);
  tempCelcius = ((tempSensorVoltage - 0.5)/ VOLTS_PER_DEGREE);
  showIt();
}

void loop()
{
  // do nothing 
}

void showIt()
{
  Serial.print("tempSensorADC = ");
  Serial.println(tempSensorADC);
  Serial.print("tempSensorVoltage = ");
  Serial.println(tempSensorVoltage); 
  Serial.print("Temperature in Celcius = ");
  Serial.println(tempCelcius); 
  Serial.print("Temperature in Fahrenheit = ");
  // °F = °C  x  9/5 + 32 
  Serial.println( ((tempCelcius * 9)/5) + 32);   
}
