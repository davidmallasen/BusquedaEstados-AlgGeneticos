package jungla_cristal.demo;

import java.util.List;
import java.util.Properties;

import jungla_cristal.core.Garrafas;
import jungla_cristal.core.GarrafasFunctionFactory;
import jungla_cristal.core.GarrafasGoalTest;
import jungla_cristal.core.GarrafasHeuristicFunction;
import aima.core.agent.Action;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.framework.qsearch.TreeSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.UniformCostSearch;

public class GarrafasDemo {

    /**
     * La garrafa de mayor tamanyo tiene que ser la segunda, ya que nuestro
     * programa devuelve cuando el resultado esta en la segunda, y si fuera la
     * menor podria darse el caso de que no entrara, cosa que generaria un bucle
     * infinito
     */
    private static final Garrafas CASO = new Garrafas(new int[]{0, 0}, new int[]{3, 7});

    private static final int CASO_OBJETIVO = 1;

    public static void main(String[] args) {
        System.out.println("----------------------------------------------");
        System.out.println("Jungla de Cristal con parametros:");
        System.out.println("Capacidad maxima de la primera garrafa: " + CASO.getCapacidadMax()[0]);
        System.out.println("Capacidad maxima de la segunda garrafa: " + CASO.getCapacidadMax()[1]);
        System.out.println("Cantidad objetivo: " + CASO_OBJETIVO);
        System.out.println("----------------------------------------------");

        executeDemoCases();
    }

    private static void executeDemoCases() {
        System.out.println("\nGarrafasDemo Anchura TS -->");
        garrafasDemoCase(new BreadthFirstSearch(new TreeSearch()));
        System.out.println("-------------------------------");

        System.out.println("\nGarrafasDemo Anchura GS -->");
        garrafasDemoCase(new BreadthFirstSearch(new GraphSearch()));

        System.out.println("\nGarrafasDemo Profundidad GS -->");
        garrafasDemoCase(new DepthFirstSearch(new GraphSearch()));

        System.out.println("\nGarrafasDemo Coste Uniforme TS -->");
        garrafasDemoCase(new UniformCostSearch(new GraphSearch()));

        System.out.println("\nGarrafasDemo Coste Uniforme GS -->");
        garrafasDemoCase(new UniformCostSearch(new GraphSearch()));

        System.out.println("\nGarrafasDemo Voraz GS -->");
        garrafasDemoCase(new GreedyBestFirstSearch(
                new GraphSearch(), new GarrafasHeuristicFunction()));

        System.out.println("\nGarrafasDemo A* TS -->");
        garrafasDemoCase(new AStarSearch(new TreeSearch(),
                new GarrafasHeuristicFunction()));
        System.out.println("-------------------------------");

        System.out.println("\nGarrafasDemo A* GS -->");
        garrafasDemoCase(new AStarSearch(new GraphSearch(),
                new GarrafasHeuristicFunction()));
    }

    private static void garrafasDemoCase(SearchForActions search) {
        try {
            Problem problem = new Problem(CASO,
                    GarrafasFunctionFactory.getActionsFunction(),
                    GarrafasFunctionFactory.getResultFunction(),
                    new GarrafasGoalTest(CASO_OBJETIVO));

            long timeStart;
            long timeEnd;
            timeStart = System.nanoTime();
            SearchAgent agent = new SearchAgent(problem, search);
            timeEnd = System.nanoTime();

            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
            System.out.println("Time: " + (timeEnd - timeStart) / 1000 + " microseconds");
            System.out.println("-------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printInstrumentation(Properties properties) {
        System.out.println("\nInfo:\n......");
        for (Object o : properties.keySet()) {
            String key = (String) o;
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }

    private static void printActions(List<Action> actions) {
        Garrafas garrafa = new Garrafas(CASO);
        for (Action action : actions) {
            System.out.println(action);
            if (action == Garrafas.LLENA_G0) {
                garrafa.llenaGarrafa0();
            } else if (action == Garrafas.LLENA_G1) {
                garrafa.llenaGarrafa1();
            } else if (action == Garrafas.VACIA_G0) {
                garrafa.vaciaGarrafa0();
            } else if (action == Garrafas.VACIA_G1) {
                garrafa.vaciaGarrafa1();
            } else if (action == Garrafas.VIERTE_G0_A_G1) {
                garrafa.vierteG0aG1();
            } else if (action == Garrafas.VIERTE_G1_A_G0) {
                garrafa.vierteG1aG0();
            }
            System.out.println("(" + garrafa.getState()[0] + "," + garrafa.getState()[1] + ")");
        }
    }
}
