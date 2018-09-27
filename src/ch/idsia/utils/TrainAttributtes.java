package ch.idsia.utils;

import static ch.idsia.utils.TrainFileManager.LIMIT_VISION_DEPTH;
import static ch.idsia.utils.TrainFileManager.MARIO_IN_MATRIX;

public class TrainAttributtes {
    public static final String RELATION = "@RELATION mario-train-dataset";
    public static final String DATA = "@data";
    public static final String ATTRIBUTE = "@ATTRIBUTE ";
    public static final String MERGED_MATRIX = "merged_matrix";
    public static final String MARIO_MODE = "mario_mode {0, 1, 2}";
    public static final String MARIO_STATUS = "mario_status {0, 1, 2}";
    public static final String MARIO_MOVING = "mario_moving {0, 1}";
    public static final String MARIO_CAN_JUMP = "mario_can_jump {0, 1}";
    public static final String LADRILLOS_EN_PANTALLA = "ladrillos_en_pantalla {0, 1}";
    public static final String ENEMIGOS_EN_PANTALLA = "enemigos_en_pantalla {0, 1}";
    public static final String KILLS_TOTAL = "kills_total NUMERIC";
    public static final String CLOSEST_ENEMY = "closest_enemy NUMERIC";
    public static final String CLOSEST_OBJECT = "closest_object NUMERIC";
    public static final String CLOSEST_COIN = "closest_coin NUMERIC";
    public static final String APROXIMATING_ENEMY = "aproximating_enemy {0, 1}";
    public static final String APROXIMATING_OBJECT = "aproximating_object {0, 1}";
    public static final String NEAR_OBSTACLE = "near_obstacle {0, 1}";
    public static final String VERY_NEAR_ENEMY = "very_near_enemy {0, 1}";
    public static final String DISTANCE_PASSED_PHYS_N24 = "distance_passed_phys NUMERIC";
    public static final String LATITUDE_ENEMY = "latitude_enemy NUMERIC";
    public static final String LATITUDE_OBJECT = "latitude_object NUMERIC";
    public static final String ENEMY_HEIGHT = "enemy_height NUMERIC";
    public static final String OBJECT_HEIGHT = "object_height NUMERIC";
    public static final String TICKS_MARIO_SALTO = "ticks_mario_salto NUMERIC";
    public static final String MARIO_CAN_WALK_THROUGH = "mario_can_walk_through {0, 1}";
    public static final String MONEDAS_EN_PANTALLA = "monedas_en_pantalla {0, 1}";
    public static final String ACTION = "action {RIGHT, JUMP, JUMP_RIGHT, NONE}";
    public static final String SITUACION = "situacion {0, 1, 2, 3}";
    public static final String INTERMEDIATE_REWARD_N6 = "intermediate_reward_n6 NUMERIC";
    public static final String INTERMEDIATE_REWARD_N12 = "intermediate_reward_n12 NUMERIC";
    public static final String INTERMEDIATE_REWARD_N24 = "intermediate_reward_n24 NUMERIC";
    public static final String CLOSEST_COIN_BEHIND = "closes_coin_behind {0, 1}";
    public static final String MARIO_IS_DESCENDING = "mario_is_descending {0, 1}";
    public static final String MARIO_IS_ASCENDING = "mario_is_ascending {0, 1}";

    public static final String MARIO_MODE_N24 = "mario_mode_n24 {0, 1, 2}";
    public static final String MARIO_STATUS_N24 = "mario_status_n24 {0, 1, 2}";
    public static final String MARIO_MOVING_N24 = "mario_moving_n24 {0, 1}";
    public static final String MARIO_CAN_JUMP_N24 = "mario_can_jump_n24 {0, 1}";
    public static final String LADRILLOS_EN_PANTALLA_N24 = "ladrillos_en_pantalla_n24 {0, 1}";
    public static final String ENEMIGOS_EN_PANTALLA_N24 = "enemigos_en_pantalla_n24 {0, 1}";
    public static final String KILLS_TOTAL_N24 = "kills_total_n24 NUMERIC";
    public static final String CLOSEST_ENEMY_N24 = "closest_enemy_n24 NUMERIC";
    public static final String CLOSEST_OBJECT_N24 = "closest_object_n24 NUMERIC";
    public static final String CLOSEST_COIN_N24 = "closest_coin_n24 NUMERIC";
    public static final String APROXIMATING_ENEMY_N24 = "aproximating_enemy_n24 {0, 1}";
    public static final String APROXIMATING_OBJECT_N24 = "aproximating_object_n24 {0, 1}";
    public static final String NEAR_OBSTACLE_N24 = "near_obstacle_n24 {0, 1}";
    public static final String VERY_NEAR_ENEMY_N24 = "very_near_enemy_n24 {0, 1}";
    public static final String LATITUDE_ENEMY_N24 = "latitude_enemy_n24 NUMERIC";
    public static final String LATITUDE_OBJECT_N24 = "latitude_object_n24 NUMERIC";
    public static final String ENEMY_HEIGHT_N24 = "enemy_height_n24 NUMERIC";
    public static final String OBJECT_HEIGHT_N24 = "object_height_n24 NUMERIC";
    public static final String TICKS_MARIO_SALTO_N24 = "ticks_mario_salto_n24 NUMERIC";
    public static final String MARIO_CAN_WALK_THROUGH_N24 = "mario_can_walk_through_n24 {0, 1}";
    public static final String MONEDAS_EN_PANTALLA_N24 = "monedas_en_pantalla_n24 {0, 1}";
    public static final String CLOSEST_COIN_BEHIND_N24 = "closes_coin_behind_n24 {0, 1}";
    public static final String MARIO_IS_DESCENDING_N24 = "mario_is_descending_n24 {0, 1}";
    public static final String MARIO_IS_ASCENDING_N24 = "mario_is_ascending_n24 {0, 1}";

    /**
     * Método que construye la cabecera del fichero .arff para su posterior utilización en Weka
     */
    public static String buildCabecera() {
        String cabecera = RELATION + "\n\n\t";

        /*Cabecera para el estado n*/

        for (int i = 0; i < TrainFileManager.MATRIX_SIZE; i++) {
            for (int j = 0; j < TrainFileManager.MATRIX_SIZE; j++) {
                if (j < MARIO_IN_MATRIX + LIMIT_VISION_DEPTH) {
                    cabecera = cabecera.concat(ATTRIBUTE + MERGED_MATRIX + "_" + i + "_" + j + " NUMERIC\n\t");
                }
            }
        }
        cabecera = cabecera.concat(ATTRIBUTE + MARIO_MODE + "\n\t")
                .concat(ATTRIBUTE + MARIO_STATUS + "\n\t")
                .concat(ATTRIBUTE + MARIO_MOVING + "\n\t")
                .concat(ATTRIBUTE + MARIO_CAN_JUMP + "\n\t")
                .concat(ATTRIBUTE + LADRILLOS_EN_PANTALLA + "\n\t")
                .concat(ATTRIBUTE + ENEMIGOS_EN_PANTALLA + "\n\t")
                .concat(ATTRIBUTE + KILLS_TOTAL + "\n\t")
                .concat(ATTRIBUTE + CLOSEST_ENEMY + "\n\t")
                .concat(ATTRIBUTE + CLOSEST_OBJECT + "\n\t")
                .concat(ATTRIBUTE + APROXIMATING_ENEMY + "\n\t")
                .concat(ATTRIBUTE + APROXIMATING_OBJECT + "\n\t")
                .concat(ATTRIBUTE + NEAR_OBSTACLE + "\n\t")
                .concat(ATTRIBUTE + LATITUDE_ENEMY + "\n\t")
                .concat(ATTRIBUTE + LATITUDE_OBJECT + "\n\t")
                .concat(ATTRIBUTE + ENEMY_HEIGHT + "\n\t")
                .concat(ATTRIBUTE + OBJECT_HEIGHT + "\n\t")
                .concat(ATTRIBUTE + MARIO_CAN_WALK_THROUGH + "\n\t")
                .concat(ATTRIBUTE + CLOSEST_COIN + "\n\t")
                .concat(ATTRIBUTE + MONEDAS_EN_PANTALLA + "\n\t")
                .concat(ATTRIBUTE + TICKS_MARIO_SALTO + "\n\t")
                .concat(ATTRIBUTE + VERY_NEAR_ENEMY + "\n\t")
                .concat(ATTRIBUTE + CLOSEST_COIN_BEHIND + "\n\t")
                .concat(ATTRIBUTE + MARIO_IS_DESCENDING + "\n\t")
                .concat(ATTRIBUTE + MARIO_IS_ASCENDING + "\n\t")
                .concat(ATTRIBUTE + ACTION + "\n\t");

        /*Cabecera para el estado n+24*/

        for (int i = 0; i < TrainFileManager.MATRIX_SIZE; i++) {
            for (int j = 0; j < TrainFileManager.MATRIX_SIZE; j++) {
                if (j < MARIO_IN_MATRIX + LIMIT_VISION_DEPTH) {
                    cabecera = cabecera.concat(ATTRIBUTE + MERGED_MATRIX + "_" + i + "_" + j + "_n24 NUMERIC\n\t");
                }
            }
        }

        cabecera = cabecera.concat(ATTRIBUTE + MARIO_MODE_N24 + "\n\t")
                .concat(ATTRIBUTE + MARIO_STATUS_N24 + "\n\t")
                .concat(ATTRIBUTE + MARIO_MOVING_N24 + "\n\t")
                .concat(ATTRIBUTE + MARIO_CAN_JUMP_N24 + "\n\t")
                .concat(ATTRIBUTE + LADRILLOS_EN_PANTALLA_N24 + "\n\t")
                .concat(ATTRIBUTE + ENEMIGOS_EN_PANTALLA_N24 + "\n\t")
                .concat(ATTRIBUTE + KILLS_TOTAL_N24 + "\n\t")
                .concat(ATTRIBUTE + CLOSEST_ENEMY_N24 + "\n\t")
                .concat(ATTRIBUTE + CLOSEST_OBJECT_N24 + "\n\t")
                .concat(ATTRIBUTE + APROXIMATING_ENEMY_N24 + "\n\t")
                .concat(ATTRIBUTE + APROXIMATING_OBJECT_N24 + "\n\t")
                .concat(ATTRIBUTE + NEAR_OBSTACLE_N24 + "\n\t")
                .concat(ATTRIBUTE + LATITUDE_ENEMY_N24 + "\n\t")
                .concat(ATTRIBUTE + LATITUDE_OBJECT_N24 + "\n\t")
                .concat(ATTRIBUTE + ENEMY_HEIGHT_N24 + "\n\t")
                .concat(ATTRIBUTE + OBJECT_HEIGHT_N24 + "\n\t")
                .concat(ATTRIBUTE + MARIO_CAN_WALK_THROUGH_N24 + "\n\t")
                .concat(ATTRIBUTE + CLOSEST_COIN_N24 + "\n\t")
                .concat(ATTRIBUTE + MONEDAS_EN_PANTALLA_N24 + "\n\t")
                .concat(ATTRIBUTE + TICKS_MARIO_SALTO_N24 + "\n\t")
                .concat(ATTRIBUTE + VERY_NEAR_ENEMY_N24 + "\n\t")
                .concat(ATTRIBUTE + CLOSEST_COIN_BEHIND_N24 + "\n\t")
                .concat(ATTRIBUTE + MARIO_IS_DESCENDING_N24 + "\n\t")
                .concat(ATTRIBUTE + MARIO_IS_ASCENDING_N24 + "\n\t")
                .concat(ATTRIBUTE + DISTANCE_PASSED_PHYS_N24 + "\n\t")
                .concat(ATTRIBUTE + INTERMEDIATE_REWARD_N24 + "\n\t")
                .concat(DATA + "\n");

        return cabecera;
    }

    public static String buildCabeceraRegresion() {
        String cabecera = RELATION + "_regresion";
        if (Regresion.REWARDN6) {
            cabecera = cabecera.concat("_n6" + "\n\n\t");
        } else if (Regresion.REWARDN12) {
            cabecera = cabecera.concat("_n12" + "\n\n\t");
        } else if (Regresion.REWARDN24) {
            cabecera = cabecera.concat("_n24" + "\n\n\t");
        }

        for (int i = 0; i < TrainFileManager.MATRIX_SIZE; i++) {
            for (int j = 0; j < TrainFileManager.MATRIX_SIZE; j++) {
                cabecera = cabecera.concat(ATTRIBUTE + MERGED_MATRIX + "_" + i + "_" + j + " NUMERIC\n\t");
            }
        }
        cabecera = cabecera.concat(ATTRIBUTE + MARIO_MODE + "\n\t")
                .concat(ATTRIBUTE + MARIO_STATUS + "\n\t")
                .concat(ATTRIBUTE + MARIO_MOVING + "\n\t")
                .concat(ATTRIBUTE + MARIO_CAN_JUMP + "\n\t")
                .concat(ATTRIBUTE + LADRILLOS_EN_PANTALLA + "\n\t")
                .concat(ATTRIBUTE + ENEMIGOS_EN_PANTALLA + "\n\t")
                .concat(ATTRIBUTE + KILLS_TOTAL + "\n\t")
                .concat(ATTRIBUTE + CLOSEST_ENEMY + "\n\t")
                .concat(ATTRIBUTE + CLOSEST_OBJECT + "\n\t")
                .concat(ATTRIBUTE + APROXIMATING_ENEMY + "\n\t")
                .concat(ATTRIBUTE + APROXIMATING_OBJECT + "\n\t")
                .concat(ATTRIBUTE + LATITUDE_ENEMY + "\n\t")
                .concat(ATTRIBUTE + LATITUDE_OBJECT + "\n\t")
                .concat(ATTRIBUTE + ENEMY_HEIGHT + "\n\t")
                .concat(ATTRIBUTE + OBJECT_HEIGHT + "\n\t")
                .concat(ATTRIBUTE + MARIO_CAN_WALK_THROUGH + "\n\t")
                .concat(ATTRIBUTE + MONEDAS_EN_PANTALLA + "\n\t")
                .concat(ATTRIBUTE + CLOSEST_COIN + "\n\t");
        if (Regresion.REWARDN6) {
            cabecera = cabecera.concat(ATTRIBUTE + INTERMEDIATE_REWARD_N6 + "\n\t");
        } else if (Regresion.REWARDN12) {
            cabecera = cabecera.concat(ATTRIBUTE + INTERMEDIATE_REWARD_N12 + "\n\t");
        } else if (Regresion.REWARDN24) {
            cabecera = cabecera.concat(ATTRIBUTE + INTERMEDIATE_REWARD_N24 + "\n\t");
        }
        cabecera = cabecera.concat(DATA + "\n");

        return cabecera;
    }
}