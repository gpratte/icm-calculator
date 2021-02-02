package com.gilpratte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * An implementation of the Malmuth-Harville algorithm of the
 * Independent Chip Model.
 * <p>
 * See http://mathsociety.ph/matimyas/images/vol43/MarfilMatimyas.pdf
 * </p>
 */
public class Malmuth_Harville {

  /**
   * Figure out the order of the stacks that will be needed later for the summations.
   * For example if there are three stacks
   * <ul>
   *   <li>100</li>
   *   <li>75</li>
   *   <li>50</li>
   * </ul>
   * Then the order needed when applying the summations (the other method in the class)
   * <p>
   * Example: for chip stack 100: for 2nd place would be
   * <pre>
   *     100*75         100*50
   *   -----------  +  -----------
   *   225(225-75)     225(225-50)
   * </pre>
   * So the rankings would be [75],[50]
   * </p>
   * <p>
   * Example: for the chip stack 100: for 3nd place would be
   * <pre>
   *        100*75*50                   100*50*75
   *   ----------------------  +  ----------------------
   *   225(225-75)(225-75-50)     225(225-50)(225-50-75)
   * </pre>
   * So the rankings would be [75,50],[50,75]
   * </p>
   *
   * @param stackIds the chip stacks
   * @param depth    how deep to go (the place in the examples above)
   * @return a list of lists of rankings (see example above)
   */
  static List<List<String>> rankings(List<String> stackIds, int depth) {
    if (depth == 0) {
      return Collections.emptyList();
    }

    // Pull the first stack
    String stack = stackIds.get(0);

    // All the possible ways to arrange the other stacks
    List<String> allOtherStackIds = new ArrayList<>(stackIds.size() - 1);
    for (int i = 1; i < stackIds.size(); i++) {
      allOtherStackIds.add(stackIds.get(i));
    }
    List<List<String>> permutations = HeapsAlgorithm.generateRankings(allOtherStackIds);

    List<List<String>> paths = new LinkedList<>();
    // Keep the non-duplicates up to the depth
    for (List<String> perm : permutations) {
      // Trim the list to the depth
      List<String> trimmedList = perm;
      if (perm.size() > depth) {
        trimmedList = new ArrayList<>(depth);
        for (int i = 0; i < depth; i++) {
          trimmedList.add(perm.get(i));
        }
      }
      if (!paths.contains(trimmedList)) {
        paths.add(trimmedList);
      }
    }
    return paths;
  }

  /**
   * Calculate the percentage from the rankings.
   * Given
   * <ul>
   *   <li>100</li>
   *   <li>75</li>
   *   <li>50</li>
   * </ul>
   * <p>
   * Example: for 2nd place with rankings [75],[50]
   * <pre>
   *     100*75         100*50
   *   -----------  +  -----------
   *   225(225-75)     225(225-50)
   * </pre>
   * </p>
   * <p>
   * Example: for 3nd place with rankings [75,50],[50,75]
   * <pre>
   *        100*75*50                   100*50*75
   *   ----------------------  +  ----------------------
   *   225(225-75)(225-75-50)     225(225-50)(225-50-75)
   * </pre>
   * </p>
   *
   * @param stackId        the chip stacks
   * @param sumOfAllStacks the sum of all the chip stacks
   * @param paths          the rankings
   * @return the percentage for that place
   */
  static double calculateProbabilityForPlace(String stackId, int sumOfAllStacks, List<List<String>> paths) {
    if (paths.size() == 0) {
      return stackToInt(stackId) / (double) sumOfAllStacks;
    }
    double probability = 0.0;
    for (List<String> path : paths) {
      double pathProbability = stackToInt(stackId) / (double) sumOfAllStacks;
      int denominator = sumOfAllStacks;
      for (String place : path) {
        denominator -= stackToInt(place);
        pathProbability *= stackToInt(place) / (double) denominator;
      }
      probability += pathProbability;
    }
    return probability;
  }

  static int stackToInt(String stack) {
    return Integer.parseInt(stack.substring(0, stack.indexOf('-')));
  }
}
