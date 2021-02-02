package com.gilpratte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Based on Heap's algorithm https://en.wikipedia.org/wiki/Heap%27s_algorithm.
 * <pre>
 *   For example if there are two inputs, A and B.
 *   2! (2 factorial) is 2.
 *   So A,B or B,A.
 *   For example if there are three inputs, A and B and C.
 *   3! (3 factorial) is 6.
 *   A,B,C or A,C,B or B,A,C or B,C,A or C,A,B or C,B,A.
 * </pre>
 */
public class HeapsAlgorithm {

  /**
   * Generate all the different permutations
   *
   * @param stackIds the chip stacks
   * @return all the permutations of the stacks
   */
  static List<List<String>> generateRankings(List<String> stackIds) {
    List<List<String>> rankings = new LinkedList<>();
    // Clone the stacks list
    generateRankingsRecursive(stackIds.size(), new ArrayList<>(stackIds), rankings);
    return rankings;
  }

  private static void generateRankingsRecursive(int n, List<String> elements, List<List<String>> rankings) {
    if (n == 1) {
      rankings.add(new ArrayList<>(elements));
    } else {
      for (int i = 0; i < n - 1; i++) {
        generateRankingsRecursive(n - 1, elements, rankings);
        if (n % 2 == 0) {
          Collections.swap(elements, i, n - 1);
        } else {
          Collections.swap(elements, 0, n - 1);
        }
      }
      generateRankingsRecursive(n - 1, elements, rankings);
    }
  }
}
