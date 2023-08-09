package com.matheus.controllers.pid.incremental;

import com.matheus.controllers.def.info.Info;
import com.matheus.controllers.def.ops.IController;

public class IncrementalPID implements IController{
    private Info info;
    private static final double dt = 1;

    public IncrementalPID() {
        this.info = new Info();
    }

    @Override
    public void initialize(double... params) {
        if(params.length < 8) {
            throw new IllegalArgumentException("IncrementalPID requires 8 parameters: setpoint, direction, min, max, PC, kp, ki and kd");
        }
        this.info.setSetPoint(params[0]);
        this.info.setDirection(params[1]);
        this.info.setMin(params[2]);
        this.info.setMax(params[3]);
        this.info.setPC(params[4]);
        this.info.setKp(params[5]);
        this.info.setKi(params[6]);
        this.info.setKd(params[7]);

        this.info.setIntegrator(0);
        this.info.setSumPrevErrors(0);
        this.info.setPreviousOut(0);
        this.info.setPreviousError(0);
        this.info.setPreviousPreviousError(0);
        this.info.setPreviousDifferentiator(0);
    }

    @Override
    public double update(double... input) {
        double setPoint = info.getSetPoint();
        double currentValue = input[0];
        double error = this.info.getDirection() * (setPoint - currentValue);
        
        double deltaU = this.info.getKp() * (error - this.info.getPreviousError()) + this.info.getKi() * error * dt + this.info.getKd() * (error - 2 * this.info.getPreviousError() + this.info.getPreviousPreviousError()) / dt;

        double output = this.info.getPreviousOut() + deltaU;

        if(output > this.info.getMax()){
            output = this.info.getMax();
        } else if(output < this.info.getMin()){
            output = this.info.getMin();
        }

        this.info.setPreviousError(error);
        this.info.setPreviousPreviousError(this.info.getPreviousError());
        this.info.setPreviousOut(output);
        this.info.setSumPrevErrors(this.info.getSumPrevErrors() + error);

        return output;
    }

    @Override
    public void updateSetPoint(double setPoint) {
        this.info.setSetPoint(setPoint);
    }


}