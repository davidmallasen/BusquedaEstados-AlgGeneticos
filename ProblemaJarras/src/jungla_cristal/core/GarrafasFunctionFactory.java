package jungla_cristal.core;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.ResultFunction;

public class GarrafasFunctionFactory {
    private static ActionsFunction actionsFunction = null;
    private static ResultFunction resultFunction = null;

    private GarrafasFunctionFactory() {
        throw new IllegalStateException("No se puede instanciar esta clase.");
    }

    public static ActionsFunction getActionsFunction() {
        if (actionsFunction == null) {
            actionsFunction = new EPActionsFunction();
        }
        return actionsFunction;
    }

    public static ResultFunction getResultFunction() {
        if (resultFunction == null) {
            resultFunction = new EPResultFunction();
        }
        return resultFunction;
    }

    private static class EPActionsFunction implements ActionsFunction {
        public Set<Action> actions(Object state) {
            Garrafas estado = (Garrafas) state;

            Set<Action> actions = new LinkedHashSet<>();

            if (estado.canApplyAction(Garrafas.LLENA_G0)) {
                actions.add(Garrafas.LLENA_G0);
            }
            if (estado.canApplyAction(Garrafas.LLENA_G1)) {
                actions.add(Garrafas.LLENA_G1);
            }
            if (estado.canApplyAction(Garrafas.VACIA_G0)) {
                actions.add(Garrafas.VACIA_G0);
            }
            if (estado.canApplyAction(Garrafas.VACIA_G1)) {
                actions.add(Garrafas.VACIA_G1);
            }
            if (estado.canApplyAction(Garrafas.VIERTE_G0_A_G1)) {
                actions.add(Garrafas.VIERTE_G0_A_G1);
            }
            if (estado.canApplyAction(Garrafas.VIERTE_G1_A_G0)) {
                actions.add(Garrafas.VIERTE_G1_A_G0);
            }

            return actions;
        }
    }

    private static class EPResultFunction implements ResultFunction {
        public Object result(Object s, Action a) {
            Garrafas estado = (Garrafas) s;
            if (Garrafas.LLENA_G0.equals(a)
                    && estado.canApplyAction(Garrafas.LLENA_G0)) {
                Garrafas newEstado = new Garrafas(estado);
                newEstado.llenaGarrafa0();
                return newEstado;
            } else if (Garrafas.LLENA_G1.equals(a)
                    && estado.canApplyAction(Garrafas.LLENA_G1)) {
                Garrafas newEstado = new Garrafas(estado);
                newEstado.llenaGarrafa1();
                return newEstado;
            } else if (Garrafas.VACIA_G0.equals(a)
                    && estado.canApplyAction(Garrafas.VACIA_G0)) {
                Garrafas newEstado = new Garrafas(estado);
                newEstado.vaciaGarrafa0();
                return newEstado;
            } else if (Garrafas.VACIA_G1.equals(a)
                    && estado.canApplyAction(Garrafas.VACIA_G1)) {
                Garrafas newEstado = new Garrafas(estado);
                newEstado.vaciaGarrafa1();
                return newEstado;
            } else if (Garrafas.VIERTE_G0_A_G1.equals(a)
                    && estado.canApplyAction(Garrafas.VIERTE_G0_A_G1)) {
                Garrafas newEstado = new Garrafas(estado);
                newEstado.vierteG0aG1();
                return newEstado;
            } else if (Garrafas.VIERTE_G1_A_G0.equals(a)
                    && estado.canApplyAction(Garrafas.VIERTE_G1_A_G0)) {
                Garrafas newEstado = new Garrafas(estado);
                newEstado.vierteG1aG0();
                return newEstado;
            }

            // The Action is not understood or is a NoOp
            // the result will be the current state.
            return s;
        }
    }
}
