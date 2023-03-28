package com.matheus;

import com.matheus.aStar.cap.Capacitor;
import com.matheus.controllers.def.ops.IController;
import com.matheus.shared.Shared;

import java.lang.Math;

public class Main {
    public static void main(String[] args) {
        double vnew = Shared.InitialVoltage;

        // Create new controller instance and initialize it
        IController controller = IController.createController(Shared.BASIC_ONOFF, 0.0, 1000.0);

        Capacitor capacitor = new Capacitor();
        capacitor.setPattern(Shared.RandomHarvesting);
        capacitor.setPreviousVoltageLevel(Shared.InitialVoltage);

        for (int i = 0; i < Shared.AdaptationCycles; i++) {
            vnew = capacitor.getCapacitorVoltageLevel();

            double rnewController = controller.update(3.7, vnew);

            System.out.printf("%.2f;%d;\n", vnew, Math.round(rnewController));
            //System.out.printf("%.2f;%d;%d\n", vnew, Math.round((double) rnewController));
        }
        System.out.println("End...");
    }
}
