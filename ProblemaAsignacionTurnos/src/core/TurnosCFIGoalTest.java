package core;

import aima.core.search.framework.problem.GoalTest;
import demo.TurnosCFIDemo;

public class TurnosCFIGoalTest implements GoalTest {

    @Override
    public boolean isGoalState(Object h) {
        TurnosCFIHorario horario = (TurnosCFIHorario) h;

        //Comprobamos que el profesor asignado a cada turno tiene a dicho
        // turno en su lista de preferencias, si es posible.
        if (horario.numTurnosPref() != TurnosCFIDemo.numPreferencias) {
            return false;
        }

        //Comprobamos que los turnos estan repartidos de forma equilibrada.
        int maxTurnos = horario.numMaxTurnosProf();
        double numProfs = TurnosCFIDemo.profesores.size() - 1;
        double mediaTurnos = TurnosCFIDemo.numTurnos / numProfs;

        return !(maxTurnos != Math.ceil(mediaTurnos) && maxTurnos != Math.floor(mediaTurnos));
    }

}
