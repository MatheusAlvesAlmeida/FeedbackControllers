package com.matheus.controllers.def.ops;

import com.matheus.controllers.aStar.aStar;
import com.matheus.controllers.hpa.HPA;
import com.matheus.controllers.onoff.basic.BasicOnOff;
import com.matheus.controllers.onoff.deadzone.DeadZoneOnOff;
import com.matheus.controllers.onoff.hysteresis.HysteresisOnOff;
import com.matheus.controllers.pid.basic.BasicPID;
import com.matheus.shared.Shared;

public interface IController {
    void initialize(double... params);
    double update(double... input);
    void updateSetPoint(double setPoint);

    static IController createController(String typeName, double... params) {
        switch (typeName) {
            case Shared.BASIC_ONOFF:
                BasicOnOff onoffBasic = new BasicOnOff();
                onoffBasic.initialize(params);
                return onoffBasic;

            case Shared.DEADZONE_ONOFF:
                DeadZoneOnOff onoffDeadZone = new DeadZoneOnOff();
                onoffDeadZone.initialize(params);
                return onoffDeadZone;

            case Shared.HYSTERESIS_ONOFF:
                HysteresisOnOff onoffHysteresis = new HysteresisOnOff();
                onoffHysteresis.initialize(params);
                return onoffHysteresis;

            case Shared.ASTAR:
                aStar aStar = new aStar();
                aStar.initialize(params);
                return aStar;
                
            case Shared.HPA:
                HPA hpa = new HPA();
                hpa.initialize(params);
                return hpa;

            case Shared.BASIC_PID:
                BasicPID pid = new BasicPID();
                pid.initialize(params);
                return pid;

            default:
                System.out.println("Error: Controller type ´" + typeName + "´ is unknown!");
                System.exit(0);
                return null;
        }
    }
}


