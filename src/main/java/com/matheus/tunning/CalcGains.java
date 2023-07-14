package com.matheus.tunning;

public class CalcGains {
    public static void calculateRootLocusGains(double[][] data, String typeName) {
        double sumU = 0.0;
        double sumY = 0.0;
        int dataSize = data.length;

        for (int i = 0; i < dataSize - 1; i++) {
            sumU += data[i][0]; // Assuming PC values are in the first column
        }

        for (int i = 1; i < dataSize; i++) {
            sumY += data[i][1]; // Assuming Rate values are in the second column
        }

        double mu = sumU / (dataSize - 1);
        double my = sumY / (dataSize - 1);

        double s1 = 0.0;
        double s2 = 0.0;
        double s3 = 0.0;
        double s4 = 0.0;
        double s5 = 0.0;

        double[] uLine = new double[dataSize];
        double[] yLine = new double[dataSize];

        for (int i = 0; i < dataSize; i++) {
            uLine[i] = data[i][0] - mu;
            yLine[i] = data[i][1] - my;
        }

        for (int i = 0; i < dataSize - 1; i++) {
            s1 += Math.pow(yLine[i], 2.0);
            s2 += uLine[i] * yLine[i];
            s3 += Math.pow(uLine[i], 2.0);
            s4 += yLine[i] * yLine[i + 1];
            s5 += uLine[i] * yLine[i + 1];
        }

        double a = (s3 * s4 - s2 * s5) / (s1 * s3 - Math.pow(s2, 2.0));
        double b = (s1 * s5 - s2 * s4) / (s1 * s3 - Math.pow(s2, 2.0));

        double kp = 0.0;
        double ki = 0.0;
        double kd = 0.0;

        switch (typeName) {
            case "BasicP":
                kp = (1 + a) / b;
                ki = 0.0;
                kd = 0.0;
                break;
            case "BasicPi":
                kp = (a - 0.36) / b;
                ki = (a - b * kp) / b;
                kd = 0.0;
                break;
            case "BasicPid":
                kd = 0.11 / b;
                kp = (-0.063 + a - 2 * b * kd) / b;
                ki = (0.3 - b * kp - b * kd + a) / b;
                break;
        }

        // Print or use the calculated gains as required
        System.out.println("Kp: " + kp);
        System.out.println("Ki: " + ki);
        System.out.println("Kd: " + kd);
    }

    public static double[] calculateZieglerGains(double[][] data, String typeName) {
        double sumRate1 = 0.0;
        double sumRate2 = 0.0;
        int dataSize = data.length;

        for (int i = 2; i < dataSize; i++) {
            if (i % 2 == 0) {
                sumRate1 += data[i][1]; // Assuming Rate values are in the second column
            } else {
                sumRate2 += data[i][1]; // Assuming Rate values are in the second column
            }
        }

        double dataSizeDouble = (dataSize - 2) / 2.0;
        double meanRate1 = sumRate1 / dataSizeDouble;
        double meanRate2 = sumRate2 / dataSizeDouble;

        double diffRate = meanRate2 - meanRate1;
        double K = diffRate;

        double tau = 0.0; // Provide the value of tau
        double L = 0.0; // Provide the value of L

        double[] gains = new double[3];

        gains[0] = tau / K * L; // Kp value

        switch (typeName) {
            case "BasicP":
                gains[1] = 0.0; // Ki value
                gains[2] = 0.0; // Kd value
                break;
            case "BasicPi":
                double ti = L / 0.3;
                gains[1] = gains[0] / ti; // Ki value
                gains[2] = 0.0; // Kd value
                break;
            case "BasicPid":
                ti = 2 * L;
                double td = 0.5 * L;
                gains[1] = gains[0] / ti; // Ki value
                gains[2] = gains[0] * td; // Kd value
                break;
        }

        return gains;
    }

    public static double[] calculateCohenGains(double[][] data, String typeName) {
        double sumRate1 = 0.0;
        double sumRate2 = 0.0;
        int dataSize = data.length;

        for (int i = 2; i < dataSize; i++) {
            if (i % 2 == 0) {
                sumRate1 += data[i][1]; // Assuming Rate values are in the second column
            } else {
                sumRate2 += data[i][1]; // Assuming Rate values are in the second column
            }
        }

        double dataSizeDouble = (dataSize - 2) / 2.0;
        double meanRate1 = sumRate1 / dataSizeDouble;
        double meanRate2 = sumRate2 / dataSizeDouble;

        double diffRate = meanRate2 - meanRate1;
        double K = diffRate;

        double tau = 0.0; // Provide the value of tau
        double T = 0.0; // Provide the value of T

        double[] gains = new double[3];

        double theta = tau / (tau + T);

        gains[0] = 1 / K * (1 + (0.35 * theta / (1 - theta))) * T / tau; // Kp value

        switch (typeName) {
            case "BasicP":
                gains[1] = 0.0; // Ki value
                gains[2] = 0.0; // Kd value
                break;
            case "BasicPi":
                double ti = ((3.3 - 3.0 * theta) / (1 + 1.2 * theta)) * tau;
                gains[1] = gains[0] / ti; // Ki value
                gains[2] = 0.0; // Kd value
                break;
            case "BasicPid":
                ti = ((2.5 - 2.0 * theta) / (1 - 0.39 * theta)) * tau;
                double td = ((0.37 * (1 - theta)) / (1 - 0.81 * theta)) * tau;
                gains[1] = gains[0] / ti; // Ki value
                gains[2] = gains[0] * td; // Kd value
                break;
        }

        return gains;
    }

    public static double[] calculateAMIGOGains(double[][] data, String typeName) {
        double sumRate1 = 0.0;
        double sumRate2 = 0.0;
        int dataSize = data.length;

        for (int i = 2; i < dataSize; i++) {
            if (i % 2 == 0) {
                sumRate1 += data[i][1]; // Assuming Rate values are in the second column
            } else {
                sumRate2 += data[i][1]; // Assuming Rate values are in the second column
            }
        }

        double dataSizeDouble = (dataSize - 2) / 2.0;
        double meanRate1 = sumRate1 / dataSizeDouble;
        double meanRate2 = sumRate2 / dataSizeDouble;

        double diffRate = meanRate2 - meanRate1;
        double K = diffRate;

        double tau = 0.0; // Provide the value of tau
        double T = 0.0; // Provide the value of T

        double[] gains = new double[3];

        switch (typeName) {
            case "BasicPi":
                double ti = (0.35 + 13 * Math.pow(T, 2) / (Math.pow(T, 2) + 12 * tau * T + 7 * Math.pow(tau, 2))) * tau;
                gains[0] = 1 / K * (0.15 + (0.35 - tau * T / Math.pow(tau + T, 2)) * T / tau); // Kp value
                gains[1] = gains[0] / ti; // Ki value
                gains[2] = 0.0; // Kd value
                break;
            case "BasicPid":
                ti = ((0.4 * tau + 0.8 * T) / (tau + 0.1 * T)) * tau;
                double td = (0.5 * T / (0.3 * tau + T)) * tau;
                gains[0] = 1 / K * (0.2 + 0.45 * T / tau); // Kp value
                gains[1] = gains[0] / ti; // Ki value
                gains[2] = gains[0] * td; // Kd value
                break;
        }

        return gains;
    }

    public static void main(String[] args) {
        double[][] trainingDataRootLocus = {
                { 5459.137931, 1 },
                { 8657.853333, 2 },
                { 10821.68667, 3 },
                { 12417.12667, 4 },
                { 13191.15333, 5 },
                { 13803.63333, 6 },
                { 12838.82, 7 },
                { 14167.92, 8 },
                { 16711.89333, 9 },
                { 17171.3, 10 }
        };

        double[][] dataArray = {
                { 1, 7027.2 },
                { 2, 11952.0 },
                { 1, 6933.2 },
                { 2, 10438.6 },
                { 1, 6175.8 },
                { 2, 11957.0 },
                { 1, 6196.0 },
                { 2, 11000.0 },
                { 1, 7025.6 },
                { 2, 10996.8 },
                { 1, 6051.0 },
                { 2, 11255.2 },
                { 1, 6640.6 },
                { 2, 10662.8 },
                { 1, 5723.0 },
                { 2, 9352.6 },
                { 1, 6012.0 },
                { 2, 9779.0 },
                { 1, 7053.4 },
                { 2, 10893.4 }
        };

        System.out.println("Root Locus Gains: ");
        calculateRootLocusGains(trainingDataRootLocus, "BasicPid");

        System.out.println("Ziegler Gains: ");
        double[] zieglerGains = calculateZieglerGains(dataArray, "BasicPid");
        System.out.println("Kp: " + zieglerGains[0]);
        System.out.println("Ki: " + zieglerGains[1]);
        System.out.println("Kd: " + zieglerGains[2]);

        System.out.println("Cohen Gains: ");
        double[] cohenGains = calculateCohenGains(dataArray, "BasicPid");
        System.out.println("Kp: " + cohenGains[0]);
        System.out.println("Ki: " + cohenGains[1]);
        System.out.println("Kd: " + cohenGains[2]);

        System.out.println("AMIGO Gains: ");
        double[] amigoGains = calculateAMIGOGains(dataArray, "BasicPid");
        System.out.println("Kp: " + amigoGains[0]);
        System.out.println("Ki: " + amigoGains[1]);
        System.out.println("Kd: " + amigoGains[2]);
    }

}
