package ch.idsia.utils;

import ch.idsia.benchmark.mario.engine.sprites.Mario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BaseConocimiento {
    private static final String RUTA = "base-conocimiento.arff";
    private static final int INSTANCIAS_POR_SITUACION = 250;
    private static final double PROBABILIDAD_DE_GRABADO = 0.6;
    private static final int NUMERO_SITUACIONES = 6;
    public static Random random;
    private EvaluadorPertenecia evaluadorPertenecia;
    private EvaluadorEvaluacion evaluadorEvaluacion;
    private ArrayList<String[]> datosLectura;
    public ArrayList<QInstance> matrizConocimiento;
    public int[] limiteLectura;

    public BaseConocimiento() {
        evaluadorPertenecia = new EvaluadorPertenecia();
        datosLectura = new ArrayList<>();
        evaluadorEvaluacion = new EvaluadorEvaluacion();
        limiteLectura = new int[NUMERO_SITUACIONES];
        random = new Random(0);
        int contador = 0;

        while (seguirLeyendo()) {
            contador++;
            lecturaDatos();
        }

        matrizConocimiento = parsetoQInstances(datosLectura);

        System.out.println("Numero de vueltas al fichero: " + contador
                + "\nTamaño matrizConocimiento: " + matrizConocimiento.size()
                + "\nNumero de instancias por situacion: " + Arrays.toString(limiteLectura));

        //Código para verificar el contenido de la matriz de conocimiento

        /*for (int i = 0; i < matrizConocimiento.size(); i++) {
            System.out.println("Instancia " + i + ":\n"
                    + "\testado: " + matrizConocimiento.get(i).getEstado()
                    + "\n\taccion: " + matrizConocimiento.get(i).getAccion()
                    + "\n\testadoSiguiente: " + matrizConocimiento.get(i).getEstadoSiguiente()
                    + "\n\trefuerzo: " + matrizConocimiento.get(i).getRefuerzo()
                    + "\n--------------------------");
        }*/
    }


    /**
     * Método que lee un fichero que contiene las instancias del juego y las carga en una estructura
     * tridimensional, indexada según la situación a la que pertenezca cada instancia.
     */
    private void lecturaDatos() {
        BufferedReader br = null;
        FileReader fr = null;
        boolean readData = false;
        String sCurrentLine;
        String[] arrayAuxiliar;
        int pertenenciaLinea;

        try {
            fr = new FileReader(RUTA);
            br = new BufferedReader(fr);
            sCurrentLine = br.readLine();

            while (sCurrentLine != null) {
                if (readData) {
                    String[] linea = sCurrentLine.split(", ");
                    pertenenciaLinea = evaluadorPertenecia.getPertenecia(Arrays.copyOfRange(linea, 0, ((linea.length - 1) / 2) - 1));
                    if (limiteLectura[pertenenciaLinea] < INSTANCIAS_POR_SITUACION
                            && random.nextDouble() < PROBABILIDAD_DE_GRABADO
                            ) {
                        datosLectura.add(linea);
                        limiteLectura[pertenenciaLinea] += 1;
                    }
                }

                if (sCurrentLine.contains("@data")) readData = true;
                sCurrentLine = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Método que devuelve la mejor acción que Mario puede realizar siguiendo el algortimo de aprendizaje IBL
     *
     * @param linea Cadena con los datos de la instancia actual del juego.
     * @return Array de acciones que Mario debe realizar.
     */
    public void getNextAction(String[] linea) {
    }

    /**
     * Método que formatea una acción de String a double.
     *
     * @param action acción del agente.
     * @return devuelve la acción en formato double.
     */
    public double actionToDouble(String action) {
        double result;

        switch (action) {
            case "JUMP":
                result = 0;
                break;
            case "RIGHT":
                result = 1;
                break;
            case "JUMP_RIGHT":
                result = 2;
                break;
            case "LEFT":
                result = 3;
                break;
            case "JUMP_LEFT":
                result = 4;
                break;
            default:
                result = 5;
                break;
        }
        return result;
    }

    /**
     * Método que formatea una acción de double a boolean[].
     *
     * @param action acción del agente.
     * @return devuelve la acción en formato boolean[].
     */
    public boolean[] actionDoubleToBoolean(double action) {
        boolean[] result = new boolean[6];

        switch ((int) action) {
            case 0:
                result[Mario.KEY_JUMP] = true;
                break;
            case 1:
                result[Mario.KEY_RIGHT] = true;
                break;
            case 2:
                result[Mario.KEY_JUMP] = true;
                result[Mario.KEY_RIGHT] = true;
                break;
            case 3:
                result[Mario.KEY_LEFT] = true;
                break;
            case 4:
                result[Mario.KEY_JUMP] = true;
                result[Mario.KEY_LEFT] = true;
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * Método de pasa las lineas de datos a objetos tipo QInstance.
     *
     * @param datosLectura ArrayList con datos brutos leidos del fichero.
     * @return devuelve un nuevo ArrayList de tipo QInstance.
     */
    public ArrayList<QInstance> parsetoQInstances(ArrayList<String[]> datosLectura) {
        ArrayList<QInstance> result = new ArrayList<>();
        int tamanoMatriz = datosLectura.get(0).length - 1;
        double estado;
        double estadoSiguiente;
        double refuerzo;
        double accion;

        for (int i = 0; i < datosLectura.size(); i++) {
            estado = evaluadorPertenecia.getPertenecia(Arrays.copyOfRange(datosLectura.get(i), 0, (tamanoMatriz / 2) - 1));
            estadoSiguiente = evaluadorPertenecia.getPertenecia(Arrays.copyOfRange(datosLectura.get(i), (tamanoMatriz / 2), tamanoMatriz - 1));
            accion = actionToDouble(datosLectura.get(i)[(tamanoMatriz / 2) - 1]);
            refuerzo = evaluadorEvaluacion.getEvaluacion(datosLectura.get(i)[tamanoMatriz - 1], datosLectura.get(i)[tamanoMatriz]);
            result.add(new QInstance(estado, estadoSiguiente, accion, refuerzo));
        }

        return result;
    }

    /**
     * Método que devuelve un booleano indicando si se debe leer el archivo .arff de nuevo para conseguir
     * todas las instancias necesarias de cada situación.
     *
     * @return valor booleano que indica si hay que volver a leer el fivhero .arff.
     */
    public boolean seguirLeyendo() {
        for (int i = 0; i < limiteLectura.length; i++) {
            if (limiteLectura[i] < INSTANCIAS_POR_SITUACION) return true;
        }
        return false;
    }

}
