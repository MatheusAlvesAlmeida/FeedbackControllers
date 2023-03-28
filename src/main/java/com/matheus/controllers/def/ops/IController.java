package com.matheus.controllers.def.ops;

import com.matheus.controllers.onoff.basic.BasicOnOff;
import com.matheus.shared.Shared;

public interface IController {
    void initialize(double... params);
    double update(double... input);

    static IController createController(String typeName, double... params) {
        switch (typeName) {
            case Shared.BASIC_ONOFF:
                BasicOnOff onoffBasic = new BasicOnOff();
                onoffBasic.initialize(params);
                return onoffBasic;

            default:
                System.out.println("Error: Controller type ´" + typeName + "´ is unknown!");
                System.exit(0);
                return null;
        }
    }
}

