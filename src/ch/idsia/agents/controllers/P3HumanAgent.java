package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.utils.EvaluadorPertenecia;
import ch.idsia.utils.TrainFileManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public final class P3HumanAgent extends KeyAdapter implements Agent {

    private boolean[] action = null;
    private String Name = "P3HumanAgent";
    private int ticks;
    private Environment environment;
    private TrainFileManager trainFileManager;
    private EvaluadorPertenecia evaluadorPertenecia;
    private boolean escribe = false;
    private boolean ticPrevioEscribe = false;

    public P3HumanAgent() {
        this.reset();
        trainFileManager = new TrainFileManager("P3HumanAgent.arff");
        evaluadorPertenecia = new EvaluadorPertenecia();
        ticks = 0;
    }

    @Override
    public String getName() {
        return Name;
    }

    @Override
    public void setName(String name) {
        Name = name;
    }

    /**
     * Dado que para este agente no hay acción realizada automáticamente, en este método solo se hace lo necesario para
     * la escritura del archivo de datos de entrenamiento y se aumenta el contador de ticks.
     *
     * @return action
     */
    @Override
    public boolean[] getAction() {
        if (escribe) {
            trainFileManager.escrituraDatos(environment, action);
        }
        String[] linea = trainFileManager.getDatos(environment).split(", ");
        evaluadorPertenecia.getPertenecia(linea);
        //System.out.printf("%d; %d; %d; %d\n", ticks, environment.getEvaluationInfo().distancePassedCells, environment.getIntermediateReward(), environment.getMarioMode());
        ticks++;
        return action;
    }

    /**
     * Se utiliza el parámetro para asignar valor al atributo de la clase con el mismo nombre.
     *
     * @param environment Objeto de tipo Environment con toda la información sobre el estado del mundo.
     */
    @Override
    public void integrateObservation(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void giveIntermediateReward(float intermediateReward) {
    }

    /**
     * Se crea el vector que almacena las acciones a realizar de Mario.
     */
    @Override
    public void reset() {
        action = new boolean[Environment.numberOfKeys];
    }

    @Override
    public void setObservationDetails(final int rfWidth, final int rfHeight, final int egoRow, final int egoCol) {
    }

    /**
     * Detección de las teclas presionadas por el usuario.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        toggleKey(e.getKeyCode(), true);
    }

    /**
     * Detección de liberación de tecla por el ususario.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        toggleKey(e.getKeyCode(), false);
    }

    /**
     * Se almacena en el vector Acciones los comandos del usuario a ejecutar por Mario.
     */
    private void toggleKey(int keyCode, boolean isPressed) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                action[Mario.KEY_LEFT] = isPressed;
                break;
            case KeyEvent.VK_RIGHT:
                action[Mario.KEY_RIGHT] = isPressed;
                break;
            case KeyEvent.VK_DOWN:
                action[Mario.KEY_DOWN] = isPressed;
                break;
            case KeyEvent.VK_UP:
                action[Mario.KEY_UP] = isPressed;
                break;

            case KeyEvent.VK_S:
                action[Mario.KEY_JUMP] = isPressed;
                break;
            case KeyEvent.VK_A:
                action[Mario.KEY_SPEED] = isPressed;
                break;
            case KeyEvent.VK_P:
                escribe = true;
                System.out.println("ACTIVADA GRABACIÓN - PULSAR O PARA DESACTIVAR.");
                break;
            case KeyEvent.VK_O:
                escribe = true;
                System.out.println("DESACTIVADA GRABACIÓN.");
                break;
        }
    }

}
