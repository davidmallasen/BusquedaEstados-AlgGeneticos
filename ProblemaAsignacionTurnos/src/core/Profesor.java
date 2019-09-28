package core;

public class Profesor {

    /**
     * Restricciones asociadas al profesor.
     */
    private int[] restr;

    /**
     * Preferencias del profesor.
     */
    private int[] pref;

    public Profesor(int[] restr, int[] pref) {
        this.restr = restr;
        this.pref = pref;
    }

    /**
     * Comprueba si un turno esta en la lista de preferencias del profesor
     *
     * @param turno turno a comprobar.
     * @return True si el turno esta en la lista de preferencias del profesor.
     * False si no.
     */
    public boolean esTurnoPreferido(int turno) {
        for (int i : pref)
            if (i == turno) return true;
        return false;
    }

    /**
     * Comprueba si un turno esta en la lista de preferencias del profesor
     *
     * @param turno turno a comprobar.
     * @return True si el turno esta en la lista de preferencias del profesor.
     * False si no.
     */
    public boolean esTurnoValido(int turno) {
        for (int i : restr)
            if (i == turno) return false;
        return true;
    }

    public void setRestr(int[] restr) {
        this.restr = restr;
    }

    public void setPref(int[] pref) {
        this.pref = pref;
    }
}
