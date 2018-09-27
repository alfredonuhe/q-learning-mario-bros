package ch.idsia.utils;

import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.environments.MarioEnvironment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;

public class TrainFileManager {
    private BufferedWriter bw = null;
    private Regresion regresion = null;
    private EvaluadorPertenecia evaluator = null;
    private String ruta;
    private String prevAction = "";
    private static double f1Coef;
    private static double f2Coef;
    private static double f1Const;
    private static double f2Const;
    private double prevClosestEnemy = -1;
    private double prevClosestObject = -1;
    private double distanceClosestEnemy;
    private double distanceClosestObject;
    private boolean prevEnemyAproximating = false;
    private boolean prevObjectAproximating = false;
    private boolean prevMarioIsMoving = false;
    private boolean marioCanWalkThrough;
    private int prevDistancePassedPhys = -1;
    private int tick_aproximation = 0;
    private int closestEnemyLatitude;
    private int closestObjectLatitude;
    private int objectHeight;
    private int enemyHeight;
    private int ticks_mario_salto = 0;
    private boolean nearObstacle;
    private boolean veryNearEnemy;

    //Variables para configurar la regresión
    private boolean esRegresion = false;


    public static final int LIMIT_VISION_DEPTH = 6;
    public static final int MATRIX_SIZE = 19;
    public static final int MARIO_IN_MATRIX = 9;
    public static final double DISTANCE_NEAR_OBSTACLE = 3;
    public static final double DISTANCE_VERY_NEAR_OBSTACLE = 2.9;
    private static final double DEGREES_MARIO_VISION = 70;


    public TrainFileManager(String ruta) {
        if (esRegresion) {
            this.ruta = Regresion.getFileName(ruta);
        } else {
            this.ruta = ruta;
        }
        FileWriter fw = null;
        calcMarioVisionParam(DEGREES_MARIO_VISION);
        checkHeader();

        try {
            fw = new FileWriter(this.ruta, true);
        } catch (IOException e) {
            System.out.println("El archivo no se ha podido crear.");
            e.printStackTrace();
        }
        if (fw != null) {
            bw = new BufferedWriter(fw);
        }
        regresion = new Regresion(bw);
        evaluator = new EvaluadorPertenecia();
    }


    /**
     * Método que lee el archivo con los datos de entrenamiento y borra la última línea de este.
     */
    public void deleteLastLine() {
        RandomAccessFile f = null;
        try {
            f = new RandomAccessFile(ruta, "rw");
            long length;
            length = f.length() - 1;
            byte b = 0;
            while (b != 10 && length > 0) {
                length -= 1;
                f.seek(length);
                b = f.readByte();
            }
            if (length == 0) {
                f.setLength(length);
            } else {
                f.setLength(length + 1);
            }
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que comprueba si hay una cabecera escrita, y, si no, la escribe
     */
    private void checkHeader() {
        RandomAccessFile f = null;
        try {
            f = new RandomAccessFile(ruta, "rw");
            byte[] b = new byte[9];
            f.read(b, 0, 9);
            if (!Objects.equals(new String(b), "@RELATION")) {
                f.seek(0);
                if (esRegresion) {
                    f.write(TrainAttributtes.buildCabeceraRegresion().getBytes());
                } else {
                    f.write(TrainAttributtes.buildCabecera().getBytes());
                }
            }
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Escritura del archivo con los datos de entrenamiento que se pueden obtener del estado del mundo. Se van
     * concatenando valores con comas para finalizar con una única cadena de caracteres que contenga todos los datos
     * separados por comas y puedan ser escritos de una sola vez.
     */
    public void escrituraDatos(Environment environment, boolean[] action) {
        String actionMario;
        boolean marioIsMoving = prevMarioIsMoving;
        int[] infoEvaluacion;
        infoEvaluacion = environment.getEvaluationInfoAsInts();
        int intermediateReward = environment.getIntermediateReward();

        /*Los booleanos que representan las acciones se codifican en 0 o 1 */
        double action_code = 0;
        for (int i = 0; action.length > i; i++) {
            if (action[i]) action_code = action_code + Math.pow(2, i);
        }

        /*Asignación de valor dependiendo de código binario*/
        switch ((int) action_code) {
            case 2:
                actionMario = "RIGHT";
                break;
            case 8:
                actionMario = "JUMP";
                break;
            case 10:
                actionMario = "JUMP_RIGHT";
                break;
            case 32:
                actionMario = "LEFT";
                break;
            case 36:
                actionMario = "JUMP_LEFT";
                break;
            default:
                actionMario = "NONE";
                break;
        }

        String datos = getDatos(environment);
        datos = datos.concat(actionMario.concat(", "));

        datos = datos.concat(String.valueOf(infoEvaluacion[1]).concat(", "));
        datos = datos.concat(String.valueOf(intermediateReward).concat(", "));


        regresion.escribirRegresion(datos, infoEvaluacion[1], intermediateReward);

        prevAction = actionMario;
        tick_aproximation++;
    }

    /**
     * Método que devuelve un string que contiene los datos que pueden ser obtenidos del entorno
     * en tiempo real.
     *
     * @param environment Variable de entorno del juego
     * @return Cadena que contiene los datos
     */
    public String getDatos(Environment environment) {
        boolean enemigosEnPantalla = false;
        boolean ladrillosEnPantalla = false;
        boolean monedasEnPantalla = false;
        boolean enemyApproximating = prevEnemyAproximating;
        boolean objectApproximating = prevObjectAproximating;
        boolean marioIsMoving = prevMarioIsMoving;
        boolean marioIsAbleToJump = environment.isMarioAbleToJump();
        double distanceClosestCoin = 10;
        boolean closestCoinBehind = false;
        boolean marioIsDescending = false;
        boolean marioIsAscending = false;
        StringBuilder datos = new StringBuilder();

        closestEnemyLatitude = -10;
        closestObjectLatitude = -10;
        enemyHeight = 0;
        objectHeight = 0;
        distanceClosestObject = 10;
        distanceClosestEnemy = 10;
        marioCanWalkThrough = true;
        nearObstacle = false;
        veryNearEnemy = false;

        byte[][] env;
        env = environment.getMergedObservationZZ(2, 1);

        int[] infoEvaluacion;
        infoEvaluacion = environment.getEvaluationInfoAsInts();

        /*Cálculo de mergedMatrix, closestEnemy, closestEnemyLatitude, closestObject, closestObjectLatitude,
         * ladrillosEnPantalla y enemigosEnPantalla */
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                if (j < MARIO_IN_MATRIX + LIMIT_VISION_DEPTH) datos.append(String.valueOf(env[i][j])).append(", ");
                /*Se calcula la distancia y latitud de los enemigos del entorno*/
                if ((env[i][j] == 80)) {
                    enemigosEnPantalla = true;
                    if (euclideanDistance(MARIO_IN_MATRIX, MARIO_IN_MATRIX, i, j) < distanceClosestEnemy) {
                        calcEnemObjData(i, j, env, infoEvaluacion, true);
                    }
                }
                /*Se calcula la distancia y latitud de los objetos que se encuentren en el campo de visión de mario*/
                if (env[i][j] == -60 || env[i][j] == 1) {
                    ladrillosEnPantalla = true;
                    if ((euclideanDistance(MARIO_IN_MATRIX, MARIO_IN_MATRIX, i, j) < distanceClosestObject)
                            && i <= MARIO_IN_MATRIX) {
                        calcEnemObjData(i, j, env, infoEvaluacion, false);
                    }
                }
                if (env[i][j] == 2) {
                    monedasEnPantalla = true;
                    if (euclideanDistance(MARIO_IN_MATRIX, MARIO_IN_MATRIX, i, j) < distanceClosestCoin) {
                        distanceClosestCoin = euclideanDistance(MARIO_IN_MATRIX, MARIO_IN_MATRIX, i, j);
                        closestCoinBehind = MARIO_IN_MATRIX > j;
                    }
                }
            }
        }

        if (!environment.isMarioAbleToJump()) ticks_mario_salto++;
        else ticks_mario_salto = 0;

        /*Comprobación cada cinco tics si existe una aproximación de enemigos u objetos */
        if (tick_aproximation % 5 == 0) {
            enemyApproximating = prevClosestEnemy > distanceClosestEnemy;
            objectApproximating = prevClosestObject > distanceClosestObject;
            marioIsMoving = prevDistancePassedPhys < infoEvaluacion[1];
            prevEnemyAproximating = enemyApproximating;
            prevObjectAproximating = objectApproximating;
            prevMarioIsMoving = marioIsMoving;
            prevClosestEnemy = distanceClosestEnemy;
            prevClosestObject = distanceClosestObject;
            prevDistancePassedPhys = infoEvaluacion[1];
        }

        /*Cálculo de veryNearEnemy y nearObstacle*/
        if (distanceClosestEnemy <= DISTANCE_NEAR_OBSTACLE
                || distanceClosestObject <= DISTANCE_NEAR_OBSTACLE) nearObstacle = true;
        if (distanceClosestEnemy <= DISTANCE_VERY_NEAR_OBSTACLE) veryNearEnemy = true;

        if (MarioEnvironment.getInstance().getMario().yOld < MarioEnvironment.getInstance().getMario().y) {
            marioIsDescending = true;
        } else if (MarioEnvironment.getInstance().getMario().yOld > MarioEnvironment.getInstance().getMario().y) {
            marioIsAscending = true;
        }

        /*Escritura de atributos a incluir en archivo .arff*/
        datos.append(String.valueOf(infoEvaluacion[7]).concat(", "))
                .append(String.valueOf(infoEvaluacion[8]).concat(", "))
                .append(String.valueOf(Boolean.compare(marioIsMoving, false)).concat(", "))
                .append(String.valueOf(Boolean.compare(marioIsAbleToJump, false)).concat(", "))
                .append(String.valueOf(Boolean.compare(ladrillosEnPantalla, false)).concat(", "))
                .append(String.valueOf(Boolean.compare(enemigosEnPantalla, false)).concat(", "))
                .append(String.valueOf(infoEvaluacion[6]).concat(", "))
                .append(String.valueOf(distanceClosestEnemy).concat(", "))
                .append(String.valueOf(distanceClosestObject).concat(", "))
                .append(String.valueOf(Boolean.compare(enemyApproximating, false)).concat(", "))
                .append(String.valueOf(Boolean.compare(objectApproximating, false)).concat(", "))
                .append(String.valueOf(Boolean.compare(nearObstacle, false)).concat(", "))
                .append(String.valueOf(closestEnemyLatitude).concat(", "))
                .append(String.valueOf(closestObjectLatitude).concat(", "))
                .append(String.valueOf(enemyHeight).concat(", "))
                .append(String.valueOf(objectHeight).concat(", "))
                .append(String.valueOf(Boolean.compare(marioCanWalkThrough, false)).concat(", "))
                .append(String.valueOf(distanceClosestCoin).concat(", "))
                .append(String.valueOf(Boolean.compare(monedasEnPantalla, false)).concat(", "))
                .append(String.valueOf(ticks_mario_salto).concat(", "))
                .append(String.valueOf(Boolean.compare(veryNearEnemy, false)).concat(", "))
                .append(String.valueOf(Boolean.compare(closestCoinBehind, false)).concat(", "))
                .append(String.valueOf(Boolean.compare(marioIsDescending, false)).concat(", "))
                .append(String.valueOf(Boolean.compare(marioIsAscending, false)).concat(", "));
        return datos.toString();
    }


    /*Calcula la distanca euclidea entre dos celdas de la matriz*/
    public static double euclideanDistance(int posx1, int posy1, int posx2, int posy2) {
        return Math.sqrt(Math.pow(posx2 - posx1, 2) + Math.pow(posy2 - posy1, 2));
    }

    /*Calcula si la celda se encuentra dentro de la visión definida para Mario*/
    public static boolean cellInMarioVision(int i, int j) {
        int y = (i - 18) * (-1);
        int x = j;
        if (y - (x * f1Coef) - f1Const <= 0 && y - (x * f2Coef) - f2Const >= 0) return true;
        return false;
    }

    /*Calcula si Mario puede seguir avanzando hacia delante sin toparse con un objeto o enemigo*/
    public static boolean canMarioWalkThrough(int j, byte[][] env, int[] infoEvaluacion, boolean isEnemy) {
        if (isEnemy) {
            if (infoEvaluacion[7] == 0) {
                for (int i = MARIO_IN_MATRIX; i < MARIO_IN_MATRIX + DISTANCE_NEAR_OBSTACLE; i++) {
                    if (env[MARIO_IN_MATRIX][i] == 80) return false;
                }
            } else {
                for (int i = MARIO_IN_MATRIX; i < MARIO_IN_MATRIX + DISTANCE_NEAR_OBSTACLE; i++) {
                    if ((env[MARIO_IN_MATRIX][i] == 80) || (env[MARIO_IN_MATRIX - 1][i] == 80)) return false;
                }
            }
        } else {
            if (infoEvaluacion[7] == 0) {
                for (int i = MARIO_IN_MATRIX; i < MARIO_IN_MATRIX + DISTANCE_NEAR_OBSTACLE; i++) {
                    if (env[MARIO_IN_MATRIX][i] != 0 && env[MARIO_IN_MATRIX][i] != 2 && env[9][i] != 80)
                        return false;
                }
            } else {
                for (int i = MARIO_IN_MATRIX; i < MARIO_IN_MATRIX + DISTANCE_NEAR_OBSTACLE; i++) {
                    if ((env[MARIO_IN_MATRIX][i] != 0 && env[MARIO_IN_MATRIX][i] != 2 && env[MARIO_IN_MATRIX][i] != 80)
                            || (env[MARIO_IN_MATRIX - 1][i] != 0 && env[MARIO_IN_MATRIX - 1][i] != 2 && env[MARIO_IN_MATRIX - 1][i] != 80))
                        return false;
                }
            }
        }
        return true;
    }

    /*Calcula la altura de un enemigo u objeto en relación a la altura de Mario*/
    public static int calcObstacleHeight(int i, int j, byte[][] env) {
        int altura = i;
        while (altura > 0 && env[i][j] == env[altura][j]) altura--;
        altura = i - altura;
        return altura;
    }

    /*Calcula distintos atributos de objetos y enemigos*/
    public void calcEnemObjData(int i, int j, byte[][] env, int[] infoEvaluacion, boolean isEnemy) {
        if (isEnemy) {
            distanceClosestEnemy = euclideanDistance(MARIO_IN_MATRIX, MARIO_IN_MATRIX, i, j);
            closestEnemyLatitude = (i - MARIO_IN_MATRIX) * (-1);
            enemyHeight = calcObstacleHeight(i, j, env);
        } else {
            distanceClosestObject = euclideanDistance(MARIO_IN_MATRIX, MARIO_IN_MATRIX, i, j);
            closestObjectLatitude = (i - MARIO_IN_MATRIX) * (-1);
            objectHeight = calcObstacleHeight(i, j, env);
        }
        if (marioCanWalkThrough) marioCanWalkThrough = canMarioWalkThrough(j, env, infoEvaluacion, isEnemy);
    }

    /*Calcula las funciones de vision de Mario que acotan el grabado de datos de merged_matrix*/
    public static void calcMarioVisionParam(double percentage) {
        percentage = Math.toRadians(percentage % 90);
        double f1_x1 = MARIO_IN_MATRIX;
        double f1_y1 = MARIO_IN_MATRIX;
        double f1_x2 = f1_x1 + 1;
        double f1_y2 = (Math.sin(percentage) / Math.cos(percentage)) + f1_y1;
        f1Coef = (f1_y2 - f1_y1) / (f1_x2 - f1_x1);
        f1Const = f1_y1 - (f1Coef * f1_x1);
        f2Coef = f1Coef * (-1);
        f2Const = (2 * MARIO_IN_MATRIX) - f1Const;
    }
}
