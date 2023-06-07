package com.matheus.controllers.aStar;

import com.matheus.controllers.def.info.Info;
import com.matheus.controllers.def.ops.IController;
import com.matheus.shared.Shared;

public class aStar implements IController {
    private Info info;

    public aStar() {
        this.info = new Info();
    }

    @Override
    public void initialize(double... params) {
        if (params.length < 4) {
            throw new IllegalArgumentException(
                    "Error: AStar controller requires 4 parameters setPoint, min, max and hysteresis band \n");
        }

        this.info.setType(Shared.ASTAR);
        this.info.setSetPoint(params[0]);
        this.info.setMin(params[1]);
        this.info.setMax(params[2]);
        this.info.setHysteresisBand(params[3]);
        this.info.setPreviousOut(0);
        this.info.setPreviousRate(0);
    }

    @Override
    public double update(double... params) {
        double newPrefetchCount = 0.0;
        double setpoint = this.info.getSetPoint();
        double currentValue = params[0]; // measured arrival rate

        if (currentValue < (setpoint - this.info.getHysteresisBand())) { // The system is below the goal
            if (currentValue > this.info.getPreviousRate()) {
                newPrefetchCount = this.info.getPreviousOut() + 1;
            } else {
                newPrefetchCount = this.info.getPreviousOut() * 2;
            }
        } else if (currentValue > (setpoint + this.info.getHysteresisBand())) { // The system is above the goal
            if (currentValue < this.info.getPreviousRate()) {
                newPrefetchCount = this.info.getPreviousOut() - 1;
                // System.out.printf("Above the goal (Reducing) [%.4f]",
                // this.info.getOptimumLevel() + this.info.getHysteresisBand());
            } else {
                newPrefetchCount = this.info.getPreviousOut() / 2;
                // System.out.printf("Above the goal (Reducing fast) [%.4f]",
                // this.info.getOptimumLevel() + this.info.getHysteresisBand());
            }
        } else { // The system is at the Optimum state, no action required
            newPrefetchCount = this.info.getPreviousOut();
            // System.out.printf("Optimum Level ");
        }
        
        // final check of newPrefetchCount
        if (newPrefetchCount < this.info.getMin()) {
            newPrefetchCount = this.info.getMin();
        }
        if (newPrefetchCount > this.info.getMax()) {
            newPrefetchCount = this.info.getMax();
        }

        this.info.setPreviousOut(newPrefetchCount);
        this.info.setPreviousRate(currentValue);

        return newPrefetchCount;
    }

    @Override
    public void updateSetPoint(double setPoint) {
        this.info.setSetPoint(setPoint);
    }

}
