package com.gilpratte;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * JUnit test for the ICM calculator. The expected number can be found using
 * any online ICM calculator. I used https://www.primedope.com/icm-deal-calculator/
 * because it also showed the percentage and not just the payouts.
 */
public class ICMCalculatorTest {

  @Test
  public void noCalculations() {
    List<Double> result = ICMCalculator.calculate(null, null);
    assertNotNull("result should not be null", result);
    assertEquals("result should be empty", 0, result.size());

    result = ICMCalculator.calculate(Collections.<Integer>emptyList(), Collections.<Integer>emptyList());
    assertNotNull("result should not be null", result);
    assertEquals("result should be empty", 0, result.size());
  }

  @Test
  public void onePayoutGetsTopPrize() {
    List<Double> result = ICMCalculator.calculate(Collections.singletonList(500), Collections.singletonList(100));
    assertNotNull("result should not be null", result);
    assertEquals("result should not have one value", 1, result.size());
    assertEquals("result should be first place", 500, result.get(0), 0.0);
  }

  /*
   * Stacks: 1500, 1200
   * Payouts: 100, 76
   * Finish distribution
   *    Player 1	55.6%	44.4%
   *    Player 2	44.4%	55.6%
   * Prize distribution:
   *    Player 1	89.33
   *    Player 2	86.67
   */
  @Test
  public void twoPayoutsTwoStacks() {
    List<Integer> monies = Arrays.asList(100, 76);
    List<Integer> stacks = Arrays.asList(1500, 1200);
    List<Double> expectedMonies = Arrays.asList(89.33, 86.67);
    List<Double> result = ICMCalculator.calculate(monies, stacks);
    assertResults(expectedMonies, result);
  }

  /*
   * Stacks: 1500, 1200, 975
   * Payouts: 100, 76
   * Finish distribution
   *    Player 1	40.8%	34.5%	24.7%
   *    Player 2	32.7%	34.3%	33.0%
   *    Player 3	26.5%	31.2%	42.3%
   * Prize distribution:
   *    Player 1	67.06
   *    Player 2	58.73
   *    Player 3	50.21
   */
  @Test
  public void twoPayoutsThreeStacks() {
    List<Integer> monies = Arrays.asList(100, 76);
    List<Integer> stacks = Arrays.asList(1500, 1200, 975);
    List<Double> expectedMonies = Arrays.asList(67.06, 58.73, 50.21);
    List<Double> result = ICMCalculator.calculate(monies, stacks);
    assertResults(expectedMonies, result);
  }

  /*
   * Stacks: 1500, 1500, 975
   * Payouts: 100, 76
   * Finish distribution
   *    Player 1	37.7%	35.1%	27.1%
   *    Player 2	37.7%	35.1%	27.1%
   *    Player 3	24.5%	29.7%	45.7%
   * Prize distribution:
   *    Player 1	64.44
   *    Player 2	64.44
   *    Player 3	47.12
   */
  @Test
  public void twoPayoutsThreeStacksTwoIdentical() {
    List<Integer> monies = Arrays.asList(100, 76);
    List<Integer> stacks = Arrays.asList(1500, 1500, 975);
    List<Double> expectedMonies = Arrays.asList(64.44, 64.44, 47.12);
    List<Double> result = ICMCalculator.calculate(monies, stacks);
    assertResults(expectedMonies, result);
  }

  /*
   * Stacks: 1500, 1200, 975, 350
   * Payouts: 100, 76
   * Finish distribution
   *    Player 1	37.3%	31.3%	22.9%	8.6%
   *    Player 2	29.8%	30.1%	27.4%	12.7%
   *    Player 3	24.2%	27.0%	31.2%	17.6%
   *    Player 4	8.7%	11.6%	18.5%	61.1%
   * Prize distribution:
   *    Player 1	61.05
   *    Player 2	52.68
   *    Player 3	44.73
   *    Player 4	17.54
   */
  @Test
  public void twoPayoutsFourStacks() {
    List<Integer> monies = Arrays.asList(100, 76);
    List<Integer> stacks = Arrays.asList(1500, 1200, 975, 350);
    List<Double> expectedMonies = Arrays.asList(61.05, 52.68, 44.73, 17.54);
    List<Double> result = ICMCalculator.calculate(monies, stacks);
    assertResults(expectedMonies, result);
  }

  /*
   * Stacks: 1500, 1200, 975, 350, 275
   * Payouts: 100, 76
   * Finish distribution
   *    Player 1	34.9%	29.2%	21.7%	11.1%	3.0%
   *    Player 2	27.9%	27.5%	24.6%	15.0%	5.0%
   *    Player 3	22.7%	24.5%	26.4%	19.0%	7.5%
   *    Player 4	8.1%	10.5%	15.1%	29.9%	36.5%
   *    Player 5	6.4%	8.3%	12.3%	25.0%	48.0%
   * Prize distribution:
   *    Player 1	57.08
   *    Player 2	48.82
   *    Player 3	41.28
   *    Player 4	16.08
   *    Player 5	12.74
   */
  @Test
  public void twoPayoutsFiveStacks() {
    List<Integer> monies = Arrays.asList(100, 76);
    List<Integer> stacks = Arrays.asList(1500, 1200, 975, 350, 275);
    List<Double> expectedMonies = Arrays.asList(57.08, 48.82, 41.28, 16.08, 12.74);
    List<Double> result = ICMCalculator.calculate(monies, stacks);
    assertResults(expectedMonies, result);
  }

  /*
   * Stacks: 150000, 98750, 45500
   * Payouts: 425, 280, 130
   * Finish distribution
   *    Player 1	51.0%	35.1%	13.9%
   *    Player 2	33.6%	41.0%	25.4%
   *    Player 3	15.5%	23.9%	60.6%
   * Prize distribution:
   *    Player 1	332.99
   *    Player 2	290.56
   *    Player 3	211.45
   */
  @Test
  public void threePayoutsThreeStacks() {
    List<Integer> monies = Arrays.asList(425, 280, 130);
    List<Integer> stacks = Arrays.asList(150000, 98750, 45500);
    List<Double> expectedMonies = Arrays.asList(332.99, 290.56, 211.45);
    List<Double> result = ICMCalculator.calculate(monies, stacks);
    assertResults(expectedMonies, result);
  }

  /*
   * Stacks: 150000, 98750, 45500, 13250
   * Payouts: 425, 280, 130, 75
   * Finish distribution
   *    Player 1	48.8%	33.7%	15.1%	2.4%
   *    Player 2	32.1%	37.6%	24.8%	5.5%
   *    Player 3	14.8%	21.8%	44.4%	19.1%
   *    Player 4	4.3%	6.9%	15.8%	73.0%   * Prize distribution:
   * Prize distribution:
   *    Player 1	323.2
   *    Player 2	278.12
   *    Player 3	195.79
   *    Player 4	112.89
   */
  @Test
  public void fourPayoutsFourStacks() {
    List<Integer> monies = Arrays.asList(425, 280, 130, 75);
    List<Integer> stacks = Arrays.asList(150000, 98750, 45500, 13250);
    List<Double> expectedMonies = Arrays.asList(323.2, 278.12, 195.79, 112.89);
    List<Double> result = ICMCalculator.calculate(monies, stacks);
    assertResults(expectedMonies, result);
  }

  private void assertResults(List<Double> expectedMonies, List<Double> result) {
    assertEquals("result should not have " + expectedMonies.size() + " values", expectedMonies.size(), result.size());

    for (int i = 0; i < expectedMonies.size(); i++) {
      double actual = result.get(i);
      assertEquals((i + 1) + " place should have " + expectedMonies.get(i), expectedMonies.get(i), actual, 0.0);
    }
  }
}