package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.utils.EvaluadorPertenecia;
import ch.idsia.utils.TrainFileManager;

import java.util.Random;

public class TestBot extends BasicMarioAIAgent implements Agent {

    private int ticks;
    private Random R = null;
    private Environment environment;
    private float[] posicionAnterior;
    private TrainFileManager trainFileManager;
    private EvaluadorPertenecia evaluadorPertenecia;

    public TestBot() {
        super("TestBot");
        reset();
        trainFileManager = new TrainFileManager("TestBot.arff");
        evaluadorPertenecia = new EvaluadorPertenecia();
        ticks = 0;
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
        //trainFileManager.escrituraDatos(environment, action);
        String[] linea = trainFileManager.getDatos(environment).split(", ");
        int pertenencia = evaluadorPertenecia.getPertenecia(linea);
        //System.out.printf("%d; %d; %d; %d\n", ticks, environment.getEvaluationInfoAsInts()[1], environment.getIntermediateReward(), environment.getMarioMode());
        ticks++;
        if (pertenencia == 0){
            action[Mario.KEY_LEFT] = false;
            action[Mario.KEY_RIGHT] = true;
            action[Mario.KEY_DOWN] = false;
            action[Mario.KEY_JUMP] = true;
            action[Mario.KEY_SPEED] = false;
            action[Mario.KEY_UP] = false;
        } else if (pertenencia == 1 || pertenencia == 6){
            action[Mario.KEY_LEFT] = false;
            action[Mario.KEY_RIGHT] = true;
            action[Mario.KEY_DOWN] = false;
            action[Mario.KEY_JUMP] = false;
            action[Mario.KEY_SPEED] = false;
            action[Mario.KEY_UP] = false;
        } else if (pertenencia == 5){
            action[Mario.KEY_LEFT] = true;
            action[Mario.KEY_RIGHT] = false;
            action[Mario.KEY_DOWN] = false;
            action[Mario.KEY_JUMP] = false;
            action[Mario.KEY_SPEED] = false;
            action[Mario.KEY_UP] = false;
        }
        return action;
    }
}