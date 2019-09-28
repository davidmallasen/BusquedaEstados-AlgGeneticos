package jungla_cristal.core;

import aima.core.search.framework.evalfunc.HeuristicFunction;

public class GarrafasHeuristicFunction implements HeuristicFunction {

    @Override
    public double h(Object state) {
        Garrafas estado = (Garrafas) state;
        if (estado.getState()[1] == estado.getCantidadObjetivo()) {
            return 0;
        } else if (estado.getState()[0] + estado.getState()[1] == estado.getCantidadObjetivo()
                || estado.getState()[1] -
                (estado.getCapacidadMax()[0] - estado.getState()[0]) == estado.getCantidadObjetivo()) {
            return 1;
        } else {
            return 2;
        }
    }
}
