package jungla_cristal.core;

import aima.core.search.framework.problem.GoalTest;

public class GarrafasGoalTest implements GoalTest {

    private int cantidadObjetivo;

    public GarrafasGoalTest() {
        this.cantidadObjetivo = 4;
    }

    public GarrafasGoalTest(int capacidad) {
        this.cantidadObjetivo = capacidad;
    }

    @Override
    public boolean isGoalState(Object state) {
        Garrafas estado = (Garrafas) state;
        return estado.getState()[1] == cantidadObjetivo;
    }

    public int getCantidadObjetivo() {
        return cantidadObjetivo;
    }
}
