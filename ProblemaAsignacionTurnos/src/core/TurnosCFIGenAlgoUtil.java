package core;

import java.util.*;

import aima.core.search.framework.problem.GoalTest;
import aima.core.search.local.FitnessFunction;
import aima.core.search.local.Individual;
import demo.TurnosCFIDemo;

public class TurnosCFIGenAlgoUtil {

    private TurnosCFIGenAlgoUtil() {
        throw new IllegalStateException("Clase no instanciable");
    }

    public static FitnessFunction<Integer> getFitnessFunction() {
        return new TurnosCFIFitnessFunction();
    }

    public static GoalTest getGoalTest() {
        return new TurnosCFIGenAlgoGoalTest();
    }

    public static Individual<Integer> generateRandomIndividual() {
        Integer[] randInd = new Integer[TurnosCFIDemo.NUM_HUECOS];
        for (int i = 0; i < TurnosCFIDemo.NUM_HUECOS; ++i) {
            randInd[i] = 0;
        }

        int i = 0;
        while (i < TurnosCFIDemo.numTurnos) {
            int pos = new Random().nextInt(TurnosCFIDemo.NUM_HUECOS);
            if (randInd[pos] == 0) { //El turno esta vacio
                int prof = randomProfValido(pos);
                if (prof != 0) {
                    randInd[pos] = prof;
                    i++;
                }
            }
        }
        return new Individual<>(Arrays.asList(randInd));
    }

    public static Collection<Integer> getFiniteAlphabetForNumProfs(int numProfs) {
        Collection<Integer> fab = new ArrayList<>();

        for (int i = 0; i <= numProfs; i++) {
            fab.add(i);
        }

        return fab;
    }

    public static class TurnosCFIFitnessFunction implements FitnessFunction<Integer> {

        @Override
        public double apply(Individual<Integer> individual) {
            TurnosCFIHorario horario = new TurnosCFIHorario(individual);
            double numProfs = TurnosCFIDemo.profesores.size() - 1;

            // Numero de profesores que tienen asignado un numero de turnos
            // que esta en la media de turnos.
            double fitTurnosEquil = 0;
            double mediaTurnos = TurnosCFIDemo.numTurnos / numProfs;
            int[] numTurnos = horario.numTurnosProfesor();

            for (int i = 1; i < numTurnos.length; ++i) {
                if (numTurnos[i] == Math.ceil(mediaTurnos)
                        || numTurnos[i] == Math.floor(mediaTurnos)) {
                    fitTurnosEquil++;
                }
            }

            //Numero de turnos cuyo profesor asignado tiene a dicho turno en
            // su lista de preferencias.
            double fitPref = horario.numTurnosPref();

            double fitness = ((fitTurnosEquil / numProfs) * TurnosCFIDemo.PESO_TURNOS_EQUIL)
                    + ((fitPref / TurnosCFIDemo.numPreferencias) * TurnosCFIDemo.PESO_PREFERENCIAS);

            return Math.max(fitness, 0.1);
        }

    }

    public static class TurnosCFIGenAlgoGoalTest implements GoalTest {
        private final TurnosCFIGoalTest goalTest = new TurnosCFIGoalTest();

        @SuppressWarnings("unchecked")
        public boolean isGoalState(Object state) {
            return goalTest.isGoalState(new TurnosCFIHorario((Individual<Integer>) state));
        }
    }

    /**
     * Aumenta o disminuye el numero de turnos asignados hasta tener un
     * horario valido.
     *
     * @return horario validado.
     */
    public static TurnosCFIHorario validarHorario(TurnosCFIHorario horario) {
        int numTurnosOcupados = horario.numTurnosOcupados();

        while (numTurnosOcupados > TurnosCFIDemo.numTurnos) {
            //Buscamos un turno ocupado
            int pos = new Random().nextInt(TurnosCFIDemo.NUM_HUECOS);
            while (horario.getHorario()[pos] == 0) {
                pos++;
                pos = pos % TurnosCFIDemo.NUM_HUECOS;
            }
            //Liberamos el turno
            horario.setHorario(pos, 0);
            numTurnosOcupados--;
        }

        while (numTurnosOcupados < TurnosCFIDemo.numTurnos) {
            //Buscamos un turno vacio
            int pos = new Random().nextInt(TurnosCFIDemo.NUM_HUECOS);
            while (horario.getHorario()[pos] != 0) {
                pos++;
                pos = pos % TurnosCFIDemo.NUM_HUECOS;
            }
            // Se puede poner un profesor
            int prof = randomProfValido(pos);
            if (prof != 0) {
                horario.setHorario(pos, prof);
                numTurnosOcupados++;
            }
        }

        return horario;
    }

    /**
     * Devuelve el numero correspondiente a un profesor aleatorio que no
     * tenga a pos en su lista de restricciones.
     *
     * @param pos Posicion para la que queremos el profesor.
     * @return numero correspondiente a un profesor valido. 0 en caso de que
     * no se pueda poner ningun profesor.
     */
    public static int randomProfValido(int pos) {
        int prof = new Random().nextInt(TurnosCFIDemo.profesores.size() - 1) + 1;

        //pos+1 pues los turnos en los profesores van de 1 a 16 en vez de 0 a 15
        int iter = 0;   //Para evitar bucle infinito si no existe solucion
        while (!TurnosCFIDemo.profesores.get(prof).esTurnoValido(pos + 1)
                && iter < TurnosCFIDemo.profesores.size()) {
            prof++;
            iter++;
            if (prof == TurnosCFIDemo.profesores.size()) {
                prof = 1;
            }
        }
        if (iter < TurnosCFIDemo.profesores.size()) {
            return prof;
        } else {
            return 0;
        }
    }
}
