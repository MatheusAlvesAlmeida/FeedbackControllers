package com.matheus.controllers.onoff.basic;

import com.matheus.controllers.def.info.Info;
import com.matheus.controllers.def.ops.IController;
import com.matheus.shared.Shared;

public class BasicOnOff implements IController{
    private Info info;

    public BasicOnOff() {
        this.info = new Info();
    }

    @Override
    public void initialize(double... params) {
        if (params.length < 3) {
            throw new IllegalArgumentException("Error: 'OnOffBasic' controller requires 3 parameters: setpoint, min and max");
        }
        
        double setpoint = params[0];
        double min = params[1];
        double max = params[2];
        
        if(min > max) {
            throw new IllegalArgumentException("Error: 'OnOffBasic' controller requires min < max");
        }
        
        this.info.setType(Shared.BASIC_ONOFF);
        this.info.setSetPoint(setpoint);
        this.info.setMin(min);
        this.info.setMax(max);
    }

    @Override
    public double update(double... input) {
        if (input == null || input.length < 1) {
            throw new IllegalArgumentException("Input array should contain the current arrival rate");
        }
        
        double direction = 1.0;
        double newPrefetchCount = 0.0;

        double setPoint = this.info.getSetPoint();
        double currentValue = input[0];
        
        double err = direction * (setPoint - currentValue);
        
        if(err >= 0) {
            newPrefetchCount = this.info.getMax();
        } else {
            newPrefetchCount = this.info.getMin();
        }
        
        return newPrefetchCount;
    }
}
