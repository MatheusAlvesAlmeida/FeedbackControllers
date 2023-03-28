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
        if (params.length < 2) {
            System.out.println("Error: 'OnOffBasic' controller requires two parameters: min and max");
            System.exit(0);
        }
        
        double min = params[0];
        double max = params[1];
        
        if(min > max) {
            throw new IllegalArgumentException("Error: 'OnOffBasic' controller requires min < max");
        }
        
        this.info.setType(Shared.BASIC_ONOFF);
        this.info.setMin(min);
        this.info.setMax(max);
    }

    @Override
    public double update(double... input) {
        // Validate input
        if (input == null || input.length < 2) {
            throw new IllegalArgumentException("Input array should contain at least two values");
        }
        
        double direction = -1.0;
        double u = 0.0;

        // Calculate error
        double s = input[0];
        double y = input[1];
        
        // error
        double err = direction * (s - y);
        
        // control law
        if(err >= 0) {
            u = this.info.getMax();
        } else {
            u = this.info.getMin();
        }
        
        return u;
    }
}
