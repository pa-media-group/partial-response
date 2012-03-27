package com.pressassociation.fire.partialresponse.fields.match;

/**
 * Utilities relating to Matcher and associated classes.
 *
 * @author Matt Nathan
 */
public class Matchers {
  /**
   * Return whether this matcher (the the best of our knowledge) should match all paths. This can be used to optimise
   * certain code paths that would do extra work to support matchers when it would not be needed.
   *
   * @param matcher The matcher to check
   * @return {@code true} if the matcher should match all paths.
   */
  public static boolean matchesAll(Matcher matcher) {
    return matcher instanceof AllMatcher ||
           matcher.patternString().equals("*");
  }

  private Matchers() {
  }
}
