package com.matheus.controllers.onoff.hysteresis;

import com.matheus.controllers.def.info.Info;
import com.matheus.controllers.def.ops.IController;
import com.matheus.shared.Shared;

public class Hysteresis implements IController{
    private Info info;

    public Hysteresis() {
        this.info = new Info();
    }

    @Override
    public void initialize(double... params) {
        if (params.length < 3) {
            System.out.printf("Error: '%s' controller requires 3 info (min,max,hysteresis band) %n", Shared.HYSTERESIS_ONOFF);
            System.exit(0);
        }
        this.info.Min = params[0];
        this.info.Max = params[1];
        this.info.HysteresisBand = params[2];
        this.info.PreviousOut = 0.0;
    }

    @Override
    public double update(double... input) {
        double direction = -1.0;
        double u = 0.0;

        double s = input[0];
        double y = input[1];

        double err = direction * (s - y);

        if (err > -info.HysteresisBand/2.0 && err < info.HysteresisBand/2.0) {
            u = info.PreviousOut; // no action
        }
        if (err >= info.HysteresisBand/2.0) {
            u = info.Max;
        }
        if (err <= -info.HysteresisBand/2) {
            u = info.Min;
        }
    
        if (u < info.Min) {
            u = info.Min;
        }
        if (u > info.Max) {
            u = info.Max;
        }

        info.PreviousOut = u;
        return u;
    }
    
}
