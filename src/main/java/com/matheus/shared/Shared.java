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

    public static final String ASTAR = "AStar";

    public static final String HPA = "HPA";

    public static final String GAIN_SCHEDULING = "GainScheduling";

    // Prefetch count variables
    public static final double TARGET_PREFETCH_COUNT = 1;

    public static final double SV = 2.7; // Shutoff voltage (page 17) = 2.7 V
    public static final double OV = 3.7; // Optimum voltage (page 17) = 3.7 V
    public static final double HYSTERESIS = 0.001; // 10 mV
}