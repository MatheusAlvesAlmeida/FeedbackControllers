package com.matheus.controllers.def.info;

public class Info {

    public String TypeName; // Controller type name
    public double SetPoint; // Set point
    public double Kp; // kp constant used by PID controllers
    public double Ki; // ki constant used by PID controllers
    public double Kd; // kd constant used by PID controllers

    public double Min; // Minimum value of the controller output
    public double Max; // Maximum value of the controller output

    public double Integrator; // Integrator component
    public double SumPrevErrors; // Sum of previous errors -- used by some controllers
    public double PreviousOut; // Previous output -- used by some controllers
    public double PreviousError; // Last error -- used by some controllers
    public double PreviousPreviousError; // Penultimate error -- used by some controllers
    public double PreviousDifferentiator; // Antepenultimate error -- used by some controllers
    public double DeadZone; // Dead zone band used by some controllers
    public double HysteresisBand; // Hysteresis band used by some controllers
    public double Out; // Controller output

    public void setType(String basicOnoff) {
        this.TypeName = basicOnoff;
    }

    public void setMin(double min2) {
        this.Min = min2;
    }

    public void setMax(double max2) {
        this.Max = max2;
    }

    public double getMax() {
        return this.Max;
    }

    public double getMin() {
        return this.Min;
    }

    public void setSetPoint(double setpoint) {
        this.SetPoint = setpoint;
    }

    public double getSetPoint() {
        return this.SetPoint;
    }

    public void setPreviousOut(double previousOut) {
        this.PreviousOut = previousOut;
    }
}
