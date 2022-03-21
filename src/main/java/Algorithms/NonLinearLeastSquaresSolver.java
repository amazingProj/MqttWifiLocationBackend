package Algorithms;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresFactory;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DiagonalMatrix;

//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;

/**
 * Solves a Trilateration problem with an instance of a
 * {@link LeastSquaresOptimizer}
 *
 * @author assaf
 *
 */
public class NonLinearLeastSquaresSolver {

    /*********************** Fields ************************/
    private final TrilaterationFunction function;
    private final LeastSquaresOptimizer leastSquaresOptimizer;

    private int MAX_NUMBER_OF_ITERATIONS = 1000;
    private int threads = 1;

    /*********************** Constructor ************************/

    public NonLinearLeastSquaresSolver(TrilaterationFunction function, LeastSquaresOptimizer leastSquaresOptimizer) {
        this.function = function;
        this.leastSquaresOptimizer = leastSquaresOptimizer;
    }

    /*********************** Getter and Setter ************************/

    public int getMAX_NUMBER_OF_ITERATIONS() {
        return MAX_NUMBER_OF_ITERATIONS;
    }

    public void setMAX_NUMBER_OF_ITERATIONS(int MAX_NUMBER_OF_ITERATIONS) {
        this.MAX_NUMBER_OF_ITERATIONS = MAX_NUMBER_OF_ITERATIONS;
    }

    public int getThreads() {
        return threads;
    }

    /**
     * setter of numberof threads
     *
     * @param threads - the number of threads
     */
    public void setThreads(int threads) {
        this.threads = threads;
    }

    /*********************** Functions ************************/

    public Optimum solve(double[] target, double[] weights, double[] initialPoint, boolean debugInfo) {
        if (debugInfo) {
            System.out.println("Max Number of Iterations : " + MAX_NUMBER_OF_ITERATIONS);
        }
        //int cores = Runtime.getRuntime().availableProcessors();
        //ExecutorService exec = threads == 1 ? null : Executors.newFixedThreadPool(threads);
        //System.out.println("Cores: " + cores + ", threads: " + threads);

        LeastSquaresProblem leastSquaresProblem = LeastSquaresFactory.create(
                // function to be optimized
                function,
                // target values at optimal point in least square equation
                // (x0+xi)^2 + (y0+yi)^2 + ri^2 = target[i]
                new ArrayRealVector(target, false), new ArrayRealVector(initialPoint, false), new DiagonalMatrix(weights), null, MAX_NUMBER_OF_ITERATIONS, MAX_NUMBER_OF_ITERATIONS);

        return leastSquaresOptimizer.optimize(leastSquaresProblem);
    }

    public Optimum solve(double[] target, double[] weights, double[] initialPoint) {
        return solve(target, weights, initialPoint, false);
    }

    public Optimum solve(boolean debugInfo) {
        int numberOfPositions = function.getPositions().length;
        int positionDimension = function.getPositions()[0].length;

        double[] initialPoint = new double[positionDimension];
        // initial point, use average of the vertices
        for (int i = 0; i < function.getPositions().length; i++) {
            double[] vertex = function.getPositions()[i];
            for (int j = 0; j < vertex.length; j++) {
                initialPoint[j] += vertex[j];
            }
        }
        for (int j = 0; j < initialPoint.length; j++) {
            initialPoint[j] /= numberOfPositions;
        }

        if (debugInfo) {
            StringBuilder output = new StringBuilder("initialPoint: ");
            for (int i = 0; i < initialPoint.length; i++) {
                output.append(initialPoint[i]).append(" ");
            }
            System.out.println(output);
        }

        double[] target = new double[numberOfPositions];
        double[] distances = function.getDistances();
        double[] weights = new double[target.length];
        for (int i = 0; i < target.length; i++) {
            target[i] = 0.0;
            weights[i] = inverseSquareLaw(distances[i]);
        }

        return solve(target, weights, initialPoint, debugInfo);
    }

    private double inverseSquareLaw(double distance) {
        return 1 / (distance * distance);
    }

    public Optimum solve() {
        return solve(false);
    }
}