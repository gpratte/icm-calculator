package com.gilpratte;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Calculates each player's percents of the amounts using the Independent Chip Model (ICM).
 * The percents are then applied to the payouts to determine how much of the prize pot each
 * player gets.
 * <p>
 * See https://www.tournamentterminator.com/tournament-strategy/icm/independent-chip-model-example-calculation/ for an explaination of what ICM is.
 * </p>
 */
public class ICMCalculator {

  /**
   * Entry point to calculate the ICM
   *
   * @param amounts the amount to be split up (normally the payouts)
   * @param stacks  the chip stack of each remaining player
   * @return the amount for each player (in the same order as stacks)
   */
  public static List<Double> calculate(List<Integer> amounts, List<Integer> stacks) {
    if (amounts == null || amounts.size() == 0 || stacks == null || stacks.size() == 0) {
      return Collections.emptyList();
    }

    // Sort the input
    Collections.sort(amounts);
    Collections.reverse(amounts);

    // Since there may be multiple stacks with the same number of chips
    // we need to be able to uniquely identify them. Add a unique
    // identifier to the end of the stack.
    List<String> stackIds = new ArrayList<>(stacks.size());
    stacks.forEach(stack -> stackIds.add(stack + "-" + UUID.randomUUID().toString()));

    // This is the data structure that will hold the probability of each player
    // coming in first, second, ... .
    Map<String, List<Double>> stackProbabilities = new LinkedHashMap<>(stacks.size());
    for (String stackId : stackIds) {
      List<Double> probabilities = new ArrayList<>(stacks.size());
      stackProbabilities.put(stackId, probabilities);
    }

    // First calculate the probabilities
    calculateProbabilities(stackIds, stackProbabilities);
    // Apply the probabilities to the amounts
    return calculateAmounts(amounts, stackProbabilities);
  }

  // Figure out what percent each player gets by comparing chip stacks
  static void calculateProbabilities(List<String> stackIds, Map<String, List<Double>> stackProbabilities) {
    int sumAllStacks = sumAllStacks(stackIds);

    int numStacks = stackIds.size();
    for (int stackIndex = 0; stackIndex < numStacks; stackIndex++) {
      // The stackIds gets rotated so always take the first one
      String stackId = stackIds.get(0);
      // Go through the places. If there are 2 stacks then calculate
      // first and second place. If there are 3 stacks then calculate
      // first, second and third place. Et cetera.
      for (int place = 0; place < numStacks; place++) {
        // The paths are all the permutations of the stacks for a given place (2nd, 3rd, ...)
        // which is needed for input for the Malmuth-Harville equation.
        List<List<String>> paths = Malmuth_Harville.rankings(stackIds, place);
        // Calculate the probability of coming in the place
        double probablityForPlace = Malmuth_Harville.calculateProbabilityForPlace(stackId, sumAllStacks, paths);
        // Store the probability
        stackProbabilities.get(stackId).add(probablityForPlace);
      }
      // Rotate the stack to process the probabilities for the next stack
      Collections.rotate(stackIds, 1);
    }
  }

  // Sum up each player's percent of each amount
  static List<Double> calculateAmounts(List<Integer> amounts, Map<String, List<Double>> stackProbabilities) {
    List<Double> icmAmounts = new LinkedList<>();

    for (String stack : stackProbabilities.keySet()) {
      double sum = 0.0;
      List<Double> probabilities = stackProbabilities.get(stack);
      for (int i = 0; i < amounts.size(); i++) {
        Integer amount = amounts.get(i);
        Double probability = probabilities.get(i);
        sum += amount * probability;
      }
      // Round to 2 decimal places
      BigDecimal bd = new BigDecimal(Double.toString(sum));
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      icmAmounts.add(bd.doubleValue());
    }
    return icmAmounts;
  }

  // Add up all the stacks
  static Integer sumAllStacks(List<String> stacks) {
    return stacks.stream()
      .mapToInt(ICMCalculator::stackToInt)
      .sum();
  }

  // Convert the stack with the unique identifier back to the number of chips
  static int stackToInt(String stack) {
    return Integer.parseInt(stack.substring(0, stack.indexOf('-')));
  }
}
