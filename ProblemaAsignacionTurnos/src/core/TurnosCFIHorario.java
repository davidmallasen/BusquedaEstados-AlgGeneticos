package core;

import aima.core.search.local.Individual;
import demo.TurnosCFIDemo;

public class TurnosCFIHorario {

    /**
     * horario[i] contiene el numero de profesor asignado al turno i. Si
     * horario[i] = 0, entones ese turno esta vacio.
     */
    private Integer[] horario;

    public TurnosCFIHorario(Individual<Integer> individual) {
        this.horario = individual.getRepresentation().toArray(new Integer[0]);
    }

    /**
     * @return maximo numero de turnos que tiene asignado un profesor.
     */
    public int numMaxTurnosProf() {
        int[] numTurnos = numTurnosProfesor();

        int max = 0;
        // Empezamos en 1 para no tener en cuenta los turnos vacios
        for (int i = 1; i < numTurnos.length; ++i) {
            max = Math.max(max, numTurnos[i]);
        }

        return max;
    }

    /**
     * Devuelve el numero de turnos que tiene asignado cada profesor. Hay que
     * tener en cuenta que en array[0] estara el numero de turnos libres.
     *
     * @return array[i] = numero de turnos que tiene asignado el profesor i
     */
    public int[] numTurnosProfesor() {
        int[] numTurnos = new int[TurnosCFIDemo.profesores.size()];

        for (Integer i : horario) {
            numTurnos[i]++;
        }

        return numTurnos;
    }

    /**
     * @return numero de turnos cuyo profesor asignado tiene a dicho turno en su
     * lista de preferencias.
     */
    public int numTurnosPref() {
        int num = 0;
        for (int i = 0; i < horario.length; ++i) {
            if (horario[i] != 0 &&
                    TurnosCFIDemo.profesores.get(horario[i]).esTurnoPreferido(i)) {
                num++;
            }
        }
        return num;
    }

    /**
     * @return numero de turnos que estan ocupados.
     */
    public int numTurnosOcupados() {
        int numTurnosOcupados = 0;
        for (Integer i : horario) {
            if (i != 0) {
                numTurnosOcupados++;
            }
        }
        return numTurnosOcupados;
    }

    public Integer[] getHorario() {
        return horario;
    }

    public void setHorario(int pos, int num) {
        this.horario[pos] = num;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer i : horario) {
            sb.append(i);
        }
        sb.append('\n');
        return sb.toString();
    }

}
