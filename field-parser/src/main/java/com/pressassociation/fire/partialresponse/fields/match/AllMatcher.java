package com.pressassociation.fire.partialresponse.fields.match;

/**
 * Matcher implementation that matches all paths
 *
 * @author Matt Nathan
 */
class AllMatcher extends Matcher {
  @Override
  public boolean matches(CharSequence path) {
    return true;
  }

  @Override
  protected String patternString() {
    return "*";
  }
}
