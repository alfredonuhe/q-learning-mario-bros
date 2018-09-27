package ch.idsia.utils;

public class EvaluadorPertenecia {
    private static final int SITUACION_CERCA_OBSTACULO = 0;
    private static final int SITUACION_AVANZAR = 1;
    private static final int SITUACION_MONEDA_DELANTE = 2;
    private static final int SITUACION_MONEDA_DETRAS = 3;
    private static final int SITUACION_IRROMPIBLE = 4;
    private static final int SITUACION_MARIO_CAYENDO = 5;
    private static final int SITUACION_MUY_CERCA_ENEMIGO = 6;


    public EvaluadorPertenecia() {
    }

    /**
     * Método que devuelve la situación a la que pertenece una instancia del juego según la distancia euclídea con pesos
     * a prototipos de situación definidos, eligiendo la situación que está a menor distancia.
     *
     * @param linea String que representa la instancia del juego.
     * @return Número de situación a la que pertenece la instancia.
     */
    public int getPertenecia(String[] linea) {
        int situacion;
        int matrix_6_9 = Integer.parseInt(linea[99]);
        int matriz_7_9 = Integer.parseInt(linea[114]);
        int marioCanJump = Integer.parseInt(linea[288]);
        int nearObstacle = Integer.parseInt(linea[296]);
        int marioCanWalkThrough = Integer.parseInt(linea[301]);
        double distanceCoin = Double.parseDouble(linea[302]);
        int veryNearEnemy = Integer.parseInt(linea[305]);
        int coinBehind = Integer.parseInt(linea[306]);
        int marioIsDescending = Integer.parseInt(linea[307]);
        int marioIsAscending = Integer.parseInt(linea[308]);

        if ((matrix_6_9 == -60 || matriz_7_9 == -60) && marioCanWalkThrough == 0) {
            situacion = SITUACION_IRROMPIBLE;
        /*} else if (veryNearEnemy == 1 && marioCanJump == 0) {
            situacion = SITUACION_MUY_CERCA_ENEMIGO;*/
        } else if (marioIsDescending == 1) {
            situacion = SITUACION_MARIO_CAYENDO;
        } else if (marioCanWalkThrough == 0 && nearObstacle == 1 && (marioCanJump == 1 || marioIsAscending == 1)) {
            situacion = SITUACION_CERCA_OBSTACULO;
        } else if (marioCanJump == 1 && distanceCoin < 4 && coinBehind == 0) {
            situacion = SITUACION_MONEDA_DELANTE;
        } else if (marioCanJump == 1 && distanceCoin < 4 && coinBehind == 1) {
            situacion = SITUACION_MONEDA_DETRAS;
        } else {
            situacion = SITUACION_AVANZAR;
        }

        /*
        System.out.println("Situacion: " + situacion + " [ " + nearObstacle + ", "
                + veryNearEnemy + ", " + marioCanJump + ", " + marioCanWalkThrough + " ]"
                + matriz_7_9 + " " + matrix_6_9);
        */

        return situacion;
    }
}
