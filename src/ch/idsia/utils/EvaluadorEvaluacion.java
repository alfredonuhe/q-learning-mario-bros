package ch.idsia.utils;

public class EvaluadorEvaluacion {
    private static final double[] PESOS = new double[]{20, 2};

    public EvaluadorEvaluacion() {
    }

    public double getEvaluacion(String distancePassedPhys, String intermediateReward) {
        double result;
        result = PESOS[0] * Double.parseDouble(distancePassedPhys)
                + PESOS[1] * Double.parseDouble(intermediateReward);
        if (result < 0) {
            return 0;
        } else {
            return result;
        }
    }
}
