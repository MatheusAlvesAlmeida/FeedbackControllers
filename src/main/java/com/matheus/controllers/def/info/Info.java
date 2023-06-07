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
    public double previousRate; // Previous arrival rate -- used by some controllers

    public void setPreviousRate(double previousRate) {
        this.previousRate = previousRate;
    }

    public double getPreviousRate() {
        return this.previousRate;
    }

    public void setType(String type) {
        this.typeName = type;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMax() {
        return this.max;
    }

    public double getMin() {
        return this.min;
    }

    public void setSetPoint(double setPoint) {
        this.setPoint = setPoint;
    }

    public double getSetPoint() {
        return this.setPoint;
    }

    public void setPreviousOut(double previousOut) {
        this.previousOut = previousOut;
    }

    public double getPreviousOut() {
        return this.previousOut;
    }

    public void setDeadZone(double deadZone) {
        this.deadZone = deadZone;
    }

    public double getDeadZone() {
        return this.deadZone;
    }

    public void setHysteresisBand(double hysteresisBand) {
        this.hysteresisBand = hysteresisBand;
    }

    public double getHysteresisBand() {
        return this.hysteresisBand;
    }

    public void setIntegrator(double integrator) {
        this.integrator = integrator;
    }

    public double getIntegrator() {
        return this.integrator;
    }

    public void setSumPrevErrors(double sumPrevErrors) {
        this.sumPrevErrors = sumPrevErrors;
    }

    public double getSumPrevErrors() {
        return this.sumPrevErrors;
    }

    public void setPreviousError(double previousError) {
        this.previousError = previousError;
    }

    public double getPreviousError() {
        return this.previousError;
    }

    public void setPreviousPreviousError(double previousPreviousError) {
        this.previousPreviousError = previousPreviousError;
    }

    public double getPreviousPreviousError() {
        return this.previousPreviousError;
    }

    public void setPreviousDifferentiator(double previousDifferentiator) {
        this.previousDifferentiator = previousDifferentiator;
    }

    public double getPreviousDifferentiator() {
        return this.previousDifferentiator;
    }

    public void setKp(double kp) {
        this.kp = kp;
    }

    public double getKp() {
        return this.kp;
    }

    public void setKi(double ki) {
        this.ki = ki;
    }

    public double getKi() {
        return this.ki;
    }

    public void setKd(double kd) {
        this.kd = kd;
    }

    public double getKd() {
        return this.kd;
    }
}
