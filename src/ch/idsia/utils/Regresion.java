package ch.idsia.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class Regresion {
    private LinkedList<String> cadenaSeisTicks;
    private LinkedList<String> cadenaDoceTicks;
    private LinkedList<String> cadenaVeinticuatroTicks;
    private BufferedWriter bw = null;

    public static boolean REWARDN6 = false;
    public static boolean REWARDN12 = false;
    public static boolean REWARDN24 = true;

    public Regresion(BufferedWriter bw) {
        this.bw = bw;
        cadenaSeisTicks = new LinkedList<>();
        cadenaDoceTicks = new LinkedList<>();
        cadenaVeinticuatroTicks = new LinkedList<>();
    }

    public void escribirRegresion(String resultado, double distancePassedPhys, int intermediateReward) {
        if (REWARDN6) {
            cadenaSeisTicks.add(resultado);
            if (cadenaSeisTicks.size() == 6) {
                escribir(cadenaSeisTicks, resultado, distancePassedPhys, intermediateReward);
            }
        } else if (REWARDN12) {
            cadenaDoceTicks.add(resultado);
            if (cadenaDoceTicks.size() == 12) {
                escribir(cadenaDoceTicks, resultado, distancePassedPhys, intermediateReward);
            }
        } else if (REWARDN24) {
            cadenaVeinticuatroTicks.add(resultado);
            if (cadenaVeinticuatroTicks.size() == 24) {
                escribir(cadenaVeinticuatroTicks, resultado, distancePassedPhys, intermediateReward);
            }
        }
    }

    private void escribir(LinkedList<String> listaCircular, String estadoActual, double distancePassedPhys, int intermediateReward) {
        try {
            String cadenaFutura = getCadenaFutura(listaCircular, estadoActual, distancePassedPhys, intermediateReward);
            if (!cadenaFutura.equals("")) {
                bw.write(cadenaFutura);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCadenaFutura(LinkedList<String> linkedList, String estadoActual, double distancePassedPhys, int intermediateReward) {
        String cadenaExtraida = linkedList.remove();
        LinkedList<String> datosCadena = new LinkedList<>(Arrays.asList(cadenaExtraida.split(", ")));
        LinkedList<String> listaEstadoActual = new LinkedList<>(Arrays.asList(estadoActual.split(", ")));
        int rewardActual = Integer.parseInt(datosCadena.remove(datosCadena.size() - 1));
        int distanceActual = Integer.parseInt(datosCadena.remove(datosCadena.size() - 1));
        int rewardFuturo = intermediateReward - rewardActual;
        double distanceFuturo = distancePassedPhys - distanceActual;

        for (int i = 0; i < 3; i++) listaEstadoActual.removeLast();
        estadoActual = listaEstadoActual.toString().replace("[", "").replace("]", "");

        cadenaExtraida = datosCadena.toString()
                .replace("[", "").replace("]", "")
                .concat(", ")
                .concat(estadoActual)
                .concat(", ")
                .concat(String.valueOf(distanceFuturo))
                .concat(", ")
                .concat(String.valueOf(rewardFuturo)).concat("\n");
        return cadenaExtraida;
    }

    public static String getFileName(String ruta) {
        if (Regresion.REWARDN6) {
            ruta = "regresion_n6_" + ruta;
        } else if (Regresion.REWARDN12) {
            ruta = "regresion_n12_" + ruta;
        } else if (Regresion.REWARDN24) {
            ruta = "regresion_n24_" + ruta;
        }
        return ruta;
    }
}