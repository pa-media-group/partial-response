package com.pressassociation.fire.partialresponse.fields.match;

/**
 * Matcher implementation that matches all paths.
 *
 * @author Matt Nathan
 */
class AllMatcher extends Matcher {
  /**
   * Single instance for this type.
   */
  static final Matcher INSTANCE = new AllMatcher();

  @Override
  public boolean matches(Leaf path) {
    return true;
  }

  @Override
  protected String patternString() {
    return "*";
  }
}
