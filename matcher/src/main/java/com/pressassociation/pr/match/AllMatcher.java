/*
 * Copyright (c) 2014 Press Association Limited
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 */

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
