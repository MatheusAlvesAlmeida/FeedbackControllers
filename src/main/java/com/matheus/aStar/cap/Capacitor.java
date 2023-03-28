package com.matheus.aStar.cap;

import java.util.Random;

import com.matheus.shared.Shared;

public class Capacitor {
    private double previousVoltageLevel;
    private int pattern;
    private int previousPattern;
    private int countCycles;
    private int state;
    private int indx;

    Random random = new Random();

    private static double[] voltageFromFile = { 5.156, 4.95, 4.85, 4.788, 4.75, 4.743, 4.692, 4.634, 4.634, 4.582,
            4.556, 4.537, 4.537, 4.537, 4.692, 4.95, 5.143, 5.336, 5.568, 5.568, 5.588, 5.568, 5.517, 5.459, 5.407,
            5.356, 5.356, 5.336, 5.31, 5.259, 5.2, 5.149, 5.149, 5.13, 5.104, 5.046, 5.04, 4.995, 4.956, 4.95, 4.95,
            4.95, 4.95, 4.95, 4.95, 5.149, 5.407, 5.459, 5.452, 5.407, 5.542, 5.459, 5.459, 5.42, 5.39, 5.35, 5.31,
            5.31, 5.259, 5.201, 5.15, 5.135, 5.13, 5.12, 5.12, 5.11, 5.11, 4.98, 4.956, 4.95, 4.95, 4.95, 4.95, 4.95,
            4.995, 5, 5.356, 5.459, 5.459, 5.54, 5.5, 5.49, 5.49, 5.47, 5.47, 5.356, 5.356, 5.349, 5.31, 5.31, 5.259,
            5.233, 5.201, 5.149, 5.149, 5.13, 5.104, 5.046, 4.995, 4.995, 4.956, 4.95, 4.956, 4.995, 4.995, 5.046,
            5.149, 5.259, 5.31, 5.31, 5.259, 5.201, 5.201, 5.149, 5.149, 5.13, 5.104, 5.046, 4.995, 4.995, 4.956, 4.95,
            4.95, 4.95, 4.95, 4.95, 4.95, 4.763, 4.743, 4.743, 4.692, 4.692 };

    public double getCapacitorVoltageLevelFromFile() {
        double r = voltageFromFile[indx];
        indx++;

        if (indx >= voltageFromFile.length) {
            indx = 0;
        }
        return r;
    }

    public double getCapacitorVoltageLevel() {
        double capacitorVoltage = 0.0;
        int pattern = this.pattern;

        if (this.pattern == Shared.RandomHarvesting) {
            switch (this.state) {
                case 0:
                    pattern = new Random().nextInt(Shared.RandomHarvesting);
                    this.previousPattern = pattern;
                    this.state = 1;
                    break;
                case 1:
                    if (this.countCycles < Shared.SamplingCycleSize) {
                        pattern = this.previousPattern;
                        this.countCycles++;
                    } else {
                        this.countCycles = 0;
                        while (true) {
                            pattern = new Random().nextInt(Shared.RandomHarvesting);
                            if (pattern != this.previousPattern) {
                                this.previousPattern = pattern;
                                break;
                            }
                        }
                    }
                    break;
            }
        }

        switch (pattern) {
            case Shared.IncreasingHarvesting:
                double gain = Shared.StepVoltage * (random.nextInt(100) + 1);
                if ((this.previousVoltageLevel + gain) > Shared.MaximumVoltage) {
                    capacitorVoltage = Shared.MaximumVoltage;
                } else {
                    capacitorVoltage = this.previousVoltageLevel + gain;
                }
                break;
            case Shared.HalfIncreasingHarvesting:
                gain = Shared.StepVoltage * (random.nextInt(100) + 1) / 2.0;
                if ((this.previousVoltageLevel + gain) > Shared.MaximumVoltage) {
                    capacitorVoltage = Shared.MaximumVoltage;
                } else {
                    capacitorVoltage = this.previousVoltageLevel + gain;
                }
                break;
            case Shared.QuarterIncreasingHarvesting:
                gain = Shared.StepVoltage * (random.nextInt(100) + 1) / 4.0;
                if ((this.previousVoltageLevel + gain) > Shared.MaximumVoltage) {
                    capacitorVoltage = Shared.MaximumVoltage;
                } else {
                    capacitorVoltage = this.previousVoltageLevel + gain;
                }
                break;
            case Shared.DecreasingHarvesting:
                gain = Shared.StepVoltage * (random.nextInt(100) + 1);
                if ((this.previousVoltageLevel - gain) < 0) {
                    capacitorVoltage = 0.0;
                } else {
                    capacitorVoltage = this.previousVoltageLevel - gain;
                }
                break;
            case Shared.HalfDecreasingHarvesting:
                gain = Shared.StepVoltage * (random.nextInt(100) + 1) / 2.0;
                if ((this.previousVoltageLevel - gain) < 0) {
                    capacitorVoltage = 0.0;
                } else {
                    capacitorVoltage = this.previousVoltageLevel - gain;
                }
                break;
            case Shared.QuarterDecreasingHarvesting:
                gain = Shared.StepVoltage * (random.nextInt(100) + 1) / 4.0;
                if ((this.previousVoltageLevel - gain) < 0) {
                    capacitorVoltage = 0.0;
                } else {
                    capacitorVoltage = this.previousVoltageLevel - gain;
                }
                break;
            case Shared.ConstantHarvesting:
                capacitorVoltage = Shared.StepVoltage * 5; // 2 TODO
                break;
            default:
                System.out.println("Something wrong in the cap behaviour!! " + pattern);
                System.exit(0);
                break;
        }
        this.previousVoltageLevel = capacitorVoltage;

        return capacitorVoltage;
    }

    // Create setters
    public void setPattern(int pattern) {
        this.pattern = pattern;
    }

    public void setPreviousVoltageLevel(double previousVoltageLevel) {
        this.previousVoltageLevel = previousVoltageLevel;
    }

    public double getPreviousVoltageLevel(){
        return this.previousVoltageLevel;
    }

}