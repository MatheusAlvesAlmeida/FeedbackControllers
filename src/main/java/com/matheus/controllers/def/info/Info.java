package com.matheus.controllers.def.info;

public class Info {
    public String typeName; // Controller type name
    public double setPoint; // Set point
    public double kp; // kp constant used by PID controllers
    public double ki; // ki constant used by PID controllers
    public double kd; // kd constant used by PID controllers

    public double min; // Minimum value of the controller output
    public double max; // Maximum value of the controller output

    public double integrator; // Integrator component
    public double sumPrevErrors; // Sum of previous errors -- used by some controllers
    public double previousOut; // Previous output -- used by some controllers
    public double previousError; // Last error -- used by some controllers
    public double previousPreviousError; // Penultimate error -- used by some controllers
    public double previousDifferentiator; // Antepenultimate error -- used by some controllers
    public double deadZone; // Dead zone band used by some controllers
    public double hysteresisBand; // Hysteresis band used by some controllers
    public double out; // Controller output

    public void setType(String basicOnoff) {
        this.typeName = basicOnoff;
    }

    public void setMin(double min2) {
        this.min = min2;
    }

    public void setMax(double max2) {
        this.max = max2;
    }

    public double getMax() {
        return this.max;
    }

    public double getMin() {
        return this.min;
    }

    public void setSetPoint(double setpoint) {
        this.setPoint = setpoint;
    }

    public double getSetPoint() {
        return this.setPoint;
    }

    public void setPreviousOut(double previousOut) {
        this.previousOut = previousOut;
    }

}
