package com.matheus.shared;

public class Shared {

    // Controller type names
    public static final String BASIC_ONOFF = "OnOff";
    public static final String DEADZONE_ONOFF = "OnOffwithDeadZone";
    public static final String HYSTERESIS_ONOFF = "OnOffwithHysteresis";

    public static final String BASIC_PID = "BasicPID";
    public static final String SMOOTHING_PID = "SmoothingDerivativePID";
    public static final String INCREMENTAL_FORM_PID = "IncrementalFormPID";
    public static final String ERROR_SQUARE_PID = "ErrorSquarePID";
    public static final String DEADZONE_PID = "DeadZonePID";

    public static final String GAIN_SCHEDULING = "GainScheduling";

    // Prefetch count variables
    public static final double TARGET_PREFETCH_COUNT = 1;

    // Capacitor behaviour patterns/AsTAR
    public static final int IncreasingHarvesting = 0;
    public static final int HalfIncreasingHarvesting = 1;
    public static final int QuarterIncreasingHarvesting = 2;
    public static final int DecreasingHarvesting = 3;
    public static final int HalfDecreasingHarvesting = 4;
    public static final int QuarterDecreasingHarvesting = 5;
    public static final int ConstantHarvesting = 6;
    public static final int RandomHarvesting = 7;
    public static final int NoHarvesting = 1000;

    // AsTAR parameters
    public static final double SV = 2.7; // Shutoff voltage (page 17) = 2.7 V
    public static final double OV = 3.7; // Optimum voltage (page 17) = 3.7 V
    public static final double HYSTERESIS = 0.001; // 10 mV
    public static final double MinimumTaskExecutionRate = 1;
    public static final double MaximumTaskExecutionRate = 1200;
    public static final double MaximumVoltage = 100.0;
    public static final double StepVoltage = 0.001; // in Volts
    public static final double AdaptationCycles = 131;
    public static final double InitialVoltage = 3.3; // 3.3 V - page 19
    public static final double InitialRate = 1;
    public static final double SamplingCycleSize = 30;
    public static final double AverageConsumption = 0.01;
}