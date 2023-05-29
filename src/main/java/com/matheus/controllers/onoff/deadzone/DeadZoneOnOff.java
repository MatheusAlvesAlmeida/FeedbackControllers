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
        this.info.SetPoint = params[0];
        this.info.Min = params[1];
        this.info.Max = params[2];
        this.info.DeadZone = params[3];
    }

    @Override
    public double update(double... input) {
        double direction = 1.0;
        double updatedValue = 0.0;

        double setPoint = this.info.SetPoint;
        double currentValue = input[0];

        double err = direction * (setPoint - currentValue);

        if (err > -info.DeadZone/2.0 && err < info.DeadZone/2.0) {
            updatedValue = 0.0; // no action
        }else if (err >= info.DeadZone/2.0) {
            updatedValue = info.Max;
        }else if (err <= -info.DeadZone/2) {
            updatedValue = info.Min;
        }
    
        if (updatedValue < info.Min) {
            updatedValue = info.Min;
        }else if (updatedValue > info.Max) {
            updatedValue = info.Max;
        }

        return updatedValue;
    }
    
}
