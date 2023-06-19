package com.matheus.controllers.onoff.hysteresis;

import com.matheus.controllers.def.info.Info;
import com.matheus.controllers.def.ops.IController;

public class HysteresisOnOff implements IController {
    private Info info;

    public HysteresisOnOff() {
        this.info = new Info();
    }

    @Override
    public void initialize(double... params) {
        if (params.length < 4) {
            throw new IllegalArgumentException(
                    "Error: 'HysteresisOnOff' controller requires 4 parameters: setPoint, min, max and hysteresis band");
        }
        this.info.setPoint = params[0];
        this.info.min = params[1];
        this.info.max = params[2];
        this.info.hysteresisBand = params[3];
        this.info.previousOut = 0.0;
    }

    @Override
    public double update(double... input) {
        double direction = 1.0;
        double u = 0.0;

        double s = this.info.setPoint;
        double y = input[0];

        double err = direction * (s - y);

        if (err > -info.hysteresisBand / 2.0 && err < info.hysteresisBand / 2.0) {
            u = info.previousOut; // no action
        } else if (err >= info.hysteresisBand / 2.0) {
            u = info.max;
        } else if (err <= -info.hysteresisBand / 2) {
            u = info.min;
        }

        if (u < info.min) {
            u = info.min;
        } else if (u > info.max) {
            u = info.max;
        }

        this.info.setPreviousOut(u);

        return u;
    }

    @Override
    public void updateSetPoint(double setPoint) {
        this.info.setSetPoint(setPoint);
    }
}
