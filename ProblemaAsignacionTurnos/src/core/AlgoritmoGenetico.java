package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.local.Individual;
import aima.core.util.datastructure.Pair;
import demo.TurnosCFIDemo;

public class AlgoritmoGenetico extends GeneticAlgorithm<Integer> {

    public AlgoritmoGenetico(int individualLength,
                             Collection<Integer> finiteAlphabet, double mutationProbability) {
        super(individualLength, finiteAlphabet, mutationProbability);
    }

    /**
     * Primitive operation which is responsible for creating the next generation.
     */
    @SuppressWarnings("unused")
    @Override
    protected List<Individual<Integer>> nextGeneration(
            List<Individual<Integer>> population,
            FitnessFunction<Integer> fitnessFn) {
        if (TurnosCFIDemo.NUM_HIJOS_CRUCE == 1) {
            return super.nextGeneration(population, fitnessFn);
        } else {
            List<Individual<Integer>> newPopulation = new ArrayList<>(population.size());
            for (int i = 0; i < (population.size() / 2); i++) {
                Individual<Integer> x = randomSelection(population, fitnessFn);
                Individual<Integer> y = randomSelection(population, fitnessFn);
                ArrayList<Individual<Integer>> childs = reproduce2(x, y);

                if (random.nextDouble() <= mutationProbability) {
                    childs.set(0, mutate(childs.get(0)));
                }
                if (random.nextDouble() <= mutationProbability) {
                    childs.set(1, mutate(childs.get(1)));
                }

                // Anyadimos los hijos a la nueva generacion
                if (TurnosCFIDemo.CRUCE_DESTRUCTIVO) {
                    newPopulation.add(childs.get(0));
                    newPopulation.add(childs.get(1));
                } else { // Cogemos los dos mejores
                    ArrayList<Pair<Double, Individual<Integer>>> familia = new ArrayList<>();
                    familia.add(new Pair<>(fitnessFn.apply(x), x));
                    familia.add(new Pair<>(fitnessFn.apply(y), y));
                    familia.add(new Pair<>(fitnessFn.apply(childs.get(0)), childs.get(0)));
                    familia.add(new Pair<>(fitnessFn.apply(childs.get(1)), childs.get(1)));

                    familia.sort(comp);
                    newPopulation.add(familia.get(0).getSecond());
                    newPopulation.add(familia.get(1).getSecond());
                }
            }
            notifyProgressTracers(getIterations(), population);
            return newPopulation;
        }
    }

    private static Comparator<Pair<Double, Individual<Integer>>> comp =
            (x, y) -> {
                if (x.getFirst() > y.getFirst()) {
                    return -1;
                } else if (x.getFirst().equals(y.getFirst())) {
                    return 0;
                } else {
                    return 1;
                }
            };

    /**
     * function REPRODUCE2(x, y) returns two individuals inputs: x, y, parent
     * individuals
     */
    @SuppressWarnings("unused")
    private ArrayList<Individual<Integer>> reproduce2(Individual<Integer> x,
                                                      Individual<Integer> y) {
        ArrayList<Individual<Integer>> hijos = new ArrayList<>();

        // Comprobamos si tenemos que cruzar
        if (random.nextFloat() <= TurnosCFIDemo.PROB_CRUCE) {
            // Elegmios el punto de cruce
            int c = randomOffset(individualLength);

            List<Integer> childRepresentation1 = new ArrayList<>();
            List<Integer> childRepresentation2 = new ArrayList<>();

            if (TurnosCFIDemo.NUM_PUNTOS_CORTE == 1) {
                //Formamos el primer hijo
                childRepresentation1.addAll(x.getRepresentation().subList(0, c));
                childRepresentation1.addAll(y.getRepresentation().subList(c, individualLength));

                //Formamos el segundo hijo
                childRepresentation2.addAll(y.getRepresentation().subList(0, c));
                childRepresentation2.addAll(x.getRepresentation().subList(c, individualLength));
            } else {
                int c2 = randomOffset(individualLength);
                if (c2 < c) {
                    int aux = c2;
                    c2 = c;
                    c = aux;
                }
                //Formamos el primer hijo
                childRepresentation1.addAll(x.getRepresentation().subList(0, c));
                childRepresentation1.addAll(y.getRepresentation().subList(c, c2));
                childRepresentation1.addAll(x.getRepresentation().subList(c2, individualLength));

                //Formamos el segundo hijo
                childRepresentation2.addAll(y.getRepresentation().subList(0, c));
                childRepresentation2.addAll(x.getRepresentation().subList(c, c2));
                childRepresentation2.addAll(y.getRepresentation().subList(c2, individualLength));
            }

            // Los hacemos validos
            TurnosCFIHorario hijo1 = new TurnosCFIHorario(new Individual<>(childRepresentation1));
            hijo1 = TurnosCFIGenAlgoUtil.validarHorario(hijo1);

            TurnosCFIHorario hijo2 = new TurnosCFIHorario(new Individual<>(childRepresentation2));
            hijo2 = TurnosCFIGenAlgoUtil.validarHorario(hijo2);

            // Devolvemos los hijos
            hijos.add(new Individual<>(Arrays.asList(hijo1.getHorario())));
            hijos.add(new Individual<>(Arrays.asList(hijo2.getHorario())));
        } else { // No cruzamos, devolvemos los padres
            hijos.add(x);
            hijos.add(y);
        }

        return hijos;
    }

    @SuppressWarnings("unused")
    @Override
    protected Individual<Integer> reproduce(Individual<Integer> x, Individual<Integer> y) {
        // Comprobamos si tenemos que cruzar
        if (random.nextFloat() < TurnosCFIDemo.PROB_CRUCE) {
            // Elegmios el punto de cruce
            int c = randomOffset(individualLength);
            List<Integer> childRepresentation = new ArrayList<>();

            if (TurnosCFIDemo.NUM_PUNTOS_CORTE == 1) {
                childRepresentation.addAll(x.getRepresentation().subList(0, c));
                childRepresentation.addAll(y.getRepresentation().subList(c, individualLength));
            } else {
                int c2 = randomOffset(individualLength);
                if (c2 < c) {
                    int aux = c2;
                    c2 = c;
                    c = aux;
                }
                childRepresentation.addAll(x.getRepresentation().subList(0, c));
                childRepresentation.addAll(y.getRepresentation().subList(c, c2));
                childRepresentation.addAll(x.getRepresentation().subList(c2, individualLength));
            }

            // Lo hacemos valido
            TurnosCFIHorario hijo = new TurnosCFIHorario(new Individual<>(childRepresentation));
            hijo = TurnosCFIGenAlgoUtil.validarHorario(hijo);

            return new Individual<>(Arrays.asList(hijo.getHorario()));
        } else {
            return x;
        }

    }

    @Override
    protected Individual<Integer> mutate(Individual<Integer> child) {
        TurnosCFIHorario mutado = new TurnosCFIHorario(child);
        Integer[] horario = mutado.getHorario();
        for (int i = 0; i < horario.length; ++i) {
            if (random.nextFloat() < TurnosCFIDemo.PROB_MUTACION) {
                mutado.setHorario(i, TurnosCFIGenAlgoUtil.randomProfValido(i));
            }
        }
        // Ya mutado, lo hago valido
        mutado = TurnosCFIGenAlgoUtil.validarHorario(mutado);

        return new Individual<>(Arrays.asList(mutado.getHorario()));
    }
}
