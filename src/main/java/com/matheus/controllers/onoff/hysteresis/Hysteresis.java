package com.matheus.controllers.onoff.hysteresis;

import com.matheus.controllers.def.info.Info;
import com.matheus.controllers.def.ops.IController;

public class Hysteresis implements IController{
    private Info info;

    public Hysteresis() {
        this.info = new Info();
    }

    @Override
    public void initialize(double... params) {
        if (params.length < 4) {
            throw new IllegalArgumentException("Error: 'HysteresisOnOff' controller requires 4 parameters: setpoint, min, max and hysteresis band");
        }
        this.info.SetPoint = params[0];
        this.info.Min = params[1];
        this.info.Max = params[2];
        this.info.HysteresisBand = params[3];
        this.info.PreviousOut = 0.0;
    }

    @Override
    public double update(double... input) {
        double direction = 1.0;
        double u = 0.0;

        double s = this.info.SetPoint;
        double y = input[0];

        double err = direction * (s - y);

        if (err > -info.HysteresisBand/2.0 && err < info.HysteresisBand/2.0) {
            u = info.PreviousOut; // no action
        }else if (err >= info.HysteresisBand/2.0) {
            u = info.Max;
        }else if (err <= -info.HysteresisBand/2) {
            u = info.Min;
        }
    
        if (u < info.Min) {
            u = info.Min;
        }else if (u > info.Max) {
            u = info.Max;
        }

        this.info.setPreviousOut(u);

        return u;
    }
    
}
