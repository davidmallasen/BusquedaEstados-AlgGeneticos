package demo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import core.AlgoritmoGenetico;
import core.Profesor;
import core.TurnosCFIGenAlgoUtil;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.local.FitnessFunction;
import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.local.Individual;

public class TurnosCFIDemo {

    // Parametros algoritmo genetico
    private static final int NUM_INDIVIDUOS_POBLACION = 50;
    public static final double PROB_MUTACION = 0.1;
    public static final double PROB_CRUCE = 0.7;

    /**
     * Valores: 1 o 2.
     * Si NUM_HIJOS_CRUCE = 2, NUM_INDIVIDUOS_POBLACION debe ser par.
     */
    public static final int NUM_HIJOS_CRUCE = 2;

    /**
     * Si true, los hijos siempre reemplazaran a los padres. Si false, se
     * comprobara a ver si el hijo mejora el fitness del padre.
     */
    public static final boolean CRUCE_DESTRUCTIVO = false;

    /**
     * Numero de puntos de corte al cruzar dos individuos. Valores: 1 o 2.
     */
    public static final int NUM_PUNTOS_CORTE = 1;

    /**
     * Peso correspondiente a tener los turnos equilibrados entre los
     * profesores. Ambos pesos deberian sumar 100.
     */
    public static final int PESO_TURNOS_EQUIL = 50;

    /**
     * Peso correspondiente a cumplir las preferencias de los profesores. Ambos
     * pesos deberian sumar 100.
     */
    public static final int PESO_PREFERENCIAS = 50;

    // Parametros pruebas

    /**
     * Numero de veces a ejecutar la prueba.
     */
    private static int numPruebas = 100;

    /**
     * Tiempo maximo en milisegundos al ejecutar una prueba.
     */
    private static long tiempoMilisegundos = 1000L;

    // Datos de entrada

    /**
     * Numero de turnos a los que hay que asignar profesor.
     */
    public static int numTurnos;

    /**
     * Minimo entre el numero de turnos a rellenar y el numero de turnos que son
     * preferidos por algun profesor. Afina mas a la hora de calcular el fitness
     * de un individuo y comprobar si se esta en un estado objetivo.
     */
    public static int numPreferencias;

    /**
     * Lista con los profesores disponibles. El primer profesor estara en la
     * posicion 1. El "profesor 0" esta reservado para representar un hueco.
     * <p>
     * numProfs + 1 = TurnosCFIDemo.profesores.size()
     */
    public static List<Profesor> profesores;

    // Parametros varios

    /**
     * Numero de posiciones en el horario.
     */
    public static final int NUM_HUECOS = 16;

    // -------------------------------------------

    public static void main(String[] args) {
        System.out.println("configuracionConvocatoria1.txt");
        leerDatosEntrada("configuracionConvocatoria1.txt");
        numPruebas = 50;
        tiempoMilisegundos = 1000L;
        ejecutaPrueba();

        System.out.println("configuracionConvocatoria2.txt");
        leerDatosEntrada("configuracionConvocatoria2.txt");
        numPruebas = 100;
        tiempoMilisegundos = 1000L;
        ejecutaPrueba();

        numPruebas = 50;
        tiempoMilisegundos = 5000L;
        ejecutaPrueba();

        System.out.println("configuracionConvocatoria3.txt");
        leerDatosEntrada("configuracionConvocatoria3.txt");
        numPruebas = 100;
        tiempoMilisegundos = 1000L;
        ejecutaPrueba();

        numPruebas = 50;
        tiempoMilisegundos = 5000L;
        ejecutaPrueba();
    }

    /**
     * Ejecuta una prueba con los distintos parametros indicados.
     */
    private static void ejecutaPrueba() {
        for (double i = 1; i > 0.65; i -= 0.10) {
            for (int j = 1; j <= 2; j++) {
                PROB_CRUCE = i;
                NUM_HIJOS_CRUCE = j;
                CRUCE_DESTRUCTIVO = false;
                turnosCFIGenerticAlgorithmStat(numPruebas, tiempoMilisegundos);
                CRUCE_DESTRUCTIVO = true;
                turnosCFIGenerticAlgorithmStat(numPruebas, tiempoMilisegundos);
            }
        }
    }

    /**
     * Ejecuta el algoritmo genetico durante 1 segundo. Se muestran por consola
     * los resultados de la ejecucion.
     */
    public static void turnosCFIGeneticAlgorithmSearch() {
        System.out.println("\nTurnosCFI GeneticAlgorithm  -->");
        try {
            FitnessFunction<Integer> fitnessFunction = TurnosCFIGenAlgoUtil.getFitnessFunction();
            GoalTest goalTest = TurnosCFIGenAlgoUtil.getGoalTest();
            // Generate an initial population
            Set<Individual<Integer>> population = new HashSet<>();
            for (int i = 0; i < NUM_INDIVIDUOS_POBLACION; i++) {
                population.add(TurnosCFIGenAlgoUtil.generateRandomIndividual());
            }

            GeneticAlgorithm<Integer> ga = new AlgoritmoGenetico(NUM_HUECOS,
                    TurnosCFIGenAlgoUtil.getFiniteAlphabetForNumProfs(profesores.size() - 1),
                    PROB_MUTACION);

            // Run for a set amount of time
            Individual<Integer> bestIndividual = ga.geneticAlgorithm(population,
                    fitnessFunction, goalTest, 1000L);

            System.out.println("Max Time (1 second) Best Individual=\n"
                    + bestIndividual.getRepresentation().toString());
            System.out.println("Fitness         = "
                    + fitnessFunction.apply(bestIndividual));
            System.out.println("Is Goal         = "
                    + goalTest.isGoalState(bestIndividual));
            System.out.println("Population Size = " + ga.getPopulationSize());
            System.out.println("Iterations      = " + ga.getIterations());
            System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ejecuta el algoritmo numPruebas veces con maxTiempoMilis como limite de
     * tiempo. Muestra por consola estadisticas de todas las ejecuciones.
     *
     * @param numPruebas     Numero de veces a ejecutar el algoritmo.
     * @param maxTiempoMilis Limite de tiempo para cada prueba.
     */
    public static void turnosCFIGenerticAlgorithmStat(int numPruebas, long maxTiempoMilis) {
        double mejorFitness = Double.NEGATIVE_INFINITY;
        int numIndividuosOptimos = 0;
        double[] fitness = new double[numPruebas];

        try {
            FitnessFunction<Integer> fitnessFunction = TurnosCFIGenAlgoUtil.getFitnessFunction();
            GoalTest goalTest = TurnosCFIGenAlgoUtil.getGoalTest();

            for (int k = 0; k < numPruebas; ++k) {
                // Generate an initial population
                Set<Individual<Integer>> population = new HashSet<>();
                for (int i = 0; i < NUM_INDIVIDUOS_POBLACION; i++) {
                    population.add(TurnosCFIGenAlgoUtil.generateRandomIndividual());
                }

                GeneticAlgorithm<Integer> ga = new AlgoritmoGenetico(NUM_HUECOS,
                        TurnosCFIGenAlgoUtil.getFiniteAlphabetForNumProfs(profesores.size() - 1),
                        PROB_MUTACION);

                // Run for a set amount of time
                Individual<Integer> bestIndividual = ga.geneticAlgorithm(
                        population, fitnessFunction, goalTest, maxTiempoMilis);

                fitness[k] = fitnessFunction.apply(bestIndividual);
                if (fitness[k] > mejorFitness) {
                    mejorFitness = fitness[k];
                }

                if (goalTest.isGoalState(bestIndividual))
                    numIndividuosOptimos++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        double mediaFitness = mediaValores(fitness);
        double varianzaFitness = varianzaValores(fitness, mediaFitness);
        double desviacionTipicaFitness = Math.sqrt(varianzaFitness);


        System.out.println("\nTurnosCFI GeneticAlgorithm Statistics -->");
        System.out.println(TurnosCFIDemo.numPruebas + ", " + tiempoMilisegundos + ", " + PROB_CRUCE
                + ", " + NUM_HIJOS_CRUCE + ", " + CRUCE_DESTRUCTIVO);

        System.out.println(/*"Mejor fitness: " + */mejorFitness);
        System.out.println(/*"Numero de veces que se ha alcanzado un individuo "
                + "optimo: " + */numIndividuosOptimos);
        System.out.println(/*"Media fitness: " + */mediaFitness);
        System.out.println(/*"Varianza fitness: " + */varianzaFitness);
        System.out.println(
                /*"Desviacion tipica fitness: " + */desviacionTipicaFitness);
    }

    private static double mediaValores(double[] input) {
        double sum = 0;
        for (double d : input) {
            sum += d;
        }
        return sum / input.length;
    }

    private static double varianzaValores(double[] input, double media) {
        double varianza = 0;
        for (double d : input) {
            varianza += Math.pow(d - media, 2);
        }
        varianza /= input.length;
        return varianza;
    }

    /**
     * Encargada de leer los datos desde un archivo.
     *
     * @param nombreArchivo Nombre del archivo del que leemos los datos.
     */
    private static void leerDatosEntrada(String nombreArchivo) {
        Scanner in = null;
        try {
            // Abre el fichero
            in = new Scanner(new FileReader(nombreArchivo));
            // Leemos el numero de turnos
            numTurnos = in.nextInt();
            // Leemos el salto de linea
            in.nextLine();

            // Procedemos a la lectura de profesores
            profesores = new ArrayList<>();

            String nomProf = in.nextLine();
            String[] listProf = nomProf.split(", ");

            // No hay profesor asignado
            profesores.add(new Profesor(null, null));

            for (int i = 0; i < listProf.length; i++) {
                profesores.add(new Profesor(null, null));
            }
            // Restricciones
            for (int i = 1; i < profesores.size(); i++) {
                in.next(); // Lee el nombre y los dos puntos
                String r = in.nextLine(); // Si despues vienen numeros o no
                if (r.length() >= 2) {
                    String[] res = (r.trim()).split(",");
                    int[] valores = new int[res.length];
                    for (int j = 0; j < res.length; j++) {
                        valores[j] = Integer.parseInt(res[j]);
                    }
                    profesores.get(i).setRestr(valores);
                } else {
                    profesores.get(i).setRestr(new int[0]);
                }
            }
            // Preferencias
            boolean[] esPreferido = new boolean[NUM_HUECOS + 1];

            for (int i = 1; i < profesores.size(); i++) {
                in.next(); // Lee el nombre y los dos puntos
                String r = in.nextLine();
                if (r.length() >= 1) {
                    String[] pref = (r.trim()).split(",");
                    int[] valores = new int[pref.length];
                    for (int j = 0; j < pref.length; j++) {
                        valores[j] = Integer.parseInt(pref[j]);
                        esPreferido[valores[j]] = true;
                    }
                    profesores.get(i).setPref(valores);
                } else {
                    profesores.get(i).setPref(new int[0]);
                }
            }

            // Calculamos cuantos turnos distintos son preferidos por algun
            // profesor
            numPreferencias = 0;
            for (boolean b : esPreferido) {
                if (b) {
                    numPreferencias++;
                }
            }
            numPreferencias = Math.min(numPreferencias, numTurnos);

        } catch (FileNotFoundException e) {
            System.out.println("Error abriendo el fichero " + nombreArchivo);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
