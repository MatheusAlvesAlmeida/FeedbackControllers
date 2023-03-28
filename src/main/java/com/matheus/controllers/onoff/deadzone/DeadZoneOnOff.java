package com.matheus.controllers.onoff.deadzone;

import com.matheus.controllers.def.info.Info;
import com.matheus.controllers.def.ops.IController;
import com.matheus.shared.Shared;

public class DeadZoneOnOff implements IController{
    private Info info;

    public DeadZoneOnOff() {
        this.info = new Info();
    }

    @Override
    public void initialize(double... params) {
        if (params.length < 3) {
            System.out.printf("Error: '%s' controller requires 3 info (min, max, dead zone band) %n", Shared.DEADZONE_ONOFF);
            System.exit(0);
        }
        this.info.Min = params[0];
        this.info.Max = params[1];
        this.info.DeadZone = params[2];
    }

    @Override
    public double update(double... input) {
        double direction = -1.0;
        double u = 0.0;

        double s = input[0];
        double y = input[1];

        double err = direction * (s - y);

        if (err > -info.DeadZone/2.0 && err < info.DeadZone/2.0) {
            u = 0.0; // no action
        }
        if (err >= info.DeadZone/2.0) {
            u = info.Max;
        }
        if (err <= -info.DeadZone/2) {
            u = info.Min;
        }
    
        if (u < info.Min) {
            u = info.Min;
        }
        if (u > info.Max) {
            u = info.Max;
        }

        return u;
    }
    
}
