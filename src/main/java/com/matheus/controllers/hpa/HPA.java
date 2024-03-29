package com.matheus.controllers.hpa;

import com.matheus.controllers.def.info.Info;
import com.matheus.controllers.def.ops.IController;

public class HPA implements IController{
    private Info info;

    public HPA() {
        this.info = new Info();
    }

    @Override
    public void initialize(double... params) {
        if(params.length < 5) {
            throw new IllegalArgumentException("HPA requires 5 parameters: setpoint, direction, min, max, PC");
        }
        this.info.setSetPoint(params[0]);
        this.info.setDirection(params[1]);
        this.info.setMin(params[2]);
        this.info.setMax(params[3]);
        this.info.setPC(params[4]);
    }

    @Override
    public double update(double... params) {
        double newPrefetchCount = 0;
        double setPoint = info.getSetPoint();
        double currentValue = params[0];

        newPrefetchCount = Math.round(this.info.getPC() * setPoint / currentValue);

        if(newPrefetchCount > this.info.getMax()){
            newPrefetchCount = this.info.getMax();
        } else if(newPrefetchCount < this.info.getMin()){
            newPrefetchCount = this.info.getMin();
        }

        this.info.setPC(newPrefetchCount);

        return newPrefetchCount;
    }

    @Override
    public void updateSetPoint(double setPoint) {
        this.info.setSetPoint(setPoint);
    }
    
}
