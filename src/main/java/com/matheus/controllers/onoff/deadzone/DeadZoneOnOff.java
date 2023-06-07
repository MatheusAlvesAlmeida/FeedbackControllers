package com.matheus.controllers.onoff.deadzone;

import com.matheus.controllers.def.info.Info;
import com.matheus.controllers.def.ops.IController;

public class DeadZoneOnOff implements IController{
    private Info info;

    public DeadZoneOnOff() {
        this.info = new Info();
    }

    @Override
    public void initialize(double... params) {
        if (params.length < 4) {
            throw new IllegalArgumentException("Error: 'DeadZoneOnOff' controller requires 4 parameters: setpoint, min, max and deadzone");
        }
        this.info.setPoint = params[0];
        this.info.min = params[1];
        this.info.max = params[2];
        this.info.deadZone = params[3];
    }

    @Override
    public double update(double... input) {
        double direction = 1.0;
        double updatedValue = 0.0;

        double setPoint = this.info.setPoint;
        double currentValue = input[0];

        double err = direction * (setPoint - currentValue);

        if (err > -info.deadZone/2.0 && err < info.deadZone/2.0) {
            updatedValue = 0.0; // no action
        }else if (err >= info.deadZone/2.0) {
            updatedValue = info.max;
        }else if (err <= -info.deadZone/2) {
            updatedValue = info.min;
        }
    
        if (updatedValue < info.min) {
            updatedValue = info.min;
        }else if (updatedValue > info.max) {
            updatedValue = info.max;
        }

        return updatedValue;
    }
    
}
