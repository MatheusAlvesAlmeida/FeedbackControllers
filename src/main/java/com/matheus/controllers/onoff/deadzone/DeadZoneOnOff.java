package com.matheus.controllers.onoff.deadzone;

import com.matheus.controllers.def.info.Info;
import com.matheus.controllers.def.ops.IController;

public class DeadZoneOnOff implements IController {
    private Info info;

    public DeadZoneOnOff() {
        this.info = new Info();
    }

    @Override
    public void initialize(double... params) {
        if (params.length < 4) {
            throw new IllegalArgumentException(
                    "Error: 'DeadZoneOnOff' controller requires 4 parameters: setpoint, min, max and deadzone");
        }
        this.info.setType("DeadZoneOnOff");
        this.info.setSetPoint(params[0]);
        this.info.setMin(params[1]);
        this.info.setMax(params[2]);
        this.info.setDeadZone(params[3]);
    }

    @Override
    public double update(double... input) {
        double direction = 1.0;
        double newPrefetchCount = 0.0;

        double setPoint = this.info.getSetPoint();
        double currentValue = input[0];

        double err = direction * (setPoint - currentValue);

        if (err > -this.info.getDeadZone() / 2.0 && err < this.info.getDeadZone() / 2.0) {
            newPrefetchCount = 0.0; // no action
        } else if (err >= this.info.getDeadZone() / 2.0) {
            newPrefetchCount = this.info.getMax();
        } else if (err <= -this.info.getDeadZone() / 2) {
            newPrefetchCount = this.info.getMin();
        }

        if (newPrefetchCount < this.info.getMin()) {
            newPrefetchCount = this.info.getMin();
        } else if (newPrefetchCount > this.info.getMax()) {
            newPrefetchCount = this.info.getMax();
        }

        return newPrefetchCount;
    }

    @Override
    public void updateSetPoint(double setPoint) {
        this.info.setSetPoint(setPoint);
    }
}
