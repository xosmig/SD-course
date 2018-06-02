package ru.spbau.mit.roguelike.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Util for common random methods
 */
public final class RandomUtils {
    private static final Random random = new Random();

    public static void setSeed(long seed) {
        random.setSeed(seed);
    }

    public static boolean getBoolean() {
        return random.nextBoolean();
    }

    public static boolean getBoolean(double p) {
        return random.nextDouble() < p;
    }

    public static int getInt(int left, int right) {
        if (right == left) {
            return right;
        }
        return random.nextInt(right - left) + left;
    }

    public static double getDouble(double left, double right) {
        return random.nextDouble() * (right - left) + left;
    }

    public static Point getPoint(int width, int height) {
        return Point.of(getInt(1, width), getInt(1, height));
    }

    public static <T> void shuffle(List<T> list) {
        Collections.shuffle(list, random);
    }

    public static <T> T chooseRandom(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    public static <T> T chooseRandom(List<T> list, List<Double> probabilities) {
        double roll = random.nextDouble();
        double sum = 0.;
        for (int i = 0; i < list.size() - 1 && i < probabilities.size(); i++) {
            sum += probabilities.get(i);
            if (roll < sum) {
                return list.get(i);
            }
        }
        return chooseRandom(list.subList(probabilities.size() - 1, list.size()));
    }

    public static List<Integer> asSumWithoutZeros(int source, int numberOfTerms) {
        List<Integer> result = asSum(source - numberOfTerms, numberOfTerms);
        for (int i = 0; i < result.size(); i++) {
            result.set(i, result.get(i) + 1);
        }
        return result;
    }

    public static List<Integer> asSum(int source, int numberOfTerms) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < numberOfTerms - 1; i++) {
            int term = random.nextInt(source + 1);
            result.add(term);
            source -= term;
        }
        result.add(source);
        shuffle(result);
        return result;
    }

    public static List<Integer> asWeightedSum(int source, List<Integer> weights) {
        List<Integer> weightsOrder = new ArrayList<>();
        for (int i = 0; i < weights.size(); i++) {
            weightsOrder.add(i);
        }
        shuffle(weightsOrder);
        List<Integer> result = new ArrayList<>(weights.size());
        for (int i = 0; i < weights.size(); i++) {
            result.add(0);
        }
        for (int i = 0; i < weightsOrder.size() - 1; i++) {
            int id = weightsOrder.get(i);
            if (source / weights.get(id) > 0) {
                int term = random.nextInt(source / weights.get(id) + 1);
                result.set(id, term);
                source -= term * weights.get(id);
            }
        }
        result.set(weightsOrder.get(weightsOrder.size() - 1),
                source / weights.get(weightsOrder.get(weightsOrder.size() - 1)));
        return result;
    }
}
