package com.pressassociation.pr.match;

import com.google.common.base.Function;

import com.pressassociation.pr.ast.AstNode;
import com.pressassociation.pr.ast.Wildcard;

import static com.google.common.base.Preconditions.checkNotNull;

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
  public boolean matchesParent(Leaf node) {
    return true;
  }

  @Override
  protected String patternString() {
    return "*";
  }

  @Override
  public boolean matchesAll() {
    return true;
  }

  @Override
  public Matcher transform(Function<? super String, String> nameTransformer) {
    checkNotNull(nameTransformer);
    return this; // can't transform a wildcard to anything else
  }

  @Override
  protected AstNode getAstNode() {
    return Wildcard.getSharedInstance();
  }
}
