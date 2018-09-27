package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QBot extends BasicMarioAIAgent implements Agent {

    private int ticks;
    private Random R = null;
    private Environment environment;
    private BaseConocimiento baseConocimiento;
    private TrainFileManager trainFileManager;
    private EvaluadorPertenecia evaluadorPertenecia;
    private double[] estado;
    private double[] accion;
    private QLearning ql;
    private List<Tupla> mapa;
    private String[] acciones;
    private String[] estados;

    private static final int LEARNING_CYCLES = 5;
    private int ticksMarioNotMoving;

    public QBot() {
        super("QBot");
        reset();
        ticks = 0;
        mapa = new ArrayList<Tupla>();
        acciones = new String[]{"0", "1", "2", "3", "4"};
        estados = new String[]{"0", "1", "2", "3", "4", "5"};
        estado = new double[]{2};
        accion = new double[]{0};
        ql = new QLearning(0.1, 0.6, 0.3, estados, acciones, 6, 5);
        trainFileManager = new TrainFileManager("QBot.arff");
        evaluadorPertenecia = new EvaluadorPertenecia();
        ticksMarioNotMoving = 0;
        baseConocimiento = new BaseConocimiento();
        QAprendizaje();
    }

    public void reset() {
        // Dummy reset, of course, but meet formalities!
        R = new Random();
    }

    /**
     * Se utiliza el parámetro para asignar valor al atributo de la clase con el mismo nombre.
     *
     * @param environment Objeto de tipo Environment con toda la información sobre el estado del mundo.
     */
    public void integrateObservation(Environment environment) {
        this.environment = environment;
    }

    /**
     * Se analizan los alrededores del personaje, de forma que avance a la derecha constantemente y salte cuando
     * tenga enemigos, obstáculos y pozos delante.
     *
     * @return action
     */
    public boolean[] getAction() {
        double action;
        String[] linea = trainFileManager.getDatos(environment).split(", ");
        estado[0] = evaluadorPertenecia.getPertenecia(linea);
        ticksMarioNotMoving = (Integer.parseInt(linea[287]) == 0) ? (ticksMarioNotMoving + 1) : 0;
        if (ticksMarioNotMoving < 10) {
            action = ql.obtenerMejorAccion(estado)[0];
        } else {
            action = ql.obtenerAccionEGreedy(estado)[0];
        }

        //System.out.printf("%d; %d; %d; %d\n", ticks, environment.getEvaluationInfo().distancePassedCells, environment.getIntermediateReward(), environment.getMarioMode());
        ticks++;

        return baseConocimiento.actionDoubleToBoolean(action);
    }

    /**
     * Método que aprende la tabla Q.
     */
    private void QAprendizaje() {

        for (int i = 0; i < baseConocimiento.matrizConocimiento.size(); i++) {
            if (baseConocimiento.matrizConocimiento.get(i).getAccion()[0] != 5) {
                mapa.add(new Tupla(baseConocimiento.matrizConocimiento.get(i).getEstado(),
                        baseConocimiento.matrizConocimiento.get(i).getAccion(),
                        baseConocimiento.matrizConocimiento.get(i).getEstadoSiguiente(),
                        baseConocimiento.matrizConocimiento.get(i).getRefuerzo()));
            }
        }

        for (int i = 0; i < LEARNING_CYCLES; i++) {
            for (Tupla tupla : mapa) ql.actualizarTablaQ(tupla);
        }
        ql.mostrarTablaQ();
    }
}
