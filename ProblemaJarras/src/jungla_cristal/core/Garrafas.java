package jungla_cristal.core;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;

public class Garrafas {
    public static final Action LLENA_G0 = new DynamicAction("LlenaG0");
    public static final Action LLENA_G1 = new DynamicAction("LlenaG1");
    public static final Action VACIA_G0 = new DynamicAction("VaciaG0");
    public static final Action VACIA_G1 = new DynamicAction("VaciaG1");
    public static final Action VIERTE_G0_A_G1 = new DynamicAction("VierteG0aG1");
    public static final Action VIERTE_G1_A_G0 = new DynamicAction("VierteG1aG0");

    private int[] state;

    // Por defecto 3-5
    private int[] capacidadMax = {3, 5};

    public Garrafas() {
        state = new int[]{0, 0};
    }

    public Garrafas(int[] s) {
        this.state = new int[]{s[0], s[1]};
    }

    public Garrafas(int[] s, int[] c) {
        this(s);
        this.capacidadMax[0] = c[0];
        this.capacidadMax[1] = c[1];
    }

    public Garrafas(Garrafas copyGarrafas) {
        this(copyGarrafas.getState());
    }

    public int[] getState() {
        return state;
    }

    public void llenaGarrafa0() {
        state[0] = capacidadMax[0];
    }

    public void llenaGarrafa1() {
        state[1] = capacidadMax[1];
    }

    public void vaciaGarrafa0() {
        state[0] = 0;
    }

    public void vaciaGarrafa1() {
        state[1] = 0;
    }

    public void vierteG0aG1() {
        int v = Math.min(state[0], capacidadMax[1] - state[1]);
        state[0] -= v;
        state[1] += v;
    }

    public void vierteG1aG0() {
        int v = Math.min(state[1], capacidadMax[0] - state[0]);
        state[0] += v;
        state[1] -= v;
    }

    public boolean canApplyAction(Action action) {
        boolean retVal = true;
        if (action.equals(LLENA_G0))
            retVal = state[0] < capacidadMax[0];
        else if (action.equals(LLENA_G1))
            retVal = state[1] < capacidadMax[1];
        else if (action.equals(VACIA_G0))
            retVal = state[0] > 0;
        else if (action.equals(VACIA_G1))
            retVal = state[1] > 0;
        else if (action.equals(VIERTE_G0_A_G1))
            retVal = (state[0] > 0) && (state[1] < capacidadMax[1]);
        else if (action.equals(VIERTE_G1_A_G0))
            retVal = (state[1] > 0) && (state[0] < capacidadMax[0]);
        return retVal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (this.getClass() != o.getClass())) {
            return false;
        }
        Garrafas oGarrafas = (Garrafas) o;

        return  !((this.state[0] != oGarrafas.state[0]) || (this.state[1] != oGarrafas.state[1]));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + this.state[0];
        result = 37 * result + this.state[1];
        return result;
    }

    @Override
    public String toString() {
        return "Garrafa 0: " + this.state[0] + "\nGarrafa 1: " + this.state[1];
    }

    public int[] getCapacidadMax() {
        return capacidadMax;
    }
}
