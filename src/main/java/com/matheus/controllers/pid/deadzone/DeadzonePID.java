package com.matheus.controllers.pid.deadzone;

import com.matheus.controllers.def.info.Info;
import com.matheus.controllers.def.ops.IController;

public class DeadzonePID implements IController{
    private Info info;
    private static final double dt = 1;

    public DeadzonePID() {
        this.info = new Info();
    }

    @Override
    public void initialize(double... params) {
        if(params.length < 5) {
            throw new IllegalArgumentException("BasicPID requires 9 parameters: setpoint, direction, min, max, PC, kp, ki, kd and deadzone band");
        }
        this.info.setSetPoint(params[0]);
        this.info.setDirection(params[1]);
        this.info.setMin(params[2]);
        this.info.setMax(params[3]);
        this.info.setPC(params[4]);
        this.info.setKp(params[5]);
        this.info.setKi(params[6]);
        this.info.setKd(params[7]);
        this.info.setDeadZone(params[8]);

        this.info.setIntegrator(0);
        this.info.setSumPrevErrors(0);
        this.info.setPreviousOut(0);
        this.info.setPreviousError(0);
        this.info.setPreviousPreviousError(0);
    }

    @Override
    public double update(double... input) {
        double setPoint = info.getSetPoint();
        double currentValue = input[0];
        double error = this.info.getDirection() * (setPoint - currentValue);
        double output = 0;

        if(Math.abs(error) < this.info.getDeadZone()){
            return this.info.getPreviousOut();
        }else{
            double proportional = this.info.getKp() * error;
            this.info.setIntegrator(this.info.getIntegrator() + error * dt);
            double integral = this.info.getKi() * this.info.getIntegrator();
            double differentiator = this.info.getKd() * (error - this.info.getPreviousError()) / dt;

            output = proportional + integral + differentiator;
        }

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
