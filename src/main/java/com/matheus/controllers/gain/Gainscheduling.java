package com.matheus.controllers.gain;

import com.matheus.controllers.def.info.Info;
import com.matheus.controllers.def.ops.IController;
import com.matheus.shared.Shared;

public class Gainscheduling implements IController{
    private static final int DELTA_TIME = 1;

    private Info info;
    public double[][] gainTable;

    public Gainscheduling() {
        this.info = new Info();
        this.gainTable = new double[2][3];
    }

    @Override
    public void initialize(double... params) {
        this.gainTable[0][0] = -9600; 
        this.gainTable[0][1] = 0.0;   
        this.gainTable[0][2] = 0.0;   

        this.gainTable[1][0] = -9600; 
        this.gainTable[1][1] = 0.5;   
        this.gainTable[1][2] = 0.0;   

        double kp = this.gainTable[0][0];
        double ki = this.gainTable[0][1];
        double kd = this.gainTable[0][2];

        this.info.Min = params[0];
        this.info.Max = params[1];

        this.info.Kp = kp;
        this.info.Ki = ki;
        this.info.Kd = kd;

        this.info.Integrator = 0.0;

        this.info.PreviousError = 0.0;
        this.info.PreviousPreviousError = 0.0;
        this.info.SumPrevErrors = 0.0;
        this.info.Out = 0.0;
        this.info.PreviousDifferentiator = 0.0;
    }

    @Override
    public double update(double... input) {
        double setpoint = input[0];
        double pv = input[1];

        double error = setpoint - pv;

        if (pv < Shared.OV) { // gain scheduling 1 (P Controller)
            this.info.Kp = this.gainTable[0][0];
            this.info.Ki = this.gainTable[0][1];
            this.info.Kd = this.gainTable[0][2];
        } else { // gain scheduling 2 (PI Controller)
            this.info.Kp = this.gainTable[1][0];
            this.info.Ki = this.gainTable[1][1];
            this.info.Kd = this.gainTable[1][2];
        }

        double pTerm = this.info.Kp * error;
        double iTerm = this.info.Integrator + (this.info.Ki * error * DELTA_TIME);
        double dTerm = this.info.Kd * (error - this.info.PreviousError) / DELTA_TIME;

        this.info.Integrator = iTerm;

        double out = pTerm + iTerm + dTerm;

        if (out > this.info.Max) {
            out = this.info.Max;
        } else if (out < this.info.Min) {
            out = this.info.Min;
        }

        this.info.PreviousError = error;
        this.info.SumPrevErrors += error;

        return out;
    }
    
}
